import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;
/**
 * ECAioTruthTables puts all 8-bit possible inputs into all 256 8-bit ECA
 */
public class ECAioTruthTables extends JPanel {
    /**
     * all 0-255 ECA Wolfram codes
     */
    public int[][] ruleTruthTable = new int[256][8];
    /**
     * numbers between 0..65536 in binary as an array
     */
    public int[][] numasbin = new int[65536][16];
    /**
     * result of all 8 bit inputs into all 256 8 bit ECA
     */
    public int[][][] tables = new int[256][256][8];
    /**
     * Swing component to display this class
     */
    JFrame jFrame = new JFrame();
    /**
     * initializes some binary arrays, otherwise empty
     */
    public ECAioTruthTables() {
        for (int n = 0; n < 256; n++) {
            int temp = n;
            for (int power = 0; power < 8; power++) {
                ruleTruthTable[n][power] = temp % 2;
                temp = temp - (temp % 2);
                temp = temp / 2;
            }
        }
//        for (int n = 0; n < 65536; n++) {
//            int temp = n;
//            for (int power = 0; power < 16; power++) {
//                numasbin[n][power] = temp % 2;
//                temp = temp - (temp % 2);
//                temp = temp / 2;
//            }
//        }
//        generateTables(8);
//        jFrame.setSize(1024,1024);
//        jFrame.add(this);
//        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        jFrame.setVisible(true);
        //generateTables(8);
    }
    /**
     * This function puts all 256 8 bit inputs into all 256 8 bit truth tables. The mask variables
     * and loops are because of the diagonal 'shadows'; the output depends not just on the 8 bits of input, but also
     * on the 8 bits to the left and right of the main input columns. The 3 bit mask specifices whether those left center and right
     * 8 bits are zeros or copies of the input. Each bitmask is a layer in the output
     *
     * @param length length of input
     * @return set of inputs to ECA in layers defined by the 3 bit bitmask applied to the 24 bits of input
     */
    public int[][][] generateTables(int length) {
        int lp = (int) Math.pow(2, length);
        int[][] field = new int[length + 1][5 * (length)];
        for (int n = 0; n < 256; n++) {
            for (int input = 0; input < lp; input++) {
                for (int mask = 0; mask < 8; mask++) {
                    field = new int[length + 1][5 * (length)];
                    for (int maskBit = 0; maskBit < 3; maskBit++) {
                        if (mask / (int) Math.pow(2, maskBit) % 2 == 1) {
                            for (int column = 0; column < length; column++) {
                                field[0][(1 + maskBit) * length + column] = numasbin[input][column];
                            }
                        }
                    }
                    for (int row = 1; row <= length; row++) {
                        for (int column = 1; column < 5 * length - 1; column++) {
                            field[row][column] = field[row - 1][column - 1] + 2 * field[row - 1][column] + 4 * field[row - 1][column + 1];
                            field[row][column] = ruleTruthTable[n][field[row][column]];
                        }
                    }
                    int out = 0;
                    for (int column = 0; column < length; column++) {
                        out += (int) Math.pow(2, column) * field[length][2 * length + column];
                    }
                    tables[n][input][mask] = out;
                }
            }
        }
        String outstring = "";
        int[] used = new int[256];
        for (int mask = 2; mask < 3; mask++) {
            System.out.println("mask " + mask);
            for (int n = 0; n < 256; n++) {
                for (int input = 0; input < lp; input++) {
                    outstring += tables[n][input][mask] + " ";
                    used[tables[n][input][mask]]++;
                }
                System.out.println(outstring);
                outstring = "";
            }
            System.out.println();
        }
        System.out.println("used: " + Arrays.toString(used));

        return tables;
    }
    /**
     * This function checks every ECA against the bit-place layers against another kind of cellular automaton,
     * generated through multiplying the neighborhood together instead of summing it and applying a truth table. The truth
     * table is a user-provided multiplication table.
     * <p>
     * There is a 5 bit bitmask that tells the algorithm whether to lay down that iteration of the sequence and bitmask
     * <p>
     * Then it lays down a sequence 0..lengthSequence in order 5 times, then applies another bitmask, a negative sign bitmask
     * to that sequence. This bitmask is used as input
     *
     * @param multTable      multiplication table to be used in analysis
     * @param lengthSequence length of the sequence and bitmask to be used
     * @param numFactors     size of neighborhood to multiply together
     */
    public void ecaInvolvedInMultiplicationAutomata(int[][] multTable, int lengthSequence, int numFactors) {
        int mtPlaces = 3;
        //ECA layers as negative's bitmask input
        int[][][][] ecaLayers = new int[mtPlaces][256][lengthSequence * 3 + 1][9 * lengthSequence];
        //multiplication automtata layer with a negative's signs bitmask
        int[][] hypercomplexLayer = new int[lengthSequence * 3 + 1][9 * lengthSequence];
        //number of possible negative's bitmasks are possible for the sequence length
        int possSeq = (int) Math.pow(2, lengthSequence);
        //calculation loop
        for (int n = 0; n < 256; n++) {
            System.out.println("n: " + n);
            //all possible negative's bitmasks of length lengthSequence
            for (int input = 0; input < possSeq; input++) {
                //if this bitmask spot is zero
                for (int mask = 0; mask < 32; mask++) {
                    for (int repeat = 0; repeat < 5; repeat++) {
                        ecaLayers = new int[mtPlaces][256][lengthSequence * 3 + 1][9 * lengthSequence];
                        //apply the sequence as a positive
                        for (int layer = 0; layer < mtPlaces - 1; layer++) {
                            for (int index = 0; index < lengthSequence; index++) {
                                ecaLayers[layer][n][0][repeat * (lengthSequence + 4) + index] = (index / (int) Math.pow(2, layer)) % 2;
                            }
                        }
                        for (int index = 0; index < lengthSequence; index++) {
                            hypercomplexLayer[0][repeat * (lengthSequence + 4) + index] = index;
                        }
                        if ((mask / (int) Math.pow(2, repeat)) % 2 == 0) {
                        } else {
                            //apply the negative bitmask
                            for (int index = 0; index < lengthSequence; index++) {
                                ecaLayers[mtPlaces - 1][n][0][repeat * (lengthSequence + 4) + index] = input / (int) Math.pow(2, index) % 2;
                                if (input / (int) Math.pow(2, index) % 2 == 1) {
                                    hypercomplexLayer[0][repeat * (lengthSequence + 4) + index] += 8;
                                    hypercomplexLayer[0][repeat * (lengthSequence + 4) + index] = hypercomplexLayer[0][repeat * (lengthSequence + 4) + index] % 8;
                                }
                            }
                        }
                        //calculate the whole thing row by row column by column
                        for (int row = 1; row <= lengthSequence * 3; row++) {
                            for (int column = 1; column < 9 * lengthSequence - 2; column++) {
                                hypercomplexLayer[row][column] = hypercomplexLayer[row - 1][column - numFactors / 2];
                                for (int mult = 1; mult < numFactors; mult++) {
                                    hypercomplexLayer[row][column] = multTable[hypercomplexLayer[row][column]][hypercomplexLayer[row - 1][column - numFactors / 2 + mult]];
                                }
                            }
                        }
//                        System.out.println("Initial Layers");
//                        for (int layer = 0; layer < mtPlaces; layer++){
//                            System.out.println(Arrays.toString(ecaLayers[layer][n][0]));
//                        }
                        //calculate the whole thing row by row column by column
                        for (int layer = 0; layer < mtPlaces; layer++) {
                            for (int row = 1; row <= lengthSequence * 3; row++) {
                                for (int column = row; column < 9 * lengthSequence - 2; column++) {
                                    ecaLayers[layer][n][row][column] = ecaLayers[layer][n][row - 1][column - 1] + 2 * ecaLayers[layer][n][row - 1][column] + 4 * ecaLayers[layer][n][row - 1][column + 1];
                                    ecaLayers[layer][n][row][column] = ruleTruthTable[n][ecaLayers[layer][n][row][column]];
                                }
                            }
                        }
                    }
                }
            }
        }
        //results totaled as integers at row increment sequence length
        //equivalent to the partial match check, with rows
        //incrementing by lengthSequence
        int[][][] compressedECAResults = new int[mtPlaces][3][9];
        //analysis loops
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                for (int row = 0; row < 3; row++) {
                    for (int spot = 0; spot < 9; spot++) {
                        for (int column = 0; column < lengthSequence; column++) {
                            compressedECAResults[layer][row][spot] += (int) Math.pow(2, column) * ecaLayers[layer][n][row * lengthSequence][spot * lengthSequence + column];
                        }
                    }
                }
            }
        }
        //ECA partial matches, 0 = false, 1 = true;
        int[][] partialRowMatch = new int[256][mtPlaces];
        //ECA exact matches, 0 = false, 1 = true;
        int[][] exactRowsMatch = new int[256][mtPlaces];
        //for every 0-255 ECA, see whether it is an exact or partial match to the multiplication automata
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                boolean exact = true;
                boolean partial = true;
                //exact match loop checks every single row
                for (int row = 0; row < 3 * lengthSequence + 1; row++) {
                    for (int column = row; column < 9 * lengthSequence - row - 1; column++) {
                        if (ecaLayers[layer][n][row][column] != (hypercomplexLayer[row][column] / (int) Math.pow(2, layer) % 2)) {
                            exact = false;
                        }
                    }
                }
                //partial match loop checks rows incremented by lengthSequence
                for (int row = 0; row < 3; row++) {
                    for (int column = row; column < lengthSequence; column++) {
                        if (ecaLayers[layer][n][row * lengthSequence][column] != (hypercomplexLayer[row][column] / (int) Math.pow(2, layer) % 2)) {
                            partial = false;
                        }
                    }
                }
                if (exact) {
                    exactRowsMatch[n][mtPlaces] = 1;
                }
                if (partial) {
                    partialRowMatch[n][mtPlaces] = 1;
                }
            }
        }
    }
    /**
     * This function checks every ECA against the bit-place layers against another kind of cellular automaton,
     * generated through multiplying the neighborhood together instead of summing it and applying a truth table. The truth
     * table is a user-provided multiplication table.
     * <p>
     * There is a 5 bit bitmask that tells the algorithm whether to lay down that iteration of the sequence and bitmask
     * <p>
     * Then it lays down a sequence 0..lengthSequence in order 5 times, then applies another bitmask, a negative sign bitmask
     * to that sequence. This bitmask is used as input
     *
     * @param multTable      multiplication table to be used in analysis
     * @param numBits        number of bits to consider, number of layers calculated and compared
     * @param lengthSequence length of the sequence and bitmask to be used
     * @param numFactors     size of neighborhood to multiply together
     */
    public void ecaInvolvedInMultiplicationAutomataSmallerVersion(int[][] multTable, int numBits, int lengthSequence, int numFactors) {
        int mtPlaces = numBits;
        //ECA layers as negative's bitmask input
        int[][][][] ecaLayers = new int[mtPlaces][256][lengthSequence + 1][3 * lengthSequence];
        //multiplication automtata layer with a negative's signs bitmask
        int[][] hypercomplexLayer = new int[lengthSequence + 1][3 * lengthSequence];
        //number of possible negative's bitmasks are possible for the sequence length
        int possSeq = (int) Math.pow(2, lengthSequence * 3);
        //ECA partial matches, 0 = false, 1 = true;
        int[][] partialRowMatch = new int[256][mtPlaces];
        //ECA exact matches, 0 = false, 1 = true;
        int[][] exactRowsMatch = new int[256][mtPlaces];
        int[] initSequence = new int[lengthSequence * 3];
        boolean[][][] partials = new boolean[256][mtPlaces][mtPlaces];
        boolean[][][] exacts = new boolean[256][mtPlaces][mtPlaces];
        Random rand = new Random();
        int random = rand.nextInt(0, possSeq);
        for (int repeat = 0; repeat < 3; repeat++) {
            for (int spot = 0; spot < lengthSequence; spot++) {
                initSequence[lengthSequence * repeat + spot] = spot;
            }
        }
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                for (int llayer = 0; llayer < mtPlaces; llayer++) {
                    partials[n][layer][llayer] = true;
                    exacts[n][layer][llayer] = true;
                }
            }
        }
        System.out.println(Arrays.toString(initSequence));
        //calculation loop
        for (int n = 0; n < 256; n++) {
            //all possible negative's sign bitmasks of length lengthSequence
            for (int input = 0; input < possSeq; input++) {
                hypercomplexLayer = new int[lengthSequence + 1][3 * lengthSequence];
                for (int index = 0; index < lengthSequence * 3; index++) {
                    hypercomplexLayer[0][index] = initSequence[index];
                    if (input / (int) Math.pow(2, index) % 2 == 1) {
                        hypercomplexLayer[0][index] = (hypercomplexLayer[0][lengthSequence] + 4) % 8;
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int index = 0; index < 3 * lengthSequence; index++) {
                        ecaLayers[layer][n][0][index] = (hypercomplexLayer[0][index] / (int) Math.pow(2, layer)) % 2;
                    }
                }
                for (int row = 1; row <= lengthSequence; row++) {
                    for (int column = row; column < 3 * lengthSequence - row; column++) {
                        hypercomplexLayer[row][column] = hypercomplexLayer[row - 1][column - numFactors / 2];
                        for (int mult = 1; mult < numFactors; mult++) {
                            hypercomplexLayer[row][column] = multTable[hypercomplexLayer[row][column]][hypercomplexLayer[row - 1][column - numFactors / 2 + mult]];
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int row = 1; row <= lengthSequence; row++) {
                        for (int column = row; column < 3 * lengthSequence - row; column++) {
                            ecaLayers[layer][n][row][column] = ecaLayers[layer][n][row - 1][column - 1] + 2 * ecaLayers[layer][n][row - 1][column] + 4 * ecaLayers[layer][n][row - 1][column + 1];
                            ecaLayers[layer][n][row][column] = ruleTruthTable[n][ecaLayers[layer][n][row][column]];
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int llayer = 0; llayer < mtPlaces; llayer++) {
                        for (int column = lengthSequence; column < lengthSequence * 2; column++) {
                            if (ecaLayers[layer][n][lengthSequence][column] != (hypercomplexLayer[lengthSequence][column] / (int) Math.pow(2, llayer) % 2)) {
                                partials[n][layer][llayer] = false;
                            }
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int llayer = 0; llayer < mtPlaces; llayer++) {
                        //partial match loop checks rows incremented by lengthSequence
                        for (int row = 1; row < lengthSequence + 1; row++) {
                            for (int column = row; column < 3 * lengthSequence - row; column++) {
                                if (ecaLayers[layer][n][row][column] != (hypercomplexLayer[row][column] / (int) Math.pow(2, llayer) % 2)) {
                                    exacts[n][layer][llayer] = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        int totExact = 0;
        int totPartial = 0;
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                for (int llayer = 0; llayer < mtPlaces; llayer++) {
                    if (exacts[n][layer][llayer]) {
                        totExact++;
                        System.out.println("exact n: " + n + " layers (" + layer + "," + llayer + ")");
                    }
                    if (partials[n][layer][llayer]) {
                        totPartial++;
                        System.out.println("partial n: " + n + " layers (" + layer + "," + llayer + ")");
                    }
                }
            }
        }
        System.out.println("totExact: " + totExact);
        System.out.println("totPartial: " + totPartial);
    }
    /**
     * This function checks every ECA against the bit-place layers against another kind of cellular automaton,
     * generated through multiplying the neighborhood together instead of summing it and applying a truth table. The truth
     * table is a user-provided multiplication table.
     * <p>
     * There is a 5 bit bitmask that tells the algorithm whether to lay down that iteration of the sequence and bitmask
     * <p>
     * Then it lays down a sequence 0..lengthSequence in order 5 times, then applies another bitmask, a negative sign bitmask
     * to that sequence. This bitmask is used as input
     *
     * @param multTable      multiplication table to be used in analysis
     * @param numBits        number of bits to consider, number of layers to calculate and compare
     * @param lengthSequence length of the sequence and bitmask to be used
     * @param numFactors     size of neighborhood to multiply together
     */
    public void ecaInvolvedInMultiplicationAutomataSmallerVersionRandom(int[][] multTable, int numBits, int lengthSequence, int numFactors) {

        int mtPlaces = numBits;
        //ECA layers as negative's bitmask input
        int[][][][] ecaLayers = new int[mtPlaces][256][lengthSequence + 1][3 * lengthSequence];
        //multiplication automtata layer with a negative's signs bitmask
        int[][] hypercomplexLayer = new int[lengthSequence + 1][3 * lengthSequence];
        //number of possible negative's bitmasks are possible for the sequence length
        int possSeq = (int) Math.pow(2, lengthSequence * 3);
        //ECA partial matches, 0 = false, 1 = true;
        int[][] partialRowMatch = new int[256][mtPlaces];
        //ECA exact matches, 0 = false, 1 = true;
        int[][] exactRowsMatch = new int[256][mtPlaces];
        int[] initSequence = new int[lengthSequence * 3];
        boolean[][][] partials = new boolean[256][mtPlaces][mtPlaces];
        boolean[][][] exacts = new boolean[256][mtPlaces][mtPlaces];
        Random rand = new Random();
        int random = rand.nextInt(0, possSeq);
        for (int repeat = 0; repeat < 3; repeat++) {
            for (int spot = 0; spot < lengthSequence; spot++) {
                initSequence[lengthSequence * repeat + spot] = spot;
            }
        }
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                for (int llayer = 0; llayer < mtPlaces; llayer++) {
                    partials[n][layer][llayer] = true;
                    exacts[n][layer][llayer] = true;
                }
            }
        }
        //System.out.println(Arrays.toString(initSequence));
        //calculation loop
        for (int n = 0; n < 256; n++) {
            //all possible negative's sign bitmasks of length lengthSequence
            inputLoop:
            for (int input = 0; input < 1000; input++) {
                hypercomplexLayer = new int[lengthSequence + 1][3 * lengthSequence];
                for (int index = 0; index < lengthSequence * 3; index++) {

                        //hypercomplexLayer[0][index] = 4*rand.nextInt(0,2)+(index % lengthSequence) % 8;
                        hypercomplexLayer[0][index] = rand.nextInt(0,(int)Math.pow(2,mtPlaces));
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int index = 0; index < 3 * lengthSequence; index++) {
                        ecaLayers[layer][n][0][index] = (hypercomplexLayer[0][index] / (int) Math.pow(2, layer)) % 2;
                    }
                }
                for (int row = 1; row <= lengthSequence; row++) {
                    for (int column = row; column < 3 * lengthSequence - row; column++) {
                        hypercomplexLayer[row][column] = hypercomplexLayer[row - 1][column - numFactors / 2];
                        for (int mult = 1; mult < numFactors; mult++) {
                            hypercomplexLayer[row][column] = multTable[hypercomplexLayer[row][column]][hypercomplexLayer[row - 1][column - numFactors / 2 + mult]];
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int row = 1; row <= lengthSequence; row++) {
                        for (int column = row; column < 3 * lengthSequence - row; column++) {
                            ecaLayers[layer][n][row][column] = ecaLayers[layer][n][row - 1][column - 1] + 2 * ecaLayers[layer][n][row - 1][column] + 4 * ecaLayers[layer][n][row - 1][column + 1];
                            ecaLayers[layer][n][row][column] = ruleTruthTable[n][ecaLayers[layer][n][row][column]];
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int llayer = 0; llayer < mtPlaces; llayer++) {
                        for (int column = lengthSequence; column < lengthSequence * 2; column++) {
                            if (ecaLayers[layer][n][lengthSequence][column] != (hypercomplexLayer[lengthSequence][column] / (int) Math.pow(2, llayer) % 2)) {
                                partials[n][layer][llayer] = false;
                            }
                        }
                    }
                }
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int llayer = 0; llayer < mtPlaces; llayer++) {
                        //partial match loop checks rows incremented by lengthSequence
                        for (int row = 1; row < lengthSequence + 1; row++) {
                            for (int column = row; column < 3 * lengthSequence - row; column++) {
                                if (ecaLayers[layer][n][row][column] != (hypercomplexLayer[row][column] / (int) Math.pow(2, llayer) % 2)) {
                                    exacts[n][layer][llayer] = false;
                                }
                            }
                        }
                    }
                }
                boolean exit = true;
                for (int layer = 0; layer < mtPlaces; layer++) {
                    for (int llayer = 0; llayer < mtPlaces; llayer++) {
                        if (exacts[n][layer][llayer] || partials[n][layer][llayer]){
                            exit = false;
                        }
                    }
                }
                if (exit){
                    break inputLoop;
                }

            }
        }
        int totExact = 0;
        int totPartial = 0;
        for (int n = 0; n < 256; n++) {
            for (int layer = 0; layer < mtPlaces; layer++) {
                for (int llayer = 0; llayer < mtPlaces; llayer++) {
                    if (exacts[n][layer][llayer]) {
                        totExact++;
                        System.out.println("exact n: " + n + " layers (" + layer + "," + llayer + ")");
                    }
                    if (partials[n][layer][llayer]) {
                        totPartial++;
                        System.out.println("partial n: " + n + " layers (" + layer + "," + llayer + ")");
                    }
                }
            }
        }
        System.out.println("totExact: " + totExact);
        System.out.println("totPartial: " + totPartial);

    }
    /**
     * wrapper function that checks sets of multiplication tables for ECA vs  multiplication CA
     */
    public void checkTablesAgainstMultiplicationAutomata(){
        CayleyDickson cd = new CayleyDickson();
        Fano fano = new Fano();
        int[][][] tabless = cd.preCalculateTables(3);
        System.out.println("Cayley Dickson permuted octonions");
        for (int table = 0; table < tabless.length; table++){
            //ecaInvolvedInMultiplicationAutomataSmallerVersionRandom(tabless[table],4,4,3);
        }
        tabless = fano.fanoGenerate();
        for (int table = 0; table < tabless.length; table++){
            ecaInvolvedInMultiplicationAutomataSmallerVersionRandom(tabless[table],4,4,3);
        }
    }
    /**
     * idle time exploration of rule thirty
     */
    public void ruleThirty(){
        int[][][] table = new int[10][4096][4096];
        int[][][] ruleStretchBin = new int[11][4][4096];
        int[][] ruleStretchQuat = new int[11][4096];
        int[] ruleThirty = new int[]{0,1,1,1,1,0,0,0};
        for (int power = 0; power < 8; power++){
            ruleStretchBin[3][3][power] = ruleThirty[power];
        }
        for (int neighborhood = 3; neighborhood < 9; neighborhood+=2){
            int length = (int)Math.pow(2,neighborhood);
            int nextLength = length*4;
            for (int row = 0; row < 3; row++){
                for (int spot = 0; spot < nextLength; spot++){
                    ruleStretchBin[neighborhood+2][row][spot] = ruleStretchBin[neighborhood][3][spot/(int)Math.pow(2,row)%length];
                }
            }
            for (int spot = 0; spot < nextLength; spot++){
                int tot = 0;
                for (int row = 0; row < 3; row++){
                    tot += (int)Math.pow(2,row)*ruleStretchBin[neighborhood+2][row][spot];
                }
                ruleStretchQuat[neighborhood+2][spot] = tot;
                ruleStretchBin[neighborhood+2][3][spot] = ruleStretchBin[3][3][tot];
            }
        }
        CayleyDickson cd = new CayleyDickson();
        int[][] activeTable;
        for (int neighborhood = 3; neighborhood < 5; neighborhood+=2){
            int length = (int)Math.pow(2,neighborhood);
            activeTable = new int[length][length];
            for (int row = 0; row < length; row++){
                for (int column = 0; column < length; column++){
                    activeTable[row][column] = cd.multiplyUnitHyperComplex(2,row,ruleStretchQuat[neighborhood][column],0,0);
                    //activeTable[row][column] = ruleStretchBin[neighborhood+2][3][row] ^ ruleStretchBin[neighborhood+2][3][column];
                    activeTable[row][column] = ruleStretchBin[3][3][row] ^ ruleStretchBin[3][3][column];
                }
            }
            System.out.println("Neighborhood " + neighborhood);
            for (int row = 0; row < length; row++){
                for (int column = 0; column < length; column++){
//                    if (activeTable[row][column] == 1){
//                        System.out.print("1");
//
//                    } else {
//                        System.out.print(" ");
//                    }
                    System.out.print(activeTable[row][column]);
                }
                System.out.print("\n");
            }
//            for (int row = 0; row < length; row++){
//                System.out.println(Arrays.toString(activeTable[row]));
//            }
//            int[][] currentTable = new int[length][length];
//            for (int power = 0; power < 3; power++){
//                System.out.println("power " + power );
//                for (int row = 0; row < length; row++) {
//                    for (int column = 0; column < length; column++) {
//                        currentTable[row][column] = activeTable[row][column] / (int)Math.pow(2,power) % 2;
//                    }
//                }
//                for (int row = 0; row < length; row++){
//                    System.out.println(Arrays.toString(currentTable[row]));
//                }
//            }
            System.out.println();
        }
    }
    public void paintComponent(Graphics g){
        int pixelSize = 1;
        int layer = 7;
        BufferedImage bufferedImage = new BufferedImage(pixelSize*256,pixelSize*256,BufferedImage.TYPE_INT_RGB);
        int[] raster = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
        for (int row = 0; row < 256; row++){
            for (int column = 0; column < 256; column++){
                raster[row*256+column] = tables[row][column][layer];
            }
        }
        g.drawImage(bufferedImage,0,0,null);

    }
    public void checkECAforComplex(){
        Fano fano = new Fano();
        BasicECA beca = new BasicECA();
        int[][][] ecaSquare = new int[256][4][4];
        for (int n = 0; n < 256; n++){
            int[] wolframCode = beca.ruleExtension(n)[4];
            wolframCode = Arrays.copyOfRange(wolframCode,0,16);
            for (int row = 0; row < 4; row++){
                for (int column = 0; column < 4; column++){
                    ecaSquare[n][row][column] = wolframCode[4*row+column];
                }
            }
        }
        for (int n = 0; n < 256; n++){
            int[] tots = new int[4];
            for (int row = 0; row < 4; row++){
                for (int column = 0; column < 4; column++){
                    tots[ecaSquare[n][row][column]]++;
                }
            }
           if (Arrays.equals(tots,new int[]{4,4,4,4})){

           }
           if (fano.isLatin(ecaSquare[n])){
               System.out.println("n:" + n);
               for (int row = 0; row < 4; row++) {
                   System.out.println(Arrays.toString(ecaSquare[n][row]));
               }
               System.out.println("By powers: " );
               for (int power = 0; power < 2; power++) {
                   for (int row = 0; row < 4; row++) {
                       for (int column = 0; column < 4; column++) {
                           System.out.print((ecaSquare[n][row][column]/(int)Math.pow(2,power) % 2));
                       }
                       System.out.print("\n");
                   }
                   System.out.print("\n");
               }
           }
        }
    }
    public void checkECAforComplex(int size){
        Fano fano = new Fano();
        BasicECA beca = new BasicECA();
        int[][][] ecaSquare = new int[256][size][size];
        for (int n = 0; n < 256; n++){
            int[] wolframCode = beca.ruleExtension(n)[6];
            wolframCode = Arrays.copyOfRange(wolframCode,0,64);
            for (int row = 0; row < size; row++){
                for (int column = 0; column < size; column++){
                    ecaSquare[n][row][column] = wolframCode[size*row+column];
                }
            }
        }
        for (int n = 0; n < 256; n++){
//            int[] tots = new int[size];
//            for (int row = 0; row < 4; row++){
//                for (int column = 0; column < 4; column++){
//                    tots[ecaSquare[n][row][column]]++;
//                }
//            }
//            if (Arrays.equals(tots,new int[]{4,4,4,4})){
//
//            }
            if (fano.isLatin(ecaSquare[n])){
                System.out.println("n:" + n);
                for (int row = 0; row < size; row++) {
                    System.out.println(Arrays.toString(ecaSquare[n][row]));
                }
                System.out.println("By powers: " );
                for (int power = 0; power < (int)(Math.log(size)/Math.log(2)); power++) {
                    for (int row = 0; row < size; row++) {
                        for (int column = 0; column < size; column++) {
                            System.out.print((ecaSquare[n][row][column]/(int)Math.pow(2,power) % 2));
                        }
                        System.out.print("\n");
                    }
                    System.out.print("\n");
                }
            }
        }
    }
    public void checkNinety(int lengthSequence){
        BasicECA beca = new BasicECA();
        int numBits = (int) (Math.log(lengthSequence)/Math.log(2));
        int[] checkThese = new int[6];
        int[][][][] eca = new int[256][numBits+1][lengthSequence+1][lengthSequence*3];
        //calculation loop
        for (int n = 0; n < 256; n++) {
            for (int repeat = 0; repeat < 3; repeat++){
                for (int spot = 0; spot < lengthSequence; spot++){
                    for (int layer = 0; layer < numBits; layer++) {
                        eca[n][layer][0][repeat * lengthSequence + spot] = spot/(int)Math.pow(2,layer) % 2;
                        eca[n][numBits][0][repeat*lengthSequence+spot] += (int)Math.pow(2,layer)*eca[n][layer][0][repeat*lengthSequence+spot];
                    }
                }
            }
            for (int layer = 0; layer < numBits; layer++){

            }

            for (int row = 1; row <= lengthSequence; row++){
                for (int column = row; column < lengthSequence*3-row; column++){
                    for (int layer = 0; layer < numBits; layer++) {
                        eca[n][layer][row][column] = eca[n][layer][row - 1][column - 1] + 2 * eca[n][layer][row - 1][column] + 4 * eca[n][layer][row - 1][column + 1];
                        eca[n][layer][row][column] = n / (int) Math.pow(2, eca[n][layer][row][column]) % 2;
                        eca[n][numBits][row][column] += (int)Math.pow(2,layer)*eca[n][layer][row][column];
                    }
                }
            }
            if (n == 90 || n == 165 || n == 102 || n == 153 || n == 105 || n == 150){
                System.out.println("n: " + n);
                for (int row = 0; row <= lengthSequence; row++){
                    System.out.println(Arrays.toString(eca[n][numBits][row]));
                }

            }

        }
        int cIndex = 0;

        for (int n = 0; n < 256; n++) {
            if (n == 90 || n == 165 || n == 102 || n == 153 || n == 105 || n == 150) {
                System.out.println(Arrays.toString(beca.ruleTruthTable[n]));
                for (int power = 0; power < 4; power++){
                    checkThese[cIndex] += (int)Math.pow(2,power)*beca.ruleTruthTable[n][power];
                }
                cIndex++;
            }
        }
        System.out.println(Arrays.toString(checkThese));
        checkThese = new int[]{3,12,5,10,9,6};
        PermutationsFactoradic pf = new PermutationsFactoradic();
        System.out.println(Arrays.deepToString(pf.combinations(4,2)));
        int[][] twos = pf.combinations(4,2);
        int[] twosCheck = new int[6];
        for (int combo = 0; combo < 6; combo++){
            int tot = (int)(Math.pow(2,twos[combo][0])+Math.pow(2,twos[combo][1]));
            System.out.println("tot: " + tot);
            twosCheck[combo] = tot;
        }
        int[][][] out = new int[4][6][8];
        int[] used = new int[256];
        int[] symmUsed = new int[88];

        for (int combo = 0; combo < 6; combo++){
            System.out.println("n: " + twosCheck[combo]);
            for (int mask = 0; mask < 4; mask++){
                for (int half = 0; half < 2; half++) {
                    for (int spot = 0; spot < 4; spot++) {
                        out[mask][combo][half*4+spot] = (int) (twosCheck[combo]/Math.pow(2,spot) % 2);
                    }
                }
                for (int half = 0; half < 2; half++){
                    for (int spot = 0; spot < 4; spot++){
                        out[mask][combo][4*half+spot] = ((out[mask][combo][4*half+spot] + (mask/(int)Math.pow(2,half)%2) )% 2);
                    }
                }
                int tot = 0;
                for (int power = 0; power < 8; power++){
                    tot += out[mask][combo][power]*(int)Math.pow(2,power);
                }
                for (int group = 0; group < 88; group++){
                    for (int symm = 0; symm < 4; symm++){
                        if (tot == beca.equivRules[group][symm]) symmUsed[group] = 1;
                    }
                }
                for (int symm = 0; symm < 4; symm++){
                    used[beca.lrTableInDecimal[tot][symm]] = 1;
                }
                System.out.println(tot);
            }
        }
        int totUsed = 0;
        System.out.println("Used rules");
        for (int n= 0; n < 256; n++){
            if (used[n] == 1){
                System.out.println("n: " + n + " class: " + beca.ruleClasses[n]);
                totUsed++;
            }
        }
        System.out.println("totUsed: " + totUsed);
        for (int group = 0; group < 88; group++){
            if (symmUsed[group] == 1){
                System.out.println("Group: " + group);
                System.out.println(Arrays.toString(beca.equivRules[group]));
            }
        }
    }
//    public void checkTriplets(){
//        int[][] table = specificTable(4,0,0);
//        int[][] triplets = new int[105][3];
//        int[][] rawTriplets = new int[105][3];
//        int index = 0;
//        for (int row = 1; row < 15; row++){
//            for (int column = row+1; column < 16; column++){
//                rawTriplets[index][0] = row % 16;
//                rawTriplets[index][1] = column % 16;
//                rawTriplets[index][2] = table[row][column] % 16;
//                for (int spot = 0; spot < 3; spot++){
//                    for (int sspot = 0; sspot < 3; sspot++){
//                        if (rawTriplets[index][spot] < rawTriplets[index][sspot]){
//                            int temp = rawTriplets[index][sspot];
//                            rawTriplets[index][sspot] = rawTriplets[index][spot];
//                            rawTriplets[index][spot] = temp;
//                        }
//                    }
//                }
//                index++;
//
//            }
//        }
//        int[] firstUse = new int[16];
//        for (int spot = 0; spot < 105; spot++){
//            for (int place = 0; place < 3; place++){
//                if (firstUse[rawTriplets[spot][place]] == 0){
//                    firstUse[rawTriplets[spot][place]] = spot;
//                }
//            }
//        }
//        for (int spot = 0; spot < 105; spot++){
//            for (int sspot = 0; sspot < 105; sspot++){
//                int a = 0;
//                int b = 0;
//                for (int place = 0; place < 3; place++){
//                    a += (int)Math.pow(7,place)*rawTriplets[spot][place];
//                    b += (int)Math.pow(7,place)*rawTriplets[sspot][place];
//                }
//                if ( a < b){
//                    for (int place = 0; place < 3; place++){
//                        int c = rawTriplets[spot][place];
//                        rawTriplets[spot][place] = rawTriplets[sspot][place];
//                        rawTriplets[sspot][place] = c;
//                    }
//                }
//            }
//        }
//        for (int spot = 0; spot < 105; spot++){
//            for (int place = 0; place < 3; place++){
//                if (firstUse[rawTriplets[spot][place]] == 0){
//                    firstUse[rawTriplets[spot][place]] = spot;
//                }
//            }
//        }
//        System.out.println(Arrays.deepToString(rawTriplets));
//        System.out.println(Arrays.toString(firstUse));
//    }
}


import java.util.Arrays;
import java.util.Random;
/**
 * Utility class for elementary cellular automata (ECA), Wolfram codes 0-255
 */
public class BasicECA {
    /**
     * General permutations and factoradic utility class
     */
    public PermutationsFactoradic pf = new PermutationsFactoradic();
    /**
     * numRowsOutput + 1 = the number of rows in the output grids
     * 3*(numRowsOutput + 1) = the number of columns in the output grids
     */
    public int numRowsOutput = 1000;
    /**
     * ECA 0-255 symmetry groups
     */
    public int[][] lrTableInDecimal = new int[256][4];
    /**
     * ECA 0-255 symmetry groups arranged by 0-88 independent groups
     */
    public int[][] equivRules = new int[88][4];
    /**
     * ECA Wolfram Codes 0-255
     */
    public int[][] ruleTruthTable = new int[256][8];
    /**
     * True means use single bit init input, false means use random input
     */
    public boolean singleInit;
    /**
     * Length of Wolfram code
     */
    public int wolframLength;
    /**
     * User generated initial input
     */
    public int[][] initInput;
    /**
     * Single bit initial input ECA output
     */
    public int[][] basicOutput = new int[numRowsOutput + 1][3 * (numRowsOutput + 1)];
    /**
     * Random initial input ECA output;
     */
    public int[][] randomInitOutput = new int[numRowsOutput + 1][3 * (numRowsOutput + 1)];
    /**
     * Wolfram codes of ECA beyond row 1, by size of input neighborhood, includes even numbers as a 2-bit place number.
     * Contains data for the last extended ECA rule in ruleExtension()
     */
    public int[][] ruleExtensions = new int[10][1024];
    /**
     * Width of random input to generate, in columns
     */
    public int widthRandom = 50;
    /**
     * Wolfram complexity classification, by rule
     */
    public int[] ruleClasses;
    /**
     * Wolfram complexity classification, by class
     */
    public int[][] classesRules;
    /**
     * ECA with permuted index bits, the left-right symmetry is a specific case of this, reversing the order of bits
     */
    public int[][] cousins;
    /**
     * Init
     */
    public BasicECA() {
        singleInit = true;
        for (int n = 0; n < 256; n++) {
            int temp = n;
            for (int power = 0; power < 8; power++) {
                ruleTruthTable[n][power] = temp % 2;
                temp = temp - (temp % 2);
                temp = temp / 2;
            }
        }
        initInput = new int[4096][16];
        for (int n = 0; n < 4096; n++) {
            int temp = n;
            for (int power = 0; power < 16; power++) {
                initInput[n][power] = temp % 2;
                temp = temp - (temp % 2);
                temp = temp / 2;
            }
        }
        lrTableInit();
        ruleClassify();
    }
    /**
     * Set of wolfram codes for a given ECA rule beyond row 1, calculated by running the ECA for every possible input neighborhood with length log(wolframLength(rows))
     *
     * @param n elementary cellular automata rule
     * @return array of wolfram codes for a given ECA rule beyond row 1
     */
    public int[][] ruleExtension(int n) {
        int[][] out = new int[10][512];
        for (int spot = 0; spot < 8; spot++) {
            out[3][spot] = ruleTruthTable[n][spot];
        }
        int[][] field;
        //odd numbered neighborhoods
        for (int neighborhood = 5; neighborhood < 10; neighborhood += 2) {
            wolframLength = (int) Math.pow(2, neighborhood);
            int numRows = (neighborhood - 1) / 2;
            field = new int[numRows + 1][neighborhood];
            //for all possible neighborhoods of wolframLength
            for (int input = 0; input < wolframLength; input++) {
                //initialize neighborhood
                for (int power = 0; power < neighborhood; power++) {
                    field[0][power] = initInput[input][power];
                }
                //calculate neighborhood
                for (int row = 1; row <= numRows; row++) {
                    for (int column = row; column < neighborhood - row; column++) {
                        field[row][column] = field[row - 1][column - 1] + 2 * field[row - 1][column] + 4 * field[row - 1][column + 1];
                        field[row][column] = out[3][field[row][column]];
                    }
                }
                //place result in Wolfram code
                out[neighborhood][input] = field[numRows][numRows];
            }
        }
        //even numbered neighborhoods, Wolfram code is a set of 2 bit numbers
        for (int neighborhood = 4; neighborhood < 9; neighborhood += 2) {
            int numRows = (neighborhood) / 2;
            wolframLength = (int) Math.pow(2, neighborhood);
            field = new int[neighborhood][neighborhood + 1];
            //for all possible neighborhoods of wolframLength
            for (int input = 0; input < wolframLength; input++) {
                //initialize neighborhood
                for (int power = 0; power < neighborhood; power++) {
                    field[0][power] = initInput[input][power];
                }
                //calculate results
                for (int row = 1; row <= numRows; row++) {
                    for (int column = row; column < neighborhood - row; column++) {
                        field[row][column] = field[row - 1][column - 1] + 2 * field[row - 1][column] + 4 * field[row - 1][column + 1];
                        field[row][column] = out[3][field[row][column]];
                    }
                }
                //place result in Wolfram code
                out[neighborhood][input] = 2 * field[numRows - 1][numRows - 1] + field[numRows - 1][numRows];
            }
        }
        ruleExtensions = out;
        return out;
    }
    /**
     * Generates the left-right-black-white rule symmetry groups for the 0-255 elementary cellular automata
     * <p>
     * The black white symmetry is generated by reversing the 8 bits of the wolfram code and negating them
     * <p>
     * The left right symmetry is generated by reversing the 3 places (1's place, 2's place, 4's place) of each
     * of the 8 bits
     * <p>
     * Left-right-black-white symmetry is doing both of the operations
     */
    public void lrTableInit() {
        for (int n = 0; n < 256; n++) {
            int[] outDec = new int[4];
            int[][] out = new int[4][8];
            for (int power = 0; power < 8; power++) {
                //identity
                out[0][power] = ruleTruthTable[n][power];
                //reverse code order and negate
                out[1][power] = ruleTruthTable[n][7 - power];
                out[1][power] = (out[1][power] + 1) % 2;
                //reverse bit order
                int lr = 0;
                for (int bit = 0; bit < 3; bit++) {
                    lr += (int) Math.pow(2, 2 - bit) * (power / (int) Math.pow(2, bit) % 2);
                }
                out[2][power] = ruleTruthTable[n][lr];
                //reverse code order and negate
                out[3][7 - power] = ruleTruthTable[n][lr];
                out[3][7 - power] = (out[3][7 - power] + 1) % 2;
            }
            //generate integer values of Wolfram codes
            for (int var = 0; var < 4; var++) {
                for (int power = 0; power < 8; power++) {
                    outDec[var] += (int) Math.pow(2, power) * out[var][power];
                }
            }
            for (int var = 0; var < 4; var++) {
                lrTableInDecimal[n][var] = outDec[var];
            }
        }
        //This section groups the 256 elementary rules into the 88 independent symmetry groups
        for (int i = 0; i < 88; i++) {
            for (int j = 0; j < 4; j++) {
                equivRules[i][j] = -1;
            }
        }
        boolean spotFound = true;
        for (int n = 0; n < 256; n++) {
            spotFound = false;
            boolean alreadyEqual = false;
            int index = 0;
            if (equivRules[index][0] == -1 || n == equivRules[index][0] || n == equivRules[index][1] || n == equivRules[index][2] || n == equivRules[index][3]) {
                spotFound = true;
                if (equivRules[index][0] == n || equivRules[index][1] == n || equivRules[index][2] == n || equivRules[index][3] == n) {
                    alreadyEqual = true;
                }
            }
            while (!spotFound) {
                index++;
                if (equivRules[index][0] == -1 || n == equivRules[index][0] || n == equivRules[index][1] || n == equivRules[index][2] || n == equivRules[index][3]) {
                    spotFound = true;
                }
                if (equivRules[index][0] == n || equivRules[index][1] == n || equivRules[index][2] == n || equivRules[index][3] == n) {
                    alreadyEqual = true;
                }
            }
            if (!alreadyEqual) {
                for (int column = 0; column < 4; column++) {
                    equivRules[index][column] = lrTableInDecimal[n][column];
                }
            }
            //System.out.println("n, " + n + " index, " + index);
        }
    }
    /**
     * Outputs the left-right-black-white rule symmetry groups
     */
    public void display() {
        System.out.println("All elementary automata rules and their equivalents");
        for (int n = 0; n < 256; n++) {
            System.out.println(Arrays.toString(lrTableInDecimal[n]));
        }
        System.out.println("Equivalent Rules sorted by group");
        for (int rule = 0; rule < 88; rule++) {
            //System.out.println("spot: " + rule + " \t" + equivRules[rule][0] + "\t" + equivRules[rule][1] + "\t" + equivRules[rule][2] + "\t" + equivRules[rule][3]);
            System.out.println(Arrays.toString(equivRules[rule]));
        }
    }
    /**
     * Elementary cellular automata with single-bit initial input
     *
     * @param n       elementary cellular automata rule, 0-255
     * @param rows    number of rows to calculate
     * @param columns number of columns to calculate
     * @return 2D binary integer array with the results of the ECA's single bit init input;
     */
    public int[][] basicOut(int n, int rows, int columns) {
        int[][] out = new int[rows + 1][columns];
        //initialize single bit initial input
        out[0][columns / 2] = 1;
        //run standard ECA calculation
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column < columns - 2; column++) {
                out[row][column] = (out[row - 1][column - 1] + 2 * out[row - 1][column] + 4 * out[row - 1][column + 1]);
                out[row][column] = ruleTruthTable[n][out[row][column]];
            }
        }
        return out;
    }
    /**
     * Elementary cellular automata with random initial input
     *
     * @param n           elementary cellular automata rule, 0-255
     * @param rows        number of rows to calculate
     * @param columns     number of columns to calculate
     * @param widthRandom desired width of random input
     * @return 2d integer array of ECA rule with random initial input
     */
    public int[][] randomOut(int n, int rows, int columns, int widthRandom) {
        int[][] randOut = new int[rows + 1][columns];
        //initialize neighborhood to random values
        for (int column = 0; column < widthRandom; column++) {
            randOut[0][columns / 2 - widthRandom / 2 + column] = randomInitOutput[0][column];
        }
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column < columns - 2; column++) {
                randOut[row][column] = (randOut[row - 1][column - 1] + 2 * randOut[row - 1][column] + 4 * randOut[row - 1][column + 1]);
                randOut[row][column] = ruleTruthTable[n][randOut[row][column]];
                //randomInitOutput[row][column] =  randOut[row][column];
            }
        }
        return randOut;
    }
    /**
     * Elementary cellular automata with single-bit initial input
     *
     * @param n elementary cellular automata rule, 0-255
     * @return 2D binary integer array with the results of the ECA's single bit init input;
     */
    public int[][] basicOut(int n) {
        int[][] out = new int[400][1000];
        //initialize single bit init input
        out[0][400] = 1;
        //standard ECA calculation
        for (int row = 1; row < 400; row++) {
            for (int column = 1; column < 998; column++) {
                out[row][column] = (out[row - 1][column - 1] + 2 * out[row - 1][column] + 4 * out[row - 1][column + 1]);
                out[row][column] = ruleTruthTable[n][out[row][column]];
            }
        }
        return out;
    }
    /**
     * Elementary cellular automata with random initial input
     *
     * @param n elementary cellular automata rule, 0-255
     * @return 2d integer array of ECA rule with random initial input
     */
    public int[][] randomOut(int n) {
        int[][] randOut = new int[400][1000];
        //initialize random input
        for (int column = 400; column < 500; column++) {
            randOut[0][column] = randomInitOutput[0][column];
        }
        //standard ECA calculation
        for (int row = 1; row < 400; row++) {
            for (int column = 1; column < 998; column++) {
                randOut[row][column] = (randOut[row - 1][column - 1] + 2 * randOut[row - 1][column] + 4 * randOut[row - 1][column + 1]);
                randOut[row][column] = ruleTruthTable[n][randOut[row][column]];
            }
        }
        return randOut;
    }
    /**
     * Console output of single-bit init input ECA
     *
     * @param inrule ECA rule to display
     */
    public void specificRuleOutput(int inrule) {
        basicOut(inrule);
        String outstring = "";
        System.out.println("Specific Rule " + inrule + " Initial conditions one 1, the rest are 0");
        for (int row = 0; row < 50; row++) {
            for (int column = 475; column < 525; column++) {
                if (basicOutput[row][column] == 1) {
                    outstring += Integer.toString(basicOutput[row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
        outstring = "";
        System.out.println("Specific Rule " + inrule + " Random Initial conditions");
        for (int row = 0; row < 50; row++) {
            for (int column = 1; column < 98; column++) {
                if (randomInitOutput[row][column] == 1) {
                    outstring += Integer.toString(randomInitOutput[row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
    }
    /**
     * Behavior classes of the ECA
     *
     * @return an integer array of every ECA's class
     */
    public int[] ruleClassify() {
        int[][] classes = new int[4][256];
        classes[0] = new int[]{0, 8, 32, 40, 128, 136, 160, 168};
        classes[1] = new int[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 19, 23, 24, 25, 26, 27, 28, 29, 33, 34, 35, 36, 37, 38, 42, 43, 44, 46, 50, 51, 56, 57, 58, 62, 72, 73, 74, 76, 77, 78, 94, 104, 108, 130, 132, 134, 138, 140, 142, 152, 154, 156, 162, 164, 170, 172, 178, 184, 200, 204, 232};
        classes[2] = new int[]{18, 22, 30, 45, 60, 90, 105, 122, 126, 146, 150};
        classes[3] = new int[]{41, 54, 106, 110};
        ruleClasses = new int[256];
        for (int row = 0; row < 4; row++) {
            for (int spot = 0; spot < classes[row].length; spot++) {
                for (int symm = 0; symm < 4; symm++) {
                    ruleClasses[lrTableInDecimal[classes[row][spot]][symm]] = row + 1;
                }
            }
        }


        classesRules = classes;
        return ruleClasses;
    }

}


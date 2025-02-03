import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * Contains a few examples using the algorithm, one with a class 4 focus, one with an additive rule focus, and a version of Conway's Life
 */
public class Examples {
    Random rand = new Random();
    /**
     * Finds solutions for class 4 rules
     */
    public void classFourFocus() {
        BasicECA basicECA = new BasicECA();
        GaloisFields gf = new GaloisFields();
        int[][][] tables = new int[1][8][8];
        int[][] multiplicationTable = gf.galoisFieldMultiplication(2, 3);
        tables[0] = multiplicationTable;
        List<List<ValidSolution>> validSolutions = new ArrayList<>();

        ECAasMultiplication ecam = new ECAasMultiplication();
        //ecam.post.partialProductTable = gf.galoisFieldAddition(3);
        GaloisStaging galoisStaging = new GaloisStaging();
        ecam.post.partialProductTable = galoisStaging.galoisAddition(3,1);

        int[] wolframCode = new int[8];
        for (int n = 0; n < 256; n++) {
            for (int power = 0; power < 8; power++) {
                wolframCode[power] = (n / (int) Math.pow(2, power) % 2);
            }
            if (basicECA.ruleClasses[n] == 4) {
                ecam.specific.generalWolframCode(wolframCode, 5, tables, 2);
                List<ValidSolution> localList = new ArrayList<>();
                for (int solution = 0; solution < ecam.specific.numSolutions; solution++){
                    localList.add(ecam.specific.validSolutions[solution]);
                }
                validSolutions.add(localList);
            }
        }
    }
    /**
     * Finds solutions for additive elementary cellular automata and outputs a particular rule's solution on the Swing panel
     *
     * Rowland, Todd and Weisstein, Eric W. "Additive Cellular Automaton." From MathWorld--A Wolfram Web Resource. https://mathworld.wolfram.com/AdditiveCellularAutomaton.html
     */
    public void additiveFocus() {
        int[] additiveList = new int[]{0, 60, 90, 102, 150, 170, 204, 240};
        List<List<ValidSolution>> validSolutions = new ArrayList<>();
        BasicECA basicECA = new BasicECA();
        GaloisFields gf = new GaloisFields();
        GaloisStaging galois = new GaloisStaging();
        GaloisStaging galoisStaging = new GaloisStaging();
        int[][][] tables = new int[1][8][8];
        int numFactors = 5;
        int[][] multiplicationTable = gf.galoisFieldMultiplication(2, 3);
        tables[0] = multiplicationTable;
        ECAasMultiplication ecam = new ECAasMultiplication();
        //ecam.post.partialProductTable = gf.galoisFieldAddition(3);
        ecam.post.partialProductTable = galoisStaging.galoisAddition(3,1);

        int[] wolframCode = new int[8];
        for (int n = 0; n < additiveList.length; n++) {
            for (int power = 0; power < 8; power++) {
                wolframCode[power] = (additiveList[n] / (int) Math.pow(2, power) % 2);
            }
            ecam.specific.generalWolframCode(wolframCode, numFactors, tables, 2);
            List<ValidSolution> localList = new ArrayList<>();
            for (int solution = 0; solution < ecam.specific.numSolutions; solution++){
                localList.add(ecam.specific.validSolutions[solution]);
            }
            validSolutions.add(localList);
        }
        //pick a rule, pick a solution
        SwingComplexOutput swingPanel = new SwingComplexOutput("Complex output");
        validSolutions.get(0).get(0).polynomial = ecam.post.generatePolynomial(validSolutions.get(0).get(0));
        swingPanel.currentSolution = validSolutions.get(0).get(0);
        swingPanel.complexField = ecam.post.multiplicativeSolutionOutput(validSolutions.get(0).get(0),ecam.post.randomComplexInput(-1,1),400,1000);
        swingPanel.repaint();
    }
    /**
     * Generates Conway's Life truth table
     *
     * @return a non-totalistic, power additive Wolfram code of Conway's Life
     */
    public int[] generateConwayWolfram() {
        int[] conway = new int[512];
        for (int n = 0; n < 512; n++) {
            int[] nn = new int[9];
            for (int power = 0; power < 9; power++) {
                nn[power] = n / (int) Math.pow(2, power) % 2;
            }
            int tot = 0;
            for (int power = 1; power < 8; power++) {
                tot += nn[power];
            }
            if (nn[0] == 1) {
                if (tot < 2) conway[n] = 0;
                if (tot == 2 || tot == 3) conway[n] = 1;
                if (tot > 3) conway[n] = 0;
            } else {
                if (tot == 3) {
                    conway[n] = 1;
                } else {
                    conway[n] = 0;
                }
            }
        }
        for (int index = 0; index < 512; index++) {
            if (index % 64 == 0) System.out.print("\n");
            System.out.print(conway[index]);
        }
        conway = Arrays.copyOfRange(conway, 0, 256);
        return conway;
    }
    /**
     * Generates a random bit selection array for the Cayley-Dickson automorphisms, a factoradic
     * @param degree degree of Cayley-Dickson table being used, 2=quaternions, 3=octonions, etc
     * @return a random factoradic of length degree
     */
    public int[] randomCDZremainder(int degree) {
        int[] out = new int[degree];
        for (int spot = 0; spot < degree - 1; spot++) {
            out[spot] = rand.nextInt(0, degree - 1 - spot);
        }
        out[degree - 1] = 0;
        return out;
    }
    /**
     * Randomizes the factors from generalWolframCode(), normally handled through permutations and factoriadics, but the degree factorial algorithm, higher than 16! is instead randomized
     * @param in factor to be randomized, in binary array form
     * @return randomized factor, in place of permutations in ECAasMultiplications
     */
    public int[] shuffleFactor(int[] in) {
        for (int shuffle = 0; shuffle < 2 * in.length; shuffle++) {
            int a = rand.nextInt(0, in.length);
            int b = rand.nextInt(0, in.length);
            int c = in[a];
            in[a] = in[b];
            in[b] = c;
        }
        return in;
    }

    /**
     * A specialized version of generalWolframCode() from ECAspecific, used for experimenting with finding multiplicative solutions for Life.
     *
     * Life is a totalistic automata, but is treated like an additive automata for the sake of this function. A totalistic automata uses a truth table lookup using the quantity of 0s and 1s and the position doesn't matter, only the total. Elementary cellular automata use a power weighted sum of the neighborhood to lookup the truth table.
     *
     * @param wolframCode User-supplied Wolfram, a version of the Life code
     * @param numFactors  Number of factors to use, number of dimensions in the multiplication table
     */
    public void generalWolframCode(int[] wolframCode, int numFactors) {
        int[] tempCode = new int[wolframCode.length];
        for (int spot = 0; spot < wolframCode.length; spot++) {
            tempCode[spot] = wolframCode[wolframCode.length - 1 - spot];
        }
        //wolframCode = tempCode;
        PermutationsFactoradic pf = new PermutationsFactoradic();
        CayleyDickson cd = new CayleyDickson();
        Random rand = new Random();
        //number of bits used to address the Wolfram code
        int places = (int) (Math.log(wolframCode.length) / Math.log(2));
        System.out.println("wolframCode.length: " + wolframCode.length);
        System.out.println("places: " + places);
        //used as operand in the multiplications
        int multResult = 0;
        //active local solution number
        //set of permuted axes used as factors currently being considered
        int[][] factors = new int[numFactors + 1][wolframCode.length];
        //set of permutations currently being considered
        int[] ps = new int[numFactors];
        int maxAttempts = 500000;
        int maxTableAttempts = 1;
        int[] cdz;
        int[] cdo;
        int[][] specificPermutations = new int[numFactors][places];
        int longestStreak = 0;
        int factor = 0;
        int input = 0;
        int degree = places - 1;
        int tableAttempt = 0;
        mainLoop:
        for (int l = 0; l < maxAttempts; l++) {
//            numFactors = rand.nextInt(10, 64);
//            degree = rand.nextInt(16, 30);
            numFactors = 23;
            degree = 17;
            specificPermutations = new int[numFactors][degree + 1];
            for (factor = 0; factor < numFactors; factor++) {
                for (int place = 0; place < degree + 1; place++) {
                    specificPermutations[factor][place] = place;
                }
                specificPermutations[factor] = shuffleFactor(specificPermutations[factor]);
            }
            factors = new int[numFactors + 1][wolframCode.length];
            for (input = 0; input < wolframCode.length; input++) {
                for (factor = 0; factor < numFactors; factor++) {
                    for (int place = 0; place < degree + 1; place++) {
                        factors[factor][input] += (int) Math.pow(2, place) * (input / (int) Math.pow(2, specificPermutations[factor][place]) % 2);
                    }
                }
            }
            tableLoop:
            for (tableAttempt = 0; tableAttempt < maxTableAttempts; tableAttempt++) {
                //multiplication using given parameters
                cdz = randomCDZremainder(degree);
                cdo = randomCDZremainder(degree);
                for (input = 0; input < wolframCode.length; input++) {
                    multResult = factors[0][input];
                    for (factor = 1; factor < numFactors; factor++) {
                        multResult = cd.multiply(degree, multResult, factors[factor][input], cdz, cdo);
                    }
                    if (wolframCode[multResult % 128] != wolframCode[input]) {
                        //if this set of parameters does not reproduce the given truth table, exit the loops
                        continue tableLoop;
                    }
                    if (input > longestStreak) {
                        longestStreak = input;
                        System.out.println("New record streak! " + longestStreak);
                        System.out.println("numFactors: " + numFactors + " degree: " + degree);
                        if (input >= 63) {
                            System.out.println("Holee Shit");
                            System.out.println("factors: " + numFactors);
                            System.out.println("cdz: " + Arrays.toString(cdz));
                            System.out.println("cdo: " + Arrays.toString(cdo));
                        }
                    }
                }
                System.out.println("Holee Shit");
                System.out.println("factors: " + factors);
                System.out.println("cdz: " + Arrays.toString(cdz));
                System.out.println("cdo: " + Arrays.toString(cdo));
                break mainLoop;
            }
            if (l % 1000 == 0) {
                System.out.println("Attempts: " + l);
            }
        }
    }
    //Backwards
    //Holee Shit
    //factors: 23
    //cdz: [15, 2, 13, 1, 4, 9, 4, 0, 6, 3, 2, 1, 3, 1, 0, 0, 0]
    //cdo: [3, 10, 5, 3, 7, 3, 1, 5, 0, 4, 0, 4, 2, 0, 1, 0, 0]
    //Forwards
    //New record streak! 63
    //numFactors: 23 degree: 17
    //Holee Shit
    //factors: 23
    //cdz: [6, 10, 12, 0, 11, 4, 3, 0, 2, 2, 0, 4, 2, 1, 0, 0, 0]
    //cdo: [3, 4, 10, 10, 11, 7, 2, 7, 0, 4, 0, 2, 2, 2, 0, 0, 0]
}


//Daniel McKinley
//12/31/2023
//dmcki23@wgu.edu
//
//
//This class searches for elementary automata solutions as a path through a sedonion
//multiplication truth table cube
//search() and searchStop() are the control functions for search all of each of the groups
//phaseSearch() and multSearch() are the relevant search implementation functions
//specificPatternDisplay() displays a successful permutation set for a rule n
//note that the elementary automata calculations are done big-endian in this implementation
//import java.rmi.server.LogStream;
import java.util.Arrays;
import java.util.Random;
/**
 * Manages ECAMdeep, ECAMpostProcessing, and ECAMspecific, contains the heart of the project in generalWolframCode() and multiplicativeSolutionOutput()
 *
 * @author Daniel W McKinley
 */
public class ECAasMultiplication {
    /**
     * Names of the various multiplication tables
     */
    public enum whichMultTableNames {
        /**
         * as named
         */
        PERMUTED_CAYLEY_DICKSON,
        /**
         * as named
         */
        XOR_GALOIS_ADDITION,
        /**
         * as named
         */
        GALOIS_THREE_TWO,
        /**
         * as named
         */
        GALOIS_SEVENTEEN_ONE,
        /**
         * as named
         */
        GALOIS_TWO_THREE,
        /**
         * as named
         */
        GALOIS_TWO_FOUR,
        /**
         * as named
         */
        GALOIS_TWO_FIVE,
        /**
         * as named
         */
        GALOIS_TWO_SIX,
        /**
         * as named
         */
        FANO_TRIPLETS,
    }
    /**
     * Logic gate names as strings
     */
    public String[] logicGateNames = new String[]{"False", "NOR", "B and Not A", "Not A", "A and Not B", "Not B", "XOR", "NAND", "AND", "XNOR", "A", "B Or Not A", "B", "A Or Not B", "OR", "True"};
    /**
     * Basic ECA utility class
     */
    public BasicECA beca = new BasicECA();
    /**
     * Generates hypercomplex numbers and multiplication tables using permutation variations of the Cayley-Dickson method
     */
    public CayleyDickson cds = new CayleyDickson();

    /**
     * Once a solution is found, it is sent here for further processing and output
     */
    public ECAMpostProcessing post = new ECAMpostProcessing();
    /**
     * Generates Galois field multiplication and addition tables
     */
    public GaloisFields galoisFields = new GaloisFields();





    /**
     * Contains functions that exhaustively search various length sets of Wolfram codes and analyze the results
     */
    public ECAMdeep deep;
    /**
     * Contains functions that search specific Wolfram codes for neighborhood permutation multiplication solutions
     */
    public ECAMspecific specific;

    /**
     * Initializes fields
     */
    public ECAasMultiplication() {
        deep = new ECAMdeep(this);
        specific = new ECAMspecific(this);
    }




    /**
     * This tests generalWolframCode() and ECAMpostProceessing with random Wolfram codes and random data
     *
     * @param length  length of random wolfram code to use, must be powers of 2
     * @param rows    number of rows to calculate
     * @param columns number of columns to calculate
     * @param randRange lower bound of random input
     * @param randMax upper bound of random input
     * @return the Wolfram code used
     */
    public int[] testGeneralWolframCode(int length, int rows, int columns, int randRange, int randMax) {
        int numBits = (int) (Math.log(length) / Math.log(2));
        int[] code = new int[length];
        Random rand = new Random();
        int[] binaryInput = post.randomBinaryInput();
        double[] coeffInput = post.randomDoubleInput(randRange, randMax);

        for (int n = 0; n < length; n++) {
            code[n] = rand.nextInt(0, 2);
        }
        GaloisFields galois = new GaloisFields();
        int[][][] tables = new int[1][length][length];
        tables[0] = galois.galoisFieldAddition(length);
        post.partialProductTable = galoisFields.galoisFieldAddition(numBits);
        int[][] boringTable = new int[numBits][numBits];
        for (int row = 0; row < numBits; row++){
            for (int column =  0; column < numBits; column++){
                boringTable[row][column] = column;
            }
        }
        //post.useNorm = true;
        //post.partialProductTable = boringTable;

        specific.generalWolframCode(code, 5, cds.preCalculateTables(numBits - 1),1);
        //secapp.partialProductTable = galois.galoisFieldAddition(numBits);
        System.out.println("Solution totals " + specific.numSolutions);
        System.out.println("Factors      " + Arrays.deepToString(specific.validSolutions[0].factors));
        System.out.println("Permutations " + Arrays.toString(specific.validSolutions[0].permutationGroup));
        post.multiplicativeSolutionOutput(specific.validSolutions[0], binaryInput, coeffInput, 400,1000);
        specific.validSolutions[0].polynomial = post.generatePolynomial(specific.validSolutions[0]);
        specific.validSolutions[0].polynomialString = post.polynomialAsStrings(specific.validSolutions[0].polynomial);
        return code;
    }

    /**
     * Generates a set of multiplication tables with the given parameters
     *
     * @param whichMultTable which set of multiplication table(s) to use      @see whichMultTableNames
     * @param degree         if selecting permuted Cayley-Dickson, what degree, 2=quaternions, 3=sedonions
     * @param places         number of bits to use per index, log(gridSize)
     * @return a set of multiplication tables
     */
    public int[][][] generateMultTable(int whichMultTable, int degree, int places) {
        //size of multiplication table
        int gridSize = 8;
        //output set of multiplication tables
        int[][][] multTables = new int[1][1][1];
        GaloisFields galois = new GaloisFields();
        if (whichMultTable == whichMultTableNames.PERMUTED_CAYLEY_DICKSON.ordinal()) {
            multTables = cds.preCalculateTables(degree);

        } else if (whichMultTable == 1) {
            gridSize = (int) Math.pow(2, places);
            multTables = new int[1][gridSize][gridSize];
            for (int row = 0; row < gridSize; row++) {
                for (int column = 0; column < gridSize; column++) {
                    multTables[0][row][column] = row ^ column;
                }
            }
        } else if (whichMultTable == whichMultTableNames.XOR_GALOIS_ADDITION.ordinal()) {
            int[][] tempTable;
            tempTable = galois.galoisFieldMultiplication(3, 2);
            multTables = new int[1][8][8];
            for (int row = 0; row < 8; row++) {
                for (int column = 0; column < 8; column++) {
                    multTables[0][row][column] = tempTable[row + 1][column + 1] - 1;
                }
            }
        } else if (whichMultTable == whichMultTableNames.GALOIS_SEVENTEEN_ONE.ordinal()) {
            int[][] tempTable = galois.galoisFieldMultiplication(17, 1);
            multTables = new int[1][16][16];
            for (int row = 0; row < 8; row++) {
                for (int column = 0; column < 8; column++) {
                    multTables[0][row][column] = tempTable[row + 1][column + 1] - 1;
                }
            }
        } else if (whichMultTable == whichMultTableNames.GALOIS_TWO_THREE.ordinal()) {
            multTables = new int[1][8][8];
            multTables[0] = galois.galoisFieldMultiplication(2, 3);
        } else if (whichMultTable == whichMultTableNames.GALOIS_TWO_FOUR.ordinal()) {
            multTables = new int[1][16][16];
            multTables[0] = galois.galoisFieldMultiplication(2, 4);
        } else if (whichMultTable == whichMultTableNames.GALOIS_TWO_FIVE.ordinal()) {
            multTables = new int[1][32][32];
            multTables[0] = galois.galoisFieldMultiplication(2, 5);
        } else if (whichMultTable == whichMultTableNames.GALOIS_TWO_SIX.ordinal()) {
            multTables = new int[1][64][64];
            multTables[0] = galois.galoisFieldMultiplication(2, 6);
        } else if (whichMultTable == whichMultTableNames.FANO_TRIPLETS.ordinal()) {
            multTables = new int[1][16][16];
            Fano fano = new Fano();
            multTables = fano.fanoGenerate();
        } else if (whichMultTable == whichMultTableNames.GALOIS_THREE_TWO.ordinal()) {
            multTables = new int[1][8][8];
            multTables[0] = galois.generateTable(3, 2, true);
        }
        return multTables;
    }
    /**
     * Displays a valid solution
     * @param solution any valid ECAasMultiplication solution
     */
    public void displayValidSolution(ValidSolution solution){
        System.out.println();
        System.out.println("ValidSolution");
        System.out.println("Wolfram code: " + Arrays.toString(solution.wolframCode));
        int[] permutedMultiplicationWolframCode = new int[solution.wolframCode.length];
        for (int factor = 0; factor <= solution.numFactors; factor++){
            if (factor!= solution.numFactors){
                System.out.println("Permutation: " + solution.permutationGroup[factor] + "\t Permuted Axis: " + Arrays.toString(solution.factors[factor]));
                System.out.println("\t times");
            } else {
                System.out.println("-----------------");
                System.out.println("Equals:                                              " + Arrays.toString(solution.factors[factor]));
                for (int spot = 0; spot < permutedMultiplicationWolframCode.length; spot++){
                    permutedMultiplicationWolframCode[spot] = solution.factors[solution.numFactors][spot];
                    permutedMultiplicationWolframCode[spot] = solution.wolframCode[permutedMultiplicationWolframCode[spot]];
                }
                System.out.println("Apply Wolfram code to multiplication result");
                System.out.println("Equals:                                              "+Arrays.toString(permutedMultiplicationWolframCode));
                System.out.println("Original Wolfram code:                  "+Arrays.toString(solution.wolframCode));
            }
        }
        System.out.println("Permutation composition product: " + solution.permGroupProduct + " inverse: " + solution.permGroupProductInverse);
        System.out.println();
        System.out.println("Multiplication table type: " + solution.whichMultTable);
        System.out.println("2D multiplication table used: " );
        for (int row = 0; row < solution.multTable.length; row++){
            System.out.println(Arrays.toString(solution.multTable[row]));
        }
        System.out.println("numFactors: " + solution.numFactors + " numBits: " + solution.numBits + " ");
        solution.polynomial = post.generatePolynomial(solution);
        solution.polynomialString = post.polynomialAsStrings(solution.polynomial);
        for (int row  = 0; row < solution.numBits; row++){
            System.out.println(solution.polynomialString[row]);
            System.out.println();
        }
        //solution.polynomial = post.generatePolynomial(solution);

    }
    /**
     * Displays a valid solution in String form
     * @param solution any valid ECAasMultiplication solution
     * @param overloadDistinguisher dummy value to distinguish this version of the function from the other overload
     * @return a String version of the console output from the other displayValidSolution()
     */
    public String displayValidSolution(ValidSolution solution, boolean overloadDistinguisher){
        System.out.println();
        String outstring = "";
        outstring += "\n" +  ("ValidSolution");
        outstring += "\n" +  ("Wolfram code: " + Arrays.toString(solution.wolframCode));
        int[] permutedMultiplicationWolframCode = new int[solution.wolframCode.length];
        for (int factor = 0; factor <= solution.numFactors; factor++){
            if (factor!= solution.numFactors){
                outstring += "\n" +  ("Permutation: " + solution.permutationGroup[factor] + "\t Permuted Axis: " + Arrays.toString(solution.factors[factor]));
                outstring += "\n" +  ("\t times");
            } else {
                outstring += "\n" +  ("-----------------");
                outstring += "\n" +  ("Equals:                                              " + Arrays.toString(solution.factors[factor]));
                for (int spot = 0; spot < permutedMultiplicationWolframCode.length; spot++){
                    permutedMultiplicationWolframCode[spot] = solution.factors[solution.numFactors][spot];
                    permutedMultiplicationWolframCode[spot] = solution.wolframCode[permutedMultiplicationWolframCode[spot]];
                }
                outstring += "\n" +  ("Apply Wolfram code to multiplication result");
                outstring += "\n" +  ("Equals:                                              "+Arrays.toString(permutedMultiplicationWolframCode));
                outstring += "\n" +  ("Original Wolfram code:                  "+Arrays.toString(solution.wolframCode));
            }
        }
        outstring += "\n" +  ("Permutation composition product: " + solution.permGroupProduct + " inverse: " + solution.permGroupProductInverse);
        outstring += "\n" ;
        outstring += "\n" +  ("Multiplication table type: " + solution.whichMultTable);
        outstring += "\n" +  ("2D multiplication table used: " );
        for (int row = 0; row < solution.multTable.length; row++){
            outstring += "\n" +  (Arrays.toString(solution.multTable[row]));
        }
        outstring += "\n" +  ("numFactors: " + solution.numFactors + " numBits: " + solution.numBits + " ");
        solution.polynomial = post.generatePolynomial(solution);
        solution.polynomialString = post.polynomialAsStrings(solution.polynomial);
        for (int row  = 0; row < solution.numBits; row++){
            outstring += "\n" +  (solution.polynomialString[row]);
            outstring += ("\n");
        }
        return outstring;
        //solution.polynomial = post.generatePolynomial(solution);

    }

}






/**
 * A solution from ECAasMultiplication is in this form
 */
public class ValidSolution {
    /**
     * Multiplication table used
     */
    public int[][] multTable;
    /**
     * Set of factors produced from the permutationGroup[], factors[numFactors][] is the multiplication result row
     */
    public int[][] factors;
    /**
     * Permutation set used
     */
    public int[] permutationGroup;
    /**
     * Set of factor and result path lengths
     */
    public double[] pathLengths;
    /**
     * Set index of multiplication table used
     */
    public int tableNumber;
    /**
     * Type of multiplication table, @see ECAasMultiplication.whichMultTableNames
     */
    public int whichMultTable;
    /**
     * Wolfram code of solution
     */
    public int[] wolframCode;
    /**
     * Number of factors used in solution
     */
    public int numFactors;
    /**
     * Number of rows used, if applicable
     */
    public int numRows;
    /**
     * Number of bits in solution, size of ECA neighborhood
     */
    public int numBits;
    /**
     * Whether this solution is an identity
     */
    public boolean identity;
    /**
     * Whether this solution is a non-trivial identity
     */
    public boolean nonTrivialIdentity;
    /**
     * Whether this solution's multiplication result has repeated indices
     */
    public boolean noRepeats;
    /**
     * Solution in polynomial form. polynomial[0..places-1][][] is the terms, and polynomial[places][][row] is the coefficient for that term, the coefficients are the last row in the array
     */
    public int[][][] polynomial;
    /**
     * The permutation composition product of the solution's permutation group
     */
    public int permGroupProduct;
    /**
     * The inverse of the permutation composition product of the solution's permutation group
     */
    public int permGroupProductInverse;
    /**
     * Value of the Wolfram code in decimal
     */
    int wolframCodeDecimal;
    /**
     * Value of the gpaWolframCode in decimal
     */
    int gpaCodeDecimal;
    /**
     * Initializes empty values so an empty ValidSolution doesn't throw null exceptions
     */
    public String[] polynomialString;
    /**
     * Applies the permutation composition product of the group to the result layer of factors[][]
     */
    public int[] groupProductArray;
    /**
     * Applies the original Wolfram code to the groupProductArray[]
     */
    public int[] gpaWolframCode;
    /**
     * Initializes fields to zero and arrays of length 1
     */
    ValidSolution() {
        permGroupProduct = 0;
        wolframCode = new int[1];
        multTable = new int[1][1];
        factors = new int[1][1];
        permutationGroup = new int[1];
        tableNumber = 0;
        whichMultTable = 0;
        numFactors = 0;
        numBits = 0;
        numRows = 0;
        identity = false;
        nonTrivialIdentity = false;
        noRepeats = false;
        polynomial = new int[1][1][1];
        polynomialString = new String[]{"", "", ""};
        pathLengths = new double[numFactors];
        groupProductArray = new int[1];
        gpaWolframCode = new int[1];
    }
    /**
     * Extracts the factors[][] from an array of ValidSolution
     * @param solutions an array of solutions from ECAasMultiplication
     * @return the factors[][] part of a ValidSolution[] array
     */
    public static int[][][] extractFactors(ValidSolution[] solutions) {
        int[][][] out = new int[solutions.length][1][1];
        for (int sol = 0; sol < solutions.length; sol++) {
            out[sol] = solutions[sol].factors;
        }
        return out;
    }
    /**
     * Extracts the permutation groups from an array of ValidSolution[]
     * @param solutions an array of  solutions from ECAasMultiplication
     * @return the permutationGroup part of the ValidSolution[] array
     */
    public static int[][] extractPermutationGroups(ValidSolution[] solutions){
        int[][] out = new int[solutions.length][1];
        for (int sol = 0; sol < solutions.length; sol++){
            out[sol] = solutions[sol].permutationGroup;
        }
        return out;
    }
    /**
     * Extracts the group product array from an array of ValidSolution[]
     * @param solutions an array of solutions from ECAasMultiplication
     * @return the groupProductArray[] part of the ValidSolution[] array
     */
    public static int[][] extractGroupProductArray(ValidSolution[] solutions){
        int[][] out = new int[solutions.length][1];
        for (int sol = 0; sol < solutions.length; sol++){
            out[sol] = solutions[sol].groupProductArray;
        }
        return out;
    }
    /**
     * Extracts the polynomials from an array of ValidSolution[]
     * @param solutions an array of solutions from ECAasMultiplication
     * @return the polynomial[][][] part of the ValidSolution[] array
     */
    public static int[][][][] extractPolynomials(ValidSolution[] solutions){
        int[][][][] out = new int[solutions.length][1][1][1];
        for (int sol = 0; sol < solutions.length; sol++){
            out[sol] = solutions[sol].polynomial;
        }
        return out;
    }
}

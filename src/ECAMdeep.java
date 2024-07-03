
import java.util.Arrays;
/**
 * Exhaustively searches sets of  all Wolfram codes of various lengths, with various parameters
 */
public class ECAMdeep {
    /**
     * Number of solutions for each ECA
     */
    public int[] ecaSolutionTotals;
    /**
     * Number of solutions for each logic gate
     */
    public int[] logicSolutionTotals;
    /**
     * Each ECA's total number of solutions with repeats in ValidSolution.factors[numFactors][], the result row
     */
    public int[] hasRepeats;
    /**
     * Each ECA's total number of solutions without repeats in ValidSolution.factors[numFactors][], the result row
     */
    public int[] noRepeats;
    /**
     * Each ECA's total number of identity solutions in ValidSolutions.factors[numFactors][], the result row
     */
    public int[] hasIdentity;
    /**
     * Each ECA's total number of non-trivial identity solutions in ValidSolutions.factors[numFactors][], the result row
     */
    public int[] hasNonTrivialIdentity;
    /**
     * Array of all ECA solutions with given parameters
     */
    public ValidSolution[][] ecaSolutions;
    /**
     * Array of all 0-15 logic gate solutions with given parameters
     */
    public ValidSolution[][] logicSolutions;
    /**
     * Super class
     */
    ECAasMultiplication ecam;
    /**
     * Initializes ecam
     *
     * @param inEcam super class
     */
    public ECAMdeep(ECAasMultiplication inEcam) {
        ecam = inEcam;
    }
    /**
     * Similar to deepECAsearch() except that it uses the 0-16 four bit logic gate truth table instead of the 0-255 ECA.
     * <p>
     * Cross-references solutions to find which gates have solutions in common. Aggregates solution identiity information.
     *
     * @param numFactors number of factors to permute
     * @param whichTable which set of multiplication tables to use, @see whichMultTableNames
     * @return a String of the output, used in SwingTextPanel
     */
    public String deepLogicSearch(int numFactors, int whichTable) {
        //setup
        int[][][] tables = new int[1][4][4];
        GaloisFields galois = new GaloisFields();
        if (whichTable == 0) {
            tables = new int[1][4][4];
            tables[0] = galois.galoisFieldAddition(4);
        } else if (whichTable == 1) {
            tables = new int[1][4][4];
            tables[0] = galois.generateTable(5, 1, true);
        } else if (whichTable == 2) {
            tables = new int[1][4][4];
            tables[0] = galois.generateTable(2, 2, false);
        }
        logicSolutions = new ValidSolution[16][1];
        logicSolutionTotals = new int[16];
        //main Multiplications A loop
        for (int gate = 0; gate < 16; gate++) {
            ecam.specific.logicGateSearchSpecific(gate, numFactors, tables, whichTable);
            logicSolutions[gate] = ecam.specific.validSolutions.clone();
            logicSolutionTotals[gate] = logicSolutions[gate].length;
        }
        //tallying results
        int[] hasRepeats = new int[16];
        int[] identities = new int[16];
        int[] nonTrivialIdentities = new int[16];
        int[] noRepeats = new int[16];
        for (int gate = 0; gate < 16; gate++) {
            for (int sol = 0; sol < logicSolutionTotals[gate]; sol++) {
                if (logicSolutions[gate][sol].identity) identities[gate]++;
                if (logicSolutions[gate][sol].nonTrivialIdentity) nonTrivialIdentities[gate]++;
                if (logicSolutions[gate][sol].noRepeats) noRepeats[gate]++;
                if (!logicSolutions[gate][sol].noRepeats) hasRepeats[gate]++;
            }
        }
        //outputting results
        for (int gate = 0; gate < 16; gate++) {
            System.out.println("Gate: " + gate + "\t Total solutions: " + logicSolutionTotals[gate] + "\t Identities: " + identities[gate] + "\t  Non-tirivial identities: " + nonTrivialIdentities[gate] + "\t Repeats: " + hasRepeats[gate] + "\t No repeats: " + noRepeats[gate]);
        }
        String outstring = "";
        for (int gate = 0; gate < 16; gate++) {
            outstring += ("Gate: " + gate + "\t Total solutions: " + logicSolutionTotals[gate] + "\nIdentities: " + identities[gate] + "\t  Non-tirivial identities: " + nonTrivialIdentities[gate] + "\nRepeats: " + hasRepeats[gate] + "\t No repeats: " + noRepeats[gate]) + "\n\n";
        }
        return outstring;
    }
    /**
     * This function searches every 0-255 ECA rule with the given parameters for valid solutions and cross-references and displays the results, similar to deepLogicSearch() but focused on ECA rather than logic gate Wolfram codes
     * <p>
     * Cross-references solutions to see which ECA have solutions in common. Checks all ECA solutions for identities and non-trivial identities ( a non-trivial identity is a multiplication result row that equals the identity, with a non-zero permutation set).
     * Checks all ECA solutions for having repeat indices and non-repeat indices.
     *
     * @param degree         degree hypercomplex, 2 = quaternions, 3 = octonions, etc.
     * @param numRows        number of rows in the ECA analyze, 1 row is standard 3-bit input neighborhood, 2 rows is a 5-bit neighborhood, etc
     * @param numFactors     number of factors to use in calculation
     * @param whichMultTable which multiplication table option
     * @return a String of the output, used in SwingTextPanel
     */
    public String deepECASearch(int degree, int numRows, int numFactors, int whichMultTable) {
        //aggregate solution bins
        int numBits = (2 * numRows + 1);
        ecaSolutions = new ValidSolution[256][1];
        for (int n = 0; n < 256; n++) {
            ecaSolutions[n][0] = new ValidSolution();
        }
        ecaSolutionTotals = new int[256];
        //set of multiplication tables to use
        int[][][] multTables = new int[1][8][8];
        multTables = ecam.generateMultTable(whichMultTable, degree, numBits);
        //do for every 0-255 ECA, main Multiplications A loop
        for (int n = 0; n < 256; n++) {
            System.out.print(n + " ");
            if (n % 16 == 0) System.out.print("\n");
            ecam.specific.ecaSpecificRuleSearch(n, numRows, numFactors, multTables, whichMultTable);
            ecaSolutions[n] = ecam.specific.validSolutions;
            ecaSolutionTotals[n] = ecam.specific.numSolutions;
        }
        //tally and output results
        System.out.println();
        String outstring = "";
        System.out.println("Total Solutions grouped by left right black white rule symmetry");
        for (int group = 0; group < 88; group++) {
            System.out.println("Group " + group + ": " + ecam.beca.equivRules[group][0] + " " + ecam.beca.equivRules[group][1] + " " + ecam.beca.equivRules[group][2] + " " + ecam.beca.equivRules[group][3]);
            System.out.println(ecaSolutionTotals[ecam.beca.equivRules[group][0]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][1]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][2]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][3]]);
            System.out.println();
        }
        outstring = "";
        System.out.println("ECA 0-255 solution totals arranged in a 16x16 grid");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += ecaSolutionTotals[row * 16 + column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
        hasIdentity = new int[256];
        hasRepeats = new int[256];
        noRepeats = new int[256];
        hasNonTrivialIdentity = new int[256];
        for (int n = 0; n < 256; n++) {
            for (int sol = 0; sol < ecaSolutionTotals[n]; sol++) {
                if (ecaSolutions[n][sol].noRepeats) noRepeats[n]++;
                if (!ecaSolutions[n][sol].noRepeats) hasRepeats[n]++;
                if (ecaSolutions[n][sol].nonTrivialIdentity) hasNonTrivialIdentity[n]++;
                if (ecaSolutions[n][sol].identity) hasIdentity[n]++;
            }
        }
        System.out.println("ECA rules with a multiplication result with no repeated indexes");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += noRepeats[row * 16 + column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("ECA rules with a multiplication result that has repeated indexes");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += hasRepeats[row * 16 + column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("Has identity");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + " " + hasIdentity[row * 16 + column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("ECA rules' non trivial identity arrays, {0 = not exist, 1 = exist} as a multiplication result");
        System.out.println("a non trivial identity is an ");
        System.out.println("ECApathPermutation solution");
        System.out.println("(1) with the unit identity array [0,1,2,3,4,5,6,7..k]");
        System.out.println("as the result of the multiplications, in the numFactors column of the factor array");
        System.out.println("(2) with a non-zero set of permutations used on the indexes");
        System.out.println("");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + " " + hasNonTrivialIdentity[16 * row + column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("ECA rule has solution, {0 = no solutions, 1 = has solution(s)}, arranged in a 16 by 16 grid");
        System.out.println("Easy to check if a set of parameters, multiplicationTables numFactors, has a solution in every rule ");
        outstring = "";
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                if (ecaSolutionTotals[16 * row + column] > 0) {
                    outstring += "1";
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        //
        //
        //
        //
        //same results to return a String
        outstring = "";
        outstring += "\n" + ("Total Solutions grouped by left right black white rule symmetry\n");
        for (int group = 0; group < 88; group++) {
            outstring += "\n" + ("Group " + group + ": " + ecam.beca.equivRules[group][0] + " " + ecam.beca.equivRules[group][1] + " " + ecam.beca.equivRules[group][2] + " " + ecam.beca.equivRules[group][3]);
            outstring += "\n" + (ecaSolutionTotals[ecam.beca.equivRules[group][0]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][1]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][2]] + " " + ecaSolutionTotals[ecam.beca.equivRules[group][3]]);
            outstring += "\n";
        }
        outstring += "\n";
        outstring += "\n" + ("ECA 0-255 solution totals arranged in a 16x16 grid\n");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + ": \t" + ecaSolutionTotals[row * 16 + column] + "\t";
            }
            outstring += "\n";
        }
        outstring += "\n";
        hasIdentity = new int[256];
        hasRepeats = new int[256];
        noRepeats = new int[256];
        hasNonTrivialIdentity = new int[256];
        for (int n = 0; n < 256; n++) {
            for (int sol = 0; sol < ecaSolutionTotals[n]; sol++) {
                if (ecaSolutions[n][sol].noRepeats) noRepeats[n]++;
                if (!ecaSolutions[n][sol].noRepeats) hasRepeats[n]++;
                if (ecaSolutions[n][sol].nonTrivialIdentity) hasNonTrivialIdentity[n]++;
                if (ecaSolutions[n][sol].identity) hasIdentity[n]++;
            }
        }
        outstring += "\n" + ("ECA rules with a multiplication result with no repeated indexes");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + "\t " + noRepeats[row * 16 + column] + "\n";
            }
        }
        outstring += "\n" + ("ECA rules with a multiplication result that has repeated indexes");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + "\t " + hasRepeats[row * 16 + column] + "\n";
            }
            outstring += "\n";
        }
        outstring += "\n";
        outstring += "\n" + ("Has identity");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + "\t " + hasIdentity[row * 16 + column] + "\n";
            }
            outstring += "\n";
        }
        outstring += "\n";
        outstring += "\n" + ("ECA rules' non trivial identity arrays, {0 = not exist, 1 = exist} as a multiplication result");
        outstring += "\n" + ("a non trivial identity is an ");
        outstring += "\n" + ("ECApathPermutation solution");
        outstring += "\n" + ("(1) with the unit identity array [0,1,2,3,4,5,6,7..k], k = wolframCode.length");
        outstring += "\n" + ("as the result of the multiplications, in the numFactors column of the factor array");
        outstring += "\n" + ("(2) and with a non-zero set of permutations used on the indexes");
        outstring += "\n" + ("");
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring += "n: " + (16 * row + column) + " \t" + hasNonTrivialIdentity[16 * row + column] + "\n";
            }
            outstring += "\n";
        }
        outstring += "\n" + ("ECA rule has solution, {0 = no solutions, 1 = has solution(s)}, arranged in a 16 by 16 grid");
        outstring += "\n" + ("Easy to check if a set of parameters, multiplicationTables numFactors, has a solution in every rule ");
        outstring += "\n";
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                if (ecaSolutionTotals[16 * row + column] > 0) {
                    outstring += "1";
                } else {
                    outstring += " ";
                }
            }
            outstring += "\n";
        }
        return outstring;
    }
}

import java.util.Arrays;
/**
 * Main algorithm, heart of the project, takes a given length Wolfram code,searches sets of permutations that permute a cellular automata neighborhood such that their hypercomplex multiplication of  n dimensions always results in a value that is an equal Wolfram code value. Neighborhood permutation multiplication results in a pass-by-reference  array that reproduces the original Wolfram code. These results are stored in ValidSolution arrays and passed to ECAMpostProcessing for polynomial and output
 */
public class ECAMspecific {
    /**
     * A timeout variable for the search functions, in milliseconds
     */
    public long searchStop;

    /**
     * Limits the searches to a maximum number of functions, can be set to 1 for first available solution
     */
    public int maxSolutions;
    /**
     * Number of solutions found in last search
     */
    public int numSolutions;
    /**
     * Last Wolfram code passed to generalWolframCode() or ecaSpecificRuleSearch()
     */
    public int[] lastWolframCode;
    /**
     * Last number of factors used in generalWolframCode() or ecaSpecificRuleSearch()
     */
    public int lastNumFactors;
    /**
     * Utility class that has permutations, factoradics, combinations, etc
     */
    public PermutationsFactoradic pf = new PermutationsFactoradic();
    /**
     * All solutions found in the last search
     */
    public ValidSolution[] validSolutions;

    /**
     * Manager
     */
    public ECAasMultiplication ecam;
    /**
     * Initializes a few things
     *
     * @param inPath instance of manager class
     */
    public ECAMspecific(ECAasMultiplication inPath) {
        ecam = inPath;
        maxSolutions = 10000;
        searchStop = 60000 * 5;
    }
    /**
     * Multiplications A in the paper.
     *
     * Finds permutation groups with length numFactors, of degree numBits, such that when each permutation is applied to its axis, the diagonal through the numFactors-dimensional multiplication table produces a pointer array that points to equal values of the original Wolfram code, reproducing the original Wolfram code.
     *
     * This algorithm brute forces possible permutation groups, and the permutation group multiplication operation here is hypercomplex or finite rather than permutation composition, though it is still a closed operation. A valid permutation group is such that for every possible input neighborhood, the hypercomplex multiplication result points to an equal value within the Wolfram code
     * of the Wolfram code.
     *
     * Has two timeout escape variables, maxSolutions and searchStop, which are the number of solutions to break the main loop at and number of milliseconds to break at. It is not guaranteed that it will reach either of these numbers, but high numbers of numFactors and longer Wolfram codes take exponentially-factorially longer. O(n)=(bits!)^numFactors
     *
     * @param wolframCode    binary integer array used in place of an ECA truth table
     * @param numFactors     number of factors to use in calculation
     * @param tables         multiplication tables to use
     * @param whichMultTable type of multiplication table
     * @return array of all solutions for the given rule, in factor array format
     */
    public int[][][] generalWolframCode(int[] wolframCode, int numFactors, int[][][] tables, int whichMultTable) {
        //class field, last Wolfram code used
        lastWolframCode = wolframCode;
        //class field, last number of factors used
        lastNumFactors = numFactors;
        //time at beginning of function call, used to keep a timeout loop break
        long initialTime = System.currentTimeMillis();
        //number of bits used to address the Wolfram code
        int numBits = (int) (Math.log(wolframCode.length) / Math.log(2));
        //list of permutations of length place
        int[][] perms = pf.permSequences(numBits);
        //local copy of all valid table indexes
        int[] localTableNumbers = new int[maxSolutions];
        //local copy of all valid sets of permuted factors
        int[][][] solutions = new int[maxSolutions][numFactors + 1][wolframCode.length];
        //local copy of all valid sets of solution lengths
        double[][] lengths = new double[maxSolutions][wolframCode.length];
        //local copy of all valid sets of permutations
        int[][] localPermSolutions = new int[maxSolutions][numFactors];
        //table numbers of valid solutions
        int[] tableNumbers = new int[maxSolutions];
        //length Wolfram code
        int wolframLength = wolframCode.length;
        //Permutation composition grid
        int[][] pop = pf.permsOfPermsGrid(numBits);
        //used as operand in the multiplications
        int multResult = 0;
        //active local solution number
        int activeSolution = 0;
        //set of permuted axes used as factors currently being considered
        int[][] factors = new int[5][wolframCode.length];
        //set of permutations currently being considered
        int[] ps = new int[numFactors];
        //number of possible permutations per factor
        int placesFactorial = pf.factorial(numBits);
        mainLoop:
        for (int l = 0; l < (int) Math.pow(pf.factorial(numBits), numFactors); l++) {
            //splits off the main loop counter into a permutation of each factor, converts it into base places!
            for (int factor = 0; factor < numFactors; factor++) {
                ps[factor] = (l / (int) Math.pow(placesFactorial, factor)) % placesFactorial;
            }
            //applies the permutation group to each axes' elements
            factors = new int[numFactors + 1][wolframCode.length];
            for (int input = 0; input < wolframCode.length; input++) {
                for (int factor = 0; factor < numFactors; factor++) {
                    for (int place = 0; place < numBits; place++) {
                        factors[factor][input] += (int) Math.pow(2, place) * (input / (int) Math.pow(2, perms[ps[factor]][place]) % 2);
                    }
                }
            }
            //for each table in the multiplication table set
            tableLoop:
            for (int table = 0; table < tables.length; table++) {
                //multiplication using given parameters
                for (int input = 0; input < wolframCode.length; input++) {
                    multResult = factors[0][input];
                    for (int factor = 1; factor < numFactors; factor++) {
                        multResult = tables[table][multResult][factors[factor][input]] % wolframLength;
                    }
                    factors[numFactors][input] = multResult;
                    //trims the result to size, the table size and the binary input array size
                    if (wolframCode[multResult] != wolframCode[input]) {
                        //if this set of parameters does not reproduce the given truth table, continue the loop
                        continue tableLoop;
                    }
                }
                //stashes the results in the local aggregate variables
                localTableNumbers[activeSolution] = table;
                tableNumbers[activeSolution] = table;
                lengths[activeSolution] = calculatePathLengths(factors, numFactors, wolframCode.length);
                for (int factor = 0; factor <= numFactors; factor++) {
                    solutions[activeSolution][factor] = Arrays.copyOfRange(factors[factor], 0, factors[factor].length);
                }
                localPermSolutions[activeSolution] = Arrays.copyOfRange(ps, 0, ps.length);
                activeSolution++;
                if (System.currentTimeMillis() - initialTime > searchStop) {
                    break mainLoop;
                }
                if (activeSolution >= maxSolutions) {
                    break mainLoop;
                }
            }
        }
        //initializes the variables of the solutions
        validSolutions = new ValidSolution[activeSolution];
        for (int sol = 0; sol < activeSolution; sol++) {
            validSolutions[sol] = new ValidSolution();
            validSolutions[sol].factors = new int[numFactors + 1][wolframCode.length];
            for (int factor = 0; factor <= numFactors; factor++) {
                validSolutions[sol].factors[factor] = Arrays.copyOfRange(solutions[sol][factor], 0, wolframCode.length);
            }
            validSolutions[sol].permutationGroup = localPermSolutions[sol];
            validSolutions[sol].pathLengths = lengths[sol];
            validSolutions[sol].tableNumber = localTableNumbers[sol];
            validSolutions[sol].multTable = tables[localTableNumbers[sol]];
            validSolutions[sol].numBits = numBits;
            validSolutions[sol].wolframCode = wolframCode;
            validSolutions[sol].numFactors = numFactors;
            validSolutions[sol].wolframCode = wolframCode;
            validSolutions[sol].whichMultTable = whichMultTable;
            validSolutions[sol].permGroupProduct = validSolutions[sol].permutationGroup[0];
            for (int factor = 1; factor < numFactors; factor++) {
                validSolutions[sol].permGroupProduct = pop[validSolutions[sol].permGroupProduct][validSolutions[sol].permutationGroup[factor]];
            }
            for (int inverse = 0; inverse < pop.length; inverse++){
                if (pop[validSolutions[sol].permGroupProduct][inverse] == 0){
                    validSolutions[sol].permGroupProductInverse = inverse;
                }
            }

            validSolutions[sol] = checkIdentities(validSolutions[sol]);
        }
        numSolutions = activeSolution;
        return solutions;
    }
    /**
     * Focuses on 8 bit elementary automata
     *
     * @param n                   ECA rule to search
     * @param numRows             number of rows in the ECA analyze, 1 row is standard 3-bit input neighborhood, 2 rows is a 5-bit neighborhood, etc
     * @param numFactors          number of factors to use in calculation
     * @param multiplicationField the multiplication table to search for an index-permuted diagonal through
     * @param whichMultTable      type of multiplication table, @see ECAasMultiplications.whichMultTableNames
     */
    public void ecaSpecificRuleSearch(int n, int numRows, int numFactors, int[][][] multiplicationField, int whichMultTable) {

        int[] wolframCode = Arrays.copyOfRange(ecam.beca.ruleExtension(n)[2 * numRows + 1], 0, (int) Math.pow(2, 2 * numRows + 1));
        generalWolframCode(wolframCode,numFactors,multiplicationField,whichMultTable);

    }

    /**
     * Focuses on 4-bit logic gate truth table Wolfram codes
     *
     * @param gate           logic gate 0-15 to search for
     * @param numFactors     number of factors to use in the search
     * @param tables         set of multiplication tables to use, typically from generateMultTable()
     * @param whichMultTable type of multiplication table
     */
    public void logicGateSearchSpecific(int gate, int numFactors, int[][][] tables, int whichMultTable) {
        //truth table of current logic gate
        int[] activeGate = new int[4];
        //set of multiplication tables used
        int[][][] table = tables;
        //generates gate truth table
        for (int power = 0; power < 4; power++) {
            activeGate[power] = gate / (int) Math.pow(2, power) % 2;
        }
        //actual search function
        generalWolframCode(activeGate, numFactors, table, whichMultTable);
    }
    /**
     * Calculates the path lengths of a set of valid permuted factors from a search function using
     * the n-dim Pythagorean Theorem
     *
     * @param factors    set of valid permuted factors from a search function, factors[numFactors+1][codeSpot]
     *                   the 0-numFactors rows are the factors, the numFactors row is the multiplication result
     * @param numFactors number of factors considered in the set
     * @param codeLength length of factor set, Wolfram code
     * @return path lengths for each factor and the result
     */
    public double[] calculatePathLengths(int[][] factors, int numFactors, int codeLength) {
        //calculates length via Pythagorean Theorem for each factor and the result row
        double[] out = new double[numFactors + 1];
        //cells 1 through codeLength
        for (int input = 1; input < codeLength; input++) {
            //factor lengths
            for (int factor = 0; factor < numFactors; factor++) {
                double segment = factors[factor][input] - factors[factor][input - 1];
                segment = segment * segment + 1;
                segment = Math.sqrt(segment);
                out[factor] += segment;
            }
            //result length
            double diagLength = 0;
            double tot = 0;
            for (int factor = 0; factor < numFactors; factor++) {
                diagLength = factors[factor][input] - factors[factor][input - 1];
                diagLength = diagLength * diagLength;
                tot += diagLength;
            }
            out[numFactors] += Math.sqrt(tot);
        }
        //final cell
        //factor lengths
        for (int factor = 0; factor < numFactors; factor++) {
            double segment = codeLength - factors[factor][codeLength - 1];
            segment = segment * segment + 1;
            segment = Math.sqrt(segment);
            out[factor] += segment;
        }
        //result length
        double diagLength = 0;
        double tot = 0;
        for (int factor = 0; factor < numFactors; factor++) {
            diagLength = codeLength - factors[factor][codeLength - 1];
            diagLength = diagLength * diagLength;
            tot += diagLength;
        }
        out[numFactors] += Math.sqrt(tot);
        return out;
    }
    /**
     * Takes a validSolution and returns it with the appropriate boolean identity fields flagged appropriately. An identity is a result layer of Multiplications A [0,1,2,3,4,5,6,7...] as appropriately long. A non-trivial identity is an identity with a non-zero set of permutations that produces it. No repeats is one of everything not necessarily in identity order.
     *
     * @param validSolution a valid solution from a search function
     * @return returns the input with the boolean identity and repeat flags flagged appropriately
     */
    public ValidSolution checkIdentities(ValidSolution validSolution) {
        int length = validSolution.wolframCode.length;
        int numFactors = validSolution.numFactors;
        int[] used = new int[length];
        for (int spot = 0; spot < length; spot++) {
            used[validSolution.factors[numFactors][spot]]++;
        }
        boolean repeats = false;
        for (int spot = 0; spot < length; spot++) {
            if (used[spot] > 1 || used[spot] == 0) {
                repeats = true;
            }
        }
        if (repeats) {
            validSolution.noRepeats = false;
        } else {
            validSolution.noRepeats = true;
        }
        boolean nonZeropermutationGroup = false;
        for (int spot = 0; spot < numFactors; spot++) {
            if (validSolution.permutationGroup[spot] > 0) nonZeropermutationGroup = true;
        }
        boolean identity = true;
        for (int spot = 0; spot < length; spot++) {
            if (spot != validSolution.factors[numFactors][spot]) {
                identity = false;
            }
        }
        if (identity && nonZeropermutationGroup) {
            validSolution.nonTrivialIdentity = true;
        }
        if (identity) {
            validSolution.identity = true;
        }
        return validSolution;
    }
}

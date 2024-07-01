import java.util.Arrays;
import java.util.Random;
/**
 * Secondary processing of solutions found in ECAasMultiplication, generates output of a ValidSolution, generates the polynomial
 *
 * @author Daniel W McKinley
 */
public class ECAMpostProcessing {
    /**
     * When false, some functions output to the console, when true this console output is suppressed
     */
    boolean suppressConsole;
    /**
     * General permutations and factoradic utility class
     */
    PermutationsFactoradic pf = new PermutationsFactoradic();
    /**
     * Variable used in outputting to console
     */
    public String outstring = "";
    /**
     * Number of unique terms in the neighborhood of the polynomial from generatePolynomial()
     */
    int[] uniqueIndex;
    /**
     * Coefficient of each term in the polynomial from generatePolynomial
     */
    int[][] uniqueTermsTally;
    /**
     * Unique terms of each neighborhood cell from generatePolynomial;
     */
    int[][][] uniqueTerms;
    /**
     * ValidSolution output data, non-negative real
     */
    public double[][] coefficientField;
    /**
     * Width of random input to use
     */
    public int widthOfRandomInput = 50;
    /**
     * 2D array of doubles with the coefficients of a validSolutionCoefficientCalculation()
     */
    public double[][] field;
    /**
     * Binary 2D array of integers of a Wolfram code calculation with given input that validSolutionCoefficientCalculation() parallel multiplication operates
     */
    public int[][] ruleField;
    /**
     * Binary 2D array of the Wolfram code output
     */
    public int[][] ruleOutput;
    /**
     * Partial product table used in the neighborhood permutation multiplications
     */
    public int[][] partialProductTable;
    /**
     * This is used in localNormalize()
     * if true, it is the norm of the neighborhood, sqrt(place[0]^2+place[1]^2+place[2]^2...)
     * if false, it is  ((2^0)*place[0]+(2^1)*place[1]+(2^2)*place[2]...)^(1/numFactors)
     */
    boolean useNorm;
    /**
     * Binary neighborhoods with a valid permutation group applied, each permutation creates a layer, each cell in a layer is a factor in the multiplication
     */
    public int[][][] factorLayers;
    /**
     * This is the multiplication result for each cell, just after the geometric mean, and just before the neighborhood normalization, each layer is the unit vector coefficient for that cell, Multiplications B, with non neg real floating point
     */
    public double[][][] vectorField;
    /**
     * Output of a ValidSolution in validSolutionCoefficientCalculation(), complex vector, Multiplications B just prior to normalization
     */
    public Complex[][][] complexVectorField;
    /**
     * Output of a ValidSolution in validSolutionCoefficientCalculation(), complex, Multiplications B, after normalization
     */
    public Complex[][] complexField;
    /**
     * Solution currently being used
     */
    public ValidSolution currentSolution;
    /**
     * If true, when normalizing, the normalizing is done on the imaginary and real parts separately, if false the normalizations are done on the complex number as a whole
     */
    boolean useComplexComponentRoots;
    /**
     * How many rows and columns to calculate. validSolutionCoefficientCalculation() calculates this size field, the other functions operate as subsets of this array[gridSize][gridSize].
     */
    public int gridSize;
    /**
     * This is the calculation field of the version of the algorithm that generates the complex neighborhood factors the same way as the hypercomplex or finite neighborhood unit vectors are calculated.
     * Each permutation of the neighborhood is a power weighted sum of that neighborhood. This is a direct parallel to the binary version of the automata solution. Multiplications C output
     */
    Complex[][] neighborhoodNormalizeFirst;
    /**
     * Whether to normalize the cell to the unit circle
     */
    public boolean normalizeUnit;
    /**
     * If true, Wolfram code lookups that equal 1 negate the real or imaginary parts of the cell. If false, the Wolfram code lookups zero out the real or imaginary parts of the cell
     */
    public boolean wolframIsNegOne;
    /**
     * If true, and if normalizeUnit is true, if the radius is zero, set it equal to 1 to avoid division by zero.
     */
    public boolean avoidDivZero;
    /**
     * If true, it does a binary sum of the real and imaginary parts of the input neighborhood, greater than 0 being 0 and  less than 0  being 1, the real and imaginary treated as seperate, and calls the solution's Wolfram code with that value,
     * and depending on the Wolfram code value at that additive spot, it sets it to 0 or -1, depending on wolframIsNegOne
     */
    public boolean doAddititiveWolfram;
    /**
     * Currently empty
     */
    public ECAMpostProcessing() {
        gridSize = 4000;
        normalizeUnit = true;
        wolframIsNegOne = false;
        avoidDivZero = true;
        doAddititiveWolfram = true;
    }
    /**
     * Uses a solution found in ECAasMultiplication and applies the same permutation group multiplication to a neighborhood of non-negative real numbers in addition to the standard binary Wolfram calculation. The partialProduct[][] table is a class field set
     * externally from this function. The partialProduct[][] table is a closed group of size numBits x numBits, typically Galois addition or multiplication
     * <p>
     * Non-negative real
     *
     * @param solution    user-supplied ValidSolution from ECAasMultiplication
     * @param binaryInput user-supplied binary array to use as input at row 0 in the ECA calculation
     * @param doubleInput user-supplied double array to use as coefficient input at row 0 in the ECA calculation
     * @param rows        user supplied number of rows to calculate
     * @param columns     user supplied number of columns to calculate
     * @return a 2d double array of the solution applied to the given rule using random input
     */
    public double[][] multiplicativeSolutionOutput(ValidSolution solution, int[] binaryInput, double[] doubleInput, int rows, int columns) {
        //useNorm = !useNorm;
        int[] rule = solution.wolframCode;
        int places = solution.numBits;
        int numFactors = solution.numFactors;
        int[][] factors = solution.factors;
        int[] permutationGroup = solution.permutationGroup;
        currentSolution = solution;
        System.out.println("binaryInput " + Arrays.toString(binaryInput));
        System.out.println("coeffInput " + Arrays.toString(doubleInput));
        //coefficient multiplication results
        field = new double[gridSize][gridSize];
        //standard ECA calculation with given input
        ruleField = new int[gridSize][gridSize];
        vectorField = new double[places][gridSize][gridSize];
        System.out.println("numFactors: " + numFactors);
        System.out.println(factors.length);
        System.out.println(Arrays.deepToString(factors));
        //Wolfram code used in ECA calculation
        System.out.println(Arrays.toString(rule));
        System.out.println(Arrays.toString(permutationGroup));
        //initializes to input and calculates the standardECA computation
        for (int column = (gridSize / 2) - (binaryInput.length / 2); column < (gridSize / 2) + (binaryInput.length / 2); column++) {
            field[0][column] = doubleInput[column - (gridSize / 2) + (doubleInput.length / 2)];
            ruleField[0][column] = binaryInput[column - (gridSize / 2) + (binaryInput.length / 2)];
            if (ruleField[0][column] == 0) field[0][column] = 0;
        }
        System.out.println(Arrays.toString(ruleField[0]));
        //standard ECA calculation
        for (int row = 1; row <= rows; row++) {
            for (int column = places; column < gridSize - 1 - places; column++) {
                for (int relativeColumn = 0; relativeColumn < places; relativeColumn++) {
                    ruleField[row][column] += (int) Math.pow(2, relativeColumn) * ruleField[row - 1][column - places / 2 + relativeColumn];
                }
                ruleField[row][column] = rule[ruleField[row][column]];
            }
        }
        //set of permutations of length places
        int[][] perms = pf.permSequences(places);
        //active set of coefficients
        double[] nextMult = new double[places];
        //running tally of multiplications
        double[][] mults = new double[numFactors][places];
        //main loops
        for (int row = 1; row <= rows; row++) {
            for (int column = places; column < gridSize - 1 - places; column++) {
                mults = new double[numFactors + 1][places];
                int[] neighborhoodMask = new int[places];
                for (int place = 0; place < places; place++) {
                    mults[0][place] = field[row - 1][column - places / 2 + perms[permutationGroup[0]][place]];
                    neighborhoodMask[place] = ruleField[row - 1][column - places / 2 + perms[solution.permGroupProduct][place]];
                }
                //multiplication loop
                for (int mult = 1; mult < numFactors; mult++) {
                    //set up next factor
                    for (int place = 0; place < places; place++) {
                        nextMult[place] = field[row - 1][column - places / 2 + perms[permutationGroup[mult]][place]];
                    }
                    //multiply and add result to appropriate column  according to partial product table
                    for (int place = 0; place < places; place++) {
                        for (int axxis = 0; axxis < places; axxis++) {
                            mults[mult][partialProductTable[place][axxis]] += mults[mult - 1][place] * nextMult[axxis];
                        }
                    }
                }
                //normalize factor-wise
                for (int place = 0; place < places; place++) {
                    mults[numFactors][place] = geometricMean(mults[numFactors - 1][place], numFactors);
                    if (ruleField[row][column] == 1) {
                        vectorField[place][row][column] = mults[numFactors - 1][place];
                    } else {
                        vectorField[place][row][column] = 0;
                    }
                }
                //if the standard ECA calculation value at each cell is zero, the coefficient is zero
                if (ruleField[row][column] == 1) {
                    //normalize neighborhood-wise
                    field[row][column] = localNormalize(mults[numFactors]);
                }
            }
        }
        //console display stuff
        System.out.println("partialProductTable ");
        System.out.println(Arrays.deepToString(partialProductTable));
        System.out.println("ECA field");
        outstring = "";
        for (int row = 0; row < 40; row++) {
            for (int column = (gridSize / 2) - columns / 2; column < (gridSize / 2) + columns / 2; column++) {
                outstring += ruleField[row][column];
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("ECA field with real positive integer coefficients multiplied out by the quaternion permutation multiplication solution ");
        outstring = "";
        for (int row = 0; row < 40; row++) {
            for (int column = (gridSize / 2) - columns / 2; column < (gridSize / 2) + columns / 2; column++) {
                outstring += String.format("%3.2f", field[row][column]) + "\t";
                //outstring += field[row][column] + "\t";
            }
            System.out.println(outstring);
            outstring = "";
        }
//        int[][] outField = new int[rows][columns];
//        for (int row = 0; row < rows; row++) {
//            for (int column = 1400 - columns / 2; column < 1400 + columns / 2; column++) {
//                outField[row][column + columns / 2 - 1400] = ruleField[row][column];
//            }
//        }
//        double[][] outCoeff = new double[rows][columns];
//        for (int row = 0; row < rows; row++) {
//            for (int column = 1400 - columns / 2; column < 1400 + columns / 2; column++) {
//                outCoeff[row][column + columns / 2 - 1400] = field[row][column];
//            }
//        }
        coefficientField = new double[rows][columns];
        ruleOutput = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                coefficientField[row][column] = field[row][(gridSize / 2) - columns / 2 + column];
                ruleOutput[row][column] = ruleField[row][(gridSize / 2) - columns / 2 + column];
            }
        }
        return field;
    }
    /**
     * Uses a solution found in ECAasMultiplication and applies the same permutations and constructions to a neighborhood of complex numbers.  Multiplication B in the paper screenshot. Multiplication C is the other part of this function. Uses the solution's polynomial from generatePolynomial() to multiply, the partial product table was applied there.
     * <p>
     * Complex
     *
     * @param solution     user-supplied ValidSolution from ECAasMultiplication
     * @param complexInput user-supplied complex array to use as coefficient input at row 0 in the ECA calculation
     * @param rows         user supplied number of rows to calculate
     * @param columns      user supplied number of columns to calculate
     * @return a 2d double array of the solution applied to the given rule using random input
     */
    public Complex[][] multiplicativeSolutionOutput(ValidSolution solution, Complex[] complexInput, int rows, int columns) {
        //setup and initialize
        int[] rule = solution.wolframCode;
        //number of columns in the neighborhood
        int places = solution.numBits;
        int numFactors = solution.numFactors;
        int[][] factors = solution.factors;
        int[] permutationGroup = solution.permutationGroup;
        //set of permutations of length places
        int[][] perms = pf.permSequences(places);
        //used to multiply out the polynomial
        Complex[] complexMults = new Complex[places];
        currentSolution = solution;
        //Multiplications B's vector, result just prior to normalization
        complexVectorField = new Complex[places][gridSize][gridSize];
        //Multiplication B's normalized result
        complexField = new Complex[gridSize][gridSize];
        //Multiplication C's output
        neighborhoodNormalizeFirst = new Complex[gridSize][gridSize];
        System.out.println("numFactors: " + numFactors);
        System.out.println(factors.length);
        System.out.println(Arrays.deepToString(factors));
        System.out.println(Arrays.toString(rule));
        System.out.println(Arrays.toString(permutationGroup));
        //if you don't construct every cell specifically, you get null pointers
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                complexField[row][column] = new Complex(0, 0);
                neighborhoodNormalizeFirst[row][column] = new Complex(0, 0);
                for (int place = 0; place < places; place++) {
                    complexVectorField[place][row][column] = new Complex(0, 0);
                }
            }
        }
        //user input data is placed in row 0, centered to the middle of gridSize.
        for (int column = (gridSize / 2) - (complexInput.length / 2); column < (gridSize / 2) + (complexInput.length / 2); column++) {
            complexField[0][column] = complexInput[column - gridSize / 2 + complexInput.length / 2];
            neighborhoodNormalizeFirst[0][column] = complexInput[column - gridSize / 2 + complexInput.length / 2];
        }
        //for every cell in the calculation field
        mainLoop:
        for (int row = 1; row <= rows; row++) {
            for (int column = places + row; column < gridSize - places - row; column++) {
                //initialize factor array
                complexMults = new Complex[places];
                for (int place = 0; place < places; place++) {
                    complexMults[place] = new Complex();
                }
                //load first permutation of the cell's neighborhood as a factor
                for (int place = 0; place < places; place++) {
                    complexMults[place] = complexField[row - 1][column - places / 2 + perms[permutationGroup[0]][place]];
                }
                //
                //
                //
                //
                //multiplication loop, uses the polynomial, Multiplication B
                for (int place = 0; place < places; place++) {
                    for (int term = 0; term < solution.polynomial[0].length; term++) {
                        if (solution.polynomial[places][term][place] == 0) continue;
                        Complex cterm = new Complex(solution.polynomial[places][term][place], 0);
                        for (int spot = 0; spot < places; spot++) {
                            for (int power = 0; power < solution.polynomial[place][term][spot]; power++) {
                                cterm = Complex.multiplyComplex(cterm, complexField[row - 1][column - places / 2 + spot]);
                            }
                        }
                        complexMults[place] = Complex.addComplex(complexMults[place], cterm);
                    }
                }
                Complex[] neighborhood = new Complex[places];
                //the permutation composition product, Multiplication D, is applied to the result neighborhood of Multiplication B to produce the vector
                for (int place = 0; place < places; place++) {
                    neighborhood[place] = complexMults[perms[solution.permGroupProductInverse][place]];
                    complexVectorField[place][row][column] = new Complex(complexMults[perms[solution.permGroupProductInverse][place]].real, complexMults[perms[solution.permGroupProductInverse][place]].imaginary);
                }
                complexField[row][column] = localNormalize(neighborhood);
                //this is the additive automata using the signs of the neighborhood's components to moderate the output
                Complex tots = new Complex(0, 0);
                for (int place = 0; place < places; place++) {
                    double a = 0;
                    double b = 0;
                    if ((complexField[row - 1][column - places / 2 + place].real) < 0) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    if (complexField[row - 1][column - places / 2 + place].imaginary < 0) {
                        b = 1;
                    } else {
                        b = 0;
                    }
                    tots.real += Math.pow(2, place) * a;
                    tots.imaginary += Math.pow(2, place) * b;
                }
                if (doAddititiveWolfram) {
                    if (wolframIsNegOne) {
                        if (solution.wolframCode[(int) tots.real] == 0) complexField[row][column].real *= -1;
                        if (solution.wolframCode[(int) tots.imaginary] == 0) complexField[row][column].imaginary *= -1;
                    } else {
                        if (solution.wolframCode[(int) tots.real] == 0) complexField[row][column].real = 0;
                        if (solution.wolframCode[(int) tots.imaginary] == 0) complexField[row][column].imaginary = 0;
                    }
                }
                //
                //
                //
                //
                //Multiplication C, does B's normalization before the multiplication, which is the same construction as the binary unit vectors from Multiplications A just applied to complex data
                Complex[] nFirstFactors = new Complex[solution.numFactors];
                Complex nFirstResult = new Complex();
                for (int factor = 0; factor < solution.numFactors; factor++) {
                    nFirstFactors[factor] = new Complex(0, 0);
                    for (int place = 0; place < places; place++) {
                        nFirstFactors[factor].real += Math.pow(2, place) * neighborhoodNormalizeFirst[row - 1][column - places / 2 + perms[permutationGroup[factor]][place]].real;
                        nFirstFactors[factor].imaginary += Math.pow(2, place) * neighborhoodNormalizeFirst[row - 1][column - places / 2 + perms[permutationGroup[factor]][place]].imaginary;
                    }
                }
                nFirstResult = nFirstFactors[0];
                for (int factor = 1; factor < numFactors; factor++) {
                    nFirstResult = Complex.multiplyComplex(nFirstResult, nFirstFactors[factor]);
                }
                //normalizations
                nFirstResult.real = Math.signum(nFirstResult.real) * Math.pow(Math.abs(nFirstResult.real), Math.pow(numFactors, -1));
                nFirstResult.imaginary = Math.signum(nFirstResult.imaginary) * Math.pow(Math.abs(nFirstResult.real), Math.pow(numFactors, -1));
                double radius = nFirstResult.radius();
                if (radius == 0 && avoidDivZero) radius = 1;
                if (normalizeUnit) {
                    nFirstResult.real = nFirstResult.real / radius;
                    nFirstResult.imaginary = nFirstResult.imaginary / radius;
                }
                neighborhoodNormalizeFirst[row][column] = nFirstResult;
                //additive automata operating on the negative signs of the components of the neighborhood, moderating the complex output
                tots = new Complex(0, 0);
                for (int place = 0; place < places; place++) {
                    double a = 0;
                    double b = 0;
                    if ((neighborhoodNormalizeFirst[row - 1][column - places / 2 + place].real) < 0) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    if (neighborhoodNormalizeFirst[row - 1][column - places / 2 + place].imaginary < 0) {
                        b = 1;
                    } else {
                        b = 0;
                    }
                    tots.real += Math.pow(2, place) * a;
                    tots.imaginary += Math.pow(2, place) * b;
                }
                if (doAddititiveWolfram) {
                    if (wolframIsNegOne) {
                        if (solution.wolframCode[(int) tots.real] == 0)
                            neighborhoodNormalizeFirst[row][column].real *= -1;
                        if (solution.wolframCode[(int) tots.imaginary] == 0)
                            neighborhoodNormalizeFirst[row][column].imaginary *= -1;
                    } else {
                        if (solution.wolframCode[(int) tots.real] == 0)
                            neighborhoodNormalizeFirst[row][column].real = 0;
                        if (solution.wolframCode[(int) tots.imaginary] == 0)
                            neighborhoodNormalizeFirst[row][column].imaginary = 0;
                    }
                }
            }
        }
        Complex[][] complexOut = new Complex[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                complexOut[row][column] = complexField[row][(gridSize / 2) - columns / 2 + column];
            }
        }
        return complexOut;
    }
    /**
     * Geometric mean of the neighborhood's columns, in Multiplications B normalization, this is acting on each unit vector of the vector produced, non-negative real numbers
     *
     * @param in         double to average
     * @param numFactors number of factors used
     * @return numFactors-th root of in
     */
    public double geometricMean(double in, int numFactors) {
        double out = Math.pow(in, 1.0 / (double) numFactors);
        return out;
    }
    /**
     * Result of neighborhood permutation multiplications, already factor-wise normalized by geometric mean,
     * here normalized neighborhood-wise this time, by the binary weighted sum of the neighborhood acting on non-negative real numbers
     *
     * @param in double array with length places,
     * @return a permuted ECA multiplication result normalized according to parameter useNorm
     */
    public double localNormalize(double[] in) {
        double out = 0;
        for (int spot = 0; spot < in.length; spot++) {
            out += (int) Math.pow(2, spot) * in[spot];
        }
        out = Math.pow(out, 1.0 / (double) in.length);
        return out;
    }
    /**
     * Normalizes the neighborhood of the result of permutation multiplications to a single output cell, with the option to normalize it to the unit circle
     *
     * @param in double array with length places,
     * @return a permuted ECA multiplication result normalized according to parameter useNorm
     */
    public Complex localNormalize(Complex[] in) {
        Complex out = new Complex(0, 0);
        for (int spot = 0; spot < in.length; spot++) {
            out.real += Math.pow(2, spot) * in[spot].real;
            out.imaginary += Math.pow(2, spot) * in[spot].imaginary;
        }
        out.real = Math.signum(out.real) * Math.pow(Math.abs(out.real), Math.pow(in.length, -1)) * Math.pow(in.length, in.length / 2);
        out.imaginary = Math.signum(out.imaginary) * Math.pow(Math.abs(out.imaginary), Math.pow(in.length, -1)) * Math.pow(in.length, in.length / 2);
        double radius = out.real * out.real + out.imaginary * out.imaginary;
        if (normalizeUnit) {
            radius = Math.sqrt(radius);
            if (radius == 0 && avoidDivZero) radius = 1;
            out.real = out.real / radius;
            out.imaginary = out.imaginary / radius;
        }
        return out;
    }
    /**
     * Generates the algebraic polynomial of the permuted neighborhood multiplications, polynomial[row=0..places-1][term][place] is the term's exponents and polynomial[places][term][row] is the row's term coefficient. [Update] I changed this to include permuting these by the inverse of the permutation composition product for the final result
     * <p>
     * Terms are lexicographically sorted in ascending order
     *
     * @param solution a ValidSolution from ECAasMultiplication
     * @return the polynomial of the permuted factor multiplication for a single neighborhood
     */
    public int[][][] generatePolynomial(ValidSolution solution) {
        int[] permutationGroup = solution.permutationGroup;
        int places = solution.numBits;
        int numFactors = solution.numFactors;
        int[][][] out = new int[places + 1][(int) Math.pow(places, numFactors - 1)][places];
        int[][] perms = pf.permSequences(places);
        int[][][][] intermediate = new int[numFactors][places][(int) Math.pow(places, numFactors - 1)][places];
        //initialize first terms
        for (int place = 0; place < places; place++) {
            intermediate[0][place][0][perms[permutationGroup[0]][place]] = 1;
        }
        //calculates each neighborhood column's terms and integer coefficients
        //the 2D partial product table becomes an N-D cube, N=numFactors with each axis being a permuted neighborhood
        //a 0 in the partial product table places the results of row*column*zee...numFactors into column 0 of the output, a 1 in the partial product table places the results of row*column*zee...numFactors into column 1 of the output, etc
        for (int factor = 1; factor < numFactors; factor++) {
            int lengthExisting = (int) Math.pow(places, factor - 1);
            int[] nextTerm = new int[places];
            for (int place = 0; place < places; place++) {
                nextTerm[place] = perms[permutationGroup[factor]][place];
            }
            int[] existingCounter = new int[places];
            for (int row = 0; row < places; row++) {
                for (int column = 0; column < places; column++) {
                    for (int existing = 0; existing < lengthExisting; existing++) {
                        for (int place = 0; place < places; place++) {
                            intermediate[factor][partialProductTable[row][column]][existingCounter[partialProductTable[row][column]] + existing][place] = intermediate[factor - 1][column][existing][place];
                        }
                        intermediate[factor][partialProductTable[row][column]][existingCounter[partialProductTable[row][column]] + existing][nextTerm[row]]++;
                    }
                    existingCounter[partialProductTable[row][column]] += lengthExisting;
                }
            }
        }
        //collects like terms and sorts in lexicographic order
        uniqueTerms = new int[places][1000][places];
        uniqueTermsTally = new int[places][1000];
        uniqueIndex = new int[places];
        for (int column = 0; column < places; column++) {
            termLoop:
            for (int term = 0; term < intermediate[numFactors - 1][column].length; term++) {
                for (int spot = 0; spot <= uniqueIndex[column]; spot++) {
                    if (Arrays.equals(intermediate[numFactors - 1][column][term], uniqueTerms[column][spot])) {
                        uniqueTermsTally[column][spot]++;
                        continue termLoop;
                    }
                }
                uniqueTerms[column][uniqueIndex[column]] = intermediate[numFactors - 1][column][term];
                uniqueTermsTally[column][uniqueIndex[column]]++;
                uniqueIndex[column]++;
            }
        }
        int[] totalTerms = new int[places];
        for (int column = 0; column < places; column++) {
            for (int term = 0; term < uniqueIndex[column]; term++) {
                totalTerms[column] += uniqueTermsTally[column][term];
            }
        }
        int[][][] sortedTerms = new int[places][(int) Math.pow(places, numFactors - 1)][places];
        for (int column = 0; column < places; column++) {
            for (int term = 0; term < uniqueIndex[column]; term++) {
                for (int spot = 0; spot < places; spot++) {
                    sortedTerms[column][term][spot] = uniqueTerms[column][term][spot];
                }
            }
        }
        int[][] sortedTally = new int[places][(int) Math.pow(places, numFactors - 1)];
        for (int column = 0; column < places; column++) {
            for (int term = 0; term < uniqueIndex[column]; term++) {
                sortedTally[column][term] = uniqueTermsTally[column][term];
            }
        }
        for (int column = 0; column < places; column++) {
            for (int term = 0; term < uniqueIndex[column]; term++) {
                for (int tterm = 0; tterm < uniqueIndex[column]; tterm++) {
                    if (term == tterm) continue;
                    boolean isLessThan = true;
                    checkLoop:
                    for (int spot = places - 1; spot >= 0; spot--) {
                        if (sortedTerms[column][term][spot] > sortedTerms[column][tterm][spot]) {
                            isLessThan = false;
                            break checkLoop;
                        } else if (sortedTerms[column][term][spot] == sortedTerms[column][tterm][spot]) {
                        } else if (sortedTerms[column][term][spot] < sortedTerms[column][tterm][spot]) {
                            isLessThan = true;
                            break checkLoop;
                        }
                    }
                    if (isLessThan) {
                        int a = sortedTally[column][term];
                        sortedTally[column][term] = sortedTally[column][tterm];
                        sortedTally[column][tterm] = a;
                        int[] temp = new int[places];
                        for (int place = 0; place < places; place++) {
                            temp[place] = sortedTerms[column][term][place];
                        }
                        for (int place = 0; place < places; place++) {
                            sortedTerms[column][term][place] = sortedTerms[column][tterm][place];
                        }
                        for (int place = 0; place < places; place++) {
                            sortedTerms[column][tterm][place] = temp[place];
                        }
                    }
                }
            }
        }
//        for (int column = 0; column < places; column++) {
//            for (int term = 0; term < (int) Math.pow(places, numFactors - 1); term++) {
//                for (int place = 0; place < places; place++) {
//                    out[column][term][place] = uniqueTerms[column][term][place];
//                }
//                out[places][term][column] = uniqueTermsTally[column][term];
//            }
//        }
        for (int column = 0; column < places; column++) {
            for (int term = 0; term < (int) Math.pow(places, numFactors - 1); term++) {
                for (int place = 0; place < places; place++) {
                    out[perms[solution.permGroupProductInverse][column]][term][place] = uniqueTerms[column][term][place];
                }
                out[places][term][perms[solution.permGroupProductInverse][column]] = uniqueTermsTally[column][term];
            }
        }
        return out;
    }
    /**
     * This function transforms the output of a standard ECA calculation into the constituent factors of a solution from ECApathPermutations
     *
     * @param solution a valid solution from ECAasMultiplications
     * @param rows     number of rows to output
     * @param columns  number of columns to output
     * @return int array[layers][row][column] where each layer of a cell is that cell's neighborhood permuted with the permutation set
     */
    public int[][][] subsectionFactorLayers(ValidSolution solution, int rows, int columns) {
        int[][] factors = solution.factors;
        int places = solution.numBits;
        int numFactors = solution.numFactors;
        int[][][] out = new int[numFactors + 1][rows][columns];
        //for each cell in the standard ECA calculation
        for (int row = 1; row < rows; row++) {
            for (int column = gridSize / 2 - columns / 2; column < gridSize / 2 + columns / 2; column++) {
                //the permutations were computed in ECApathPermutations, here you just look it up in the factor table
                int tot = 0;
                for (int place = 0; place < places; place++) {
                    tot += (int) Math.pow(2, place) * ruleField[row - 1][column - places / 2 + place];
                }
                //look up that truth table pointer in the factor set
                for (int factor = 0; factor <= numFactors; factor++) {
                    out[factor][row][column - gridSize / 2 + columns / 2] = factors[factor][tot];
                }
            }
        }
        return out;
    }
    /**
     * Generates the standard Wolfram code calculation with single bit initial input using the given truth table
     *
     * @param wolframCode binary array of length powers of 2
     * @param rows        number of rows to output
     * @param columns     number of columns to output
     * @return output of Wolfram code with single bit initial input;
     */
    public int[][] singleBitInput(int[] wolframCode, int rows, int columns) {
        int places = (int) (Math.log(wolframCode.length) / Math.log(2));
        int[][] field = new int[rows * 4][columns * 4];
        field[0][columns * 2] = 1;
        for (int row = 1; row < rows * 2; row++) {
            for (int column = places; column < columns * 4 - places - 1; column++) {
                for (int place = 0; place < places; place++) {
                    field[row][column] += (int) Math.pow(2, place) * field[row - 1][column - places / 2 + place];
                }
                field[row][column] = wolframCode[field[row][column]];
            }
        }
        int[][] outField = new int[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                outField[row][column] = field[row][2 * columns - columns / 2 + column];
            }
        }
        return outField;
    }
    /**
     * This function gets a subset of the output data calculated in validSolutionCoefficientCalculation(), non-negative real
     *
     * @param solution a solution from ECAMspecific
     * @param rows     number of rows to output
     * @param columns  number of columns to output
     * @return coefficient field produced by the polynomial rather than the function itself
     */
    public double[][] subsectionCoefficientMultiplication(ValidSolution solution, int rows, int columns) {
        int[][][] polynomial = solution.polynomial;
        int numFactors = solution.numFactors;
        ;
        double[][] coeffField = new double[rows][columns];
        int places = solution.numBits;
        for (int place = 0; place < places; place++) {
            //System.out.println(solution.polynomialString[place]);
        }
        for (int row = 1; row < rows; row++) {
            for (int column = gridSize / 2 - columns / 2; column < gridSize / 2 + columns / 2; column++) {
                if (ruleField[row][column] == 0) continue;
                double[] mult = new double[places];
                for (int place = 0; place < places; place++) {
                    double runningTotal = 0;
                    termLoop:
                    for (int term = 0; term < polynomial[place].length; term++) {
                        if (Arrays.equals(polynomial[place][term], new int[places])) break termLoop;
                        double termValue = polynomial[places][term][place];
                        for (int spot = 0; spot < places; spot++) {
                            for (int power = 0; power < polynomial[place][term][spot]; power++) {
                                termValue = termValue * field[row - 1][column - places / 2 + spot];
                            }
                        }
                        runningTotal += termValue;
                    }
                    mult[place] = geometricMean(runningTotal, numFactors);
                }
                coeffField[row][column - gridSize / 2 + columns / 2] = localNormalize(mult);
            }
        }
        return coeffField;
    }
    /**
     * Gets a rectangular subsection of the output from validSolutionCoefficientCalculation(), complex vector, each unit vector is a column in the neighborhood after multiplication just before normalization
     *
     * @param solution ValidSolution used
     * @param rows     number of rows to return
     * @param columns  number of columns to return
     * @return a rectangular subsection of the output from validSolutionCoefficientCalculations()
     */
    public Complex[][][] subsectionComplexVectorField(ValidSolution solution, int rows, int columns) {
        Complex[][][] out = new Complex[solution.numBits][rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                for (int place = 0; place < solution.numBits; place++) {
                    out[place][row][column] = complexVectorField[place][row][gridSize / 2 - columns / 2 + column];
                }
            }
        }
        return out;
    }
    /**
     * Gets a rectangular subsection of the output from validSolutionCoefficientCalculation(), complex
     *
     * @param solution ValidSolution used
     * @param rows     number of rows to return
     * @param columns  number of columns to return
     * @return a rectangular subsection of the output from validSolutionCoefficientCalculations()
     */
    public Complex[][] subsectionComplexField(ValidSolution solution, int rows, int columns) {
        Complex[][] out = new Complex[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                out[row][column] = complexField[row][gridSize / 2 - columns / 2 + column];
            }
        }
        return out;
    }
    /**
     * Outputs the given polynomial in String[] form, columnName[] is currently hard-coded but can be changed for other variable names
     *
     * @param polynomial array containing a polynomial from generatePolynomial()
     * @return String[] version of polynomial[][][]
     */
    public String[] polynomialAsStrings(int[][][] polynomial) {
        int places = polynomial.length - 1;
        int length = polynomial[0].length;
        int[][][] terms = new int[places][length][places];
        int[][] coefficients = new int[places][length];
        for (int row = 0; row < places; row++) {
            for (int term = 0; term < length; term++) {
                terms[row][term] = polynomial[row][term];
                coefficients[row][term] = polynomial[places][term][row];
            }
        }
        String[] columnName = new String[]{"a", "b", "c", "d", "e", "f"};
        String[] out = new String[places];
        for (int row = 0; row < places; row++) {
            out[row] = "";
        }
        for (int row = 0; row < places; row++) {
            for (int term = 0; term < length; term++) {
                out[row] += (coefficients[row][term] + "*(");
                for (int place = 0; place < places; place++) {
                    out[row] += ("(" + columnName[place] + "^" + terms[row][term][place] + ")");
                    if (place != places - 1) {
                        out[row] += ("*");
                    }
                }
                out[row] += ")";
                if (term == length - 1) break;
                if (coefficients[row][term + 1] != 0) {
                    out[row] += (" + ");
                } else {
                    break;
                }
            }
        }
        //this section does the same output as above, except without the parentheses and asterisks
//        for (int row = 0; row < places; row++) {
//            for (int term = 0; term < length; term++) {
//                out[row] += (coefficients[row][term]+" "  );
//                for (int place = 0; place < places; place++) {
//                    out[row] += columnName[place] + "^" + terms[row][term][place] + " ";
//                    if (place != places - 1) {
//                        //out[row] += ("*");
//                    }
//                }
//                //out[row] += ")";
//                if (term == length - 1) break;
//                if (coefficients[row][term + 1] != 0) {
//                    out[row] += (" + ");
//                } else {
//                    break;
//                }
//            }
//        }
        //System.out.println("Polynomial for each cell of a neighborhood");
        for (int row = 0; row < places; row++) {
            //System.out.println(out[row]);
        }
        return out;
    }
    /**
     * Generates random binary data for multiplicativeSolutionOutput, this is used in parallel to the non-negative real version
     *
     * @return a binary array of width widthRandomInput
     */
    public int[] randomBinaryInput() {
        Random rand = new Random();
        int[] out = new int[widthOfRandomInput];
        for (int spot = 0; spot < widthOfRandomInput; spot++) {
            out[spot] = rand.nextInt(0, 2);
        }
        return out;
    }
    /**
     * Generates random complex row 0 data for multiplicativeSolutionOutput()
     *
     * @param bottomRange lower range of random input
     * @param topRange    upper range of random input
     * @return an array of random complex numbers
     */
    public Complex[] randomComplexInput(double bottomRange, double topRange) {
        Random rand = new Random();
        Complex[] out = new Complex[widthOfRandomInput];
        for (int spot = 0; spot < widthOfRandomInput; spot++) {
            out[spot] = new Complex(rand.nextDouble(bottomRange, topRange), rand.nextDouble(bottomRange, topRange));
        }
        return out;
    }
    /**
     * Generates random double data for multiplicativeSolutionOutput, non-negative real
     *
     * @param randRange    lower bound of random input
     * @param randRangeMax upper bound of random input
     * @return a random double array
     */
    public double[] randomDoubleInput(int randRange, int randRangeMax) {
        Random rand = new Random();
        double[] out = new double[widthOfRandomInput];
        for (int spot = 0; spot < widthOfRandomInput; spot++) {
            out[spot] = rand.nextDouble(randRange, randRangeMax);
        }
        return out;
    }
    /**
     * Returns a subsection of the vector field calculated in validSolutionCoefficientCalculation(), it is the neighborhood polynomial results just prior to the neighborhood normalization and just after the geometric mean
     *
     * @param solution a valid solution from ecam.specific
     * @param rows     number of rows to return
     * @param columns  number of columns to return
     * @return double[numBits][rows][columns], each layer is the coefficient of that layer's unit vector, of dimension numBits
     */
    public double[][][] subsectionVectorField(ValidSolution solution, int rows, int columns) {
        double[][][] out = new double[solution.numBits][rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = gridSize / 2 - columns / 2; column < gridSize / 2 + columns / 2; column++) {
                for (int layer = 0; layer < solution.numBits; layer++) {
                    out[layer][row][column - gridSize / 2 + columns / 2] = vectorField[layer][row][column];
                }
            }
        }
        return out;
    }
    /**
     * Returns a subsection of the vector field calculated in validSolutionCoefficientCalculation(), it is the neighborhood polynomial results just prior to the neighborhood normalization and just after the geometric mean
     *
     * @param solution a valid solution from ecam.specific
     * @param rows     number of rows to return
     * @param columns  number of columns to return
     * @return double[numBits][rows][columns], each layer is the coefficient of that layer's unit vector, of dimension numBits
     */
    public Complex[][] subsectionNeighborhoodFirst(ValidSolution solution, int rows, int columns) {
        Complex[][] out = new Complex[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = gridSize / 2 - columns / 2; column < gridSize / 2 + columns / 2; column++) {
                out[row][column - gridSize / 2 + columns / 2] = neighborhoodNormalizeFirst[row][column];
            }
        }
        return out;
    }
    /**
     * Takes the complex output of a solution, if the real and imaginary parts are negative, they become 1's in the output, if positive, 0.
     * The 0 index layer is the real part and the 1 index layer is the imaginary part
     *
     * @param inField Complex output of a solution
     * @return two layer binary array with the sign values of the real and complex parts as 1s and 0s
     */
    public int[][][] subsectionComplexFieldToBinary(Complex[][] inField) {
        int[][][] out = new int[2][inField.length][inField[0].length];
        for (int row = 0; row < inField.length; row++) {
            for (int column = 0; column < inField.length; column++) {
                if (inField[row][column].real < 0) {
                    out[0][row][column] = 1;
                } else {
                    out[0][row][column] = 0;
                }
                if (inField[row][column].imaginary < 0) {
                    out[1][row][column] = 1;
                } else {
                    out[1][row][column] = 0;
                }
            }
        }
        return out;
    }
}

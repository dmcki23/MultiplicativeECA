import java.util.Arrays;
/**
 * Subsections of solutions' output from ECAMpostProcessing, trimmed to size for display
 */
public class ECAMoutputSubsections {
    /**
     * Processes and outputs ValidSolution (s) from ECAspecific
     */
    ECAMpostProcessing post;
    /**
     * Initializes manager class to the local version
     * @param inPost manager class
     */
    public ECAMoutputSubsections(ECAMpostProcessing inPost){
        post = inPost;

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
            for (int column = post.gridSize / 2 - columns / 2; column < post.gridSize / 2 + columns / 2; column++) {
                //the permutations were computed in ECApathPermutations, here you just look it up in the factor table
                int tot = 0;
                for (int place = 0; place < places; place++) {
                    tot += (int) Math.pow(2, place) * post.ruleField[row - 1][column - places / 2 + place];
                }
                //look up that truth table pointer in the factor set
                for (int factor = 0; factor <= numFactors; factor++) {
                    out[factor][row][column - post.gridSize / 2 + columns / 2] = factors[factor][tot];
                }
            }
        }
        return out;
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
            for (int column = post.gridSize / 2 - columns / 2; column < post.gridSize / 2 + columns / 2; column++) {
                if (post.ruleField[row][column] == 0) continue;
                double[] mult = new double[places];
                for (int place = 0; place < places; place++) {
                    double runningTotal = 0;
                    termLoop:
                    for (int term = 0; term < polynomial[place].length; term++) {
                        if (Arrays.equals(polynomial[place][term], new int[places])) break termLoop;
                        double termValue = polynomial[places][term][place];
                        for (int spot = 0; spot < places; spot++) {
                            for (int power = 0; power < polynomial[place][term][spot]; power++) {
                                termValue = termValue * post.field[row - 1][column - places / 2 + spot];
                            }
                        }
                        runningTotal += termValue;
                    }
                    mult[place] = post.geometricMean(runningTotal, numFactors);
                }
                coeffField[row][column - post.gridSize / 2 + columns / 2] = post.localNormalize(mult);
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
                    out[place][row][column] = post.complexVectorField[place][row][post.gridSize / 2 - columns / 2 + column];
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
                out[row][column] = post.complexField[row][post.gridSize / 2 - columns / 2 + column];
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
    public double[][][] subsectionVectorField(ValidSolution solution, int rows, int columns) {
        double[][][] out = new double[solution.numBits][rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = post.gridSize / 2 - columns / 2; column < post.gridSize / 2 + columns / 2; column++) {
                for (int layer = 0; layer < solution.numBits; layer++) {
                    out[layer][row][column - post.gridSize / 2 + columns / 2] = post.vectorField[layer][row][column];
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
            for (int column = post.gridSize / 2 - columns / 2; column < post.gridSize / 2 + columns / 2; column++) {
                out[row][column - post.gridSize / 2 + columns / 2] = post.neighborhoodNormalizeFirst[row][column];
            }
        }
        return out;
    }
    /**
     * Takes the complex output of a solution, if the real and imaginary parts are negative, they become 1's in the output, if positive, 0.
     * The 0 index layer is the real part and the 1 index layer is the imaginary part
     *
     * @param rows number of rows of subsection of complexField[rows][post.gridSize]
     * @param columns number of columns of subsection of complexField[rows][post.gridSize]
     * @return two layer binary array with the sign values of the real and complex parts as 1s and 0s
     */
    public int[][][] subsectionComplexFieldToBinary(int rows, int columns) {
        int[][][] out = new int[2][rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = post.gridSize/2-columns/2; column < post.gridSize/2+columns/2; column++) {
                if (post.complexField[row][column].real < 0) {
                    out[0][row][column-post.gridSize/2+columns/2] = 1;
                } else {
                    out[0][row][column-post.gridSize/2+columns/2] = 0;
                }
                if (post.complexField[row][column].imaginary < 0) {
                    out[1][row][column-post.gridSize/2+columns/2] = 1;
                } else {
                    out[1][row][column-post.gridSize/2+columns/2] = 0;
                }
            }
        }
        return out;
    }
    /**
     * Takes the complex output of a solution, if the real and imaginary parts are negative, they become 1's in the output, if positive, 0.
     * The 0 index layer is the real part and the 1 index layer is the imaginary part
     * <p>
     *     This function along with its display class, SwingBinaryShadow, are experimental, and show that the binary shadow of the algorithm is not the original Wolfram code
     * </p>
     * @param solution a ValidSolution from ECAMspecific
     * @param rows number of rows of subsection of complexField[rows][post.gridSize]
     * @param columns number of columns of subsection of complexField[rows][post.gridSize]
     * @return two layer binary array with the sign values of the real and complex parts as 1s and 0s
     */
    public int[][][] subsectionComplexFieldToBinary(ValidSolution solution, int rows, int columns) {
        int[][][] out = new int[2][rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = post.gridSize/2-columns/2; column < post.gridSize/2+columns/2; column++) {
                if (post.complexField[row][column].real < 0) {
                    out[0][row][column-post.gridSize/2+columns/2] = 1;
                } else {
                    out[0][row][column-post.gridSize/2+columns/2] = 0;
                }
                if (post.complexField[row][column].imaginary < 0) {
                    out[1][row][column-post.gridSize/2+columns/2] = 1;
                } else {
                    out[1][row][column-post.gridSize/2+columns/2] = 0;
                }
            }
        }

        int[] attemptedWolfram = new int[(int) Math.pow(2, solution.numBits*2)];
        for (int spot = 0; spot < attemptedWolfram.length; spot++) {
            attemptedWolfram[spot] = -1;
        }
        int totWolframConflicts = 0;
        for (int row = 2; row < 75; row++) {
            for (int column = solution.numBits+row; column < columns-solution.numBits-row; column++) {
                int tot = 0;
                for (int spot = 0; spot < solution.numBits; spot++) {
                    tot += (int) Math.pow(2, spot) * out[0][row - 1][column - solution.numBits / 2 + spot];
                    tot += (int)Math.pow(2,spot+solution.numBits)*out[1][row-1][column-solution.numBits/2+spot];
                }
                if (attemptedWolfram[tot] == -1) {
                    attemptedWolfram[tot] = out[0][row][column]+2*out[1][row][column];
                } else if (attemptedWolfram[tot] == (out[0][row][column]+2*out[1][row][column])) {

                } else if (attemptedWolfram[tot] != -1 && attemptedWolfram[tot] != (out[0][row][column]+2*out[1][row][column])) {
                    totWolframConflicts++;
                }
            }
        }
        System.out.println("Total attempted Wolfram code conflicts: " + totWolframConflicts);
        System.out.println(Arrays.toString(attemptedWolfram));
        return out;
    }
}

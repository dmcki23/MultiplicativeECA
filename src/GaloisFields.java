import java.util.Arrays;
/**
 * Generates discrete Galois multiplication and addition tables
 */
public class GaloisFields {
    /**
     * Currently empty constructor
     */
    GaloisFields() {
    }
    /**
     * Galois fields of base p, power m
     *
     * @param p base of Galois field, must be prime to work correctly
     * @param m exponent of Galois field base, can be any integer > 1
     * @return discrete Galois Field (p,m)
     */
    public int[][] galoisFieldMultiplication(int p, int m) {
        int gridSize = (int) Math.pow(p, m);
        int[][] outMultiplication = new int[gridSize][gridSize];
        int[] outPoly = new int[m];
        int[] rowPoly = new int[m];
        int[] columnPoly = new int[m];
        int[] subPoly = new int[2];
        for (int row = 0; row < gridSize; row++) {
            for (int spot = 0; spot < m; spot++) {
                rowPoly[spot] = row / (int) Math.pow(p, spot) % p;
            }
            for (int column = 0; column < gridSize; column++) {
                for (int spot = 0; spot < m; spot++) {
                    columnPoly[spot] = column / (int) Math.pow(p, spot) % p;
                }
                outPoly = new int[m];
                int[][] convolution = new int[m][m];
                for (int spot = 0; spot < m; spot++) {
                    for (int sspot = 0; sspot < m; sspot++) {
                        convolution[spot][sspot] = rowPoly[spot] * columnPoly[sspot];
                    }
                }
                for (int spot = 0; spot < m; spot++) {
                    for (int sspot = 0; sspot < m; sspot++) {
                        if (spot + sspot > m - 1) {
                            subPoly = new int[]{0, 1};
                            for (int sssspot = 0; sssspot < (spot + sspot) - (m) - 1; sssspot++) {
                                subPoly[0]++;
                                subPoly[1]++;
                            }
                            outPoly[subPoly[0]] += convolution[spot][sspot];
                            outPoly[subPoly[1]] += convolution[spot][sspot];
                        } else {
                            outPoly[spot + sspot] += convolution[spot][sspot];
                        }
                    }
                }
                int out = 0;
                for (int spot = 0; spot < m; spot++) {
                    outPoly[spot] = outPoly[spot] % p;
                    out += (int) Math.pow(p, spot) * outPoly[spot];
                }
                outMultiplication[row][column] = out % gridSize;
            }
        }
        return outMultiplication;
    }

    /**
     * Galois addition table, equivalent to (row xor column), also represents complex multiplication
     *
     * @param gridSize can be a prime, or a prime power
     * @return Galois addition table
     */
    public int[][] galoisFieldAddition(int gridSize) {
        int[][] outAddition = new int[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                outAddition[row][column] = (row + column) % gridSize;
            }
        }
        return outAddition;
    }
    /**
     * Wrapper function that produces a table used to sum the multiplications of the coefficients of a solution from ECApathPermutations
     * @param p must be prime to work correctly
     * @param m power of p, p^m
     * @param shifted shifts the table to exclude the zeros of a Galois Field, shifting by one row up and one column left and subtracting one
     * @return an integer array that is used to sum the results of the multiplications
     */
    public int[][] generateTable(int p, int m, boolean shifted) {
        int gridSize = (int) Math.pow(p, m);
        if (shifted) gridSize--;
        int[][] out = new int[gridSize][gridSize];
        int[][] temp;
        if (m == 1) {
            temp = galoisFieldMultiplication(p,1);
            if (shifted) {
                out = new int[p - 1][p - 1];
                for (int row = 0; row < out.length; row++) {
                    for (int column = 0; column < out.length; column++) {
                        out[row][column] = temp[row + 1][column + 1] - 1;
                    }
                }
            } else {
                out = galoisFieldMultiplication(p,1);
            }
        } else {
            temp = galoisFieldMultiplication(p, m);
            gridSize = (int) Math.pow(p, m);
            if (shifted) {
                gridSize--;
                out = new int[gridSize][gridSize];
                for (int row = 0; row < gridSize; row++) {
                    for (int column = 0; column < gridSize; column++) {
                        out[row][column] = temp[row + 1][column + 1] - 1;
                    }
                }
            } else {
                out = temp;
            }
        }
        return out;
    }
    /**
     * Checks how many factors are required to produce an identity array with Galois multiplication tables. Hypercomplex tables
     */
    public void numFactorsRequiredForIdentity(){
        int[] primes = new int[]{2,3,5,7,11,13,17};
        for (int prime = 0; prime < primes.length; prime++){
            for (int power = 1; power < 4; power++){
                for (int factors = 1; factors < 20; factors++){
                    checkNumFactorIdentities(primes[prime],power,factors);
                }
            }
        }
    }
    /**
     * Util class of numFactorsRequiredForIdentity()
     * @param p prime number
     * @param m exponent
     * @param numFactors number of factors to consider
     */
    public void checkNumFactorIdentities(int p, int m, int numFactors){
        int[][] table =generateTable(p,m,false);
        int size = table.length;
        int[] out = new int[size];
        for (int neighborhood = 0; neighborhood < size; neighborhood++){
            int mainFactor = neighborhood;
            for (int factor = 1; factor <= numFactors; factor++){
                mainFactor = table[mainFactor][neighborhood];
            }
            out[neighborhood] = mainFactor;
        }
        boolean isIdentity = true;
        for (int spot = 0; spot < size; spot++){
            if (out[spot] != spot){
                isIdentity = false;
            }
        }
        if (isIdentity) {
            System.out.println("p: " + p + " m: " + m + " numFactors: " + numFactors);
            System.out.println(Arrays.toString(out));
        }
    }
}






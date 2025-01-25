import java.util.Arrays;

public class GaloisStaging {
    int numPrimePolys;

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
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                outMultiplication[row][column] = (row * column) % p;
            }
        }
        return outMultiplication;
    }

    /**
     * Finds all the irreducible polynomials of degree m for prime p
     *
     * @param p any prime number
     * @param m power of the prime
     * @return
     */
    public int[][] factor(int p, int m) {
        numPrimePolys = 0;
        int iLength = (int) Math.pow(p, m);
        int length = (int) Math.pow(p, m + 1);
        int[][] out = new int[length][iLength];
        int[] poly = new int[2 * m];
        int[] rowPoly = new int[2 * m];
        int[] columnPoly = new int[2 * m];
        int outIndex = 0;
        int startIndex = (int) Math.pow(p, m);
        System.out.println(startIndex + " " + length);
        for (int eq = startIndex; eq < length; eq++) {
            for (int power = 0; power < 2 * m; power++) {
                poly[power] = ((eq / (int) Math.pow(p, power)) % p);
            }
            int numFactors = 0;
            //System.out.println(eq + " " + Arrays.toString(poly));
            for (int row = 2; row < length; row++) {
                for (int power = 0; power < m; power++) {
                    rowPoly[power] = ((row / (int) Math.pow(p, power)) % p);
                }
                for (int column = 2; column < length; column++) {
                    for (int power = 0; power < m; power++) {
                        columnPoly[power] = ((column / (int) Math.pow(p, power)) % p);
                    }
                    int[] partial = new int[2 * m];
                    for (int subRow = 0; subRow < m; subRow++) {
                        for (int subColumn = 0; subColumn < m; subColumn++) {
                            partial[subRow + subColumn] += (rowPoly[subRow] * columnPoly[subColumn]) % p;
                        }
                    }
                    for (int power = 0; power < 2 * m; power++) {
                        partial[power] %= p;
                    }
                    boolean areFactors = true;
                    for (int power = 0; power < 2 * m; power++) {
                        if (partial[power] != poly[power]) {
                            areFactors = false;
                            break;
                        }
                    }
                    if (areFactors) {
                        //System.out.println(Arrays.toString(rowPoly) + " x " + Arrays.toString(columnPoly) + " = " + Arrays.toString(partial));
                        numFactors++;
                    }
                }
            }
            if (numFactors == 0) {
                System.out.println("No factors found " + Arrays.toString(poly));
                out[outIndex] = Arrays.copyOf(poly, poly.length);
                outIndex++;
            }
            System.out.println();
        }
        numPrimePolys = outIndex;
        return out;
    }

    /**
     * Checks to see if two arrays are orthogonal
     *
     * @param a integer array
     * @param b integer array
     * @return true if they are orthogonal, false if not
     */
    public boolean mutuallyOrthogonal(int[][] a, int[][] b) {
        int size = a.length;
        if (!CustomArray.isLatin(a) || !CustomArray.isLatin(b)) {
            System.out.println("not latin");
            //return false;
        }
        int[][] ab = new int[size][size];
        int[] tally = new int[size * size];
        int[][] errors = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int tot = size * a[row][col] + b[row][col];
                tally[tot]++;
                ab[row][col] = tot;
                if (tally[tot] > 1) {
                    //System.out.println("duplicates");
                    //return false;
                }
            }
        }
        CustomArray.plusArrayDisplay(ab, false, false, "magic square?");
        boolean error = true;
        for (int spot = 0; spot < size * size; spot++) {
            if (tally[spot] == 0 || tally[spot] > 1) {
                //System.out.println("holes " + tally[spot]);
                errors[spot % size][spot / size] = tally[spot];
                error = false;
            }
        }
        CustomArray.plusArrayDisplay(errors, false, false, "errors");
        return error;
    }

    /**
     * Checks whether a set of integer arrays is mutually orthogonal
     *
     * @param cube a set of integer arrays in the form of a cube
     * @return whether the set of arrays is mutually orthogonal
     */
    public boolean mols(int[][][] cube) {
        int size = cube.length;
        int totValid = 0;
        int[][] a = new int[size][size];
        int[][] b = new int[size][size];
        for (int abc = 0; abc < size; abc++) {
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int[] coords = new int[]{row, col, abc};
                    int tot = 0;
                    for (int power = 0; power < 3; power++) {
                        tot += (int) Math.pow(size, power) * coords[power];
                    }
                    a[row][col] = cube[row][col][abc];
                }
            }
            for (int def = 0; def < size; def++) {
                if (abc == def) continue;
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        int[] coords = new int[]{row, col, def};
                        int tot = 0;
                        for (int power = 0; power < 3; power++) {
                            tot += (int) Math.pow(size, power) * coords[power];
                        }
                        b[row][col] = cube[row][col][def];
                    }
                }
                CustomArray.plusArrayDisplay(a, false, false, "a");
                CustomArray.plusArrayDisplay(b, false, false, "b");
                if (!mutuallyOrthogonal(a, b)) {
                    ;
                    //System.out.println("not mutually orthogonal");
                    //return false;
                } else {
                    System.out.println("MOLS!!!");
                    totValid++;
                }
            }
        }
        System.out.println("totValid = " + totValid);
        return true;
    }

    /**
     * Galois fields of base p, power m
     *
     * @param p    base of Galois field, must be prime to work correctly
     * @param m    exponent of Galois field base, can be any integer > 1
     * @param poly an irreducible polynomial of Zp, degree m+1
     * @return discrete Galois Field (p,m)
     */
    public int[][] galoisFieldMultiplication(int p, int m, int[] poly) {
        int gridSize = (int) Math.pow(p, m);
        int[][] outMultiplication = new int[gridSize][gridSize];
        int[] outPoly = new int[m];
        int[] rowPoly = new int[m];
        int[] columnPoly = new int[m];
        for (int row = 0; row < gridSize; row++) {
            for (int spot = 0; spot < m; spot++) {
                rowPoly[spot] = (row / (int) Math.pow(p, spot)) % p;
            }
            for (int column = 0; column < gridSize; column++) {
                for (int spot = 0; spot < m; spot++) {
                    columnPoly[spot] = (column / (int) Math.pow(p, spot)) % p;
                }
                outPoly = multiplyAndMod(p, m, rowPoly, columnPoly, poly);
                for (int power = 0; power < m; power++) {
                    outMultiplication[row][column] += (int) Math.pow(p, power) * (outPoly[power] % p);
                }
            }
        }
        int[][] temp = new int[gridSize - 1][gridSize - 1];
        for (int row = 0; row < gridSize - 1; row++) {
            for (int col = 0; col < gridSize - 1; col++) {
                temp[row][col] = outMultiplication[row + 1][col + 1] - 1;
            }
        }
        System.out.println(CustomArray.isLatin(temp));
        return outMultiplication;
    }

    /**
     * Multiplies two values in a finite field
     *
     * @param p          a prime number
     * @param m          power of the prime
     * @param rowPoly    first multiplicand polynomial, in integer array form
     * @param columnPoly second multiplicand polynomial, also in integer array form
     * @param inPoly     an irreducible polynomial
     * @return the results of multiplying the two polynomials mod inPoly
     */
    public int[] multiplyAndMod(int p, int m, int[] rowPoly, int[] columnPoly, int[] inPoly) {
        int[] partial = new int[2 * m];
        for (int row = 0; row < m; row++) {
            for (int column = 0; column < m; column++) {
                partial[row + column] += (rowPoly[row] * columnPoly[column]) % p;
            }
        }
        for (int power = 0; power < m; power++) {
            partial[power] %= p;
        }
        return polyMod(p, m, partial, inPoly);
    }

    /**
     * Subroutine of multiplyAndMod(), does the mod part
     *
     * @param p        a prime number
     * @param m        power of the prime
     * @param dividend multiplication results pre-mod
     * @param divisor  an irreducible polynomial of appropriate degree
     * @return the dividend mod divisor
     */
    public int[] polyMod(int p, int m, int[] dividend, int[] divisor) {
        int[] firstActive = new int[2 * m];
        int[] active = new int[2 * m];
        int[] secondActive = new int[2 * m];
        for (int place = 0; place < 2 * m; place++) {
            active[place] = dividend[place];
        }
        for (int place = 2 * m - 1; place >= m; place--) {
            if (active[place] == 0) continue;
            int power = place - m;
            if (power < 0) continue;
            int digit = divisor[m] / active[place];
            firstActive = new int[2 * m];
            for (int row = 0; row < 2 * m && (row + power) < 2 * m; row++) {
                firstActive[row + power] = divisor[row] * digit;
            }
            secondActive = new int[2 * m];
            for (int row = 0; row < 2 * m; row++) {
                secondActive[row] = (active[row] - firstActive[row] + p) % p;
            }
            for (int row = 0; row < 2 * m; row++) {
                active[row] = secondActive[row];
            }
        }
        for (int place = 0; place < m; place++) {
            active[place] = (active[place] + p) % p;
        }
        return active;
    }

    /**
     * Galois addition (with p = 2, is XOR)
     *
     * @param p a prime number
     * @param m power of the prime
     * @return the Galois addition table
     */
    public int[][] galoisAddition(int p, int m) {
        int length = (int) Math.pow(p, m);
        int[][] table = new int[length][length];
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                int[] rp = new int[m];
                int[] cp = new int[m];
                int[] op = new int[m];
                for (int power = 0; power < m; power++) {
                    rp[power] = ((row / (int) Math.pow(p, power)) % p);
                    cp[power] = ((col / (int) Math.pow(p, power)) % p);
                    op[power] = rp[power] + cp[power];
                    op[power] %= p;
                    table[row][col] += (int) Math.pow(p, power) * op[power];
                }
            }
        }
        return table;
    }
}

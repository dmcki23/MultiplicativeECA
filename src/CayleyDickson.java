
import java.util.Arrays;
import java.util.Random;
/**
 * Used to multiply hypercomplex numbers using the Cayley-Dickson algorithm, as well as compare Cayley-Dickson solutions
 */
public class CayleyDickson {
    /**
     * Initializes some binary arrays, otherwise empty
     */
    public CayleyDickson() {
        for (int n = 0; n < 512; n++) {
            int temp = n;
            for (int power = 0; power < 9; power++) {
                numasbin[n][power] = temp % 2;
                temp = temp - (temp % 2);
                temp = temp / 2;
            }
        }
        for (int power = 0; power < 16; power++) {
            binPowers[power] = (int) Math.pow(2, power);
        }
        //preCalculateTables();
    }
    /**
     * General permutations and factoradic class
     */
    public PermutationsFactoradic pf = new PermutationsFactoradic();
    /**
     * Integers as a binary array
     */
    public int[][] numasbin = new int[512][9];
    /**
     * Powers of two in array form, for performance rather than a Math.pow library call
     */
    public int[] binPowers = new int[16];
    /**
     * Wrapper function used to initiate the recursion when multiplying hypercomplex numbers. Initializes the factoradics.
     *
     * @param rootDegree hypercomplex degree, 2 = quaternions, 3 = octonions, 4 = sedonions, etc
     * @param row        first hypercomplex unit vector factor
     * @param column     second hypercomplex unit vector factor
     * @param cdz        Cayley-Dickson permutation factoradic number, used in recursion on the way down
     * @param cdo        Cayley-Dickson permutation factoradic number, used in recursion on the way up
     * @return returns the result of multiplying two hypercomplex numbers row, column, with CD solution, cdz, cdo
     */
    public int multiplyUnitHyperComplex(int rootDegree, int row, int column, int cdz, int cdo) {
//        int[][] startRemainders = pf.permRemainders(rootDegree);
//        int[] cdzRemainders = startRemainders[cdz].clone();
//        int[] cdoRemainders = startRemainders[cdo].clone();
        int[][] startRemainders;
        int[] cdzRemainders;
        int[] cdoRemainders;
        if (rootDegree > 8){
            cdzRemainders = pf.specificFactoradic(rootDegree,cdz);
            cdoRemainders = pf.specificFactoradic(rootDegree,cdo);
        } else {
            startRemainders = pf.permRemainders(rootDegree);
            cdzRemainders = startRemainders[cdz].clone();
            cdoRemainders = startRemainders[cdo].clone();
        }
        return multRecursive(rootDegree, 0, row, column, cdzRemainders, cdoRemainders);
    }
    /**
     * The recursive function used to multiply two hypercomplex numbers
     *
     * @param rootDegree    degree hypercomplex, 2 = quaternions, 3 = octonions, 4 = sedonions, etc
     * @param depth         current depth of recursion
     * @param row           first factor to be multiplied
     * @param column        second factor to be multiplied
     * @param cdzRemainders the integer array of the factoradic of the permutation used on the way down in recursion
     * @param cdoRemainders the integer array of the factoradic of the permutation used on the way back up in recursion
     * @return internal recursion result, returns the next step in rebuilding the hypercomplex number after having split it on the way down
     */
    public int multRecursive(int rootDegree, int depth, int row, int column, int[] cdzRemainders, int[] cdoRemainders) {
        int hl = (int) Math.pow(2, rootDegree - depth);
        int g = 0;
        //adjusts the negative sign according to quadrants in the multiplication table
        int rowBack = 0;
        int colBack = 0;
        if (row >= hl) {
            row -= hl;
            rowBack++;
        }
        if (column >= hl) {
            column -= hl;
            colBack++;
        }
        //ends the recursion and sends it back up
        if (rootDegree - depth == 0) {
            return row ^ column;
        }
        //This loop chooses the bit to split the n-ion
        int otherBit = cdoRemainders[depth];
        int bit = cdzRemainders[depth];
        //
        //
        //
        //This section breaks (X,Y) down into (a,b),(c,d)
        //and applies the proper conjugate
        //It pops a bit out of each factor, recurses, and pops it back in later
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        if ((row / (int) Math.pow(2, bit) % 2) == 1) {
            a = -1;
            b = pf.removeBit(row, bit);
        } else {
            b = -1;
            a = pf.removeBit(row, bit);
        }
        if ((column / (int) Math.pow(2, bit) % 2) == 1) {
            c = -1;
            d = pf.removeBit(column, bit);
        } else {
            d = -1;
            c = pf.removeBit(column, bit);
        }
        //
        //
        //
        //This section is the multiplication. Because we are using only the unit vectors in this implementation,
        //(a,b)x(c,d)=(ac-d*b,da+bc*)
        //there is only one multiplication result out of the four possibilities that can occur, ac, d*b, da, bc*
        //computes this multiplication and pops the bit back in from earlier according to the factoradic given
        if (a == -1 && c == -1) {
            if (d != 0 && d != hl / 2) {
                d += hl / 2;
                d = d % hl;
            }
            g = multRecursive(rootDegree, depth + 1, d, b, cdzRemainders, cdoRemainders);
            g = ((g + (hl / 2)) % hl);
            g = pf.insertBit(g, otherBit, 0);
        } else if (a == -1 && d == -1) {
            if (c != 0 && c != hl / 2) {
                c += hl / 2;
            }
            g = multRecursive(rootDegree, depth + 1, b, c, cdzRemainders, cdoRemainders);
            g = pf.insertBit(g, otherBit, 1);
        } else if (b == -1 && d == -1) {
            g = multRecursive(rootDegree, depth + 1, a, c, cdzRemainders, cdoRemainders);
            g = pf.insertBit(g, otherBit, 0);
        } else if (b == -1 && c == -1) {
            g = multRecursive(rootDegree, depth + 1, d, a, cdzRemainders, cdoRemainders);
            g = pf.insertBit(g, otherBit, 1);
        }
        if (rowBack == 1) {
            g = (g + hl) % (hl * 2);
        }
        if (colBack == 1) {
            g = (g + hl) % (hl * 2);
        }
        return g;
    }
    /**
     * Returns a specific hypercomplex multiplication table
     *
     * @param degree hypercomplex degree, 2 = quaternions, 3 = octonions, 4 = sedonions
     * @param cdz    Cayley-Dickson permutation factoradic number, used in recursion on the way down
     * @param cdo    Cayley-Dickson permutation factoradic number, used in recursion on the way up
     * @return a hypercomplex multiplication table with the degree and permutation set given
     */
    public int[][] specificTable(int degree, int cdz, int cdo) {
        int[][] out = new int[(int) Math.pow(2, degree + 1)][(int) Math.pow(2, degree + 1)];
        for (int row = 0; row < out[0].length; row++) {
            for (int column = 0; column < out[0].length; column++) {
                out[row][column] = multiplyUnitHyperComplex(degree, row, column, cdz, cdo);
            }
        }
        return out;
    }
    /**
     * Compares Cayley-Dickson solution hypercomplex multiplication tables, (cdz,cdo),(cdzz,cdoo),
     * results in (cdz,cdzz) and (cdo,cdoo) each independently interacting in the same way as permutations of permutations
     * <p>
     * This version checks all possible solutions
     *
     * @param degree hypercomplex degree number, quaternions = 2, octonions = 3, sedonions = 4
     * @return a 2d int array of the interactions
     */
    public int[][] cdCompare(int degree) {
        int[][] out = new int[pf.factorial(degree) * pf.factorial(degree)][pf.factorial(degree) * pf.factorial(degree)];
        int degreeFactorial = pf.factorial(degree);
        System.out.println();
        System.out.println();
        int hl = (int) Math.pow(2, degree);
        int[][] permutedQuat = new int[hl][hl];
        int row = 0;
        int column = 0;
        int place = 0;
        int power = 0;
        int tot = 0;

        int fperm = 0;
        int perm = 0;
        int pperm = 0;
        int ppperm = 0;
        int pppperm = 0;
        int ppppperm = 0;
        int[][] temp = new int[hl][hl];
        int[][] nextQuat = new int[hl][hl];
        int[][] perms = pf.permSequences(degree);
        //permuted Cayley-Dickson multiplication table row, permuted by column, equals another Cayley-Dickson table
        //fperm, perm represent the place values of the row of the output table, row=degreeFactorial*fperm+perm
        for (fperm = 0; fperm < degreeFactorial; fperm++) {
            System.out.println("fperm: " + fperm);
            //oneLoop:
            for (perm = 0; perm < degreeFactorial; perm++) {
                //multiplication table
                for (row = 0; row < hl; row++) {
                    for (column = 0; column < hl; column++) {
                        permutedQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, fperm, perm);
                    }
                }
                System.out.println("perm: " + perm);
                //pperm, ppperm represent the place values of the column of the output table, column=degreeFactorial*pperm+ppperm
                for (pperm = 0; pperm < degreeFactorial; pperm++) {
                    threeLoop:
                    for (ppperm = 0; ppperm < degreeFactorial; ppperm++) {
                        //does the permuting
                        for (row = 0; row < hl; row++) {
                            for (column = 0; column < hl; column++) {
                                tot = 0;
                                for (place = 0; place < degree; place++) {
                                    tot += (int) Math.pow(2, place) * ((permutedQuat[row][column] / binPowers[perms[pperm][place]]) % 2);
                                }
                                tot += (int) Math.pow(2, degree) * ((permutedQuat[row][column] / binPowers[degree]) % 2);
                                temp[row][column] = tot;
                            }
                        }
                        for (row = 0; row < hl; row++) {
                            for (column = 0; column < hl; column++) {
                                permutedQuat[row][column] = temp[row][column];
                            }
                        }
                        //the row table permuted by column equals these two loops, representing the place values of the solution
                        for (pppperm = 0; pppperm < degreeFactorial; pppperm++) {
                            fiveLoop:
                            for (ppppperm = 0; ppppperm < degreeFactorial; ppppperm++) {
                                boolean found = true;
                                for (row = 0; row < hl; row++) {
                                    for (column = 0; column < hl; column++) {
                                        nextQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, pppperm, ppppperm);
                                        if (nextQuat[row][column] != permutedQuat[row][column]) {
                                            //found = false;
                                            continue fiveLoop;
                                        }
                                    }
                                }
                                //solution or not
                                if (found) {
                                    out[fperm * degreeFactorial + perm][pperm * degreeFactorial + ppperm] = degreeFactorial * pppperm + ppppperm;
                                    continue threeLoop;
                                }
                            }
                        }
                    }
                }
            }
        }
        //Console output
        System.out.println("Cayley-Dickson numbers ((cdz,cdo),(cdzz,cdoo)) composition of permutations comparison");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(out[row]));
        }
        int[][][] placesTable = new int[2][degreeFactorial * degreeFactorial][degreeFactorial * degreeFactorial];
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            for (column = 0; column < degreeFactorial * degreeFactorial; column++) {
                placesTable[0][row][column] = out[row][column] % degreeFactorial;
                placesTable[1][row][column] = out[row][column] / degreeFactorial;
            }
        }
        System.out.println("By places , ones place");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(placesTable[0][row]));
        }
        System.out.println("By places , degreeFactorial's place");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(placesTable[1][row]));
        }
        System.out.println("Permutation compositions (transpose) calculated independently of the Cayley-Dickson ");
        int[][] permCompositionsGrid = pf.permsOfPermsGrid(degree);
        for (row = 0; row < degreeFactorial; row++) {
            System.out.println(Arrays.toString(permCompositionsGrid[row]));
        }
        System.out.println("This shows that the permuted Cayley-Dickson numbers ((cdz,cdo),(cdzz,cdoo))");
        System.out.println("Representing the permutations used in recursion ((down,up),(down,up))");
        System.out.println("(cdz,cdzz), the degreeFactorial's places, interact as permutations of permutations");
        System.out.println("(cdo,cdoo), the one's places, interact as permutations of permutations");
        return out;
    }
    /**
     * Compares Cayley-Dickson solution hypercomplex multiplication tables, (cdz,cdo),(cdzz,cdoo), results in (cdz,cdzz),(cdo,cdoo) each independently interacting in the same way as permutations of permutations.
     * <p>
     * This function is the same as cdCompare(), except that cdCompare() checks all possible solutions, and this function assumes the permutation composition interaction and outputs and error if it is not correct, and it doesn't output any errors as high as I've tested it.  * chooses specific values to verify the permutation composition interchange, if this wasn't the case, error messages would print
     *
     * @param degree hypercomplex degree number, quaternions = 2, octonions = 3, sedonions = 4
     * @return a 2d int array of the interactions
     */
    public int[][] cdCompareAgainstPoP(int degree) {
        int[][] pop = pf.permsOfPermsGrid(degree);
        int[][] temp = new int[pop.length][pop.length];
        int[][] out = new int[pf.factorial(degree) * pf.factorial(degree)][pf.factorial(degree) * pf.factorial(degree)];
        int degreeFactorial = pf.factorial(degree);
        System.out.println();
        System.out.println();
        //half the table size, because multiplication tables are semi-symmetric about the quadrants, you only need to check the first quadrant
        int hl = (int) Math.pow(2, degree);
        //Cayley-Dickson row multiplication table
        int[][] permutedQuat = new int[hl][hl];
        //temporary array used to rearrange permutedQuat[][]
        temp = new int[hl][hl];
        //used to check potential matches
        int[][] nextQuat = new int[hl][hl];
        //set of permutations of length degree
        int[][] perms = pf.permSequences(degree);
        //these values are declared here to save the Java initialization time downstream
        int row = 0;
        int column = 0;
        int place = 0;
        int tot = 0;

        int fperm = 0;
        int perm = 0;
        int pperm = 0;
        int ppperm = 0;
        int pppperm = 0;
        int ppppperm = 0;
        //row permuted by column equals value
        //these two loops, fperm and perm are the place values of the row of the output table, row = degreeFactorial*fperm+perm
        for (fperm = 0; fperm < degreeFactorial; fperm++) {
            System.out.println("fperm: " + fperm);
            //oneLoop:
            for (perm = 0; perm < degreeFactorial; perm++) {
                //generate the row's multiplication table
                //because the multiplication table is produced by quadrants, you only have to check the first quadrant
                for (row = 0; row < hl; row++) {
                    for (column = 0; column < hl; column++) {
                        permutedQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, fperm, perm);
                    }
                }
                System.out.println("perm: " + perm);
                //these two loops, pperm and ppperm are the place values of the column of the output table, column = degreeFactorial*pperm+ppperm
                for (pperm = 0; pperm < degreeFactorial; pperm++) {
                    threeLoop:
                    for (ppperm = 0; ppperm < degreeFactorial; ppperm++) {
                        //these loops permute the element at that location in the table
                        for (row = 0; row < hl; row++) {
                            for (column = 0; column < hl; column++) {
                                tot = 0;
                                for (place = 0; place < degree; place++) {
                                    tot += (int) Math.pow(2, place) * ((permutedQuat[row][column] / binPowers[perms[pperm][place]]) % 2);
                                }
                                tot += (int) Math.pow(2, degree) * ((permutedQuat[row][column] / binPowers[degree]) % 2);
                                temp[row][column] = tot;
                            }
                        }
                        for (row = 0; row < hl; row++) {
                            for (column = 0; column < hl; column++) {
                                permutedQuat[row][column] = temp[row][column];
                            }
                        }
                        //these loops check which Cayley-Dickson solution that the row permuted by column equals
                        //these loop values are what they are because of the mutual place values' relationships by permutations of permutations
                        //if that's not the case an error message will print
                        //the other version of this function, cdCompare(), is the brute force version that checks all possible solutions
                        //and they differ in these loop values, I discovered this correlation after seeing the results of the brute force check
                        for (pppperm = pop[fperm][pperm]; pppperm < pop[fperm][pperm] + 1; pppperm++) {
                            fiveLoop:
                            for (ppppperm = pop[perm][ppperm]; ppppperm < pop[perm][ppperm] + 1; ppppperm++) {
                                boolean found = true;
                                for (row = 0; row < hl; row++) {
                                    for (column = 0; column < hl; column++) {
                                        nextQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, pppperm, ppppperm);
                                        if (nextQuat[row][column] != permutedQuat[row][column]) {
                                            //found = false;
                                            //continue fiveLoop;
                                        }
                                    }
                                }
                                //if the permutations of permutations equation is correct, else print error message
                                if (found) {
                                    out[fperm * degreeFactorial + perm][pperm * degreeFactorial + ppperm] = degreeFactorial * pppperm + ppppperm;

                                } else {
                                    System.out.println("Error");
                                }
                            }
                        }
                    }
                }
            }
        }
        //Console output
        System.out.println("Cayley-Dickson numbers ((cdz,cdo),(cdzz,cdoo)) composition of permutations comparison");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(out[row]));
        }
        int[][][] placesTable = new int[2][degreeFactorial * degreeFactorial][degreeFactorial * degreeFactorial];
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            for (column = 0; column < degreeFactorial * degreeFactorial; column++) {
                placesTable[0][row][column] = out[row][column] % degreeFactorial;
                placesTable[1][row][column] = out[row][column] / degreeFactorial;
            }
        }
        System.out.println("By places , ones place");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(placesTable[0][row]));
        }
        System.out.println("By places , degreeFactorial's place");
        for (row = 0; row < degreeFactorial * degreeFactorial; row++) {
            System.out.println(Arrays.toString(placesTable[1][row]));
        }
        System.out.println("Permutation compositions (transpose) calculated independently of the Cayley-Dickson ");
        int[][] permCompositionsGrid = pf.permsOfPermsGrid(degree);
        for (row = 0; row < degreeFactorial; row++) {
            System.out.println(Arrays.toString(permCompositionsGrid[row]));
        }
        System.out.println("This shows that the permuted Cayley-Dickson numbers ((cdz,cdo),(cdzz,cdoo))");
        System.out.println("Representing the permutations used in recursion ((down,up),(down,up))");
        System.out.println("(cdz,cdzz), the degreeFactorial's places, interact as permutations of permutations");
        System.out.println("(cdo,cdoo), the one's places, interact as permutations of permutations");
        return out;
    }
    /**
     * Compares Cayley-Dickson solution hypercomplex multiplication tables, (cdz,cdo),(cdzz,cdoo),
     * results in (cdz,cdzz),(cdo,cdoo) each independently interacting in the same way as permutations of permutations.
     * <p>
     * This function is the same as cdCompare(), except that cdCompare() checks all possible solutions, and this function
     * chooses specific values at random locations to verify the permutation composition interchange, if this wasn't the case, error messages would print.
     * <p>
     * This function exists because exhaustively checking every value at higher than sedonions takes prohibitively long.
     * <p>
     * This function helps to verify the integrity of the permuted Cayley-Dickson multiplication tables because it is independently calculating permutations of permutations
     *
     * @param degree hypercomplex degree number, quaternions = 2, octonions = 3, sedonions = 4
     * @return a 2d int array of the interactions
     */
    public int[][] cdCompareAgainstPoPStochastic(int degree) {
        System.out.println("cdCompareAgainstPopStochastic(int degree)");
        System.out.println("This function exists because exhaustive searches of permuted Cayley-Dickson multiplication tables take prohibitively long");
        System.out.println("This algorithm has an O(n)=d!*d!*d!*d!*2^(2d), so beyond degree 4, this version of the function selects 1000 random ((cdz,cdo),(cdzz,cdoo)) elements");
        System.out.println("to test the algorithm against and reports the results");
        //permutation composition table
        int[][] pop = pf.permsOfPermsGrid(degree);
        //used to rearrange nextQuat
        int[][] temp = new int[pop.length][pop.length];
        //output table
        int[][] out = new int[pf.factorial(degree) * pf.factorial(degree)][pf.factorial(degree) * pf.factorial(degree)];
        //number of Cayley-Dickson solutions, one direction
        int degreeFactorial = pf.factorial(degree);
        System.out.println();
        //half the table size, because you only need to check one quadrant
        int hl = (int) Math.pow(2, degree);
        //Cayley-Dickson solution of the row value of out[][]
        int[][] permutedQuat = new int[hl][hl];
        //trial CD table to compare to permutedQuat[][]
        int[][] nextQuat = new int[hl][hl];
        //set of permutations of length degree
        int[][] perms = pf.permSequences(degree);
        //number of random checks to conduct
        int numAttempts = 5000;
        //number of errors found
        int errors = 0;
        //random class
        Random rand = new Random();
        //declared here to save declarations downstream
        int row = 0;
        int column = 0;
        int place = 0;
        int power = 0;
        int tot = 0;

        int fperm = 0;
        int perm = 0;
        int pperm = 0;
        int ppperm = 0;
        int pppperm = 0;
        int ppppperm = 0;
        //random attempt loop
        for (int attempt = 0; attempt < numAttempts; attempt++) {
            //initialize random multiplication tables to inspect
            int[] randPerms = new int[4];
            for (int spot = 0; spot < 4; spot++) {
                randPerms[spot] = rand.nextInt(0, degreeFactorial);
            }
            //row permuted by column equals value
            //these two loops, fperm and perm are the place values of the row of the output table, row = degreeFactorial*fperm+perm
            for (fperm = randPerms[0]; fperm < randPerms[0] + 1; fperm++) {
                //System.out.println("fperm: " + fperm);
                //oneLoop:
                for (perm = randPerms[1]; perm < randPerms[1] + 1; perm++) {
                    //generate the row's multiplication table
                    //because the multiplication table is produced by quadrants, you only have to check the first quadrant
                    for (row = 0; row < hl; row++) {
                        for (column = 0; column < hl; column++) {
                            permutedQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, fperm, perm);
                        }
                    }
                    //these two loops, pperm and ppperm are the place values of the column of the output table, column = degreeFactorial*pperm+ppperm
                    for (pperm = randPerms[2]; pperm < randPerms[2] + 1; pperm++) {
                        threeLoop:
                        for (ppperm = randPerms[3]; ppperm < randPerms[3] + 1; ppperm++) {
                            //these loops permute the element at that location in the table
                            for (row = 0; row < hl; row++) {
                                for (column = 0; column < hl; column++) {
                                    tot = 0;
                                    for (place = 0; place < degree; place++) {
                                        tot += (int) Math.pow(2, place) * ((permutedQuat[row][column] / binPowers[perms[pperm][place]]) % 2);
                                    }
                                    tot += (int) Math.pow(2, degree) * ((permutedQuat[row][column] / binPowers[degree]) % 2);
                                    temp[row][column] = tot;
                                }
                            }
                            for (row = 0; row < hl; row++) {
                                for (column = 0; column < hl; column++) {
                                    permutedQuat[row][column] = temp[row][column];
                                }
                            }
                            //these loops check which Cayley-Dickson solution that the row permuted by column equals
                            //these loop values are what they are because of the mutual place values' relationships by permutations of permutations
                            //if that's not the case an error message will print
                            //the other version of this function, cdCompare(), is the brute force version that checks all possible solutions
                            //and they differ in these loop values, I discovered this correlation after seeing the results of the brute force check
                            for (pppperm = pop[fperm][pperm]; pppperm < pop[fperm][pperm] + 1; pppperm++) {
                                fiveLoop:
                                for (ppppperm = pop[perm][ppperm]; ppppperm < pop[perm][ppperm] + 1; ppppperm++) {
                                    boolean found = true;
                                    for (row = 0; row < hl; row++) {
                                        for (column = 0; column < hl; column++) {
                                            nextQuat[row][column] = multiplyUnitHyperComplex(degree, row, column, pppperm, ppppperm);
                                            if (nextQuat[row][column] != permutedQuat[row][column]) {
                                                //found = false;
                                                continue fiveLoop;
                                            }
                                        }
                                    }
                                    //if the permutations of permutations
                                    if (found) {
                                        out[fperm * degreeFactorial + perm][pperm * degreeFactorial + ppperm] = degreeFactorial * pppperm + ppppperm;
                                        //out[pperm*degreeFactorial+ppperm][fperm*degreeFactorial+perm] = degreeFactorial*pppperm+ppppperm;
                                        continue threeLoop;
                                    } else {
                                        errors++;
                                        System.out.println("Error");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("numAttempts: " + numAttempts + " errors: " + errors);
        System.out.println("done");

        return out;
    }

    /**
     * Checks a 2D int array for Latin-ness, each row and column contains exactly one of each value. Used to help verify the integerity of the multiplication tables generated
     *
     * @param in 2D int array
     * @return       boolean  true if in[][] is Latin and false if not
     */
    public boolean isLatin(int[][] in) {
        boolean isLatin = true;
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                for (int comp = 0; comp < in.length; comp++) {
                    if (comp == column) continue;
                    if (in[row][column] == in[row][comp]) isLatin = false;
                }
                for (int comp = 0; comp < in.length; comp++) {
                    if (comp == row) continue;
                    if (in[row][column] == in[comp][column]) isLatin = false;
                }
            }
        }
        return isLatin;
    }
    /**
     * Pre-calculates the multiplication tables of the permuted Cayley-Dickson quaternions,
     * octonions, and sedonions. Pre-calculating beyond sedonions (with 5!*5! entries) is very
     * time and memory intensive.
     *
     * @param degree degree hypercomplex, 2 = quaternions, 3 = octonions, 4 = sedonions...
     * @return an array of all permuted Cayley-Dickson multiplication tables for degree hypercomplex
     */
    public int[][][] preCalculateTables(int degree) {
        int gridSize = (int) Math.pow(2, degree + 1);
        int[][][] out = new int[pf.factorial(degree) * pf.factorial(degree)][gridSize][gridSize];
        for (int cdz = 0; cdz < pf.factorial(degree); cdz++) {
            for (int cdo = 0; cdo < pf.factorial(degree); cdo++) {
                for (int row = 0; row < gridSize; row++) {
                    for (int column = 0; column < gridSize; column++) {
                        out[cdz * pf.factorial(degree) + cdo][row][column] = multiplyUnitHyperComplex(degree, row, column, cdz, cdo);
                    }
                }
            }
        }
        return out;
    }


}

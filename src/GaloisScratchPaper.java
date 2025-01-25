import java.util.Arrays;
/**
 * Experimental GF() using multiples of places on the multiplication grid
 */
public class GaloisScratchPaper {
    /**
     * Ordinary GF()
     */
    GaloisFields galoisFields = new GaloisFields();
    /**
     * Three times m^n
     * @param m prime
     * @param n power
     * @param numFactors number of dimensions in table
     * @return ???
     */
    public int[] galoisThree(int m, int n, int numFactors) {
        int size = (int) Math.pow(m, n);
        int[] out = new int[(int)Math.pow(size,numFactors)];
        int[][][] partial = new int[n][n][n];
        int[][] partialSeed = new int[3][n];
        int[] partialTotal = new int[1 + n];
        int[][][] pd;
        int[][][][][] partialDirection = new int[n][n][n][n][n];
        int[] localSpot = new int[numFactors * size];
        int flatSize = (int) Math.pow(m, numFactors * n);
        pd = new int[(int) Math.pow(n, numFactors)][n][n];
        for (int spot = 0; spot < (int) Math.pow(n, numFactors); spot++) {
            int distance = 0;
            for (int power = 0; power < numFactors; power++) {
                localSpot[power] = (spot / (int) Math.pow(n, power)) % n;
                distance += localSpot[power];
            }
            if (distance <= m) {
                pd[spot][0][0] = distance;
                for (int power = 1; power < n; power++) {
                    pd[spot][power][0] = -1;
                    pd[spot][power][1] = -1;
                }
                continue;
            }
            for (int round = 0; round < n; round++) {
                if (distance <= m) {
                    pd[spot][round][0] = distance;
                    pd[spot][round][1] = -1;
                    pd[spot][round][2] = -1;
                } else {
                    pd[spot][round][0] = 0;
                    pd[spot][round][1] = 1;
                    pd[spot][round][2] = -1;
                    for (int rround = 0; rround < round; rround++) {
                        pd[spot][round - 1][0] += (distance % m);
                        pd[spot][round - 1][1] += (distance % m);
                        pd[spot][round][0] += (pd[spot][round - 1][1] % m);
                        pd[spot][round][1] += (pd[spot][round - 1][1] % m);
                        pd[spot][round - 1][0] = (pd[spot][round - 1][0] % m);
                        pd[spot][round - 1][1] = (pd[spot][round - 1][1] % m);
                    }
                }
                distance -= m;
                if (distance <= 0) break;
            }
        }
        for (int spot = 0; spot < flatSize; spot++) {
            for (int power = 0; power < numFactors; power++) {
                localSpot[power] = (spot / (int) Math.pow(size, power)) % size;
            }
            int[] tally = new int[numFactors*n];
            for (int cell = 0; cell < pd.length; cell++) {
                for (int round = 0; round < n; round++) {
                    for (int term = 0; term < 2; term++) {
                        if (pd[cell][round][term] == -1) break;
                        int end = 1;
                        for (int p = 0; p < numFactors; p++) {
                            end *= localSpot[p];
                        }
                        tally[pd[cell][round][term]] += end;
                    }
                }
            }
            int result = 0;
            for (int power = 0; power < numFactors; power++){
                tally[power] = tally[power] % m;
                result += (int)(tally[power]/Math.pow(m,power) )% m;
            }
            out[spot] = result;
        }
//        for (int place = 0)
//        for (int row = 0; row < n; row++) {
//            for (int column = 0; column < n; column++) {
//                for (int zee = 0; zee < n; zee++) {
//                    int distance = row + column + zee;
//                    for (int power = 0; power < n; power++) {
//                        partialDirection[row][column][zee][power] = distance % m;
//                        distance /= m;
//                    }
//                    partialDirection[row][column][zee][n] = distance;
//                }
//            }
//        }
//        for (int row = 0; row < size; row++) {
//            for (int column = 0; column < size; column++) {
//                for (int zee = 0; zee < size; zee++) {
//                    int[] rzc = new int[]{row, column, zee};
//                    for (int factor = 0; factor < 3; factor++) {
//                        for (int power = 0; power < n; power++) {
//                            partialSeed[factor][power] = (rzc[factor] / (int) Math.pow(m, power)) % m;
//                        }
//                    }
//                    for (int rrow = 0; rrow < n; rrow++) {
//                        for (int ccolumn = 0; ccolumn < n; ccolumn++) {
//                            for (int zzee = 0; zzee < n; zzee++) {
//                                partial[rrow][ccolumn][zzee] = partialSeed[0][rrow] * partialSeed[1][ccolumn] * partialSeed[2][zzee];
//                            }
//                        }
//                    }
//                    for (int rrow = 0; rrow < n; rrow++) {
//                        for (int ccolumn = 0; ccolumn < n; ccolumn++) {
//                            for (int zzee = 0; zzee < n; zzee++) {
//                                for (int power = 0; power <= n; power++) {
//                                    partialTotal[partialDirection[rrow][ccolumn][zzee][power]] += partial[rrow][ccolumn][zzee];
//                                }
//                            }
//                        }
//                    }
//                    for (int power = 0; power < n; power++) {
//                        out[row][column][zee] += (int) Math.pow(m, power) * (partialTotal[power] % m);
//                    }
//                }
//            }
//        }
        return out;
    }
    /**
     *
     * @param m prime
     * @param n power
     */
    public void check(int m, int n) {
        int[][] galois = galoisFields.galoisFieldMultiplication(m, n);
        //int[] cube = galoisThree(m, n,3);
        int size = (int) Math.pow(m, n);
        int[][] flattened = new int[2][(int) Math.pow(size, size)];
        int[][] tg = galoisFields.generateTable(m, n, true);
        int[][][] gcube = new int[size][size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                for (int zee = 0; zee < size; zee++) {
                    gcube[row][column][zee] = galois[row][column];
                    gcube[row][column][zee] = galois[galois[row][column]][zee];
                }
            }
        }
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                for (int zee = 0; zee < size; zee++) {
                    //System.out.print("(" + cube[row][column][zee] + ") ");
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }
//        size = 8;
//        for (int row = 0; row < size; row++) {
//            for (int column = 0; column < size; column++) {
//                for (int zee = 0; zee < size; zee++) {
//                    flattened[0][size * size * row + size * column + zee] = gcube[row + 1][column + 1][zee + 1];
//                    //flattened[1][size * size * row + size * column + zee] = cube[row + 1][column + 1][zee + 1];
//                }
//            }
//        }
//        System.out.println(isLatinFlattened(flattened[0], size, 2));
//        System.out.println(isLatinFlattened(flattened[0], size, 3));
//        size = 8;
//        System.out.println(isLatinFlattened(flattened[1], size, 3));
        size--;

            for (int depth = 2; depth < 9; depth++) {
                int l = (int)Math.pow(size,depth);
                int[] flatt = new int[l];
                for (int spot = 0; spot < l; spot++){
                    int[] local = new int[depth];
                    for (int power = 0; power < depth; power++){
                        local[power] = (spot/(int)Math.pow(size,power))%size;
                    }
                    int out = tg[local[0]][local[1]];
                    for (int power = 2; power < depth; power++){
                        out = tg[out][local[power]];
                    }
                    flatt[spot] = out;
                }
                System.out.println("flatten "  + depth + " " + isLatinFlattened(flatt, size, depth));
            }
    }
    /**
     * Probes the depth of GF(), to see at what dimension the MOLS-ishness of it ends. I don't know if this version works, there IS a working version somewhere, where ???, maybe this?
     * @param in
     * @param size
     * @param depth
     * @return
     */
    public boolean isLatinFlattened(int[] in, int size, int depth) {
        boolean out = true;
        int l = (int) Math.pow(size, depth);
        int[] lv;
        for (int spot = 0; spot < l; spot++) {
            lv = new int[depth];
            for (int power = 0; power < depth; power++) {
                lv[power] = (spot / (int) Math.pow(size, power)) % size;
            }
            for (int place = 0; place < depth; place++) {
                phaseLoop:
                for (int phase = 1; phase < depth; phase++) {
                    //if (phase == place) continue phaseLoop;
                    int tot = 0;
                    int[] localLV = new int[depth];
                    for (int power = 0; power < depth; power++) {
                        localLV[power] = lv[power];
                    }
                    localLV[place] = (lv[place] + phase) % size;
                    for (int power = 0; power < depth; power++) {
                        tot += (int) Math.pow(size, power) * localLV[power];
                    }
                    //System.out.println(Arrays.toString(localLV));
                    if (in[spot] == in[tot]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public int manageCheckDepth(int[][] in, int degree){
        CustomArray.plusArrayDisplay(in, false, false, "in[][]");
        int[] flattened = new int[in.length*in.length];
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                flattened[in.length*row+column] = in[row][column];
            }
        }
        System.out.println("in.length: " + in.length);
        int nonMutableDegree = degree;
        int out = 2;
        for (int d = 3; d <= 12; d++) {
            int tempTwo = nonMutableDegree;
            System.out.println("  depth: " + d);
            int temp = d;
            if (isLatinFlattenedTwo(flattened, degree, d)) {
                out = d;
                // System.out.println("  depth: " + d);
            } else {
                System.out.println("no");
                break;
            }
        }
        System.out.println();
        return out;
    }
    /**
     * Probes the depth of GF(), to see at what dimension the MOLS-ishness of it ends. I don't know if this version works, there IS a working version somewhere, where ???, maybe this?
     * @param in
     * @param size
     * @param depth
     * @return
     */
    public boolean isLatinFlattenedTwo(int[] in, int size, int depth) {
        boolean out = true;
        int l = (int) Math.pow(size, depth);
        int[] lv;
        lvLoop:
        for (int spot = 0; spot < l; spot++) {
            lv = new int[depth];
            for (int power = 0; power < depth; power++) {
                lv[power] = (spot / (int) Math.pow(size, power)) % size;
                //if (lv[power] == 0) {continue lvLoop;}
            }
            int b = in[lv[0]*size+lv[1]];
            for (int location = 2; location < depth; location++ ) {
                b = in[b*size+lv[location]];
            }
            System.out.println("spot: " + spot + " b: " + b);
            for (int place = 0; place < depth; place++) {
                phaseLoop:
                for (int phase = 1; phase < size; phase++) {
                    //if (phase == place) continue phaseLoop;
                    int tot = 0;
                    int[] localLV = new int[depth];
                    for (int power = 0; power < depth; power++) {
                        localLV[power] = lv[power];
                    }
                    localLV[place] = (localLV[place] + phase) % size;
                    //System.out.println("lv: " + Arrays.toString(lv) + " local LV: " + Arrays.toString(localLV));
                    //if (localLV[place] == 0) {continue phaseLoop;}
                    for (int power = 0; power < depth; power++) {
                        tot += (int) Math.pow(size, power) * localLV[power];
                    }
                    int a = in[localLV[0]*size+localLV[1]];
                    for (int location = 2; location < depth; location++ ) {
                        //System.out.println("a: " + a + " location: " + location);

                        a = in[a*size+localLV[location]];
                    }
                    //System.out.println(Arrays.toString(localLV));
                    if (a == b) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

import java.util.Arrays;

/**
 * array display class
 */
public class CustomArray {

    /**
     * Same mediator layers from StirlingStacks
     */
    int[][][] intermediators;

    /**
     * displays a 2D integer array
     *
     * @param in       2D integer array to display
     * @param noZeros  true if you want to replace zeros with spaces on output
     * @param noSpaces true if you don't want any spaces between elements
     */
    public static void plusArrayDisplay(int[][] in, boolean noZeros, boolean noSpaces) {
        int rows = in.length;
        int columns = in[0].length;
        String outstring = "";
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (in[row][column] == 0 && noZeros) {
                    outstring += " ";
                } else {
                    outstring += in[row][column];
                }
                if (!noSpaces) {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
    }

    /**
     * displays a 2D integer array
     *
     * @param in       2D integer array to display
     * @param noZeros  true if you want to replace zeros with spaces on output
     * @param noSpaces true if you don't want space padding on output
     * @param label    array title
     */
    public static void plusArrayDisplay(int[][] in, boolean noZeros, boolean noSpaces, String label) {
        int rows = in.length;
        int columns = in[0].length;
        String outstring = "";
        System.out.println(label);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (in[row][column] == 0 && noZeros) {
                    outstring += " ";
                } else {
                    outstring += in[row][column];
                }
                if (!noSpaces) {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
    }

    /**
     * displays a 2D integer array
     *
     * @param in       2D integer array to display
     * @param noZeros  true if you want to replace zeros with spaces on output
     * @param noSpaces true if you don't want space padding on output
     * @param label    array title
     */
    public static void layersDisplay(int[][][] in, boolean noZeros, boolean noSpaces, String label) {
        int rows = in.length;
        int columns = in[0][0].length;
        String outstring = "";
        System.out.println(label);
        for (int layer = 0; layer < in.length; layer++) {
            System.out.println("Layer " + layer);
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    if (in[layer][row][column] == 0 && noZeros) {
                        outstring += " ";
                    } else {
                        outstring += in[layer][row][column];
                    }
                    if (!noSpaces) {
                        outstring += " ";
                    }
                }
                System.out.println(outstring);
                outstring = "";
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * displays a 2D integer array on the console
     *
     * @param in       2D integer array to display
     * @param noZeros  true if you want to replace zeros with spaces on output
     * @param noSpaces true if you don't want space padding on output
     * @param rowDiv   number of rows used on increment
     * @param colDiv   number of columns used on increment
     */
    public static void plusPlusArrayDisplay(int[][] in, boolean noZeros, boolean noSpaces, int rowDiv, int colDiv) {
        int rows = in.length;
        int columns = in[0].length;
        String outstring = "";
        for (int row = 0; row < rows; row += rowDiv) {
            for (int column = 0; column < rows; column += colDiv) {
                if (in[row][column] != 0) {
                    outstring += in[row][column];
                } else if (in[row][column] == 0 && noZeros) {
                    outstring += " ";
                } else {
                    outstring += in[row][column];
                }
                if (!noSpaces) {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
    }

    /**
     * displays a 2D integer array
     *
     * @param in 2D integer array to display
     */
    public static void basicArrayDisplay(int[][] in) {
        int rows = in.length;
        int columns = in[0].length;
        String outstring = "";
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                outstring += in[row][column];
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println();
    }

    /**
     * Applies a gray code to each axis of the input array
     *
     * @param in 2D input array
     * @return input with Gray codes applied
     */
    public static int[][] grayIFY(int[][] in) {
        int[][] out = new int[in.length][in.length];
        //int[] gray = PermutationsFactoradic.graySequence(8);
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                //out[gray[row]][gray[column]] = in[row][column];
            }
        }
        return out;
    }

    /**
     * Checks a 2D int array for Latin-ness, each row and column contains exactly one of each value. Used to help verify the integerity of the multiplication tables generated
     *
     * @param in 2D int array
     * @return boolean  true if in[][] is Latin and false if not
     */
    public static boolean isLatin(int[][] in) {
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

    public static int[][] reflectRotateTranspose(int[][] in, int rotation) {
        int size = in.length;
        int[][] out = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                out[row][column] = in[row][column];
            }
        }
        if (rotation % 2 == 1) {
            int[][] nextTemp = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    nextTemp[row][column] = out[size - 1 - row][column];
                }
            }
            out = nextTemp;
        }
        if (((rotation / 2) % 2) == 1) {
            int[][] nextTemp = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    nextTemp[row][column] = out[row][size - 1 - column];
                }
            }
            out = nextTemp;
        }
        if (((rotation / 4) % 2) == 1) {
            int[][] nextTemp = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    nextTemp[row][column] = out[column][row];
                }
            }
            out = nextTemp;
        }
        return out;
    }

    /**
     * A particular place of Pascal mod inDegree
     *
     * @param inDegree Pascal mod this
     * @param size     input array side length
     * @param layer    which place value layer to return
     * @return Pascal mod inDegree, layer's place
     */
    public static int[][] stackPascalPlaceLayer(int inDegree, int size, int layer) {
        int[][] out = new int[size][size];
        for (int row = 0; row < size; row++) {
            out[row][row] = 1;
            out[row][0] = 1;
        }
        for (int row = 2; row < size; row++) {
            for (int column = 1; column < row; column++) {
                out[row][column] = out[row - 1][column - 1] + out[row - 1][column];
                out[row][column] = out[row][column] % inDegree;
                //out[row][column] /= (int)Math.pow(2,layer-1);
                //out[row][column] %= 2;
            }
        }
        for (int row = 2; row < size; row++) {
            for (int column = 1; column < row; column++) {
//                out[row][column] = out[row - 1][column - 1] + (column % inDegree)*out[row - 1][column];
//                out[row][column] = out[row][column] % inDegree;
                out[row][column] /= (int) Math.pow(2, layer);
                out[row][column] %= 2;
            }
        }
//        System.out.println("Pascal");
//        System.out.println("Degree " + inDegree);
//        for (int row = 0; row < 100; row++) {
//            for (int column = 0; column < 100; column++) {
//                System.out.print(out[row][column]);
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");
//        for (int row = 2; row < size; row++) {
//            for (int column = 1; column < row; column++) {
//                for (int power = 0; power < inDegree / 2; power++) {
//                    pascalStack[power][row][column] = (out[row][column] / (int) Math.pow(inDegree, power)) % 2;
//                }
//            }
//        }
        //pascal = out;
        return out;
    }

    /**
     * A particular place of Stirling mod inDegree
     *
     * @param inDegree Stirling mod this
     * @param size     input array side length
     * @param layer    which place value layer to return
     * @return Stirling mod inDegree, layer's place
     */
    public static int[][] stackStirlingPlaceLayer(int inDegree, int size, int layer) {
        int[][] out = new int[size][size];
//        System.out.println("Calculating mod " + inDegree);
//        for (int row = 0; row < size; row++) {
//            out[row][0] = 1;
//            out[row][row] = 1;
//        }
//        for (int row = 2; row < size; row++) {
//            for (int column = 1; column < row; column++) {
//                out[row][column] = (out[row - 1][column - 1] + (column % inDegree) * out[row - 1][column]) % inDegree;
//                //out[row][column] = (out[row][column] / (int) Math.pow(2, layer)) % 2;
//                //out[row][column] %= 2;
////                    out[row][column] = out[row][column-1] + degree * out[row - 1][column-1] + degree * degree * out[row - 1][column];
//                //                  out[row][column] = out[row][column] % degree;
//            }
//        }
//        out = stackStirling(inDegree, size);
//        for (int row = 0; row < size; row++) {
//            for (int column = 0; column < size; column++) {
////                out[row][column] = out[row - 1][column-1] + (column % inDegree)*out[row - 1][column];
////                out[row][column] = out[row][column] % inDegree;
//                out[row][column] = out[row][column] / (int) Math.pow(2, layer);
//                out[row][column] = out[row][column] % 2;
//            }
//        }
//        //
//        //basis shift from down, down-right to down-right
////        for (int degree = 0; degree < 14; degree++) {
////            for (int column = 1; column < size; column++) {
////                for (int row = 0; row + column < size; row++) {
////                    stirlingMod[degree][row][column] = stirlingMod[degree][row+column][column];
////                }
////            }
////            for (int column = 1; column < size; column++) {
////                for (int row = 0; row < column ; row++) {
////                    stirlingMod[degree][4999-row][column] = 0;
////                }
////
////            }
////        }
////        System.out.println("Stirling numbers");
////        System.out.println("Degree " + inDegree);
////        int[][] alt = new int[128][128];
////        for (int row = 0; row < 128; row++) {
////            for (int column = 0; column < 128; column++) {
////                if (out[row+(column/2)][column] != 0) System.out.print(out[row+(column/2)][column]);
////                else System.out.print(" ");
////                alt[row][column] = out[row+(column/2)][column];
////            }
////            System.out.print("\n");
////        }
//        //stackConflict(2,alt,conflictStack,wolframStack);
//        //findWolfram(alt,2,conflictStack,wolframStack,0);
////        System.out.print("\n");
////        for (int row = 2; row < size; row++) {
////            for (int column = 1; column < row; column++) {
////                for (int power = 0; power < inDegree / 2; power++) {
////                    stirlingStack[power][row][column] = (out[row][column] / (int) Math.pow(inDegree, power)) % 2;
////                }
////            }
////        }
//        //stirling = out;
        return out;
    }

    /**
     * Reduces two input grids
     *
     * @param in         input a
     * @param stirlingIn input b
     * @return a 'operation' b
     */
    public static int[][] reduce(int[][] in, int[][] stirlingIn) {
        //pascal stirling at bottom
        //AND pascal, AND stirling
        //XOR pascal, XOR stirling
        //etc
        int[][] out = new int[in.length][in.length];
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                //out[row][column] = in[row][column] & ( (stirlingIn[row][column]+1)%2);
                //out[row][column] = in[row][column] | stirlingIn[row][column];
                out[row][column] = in[row][column] ^ stirlingIn[row][column];
            }
        }
        return out;
    }

    /**
     * A particular place value's layer of an input array
     *
     * @param in    2D grid
     * @param layer which place value
     * @param base  which base
     * @param modN  which mod
     * @return input with parameters applied
     */
    static int[][] layerOf(int[][] in, int layer, int base, int modN) {
        int[][] out = new int[in.length][in[0].length];
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                out[row][column] = ((in[row][column]) / (int) Math.pow(base, layer)) % modN;
            }
        }
        return out;
    }

    public static double[][] rowReduceTwo(int[][] in) {
        double[][] out = new double[in.length][in[0].length];
        int size = in.length;
        double[] vector = new double[size];
        for (int row = 0; row < in.length; row++) {
            vector[row] = 1;
        }
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                out[row][column] = in[row][column];
            }
        }
        int h = 0;
        for (int pivot = 0; pivot < size && h < size; pivot++) {
            double dRowMax = 0;
            int rowMax = 0;
            for (int row = h; row < in.length; row++) {
                if (out[row][pivot] > dRowMax) {
                    dRowMax = out[row][pivot];
                    rowMax = row;
                }
            }
            if (dRowMax == 0) {
                continue;
            }
            for (int column = 0; column < in.length; column++) {
                double temp = out[rowMax][column];
                out[rowMax][column] = out[h][column];
                out[h][column] = temp;
            }
            double temp = vector[h];
            vector[h] = vector[rowMax];
            vector[rowMax] = temp;
            for (int row = h + 1; row < in.length; row++) {
                double f = out[row][pivot]/out[h][pivot];
                out[row][pivot] = 0;
                for (int column = pivot + 1; column < in.length; column++) {
                    out[row][column] = out[row][column] - f * out[row][column];
                }
            }
            h++;
        }
        System.out.println("Output vector");
        System.out.println(Arrays.toString(vector));
        System.out.println("Reduced matrix");
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                System.out.print(String.format("%3f", out[row][column]) + " ");
            }
            System.out.println();
        }
        h = 0;
        for (int pivot = size-1; pivot >= 0; pivot--) {
            double dRowMax = 0;
            int rowMax = 0;
            for (int row = size-1; row >= 0; row--) {
                if (out[row][pivot] > 0) {
                    dRowMax = out[row][pivot];
                    rowMax = row;
                    break;
                }
            }
            if (dRowMax == 0) {
                continue;
            }
            for (int column = 0; column < in.length; column++) {
                //out[rowMax][column] = out[rowMax][column]/dRowMax;
            }
//            for (int column = 0; column < in.length; column++) {
//                double temp = out[rowMax][column];
//                out[rowMax][column] = out[h][column];
//                out[h][column] = temp;
//            }
//            double temp = vector[h];
//            vector[h] = vector[rowMax];
//            vector[rowMax] = temp;
            for (int row = rowMax-1; row >= 0; row--) {
                double f = out[row][pivot]/out[rowMax][pivot];
                //out[row][pivot] = 0;
                //double f = out[row][pivot];
                for (int column = 0; column < in.length; column++) {
                    out[row][column] = out[row][column] - f * out[rowMax][column];
                }
            }
            h++;
        }
        double pivot = -1;
        for (int row = 0; row < in.length; row++) {
            boolean foundPivot = false;
            for (int column = 0; column < in.length; column++) {
                if (out[row][column] > 0) {
                    pivot = out[row][column];
                    foundPivot = true;
                }
                if (foundPivot){
                    out[row][column] /= pivot;
                }
            }

        }
        System.out.println("Further Reduced matrix");
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                System.out.print(String.format("%3f", out[row][column]) + " ");
            }
            System.out.println();
        }
        return out;
    }


    public static double[][] rowReduce(int[][] in) {
        double[][] out = new double[in.length][in[0].length];
        int size = in.length;
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                out[row][column] = in[row][column];
            }
        }
        int h = 0;
        int k = 0;
        int m = size;
        int n = size;
        double[] vector = new double[m];
        for (int spot = 0; spot < m; spot++) {
            vector[spot] = 1;
        }
        while (k < m && h < n) {
            int rowMax = 0;
            double dRowMax = 0;
            for (int row = h; row < in.length; row++) {
                if (Math.abs(out[row][k]) > dRowMax) {
                    dRowMax = out[row][k];
                    rowMax = row;
                }
            }
            if (out[rowMax][k] == 0) {
                k++;
            } else {
                for (int column = 0; column < in.length; column++) {
                    double temp = out[h][column];
                    out[h][column] = out[rowMax][column];
                    out[rowMax][column] = temp;
                }
                double temp = vector[h];
                vector[h] = vector[rowMax];
                vector[rowMax] = temp;
                for (int i = h; i < in.length; i++) {
                    double f = out[i][k] / out[h][k];
                    out[i][k] = 0;
                    for (int j = k; j < in.length; j++) {
                        out[i][j] = out[i][j] - out[h][j] * f;
                    }
                    vector[i] = vector[i] - vector[h] * f;
                }
                h++;
                k++;
            }
        }

        System.out.println("Output vector");
        System.out.println(Arrays.toString(vector));
        System.out.println("Reduced matrix");
        for (int row = 0; row < in.length; row++) {
            for (int column = 0; column < in.length; column++) {
                System.out.print(String.format("%3f", out[row][column]) + " ");
            }
            System.out.println();
        }
        //Wikipedia 2025
//        h := 1 /* Initialization of the pivot row */
//        k := 1 /* Initialization of the pivot column */
//
//        while h ≤ m and k ≤ n
//        /* Find the k-th pivot: */
//        i_max := argmax (i = h ... m, abs(A[i, k]))
//        if A[i_max, k] = 0
//        /* No pivot in this column, pass to next column */
//        k := k + 1
//    else
//        swap rows(h, i_max)
//        /* Do for all rows below pivot: */
//        for i = h + 1 ... m:
//        f := A[i, k] / A[h, k]
//        /* Fill with zeros the lower part of pivot column: */
//        A[i, k] := 0
//        /* Do for all remaining elements in current row: */
//        for j = k + 1 ... n:
//        A[i, j] := A[i, j] - A[h, j] * f
//        /* Increase pivot row and column */
//        h := h + 1
//        k := k + 1
        return out;
    }

    /**
     * Generates the intermediator array
     *
     * @param inDegree base-N, range of possibilities in each cell
     * @param size     array size
     * @param layer    not used atm
     * @return 2D intermediator array
     */
    public int[][] conflictStackIntermediator(int inDegree, int size, int layer) {
        int[][] out = new int[size][size];
        intermediators = new int[5][size][size];
        for (int row = 0; row < size; row++) {
            out[row][row] = 1;
            out[row][0] = 1;
        }
        for (int row = 1; row < size; row++) {
            for (int column = 1; column < row; column++) {
                out[row][column] = out[row - 1][column - 1] + out[row - 1][column] + out[row][column - 1];
                out[row][column] = out[row][column] % inDegree;
                intermediators[1][row][column] = out[row][column];
                //intermediators[1][row][column] = ((intermediators[1][row][column] / (1 << layer)) % 2);
            }
        }
        for (int row = 2; row < size; row++) {
            for (int column = 1; column < row; column++) {
                intermediators[1][row][column] /= 2;
                intermediators[1][row][column] %= 2;
                //intermediators[1][row][column] = ((intermediators[1][row][column] / (1 << layer)) % 2);
            }
        }
        for (int row = 2; row < size; row += 2) {
            //this constant valued intermediator is a conjugate vector
            //because the 0-th position is 0 since it is greater than the diagonal
            intermediators[2][row][1] = 1;
        }
//        System.out.println("150");
//        System.out.println("Degree " + inDegree);
//        for (int row = 0; row < out.length; row++) {
//            for (int column = 0; column <= row; column++) {
//                System.out.print(out[row][column]+" ");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");
        return out;
    }
}

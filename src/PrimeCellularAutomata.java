import java.util.Arrays;
import java.util.Random;
public class PrimeCellularAutomata {
    public int[] primeRule;
    public int[][] primeField;
    public int[][][] primeFieldpowers;
    PrimeCellularAutomata() {
        generateMainRule();
        String outstring = "";
        for (int row = 0; row < 100; row++) {
            for (int column = 450; column < 550; column++) {
                if (primeFieldpowers[0][row][column] != 0) {
                    outstring += Integer.toString(primeFieldpowers[0][row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("\n\n\n");
        for (int row = 0; row < 100; row++) {
            for (int column = 450; column < 550; column++) {
                if (primeFieldpowers[1][row][column] != 0) {
                    outstring += Integer.toString(primeFieldpowers[1][row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("\n\n\n");
        for (int row = 0; row < 100; row++) {
            for (int column = 450; column < 550; column++) {
                if (primeFieldpowers[2][row][column] != 0) {
                    outstring += Integer.toString(primeFieldpowers[2][row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        System.out.println("\n\n\n");
        for (int row = 0; row < 100; row++) {
            for (int column = 450; column < 550; column++) {
                if (primeFieldpowers[3][row][column] != 0) {
                    outstring += Integer.toString(primeFieldpowers[3][row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
    }
    public void outputPrimeField() {
        String outstring = "";
        for (int row = 0; row < 100; row++) {
            for (int column = 450; column < 550; column++) {
                if (primeField[row][column] != 0) {
                    outstring += Integer.toString(primeField[row][column]);
                } else {
                    outstring += " ";
                }
            }
            System.out.println(outstring);
            outstring = "";
        }
        for (int row = 0; row < 16; row++) {
            for (int column = 0; column < 16; column++) {
                outstring = "";
                for (int zee = 0; zee < 16; zee++) {
                    int ns = 256 * row + 16 * column + zee;
                    outstring += Integer.toString(primeRule[ns]) + "\t";
                }
                //System.out.println(outstring);
                outstring = "";
            }
            //System.out.println();
        }
    }
    public int[] generateExtension(int[] in, int elements){
        int newLength = in.length*elements*elements;
        int[][] extElemental = new int[4][newLength];
        int[] extWolfram = new int[newLength];
        for (int spot = 0; spot < newLength; spot++){
            for (int row = 0; row < 3; row++){
                extElemental[row][spot] = in[spot/(int)Math.pow(elements,row) % in.length];
                extElemental[3][spot] += (int)Math.pow(elements,row)*extElemental[row][spot];
            }
            extWolfram[spot] = in[extElemental[3][spot]];
        }
        return extWolfram;

    }
    public int[] generateMainRule() {
        int[] hexadecimal_rule_truth_table = new int[4096];
        //With[{rule = {{13, 3, 13}->12,
        // {6, _, 4}->15,
        // {10, _, 3|11}->15,
        // {13, 7, _}->8,
        // {13, 8, 7}->13,
        // {15, 8, _}->1,
        // {8, _, _}->7,
        // {15, 1, _}->2,
        // {_, 1, _}->1,
        // {1, _, _}->8,
        // {2|4|5, _, _}->13,
        // {15, 2, _}->4,
        // {_, 4, 8}->4,
        // {_, 4, _}->5,
        // {_, 5, _}->3,
        // {15, 3, _}->12,
        // {_, x:2|3|8, _}:> x,
        // {_, x:11|12, _} :> x - 1,
        // {11, _, _}->13,
        // {13, _, 1|2|3|5|6|10|11}-> 15,
        // {13, 0, 8}->15,
        // {14, _, 6|10}->15,
        // {10, 0|9|13, 6|10}->15,
        // {6, _, 6}->0,
        // {_, _, 10}->9,
        // {6|10, 15, 9}->14,
        // {_, 6|10, 9|14|15}->10,
        // {_, 6|10, _}->6,
        // {6|10, 15, _}->13,
        // {13|14, _, 9|15}->14,
        // {13|14, _, _}->13,
        // {_, _, 15}->15,
        // {_, _, 9|14}->9,
        // {_, _, _}->0},
        // init = {{10, 0, 4, 8}, 0}},
        primeRule = new int[4096];
        int n = 0;
        //With[{rule = {{13, 3, 13}->12,
        //#0
        n = 13 * 256 + 3 * 16 + 13;
        primeRule[n] = 12;
        // {6, _, 4}->15,
        //#1
        for (int middle = 0; middle < 16; middle++) {
            primeRule[6 * 256 + middle * 16 + 4] = 15;
        }
        // {10, _, 3|11}->15,
        //#2
        for (int middle = 0; middle < 16; middle++) {
            primeRule[10 * 256 + middle * 16 + 3] = 15;
            primeRule[10 * 256 + middle * 16 + 11] = 15;
        }
        // {13, 7, _}->8,
        //#3
        for (int right = 0; right < 16; right++) {
            primeRule[13 * 256 + 7 * 16 + right] = 8;
        }
        // {13, 8, 7}->13,
        //#4
        primeRule[13 * 256 + 8 * 16 + 7] = 13;
        // {15, 8, _}->1,
        //#5
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 8 * 16 + right] = 1;
        }
        // {8, _, _}->7,
        //#6
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[8 * 256 + middle * 16 + right] = 7;
            }
        }
        // {15, 1, _}->2,
        //#7
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 16 + right] = 2;
        }
        // {_, 1, _}->1,
        //#8
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 16 + right] = 1;
            }
        }
        // {1, _, _}->8,
        //#9
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[256 + middle * 16 + right] = 8;
            }
        }
        // {2|4|5, _, _}->13,
        //#10
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[2 * 256 + middle * 16 + right] = 13;
                primeRule[4 * 256 + middle * 16 + right] = 13;
                primeRule[5 * 256 + middle * 16 + right] = 13;
            }
        }
        // {15, 2, _}->4,
        //#11
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 2 * 16 + right] = 4;
        }
        // {_, 4, 8}->4,
        //#12
        for (int left = 0; left < 16; left++) {
            primeRule[left * 256 + 4 * 16 + 8] = 4;
        }
        // {_, 4, _}->5,
        //#13
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 4 * 16 + right] = 5;
            }
        }
        // {_, 5, _}->3,
        //#14
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 5 * 16 + right] = 3;
            }
        }
        // {15, 3, _}->12,
        //#15
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 3 * 16 + right] = 12;
        }
        // {_, x:2|3|8, _}:> x,
        //#16
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 2 * 16 + right] = 2;
                primeRule[left * 256 + 3 * 16 + right] = 3;
                primeRule[left * 256 + 8 * 16 + right] = 8;
            }
        }
        // {_, x:11|12, _} :> x - 1,
        //#17
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 11 * 16 + right] = 10;
                primeRule[left * 256 + 12 * 16 + right] = 11;
            }
        }
        // {11, _, _}->13,
        //#18
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[11 * 256 + middle * 16 + right] = 13;
            }
        }
        // {13, _, 1|2|3|5|6|10|11}-> 15,
        //#19
        for (int middle = 0; middle < 16; middle++) {
            primeRule[13 * 256 + middle * 16 + 1] = 15;
            primeRule[13 * 256 + middle * 16 + 2] = 15;
            primeRule[13 * 256 + middle * 16 + 3] = 15;
            primeRule[13 * 256 + middle * 16 + 5] = 15;
            primeRule[13 * 256 + middle * 16 + 6] = 15;
            primeRule[13 * 256 + middle * 16 + 10] = 15;
            primeRule[13 * 256 + middle * 16 + 11] = 15;
        }
        // {13, 0, 8}->15,
        //#20
        primeRule[13 * 256 + 8] = 15;
        // {14, _, 6|10}->15,
        //#21
        for (int middle = 0; middle < 16; middle++) {
            primeRule[14 * 256 + middle * 16 + 6] = 15;
            primeRule[14 * 256 + middle * 16 + 10] = 15;
        }
        // {10, 0|9|13, 6|10}->15,
        //#22
        primeRule[10 * 256 + 6] = 15;
        primeRule[10 * 256 + 10] = 15;
        primeRule[10 * 256 + 9 * 16 + 6] = 15;
        primeRule[10 * 256 + 9 * 16 + 10] = 15;
        primeRule[10 * 256 + 13 * 16 + 6] = 15;
        primeRule[10 * 256 + 13 * 16 + 10] = 15;
        // {6, _, 6}->0,
        //#23
        for (int middle = 0; middle < 16; middle++) {
            primeRule[256 * 6 + middle * 16 + 6] = 0;
        }
        // {_, _, 10}->9,
        //#24
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 10] = 9;
            }
        }
        // {6|10, 15, 9}->14,
        //#25
        primeRule[6 * 256 + 15 * 16 + 9] = 14;
        primeRule[10 * 256 + 15 * 16 + 9] = 14;
        // {_, 6|10, 9|14|15}->10,
        //#26
        for (int left = 0; left < 16; left++) {
            primeRule[left * 256 + 6 * 16 + 9] = 10;
            primeRule[left * 256 + 6 * 16 + 14] = 10;
            primeRule[left * 256 + 6 * 16 + 15] = 10;
            primeRule[left * 256 + 10 * 16 + 9] = 10;
            primeRule[left * 256 + 10 * 16 + 14] = 10;
            primeRule[left * 256 + 10 * 16 + 15] = 10;
        }
        // {_, 6|10, _}->6,
        //#27
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 6 * 16 + right] = 6;
                primeRule[left * 256 + 10 * 16 + right] = 6;
            }
        }
        // {6|10, 15, _}->13,
        //#28
        for (int right = 0; right < 16; right++) {
            primeRule[6 * 256 + 15 * 16 + right] = 13;
            primeRule[10 * 256 + 15 * 16 + right] = 13;
        }
        // {13|14, _, 9|15}->14,
        //#29
        for (int middle = 0; middle < 16; middle++) {
            primeRule[13 * 256 + middle * 16 + 9] = 14;
            primeRule[13 * 256 + middle * 16 + 15] = 14;
            primeRule[14 * 256 + middle * 16 + 9] = 14;
            primeRule[14 * 256 + middle * 16 + 15] = 14;
        }
        // {13|14, _, _}->13,
        //#30
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[13 * 256 + middle * 16 + right] = 13;
                primeRule[14 * 256 + middle * 16 + right] = 13;
            }
        }
        // {_, _, 15}->15,
        //#31
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 15] = 15;
            }
        }
        // {_, _, 9|14}->9,
        //#32
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 9] = 9;
                primeRule[left * 256 + middle * 16 + 14] = 9;
            }
        }
        // {_, _, _}->0},
        //#33
        for (int ns = 0; ns < 4096; ns++) {
            primeRule[ns] = 0;
        }
        // {_, _, 9|14}->9,
        //#32
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 9] = 9;
                primeRule[left * 256 + middle * 16 + 14] = 9;
            }
        }
        // {_, _, 15}->15,
        //#31
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 15] = 15;
            }
        }
        // {13|14, _, _}->13,
        //#30
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[13 * 256 + middle * 16 + right] = 13;
                primeRule[14 * 256 + middle * 16 + right] = 13;
            }
        }
        // {13|14, _, 9|15}->14,
        //#29
        for (int middle = 0; middle < 16; middle++) {
            primeRule[13 * 256 + middle * 16 + 9] = 14;
            primeRule[13 * 256 + middle * 16 + 15] = 14;
            primeRule[14 * 256 + middle * 16 + 9] = 14;
            primeRule[14 * 256 + middle * 16 + 15] = 14;
        }
        // {6|10, 15, _}->13,
        //#28
        for (int right = 0; right < 16; right++) {
            primeRule[6 * 256 + 15 * 16 + right] = 13;
            primeRule[10 * 256 + 15 * 16 + right] = 13;
        }
        // {_, 6|10, _}->6,
        //#27
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 6 * 16 + right] = 6;
                primeRule[left * 256 + 10 * 16 + right] = 6;
            }
        }
        // {_, 6|10, 9|14|15}->10,
        //#26
        for (int left = 0; left < 16; left++) {
            primeRule[left * 256 + 6 * 16 + 9] = 10;
            primeRule[left * 256 + 6 * 16 + 14] = 10;
            primeRule[left * 256 + 6 * 16 + 15] = 10;
            primeRule[left * 256 + 10 * 16 + 9] = 10;
            primeRule[left * 256 + 10 * 16 + 14] = 10;
            primeRule[left * 256 + 10 * 16 + 15] = 10;
        }
        // {6|10, 15, 9}->14,
        //#25
        primeRule[6 * 256 + 15 * 16 + 9] = 14;
        primeRule[10 * 256 + 15 * 16 + 9] = 14;
        // {_, _, 10}->9,
        //#24
        for (int left = 0; left < 16; left++) {
            for (int middle = 0; middle < 16; middle++) {
                primeRule[left * 256 + middle * 16 + 10] = 9;
            }
        }
        // {6, _, 6}->0,
        //#23
        for (int middle = 0; middle < 16; middle++) {
            primeRule[256 * 6 + middle * 16 + 6] = 0;
        }
        // {10, 0|9|13, 6|10}->15,
        //#22
        primeRule[10 * 256 + 6] = 15;
        primeRule[10 * 256 + 10] = 15;
        primeRule[10 * 256 + 9 * 16 + 6] = 15;
        primeRule[10 * 256 + 9 * 16 + 10] = 15;
        primeRule[10 * 256 + 13 * 16 + 6] = 15;
        primeRule[10 * 256 + 13 * 16 + 10] = 15;
        // {14, _, 6|10}->15,
        //#21
        for (int middle = 0; middle < 16; middle++) {
            primeRule[14 * 256 + middle * 16 + 6] = 15;
            primeRule[14 * 256 + middle * 16 + 10] = 15;
        }
        // {13, 0, 8}->15,
        //#20
        primeRule[13 * 256 + 8] = 15;
        // {13, _, 1|2|3|5|6|10|11}-> 15,
        //#19
        for (int middle = 0; middle < 16; middle++) {
            primeRule[13 * 256 + middle * 16 + 1] = 15;
            primeRule[13 * 256 + middle * 16 + 2] = 15;
            primeRule[13 * 256 + middle * 16 + 3] = 15;
            primeRule[13 * 256 + middle * 16 + 5] = 15;
            primeRule[13 * 256 + middle * 16 + 6] = 15;
            primeRule[13 * 256 + middle * 16 + 10] = 15;
            primeRule[13 * 256 + middle * 16 + 11] = 15;
        }
        // {11, _, _}->13,
        //#18
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[11 * 256 + middle * 16 + right] = 13;
            }
        }
        // {_, x:11|12, _} :> x - 1,
        //#17
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 11 * 16 + right] = 10;
                primeRule[left * 256 + 12 * 16 + right] = 11;
            }
        }
        // {_, x:2|3|8, _}:> x,
        //#16
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 2 * 16 + right] = 2;
                primeRule[left * 256 + 3 * 16 + right] = 3;
                primeRule[left * 256 + 8 * 16 + right] = 8;
            }
        }
        // {15, 3, _}->12,
        //#15
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 3 * 16 + right] = 12;
        }
        // {_, 5, _}->3,
        //#14
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 5 * 16 + right] = 3;
            }
        }
        //#13
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 4 * 16 + right] = 5;
            }
        }
        // {_, 4, 8}->4,
        //#12
        for (int left = 0; left < 16; left++) {
            primeRule[left * 256 + 4 * 16 + 8] = 4;
        }
        // {15, 2, _}->4,
        //#11
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 2 * 16 + right] = 4;
        }
        // {2|4|5, _, _}->13,
        //#10
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[2 * 256 + middle * 16 + right] = 13;
                primeRule[4 * 256 + middle * 16 + right] = 13;
                primeRule[5 * 256 + middle * 16 + right] = 13;
            }
        }
        // {1, _, _}->8,
        //#9
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[256 + middle * 16 + right] = 8;
            }
        }
        // {_, 1, _}->1,
        //#8
        for (int left = 0; left < 16; left++) {
            for (int right = 0; right < 16; right++) {
                primeRule[left * 256 + 16 + right] = 1;
            }
        }
        // {15, 1, _}->2,
        //#7
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 16 + right] = 2;
        }
        // {8, _, _}->7,
        //#6
        for (int middle = 0; middle < 16; middle++) {
            for (int right = 0; right < 16; right++) {
                primeRule[8 * 256 + middle * 16 + right] = 7;
            }
        }
        // {15, 8, _}->1,
        //#5
        for (int right = 0; right < 16; right++) {
            primeRule[15 * 256 + 8 * 16 + right] = 1;
        }
        // {13, 8, 7}->13,
        //#4
        primeRule[13 * 256 + 8 * 16 + 7] = 13;
        // {13, 7, _}->8,
        //#3
        for (int right = 0; right < 16; right++) {
            primeRule[13 * 256 + 7 * 16 + right] = 8;
        }
        // {10, _, 3|11}->15,
        //#2
        for (int middle = 0; middle < 16; middle++) {
            primeRule[10 * 256 + middle * 16 + 3] = 15;
            primeRule[10 * 256 + middle * 16 + 11] = 15;
        }
        // {6, _, 4}->15,
        //#1
        for (int middle = 0; middle < 16; middle++) {
            primeRule[6 * 256 + middle * 16 + 4] = 15;
        }
        //With[{rule = {{13, 3, 13}->12,
        //#0
        n = 13 * 256 + 3 * 16 + 13;
        primeRule[n] = 12;
        primeField = new int[1000][1000];
        primeField[0][498] = 10;
        primeField[0][500] = 4;
        primeField[0][501] = 8;
        for (int row = 1; row < 1000; row++) {
            for (int column = 1; column < 1000 - 1; column++) {
                primeField[row][column] = 256 * primeField[row - 1][column - 1] + 16 * primeField[row - 1][column] + primeField[row - 1][column + 1];
                primeField[row][column] = primeRule[primeField[row][column]];
            }
        }
        primeFieldpowers = new int[4][1000][1000];
        for (int row = 0; row < 1000; row++) {
            for (int column = 0; column < 1000; column++) {
                for (int power = 0; power < 4; power++) {
                    primeFieldpowers[power][row][column] = primeField[row][column] & ((int) Math.pow(2, power));
                }
            }
        }
        return primeRule;
    }
    /**
     *
     */
    public void generalWolframCode() {
        PermutationsFactoradic pf = new PermutationsFactoradic();
        CayleyDickson cd = new CayleyDickson();
        int[] tempCode = generateMainRule();
        int[] wolframCode = generateMainRule();
        for (int spot = 0; spot < wolframCode.length; spot++) {
            wolframCode[spot] = wolframCode[spot] / 2 % 2;
        }
        int[][] temp = new int[3][4096 * 256];
        for (int row = 0; row < 3; row++) {
            for (int spot = 0; spot < 4096 * 256; spot++) {
                temp[row][spot] = tempCode[spot / (int) Math.pow(16, row) % 4096];
            }
        }
        wolframCode = new int[4096 * 256];
        for (int spot = 0; spot < 4096 * 256; spot++) {
            int tot = 0;
            for (int row = 0; row < 3; row++) {
                tot += (int) Math.pow(16, row) * temp[row][spot];
            }
            wolframCode[spot] = wolframCode[tot];
        }
        Random rand = new Random();
        //number of bits used to address the Wolfram code
        int places = (int) (Math.log(wolframCode.length) / Math.log(2));
        int numFactors = 11;
        //list of permutations of length place
        //short[][] perms = pf.permSequences((short)places);
        int degreeFactorial = pf.factorial(places - 1);
        int placesFactorial = pf.factorial(places);
        degreeFactorial = 1;
        for (long factor = 2; factor <= places - 1; factor++) {
            degreeFactorial *= factor;
        }
        System.out.println("placesFactorial: " + placesFactorial);
        //used as operand in the multiplications
        int multResult = 0;
        //active local solution number
        //set of permuted axes used as factors currently being considered
        int[][] factors = new int[numFactors + 1][wolframCode.length];
        //set of permutations currently being considered
        int[] ps = new int[numFactors];
        int maxAttempts = 500000;
        int maxTableAttempts = 20;
        int cdz = 0;
        int cdo = 0;
        int[][] specificPermutations = new int[numFactors][places];
        int longestStreak = 0;
        int[][] table = new int[4096][4096];
        int factor = 0;
        int input = 0;
        int place = 0;
        int tableAttempt = 0;
        for (int row = 0; row < 4096; row++) {
            for (int column = 0; column < 4096; column++) {
                table[row][column] = row ^ column;
            }
        }
        int degree = places - 1;
        mainLoop:
        for (int l = 0; l < maxAttempts; l++) {
            numFactors = rand.nextInt(3, 13);
            degree = rand.nextInt(places - 1, places + 5);
            ps = new int[numFactors];
            specificPermutations = new int[numFactors][places];
            for (factor = 0; factor < numFactors; factor++) {
                ps[factor] = rand.nextInt(0, placesFactorial);
                specificPermutations[factor] = pf.specificPermutation(places, ps[factor]);
                //System.out.println(Arrays.toString(specificPermutations[factor]));
            }
            //applies the permutations of each set of axes' indexes
            factors = new int[numFactors + 1][wolframCode.length];
            for (input = 0; input < wolframCode.length; input++) {
                for (factor = 0; factor < numFactors; factor++) {
                    for (place = 0; place < places; place++) {
                        factors[factor][input] += (int) Math.pow(2, place) * (input / (int) Math.pow(2, specificPermutations[factor][place]) % 2);
                    }
                }
            }
            //System.out.println("Starting table loop");
            tableLoop:
            for (tableAttempt = 0; tableAttempt < maxTableAttempts; tableAttempt++) {
                cdz = rand.nextInt(0, degreeFactorial);
                cdo = rand.nextInt(0, degreeFactorial);
                //multiplication using given parameters
                for (input = 0; input < wolframCode.length; input++) {
                    multResult = factors[0][input];
                    for (factor = 1; factor < numFactors; factor++) {
                        multResult = cd.multiplyUnitHyperComplex(degree, multResult, factors[factor][input], cdz, cdo);
                        //multResult = table[multResult][factors[factor][input]];
                    }
                    factors[numFactors][input] = multResult;
                    //trims the result to size, the table size and the binary input array size
                    //System.out.println("Correct");
                    if (wolframCode[multResult] != wolframCode[input]) {
                        //System.out.println("Incorrect\n\n");
                        //if this set of parameters does not reproduce the given truth table, exit the loops
                        continue tableLoop;
                    }
                    if (input > longestStreak) {
                        longestStreak = input;
                        System.out.println("New record streak! " + longestStreak);
                        System.out.println("numFactors: " + numFactors + " degree: " + degree);
                    }
                }
                System.out.println("Holee Shit");
                System.out.println("ps: " + Arrays.toString(ps));
                System.out.println("factors: " + factors);
                System.out.println("cdz, cdo: " + cdz + "," + cdo);
                break mainLoop;
            }
            if (l % 1000 == 0) {
                System.out.println("Attempts: " + l);
            }
        }
    }
    /**
     * @param wolframCode User-supplied Wolfram
     * @param numFactors Number of factors to use, number of dimensions in the multiplication ta
     */
    public void generalWolframCode(int[] wolframCode, int numFactors) {
        PermutationsFactoradic pf = new PermutationsFactoradic();
        CayleyDickson cd = new CayleyDickson();

        Random rand = new Random();
        //number of bits used to address the Wolfram code
        int places = (int) (Math.log(wolframCode.length) / Math.log(2));
        System.out.println("wolframCode.length: " + wolframCode.length);
        System.out.println("places: " + places);
        //list of permutations of length place
        //short[][] perms = pf.permSequences((short)places);
        int degreeFactorial = pf.factorial(places - 1);
        int placesFactorial = pf.factorial(places);
        degreeFactorial = 1;
        for (long factor = 2; factor <= places - 1; factor++) {
            degreeFactorial *= factor;
        }
        System.out.println("placesFactorial: " + placesFactorial);
        //used as operand in the multiplications
        int multResult = 0;
        //active local solution number
        //set of permuted axes used as factors currently being considered
        int[][] factors = new int[numFactors + 1][wolframCode.length];
        //set of permutations currently being considered
        int[] ps = new int[numFactors];
        int maxAttempts = 500000;
        int maxTableAttempts = 1;
        int cdz = 0;
        int cdo = 0;
        int[][] specificPermutations = new int[numFactors][places];
        int longestStreak = 0;
        int[][] table = new int[4096][4096];
        int factor = 0;
        int input = 0;
        int place = 0;
        int tableAttempt = 0;
        for (int row = 0; row < 4096; row++) {
            for (int column = 0; column < 4096; column++) {
                table[row][column] = row ^ column;
            }
        }
        int[][] codePermutations = pf.permSequences(8);
        int degree = places - 1;
        int maxPerm = pf.factorial(8);
        mainLoop:
        for (int l = 0; l < maxAttempts; l++) {
            numFactors = rand.nextInt(256, 257);
            degree = rand.nextInt(7 , 8);
            ps = new int[numFactors];
            specificPermutations = new int[numFactors][places];
            for (factor = 0; factor < numFactors; factor++) {
                ps[factor] = rand.nextInt(0, placesFactorial);
                specificPermutations[factor] = pf.specificPermutation(places, ps[factor]);
                //System.out.println(Arrays.toString(specificPermutations[factor]));
            }
            //applies the permutations of each set of axes' indexes
            factors = new int[numFactors + 1][wolframCode.length];
            for (input = 0; input < wolframCode.length; input++) {
                for (factor = 0; factor < numFactors; factor++) {
                    for (place = 0; place < places; place++) {
                        factors[factor][input] += (int) Math.pow(2, place) * (input / (int) Math.pow(2, specificPermutations[factor][place]) % 2);
                    }
                }
            }
            //System.out.println("Starting table loop");
            tableLoop:
            for (tableAttempt = 0; tableAttempt < maxTableAttempts; tableAttempt++) {
                cdz = rand.nextInt(0, degreeFactorial);
                cdo = rand.nextInt(0, degreeFactorial);
                int cdzz = rand.nextInt(0,degreeFactorial);
                int cdoo = rand.nextInt(0,degreeFactorial);
                int p = rand.nextInt(0,maxPerm);
                //multiplication using given parameters
                for (input = 0; input < wolframCode.length; input++) {
                    multResult = factors[0][input];
                    for (factor = 1; factor < numFactors; factor++) {
                        multResult = cd.multiplyUnitHyperComplex(degree, multResult, factors[factor][input], cdz, cdo);
                        multResult = cd.multiplyUnitHyperComplex(degree, multResult, factors[factor][input],cdzz,cdoo);
                        //multResult = table[multResult][factors[factor][input]];
                    }
                    factors[numFactors][input] = multResult;
                    int tot = 0;
                    for (int power = 0; power < 8; power++){
                        tot += (int)Math.pow(2,power)*(input/(int)Math.pow(2,codePermutations[p][power]) % 2);
                    }
                    //trims the result to size, the table size and the binary input array size
                    //System.out.println("Correct");
                    if (wolframCode[multResult%256] != wolframCode[tot]) {
                        //System.out.println("Incorrect\n\n");
                        //if this set of parameters does not reproduce the given truth table, exit the loops
                        continue tableLoop;
                    }
                    if (input > longestStreak) {
                        longestStreak = input;
                        System.out.println("New record streak! " + longestStreak);
                        System.out.println("numFactors: " + numFactors + " degree: " + degree);
                    }
                }
                System.out.println("Holee Shit");
                System.out.println("ps: " + Arrays.toString(ps));
                System.out.println("factors: " + factors);
                System.out.println("cdz, cdo: " + cdz + "," + cdo);
                break mainLoop;
            }
            if (l % 1000 == 0) {
                System.out.println("Attempts: " + l);
            }
        }
    }
//    public void eca() {
//        BasicECA basicECA = new BasicECA();
//        int[][] table = new int[4][4];
//        int[] extRule = new int[16];
//        for (int n = 0; n < 256; n++) {
//            System.out.println("n: " + n);
//            extRule = Arrays.copyOfRange(basicECA.ruleExtension(n)[4], 0, 16);
//            for (int input = 0; input < 16; input++) {
//                table[input / 4][input % 4] = extRule[input];
//            }
//            for (int row = 0; row < 4; row++) {
//                System.out.println(Arrays.toString(table[row]));
//            }
//            System.out.println();
//        }
//        int[][] ecaRot = new int[256][4];
//        for (int n = 0; n < 256; n++) {
//            ecaRot[n][0] = n;
//            int[] rule = basicECA.ruleTruthTable[n];
//            for (int rot = 0; rot < 3; rot++) {
//                int[] temp = new int[8];
//                for (int input = 0; input < 8; input++) {
//                    int tot = 0;
//                    for (int power = 0; power < 3; power++) {
//                        tot += (int) Math.pow(2, power) * (input / (int) Math.pow(2, (power + rot) % 3) % 2);
//                    }
//                    temp[input] = rule[tot];
//                }
//                int tot = 0;
//                for (int input = 0; input < 8;  input++){
//                    tot += (int)Math.pow(2,input)*temp[input];
//                }
//                ecaRot[n][rot+1] = tot;
//            }
//        }
//        for (int n= 0; n < 256; n++){
//            System.out.println("n: " + n + " " + Arrays.toString(ecaRot[n]));
//        }
//    }
    public int[] generateConwayWolfram(){
        int[] conway = new int[512];
        for (int n = 0; n < 512; n++){
            int[] nn = new int[9];
            for (int power = 0; power < 9; power++){
                nn[power] = n/(int)Math.pow(2,power) % 2;
            }
            int tot = 0;
            for (int power = 1; power < 8; power++){
                tot += nn[power];
            }
            if (nn[0] == 1) {
                if (tot < 2) conway[n] = 0;
                if (tot == 2 || tot == 3) conway[n] = 1;
                if (tot > 3) conway[n] = 0;
            } else {
                if (tot == 3){
                    conway[n] = 1;
                } else {
                    conway[n] = 0;
                }
            }

        }
        for (int index = 0; index < 512; index++){
            if (index % 64 == 0) System.out.print("\n");
            System.out.print(conway[index]);
        }
        conway = Arrays.copyOfRange(conway,0,256);
        return conway;
    }
    public void breakdownConway(){
        PermutationsFactoradic pf = new PermutationsFactoradic();
        int[] conway = generateConwayWolfram();
        int[] permConway = new int[conway.length];
        int[][] slices;
        int length = conway.length;
        int[][] perms = pf.permSequences(8);
        int degreeFactorial = pf.factorial(8);
        Random rand = new Random();
        for (int attempt = 0; attempt < 10; attempt++){
            int p = rand.nextInt(0,degreeFactorial);
            for (int spot = 0; spot < length; spot++){
                int tot = 0;
                for (int power = 0; power < 8; power++){
                    tot += (int)Math.pow(2,power)*(spot/(int)Math.pow(2,perms[p][power]) % 2);
                }
                permConway[spot] = conway[tot];

            }
            slices = new int[8][256];
            for (int row = 1; row < 8; row++){

                for (int spot = 0; spot < 256; spot++){
                    slices[row][spot/((int)Math.pow(2,row))] += (int)Math.pow(2,spot%(row+1))*permConway[spot];
                }
            }
            for (int row = 0; row < 8; row++){
                System.out.println(Arrays.toString(slices[row]));
            }
            System.out.println();


        }
        System.out.println((int)Math.pow(2,0));
    }
    public void checkThirty(){
        BasicECA beca = new BasicECA();
        int[][] twos = new int[13][18];
        int[][] rules = beca.ruleExtension(30);
        for (int row = 3; row < 11; row+=2){
            twos[row][0] = rules[row][0];
            for (int spot = 0; spot < row; spot++){
                twos[row][spot+1] = rules[row][(int)Math.pow(2,spot)];
            }
        }
        for (int row = 3; row < 11; row+=2){
            System.out.println(Arrays.toString(Arrays.copyOfRange(twos[row],0,row)));
        }
        System.out.println(Arrays.toString(Arrays.copyOfRange(rules[4],0,16)));
        for (int row = 3; row < 10; row++){
            System.out.println(Arrays.toString(rules[row]));
        }
        int[][] pt = new int[11][11];
        for (int row = 3; row < 10; row++){
            pt[row][0] = rules[row][0];
            for (int spot = 0; spot < row; spot++){
                pt[row][spot+1] = rules[row][(int)Math.pow(2,spot)];
            }
        }
        System.out.println();
        for (int row = 3; row < 10; row++){
            System.out.println(Arrays.toString(pt[row]));
        }
    }
}

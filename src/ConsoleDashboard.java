
import java.util.Arrays;
/**
 *  Console output
 */
public class ConsoleDashboard {
    /**
     * Generates Fano triplet octonions and E8 lattice root automorphisms
     */
    public Fano fano = new Fano();
    /**
     * Currently being displayed multiplication table or Galois field
     */
    public int[][] display = new int[1][1];
    /**
     * Text display panel
     */
    SwingTextPanel stp;

    /**
     * Initializes external components
     * @param inPanel existing text panel
     */
    public ConsoleDashboard(SwingTextPanel inPanel) {
        stp = inPanel;

    }

    /**
     * Displays a specific Fano octonion table out of 480
     *
     * @param table which table out of 480
     * @return a String of the output
     */
    public String displayFano(int table) {
        fano = new Fano();
        fano.fanoGenerate();
        display = fano.fanoTables[table];
        System.out.println("Fano table " + table);
        for (int row = 0; row < 16; row++) {
            System.out.println(Arrays.toString(display[row]));
        }
        System.out.println("By places");
        for (int power = 0; power < 4; power++) {
            System.out.println("Power " + power);
            for (int row = 0; row < 16; row++) {
                for (int column = 0; column < 16; column++) {
                    System.out.print((display[row][column] / ((int) Math.pow(2, power)) % 2));
                }
                System.out.print("\n");
            }
        }
        System.out.println();
        String outstring = "Fano table: " + table + "\n";
        for (int row = 0; row < 16; row++) {
            outstring += (Arrays.toString(display[row])) + "\n";
        }
        outstring += ("By places\n");
        for (int power = 0; power < 4; power++) {
            outstring += ("Power " + power) + "\n";
            for (int row = 0; row < 16; row++) {
                for (int column = 0; column < 16; column++) {
                    outstring += ((display[row][column] / ((int) Math.pow(2, power)) % 2));
                }
                outstring += ("\n");
            }
        }
        outstring += "\n";
        return outstring;
    }
    /**
     * Displays a specific hypercomplex multiplication table with Cayley-Dickson permutation numbers (cdz, cdo)
     *
     * @param degree degree hypercomplex, 2 = quaternions, 3 = octonions, 4  = sedonions, etc
     * @param cdz    Cayley-Dickson permutations used as splitting bit, down in recursion
     * @param cdo    Cayley-Dickson permutations used as recombination bit, up in recursion
     * @return a String of the output
     */
    public String displayCayleyDickson(int degree, int cdz, int cdo) {
        System.out.println("hypercomplex degree " + degree);
        System.out.println("Cayley Dickson permutation numbers (" + cdz + "," + cdo + ")");
        CayleyDickson cd = new CayleyDickson();
        display = cd.specificTable(degree, cdz, cdo);
        for (int row = 0; row < (int) Math.pow(2, degree + 1); row++) {
            System.out.println(Arrays.toString(display[row]));
        }
        for (int power = 0; power < degree + 1; power++) {
            System.out.println("Power " + power);
            for (int row = 0; row < display.length; row++) {
                for (int column = 0; column < display.length; column++) {
                    System.out.print((display[row][column] / (int) Math.pow(2, power) % 2));
                }
                System.out.print("\n");
            }
        }
        System.out.println();
        String outstring = "";
        outstring += ("hypercomplex degree " + degree) + "\n";
        outstring += ("Cayley Dickson permutation numbers (" + cdz + "," + cdo + ")")+ "\n";
        for (int row = 0; row < (int) Math.pow(2, degree + 1); row++) {
            outstring += (Arrays.toString(display[row]))+ "\n";
        }
        for (int power = 0; power < degree + 1; power++) {
            outstring += ("Power " + power)+ "\n";
            for (int row = 0; row < display.length; row++) {
                for (int column = 0; column < display.length; column++) {
                    outstring += ((display[row][column] / (int) Math.pow(2, power) % 2));
                }
                outstring += ("\n");
            }
        }
        outstring += "\n";
        return outstring;
    }
    /**
     * Displays a specific Galois multiplication field
     *
     * @param m must be prime to work correctly, however it is not error-corrected yet and
     *          will output garbage
     * @param n power of m, m^n, GF(m,n)
     * @return a String of the output
     */
    public String displayGalois(int m, int n) {
        System.out.println("Galois Field");
        System.out.println("prime " + m + ", power" + n);
        GaloisFields qls = new GaloisFields();
        display = qls.generateTable(m, n, false);
        for (int row = 0; row < (int) Math.pow(m, n); row++) {
            System.out.println(Arrays.toString(display[row]));
        }
        System.out.println();
        String outstring = "Galois Field, prime: " + m + " , power: " + n + "\n";
        for (int row = 0; row < (int)Math.pow(m,n); row++){
            outstring += Arrays.toString(display[row])+"\n";
        }
        outstring += "\n";
        return outstring;
    }


    /**
     * Outputs permutation compositions in both list and grid form
     *
     * @param length length of array to permute
     * @return the symmetric group in String form
     */
    public String permutationsOfPermutations(int length) {
        System.out.println("Permutations of permutations");
        PermutationsFactoradic pf = new PermutationsFactoradic();
        int[][] pop = pf.permsOfPermsGrid(length);

        System.out.println("Compositions of Permutations Interactions");
        for (int row = 0; row < pf.factorial(length); row++) {
            System.out.println(Arrays.toString(pop[row]));
        }
        System.out.println();
        String outstring = "";
        for (int row = 0; row < pf.factorial(length); row++){
            outstring += Arrays.toString(pop[row]) + "\n";
        }
        return outstring;
    }


    /**
     * Calls Fano.fanoTest(), which does comparisons between permuted Cayley-Dickson octonions,
     * Fano numbering triplets, and the 480 automorphisms produced by the E8 roots of each of these
     */
    public void fanoTest() {
        fano.fanoTest();
        System.out.println();
    }
    /**
     * Displays on a Swing text area various multiplication tables and permutation compositions
     * @param degree degree hypercomplex, 2 = quaternions, 3 = octonions, etc.
     * @param cdz Cayley-Dickson down recursion factoradic
     * @param cdo Cayley-Dickson up recursion factoradic
     * @param table Fano table number, 0-480
     * @param m Galois field (m,_), a prime number
     * @param n prime m to the n power
     * @param length length of permutation alphabet
     */
    public void displaySet(int degree, int cdz, int cdo, int table, int m, int n, int length) {
        stp.jTextArea.setText("");
        stp.jTextArea.append("\n"+"hypercomplex degree " + degree);
        stp.jTextArea.append("\n"+"Cayley Dickson permutation numbers (" + cdz + "," + cdo + ")");
        CayleyDickson cd = new CayleyDickson();
        display = cd.specificTable(degree, cdz, cdo);
        for (int row = 0; row < (int) Math.pow(2, degree + 1); row++) {
            stp.jTextArea.append("\n"+Arrays.toString(display[row]));
        }
//        int[][][] byPower = new int[degree + 1][display.length][display.length];
//        for (int power = 0; power < degree + 1; power++) {
//            stp.jTextArea.append("\n"+"Power " + power);
//            for (int row = 0; row < display.length; row++) {
//                for (int column = 0; column < display.length; column++) {
//                    int element = (display[row][column] / (int) Math.pow(2, power) % 2);
//                    stp.jTextArea.append(Integer.toString(element));
//                }
//                stp.jTextArea.append("\n");
//            }
//        }
        stp.jTextArea.append("\n");
        fano = new Fano();
        fano.fanoGenerate();
        display = fano.fanoTables[table];
        stp.jTextArea.append("\n"+"Fano table " + table);
        for (int row = 0; row < 16; row++) {
            stp.jTextArea.append("\n"+Arrays.toString(display[row]));
        }
//        stp.jTextArea.append("\n"+"By places");
//        for (int power = 0; power < 4; power++) {
//            stp.jTextArea.append("\n"+"Power " + power);
//            for (int row = 0; row < 16; row++) {
//                for (int column = 0; column < 16; column++) {
//                    stp.jTextArea.append(Integer.toString(display[row][column] / ((int) Math.pow(2, power)) % 2));
//                }
//                stp.jTextArea.append("\n");
//            }
//        }
        stp.jTextArea.append("\n");
        stp.jTextArea.append("\n"+"Galois Field");
        stp.jTextArea.append("\n"+"prime " + m + ", power" + n);
        GaloisFields qls = new GaloisFields();
        display = qls.generateTable(m, n, false);
        for (int row = 0; row < (int) Math.pow(m, n); row++) {
            stp.jTextArea.append("\n"+Arrays.toString(display[row]));
        }
        stp.jTextArea.append("\n");
        stp.jTextArea.append("\n"+"Permutations of permutations");
        PermutationsFactoradic pf = new PermutationsFactoradic();
        int[][] pop = pf.permsOfPermsGrid(length);

        stp.jTextArea.append("\n"+"Compositions of Permutations Interactions");
        for (int row = 0; row < pf.factorial(length); row++) {
            stp.jTextArea.append("\n"+Arrays.toString(pop[row]));
        }
        stp.jTextArea.append("\n");
    }
    /**
     * Calls the function that compares permuted Cayley-Dickson multiplication tables against other permuted Cayley-DIckson multiplication tables and displays it
     * @param degree degree hypercomplex, 2 = quaternions, 3 = octonions, etc
     */
    public void compareCayleyDickson(int degree){
        CayleyDickson cd = new CayleyDickson();
        display = cd.cdCompareAgainstPoP(degree);
        String outstring = "";
        int degreeFactorial = cd.pf.factorial(degree);
        for (int row = 0; row < degreeFactorial*degreeFactorial; row++){
            outstring += Arrays.toString(display[row]) + "\n";
        }
        int[][][] byPlaces = new int[2][degreeFactorial*degreeFactorial][degreeFactorial*degreeFactorial];
        for (int place = 0; place < 2; place++){
            for (int row = 0; row < degreeFactorial*degreeFactorial; row++){
                for (int column = 0; column < degreeFactorial*degreeFactorial; column++){
                    byPlaces[place][row][column] = display[row][column]/(int)Math.pow(degreeFactorial,place) % degreeFactorial;
                }
            }
        }
        outstring += "By places, (cdz,cdo),(cdzz,cdoo) interacting as permutation products (cdz,cdzz),(cdo,cdoo)\n";
        outstring += "One's place\n";
        for (int row = 0; row < degreeFactorial*degreeFactorial; row++){
            outstring += Arrays.toString(byPlaces[0][row])+"\n";
        }
        outstring += "degreeFactorial's place\n";
        for (int row = 0; row < degreeFactorial*degreeFactorial; row++){
            outstring += Arrays.toString(byPlaces[1][row])+"\n";
        }
        stp.jTextArea.setText(outstring);

    }

}

import java.util.Arrays;
/**
 * Standard Java Main class
 *
 * @author Daniel W McKinley
 */
public class Main {
    /**
     * Main class of this project
     *
     * @param args Command line arguments
     */
    public static void main(String[] args)  {
//        System.out.println(Math.pow(0,0));
//        System.out.println(Math.pow(0,1));
//        System.out.println(Math.pow(0,.5));
//        System.out.println((Math.pow(2,-1)));
//        System.out.println((4%(Math.PI)));
//
////        //SwingDashboard sd = new SwingDashboard();
////        ECAioTruthTables eio = new ECAioTruthTables();
////        //eio.checkECAforComplex();
////        //eio.checkNinety(4);
////        //eio.checkECAforComplex();
////        eio.checkECAforComplex(8);
////        eio.checkNinety(4);
//        //SwingDashboard sd = new SwingDashboard();
//        Fano fano = new Fano();
//        //fano.sedonionFanoGenerate();
       BasicECA beca = new BasicECA();
//        beca.ruleClassify();
//        //RuleImageCollection ric = new RuleImageCollection();
//        //ric.runCollection();
//        //ric.getImages(8,54);
//        //beca.reverse(102);
//        //beca.checkReverses();
//        //beca.reverse(90);
//        CayleyDickson cd = new CayleyDickson();
//        RuleImageCollection ric = new RuleImageCollection();
//        //ric.runLogicCollection();
//        //htmLdashboard.generateLogicIndexFiles();
//        int[][] cousins = beca.cousins();
//        //ric.runCollectionNormalizations();
               HTMLdashboard htmLdashboard = new HTMLdashboard();
//
        //htmLdashboard.generateIndexFilesWithNormalizations();
//        //RuleImageCollection ric = new RuleImageCollection();
//       // ric.runCollectionNormalizationsLogic();
        //htmLdashboard.generateIndexFilesWithNormalizationsLogic();
        //htmLdashboard.generateIndexFilesWithNormalizations();
        int[][] axes = new int[6][8];
        int[][] perms = PermutationsFactoradic.permSequences(3);
        for (int axis = 0; axis < 6; axis++){
            for (int spot = 0; spot  < 8; spot++){
                int tot = 0;
                for (int power = 0; power < 3; power++){
                    tot += (int)Math.pow(2,power)*(spot/(int)Math.pow(2,perms[axis][power]) % 2);
                }
                axes[axis][spot] = tot;
            }

        }

        System.out.println();
        for (int axis = 0; axis < 6; axis++){
            System.out.println("Permutation: " + axis + ", " + Arrays.toString(axes[axis]));
        }
        //htmLdashboard.generateDirectory("");
        //htmLdashboard.generateIndexFilesWithNormalizationsLogic();
        //htmLdashboard.generateIndexFilesWithNormalizations();
        SwingDashboard swingDashboard = new SwingDashboard();
    }


}

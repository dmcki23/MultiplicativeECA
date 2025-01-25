import java.util.Arrays;
/**
 * Standard Java Main class
 *
 */
public class Main {
    /**
     * Main class of this project
     *
     * @param args Command line arguments
     */
    public static void main(String[] args)  {
        GaloisFields galoisFields = new GaloisFields();
        GaloisScratchPaper galoisScratchPaper = new GaloisScratchPaper();
        System.out.println("depth: " + galoisScratchPaper.manageCheckDepth(galoisFields.generateTable(5,1,false),7));

        //SwingDashboard swingDashboard = new SwingDashboard();
        //Examples examples = new Examples();
        //examples.classFourFocus();
        //examples.additiveFocus();

    }


}

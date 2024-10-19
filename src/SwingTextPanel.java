import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
/**
 * Swing text output in a form copyable to a clipboard, JARs don't have a default console to output text to
 */
public class SwingTextPanel extends JPanel {
    /**
     * Outputs text that can be copied to the clipboard
     */
    JTextArea jTextArea;
    /**
     * Swing component used to display this panel
     */
    JFrame jFrame;
    /**
     * Class that manages brute-forcing possible permutation groups
     */
    ECAasMultiplication ecam;
    /**
     * A solution from ECAasMultiplication
     */
    ValidSolution solution;
    /**
     * Initializes Swing parameters
     * @param inECAM Current ECAasMultiplication instance
     */
    public SwingTextPanel(ECAasMultiplication inECAM){
        ecam = inECAM;
        jFrame = new JFrame();
        jFrame.setSize(500,500);
        jFrame.setLocation(0,0);
        jFrame.setTitle("Text output");

        jTextArea = new JTextArea("",60,60);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.add(jScrollPane);
        jFrame.add(this);
        jFrame.pack();
        jFrame.setVisible(true);
        
    }
    /**
     * Displays a valid solution
     * @param solution any valid ECAasMultiplication solution
     */
    public void displayValidSolution(ValidSolution solution){
//        String outstring = "";
//        jTextArea.setText("");
//        jTextArea.setText("ABCDE");
//        jTextArea.append ("ValidSolution"+" ");
//        jTextArea.append ("Wolfram code: " + Arrays.toString(solution.wolframCode)+" ");
//        int[] permutedMultiplicationWolframCode = new int[solution.wolframCode.length];
//        for (int factor = 0; factor <= solution.numFactors; factor++){
//            if (factor!= solution.numFactors){
//                jTextArea.append ("Permutation: " + solution.permutationSet[factor] + "\t Permuted Axis: " + Arrays.toString(solution.factors[factor])+" ");
//                jTextArea.append ("\t times"+" ");
//            } else {
//                jTextArea.append ("-----------------"+" ");
//                jTextArea.append ("Equals:                                              " + Arrays.toString(solution.factors[factor])+" ");
//                for (int spot = 0; spot < permutedMultiplicationWolframCode.length; spot++){
//                    permutedMultiplicationWolframCode[spot] = solution.factors[solution.numFactors][spot];
//                    permutedMultiplicationWolframCode[spot] = solution.wolframCode[permutedMultiplicationWolframCode[spot]];
//                }
//                jTextArea.append ("Apply Wolfram code to multiplication result"+" ");
//                jTextArea.append ("Equals:                                              "+Arrays.toString(permutedMultiplicationWolframCode)+" ");
//                jTextArea.append ("Original Wolfram code:                  "+Arrays.toString(solution.wolframCode)+" ");
//            }
//        }
//        jTextArea.append ("Multiplication table type: " + solution.whichMultTable+" ");
//        jTextArea.append ("2D multiplication table used: " +" ");
//        for (int row = 0; row < solution.multTable.length; row++){
//            jTextArea.append (Arrays.toString(solution.multTable[row])+" ");
//        }
//        jTextArea.append ("numFactors: " + solution.numFactors + " numBits: " + solution.numBits + " "+" ");
//
//        for (int row  = 0; row < solution.numBits; row++){
//            jTextArea.append (solution.polynomialString[row]+" ");
//            jTextArea.append( " ");
//
//        }
//        jTextArea.setText(outstring);
//        jFrame.repaint();

        //solution.polynomial = post.generatePolynomial(solution);
        
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (solution == null) return;
        String outstring = "";
        jTextArea.setText("");
        jTextArea.append ("ValidSolution"+"\n");
        jTextArea.append ("Wolfram code: " + Arrays.toString(solution.wolframCode)+"\n");
        int[] permutedMultiplicationWolframCode = new int[solution.wolframCode.length];
        for (int factor = 0; factor <= solution.numFactors; factor++){
            if (factor!= solution.numFactors){
                jTextArea.append ("Permutation: " + solution.permutationGroup[factor] + "\t Permuted Axis: " + Arrays.toString(solution.factors[factor])+"\n");
                jTextArea.append ("\t times"+"\n");
            } else {
                jTextArea.append ("-----------------"+"\n");
                jTextArea.append ("Equals:                                              " + Arrays.toString(solution.factors[factor])+"\n");
                for (int spot = 0; spot < permutedMultiplicationWolframCode.length; spot++){
                    permutedMultiplicationWolframCode[spot] = solution.factors[solution.numFactors][spot];
                    permutedMultiplicationWolframCode[spot] = solution.wolframCode[permutedMultiplicationWolframCode[spot]];
                }
                jTextArea.append ("Apply Wolfram code to multiplication result"+"\n");
                jTextArea.append ("Equals:                                              "+Arrays.toString(permutedMultiplicationWolframCode)+"\n");
                jTextArea.append ("Original Wolfram code:                  "+Arrays.toString(solution.wolframCode)+"\n");
            }
        }
        jTextArea.append("\nPermutation composition product: " + solution.permGroupProduct + ", inverse: " + solution.permGroupProductInverse+"\n");
        jTextArea.append ("\nMultiplication table type: " + solution.whichMultTable+"\n");
        jTextArea.append ("2D multiplication table used: " +"\n");
        for (int row = 0; row < solution.multTable.length; row++){
            jTextArea.append (Arrays.toString(solution.multTable[row])+"\n");
        }
        jTextArea.append ("\nnumFactors: " + solution.numFactors + " numBits: " + solution.numBits + "\n"+"\n");
        int polySum = 0;
        for (int row = 0; row< solution.numBits; row++) {
            for (int spot = 0; spot < solution.polynomial.length; spot++) {
                    polySum += solution.polynomial[solution.numBits][spot][row];

            }
        }
        for (int row  = 0; row < solution.numBits; row++){
            jTextArea.append (solution.polynomialString[row]+"\n"+"Coefficient sums = " + polySum);
            jTextArea.append( "\n");

        }
        //jTextArea.setText(outstring);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Each cell in the output of the standard additive Wolfram code output has a value that is the weighted binary sum of its input neighborhood, if the Wolfam code is [1,1,1,1,0,0,0,0] the neighborhoods can be one of [0,1,2,3,4,5,6,7]. A Multiplications A ValidSolution from ECAMspecific permutes this neighborhood for each permutation in the set, becoming a set of binary hypercomplex factors for each cell. This class displays that information layer-by-layer
 */
public class SwingBinaryFactorLayers extends JPanel {
    /**
     * Permuted neighborhood factor layer selection
     */
    JComboBox layerBox;
    /**
     * Updates the display with the selected layer
     */
    JButton refreshButton;
    /**
     * Displays output in text form
     */
    JTextArea jTextArea;
    /**
     * Currently selected layer
     */
    int activeLayer;
    /**
     * Current active solution
     */
    ValidSolution solution;
    /**
     * Permuted neighborhood hypercomplex factor set, by layers, layers[layer][row][column], these are all permutations of additiveLayer, layers[layer=numFactors][][] is the multiplication result layer
     */
    int[][][] layers;
    /**
     * Additive Wolfram code neighborhood value
     */
    int[][] additiveLayer;
    /**
     * Initializes Swing components, has button action
     */
    public SwingBinaryFactorLayers() {
        ScrollPane scrollPane = new ScrollPane();

        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        scrollPane.add(jTextArea);

        activeLayer = 0;
        layerBox = new JComboBox();
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateText();
            }
        });
        JFrame frame = new JFrame();

        frame.setTitle("Additive Wolfram output into constituent permuted neighborhood factors, as layers");
        //JPanel panel = new JPanel();
       // panel.add(layerBox);
        //panel.add(refreshButton);
        //this.add(panel);
        //frame.add(layerBox);
        //frame.add(refreshButton);
        //frame.add(panel);
        //frame.add(layerBox);
        //frame.add(refreshButton);
        this.add(layerBox);
        this.add(refreshButton);
        this.add(scrollPane);
        frame.add(this);
        //frame.add(scrollPane);

        frame.pack();
        frame.setVisible(true);
       //this.setVisible(true);
        scrollPane.setSize(800,300);
        frame.setSize(1000, 400);
    }
    /**
     * Updates the layer selection box with solution
     */
    public void updateBox() {
        layerBox.removeAllItems();
        for (int layer = 0; layer <= solution.numFactors; layer++) {
            if (layer == solution.numFactors) {
                layerBox.addItem(layer + ", multiplication result layer, points to an equal value within the Wolfram code");
                continue;
            }
            layerBox.addItem(layer);
        }
        layerBox.addItem("Standard additive neighborhood values");
    }
    /**
     * Updates the text in the text area
     */
    public void updateText() {
        activeLayer = layerBox.getSelectedIndex();
        String outstring = "";
        if (activeLayer <= solution.numFactors) {
            for (int row = 0; row < 20; row++) {
                for (int column = layers[0][0].length/2-30; column < layers[0][0].length/2+30; column++) {
                    outstring += layers[activeLayer][row][column];
                }
                outstring += "\n";
            }
            outstring += "\n";
        } else {
            for (int row = 0; row < 30; row++){
                for (int column = additiveLayer[0].length/2-30; column < additiveLayer[0].length/2+30; column++){
                    outstring += additiveLayer[row][column];
                }
                outstring += "\n";
            }
            outstring += "\n";
        }
        jTextArea.setText(outstring);
    }
}

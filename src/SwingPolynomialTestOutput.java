
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
/**
 * This displays the results of applying a polynomial from polynomialGenerate() from an ECApathPermutationSolution, verifying the integrity of polynomialGenerate(). Also potentially displayed are the unit vector coefficients as layers and the permuted binary neighborhood layers.
 * <p>
 * The difference between the polynomial-applied field and the field produced by various CoefficientCalculation overloads is zero, so generatePolynomial() is accurate
 */
public class SwingPolynomialTestOutput extends JPanel {
    /**
     * A ValidSolution applied as automata with user row 0 input, non-negative real
     */
    double[][] field;
    /**
     * Same application of a ValidSolution as field[][], just prior to the neighborhood normalization, just after the geometric mean of the column
     */
    double[][][] vectorField;

    /**
     * Binary application of a ValidSolution as an automata, each layer of cells is a permutation of that cell's binary neighborhood
     */
    int[][][] factorLayers;
    /**
     * field[][] in image form for display
     */
    BufferedImage fieldImage;
    /**
     * vectorField[][][] in image form for display
     */
    BufferedImage vectorImage;
    /**
     * factorLayers[][][] in image form for display
     */
    BufferedImage factorImage;
    /**
     * complexField[][] in image form for display
     */
    BufferedImage complexImage;
    /**
     * Raster for fieldImage
     */
    int[] imageRaster;
    /**
     * Raster for vectorImage
     */
    int[] vectorRaster;
    /**
     * Raster for factorImage
     */
    int[] factorRaster;
    /**
     * Raster for complexImage
     */
    int[] complexRaster;
    /**
     * Main algorithm instance currently being used
     */
    ECAasMultiplication ecam;
    /**
     * Used in layerBox to select which layer of which set is displayed, the first part are the unit vector coefficient layers and the second part is the binary neighborhoods converted into layers of factors
     */
    int activeLayer;

    /**
     * Swing component that selects what you want to display
     */
    JComboBox layerBox;
    /**
     * Swing component that labels layerBox
     */
    JLabel layerLabel;
    /**
     * Initiates an update of the display
     */
    JButton layerButton;
    /**
     * Currently active solution
     */
    ValidSolution solution;
    /**
     * Labels of layers
     */
    int[] types;
    /**
     * Initializes Swing components
     *
     * @param in Currently active ECAasMultiplication
     */
    public SwingPolynomialTestOutput(ECAasMultiplication in) {
        ecam = in;
        field = new double[400][1000];
        JFrame jFrame = new JFrame();
        jFrame.setSize(1100, 830);
        jFrame.setLocation(50, 50);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        layerBox = new JComboBox();
        layerLabel = new JLabel("Vector field by unit vector or binary neighborhood permutations as factors");
        if (solution != null) redoLayerBox();
        layerButton = new JButton("Load selected image");
        layerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activeLayer = layerBox.getSelectedIndex();
                repaint();
            }
        });
        this.setLayout(new FlowLayout());
        this.add(layerLabel);
        this.add(layerBox);
        this.add(new JLabel(""));
        this.add(layerButton);
        jFrame.setTitle("PolynomialTestFrame");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(this);
        //jFrame.pack();
        jFrame.setVisible(true);
    }
    /**
     * Updates the layer box
     */
    public void redoLayerBox() {
        types = new int[solution.numBits+1+solution.numFactors+solution.numBits];
        layerBox.removeAllItems();
        for (int spot = 0; spot < solution.numBits; spot++) {
            layerBox.addItem("Unit Vector e" + spot + " coefficient layer");
        }
        layerBox.addItem("Coefficient result layer");
        for (int spot = 0; spot < solution.numFactors; spot++) {
            layerBox.addItem("Factor layer " + spot);
        }
        layerBox.addItem("Factor result layer ");
//        for (int spot = 0; spot < solution.numBits; spot++){
//            layerBox.addItem("Complex unit vector e" + spot);
//        }
    }
    /**
     * JFrame paint() implementation
     *
     * @param g the specified Graphics window
     */
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1100, 500);
        if (solution == null) return;
        if (activeLayer < solution.numBits) {
            vectorImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
            vectorRaster = ((DataBufferInt) vectorImage.getRaster().getDataBuffer()).getData();
            for (int row = 0; row < 400; row++) {
                for (int column = 0; column < 1000; column++) {
                    for (int power = 0; power < 24; power++) {
                        vectorRaster[row * 1000 + column] += (int) Math.pow(2, power) * (vectorField[activeLayer][row][column] / Math.pow(2, -power + 3) % 2);
                    }
                }
            }
            g.drawImage(vectorImage, 0, 0, null);
        } else if (activeLayer > solution.numBits && activeLayer < solution.numBits+1+solution.numFactors) {
            factorImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
            factorRaster = ((DataBufferInt) factorImage.getRaster().getDataBuffer()).getData();
            for (int row = 0; row < 400; row++) {
                for (int column = 0; column < 1000; column++) {
                    factorRaster[row * 1000 + column] = (int) Math.pow(2, factorLayers[activeLayer - solution.numBits - 1][row][column]) + (int) Math.pow(2, ((3 + factorLayers[activeLayer - solution.numBits - 1][row][column]) % 8) + 8);
                }
            }
            g.drawImage(factorImage, 0, 0, null);
        } else if (activeLayer == solution.numBits) {
            fieldImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
            imageRaster = ((DataBufferInt) fieldImage.getRaster().getDataBuffer()).getData();
            for (int row = 0; row < 400; row++) {
                for (int column = 0; column < 1000; column++) {
                    for (int power = 0; power < 24; power++) {
                        imageRaster[row * 1000 + column] += (int) Math.pow(2, power) * (field[row][column] / Math.pow(2, -power + 3) % 2);
                    }
                }
            }
            g.drawRect(0, 415, 400, 1000);
        }

//        } else if (activeLayer >solution.numBits+solution.numFactors+1 && activeLayer != solution.numBits+solution.numFactors+1+solution.numBits+1) {
//            fieldImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
//            imageRaster = ((DataBufferInt) fieldImage.getRaster().getDataBuffer()).getData();
//            for (int row = 0; row < 400; row++) {
//                for (int column = 0; column < 1000; column++) {
//                    for (int power = 0; power < 24; power++) {
//                        imageRaster[row * 1000 + column] += (int) Math.pow(2, power) * ( complexVectorField[activeLayer-1-solution.numBits-solution.numFactors][row][column].real / Math.pow(2, -power + 3) % 2);
//                    }
//                }
//            }
//            complexImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
//            complexRaster = ((DataBufferInt) complexImage.getRaster().getDataBuffer()).getData();
//            for (int row = 0; row < 400; row++) {
//                for (int column = 0; column < 1000; column++) {
//                    for (int power = 0; power < 24; power++) {
//                        complexRaster[row * 1000 + column] += (int) Math.pow(2, power) * (complexVectorField[activeLayer-1-solution.numBits-solution.numFactors][row][column].imaginary / Math.pow(2, -power + 3) % 2);
//                    }
//                }
//            }
//            g.drawImage(fieldImage, 0, 0, null);
//            g.drawImage(complexImage, 0, 415, null);
//
//        } else {
//            fieldImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
//            imageRaster = ((DataBufferInt) fieldImage.getRaster().getDataBuffer()).getData();
//            for (int row = 0; row < 400; row++) {
//                for (int column = 0; column < 1000; column++) {
//                    for (int power = 0; power < 24; power++) {
//                        imageRaster[row * 1000 + column] += (int) Math.pow(2, power) * (complexField[row][column].real / Math.pow(2, -power + 3) % 2);
//                    }
//                }
//            }
//            complexImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
//            complexRaster = ((DataBufferInt) complexImage.getRaster().getDataBuffer()).getData();
//            for (int row = 0; row < 400; row++) {
//                for (int column = 0; column < 1000; column++) {
//                    for (int power = 0; power < 24; power++) {
//                        complexRaster[row * 1000 + column] += (int) Math.pow(2, power) * (complexField[row][column].imaginary / Math.pow(2, -power + 3) % 2);
//                    }
//                }
//            }
//            g.drawImage(fieldImage, 0, 0, null);
//            g.drawImage(complexImage, 0, 415, null);
//        }
    }
}


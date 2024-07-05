
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
/**
 * Displays the standard additive binary Wolfram code output for single bit and random initial input, as well as non-negative real input
 */
public class SwingOutputPanel extends JPanel {
    /**
     * Used to display an ECA with random initial input
     */
    public BufferedImage randomImage;
    /**
     * Used to display an ECA with single bit initial input
     */
    public BufferedImage singleBitImage;
    /**
     * Displays solutions' coefficient results
     */
    public BufferedImage coefficientImage;
    /**
     * Displays solutions' ECA calculation results
     */
    public BufferedImage blankImage;
    /**
     * Used as raster for coefficientImage
     */
    int[] coeffRaster;
    /**
     * Used as raster for rF
     */
    int[] singleBitRaster;
    /**
     * Used as raster for sF
     */
    int[] randomRaster;
    /**
     * Array used with single bit initial input
     */
    public int[][] singleBitField = new int[400][1000];
    /**
     * Array used with random initial input
     */
    public int[][] randomField;
    /**
     * Main output array of applying the solution from ECApathPermutations to real positive
     * coefficients in the form of doubles
     */
    public double[][] coeffField = new double[1][1];
    /**
     * Current ValidSolution being displayed
     */
    public ValidSolution currentSolution;
    /**
     * Index of the solution, ecam.specific.validSolutions[solutionNumber];
     */
    int solutionNumber;
    /**
     * If true, the pixels are generated by places greater than zero, if false, the pixels are generated by places less than four.
     */
    boolean pixelRangePositives;
    // public JFrame frame;
    /**
     * Initializes a JFrame on which to display the panel
     */
    SwingOutputPanel() {
        pixelRangePositives = false;
        randomField = new int[2400][2400];

        currentSolution = new ValidSolution();
        this.setSize(1030, 1335);
        //this.setVisible(true);
        JFrame frame = new JFrame();

        //frame.setLayout(new GridLayout());
        frame.setTitle("OutputPanel");
        frame.setSize(1030, 1335);
        frame.setLocation(150,150);
        frame.add(this);
        frame.setVisible(true);
    }
    /**
     * JPanel paint implementation
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,1030,1335);
        g.setColor(Color.BLACK);
//        int[] multResult = currentSolution.factors[currentSolution.numFactors];
//        int[] wolframPointer = new int[multResult.length];
//        for (int spot = 0; spot < wolframPointer.length; spot++) {
//            wolframPointer[spot] = currentSolution.wolframCode[multResult[spot]];
//        }
//        //super.paintComponent(g);
//        //System.out.println("Painting");
        coeffRaster = new int[1];
        singleBitRaster = new int[1];
        randomRaster = new int[1];
        coefficientImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        singleBitImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        randomImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        coeffRaster = ((DataBufferInt) coefficientImage.getRaster().getDataBuffer()).getData();
        singleBitRaster = ((DataBufferInt) singleBitImage.getRaster().getDataBuffer()).getData();
        randomRaster = ((DataBufferInt) randomImage.getRaster().getDataBuffer()).getData();

        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                if (randomField[row][column ] == 0) {
                    randomRaster[row * 1000 + column] = (int) Math.pow(2, 24) - 1;
                }
            }
        }
        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                if (singleBitField[row][column ] == 0) {
                    singleBitRaster[row * 1000 + column] = (int) Math.pow(2, 24) - 1;
                }
            }
        }
            for (int row = 0; row < 400; row++) {
                for (int column = 0; column < 1000; column++) {
                    for (int power = 0; power < 24; power++) {
                        coeffRaster[row * 1000 + column] += (int) Math.pow(2, power) * (coeffField[row][column] / Math.pow(2, -power + 3) % 2);
                    }
                }
            }


        int index = 15;
        g.drawString("Single bit initial input: ", 15, index);
        index += 15;
        g.drawImage(singleBitImage, 15, index, null);
        index += 415;
        g.drawString("Random initial input: ", 25, index);
        index += 15;
        g.drawImage(randomImage, 15, index, null);
        index += 415;
        g.drawString("Same random initial input with solution applied to random {0..1} non negative real: ", 15, index);
        index += 15;
        g.drawImage(coefficientImage, 15, index, null);
    }
    /**
     * Writes the image data to the rasters, used in writing images to file rather than display
     */
    public void paintImages(){
        coeffRaster = new int[1];
        singleBitRaster = new int[1];
        randomRaster = new int[1];
        coefficientImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        singleBitImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        randomImage = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        coeffRaster = ((DataBufferInt) coefficientImage.getRaster().getDataBuffer()).getData();
        singleBitRaster = ((DataBufferInt) singleBitImage.getRaster().getDataBuffer()).getData();
        randomRaster = ((DataBufferInt) randomImage.getRaster().getDataBuffer()).getData();

        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                if (randomField[row][column ] == 0) {
                    randomRaster[row * 1000 + column] = (int) Math.pow(2, 24) - 1;
                }
            }
        }
        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                if (singleBitField[row][column ] == 0) {
                    singleBitRaster[row * 1000 + column] = (int) Math.pow(2, 24) - 1;
                }
            }
        }
        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                for (int power = 0; power < 24; power++) {
                    coeffRaster[row * 1000 + column] += (int) Math.pow(2, power) * (coeffField[row][column] / Math.pow(2, -power + 3) % 2);
                }
            }
        }
    }

}

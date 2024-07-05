
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
/**
 * The binary shadow is 2 planes, one is the real component of the output of a solution and the other is the imaginary component, the input is complex and the components get rounded, less than zero is 1 greater than zero is 0
 */
public class SwingBinaryShadow extends JPanel {
    /**
     * Raster of realImage
     */
    public int[] realRaster;
    /**
     * Raster of imImage;
     */
    public int[] imRaster;
    /**
     * Output form of complexField, real part
     */
    public BufferedImage realImage;
    /**
     * Output form of complexField, imaginary part
     */
    public BufferedImage imImage;
    /**
     * Data to display, is a ValidSolution from ecam.specific applied to complex numbers
     */
   // public Complex[][] complexField;
    public int[][][] binaryField;
    /**
     * Current ValidSolution being displayed
     */
    public ValidSolution currentSolution;

    /**
     * Index of the solution, ecam.specific.validSolutions[solutionNumber];
     */
    int solutionNumber;
    // public JFrame frame;
    /**
     * Initializes a JFrame on which to display the panel
     */
    SwingBinaryShadow(String titleString) {

        binaryField = new int[2][400][1000];
        currentSolution = new ValidSolution();
        this.setSize(1030, 850);
        //this.setVisible(true);
        JFrame frame = new JFrame();

        frame.setLayout(new GridLayout(1,1));
        frame.setTitle(titleString);
        frame.setSize(1030, 850);
        frame.setLocation(250,250);
        frame.add(this);
        //this.setVisible(true);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1030,850);
    }
    /**
     * JPanel paint implementation
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,1030,830);
        g.setColor(Color.BLACK);
//
//        realRaster  = new int[1];
//        imRaster = new int[1];
        realImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
        imImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
        realRaster = ((DataBufferInt) realImage.getRaster().getDataBuffer()).getData();
        imRaster = ((DataBufferInt) imImage.getRaster().getDataBuffer()).getData();
        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {

                for (int power = 0; power < 24; power++) {
                    realRaster[row * 1000 + column] = ((int)Math.pow(2,24)-1)* binaryField[0][row][column];

                }
            }
        }
        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {

                for (int power = 0; power < 24; power++) {
                    imRaster[row * 1000 + column] = ((int)Math.pow(2,24)-1)* binaryField[1][row][column];
                }
            }
        }

        g.drawImage(realImage,15,15,null);
        g.drawString("Complex part",15,430);
        g.drawImage(imImage,15,445,null);
    }
    /**
     * Writes display data to the rasters, used in writing images to file rather than display
     */
    public void paintImages(){

//        realImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
//        imImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
//        realRaster = ((DataBufferInt) realImage.getRaster().getDataBuffer()).getData();
//        imRaster = ((DataBufferInt) imImage.getRaster().getDataBuffer()).getData();
//        for (int row = 0; row < 400; row++) {
//            for (int column = 0; column < 1000; column++) {
//
//            }
//        }
//        for (int row = 0; row < 400; row++) {
//            for (int column = 0; column < 1000; column++) {
//
//            }
//        }
    }
}

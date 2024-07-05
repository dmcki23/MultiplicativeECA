import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
/**
 * Multiplications B, displays results of the non-negative real output processed with the polynomial from generatePolynomial() rather than multiplying out the whole numFactors-dim partial product table, verifies generatePolynomial()
 */
public class SwingPolyTest extends JPanel {
    /**
     * output from subsection.complexFieldToBinary()
     */
    public double[][] fieldFromPolynomial;
    /**
     * Display image
     */
    public BufferedImage bufferedImage;
    /**
     * Display image raster
     */
    public int[] raster;
    /**
     * Initializes a Swing JFrame to put this JPanel on
     */
    public SwingPolyTest(){
        fieldFromPolynomial = new double[400][1000];
        JFrame frame = new JFrame();
        frame.setTitle("Multiplications B with non-neg real, but using the polynomial instead of the entire numFactors-dim partial product method");
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1000,400);



    }
    /**
     * Paints this JPanel
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g){
        bufferedImage = new BufferedImage(1000,400,BufferedImage.TYPE_INT_RGB);
        raster = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

        for (int row = 0; row < 400; row++) {
            for (int column = 0; column < 1000; column++) {
                    for (int power = 0; power < 24; power++) {
                        raster[row * 1000 + column] += (int) Math.pow(2,power) * (fieldFromPolynomial[row][column] / Math.pow(2, -power + 3) % 2);

                    }
            }
        }
        g.drawImage(bufferedImage,0,0,null);
    }
}

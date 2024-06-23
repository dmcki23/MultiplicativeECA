import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
/**
 * Collects images of valid ECA solutions and writes to file
 */
public class RuleImageCollection {
    /**
     * Set of multiplication tables
     */
    int[][][] multTables;
    /**
     * Solution search class
     */
    ECAasMultiplication eca = new ECAasMultiplication();
    /**
     * Swing Panel used to render the images
     */
    SwingPolynomialTestOutput ptf;
    /**
     * Swing Panel used to render the images
     */
    SwingOutputPanel outputPanel;
    /**
     * Swing panel used to render the images
     */
    SwingComplexOutput swingComplexOutput;
    /**
     * Swing Panel used to render the images
     */
    SwingComplexOutput neighborhoodFirstOut;
    /**
     * Swing Panel used to render text output of solutions
     */
    SwingTextPanel stp;
    /**
     * Initializes render panels
     */
    public RuleImageCollection() {
        ptf = new SwingPolynomialTestOutput(eca);
        outputPanel = new SwingOutputPanel();
        stp = new SwingTextPanel(eca);
        swingComplexOutput = new SwingComplexOutput("");
        neighborhoodFirstOut = new SwingComplexOutput("");
        //runCollection();
    }
    /**
     * Runs the 0-255 ECA generator with no normalization sub-folders
     */
    public void runCollection() {
        for (int n = 0; n < 256; n++) {
            getImagesRandomSolution( n);
        }
    }
    /**
     * Runs the 0-255 generator with normalization sub-folders
     */
    public void runCollectionNormalizations() {
        multTables = eca.cds.preCalculateTables(2);
        eca.specific.maxSolutions = 300;
        eca.specific.validSolutions = new ValidSolution[1];
        eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(3);
        eca.post.widthOfRandomInput = 200;
        Random rand = new Random();
        for (int n = 0; n < 256; n++) {
            for (int normalization = 0; normalization < 8; normalization++) {
                getImagesRandomSolution( n, "Normalization_" + normalization, normalization, rand);
            }
        }
    }
    /**
     * Runs the 0-15 logic gate Wolfram code generator with normalizations
     */
    public void runCollectionNormalizationsLogic() {
        multTables = new int[1][2][2];
        multTables[0] = eca.galoisFields.galoisFieldAddition(4);
        eca.specific.maxSolutions = 300;
        eca.specific.validSolutions = new ValidSolution[1];
        eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(2);
        eca.post.widthOfRandomInput = 200;
        Random rand = new Random();
        for (int n = 0; n < 16; n++) {
            for (int normalization = 0; normalization < 8; normalization++) {
                getImagesRandomSolutionLogic(n , "Normalization_" + normalization, normalization, rand);
            }
        }
    }
    /**
     * Runs the 0-15 logic gate Wolfram code generator with no normalizations
     */
    public void runLogicCollection() {
        for (int gate = 0; gate < 16; gate++) {
            getImagesRandomSolutionLogic( gate);
        }
    }
    /**
     * Runs the solution generator for a specific ECA rule
     * @param rule 0-255 ECA rule
     */
    public void getImagesRandomSolution(int rule) {
        int activeRule = rule;
        eca.specific.maxSolutions = 10000;
        eca.specific.validSolutions = new ValidSolution[1];
        int numBit = 3;
        int numRows = 1;
        int degree = 2;
        if (numRows == 1) {
            numBit = 3;
        } else {
            numBit = 5;
        }
        int[][][] multTables = new int[1][8][8];
        eca.specific.searchStop = 10000;
        multTables = eca.generateMultTable(1, 2, 3);
        int[] wolframCode = Arrays.copyOfRange(eca.beca.ruleExtension(activeRule)[numBit], 0, (int) Math.pow(2, 3));
        eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(3);
        eca.specific.generalWolframCode(wolframCode, 5, multTables, 1);
        System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Random rand = new Random();
        int activeSolution = rand.nextInt(0, eca.specific.numSolutions);
        int partialIndex = 1;
        if (numRows == 1) {
            if (partialIndex == 0) {
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            } else if (partialIndex == 1) {
                eca.post.partialProductTable = eca.galoisFields.generateTable(2, 2, true);
            } else {
                eca.post.partialProductTable = new int[][]{{0, 1, 2}, {0, 1, 2}, {0, 1, 2}};
            }
        } else {
            if (partialIndex == 0)
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            else if (partialIndex == 1) {
                eca.post.partialProductTable = new int[5][5];
                for (int row = 0; row < 5; row++) {
                    for (int column = 0; column < 5; column++) {
                        eca.post.partialProductTable[row][column] = column;
                    }
                }
            }
        }
        eca.post.widthOfRandomInput = 200;
        eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomBinaryInput(), eca.post.randomDoubleInput(0, 10), 400, 1000);
        eca.specific.validSolutions[activeSolution].polynomial = eca.post.generatePolynomial(eca.specific.validSolutions[activeSolution]);
        eca.specific.validSolutions[activeSolution].polynomialString = eca.post.polynomialAsStrings(eca.specific.validSolutions[activeSolution].polynomial);
        outputPanel.solutionNumber = activeSolution;
        outputPanel.coeffField = eca.post.coefficientField;
        outputPanel.randomField = eca.post.ruleOutput;
        outputPanel.singleBitField = eca.post.singleBitInput(eca.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
        outputPanel.currentSolution = eca.specific.validSolutions[activeSolution];
        eca.displayValidSolution(eca.specific.validSolutions[activeSolution]);
//        ptf.vectorField = eca.post.subsectionVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.factorLayers = eca.post.subsectionFactorLayers(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.field = eca.post.subsectionCoefficientMultiplication(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexField = eca.post.subsectionComplexField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexVectorField = eca.post.subsectionComplexVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.solution = eca.specific.validSolutions[activeSolution];
        stp.solution = eca.specific.validSolutions[activeSolution];
        stp.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        swingComplexOutput.currentSolution = eca.specific.validSolutions[activeSolution];
        swingComplexOutput.complexField = eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomComplexInput(-10, 10), 400, 1000);
        neighborhoodFirstOut.currentSolution = eca.specific.validSolutions[activeSolution];
        neighborhoodFirstOut.complexField = eca.post.subsectionNeighborhoodFirst(eca.specific.validSolutions[activeSolution], 400, 1000);
//        outputPanel.repaint();
//        swingComplexOutput.repaint();
//        neighborhoodFirstOut.repaint();
        int totImages = eca.specific.validSolutions[activeSolution].numBits * 2 + 2 + eca.specific.validSolutions[activeSolution].numFactors;
        BufferedImage[] imageArray = new BufferedImage[7];
        for (int image = 0; image < 7; image++) {
            imageArray[image] = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        }
        String[] types = new String[]{"Single bit initial input", "Random initial input", "Solution applied with non negative reals", "Solution applied with complex, real part", "Solution applied with complex, imaginary part", "Solution applied with complex normalization first, real part", "Solution applied with complex normalization first, imaginary part"};
        imageArray[0] = outputPanel.singleBitImage;
        imageArray[1] = outputPanel.randomImage;
        imageArray[2] = outputPanel.coefficientImage;
        imageArray[3] = swingComplexOutput.realImage;
        imageArray[4] = swingComplexOutput.imImage;
        imageArray[5] = neighborhoodFirstOut.realImage;
        imageArray[6] = neighborhoodFirstOut.imImage;
        outputPanel.paintImages();
        swingComplexOutput.paintImages();
        neighborhoodFirstOut.paintImages();
        File file;
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_nonneg.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.coefficientImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_singlebit.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.singleBitImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_randombinary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.randomImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_nfReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_nfImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_complexReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_complexImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //file = new File("imageData\\ECA_"+rule+"\\RandomSolution"+"\\eca"+rule+"_info.txt");
        //file.mkdirs();
        try {
            PrintWriter fw = new PrintWriter("imageData\\ECA_" + rule + "\\RandomSolution" + "\\eca" + rule + "_info.txt");
            fw.print(eca.displayValidSolution(eca.specific.validSolutions[activeSolution], true));
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * Runs the generator for a specific ECA rule with given parameters
     * @param rule 0-255 ECA rule
     * @param directoryName name of the directory to put it in
     * @param normalization 0-7 normalization number
     * @param rand an instance of the Random class
     */

    public void getImagesRandomSolution(int rule, String directoryName, int normalization, Random rand) {
        if (normalization % 2 == 0) {
            eca.post.normalizeUnit = false;
        } else {
            eca.post.normalizeUnit = true;
        }
        if (normalization / 2 % 2 == 0) {
            eca.post.wolframIsNegOne = false;
        } else {
            eca.post.wolframIsNegOne = true;
        }
        if (normalization / 4 % 2 == 0) {
            eca.post.avoidDivZero = false;
        } else {
            eca.post.avoidDivZero = true;
        }


        int numBit = 3;
        int[] wolframCode = Arrays.copyOfRange(eca.beca.ruleExtension(rule)[numBit], 0, (int) Math.pow(2, 3));
        eca.specific.generalWolframCode(wolframCode, 5, multTables, 1);
        int activeSolution = rand.nextInt(0, eca.specific.numSolutions);
        eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomBinaryInput(), eca.post.randomDoubleInput(0, 10), 400, 1000);
        eca.specific.validSolutions[activeSolution].polynomial = eca.post.generatePolynomial(eca.specific.validSolutions[activeSolution]);
        eca.specific.validSolutions[activeSolution].polynomialString = eca.post.polynomialAsStrings(eca.specific.validSolutions[activeSolution].polynomial);
        outputPanel.solutionNumber = activeSolution;
        outputPanel.coeffField = eca.post.coefficientField;
        outputPanel.randomField = eca.post.ruleOutput;
        outputPanel.singleBitField = eca.post.singleBitInput(eca.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
        outputPanel.currentSolution = eca.specific.validSolutions[activeSolution];
        //eca.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        stp.solution = eca.specific.validSolutions[activeSolution];
        stp.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        swingComplexOutput.currentSolution = eca.specific.validSolutions[activeSolution];
        swingComplexOutput.complexField = eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomComplexInput(-10, 10), 400, 1000);
        neighborhoodFirstOut.currentSolution = eca.specific.validSolutions[activeSolution];
        neighborhoodFirstOut.complexField = eca.post.subsectionNeighborhoodFirst(eca.specific.validSolutions[activeSolution], 400, 1000);

        String[] types = new String[]{"Single bit initial input", "Random initial input", "Solution applied with non negative reals", "Solution applied with complex, real part", "Solution applied with complex, imaginary part", "Solution applied with complex normalization first, real part", "Solution applied with complex normalization first, imaginary part"};

        outputPanel.paintImages();
        swingComplexOutput.paintImages();
        neighborhoodFirstOut.paintImages();
        File file;
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_nonneg.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.coefficientImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_singlebit.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.singleBitImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_randombinary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.randomImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_nfReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_nfImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_complexReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_complexImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            PrintWriter fw = new PrintWriter("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_normalizations.txt");
            if (normalization % 2 == 0) {
                fw.print("Unit circle normalization, post multiplication = false\n");
            } else {
                fw.print("Unit circle normalization, post multiplication = true\n");
            }
            if (normalization / 2 % 2 == 0) {
                fw.print("A zero in the Wolfram code becomes: 0\n");
            } else {
                fw.print("A zero in the Wolfram code becomes: -1\n");
            }
            if (normalization / 4 % 2 == 0) {
                fw.print("When doing unit circle normalizations, avoid division by zero = false\n");
            } else {
                fw.print("When doing unit circle normalizations, avoid division by zero = true\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //file = new File("imageData\\ECA_"+rule+"\\"+directoryName+""+"\\eca"+rule+"_info.txt");
        //file.mkdirs();
        try {
            PrintWriter fw = new PrintWriter("imageData\\ECA_" + rule + "\\" + directoryName + "" + "\\eca" + rule + "_info.txt");
            fw.print(eca.displayValidSolution(eca.specific.validSolutions[activeSolution], true));
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * Runs the solution search function for a specific 0-15 logic gate Wolfram code with given parameters
     * @param rule 0-15 logic gate
     * @param directoryName directory to place results in
     * @param normalization 0-7 normalization number
     * @param rand an instance of the Random class
     */
    public void getImagesRandomSolutionLogic(int rule, String directoryName, int normalization, Random rand) {
        if (normalization % 2 == 0) {
            eca.post.normalizeUnit = false;
        } else {
            eca.post.normalizeUnit = true;
        }
        if (normalization / 2 % 2 == 0) {
            eca.post.wolframIsNegOne = false;
        } else {
            eca.post.wolframIsNegOne = true;
        }
        if (normalization / 4 % 2 == 0) {
            eca.post.avoidDivZero = false;
        } else {
            eca.post.avoidDivZero = true;
        }


        int numBit = 2;
        int[] wolframCode = new int[4];
        for (int power = 0; power < 4; power++){
            wolframCode[power] = rule/(int)Math.pow(2,power) % 2;
        }
        eca.specific.generalWolframCode(wolframCode, 5, multTables, 0);
        int activeSolution = rand.nextInt(0, eca.specific.numSolutions);
        eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomBinaryInput(), eca.post.randomDoubleInput(0, 10), 400, 1000);
        eca.specific.validSolutions[activeSolution].polynomial = eca.post.generatePolynomial(eca.specific.validSolutions[activeSolution]);
        eca.specific.validSolutions[activeSolution].polynomialString = eca.post.polynomialAsStrings(eca.specific.validSolutions[activeSolution].polynomial);
        outputPanel.solutionNumber = activeSolution;
        outputPanel.coeffField = eca.post.coefficientField;
        outputPanel.randomField = eca.post.ruleOutput;
        outputPanel.singleBitField = eca.post.singleBitInput(eca.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
        outputPanel.currentSolution = eca.specific.validSolutions[activeSolution];
        //eca.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        stp.solution = eca.specific.validSolutions[activeSolution];
        stp.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        swingComplexOutput.currentSolution = eca.specific.validSolutions[activeSolution];
        swingComplexOutput.complexField = eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomComplexInput(-10, 10), 400, 1000);
        neighborhoodFirstOut.currentSolution = eca.specific.validSolutions[activeSolution];
        neighborhoodFirstOut.complexField = eca.post.subsectionNeighborhoodFirst(eca.specific.validSolutions[activeSolution], 400, 1000);

        String[] types = new String[]{"Single bit initial input", "Random initial input", "Solution applied with non negative reals", "Solution applied with complex, real part", "Solution applied with complex, imaginary part", "Solution applied with complex normalization first, real part", "Solution applied with complex normalization first, imaginary part"};

        outputPanel.paintImages();
        swingComplexOutput.paintImages();
        neighborhoodFirstOut.paintImages();
        File file;
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_nonneg.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.coefficientImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_singlebit.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.singleBitImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_randombinary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.randomImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_nfReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_nfImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_complexReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_complexImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            PrintWriter fw = new PrintWriter("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_normalizations.txt");
            if (normalization % 2 == 0) {
                fw.print("Unit circle normalization, post multiplication = false\n");
            } else {
                fw.print("Unit circle normalization, post multiplication = true\n");
            }
            if (normalization / 2 % 2 == 0) {
                fw.print("A zero in the Wolfram code becomes: 0\n");
            } else {
                fw.print("A zero in the Wolfram code becomes: -1\n");
            }
            if (normalization / 4 % 2 == 0) {
                fw.print("When doing unit circle normalizations, avoid division by zero = false\n");
            } else {
                fw.print("When doing unit circle normalizations, avoid division by zero = true\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //file = new File("imageData\\Logic_"+rule+"\\"+directoryName+""+"\\logic"+rule+"_info.txt");
        //file.mkdirs();
        try {
            PrintWriter fw = new PrintWriter("imageData\\Logic_" + rule + "\\" + directoryName + "" + "\\logic" + rule + "_info.txt");
            fw.print(eca.displayValidSolution(eca.specific.validSolutions[activeSolution], true));
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * Runs the solution search function for a specific logic gate
     * @param rule 0-15 logic gate
     */
    public void getImagesRandomSolutionLogic( int rule) {
        int activeRule = rule;
        eca.specific.maxSolutions = 10000;
        eca.specific.validSolutions = new ValidSolution[1];
        int numBit = 2;
        int numRows = 1;
        int degree = 1;
        int[][][] multTables = new int[1][4][4];
        eca.specific.searchStop = 10000;
        multTables[0] = eca.galoisFields.galoisFieldAddition(4);
        int[] gate = new int[4];
        for (int place = 0; place < 4; place++) {
            gate[place] = rule / (int) Math.pow(2, place) % 2;
        }
        int[] wolframCode = gate;
        eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(3);
        eca.specific.generalWolframCode(wolframCode, 5, multTables, 0);
        System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Random rand = new Random();
        int activeSolution = rand.nextInt(0, eca.specific.numSolutions);
        int partialIndex = 0;
        if (numRows == 1) {
            if (partialIndex == 0) {
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            } else if (partialIndex == 1) {
                eca.post.partialProductTable = eca.galoisFields.generateTable(2, 2, true);
            } else {
                eca.post.partialProductTable = new int[][]{{0, 1, 2}, {0, 1, 2}, {0, 1, 2}};
            }
        } else {
            if (partialIndex == 0)
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            else if (partialIndex == 1) {
                eca.post.partialProductTable = new int[5][5];
                for (int row = 0; row < 5; row++) {
                    for (int column = 0; column < 5; column++) {
                        eca.post.partialProductTable[row][column] = column;
                    }
                }
            }
        }
        eca.post.widthOfRandomInput = 200;
        eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomBinaryInput(), eca.post.randomDoubleInput(0, 10), 400, 1000);
        eca.specific.validSolutions[activeSolution].polynomial = eca.post.generatePolynomial(eca.specific.validSolutions[activeSolution]);
        eca.specific.validSolutions[activeSolution].polynomialString = eca.post.polynomialAsStrings(eca.specific.validSolutions[activeSolution].polynomial);
        outputPanel.solutionNumber = activeSolution;
        outputPanel.coeffField = eca.post.coefficientField;
        outputPanel.randomField = eca.post.ruleOutput;
        outputPanel.singleBitField = eca.post.singleBitInput(eca.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
        outputPanel.currentSolution = eca.specific.validSolutions[activeSolution];
        eca.displayValidSolution(eca.specific.validSolutions[activeSolution]);
//        ptf.vectorField = eca.post.subsectionVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.factorLayers = eca.post.subsectionFactorLayers(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.field = eca.post.subsectionCoefficientMultiplication(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexField = eca.post.subsectionComplexField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexVectorField = eca.post.subsectionComplexVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.solution = eca.specific.validSolutions[activeSolution];
        stp.solution = eca.specific.validSolutions[activeSolution];
        stp.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        swingComplexOutput.currentSolution = eca.specific.validSolutions[activeSolution];
        swingComplexOutput.complexField = eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomComplexInput(-10, 10), 400, 1000);
        neighborhoodFirstOut.currentSolution = eca.specific.validSolutions[activeSolution];
        neighborhoodFirstOut.complexField = eca.post.subsectionNeighborhoodFirst(eca.specific.validSolutions[activeSolution], 400, 1000);
//        outputPanel.repaint();
//        swingComplexOutput.repaint();
//        neighborhoodFirstOut.repaint();
        int totImages = eca.specific.validSolutions[activeSolution].numBits * 2 + 2 + eca.specific.validSolutions[activeSolution].numFactors;
        BufferedImage[] imageArray = new BufferedImage[7];
        for (int image = 0; image < 7; image++) {
            imageArray[image] = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        }
        String[] types = new String[]{"Single bit initial input", "Random initial input", "Solution applied with non negative reals", "Solution applied with complex, real part", "Solution applied with complex, imaginary part", "Solution applied with complex normalization first, real part", "Solution applied with complex normalization first, imaginary part"};
        imageArray[0] = outputPanel.singleBitImage;
        imageArray[1] = outputPanel.randomImage;
        imageArray[2] = outputPanel.coefficientImage;
        imageArray[3] = swingComplexOutput.realImage;
        imageArray[4] = swingComplexOutput.imImage;
        imageArray[5] = neighborhoodFirstOut.realImage;
        imageArray[6] = neighborhoodFirstOut.imImage;
        outputPanel.paintImages();
        swingComplexOutput.paintImages();
        neighborhoodFirstOut.paintImages();
        File file;
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_nonneg.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.coefficientImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_singlebit.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.singleBitImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_randombinary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.randomImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_nfReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_nfImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_complexReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_complexImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //file = new File("imageData\\Logic_"+rule+"\\RandomSolution"+"\\logic"+rule+"_info.txt");
        //file.mkdirs();
        try {
            PrintWriter fw = new PrintWriter("imageData\\Logic_" + rule + "\\RandomSolution" + "\\logic" + rule + "_info.txt");
            fw.print(eca.displayValidSolution(eca.specific.validSolutions[activeSolution], true));
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * Gets images of a specific ECA solution
     * @param rule 0-255 ECA rule
     */
    public void getImages( int rule) {
        int activeRule = rule;
        eca.specific.maxSolutions = 10000;
        eca.specific.validSolutions = new ValidSolution[1];
        int numBit = 3;
        int numRows = 1;
        int degree = 2;
        if (numRows == 1) {
            numBit = 3;
        } else {
            numBit = 5;
        }
        int[][][] multTables = new int[1][8][8];
        eca.specific.searchStop = 10000;
        multTables = eca.generateMultTable(1, 2, 3);
        int[] wolframCode = Arrays.copyOfRange(eca.beca.ruleExtension(activeRule)[numBit], 0, (int) Math.pow(2, 3));
        eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(3);
        eca.specific.generalWolframCode(wolframCode, 5, multTables, 1);
        System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        int activeSolution = 0;
        int partialIndex = 1;
        if (numRows == 1) {
            if (partialIndex == 0) {
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            } else if (partialIndex == 1) {
                eca.post.partialProductTable = eca.galoisFields.generateTable(2, 2, true);
            } else {
                eca.post.partialProductTable = new int[][]{{0, 1, 2}, {0, 1, 2}, {0, 1, 2}};
            }
        } else {
            if (partialIndex == 0)
                eca.post.partialProductTable = eca.galoisFields.galoisFieldAddition(numBit);
            else if (partialIndex == 1) {
                eca.post.partialProductTable = new int[5][5];
                for (int row = 0; row < 5; row++) {
                    for (int column = 0; column < 5; column++) {
                        eca.post.partialProductTable[row][column] = column;
                    }
                }
            }
        }
        eca.post.widthOfRandomInput = 200;
        eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomBinaryInput(), eca.post.randomDoubleInput(0, 10), 400, 1000);
        eca.specific.validSolutions[activeSolution].polynomial = eca.post.generatePolynomial(eca.specific.validSolutions[activeSolution]);
        eca.specific.validSolutions[activeSolution].polynomialString = eca.post.polynomialAsStrings(eca.specific.validSolutions[activeSolution].polynomial);
        outputPanel.solutionNumber = activeSolution;
        outputPanel.coeffField = eca.post.coefficientField;
        outputPanel.randomField = eca.post.ruleOutput;
        outputPanel.singleBitField = eca.post.singleBitInput(eca.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
        outputPanel.currentSolution = eca.specific.validSolutions[activeSolution];
        eca.displayValidSolution(eca.specific.validSolutions[activeSolution]);
//        ptf.vectorField = eca.post.subsectionVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.factorLayers = eca.post.subsectionFactorLayers(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.field = eca.post.subsectionCoefficientMultiplication(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexField = eca.post.subsectionComplexField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.complexVectorField = eca.post.subsectionComplexVectorField(eca.specific.validSolutions[activeSolution], 400, 1000);
//        ptf.solution = eca.specific.validSolutions[activeSolution];
        stp.solution = eca.specific.validSolutions[activeSolution];
        stp.displayValidSolution(eca.specific.validSolutions[activeSolution]);
        swingComplexOutput.currentSolution = eca.specific.validSolutions[activeSolution];
        swingComplexOutput.complexField = eca.post.validSolutionCoefficientCalculation(eca.specific.validSolutions[activeSolution], eca.post.randomComplexInput(-10, 10), 400, 1000);
        neighborhoodFirstOut.currentSolution = eca.specific.validSolutions[activeSolution];
        neighborhoodFirstOut.complexField = eca.post.subsectionNeighborhoodFirst(eca.specific.validSolutions[activeSolution], 400, 1000);
//        outputPanel.repaint();
//        swingComplexOutput.repaint();
//        neighborhoodFirstOut.repaint();
        int totImages = eca.specific.validSolutions[activeSolution].numBits * 2 + 2 + eca.specific.validSolutions[activeSolution].numFactors;
        BufferedImage[] imageArray = new BufferedImage[7];
        for (int image = 0; image < 7; image++) {
            imageArray[image] = new BufferedImage(1000, 400, BufferedImage.TYPE_INT_RGB);
        }
        String[] types = new String[]{"Single bit initial input", "Random initial input", "Solution applied with non negative reals", "Solution applied with complex, real part", "Solution applied with complex, imaginary part", "Solution applied with complex normalization first, real part", "Solution applied with complex normalization first, imaginary part"};
        imageArray[0] = outputPanel.singleBitImage;
        imageArray[1] = outputPanel.randomImage;
        imageArray[2] = outputPanel.coefficientImage;
        imageArray[3] = swingComplexOutput.realImage;
        imageArray[4] = swingComplexOutput.imImage;
        imageArray[5] = neighborhoodFirstOut.realImage;
        imageArray[6] = neighborhoodFirstOut.imImage;
        outputPanel.paintImages();
        swingComplexOutput.paintImages();
        neighborhoodFirstOut.paintImages();
        File file;
        file = new File("imageData\\ECA_" + rule + "\\image_nonneg.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.coefficientImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_singlebit.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.singleBitImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_randombinary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(outputPanel.randomImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_nfReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_nfImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(neighborhoodFirstOut.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_complexReal.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.realImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("imageData\\ECA_" + rule + "\\image_complexImaginary.jpg");
        file.mkdirs();
        try {
            ImageIO.write(swingComplexOutput.imImage, "jpg", file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        file = new File("\\ECA_" + rule + "\\eca" + rule + "_info.txt");
        file.mkdirs();
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(eca.displayValidSolution(eca.specific.validSolutions[0], true));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            PrintWriter fw = new PrintWriter("imageData\\ECA_" + rule + "\\eca" + rule + "_info.txt");
            fw.print(eca.displayValidSolution(eca.specific.validSolutions[0], true));
            fw.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    /**
     * Generates a text file of the normalization parameters for a solution
     */
    public void identityInfo() {
        for (int rule = 0; rule < 256; rule++) {
            File file = new File("\\ECA_" + rule + "\\eca" + rule + "_info.txt");
            file.mkdirs();
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(eca.displayValidSolution(eca.specific.validSolutions[0], true));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * GUI control panel, manages the other Swing classes, there is no algorithm logic in the GUI components, that is all in ECAasMultiplication. This GUI is not necessary for the algorithm, and can be split off for integration in other projects.
 */
public class SwingDashboard extends JPanel {

    /**
     * Real positive coefficients on input, multiplied out for each cell using a solution from ECAecam
     */
    public double[][] coeffField;

    /**
     * Current ECA number
     */
    public int activeRule;
    /**
     * Degree hypercomplex used
     */
    public int degree;
    /**
     * Number of factors in desired or produced solution
     */
    public int numFactors;
    /**
     * Number of rows in desired or produced solution
     */
    public int numRows;
    /**
     * Number of bit numBit in desired or produced solution
     */
    public int numBit;
    /**
     * Number of factors used in the 4 bit logic truth table searches
     */
    int numLogicFactors;
    /**
     * Currently active logic gate solutions
     */
    int activeGate;
    /**
     * Bottom end of random input range
     */
    int componentRandRange;
    /**
     * Top end of random input range
     */
    int componentRandRangeMax;
    /**
     * Basic ECA utility class
     */
    public BasicECA beca = new BasicECA();
    /**
     * Finds a set of permutations that when applied to the indexes a multiplication table, creates
     * a set of pointers that reproduces the original ECA Wolfram code
     */
    ECAasMultiplication ecam = new ECAasMultiplication();
    /**
     * Swing component that displays valid solutions as ECA
     */
    //OutputPanel outputPanel;
    SwingOutputPanel outputPanel;
    /**
     * Displays the results of generatePolynomial() applied to a coefficient field, verifies the integrity of generatePolynomial()
     */
    SwingLayers ptf;
    /**
     * Swing frame to display this panel in
     */
    JFrame frame = new JFrame();
    /**
     * Swing frame to display text output that previously went to just the console in debugging
     */
    SwingTextPanel stp;
    /**
     * Displays types of multiplication tables to the console
     */
    ConsoleDashboard consoleDashboard;
    /**
     * Displays a complex version of an applied ValidSolution, polynomial version, normalization after multiplication
     */
    SwingComplexOutput swingComplexOutput;
    /**
     * Displays the complex output of an applied ValidSolution, doing the base 2 normalization first
     */
    SwingComplexOutput neighborhoodFirstOut;
    /**
     * Displays the complex vector output of an applied ValidSolution doing the normalization last, in layer form
     */
    SwingComplexLayers swingComplexLayers;
    /**
     * Displays output of subsection.complexFieldToBinary(), experimental
     */
    SwingBinaryShadow swingBinaryShadow;
    /**
     * Displays the non-negative real output processed with the polynomial of the solution from generatePolynomial(), rather than multiplying out the whole numFactors-dim partial product table
     */
    SwingPolyTest swingPolyTest;
    /**
     * Multiplications A, each layer is a factor for that cell
     */
    SwingBinaryFactorLayers swingBinaryFactorLayers;

    /**
     * Contains all the Java Swing component initialization and function-calling logic
     */
    public SwingDashboard() {
        componentRandRange = 0;
        componentRandRangeMax = 1;
        ptf = new SwingLayers(ecam);
        outputPanel = new SwingOutputPanel();
        stp = new SwingTextPanel(ecam);
        swingComplexOutput = new SwingComplexOutput("Multiplications B, partial product multiplication as a polynomial");
        neighborhoodFirstOut = new SwingComplexOutput("Multiplications C, construction from A; like B but normalization first");
        swingComplexLayers = new SwingComplexLayers("Multiplications B, complex neighborhood vector just prior to normalization");
        swingBinaryShadow = new SwingBinaryShadow("Binary shadow of complex Multiplications B");
        swingBinaryFactorLayers = new SwingBinaryFactorLayers();
        swingPolyTest = new SwingPolyTest();
        //complexOutAlt = new SwingComplexOutput();
        consoleDashboard = new ConsoleDashboard(stp);
        coeffField = new double[1][1];
        addECAcomponents();
        addLogicComponents();
        addGeneralDisplayComponents();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1100);
        frame.setLocation(200, 200);
        frame.pack();
        frame.setVisible(true);
        ptf.setSize(1000, 450);
        ptf.setVisible(true);
        componentRandRange = -10;
        componentRandRangeMax = 10;
    }
    /**
     * Deals with ECA Swing components
     */
    public void addECAcomponents() {
        //ECA components
        JComboBox ruleComboBox = new JComboBox();
        for (int n = 0; n < 256; n++) {
            ruleComboBox.addItem(n);
        }
        JComboBox whichMultTable = new JComboBox();
        JComboBox numberRows = new JComboBox();
        numberRows.addItem(1);
        numberRows.addItem(2);
        JComboBox numberFactors = new JComboBox();
        for (int spot = 0; spot < 11; spot++) {
            numberFactors.addItem(spot);
        }
        whichMultTable.addItem("Permuted Cayley-Dickson");
        whichMultTable.addItem("XOR");
        whichMultTable.addItem("Galois Field (3,2), adjusted to exclude zeros");
        whichMultTable.addItem("Galois Field (17,1), adjusted to exclude zeros");
        whichMultTable.addItem("Galois Field (2,3)");
        whichMultTable.addItem("Galois Field (2,4)");
        whichMultTable.addItem("Galois Field (2,5)");
        whichMultTable.addItem("Galois Field (2,6)");
        whichMultTable.addItem("Fano plane octonions");
        JComboBox whichConvoTable = new JComboBox();
        whichConvoTable.addItem("XOR, n x n , n = numBit");
        whichConvoTable.addItem("GF(2,2) shifted, 3x3 bits excluding zeros");
        JComboBox degreeBox = new JComboBox();
        degreeBox.addItem(2);
        degreeBox.addItem(3);
        degreeBox.addItem(4);
        JButton calculateButton = new JButton("Refresh");
        JComboBox whichSolutionBox = new JComboBox();
        JButton specificSolutionButton = new JButton("Display specific solution");
        numberFactors.setSelectedIndex(5);
        ruleComboBox.setSelectedIndex(54);
        degreeBox.setSelectedIndex(0);
        numberRows.setSelectedIndex(0);
        JButton rowButton = new JButton();
        JComboBox fanoBox = new JComboBox();
        for (int index = 0; index < 480; index++) {
            fanoBox.addItem(index);
        }
        JLabel sliderLabel = new JLabel("Width of random input 200");
        JSlider slider = new JSlider(0, 400);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ecam.post.widthOfRandomInput = slider.getValue();
            }
        });
        JButton deepButton = new JButton("Start deep search");
        JLabel deepLabel = new JLabel("Deep search using above parameters");
        JLabel ruleLabel = new JLabel("ECA rule");
        JLabel multLabel = new JLabel("Multiplication Table to use");
        JLabel degreeLabel = new JLabel("Degree: 2 = quaternions, 3 = octonions, etc., if applicable");
        JLabel factorLabel = new JLabel("Number of factors to use");
        JLabel rowLabel = new JLabel("Number of rows in the ECA, 1 row = 3 bit neighborhood, 2 rows = 5 bit neighborhood");
        JLabel solutionLabel = new JLabel("Specific solution to use");
        JLabel calculateLabel = new JLabel("the calculate button produces all solutions for the chosen parameters");
        JLabel specificLabel = new JLabel("this button re-randomizes and displays the ECA rule with the particular solution number chosen");
        slider.setValue(200);
        ecam.post.widthOfRandomInput = slider.getValue();
        rowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        JComboBox timeOutBox = new JComboBox();
        timeOutBox.addItem((long) 30);
        timeOutBox.addItem((long) 60);
        timeOutBox.addItem((long) 120);
        timeOutBox.addItem((long) 240);
        timeOutBox.addItem((long) 480);
        JLabel tBoxLabel = new JLabel("Keeps functions from running longer than the user want, in seconds");
        JComboBox partialProductBox = new JComboBox();
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecam.specific.maxSolutions = 10000;
                ecam.specific.validSolutions = new ValidSolution[1];
                activeRule = ruleComboBox.getSelectedIndex();
                numRows = (int) numberRows.getSelectedItem();
                numFactors = (int) numberFactors.getSelectedItem();
                degree = (int) degreeBox.getSelectedItem();
                whichSolutionBox.removeAllItems();
                if (numRows == 1) {
                    numBit = 3;
                } else {
                    numBit = 5;
                }
                int width = slider.getValue();
                sliderLabel.setText("Width of random input " + width);
                //if ((boolean) absValBox.getSelectedItem() == false) numBit++;
                int[][][] multTables = new int[1][8][8];
                long searchStop = (long) timeOutBox.getSelectedItem();
                searchStop = searchStop;
                ecam.specific.searchStop = searchStop;
                //ecaps.searchStop = searchStop;
                //multTables =
                multTables = ecam.generateMultTable(whichMultTable.getSelectedIndex(), degree, numBit);
                int[] wolframCode = Arrays.copyOfRange(beca.ruleExtension(activeRule)[numBit], 0, (int) Math.pow(2, numBit));
                ecam.post.partialProductTable = ecam.galoisFields.galoisFieldAddition(numBit);
                ecam.specific.generalWolframCode(wolframCode, numFactors, multTables, whichMultTable.getSelectedIndex());
                whichSolutionBox.removeAllItems();
                for (int spot = 0; spot < ecam.specific.numSolutions; spot++) {
                    whichSolutionBox.addItem(spot);
                }
                if ((int) numberRows.getSelectedItem() == 1) {
                    partialProductBox.removeAllItems();
                    partialProductBox.addItem("Galois addition, XOR, 3x3");
                    partialProductBox.addItem("GF(2,2) shifted to exclude zeros, 3x3");
                    partialProductBox.addItem("Plain table, {{0,1,2},{0,1,2},{0,1,2}");
                } else {
                    partialProductBox.removeAllItems();
                    partialProductBox.addItem("Galois addition, XOR, 5x5");
                    partialProductBox.addItem("Plain table, {{0,1,2,3,4},{0,1,2,3,4}..}");
                }
            }
        });
        JLabel partialProductLabel = new JLabel("Partial product table, size = places x places");
        specificSolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                int activeSolution = whichSolutionBox.getSelectedIndex();
                if (numRows == 1) {
                    if (partialProductBox.getSelectedIndex() == 0) {
                        ecam.post.partialProductTable = ecam.galoisFields.galoisFieldAddition(numBit);
                    } else if (partialProductBox.getSelectedIndex() == 1) {
                        ecam.post.partialProductTable = ecam.galoisFields.generateTable(2, 2, true);
                    } else {
                        ecam.post.partialProductTable = new int[][]{{0, 1, 2}, {0, 1, 2}, {0, 1, 2}};
                    }
                } else {
                    if (partialProductBox.getSelectedIndex() == 0)
                        ecam.post.partialProductTable = ecam.galoisFields.galoisFieldAddition(numBit);
                    else if (partialProductBox.getSelectedIndex() == 1) {
                        ecam.post.partialProductTable = new int[5][5];
                        for (int row = 0; row < 5; row++) {
                            for (int column = 0; column < 5; column++) {
                                ecam.post.partialProductTable[row][column] = column;
                            }
                        }
                    }
                }
                //beca.widthRandom = slider.getValue();
                ecam.post.widthOfRandomInput = slider.getValue();
                ecam.post.multiplicativeSolutionOutput(ecam.specific.validSolutions[activeSolution], ecam.post.randomBinaryInput(), ecam.post.randomDoubleInput(0, componentRandRangeMax), 400, 1000);
                ecam.specific.validSolutions[activeSolution].polynomial = ecam.post.generatePolynomial(ecam.specific.validSolutions[activeSolution]);
                ecam.specific.validSolutions[activeSolution].polynomialString = ecam.post.polynomialAsStrings(ecam.specific.validSolutions[activeSolution].polynomial);
                outputPanel.solutionNumber = activeSolution;
                outputPanel.coeffField = ecam.post.coefficientField;
                outputPanel.randomField = ecam.post.ruleOutput;
                outputPanel.singleBitField = ecam.post.singleBitInput(ecam.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
                outputPanel.repaint();
                outputPanel.currentSolution = ecam.specific.validSolutions[activeSolution];
                outputPanel.repaint();
                ecam.displayValidSolution(ecam.specific.validSolutions[activeSolution]);
                stp.solution = ecam.specific.validSolutions[activeSolution];
                stp.displayValidSolution(ecam.specific.validSolutions[activeSolution]);
                stp.repaint();
                swingComplexOutput.currentSolution = ecam.specific.validSolutions[activeSolution];
                swingComplexOutput.complexField = ecam.post.multiplicativeSolutionOutput(ecam.specific.validSolutions[activeSolution], ecam.post.randomComplexInput(componentRandRange, componentRandRangeMax), 400, 1000);
                swingComplexOutput.repaint();
                neighborhoodFirstOut.currentSolution = ecam.specific.validSolutions[activeSolution];
                neighborhoodFirstOut.complexField = ecam.post.subsection.subsectionNeighborhoodFirst(ecam.specific.validSolutions[activeSolution], 400, 1000);
                neighborhoodFirstOut.repaint();
                ptf.solution = ecam.specific.validSolutions[activeSolution];
                ptf.vectorField = ecam.post.subsection.subsectionVectorField(ecam.specific.validSolutions[activeSolution], 400, 1000).clone();
                ptf.field = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[activeSolution], 400, 1000);
                ptf.redoLayerBox();
                ptf.repaint();
                swingComplexLayers.currentSolution = ecam.specific.validSolutions[activeSolution];
                swingComplexLayers.complexField = ecam.post.subsection.subsectionComplexField(ecam.specific.validSolutions[activeSolution], 400, 1000);
                swingComplexLayers.vectorField = ecam.post.subsection.subsectionComplexVectorField(ecam.specific.validSolutions[activeSolution], 400, 1000);
                for (int row = 0; row < 20; row++) {
                    for (int column = 175; column < 225; column++) {
                        System.out.print(swingComplexLayers.vectorField[0][row][column].real);
                    }
                    System.out.print("\n");
                }
                System.out.println();
                swingComplexLayers.redoLayerBox();
                swingComplexLayers.repaint();
                swingBinaryShadow.binaryField = ecam.post.subsection.subsectionComplexFieldToBinary(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryShadow.repaint();
                swingPolyTest.fieldFromPolynomial = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[activeSolution],400,1000);
                swingPolyTest.repaint();
                //ecam.post.subsection.subsectionFactorLayers(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryFactorLayers.additiveLayer = ecam.post.subsection.additiveNeighborhood(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryFactorLayers.layers = ecam.post.subsection.subsectionFactorLayers(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryFactorLayers.solution = ecam.specific.validSolutions[activeSolution];
                swingBinaryFactorLayers.updateBox();
                swingBinaryFactorLayers.activeLayer = 0;
                swingBinaryFactorLayers.updateText();
            }
        });
        deepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                activeRule = ruleComboBox.getSelectedIndex();
                numFactors = (int) numberFactors.getSelectedItem();
                degree = (int) degreeBox.getSelectedItem();
                whichSolutionBox.removeAllItems();
                ecam.specific.maxSolutions = 10000;
                String outstring = (ecam.deep.deepECASearch(degree, 1, numFactors, whichMultTable.getSelectedIndex()));
                stp.jTextArea.setText(outstring);
                whichSolutionBox.removeAllItems();
            }
        });
        frame.setTitle("SwingDashboard");
        frame.setLayout(new GridLayout(45, 2));
        frame.setLocation(0, 0);
        frame.add(ruleLabel);
        frame.add(ruleComboBox);
        frame.add(multLabel);
        frame.add(whichMultTable);
        frame.add(solutionLabel);
        frame.add(whichSolutionBox);
        frame.add(degreeLabel);
        frame.add(degreeBox);
        frame.add(factorLabel);
        frame.add(numberFactors);
        frame.add(rowLabel);
        frame.add(numberRows);
        frame.add(partialProductLabel);
        frame.add(partialProductBox);
        frame.add(tBoxLabel);
        frame.add(timeOutBox);
        frame.add(calculateLabel);
        frame.add(calculateButton);
        frame.add(specificLabel);
        frame.add(specificSolutionButton);
        frame.add(deepLabel);
        frame.add(deepButton);
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(sliderLabel);
        frame.add(slider);
    }
    /**
     * Deals with logic gate search Swing components
     */
    public void addLogicComponents() {
        JComboBox partialProductBox = new JComboBox();
        partialProductBox.addItem("Galois addition, XOR, 2x2");
        partialProductBox.addItem("XNOR, 2x2");
        partialProductBox.addItem("Plain table, 10, A");
        JLabel gateLabel = new JLabel("Logic gate, AND = 8, OR = 14, XOR = 6, etc");
        JComboBox gateBox = new JComboBox();
        for (int gate = 0; gate < 16; gate++) {
            gateBox.addItem(gate + ": " + ecam.logicGateNames[gate]);
        }
        JLabel gateSolutionLabel = new JLabel("Logic gate solution: ");
        JComboBox gateSolutionBox = new JComboBox();
        JLabel logicButtonLabel = new JLabel("Display specific logic gate solution");
        JButton logicDisplayButton = new JButton("Display specific solution");
        JLabel logicFactorsLabel = new JLabel("Number of factors in logic gate search");
        JComboBox logicFactorsBox = new JComboBox();
        for (int factor = 2; factor <= 11; factor++) {
            logicFactorsBox.addItem(factor);
        }
        logicFactorsBox.setSelectedIndex(3);
        gateBox.setSelectedIndex(6);
        JLabel logicRefreshLabel = new JLabel("Refresh logic gate solutions");
        JButton logicRefreshButton = new JButton("Refresh");
        JLabel logicTableLabel = new JLabel("Which multiplication table to use");
        JComboBox logicTableBox = new JComboBox();
        logicTableBox.addItem("XOR");
        logicTableBox.addItem("GF(5,1) shifted to exclude zeros");
        logicTableBox.addItem("GF(2,2)");
        JLabel deepLogicLabel = new JLabel("Search all logic gates for solutions and cross reference gates that have solutions in common");
        JButton deepLogicButton = new JButton("Deep logic gate search");
        logicRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numLogicFactors = logicFactorsBox.getSelectedIndex() + 2;
                activeGate = gateBox.getSelectedIndex();
                ecam.specific.maxSolutions = 10000;
                int[][][] tables = new int[1][4][4];
                if (logicTableBox.getSelectedIndex() == 0) {
                    tables = new int[1][4][4];
                    tables[0] = ecam.galoisFields.galoisFieldAddition(4);
                } else if (logicTableBox.getSelectedIndex() == 1) {
                    tables = new int[1][4][4];
                    tables[0] = ecam.galoisFields.generateTable(5, 1, true);
                } else if (logicTableBox.getSelectedIndex() == 2) {
                    tables = new int[1][4][4];
                    tables[0] = ecam.galoisFields.galoisFieldMultiplication(2, 2);
                }
                int[] gate = new int[4];
                for (int power = 0; power < 4; power++) {
                    gate[power] = activeGate / (int) Math.pow(2, power) % 2;
                }
                ecam.post.partialProductTable = ecam.galoisFields.galoisFieldAddition(2);
                ecam.specific.generalWolframCode(gate, numLogicFactors, tables, logicTableBox.getSelectedIndex());
                gateSolutionBox.removeAllItems();
                for (int spot = 0; spot < ecam.specific.numSolutions; spot++) {
                    gateSolutionBox.addItem(spot);
                }
                System.out.println("numLogicFactors: " + numLogicFactors);
            }
        });
        logicDisplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                int activeSolution = gateSolutionBox.getSelectedIndex();
                ecam.post.partialProductTable = ecam.galoisFields.galoisFieldAddition(2);
                if (partialProductBox.getSelectedIndex() == 1) {
                    for (int row = 0; row < 2; row++) {
                        for (int column = 0; column < 2; column++) {
                            ecam.post.partialProductTable[row][column] = (ecam.post.partialProductTable[row][column] + 1) % 2;
                        }
                    }
                }
                if (partialProductBox.getSelectedIndex() == 2) {
                    ecam.post.partialProductTable = new int[][]{{0, 1}, {0, 1}};
                }
                ecam.specific.validSolutions[activeSolution].polynomial = ecam.post.generatePolynomial(ecam.specific.validSolutions[activeSolution]);
                ecam.specific.validSolutions[activeSolution].polynomialString = ecam.post.polynomialAsStrings(ecam.specific.validSolutions[activeSolution].polynomial);
                ecam.post.multiplicativeSolutionOutput(ecam.specific.validSolutions[activeSolution], ecam.post.randomBinaryInput(), ecam.post.randomDoubleInput(0, componentRandRangeMax), 400, 1000);
                ecam.displayValidSolution(ecam.specific.validSolutions[activeSolution]);
                outputPanel.solutionNumber = activeSolution;
                outputPanel.coeffField = ecam.post.coefficientField;
                outputPanel.randomField = ecam.post.ruleOutput;
                outputPanel.singleBitField = ecam.post.singleBitInput(ecam.specific.validSolutions[activeSolution].wolframCode, 400, 1000);
                outputPanel.repaint();
                outputPanel.currentSolution = ecam.specific.validSolutions[activeSolution];
                outputPanel.repaint();
                ptf.vectorField = ecam.post.subsection.subsectionVectorField(ecam.specific.validSolutions[activeSolution], 400, 1000);
                ptf.field = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[activeSolution], 400, 1000);
                ptf.solution = ecam.specific.validSolutions[0];
                ptf.redoLayerBox();
                ptf.repaint();
                stp.solution = ecam.specific.validSolutions[activeSolution];
                stp.displayValidSolution(ecam.specific.validSolutions[activeSolution]);
                stp.repaint();
                swingComplexOutput.currentSolution = ecam.specific.validSolutions[activeSolution];
                swingComplexOutput.complexField = ecam.post.multiplicativeSolutionOutput(ecam.specific.validSolutions[activeSolution], ecam.post.randomComplexInput(componentRandRange, componentRandRangeMax), 400, 1000);
                swingComplexOutput.repaint();
                neighborhoodFirstOut.currentSolution = ecam.specific.validSolutions[activeSolution];
                neighborhoodFirstOut.complexField = ecam.post.subsection.subsectionNeighborhoodFirst(ecam.specific.validSolutions[activeSolution], 400, 1000);
                neighborhoodFirstOut.repaint();
                swingComplexLayers.currentSolution = ecam.specific.validSolutions[activeSolution];
                swingComplexLayers.complexField = ecam.post.subsection.subsectionComplexField(ecam.specific.validSolutions[activeSolution], 400, 1000);
                swingComplexLayers.vectorField = ecam.post.subsection.subsectionComplexVectorField(ecam.specific.validSolutions[activeSolution], 400, 1000);
                swingComplexLayers.redoLayerBox();
                swingComplexLayers.repaint();
                swingBinaryShadow.binaryField = ecam.post.subsection.subsectionComplexFieldToBinary(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryShadow.repaint();
                swingPolyTest.fieldFromPolynomial = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[activeSolution],400,1000);
                swingPolyTest.repaint();
                swingBinaryFactorLayers.additiveLayer = ecam.post.subsection.additiveNeighborhood(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryFactorLayers.layers = ecam.post.subsection.subsectionFactorLayers(ecam.specific.validSolutions[activeSolution],400,1000);
                swingBinaryFactorLayers.solution = ecam.specific.validSolutions[activeSolution];
                swingBinaryFactorLayers.updateBox();
                swingBinaryFactorLayers.activeLayer = 0;
                swingBinaryFactorLayers.updateText();
            }
        });
        deepLogicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecam.specific.maxSolutions = 10000;
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                numLogicFactors = logicFactorsBox.getSelectedIndex() + 2;
                String outstring = ecam.deep.deepLogicSearch(numLogicFactors, logicTableBox.getSelectedIndex());
                stp.jTextArea.setText(outstring);
            }
        });
        //
        //
        //
        //logic components
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(logicFactorsLabel);
        frame.add(logicFactorsBox);
        frame.add(gateLabel);
        frame.add(gateBox);
        frame.add(gateSolutionLabel);
        frame.add(gateSolutionBox);
        frame.add(logicTableLabel);
        frame.add(logicTableBox);
        frame.add(new JLabel("Partial product table"));
        frame.add(partialProductBox);
        frame.add(logicRefreshLabel);
        frame.add(logicRefreshButton);
        frame.add(logicButtonLabel);
        frame.add(logicDisplayButton);
        frame.add(deepLogicLabel);
        frame.add(deepLogicButton);
        frame.add(new JLabel());
    }
    /**
     * Deals with general output Swing components
     */
    public void addGeneralDisplayComponents() {
        JComboBox cdDispBox = new JComboBox();
        cdDispBox.addItem(2);
        cdDispBox.addItem(3);
        cdDispBox.addItem(4);
        cdDispBox.setSelectedIndex(0);
        JComboBox specificTableBoxA = new JComboBox();
        JComboBox specificTableBoxB = new JComboBox();
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dispDegree = (int) cdDispBox.getSelectedItem();
                specificTableBoxB.removeAllItems();
                specificTableBoxA.removeAllItems();
                int cdChoices = ecam.specific.pf.factorial(dispDegree);
                for (int spot = 0; spot < cdChoices; spot++) {
                    specificTableBoxA.addItem(spot);
                    specificTableBoxB.addItem(spot);
                }
                specificTableBoxA.setSelectedIndex(0);
                specificTableBoxB.setSelectedIndex(0);
            }
        });
        JButton cdButton = new JButton("Display specific tables");
        JComboBox fanoTripletsBox = new JComboBox();
        for (int spot = 0; spot < 480; spot++) {
            fanoTripletsBox.addItem(spot);
        }
        JComboBox primeSelector = new JComboBox();
        JComboBox powerSelector = new JComboBox();
        primeSelector.addItem(2);
        primeSelector.addItem(3);
        primeSelector.addItem(5);
        primeSelector.addItem(7);
        primeSelector.addItem(11);
        primeSelector.addItem(13);
        primeSelector.addItem(17);
        powerSelector.addItem(1);
        powerSelector.addItem(2);
        powerSelector.addItem(3);
        powerSelector.addItem(4);
        powerSelector.addItem(5);
        JComboBox lengthBox = new JComboBox();
        for (int l = 1; l < 6; l++) {
            lengthBox.addItem(l);
        }
        lengthBox.setSelectedIndex(3);
        cdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                int dispDegree = (int) cdDispBox.getSelectedItem();
                int specTableA = (int) specificTableBoxA.getSelectedItem();
                int specTableB = (int) specificTableBoxB.getSelectedItem();
                int fanoTable = (int) fanoTripletsBox.getSelectedItem();
                //consoleDashboard.displayCayleyDickson(dispDegree, specTableA, specTableB);
                //consoleDashboard.displayFano(fanoTable);
                //consoleDashboard.displayGalois((int) primeSelector.getSelectedItem(), (int) powerSelector.getSelectedItem());
                //consoleDashboard.displaySet(dispDegree, specTableA, specTableB, fanoTable, (int) primeSelector.getSelectedItem(), (int) powerSelector.getSelectedItem(), (int) lengthBox.getSelectedItem());
                stp.jTextArea.setText(consoleDashboard.displayCayleyDickson(dispDegree, specTableA, specTableB) + "\n" + consoleDashboard.displayFano(fanoTable) + "\n" + consoleDashboard.displayGalois((int) primeSelector.getSelectedItem(), (int) powerSelector.getSelectedItem()));
                //stp.repaint();
            }
        });
        //refresh.doClick();
        JLabel fanoTestLabel = new JLabel("Compare Fano-generated octonions with permuted Cayley-Dickson octonions");
        JButton fanoTestButton = new JButton("Fano/CD Compare");
        fanoTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                consoleDashboard.fanoTest();
                consoleDashboard.stp.jTextArea.setText(consoleDashboard.fano.fanoTest(true));
            }
        });
        JLabel cdCompareLabel = new JLabel("Compare permuted CD with permuted CD");
        JButton cdCompareButton = new JButton("CD v CD");
        cdCompareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dispDegree = (int) cdDispBox.getSelectedItem();
                ecam.cds.cdCompareAgainstPoP(dispDegree);
                consoleDashboard.compareCayleyDickson(dispDegree);
            }
        });
        JLabel randomWolframCodeTestLabel = new JLabel("Picks a random Wolfram code with 5 factors, identity solution");
        JButton randomWolframCodeButton = new JButton("Random Wolfram Code");
        randomWolframCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecam.post.useNorm = true;
                System.out.println("\n\n\n\n\n\n\n\n\n\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                Random rand = new Random();
                ecam.post.widthOfRandomInput = 200;
                ecam.specific.maxSolutions = 1;
                ecam.testGeneralWolframCode((int) Math.pow(2, rand.nextInt(2, 6)), 1000, 2000, componentRandRange, componentRandRangeMax);
                ecam.specific.validSolutions[0].polynomial = ecam.post.generatePolynomial(ecam.specific.validSolutions[0]);
                ecam.displayValidSolution(ecam.specific.validSolutions[0]);
                outputPanel.solutionNumber = 0;
                outputPanel.coeffField = ecam.post.coefficientField;
                outputPanel.randomField = ecam.post.ruleOutput;
                outputPanel.singleBitField = ecam.post.singleBitInput(ecam.specific.validSolutions[0].wolframCode, 400, 1000);
                outputPanel.repaint();
                outputPanel.currentSolution = ecam.specific.validSolutions[0];
                outputPanel.repaint();
                ptf.vectorField = ecam.post.subsection.subsectionVectorField(ecam.specific.validSolutions[0], 400, 1000);
                ptf.field = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[0], 400, 1000);
                ptf.solution = ecam.specific.validSolutions[0];
                ptf.redoLayerBox();
                ptf.repaint();
                stp.solution = ecam.specific.validSolutions[0];
                stp.displayValidSolution(ecam.specific.validSolutions[0]);
                stp.repaint();
                swingComplexOutput.currentSolution = ecam.specific.validSolutions[0];
                swingComplexOutput.complexField = ecam.post.multiplicativeSolutionOutput(ecam.specific.validSolutions[0], ecam.post.randomComplexInput(componentRandRange, componentRandRangeMax), 400, 1000);
                swingComplexOutput.repaint();
                neighborhoodFirstOut.currentSolution = ecam.specific.validSolutions[0];
                neighborhoodFirstOut.complexField = ecam.post.subsection.subsectionNeighborhoodFirst(ecam.specific.validSolutions[0], 400, 1000);
                neighborhoodFirstOut.repaint();
                swingComplexLayers.currentSolution = ecam.specific.validSolutions[0];
                swingComplexLayers.complexField = ecam.post.subsection.subsectionComplexField(ecam.specific.validSolutions[0], 400, 1000);
                swingComplexLayers.vectorField = ecam.post.subsection.subsectionComplexVectorField(ecam.specific.validSolutions[0], 400, 1000);
                swingComplexLayers.redoLayerBox();
                swingComplexLayers.repaint();
                swingBinaryShadow.binaryField = ecam.post.subsection.subsectionComplexFieldToBinary(ecam.specific.validSolutions[0],400,1000);
                swingBinaryShadow.repaint();
                swingPolyTest.fieldFromPolynomial = ecam.post.subsection.subsectionCoefficientMultiplication(ecam.specific.validSolutions[0],400,1000);
                swingPolyTest.repaint();
                swingBinaryFactorLayers.additiveLayer = ecam.post.subsection.additiveNeighborhood(ecam.specific.validSolutions[0],400,1000);
                swingBinaryFactorLayers.layers = ecam.post.subsection.subsectionFactorLayers(ecam.specific.validSolutions[0],400,1000);
                swingBinaryFactorLayers.solution = ecam.specific.validSolutions[0];
                swingBinaryFactorLayers.updateBox();
                swingBinaryFactorLayers.activeLayer = 0;
                swingBinaryFactorLayers.updateText();
            }
        });
        JComboBox normalizeUnitBox = new JComboBox();
        normalizeUnitBox.addItem(true);
        normalizeUnitBox.addItem(false);
        JComboBox wolframIsNegOneBox = new JComboBox();
        wolframIsNegOneBox.addItem(0);
        wolframIsNegOneBox.addItem(-1);
        JComboBox avoidDivZeroBox = new JComboBox();
        avoidDivZeroBox.addItem(true);
        avoidDivZeroBox.addItem(false);
        JComboBox additiveBox = new JComboBox();
        additiveBox.addItem(true);
        additiveBox.addItem(false);
        JButton normalizationRefreshButton = new JButton("Refresh normalizations");
        normalizationRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (normalizeUnitBox.getSelectedIndex() == 0) {
                    ecam.post.normalizeUnit = true;
                } else {
                    ecam.post.normalizeUnit = false;
                }
                if (wolframIsNegOneBox.getSelectedIndex() == 1) {
                    ecam.post.wolframIsNegOne = true;
                } else {
                    ecam.post.wolframIsNegOne = false;
                }
                if (avoidDivZeroBox.getSelectedIndex() == 0) {
                    ecam.post.avoidDivZero = true;
                } else {
                    ecam.post.avoidDivZero = false;
                }
                ecam.post.doAddititiveWolfram = (boolean) additiveBox.getSelectedItem();
            }
        });
        JButton restoreDefaultsButton = new JButton("Restore default normalizations");
        restoreDefaultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecam.post.normalizeUnit = true;
                ecam.post.wolframIsNegOne = false;
                ecam.post.avoidDivZero = true;
                normalizeUnitBox.setSelectedIndex(0);
                wolframIsNegOneBox.setSelectedIndex(0);
                avoidDivZeroBox.setSelectedIndex(0);
            }
        });
        frame.add(new JLabel());
        frame.add(new JLabel("Table Display Degree, 2 = quaternions, 3 = octonions, 4 = sedonions"));
        frame.add(cdDispBox);
        frame.add(new JLabel("Cayley-Dickson permutation number, (cdz, ___), down in recursion"));
        frame.add(specificTableBoxA);
        frame.add(new JLabel("Cayley-Dickson permutation= number, (___, cdo), up in recursion"));
        frame.add(specificTableBoxB);
        frame.add(new JLabel("Fano plane octonions"));
        frame.add(fanoTripletsBox);
        frame.add(new JLabel("Galois Field, Prime"));
        frame.add(primeSelector);
        frame.add(new JLabel("Galois Field, Power"));
        frame.add(powerSelector);
        frame.add(new JLabel("Length of permutations"));
        frame.add(lengthBox);
        frame.add(new JLabel("Refresh permuted Cayley-Dickson solutions"));
        frame.add(refresh);
        frame.add(new JLabel("Display tables with above parameters"));
        frame.add(cdButton);
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(fanoTestLabel);
        frame.add(fanoTestButton);
        frame.add(cdCompareLabel);
        frame.add(cdCompareButton);
        frame.add(randomWolframCodeTestLabel);
        frame.add(randomWolframCodeButton);
        frame.add(new JLabel());
        frame.add(new JLabel());
        frame.add(new JLabel("Whether to normalize the cell to the unit complex circle"));
        frame.add(normalizeUnitBox);
        frame.add(new JLabel("Do the additive Wolfram code call on the complex neighborhood"));
        frame.add(additiveBox);
        frame.add(new JLabel("How to treat a 0 in the Wolfram code, {0,-1}"));
        frame.add(wolframIsNegOneBox);
        frame.add(new JLabel("Whether to avoid divisions by zero in the normalization"));
        frame.add(avoidDivZeroBox);
        frame.add(new JLabel("Refresh normalization parameters"));
        frame.add(normalizationRefreshButton);
//        frame.add(new JLabel("Restore default normalizations"));
//        frame.add(restoreDefaultsButton);
    }
}

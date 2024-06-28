import java.io.File;
import java.io.PrintWriter;
/**
 * This outputs the HTML for the imageData solution database,
 */
public class HTMLdashboard {
    /**
     * Wolfram code utility class
     */
    BasicECA beca = new BasicECA();
    /**
     * Generates HTML menu of the 0-255 ECA rules arranged by left-right-black-white symmetries
     * @return HTML string
     */
    public String generateLRBW() {
        String out = "<p>0-255 ECA by left, right, black-white,left-right-black-white symmetry groups</p>\n";
        out += "<table >\n";
        for (int row = 0; row < 88; row++) {
            out += "<tr>\n";
            for (int column = 0; column < 4; column++) {
                out += "<td>" + "<a href=\"..\\ECA_" + beca.equivRules[row][column] + "\\ECA_" + beca.equivRules[row][column] + "_index.html\">" + beca.equivRules[row][column] + "</a>" + "</td>\n";
            }
            out += "</tr>\n";
        }
        out += "</table>\n";
        return out;
    }
    /**
     * Generates the 0-255 ECA as an HTML menu organized in a 16x16 square
     * @return HTML string
     */
    public String generateMainSquare() {
        String out = "<p>0-255 ECA</p>\n";
        out += "<table >\n";
        for (int row = 0; row < 16; row++) {
            out += "<tr>\n";
            for (int column = 0; column < 16; column++) {
                out += "<td>" + "<a href=\"..\\ECA_" + (16 * row + column) + "\\ECA_" + (16 * row + column) + "_index.html\">" + (16 * row + column) + "</a></td>\n";
            }
            out += "</tr>\n";
        }
        out += "</table>\n";
        return out;
    }
    /**
     * Generates an ECA HTML menu organized by left-right-black-white rule symmetry
     * @param overloadDummy dummy variable to overload the other generateLRBW()
     * @return HTML string
     */
    public String generateLRBW(boolean overloadDummy) {
        String out = "<p>0-255 ECA by left, right, black-white,left-right-black-white symmetry groups</p>\n";
        out += "<table >\n";
        for (int row = 0; row < 88; row++) {
            out += "<tr>\n";
            for (int column = 0; column < 4; column++) {
                out += "<td>" + "<a href=\"ECA_" + beca.equivRules[row][column] + "\\ECA_" + beca.equivRules[row][column] + "_index.html\">" + beca.equivRules[row][column] + "</a>" + "</td>\n";
            }
            out += "<td> Class: " + beca.ruleClasses[beca.equivRules[row][0]] + "</td>";
            out += "</tr>\n";
        }
        out += "</table>\n";
        return out;
    }
    /**
     * Generates the 0-255 ECA HTML nav menu organized in a 16x16 square
     * @param overloadDummy dummy variable to overload the other generateMainSquare()
     * @return HTML string
     */
    public String generateMainSquare(boolean overloadDummy) {
        String out = "<p>0-255 ECA</p>\n";
        out += "<table >\n";
        for (int row = 0; row < 16; row++) {
            out += "<tr>\n";
            for (int column = 0; column < 16; column++) {
                out += "<td>" + "<a href=\"ECA_" + (16 * row + column) + "\\ECA_" + (16 * row + column) + "_index.html\">" + (16 * row + column) + "</a></td>\n";
            }
            out += "</tr>\n";
        }
        out += "</table>\n";
        return out;
    }
    /**
     * Generates the site navigation HTML menu for logic gate Wolfram code solutions
     * @return HTML string
     */
    public String generateLogicMenu() {
        String[] logicGateNames = new String[]{"False", "NOR", "B and Not A", "Not A", "A and Not B", "Not B", "XOR", "NAND", "AND", "XNOR", "A", "B Or Not A", "B", "A Or Not B", "OR", "True"};
        String out = "<p>0-16 Logic gate truth tables as Wolfram codes</p>\n";
        out += "<table>\n";
        for (int gate = 0; gate < 16; gate++) {
            out += "<tr>\n";
            out += "<td><a href=\"Logic_" + gate + "\\logic_" + gate + "_index.html\">" + "Gate: " + gate + ", " + logicGateNames[gate] + "</a></td>\n";
            out += "</tr>\n";
        }
        out += "</table>";
        return out;
    }
    /**
     * Generates the HTML image code for an ECA solution
     * @param n 0-255 ECA rule
     * @return HTML string
     */
    public String generateSpecificRule(int n) {
        String out = "";
        out += "<p>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_singlebit.jpg\">Single bit initial input</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_randombinary.jpg\">Random binary input</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_nonneg.jpg\">Non-negative real</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_complexReal.jpg\">Multiplications B, real part</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_complexImaginary.jpg\">Multiplications B, imaginary part</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_nfReal.jpg\">Multiplications C, real part</img>\n";
        out += "<img src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_nfImaginary.jpg\">Multiplications C, imaginary part</img>\n";
        out += "</p>\n";
        out += "<p>\n";
        out += "<embed src = \"..\\ECA_" + n + "\\RandomSolution\\eca" + n + "_info.txt\" width=\"400\" height=\"400\"/>\n";
        out += "</p>";
        return out;
    }
    /**
     * Generates the images HTML for a specific ECA rule
     * @param n 0-255 ECA rule
     * @param normalization normalization 0-7
     * @return HTML string
     */
    public String generateSpecificRule(int n, int normalization) {
        String out = "";
        out += "<p>\n";
        out += ("<br><br><img src = \"eca" + n + "_nonneg.jpg\"><br>Non-negative real</img><br>\n");
        out += "<br><br><img src = \"eca" + n + "_complexReal.jpg\"><br>Multiplications B, real part</img><br>\n";
        out += "<br><br><img src = \"eca" + n + "_complexImaginary.jpg\"><br>Multiplications B, imaginary part</img><br>\n";
        out += "<br><br><img src = \"eca" + n + "_nfReal.jpg\"><br>Multiplications C, real part</img><br>\n";
        out += "<br><br><img src = \"eca" + n + "_nfImaginary.jpg\"><br>Multiplications C, imaginary part</img><br>\n";
        out += "<p>\n";
        out += "<embed src = \"eca" + n + "_info.txt\" width=\"400\" height=\"400\"/>\n";
        out += "<embed src =\"eca"+n+"_normalizations.txt\" width = \"400\" height =\"400\"/>\n";

        out += "</p>";
        out += "</p>\n";
        return out;
    }
    /**
     * Generates the image HTML for a specific logic gate solution
     * @param n 0-15 logic gate
     * @return HTML string
     */
    public String generateSpecificRuleLogic(int n) {
        String out = "";
        out += "<p>\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_singlebit.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_randombinary.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_nonneg.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_complexReal.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_complexImaginary.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_nfReal.jpg\">\n";
        out += "<img src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_nfImaginary.jpg\">\n";
        out += "</p>\n";
        out += "<p>\n";
        out += "<embed src = \"..\\Logic_" + n + "\\RandomSolution\\logic" + n + "_info.txt\" width=\"400\" height=\"400\"/>\n";

        out += "</p>";
        return out;
    }
    /**
     * generates the string of a generic HTML doc opener
     * @return HTML string of generic doc opener
     */
    public String generateGenericOpener() {
        String out = "";
        out += "<!-- index.html -->\n";
        out += "<!DOCTYPE html>\n";
        out += "<html>\n";
        out += "<body>\n";
        return out;
    }
    /**
     * Generates the ECA solution indexes with no sub folders
     */
    public void generateIndexFiles() {
        try {
            PrintWriter printWriter = new PrintWriter("imageData\\mainIndex.html");
            printWriter.print(generateGenericOpener());
            printWriter.write(generateLogicMenu());
            printWriter.print(generateMainSquare(true));
            printWriter.print(generateLRBW(true));
            printWriter.print("</body>\n");
            printWriter.print("</html>\n");
            printWriter.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        for (int n = 0; n < 256; n++) {
            try {
                PrintWriter printWriter = new PrintWriter("imageData\\ECA_" + n + "\\ECA_" + n + "_index.html");
                printWriter.println(generateGenericOpener());
                printWriter.println("<a href=\"..\\mainIndex.html\">Main</a>");
                printWriter.write(generateSpecificRule(n));
                printWriter.print("</body>\n");
                printWriter.print("</html>\n");
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    /**
     * Generates ECA solution indexes with normalizations sub folders
     */
    public void generateIndexFilesWithNormalizations() {
        try {
            PrintWriter printWriter = new PrintWriter("imageData\\mainIndex.html");
            printWriter.print(generateGenericOpener());
            printWriter.print(generateLogicMenu());
            printWriter.print(generateMainSquare(true));
            printWriter.print(generateLRBW(true));
            printWriter.print("</body>\n");
            printWriter.print("</html>\n");
            printWriter.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        for (int n = 0; n < 256; n++) {
            try {
                PrintWriter printWriter = new PrintWriter("imageData\\ECA_" + n + "\\ECA_" + n + "_index.html");
                printWriter.print(generateGenericOpener());
                printWriter.print("<a href=\"..\\mainIndex.html\">Main</a><br>");
                printWriter.print(generateNormalizationsIndex(n));
                printWriter.print("<br><br><img src = \"image_singlebit.jpg\"><br>Single bit initial input</img><br>\n");
                printWriter.print("<br><br><img src = \"image_randombinary.jpg\"><br>Random binary input</img><br>\n");
                printWriter.print("</body>\n");
                printWriter.print("</html>\n");
                printWriter.close();
                for (int normalization = 0; normalization < 8; normalization++) {
                    printWriter = new PrintWriter("imageData\\ECA_" + n + "\\Normalization_" + normalization + "\\ECA_" + n + "_index.html");
                    printWriter.print(generateGenericOpener());
                    printWriter.print("<a href=\"..\\ECA_" + n + "_index.html\">Rule</a><br><br>");
                    printWriter.print(generateNormalizationsNormalizations(n));
                    printWriter.print(generateSpecificRule(n, normalization));
                    printWriter.print("</body>\n");
                    printWriter.print("</html>\n");
                    printWriter.close();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    /**
     * Generates logic gate Wolfram code indexes with normalizations subfolders
     */
    public void generateIndexFilesWithNormalizationsLogic() {
//        try {
//            PrintWriter printWriter = new PrintWriter("imageData\\mainIndex.html");
//            printWriter.print(generateGenericOpener());
//            printWriter.print(generateLogicMenu());
//            printWriter.print(generateMainSquare(true));
//            printWriter.print(generateLRBW(true));
//            printWriter.print("</body>\n");
//            printWriter.print("</html>\n");
//            printWriter.close();
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
        for (int n = 0; n < 16; n++) {
            try {
                PrintWriter printWriter = new PrintWriter("imageData\\Logic_" + n + "\\logic_" + n + "_index.html");
                printWriter.print(generateGenericOpener());
                printWriter.print("<a href=\"..\\mainIndex.html\">Main</a><br>");
                printWriter.print(generateNormalizationsIndexLogic(n));
                printWriter.print("<br><br><img src = \"Normalization_0\\logic"+n+"_singlebit.jpg\">Single bit initial input</img><br>\n");
                printWriter.print("<br><br><img src = \"Normalization_0\\logic"+n+"_randombinary.jpg\">Random binary input</img><br>\n");
                printWriter.print("</body>\n");
                printWriter.print("</html>\n");
                printWriter.close();
                for (int normalization = 0; normalization < 8; normalization++) {
                    printWriter = new PrintWriter("imageData\\logic_" + n + "\\Normalization_" + normalization + "\\logic_" + n + "_index.html");
                    printWriter.print(generateGenericOpener());
                    printWriter.print("<br><br><a href=\"..\\logic_" + n + "_index.html\">Rule</a><br><br>");
                    printWriter.print(generateNormalizationsNormalizationsLogic(n));
                    printWriter.print(generateSpecificRuleLogic(n, normalization));
                    printWriter.print("</body>\n");
                    printWriter.print("</html>\n");
                    printWriter.close();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    /**
     * Generates the normalization sub-folder HTML menu for an ECA solution
     * @param n ECA rule
     * @return HTML string
     */
    public String generateNormalizationsIndex(int n) {
        String out = generateGenericOpener();
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_0\\ECA_" + n + "_index.html\">Normalization 0</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_1\\ECA_" + n + "_index.html\">Normalization 1</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_2\\ECA_" + n + "_index.html\">Normalization 2</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_3\\ECA_" + n + "_index.html\">Normalization 3</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_4\\ECA_" + n + "_index.html\">Normalization 4</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_5\\ECA_" + n + "_index.html\">Normalization 5</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_6\\ECA_" + n + "_index.html\">Normalization 6</a><br>\n";
        out += "<a href=\"..\\ECA_" + n + "\\Normalization_7\\ECA_" + n + "_index.html\">Normalization 7</a><br>\n";
        return out;
    }
    /**
     * Generates the normalization sub-folder HTML menu for a logic gate Wolfram code solution
     * @param n 0-15 logic gate
     * @return HTML string
     */
    public String generateNormalizationsIndexLogic(int n) {
        String out = generateGenericOpener();
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_0\\logic_" + n + "_index.html\">Normalization 0</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_1\\logic_" + n + "_index.html\">Normalization 1</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_2\\logic_" + n + "_index.html\">Normalization 2</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_3\\logic_" + n + "_index.html\">Normalization 3</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_4\\logic_" + n + "_index.html\">Normalization 4</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_5\\logic_" + n + "_index.html\">Normalization 5</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_6\\logic_" + n + "_index.html\">Normalization 6</a><br>\n";
        out += "<a href=\"..\\Logic_" + n + "\\Normalization_7\\logic_" + n + "_index.html\">Normalization 7</a><br>\n";
        return out;
    }
    /**
     * Generates the HTML normalization sub-folder rule nav menu
     * @param n 0-255 ECA rule
     * @return HTML string
     */
    public String generateNormalizationsNormalizations(int n) {
        String out = generateGenericOpener();
        out += "<a href=\"..\\Normalization_0\\ECA_" + n + "_index.html\">Normalization 0</a><br>\n";
        out += "<a href=\"..\\Normalization_1\\ECA_" + n + "_index.html\">Normalization 1</a><br>\n";
        out += "<a href=\"..\\Normalization_2\\ECA_" + n + "_index.html\">Normalization 2</a><br>\n";
        out += "<a href=\"..\\Normalization_3\\ECA_" + n + "_index.html\">Normalization 3</a><br>\n";
        out += "<a href=\"..\\Normalization_4\\ECA_" + n + "_index.html\">Normalization 4</a><br>\n";
        out += "<a href=\"..\\Normalization_5\\ECA_" + n + "_index.html\">Normalization 5</a><br>\n";
        out += "<a href=\"..\\Normalization_6\\ECA_" + n + "_index.html\">Normalization 6</a><br>\n";
        out += "<a href=\"..\\Normalization_7\\ECA_" + n + "_index.html\">Normalization 7</a><br>\n";
        return out;
    }
    /**
     * Generates the HTML normalization sub-folder logic gate solution menu
     * @param n Logic gate number in decimal
     * @return HTML string
     */
    public String generateNormalizationsNormalizationsLogic(int n) {
        String out = generateGenericOpener();
        out += "<a href=\"..\\Normalization_0\\logic_" + n + "_index.html\">Normalization 0</a><br>\n";
        out += "<a href=\"..\\Normalization_1\\logic_" + n + "_index.html\">Normalization 1</a><br>\n";
        out += "<a href=\"..\\Normalization_2\\logic_" + n + "_index.html\">Normalization 2</a><br>\n";
        out += "<a href=\"..\\Normalization_3\\logic_" + n + "_index.html\">Normalization 3</a><br>\n";
        out += "<a href=\"..\\Normalization_4\\logic_" + n + "_index.html\">Normalization 4</a><br>\n";
        out += "<a href=\"..\\Normalization_5\\logic_" + n + "_index.html\">Normalization 5</a><br>\n";
        out += "<a href=\"..\\Normalization_6\\logic_" + n + "_index.html\">Normalization 6</a><br>\n";
        out += "<a href=\"..\\Normalization_7\\logic_" + n + "_index.html\">Normalization 7</a><br>\n";
        return out;
    }
    /**
     * Generates the main Logic solution directory index
     */
    public void generateLogicIndexFiles() {
        for (int n = 0; n < 16; n++) {
            try {
                PrintWriter printWriter = new PrintWriter("imageData\\logic_" + n + "\\logic_" + n + "_index.html");
                printWriter.println(generateGenericOpener());
                printWriter.println("<a href=\"..\\mainIndex.html\">Main</a>");
                printWriter.write(generateSpecificRuleLogic(n));
                printWriter.print("</body>\n");
                printWriter.print("</html>\n");
                printWriter.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    /**
     * Generates the specific solution with normalization parameters of a logic gate Wolfram code
     * @param n logic gate number
     * @param normalization normalization number, 0-7
     * @return HTML string
     */
    public String generateSpecificRuleLogic(int n, int normalization) {
        String out = "";
        out += "<p>\n";
        out += ("<br><br><img src = \"logic" + n + "_nonneg.jpg\"><br>Non-negative real</img><br>\n");
        out += "<br><br><img src = \"logic" + n + "_complexReal.jpg\"><br>Multiplications B, real part</img><br>\n";
        out += "<br><br><img src = \"logic" + n + "_complexImaginary.jpg\"><br>Multiplications B, imaginary part</img><br>\n";
        out += "<br><br><img src = \"logic" + n + "_nfReal.jpg\"><br>Multiplications C, real part</img><br>\n";
        out += "<br><br><img src = \"logic" + n + "_nfImaginary.jpg\"><br>Multiplications C, imaginary part</img><br>\n";
        out += "<p>\n";
        out += "<embed src = \"logic" + n + "_info.txt\" width=\"400\" height=\"400\"/>\n";
        out += "<embed src =\"logic"+n+"_normalizations.txt\" width = \"400\" height =\"400\"/>\n";
        out += "</p>";
        out += "</p>\n";
        return out;
    }
    /**
     * Generates the directory structure
     * @param directoryName name of the directory to create
     */
    public void generateDirectory(String directoryName){
            File file = new File("imageData\\directory.txt");
            file.mkdirs();
            File[] ecaFiles = new File[256];
            File[] logicFiles = new File[16];
            File[] normalizationFiles = new File[8];
            for (int n = 0; n < 256; n++) {
                ecaFiles[n] = new File("imageData\\ECA_"+n+"\\directory.txt");
                ecaFiles[n].mkdirs();
                for (int norm = 0; norm < 8; norm++){
                    normalizationFiles[norm] = new File("imageData\\ECA_"+n+"\\Normalization_"+norm+"\\directory.txt");
                    normalizationFiles[norm].mkdirs();
                }
            }
        for (int n = 0; n < 16; n++) {
            logicFiles[n] = new File("imageData\\Logic_"+n+"\\directory.txt");
            logicFiles[n].mkdirs();
            for (int norm = 0; norm < 8; norm++){
                normalizationFiles[norm] = new File("imageData\\Logic_"+n+"\\Normalization_"+norm+"\\directory.txt");
                normalizationFiles[norm].mkdirs();
            }
        }

    }
}

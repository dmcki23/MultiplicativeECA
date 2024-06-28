Elementary Cellular Automata as Multiplicative Automata, MultiplicitaveECA
Daniel McKinley
June 27, 2024
https://github.com/dmcki23/MultiplicativeECA

This program transforms additive elementary cellular automata into multiplicative automata via permutations and extends the standard binary operation to complex via four kinds of multiplication. It includes a GUI to explore solutions, support libraries to construct hypercomplex multiplication tables, a short paper, algorithm visualization diagrams, Javadoc documentation, allows for user input, and is designed to integrate well elsewhere.

To use the GUI, call an instance of SwingDashboard.
To use the search classes apart from the GUI:
    Create ECAasMultiplication class
    Create the multiplication tables
    Set number of factors
    Call generalWolframCode()
    Set the partial product table
    Set your normalization parameters, if not default
    Set your input, or call the random input function
    Call multiplicativeSolutionOutput()
    the set of ValidSolutions is in the fields of ECAMpostProcessing
    use the extract functions of ValidSolutions if you want arrays of the solutions' fields

there is an easily browsable database of solution images at www.danielmckinley.com
consistently little-endian, consistently zero indexed,
uses Java 17 because Mathematica doesn't recognize over 61.0 and Matlab doesn't recognize over 51
error checking is mainly on user, Wolfram codes bigger than multiplication tables and multiplication tables bigger than Wolfram codes, search function calls with exponentially factorially long runtimes, grid output sizes, non-prime numbers in GaloisFields, etc. some throw errors, some produce gibberish
it is not in package format, so it is easier to use classes seperated from the GUI elsewhere,
everything is public, so you can call it from Mathematica, if it's not public, you can see it with Methods[] but not use it
Swing components use (column,row), the project uses (row,column)
there is another markdown paper about the Cayley-Dickson algorithm in the /src/Paper/ folder with some helpful diagrams
there are group theory implications here, but I come from a programming background before pure math, and speak better Java than groups atm


Installation instructions:
Option 1. Run the available jar file in /out/production/artifacts/MultiplicativeECA_jar/MultiplicativeECA.jar
Option 2. Open the source code in you favorite IDE and build and run
Option 2a. There is a zip file of the IntelliJ project available with everything in it
Option 3. Mathematica uses the JLink package to load Java objects in a notebook, see Mathematica documentation

Javadoc:
Open the source code in your favorite editor and use the generate Javadoc tool
Use a Javadoc command line tool on the source code directory





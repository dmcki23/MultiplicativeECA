Elementary Cellular Automata as Multiplicative Automata, MultiplicitaveECA
Daniel McKinley
June 27, 2024
https://github.com/dmcki23/MultiplicativeECA

This program transforms additive elementary cellular automata into multiplicative automata via permutations and extends the standard binary operation to complex via four kinds of multiplication. It includes a GUI to explore solutions, support libraries to construct hypercomplex multiplication tables, a short paper, algorithm visualization diagrams, Javadoc documentation, allows for user input, and is designed to integrate well elsewhere. The paper is in /src/Paper/

To use the GUI, call an instance of SwingDashboard.
To use the search classes apart from the GUI:
    Create ECAasMultiplication class
    Create the multiplication tables
    Set number of factors
    Call generalWolframCode()
    The set of ValidSolutions is in the fields of ECAMspecific
    Use the extract functions of ValidSolutions if you want arrays of the solutions' fields
    Set the partial product table
    Set your normalization parameters, if not default
    Set your input, or call the random input function
    Call multiplicativeSolutionOutput()
    The output is in the class fields, but the set of subsection() functions return a window of what was calculated


Installation instructions:
Option 1. Run the available jar file in /out/production/artifacts/MultiplicativeECA_jar/MultiplicativeECA.jar
Option 2. Open the source code in you favorite IDE and build and run
Option 2a. There is a zip file of the IntelliJ project available with everything in it

Javadoc:
Open the source code in your favorite editor and use the generate Javadoc tool
Use a Javadoc command line tool on the source code directory


there is an easily browsable database of solution images at www.danielmckinley.com
consistently little-endian, consistently zero indexed,
uses Java 17 because Mathematica doesn't recognize over 61.0 and Matlab doesn't recognize over 51
error checking is mainly on user, Wolfram codes bigger than multiplication tables and multiplication tables bigger than Wolfram codes, search function calls with exponentially factorially long runtimes, grid output sizes, non-prime numbers in GaloisFields, etc. some throw errors, some produce gibberish
it is not in package format, so it is easier to use classes seperated from the GUI elsewhere,
everything is public, so you can call it from Mathematica, if it's not public, you can see it with Methods[] but not use it
Swing components use (column,row), the project uses (row,column)
there is another markdown paper about the Cayley-Dickson algorithm in the /src/Paper/ folder with some helpful diagrams
there are group theory implications here, but I come from a programming background before pure math, and speak better Java than groups atm
the O(n) for both the Cayley-Dickson library, d!*d!*2^(d+1) for all the tables for each degree, and the ECAasMultiplication class, (places!)^numFactors, are spectacularly bad, but manageable with the recursive version of Cayley-Dickson multiplication and smaller numbers of neighborhoods and class sizes.

if you're curious about how this project got started, a while back i was playing around with extending Wolfram codes from 1 row operations to 2 row operations, and that the row 2 truth table could be looked at as a subsection of a cube, with each 3 bit sub-neighborhood being an axis, and noticed in some idle time that i could make a quaternion multiplication cube pretty easily and wondered if there was any overlap between that and the elementary automata, just because they were both cubes. It turns out that rule 102 has the zero permutation set as a solution with 3 factors. The initial stages of the project used sets of random combinations of columns of input neighborhoods to make the factors, but eventually through trial and error discovered the systematic permutation way of doing it.






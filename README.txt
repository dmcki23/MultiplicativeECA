Elementary Cellular Automata as Multiplicative Automata, MultiplicitaveECA
Daniel McKinley
June 27, 2024
https://github.com/dmcki23/MultiplicativeECA

This program transforms additive elementary cellular automata into multiplicative automata via permutations and extends the standard binary operation to complex via four kinds of multiplication. It includes a GUI to explore solutions, support libraries to construct hypercomplex multiplication tables, a short paper, algorithm visualization diagrams, Javadoc documentation, allows for user input, and is designed to integrate well elsewhere. The paper is in /src/Paper/


Installation instructions:
Option 1. Run the available jar file in /out/production/artifacts/MultiplicativeECA_jar/MultiplicativeECA.jar
Option 2. Open the source code in you favorite IDE and build and run
Option 2a. There is a zip file of the IntelliJ project available with everything in it

Javadoc:
Open the source code in your favorite editor and use the generate Javadoc tool
Javadoc files are available in the .zip file in the Javadoc directory

To use the code directly:
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

Out-of-scope issues:
Making Bloch spheres out of 2 layers of output
Gray code, k-cycle modifications of multiplication tables
group theory integration
Mathematica, Matlab, CellPyLib integration
Multiple inputs/animated output
Discrete Fourier transforms everywhere
2D automata, like Conway's Game of Life, this program doesn't do the 2D output, but it will give you a solution for the Wolfram code (1+4+4=9 factors in a hypercomplex identity solution) and the output the 1D version of the same code. Rendering 2D animations is going to be resource intensive and maybe better suited for C++
i feel like some of these potential features are totally obvious next directions to go in, but that the project is solid enough to be at a crossroads where further developments would be application specific, and if you want to do 2D bloch spheres in Python or Fourier transforms of solutions in Matlab or Gray codes of multiplication tables in Mathematica that's cool, but on you and out of scope
deeper analysis of results, and subsets of results, like solution totals by linear rules or non-trivial identities by XOR additive rules or deeper cross referencing of common solutions, is out of scope of this paper


there is an easily browsable database of solution images at www.danielmckinley.com
consistently little-endian, consistently zero indexed,
I use Java 17 because Mathematica doesn't recognize over 61.0 and Matlab doesn't recognize over 51
error checking is mainly on user, Wolfram codes bigger than multiplication tables and multiplication tables bigger than Wolfram codes, search function calls with exponentially factorially long runtimes, grid output sizes, non-prime numbers in GaloisFields, etc. some throw errors, some produce gibberish
it is not in package format, so it is easier to use classes seperated from the GUI elsewhere,
everything is public, so you can call it from Mathematica, if it's not public, you can see it with Methods[] but not use it
Swing components use (column,row), the project uses (row,column)
there is another markdown paper about the Cayley-Dickson algorithm in the /src/Paper/ folder with some helpful diagrams
there are group theory implications here, but I come from a programming background before pure math, and speak more fluent for-loops and arrays than groups atm, but working on it
the O(n) for both the Cayley-Dickson library, d!*d!*2^(d+1) for all the tables for each degree, and the ECAasMultiplication class, (places!)^numFactors, are spectacularly bad, but manageable with the recursive version of Cayley-Dickson multiplication and smaller numbers of neighborhoods and class sizes.
some rules don't do much when extended to complex and non-negative real, however often times their complement does do something, XNOR doesn't do anything even though XOR does, ECA like 30 and 225, etc
some normalizations work better than others, the color NaN is white, so if it's going to all white its going to NaN
brainstorming future features: Fourier analysis, applying it to the prime number cellular automata [@Wolfram, p. 640], modifying the multiplication tables by Gray code and k-cycles
to-do: my factorial implementation blows up at 10 or so, and maybe 11 or 12 with the specific version of the functions and any more I would outsource to another library anyway, address concurrency in the GUI, putting all the GUI panels in a tabbed format or something and make them optional so the algorithm doesn't have to do every single panel every single time


if you're curious about how this project got started, a while back i was playing around with extending Wolfram codes from 1 row operations to 2 row operations, and that the row 2 truth table could be looked at as a subsection of a cube, with each 3 bit sub-neighborhood being an axis, and noticed in some idle time that i could make a quaternion multiplication cube pretty easily and wondered if there was any overlap between that and the elementary automata, just because they were both cubes. It turns out that rule 102 has the zero permutation set as a solution with 3 factors. The initial stages of the project used sets of random combinations of columns of input neighborhoods to make the factors, but eventually through trial and error discovered the systematic permutation way of doing it.







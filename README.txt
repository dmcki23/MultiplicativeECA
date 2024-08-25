Elementary Cellular Automata as Multiplicative Automata, MultiplicitaveECA
Daniel McKinley
June 27, 2024
https://github.com/dmcki23/MultiplicativeECA

This program transforms additive elementary cellular automata into multiplicative automata via permutations and extends the standard binary operation to complex via four kinds of multiplication. It includes a GUI to explore solutions, support libraries to construct hypercomplex multiplication tables, a short paper, algorithm visualization diagrams, Javadoc documentation, allows for user input, and is designed to integrate well elsewhere.

The main algorithm is described in /Paper/paper.md
Secondary documentation is in /Paper/UserGuide.md, including the permuted Cayley-Dickson algorithm
Javadoc code documentation is available
An appendix with a cross-section of solutions is available at www.danielmckinley.com

Installation instructions:
Option 0: github.com/dmcki23/MultiplicativeECA/
Option 1. Run the available jar file in /out/production/artifacts/MultiplicativeECA_jar/MultiplicativeECA.jar
Option 2. Download and open the source code in you favorite IDE and build and run
Option 2a. There is a zip file of the IntelliJ project available with everything in it

Updates since first submission:
Included Javadoc files in repository
Included three examples
Updated the website link
Changed the license to MIT

If you're curious about how this project got started, a while back i was playing around with extending Wolfram codes from 1 row operations to 2 row operations, and that the row 2 truth table could be looked at as a subsection of a cube, with each 3 bit sub-neighborhood being an axis, and noticed in some idle time that i could make a quaternion multiplication cube pretty easily and wondered if there was any overlap between that and the elementary automata, just because they were both cubes. It turns out that rule 102 has the zero permutation set as a solution with 3 factors. The initial stages of the project used sets of random combinations of columns of input neighborhoods to make the factors, but eventually through trial and error discovered the systematic permutation way of doing it.







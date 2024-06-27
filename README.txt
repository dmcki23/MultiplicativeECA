consistently little-endian, consistently zero indexed,
uses Java 17 because Mathematica doesn't recognize over 61.0 and Matlab doesn't recognize over 51
error checking is mainly on user, Wolfram codes bigger than multiplication tables and multiplication tables bigger than Wolfram codes, search function calls with exponentially factorially long runtimes, grid output sizes, non-prime numbers in GaloisFields, etc. some throw errors, some produce gibberish
it is not in package format, so it is easier to use classes seperated from the GUI elsewhere,
everything is public, so you can call it from Mathematica, if it's not public, you can see it with Methods[] but not use it
Swing components use (column,row), the project uses (row,column)
//todo
Installation instructions:
Option 1. Run the available jar file in /out/production/artifacts/MultiplicativeECA_jar/MultiplicativeECA.jar
Option 2. Open the source code in you favorite IDE and build and run
Option 3. Mathematica uses the JLink package to load Java objects in a notebook, see Mathematica documentation

Javadoc:
Open the source code in your favorite editor and use the generate Javadoc tool
Browse the provided HTML Javadoc

To use the GUI, call an instance of SwingDashboard.
To use the search classes apart from the GUI:
    Create ECAasMultiplication class
    Create the multiplication tables
    Set number of factors
    Call generalWolframCode()
    Set the partial product table
    Set your normalization parameters, if not default
    Set your input, or call the random input function
    Call validSolutionCoefficientCalculation()
    the set of ValidSolutions is in the fields of ECAMpostProcessing
    use the extract functions of ValidSolutions if you want arrays of all the solutions' fields



Hypercomplex unit vector chart\
![Unit Vector Chart](/src/Paper/unitVectorChart.jpg)

The Cayley-Dickson (CD) and Fano support classes are discussed in greater detail in the readme and the documentation. The CD algorithm verifies itself by producing the symmetric group of its degree when interacting with other CD multiplications, and the Fano library octonions produce a linear match to the CD octonions. The Fano library also produces John Baez's octonions [@Baez].

Additional CD material added from readme:
These images are in the /src/Paper/ folder

![](/src/Paper/quaternionsSplitScreenshot.jpg)\
![](/src/octonionSplitScreenshot.jpg)\
![](/src/recursionDiagram.jpg)\
![](/src/cdTablesLengthScreenshot.jpg)\

The Cayley-Dickson (CD) construction of hypercomplex numbers utilizes the equation $(a,b)x(c,d)=(ac-d*b,da+bc*)$ and that each degree hypercomplex is two of that below it, quaternions are two complex numbers, octonions are two quaternions, etc. This systematic variation of the process uses recursion to multiply two unit vectors, because generating whole sets of degree d multiplication tables has an $O(n)=d!*d!*2^(2d)$. When breaking down an octonion factor into a pair of quaternions, there is a choice of bit to group the factor, excluding the negative sign bit. You can group a quaternion by 1's place or by 2's place, you can group an octonion by 1's place, 2's place, or 4's place. If the 8 octonion unit vectors formed a cube, this bit selection is the same as selecting one of the six faces of that cube, then that face and its negative become the next two quaternions in recursion, and because it only deals with unit vectors, one of those two is going to be zero and can be discarded. The heart of the multiplication happens with quadrant, negative sign and conjugate adjustments using $(ac-d*b,da+bc*)$. When the recursion hits the base case 0 index layer, it multiplies them as reals, an XOR operation. Going up in recursion, it recombines them with a seperate factoradic. This recombination process is the deconstruction in reverse, selecting the d-dim face on which to put the result.\

The choices of bit going down in recursion and all choices of bit going up in recursion are two independent factoradics. An individual path down and an individual path back up are the bit selection list, in the source code named (cdz,cdo), with values ${0..degreeFactorial}$. These permuted Cayley-Dickson hypercomplex numbers can be any degree > 1, and multiplication tables of different bit choices table0=(cdz,cdo),table1=(cdzz,cdoo) interact independently as permutationCompositions(cdz,cdzz) and permutationCompositions(cdo,cdoo), the one's place and the unit power's place each producing the symmetric group d. Verifying this equation for degrees beyond sedonions becomes difficult as the $O(n)=d!*d!*d!*d!*2^2d$, so a stochastic checker is included.

The Fano plane code brute forces all possible numberings of the Fano plane and all possible associator bitmasks of valid numberings, resulting in 480 different octonion multiplication tables. A valid numbering of the Fano plane is 7 sets of integer triplets {1..7} that share no more than one element with any other set of triplets. An associator bitmask operates on the 7 elements of the Fano plane, adjusting the quadrant of the multiplication table it is operating in. A valid bitmask adjusts the negative signs of the 7 sets of 3 such that it is still equal to the original set of triplets.

The permuted Cayley-Dickson octonions relate to Fano plane octonions as the lexicographically first set of triplets when the Cayley-Dickson numbers are equal, $cdz=cdo,cdzz=cdoo$. These two interactions are experimentally verified by Fano.fanoTest() and CayleyDickson.cdCompareAgainstPop(). The permuted CD octonion set of multiplication tables is a commonly used octonion using the initial triplet 1*2=4, when $cdz=cdzz,cdo=cdoo$, but loses some properties of octonions when $cdz!=cdzz$ or $cdo!=cdoo$. The negative signs stay the same but the unit vector bits of each individual element are permuted. The set of Fano triplets with index 10 in the code is the set that John Baez uses [@Baez].


@book{Wolfram,
Author = {Wolfram, Stephen},
Title = {A New Kind of Science},
Year = {2002},
Publisher = {Wolfram Media},
ISBN = {1579550088},
URL = {https://www.wolframscience.com},
Language = {English}
}

@paper{Baez,
author = {Baez, John},
title = {The Octonions},
publisher = {Bulleting of the American Mathematical Society},
number = {10.1090/S0273-0979-01-00934-X}
}
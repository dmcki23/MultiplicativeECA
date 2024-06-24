---
title: 'Elementary Cellular Automata as Hypercomplex Multiplicative Automata'
tags:
    - Java
    - permutation group
    - quaternion
    - Galois field
    - Fano
    - Cayley-Dickson
    - cellular automata
authors:
    - name: Daniel W. McKinley
date: 17 June 2024
bibliography: paper.bib
---

# Summary

Elementary cellular automata (ECA) is a set of simple binary programs that produce complex output. Quaternions are frequently used in 3D graphics. Both are well-studied subjects, this Java library puts them together in a new way. This project changes additive cellular automata [@Wolfram] into multiplicative automata [@Wolfram,page 861] via permutations, hypercomplex numbers, and pointer arrays. Valid solutions extend the binary ECA to complex numbers, produce a vector field, make an algebraic polynomial, and generate interesting fractals. 

# Statement of Need

 The main algorithm produces several multiplicative versions of any given standard additive binary Wolfram code up to 32 bits and is written to support user supplied complex input at row 0 with choice of type of multiplication tables and partial product tables among other parameters. An algebraic polynomial of the automata that works with complex numbers is produced, and the hypercomplex 5-factor identity solution allows for the complex extension of any binary cellular automata. The GUI, though not required, allows for visual exploration of solutions with easy acces to various parameters. The Java this is written in is designed to integrate well in other programs, such as Mathematica's JLink or Matlab, and the Cayley-Dickson and Fano construction libraries may be valuable to the open source community as well. The project is extensively documented using Javadoc and there is an HTML indexed set of solutions to browse through.

# Functions

This library mainly deals with hypercomplex unit vectors rather than whole quaternions or octonions.  The unit vectors correspond to the pattern in the table below. The negative sign bit is the highest place bit in a unit vector, and the code as a whole is consistently little-endian and zero indexed, including the Wolfram codes and permutation sets.

Hypercomplex unit vector chart\
![Unit Vector Chart](\PaperImages\unitVectorChart.jpg)

The Cayley-Dickson (CD) and Fano support classes are discussed in greater detail in the readme and the documentation. The CD algorithm verifies itself by producing the symmetric group of its degree when interacting with other CD multiplications, and the Fano library octonions produce a linear match to the CD octonions. The Fano library also produces John Baez's octonions [@Baez].\

The main algorithm uses permutation groups and four kinds of multiplication.\
![The four kinds of multiplication used](\PaperImages\MultiplicationsDiagram.jpg)

The main algorithm uses a set of permutations operating on the neighborhood of the cellular automata, with four kinds of multiplications. The first set of multiplications, column A, brute forces all possible sets of permutations. A permutation in the set rearranges the columns of the input neighborhood, these become a set of templates applied to every possible binary input neighborhood for that Wolfram code, and becomes a hypercomplex unit vector via the binary sum of the permuted neighborhood and the chart above, becoming a set of factors.  A valid set of permutations is one that, for all possible input neighborhoods, the set of constructed factors using the permuted neighborhoods always multiplies out to a value that points to an equal value within the Wolfram code. The set of multiplication results is a pointer array that reproduces the original Wolfram code for every possible binary neighborhood. Identity solutions of 5 factors using all zero permutations exist for Wolfram codes up to 32 bits, using hypercomplex numbers and Galois addition but not Galois multiplication. The factors constructed are a loose diagonal through the multidimensional multiplication table, starting at the origin and ending at the opposite corner while.not being straight.

Permutations of 3 bit neighborhoods\
![](\PaperImages\bitPermutations.jpg)

The second and third multiplications take a valid solution from the first set of multiplications, and apply them to complex input. The second multiplication is the Cartesian product of the permuted neighborhoods, using a partial product table to generate a polynomial. The third multiplication does the binary sum of complex neighborhood, then multiplies as complex. Both the second and third multiplications take the n-th root of the result, with n = numColumns and n = numFactors, respectively. Multiplications B and C both include a binary sum of the neighborhood, parallel to the construction of the binary factors from the first part, the second as part of the normalization and the third as part of the construction.  There are several other normalization parameters after the multiplication. The last multiplication is a standard permutation composition product of a valid permutation group. The second multiplication just before the normalization is a vector, with each column of the multiplication result being a unit vector coefficient. This multiplication result neighborhood is permuted by the inverse of the permutation composition product to properly order the output vector.

The algorithm works with complex numbers and is tested via the GUI with random complex numbers. The call to the Wolfram code truth table is implemented with complex numbers by considering the sign values of the real and imaginary parts as binary numbers of two planes of seperate cellular automata using the additive Wolfram code. Values less than zero are one, and values greater than one are zero, then the neighborhood's components are seperately binary summed and plugged into the Wolfram code. If the Wolfram code is zero at each total's location, its part is zeroed out or made negative depending on parameters.

Control Panel\
![Control Panel](\PaperImages\ControlPanel.jpg)\
Fifty Four\
![Fifty four binary and non-negative real](\PaperImages\FiftyFour.jpg)\
Fifty Four\
![Fifty four text output](\PaperImages\FiftyFourText.jpg)\
Fifty Four\
![Fifty four with comlex numbers](\PaperImages\FiftyFourComplex.jpg)\
Solution's diagonal through the numFactors-dim multiplication table, loaded with Mathematica's JLink\
![Fifty four permuted diagonal plotted in Mathematica](\PaperImages\Mathematica.jpg)\

# References

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
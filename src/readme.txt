consistently little-endian, consistently zero indexed
uses Java 17 because Mathematica doesn't recognize over 61.0 and Matlab doesn't recognize over 51
error checking is on user, Wolfram codes bigger than multiplication tables and multiplication tables bigger than Wolfram codes, grid output sizes, non-prime numbers in GaloisFields, some throw errors, some produce gibberish
it is not in package format so it is easier to use classes seperated from the GUI elsewhere
everything is public so you can call it from Mathematica, if it's not public, you can see it with Methods[] but not use it
to-do: even numbered rows with 0-4 values, as complex numbers, kcycles, prime number automata, 2D automata, implement negative input - results in complex numbers in postProcessing
unsure whether to swap the row, column order in some of the function calls, because the algorithm uses row, column and Swing components use column, row. as-is for now
to-do: maybe have a permutation checker that draws Wolfram Engine data to confirm the permutations?

Installation instructions:
Option 1. Run the included JAR
Option 2. Open the source code in you favorite IDE and build and run
Option 3. Mathematica uses the JLink package to load Java objects in a notebook, see Mathematica documentation
Option 4. Matlab imports Java classes, see Matlab documentation
Javadoc:
Open the source code in your favorite editor and use the generate Javadoc tool
Browse the provided HTML Javadoc





# Summary

Elementary cellular automata (ECA), octonions, permutation compositions, and Galois fields are well-studied subjects, this Java library puts them together in a new way. Standard additive ECA [@Wolfram] are tranformed into multiplicative automata [@Wolfram,page 861] via permutation groups, hypercomplex numbers, and pointer arrays. Valid solutions extend the binary ECA to complex numbers, produce a vector field, make an algebraic polynomial, and generate interesting fractals.

# Statement of Need

 The main algorithm produces several multiplicative versions of any given standard additive binary Wolfram code up to 32 bits and is written to support user supplied complex input at row 0 with choice of type of multiplication tables and partial product tables among other parameters. An algebraic polynomial of the automata that works with complex numbers is produced, and the hypercomplex 5-factor identity solution allows for the complex extension of any binary cellular automata. The GUI, though not required, allows for visual exploration of solutions with easy acces to various parameters. The Java this is written in is designed to integrate well in other programs, such as Mathematica's JLink or Matlab and the Cayley-Dickson and Fano construction libraries may be valuable to the open source community as well. The project is extensively documented using Javadoc and there is an HTML indexed set of some images produced.

# Functions

This library mainly deals with hypercomplex unit vectors rather than whole quaternions or octonions.  The unit vectors correspond to the pattern in the table below. The negative sign bit is the highest place bit in a unit vector, and the code as a whole is consistently little-endian and zero indexed, including the Wolfram codes.

Hypercomplex unit vector chart\


The Cayley-Dickson (CD) construction of hypercomplex numbers utilizes the equation $(a,b)x(c,d)=(ac-d*b,da+bc*)$ and that each degree hypercomplex is two of that below it, quaternions are two complex numbers, octonions are two quaternions, etc. This systematic variation of the process uses recursion to multiply two unit vectors, because generating whole sets of degree d multiplication tables has an $O(n)=d!*d!*2^(2d)$. When breaking down an octonion factor into a pair of quaternions, there is a choice of bit to group the factor, excluding the negative sign bit. You can group a quaternion by 1's place or by 2's place, you can group an octonion by 1's place, 2's place, or 4's place. If the 8 octonion unit vectors formed a cube, this bit selection is the same as selecting one of the six faces of that cube, then that face and its negative become the next two quaternions in recursion, and because it only deals with unit vectors, one of those two is going to be zero and can be discarded. The heart of the multiplication happens with quadrant, negative sign and conjugate adjustments using $(ac-d*b,da+bc*)$. When the recursion hits the base case 0 index layer, it multiplies them as reals, an XOR operation. Going up in recursion, it recombines them with a seperate factoradic. This recombination process is the deconstruction in reverse, selecting the d-dim face on which to put the result.\

The choices of bit going down in recursion and all choices of bit going up in recursion are two independent factoradics. An individual path down and an individual path back up are the bit selection list, in the source code named (cdz,cdo), with values ${0..degreeFactorial}$. These permuted Cayley-Dickson hypercomplex numbers can be any degree > 1, and multiplication tables of different bit choices table0=(cdz,cdo),table1=(cdzz,cdoo) interact independently as permutationCompositions(cdz,cdzz) and permutationCompositions(cdo,cdoo), the one's place and the unit power's place each producing the symmetric group d. Verifying this equation for degrees beyond sedonions becomes difficult as the $O(n)=d!*d!*d!*d!*2^2d$, so a stochastic checker is included.

The Fano plane code brute forces all possible numberings of the Fano plane and all possible associator bitmasks of valid numberings, resulting in 480 different octonion multiplication tables. A valid numbering of the Fano plane is 7 sets of integer triplets {1..7} that share no more than one element with any other set of triplets. An associator bitmask operates on the 7 elements of the Fano plane, adjusting the quadrant of the multiplication table it is operating in. A valid bitmask adjusts the negative signs of the 7 sets of 3 such that it is still equal to the original set of triplets.

The permuted Cayley-Dickson octonions relate to Fano plane octonions as the lexicographically first set of triplets when the Cayley-Dickson numbers are equal, $cdz=cdo,cdzz=cdoo$. These two interactions are experimentally verified by Fano.fanoTest() and CayleyDickson.cdCompareAgainstPop(). The permuted CD octonion set of multiplication tables is a commonly used octonion using the initial triplet 1*2=4, when $cdz=cdzz,cdo=cdoo$, but loses some properties of octonions when $cdz!=cdzz$ or $cdo!=cdoo$. The negative signs stay the same but the unit vector bits of each individual element are permuted. The set of Fano triplets with index 10 in the code is the set that John Baez uses [@Baez].

Number of multiplication tables per degree\
![Number of tables per degree hypercomplex](\PaperImages\cdTablesLengthScreenshot.jpg)

The main algorithm uses permutation groups and four kinds of multiplication using a valid permutation group.

![The four kinds of multiplication used](\PaperImages\MultiplicationsDiagram.jpg)

The first multiplication is a binary brute-force algorithm that checks whether every possible permutation group of that size and length that when its elements are applied to the axes of the multiplication table, the diagonal always produces a value that points to an equal value of the Wolfram code. Each element of the permutation group is applied to all the indices of its own axis, representing every possible automata neighborhood. Each permutation of the neighborhood becomes a factor in the multiplication with the multiplication tables provided by the support classes, and this multiplication always reproduces the original Wolfram code when treated as a pointer.

The second and third multiplications take a valid solution from the first set of multiplications, and apply them to non-binary input. If a permutation group works for every possible binary neighborhood, it works for a given neighborhood with  complex data permuted the same way. The second and third multiplications both include a base 2 sum of the neighborhood parallel to the construction of the binary factors from the first part, the second as part of the normalization and the third as part of the construction. The second multiplication uses a partial product table to generate a polynomial. The last multiplication is a standard permutation composition product of a valild permutation group. The second multiplication just before the normalization is a vector, with each column of the multiplied automata neighborhood being a unit vector coefficient. This multiplication result neighborhood is permuted by the inverse of the permutation composition product to properly order the vector.

The algorithm works with complex numbers and is tested via the GUI with random complex numbers. The cellular automata function is implemented with complex numbers by considering the sign values of the real and imaginary parts as binary numbers of two planes of seperate cellular automata using the Wolfram code. Values less than zero are one, and values greater than one are zero, then the neighborhood's values are seperately power-summed and plugged into the Wolfram code. If the Wolfram code is zero at each total's location, its part is zeroed out. The normalization functions of the second and third multiplication sets were critical. Both take the n-th root of the result, with n = numColumns and n = numFactors, respectively, and then divide the real and imaginary parts by the radius to normalize the cell to the unit circle. If the radius = 0, don't do the unit circle normalization, using $0^0=1$.


# References


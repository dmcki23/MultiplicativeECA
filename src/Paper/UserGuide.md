Daniel McKinley
MultiplicativeECA User Guide
August 8, 2024

![](quaternionsSplitScreenshot.jpg)\
![](octonionSplitScreenshot.jpg)\
![](recursionDiagram.jpg)\
![](cdTablesLengthScreenshot.jpg)\

The Cayley-Dickson (CD) construction of hypercomplex numbers utilizes the equation $(a,b)x(c,d)=(ac-d*b,da+bc*)$ and that each degree hypercomplex is two of that below it, quaternions are two complex numbers, octonions are two quaternions, etc. This systematic variation of the process uses recursion to multiply two unit vectors, because generating whole sets of degree d multiplication tables has an $O(n)=d!*d!*2^(2d)$. When breaking down an octonion factor into a pair of quaternions, there is a choice of bit to group the factor, excluding the negative sign bit. You can group a quaternion by 1's place or by 2's place, you can group an octonion by 1's place, 2's place, or 4's place. If the 8 octonion unit vectors formed a cube, this bit selection is the same as selecting one of the six faces of that cube, then that face and its negative become the next two quaternions in recursion, and because it only deals with unit vectors, one of those two is going to be zero and can be discarded. The heart of the multiplication happens with quadrant, negative sign and conjugate adjustments using $(ac-d*b,da+bc*)$. When the recursion hits the base case 0 index layer, it multiplies them as reals, an XOR operation. Going up in recursion, it recombines them with a seperate factoradic. This recombination process is the deconstruction in reverse, selecting the d-dim face on which to put the result.\

The choices of bit going down in recursion and all choices of bit going up in recursion are two independent factoradics. An individual path down and an individual path back up are the bit selection list, in the source code named (cdz,cdo), with values ${0..degreeFactorial}$. These permuted Cayley-Dickson hypercomplex numbers can be any degree > 1, degree 1 is complex numbers and degree 1 is real, and multiplication tables of different bit choices table0=(cdz,cdo),table1=(cdzz,cdoo) interact independently as permutationCompositions(cdz,cdzz) and permutationCompositions(cdo,cdoo), the one's place and the unit power's place each producing the symmetric group d. Verifying this equation for degrees beyond sedonions becomes difficult as the $O(n)=d!*d!*d!*d!*2^2d$, so a stochastic checker is included, but it intuitively makes sense as permutations of the layers of the non-negative bits of the multiplication tables.
The Fano plane code brute forces all possible numberings of the Fano plane and all possible associator bitmasks of valid numberings, resulting in 480 different octonion multiplication tables. A valid numbering of the Fano plane is 7 sets of integer triplets {1..7} that share no more than one element with any other set of triplets. An associator bitmask operates on the 7 elements of the Fano plane, adjusting the quadrant of the multiplication table it is operating in. A valid bitmask adjusts the negative signs of the 7 sets of 3 such that it is still equal to the original set of triplets. One or three changes in any triplet invalidates that bitmask, zero or two changes is allowed.

The permuted Cayley-Dickson octonions relate to Fano plane octonions as the lexicographically first set of triplets when the Cayley-Dickson numbers are equal, $cdz=cdo,cdzz=cdoo$. These two interactions are experimentally verified by Fano.fanoTest() and CayleyDickson.cdCompareAgainstPop(). The permuted CD octonion set of multiplication tables is a commonly used octonion using the initial triplet 1*2=4, when $cdz=cdzz,cdo=cdoo$, but loses some properties of octonions when $cdz!=cdzz$ or $cdo!=cdoo$. The negative signs stay the same but the unit vector bits of each individual element are permuted. The set of Fano triplets with index 10 in the code is the set that John Baez uses [@Baez].

/////////////////////////////////////////

SwingDashboard

//////////////////////////////////////////

**ECA rule**\
    0-255 elementary cellular automata Wolfram code to search

**Multiplication table to use**\
    Type of multiplication table
    Permuted Cayley-Dickson
    XOR
    Various size Galois fields
    Various size Galois fields, shifted to exclude zeros (move elements up 1 and left 1 and subtract 1)

**Specific solution to use**\
    After the Wolfram code is analyzed with parameters via the Refresh button, this box holds all the solutions found, between 0 and neighborhoodSize^numFactors


**Degree**\
    If using permuted Cayley-Dickson, selects the degree, 2 = quaternions, 3 = octonions, 4= sedonions, etc

**Number of factors to use**\
    Dimension of the multiplication cube

**Number of rows in ECA**\
    If 1, uses standard 8-bit Wolfram code 1 row at a time
    If 2, uses the 32 bit 5 cell neighborhood extended Wolfram code that goes 2 rows at a time

**Partial product table**\
    Various types of partial product tables to use in computing the output of a solution

**Timeout**\
    Sets the value for a timeout, to prevent factorially-exponentially long search times

**Refresh, button**\
    Starts the search function with the parameters selected

**Display specific solution, button**\
    Takes the specific solution box from above as a parameter and outputs the result

**Start deep search, button**\
    Using the multiplication table selected, searches all 0-255 ECA rules and does a brief overview of results

**Width of random input, slider**\
    Number of row 0 cells to initialize with random data as input

////////////////////////////////////////////

Logic gate Wolfram codes

**Number of factors in logic gate search**\
    Dimension of the multiplication table to use

**Logic gate**\
    Which 0-15 logic gate to use as a Wolfram code

**Logic gate solutions**\
    Once a logic gate is searched, the solutions appear here, between 0 and 2^numFactors

**Which multiplication table to use**\
    Various types of multiplication tables to use in the search, XOR, Fano, permuted Cayley-Dickson, Galois fields

**Partial product table**\
    Various types of partial product tables to use in computing the output of a solution

**Refresh, button**\
    Searches the logic gate with the multiplication table selected

**Display specific solution, button**\
    Displays the output of the specific solution selected from the Logic gate solutions box from above

**Deep logic gate search, button**\
    Searches all 16 logic gates using the multiplication table selected and produces a brief analysis of the results

////////////////////////////////////////////////////////////

General table display

**Table display degree**\
    Degree of permuted Cayley-Dickson to display, 2 = quaternions, 3 = octonions, 4 = sedonions, etc

**Cayley-Dickson recursion number, down**\
    D! possiblilities

**Cayley-DIckson recursion number, up**\
    D! possibilities

**Fano plane octonions**\
    Which of 480 Fano plane/associator bitmask octonions to display, each of 30 sets of triplets has 16 valid negative sign associator bitmasks

**Galois field, prime**\
    Prime number of the Galois field to display

**Galois field, power**\
    Power of the prime number of the Galois field to display

**Length of permutations**\
    Displays all permutations of this length, 3!, 4!, etc

**Refresh, button**\
    Refreshes the permuted Cayley-Dickson options

**Display specific tables, button**\
    Displays the tables with the selected parameters

///////////////////////////////////////////////////////////

General comparison functions

**Fano/CD compare, button**\
    Runs a function that compares the Fano plane octonions with the permuted Cayley-Dickson octonions

**CD v CD, button**\
    Runs a function that compares permuted Cayley-Dickson octonions with other Cayley-Dickson octonions
    Results in permutations of the unit vector bits, the negative sign bits are unaffected,

**Random Wolfram code, button**\
    Picks a random Wolfram code of length 4 to length 32 bits, uses the identity solution and outputs the results

/////////////////////////////////////////////////////////

Normalization options

**Whether to normalize the cell to the unit complex circle**\
    If true, it normalizes the complex output to the unit circle by dividing the components by its norm

**Do the additive Wolfram code call on the complex neighborhood**\
    If true, after the output multiplications, 

**How to treat a 0 in the Wolfram code, (0,-1)**\
    If using the hybrid Wolfram code option from above, this selects whether a 0 in the Wolfram code zeroes out that cell or negates it

**Whether to avoid divisions by zero in the normalization**\
    If true and the unit circle normalization option is selected, the output algorithm sidesteps normalization of cells of norm 0
    If false, normalizations of length 0 result in NaN

**Refresh normalization parameters, button**\
    Sets the selected parameters, if you change options without clicking this the options are not registered

/////////////////////////////////////////////////////////////

Panels

/////////////////////////////////////////////////////////////

**Text output**\
    Displays specific tables
    Displays solution details

**Multiplications B, partial product multiplication as polynomial**\
    Displays Multiplications B, using the generated polynomial rather than the entire partial product multiplication. Verifies the integrity of the polynomial.

**Multiplications B, vector output**\
    Displays the solution output results after multiplication, just prior to normalization. Each layer is the relative column of its neighborhood.

**Multiplications B with non-neg real, but using the polynomial instead of the enbtire numFactors-dim partial product method**\
    Displays Multiplications B, using the entire multiplication with the partial product table.

**Additive Wolfram output into constituent permuted neighborhood factors, as layers**\
    Displays the original binary Wolfram code output, with the solution applied to the binary, each cell of each layer is a constituent factor.

**Output panel (basic ECA out, retitle panel)**\
    Displays the original Wolfram code binary output, with single-cell initial input and random initial input. Also displays multiplicative solutions applied with non-negative real floating point values.

**Multiplications C, construction from A; like B but normalization first**\
    Displays Multiplications C.

**Multiplications C, complex neighborhood just prior to normalization**\
    Displays the solution output after multiplication, just prior to normalization    

**Binary shadow of Multiplications B**\
    Output for each cell is 0 if positive, and 1 if negative. This panel shows a negative, that the complex output is not another cellular automata. The code attempts to define a Wolfram code from this rounded output, and there are conflicts between cells for any attempted Wolfram code. 




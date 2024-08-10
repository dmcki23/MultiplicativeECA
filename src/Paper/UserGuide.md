Daniel McKinley
MultiplicativeECA User Guide
August 8, 2024

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
    Does not affect the solution search, used in the output of a solution with the same dimension as the multiplication cube

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

**Fano/CD compare, button**\
    Runs a function that compares the Fano plane octonions with the permuted Cayley-Dickson octonions

**CD v CD, button**\
    Runs a function that compares permuted Cayley-Dickson octonions with other Cayley-Dickson octonions
    Results in permutations of the unit vector bits, the negative sign bits are unaffected,

**Random Wolfram code, button**\
    Picks a random Wolfram code of length 4 to length 32 bits, uses the identity solution and outputs the results

/////////////////////////////////////////////////////////

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

Examples
dig deeper into class 4
dig deeper into XOR additive
Conway's Life search



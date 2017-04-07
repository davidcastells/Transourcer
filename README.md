# Transourcer
This is an attempt to create a Source 2 Source compilation framework for C (C++ might come later) from scratch 
to try to do some code manipulation.

My recent interest has been to implement the Intel pragmas **#pragma forceinline [recursive]**.

I had some experiences using Source to Source frameworks. Together with Albert Saa we 
worked on Source 2 Source translation to translate OpenMP to HMPP and MPI  (https://github.com/sdruix/AutomaticParallelization)
in the context of our PhD Thesis.

- David Castells-Rufas.  "Scalable parallel architectures on reconfigurable platforms." Universitat Autònoma de Barcelona (2016).

- Albert Saà-Garriga. "Automatic Source Code Adaptation for Heterogeneous Platforms." Universitat Autònoma de Barcelona (2016).

In these works we have been using Mercurium, and I have been looking at different Source 2 Source 
frameworks, or compilation frameworks that support some degree of Source generation.

- Scout https://tu-dresden.de/zih/forschung/projekte/scout/
- Cetus https://engineering.purdue.edu/Cetus/
- Mercurium https://pm.bsc.es/mcxx
- LLVM http://llvm.org/
- Eclipse CDT https://eclipse.org/cdt/
- Netbeans C support

These frameworks look too big and complex to me (with the exception of Cetus). Some of them work with the preprocessed C file (after
includes are inserted, macros and defines substituted, and comments removed). I want to build an Abstract Syntax Tree (AST) with 
everything that appears in the source code, including comments, includes, pragmas, macros, and constants.

Other tools need to work with preprocessed source code (e.g the output of gcc -E) because they need complete type information. I want 
to be able to work without full type information, because, when manipulating code, you can make assumptions without having it, and it 
removes the dependencies you introduce when you have to handle includes, etc. 

Something I also lack from other frameworks is the generation of XML representation of the AST, which it is usefull for visualization, 
but could also be used to interoperate with other tools.

So my goals are:

- Keep it as simple as possible
- Process single C source file
- Ignore file dependencies
- Avoid preprocessing
- Produce XML representation of the AST

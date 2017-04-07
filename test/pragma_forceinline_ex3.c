#include <stdio.h>


//=============================
// NOT WORKING CORRECTLY
// function invocation arguments are still treated as arguments, not as expressions as should be
//=============================
// Test it by running
//  java -jar ../dist/Transourcer.jar --create-input-ast-xml --handle-pragma-forceinline pragma_forceinline_ex3.c

// Testing how to inline a void function 
// this has the complexity to identify number literals
void f1(int p)
{
	// this is part of f1
	printf("%d", p + 3);	
}

int main(int argc, char* args[])
{
#pragma forceinline
	f1(2);
}
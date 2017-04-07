#include <stdio.h>

//=============================
// ALMOST WORKING CORRECTLY
// Missing comments
//=============================
// Test it by running
//  java -jar ../dist/Transourcer.jar --create-input-ast-xml --handle-pragma-forceinline pragma_forceinline_ex1.c

// Testing how to inline a void function 
void f1(int p)
{
    // this is part of f1
    int b = p;
    b++;
    
}

int main(int argc, char* args[])
{
    int a = 2;
#pragma forceinline
	f1(a);
}
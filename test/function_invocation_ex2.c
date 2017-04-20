//=============================
// NOT WORKING CORRECTLY
//=============================
// Test it by running
//  java -jar ../dist/Transourcer.jar --create-input-ast-xml --handle-pragma-forceinline function_invocation_ex2.c

int f1(int a)
{
    int x = a;
    return x+1;
}

void f2(int a)
{
    // do nothing
}

void f3()
{
    int a;
    a = f1(a++);
    f2(a++);
}

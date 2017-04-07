/*
 * Copyright (C) 2017 Universitat Autonoma de Barcelona - David Castells-Rufas <david.castells@uab.cat>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cat.uab.cephis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.Argument;
import cat.uab.cephis.ast.ArithmeticExpression;
import cat.uab.cephis.ast.ArrayDimension;
import cat.uab.cephis.ast.AssignmentExpression;
import cat.uab.cephis.ast.Comment;
import cat.uab.cephis.ast.Empty;
import cat.uab.cephis.ast.ExpressionBlock;
import cat.uab.cephis.ast.ForStatement;
import cat.uab.cephis.ast.FunctionDeclaration;
import cat.uab.cephis.ast.FunctionDefinition;
import cat.uab.cephis.ast.FunctionInvocation;
import cat.uab.cephis.ast.IfStatement;
import cat.uab.cephis.ast.LiteralNumber;
import cat.uab.cephis.ast.LogicalExpression;
import cat.uab.cephis.ast.PreprocessorGlobalInclude;
import cat.uab.cephis.ast.PreprocessorLocalInclude;
import cat.uab.cephis.ast.PreprocessorPragma;
import cat.uab.cephis.ast.ReturnStatement;
import cat.uab.cephis.ast.StatementsBlock;
import cat.uab.cephis.ast.TernaryExpression;
import cat.uab.cephis.ast.TypeSpecifier;
import cat.uab.cephis.ast.VariableDeclaration;
import cat.uab.cephis.ast.VariableDefinition;
import cat.uab.cephis.ast.VariableReference;
import cat.uab.cephis.ast.WhileStatement;

/**
 *
 * @author dcr
 */
class CPPWriter
{
    private final AST ast;
    
    int indent = 0;
    private boolean skipSemicolon;
    

    CPPWriter(AST ast)
    {
        this.ast = ast;
    }

    void write(File file) throws FileNotFoundException
    {
        System.setOut(new PrintStream(file));
        dumpAST();
    }
    
    void dumpAST()
    {
        deepFirstDump(ast);
    }

    private void deepFirstDump(AST ast)
    {
        if (ast instanceof PreprocessorLocalInclude)
        {
            dumpPreprocessorLocalInclude((PreprocessorLocalInclude)ast);
            return;
        }
        if (ast instanceof PreprocessorGlobalInclude)
        {
            dumpPreprocessorGlobalInclude((PreprocessorGlobalInclude)ast);
            return;
        }
        if (ast instanceof FunctionDefinition)
        {
            dumpFunctionDefinition(((FunctionDefinition) ast));
            return;
        }
        if (ast instanceof StatementsBlock)
        {
            dumpStatementsBlock((StatementsBlock) ast);
            return;
        }
        if (ast instanceof AssignmentExpression)
        {
            dumpAssignment((AssignmentExpression) ast);
            return;
        }
        if (ast instanceof ArithmeticExpression)
        {
            dumpArithmetic((ArithmeticExpression)ast);
            return;
        }
        if (ast instanceof VariableDefinition)
        {
            dumpVariableDefinition((VariableDefinition) ast);
            return;
        }
        if (ast instanceof VariableDeclaration)
        {
            dumpVariableDeclaration((VariableDeclaration) ast);
            return;
        }
        if (ast instanceof VariableReference)
        {
            dumpVariableReference((VariableReference) ast);
            return;
        }
        if (ast instanceof TernaryExpression)
        {
            dumpTernary((TernaryExpression) ast);
            return;
        
        }
        if (ast instanceof LiteralNumber)
        {
            dumpLiteral((LiteralNumber) ast);
            return;
        }
        if (ast instanceof ExpressionBlock)
        {
            dumpExpressionBlock((ExpressionBlock) ast);
            return;
        }
        if (ast instanceof LogicalExpression)
        {
            dumpLogical((LogicalExpression) ast);
            return;
        }
        if (ast instanceof ForStatement)
        {
            dumpFor((ForStatement) ast);
            return;
        }
        if (ast instanceof WhileStatement)
        {
            dumpWhile((WhileStatement) ast);
            return;
        }
        if (ast instanceof ReturnStatement)
        {
            dumpReturn((ReturnStatement) ast);
            return;
        }
        if (ast instanceof ArrayDimension)
        {
            dumpArrayDimension((ArrayDimension) ast);
            return;                    
        }
        if (ast instanceof FunctionInvocation)
        {
            dumpFunctionInvocation((FunctionInvocation) ast);
            return;
        }
        if (ast instanceof Argument)
        {
            dumpArgument((Argument)ast);
            return;
        }
        if (ast instanceof IfStatement)
        {
            dumpIf((IfStatement)ast);
            return;
        }
        if (ast instanceof Comment)
        {
            dumpComment((Comment) ast);
            return;
        }
        if (ast instanceof PreprocessorPragma)
        {
            dumpPreprocessorPragma((PreprocessorPragma) ast);
            return;
        }
        if (ast instanceof Empty)
        {
            System.out.print(" ");
            return;
        }
        
        
        if (ast.size() == 0)
        {
            System.out.print("<" + ast.getClass().getSimpleName());
            System.out.print(" " );
            System.out.print("" + ast.getProperties());
            System.out.println( "/>");
        }
        else
        {
            if (ast != this.ast)
            {
                System.out.print("<" + ast.getClass().getSimpleName());
                System.out.print(" " );
                System.out.print("" + ast.getProperties());
                System.out.println( ">");
            }
        
            for (AST child : ast)
            {
                deepFirstDump(child);
            }

            if (ast != this.ast)
                System.out.println("</" + ast.getClass().getSimpleName() + ">");
        }        
    }

    private void dumpTernary(TernaryExpression ast)
    {
        deepFirstDump(ast.get(0));
        System.out.print("? ");
        deepFirstDump(ast.get(1));
        System.out.print(" : ");
        deepFirstDump(ast.get(2));
    }

    private void dumpPreprocessorLocalInclude(PreprocessorLocalInclude ast)
    {
        System.out.println("#include \"" + ast.path + "\";");
    }
    
    private void dumpPreprocessorGlobalInclude(PreprocessorGlobalInclude ast)
    {
        System.out.println("#include \"" + ast.path + "\";");
    }

    /**
     * 
     * @param ast 
     */
    private void dumpFunctionDefinition(FunctionDefinition ast)
    {
        if (ast.size() != 2)
            throw new RuntimeException();
        
        FunctionDeclaration decl = ast.getFunctionDeclaration();
        AST body = ast.getFunctionBody();
        
        dumpFunctionDeclaration(decl);
        
        deepFirstDump(body);
        
        System.out.println("");
        
    }

    private void dumpFunctionDeclaration(FunctionDeclaration decl)
    {
        TypeSpecifier type = decl.getType();
        String name = decl.getName();
        
        dumpType(type);
        
        System.out.print(" ");
        
        System.out.print(name);
        System.out.print("(");
        
        ArrayList<Argument> params = decl.getArguments();
        String sLink = "";
        for (Argument par : params)
        {
            System.out.print(sLink);
            dumpArgument(par);
            sLink = ",";
        }
        
        System.out.println(")");
        
        if (!(decl.getParent() instanceof FunctionDefinition))
            System.out.println(";");
        
        
    }

    private void dumpArgument(Argument par)
    {
        TypeSpecifier type = par.getType();
        String name = par.getName();
        String modifiers = par.getModifiers();
        
        dumpType(type);
        System.out.print(" " + name);
        System.out.print(modifiers);
    }

    private void dumpType(TypeSpecifier type)
    {
        System.out.print(type.type);
    }

    private void dumpStatementsBlock(StatementsBlock statementsBlock)
    {
        System.out.println("{");
        int lastIndent = indent;
        indent += 3;
        
        skipSemicolon = false;
        
        for (AST child : statementsBlock)
        {
            System.out.print(indentify());

            deepFirstDump(child);

            // @todo when to avoid ;
            if (skipSemicolon)
                skipSemicolon = false;
            else
                System.out.println(";");
        }
        
        indent = lastIndent;
        System.out.println(indentify() + "}");
        
        skipSemicolon= true;
    }

    private String indentify()
    {
        String ret ="";
        for (int i=0; i< indent; i++)
            ret += " ";
        
        return ret;
    }

    private void dumpAssignment(AssignmentExpression exp)
    {
        AST left = exp.get(0);
        AST right = exp.get(1);
        
        
        deepFirstDump(left);
        
        System.out.print(" " + exp.operator + " ");
        
        deepFirstDump(right);
    }

    private void dumpVariableReference(VariableReference var)
    {
        System.out.print(var.name);
        
        for (AST child : var)
        {
            deepFirstDump(child);
        }
    }

    private void dumpLiteral(LiteralNumber lit)
    {
        System.out.print(lit.value);
    }

    private void dumpExpressionBlock(ExpressionBlock blk)
    {
        System.out.print("(");
        deepFirstDump(blk.get(0));
        System.out.print(")");
    }

    private void dumpLogical(LogicalExpression log)
    {
        deepFirstDump(log.get(0));
        System.out.print(" " + log.operator + " ");
        deepFirstDump(log.get(1));
    }

    private void dumpVariableDefinition(VariableDefinition var)
    {
        TypeSpecifier type = null;
        
        try
        {
            type = (TypeSpecifier) var.get(0);
        } 
        catch(Exception e)
        {
            reportError(var);
            return;
        }
        System.out.print(type.type);
        System.out.print(" ");
        System.out.print(var.name);
        
        ArrayList<ArrayDimension> dims = var.getArrayDimensions();
        
        for (ArrayDimension dim : dims)
        {
            dumpArrayDimension(dim);
        }
        
        System.out.print(" = ");
        
        deepFirstDump(var.get(1));
    }

    private void dumpVariableDeclaration(VariableDeclaration var)
    {
        TypeSpecifier type = (TypeSpecifier) var.get(0);
        System.out.print(type.type);
        System.out.print(" ");
        System.out.print(var.name);
        
        ArrayList<ArrayDimension> dims = var.getArrayDimensions();
        
        for (ArrayDimension dim : dims)
        {
            dumpArrayDimension(dim);
        }
    }

        
    private void dumpFor(ForStatement fs)
    {
        System.out.print("for (");
        deepFirstDump(fs.get(0));
        System.out.print(";");
        deepFirstDump(fs.get(1));
        System.out.print(";");
        deepFirstDump(fs.get(2));
        System.out.println(")");
        
        if (!(fs.get(3) instanceof StatementsBlock))
        {
            int lastIndent = indent;
            indent += 3;
            System.out.print(indentify());
            deepFirstDump(fs.get(3));
            indent = lastIndent;
        }
        else
        {
            System.out.print(indentify());
            deepFirstDump(fs.get(3));
        }
    }

    private void dumpArithmetic(ArithmeticExpression ari)
    {
        if (ari.isUnary())
        {
            if (ari.isPost())
            {
                deepFirstDump(ari.get(0));
                System.out.print(ari.operator);
            }
            else
            {
                System.out.print(ari.operator);
                deepFirstDump(ari.get(0));
            }
        }
        else
        {
            deepFirstDump(ari.get(0));
            System.out.print(" " + ari.operator + " ");
            deepFirstDump(ari.get(1));
        }
    }

    private void dumpArrayDimension(ArrayDimension dim)
    {
        System.out.print("[");
        deepFirstDump(dim.get(0));
        System.out.print("]");
    }

    private void dumpFunctionInvocation(FunctionInvocation func)
    {
        System.out.print(func.name);
        System.out.print("(");
        String sLink = "";
        for (AST arg : func)
        {
            System.out.print(sLink);
            deepFirstDump(arg);
            sLink = " , ";
        }
        System.out.print(")");
    }

    private void dumpIf(IfStatement ifst)
    {
        System.out.print("if (");
        deepFirstDump(ifst.get(0));
        System.out.println(")");
        
        if (!(ifst.get(1) instanceof StatementsBlock))
        {
            int lastIndent = indent;
            indent += 3;
            System.out.print(indentify());
            deepFirstDump(ifst.get(1));
            indent = lastIndent;
        }
        else
        {
            System.out.print(indentify());
            deepFirstDump(ifst.get(1));
        }
        
        if (ifst.size() <= 2)
            // no else
            return;
        
        System.out.println(indentify() + "else");
        
        if (!(ifst.get(2) instanceof StatementsBlock))
        {
            int lastIndent = indent;
            indent += 3;
            System.out.print(indentify());
            deepFirstDump(ifst.get(2));
            indent = lastIndent;
        }
        else
            deepFirstDump(ifst.get(2));
    }

    private void reportError(AST var)
    {
        System.err.println("Invalid cast in: ");
        do
        {
            System.err.println("+" + var.toString());
            var= var.getParent();
        } while (var != null);

    }

    private void dumpComment(Comment comment)
    {
        if (comment.isSingleLine())
            System.out.println("//" + comment.comment);
        else
            System.out.println("/*" + comment.comment + "*/");
        
        skipSemicolon = true;
    }

    private void dumpPreprocessorPragma(PreprocessorPragma prag)
    {
        System.out.println("#pragma " + prag.value);
        
        skipSemicolon = true;
    }

    private void dumpReturn(ReturnStatement st)
    {
        System.out.print("return ");
        
        deepFirstDump(st.get(0));
    }

    private void dumpWhile(WhileStatement wst)
    {
        System.out.print("while (");
        deepFirstDump(wst.get(0));
        System.out.println(")");
        
        if (!(wst.get(1) instanceof StatementsBlock))
        {
            int lastIndent = indent;
            indent += 3;
            System.out.print(indentify());
            deepFirstDump(wst.get(1));
            indent = lastIndent;
        }
        else
        {
            System.out.print(indentify());
            deepFirstDump(wst.get(1));
        }
    }

    
}

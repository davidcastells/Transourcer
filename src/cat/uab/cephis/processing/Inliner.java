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
package cat.uab.cephis.processing;

import cat.uab.cephis.TextUtils;
import java.util.ArrayList;
import cat.uab.cephis.analysis.ExecutionBranches;
import cat.uab.cephis.analysis.Hierarchy;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.Argument;
import cat.uab.cephis.ast.AssignmentExpression;
import cat.uab.cephis.ast.Comment;
import cat.uab.cephis.ast.FunctionDeclaration;
import cat.uab.cephis.ast.FunctionDefinition;
import cat.uab.cephis.ast.FunctionInvocation;
import cat.uab.cephis.ast.LiteralNumber;
import cat.uab.cephis.ast.PreprocessorPragma;
import cat.uab.cephis.ast.ReturnStatement;
import cat.uab.cephis.ast.StatementsBlock;
import cat.uab.cephis.ast.TypeSpecifier;
import cat.uab.cephis.ast.VariableDeclaration;
import cat.uab.cephis.ast.VariableReference;

/**
 *
 * @author dcr
 */
public class Inliner
{
    private final AST ast;
    boolean verbose = true;
    
    private static int inlineCounter = 0;

    public Inliner(AST ast, boolean verbose)
    {
        this.ast = Cloner.clone(ast);
        this.verbose = verbose;
    }

    /**
     * Handle pragma force inline recursive
     * @return A new object with the orginal ast with the substituted function 
     *      invocations
     */
    public AST handlePragmaForceInline()
    {
        ArrayList<AST> pragmas = Matcher.findAllMatchingFromClass(ast, PreprocessorPragma.class);
        
        for (AST child : pragmas)
        {
            
            PreprocessorPragma pragma = (PreprocessorPragma) child;
            if (pragma.value.equals("forceinline recursive"))   
            {
                AST nextAST = Hierarchy.getNextSibling(pragma); //  ast.get(++i);
                
                if (nextAST instanceof FunctionInvocation)
                {
                    inlineFunction((FunctionInvocation) nextAST);
                    // Now inline all the function invocations in the inlined block
                    nextAST = Hierarchy.getNextSiblingOfClass(pragma, StatementsBlock.class);
                    
                    // if function definition was not found nextAST will be null
                    if (nextAST == null)
                        Commenter.addAfter(pragma, "[Transourcer - WARNING] Function body was not found");
                    else
                        inlineFunctionsCalledIn(nextAST);
                }
                else
                    inlineFunctionsCalledIn(nextAST);
            }
            else if (pragma.value.equals("forceinline"))  
            {
                AST nextAST = Hierarchy.getNextSibling(pragma); //  ast.get(++i);
                if (nextAST instanceof FunctionInvocation)
                {
                    inlineFunction((FunctionInvocation) nextAST);
                }
                else throw new RuntimeException();
            }
            else
                throw new RuntimeException();
            
        }
        
        return ast;
    }

    private void inlineFunctionsCalledIn(AST func)
    {
        if (verbose) System.out.println("[INFO] Inlining Functions called in function " + Hierarchy.getPathString(func));
        
        while (true)
        {
            FunctionInvocation inv = findAnyFunctionInvocationIn(func);
        
            if (inv == null)
                return;

            if (verbose) System.out.println("[INFO] handling call to function " + inv.name);

            if (!inlineFunction(inv))
                return;

        }        
    }

    /**
     * Gets any function invocation 
     * @param node
     * @return 
     */
    private FunctionInvocation findAnyFunctionInvocationIn(AST node)
    {
        if (node instanceof FunctionInvocation)
            return (FunctionInvocation) node;
        
        if (node.size() == 0)
            return null;
        
        for (AST child : node)
        {
            FunctionInvocation fi = findAnyFunctionInvocationIn(child);
            
            if (fi != null)
                return fi;
        }
        
        return null;
    }

    private FunctionDeclaration findFunctionDeclaration(AST ast, String funcName)
    {
        AST found = Matcher.findFirstMatching(ast, new MatchingRule()
        {

            @Override
            public boolean matches(AST node)
            {
                if (!(node instanceof FunctionDeclaration))
                    return false;
                
                FunctionDeclaration decl = (FunctionDeclaration) node;
                
                return decl.name.equals(funcName);
            }
        });
        
        return (FunctionDeclaration) found;
    }

    /**
     * 
     * @param ast
     * @param name
     * @return 
     */
    private FunctionDefinition findFunctionDefinition(AST ast, String funcName)
    {
        AST found = Matcher.findFirstMatching(ast, new MatchingRule()
        {

            @Override
            public boolean matches(AST node)
            {
                if (!(node instanceof FunctionDefinition))
                    return false;
                
                FunctionDefinition decl = (FunctionDefinition) node;
                
                return decl.name.equals(funcName);
            }
        });
        
        return (FunctionDefinition) found;
    }

    /**
     * 
     * @param inv
     * @return true if it was ok
     */
    private boolean inlineFunction(FunctionInvocation inv)
    {

        FunctionDeclaration decl = findFunctionDeclaration(ast, inv.name);
        FunctionDefinition def = findFunctionDefinition(ast, inv.name);


        if (verbose)
            System.out.println("[INFO] Inlining function " + inv.name);

        if (def == null)
        {
            System.out.println("[WARNING] definition of function " + inv.name + " not found!");
            return false;
        }
        
        TypeSpecifier returnType = decl.getType();
        boolean hasReturnValue = !(returnType.type.equals("void"));
        
        if (hasReturnValue)
        {
            AST invDecl = inv.getParent();
            
            VariableDeclaration varDecl = new VariableDeclaration("inlined_ret_val_" + inlineCounter );
            varDecl.add(returnType);
            
            Inserter.insertBefore(invDecl, varDecl);
            Commenter.addBefore(varDecl, "Inlining function " + inv.name);
            
            
        }
        else
            Commenter.addBefore(inv, "Inlining function " + inv.name);


        AST funcBody = Cloner.clone(def.getFunctionBody());
        
        if (hasReturnValue)
        {
            // check that there are no instructions after return
            if (returnsAreCorretlyPlaced(funcBody))
            {
                Replacer.replaceReturnsByVariableAssigment(funcBody, "inlined_ret_val_" + inlineCounter);
            }
            else
                throw new RuntimeException("Returns not correctly placed");
        }

        // it's important to know if parameters are passed by value or by reference
        // because parameters passed by value will create a local copy
        // wether referenced ones will be used directly
        ArrayList<AST> invArgs = inv.getArguments();
        ArrayList<Argument> declArgs = decl.getArguments();

        if (invArgs.size() != declArgs.size())
            throw new RuntimeException("Invocation of function " + inv.name + " mitmatched number of arguments. Declaration has " + declArgs.size() 
                    + " arguments while invocation has " + invArgs.size());
        
        ArrayList<String> variablesNotToPrefix = new ArrayList<String>();
        variablesNotToPrefix.add("inlined_ret_val_" + inlineCounter);

        funcBody.add(0, new Comment("Old Body", true));


        for (int i=0; i < declArgs.size(); i++)
        {
            Argument declArg = declArgs.get(i);
            AST invArg = invArgs.get(i);
            
            if (verbose)
                System.out.print("[INFO] arg["+i+"] " + (declArg.isPassedByValue()?"V":"R") + " " );

            ArrayList<AST> refsInArgument = Matcher.findAllMatchingFromClass(invArg, VariableReference.class);

            for (AST refInArg : refsInArgument) 
                variablesNotToPrefix.add(((VariableReference)refInArg).name);

            if (declArg.isPassedByValue())
            {
                if (verbose)
                    System.out.println("[INFO] Assign " + declArg.name + " = " +  invArg);

                AST firstFunctionExpression = funcBody.get(0);

                AssignmentExpression assign = new AssignmentExpression("=");
                VariableDeclaration ref = new VariableDeclaration(declArg.name);
                ref.add(declArg.getType());
                assign.add(ref);

                assign.add(Cloner.clone(invArg));
//                // @todo when correctly parsing function invocations this shouldn't be necessary
//                if (TextUtils.isNumber(invArg.name))
//                {
//                    LiteralNumber lit = new LiteralNumber(invArg.name);
//                    assign.add(lit);
//                }
//                else
//                {
//                    VariableReference parRef = new VariableReference(invArg.name);
//                    assign.add(parRef);
//                }

                funcBody.add(0, assign);
            }
            else
            {
                if (!(invArg instanceof VariableReference))
                    throw new RuntimeException("reference arguments should be variable references");
                
                VariableReference varref = (VariableReference) invArg;
                
                if (verbose)
                    System.out.println("[INFO] Replace " + declArg.name + " by " +  varref.name);

                Replacer.replaceVariableReferences(funcBody, declArg.name, varref.name);
            }    
        }

        funcBody.add(0, new Comment("Defining argument variables", true));

        Replacer.addPrefixToAllVariablesNotInSet(funcBody, variablesNotToPrefix, "inlined_"+inlineCounter+"_");

        if (hasReturnValue)
        {
            VariableReference ref = new VariableReference("inlined_ret_val_" + inlineCounter);
            Replacer.replaceNode(inv, ref);
            Inserter.insertBefore(ref.getParent(), funcBody);
        }
        else
            Replacer.replaceNode(inv, funcBody);
        
        inlineCounter++;
        
        return true;
    }

    /**
     * Tells if the function has a good return structure which is good
     * for just substituting returns by variable assignment
     * @param funcBody
     * @return 
     */
    private boolean returnsAreCorretlyPlaced(AST funcBody)
    {
        ArrayList<AST> returns = Matcher.findAllMatchingFromClass(funcBody, ReturnStatement.class);
        
        for (AST ret : returns)
        {
            if (!ExecutionBranches.isLastChildFromExecutionBranch(ret, funcBody))
                return false;
        }
        
        return true;
    }
    
}

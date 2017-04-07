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

import java.util.ArrayList;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.Argument;
import cat.uab.cephis.ast.AssignmentExpression;
import cat.uab.cephis.ast.FunctionInvocation;
import cat.uab.cephis.ast.ReturnStatement;
import cat.uab.cephis.ast.VariableDeclaration;
import cat.uab.cephis.ast.VariableDefinition;
import cat.uab.cephis.ast.VariableReference;

/**
 *
 * @author dcr
 */
public class Replacer
{

    static void replaceVariableReferences(AST ast, String from, String toName)
    {
        ArrayList<AST> nodes = Matcher.findAllMatching(ast, new MatchingRule()
        {
            @Override
            public boolean matches(AST node)
            {
                if (node instanceof VariableReference)
                {
                    VariableReference ref = (VariableReference) node;

                    if (ref.name.equals(from))
                        return true;

                    return false;
                }
                else if (node instanceof Argument)
                {
                    // @todo this should not be necessary if we would build a correct AST from a function invocation
                    Argument ref = (Argument) node;

                    if (ref.name.equals(from))
                        return true;

                    return false;
                }
                else
                    return false;
            }
        });
        
        for (AST node : nodes)
        {
            if (node instanceof VariableReference)
            {
                VariableReference ref = (VariableReference) node;

                ref.name = toName;
            }
            else
            {
                ((Argument)node).name = toName;
            }
                    
        }
    }

    /**
     * Replace the 
     * @param inv
     * @param funcBody 
     */
    static void replaceNode(AST replacedNode, AST newNode)
    {
        AST parent = replacedNode.getParent();
        int i = parent.indexOf(replacedNode);
        parent.set(i, newNode);
    }

    static void addPrefixToAllVariablesNotInSet(AST ast, ArrayList<String> variablesNotToPrefix, String prefix)
    {
        ArrayList<AST> nodes = Matcher.findAllMatching(ast, new MatchingRule()
        {
            @Override
            public boolean matches(AST node)
            {
                if (node instanceof VariableReference)
                {
                    VariableReference ref = (VariableReference) node;
                
                    if (variablesNotToPrefix.contains(ref.name))
                        return false;

                    return true;
                }
                else if (node instanceof VariableDeclaration)
                {
                    VariableDeclaration ref = (VariableDeclaration) node;
                
                    // all variable declarations must be inlined
//                    if (variablesNotToPrefix.contains(ref.name))
//                        return false;

                    return true;
                }
                else if (node instanceof VariableDefinition)
                {
                    VariableDefinition ref = (VariableDefinition) node;
                
                    if (variablesNotToPrefix.contains(ref.name))
                        return false;

                    return true;
                }
                else if (node instanceof Argument)
                {
                    // @todo this should be avoided when correctly parsing function invocations
                    Argument ref = (Argument) node;
                
                    if (variablesNotToPrefix.contains(ref.name))
                        return false;

                    return true;
                }
                else
                    return false;
            }
        });
        
        for (AST node : nodes)
        {
            if (node instanceof VariableReference)
            {
                VariableReference ref = (VariableReference) node;

                ref.name = prefix + ref.name;
            }
            else if (node instanceof VariableDeclaration)
            {
                VariableDeclaration ref = (VariableDeclaration) node;

                ref.name = prefix + ref.name;
            }
            else if (node instanceof VariableDefinition)
            {
                VariableDefinition ref = (VariableDefinition) node;

                ref.name = prefix + ref.name;
            }
            else if (node instanceof Argument)
            {
                Argument ref = (Argument) node;

                ref.name = prefix + ref.name;
            }
            else
                throw new RuntimeException();
        }
    }

    /**
     * Replaces a return statement by a variable assigment
     * @param funcBody
     * @param string 
     */
    static void replaceReturnsByVariableAssigment(AST funcBody, String string)
    {
        ArrayList<AST> returns = Matcher.findAllMatchingFromClass(funcBody, ReturnStatement.class);
        
        for (AST ret : returns)
        {
            AssignmentExpression assign = new AssignmentExpression("=");
            assign.add(new VariableReference(string));
            assign.add(ret.get(0));
            
            replaceNode(ret, assign);
        }
    }
    
}

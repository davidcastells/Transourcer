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
package cat.uab.cephis.analysis;

import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.FunctionDefinition;
import cat.uab.cephis.ast.FunctionInvocation;
import cat.uab.cephis.ast.PreprocessorPragma;
import cat.uab.cephis.ast.StatementsBlock;
import cat.uab.cephis.ast.VariableDefinition;

/**
 *
 * @author dcr
 */
public class Hierarchy
{

    /**
     * Gets the next sibling of the given AST
     * @param ast object 
     * @return the next AST sibling or null if it is already the last element
     */
    public static AST getNextSibling(AST ast)
    {
        AST parent = ast.getParent();
        int pos = parent.indexOf(ast);
        if (pos == -1)
            return null;
        if (pos == (parent.size()-1))
            return null;
        return parent.get(pos+1);
    }

    public static String getPathString(AST func)
    {
        AST parent = func.getParent();
        if (parent == null)
            return "";
        else
            return getPathString(parent) + "/" + getSimpleNameForPath(func);
    }

    private static String getSimpleNameForPath(AST node)
    {
        if (node instanceof FunctionDefinition)
            return ((FunctionDefinition)node).name;
        if (node instanceof FunctionInvocation)
            return ((FunctionInvocation)node).name;
        
        return node.getClass().getSimpleName();
    }

    /**
     * Gets the next sibling of a given class
     * @param nextAST
     * @param clz
     * @return 
     */
    public static AST getNextSiblingOfClass(AST nextAST, Class clz)
    {        
        do
        {
            nextAST = Hierarchy.getNextSibling(nextAST);
            
            if (nextAST == null)
                return null;
        } while (!(nextAST.getClass().equals(clz)));

        return nextAST;
    }

    public static boolean inFunctionInvocation(AST ast)
    {
        if (ast == null)
            return false;
        
        if (ast instanceof FunctionInvocation)
            return true;
        
        return inFunctionInvocation(ast.getParent());
    }

    /**
     * 
     * @param ast
     * @return 
     */
    public static boolean checkValidAscending(AST ast)
    {
        if (ast.getParent() == null)
            return true;
        
        AST parent = ast.getParent();
        
        if (parent.indexOf(ast) == -1)
        {
            System.err.println("child " + ast.toString() + " not part of children of " + parent.toString());
            return false;
        }
        
        return checkValidAscending(parent);
    }

    /**
     * Gets the last child from a node
     * @param node
     * @return the child in the highest position of the array, or null
     * if no child exist
     */
    public static AST getLastChild(AST node)
    {
        if (node.size()==0)
            return null;
        
        return node.get(node.size()-1);
    }

    /**
     * Returns the scope of this variable, which is either a function definition,
     * a block (for local variables), or the whole AST for global variables
     * 
     * We find the scope by ascending into the hierarchy and returning the first 
     * object being either FuncionDefinition, or StatementsBlock, or when parent 
     * is null for global scope
     * 
     * @param node the variable AST
     * @return 
     */
    public static AST getScope(AST node)
    {
        if (node instanceof FunctionDefinition)
            return node;
        else if (node instanceof StatementsBlock)
            return node;
        
        AST parent = node.getParent();
        
        if (parent == null)
            return node;
        
        return getScope(parent);
    }
    
}

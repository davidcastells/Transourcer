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
import cat.uab.cephis.ast.ArithmeticExpression;
import cat.uab.cephis.ast.AssignmentExpression;
import cat.uab.cephis.ast.ExpressionBlock;
import cat.uab.cephis.ast.VariableDefinition;

/**
 *
 * @author dcr
 */
public class ExecutionBranches
{
    /**
     * checks that this child has no further possible instructions after it
     * 
     * @param child
     * @param topParent
     * @return 
     */
    public static boolean isLastChildFromExecutionBranch(AST child, AST topParent)
    {
       if (child == topParent)
           return true;
       
        AST parent = child.getParent();
       
        // @todo check this for all type of control statements
        if (!isLastChild(child, parent))
            return false;
        
        return isLastChildFromExecutionBranch(parent, topParent);
    }

    private static boolean isLastChild(AST child, AST parent)
    {
        return parent.get(parent.size()-1) == child;
    }

    /**
     * The previous instruction is determined by checking the parent 
     * executability as a single instruction/block,
     * if a control instruction is found in the upwards path we return null
     * @param node
     * @return 
     */
    public static AST getPreviousInstruction(AST node)
    {
        return Hierarchy.getPreviousSibling(getTopOfInstruction(node));        
    }
    
    public static AST getTopOfInstruction(AST node)
    {
        AST parent = node.getParent();
        AST ret = null;
        
        if (parent instanceof ExpressionBlock)
            return getTopOfInstruction(parent);
        if (parent instanceof ArithmeticExpression)
            return getTopOfInstruction(parent);
        if (parent instanceof AssignmentExpression)
            ret = parent;
        if (parent instanceof VariableDefinition)
            ret = parent;
        
        return ret;
    }
    
}

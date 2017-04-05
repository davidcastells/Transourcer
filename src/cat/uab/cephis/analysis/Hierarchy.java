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

/**
 *
 * @author dcr
 */
public class Hierarchy
{

    public static AST getNextSibling(AST ast)
    {
        AST parent = ast.getParent();
        int pos = parent.indexOf(ast);
        if (pos == -1)
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
    
}

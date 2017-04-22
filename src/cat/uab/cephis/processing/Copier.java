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

import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.VariableDeclaration;
import cat.uab.cephis.ast.VariableDefinition;

/**
 *
 * @author dcr
 */
public class Copier
{

    /**
     * Copies the children from an object to the target object.
     * The original children are cloned
     * @param from
     * @param to 
     */
    static void copyChildren(AST from, AST to)
    {
        for (AST child : from)
        {
            AST copy = Cloner.clone(child);
            to.add(copy);
        }
    }
    
}

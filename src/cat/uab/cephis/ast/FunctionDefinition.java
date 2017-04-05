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
package cat.uab.cephis.ast;

/**
 *
 * @author dcr
 */
public class FunctionDefinition extends AST
{
    public String name;

    @Override
    public String getProperties()
    {
        return "name=\"" + name +"\"";
    }

    @Override
    public String toString()
    {
        return "FunctionDefinition: " + name;
    }
    
    
            
    public FunctionDeclaration getFunctionDeclaration()
    {
        return (FunctionDeclaration) get(0);
    }
    
    public AST getFunctionBody()
    {
        return get(1);
    }
    
}

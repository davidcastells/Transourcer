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
public class Argument extends AST
{
    
    public  String name;
    private final String modifiers;

    private boolean passedByValue = true;

    public Argument(TypeSpecifier paramType, String paramName, String modifiers)
    {
        this.name = paramName;
        this.modifiers = modifiers;
        
        setType(paramType);
    }

    public void setType(TypeSpecifier type)
    {
        add(type);
        
        if (type.type.contains("*"))
            passedByValue = false;
        if (type.type.contains("&"))
            passedByValue = false;
    }

    public TypeSpecifier getType()
    {
        return (TypeSpecifier) get(0);
    }

    public String getName()
    {
        return name;
    }
    
    
    @Override
    public String getProperties()
    {
        return "name=\"" + name +"\" modifiers=\"" + modifiers + "\""; 
    }

    public String getModifiers()
    {
        return modifiers;
    }
    
    public boolean isPassedByValue()
    {
        return passedByValue;
    }
    
}

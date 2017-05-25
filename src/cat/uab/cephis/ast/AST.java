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

import java.util.ArrayList;

/**
 *
 * @author dcr
 */
public class AST extends ArrayList<AST>
{
    AST children;
    AST parent;

    public String getProperties()
    {
        return "";
    }

    /**
     * Check for exact the same reference (not for equality as ArrayList does)
     * @param o
     * @return 
     */
    @Override
    public int indexOf(Object o)
    {
        for (int i=0; i < size(); i++)
            if (get(i) == o)
                return i;
        
        return -1;
    }
    
    
    
    @Override
    public String toString()
    {
        return getProperties();
    }

    @Override
    public boolean add(AST e)
    {
        e.parent = this;
        return super.add(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int i, AST e)
    {
        e.parent = this;
        super.add(i, e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AST set(int i, AST e)
    {
        e.parent = this;
        return super.set(i, e); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    public AST getParent()
    {
        return parent;
    }

    /**
     * 
     * @param o
     * @return 
     */
    public AST removeLast(Object o)
    {
        AST last = get(size()-1);

        if (last != o)
            throw new RuntimeException("Not matching");
            
        return remove(size()-1);
    }
    
    protected ArrayList getChildrenFromClass(Class theClass)
    {
        ArrayList ret = new ArrayList();
        
        for (AST child : this)
        {
            if (child.getClass().equals(theClass))
                ret.add(child);
        }
        
        return ret;
    }

    public AST getLastChild()
    {
        if (size() == 0)
            return null;
        
        return get(size()-1);
    }
    
    
    
}

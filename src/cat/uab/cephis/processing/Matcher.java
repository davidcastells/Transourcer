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



/**
 *
 * @author dcr
 */
public class Matcher
{
    /**
     * Traverses (deep first) the ast finding the first matching element
     * @param ast
     * @param rule
     * @return 
     */
    public static AST findFirstMatching(AST ast, MatchingRule rule)
    {
        if (rule.matches(ast))
            return ast;
        
        for (AST child : ast)
        {
            AST found = findFirstMatching(child, rule);
            
            if (found != null)
                return found;
        }
        
        return null;
    }
    
    
    /**
     * Traverses (deep first) the ast finding all matching elements
     * @param ast
     * @param rule
     * @return 
     */
    public static ArrayList<AST> findAllMatching(AST ast, MatchingRule rule)
    {
        ArrayList<AST> ret = new ArrayList<AST>();
        
        if (rule.matches(ast))
        {
            ret.add(ast);
        }
        
        for (AST child : ast)
        {
            ArrayList<AST> found = findAllMatching(child, rule);
            
            ret.addAll(found);
        }
        
        return ret;
    }
    
    /**
     * 
     * @param ast
     * @param cls
     * @return 
     */
    public static ArrayList<AST> findAllMatchingFromClass(AST ast, Class cls)
    {
        return findAllMatching(ast, new MatchingRule()
        {

            @Override
            public boolean matches(AST node)
            {
                return cls.equals(node.getClass());
            }
        });
    }
}

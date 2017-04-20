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
import cat.uab.cephis.ast.Comment;
import java.util.ArrayList;

/**
 *
 * @author dcr
 */
public class Remover
{
    private final AST ast;

    public Remover(AST ast)
    {
        this.ast = Cloner.clone(ast);
    }

    /**
     * Remove all the comments from an AST
     * @return 
     */
    public AST removeComments()
    {
        ArrayList<AST> comments = Matcher.findAllMatchingFromClass(ast, Comment.class);
        
        for (AST cmt : comments)
        {
            removeNode(cmt);
        }
        
        return ast;
    }

    private void removeNode(AST cmt)
    {
        AST parent = cmt.getParent();
        int pos = parent.indexOf(cmt);
        parent.remove(pos);
    }
    
}

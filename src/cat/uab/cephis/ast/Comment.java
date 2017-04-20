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

import cat.uab.cephis.TextUtils;

/**
 * This object represents a comment in the source code, either multiline
 * or single line (c++ style)
 * @author dcr
 */
public class Comment extends AST
{
    public String comment;
    private boolean singleLine = false;

    /**
     * Creates an old style C comment
     * @param string the text of the comment
     */
    public Comment(String string)
    {
        comment = string;
    }
    
    /**
     * Creates a comment, optionally single line style
     * @param string comment text
     * @param b true to create single line comments
     */
    public Comment(String string, boolean b)
    {
        comment = string;
        singleLine = b;
    }

    /**
     * Properties for XML 
     * @return 
     */
    @Override
    public String getProperties()
    {
        return "comment=\"" + TextUtils.encode(comment) + "\"";
    }

    /**
     * Use it to change the style of the comment to single line or multiline
     * @param b 
     */
    public void setSingleLine(boolean b)
    {
        this.singleLine = b;
    }

    /**
     * 
     * @return true if this is a single line comment
     */
    public boolean isSingleLine()
    {
        return singleLine;
    }
    
}

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
 *
 * @author dcr
 */
public class Comment extends AST
{
    public String comment;
    private boolean singleLine = false;

    public Comment(String string)
    {
        comment = string;
    }
    
    public Comment(String string, boolean b)
    {
        comment = string;
        singleLine = b;
    }

    @Override
    public String getProperties()
    {
        return "comment=\"" + TextUtils.encode(comment) + "\"";
    }

    public void setSingleLine(boolean b)
    {
        this.singleLine = b;
    }

    public boolean isSingleLine()
    {
        return singleLine;
    }
    
}

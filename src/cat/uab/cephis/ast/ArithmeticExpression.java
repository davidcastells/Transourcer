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
public class ArithmeticExpression extends AST
{
    public String operator;
    private boolean unary = false;  // binary expressions by default
    private boolean isPost = true;

    public ArithmeticExpression()
    {    
    }
    
    public ArithmeticExpression(String operator, AST left, AST right)
    {
        this.operator = operator;
        add(left);
        add(right);
    }

    
    @Override
    public String getProperties()
    {
        return "operator=\"" + TextUtils.encode(operator) + "\"";
    }

    /**
     * @param operator 
     */
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    /**
     * @param b 
     */
    public void setUnary(boolean b)
    {
        unary = b;
    }

    public boolean isUnary()
    {
        return unary;
    }

    public boolean isPost()
    {
        return isPost;
    }

    public void setPos(boolean b)
    {
        isPost = b;
    }

    
}

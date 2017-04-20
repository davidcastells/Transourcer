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
package cat.uab.cephis.tokenizer;


/**
 *
 * @author dcr
 */
public class CPPToken
{
    public int token;
    public String sval;
    public int lineNumber;

    public CPPToken(int v, String sval, int lineno)
    {
        token = v;
        if (v == StreamTokenizer.TT_WORD)
            this.sval = sval;
        else if (v == StreamTokenizer.TT_NUMBER)
            this.sval = sval;
        else if (v == StreamTokenizer.TT_OPERATOR)
            this.sval = sval;
        else if (v == StreamTokenizer.TT_PREPROCESSOR)
            this.sval = sval;
        else if (v == StreamTokenizer.TT_COMMENT)
            this.sval = sval;
        else if (v == '\"')
        {
            // sval already contains the text literal
            this.sval = sval;
        }
        else 
            this.sval = "" + Character.valueOf((char)v);
        
        lineNumber = lineno;
    }
    
    public String toString()
    {
        if (token == StreamTokenizer.TT_WORD)
            return "\"" + sval +"\"";
        else if (token == StreamTokenizer.TT_NUMBER)
            return "" + sval;
        else if (token == StreamTokenizer.TT_OPERATOR)
            return "" + sval;
        else
            return "["+token+"] " + sval;
    }

    


    public boolean isAnyOperator()
    {
        return (token == StreamTokenizer.TT_OPERATOR);
    }
    
    public boolean isArithmeticOperator()
    {
        return (isUnaryArithmeticOperator() || isBinaryArithmeticOperator());
    }
    
    public boolean isLogicalOperator()
    {
        return (isUnaryLogicalOperator() || isBinaryLogicalOperator());
    }
    
    public boolean isTernaryOperator()
    {
        return (token == StreamTokenizer.TT_OPERATOR && sval.equals("?"));
    }

    public boolean isUnaryArithmeticOperator()
    {
        if (token != StreamTokenizer.TT_OPERATOR)
            return false;
        
        if (sval.equals("++")) return true;
        if (sval.equals("--")) return true;
        if (sval.equals("~")) return true;
        
        return false;
    }

    public boolean isBinaryArithmeticOperator()
    {
        if (token != StreamTokenizer.TT_OPERATOR)
            return false;
        
        if (sval.equals("+")) return true;
        if (sval.equals("-")) return true;
        if (sval.equals("/")) return true;
        if (sval.equals("*")) return true;
        if (sval.equals("%")) return true;
        if (sval.equals(">>")) return true;
        if (sval.equals("<<")) return true;
        if (sval.equals("|")) return true;
        if (sval.equals("&")) return true;
        if (sval.equals("^")) return true;
        
        return false;
    }
    
    public boolean isAssigmentOperator()
    {
        // @deprecated folloding check should be removed 
        if (token == '=')
            return true;
        
        if (token != StreamTokenizer.TT_OPERATOR)
            return false;
        
        if (sval.equals("=")) return true;
        if (sval.equals("+=")) return true;
        if (sval.equals("-=")) return true;
        if (sval.equals("/=")) return true;
        if (sval.equals("*=")) return true;
        if (sval.equals("%=")) return true;
        if (sval.equals(">>=")) return true;
        if (sval.equals("<<=")) return true;
        if (sval.equals("|=")) return true;
        if (sval.equals("&=")) return true;
        if (sval.equals("^=")) return true;
        
        return false;
    }

    public boolean isUnaryLogicalOperator()
    {
        if (token != StreamTokenizer.TT_OPERATOR)
            return false;
        
        if (sval.equals("!")) return true;
        
        return false;
    }

    public boolean isBinaryLogicalOperator()
    {
        if (token != StreamTokenizer.TT_OPERATOR)
            return false;
        
        if (sval.equals(">")) return true;
        if (sval.equals("<")) return true;
        if (sval.equals(">=")) return true;
        if (sval.equals("<=")) return true;
        if (sval.equals("==")) return true;
        if (sval.equals("!=")) return true;
        if (sval.equals("&&")) return true;
        if (sval.equals("||")) return true;
        
        return false;
    }

    public boolean isNumber()
    {
        return token == StreamTokenizer.TT_NUMBER;
    }

    public boolean isWord()
    {
        return token == StreamTokenizer.TT_WORD;
    }

    public boolean isPreprocessor()
    {
        return token == StreamTokenizer.TT_PREPROCESSOR;
    }
}

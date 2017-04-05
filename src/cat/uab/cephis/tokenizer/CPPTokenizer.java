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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import cat.uab.cephis.ListUtils;

/**
 *
 * @author dcr
 */
public class CPPTokenizer
{
    public StreamTokenizer st;
    ArrayDeque<CPPToken> tokenBuffer;
    
    public static final int TOKEN_CODE_EXCLAMATION = 33;
    public static final int TOKEN_CODE_OPEN_PARENTHESIS = 40;
    public static final int TOKEN_CODE_CLOSE_PARENTHESIS = 41;
    public static final int TOKEN_CODE_ASTERISC = 42;
    public static final int TOKEN_CODE_PLUS = 43;
    public static final int TOKEN_CODE_COMMA = 44;
    public static final int TOKEN_CODE_MINUS = 45;
    public static final int TOKEN_CODE_SLASH = 47;
    public static final int TOKEN_CODE_COLON = 58;
    public static final int TOKEN_CODE_SEMICOLON = 59;
    public static final int TOKEN_CODE_LESS_THAN = 60;
    public static final int TOKEN_CODE_EQUAL = 61;
    public static final int TOKEN_CODE_MORE_THAN = 62;
    public static final int TOKEN_CODE_QUESTION = 63;
    public static final int TOKEN_CODE_OPEN_ARRAY = 91;
    public static final int TOKEN_CODE_CLOSE_ARRAY = 93;
    public static final int TOKEN_CODE_OPEN_BLOCK = 123;
    public static final int TOKEN_CODE_CLOSE_BLOCK = 125;
        

    public CPPTokenizer(FileReader fr)
    {
        st = new StreamTokenizer(fr);
        st.slashStarComments(true);
        st.slashSlashComments(true);
        st.wordChars('_', '_');
        st.ordinaryChar('/');
        
        tokenBuffer = new ArrayDeque<CPPToken>();
    }

    public CPPTokenizer(ArrayList<CPPToken> tokens)
    {
        st = null;
        tokenBuffer = new ArrayDeque<CPPToken>();
        tokenBuffer.addAll(tokens);
    }
    
    public CPPToken nextToken() throws IOException
    {
        if (tokenBuffer.size() > 0)
            return tokenBuffer.removeFirst();
        
        if (st == null)
            return new CPPToken(StreamTokenizer.TT_EOF, "",  0);
        
        return pullTokenFromStream();
    }

    /**
     * Gets all the tokens before the given token
     * @param code
     * @return
     * @throws IOException 
     */
    public ArrayList<CPPToken> nextTokensBefore(int code) throws IOException
    {
        ArrayList<CPPToken> ret = new ArrayList<CPPToken>();
        
        CPPToken lastToken = null;
        do
        {
            lastToken = nextToken();
            
            if (lastToken.token != code && lastToken.token != StreamTokenizer.TT_EOF)
                ret.add(lastToken);
            
        } while (lastToken.token != code && lastToken.token != StreamTokenizer.TT_EOF);
        
        pushBack(lastToken);
        
        return ret;
    }
    
    public boolean hasTokenBeforeSemicolon(int code) throws IOException
    {
        return hasTokenBeforeToken(code, TOKEN_CODE_SEMICOLON);
    }
    
    public boolean hasTokenBeforeToken(int code, int lastCode) throws IOException
    {
        prefetchUntilToken(lastCode);
        
        int semiColonPos = findTokenPos(lastCode);
        int openParent = findTokenPos(code);

        if (openParent == -1)
            return false;
        
        if (openParent < semiColonPos)
            return true;
        else
            return false;

    }

    public boolean hasConsecutiveTokensBeforeToken(int firstCode, int secondCode, int lastCode) throws IOException
    {
        prefetchUntilToken(lastCode);
        
        int semiColonPos = findTokenPos(lastCode);
        int openParent = findTokenPairPos(firstCode, secondCode);

        if (openParent == -1)
            return false;
        
        if (openParent < semiColonPos)
            return true;
        else
            return false;

    }

    /**
     * Determine if the function has parameters by finding a '(' token before the ';' token
     * @return 
     */
    public boolean hasParameters() throws IOException
    {
        return hasTokenBeforeSemicolon(TOKEN_CODE_OPEN_PARENTHESIS);
    }

    /**
     * Determine if the function has body by finding a '{' token before the ';' token
     * @return 
     */
    public boolean hasBody() throws IOException
    {
        return hasTokenBeforeSemicolon(TOKEN_CODE_OPEN_BLOCK);
    }
    
    /**
     * @deprecated 
     * @return
     * @throws IOException 
     */
    public boolean hasEqual() throws IOException
    {
        return hasTokenBeforeSemicolon(TOKEN_CODE_EQUAL);
    }
    
    public void pushBack(CPPToken tk)
    {
        tokenBuffer.addFirst(tk);
    }

    /**
     * Get a token from the tokenizer
     */
    private void prefetchToken() throws IOException
    {
        if (st == null)
            throw new RuntimeException("Token Prefetching not supported in non streams");

        tokenBuffer.addLast(pullTokenFromStream());
    }

    /**
     * Find the position of the token with the given code in the incomming 
     * token list
     * @param code
     * @return 
     */
    public int findTokenPos(int code)
    {
        CPPToken[] ar = new CPPToken[tokenBuffer.size()];
        
        ar = tokenBuffer.toArray(ar);
        
        for (int i=0; i < ar.length; i++)
        {
            if (ar[i].token == code)
                return i;
        }
        
        return -1;
    }
    
    public int findTokenPos(CPPToken code)
    {
        CPPToken[] ar = new CPPToken[tokenBuffer.size()];
        
        ar = tokenBuffer.toArray(ar);
        
        for (int i=0; i < ar.length; i++)
        {
            if (ar[i] == code)
                return i;
        }
        
        return -1;
    }

    private boolean prefetchBufferContainsToken(int code)
    {
        return (findTokenPos(code) != -1);
    }

    public boolean hasTokens()
    {
        if (tokenBuffer.size() > 0)
            return true;
        
        if (st == null)
            return false;
        
        // @todo check for EOF 
        return true;
    }

    /**
     * How to guess that we have a variable declaration without controlling 
     * the type? 
     * Examples:
     * Good:
     * int a;       // we have two words
     * int* p;      // we have 
     * int a = 0;   // we have two words before equal
     * Bad:
     * f1(2);       // function invocation, one word before (
     * a++;         // unary operator, would be like an implicit assignment, there is no type
     * 
     * @return 
     */
    public boolean isVariableDeclarationOrDefinition(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        
        
        String debug = dumpBufferTokens();
        
        int posSemicolon = findTokenPos(TOKEN_CODE_SEMICOLON);
        int posEqual = findTokenPos(TOKEN_CODE_EQUAL);
        int posOpenParenthesis = findTokenPos(TOKEN_CODE_OPEN_PARENTHESIS);
        int posOpenArray = findTokenPos(TOKEN_CODE_OPEN_ARRAY);
        
        int posArithOperator = -1;
        if (hasArithmeticOperatorsBefore(endingCode))
        {
            CPPToken arithOperator = findFirstArithmeticOperator(endingCode);
            posArithOperator = findTokenPos(arithOperator);
        }
        
        int posAssignOperator = -1;
        if (hasAssignOperatorsBefore(endingCode))
        {
            CPPToken assignOperator = findFirstAssignmentOperator(endingCode);
            posAssignOperator = findTokenPos(assignOperator);
        }
                
        if (posSemicolon < 2)
            // one token can not be a variable definition
            return false;
            
        if (posOpenParenthesis >=0)
            if (posOpenParenthesis < 2)
                return false;
        
        if (posAssignOperator == 1)
            // no room for type
            return false;
        
        if (posEqual >= 0 && posEqual < posSemicolon)
        {
            if (posEqual == 1)
                // no room for type
                return false;
            
            // We have an assigment, but it could be an expression
            if (posOpenArray >= 0 && posOpenArray < posEqual)
                if (posOpenArray == 1)
                    // no room for type + name 
                    return false;
            CPPToken prev = peekToken(posEqual-1);
            
            if (prev != null)
                if (prev.isAnyOperator())
                    return false;
            
        }
        
        if (posArithOperator >= 0 && posArithOperator < 2)
            // no room for type
            return false;
        
        return true;
    }

    public void prefetchUntilToken(int lastCode) throws IOException
    {
        while (!prefetchBufferContainsToken(lastCode))
        {
            prefetchToken();
        }
   }

    public String dumpBufferTokens()
    {
        String ret = "";
    
        for (CPPToken tk : tokenBuffer)
        {
            ret += tk.toString();
            ret += ",";
        }
        
        return ret;
    }

    public int countTokensBefore(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        return findTokenPos(endingCode);
    }

    /**
     * @todo substitute for a check of incoming tokens with their functions
     * @param endingCode
     * @return
     * @throws IOException 
     */
    public boolean hasArithmeticOperatorsBefore(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        
        for (CPPToken token : tokenBuffer)
        {
            if (token.isArithmeticOperator())
                return true;
            
            if (token.token == endingCode)
                return false;
        }
        
        return false;        
    }

    /**
     * @deprecated  should work in a stack based fashion
     * Get the token
     * @param endingCode
     * @return the token
     */
    public CPPToken findFirstArithmeticOperator(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        
        for (CPPToken token : tokenBuffer)
        {
            if (token.isArithmeticOperator())
                return token;
        }
        
        return null;
    }
    
    /**
     * @deprecated  should work in a stack based fashion
     * @param endingCode
     * @return
     * @throws IOException 
     */
    public CPPToken findFirstAssignmentOperator(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        
        for (CPPToken token : tokenBuffer)
        {
            if (token.isAssigmentOperator())
                return token;
        }
        
        return null;
    }

        /**
     * 
     * @param endingCode
     * @return the token
     */
    public int findFirstLogicalOperator(int endingCode)
    {
        int moreThanPos = findTokenPos(TOKEN_CODE_MORE_THAN);
        int lessThanPos = findTokenPos(TOKEN_CODE_LESS_THAN);
        int equalPos = findTokenPairPos(TOKEN_CODE_EQUAL, TOKEN_CODE_EQUAL);
        int diffPos = findTokenPairPos(TOKEN_CODE_EXCLAMATION, TOKEN_CODE_EQUAL);
        
        int min = ListUtils.minNonNeg(moreThanPos, lessThanPos, equalPos, diffPos);
                
        if (min == -1)
            throw new RuntimeException("We should be sure about finding an operator");
        
        if (min == moreThanPos) return TOKEN_CODE_MORE_THAN;
        if (min == lessThanPos) return TOKEN_CODE_LESS_THAN;
        if (min == equalPos) return TOKEN_CODE_EQUAL;
        if (min == diffPos) return TOKEN_CODE_EXCLAMATION;
        
        throw new RuntimeException("We should be sure about finding an operator");
    }

    public boolean hasLogicOperatorsBefore(int endingCode) throws IOException
    {
        if (hasTokenBeforeToken(TOKEN_CODE_LESS_THAN, endingCode)) 
        {
            int pos = findTokenPos(TOKEN_CODE_LESS_THAN);
            if (peekToken(pos+1).token == TOKEN_CODE_LESS_THAN)
                // << is not a logical operator, but a arithmetic one
                return false;
            
            return true;
        }
        if (hasTokenBeforeToken(TOKEN_CODE_MORE_THAN, endingCode))
        {
            int pos = findTokenPos(TOKEN_CODE_MORE_THAN);
            if (peekToken(pos+1).token == TOKEN_CODE_MORE_THAN)
                // >> is not a logical operator, but a arithmetic one
                return false;
            
            return true;
        }
        if (hasConsecutiveTokensBeforeToken(TOKEN_CODE_EQUAL, TOKEN_CODE_EQUAL, endingCode)) return true;
        if (hasConsecutiveTokensBeforeToken(TOKEN_CODE_EXCLAMATION, TOKEN_CODE_EQUAL, endingCode)) return true;
        
        return false;        
    }

    private int findTokenPairPos(int firstCode, int secondCode)
    {
        CPPToken[] ar = new CPPToken[tokenBuffer.size()];
        
        ar = tokenBuffer.toArray(ar);
        
        for (int i=0; i < ar.length -1; i++)
        {
            if ((ar[i].token == firstCode) && (ar[i+1].token == secondCode))
                return i;
        }
        
        return -1;
    }

    public void consumeToken(int endingCode) throws IOException
    {
        CPPToken token = nextToken();
            
        if (token.token != endingCode)
        {
            throw new RuntimeException("Not matching expecting " + endingCode + " received " + token + " at line " + st.lineno());
        }
    }

    public boolean hasFunctionInvocationBefore(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        int openPar = findTokenPos(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
        
        if (openPar < 1)
            return false;
        
        CPPToken token = peekToken(openPar-1);
        
        return (token.token == StreamTokenizer.TT_WORD);
    }

    /**
     * Gets a token from a given position
     * @param pos
     * @return 
     */
    private CPPToken peekToken(int pos)
    {
        if (pos >= tokenBuffer.size())
            return null;
        
        CPPToken[] ar = new CPPToken[tokenBuffer.size()];
        
        ar = tokenBuffer.toArray(ar);
                
        return ar[pos];
    }

    public boolean hasTernaryOperatorBefore(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        int question = findTokenPos(CPPTokenizer.TOKEN_CODE_QUESTION);
        int colon = findTokenPos(CPPTokenizer.TOKEN_CODE_COLON);
        int endPos = findTokenPos(endingCode);
        
        if (question < 0) return false;
        if (colon < 0) return false;

        if (question > endPos) return false;
        if (colon > endPos) return false;
        
        if (question < colon) return true;
        
        return false;
    }

    /**
     * 
     * @param test
     * @return
     * @throws IOException 
     */
    public boolean isNextToken(int test) throws IOException
    {
        return (peekNextToken().token == test);
    }
    
    public boolean isNextToken(String test) throws IOException
    {
        CPPToken token = nextToken();
        boolean ret = false;
        if (token.token == StreamTokenizer.TT_WORD)
            if (token.sval.equals(test))
                ret = true;

        pushBack(token);
        
        return ret;
    }

    public boolean hasVariableReference(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        int posArray = findTokenPos(TOKEN_CODE_OPEN_ARRAY);
        int posEnd = findTokenPos(endingCode);
        
        if (posEnd == 0) return false;
        
        return true;
    }

    /**
     * @deprecated use stack based fashion
     * @param endingCode
     * @return
     * @throws IOException 
     */
    public boolean hasAssignBefore(int endingCode) throws IOException
    {
//         if (hasTokenBeforeToken(TOKEN_CODE_EQUAL, endingCode))
//             return true;
         
         return (hasAssignOperatorsBefore(endingCode));
         
         /*int posEqual = findTokenPos(TOKEN_CODE_EQUAL);
         
         
         
        CPPToken token = peekToken(posEqual+1);
         
        if (token.token == TOKEN_CODE_EQUAL)
            return false;
        
         if (posEqual > 0)
         {
            token = peekToken(posEqual-1);

            if (token.token == TOKEN_CODE_EXCLAMATION)
               return false;
            
            // combined operator + = is considered assignment
            if (token.isLogicalOperator())
                return false;
         }
        
         return true;*/
    }

    public ArrayList<CPPToken> nextTokens(int numberOfTokens) throws IOException
    {
        ArrayList<CPPToken> ret = new ArrayList<CPPToken>();
        
        for (int i=0; i < numberOfTokens; i++)
            ret.add(nextToken());
        
        return ret;
    }

    private boolean hasTokenPairBeforeToken(int code0, int code1, int lastCode) throws IOException
    {
         prefetchUntilToken(lastCode);
        
        int semiColonPos = findTokenPos(lastCode);
        int openParent = findTokenPairPos(code0, code1);

        if (openParent == -1)
            return false;
        
        if (openParent < semiColonPos)
            return true;
        else
            return false;
   }

    /**
     * @deprecated 
     * @param v
     * @return 
     */
    private boolean isSingleOperatorChar(int v)
    {
        switch (v)
        {
        case TOKEN_CODE_ASTERISC:
        case TOKEN_CODE_SLASH:
        case TOKEN_CODE_PLUS:
        case TOKEN_CODE_MINUS:
        case TOKEN_CODE_MORE_THAN:
        case TOKEN_CODE_LESS_THAN:
            return true;
        }
        
        return false;
    }

    /**
     * @todo could we integrate this into the StreamTokenizer? to avoid problems with multilines..
     * 
     * @return
     * @throws IOException 
     */
    private CPPToken pullTokenFromStream() throws IOException
    {        
        int v = st.nextToken();
        
        if (v == '+')
        {
            int v2 = st.nextToken();
            if (v2 == '+')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "++", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "+=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "+", st.lineno());                
        }
        if (v == '-')            
        {
            int v2 = st.nextToken();
            if (v2 == '-')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "--", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "-=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "-", st.lineno());
        }
        if (v == '*')
        {
            int v2 = st.nextToken();
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "*=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "*", st.lineno());                
        }
        if (v == '/')            
        {
            int v2 = st.nextToken();
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "/=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "/", st.lineno());
        }
        if (v == '>')
        {
            int v2 = st.nextToken();
            if (v2 == '>')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, ">>", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, ">=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, ">", st.lineno());
        }
        if (v == '<')
        {
            int v2 = st.nextToken();
            if (v2 == '<')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "<<", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "<=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "<", st.lineno());
        }
        if (v == '|')
        {
            int v2 = st.nextToken();
            if (v2 == '|')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "||", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "|=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "|", st.lineno());
        }
        if (v == '&')
        {
            int v2 = st.nextToken();
            if (v2 == '&')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "&&", st.lineno());
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "&=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "&", st.lineno());
        }
        if (v == '=')
        {
            int v2 = st.nextToken();
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "==", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "=", st.lineno());
        }
        if (v == '!')
        {
            int v2 = st.nextToken();
            if (v2 == '=')
                return new CPPToken(StreamTokenizer.TT_OPERATOR, "!=", st.lineno());
            
            st.pushBack();
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "!", st.lineno());
        }
        if (v == '~')
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "~", st.lineno());
        if (v == '?')
            return new CPPToken(StreamTokenizer.TT_OPERATOR, "?", st.lineno());
        
        return new CPPToken(v, st.sval,  st.lineno());
    }

    private boolean hasAssignOperatorsBefore(int endingCode) throws IOException
    {
        prefetchUntilToken(endingCode);
        
        for (CPPToken token : tokenBuffer)
        {
            if (token.isAssigmentOperator())
                return true;
            
            if (token.token == endingCode)
                return false;
        }
        
        return false;   
    }

    public CPPToken peekNextToken() throws IOException
    {
        CPPToken token = nextToken();
        pushBack(token);
        return token;
    }
    

    
    
}

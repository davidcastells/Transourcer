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
package cat.uab.cephis;

import cat.uab.cephis.analysis.Hierarchy;
import cat.uab.cephis.tokenizer.CPPTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.Argument;
import cat.uab.cephis.ast.ArithmeticExpression;
import cat.uab.cephis.ast.ArrayDimension;
import cat.uab.cephis.ast.AssignmentExpression;
import cat.uab.cephis.ast.Comment;
import cat.uab.cephis.ast.Empty;
import cat.uab.cephis.ast.ExpressionBlock;
import cat.uab.cephis.ast.ForStatement;
import cat.uab.cephis.ast.FunctionDeclaration;
import cat.uab.cephis.ast.FunctionDefinition;
import cat.uab.cephis.ast.FunctionInvocation;
import cat.uab.cephis.ast.IfStatement;
import cat.uab.cephis.ast.LiteralNumber;
import cat.uab.cephis.ast.LogicalExpression;
import cat.uab.cephis.ast.PreprocessorGlobalInclude;
import cat.uab.cephis.ast.PreprocessorLocalInclude;
import cat.uab.cephis.ast.PreprocessorPragma;
import cat.uab.cephis.ast.ReturnStatement;
import cat.uab.cephis.ast.TypeSpecifier;
import cat.uab.cephis.ast.StatementsBlock;
import cat.uab.cephis.ast.TernaryExpression;
import cat.uab.cephis.ast.VariableDeclaration;
import cat.uab.cephis.ast.VariableDefinition;
import cat.uab.cephis.ast.VariableReference;
import cat.uab.cephis.ast.WhileStatement;
import cat.uab.cephis.tokenizer.CPPToken;
import cat.uab.cephis.tokenizer.StreamTokenizer;

/**
 * We try to work with incomplete type information
 * @author dcr
 */
class CPPParser
{
    private CPPTokenizer st;

    static final int PARSER_STATE_IDLE = 0;                 // 
    static final int PARSER_STATE_PREPROCESSOR = 2;         // #
    static final int PARSER_STATE_MULTILINE_COMMENT = 3;    // /* */ style
    static final int PARSER_STATE_LINE_COMMENT = 4;         // // style
    static final int PARSER_STATE_FINISH = 100;

    int parserState = PARSER_STATE_IDLE;
    private File outputFile;
    private AST doc;
    
    public boolean verbose = false;
    
    public CPPParser(boolean b)
    {
        verbose = b;
    }
    
    AST createAST(File file) throws FileNotFoundException
    {
        FileReader fr = new FileReader(file);
        
        // @todo use a tokenizer that emits comments as a token
        st = new CPPTokenizer(fr);
        
        doc = new AST();
        
        try
        {
            while (parserState != PARSER_STATE_FINISH)
                parseToken(doc);
        } 
        catch (RuntimeException ex)
        {
            Logger.getLogger(CPPParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(CPPParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return doc;
    }

    private void parseToken(AST ast) throws IOException
    {
        
        
        switch (parserState)
        {
            case PARSER_STATE_IDLE:
                parserFileIdle(ast);
                break;
            case PARSER_STATE_PREPROCESSOR:
                parsePreprocessor(ast);
                break;
            case PARSER_STATE_FINISH:
                break;
        }
    }

    
    
    private void parserFileIdle(AST ast) throws IOException
    {
        CPPToken tk = st.nextToken();

        switch (tk.token)
        {
            case StreamTokenizer.TT_PREPROCESSOR:
                st.pushBack(tk);
                parsePreprocessor(ast);
                break;
            case StreamTokenizer.TT_WORD:
                st.pushBack(tk);
                
                if (st.hasParameters())
                {
                    if (st.hasBody())
                    {
                        FunctionDefinition def = new FunctionDefinition();
                        ast.add(def);
                        parseFunctionDefinition(def);
                    }
                    else
                    {
                        FunctionDeclaration decl = new FunctionDeclaration();
                        ast.add(decl);
                        parseFunctionDeclaration(decl);
                    }
                }
                else
                {
                    if (st.hasEqual())
                    {
                        VariableDefinition def = new VariableDefinition();
                        ast.add(def);
                        parseVariableDefinition(def, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
                    }
                    else
                    {
                        VariableDeclaration dec= new VariableDeclaration();
                        ast.add(dec);
                        parseVariableDeclaration(dec, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
                    }
                }
                break;
                
            case StreamTokenizer.TT_NUMBER:
                String num = tk.sval;
                break;
            case StreamTokenizer.TT_EOF:
                parserState = PARSER_STATE_FINISH;
                break;
            case 35:
                parserState = PARSER_STATE_PREPROCESSOR;
                break;
            case StreamTokenizer.TT_COMMENT:
                st.pushBack(tk);
                parseComment(ast);
                break;
            default:
                throw new RuntimeException("Unexpected token : " + tk);
                //System.out.println("TK[" +  tk.token + "]");
            
        }    
    }

    /**
     * We have special tokens for preprocessor lines
     * @param ast
     * @throws IOException 
     */
    private void parsePreprocessor(AST ast) throws IOException
    {
        CPPToken tk = st.nextToken();

        if (!tk.isPreprocessor())
            throw new RuntimeException("Expected preprocessor token");
        
        String word = tk.sval;
        
        StreamTokenizer preSt = new StreamTokenizer(new StringReader(word));
        //preSt.whitespaceChars(' ', ' ');
        
        int preTk = preSt.nextToken();
        
        switch (preTk)
        {
            case StreamTokenizer.TT_WORD:
                word = preSt.sval;
                break;
                
            default:
                System.err.println("Syntax ERROR in line " + tk.lineNumber + " TK[" +  tk + "]");
                parserState = PARSER_STATE_FINISH;
        }
        
        if (word == null)
            throw new RuntimeException("Unexpected token " + " TK[" +  tk + "]");
        
        if (word.equals("include"))
            parseInclude(ast, preSt.untokenizedStream());
        else if (word.equals("pragma"))
            parsePragma(ast, preSt.untokenizedStream());
        else
            throw new RuntimeException("preprocessor option not supported " + word);
            
    }

    private void parseInclude(AST ast, String str) throws IOException
    {
        StreamTokenizer st = new StreamTokenizer(new StringReader(str));
        
        int token = st.nextToken();
        
        if (token == '\"')
        {
            
            // "" style includes
            String path = st.sval;
            
            ast.add(new PreprocessorLocalInclude(path));
            parserState = PARSER_STATE_IDLE;
        }
        else if (token == '<')
        {
            
            // "" style includes
            String path = st.untokenizedStream();
            if (!path.endsWith(">"))
                throw new RuntimeException();
            
            path = path.substring(0, path.length()-1);
            
            ast.add(new PreprocessorGlobalInclude(path));
            parserState = PARSER_STATE_IDLE;
        }
        else
        {
            System.err.println("Syntax ERROR in line " + st.lineno() + " Invalid Include TK[" +  token + "]");
            parserState = PARSER_STATE_FINISH;
        }
    }

    private void parseFunctionDefinition(FunctionDefinition def) throws IOException
    {
        FunctionDeclaration decl = new FunctionDeclaration();
        def.add(decl);
        parseFunctionDeclaration(decl);
        
        def.name = decl.name;
        
        if (verbose)
            System.out.println("[INFO] parsing function definition " + def.name);
        
        StatementsBlock block = new StatementsBlock();
        def.add(block);
        parseBlock(block);
    }

    private void parseFunctionDeclaration(FunctionDeclaration func) throws IOException
    {
        ArrayList<CPPToken> tokens = st.nextTokensBefore(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
        
        // consume (
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
        
        TypeSpecifier type = createTypeFromTokens(tokens, 0, tokens.size()-2);
        func.setReturnType(type);
        
        String functionName = tokens.get(tokens.size()-1).sval; 
        func.name = functionName;
        
        tokens = st.nextTokensBefore(CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS);
        
        CPPTokenizer subtokenizer = new CPPTokenizer(tokens);

        while (subtokenizer.hasTokens())
        {
            tokens = subtokenizer.nextTokensBefore(CPPTokenizer.TOKEN_CODE_COMMA);
            Argument argument = createArgumentFromTokens(tokens);
            func.addArgument(argument);
            
            if (subtokenizer.hasTokens())
                subtokenizer.nextToken();   // consume comma
        }
        
        // consume )
        CPPToken dummy = st.nextToken();
    }

    /**
     * We can asume that the last name is the variable name, and
     */
    private void parseVariableDeclaration(VariableDeclaration ast, int endingCode, boolean consumeEndingCode) throws IOException
    {
        int endTokenPos = st.countTokensBefore(endingCode);
        
        String debug = st.dumpBufferTokens();
        
        int commaPos = st.findTokenPos(CPPTokenizer.TOKEN_CODE_COMMA);
        int openArrayPos = st.findTokenPos(CPPTokenizer.TOKEN_CODE_OPEN_ARRAY);
        CPPToken assignOp = st.findFirstAssignmentOperator(endingCode);
        int assignPos = st.findTokenPos(assignOp);
        boolean isList = (commaPos >= 0);

        int namePos = ListUtils.minNonNeg(commaPos, openArrayPos, endTokenPos, assignPos) - 1; 
        
        if (namePos < 0)
            throw new RuntimeException("No variable name detected  - " + debug);
        
        TypeSpecifier type = new TypeSpecifier();
        ast.add(type);
        parseTypeSpecifier(type, namePos);
        
        ast.name = st.nextToken().sval;
        
        // @todo check the situations where 

        while (st.isNextToken(CPPTokenizer.TOKEN_CODE_OPEN_ARRAY))
        {
            CPPToken token = st.nextToken();
            if (token.token != CPPTokenizer.TOKEN_CODE_OPEN_ARRAY)
                throw new RuntimeException("Unknown token");
            
            ArrayDimension dim = new ArrayDimension();
            ast.add(dim);
            parseArrayDimension(dim, CPPTokenizer.TOKEN_CODE_CLOSE_ARRAY, true);
        }
        
//        CPPToken token = st.nextToken();
//                
//        commaPos = st.findTokenPos(CPPTokenizer.TOKEN_CODE_COMMA);
// 
//        if (commaPos >= 0)
//            throw new RuntimeException("Variable lists (comma separated) not supported yet. Line# " + st.st.lineno());
        if (st.isNextToken(CPPTokenizer.TOKEN_CODE_COMMA))
                throw new RuntimeException("Variable lists (comma separated) not supported yet. Line# " + st.st.lineno());
        
        if (!st.isNextToken(endingCode))
            parseUnexpectedToken(ast, endingCode, consumeEndingCode);
        else if (consumeEndingCode)
            st.consumeToken(endingCode);
    }

    private void parseVariableDefinition(VariableDefinition def, int endingCode, boolean consumeEndingCode) throws IOException
    {
        int endTokenPos = st.countTokensBefore(endingCode);
        
        String debug = st.dumpBufferTokens();

        int commaPos = st.findTokenPos(CPPTokenizer.TOKEN_CODE_COMMA);
        int openArrayPos = st.findTokenPos(CPPTokenizer.TOKEN_CODE_OPEN_ARRAY);
        CPPToken assignOp = st.findFirstAssignmentOperator(endingCode);
        int assignPos = st.findTokenPos(assignOp);
        boolean isList = (commaPos >= 0);

        int namePos = ListUtils.minNonNeg(commaPos, openArrayPos, endTokenPos, assignPos) - 1; 
        
        TypeSpecifier type = new TypeSpecifier();
        def.add(type);
        parseTypeSpecifier(type, namePos);
        
        def.name = st.nextToken().sval;

        dumpXML();

        // @todo check the situations where 

        while (st.isNextToken(CPPTokenizer.TOKEN_CODE_OPEN_ARRAY))
        {
            CPPToken token = st.nextToken();
            if (token.token != CPPTokenizer.TOKEN_CODE_OPEN_ARRAY)
            {
                dumpXML();
                throw new RuntimeException("Unknown token");
            }
            
            ArrayDimension dim = new ArrayDimension();
            def.add(dim);
            parseArrayDimension(dim, CPPTokenizer.TOKEN_CODE_CLOSE_ARRAY, true);
        }
        
        
        CPPToken token = st.nextToken();
        
        if (!token.isAssigmentOperator())
            throw new RuntimeException("Equal not found");

        parseExpression(def, endingCode, consumeEndingCode);
        
        if (st.peekNextToken().token == CPPTokenizer.TOKEN_CODE_COMMA)
            throw new RuntimeException("Comma not supported yet in line " + token.lineNumber);
    
//        if (consumeEndingCode)
//            st.nextToken();

    }

    private TypeSpecifier createTypeFromTokens(ArrayList<CPPToken> tokens)
    {
        return createTypeFromTokens(tokens, 0, tokens.size()-1);
    }
    
    private TypeSpecifier createTypeFromTokens(ArrayList<CPPToken> tokens, int start, int stop)
    {
        String ret = "";
        boolean lastTokenWasWord = false;
                
        for (int i=start; i <= stop; i++)
            if (i < tokens.size())
            {
                if (lastTokenWasWord && tokens.get(i).token == StreamTokenizer.TT_WORD)
                    ret += " ";
                
                ret += tokens.get(i).sval;
                lastTokenWasWord =  tokens.get(i).token == StreamTokenizer.TT_WORD;
            }
        
        return new TypeSpecifier(ret);
    }

    private String createModifiersFromTokens(ArrayList<CPPToken> tokens, int start, int stop)
    {
        String ret = "";
        
        for (int i=start; i <= stop; i++)
            if (i < tokens.size())
                ret += tokens.get(i).sval;
        
        return ret;
    }
    
    private void parseBlock(StatementsBlock block) throws IOException
    {
        // consume {
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_BLOCK);
        
        boolean doRun = true;
        while (doRun)
        {
            CPPToken token = st.nextToken();
            st.pushBack(token);
            
            switch (token.token)
            {
                case CPPTokenizer.TOKEN_CODE_CLOSE_BLOCK:
                    st.nextToken();    // consume the token
                    doRun = false;
                    break;
                default:
                    parseSimpleStatementOrBlock(block);
            }
        }        
        
    }

    /**
     * 
     * @param tokens
     * @return 
     */
    private Argument createArgumentFromTokens(ArrayList<CPPToken> tokens)
    {
        int arrayModifierPos = -1;
        for (int i=0; i < tokens.size(); i++)
        {
            // check pointers and arrays, fix sval
            CPPToken token = tokens.get(i);
            if (token.token == CPPTokenizer.TOKEN_CODE_ASTERISC)
                token.sval = "*";
            else if (token.token == CPPTokenizer.TOKEN_CODE_OPEN_ARRAY)
            {
                arrayModifierPos = i;
                token.sval = "[";
            }
            else if (token.token == CPPTokenizer.TOKEN_CODE_CLOSE_ARRAY)
                token.sval = "]";
        }
        
        TypeSpecifier type;
        String name;
        String modifiers;
        
        if (arrayModifierPos >= 0)
        {
            type = createTypeFromTokens(tokens, 0, arrayModifierPos-2);
            name = tokens.get(arrayModifierPos-1).sval;
            modifiers = createModifiersFromTokens(tokens, arrayModifierPos, tokens.size()-1);
        }
        else
        {
            type = createTypeFromTokens(tokens, 0, tokens.size()-2);
            name = tokens.get(tokens.size()-1).sval;
            modifiers = "";
        }
        
        return new Argument(type, name, modifiers);
    }

    /**
     * A control statement is recognized by having a known reserved word
     * @param sval
     * @return 
     */
    private boolean isControlStatement(String sval)
    {
        if (sval.equals("for")) return true;
        if (sval.equals("if")) return true;
        if (sval.equals("do")) return true;
        if (sval.equals("while")) return true;
        if (sval.equals("switch")) return true;
        if (sval.equals("return")) return true;
        
        return false;
    }
    
    private boolean isTypeSpecifier(String sval)
    {
        if (sval.equals("const")) return true;
        if (sval.equals("char")) return true;
        if (sval.equals("unsigned")) return true;
        if (sval.equals("short")) return true;
        if (sval.equals("long")) return true;
        if (sval.equals("int")) return true;
        if (sval.equals("float")) return true;
        if (sval.equals("double")) return true;
        
        return false;
    }

    private void parseControlStatement(AST ast) throws IOException
    {
        CPPToken token = st.nextToken();
        st.pushBack(token);
        
        if (token.sval.equals("for"))
        {
            ForStatement forst = new ForStatement();
            ast.add(forst);
            parseForStatement(forst);
        }
        else if (token.sval.equals("while"))
        {
            WhileStatement whilest = new WhileStatement();
            ast.add(whilest);
            parseWhileStatement(whilest);
        }
        else if (token.sval.equals("if"))
        {
            IfStatement ifst = new IfStatement();
            ast.add(ifst);
            parseIfStatement(ifst);
        }
        else if (token.sval.equals("return"))
        {
            ReturnStatement retst = new ReturnStatement();
            ast.add(retst);
            parseReturnStatement(retst);
        }
        else
        {
            dumpXML();
            throw new RuntimeException("control statement not supported " + token.sval);
        }
    }

    private void parseIfStatement(IfStatement ifst) throws IOException
    {
        String debug = st.dumpBufferTokens();

        CPPToken dummy = st.nextToken();    // consume if
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);

        parseExpression(ifst, CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS, true);
        
        parseSimpleStatementOrBlock(ifst);
        
        if (st.isNextToken("else"))
        {
            st.nextToken(); // consume else
            
            parseSimpleStatementOrBlock(ifst);
        }
        
        
    }
    
    private void parseForStatement(ForStatement forst) throws IOException
    {
        String debug = st.dumpBufferTokens();

        CPPToken dummy = st.nextToken();    // consume for
 
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
        
        // parse the for info
        parseInitializationExpression(forst, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
        parseExpression(forst, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
        parseExpression(forst, CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS, true);
        
        //st.nextToken(); // consume )
        dumpXML();
        
        parseSimpleStatementOrBlock(forst);
        
        dumpXML();
    }

    private void parseSimpleStatementOrBlock(AST ast) throws IOException
    {
        CPPToken token = st.nextToken();
        st.pushBack(token);
        
        switch (token.token)
        {
            case CPPTokenizer.TOKEN_CODE_OPEN_BLOCK:
                StatementsBlock block = new StatementsBlock();
                ast.add(block);
                parseBlock(block);
                break;
                
            case StreamTokenizer.TT_WORD:
                if (isControlStatement(token.sval))
                {
                    parseControlStatement(ast);
                }
                else if (isTypeSpecifier(token.sval))
                {
                    //parseTypeSpecifier(ast);
                    parseVariableDeclaration(ast);
                }
//                else if (st.isVariableDeclarationOrDefinition(CPPTokenizer.TOKEN_CODE_SEMICOLON))
//                {
//                    if (st.hasEqual())
//                    {
//                        VariableDefinition def = new VariableDefinition();
//                        ast.add(def);
//                        parseVariableDefinition(def, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
//                    }
//                    else
//                    {
//                        VariableDeclaration decl = new VariableDeclaration();
//                        ast.add(decl);
//                        parseVariableDeclaration(decl, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
//                    }
//                }
                else
                {
                    parseExpression(ast, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
                }
                break;
//                else
//                {
//                    System.err.println("UNRECOGNIZED Token Stream ");
//                    ArrayList<CPPToken> tokens = st.nextTokensBefore(CPPTokenizer.TOKEN_CODE_SEMICOLON);
//                    for (CPPToken errtk : tokens)
//                        System.err.println(errtk);
//                    
//                    throw new RuntimeException("INTERRUPTED By ERRORS");
//                }
            case StreamTokenizer.TT_COMMENT:
                parseComment(ast);
                break;
            default:
                // unknown case, parse as a regular expression
  
                parseExpression(ast, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
                break;
        }
        
            
    }

    /**
     * 
     * @param ast
     * @param endingCode
     * @param consumeEndingCode
     * @throws IOException 
     */
    private void parseExpression(AST ast, int endingCode, boolean consumeEndingCode) throws IOException
    {
        st.prefetchUntilToken(endingCode);
        String debug = st.dumpBufferTokens();
        
        dumpXML();

        CPPToken nextToken = st.nextToken();
        st.pushBack(nextToken);
        
        if (nextToken.token == CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS)
        {
            ExpressionBlock blk = new ExpressionBlock();
            ast.add(blk);
            
            st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
            
            parseExpression(blk, CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS, true);
            
            if (!st.isNextToken(endingCode))
                parseUnexpectedToken(blk, endingCode, consumeEndingCode);
            else if (consumeEndingCode)
                st.consumeToken(endingCode);
        }
        else if (nextToken.isNumber())
        {
            st.nextToken();
            
            LiteralNumber lit = new LiteralNumber(nextToken.sval);
            ast.add(lit);

            if (!st.isNextToken(endingCode))
                parseUnexpectedToken(lit, endingCode, consumeEndingCode);
            else if (consumeEndingCode)
                st.consumeToken(endingCode);
            
        }
        else if (nextToken.isUnaryArithmeticOperator())
        {
            ArithmeticExpression arit = new ArithmeticExpression();
            ast.add(arit);
            
            st.consumeToken(nextToken.token);
            
            arit.setOperator(nextToken.sval);
            arit.setUnary(true);
            arit.setPos(false);
            
            parseExpression(arit, endingCode, consumeEndingCode);
        }
        else if (nextToken.isUnaryLogicalOperator())
        {
            LogicalExpression arit = new LogicalExpression();
            ast.add(arit);
            
            st.consumeToken(nextToken.token);
            
            arit.setOperator(nextToken.sval);
            arit.setUnary(true);
            
            parseExpression(arit, endingCode, consumeEndingCode);
        }
        else if (nextToken.isAnyOperator())
        {
            // Arithmetic operators are not expected
            parseUnexpectedToken(ast.getLastChild(), endingCode, consumeEndingCode);
        }
        else if (nextToken.isPreprocessor())
        {
            parsePreprocessor(ast);
        }
//        else if (st.hasAssignBefore(endingCode))
//        {
//            AssignmentExpression asg = new AssignmentExpression();
//            ast.add(asg);
//            parseAssigment(asg, endingCode, consumeEndingCode);
//        }
//        else if (st.hasTernaryOperatorBefore(endingCode))
//        {
//            TernaryExpression tern = new TernaryExpression();
//            ast.add(tern);
//            parseTernaryExpression(tern, endingCode, consumeEndingCode);
//        }        
//        else if (st.hasArithmeticOperatorsBefore(endingCode))
//        {
//            ArithmeticExpression exp = new ArithmeticExpression();
//            ast.add(exp);
//            parseArithmeticExpression(exp, endingCode, consumeEndingCode);
//        }
//        else if (st.hasLogicOperatorsBefore(endingCode))
//        {
//            LogicalExpression exp = new LogicalExpression();
//            ast.add(exp);
//            parseLogicalExpression(exp, endingCode, consumeEndingCode);
//        }
        else if (st.countTokensBefore(endingCode) == 1)
        {
            CPPToken token = st.nextToken();
            
            if (token.token == StreamTokenizer.TT_NUMBER)
            {
                LiteralNumber lit = new LiteralNumber(token.sval);
                ast.add(lit);
            }
            else
            {
                VariableReference ref = new VariableReference(token.sval);
                ast.add(ref);
            }
            
            if (consumeEndingCode)
                st.consumeToken(endingCode);// consume endingCode
        }
        else if (st.hasFunctionInvocation())
        {
            // function invocation
            FunctionInvocation func = new FunctionInvocation();
            ast.add(func);
            parseFunctionInvocation(func, endingCode, consumeEndingCode);
        }
        else if (st.hasVariableReference(endingCode))
        {
            VariableReference ref = new VariableReference();
            ast.add(ref);
            parseVariableReference(ref, endingCode, consumeEndingCode);
        }
        else if (nextToken.token == CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS)
        {
            if (Hierarchy.inFunctionInvocation(ast))
                return;

            throw new RuntimeException("Unexpected tokens " + debug );
        }
        else
        {
            dumpXML();
            
            throw new RuntimeException("Unexpected tokens " + debug );
        }
        
        dumpXML();
    }

    private void parseAssigment(AssignmentExpression asg, int endingCode, boolean consumeEndingCode) throws IOException
    {
        VariableReference var = new VariableReference();
        asg.add(var);
        parseVariableReference(var, CPPTokenizer.TOKEN_CODE_EQUAL, false);
        
        CPPToken token = st.nextToken();
        if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
        {
            // consume token
            asg.setOperator("=");
        }
        else if (token.isAnyOperator())
        {
            // 
            asg.setOperator(token.sval + "=");
            st.consumeToken(CPPTokenizer.TOKEN_CODE_EQUAL);
        }
        else
            throw new RuntimeException("");
        
        parseExpression(asg, endingCode, consumeEndingCode);
    }

    /**
     * Parse a reference to a variable
     * @param ast
     * @param endingCode
     * @param consumeEndingCode 
     */
    private void parseVariableReference(VariableReference ast, int endingCode, boolean consumeEndingCode) throws IOException
    {
        CPPToken name = st.nextToken();
        ast.name = name.sval;
        
        dumpXML();
        
        while (st.isNextToken(CPPTokenizer.TOKEN_CODE_OPEN_ARRAY))
        {
            CPPToken token = st.nextToken();
            if (token.token != CPPTokenizer.TOKEN_CODE_OPEN_ARRAY)
                throw new RuntimeException("Unknown token");
            
            ArrayDimension dim = new ArrayDimension();
            ast.add(dim);
            parseArrayDimension(dim, CPPTokenizer.TOKEN_CODE_CLOSE_ARRAY, true);
        }
        
        if (!st.isNextToken(endingCode))
            parseUnexpectedToken(ast, endingCode, consumeEndingCode);
        else
            if (consumeEndingCode)
                    st.consumeToken(endingCode);    // consume ending code
    }

    /**
     * 
     * @param dim
     * @param TOKEN_CODE_CLOSE_ARRAY
     * @param b 
     */
    private void parseArrayDimension(ArrayDimension dim, int endingCode, boolean consumeEndingCode) throws IOException
    {
        parseExpression(dim, endingCode, consumeEndingCode);
    }

    private void parseArithmeticExpression(ArithmeticExpression exp, int endingCode, boolean consumeEndingCode) throws IOException
    {
        CPPToken operator = st.findFirstArithmeticOperator(endingCode);
        
        parseExpression(exp, operator.token, false);  // do not consume operator
        
        parseArithmeticOperator(exp);

        if (exp.isUnary())
        {
            if (consumeEndingCode)
                st.consumeToken(endingCode);
        }
        else
            parseExpression(exp, endingCode, consumeEndingCode);
        
    }

    private void parseArithmeticOperator(ArithmeticExpression exp) throws IOException
    {
        CPPToken token = st.nextToken();
        
        exp.setUnary(false);
        
        if (token.token == CPPTokenizer.TOKEN_CODE_ASTERISC)
            exp.setOperator("*");
        else if (token.token == CPPTokenizer.TOKEN_CODE_SLASH)
            exp.setOperator("/");
        else if (token.token == CPPTokenizer.TOKEN_CODE_MORE_THAN)
        {
            token = st.nextToken();
            
            if (token.token == CPPTokenizer.TOKEN_CODE_MORE_THAN)
                exp.setOperator(">>");
            else
                throw new RuntimeException("Invalid operator >+" + token.sval);
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_LESS_THAN)
        {
            token = st.nextToken();
            
            if (token.token == CPPTokenizer.TOKEN_CODE_LESS_THAN)
                exp.setOperator("<<");
            else
                throw new RuntimeException("Invalid operator >+" + token.sval);
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_PLUS)
        {
            token = st.nextToken();
            
            if (token.token == CPPTokenizer.TOKEN_CODE_PLUS)
            {
                exp.setOperator("++");
                exp.setUnary(true);
            }
            else
            {
                st.pushBack(token);
                exp.setOperator("+");
            }
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_MINUS)
            exp.setOperator("-");
        else
            throw new RuntimeException("Invalid operator " + token.sval);

    }
    
    private void parseLogicalOperator(LogicalExpression exp) throws IOException
    {
        CPPToken token = st.nextToken();
        
        if (token.token == CPPTokenizer.TOKEN_CODE_LESS_THAN)
        {
            token = st.nextToken();
            if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
                exp.setOperator("<=");
            else
            {
                st.pushBack(token);
                exp.setOperator("<");
            }
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_MORE_THAN)
        {
            token = st.nextToken();
            if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
                exp.setOperator(">=");
            else
            {
                st.pushBack(token);
                exp.setOperator(">");
            }
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
        {
            token = st.nextToken();
            if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
                exp.setOperator("==");
            else
                throw new RuntimeException("Invalid operator " + token.sval);
        }
        else if (token.token == CPPTokenizer.TOKEN_CODE_EXCLAMATION)
        {
            token = st.nextToken();
            if (token.token == CPPTokenizer.TOKEN_CODE_EQUAL)
                exp.setOperator("!=");
            else
                throw new RuntimeException("Invalid operator " + token.sval);
        }
        else
            throw new RuntimeException("Invalid operator " + token.sval);

    }

    /**
     * Parses a for initialization expression
     * @param ast
     * @param endingCode
     * @param consumeEndingCode 
     */
    private void parseInitializationExpression(AST ast, int endingCode, boolean consumeEndingCode) throws IOException
    {
        st.prefetchUntilToken(endingCode);
        String debug = st.dumpBufferTokens();
        
        if (st.countTokensBefore(endingCode) == 0)
        {
            Empty nop = new Empty();
            ast.add(nop);
            if (consumeEndingCode)
                st.consumeToken(endingCode);
            return;
        } 
            
        if (st.isVariableDeclarationOrDefinition(endingCode))
        {
            if (st.hasAssignBefore(endingCode))
            {
                VariableDefinition def = new VariableDefinition();
                    ast.add(def);
                parseVariableDefinition(def, endingCode, consumeEndingCode);
            }
            else
            {
                VariableDeclaration decl = new VariableDeclaration();
                ast.add(decl);
                parseVariableDeclaration(decl, endingCode, consumeEndingCode);
            }
        }
        else
        {
            parseExpression(ast, endingCode, consumeEndingCode);
        }
    }

    /**
     * 
     * @param type
     * @param numberOfTokens
     * @throws IOException 
     */
    private void parseTypeSpecifier(TypeSpecifier type, int numberOfTokens) throws IOException
    {
        ArrayList<CPPToken> tokens = st.nextTokens(numberOfTokens);
        TypeSpecifier temp = createTypeFromTokens(tokens, 0, numberOfTokens);
        type.type = temp.type;
    }

    private void parseLogicalExpression(LogicalExpression exp, int endingCode, boolean consumeEndingCode) throws IOException
    {
        int operator = st.findFirstLogicalOperator(endingCode);
        
        parseExpression(exp, operator, false);  // do not consume operator
        
        parseLogicalOperator(exp);
        
        parseExpression(exp, endingCode, consumeEndingCode);
    }

    void setXMLOutput(File file)
    {
        outputFile = file;
    }

    void dumpXML() throws FileNotFoundException
    {
        if (outputFile == null)
            // ignore it if there is no output file
            return;
        
        XmlPrinter printer = new XmlPrinter(doc);
        printer.dumpASTToFile(outputFile);
    }

    /**
     * @todo function invocation should create variable references as parameters, not arguments!
     * 
     * @param func
     * @param endingCode
     * @param consumeEndingCode
     * @throws IOException 
     */
    private void parseFunctionInvocation(FunctionInvocation func, int endingCode, boolean consumeEndingCode) throws IOException
    {
        String name = st.nextToken().sval;
        
        func.name = name;
        
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);
        
        boolean doRun = true;
        do
        {
            int childrenCount = func.size();
            parseExpression(func, ',', true);
            doRun = childrenCount != func.size();
            
//            int parameterEndCode = st.hasTokenBeforeToken(CPPTokenizer.TOKEN_CODE_COMMA, CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS) ? 
//                    CPPTokenizer.TOKEN_CODE_COMMA : CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS;
//
//            ArrayList<CPPToken> tokens = st.nextTokensBefore(parameterEndCode);
//
//            Argument arg = createArgumentFromTokens(tokens);
//
//            func.add(arg);
//            
//            doRun = parameterEndCode == CPPTokenizer.TOKEN_CODE_COMMA;
//            
//            st.consumeToken(parameterEndCode);
            
        } while (doRun);        

        st.consumeToken(CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS);
        
        if (!st.isNextToken(endingCode))
            parseUnexpectedToken(func, endingCode, consumeEndingCode);
        else if (consumeEndingCode)
            st.consumeToken(endingCode);
        
        
    }

    private void parseTernaryExpression(TernaryExpression tern, int endingCode, boolean consumeEndingCode) throws IOException
    {
        parseExpression(tern, CPPTokenizer.TOKEN_CODE_QUESTION, true);
        parseExpression(tern, CPPTokenizer.TOKEN_CODE_COLON, true);
        parseExpression(tern, endingCode, consumeEndingCode);
    }

    private void parseUnexpectedToken(AST ast, int endingCode, boolean consumeEndingCode) throws IOException
    {
        String debug = st.dumpBufferTokens();
        
        CPPToken token = st.nextToken();

        // unlink the ast from its parent
        AST parent = ast.getParent();
        
        if (ast instanceof AssignmentExpression)
        {
            // In assigment expressions, new unexpected tokens are build on the right part
            parent = ast;
            ast = parent.getLastChild();
            parent.removeLast(ast);
        }
        else
            parent.removeLast(ast);

        if (token.isAnyOperator())
        {
            if (token.isArithmeticOperator())
            {
                if (token.isUnaryArithmeticOperator())
                {
                    // We already parsed an expression, so take it and use it as first child
                    ArithmeticExpression arit = new ArithmeticExpression();
                    parent.add(arit);
                    arit.add(ast);
                    arit.setOperator(token.sval);
                    arit.setUnary(true);
                    arit.setPos(true);
                    //parseArithmeticOperator(parent);
                    if (consumeEndingCode)
                        st.consumeToken(endingCode);
                }
                else if (token.isBinaryArithmeticOperator())
                {
                    // We already parsed an expression, so take it and use it as first child                    
                    ArithmeticExpression arit = new ArithmeticExpression();
                    parent.add(arit);
                    arit.add(ast);
                    arit.setOperator(token.sval);
                    
                    dumpXML();
                    
                    parseExpression(arit, endingCode, consumeEndingCode);
                }
                else
                    throw new RuntimeException("not supported yet");
            }
            else if (token.isLogicalOperator())
            {
                if (token.isUnaryLogicalOperator())
                {
                    throw new RuntimeException("not supported yet");
                }
                else if (token.isBinaryLogicalOperator())
                {
                    // We already parsed an expression, so take it and use it as first child
                    LogicalExpression logic = new LogicalExpression();
                    parent.add(logic);
                    logic.add(ast);
                    logic.setOperator(token.sval);
                    //parseArithmeticOperator(parent);
                    parseExpression(logic, endingCode, consumeEndingCode);
                }
                else
                    throw new RuntimeException("not supported yet");
            }
            else if (token.isTernaryOperator())
            {
                TernaryExpression tern = new TernaryExpression();
                parent.add(tern);
                tern.add(ast);
                //parseArithmeticOperator(parent);
                parseExpression(tern, ':', true);
                parseExpression(tern, endingCode, consumeEndingCode);
            }
            else if (token.isAssigmentOperator())
            {
                AssignmentExpression assign = new AssignmentExpression(token.sval);
                parent.add(assign);
                assign.add(ast);

                //parseArithmeticOperator(parent);
                parseExpression(assign, endingCode, consumeEndingCode);
            }
            else
                throw new RuntimeException(token.sval + " not supported yet");
        }
        else if (token.isNumber())
        {
            if (token.sval.startsWith("-"))
            {
                    ArithmeticExpression arit = new ArithmeticExpression();
                    parent.add(arit);
                    arit.add(ast);
                    //arit.add(new LiteralNumber(token.sval.substring(1)));
                    arit.setOperator("-");
                    
                    dumpXML();
                    
                    CPPToken numtk = new CPPToken(token.token, token.sval.substring(1), token.lineNumber);
                    st.pushBack(numtk);
                    
                    parseExpression(arit, endingCode, consumeEndingCode);

            }
            else
                throw new RuntimeException("Found number token "  + token.toString() + " not supported yet in line " + st.st.lineno());
              
        }
        else if (token.token == ')')
        {
            parent.add(ast);
            if (Hierarchy.inFunctionInvocation(ast))
            {
                boolean isOk = Hierarchy.checkValidAscending(ast);
                // we close the loop 
                st.pushBack(token);
            }
            else
                throw new RuntimeException("Found token "  + token.toString() + " not supported yet in line " + st.st.lineno());            
        }
        else
        {
            throw new RuntimeException("Found token "  + token.toString() + " not supported yet in line " + st.st.lineno());
        }
    }

    /**
     * @deprecated 
     * @param ast
     * @throws IOException 
     */
    private void parseTypeSpecifier(AST ast) throws IOException
    {
        CPPToken token = st.nextToken();
        
        TypeSpecifier type = new TypeSpecifier(token.sval);
        ast.add(type);
    }

    private void parseVariableDeclaration(AST ast) throws IOException
    {
        VariableDeclaration decl = new VariableDeclaration();
        ast.add(decl);
        
        ArrayList<CPPToken> typeTokens = new ArrayList<CPPToken>();
        
        while (isTypeSpecifier(st.peekNextToken().sval))
        {
            typeTokens.add(st.nextToken());
        }
        
        TypeSpecifier type = createTypeFromTokens(typeTokens);
        
        decl.add(type);
        
        CPPToken token = st.nextToken();
        
        if (!token.isWord())
            throw new RuntimeException("Variable name expected token: " + token);
        
        decl.name = token.sval;
        
        token = st.nextToken();
        
        if (token.token == '[')
        {
            while (token.token == CPPTokenizer.TOKEN_CODE_OPEN_ARRAY)
            {
                ArrayDimension dim = new ArrayDimension();
                decl.add(dim);
                parseArrayDimension(dim, CPPTokenizer.TOKEN_CODE_CLOSE_ARRAY, true);
                                
                token = st.nextToken();
            }
        }
        
        if (token.token == ';')
        {
            // we are done
        }
        
        else if (token.isAssigmentOperator())
        {
            //AST parent = ast.getParent();
            
            ast.removeLast(decl);
            AssignmentExpression assign = new AssignmentExpression(token.sval);
            ast.add(assign);
            assign.add(decl);
            
            parseExpression(assign, ';', true);
        }
        
        
        dumpXML();
    }

    private void parsePragma(AST ast, String str)
    {
        PreprocessorPragma pragma = new PreprocessorPragma();
        
        ast.add(pragma);
        pragma.setValue(str);
    }

    private void parseReturnStatement(ReturnStatement retst) throws IOException
    {
        String debug = st.dumpBufferTokens();

        CPPToken dummy = st.nextToken();    // consume if
        
        if (st.peekNextToken().token == ';')
        {
            // handle return on void functions
            st.nextToken();
            return;
        }
        
        parseExpression(retst, CPPTokenizer.TOKEN_CODE_SEMICOLON, true);
    }

    private void parseWhileStatement(WhileStatement wst) throws IOException
    {
        String debug = st.dumpBufferTokens();

        CPPToken dummy = st.nextToken();    // consume while
        st.consumeToken(CPPTokenizer.TOKEN_CODE_OPEN_PARENTHESIS);

        parseExpression(wst, CPPTokenizer.TOKEN_CODE_CLOSE_PARENTHESIS, true);
        
        parseSimpleStatementOrBlock(wst);
    }

    /**
     * Parses an incomming comment token
     * @param ast 
     */
    private void parseComment(AST ast) throws IOException
    {
        CPPToken tk = st.nextToken();
        Comment cmt = null;
        if (tk.sval.startsWith("//"))
            cmt = new Comment(tk.sval.substring(2), true);
        else
            cmt = new Comment(tk.sval.substring(2, tk.sval.length()-2));
        ast.add(cmt);
    }
    
    
}

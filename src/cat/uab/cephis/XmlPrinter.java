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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import cat.uab.cephis.ast.AST;

/**
 *
 * @author dcr
 */
class XmlPrinter
{
    private final AST ast;

    XmlPrinter(AST ast)
    {
        this.ast = ast;
    }

    void dumpASTToFile(File outputFile) throws FileNotFoundException
    {
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outputFile));
        dumpAST();
        System.setOut(oldOut);
    }
    
    void dumpAST()
    {
        deepFirstDump(ast);
    }

    private void deepFirstDump(AST ast)
    {
        if (ast.size() == 0)
        {
            System.out.print("<" + ast.getClass().getSimpleName());
            System.out.print(" " );
            System.out.print("" + ast.getProperties());
            System.out.println( "/>");
        }
        else
        {
            System.out.print("<" + ast.getClass().getSimpleName());
            System.out.print(" " );
            System.out.print("" + ast.getProperties());
            System.out.println( ">");

        
            for (AST child : ast)
            {
                deepFirstDump(child);
            }

            System.out.println("</" + ast.getClass().getSimpleName() + ">");
        }        
    }
    
}

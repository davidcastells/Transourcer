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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.processing.Inliner;

/**
 * Simple Source 2 Source Java Based Framework 
 * Transourcer
 * 
 * @author dcr
 */
public class Transourcer
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Transourcer compiler = new Transourcer();
        compiler.parseArgs(args);
        compiler.run();
    }

    private boolean doForceInline = false;  
    private boolean doHelp = false;
    private boolean doCreateInputAstXml = false;
    private File inputFile = null;
    private File outputFile = null;

    /**
     * Parse program arguments
     * @param args 
     */
    private void parseArgs(String[] args)
    {
        for (int i=0; i < args.length; i++)
        {
            if (args[i].equals("--handle-pragma-forceinline"))
                doForceInline = true;
            else if (args[i].equals("--help"))
                doHelp = true;
            else if (args[i].equals("--create-input-ast-xml"))
                doCreateInputAstXml = true;
            else if (args[i].equals("--output-file"))
                outputFile = new File(args[++i]);
            else
                inputFile = new File(args[i]);
            
        }
        
        if (inputFile == null)
        {
            System.err.println("[ERROR] No input file given");
            printUsage();
            System.exit(-1);
        }
        
        if (!inputFile.exists())
        {
            System.err.println("Input file " + inputFile.getAbsolutePath() + " does not exist");
            System.exit(-1);
        }
    }

    private void printUsage()
    {
        System.out.println("Usage:");
        System.out.println("  java -jar Transourcer.jar <inputFile> [options]");
        System.out.println("");
        System.out.println("[options]");
        System.out.println("  --help                        prints this message");
        System.out.println("  --handle-pragma-forceinline   performs function inlining of the pragmas");
        System.out.println("  --create-input-ast-xml        creates a XML representation of the input file AST");
        System.out.println("  --output-file <file>          specifies the output file");
    }
    
    private void run()
    {
        if (doHelp)
            printUsage();
        
        
        
        try
        {
            // TODO code application logic here
            CPPParser parser = new CPPParser();
            
            parser.setXMLOutput(new File("c:\\temp\\test.xml"));
            
            
            AST ast = parser.createAST(inputFile);
            
            parser.dumpXML();
            
            if (doForceInline)
            {
                Inliner inliner = new Inliner(ast);
                ast = inliner.handlePragmaForceInline();
                
                XmlPrinter printer = new XmlPrinter(ast);
                printer.dumpASTToFile(new File("c:\\temp\\test.inlined.xml"));
            }
            
            CPPWriter writer = new CPPWriter(ast);
            
            if (outputFile == null)
                writer.dumpAST();
            else
                writer.write(outputFile);
            
        } 
        catch (IOException ex)
        {
            Logger.getLogger(Transourcer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}

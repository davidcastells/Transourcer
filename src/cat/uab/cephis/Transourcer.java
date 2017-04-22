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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.processing.ConstantPropagator;
import cat.uab.cephis.processing.Inliner;
import cat.uab.cephis.processing.Remover;
import cat.uab.cephis.processing.Replacer;

/**
 * Simple Source 2 Source Java Based Framework 
 * Transourcer
 * 
 * @author dcr
 */
public class Transourcer
{

    /**
     * Minimal simple main
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
    private boolean doVerbose = false;
    private boolean doRemoveComments = false;
    private boolean doRemoveConstantVariables = false;

    private File inputFile = null;
    private File outputFile = null;
    
    /**
     * Parse program arguments setting the member variables that will be used
     * in the run method
     * @param args list of command line arguments
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
            else if (args[i].equals("--no-comments"))
                doRemoveComments = true;
            else if (args[i].equals("--verbose"))
                doVerbose = true;
            else if (args[i].equals("--no-constant-variables"))
                doRemoveConstantVariables = true;
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

    /**
     * Prints a help message
     */
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
        System.out.println("  --no-comments                 removes all comments from the output");
        System.out.println("  --no-constant-variables       remove constant variables");
        System.out.println("  --verbose                     generates debugging info");
    }
    
    /**
     * Does the work instructed by the fields that are assigned during argument
     * parsing
     */
    private void run()
    {
        if (doHelp)
            printUsage();
        
        
        
        try
        {
            if (doVerbose)
                System.out.println("[INFO] Start Parsing");
            
            // TODO code application logic here
            CPPParser parser = new CPPParser(doVerbose);
            
            if (doCreateInputAstXml)
            {
                File dir = inputFile.getParentFile();
               parser.setXMLOutput(new File(dir, inputFile.getName() + ".xml"));
            }
            
            AST ast = parser.createAST(inputFile);
            
            parser.dumpXML();
            
            if (doForceInline)
            {
                if (doVerbose)
                    System.out.println("[INFO] Doing Inlining");
            
                Inliner inliner = new Inliner(ast, doVerbose);
                ast = inliner.handlePragmaForceInline();
                
                XmlPrinter printer = new XmlPrinter(ast);
                printer.dumpASTToFile(new File("c:\\temp\\test.inlined.xml"));
            }
            
            if (doRemoveComments)
            {
                if (doVerbose)
                    System.out.println("[INFO] Removing Comments");
            
                Remover remover = new Remover(ast);
                ast = remover.removeComments();
                XmlPrinter printer = new XmlPrinter(ast);
                printer.dumpASTToFile(new File("c:\\temp\\test.nocomments.xml"));                
            }
            
            if (doRemoveConstantVariables)
            {
                if (doVerbose)
                    System.out.println("[INFO] Removing constant variables");
                
                
                
                ConstantPropagator propagator = new ConstantPropagator(ast);
                propagator.setXmlOutput(new File("c:\\temp\\test.noconstants.xml"));
                ast = propagator.removeConstantVariables();
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

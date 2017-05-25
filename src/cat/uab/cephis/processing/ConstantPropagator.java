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

import cat.uab.cephis.XmlPrinter;
import cat.uab.cephis.analysis.Hierarchy;
import cat.uab.cephis.ast.AST;
import cat.uab.cephis.ast.ExpressionBlock;
import cat.uab.cephis.ast.TypeSpecifier;
import cat.uab.cephis.ast.VariableDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author dcr
 */
public class ConstantPropagator
{
    private final AST ast;
    private File outputFile;

    public ConstantPropagator(AST ast)
    {
        this.ast = ast;
    }

    void dumpXML() throws FileNotFoundException
    {
        if (outputFile == null)
            // ignore it if there is no output file
            return;
        
        XmlPrinter printer = new XmlPrinter(ast);
        printer.dumpASTToFile(outputFile);
    }

    /**
     * changes 
     *      const int p = 2;
     *      int b = p;
     * by
     *      int b = 2;
     * 
     * @return 
     */
    public AST removeConstantVariables() throws FileNotFoundException
    {
        while (true)
        {
            // First, convert assigments of variable declarations to variable definitions
            Replacer.replaceVariableDeclarationAssigmentsByDefinitions(ast);
                
            // find all constant variable definitions
            ArrayList<AST> constants = Matcher.findAllMatching(ast, new MatchingRule()
            {

                @Override
                public boolean matches(AST node)
                {
                    if (!(node instanceof VariableDefinition))
                        return false;

                    VariableDefinition def = (VariableDefinition) node;
                    TypeSpecifier type = def.getType();

                    if (type.type.contains("const"))
                        return true;

                    return false;
                }
            });

            if (constants.size() == 0)
                return ast; 

            VariableDefinition def = (VariableDefinition) constants.get(0);

            System.out.println("[INFO] Substituting constant " +  def.name);

            Remover.removeNode(def);
            
            // enclose initial value between ( ) if it is not an expression block already
            AST initValue = Cloner.clone(def.getInitialValue());
            ExpressionBlock expBlock = null;
            
            if (initValue instanceof ExpressionBlock)
                expBlock = (ExpressionBlock) initValue;
            else
            {
                expBlock = new ExpressionBlock();
                expBlock.add(initValue);
            }
            
            
            Replacer.replaceVariableReferences(Hierarchy.getScope(def), def.name, expBlock);
            
            dumpXML();
        }

    }

    public void setXmlOutput(File file)
    {
        this.outputFile = file;
    }
    
}

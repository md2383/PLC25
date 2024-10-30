package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.SemanticError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.function_nodes.*;
import provided.*;

/**
 * The parse tree representation of the program.
 * 
 * Contains a list of the function nodes.
 */
public class program_Node extends Jott_Node{

    /** An array of valid function nodes */
    private final funcDef_Node[] func;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseProgramNode})
     * @param functions - an array of function nodes
     */
    private program_Node(funcDef_Node[] functions) {
        func = functions;
    }

    /**
     * Static parse method returning the {@link program_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A program node which has been validated in accordance 
     *          with the parse tree grammar
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static program_Node parseProgramNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.isEmpty()) { return new program_Node(null); }

        ArrayList<funcDef_Node> functions = new ArrayList<>();

        do {
            functions.add(funcDef_Node.parseFunctionDefNode(tokens));
        } while(!tokens.isEmpty());
        return new program_Node(functions.toArray(new funcDef_Node[functions.size()]));
    }

    @Override
    public String convertToJott() {
        if(this.func == null) { return ""; } // empty Jott file, technically Semantic error
        StringBuilder str = new StringBuilder();
        for(funcDef_Node function : func) { str.append(function.convertToJott()); }
        return str.toString();
    }

    @Override
    public boolean validateTree() {
        declared_functions.declareBuiltinFunctions();

        boolean isValid = true;

        for(funcDef_Node function : func) {
            isValid &= function.validateTree();
        }

        if(!declared_functions.contains("main")) {
            new SemanticError("Expected function 'Def main[]:Void' not present")
                .print(null, declared_functions.getNode("main").linenum);
            isValid = false;
        } 

        return isValid;
    }
}

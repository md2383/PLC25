package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * A variable declaration representation for the parse tree
 */
public class varDec_Node extends Jott_Node {
    
    /** Valid {@code type_Node} token reference */
    private final type_Node type;     
    private final id_Node id;    

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseVarDecNode})
     * @param type a validated {@code type_Node} token reference
     * @param id a validated {@code id_Node} token reference
     */
    private varDec_Node(type_Node type, id_Node id) {
        this.type = type;
        this.id = id;
    }
    
    /**
     * Static parse method returning a {@link varDec_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A variable declaration node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not Valid String}: token being 
     *                      parsed is not a {@code TokenType.STRING}
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static varDec_Node parseVarDecNode(ArrayList<Token> tokens) throws SyntaxError {
        type_Node tempType = type_Node.parseTypeNode(tokens);
        id_Node tempID = id_Node.parseIdNode(tokens);

        // check semi colon
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ';'"); }
        tokens.remove(0);
        
        return new varDec_Node(tempType, tempID);
    }

    /**
     * Converts the variable declaration node to a string literal in Jott
     * 
     * @return  the string literal in Jott
     */
    @Override
    public String convertToJott() {
        return type.convertToJott() + " " + id.convertToJott() + ";";
    }

    @Override
    public boolean validateTree() {
        boolean isValid = this.type.validateTree() && this.id.validateTree();

        // TODO: may not be needed, checks if variable id already used by function
        if(Jott_Node.declared_functions.contains(this.id.toString())) {
            new SemanticError("Variable id: {" + this.id.toString() + "} already a defined function")
                .print(Jott_Node.filename, super.linenum);
            isValid = false;
        }

        // checking if variable already declared in function scope
        if(Jott_Node.function_scope.get(current_function_ID.peek()).contains(this.id.toString())) {
            new SemanticError("Function already contains variable id: " + this.id.toString())
                .print(Jott_Node.filename, super.linenum);
            isValid = false;
        } else {
            // Adding variable to current function scope
            Jott_Node.function_scope.get(current_function_ID.peek())
                .add(this.id.toString(), this);
        }

        return isValid;
    }

    @Override
    public ReturnType getType() {
        return this.type.getType();
    }
}

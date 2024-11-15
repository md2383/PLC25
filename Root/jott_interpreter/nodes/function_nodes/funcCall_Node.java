package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.grammar_nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * A function call containing an {@code ID} [{@link id_Node}] and the list of 
 * parameters [{@link params_Node}] being called for the function.
 */
public class funcCall_Node extends Jott_Node{

    /** The name of the function being called */
    private final id_Node id;
    /** The parameter object storing the list of parameters being called for the function */
    private final params_Node params;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseFunctionCallNode})
     * @param ID            an ID node referencing the function
     * @param Parameters    a parameter node referencing the (list of) 
     *                      parameters for the function
     */
    private funcCall_Node(id_Node ID, params_Node Parameters, int line_number) {
        super(line_number);
        this.id = ID;
        this.params = Parameters;
    }

    /**
     * Static parse method returning an {@link functionCall_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A function call node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not FC_HEADER}: token being 
     *                      parsed is not a {@code TokenType.FC_HEADER}
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static funcCall_Node parseFunctionCallNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.FC_HEADER) { throw new SyntaxError("Token type not FC_HEADER"); }
        int lineNum = tokens.remove(0).getLineNum();

        id_Node tempID = id_Node.parseIdNode(tokens);

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) { throw new SyntaxError("Invalid Token: Expected ["); }
        tokens.remove(0);

        params_Node tempParameters = params_Node.parseParamsNode(tokens);
        
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) { throw new SyntaxError("Invalid Token: Expected ]"); }
        tokens.remove(0);

        return new funcCall_Node(tempID, tempParameters, lineNum);
    }

    @Override
    public String convertToJott() {
        return "::" + this.id.convertToJott() + "[" + this.params.convertToJott() + "]";
    }

    @Override
    public boolean validateTree() {
        boolean isValid = this.id.validateTree();

        // Checking if function has been defined/declared
        if(Jott_Node.declared_functions.contains(this.id.toString())) {
            Jott_Node.current_function_ID.push(this.id.toString());
            isValid = params.validateTree();
        // Else: function hasn't been defined/declared
        } else {
            new SemanticError("Function id: {" + this.id.toString() + "} is not defined/declared")
                .print(Jott_Node.filename, super.linenum);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public ReturnType getType() {
        return this.id.getType();
    }
}

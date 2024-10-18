package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * A function definition containing an {@code ID} [{@link id_Node}], the return type 
 * [{@link type_Node}], and the list of parameters [{@link funcDefParams_t_Node}] 
 * for the function.
 */
public class funcDefParams_Node extends Jott_Node {
    
    private final id_Node id;
    // Colon (Doesn't need to be stored just checked if it's there)
    private final type_Node type;
    private final ArrayList<funcDefParams_t_Node> func_def_params_t;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseFuncDefParamsNode})
     * @param id                an ID node referencing the function
     * @param type              a type node referencing the return type
     * @param func_def_params_t a list of funcDefParams_t nodes referencing the parameters for the function
     */
    private funcDefParams_Node(id_Node id, type_Node type, ArrayList<funcDefParams_t_Node> func_def_params_t) {
        this.id = id;
        this.type = type;
        this.func_def_params_t = func_def_params_t;
    }

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseFuncDefParamsNode})
     * This constructor creates a null list of parameters.
     */
    private funcDefParams_Node() {
        this.id = null;
        this.type = null;
        this.func_def_params_t = null;
    }
    
    /**
     * Static parse method returning an {@link funcDefParams_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A function definition node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Token}: token being 
     *                      parsed is not a valid token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static funcDefParams_Node parseFuncDefParamsNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            // Get the ID
            id_Node id = id_Node.parseIdNode(tokens);
            
            if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
            if (tokens.get(0).getTokenType() != TokenType.COLON) {throw new SyntaxError("Invalid Token: expected ':'");}
            else { tokens.remove(0); }
            
            // Get the type
            type_Node type = type_Node.parseTypeNode(tokens);

            // Get the rest of the parameters
            ArrayList<funcDefParams_t_Node> func_def_params_t = new ArrayList<funcDefParams_t_Node>();

            if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }

            // Loop through the rest of the parameters
            while(tokens.get(0).getTokenType() == TokenType.COMMA) {
                func_def_params_t.add(funcDefParams_t_Node.parseFuncDefParamsTNode(tokens));
                if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
            }

            // Return the new node
            return new funcDefParams_Node(id, type, func_def_params_t);
        }

        return new funcDefParams_Node(); // no params in func def
    }

    /**
     * Converts the node into a string
     * @return  the string representation of the node
     */
    @Override
    public String convertToJott() {
        if(this.id == null) { return ""; }
        else {
            String jott = "";
            jott += id.convertToJott();
            jott += ": ";
            jott += type.convertToJott();
            for (funcDefParams_t_Node f : func_def_params_t) {
                jott += f.convertToJott();
            }
            return jott;
        }
    }
}

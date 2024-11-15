package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.id_Node;
import jott_interpreter.nodes.token_nodes.type_Node;
import provided.*;

/**
 * A function definition parameter containing an {@code ID} [{@link id_Node}] and the type of the parameter [{@link type_Node}].
 */
public class funcDefParams_t_Node extends Jott_Node {
    
    // Comma (Doesn't need to be stored just checked if it's there)
    private final id_Node id;
    // Colon (Doesn't need to be stored just checked if it's there)
    private final type_Node type;
    
    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseFuncDefParamsTNode})
     * @param id    an ID node referencing the parameter
     * @param type  a type node referencing the type of the parameter
     */
    private funcDefParams_t_Node(id_Node id, type_Node type, int line_number) {
        super(line_number);
        this.id = id;
        this.type = type;
    }
    
    /**
     * Static parse method returning an {@link funcDefParams_t_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A function definition parameter node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Function Definition}: token being 
     *                      parsed is not a valid function definition
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static funcDefParams_t_Node parseFuncDefParamsTNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
        assert(tokens.get(0).getTokenType() == TokenType.COMMA);
        int lineNum = tokens.remove(0).getLineNum(); 
        
        id_Node id = id_Node.parseIdNode(tokens);

        // Check for colon
        if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.COLON) { throw new SyntaxError("Invalid Token: expected ':'"); }
        tokens.remove(0);
        
        type_Node type = type_Node.parseTypeNode(tokens);

        return new funcDefParams_t_Node(id, type, lineNum);
    }

    /**
     * Converts the node into a string
     * @return  A string representation of the node
     */
    @Override
    public String convertToJott() {
        return ", " + id.convertToJott() + ":" + type.convertToJott();
    }
    
    @Override
    public boolean validateTree() {
        boolean isValid = this.type.validateTree() && this.id.validateTree();

        // checking if variable already declared in function scope
        if(Jott_Node.function_scope.get(current_function_ID.peek()).contains(this.id.toString())) {
            new SemanticError("Function already contains variable id: " + this.id.toString())
                .print(Jott_Node.filename, super.linenum);
            isValid = false;
        } else {
            // Adding variable to current function scope
            Jott_Node.function_scope.get(current_function_ID.peek())
                .addDynamicVar(this.id.toString(), this);
        }

        return isValid;
    }

    @Override
    public ReturnType getType() {
        return this.type.getType();
    }
}

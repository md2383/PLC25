package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

/**
 * An Id token representation for the parse tree
 */
public class id_Node extends Jott_Node {

    /** Valid {@code ID_KEYWORD} token reference */
    private final Token id;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseIdNode})
     * @param idToken a validated {@code ID_KEYWORD} token reference
     */
    private id_Node(Token idToken) {
        super(idToken.getLineNum());
        this.id = idToken;
    }

    /**
     * Static parse method returning an {@link id_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  An ID node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not ID_KEYWORD}: token being 
     *                      parsed is not a {@code TokenType.ID_KEYWORD}
     * @throws SyntaxError  {@code Token is a Keyword, expected an ID}: token
     *                      being parsed is a {@code Keyword}, not an id
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static id_Node parseIdNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) { throw new SyntaxError("Token type not ID_KEYWORD"); }
        if(Character.isUpperCase(tokens.get(0).getToken().charAt(0))) { throw new SyntaxError("Token is a Keyword, expected an ID"); }

        return new id_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        return this.id.getToken();
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public ReturnType getType() {
        // if id is a function, return function ReturnType
        if(Jott_Node.declared_functions.contains(this.toString())) {
            return Jott_Node.declared_functions.getReturnType(this.toString());
        // if id is a variable reference, return variable type
        } else {
            assert (current_function_ID != null); // if cf_id == null, following line will error
            return Jott_Node.function_scope.get(current_function_ID.peek())
                .getReturnType(this.toString());
        }
    }

    @Override
    public void execute() { /* Do Nothing */ }

    @Override
    public Object getValue() {
        return Jott_Node.function_scope.get(current_function_ID.peek())
            .getNode(this.id.toString()).getValue();
    }
}
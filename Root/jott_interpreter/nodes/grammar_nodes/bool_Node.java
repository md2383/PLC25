package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

/**
 * A bool representation for the parse tree
 */
public class bool_Node extends Jott_Node {

    /** Valid {@code ID_KEYWORD} token reference */
    private final Token bool;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseBoolNode})
     * @param boolToken a validated {@code ID_KEYWORD} token reference
     */
    private bool_Node(Token boolToken) {
        this.bool = boolToken;
    }

    /**
     * Static parse method returning a {@link bool_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A bool node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not ID_KEYWORD}: token being 
     *                      parsed is not a {@code TokenType.ID_KEYWORD}
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static bool_Node parseBoolNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("Token type not Valid bool");
        }
        if (!tokens.get(0).getToken().equals("True") && !tokens.get(0).getToken().equals("False")) {
            throw new SyntaxError("Invalid Token: Expected 'True' or 'False'");
        }
        if (!Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            throw new SyntaxError("Token is an ID, expected 'True' or 'False'");
        }
        
        return new bool_Node(tokens.remove(0));
    }

    /**
     * Converts the bool node to Jott code
     * 
     * @return  the Jott code representation of the bool node
     */
    @Override
    public String convertToJott() {
        return this.bool.getToken();
    }
}

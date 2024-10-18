package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

/**
 * A type keyword token representation for the parse tree
 */
public class type_Node extends Jott_Node {
    /** Valid {@code ID_KEYWORD} token reference */
    private final Token type;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseTypeNode})
     * @param type a validated {@code ID_KEYWORD} token reference
     */
    private type_Node(Token type) {
        this.type = type;
    }

    /**
     * Static parse method returning a {@link type_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A type node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not ID_KEYWORD}: token being parsed 
     *                      is not a {@code TokenType.ID_KEYWORD}
     * @throws SyntaxError  {@code Invalid variable type}: token being parsed
     *                      is not one of the following: Double, Integer, String, Boolean
     * @implNote    The first element in the input array list of {@code Token} 
     *              objects will be removed given validation success
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static type_Node parseTypeNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("Token Type not ID_KEYWORD");
        }
        if (!Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            throw new SyntaxError("Token is an ID, expected KEYWORD");
        }
        if (
            !(
                tokens.get(0).getToken().equals("Double") ||
                tokens.get(0).getToken().equals("Integer") ||
                tokens.get(0).getToken().equals("String") ||
                tokens.get(0).getToken().equals("Boolean")
            )
        ) {
            throw new SyntaxError("Invalid variable type");
        }
        return new type_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        return this.type.getToken();
    }
}

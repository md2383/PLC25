package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.*;

/**
 * A mathop representation for the parse tree
 */
public class mathop_Node extends Jott_Node {

    /** Valid {@code MATH_OP} token reference */
    private final Token mathop;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseMathopNode})
     * @param mathop a validated {@code MATH_OP} token reference
     */
    private mathop_Node(Token mathopToken) {
        super(mathopToken.getLineNum());
        this.mathop = mathopToken;
    }

    /**
     * Static parse method returning a {@link mathop_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A mathop node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not MATH_OP}: token being 
     *                      parsed is not a {@code TokenType.MATH_OP}
     * @throws SyntaxError  {@code Invalid Mathop}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static mathop_Node parseMathopNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.MATH_OP) { throw new SyntaxError("Token type not MATH_OP"); }
        assert("+-*/".contains(tokens.get(0).getToken())); // not a syntax error (would be a problem in our code)
        return new mathop_Node(tokens.remove(0));
    }

    /**
     * Converts the mathop node to a Jott string
     * 
     * @return  the Jott string representation of the mathop node
     */
    @Override
    public String convertToJott() {
        return this.mathop.getToken();
    }
    
    @Override
    public boolean validateTree() {
        return true;
    }
}

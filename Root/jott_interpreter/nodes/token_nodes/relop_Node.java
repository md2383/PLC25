package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

/**
 * A relop representation for the parse tree
 */
public class relop_Node extends Jott_Node {
    
    /** Valid {@code REL_OP} token reference */
    private final Token relop;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseRelopNode})
     * @param relop a validated {@code REL_OP} token reference
     */
    public relop_Node(Token relop) {
        super(relop.getLineNum());
        this.relop = relop;
    }

    /**
     * Static parse method returning a {@link relop_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A relop node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not RELOP}: token being 
     *                      parsed is not a {@code TokenType.RELOP}
     * @throws SyntaxError  {@code Invalid Relop}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static relop_Node parseRelopNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.REL_OP) { throw new SyntaxError("Token type not RELOP"); }
        assert("<=>=!==".contains(tokens.get(0).getToken())); // not a syntax error (would be a problem with our code)
        return new relop_Node(tokens.remove(0));
    }

    /**
     * Converts the relop node to a Jott string
     * 
     * @return  the Jott string representation of the relop node
     */
    @Override
    public String convertToJott() {
        return this.relop.getToken();
    }

    @Override
    public boolean validateTree() {
        return true;
    }
}
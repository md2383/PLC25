package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;
import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

/**
 * A number token representation for the parse tree
 */
public class number_Node extends Jott_Node {

    /** valid {@code NUMBER} token reference */
    private Token num;  

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseNumberNode})
     * @param numberToken - a validated {@code NUMBER} token reference
     */
    private number_Node(Token numberToken) {
        super(numberToken.getLineNum());
        this.num = numberToken;
    }

    /**
     * Static parse method returning a {@link number_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A number node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not NUMBER}: token being parsed 
     *                      is not a {@code TokenType.NUMBER}
     * @implNote    The first element in the input array list of {@code Token} 
     *              objects will be removed given validation success
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static number_Node parseNumberNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.NUMBER) { throw new SyntaxError("Token type not NUMBER"); }

        return new number_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        return this.num.getToken();
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public ReturnType getType() {
        // Check if number is integer or double
        if (this.num.getToken().contains(".")) {
            return ReturnType.Double;
        } else {
            return ReturnType.Integer;
        }
    }

    @Override
    public void execute() { /* Do Nothing */ }

    @Override
    public Object getValue() {
        return Double.parseDouble(this.num.getToken());
    }
}

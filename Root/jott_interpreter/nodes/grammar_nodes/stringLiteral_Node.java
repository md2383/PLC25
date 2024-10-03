package jott_interpreter.nodes.grammar_nodes;
import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

/**
 * A string literal representation for the parse tree
 */
public class stringLiteral_Node extends Jott_Node {

    /** Valid {@code STRING} token reference */
    private Token stringLiteral;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseStringLiteralNode})
     * @param stringLiteral a validated {@code STRING} token reference
     */
    public stringLiteral_Node(Token stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    /**
     * Static parse method returning a {@link stringLiteral_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A string literal node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Token type not STRING}: token being 
     *                      parsed is not a {@code TokenType.STRING}
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static stringLiteral_Node parseStringLiteralNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.STRING) {
            throw new SyntaxError("Token type not Valid String");
        }
        if (!tokens.get(0).getToken().startsWith("\"") || !tokens.get(0).getToken().endsWith("\"")) {
            throw new SyntaxError("Invalid Token: Expected a string in \" \" ");
        }
        return new stringLiteral_Node(tokens.remove(0));
    }

    /**
     * Converts the string literal node to a string literal in Jott
     * 
     * @return  the string literal in Jott
     */
    @Override
    public String convertToJott() {
        return stringLiteral.getToken();
    }
}
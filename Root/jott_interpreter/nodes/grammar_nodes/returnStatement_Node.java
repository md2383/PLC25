package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.id_Node;
import provided.*;

// retrunStatement_Node is a Jott_Node

 // example: Return ''foo''; 3 tokens in total
public class returnStatement_Node extends Jott_Node{
    private final id_Node id;
    private final Token returnToken;
    private final Token semicolonToken;
    
    /**
     * Private Constructor for returnStatement_Node
     * @param ID - a validated {@code id_Node} token reference
     * @param returnToken - a validated {@code RETURN} token reference
     * @param semicolonToken - a validated {@code SEMICOLON} token reference
     */
    private returnStatement_Node(id_Node ID, Token returnToken, Token semicolonToken) {
        this.id = ID;
        this.returnToken = returnToken;
        this.semicolonToken = semicolonToken;
    }
    /**
     * Static parse method returning a {@link returnStatement_Node} for the parse tree.
     * @param tokens - the list of tokens being parsed into a parse tree
     * @return
     * @throws SyntaxError {@code Unexpected EOF}: no token to parse
     */
    public static expr_Node parseReturnStatementNode(ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) { throw new SyntaxError("Token type not Return"); }
        Token returnToken = tokens.remove(0);

        id_Node tempID = id_Node.parseIdNode(tokens);

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ;"); }
        Token semicolonToken = tokens.remove(0);

        return new expr_Node(); // When expr_Node is implemented, return it here and FIX it
    }

    /**
     * Converts the returnStatement_Node to a Jott string
     * @return the Jott string representation of the returnStatement_Node
     */
    @Override
    public String convertToJott() {
        return "Return " + id.convertToJott() + ";";
    }
    
}
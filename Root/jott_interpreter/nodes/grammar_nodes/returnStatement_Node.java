package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.id_Node;
import provided.*;

public class returnStatement_Node extends Jott_Node{
    private final id_Node id;
    private final Token returnToken;

    // example: Return ''foo''; 3 tokens in total
    private returnStatement_Node(id_Node ID, Token returnToken) {
        this.id = ID;
        this.returnToken = returnToken;
    }

    public static expr_Node parseReturnStatementNode(ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) { throw new SyntaxError("Token type not Return"); }
        Token returnToken = tokens.remove(0);

        id_Node tempID = id_Node.parseIdNode(tokens);

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ;"); }
        tokens.remove(0);

        return new expr_Node(); // When expr_Node is implemented, return it here and FIX it
    }

    @Override
    public String convertToJott() {
        return "Return " + id.convertToJott() + ";";
    }
    
}
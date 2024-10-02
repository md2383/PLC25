package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class id_Node extends Jott_Node {

    private final Token id;

    private id_Node(Token idToken) {
        this.id = idToken;
    }

    public static id_Node parseIdNode(ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) { throw new SyntaxError("Token type not ID_KEYWORD"); }
        if(Character.isUpperCase(tokens.get(0).getToken().charAt(0))) { throw new SyntaxError("Token is a Keyword, expected an ID"); }

        return new id_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        return this.id.getToken();
    }
    
}
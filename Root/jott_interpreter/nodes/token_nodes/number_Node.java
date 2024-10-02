package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class number_Node extends Jott_Node {

    private Token num;

    private  number_Node(Token numberToken) {
        this.num = numberToken;
    }

    public static number_Node parseNumberNode(ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.NUMBER) { throw new SyntaxError("Token type not NUMBER"); }

        return new number_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        return this.num.getToken();
    }
}

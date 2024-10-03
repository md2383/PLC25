package jott_interpreter.nodes.token_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

public class type_Node extends Jott_Node {
    private Token type;

    public type_Node(Token type) {
        this.type = type;
    }

    public static type_Node parseTypeNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("Invalid token type");
        }
        if (
            !(
                tokens.get(0).getToken().equals("Double") ||
                tokens.get(0).getToken().equals("Integer") ||
                tokens.get(0).getToken().equals("String") ||
                tokens.get(0).getToken().equals("Boolean")
            )
        ) {
            throw new SyntaxError("Invalid type keyword");
        }
        return new type_Node(tokens.remove(0));
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }
}

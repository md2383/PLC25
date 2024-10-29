package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.type_Node;
import provided.*;

public class funcReturn_Node extends Jott_Node{

    private final type_Node type;

    private funcReturn_Node() {
        this.type = null;
    }

    private funcReturn_Node(type_Node type) {
        this.type = type;
    }

    public static funcReturn_Node parseFunctionReturnNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("Unexpected token type, expected ID_KEYWORD");
        }
        if (!Character.isUpperCase(tokens.get(0).getToken().charAt(0))) {
            throw new SyntaxError("Token is ID, expected Keyword");
        }
        if (tokens.get(0).getToken().equals("Void")) {
            tokens.remove(0);
            return new funcReturn_Node();
        } else {
            return new funcReturn_Node(type_Node.parseTypeNode(tokens));
        }
    }

    @Override
    public String convertToJott() {
        if (this.type == null) {
            return "Void";
        } else {
            return this.type.convertToJott();
        }
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public ReturnType getType() {
        return this.type == null ? 
            ReturnType.Void : this.type.getType();
    }
}

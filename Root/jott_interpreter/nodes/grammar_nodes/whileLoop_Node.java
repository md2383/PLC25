package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class whileLoop_Node extends Jott_Node{
    private expr_Node expr;
    private body_Node body;

    public whileLoop_Node(expr_Node expr, body_Node body) {
        this.expr = expr;
        this.body = body;
    }

    public static whileLoop_Node parseWhileLoopNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 7) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new SyntaxError("Unexpected token type, expected ID_KEYWORD");
        }
        if (!tokens.get(0).getToken().equals("While")) {
            throw new SyntaxError("Invalid token, expected \"While\"");
        }
        tokens.remove(0);
        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            throw new SyntaxError("Invalid token, expected \"[\"");
        }
        tokens.remove(0);
        expr_Node tempExpr = expr_Node.parseExprNode(tokens);
        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            throw new SyntaxError("Invalid token, expected \"]\"");
        }
        tokens.remove(0);
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            throw new SyntaxError("Invalid token, expected \"{\"");
        }
        tokens.remove(0);
        body_Node tempBody = body_Node.parseBodyNode(tokens);
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            throw new SyntaxError("Invalid token, expected \"}\"");
        }
        tokens.remove(0);
        return new whileLoop_Node(tempExpr, tempBody);
    }

    @Override
    public String convertToJott() {
        return "While[" + this.expr.convertToJott() + "]{" + this.body.convertToJott() + "}";
    }
}

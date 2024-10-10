package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class params_t_Node extends Jott_Node {
    private expr_Node expr;

    public params_t_Node(expr_Node expr) {
        this.expr = expr;
    }

    public static params_t_Node parseParamsTNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        if (tokens.get(0).getTokenType() != TokenType.COMMA) {
            throw new SyntaxError("Invalid token type, expected \",\"");
        }
        tokens.remove(0);
        return new params_t_Node(expr_Node.parseExprNode(tokens));
    }

    @Override
    public String convertToJott() {
        return "," + this.expr.convertToJott();
    }
    
}

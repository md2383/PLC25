package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class params_t_Node extends Jott_Node {
    private final expr_Node expr;

    private params_t_Node(expr_Node expr) {
        this.expr = expr;
    }

    public static params_t_Node parseParamsTNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.COMMA) { throw new SyntaxError("Invalid token type, expected \",\""); }
        tokens.remove(0);
        return new params_t_Node(expr_Node.parseExprNode(tokens));
    }

    @Override
    public String convertToJott() {
        return "," + this.expr.convertToJott();
    }
    
    @Override
    public boolean validateTree() {
        return expr.validateTree();
    }

    @Override
    public void execute() throws SemanticError {
        // Logic moved to params_Node.execute()
        this.expr.execute();
    }
    
    @Override
    public ReturnType getType() {
        return this.expr.getType();
    }

    @Override
    public Object getValue() {
        //! DOES NOT RETURN TYPE
        //  Used by param node for function parameter logic
        return this.expr;
    }
}

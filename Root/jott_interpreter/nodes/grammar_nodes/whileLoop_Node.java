package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class whileLoop_Node extends Jott_Node{
    private final expr_Node expr;
    private final body_Node body;

    private whileLoop_Node(expr_Node expr, body_Node body, int line_number) {
        super(line_number);
        this.expr = expr;
        this.body = body;
    }

    public static whileLoop_Node parseWhileLoopNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) { throw new SyntaxError("Unexpected token type, expected ID_KEYWORD"); }
        if (!tokens.get(0).getToken().equals("While")) { throw new SyntaxError("Invalid token, expected \"While\""); }
        int lineNum = tokens.remove(0).getLineNum();
        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) { throw new SyntaxError("Invalid token, expected \"[\""); }
        tokens.remove(0);

        expr_Node tempExpr = expr_Node.parseExprNode(tokens);
        
        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) { throw new SyntaxError("Invalid token, expected \"]\""); }
        tokens.remove(0);
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) { throw new SyntaxError("Invalid token, expected \"{\""); }
        tokens.remove(0);

        body_Node tempBody = body_Node.parseBodyNode(tokens);

        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) { throw new SyntaxError("Invalid token, expected \"}\""); }
        tokens.remove(0);

        return new whileLoop_Node(tempExpr, tempBody, lineNum);
    }

    @Override
    public String convertToJott() {
        return "While[" + this.expr.convertToJott() + "]{" + this.body.convertToJott() + "}";
    }

    @Override
    public boolean validateTree() {
        boolean valid = this.expr.validateTree();

        if(!valid) { return false; } // forced early function exit
        
        if(this.expr.getType() != ReturnType.Boolean) {
            new SemanticError("Expression in while statement not of type: boolean", super.linenum)
                .print(Jott_Node.filename);
            valid = false;
        }

        return valid && this.body.validateTree();
    }

    @Override
    public void execute() throws SemanticError{
        this.expr.execute();
        Boolean bool = (Boolean)(this.expr.getValue());
        while(bool) {
            this.body.execute();
            this.expr.execute();
            bool = (Boolean)(this.expr.getValue());
        }
    }
}

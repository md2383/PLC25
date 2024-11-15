package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

/**
 * A function that represents an elseif node in the parse tree.
 * parse tree grammar: Elseif < expr > < body >
 */
public class elseif_Node extends Jott_Node{
    private final expr_Node expression;
    private final body_Node body;

    /**
     * Private Constructor
     * (validation of the node done in {@link #parseElseifNode})
     * @param expression    an expression node
     * @param body         a body node
     */
    private elseif_Node(expr_Node expression, body_Node body, int line_number) {
        super(line_number);
        this.expression = expression;
        this.body = body;
    }

    /**
     * Static parse method returning an {@link elseif_Node} for the parse tree.
     * @param tokens    the list of tokens being parsed into a parse tree
     * @return        An elseif node which has been validated in accordance
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not
     *                     the expected token
     * @implNote    The token(s) in the input array list of {@code Token}
     *             objects will be removed from the list given validation
     *            success.
     * @see {@link Token}
     * @see {@link TokenType}
     */
    public static elseif_Node parseElseifNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        assert (tokens.get(0).getToken().equals("Elseif")); // not syntax error
        int lineNum = tokens.remove(0).getLineNum();
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) { throw new SyntaxError("Invalid token: expected '['"); }
        tokens.remove(0);

        expr_Node expression = expr_Node.parseExprNode(tokens);
        
        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) { throw new SyntaxError("Invalid token: expected ']'"); }
        tokens.remove(0);
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) { throw new SyntaxError("Invalid token: expected '{'"); }
        tokens.remove(0);

        body_Node body = body_Node.parseBodyNode(tokens);

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) { throw new SyntaxError("Invalid token: expected '}'"); }
        tokens.remove(0);

        return new elseif_Node(expression, body, lineNum);
    }

    /**
     * Converts the elseif node to a string
     * @return The string representation of the elseif node
     * @implNote    The string representation of the elseif node is the
     *            concatenation of the expression and the body
     *           of the elseif node.
     */
    @Override
    public String convertToJott() {
        return "Elseif [" + this.expression.convertToJott() + "] {" + this.body.convertToJott() + "}";
    }

    @Override
    public boolean validateTree() {
        boolean valid = this.expression.validateTree();

        if(!valid) { return false; } // forced early function exit
        
        if(this.expression.getType() != ReturnType.Boolean) {
            new SemanticError("Expression in elseif statement not of type: boolean")
                .print(Jott_Node.filename, super.linenum);
            valid = false;
        }

        return valid && this.body.validateTree();
    }

    @Override
    public ReturnType getType() {
        return this.body.getType();
    }
}

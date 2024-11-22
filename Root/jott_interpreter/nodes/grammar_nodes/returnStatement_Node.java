package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

/**
 * <h>
 *  A return statement. 
 *  Functions can either return an expression, or void (no return).
 * 
 * <p>
 *  Return < expr >; | ε
 */
public class returnStatement_Node extends Jott_Node{
    
    /** The expression being returned */
    private Jott_Node returnExpr;
    
    /**
     * Private Constructor
     * (validation of the node done in {@link #parseReturnStatementNode})
     * @param returnExpression
     */
    private returnStatement_Node(expr_Node returnExpression) {
        this.returnExpr = returnExpression;
    }

    /**
     * Static parse method returning a {@link returnStatement_Node} for the parse tree.
     * @param tokens - the list of tokens being parsed into a parse tree
     * @return  A return statement node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static returnStatement_Node parseReturnStatementNode(final ArrayList<Token> tokens) throws SyntaxError {
        // void check, determines if there is an actual return statement
        if(tokens.size() > 0) { 
            if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD &&
                    tokens.get(0).getToken().equals("Return")) {
                tokens.remove(0);
                expr_Node expression = expr_Node.parseExprNode(tokens);
                
                if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
                if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ;"); }
                tokens.remove(0);

                return new returnStatement_Node(expression);
            }
        }
        
        // Void 
        // (actual function return type should be checked by the semantic analyzer)
        return new returnStatement_Node(null);
    }

    public boolean isVoid() {
        return this.returnExpr == null;
    }

    public void SetExpression(Jott_Node validReturnExpr) {
        assert (validReturnExpr.getType() != ReturnType.Void); // function should only be called with a non-void expression
        this.returnExpr = validReturnExpr;
    }

    /**
     * Converts the returnStatement_Node to a Jott string
     * @return the Jott string representation of the returnStatement_Node
     */
    @Override
    public String convertToJott() {
        // checks if the return is void
        return this.isVoid() ?
            "" : "Return " + this.returnExpr.convertToJott() + ";";
    }

    @Override
    public boolean validateTree() {
        return this.isVoid() ? true : this.returnExpr.validateTree();
    }

    @Override
    public void execute() {
        this.returnExpr.execute();
        
    }

    @Override
    public ReturnType getType() {
        return this.isVoid() ? 
            ReturnType.Void : 
            this.returnExpr.getType();
    }

    @Override
    public Object getValue() {
        return this.isVoid() ? null : this.returnExpr.getValue();
    }
}
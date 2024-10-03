package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * An assignment node containing an {@code ID} [{@link id_Node}], an assignment 
 * operator [{@link Token}], an expression [{@link expr_Node}], and a semicolon 
 * [{@link Token}] to end the assignment.
 */
public class assignment_Node extends Jott_Node {
    
    /** Parts of an assignment node */
    private id_Node id;             // ID
    private Token assign;           // =
    private expr_Node expr;         // Expression
    private Token semicolon;        // ;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseAssignmentNode})
     * @param id        an ID node referencing the variable being assigned
     * @param assign    an assignment operator token
     * @param expr      an expression node referencing the value being assigned
     * @param semicolon a semicolon token to end the assignment
     */
    private assignment_Node(id_Node id, Token assign, expr_Node expr, Token semicolon) {
        this.id = id;
        this.assign = assign;
        this.expr = expr;
        this.semicolon = semicolon;
    }

    /**
     * Static parse method returning an {@link assignment_Node} for the parse tree.
     * 
     * @param Tokens -  the list of tokens being parsed into a parse tree
     * @return  An assignment node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Token: Expected =}: token being 
     *                      parsed is not an assignment operator
     * @throws SyntaxError  {@code Invalid Token: Expected ;}: token being 
     *                      parsed is not a semicolon
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static assignment_Node parseAssignmentNode(ArrayList<Token> Tokens) throws SyntaxError {

        // Check for EOF (Anything less than 4 means theres not enough tokens for assignment node)
        if (Tokens.size() < 4) {
            throw new SyntaxError("Unexpected EOF");
        }
        
        // Check for ID
        id_Node tempID = id_Node.parseIdNode(Tokens);
        Tokens.remove(0);
        
        // Check for assignment operator. If it's not '=' then error
        Token tempAssign = Tokens.get(0);
        if (tempAssign.getTokenType() != TokenType.ASSIGN) { throw new SyntaxError("Invalid Token: Expected ="); }
        Tokens.remove(0);

        // Check for expression
        expr_Node tempExpr = expr_Node.parseExprNode(Tokens);
        Tokens.remove(0);

        // Check for semicolon. If it's not ';' then error
        Token tempSemicolon = Tokens.get(0);
        if(tempSemicolon.getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ;"); }
        Tokens.remove(0);

        // Return the assignment node
        return new assignment_Node(tempID, tempAssign, tempExpr, tempSemicolon);
    }

    /**
     * Converts the assignment node to a string
     * @return a string representation of the assignment node
     */
    @Override
    public String convertToJott() {
        String result = "";
        result += id.convertToJott();
        result += assign.getToken();
        result += expr.convertToJott();
        result += semicolon.getToken();
        return result;
    }
}

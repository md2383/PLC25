package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * An assignment node containing an {@code ID} [{@link id_Node}]
 * and an expression [{@link expr_Node}]
 */
public class assignment_Node extends Jott_Node {
    
    /** id being assigned to */
    private id_Node id;             // ID
    /** expr assigned to id */
    private expr_Node expr;         // Expression

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseAssignmentNode})
     * @param id        an ID node referencing the variable being assigned
     * @param assign    an assignment operator token
     * @param expr      an expression node referencing the value being assigned
     * @param semicolon a semicolon token to end the assignment
     */
    private assignment_Node(id_Node id, expr_Node expr, int line_number) {
        super(line_number);
        this.id = id;
        this.expr = expr;
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
    public static assignment_Node parseAssignmentNode(ArrayList<Token> tokens) throws SyntaxError {
        int lineNum = tokens.get(0).getLineNum();

        // Check for ID
        id_Node tempID = id_Node.parseIdNode(tokens);
        
        // Check for assignment operator. If it's not '=' then error
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.ASSIGN) { throw new SyntaxError("Invalid Token: Expected '='"); }
        tokens.remove(0);

        // Check for expression
        expr_Node tempExpr = expr_Node.parseExprNode(tokens);

        // Check for semicolon. If it's not ';' then error
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected ';'"); }
        tokens.remove(0);

        // Return the assignment node
        return new assignment_Node(tempID, tempExpr, lineNum);
    }

    /**
     * Converts the assignment node to a string
     * @return a string representation of the assignment node
     */
    @Override
    public String convertToJott() {
        StringBuilder result = new StringBuilder();
        result.append( this.id.convertToJott() )
              .append( "=" )
              .append( this.expr.convertToJott() )
              .append( ";" );
        return result.toString();
    }

    @Override
    public boolean validateTree() {
        boolean isValid = id.validateTree() && expr.validateTree();

        // Id must be defined in scope
        if(!Jott_Node.function_scope.get(current_function_ID.peek()).contains(this.id.toString())) {
            new SemanticError("Variable id: {" + this.id.toString() + "} not declared.")
                .print(Jott_Node.filename, super.linenum);
            isValid = false;
        } else {
            // Expression type must match id type
            if(id.getType() != expr.getType()) {
                new SemanticError("Expression type does not match id type for id: " + this.id.toString())
                    .print(Jott_Node.filename, super.linenum);
                isValid = false;
            }
        }

        return isValid;
    }
}

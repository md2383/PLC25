package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.function_nodes.*;
import provided.*;

/**
 * <h>
 *  A statement made inside a body validated according to the parse tree.
 * 
 * <p>
 *  < if_stmt >     |
 *  < while_loop >  |
 *  < assignment >  |
 *  < func_call >
 * 
 * <p>
 *  The logic for determining node type is built into the 
 *  {@link #parseBodyStmtNode} function.
 */
public class bodyStmt_Node extends Jott_Node {

    /** Abstracted {@code Jott_Node} representation of the body statement. */
    private final Jott_Node statement;
    private final boolean hasSemicolon;
    private final boolean isIfStmt;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseBodyStmtNode})
     * @param body_statement - an abstract {@link Jott_Node} decided by the parse grammar
     */
    private bodyStmt_Node(
        Jott_Node body_statement, 
        boolean contains_semicolon, 
        boolean if_statement
    ) {
        this.statement = body_statement;
        this.hasSemicolon = contains_semicolon;
        this.isIfStmt = if_statement;
    }

    /**
     * Static parse method returning an {@link bodyStmt_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A statement built and validated according to the 
     *          parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Token}: token being parsed is not 
     *                      the expected token
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static bodyStmt_Node parseBodyStmtNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        // Initial token validation and evaluation

        Jott_Node tempStmt;
        boolean containsSemicolon = false;
        boolean ifStmt = false;

        // < func_call >
        if(tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            tempStmt = funcCall_Node.parseFunctionCallNode(tokens);

            // Semicolon check - just after a func_call
            if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            if(tokens.get(0).getTokenType() != TokenType.SEMICOLON) { throw new SyntaxError("Invalid Token: Expected \";\""); }
            tokens.remove(0);
            containsSemicolon = true;
        } else {
            // < if_stmt >
            if(tokens.get(0).getToken().equals("If")) {
                ifStmt = true;
                tempStmt = ifStmt_Node.parseIfStmtNode(tokens);
            // < while_loop >
            } else if(tokens.get(0).getToken().equals("While")) {
                tempStmt = whileLoop_Node.parseWhileLoopNode(tokens);
            // < assignment >
            } else {
                tempStmt = assignment_Node.parseAssignmentNode(tokens);
            }
        }

        return new bodyStmt_Node(tempStmt, containsSemicolon, ifStmt);
    }

    @Override
    public String convertToJott() {
        return statement.convertToJott() + (this.hasSemicolon ? ";" : "");
    }

    @Override
    public boolean validateTree() {
        return statement.validateTree();
    }

    @Override
    public void execute() throws SemanticError {
        this.statement.execute();
    }
    
    @Override
    public ReturnType getType() {
        if(this.isIfStmt) { return statement.getType(); }
        else { return ReturnType.Void; }
    }

    @Override
    public Object getValue() {
        assert (this.isIfStmt);
        return statement.getType();
    }

    public ReturnType getPossibleReturn() {
        return isIfStmt ? ((ifStmt_Node)(statement)).getPossibleReturn() : ReturnType.Void;
    }
}

package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

/**
 * The body of some function expressed as a node, which has been validated 
 * according to the parse tree's grammar.
 */
public class body_Node extends Jott_Node{

    /** an array of {@link bodyStmt_Node} nodes */
    private final bodyStmt_Node[] stmts;
    /** the return statement for the body */
    private final returnStatement_Node returnStmt;

    private Object value;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseBodyNode})
     * @param statements        - an array of body statement nodes
     * @param returnStatement   - the return statement for the body
     */
    private body_Node(
            bodyStmt_Node[] statements, 
            returnStatement_Node returnStatement, 
            int line_number
        ) {
        super(line_number);
        this.stmts = statements;
        this.returnStmt = returnStatement;
    }
    
    /**
     * Static parse method returning an {@link assignment_Node} for the parse tree.
     * 
     * @param Tokens -  the list of tokens being parsed into a parse tree
     * @return  A body node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static body_Node parseBodyNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        int lineNum = tokens.get(0).getLineNum();
        ArrayList<bodyStmt_Node> stmt = new ArrayList<>();
        returnStatement_Node tempReturn = null;
        
        // checks for either a '}' or a return and otherwise parses a body statement
        while(tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            if((tempReturn = returnStatement_Node.parseReturnStatementNode(tokens)).isVoid()) {
                stmt.add(bodyStmt_Node.parseBodyStmtNode(tokens));
                if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            } else { break; }
        }

        // empty body check -> void return
        if(tempReturn == null) {
            tempReturn = returnStatement_Node.parseReturnStatementNode(tokens);
            assert tempReturn.isVoid();
        }

        return new body_Node(stmt.toArray(new bodyStmt_Node[stmt.size()]), tempReturn, lineNum);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        for(bodyStmt_Node stmt : stmts) { str.append(stmt.convertToJott()); }
        return str.append(returnStmt.convertToJott()).toString();
    }
    
    @Override
    public boolean validateTree() {
        boolean valid = true;
        boolean early_exit = false;

        for (bodyStmt_Node bodyStmt : this.stmts) {
            if(early_exit) {
                new SemanticError("Body with unreachable code", super.linenum)
                    .print(Jott_Node.filename);
                return false;
            }
            valid &= bodyStmt.validateTree();
            if(bodyStmt.getType() != ReturnType.Void) {
                if(returnStmt.isVoid()) {
                    returnStmt.SetExpression(bodyStmt);
                    early_exit = true;
                } else {
                    new SemanticError("Body with unreachable return statement", super.linenum)
                        .print(Jott_Node.filename);
                    valid = false;
                }
            }
        }

        // Validating return stmt if return stmt has not been validated already.
        // Otherwise, removing duplicate (return) expressions from tree.
        if(!early_exit) { valid &= returnStmt.validateTree(); }
        else { this.stmts[this.stmts.length-1] = null; } 

        return valid;
    }

    @Override
    public void execute() throws SemanticError {
        for(bodyStmt_Node stmt : this.stmts) {
            if(stmt != null) { stmt.execute(); } // null check consideration from validation
        }
        this.returnStmt.execute();
        this.value = this.returnStmt.getValue();
    }

    @Override
    public ReturnType getType() {
        return this.returnStmt.getType();
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    public ReturnType[] getPossibleReturn() {
        ArrayList<ReturnType> types = new ArrayList<>();
        for(int i = 0; i < this.stmts.length; i++) {
            if(this.stmts[i] != null) { types.add(this.stmts[i].getPossibleReturn()); }
        }
        return types.toArray(new ReturnType[types.size()]);
    }
}

package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

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

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseBodyNode})
     * @param statements        - an array of body statement nodes
     * @param returnStatement   - the return statement for the body
     */
    private body_Node(bodyStmt_Node[] statements, returnStatement_Node returnStatement) {
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

        ArrayList<bodyStmt_Node> stmt = new ArrayList<>();
        returnStatement_Node tempReturn = null;
        
        // checks for either a '}' or a return and otherwise parses a body statement
        while(tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            if((tempReturn = returnStatement_Node.parseReturnStatementNode(tokens)).isVoid()) {
                stmt.add(bodyStmt_Node.parseBodyStmtNode(tokens));
                if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            } else { break; }
        }

        return new body_Node((bodyStmt_Node[])(stmt.toArray()), tempReturn);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        for(bodyStmt_Node stmt : stmts) { str.append(stmt.convertToJott()); }
        return str.append(returnStmt.convertToJott()).toString();
    }
}

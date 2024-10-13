package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;


 /**
  * A function that represents an if statement node in the parse tree.
    * parse tree grammar: If [< expr >]{< body >} < elseif >⋆ < else >
  */
public class ifStmt_Node extends Jott_Node{
    private expr_Node expression;
    private body_Node body;
    private static  elseif_Node elseif;
    private static  else_Node elseNode;

    /**
     * Private Constructor
     * (validation of the node done in {@link #parseIfStmtNode})
     * @param expression    an expression node
     * @param body          a body node
     * @param elseif        an elseif node
     * @param elseNode      an else node
     */
    public ifStmt_Node(expr_Node expression, body_Node body, elseif_Node elseif, else_Node elseNode){
        this.expression = expression;
        this.body = body;
        this.elseif = elseif;
        this.elseNode = elseNode;
    }

    /**
     * Static parse method returning an {@link ifStmt_Node} for the parse tree.
     * @param tokens   the list of tokens being parsed into a parse tree
     * @returnm An if statement node which has been validated in accordance
     * @throws SyntaxError {@code Unexpected EOF}: no token to parse
     */
    public static ifStmt_Node parseIfStmtNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        assert(tokens.get(0).getToken().equals("If")); // not syntax error
        tokens.remove(0);
        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) { throw new SyntaxError("Invalid token: expected '['"); }
        tokens.remove(0);

        expr_Node expression = expr_Node.parseExprNode(tokens);

        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) { throw new SyntaxError("Invalid token: expected ']'"); }
        tokens.remove(0);

        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) { throw new SyntaxError("Invalid token: expected '{'"); }
        tokens.remove(0);

        body_Node body = body_Node.parseBodyNode(tokens);

        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) { throw new SyntaxError("Invalid token: expected '}'"); }
        tokens.remove(0);


        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getToken().equals("Elseif")) {
            elseif_Node elseif = elseif_Node.parseElseifNode(tokens);
        }

        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getToken().equals(".")) {
            tokens.remove(0);
        }

        if(tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getToken().equals("Else")) {
            else_Node elseNode = else_Node.parseElseNode(tokens);
        }

        return new ifStmt_Node(expression, body, elseif, elseNode);

    }

    /**
     * Converts the if statement node to a string
     * @return The string representation of the if statement node
     */
    @Override
    public String convertToJott() {
        return "If" + "[" + this.expression.convertToJott() + "]" + "{" + this.body.convertToJott() + "}" + this.elseif.convertToJott() + "." + this.elseNode.convertToJott();
    }
}

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

    private final expr_Node expressionN;
    private final body_Node bodyN;
    private final elseif_Node[] elseifN;
    private final else_Node elseN;

    /**
     * Private Constructor
     * (validation of the node done in {@link #parseIfStmtNode})
     * @param expression    an expression node
     * @param body          a body node
     * @param elseif        an elseif node
     * @param elseNode      an else node
     */
    public ifStmt_Node(expr_Node expression, body_Node body, elseif_Node[] elseifNodes, else_Node elseNode){
        this.expressionN = expression;
        this.bodyN = body;
        this.elseifN = elseifNodes;
        this.elseN = elseNode;
    }

    /**
     * Static parse method returning an {@link ifStmt_Node} for the parse tree.
     * @param tokens   the list of tokens being parsed into a parse tree
     * @return An if statement node which has been validated in accordance
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
        if(tokens.get(0).getTokenType() != TokenType.L_BRACE) { throw new SyntaxError("Invalid token: expected '{'"); }
        tokens.remove(0);

        body_Node body = body_Node.parseBodyNode(tokens);

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.R_BRACE) { throw new SyntaxError("Invalid token: expected '}'"); }
        tokens.remove(0);

        ArrayList<elseif_Node> elseifNodes = new ArrayList<>();
        else_Node elseNode = null;

        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        while(tokens.get(0).getToken().equals("Elseif")) {
            elseifNodes.add(elseif_Node.parseElseifNode(tokens));
            if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        }

        elseNode = else_Node.parseElseNode(tokens);

        return new ifStmt_Node(
            expression, 
            body, 
            elseifNodes.toArray(new elseif_Node[elseifNodes.size()]), 
            elseNode
        );
    }

    /**
     * Converts the if statement node to a string
     * @return The string representation of the if statement node
     */
    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();

        str.append("If[")
           .append(this.expressionN.convertToJott())
           .append("]{")
           .append(this.bodyN.convertToJott())
           .append("}");
        for(elseif_Node elseif : this.elseifN) { str.append(elseif); }
        str.append(this.elseN.convertToJott());
        
        return str.toString();
    }
}

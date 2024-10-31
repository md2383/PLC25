package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

 /**
  * A function that represents an else node in the parse tree.
    * parse tree grammar: Else { < body > }
  */
public class else_Node extends Jott_Node{

    private final body_Node body;

    /**
     * Private Constructor
     * (validation of the node done in {@link #parseElseNode})
     * @param body a body node
     */
    private else_Node(body_Node body){
        this.body = body;
    }

    /**
    * 
    * @param tokens -  the list of tokens being parsed into a parse tree
    * @return An else node which has been validated in accordance
    * @throws SyntaxError {@code Unexpected EOF}: no token to parse
    */
    public static else_Node parseElseNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() > 2) {
            if(tokens.get(0).getToken().equals("Else")) {
                tokens.remove(0);
                if(tokens.get(0).getTokenType() != TokenType.L_BRACE) { throw new SyntaxError("Invalid token: expected '{'"); }
                tokens.remove(0);

                body_Node body = body_Node.parseBodyNode(tokens);

                if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
                if(tokens.get(0).getTokenType() != TokenType.R_BRACE) { throw new SyntaxError("Invalid token: expected '}'"); }
                tokens.remove(0);

                return new else_Node(body);
            }
        }

        return new else_Node(null);
    }

    private boolean isVoid() {
        return this.body == null;
    }

    /**
     * Converts the else node to a Jott string
     */
    @Override
    public String convertToJott() {
        return isVoid() ?
            "" : "Else" + "{" + this.body.convertToJott() + "}";
    }

    @Override
    public boolean validateTree() {
        return body.validateTree();
    }
}

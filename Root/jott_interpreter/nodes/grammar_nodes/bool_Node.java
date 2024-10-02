package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.nodes.Jott_Node;
import provided.Token;
import provided.TokenType;

public class bool_Node extends Jott_Node {

    private ArrayList<Token> Tokens;
    private ArrayList<Jott_Node> children;

    public bool_Node() {
        super();
    }

    public bool_Node(ArrayList<Token> tokens) {
        super();
        this.Tokens = tokens;   
    }

    public ArrayList<Jott_Node> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Jott_Node> children) {
        this.children = children;
    }

    public ArrayList<Token> getTokens() {
        return this.Tokens;
    }

    public void setTokens(ArrayList<Token> Tokens) {
        this.Tokens = Tokens;
    }

    @Override
    public boolean validate() {
        if (this.getChildren().size() != 1) {
            return false;
        }
        if (this.Tokens.get(0).getTokenType() == TokenType.ID_KEYWORD
                && (this.Tokens.get(0).getToken().equals("True") || this.Tokens.get(0).getToken().equals("False"))) {
            return true;
        }
        return false;
    }
    
}

package jott_interpreter.nodes;

import java.util.ArrayList;
import jott_interpreter.JottNode;
import provided.Token;
import provided.TokenType;

public class boolNode extends JottNode {

    private ArrayList<Token> Tokens;
    private ArrayList<JottNode> children;

    public boolNode() {
        super();
    }

    public boolNode(ArrayList<Token> tokens) {
        super();
        this.Tokens = tokens;   
    }

    public ArrayList<JottNode> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<JottNode> children) {
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

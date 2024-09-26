package jott_interpreter;

import java.util.ArrayList;
import provided.Token;

public class JottNode {
    
    private JottNode parent;
    private ArrayList<Token> Tokens;
    private ArrayList<JottNode> children;

    // JottNode is a class that represents a node in the Jott parse tree.
    public JottNode() {
        this.parent = null;
        this.children = new ArrayList<JottNode>();
    }

    // Same thing as above, but with a parent node.
    public JottNode( JottNode parent ) {
        this.parent = parent;
        this.children = new ArrayList<JottNode>();
    }

    // Getters and setters for the parent and children of the node.
    public JottNode getParent() {
        return this.parent;
    }

    public void setParent(JottNode parent) {
        this.parent = parent;
    }

    public ArrayList<JottNode> getChildren() {
        return this.children;
    }

    public void addChild(JottNode child) {
        this.children.add(child);
    }

    public void removeChild(JottNode child) {
        this.children.remove(child);
    }

    public ArrayList<Token> getTokens() {
        return this.Tokens;
    }

    public void setTokens(ArrayList<Token> Tokens) {
        this.Tokens = Tokens;
    }

    public void addToken(Token token) {
        this.Tokens.add(token);
    }

    public void removeToken(Token token) {
        this.Tokens.remove(token);
    }

    // Validate node that gets overridden by the subclasses.
    public boolean validate() {
        return false;
    }
}

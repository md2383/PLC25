package jott_interpreter;

import java.util.ArrayList;
import provided.Token;

/**
 * JottNode is a class that represents a node in the Jott parse tree.
 */
public class JottNode {
    
    private ArrayList<Token> Tokens;
    private ArrayList<JottNode> children;

    /**
     * Head of the Parse Tree.
     * The parent is null, this is the start of the Tree.
     */
    public JottNode() {
        this.children = new ArrayList<JottNode>();
    }

    /**
     * Child node initializer.
     * 
     * @param parent    The token dictating the resulting parseTree's grammer
     */
    public JottNode( ArrayList<JottNode> children ) {
        this.children = children;
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

package jott_interpreter;

import java.util.ArrayList;

/**
 * JottNode is a class that represents a node in the Jott parse tree.
 */
public class JottNode {
    
    private JottNode parent;
    private ArrayList<JottNode> children;

    /**
     * Head of the Parse Tree.
     * The parent is null, this is the start of the Tree.
     */
    public JottNode() {
        this.parent = null;
        this.children = new ArrayList<JottNode>();
    }

    /**
     * Child node initializer.
     * 
     * @param parent    The token dictating the resulting parseTree's grammer
     */
    public JottNode( JottNode parent ) {
        this.parent = parent;
        this.children = new ArrayList<JottNode>();
    }

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
}

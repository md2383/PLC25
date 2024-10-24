package jott_interpreter.nodes;

import provided.*;

/**
 * Abstract head of the Parse Tree
 */
public abstract class Jott_Node implements JottTree{

    final public int linenum;

    public Jott_Node() {
        this.linenum = -1;
    }

    public Jott_Node(int line_number) {
        this.linenum = line_number;
    }

    @Override
    abstract public String convertToJott();

    @Override
    abstract public boolean validateTree();

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    abstract public String getType();
    
    @Override
    public String toString() {
        return this.convertToJott();
    }
}

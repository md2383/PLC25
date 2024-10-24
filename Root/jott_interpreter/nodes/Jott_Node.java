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
    abstract public String getType();

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
    @Override
    public String toString() {
        return this.convertToJott();
    }
}

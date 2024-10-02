package jott_interpreter.nodes;

import provided.*;

/**
 * Abstract head of the Parse Tree
 */
public abstract class Jott_Node implements JottTree{
    
    public Jott_Node() {

    }

    @Override
    abstract public String convertToJott();

    @Override
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

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

package jott_interpreter.nodes;

import provided.*;

/**
 * Abstract head of the Parse Tree
 */
public abstract class Jott_Node implements JottTree{

    @Override
    abstract public String convertToJott();

    @Override
    abstract public boolean validateTree();

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

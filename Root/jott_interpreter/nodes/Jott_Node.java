package jott_interpreter.nodes;

import java.util.HashMap;
import java.util.LinkedHashMap;

import jott_interpreter.IdMap;
import jott_interpreter.ReturnType;
import provided.*;

/**
 * Abstract head of the Parse Tree
 */
public abstract class Jott_Node implements JottTree{

    protected final static IdMap declared_functions = new IdMap();
    protected final static HashMap<String, IdMap> function_scope = new LinkedHashMap<>();
    protected static String current_function_ID = null;

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

    public ReturnType getType() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.convertToJott();
    }
}

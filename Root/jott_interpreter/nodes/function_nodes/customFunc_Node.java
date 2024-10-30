package jott_interpreter.nodes.function_nodes;

import java.lang.reflect.Method;

import jott_interpreter.nodes.Jott_Node;

public class customFunc_Node extends Jott_Node{
    private final String ID;
    private final Method METHOD;

    public customFunc_Node(final String id, final Method method) {
        this.ID = id;
        this.METHOD = method;
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    } 
}

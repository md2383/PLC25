package jott_interpreter.nodes.function_nodes;

import jott_interpreter.ReturnType;
import jott_interpreter.nodes.Jott_Node;

public class customParam_Node extends Jott_Node {

    private final ReturnType TYPE;

    public customParam_Node(ReturnType type) {
        this.TYPE = type;
    }

    @Override
    public String convertToJott() {
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
        // This class should never be executed
        // This class is a placeholder for validating types in custom functions
        assert (false); 
    }
    
    @Override
    public ReturnType getType() {
        return this.TYPE;
    }
}

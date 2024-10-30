package jott_interpreter.nodes.function_nodes;

import java.lang.reflect.Method;

import jott_interpreter.ReturnType;
import jott_interpreter.nodes.Jott_Node;

/**
 * <h2>customFunc_Node</h2>
 * 
 * <p>
 * Represents a custom function node within the Jott interpreter's parse tree.
 * This class encapsulates the details of a function, including its identifier,
 * the method reference for execution, and the return type of the function.
 * </p>
 * 
 * @see {@link Jott_Node}
 */
public class customFunc_Node extends Jott_Node{
    
    /** The identifier for this function */
    private final String ID;
    /** The method reflection reference for this function's implementation */
    private final Method METHOD;
    /** The return type of the function */
    private final ReturnType RETURN_TYPE;

    /**
     * Constructs a new {@link customFunc_Node} with the specified identifier,
     * method, and return type.
     * 
     * @param id The identifier for the function
     * @param method The method reference for the function's implementation
     * @param return_type The return type of the function
     */
    public customFunc_Node(final String id, final Method method, final ReturnType return_type) {
        this.ID = id;
        this.METHOD = method;
        this.RETURN_TYPE = return_type;
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        return true;
    } 

    @Override
    public ReturnType getType() {
        return RETURN_TYPE;
    }
}

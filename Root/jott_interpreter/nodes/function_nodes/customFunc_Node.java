package jott_interpreter.nodes.function_nodes;

import jott_interpreter.IdMap;
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

    /** The return value of this function */
    private Object value = null;

    /**
     * Constructs a new {@link customFunc_Node} with the specified identifier,
     * method, and return type.
     * 
     * @param id The identifier for the function
     * @param method The method reference for the function's implementation
     * @param return_type The return type of the function
     */
    public customFunc_Node(final String id) {
        this.ID = id;
        // Adding custom parameters to the scope for each custom function
        // The id for each paramater is meaningless, and simply for differentiating between
        // custom functions with more than 1 input paramater
        IdMap params = new IdMap();
        switch (this.ID) {
            case "concat" :
                params.addDynamicVar("input_b", new customParam_Node(ReturnType.String));
            case "print" :
            case "length" :
                params.addDynamicVar("input_a", new customParam_Node(ReturnType.String));
                break;
            default:
                assert (false); // function must be defined in switch case
        }
        Jott_Node.function_scope.put(this.ID, params);
    }

    @Override
    public String convertToJott() {
        return ID;
    }

    @Override
    public boolean validateTree() {
        return true;
    } 

    @Override
    public void execute() {
        switch (this.ID) {
            case "print" :
                // Grabs the input of the function, converts to an integer if applicable, prints the input value
                Jott_Node input = Jott_Node.function_scope.get("print").getNode("input_a");
                Object value = input.getValue();
                // Truncating value if it's an Integer (all numbers are doubles for expressions).
                // This is only needed for print because doubles will always calculate the same as integers
                // (except for integer division and that is handled in expr_Node).
                if(input.getType() == ReturnType.Integer) {
                    value = Integer.valueOf( ((Double)(value)).intValue() );
                }
                System.out.println(value);
                break;
            case "concat" :
                IdMap scope = Jott_Node.function_scope.get("concat");

                assert (scope.getNode("input_a").getValue() instanceof String);
                assert (scope.getNode("input_b").getValue() instanceof String);

                this.value = 
                    (String)(scope.getNode("input_a").getValue()) + 
                    (String)(scope.getNode("input_b").getValue());
                break;
            case "length" :
                assert (Jott_Node.function_scope.get("length").getNode("input_a").getValue() instanceof String);
                this.value = (
                    (String)(Jott_Node.function_scope.get("length").getNode("input_a").getValue())
                ).length();
                break;
            default:
                assert (false); // function must be defined in switch case
        }
    }

    @Override
    public ReturnType getType() {
        switch (this.ID) {
            case "print" :
                return ReturnType.Void;
            case "concat" :
                return ReturnType.String;
            case "length" :
                return ReturnType.Integer;
            default:
                assert (false); // function must be defined in switch case
                return ReturnType.Void;
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}

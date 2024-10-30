package jott_interpreter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jott_interpreter.nodes.function_nodes.customFunc_Node;
import provided.JottTree;

/**
 * <h2>IdMap</h1>
 * 
 * <p>
 * A map of the defined variables or functions in a given scope within the Jott interpreter.
 * </p>
 * 
 * <p>
 * This is built directly into the node structure, certain nodes have a scope
 *  ->  The Jott_Node contains a static {@link IdMap} for functions,
 *      each function contains an {@link IdMap} referencing the variables 
 *      declared in the scope.
 * </p>
 * 
 * @see {@link jott_interpreter.nodes.Jott_Node}
 * @see {@link jott_interpreter.nodes.function_nodes.funcBody_Node}
 * 
 * @hidden NOTE: haven't entirely decided where to put scopes for functions...
 */
public class IdMap {

    private final HashMap<String, JottTree> id_map;

    /**
     * Constructs a new {@link IdMap} instance with an empty identifier map.
     */
    public IdMap() {
        id_map = new LinkedHashMap<>();
    }

    /**
     * Declares built-in functions and maps them to their respective identifiers.
     * 
     * <p>
     * This method initializes the built-in functions that the interpreter can use, such as
     * print, concat, and length. It uses Java Reflection to create method references for 
     * these functions and adds them to the id_map.
     * </p>
     * 
     * @throws AssertionError if id_map is not empty.
     */
    public void declareBuiltinFunctions() {
        assert(id_map.isEmpty());

        // Method Reflection parameter declarations
        Class<?>[] print_params = new Class[1];
        // Class<?>[] concat_params = new Class[1];
        // Class<?>[] length_params = new Class[1];

        print_params[0] = String.class;

        // Method Reflection Instantiation
        Method PRINT = null;
        Method CONCAT = null;
        Method LENGTH = null;

        // Method Reflection Definitions
        try { 
            PRINT = IdMap.class.getMethod("print_wrapper", print_params);
            // TODO: CONCAT
            // TODO: LENGTH
        } catch (NoSuchMethodException e) {
            // TODO: handle exception
        }

        // Assigning the custom functions
        JottTree print = new customFunc_Node("print", PRINT);
        JottTree concat = new customFunc_Node("concat", CONCAT);
        JottTree length = new customFunc_Node("length", LENGTH);

        // Putting the functions into this map
        this.id_map.put("print", print);
        this.id_map.put("concat", concat);
        this.id_map.put("length", length);
    }

    /**
     * A wrapper method for printing a string to the console.
     * 
     * @param input The string to be printed.
     */
    @SuppressWarnings("unused")
    private static void print_wrapper(String input) {
        System.out.println(input);
    }

    @SuppressWarnings("unused")
    private static void concat_wrapper() {
        // TODO
    }

    @SuppressWarnings("unused")
    private static void length_wrapper() {
        // TODO
    }
}

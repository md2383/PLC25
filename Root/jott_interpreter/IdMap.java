package jott_interpreter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jott_interpreter.nodes.function_nodes.customFunc_Node;
import provided.JottTree;

/**
 * <h>
 * A map of the defined variables or functions in a given scope.
 * 
 * <p>
 * This is built directly into the node structure, certain nodes have a scope
 *  ->  The Jott_Node contains a static {@link IdMap} for functions,
 *      each function contains an {@link IdMap} referencing the variables 
 *      declared in the scope.
 * @see {@link jott_interpreter.nodes.Jott_Node}
 * @see {@link jott_interpreter.nodes.function_nodes.funcBody_Node}
 * 
 * @hidden NOTE: haven't entirely decided where to put scopes for functions...
 */
public class IdMap {

    private HashMap<String, JottTree> id_map;

    public IdMap() {
        id_map = new LinkedHashMap<>();
    }

    public void declareBuiltinFunctions() {
        assert(id_map.isEmpty());

        Class<?>[] print_params = new Class[1];
        // Class<?>[] concat_params = new Class[1];
        // Class<?>[] length_params = new Class[1];

        print_params[0] = String.class;

        Method PRINT = null;
        Method CONCAT = null;
        Method LENGTH = null;

        try { 
            PRINT = IdMap.class.getMethod("print_wrapper", print_params);
            // TODO: CONCAT
            // TODO: LENGTH
        } catch (NoSuchMethodException e) {
            // TODO: handle exception
        }

        JottTree print = new customFunc_Node("print", PRINT);
        JottTree concat = new customFunc_Node("concat", CONCAT);
        JottTree length = new customFunc_Node("length", LENGTH);

        id_map.put("print", print);
        id_map.put("concat", concat);
        id_map.put("length", length);
    }

    @SuppressWarnings("unused")
    private static void print_wrapper(String input) {
        System.out.println(input);
    }
}

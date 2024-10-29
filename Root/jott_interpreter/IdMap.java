package jott_interpreter;

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
 * @hidden NOTE: havn't entirely decided where to put scopes for functions...
 */
public class IdMap {

    private HashMap<String, JottTree> id_map;

    public IdMap() {
        id_map = new LinkedHashMap<>();
    }

    public void declareBuiltinFunctions() {
        assert(id_map.isEmpty());

        // TODO
        JottTree print = new customFunc_Node(null, null, null);
        JottTree concat = new customFunc_Node(null, null, null);
        JottTree length = new customFunc_Node(null, null, null);

        id_map.put("print", print);
        id_map.put("concat", concat);
        id_map.put("length", length);
    }
}

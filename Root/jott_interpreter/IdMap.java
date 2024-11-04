package jott_interpreter;

import java.util.LinkedHashMap;

import jott_interpreter.nodes.function_nodes.customFunc_Node;
import jott_interpreter.nodes.Jott_Node;

// Usage: documentation
import provided.JottTree;

/**
 * <h1>IdMap</h1>
 * 
 * <p>
 * A map of the defined variables or functions in a given scope within the Jott interpreter.
 * </p>
 * 
 * <p>
 * This is built directly into the node structure, certain nodes have a scope.
 *  ->  The {@link Jott_Node} contains a static {@link IdMap} for functions and
 *      a non-static {@link IdMap} from a hashmap for scope. Each function 
 *      contains an {@link IdMap} referencing the variables declared in the scope.
 * </p>
 * 
 * @see {@link Jott_Node}
 */
public class IdMap {

    /** A linked hash map assigning an id to an executable node in the {@link JottTree} */
    private final LinkedHashMap<String, Jott_Node> id_map;

    /** 
     * <p> A linked hash map referencing just the dynamic variables in {@link #id_map} </p>
     * <p> 
     * This map is used for function parameters, and would be used for 
     * global dynamic variables, if those were present in the language. 
     * </p>
     */
    private final LinkedHashMap<String, Jott_Node> dynamic_var_map;

    /**
     * Constructs a new {@link IdMap} instance with an empty identifier map.
     */
    public IdMap() {
        id_map = new LinkedHashMap<>();
        dynamic_var_map = new LinkedHashMap<>();
    }

    /**
     * Declares built-in functions and maps them to their respective identifiers.
     * 
     * <p>
     * This method initializes the built-in functions that the interpreter can use, such as
     * print, concat, and length. 
     * </p>
     * 
     * @throws AssertionError if {@link #id_map} is not empty.
     * @implSpec    This method should only be called at the creation 
     *              of the node-based parse tree.
     * @implNote    If there are functions already defined in the parse tree,
     *              this function will throw an {@code AssertionError}
     */
    public void declareBuiltinFunctions() {
        assert(id_map.isEmpty());

        // Assigning the custom functions per an id
        Jott_Node print = new customFunc_Node("print");
        Jott_Node concat = new customFunc_Node("concat");
        Jott_Node length = new customFunc_Node("length");

        // Putting the functions into this map
        this.id_map.put("print", print);
        this.id_map.put("concat", concat);
        this.id_map.put("length", length);
    }

    /**
     * Retrieves the return type of the function or variable associated with the given identifier.
     * @param id the identifier of the function or variable.
     * @return the {@link ReturnType} of the function or variable, as stored in the {@link #id_map}.
     * @throws NullPointerException if the identifier does not exist in the {@link #id_map}.
     */
    public ReturnType getReturnType(String id) {
        return id_map.get(id).getType();
    }

    /**
     * Retrieves the {@link Jott_Node} (function or variable) associated with the given identifier.
     * @param id the identifier of the function or variable.
     * @return the {@link Jott_Node} associated with the id, or null if not found.
     */
    public Jott_Node getNode(String id) {
        return id_map.get(id);
    }

    /**
     * Checks if the given identifier exists in the {@link #id_map}.
     * @param id the identifier to check for.
     * @return true if the identifier exists in the {@link #id_map}, false otherwise.
     */
    public boolean contains(String id) {
        return id_map.keySet().contains(id);
    }

    /**
     * Checks if the given identifier is a dynamic reference variable.
     * @param id the identifier to check for.
     * @return true if the identifier is dynamic, false otherwise.
     */
    public boolean isDynamic(String id) {
        return dynamic_var_map.keySet().contains(id);
    }

    /**
     * Adds a node reference to an id in the {@link #id_map}.
     * @param id the identifier of the function or variable.
     * @param node the {@link Jott_Node} referenced to the id
     */
    public void add(String id, Jott_Node node) {
        id_map.put(id, node);
    }

    /**
     * Adds a dynamic (undefined) variable to the {@link #id_map}.
     * @param id the identifier of the variable.
     * @param node a reference to the parameter (or global variable) declaration.
     */
    public void addDynamicVar(String id, Jott_Node node) {
        id_map.put(id, null);
        dynamic_var_map.put(id, node);
    }
}

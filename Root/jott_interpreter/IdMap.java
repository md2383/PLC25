package jott_interpreter;

import java.util.ArrayList;
import java.util.HashMap;
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

    /** A hash map referencing whether an id has been defined or not */
    private final HashMap<String, Boolean> defined_vars;

    /**
     * Constructs a new {@link IdMap} instance with an empty identifier map.
     */
    public IdMap() {
        this.id_map = new LinkedHashMap<>();
        this.dynamic_var_map = new LinkedHashMap<>();
        this.defined_vars = new HashMap<>();
    }

    private IdMap(
            LinkedHashMap<String, Jott_Node> idm,
            LinkedHashMap<String, Jott_Node> dvm,
            HashMap<String, Boolean> def_vars
        ) {
            this.id_map = idm;
            this.dynamic_var_map = dvm;
            this.defined_vars = def_vars;
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
        assert (this.id_map.isEmpty()); // Should only be called at the start of a program

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
        if(this.id_map.get(id) == null) {
            return this.dynamic_var_map.get(id).getType();
        } else {
            return this.id_map.get(id).getType();
        }
    }

    /**
     * Retrieves the {@link Jott_Node} (function or variable) associated with the given identifier.
     * @param id the identifier of the function or variable.
     * @return the {@link Jott_Node} associated with the id, or null if not found.
     */
    public Jott_Node getNode(String id) {
        return this.id_map.get(id);
    }

    /**
     * Checks if the given identifier exists in the {@link #id_map}.
     * @param id the identifier to check for.
     * @return true if the identifier exists in the {@link #id_map}, false otherwise.
     */
    public boolean contains(String id) {
        return this.id_map.keySet().contains(id);
    }

    /**
     * Checks if the given identifier is a dynamic reference variable.
     * @param id the identifier to check for.
     * @return true if the identifier is dynamic, false otherwise.
     */
    public boolean isDynamic(String id) {
        return this.dynamic_var_map.keySet().contains(id);
    }

    public boolean isDefined(String id) {
        return this.defined_vars.get(id);
    }

    /**
     * Adds a node reference to an id in the {@link #id_map}.
     * @param id the identifier of the function or variable.
     * @param node the {@link Jott_Node} referenced to the id
     */
    public void add(String id, Jott_Node node) {
        this.id_map.put(id, node);
        this.defined_vars.put(id, false); // defined_vars should only be used for variables
    }

    /**
     * Adds a dynamic (undefined) variable to the {@link #id_map}.
     * @param id the identifier of the variable.
     * @param node a reference to the parameter (or global variable) declaration.
     */
    public void addDynamicVar(String id, Jott_Node node) {
        this.id_map.put(id, null);
        this.dynamic_var_map.put(id, node);
        this.defined_vars.put(id, false);
    }

    public void define_var(String id) {
        assert this.contains(id);
        // this.id_map.put(id, node); // PROBLEM: x = x + 1 (circular reference)
        this.defined_vars.replace(id, true); 
    }

    /**
     * Sets the value of a variable to some expression node. 
     *  (replacing a varDec_node or previously assigned expr_node)
     * @param id
     * @param node
     * @hidden This function is for the assignment node, it can be achieved with 
     *      an add() and define_var() call on the id, but this is obviously cleaner.
     */
    public void assign_var(String id, Jott_Node node) {
        assert (this.contains(id));
        this.id_map.put(id, node);
    }

    /**
     * Gets the node references to all the declared dynamic variables in order
     * @return the array of ordered {@link Jott_Node} variable declarations
     */
    public Jott_Node[] getOrderedDynamicNodes() {
        ArrayList<Jott_Node> orderedNodes = new ArrayList<>();
        this.dynamic_var_map.forEach(
            (@SuppressWarnings("unused") String id, Jott_Node node) -> { 
                orderedNodes.add(node); 
            }
        );
        return orderedNodes.toArray(new Jott_Node[orderedNodes.size()]);
    }

    public LinkedHashMap<String, Jott_Node> getDynamicVars() {
        return this.dynamic_var_map;
    }

    public IdMap copy() {
        LinkedHashMap<String, Jott_Node> copy_id_map = new LinkedHashMap<>(this.id_map); 
        LinkedHashMap<String, Jott_Node> copy_dv_map = new LinkedHashMap<>(this.dynamic_var_map);
        HashMap<String, Boolean> copy_d_vars = new HashMap<>(this.defined_vars);

        return new IdMap(copy_id_map, copy_dv_map, copy_d_vars);
    }
}

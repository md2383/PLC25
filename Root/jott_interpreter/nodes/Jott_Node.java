package jott_interpreter.nodes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import jott_interpreter.IdMap;
import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import provided.*;

/* Used only by documentation */
import jott_interpreter.nodes.grammar_nodes.program_Node;

/**
 * <h1> Jott Node </h1>
 * <p>
 *  Abstract reference to a node in the Parse Tree. Each node in the parse 
 *  tree is associated with a particular component of the Jott language's structure.
 *  It is expected that the head of the Parse or Execution Tree will typically be a
 *  {@link program_Node}, which is a concrete subclass of this abstract class.
 * </p>
 * <p>
 *  The {@code Jott_Node} class provides common functionality for managing the 
 *  Jott parse tree nodes, including the validation, string conversion, 
 *  and execution of Jott code. It also maintains global state for the parsing 
 *  and execution process, such as the available declared functions and the call stack.
 * </p>
 * <p>
 *  This class implements the {@link JottTree} interface and provides abstract 
 *  methods for subclasses to implement specific logic for handling different 
 *  types of nodes in the Jott tree.
 * </p>
 * 
 * @see JottTree
 * @see program_Node
 */
public abstract class Jott_Node implements JottTree{
    /** The functions available to this {@code Jott_Node} (scope) */
    protected static IdMap declared_functions;
    /** The mapping of all available function ids to their variable Nodes */
    protected static HashMap<String, IdMap> function_scope;
    /** The "Call Stack" of the execution tree */
    protected static Stack<String> current_function_ID;

    /** The name of the source file containing the Jott code */
    public static String filename;

    /** The line number of the referenced node in the Jott code. */
    protected final int linenum;

    /**
     * <h>
     * Initializes the static fields of this class. 
     * This method is called before any nodes are processed.
     * </h>
     * <p>
     * This initialization typically happens in the parser before the 
     * tree is fully constructed. It (re-)sets the global environment for 
     * function declarations, function scopes, and the call stack.
     * </p>
     * @implNote    Should only be implemented by the node validation process:
     *              currently this resides only in the parse function
     *              for {@link program_Node}
     */
    protected static void init() {
        declared_functions = new IdMap();
        function_scope = new LinkedHashMap<>();
        current_function_ID = new Stack<>();
        filename = null;
    }

    /**
     * Default constructor for the Jott_Node class. 
     * Initializes the line number to -1.
     */
    public Jott_Node() {
        this.linenum = -1;
    }

    /**
     * Constructor for the Jott_Node class that allows for the specification 
     * of the {@link #linenum} where the node appears in the source code.
     * @param line_number - The line number of this node in the source code.
     */
    protected Jott_Node(int line_number) {
        this.linenum = line_number;
    }

    @Override
    abstract public String convertToJott(); // Public Docs in {@link JottTree}

    @Override
    abstract public boolean validateTree(); // Public Docs in {@link JottTree}

    @Override
    abstract public void execute() throws SemanticError; // Public Docs in {@link JottTree}

    /**
     * <h>
     * Retrieves the type of value associated with this node, if applicable.
     * </h>
     * <p>
     * This method is used to provide type information about the node, 
     * defined by the {@link ReturnType} enum. It is also partially used to 
     * differentiate variables, constants, or functions.
     * </p>
     * <p>
     * {@link ReturnType}: {
     *      Boolean,
     *      Double,
     *      Integer,
     *      String,
     *      Void
     * }
     * </p>
     * @return A {@link ReturnType} indicating the type of this node, or {@code null} if not applicable.
     */
    public ReturnType getType() {
        return null;
    }

    /**
     * Retrieves the value associated with this node, if applicable.
     * This method is used to fetch the value of a node <i> after </i> it has been executed.
     * @return The value associated with this node, or {@code null} if not applicable.
     */
    public Object getValue() {
        return null;
    }
    
    /**
     * Provides a string representation of this node, which is its Jott string representation.
     * This method returns the same as {@link #convertToJott()} for most applicable nodes.
     * @return A string representation of this node.
     */
    @Override
    public String toString() {
        return this.convertToJott();
    }
}

package jott_interpreter.nodes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import jott_interpreter.IdMap;
import jott_interpreter.ReturnType;
import provided.*;

/* Used only by documentation */
import jott_interpreter.nodes.grammar_nodes.program_Node;

/**
 * <h1>
 * Jott Node
 * </h1>
 * <p>
 * Abstract head of the Parse Tree
 * </p>
 */
public abstract class Jott_Node implements JottTree{
    /** */
    protected static IdMap declared_functions;
    /** */
    protected static HashMap<String, IdMap> function_scope;
    /** */
    protected static Stack<String> current_function_ID;

    /** */
    public static String filename;

    /** */
    protected final int linenum;

    /**
     * 
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
     * 
     */
    public Jott_Node() {
        this.linenum = -1;
    }

    /**
     * 
     * @param line_number
     */
    public Jott_Node(int line_number) {
        this.linenum = line_number;
    }

    @Override
    abstract public String convertToJott();

    @Override
    abstract public boolean validateTree();

    @Override
    abstract public void execute();

    /**
     * 
     * @return
     */
    public ReturnType getType() {
        return null;
    }
    
    /**
     * 
     */
    @Override
    public String toString() {
        return this.convertToJott();
    }
}

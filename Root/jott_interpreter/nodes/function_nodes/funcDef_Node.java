package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;
import jott_interpreter.IdMap;
import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.id_Node;
import provided.*;

/**
 * A function definition containing an {@code ID} [{@link id_Node}], the list of
 * parameters [{@link funcDefParams_Node}], the return type [{@link functionReturn_Node}],
 * and the body [{@link funcBody_Node}] of the function.
 */
public class funcDef_Node extends Jott_Node {
    
    // Def (Doesn't need to be stored just checked if it's there)
    private final id_Node id;
    // OpenBracket (Doesn't need to be stored just checked if it's there)
    private final funcDefParams_Node func_def_params;
    // CloseBracket (Doesn't need to be stored just checked if it's there)
    // Colon (Doesn't need to be stored just checked if it's there)
    private final funcReturn_Node function_return;
    // OpenBrace (Doesn't need to be stored just checked if it's there)
    private final funcBody_Node f_body;
    // CloseBrace (Doesn't need to be stored just checked if it's there)

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseFunctionDefNode})
     * @param line_number       the line number of the first token in this node
     * @param id                an ID node referencing the function
     * @param func_def_params   a funcDefParams node referencing the (list of) 
     *                          parameters for the function
     * @param function_return   a functionReturn node referencing the return type
     * @param f_body            a funcBody node referencing the body of the function
     */
    private funcDef_Node(
            int line_number,
            id_Node id, 
            funcDefParams_Node func_def_params, 
            funcReturn_Node function_return, 
            funcBody_Node f_body
        ) {
        super(line_number);
        this.id = id;
        this.func_def_params = func_def_params;
        this.function_return = function_return;
        this.f_body = f_body;
    }
    
    /**
     * Static parse method returning a {@link functionDef_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  A function definition node which has been validated in accordance 
     *          with the parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @throws SyntaxError  {@code Invalid Function Definition}: token being 
     *                      parsed is not a valid function definition
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static funcDef_Node parseFunctionDefNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        // Check the type is Id/Keyword and is Def, if not throw an error
        if (!(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(0).getToken().equals("Def"))) {
            throw new SyntaxError("Invalid Function Definition: Missing Keyword 'Def'");
        }

        int line_number = tokens.get(0).getLineNum();
        tokens.remove(0); // Removing the Def token from the list (Not storing)
        
        // Saving the ID node
        id_Node id = id_Node.parseIdNode(tokens);

        // Check the type is OpenBracket and if not throw an error
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {throw new SyntaxError("Invalid Function Definition: Missing a '[' Token");}
        tokens.remove(0); // Removing the OpenBracket token from the list (Not storing)

        // Saving the func_def_params node
        funcDefParams_Node func_def_params = funcDefParams_Node.parseFuncDefParamsNode(tokens);

        // Check the type is CloseBracket and if not throw an error
        if (tokens.size() < 2) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {throw new SyntaxError("Invalid Function Definition: Missing a ']' Token");}
        tokens.remove(0); // Removing the CloseBracket token from the list (Not storing)
        // Check the type is Colon and if not throw an error
        if (tokens.get(0).getTokenType() != TokenType.COLON) {throw new SyntaxError("Invalid Function Definition: Missing a ':' Token");}
        tokens.remove(0); // Removing the Colon token from the list (Not storing)

        // Saving the function_return node
        funcReturn_Node function_return = funcReturn_Node.parseFunctionReturnNode(tokens);

        // Check the type is OpenBrace and if not throw an error
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {throw new SyntaxError("Invalid Function Definition: Missing a '{' Token");}
        tokens.remove(0); // Removing the OpenBrace token
    
        // Saving the f_body node
        funcBody_Node f_body = funcBody_Node.parseFuncBodyNode(tokens);

        // Check the type is CloseBrace and if not throw an error
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {throw new SyntaxError("Invalid Function Definition: Missing a '}' Token");}
        tokens.remove(0); // Removing the CloseBrace token
        
        return new funcDef_Node(line_number, id, func_def_params, function_return, f_body);
    }

    /**
     * Converts the function definition node into a Jott string.
     * 
     * @return  A string representation of the function definition node
     */
    @Override
    public String convertToJott() {
        StringBuilder jott = new StringBuilder();
        jott.append( "Def " )
            .append( this.id.convertToJott() )
            .append( "[" + this.func_def_params.convertToJott() + "]" )
            .append( ":" + this.function_return.convertToJott() )
            .append( "{" + this.f_body.convertToJott() + "}" );
        return jott.toString();
    }

    @Override
    public boolean validateTree() {
        boolean isValid = this.id.validateTree() && this.function_return.validateTree();

        // Duplicate function check
        if(declared_functions.contains(this.id.toString())) {
            new SemanticError("Duplicate Function: " + this.id.toString())
                .print(Jott_Node.filename, super.linenum);
            return false; // Ignores rest of checks in case of duplicate function
        }
            
        // Adding this function to function id map
        Jott_Node.declared_functions.add(this.id.toString(), this);
        Jott_Node.current_function_ID.push(this.id.toString());
        Jott_Node.function_scope.put(current_function_ID.peek(), new IdMap());

        isValid &= this.func_def_params.validateTree();

        // Checking for valid parameters before validating body (in case of undefined parameters)
        if(isValid) { 
            isValid &= this.f_body.validateTree();
            // Function body return validation
            if(f_body.getType() != function_return.getType()) {
                new SemanticError("Function Returns: '" + f_body.getType() + "', Expected: " + "'"+function_return.getType()+"'")
                    .print(Jott_Node.filename, super.linenum);
                isValid = false;
            }
        }

        // Main function checks
        if(this.id.toString().equals("main")) {
            // Parameter check (main has no params)
            if(!func_def_params.toString().equals("")) {
                new SemanticError("Invalid {main} definition: main expects no parameters")
                    .print(Jott_Node.filename, super.linenum);
                isValid = false;
            }

            // Return check (main must return Void)
            if(function_return.getType() != ReturnType.Void) {
                new SemanticError("Invalid {main} return type: '" + function_return.getType() + "', expected: 'Void'")
                    .print(Jott_Node.filename, super.linenum);
                isValid = false;
            }
        }

        // Forced current func pop off the call stack
        Jott_Node.current_function_ID.pop();

        return isValid;
    }

    @Override
    public ReturnType getType() {
        return this.function_return.getType();
    }
}

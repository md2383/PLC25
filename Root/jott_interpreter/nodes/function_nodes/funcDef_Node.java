package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

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
     * @param id                an ID node referencing the function
     * @param func_def_params   a funcDefParams node referencing the (list of) 
     *                          parameters for the function
     * @param function_return   a functionReturn node referencing the return type
     * @param f_body            a funcBody node referencing the body of the function
     */
    private funcDef_Node(id_Node id, funcDefParams_Node func_def_params, funcReturn_Node function_return, funcBody_Node f_body) {
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
        
        return new funcDef_Node(id, func_def_params, function_return, f_body);
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
        boolean isValid = this.id.validateTree() && 
                          this.func_def_params.validateTree() &&
                          this.function_return.validateTree() &&
                          this.f_body.validateTree();
                          
        // TODO: validate body return type
        // TODO: validate id not in id HashMap

        if(this.id.toString().equals("main")) {
            if(!func_def_params.toString().equals("")) {
                new SemanticError("Invalid main definition: main expects no parameters")
                    .print(null, this.linenum);
                isValid = false;
            }

            if(function_return.getType() != ReturnType.Void) {
                new SemanticError("Invalid main return type: expected 'Void'")
                    .print(null, this.linenum);
                isValid = false;
            }
        }

        return isValid;
    }
}

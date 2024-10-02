package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

public class functionCall_Node extends Jott_Node{

    private id_Node id;
    private params_Node params;

    private functionCall_Node(id_Node ID, params_Node Parameters) {
        this.id = ID;
        this.params = Parameters;
    }

    public static functionCall_Node parseFunctionCallNode(ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 4) { throw new SyntaxError("Unexpected EOF"); }
        if(tokens.get(0).getTokenType() != TokenType.FC_HEADER) { throw new SyntaxError("Token type not FC_HEADER"); }
        tokens.remove(0);

        id_Node tempID = id_Node.parseIdNode(tokens);

        if(tokens.get(0).getTokenType() != TokenType.L_BRACKET) { throw new SyntaxError("Invalid Token"); }
        tokens.remove(0);

        params_Node tempParameters = params_Node.parseParamsNode(tokens);
        
        if(tokens.get(0).getTokenType() != TokenType.R_BRACKET) { throw new SyntaxError("Invalid Token"); }
        tokens.remove(0);

        return new functionCall_Node(tempID, tempParameters);
    }

    @Override
    public String convertToJott() {
        return "::" + this.id.convertToJott() + "[" + this.params.convertToJott() + "]";
    }
}

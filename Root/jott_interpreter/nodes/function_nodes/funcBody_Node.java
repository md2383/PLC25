package jott_interpreter.nodes.function_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.grammar_nodes.body_Node;
import jott_interpreter.nodes.grammar_nodes.varDec_Node;
import provided.*;

public class funcBody_Node extends Jott_Node{

    private final varDec_Node[] vars;
    private final body_Node body;

    private funcBody_Node(varDec_Node[] variableDeclarations, body_Node body) {
        this.vars = variableDeclarations;
        this.body = body;
    }
    
    public static funcBody_Node parseFuncBodyNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }

        ArrayList<varDec_Node> tempVars = new ArrayList<>();

        while(  tokens.get(0).getToken().equals("Double")   ||
                tokens.get(0).getToken().equals("Integer")  ||
                tokens.get(0).getToken().equals("String")   ||
                tokens.get(0).getToken().equals("Boolean")  ) {
            tempVars.add(varDec_Node.parseVarDecNode(tokens));
            if (tokens.size() < 1){ throw new SyntaxError("Unexpected EOF"); }
        }

        body_Node body = body_Node.parseBodyNode(tokens);

        return new funcBody_Node(tempVars.toArray(new varDec_Node[tempVars.size()]), body);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        for(varDec_Node var : this.vars) { str.append(var.convertToJott()); }
        str.append(body.convertToJott());
        return str.toString();
    }

    @Override
    public boolean validateTree() {
        boolean isValid = true;

        for (varDec_Node varDec : vars) {
            isValid &= varDec.validateTree();
        }

        isValid &= this.body.validateTree();

        return isValid;
    }

    @Override
    public ReturnType getType() {
        return this.body.getType();
    }
}

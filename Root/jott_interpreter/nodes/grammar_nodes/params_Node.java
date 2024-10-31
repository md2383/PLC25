package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class params_Node extends Jott_Node {
    private final expr_Node firstNode;
    private final ArrayList<params_t_Node> followingNodes;

    private params_Node() {
        this.firstNode = null;
        this.followingNodes = null;
    }

    private params_Node(expr_Node first) {
        this.firstNode = first;
        this.followingNodes = null;
    }

    private params_Node(expr_Node first, ArrayList<params_t_Node> following) {
        this.firstNode = first;
        this.followingNodes = following;
    }

    public static params_Node parseParamsNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        expr_Node tempExpr;
        ArrayList<params_t_Node> tempArr = new ArrayList<>();

        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            tempExpr = expr_Node.parseExprNode(tokens);
            if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            while (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
                tempArr.add(params_t_Node.parseParamsTNode(tokens));
                if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            }
            
            if (tempArr.isEmpty()) { return new params_Node(tempExpr); } 
            else { return new params_Node(tempExpr, tempArr); }
        } else { return new params_Node(); }
    }

    @Override
    public String convertToJott() {
        if (this.firstNode == null) {
            return "";
        } else {
            if (this.followingNodes == null) {
                return this.firstNode.convertToJott();
            } else {
                String temp = this.firstNode.convertToJott();
                for (params_t_Node node : followingNodes) {
                    temp = temp + node.convertToJott();
                }
                return temp;
            }
        }
    }

    @Override
    public boolean validateTree() {
        boolean valid = true;
        for (params_t_Node node : followingNodes) {
            if (!node.validateTree()) {
                valid = false;
            }
        }
        return firstNode.validateTree() && valid;
    }
}

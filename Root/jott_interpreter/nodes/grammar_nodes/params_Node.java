package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.SemanticError;
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
        boolean valid = firstNode.validateTree();

        // list of declared params needed for the function
        Jott_Node[] orderedParamNodes = Jott_Node.function_scope
                .get(current_function_ID.peek()).getOrderedDynamicNodes();
        
        if(followingNodes == null) {
            if(orderedParamNodes.length != 0) {
                new SemanticError("Invalid number of paramaters, expected: " + orderedParamNodes.length)
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            }
        } else {
            if(orderedParamNodes.length != followingNodes.size() + 1) {
                new SemanticError("Invalid number of paramaters, expected: " + orderedParamNodes.length)
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            } else {
                if(firstNode.getType() != orderedParamNodes[0].getType()) {
                    new SemanticError("Invalid Parameter type, expected: " + "") // TODO
                        .print(Jott_Node.filename, super.linenum);
                    valid = false;
                }
        
                for (int i = 0; i < followingNodes.size(); i++) {
                    valid &= followingNodes.get(i).validateTree();
                    if(followingNodes.get(i).getType() != orderedParamNodes[i].getType()) {
                        new SemanticError("Invalid Parameter type, expected: " + "") // TODO
                            .print(Jott_Node.filename, super.linenum);
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }
}

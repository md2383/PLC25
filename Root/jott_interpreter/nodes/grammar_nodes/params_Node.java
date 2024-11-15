package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import provided.*;

public class params_Node extends Jott_Node {
    private final expr_Node firstNode;
    private final ArrayList<params_t_Node> followingNodes;

    private params_Node(int line_number) {
        super(line_number);
        this.firstNode = null;
        this.followingNodes = null;
    }

    private params_Node(int line_number, expr_Node first) {
        super(line_number);
        this.firstNode = first;
        this.followingNodes = null;
    }

    private params_Node(int line_number, expr_Node first, ArrayList<params_t_Node> following) {
        super(line_number);
        this.firstNode = first;
        this.followingNodes = following;
    }

    public static params_Node parseParamsNode(final ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        int lineNum = tokens.get(0).getLineNum();
        expr_Node tempExpr;
        ArrayList<params_t_Node> tempArr = new ArrayList<>();

        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
            tempExpr = expr_Node.parseExprNode(tokens);
            if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            while (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {
                tempArr.add(params_t_Node.parseParamsTNode(tokens));
                if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }
            }

            if (tempArr.isEmpty()) { return new params_Node(lineNum, tempExpr); } 
            else { return new params_Node(lineNum, tempExpr, tempArr); }
        } else { return new params_Node(lineNum); }
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
        // list of declared params needed for the function
        Jott_Node[] orderedParamNodes = Jott_Node.function_scope
            .get(Jott_Node.current_function_ID.pop()).getOrderedDynamicNodes();
        // moved pop() from function call node to allow variables from previous scope into params

        // Empty parameter check
        if (this.firstNode == null) {
            if (orderedParamNodes.length != 0) {
                new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length)
                    .print(Jott_Node.filename, super.linenum);
                return false;
            } else { return true; }
        }

        boolean valid = firstNode.validateTree();

        if (followingNodes == null) {
            if (orderedParamNodes.length != 1) {
                new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length + 1)
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            }
        } else if (orderedParamNodes.length != followingNodes.size() + 1) {
            new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length + 1)
                .print(Jott_Node.filename, super.linenum);
            valid = false;
        }

        if (!valid) { return false; } // forced early function exit

        // Any value can by typecast to a string
        if(orderedParamNodes[0].getType() != ReturnType.String) {
            if (firstNode.getType() != orderedParamNodes[0].getType()) {
                new SemanticError(
                    "Invalid Parameter type: " + 
                    firstNode.getType() + 
                    ", expected: " + 
                    orderedParamNodes[0].getType()
                ).print(Jott_Node.filename, super.linenum);
                valid = false;
            }
        }

        if (followingNodes != null) {
            for (int i = 0; i < followingNodes.size(); i++) {
                valid &= followingNodes.get(i).validateTree();
                // Any value can by typecast to a string
                if(orderedParamNodes[i+1].getType() != ReturnType.String) {
                    if (followingNodes.get(i).getType() != orderedParamNodes[i+1].getType()) {
                        new SemanticError(
                            "Invalid Parameter type: " + 
                            followingNodes.get(i).getType() + 
                            ", expected: " + 
                            orderedParamNodes[i+1].getType()
                        ).print(Jott_Node.filename, super.linenum);
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }
}

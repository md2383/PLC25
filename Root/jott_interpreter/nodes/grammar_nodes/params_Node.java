package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

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
        String function_id = Jott_Node.current_function_ID.pop();
        // list of declared params needed for the function
        Jott_Node[] orderedParamNodes = Jott_Node.function_scope
            .get(function_id).getOrderedDynamicNodes();
        // moved pop() from function call node to allow variables from previous scope into params

        // Empty parameter check
        if (this.firstNode == null) {
            if (orderedParamNodes.length != 0) {
                new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length, super.linenum)
                    .print(Jott_Node.filename);
                return false;
            } else { return true; }
        }

        boolean valid = firstNode.validateTree();

        if (followingNodes == null) {
            if (orderedParamNodes.length != 1) {
                new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length + 1, super.linenum)
                    .print(Jott_Node.filename);
                valid = false;
            }
        } else if (orderedParamNodes.length != followingNodes.size() + 1) {
            new SemanticError("Invalid number of parameters, expected: " + orderedParamNodes.length + 1, super.linenum)
                .print(Jott_Node.filename);
            valid = false;
        }

        if (!valid) { return false; } // forced early function exit

        // Only the print function can typecast it's input parameter
        if(!function_id.equals("print")) {
            if (firstNode.getType() != orderedParamNodes[0].getType()) {
                new SemanticError(
                    "Invalid Parameter type: " + 
                    firstNode.getType() + 
                    ", expected: " + 
                    orderedParamNodes[0].getType(),
                    super.linenum
                ).print(Jott_Node.filename);
                valid = false;
            }
        }

        if (followingNodes != null) {
            for (int i = 0; i < followingNodes.size(); i++) {
                valid &= followingNodes.get(i).validateTree();
                // Only the print function can typecast it's input parameter
                if(!function_id.equals("print")) {
                    if (followingNodes.get(i).getType() != orderedParamNodes[i+1].getType()) {
                        new SemanticError(
                            "Invalid Parameter type: " + 
                            followingNodes.get(i).getType() + 
                            ", expected: " + 
                            orderedParamNodes[i+1].getType(), 
                            super.linenum
                        ).print(Jott_Node.filename);
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    @Override
    public void execute() throws SemanticError {
        // Early function exit given no parameters
        if(this.firstNode == null) { return; }

        String function_id = Jott_Node.current_function_ID.pop();
        LinkedHashMap<String, Jott_Node> param_map = Jott_Node.function_scope
            .get(function_id).getDynamicVars();
        // temporary call stack pop, re-introduce at end of function

        this.firstNode.execute();
        Queue<expr_Node> expressions = new LinkedList<>();
        expressions.add(firstNode);
        
        Object temp_param;
        if(this.followingNodes != null) {
            for(params_t_Node param : this.followingNodes) { 
                param.execute(); 
                temp_param = param.getValue();
                // getValue here should get entire expression node, not just the value
                assert (temp_param instanceof expr_Node);
                expressions.add((expr_Node)(temp_param));
            }
        }

        assert (expressions.size() == param_map.size());
        param_map.forEach((String var_id, @SuppressWarnings("unused") Jott_Node var_node) -> 
            Jott_Node.function_scope.get(function_id).assign_var(var_id, expressions.poll())
        );

        Jott_Node.current_function_ID.push(function_id); // callstack reversion
    }
}

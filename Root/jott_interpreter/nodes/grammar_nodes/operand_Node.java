package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.IdMap;
import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.function_nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

public class operand_Node extends Jott_Node{
    private Jott_Node child;
    private boolean neg;
    private boolean isIdNode;

    public operand_Node(Jott_Node node, boolean negative, boolean idNode, int line_number) {
        super(line_number);
        this.child = node;
        this.neg = negative;
        this.isIdNode = idNode;
    }

    public static operand_Node parseOperandNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        Jott_Node temp;
        boolean is_id = false;
        boolean neg_num = false;
        int lineNum = tokens.get(0).getLineNum();

        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            temp = id_Node.parseIdNode(tokens);
            is_id = true;
        } else if (tokens.get(0).getTokenType() == TokenType.NUMBER) {
            temp = number_Node.parseNumberNode(tokens);
        } else if (tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            temp = funcCall_Node.parseFunctionCallNode(tokens);
        } else if (tokens.get(0).getTokenType() == TokenType.MATH_OP) {
            if (tokens.get(0).getToken().equals("-")) {
                tokens.remove(0);
                temp = number_Node.parseNumberNode(tokens);
                neg_num = true;
            } else {
                throw new SyntaxError("Invalid MATH_OP operator \"" + tokens.get(0).getToken() + "\", expected \"-\"");
            }
        } else {
            throw new SyntaxError("Invalid token type");
        }
        return new operand_Node(temp, neg_num, is_id, lineNum);
    }

    @Override
    public String convertToJott() {
        if (this.neg) {
            return "-" + this.child.convertToJott();
        } else {
            return this.child.convertToJott();
        }
    }

    @Override
    public boolean validateTree() {
        boolean valid = child.validateTree();
        if(!valid) { return false; } // forced early function exit (invalid child node)

        IdMap scope = Jott_Node.function_scope.get(current_function_ID.peek());
        // Preferring flags over instanceof
        if(this.isIdNode) {
            if(scope.contains(this.child.toString())) {
                // The variable needs to be not defined yet to error
                if( !(
                    scope.isDynamic(this.child.toString()) || 
                    scope.isDefined(this.child.toString())
                    ) 
                ) {
                    new SemanticError("Variable id: {" + this.child.toString() + "} was declared but never defined")
                        .print(Jott_Node.filename, super.linenum);
                    valid = false;
                }
            } else {
                new SemanticError("Variable id: {" + this.child.toString() + "} not declared")
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            }
        }

        return valid;
    }

    @Override
    public void execute() {
        this.child.execute();
    }

    @Override
    public ReturnType getType() {
        return child.getType();
    }

    @Override
    public Object getValue() {
        if(this.neg) {
            assert (this.child.getType() == ReturnType.Integer || this.child.getType() == ReturnType.Double);
            return -((Double)(this.child.getValue()));
        } else {
            return this.child.getValue();
        }
    }
}

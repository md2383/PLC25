package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.ReturnType;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.function_nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

public class operand_Node extends Jott_Node{
    private Jott_Node child;
    private boolean neg;

    public operand_Node(Jott_Node node) {
        this.child = node;
        this.neg = false;
    }

    public operand_Node(Jott_Node node, boolean neg) {
        this.child = node;
        this.neg = neg;
    }

    public static operand_Node parseOperandNode(ArrayList<Token> tokens) throws SyntaxError {
        if (tokens.size() < 1) {
            throw new SyntaxError("Unexpected EOF");
        }
        Jott_Node temp;
        if (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD) {
            temp = id_Node.parseIdNode(tokens);
        } else if (tokens.get(0).getTokenType() == TokenType.NUMBER) {
            temp = number_Node.parseNumberNode(tokens);
        } else if (tokens.get(0).getTokenType() == TokenType.FC_HEADER) {
            temp = funcCall_Node.parseFunctionCallNode(tokens);
        } else if (tokens.get(0).getTokenType() == TokenType.MATH_OP) {
            if (tokens.get(0).getToken().equals("-")) {
                tokens.remove(0);
                temp = number_Node.parseNumberNode(tokens);
                return new operand_Node(temp, true);
            } else {
                throw new SyntaxError("Invalid MATH_OP operator \"" + tokens.get(0).getToken() + "\", expected \"-\"");
            }
        } else {
            throw new SyntaxError("Invalid token type");
        }
        return new operand_Node(temp);
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
        return child.validateTree();
    }

    @Override
    public ReturnType getType() {
        return child.getType();
    }
}

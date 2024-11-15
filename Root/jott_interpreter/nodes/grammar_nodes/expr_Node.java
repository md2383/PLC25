package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;
import jott_interpreter.ReturnType;
import jott_interpreter.SemanticError;
import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.*;
import jott_interpreter.nodes.token_nodes.*;
import provided.*;

/**
 * <h>
 *  An expression containing some combination of {@link Jott_Node} 
 *  confined to the parse grammar.
 * 
 * <p>
 *  < operand >                          |
 *  < operand ><( relop )>< operand >    |
 *  < operand ><( mathop )>< operand >   |
 *  <( string_literal )>                 |
 *  < bool >
 * 
 * <p>
 *  The logic for determining node type is built into the 
 *  {@link #parseExprNode} function.
 */
public class expr_Node extends Jott_Node{

    /** An array node representation of the expression. (Size 1 or 3) */
    private final Jott_Node[] expr;

    /**
     * Private Constructor 
     * (validation of the node done in {@link #parseExprNode})
     * @param Expression -  array of abstract {@link Jott_Node} nodes decided 
     *                      by the parse grammar
     */
    private expr_Node(Jott_Node[] Expression, int line_number) {
        super(line_number);
        this.expr = Expression;
    }

    /**
     * Static parse method returning an {@link expr_Node} for the parse tree.
     * 
     * @param tokens -  the list of tokens being parsed into a parse tree
     * @return  An expression built and validated according to the 
     *          parse tree grammar
     * @throws SyntaxError  {@code Unexpected EOF}: no token to parse
     * @implNote    The token(s) in the input array list of {@code Token} 
     *              objects will be removed from the list given validation 
     *              success.
     * @see {@link Token} 
     * @see {@link TokenType}
     */
    public static expr_Node parseExprNode(final ArrayList<Token> tokens) throws SyntaxError {
        if(tokens.size() < 1) { throw new SyntaxError("Unexpected EOF"); }

        int lineNum = tokens.get(0).getLineNum();
        Jott_Node[] Expression = new Jott_Node[1];

        // <( string_literal )>
        if(tokens.get(0).getTokenType() == TokenType.STRING) {
            Expression[0] = stringLiteral_Node.parseStringLiteralNode(tokens);

        // < bool >
        } else if(tokens.get(0).getTokenType() == TokenType.ID_KEYWORD &&
                // checking the token is a boolean
                ("True".equals(tokens.get(0).getToken()) ||
                "False".equals(tokens.get(0).getToken())) ) {
            Expression[0] = bool_Node.parseBoolNode(tokens);

        // < operand >
        } else {
            Jott_Node temp = operand_Node.parseOperandNode(tokens);

            // Checking for an operation 
            // < operand ><( mathop | relop )>< operand >
            if(tokens.size() > 1) {
                TokenType operationType = tokens.get(0).getTokenType();
                if(operationType == TokenType.REL_OP || operationType == TokenType.MATH_OP) {
                    Expression = new Jott_Node[3];

                    Jott_Node operation;
                    if(operationType == TokenType.REL_OP) {
                        operation = relop_Node.parseRelopNode(tokens);
                    } else {
                        operation = mathop_Node.parseMathopNode(tokens);
                    }
                    Expression[1] = operation;

                    // Implicit EOF check
                    Expression[2] = operand_Node.parseOperandNode(tokens);
                }
            }
            
            // Expression can be size 1 or 3
            Expression[0] = temp;
        }
        
        return new expr_Node(Expression, lineNum);
    }

    @Override
    public String convertToJott() {
        StringBuilder str = new StringBuilder();
        for(Jott_Node node : this.expr) { str.append(" ").append(node.convertToJott()); }
        return str.substring(1); // removing first space
    }

    @Override
    public boolean validateTree() {
        boolean valid = true;
        if (this.expr.length == 3) {
            for (Jott_Node node : this.expr) { valid &= node.validateTree(); }
            if(!valid) { return false; } // forced early function exit
            // Math-ops and Rel-ops can only support Ints or Doubles. 
            // Syntax only checks for operands.
            if(this.expr[0].getType() != ReturnType.Integer && this.expr[0].getType() != ReturnType.Double) {
                new SemanticError("Invalid types in expression: must be double our int.")
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            } else if (this.expr[0].getType() != this.expr[2].getType()) {
                new SemanticError("Unmatched types in expression")
                    .print(Jott_Node.filename, super.linenum);
                valid = false;
            }
        } else {
            valid = this.expr[0].validateTree();
        }
        return valid;
    }

    @Override
    public ReturnType getType() {
        // Rel-ops are booleans working against ints, 
        // and math-ops have to be integers or doubles.
        if(this.expr.length == 3) {
            if(this.expr[1].getType() == ReturnType.Integer) {
                if(
                    (this.expr[0].getType() == ReturnType.Integer) && 
                    (this.expr[2].getType() == ReturnType.Integer)
                ) { return ReturnType.Integer; } 
                else { return ReturnType.Double; }
            } else { return ReturnType.Boolean; }
        } else {
            return this.expr[0].getType();
        }
    }
}

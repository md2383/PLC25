package jott_interpreter.nodes.grammar_nodes;

import java.util.ArrayList;

import jott_interpreter.*;
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

    // Used to avoid unnecessary calls through the parse tree:
    /** The type of this expression's value. */
    private ReturnType type = null;
    /** The value of this expression. */
    private Object value = null;

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
                new SemanticError("Invalid types in expression: must be double our int.", super.linenum)
                    .print(Jott_Node.filename);
                valid = false;
            } else if (this.expr[0].getType() != this.expr[2].getType()) {
                new SemanticError("Unmatched types in expression", super.linenum)
                    .print(Jott_Node.filename);
                valid = false;
            } else if(
                (this.expr[1].getType() == ReturnType.Integer) &&
                (this.expr[1].toString().equals("/"))
            ) {
                // expr[2] needs to be a number, variables or function calls are dynamic
                if(this.expr[2].toString().chars().allMatch( 
                        (c) -> ((c == '-') || (c == '.') || (c == '0'))
                    )
                ) {
                    new SemanticError("Division by zero", super.linenum)
                        .print(Jott_Node.filename);
                    valid = false;
                }
            }
        } else {
            valid = this.expr[0].validateTree();
        }
        return valid;
    }

    /**
     * <h>
     * Performs arithmetic and relational operations on two operands, 
     * {@code x} and {@code y}, based on the specified operator.
     * </h>
     * <p>
     * The result of the operation is stored in the {@code value} field of the class. 
     * The method supports mathematical operations (addition, subtraction, multiplication, division) 
     * and relational operations (greater-than, less-than, equality, etc.).
     * </p>
     * 
     * @param x         - The first operand for the operation
     * @param operator  - The operator that determines the operation to be performed. Valid operators are:
     *              <p> arithmetic: {@code "+"}, {@code "-"}, {@code "*"}, {@code "/"} </p>
     *              <p> relational: {@code ">"}, {@code ">="}, {@code "<"}, {@code "<="}, {@code "=="} </p>
     * @param y         - The second operand for the operation
     * @throws SemanticError Runtime Exception: Division by Zero
     */
    private void operate(double x, String operator, double y) throws SemanticError{
        switch (operator) {
            //! MATH OPERATIONS
            case "/" :
                // Runtime exception: Division by zero
                //  ( Validation catches static values and variables, 
                //    runtime catches dynamic variables and function calls )
                if(y == 0) { throw new SemanticError("Division by zero", super.linenum); }
                this.value = Double.valueOf(x / y);
                if(this.getType() == ReturnType.Integer) {
                    // Truncating value if it's an Integer (Truncation needed for int division)
                    this.value = Integer.valueOf( ((Double)(this.value)).intValue() );
                    // Reverting to double for any further calculations
                    this.value = Double.valueOf( ((Integer)(this.value)).doubleValue() );
                }
                break;
            case "*" :
                this.value = Double.valueOf(x * y);
                break;
            case "+" :
                this.value = Double.valueOf(x + y);
                break;
            case "-" :
                this.value = Double.valueOf(x - y);
                break;

            //! RELATIVE OPERATIONS
            case ">" :
                this.value = Boolean.valueOf(x > y);
                break;
            case ">=" :
                this.value = Boolean.valueOf(x >= y);
                break;
            case "<" :
                this.value = Boolean.valueOf(x < y);
                break;
            case "<=" :
                this.value = Boolean.valueOf(x <= y);
                break;
            case "==" :
                this.value = Boolean.valueOf(x == y);
                break;

            // Invalid Operation
            default :
                assert (false);
                break;
        }
    }

    @Override
    public void execute() throws SemanticError{
        this.getType(); // IMPORTANT: edge case type assignment to this.type
        if(this.expr.length == 3) {

            this.expr[0].execute();
            this.expr[1].execute();
            this.expr[2].execute();

            double operand1 = (Double)(this.expr[0].getValue());
            String operator = this.expr[1].toString();
            double operand2 = (Double)(this.expr[2].getValue());

            operate(operand1, operator, operand2);
        } else {
            this.expr[0].execute();
            this.value = this.expr[0].getValue();
        }
    }

    @Override
    public ReturnType getType() {
        // Rel-ops are booleans working against numbers, 
        // and math-ops have to be integers or doubles.
        if(type == null) {
            if(this.expr.length == 3) {
                if(this.expr[1].getType() == ReturnType.Integer) {
                    if(
                        (this.expr[0].getType() == ReturnType.Integer) && 
                        (this.expr[2].getType() == ReturnType.Integer)
                    ) { this.type = ReturnType.Integer; } 
                    else { this.type = ReturnType.Double; }
                } else { this.type = ReturnType.Boolean; }
            } else {
                this.type = this.expr[0].getType();
            }
        }
        return this.type;
    }

    @Override
    public Object getValue() {
        assert (this.value != null);
        return this.value;
    }
}

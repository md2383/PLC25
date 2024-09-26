package jott_interpreter;

/**
 * Custom Syntax Error Class
 * 
 * made for use by the tokenizer to simplify syntax errors and force exit
 * the while loop upon an error
 */
public class SyntaxError extends Exception {
    public SyntaxError(String str) {
        super(str);
    }
}

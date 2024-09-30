package jott_interpreter;

/**
 * Custom Syntax Error Class
 * 
 * Made for use by the tokenizer and parser to simplify syntax errors 
 * exit the program with an Exception.
 */
public class SyntaxError extends Exception {

    /**
     * Constructs a new Exception with the specified detail message
     * 
     * @param message   - the detail message
     */
    public SyntaxError(String message) {
        super(message);
    }

    /**
     * Function to print a Syntax Error in the specified format:
     * 
     * Syntax Error
     * {Error Message}
     * {filename}:{linenum}
     * 
     * @param file  - the filename where the Exception occurred
     * @param line  - the linenumber where the Exception occurred in file
     */
    public void print(String file, int line) {
      System.err.println("Syntax Error");
      System.err.println(this.getMessage());
      System.err.println(file + ":" + line);
    }
}

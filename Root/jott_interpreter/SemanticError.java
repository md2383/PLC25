package jott_interpreter;

public class SemanticError extends Exception{
    final int line;

    public SemanticError(String message, int line_number) {
        super(message);
        this.line = line_number;
    }
    
    public void print(String file) {
        System.err.println("Semantic Error:");
        System.err.println(this.getMessage());
        System.err.println(file + ":" + this.line);
    }
}

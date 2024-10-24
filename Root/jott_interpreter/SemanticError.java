package jott_interpreter;

public class SemanticError extends Exception{

    public SemanticError(String message) {
        super(message);
    }
    
    public void print(String file, int line) {
        System.err.println("Semantic Error:");
        System.err.println(this.getMessage());
        System.err.println(file + ":" + line);
    }
}

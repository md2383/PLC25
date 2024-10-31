package jott_interpreter;

import nodes.*;
import provided.*;
import interpreter;
import ReturnType;
import SemanticError;
import SyntaxError;

public class Jott {
    
    // Tokenize
    // Parse
    // Validate
    // Evaluate
    public static void main(String[] args) {

        // Get the filename from the command line arguments
        try{
            String filename = args[0];
        } catch (Exception e) {
            System.err.println("Error finding file: " + e.getMessage());
            return;
        }

        // Tokenize the file
        try{
            ArrayList<Token> tokens = Interpreter.tokenize(filename);
        } catch (Exception e) {
            System.err.println("Error Tokenizing File: " + e.getMessage());
            return;
        }

        // Parse the tokens into a root node of the JottTree
        try{
            JottTree root = JottParser.parse(tokens);
        } catch (Exception e) {
            System.err.println("Error Parsing Tokens: " + e.getMessage());
            return;
        }

        // Validate the JottTree
        try {
            root.validate();
        } catch (Exception e) {
            System.err.println("Error Validating JottTree: " + e.getMessage());
            return;
        }

        // Evaluate the JottTree
        try {
            ReturnType result = root.evaluate();
        } catch (Exception e) {
            System.err.println("Error Evaluating JottTree: " + e.getMessage());
            return;
        }

    }
}
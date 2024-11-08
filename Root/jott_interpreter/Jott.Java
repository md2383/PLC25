package jott_interpreter;

import java.util.ArrayList;
import provided.*;

public class Jott {
    
    public static void main(String[] args) {

        // TODO:
        // Tokenize
        // Parse
        // Validate
        // Evalluate
        String filename;
        ArrayList<Token> tokens;
        JottTree root;

        // Get the filename from the command line arguments
        try{
            filename = args[0];
        } catch (Exception e) {
            System.err.println("Error getting filename: " + e.getMessage());
            return;
        }

        // Tokenize the file
        try{
            tokens = Interpreter.tokenize(filename);
        } catch (Exception e) {
            System.err.println("Error Tokenizing File: " + e.getMessage());
            return;
        }

        // Parse the tokens into a root node of the JottTree
        try{
            root = JottParser.parse(tokens);
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
            root.evaluate();
        } catch (Exception e) {
            System.err.println("Error Evaluating JottTree: " + e.getMessage());
            return;
        }


    }
}
package main;
import java.util.ArrayList;

import jott_interpreter.Interpreter;
import provided.JottParser;
import provided.JottTree;
import provided.Token;

public class Jott {
    
    public static void main(String[] args) {
        String filename;
        ArrayList<Token> tokens;
        JottTree root = null;
        boolean valid_semantics = false;

        if(args.length != 1) {
            System.err.println("Main function in Jott.java requires a filename argument.");
            System.exit(1);
        }

        /** Get the filename from the command line arguments */
        filename = args[0];

        //  PHASE 1
        /** Tokenize the file */
        tokens = Interpreter.tokenize(filename);

        //  PHASE 2
        /** Parse the tokens into a root node of the JottTree */
        try{

            root = JottParser.parse(tokens);

        } catch (Exception e) {
            System.err.println("Error Parsing Tokens: \n");
            e.printStackTrace();
            System.exit(1);
        }

        //! Prematurely End Program: 
        //  Incorrect Parse Grammar
        if(root == null) { return; }

        //  PHASE 3
        /** Validate the ParseTree */
        try {

            valid_semantics = root.validateTree();

        } catch (Exception e) {
            System.err.println("Error Validating JottTree: \n");
            e.printStackTrace();
            System.exit(1);
        }
        
        //! Prematurely End Program: 
        //  Invalid Jott Semantics
        if(!valid_semantics) { return; }

        //  PHASE 4
        /** Evaluate the JottTree */
        try {

            // root.execute(); // commented out until phase 4

        } catch (Exception e) {
            System.err.println("Error Evaluating JottTree: \n");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
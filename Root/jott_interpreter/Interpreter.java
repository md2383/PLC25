package jott_interpreter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import provided.*;

public class Interpreter {

    public Interpreter() {
        // TODO: Implement this constructor
    }

    public static ArrayList<Token> tokenize(String filename) {
        final int EOF = -1; // for use by BufferedReader
        
        // Input Stream Wrapper
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), Charset.forName("UTF-8")))) { 
            int c;
            char character;

            // Main While Loop
            while((c = reader.read()) != EOF) {
                character = (char)(c);

                // TODO: token cases

            }
        } catch (FileNotFoundException fnfE) {
            // Buffered Exception: possible future need
            fnfE.printStackTrace();
            System.exit(1);
        } catch (IOException ioE) {
            // Buffered Exception: possible future need
            ioE.printStackTrace();
            System.exit(1);
        } /* catch(CustomException E) {
            // TODO: Possible route to easily handle token errors
        } */

        return null; // TODO: Implement this method
    }

}
package provided;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
      final int EOF = -1; // for use by BufferedReader

      // Input Stream Wrapper
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          new FileInputStream(filename), Charset.forName("UTF-8")))) {
        int c;
        char character;

        // Main While Loop
        while ((c = reader.read()) != EOF) {
          character = (char) (c);
          
          // TODO Implement Tokenizer Cases
          /*
           * Whitespace: Ignore
           * "#": comment, Ignore until newline
           * ",": comma
           * "]": rBracket
           * "[": lBracket
           * "}": rBrace
           * "{": lBrace
           * "=": go to check equals function
           * "<>": go to check not equals function
           * "/" or "*" or "+" or "-": go to check mathop function
           * ";": semicolon
           * ".": got to check digit and dot function (hasDot set to true)
           * digit: go to check digit and dot function (hasDot set to false)
           * letter: go to check letter function
           * ":": go to check colon function
           * "!": go to check not equals function
           * ": go to check string function
           */
          
        }
      } catch (FileNotFoundException fnfE) {
        // Buffered Exception: possible future need
        fnfE.printStackTrace();
        System.exit(1);
      } catch (IOException ioE) {
        // Buffered Exception: possible future need
        ioE.printStackTrace();
        System.exit(1);
      } /*
         * catch(CustomException E) {
         * // TODO: Possible route to easily handle token errors
         * }
         */

      return null; // TODO: Implement this method
    }
}
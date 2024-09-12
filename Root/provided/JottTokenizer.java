package provided;

import java.io.*;
import java.nio.charset.Charset;
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
      ArrayList<Token> tokens = new ArrayList<Token>();

      // Input Stream Wrapper
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          new FileInputStream(filename), Charset.forName("UTF-8")))) {
        int c;
        char character;

        // Main While Loop
        while ((c = reader.read()) != EOF) {
          character = (char) (c);

          Token token = new Token("-_ERRORTOKEN_-", filename, 0, TokenType.ASSIGN);

          // Whitespaces
          if (Character.isWhitespace(character)) {
            // Ignore
          }

          // Comments
          if (character == '#') {
            // Ignore until newline
            while ((c = reader.read()) != EOF) {
              character = (char) (c);
              if (character == '\n') {
                break;
              }
            }
          }

          // Comma
          if (character == ',') {
            token = new Token(",", filename, 0, TokenType.COMMA);
          }

          // Semicolon
          if (character == ';') {
            token = new Token(";", filename, 0, TokenType.SEMICOLON);
          }


          // TODO Implement Tokenizer Cases
          /*
           * Whitespace: Ignore                                 - Miguel          
           * "#": comment, Ignore until newline                 - Miguel
           * ",": comma                                         - Miguel
           * "]": rBracket                                      - Miguel
           * "[": lBracket                                      - Miguel
           * "}": rBrace                                        - Miguel
           * "{": lBrace                                        - Miguel
           * "=": go to check equals function                   - Miguel
           * "<>": go to check not equals function              - Miguel
           * "/" or "*" or "+" or "-": mathOp                   - Aum
           * ";": semicolon                                     - Neav 
           * ".": got to check digit and dot function (hasDot set to true) - Aum
           * digit: go to check digit and dot function (hasDot set to false) - Aum
           * letter: go to check letter function
           * ":": go to check colon function                    - Neav
           * "!": go to check not equals function
           * ": go to check string function      
           */
          
          // Add token to arraylist
          if (token.getToken().equals("-_ERRORTOKEN_-")) {
            System.out.println("Error Token: " + character);
          } else {
            tokens.add(token);
          }
        }
      } catch (FileNotFoundException fnfE) {
        // Buffered Exception: possible future need
        fnfE.printStackTrace();
        System.exit(1);
      } catch (IOException ioE) {
        // Buffered Exception: possible future need
        ioE.printStackTrace();
        System.exit(1);
      }
      /*
       * catch(CustomException E) {
       * // TODO: Possible route to easily handle token errors
       * }
       */

      return null; // TODO: Implement this method
    }
}
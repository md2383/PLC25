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
      int linenum = 0;

      // Input Stream Wrapper
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(
          new FileInputStream(filename), Charset.forName("UTF-8")))) {
        int c;
        char character;

        // Main While Loop
        while ((c = reader.read()) != EOF) {
          character = (char) (c);

          if (character == '\n') {
            linenum++;
            continue;
          }

          Token token = new Token("-_ERRORTOKEN_-", filename, linenum, TokenType.ASSIGN);

          // Whitespaces
          if (Character.isWhitespace(character)) {
            // Go to next character
            continue;
          }

          // Comments
          if (character == '#') {
            // Ignore until newline
            while ((c = reader.read()) != EOF) {
              character = (char) (c);
              if (character == '\n') {
                linenum++;
                break;
              }
            }
          }

          // Comma
          if (character == ',') {
            token = new Token(",", filename, linenum, TokenType.COMMA);
          }

          // Right Bracket
          if (character == ']') {
            token = new Token("]", filename, linenum, TokenType.R_BRACKET);
          }

          // Left Bracket
          if (character == '[') {
            token = new Token("[", filename, linenum, TokenType.L_BRACKET);
          }

          // Right Brace
          if (character == '}') {
            token = new Token("}", filename, linenum, TokenType.R_BRACE);
          }

          // Left Brace
          if (character == '{') {
            token = new Token("{", filename, linenum, TokenType.L_BRACE);
          }

          // Equals
          if (character == '=') {
            token = new Token("=", filename, linenum, TokenType.ASSIGN);
            // Check for double equals and mark tmhe position in the reader
            reader.mark(1);
            if ((c = reader.read()) != EOF) {
              character = (char) (c);
              if (character == '=') {
                token = new Token("==", filename, linenum, TokenType.REL_OP);
              } else {
                // Go back one character
                reader.reset();
              }
            }
          }

          // Greater Than / Greater Than or Equal To
          if (character == '<') {
            // Check for Less Than or Less Than or Equal To
            reader.mark(1);
            if ((c = reader.read()) != EOF) {
              character = (char) (c);
              if (character == '=') {
                token = new Token("<=", filename, linenum, TokenType.REL_OP);
              } else {
                // Go back one character
                reader.reset();
                token = new Token("<", filename, linenum, TokenType.REL_OP);
              }
            }
          }

          // Less Than / Less Than or Equal To
          if (character == '>') {
            // Check for Less Than or Less Than or Equal To
            reader.mark(1);
            if ((c = reader.read()) != EOF) {
              character = (char) (c);
              if (character == '=') {
                token = new Token(">=", filename, linenum, TokenType.REL_OP);
              } else {
                // Go back one character
                reader.reset();
                token = new Token(">", filename, linenum, TokenType.REL_OP);
              }
            }
          }

          // Semicolon
          if (character == ';') {
            token = new Token(";", filename, linenum, TokenType.SEMICOLON);
          }

          // Division
          if (character == '/') {
            // Division
            token = new Token("/", filename, linenum, TokenType.MATH_OP);
          }
          // Multiplication
          if (character == '*') {
            // Multiplication
            token = new Token("*", filename, linenum, TokenType.MATH_OP);
          }
          // Addition
          if (character == '+') {
            // Addition
            token = new Token("+", filename, linenum, TokenType.MATH_OP);
          }
          // Subtraction
          if (character == '-') {
            // Subtraction
            token = new Token("-", filename, linenum, TokenType.MATH_OP);
          }
          
          if (character == '"') {
            String str = Character.toString(character);
            while ((c = reader.read()) != EOF) {
              character = (char) c;
              if (character != '"') {
                if (Character.isLetterOrDigit(character) || character == ' ') {
                  str = str + Character.toString(character);
                } else {
                  token = new Token("-_ERRORTOKEN_-", filename, linenum, TokenType.ASSIGN);
                  break;
                }
              } else {
                token = new Token(str, filename, linenum, TokenType.STRING);
                break;
              }
              
            }
          }
          // Colon
          if (character == ':') {
            reader.mark(1);
            if ((c = reader.read()) == ':' )
            {
              token = new Token("::", filename, 0, TokenType.FC_HEADER);
            }
            else
            {
              reader.reset();
              token = new Token(":", filename, 0, TokenType.COLON);
            }
          }

          // TODO Implement Tokenizer Cases
          /*
           * Whitespace: Ignore - Miguel
           * "#": comment, Ignore until newline - Miguel
           * ",": comma - Miguel
           * "]": rBracket - Miguel
           * "[": lBracket - Miguel
           * "}": rBrace - Miguel
           * "{": lBrace - Miguel
           * "=": go to check equals function - Miguel
           * "<>": go to check the greater/less than functions - Miguel
           * "/" or "*" or "+" or "-": mathOp - Aum
           * ";": semicolon - Neav
           * ".": got to check digit and dot function (hasDot set to true) - Aum
           * digit: go to check digit and dot function (hasDot set to false) - Aum
           * letter: go to check letter function
           * ":": go to check colon function - Neav
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
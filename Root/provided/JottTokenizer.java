package provided;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * 
 */
public class JottTokenizer {

  /**
   * Custom Syntax Error Class
   * 
   * made for use by the tokenizer to simplify syntax errors and force exit 
   * the while loop upon an error
   */
  private static class SyntaxError extends Exception {
    SyntaxError(String str) {
      super(str);
    }
  }

  /**
   * Takes in a filename and tokenizes that file into Tokens
   * based on the rules of the Jott Language
   * 
   * @param filename the name of the file to tokenize; can be relative or absolute
   *                 path
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
      Token token;

      // Main While Loop
      while ((c = reader.read()) != EOF) {
        character = (char) (c);

        if (character == '\n') {
          linenum++;
          continue;
        }

        token = new Token("-_ERRORTOKEN_-", filename, linenum, TokenType.ASSIGN);

        // Whitespaces
        if (Character.isWhitespace(character)) {
          // Go to next character
          continue;
        }

        // Comments
        if (character == '#') {
          reader.readLine();
          linenum++;
          break;
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
        // Dot
        if( character == '.') {
          String tokenString = String.valueOf(character);
          reader.mark(1);
          if ((c = reader.read()) != EOF) {
            character = (char) (c);
            if (Character.isDigit(character)) {
              tokenString += character;
              while ((c = reader.read()) != EOF) {
                character = (char) (c);
                if (Character.isDigit(character)) {
                  tokenString += character;
                  reader.mark(1);
                } else {
                  reader.reset();
                  break;
                }
              }
              token = new Token(tokenString, filename, linenum, TokenType.NUMBER);
            } else {
              throw new SyntaxError("Invalid token \".\". \".\" expects following digit");
            }
          }
        }

        // Digit
        if (Character.isDigit(character)) {
          String tokenString = String.valueOf(character);
          while ((c = reader.read()) != EOF) {
            character = (char) (c);
            if (Character.isDigit(character)) {
              tokenString += character;
              reader.mark(1);
            } else {
              reader.reset();
              break;
            }
          }
          token = new Token(tokenString, filename, linenum, TokenType.NUMBER);
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

        // String
        if (character == '"') {
          String str = Character.toString(character);
          while ((c = reader.read()) != EOF) {
            character = (char) c;
            if (character != '"') {
              if (Character.isLetterOrDigit(character) || character == ' ') {
                str = str + Character.toString(character);
              } else {
                throw new SyntaxError("Invalid string token at line " + linenum + ": String must only contain letters, digits, or spaces.");
              }
            } else {
              token = new Token(str, filename, 0, TokenType.STRING);
              tokens.add(token);
              break;
            }

          }
        }

        // Colon
        if (character == ':') {
          reader.mark(1);
          if ((c = reader.read()) == ':') {
            token = new Token("::", filename, 0, TokenType.FC_HEADER);
          } else {
            reader.reset();
            token = new Token(":", filename, 0, TokenType.COLON);
          }
        }

        // id, keyword
        if (Character.isLetter(character)) {
          String tokenString = String.valueOf(character);
          reader.mark(1);

          while ((c = reader.read()) != EOF) {
            if (Character.isLetterOrDigit(c)) {
              tokenString += c;
              reader.mark(1);
            } else {
              reader.reset();
              break;
            }
          }

          token = new Token(tokenString, filename, linenum, TokenType.ID_KEYWORD);
        }

        // Not Equals
        if(character == '!') {
          if ((c = reader.read()) == '=') { // doesn't need EOF check, if EOF -> else {Syntax Error}
            token = new Token("!=", filename, linenum, TokenType.REL_OP);
          } else {
            throw new SyntaxError("Invalid token \"!\". \"!\" expects following \"=\"");
          }
        }

        /* NEEDS TO GET DONE */
        // TODO Consolidate if statements to if/else chain to prevent implicit fallthrough and token cancellation
        // TODO Implement Exception throws for following errors
        //    TODO String: Error on EOF
        //    TODO Number: Error on invalid '.'
        // TODO May need to account for carriage returns in newline ('\r')

        /* SUGGESTIONS */
        // TODO Mathops can be reduced to a single if statement
        // TODO '<', '>' can be implemented in a single statement
        // TODO centralize global token string for tokens with more than 1 character

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
         * letter: go to check letter function - Jacob
         * ":": go to check colon function - Neav
         * "!": go to check not equals function - Jacob
         * ": go to check string function - Ian
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
    } catch(SyntaxError E) {
      System.err.println("Syntax Error");
      System.err.println(E.getMessage());
      System.err.println(filename + ":" + linenum);
    }

    return null; // TODO: Implement this method
  }
}
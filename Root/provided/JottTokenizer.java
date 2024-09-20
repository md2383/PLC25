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
   * @exception SyntaxError Exception caught in method; Error output to System.err
   *          Error format: Syntax Error
   *                        {Error Message}
   *                        {filename}:{linenum}
   * @return an ArrayList of Jott Tokens
   */
  public static ArrayList<Token> tokenize(String filename) {
    final int EOF = -1; // for use by BufferedReader

    ArrayList<Token> tokens = new ArrayList<>();
    int linenum = 1;

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
          while((c = reader.read()) != EOF) {
            if((character = (char)(c)) == '\n') { break; }
          }
          linenum++;
          continue;
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

        if(Character.isDigit(character) || character == '.') {
          String tokenString = String.valueOf(character);
          boolean contains_dot = character == '.';
          reader.mark(1);
          while ((c = reader.read()) != EOF) {
            character = (char)(c);
            if(Character.isDigit(c)) { tokenString += character; }
            else if(character == '.') {
              if(contains_dot) {
                throw new SyntaxError("Invalid token \\\".\\\". \\\".\\\" expects following digit");
              } else {
                contains_dot = true;
                tokenString += character;
              }
            } else {
              reader.reset();
              break;
            }
            reader.mark(1);
          }
          if(tokenString.equals(".")) {
            throw new SyntaxError("Invalid token \\\".\\\". \\\".\\\" expects following digit");
          }
          token = new Token(tokenString, filename, linenum, TokenType.NUMBER);
          if(c == EOF) { reader.reset(); }
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
                throw new SyntaxError("Invalid string token. String must contain only letters, digits, or spaces.");
              }
            } else {
              str += character;
              token = new Token(str, filename, linenum, TokenType.STRING);
              break;
            }
          }
          if (token.getToken().equals("-_ERRORTOKEN_-")) {
            throw new SyntaxError("Incomplete string token. String must end with '\"'.");
          }
        }

        // Colon
        if (character == ':') {
          reader.mark(1);
          if ((c = reader.read()) == ':') {
            token = new Token("::", filename, linenum, TokenType.FC_HEADER);
          } else {
            reader.reset();
            token = new Token(":", filename, linenum, TokenType.COLON);
          }
        }

        // id, keyword
        if (Character.isLetter(character)) {
          String tokenString = String.valueOf(character);
          reader.mark(1);

          while ((c = reader.read()) != EOF) {
            character = (char)(c);
            if (Character.isLetterOrDigit(character)) {
              tokenString += character;
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
        //    String: Error on invalid characters (Implemented)
        //    String: Error on EOF (Implemented)
        //    TODO Number: Error on invalid '.'
        //      TODO Dot: needs a syntax error on dot following digits following dot
        //      TODO Digit: needs to account for dots, and syntax errors on invalid dots
        // TODO May need to account for carriage returns in newline ('\r')
        // TODO String: currently double adds tokens to arraylist -> all tokens are added at end of main loop

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
      return null;
    }

    return tokens;
  }
}
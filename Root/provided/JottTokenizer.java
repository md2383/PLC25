package provided;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import jott_interpreter.SyntaxError;

/**
 * Tokenization class containing Jott Tokenizer
 */
public class JottTokenizer {

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

    // Output ArrayList, final to prevent accidental deletion
    final ArrayList<Token> tokens = new ArrayList<>();

    int linenum = 1;

    // Input Stream Wrapper
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream(filename), Charset.forName("UTF-8")))) {
      int c;
      char character;
      String tokenString;
      Token token = null; // Explicit initialization

      //! Main While Loop
      while ((c = reader.read()) != EOF) {
        character = (char) (c);

        /* Whitespaces (and Newline) */ 
        if (Character.isWhitespace(character)) {
          // NOTE: \n counts as whitespace -> implicit fallthrough
          // newline updates linenum
          if (character == '\n') { linenum++; }
          // Ignore Whitespace
          continue;
        }

        /* Comments */ 
        else if (character == '#') {
          reader.readLine();
          linenum++;
          continue;
        }

        /* Comma */ 
        else if (character == ',') {
          token = new Token(",", filename, linenum, TokenType.COMMA);
        }

        /* Right Bracket */ 
        else if (character == ']') {
          token = new Token("]", filename, linenum, TokenType.R_BRACKET);
        }

        /* Left Bracket */ 
        else if (character == '[') {
          token = new Token("[", filename, linenum, TokenType.L_BRACKET);
        }

        /* Right Brace */ 
        else if (character == '}') {
          token = new Token("}", filename, linenum, TokenType.R_BRACE);
        }

        /* Left Brace */ 
        else if (character == '{') {
          token = new Token("{", filename, linenum, TokenType.L_BRACE);
        }

        /* Equals */ 
        else if (character == '=') {
          token = new Token("=", filename, linenum, TokenType.ASSIGN);
          // Check for double equals and mark then position in the reader
          reader.mark(1);
          if ((c = reader.read()) != EOF) {
            character = (char) (c);
            if (character == '=') {
              token = new Token("==", filename, linenum, TokenType.REL_OP);
            } else {
              // Go back one character
              reader.reset();
            }
          } else { reader.reset(); }
        }

        /* Greater Than | Greater Than or Equal To */
        /* Less Than    | Less Than or Equal To */
        else if (character == '<' || character == '>') {
          tokenString = String.valueOf(character);
          reader.mark(1);
          if ((c = reader.read()) == EOF) {
            // Go back one character
            reader.reset();
          } else { // >= and <=
            character = (char)(c);
            if (character == '=') { tokenString += character; } 
            else { reader.reset(); } // Go back one character
          }
          token = new Token(tokenString, filename, linenum, TokenType.REL_OP);
        }

        /* Digits (and Dots) */
        else if(Character.isDigit(character) || character == '.') {
          tokenString = String.valueOf(character);
          boolean contains_dot = (character == '.');

          reader.mark(1);
          while ((c = reader.read()) != EOF) {
            character = (char)(c);
            if(Character.isDigit(c)) { tokenString += character; }
            else if(character == '.') {
              if(contains_dot) {
                throw new SyntaxError("Duplicate token \".\" is invalid");
              } else {
                contains_dot = true;
                tokenString += character;
              }
            } else { // not a digit or a '.'
              reader.reset();
              break;
            }
            reader.mark(1);
          }
          if(c == EOF) { reader.reset(); }

          if(tokenString.equals(".")) {
            throw new SyntaxError("Invalid token \".\". \".\" expects following digit");
          }

          token = new Token(tokenString, filename, linenum, TokenType.NUMBER);
        }
        
        /* Semicolon */
        else if (character == ';') {
          token = new Token(";", filename, linenum, TokenType.SEMICOLON);
        }

        /* Division | Multiplication | Addition | Subtraction */
        else if (
            character == '/' || 
            character == '*' || 
            character == '+' || 
            character == '-'
          ) {
          token = new Token(String.valueOf(character), filename, linenum, TokenType.MATH_OP);
        }

        /* String */ 
        else if (character == '"') {
          tokenString = Character.toString(character);
          while ((c = reader.read()) != EOF) {
            tokenString += (character = (char) c);
            if (character != '"') {
              // If character is NOT a letter, digit, or character; SyntaxError
              if (!(Character.isLetterOrDigit(character) || character == ' ')) {
                throw new SyntaxError("Invalid string token. String must contain only letters, digits, or spaces.");
              } // no else: preemptively added character to tokenString
            } else {
              token = new Token(tokenString, filename, linenum, TokenType.STRING);
              break;
            }
          }
          if (c == EOF) {
            throw new SyntaxError("Incomplete string token. String must end with '\"'.");
          }
        }

        /* Colon */ 
        else if (character == ':') {
          reader.mark(1);
          if ((c = reader.read()) == ':') {
            token = new Token("::", filename, linenum, TokenType.FC_HEADER);
          } else {
            reader.reset();
            token = new Token(":", filename, linenum, TokenType.COLON);
          }
        }

        /* Id | Keyword */ 
        else if (Character.isLetter(character)) {
          tokenString = String.valueOf(character);
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
          if(c == EOF) { reader.reset(); }

          token = new Token(tokenString, filename, linenum, TokenType.ID_KEYWORD);
        }

        /* Not Equals */ 
        else if(character == '!') {
          if ((c = reader.read()) == '=') { // doesn't need EOF check, if EOF -> else {Syntax Error}
            token = new Token("!=", filename, linenum, TokenType.REL_OP);
          } else {
            throw new SyntaxError("Invalid token \"!\". \"!\" expects following \"=\"");
          }
        }

        /* Invalid Token */
        else {
          throw new SyntaxError("Invalid Token: " + character);
        }
        
        tokens.add(token);  // Adds token generated to ArrayList

        token = null;       // Explicit variable reset for debugging
        tokenString = null; // Explicit variable reset for debugging
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
package provided;

import java.util.ArrayList;

import jott_interpreter.SyntaxError;
import jott_interpreter.nodes.grammar_nodes.program_Node;

/**
 * This class is responsible for parsing Jott Tokens
 * into a Jott parse tree.
 */
public class JottParser {

    /**
     * Parses an ArrayList of Jott tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){
      JottTree root;

      try {
        root = program_Node.parseProgramNode(tokens);
      } catch (SyntaxError Error) {
        // Error.printStackTrace(); // Debugging
        if(tokens.size() != 0) {
          Token errorToken = tokens.get(0);
          Error.print(errorToken.getFilename(), errorToken.getLineNum());
        } else {
          Error.print("", 0);
        }
        return null;
      }

      return root;
    }
}

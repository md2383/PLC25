package provided;

import java.io.*;
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
      } /*
         * catch(CustomException E) {
         * // TODO: Possible route to easily handle token errors
         * }
         */

      return null; // TODO: Implement this method
    }
}
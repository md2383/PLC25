package jott_interpreter;

import java.util.ArrayList;

import provided.*;

public class Interpreter {
    public Interpreter() {
        // TODO: Implement this constructor
    }

    public static ArrayList<Token> tokenize(String filename) {
        return JottTokenizer.tokenize(filename);
    }
}
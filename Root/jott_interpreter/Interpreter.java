package jott_interpreter;

import java.util.ArrayList;

import provided.*;

public class Interpreter {
    public static ArrayList<Token> tokenize(String filename) {
        return JottTokenizer.tokenize(filename);
    }
}
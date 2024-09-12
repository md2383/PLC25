package jott_interpreter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import provided.*;

public class Interpreter {

    static JottTokenizer tokenizer = new JottTokenizer();

    public Interpreter() {
        // TODO: Implement this constructor
    }

    public static ArrayList<Token> tokenize(String filename) {
        return tokenizer.tokenize(filename);
    }
}
package jott_interpreter;

import java.util.ArrayList;
import java.util.Scanner;

import provided.*;

public class Interpreter {

    public Interpreter() {
        // TODO: Implement this constructor
    }

    public static ArrayList<Token> tokenize(String filename) {
        // File scanning
        Scanner scanner = new Scanner(filename);
        char c;
        while (scanner.hasNext()) {
            c = scanner.next();
            // Cases
            if (c == ' ') {}
                continue;
            } else if (c == '#') { // Comments (#)
                c = scanner.next();
                if (c == '\n') {
                    continue;
                } else if (c == '#') {
                    c = scanner.next();
                    if (c == '\n') {
                        continue;
                    } else if (c == '#') {
                        boolean end = false;
                        while (!end) {
                            c = scanner.next();
                            if (c == '#') {
                                c = scanner.next();
                                if (c == '#') {
                                    c = scanner.next();
                                    if (c == '#') {
                                        end = true;
                                    }
                                }
                            }
                        }
                    } else {
                        while (c != '\n') {
                            continue;
                        }
                    }
                } else {
                    while (c != '\n') {
                        continue;
                    }
                }
            }
        }

        return null; // TODO: Implement this method
    }

}
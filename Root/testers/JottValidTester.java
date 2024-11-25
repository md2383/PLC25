package testers;

import java.io.File;

import main.Jott;

public class JottValidTester {
    public static void main(String[] args) {
        final File testCaseParent = new File("phase3testcases");
        final File[] testCases = testCaseParent.listFiles();

        for(File testCase : testCases) {
            System.out.println(testCase.getPath() + ":");
            Jott.main(new String[] { testCase.getPath() });
            System.out.println();
        }
    }
}

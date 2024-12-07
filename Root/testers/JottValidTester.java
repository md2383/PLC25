package testers;

import java.io.File;

import main.Jott;

public class JottValidTester {
    private static void printTestCase(File testFile) {
        System.out.println(testFile.getPath() + ":");
        Jott.main(new String[] { testFile.getPath() });
        System.out.println();
    }

    public static void main(String[] args) {
        final File test_folder_1 = new File("tokenizerTestCases");
        final File test_folder_2 = new File("parserTestCases");
        final File test_folder_3 = new File("phase3testcases");
        final File test_folder_4 = new File("phase4testcases");

        final File[] t1_files = test_folder_1.listFiles();
        final File[] t2_files = test_folder_2.listFiles();
        final File[] t3_files = test_folder_3.listFiles();
        final File[] t4_files = test_folder_4.listFiles();

        for(File testCase : t1_files) { printTestCase(testCase); }
        for(File testCase : t2_files) { printTestCase(testCase); }
        for(File testCase : t3_files) { printTestCase(testCase); }
        for(File testCase : t4_files) { printTestCase(testCase); }
    }
}

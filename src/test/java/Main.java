import org.testng.TestNG;

import org.testng.collections.Lists;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("No argument specified!");
            return;
        }

        // Get the path to selected .xml
        //String xmlPath = System.getProperty("user.dir") + "/" + args[0];  // linux-path
        String xmlPath = "C:\\dev\\tests\\xml\\" + args[0];                 // windows-path

        // Run all the tests in selected .xml
        TestNG testng = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add(xmlPath);
        testng.setTestSuites(suites);
        testng.setVerbose(1);
        testng.run();

        System.err.println("Return code " + testng.getStatus());
        if (testng.getStatus() != 0)
            System.exit(testng.getStatus());
    }
}

package com.github.corviv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.annotations.Listeners;
import org.testng.collections.Lists;

import java.util.List;

@Listeners(ListenerLogger.class)
public class Main {

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger("Main");

        //String xmlPath = System.getProperty("user.dir") + "/";  // linux-path
        String xmlPath = "C:\\dev\\tests\\xml\\";                 // windows-path

        if (args.length < 1) {
            logger.error("No argument specified!\n\n Usage : java -jar ui_autotest_winapp.jar <testng_xml>\n Where :\n\t testng_xml: Name of TestNG suite xml-file in \"{}\"", xmlPath);
            return;
        }

        // Run all the tests in selected .xml
        TestNG testng = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add(xmlPath + args[0]);
        testng.setTestSuites(suites);
        testng.run();

        logger.info("Return code {}", testng.getStatus());
        if (testng.getStatus() != 0)
            System.exit(testng.getStatus());
    }
}

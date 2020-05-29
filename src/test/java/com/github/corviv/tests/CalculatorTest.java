package com.github.corviv.tests;

import com.github.corviv.ListenerLogger;
import com.github.corviv.Session;

import com.github.corviv.pages.CalculatorPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenerLogger.class)
public class CalculatorTest {
    CalculatorPage objExample = null;
    Session test_example = null;
    private static final Logger logger = LoggerFactory.getLogger("CalculatorTest");

    CalculatorTest() {
        test_example = new Session();
    }

    @BeforeTest
    public void startDriver() {

        test_example.createSession();
        //test_example.connectSession(CalculatorPage.strAppWindowName);

        objExample = new CalculatorPage(Session.getDriverSession());
    }

    @AfterTest
    public void stop() {
        test_example.stopSession();
    }

    @Test
    public void test() {
        objExample.Addition();
        objExample.Subtraction();
        objExample.Division();
        objExample.Multiplication();
        objExample.Combination();
    }
}

import com.github.corviv.ListenerLogger;
import com.github.corviv.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenerLogger.class)
public class TestCalculator {
    PageCalculator objExample = null;
    Session test_example = null;
    private static final Logger logger = LoggerFactory.getLogger("TestCalculator");

    TestCalculator() {
        test_example = new Session();
    }

    @BeforeTest
    public void startDriver() {
        test_example.createSession();
        //test_example.connectSession(PageCalculator.strAppWindowName);
        objExample = new PageCalculator(Session.getDriverSession());
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

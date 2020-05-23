import com.github.corviv.ListenerLogger;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenerLogger.class)
public class PageCalculator {

    private static WindowsDriver<WindowsElement> session = null;
    private static final Logger logger = LoggerFactory.getLogger("PageCalculator");

    static String strAppWindowName = "Калькулятор";
    
    private static String btnOne = "Один";
    private static String btnSeven = "Семь";
    private static String btnEight = "Восемь";
    private static String btnNine = "Девять";

    private static String btnPlus = "Плюс";
    private static String btnDivine = "Разделить на";
    private static String btnMultiplication = "Умножить на";
    private static String btnSubtraction = "Минус";
    private static String btnEquals = "Равно";
    private static String btnClear = "Очистить";
    private static String lineResultPrefix = "На экране показано";

    public PageCalculator(WindowsDriver<WindowsElement> session) {
        this.session = session;
    }

    public void Clear()
    {
        session.findElementByName(btnClear).click();
        Assert.assertEquals("0", _getCalcResultText());
    }

    public void Addition() {
        Clear();
        session.findElementByName(btnOne).click();
        session.findElementByName(btnPlus).click();
        session.findElementByName(btnSeven).click();
        session.findElementByName(btnEquals).click();
        Assert.assertEquals("8", _getCalcResultText());
    }

    public void Subtraction()
    {
        Clear();
        session.findElementByName(btnNine).click();
        session.findElementByName(btnSubtraction).click();
        session.findElementByName(btnOne).click();
        session.findElementByName(btnEquals).click();
        Assert.assertEquals("8", _getCalcResultText());
    }

    @Test
    public void Division()
    {
        Clear();
        session.findElementByName(btnEight).click();
        session.findElementByName(btnEight).click();
        session.findElementByName(btnDivine).click();
        session.findElementByName(btnOne).click();
        session.findElementByName(btnOne).click();
        session.findElementByName(btnEquals).click();
        Assert.assertEquals("8", _getCalcResultText());
    }

    public void Multiplication()
    {
        Clear();
        session.findElementByName(btnNine).click();
        session.findElementByName(btnMultiplication).click();
        session.findElementByName(btnNine).click();
        session.findElementByName(btnEquals).click();
        Assert.assertEquals("81", _getCalcResultText());
    }

    @Test
    public void Combination()
    {
        Clear();
        session.findElementByName(btnSeven).click();
        session.findElementByName(btnMultiplication).click();
        session.findElementByName(btnNine).click();
        session.findElementByName(btnPlus).click();
        session.findElementByName(btnOne).click();
        session.findElementByName(btnEquals).click();
        session.findElementByName(btnDivine).click();
        session.findElementByName(btnEight).click();
        session.findElementByName(btnEquals).click();
        Assert.assertEquals("8", _getCalcResultText());
    }

    protected String _getCalcResultText() {
        WebElement lineCalcResult = session.findElementByAccessibilityId("CalculatorResults");
        // trim extra text and whitespace off of the display value
        return lineCalcResult.getText().replace(lineResultPrefix, "").trim();
    }
}

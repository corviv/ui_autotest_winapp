import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Driver {

    private static final String driverPath = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
    private static final String driverUrl = "http://127.0.0.1:4723";
    // Set path to testing application
    private String appPath = "C:\\Windows\\System32\\calc.exe";
    private ProcessBuilder processBuilder;
    private Process process;
    WindowsDriver driver;

    // No args
    public void createSession() {
        try {
            processBuilder = new ProcessBuilder(driverPath);
            process = processBuilder.start();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", appPath);

            driver = new WindowsDriver(new URL(driverUrl), capabilities);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // TODO: !

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    // With args
    public void createSession(String appPath, String appArgs) {
        try {
            processBuilder = new ProcessBuilder(driverPath);
            process = processBuilder.start();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", appPath);

            /* TODO: appArgs
            capabilities.setCapability("appArguments", appArgs);
            */

            driver = new WindowsDriver(new URL("http://localhost:9999"), capabilities);

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void connectSession() {
        try {
            processBuilder = new ProcessBuilder(driverPath);
            process = processBuilder.start();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", appPath);
            // TODO: setDebugConnectToRunningApp(true); ?

            driver = new WindowsDriver(new URL("http://localhost:9999"), capabilities);

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void stopSession() {
        try {
            driver.quit();
            process.destroy();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}

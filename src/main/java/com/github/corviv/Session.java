package com.github.corviv;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Listeners(ListenerLogger.class)
public class Session {
    // Default path to WinAppDriver
    private static final String driverPath = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
    // Default WinAppDriver URL
    private static final String driverUrl = "http://127.0.0.1:4723";
    // Default path to testing application
    private static final String def_appPath = "C:\\Windows\\System32\\calc.exe";
    private String def_appArgs = null;
    // Default timeout for implicitly wait (sec)
    private int def_timeout = 3;
    private long startTaskTime = 0;

    private static ProcessBuilder processBuilder = null;
    private static Process process = null;
    private static final Logger logger = LoggerFactory.getLogger("Session");
    static WindowsDriver<WindowsElement> session = null;

    public static WindowsDriver<WindowsElement> getDriverSession() {
        return session;
    }

    public void createSession() {
        createSession(def_appPath, def_appArgs, def_timeout);
    }

    public void createSession(String appPath, String appArgs, int timeout) {
        try {
            //processBuilder = new ProcessBuilder(driverPath);
            //process = processBuilder.start();

            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
            capabilities.setCapability("app", appPath);
            if (appArgs != null)
                capabilities.setCapability("appArguments", appArgs);

            try{
                session = new WindowsDriver<WindowsElement>(new URL(driverUrl), capabilities);
            }
            catch (SessionNotCreatedException e) {
                sleep(1000);
                session = new WindowsDriver<WindowsElement>(new URL(driverUrl), capabilities);
            }

            session.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
            //session.launchApp();

        } catch (IOException | InterruptedException e) {
            logger.error("Catch 'createSession' exception!\n");
            logger.info(e.getMessage());
            throw new RuntimeException("'createSession' exception!");
        }
    }


    public void connectSession(String appName) {
        connectSession(def_appPath, appName, def_timeout);
    }

    public void connectSession(String appPath, String appName, int timeout) {
        try {
            processBuilder = new ProcessBuilder(appPath);
            process = processBuilder.start();

            sleep(2000);

            DesiredCapabilities desktopCapabilities = new DesiredCapabilities();
            desktopCapabilities.setCapability("platformName", "Windows");
            desktopCapabilities.setCapability("deviceName", "WindowsPC");
            desktopCapabilities.setCapability("app", "Root");

            session = new WindowsDriver<WindowsElement>(new URL(driverUrl), desktopCapabilities);

            WebElement BHWebElement = session.findElementByName(appName);
            String WinHandleStr = BHWebElement.getAttribute("NativeWindowHandle");
            int WinHandleInt = Integer.parseInt(WinHandleStr);
            String WinHandleHex = Integer.toHexString(WinHandleInt);

            DesiredCapabilities MACapabilities = new DesiredCapabilities();
            MACapabilities.setCapability("platformName", "Windows");
            MACapabilities.setCapability("deviceName", "WindowsPC");
            MACapabilities.setCapability("appTopLevelWindow", WinHandleHex);

            session = new WindowsDriver<WindowsElement>(new URL(driverUrl), MACapabilities);

        } catch (IOException | InterruptedException e) {
            logger.error("Catch 'connectSession' exception!\n");
            logger.info(e.getMessage());
            throw new RuntimeException("'connectSession' exception!");
        }
    }

    public void stopSession() {

        if (session == null)
            return;

        try {
            session.close();
            session.quit();
            session = null;
            process.destroy();
            if (!process.waitFor(3, TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }
        } catch (InterruptedException e) {
            logger.error("Catch 'stopSession' exception!\n");
            logger.info(e.getMessage());
        }
        finally {
            logger.info("Elapsed time: " + ((System.nanoTime() - startTaskTime) / 1000000) + " ms");
        }
    }
}

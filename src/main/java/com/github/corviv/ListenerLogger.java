package com.github.corviv;

import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class ListenerLogger implements ITestListener {

    private static final String fileSeparator = System.getProperty("file.separator");
    private static final String testPath = "C:\\test\\screenshots\\results";
    private static final Logger logger = LoggerFactory.getLogger("ListenerLogger");

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting '{}.{}' with parameters {}...\n", result.getTestClass().getName(), result.getName(), result.getParameters());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test '{}' PASSED\n", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test '{}' FAILED!\n", result.getName(), result.getThrowable());

        WindowsDriver driver = Session.getDriverSession();
        String testClassName = getTestClassName(result.getInstanceName()).trim();
        String testMethodName = result.getName().toString().trim();
        String screenShotName = testMethodName + ".png";

        if (driver != null) {
            String imagePath = testPath + fileSeparator + testClassName
                    + fileSeparator
                    + takeScreenShot(driver, screenShotName, testClassName);
            logger.info("Screenshot can be found : {}", imagePath);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.error("Test '{}' SKIPPED!\n", result.getName(), result.getThrowable());
    }

    public static String takeScreenShot(WindowsDriver driver,
                                        String screenShotName, String testName) {
        try {
            File file = new File(testPath);
            if (!file.exists()) {
                logger.info("File {} created", file);
                file.mkdir();
            }

            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File targetFile = new File( testPath + fileSeparator + testName, screenShotName);
            FileUtils.copyFile(screenshotFile, targetFile);

            return screenShotName;
        } catch (Exception e) {
            logger.error("An exception occurred while taking screenshot: " + e.getCause());
            return null;
        }
    }

    public String getTestClassName(String testName) {
        String[] reqTestClassName = testName.split("\\.");
        int i = reqTestClassName.length - 1;
        logger.info("Required Test Name : " + reqTestClassName[i]);
        return reqTestClassName[i];
    }
}
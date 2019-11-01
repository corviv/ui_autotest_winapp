package TestLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerLogger implements ITestListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ListenerLogger.class);

    @Override
    public void onStart(ITestContext iTestContext) {}

    @Override
    public void onFinish(ITestContext iTestContext) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {}

    @Override
    public void onTestStart(ITestResult result)
    {
        LOG.info("Starting {}.{} with parameters {}", result.getTestClass().getName(), result.getName(), result.getParameters());
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        LOG.info("Test {} successful", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        LOG.error("Test {} failed!", result.getName(),result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        LOG.error("Test {} skipped!", result.getName(),result.getThrowable());
    }
}
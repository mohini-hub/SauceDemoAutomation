package tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.DriverSetup;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://www.saucedemo.com";

    // SauceDemo test credentials
    protected static final String VALID_USER   = "standard_user";
    protected static final String VALID_PASS   = "secret_sauce";
    protected static final String LOCKED_USER  = "locked_out_user";
    protected static final String PROBLEM_USER = "problem_user";
    protected static final String WRONG_PASS   = "wrong_password";

    @BeforeSuite
    public void initReport() {
        ExtentReportManager.getInstance();
        System.out.println("=== Test Suite Started ===");
    }

    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        driver = DriverSetup.getDriver();
        driver.get(BASE_URL);

        ExtentTest extentTest = ExtentReportManager.getInstance()
                .createTest(method.getName());
        ExtentReportManager.setTest(extentTest);

        System.out.println("--- Test Started: " + method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        ExtentTest extentTest = ExtentReportManager.getTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            extentTest.fail("Test FAILED: " + result.getThrowable().getMessage());
            extentTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            System.out.println("FAIL: " + result.getName());

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass("Test PASSED");
            System.out.println("PASS: " + result.getName());

        } else {
            extentTest.log(Status.SKIP, "Test SKIPPED");
            System.out.println("SKIP: " + result.getName());
        }

        DriverSetup.quitDriver();
    }

    @AfterSuite
    public void closeReport() {
        ExtentReportManager.flushReport();
        System.out.println("=== Test Suite Finished ===");
        System.out.println("Report saved at: reports/ExtentReport.html");
    }
}

package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ExcelReader;
import utils.ExtentReportManager;

import java.io.File;
import java.io.IOException;

public class LoginTest extends BaseTest {

    // TEST 1: Valid credentials login
    @Test(priority = 1, description = "Valid user login with correct credentials")
    public void testValidLogin() {
        ExtentReportManager.getTest().info("Attempting login with valid user: " + VALID_USER);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USER, VALID_PASS);

        boolean isLoggedIn = loginPage.isLoginSuccessful();
        ExtentReportManager.getTest().info("Login result: " + isLoggedIn);

        Assert.assertTrue(isLoggedIn, "Valid credentials should allow login!");
    }

    // TEST 2: Locked out user should be rejected
    @Test(priority = 2, description = "Locked out user should not be allowed to login")
    public void testLockedOutUser() {
        ExtentReportManager.getTest().info("Attempting login with locked user: " + LOCKED_USER);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(LOCKED_USER, VALID_PASS);

        boolean loginFailed = !loginPage.isLoginSuccessful();
        String errorMsg = loginPage.getErrorMessage();

        ExtentReportManager.getTest().info("Error message received: " + errorMsg);

        Assert.assertTrue(loginFailed, "Locked user should not be able to login!");
        Assert.assertTrue(errorMsg.contains("locked out"),
                "Error message should contain 'locked out'!");
    }

    // TEST 3: Wrong password should fail
    @Test(priority = 3, description = "Login should fail with wrong password")
    public void testWrongPassword() {
        ExtentReportManager.getTest().info("Attempting login with wrong password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USER, WRONG_PASS);

        Assert.assertFalse(loginPage.isLoginSuccessful(),
                "Wrong password should not allow login!");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for wrong password!");
    }

    // TEST 4: Empty username field
    @Test(priority = 4, description = "Empty username should show validation error")
    public void testEmptyUsername() {
        ExtentReportManager.getTest().info("Attempting login with empty username");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", VALID_PASS);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for empty username!");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username is required"),
                "Error should say 'Username is required'!");
    }

    // TEST 5: Empty password field
    @Test(priority = 5, description = "Empty password should show validation error")
    public void testEmptyPassword() {
        ExtentReportManager.getTest().info("Attempting login with empty password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USER, "");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for empty password!");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Password is required"),
                "Error should say 'Password is required'!");
    }

    // TEST 6: Both fields empty
    @Test(priority = 6, description = "Both fields empty should show validation error")
    public void testBothFieldsEmpty() {
        ExtentReportManager.getTest().info("Attempting login with both fields empty");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "");

        Assert.assertFalse(loginPage.isLoginSuccessful(),
                "Empty credentials should not allow login!");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed!");
    }

    // TEST 7: Logout after login
    @Test(priority = 7, description = "User should be able to logout successfully")
    public void testLogout() {
        ExtentReportManager.getTest().info("Testing login and logout flow");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USER, VALID_PASS);

        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login must succeed before testing logout!");

        ExtentReportManager.getTest().info("Login successful - attempting logout");
        loginPage.logout();

        boolean isOnLoginPage = loginPage.isOnLoginPage();
        Assert.assertTrue(isOnLoginPage,
                "After logout, user should be redirected to login page!");
    }

    // TEST 8: Problem user login
    @Test(priority = 8, description = "Problem user should be able to login (UI issues expected but login works)")
    public void testProblemUserLogin() {
        ExtentReportManager.getTest().info("Attempting login with problem_user: " + PROBLEM_USER);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(PROBLEM_USER, VALID_PASS);

        boolean isLoggedIn = loginPage.isLoginSuccessful();
        ExtentReportManager.getTest().info("Problem user logged in: " + isLoggedIn);

        Assert.assertTrue(isLoggedIn,
                "Problem user has valid credentials and should be able to login!");
    }

    // TEST 9: Data Driven Login from Excel
    // FIX: If Excel file not found, uses inline data as fallback
    @Test(priority = 9, dataProvider = "loginData",
          description = "Data driven login test - multiple users")
    public void testDataDrivenLogin(String username, String password, String expectedResult) {
        ExtentReportManager.getTest().info(
            "Data driven test - User: " + username + " | Expected: " + expectedResult
        );

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        if (expectedResult.equalsIgnoreCase("pass")) {
            Assert.assertTrue(loginPage.isLoginSuccessful(),
                    "Valid user login failed: " + username);
            ExtentReportManager.getTest().pass("Valid user logged in successfully: " + username);
        } else {
            Assert.assertFalse(loginPage.isLoginSuccessful(),
                    "Invalid user should not be able to login: " + username);
            ExtentReportManager.getTest().pass("Invalid user correctly rejected: " + username);
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        String excelPath = System.getProperty("user.dir") +
                "/src/test/resources/testdata/LoginData.xlsx";

        File excelFile = new File(excelPath);

        // FIX: If Excel file exists, read from it. Otherwise use hardcoded fallback data.
        if (excelFile.exists()) {
            try {
                ExcelReader reader = new ExcelReader(excelPath);
                reader.openFile();
                int rowCount = reader.getRowCount("LoginData");
                Object[][] data = new Object[rowCount][3];
                for (int i = 1; i <= rowCount; i++) {
                    data[i - 1][0] = reader.getCellData("LoginData", i, 0);
                    data[i - 1][1] = reader.getCellData("LoginData", i, 1);
                    data[i - 1][2] = reader.getCellData("LoginData", i, 2);
                }
                reader.closeFile();
                return data;
            } catch (IOException e) {
                System.out.println("Excel read failed, using fallback data: " + e.getMessage());
            }
        } else {
            System.out.println("LoginData.xlsx not found - using inline fallback data.");
            System.out.println("To use Excel: run 'python3 create_testdata.py' in project root.");
        }

        // Fallback inline test data (SauceDemo users)
        return new Object[][] {
            { "standard_user",   "secret_sauce", "pass" },
            { "locked_out_user", "secret_sauce", "fail" },
            { "problem_user",    "secret_sauce", "pass" },
            { "standard_user",   "wrong_pass",   "fail" },
            { "",                "secret_sauce", "fail" }
        };
    }
}

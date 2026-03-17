package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // SauceDemo login page locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton   = By.id("login-button");
    private By errorMessage  = By.cssSelector("[data-test='error']");
    private By menuButton    = By.id("react-burger-menu-btn");
    private By logoutLink    = By.id("logout_sidebar_link");
    private By pageTitle     = By.cssSelector(".title");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // Returns true if login was successful (Products page loaded)
    public boolean isLoginSuccessful() {
        try {
            String title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageTitle)
            ).getText();
            return title.equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
            ).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // FIX: Open burger menu, wait for sidebar animation, then click logout
    public void logout() {
        try {
            // Step 1: Click the burger menu icon
            wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();

            // Step 2: Wait for sidebar to fully slide open
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));

            // Step 3: Small pause for CSS animation to complete
            Thread.sleep(600);

            // Step 4: Click logout link
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();

        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

    // Returns true if we are back on the login page after logout
    public boolean isOnLoginPage() {
        try {
            return wait.until(
                ExpectedConditions.visibilityOfElementLocated(loginButton)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

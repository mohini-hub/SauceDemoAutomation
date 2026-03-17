package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // SauceDemo Cart page locators
    private By pageTitle     = By.cssSelector(".title");
    private By cartItems     = By.cssSelector(".cart_item");
    private By itemNames     = By.cssSelector(".inventory_item_name");
    // FIX: Use data-test attribute for remove buttons - more reliable
    private By removeButtons = By.cssSelector("[data-test^='remove']");
    private By continueBtn   = By.id("continue-shopping");
    private By checkoutBtn   = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // FIX: SauceDemo cart page title is "Your Cart" - check correctly
    public boolean isOnCartPage() {
        try {
            String title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageTitle)
            ).getText();
            return title.equalsIgnoreCase("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    // Returns number of items currently in cart
    public int getCartItemCount() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    // Remove the first item from cart
    public void removeFirstItem() {
        List<WebElement> buttons = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(removeButtons)
        );
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    public void clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueBtn)).click();
    }

    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
    }

    // Returns true if specific item name exists in cart
    public boolean isItemInCart(String itemName) {
        try {
            List<WebElement> names = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(itemNames)
            );
            return names.stream().anyMatch(e -> e.getText().equals(itemName));
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstItemName() {
        try {
            List<WebElement> names = driver.findElements(itemNames);
            return names.isEmpty() ? "" : names.get(0).getText();
        } catch (Exception e) {
            return "";
        }
    }
}

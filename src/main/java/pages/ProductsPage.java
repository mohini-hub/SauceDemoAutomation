package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // SauceDemo Products page locators
    private By pageTitle        = By.cssSelector(".title");
    private By productNames     = By.cssSelector(".inventory_item_name");
    private By productPrices    = By.cssSelector(".inventory_item_price");
    // FIX: Use data-test attribute for add to cart buttons - more reliable
    private By addToCartButtons = By.cssSelector("[data-test^='add-to-cart']");
    private By cartBadge        = By.cssSelector(".shopping_cart_badge");
    private By cartIcon         = By.cssSelector(".shopping_cart_link");
    // FIX: Use data-test attribute for sort dropdown
    private By sortDropdown     = By.cssSelector("[data-test='product-sort-container']");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Returns true if we are on the Products page
    public boolean isOnProductsPage() {
        try {
            String title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageTitle)
            ).getText();
            return title.equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }

    // Returns total number of products displayed
    public int getProductCount() {
        try {
            List<WebElement> products = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productNames)
            );
            return products.size();
        } catch (Exception e) {
            return 0;
        }
    }

    // Add the first product to cart
    public void addFirstProductToCart() {
        List<WebElement> buttons = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(addToCartButtons)
        );
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
            // Wait for cart badge to update
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            } catch (Exception ignored) {}
        }
    }

    // Add product at specific index (0 = first, 1 = second, etc.)
    public void addProductToCartByIndex(int index) {
        List<WebElement> buttons = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(addToCartButtons)
        );
        if (index < buttons.size()) {
            buttons.get(index).click();
            try { Thread.sleep(300); } catch (Exception ignored) {}
        }
    }

    // Returns number of items in cart (from badge)
    public int getCartCount() {
        try {
            // Wait a moment for badge to update after adding item
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            String count = driver.findElement(cartBadge).getText();
            return Integer.parseInt(count.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }

    // Sort products - pass values: "az", "za", "lohi", "hilo"
    public void sortProducts(String sortValue) {
        // FIX: Ensure we are on Products page before sorting
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        Select select = new Select(
            wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdown))
        );
        select.selectByValue(sortValue);
    }

    public String getFirstProductName() {
        try {
            List<WebElement> names = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(productNames)
            );
            return names.isEmpty() ? "" : names.get(0).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> getAllProductNames() {
        List<WebElement> nameElements = driver.findElements(productNames);
        return nameElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getFirstProductPrice() {
        try {
            List<WebElement> prices = driver.findElements(productPrices);
            return prices.isEmpty() ? "" : prices.get(0).getText();
        } catch (Exception e) {
            return "";
        }
    }
}

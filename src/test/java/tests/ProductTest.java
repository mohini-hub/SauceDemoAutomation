package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentReportManager;

public class ProductTest extends BaseTest {

    // Helper method - login before every product test
    private void doLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USER, VALID_PASS);
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Login must succeed before running product tests!");
    }

    // TEST 1: Products page should show 6 products
    @Test(priority = 1, description = "Verify Products page loads with 6 products")
    public void testProductsPageLoads() {
        doLogin();
        ExtentReportManager.getTest().info("Checking products page load");

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isOnProductsPage(),
                "Should be on Products page after login!");

        int count = productsPage.getProductCount();
        ExtentReportManager.getTest().info("Products found: " + count);

        Assert.assertEquals(count, 6,
                "SauceDemo should display exactly 6 products!");
    }

    // TEST 2: Add first product to cart
    @Test(priority = 2, description = "Add first product to cart and verify cart count = 1")
    public void testAddFirstProductToCart() {
        doLogin();

        ProductsPage productsPage = new ProductsPage(driver);
        String productName = productsPage.getFirstProductName();
        ExtentReportManager.getTest().info("Adding product to cart: " + productName);

        productsPage.addFirstProductToCart();

        int cartCount = productsPage.getCartCount();
        ExtentReportManager.getTest().info("Cart count after adding: " + cartCount);

        Assert.assertEquals(cartCount, 1,
                "Cart should have 1 item after adding first product!");
    }

    // TEST 3: Add 2 products to cart
    @Test(priority = 3, description = "Add 2 products to cart and verify cart count = 2")
    public void testAddMultipleProductsToCart() {
        doLogin();
        ExtentReportManager.getTest().info("Adding 2 products to cart");

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addProductToCartByIndex(0);
        try { Thread.sleep(300); } catch (Exception ignored) {}
        productsPage.addProductToCartByIndex(1);
        try { Thread.sleep(300); } catch (Exception ignored) {}

        int cartCount = productsPage.getCartCount();
        ExtentReportManager.getTest().info("Cart count after adding 2 items: " + cartCount);

        Assert.assertEquals(cartCount, 2,
                "Cart should have 2 items after adding 2 products!");
    }

    // TEST 4: Verify added product is visible in cart
    @Test(priority = 4, description = "Product added to cart should be visible on cart page")
    public void testProductVisibleInCart() {
        doLogin();

        ProductsPage productsPage = new ProductsPage(driver);
        String productName = productsPage.getFirstProductName();
        ExtentReportManager.getTest().info("Product being added: " + productName);

        productsPage.addFirstProductToCart();

        // Wait for cart badge to appear, then navigate to cart
        try { Thread.sleep(500); } catch (Exception ignored) {}
        productsPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        // FIX: Wait for cart page to load before asserting
        try { Thread.sleep(500); } catch (Exception ignored) {}

        Assert.assertTrue(cartPage.isOnCartPage(),
                "Should be on Your Cart page after clicking cart icon!");
        Assert.assertTrue(cartPage.isItemInCart(productName),
                "Added product should be visible in cart: " + productName);
    }

    // TEST 5: Remove item from cart
    @Test(priority = 5, description = "Remove an item from cart - cart should be empty")
    public void testRemoveItemFromCart() {
        doLogin();
        ExtentReportManager.getTest().info("Testing add then remove from cart");

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addFirstProductToCart();
        try { Thread.sleep(300); } catch (Exception ignored) {}
        productsPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should have 1 item before removing!");

        cartPage.removeFirstItem();
        try { Thread.sleep(300); } catch (Exception ignored) {}

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing the item!");
    }

    // TEST 6: Sort products A to Z
    // FIX: Login first, then sort - sort dropdown only visible after login
    @Test(priority = 6, description = "Sort products by Name A-Z")
    public void testSortByNameAZ() {
        doLogin();
        ExtentReportManager.getTest().info("Testing sort by Name A-Z");

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isOnProductsPage(),
                "Must be on Products page to sort!");

        productsPage.sortProducts("az");

        String firstProduct = productsPage.getFirstProductName();
        ExtentReportManager.getTest().info("First product after A-Z sort: " + firstProduct);

        Assert.assertNotNull(firstProduct, "First product name should not be null after sort!");
        Assert.assertFalse(firstProduct.isEmpty(), "First product name should not be empty!");

        // After A-Z sort, first product should start with 'S' (Sauce Labs Backpack)
        Assert.assertTrue(firstProduct.startsWith("Sauce"),
                "After A-Z sort, first product should be 'Sauce Labs Backpack'!");
    }

    // TEST 7: Sort products by price Low to High
    @Test(priority = 7, description = "Sort products by Price Low to High")
    public void testSortByPriceLowToHigh() {
        doLogin();
        ExtentReportManager.getTest().info("Testing sort by Price Low to High");

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isOnProductsPage(),
                "Must be on Products page to sort!");

        productsPage.sortProducts("lohi");

        String firstPrice = productsPage.getFirstProductPrice();
        ExtentReportManager.getTest().info("Cheapest product price: " + firstPrice);

        Assert.assertNotNull(firstPrice, "Price should not be null!");
        Assert.assertTrue(firstPrice.contains("$"),
                "Price should include dollar sign!");
    }

    // TEST 8: Continue Shopping returns to Products page
    @Test(priority = 8, description = "Continue Shopping button should navigate back to Products page")
    public void testContinueShopping() {
        doLogin();
        ExtentReportManager.getTest().info("Testing Continue Shopping navigation");

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addFirstProductToCart();
        try { Thread.sleep(300); } catch (Exception ignored) {}
        productsPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isOnCartPage(),
                "Should be on cart page!");

        cartPage.clickContinueShopping();
        try { Thread.sleep(300); } catch (Exception ignored) {}

        Assert.assertTrue(productsPage.isOnProductsPage(),
                "Continue Shopping should navigate back to Products page!");
    }
}

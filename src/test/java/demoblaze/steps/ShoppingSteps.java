package demoblaze.steps;

import demoblaze.pages.CartPage;
import demoblaze.pages.HomePage;
import demoblaze.pages.PlaceOrderModalPage;
import demoblaze.pages.ProductPage;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

/**
 * Serenity step library for shopping actions (browse, add to cart, checkout).
 * Page objects are injected automatically by the Serenity runner.
 */
public class ShoppingSteps {

    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    PlaceOrderModalPage placeOrderModalPage;

    @Step("Select first product (Samsung galaxy s6)")
    public void selectFirstProduct() {
        homePage.clickFirstProduct();
    }

    @Step("Add product to cart")
    public void addProductToCart() {
        productPage.clickAddToCart();
    }

    @Step("Navigate to cart")
    public void navigateToCart() {
        homePage.navigateToCart();
    }

    @Step("Verify cart contains at least one item")
    public void verifyCartHasItems() {
        assertTrue("Cart should have at least one item", cartPage.hasItems());
    }

    @Step("Place order for '{0}', country: '{1}', city: '{2}'")
    public void placeOrder(String name, String country, String city,
                            String card, String month, String year) {
        cartPage.clickPlaceOrder();
        placeOrderModalPage.fillOrderForm(name, country, city, card, month, year);
        placeOrderModalPage.clickPurchase();
    }

    @Step("Verify purchase confirmation message")
    public void verifyPurchaseConfirmed() {
        assertTrue("Purchase confirmation dialog should be displayed",
                placeOrderModalPage.isConfirmationDisplayed());
        String title = placeOrderModalPage.getConfirmationTitle();
        assertEquals("Thank you for your purchase!", title);
        placeOrderModalPage.clickOkOnConfirmation();
    }
}

package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Represents the Cart page (https://www.demoblaze.com/cart.html).
 */
public class CartPage extends PageObject {

    @FindBy(xpath = "//button[text()='Place Order']")
    WebElementFacade placeOrderButton;

    /**
     * Returns the number of items currently in the cart table.
     */
    public int getCartItemCount() {
        waitABit(2000); // cart items are loaded via AJAX
        List<?> rows = getDriver().findElements(By.cssSelector("#tbodyid tr"));
        return rows.size();
    }

    public boolean hasItems() {
        return getCartItemCount() > 0;
    }

    public void clickPlaceOrder() {
        placeOrderButton.waitUntilClickable().click();
        waitABit(1000);
    }
}

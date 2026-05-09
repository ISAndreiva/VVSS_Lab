package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

/**
 * Represents an individual product detail page on Demoblaze.
 */
public class ProductPage extends PageObject {

    @FindBy(xpath = "//a[text()='Add to cart']")
    WebElementFacade addToCartButton;

    @FindBy(className = "name")
    WebElementFacade productNameLabel;

    public String getProductName() {
        return productNameLabel.waitUntilVisible().getText();
    }

    /**
     * Clicks "Add to cart" and accepts the confirmation alert that appears.
     */
    public void clickAddToCart() {
        addToCartButton.waitUntilClickable().click();
        try {
            waitABit(1000);
            getDriver().switchTo().alert().accept();
        } catch (Exception ignored) {
            // No alert shown – already in cart or timing issue
        }
    }
}

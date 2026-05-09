package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

/**
 * Represents the Place Order modal and the subsequent purchase confirmation dialog.
 */
public class PlaceOrderModalPage extends PageObject {

    @FindBy(id = "name")
    WebElementFacade nameField;

    @FindBy(id = "country")
    WebElementFacade countryField;

    @FindBy(id = "city")
    WebElementFacade cityField;

    @FindBy(id = "card")
    WebElementFacade cardField;

    @FindBy(id = "month")
    WebElementFacade monthField;

    @FindBy(id = "year")
    WebElementFacade yearField;

    @FindBy(xpath = "//button[text()='Purchase']")
    WebElementFacade purchaseButton;

    // Sweet-alert confirmation elements
    @FindBy(css = ".sweet-alert h2")
    WebElementFacade confirmationTitle;

    @FindBy(css = ".sweet-alert .confirm")
    WebElementFacade confirmationOkButton;

    public void fillOrderForm(String name, String country, String city,
                               String card, String month, String year) {
        nameField.waitUntilVisible().type(name);
        countryField.type(country);
        cityField.type(city);
        cardField.type(card);
        monthField.type(month);
        yearField.type(year);
    }

    public void clickPurchase() {
        purchaseButton.waitUntilClickable().click();
        waitABit(2000);
    }

    public boolean isConfirmationDisplayed() {
        try {
            return confirmationTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationTitle() {
        return confirmationTitle.waitUntilVisible().getText();
    }

    public void clickOkOnConfirmation() {
        confirmationOkButton.waitUntilClickable().click();
        waitABit(1000);
    }
}

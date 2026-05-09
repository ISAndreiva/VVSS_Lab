package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

/**
 * Represents the Log In modal dialog on the Demoblaze home page.
 */
public class LoginModalPage extends PageObject {

    @FindBy(id = "loginusername")
    WebElementFacade usernameField;

    @FindBy(id = "loginpassword")
    WebElementFacade passwordField;

    @FindBy(xpath = "//button[text()='Log in']")
    WebElementFacade loginButton;

    public void waitForModalToBeVisible() {
        usernameField.waitUntilVisible();
    }

    public void enterUsername(String username) {
        usernameField.waitUntilVisible().clear();
        usernameField.type(username);
    }

    public void enterPassword(String password) {
        passwordField.waitUntilVisible().clear();
        passwordField.type(password);
    }

    public void clickLogin() {
        loginButton.waitUntilClickable().click();
        waitABit(2000);
    }

    /**
     * Tries to accept a browser alert if one is present (shown on login failure).
     * Returns the alert text, or an empty string if no alert appeared.
     */
    public String tryAcceptAlert() {
        try {
            waitABit(500);
            String text = getDriver().switchTo().alert().getText();
            getDriver().switchTo().alert().accept();
            return text;
        } catch (Exception e) {
            return "";
        }
    }
}

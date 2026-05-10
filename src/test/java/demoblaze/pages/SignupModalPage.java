package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

/**
 * Represents the Sign Up modal dialog on the Demoblaze home page.
 */
public class SignupModalPage extends PageObject {

    @FindBy(id = "sign-username")
    WebElementFacade usernameField;

    @FindBy(id = "sign-password")
    WebElementFacade passwordField;

    @FindBy(xpath = "//button[text()='Sign up']")
    WebElementFacade signupButton;

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

    public void clickSignup() {
        signupButton.waitUntilClickable().click();
    }

    /**
     * Waits for and accepts the browser alert that appears after clicking Sign up.
     * Returns the alert text (e.g. "Sign up successful." or "This user already exist.").
     */
    public String acceptAlert() {
        try {
            waitABit(1000);
            String text = getDriver().switchTo().alert().getText();
            getDriver().switchTo().alert().accept();
            return text;
        } catch (Exception e) {
            return "";
        }
    }
}

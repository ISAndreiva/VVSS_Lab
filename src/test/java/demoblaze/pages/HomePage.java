package demoblaze.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Represents the Demoblaze home page (https://www.demoblaze.com).
 * Handles navigation bar actions and product selection.
 */
public class HomePage extends PageObject {

    private static final String BASE_URL = "https://www.demoblaze.com";

    @FindBy(id = "signin2")
    WebElementFacade signupNavButton;

    @FindBy(id = "login2")
    WebElementFacade loginNavButton;

    @FindBy(id = "logout2")
    WebElementFacade logoutNavButton;

    @FindBy(id = "nameofuser")
    WebElementFacade loggedInUserLabel;

    @FindBy(id = "cartur")
    WebElementFacade cartNavLink;

    public void navigateToHome() {
        getDriver().manage().window().maximize();
        getDriver().get(BASE_URL);
        waitABit(1500);
    }

    public void clickSignup() {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", signupNavButton);
        waitABit(800);
    }

    public void clickLogin() {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", loginNavButton);
        waitABit(800);
    }

    public void clickLogout() {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", logoutNavButton);
        waitABit(800);
    }

    public boolean isUserLoggedIn() {
        return loggedInUserLabel.isVisible();
    }

    public String getLoggedInUsername() {
        return loggedInUserLabel.isVisible() ? loggedInUserLabel.getText() : "";
    }

    /** Clicks the first listed product (Samsung galaxy s6). */
    public void clickFirstProduct() {
        waitABit(1500); // wait for AJAX product load
        getDriver().findElement(By.linkText("Samsung galaxy s6")).click();
        waitABit(1500);
    }

    public void navigateToCart() {
        cartNavLink.waitUntilClickable().click();
        waitABit(2000);
    }
}

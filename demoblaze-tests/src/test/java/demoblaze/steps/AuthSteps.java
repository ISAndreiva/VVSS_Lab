package demoblaze.steps;

import demoblaze.pages.HomePage;
import demoblaze.pages.LoginModalPage;
import demoblaze.pages.SignupModalPage;
import net.serenitybdd.annotations.Step;

import static org.junit.Assert.*;

/**
 * Serenity step library for authentication actions (signup, login, logout).
 * Page objects are injected automatically by the Serenity runner.
 */
public class AuthSteps {

    HomePage homePage;
    SignupModalPage signupModalPage;
    LoginModalPage loginModalPage;

    /** Stored result of the most recent login attempt alert (empty = no alert). */
    private String lastLoginAlertText = "";

    @Step("Navigate to Demoblaze home page")
    public void navigateToHome() {
        homePage.navigateToHome();
    }

    /**
     * Signs up with the given credentials. Accepts any resulting alert (success or
     * "user already exists") without failing the step – used both in setup and tests.
     */
    @Step("Sign up with username '{0}'")
    public void signupUser(String username, String password) {
        homePage.clickSignup();
        signupModalPage.enterUsername(username);
        signupModalPage.enterPassword(password);
        signupModalPage.clickSignup();
        String alert = signupModalPage.acceptAlert();
        assertTrue(
                "Expected signup alert (success or already exists) but got: '" + alert + "'",
                alert.contains("Sign up successful") || alert.contains("already exist")
        );
    }

    /**
     * Same as {@link #signupUser} but ignores assertion failures – used in @Before
     * setup to ensure the fixed test user exists without failing tests when the account
     * was already created in a previous run.
     */
    @Step("Ensure test user '{0}' exists (signup if needed)")
    public void ensureUserExists(String username, String password) {
        homePage.clickSignup();
        signupModalPage.enterUsername(username);
        signupModalPage.enterPassword(password);
        signupModalPage.clickSignup();
        signupModalPage.acceptAlert(); // accept regardless of outcome
    }

    @Step("Login with username '{0}'")
    public void loginUser(String username, String password) {
        homePage.clickLogin();
        loginModalPage.waitForModalToBeVisible();
        loginModalPage.enterUsername(username);
        loginModalPage.enterPassword(password);
        loginModalPage.clickLogin();
        // Capture alert if login failed
        lastLoginAlertText = loginModalPage.tryAcceptAlert();
    }

    @Step("Verify user '{0}' is logged in")
    public void verifyLoggedIn(String username) {
        assertTrue("Expected user to be logged in (nameofuser visible)",
                homePage.isUserLoggedIn());
        assertTrue("Expected logged-in label to contain username '" + username + "'",
                homePage.getLoggedInUsername().contains(username));
    }

    @Step("Verify login failed (wrong credentials)")
    public void verifyLoginFailed() {
        assertFalse("Expected an alert message for failed login but none appeared",
                lastLoginAlertText.isEmpty());
        assertFalse("User should NOT be logged in after failed credentials",
                homePage.isUserLoggedIn());
    }

    @Step("Logout")
    public void logout() {
        homePage.clickLogout();
    }
}

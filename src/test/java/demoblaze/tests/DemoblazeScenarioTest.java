package demoblaze.tests;

import demoblaze.steps.AuthSteps;
import demoblaze.steps.ShoppingSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 * [Scenario-based Testing – 6 pts]
 *
 * End-to-end scenario covering all four tested functionalities:
 *   1. Sign up  (new unique user per run)
 *   2. Login    (with the just-created user)
 *   3. Add to cart
 *   4. Checkout (place order + verify confirmation)
 *
 * After each step a Serenity @Step assertion verifies the action succeeded,
 * making individual failures pinpointed in the Serenity HTML report.
 */
@RunWith(SerenityRunner.class)
public class DemoblazeScenarioTest {

    @Managed
    WebDriver driver;

    @Steps
    AuthSteps authSteps;

    @Steps
    ShoppingSteps shoppingSteps;

    @Test
    @Title("Scenario: signup → login → add to cart → checkout")
    public void full_shopping_scenario() {
        // Use a timestamp-based username so every run creates a fresh account
        String username = "vvss_" + System.currentTimeMillis();
        String password  = "Test@2026";

        // ── Step 1: Navigate to home ──────────────────────────────────────────
        authSteps.navigateToHome();

        // ── Step 2: Sign up ───────────────────────────────────────────────────
        authSteps.signupUser(username, password);

        // ── Step 3: Login ─────────────────────────────────────────────────────
        authSteps.loginUser(username, password);
        authSteps.verifyLoggedIn(username);

        // ── Step 4: Browse and add a product to the cart ──────────────────────
        shoppingSteps.selectFirstProduct();
        shoppingSteps.addProductToCart();

        // ── Step 5: Navigate to cart and verify item is present ───────────────
        shoppingSteps.navigateToCart();
        shoppingSteps.verifyCartHasItems();

        // ── Step 6: Place order (checkout) ────────────────────────────────────
        shoppingSteps.placeOrder(
                "John Doe",         // name
                "Romania",          // country
                "Cluj-Napoca",      // city
                "4111111111111111", // credit card
                "5",                // month
                "2026"              // year
        );

        // ── Step 7: Verify purchase confirmation ──────────────────────────────
        shoppingSteps.verifyPurchaseConfirmed();

        // ── Step 8: Logout ────────────────────────────────────────────────────
        authSteps.logout();
    }
}

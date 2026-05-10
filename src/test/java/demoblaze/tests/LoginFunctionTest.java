package demoblaze.tests;

import demoblaze.steps.AuthSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 * [Function Testing – 3 pts]
 *
 * Parametrized login test that reads test data from login_data.csv.
 * CSV columns: username, password, expectedResult ("success" | "failure")
 *
 * Before each iteration the fixed test user is ensured to exist on demoblaze.com
 * (signup is idempotent – "already exist" alert is accepted silently).
 */
@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("testdata/login_data.csv")
public class LoginFunctionTest {

    // --- CSV-bound fields (names must match CSV column headers) ---
    public String username;
    public String password;
    public String expectedResult;

    @Managed(uniqueSession = false)
    WebDriver driver;

    @Steps
    AuthSteps authSteps;

    /**
     * Ensure the "success" test user (vvsslab2026) exists before each iteration.
     * If it was already registered in a previous run, the alert is silently accepted.
     */
    @Before
    public void ensureTestUserExists() {
        authSteps.navigateToHome();
        authSteps.ensureUserExists("vvsslab2026", "password123");
    }

    @Test
    @Title("Login with username '{0}' – expected result: {2}")
    public void login_with_parametrized_credentials() {
        authSteps.navigateToHome();
        authSteps.loginUser(username, password);

        if ("success".equals(expectedResult)) {
            authSteps.verifyLoggedIn(username);
            authSteps.logout();
        } else {
            authSteps.verifyLoginFailed();
        }
    }
}

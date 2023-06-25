package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.RegistrationPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public class RegistrationWebTest extends BaseWebTest {

    private final RegistrationPage page = new RegistrationPage();

    @Test
    @AllureId("103")
    public void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(CFG.getFrontUrl() + RegistrationPage.URL);

        page.checkThatPageLoaded()
                .fillRegistrationForm("wdfsdasfs", "123", "12345")
                .checkErrorMessage("Passwords should be equal");
    }

    @Test
    @AllureId("104")
    public void errorMessageShouldBeVisibleInCaseThatUsernameNotUniq() {
        final String username = "dima";

        Selenide.open(CFG.getFrontUrl() + RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm(username, "12345", "12345")
                .checkErrorMessage("Username `" + username + "` already exists");
    }

    @Test
    @AllureId("105")
    public void errorMessageShouldBeVisibleInCaseThatPasswordsLessThan3Symbols() {
        Selenide.open(CFG.getFrontUrl() + RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm("wdfsdadfdaasfs", "1", "1")
                .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    @AllureId("106")
    public void errorMessageShouldBeVisibleInCaseThatUsernameLessThan3Symbols() {
        Selenide.open(CFG.getFrontUrl() + RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm("g", "12345", "12345")
                .checkErrorMessage("Allowed username length should be from 3 to 50 characters");
    }
}

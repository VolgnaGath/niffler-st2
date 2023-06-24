package guru.qa.niffler.test.ui;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.RegistrationPage;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

public class RegistrationWebTest extends BaseWebTest {
    @Test
    void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(RegistrationPage.URL, RegistrationPage.class)
                .checkThatPageLoaded()
                .fillRegistrationForm("inna", "123", "12345")
                .checkErrorMessage("Passwords should be equal");
    }
}

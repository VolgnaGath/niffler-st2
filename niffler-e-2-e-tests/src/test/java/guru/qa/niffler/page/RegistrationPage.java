package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
@Getter
public class RegistrationPage extends BasePage<RegistrationPage> {
    public static final String URL = Config.getConfig().getFrontUrl() + "/register";
    private final SelenideElement registrationFormHeader = $(byText("Registration form"));
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("button[type='submit']");


    @Override
    public RegistrationPage checkThatPageLoaded() {
        registrationFormHeader.shouldBe(visible);
        return this;
    }

    public RegistrationPage fillRegistrationForm(String username, String password, String passwordSubmit) {
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(passwordSubmit);
        signUpBtn.click();
        return this;
    }
    public RegistrationPage checkErrorMessage(String expectedErrorMessage) {
        $(".form__error").shouldHave(text(expectedErrorMessage));
        return this;
    }

}

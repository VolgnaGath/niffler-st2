package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class LoginPage extends BasePage<LoginPage>{
    public static final String WELCOME_PAGE_URL = Config.getConfig().getAuthUrl() + "/login";
    private final SelenideElement usernameInput = $(".form__input[name='username']");
    private final SelenideElement passwordInput = $(".form__input[name='password']");
    private final SelenideElement signInBtn = $(".form__submit[type='submit']");


    @Override
    public LoginPage checkThatPageLoaded() {
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        signInBtn.shouldBe(visible);
        return this;
    }

    public MainPage doLoginWithCorrectData(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        signInBtn.click();
        return new MainPage();
    }
}

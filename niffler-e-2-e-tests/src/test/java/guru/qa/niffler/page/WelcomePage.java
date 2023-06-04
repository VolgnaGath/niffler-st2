package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class WelcomePage extends BasePage<WelcomePage>{
    public static final String WELCOME_PAGE_URL = Config.getConfig().getFrontUrl();
    private final SelenideElement mainHeader = $(".form .main__header");
    private final SelenideElement redirectToLoginBtn = $("a[href*='redirect']");
    private final SelenideElement redirectToRegisterBtn = $("a[href*='register']");

    @Override
    public WelcomePage checkThatPageLoaded() {
        mainHeader.shouldBe(visible);
        return this;
    }
    public RegistrationPage clickOnRegisterBtn() {
        redirectToRegisterBtn.click();
        return new RegistrationPage();
    }
    public LoginPage clickOnLoginBtn(){
        redirectToLoginBtn.click();
        return new LoginPage();
    }
}

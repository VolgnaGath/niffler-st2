package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class ProfilePage extends BasePage<ProfilePage>{
    public static final String PROFILE_PAGE_URL = Config.getConfig().getFrontUrl() + "/profile";
    private final Header header = new Header();

    private final SelenideElement setYourNameInput = $(".form__input[name='firstname']");
    private final SelenideElement setYourSurnameInput = $(".form__input[name='surname']");
    private final SelenideElement updateProfileSubmitBtn = $(".main-content__section-avatar .button");
    private final SelenideElement selectCurrencyBtn = $(".form__label .select-wrapper");
    private final SelenideElement currencyArrow = $("[class$=indicatorContainer]");
    private final SelenideElement currencyMenuList = $("[class$=MenuList]");

    private final SelenideElement addNewCategoryInput = $(".form__input[name='category']");
    private final SelenideElement createNewCategoryBtn = $(".main-content__section-add-category .button");
    public Header getHeader() {
        return header;
    }

    @Override
    public ProfilePage checkThatPageLoaded() {
        setYourNameInput.shouldBe(visible);
        setYourSurnameInput.shouldBe(visible);
        addNewCategoryInput.shouldBe(visible);
        return this;
    }
    public ProfilePage createNewCategory(String categoryName) {
        addNewCategoryInput.val(categoryName);
        createNewCategoryBtn.click();
        return this;
    }

    public ProfilePage updateProfile(String name, String surname) {
        setYourNameInput.val(name);
        setYourSurnameInput.val(surname);
        updateProfileSubmitBtn.scrollTo().click();
        return this;
    }
}

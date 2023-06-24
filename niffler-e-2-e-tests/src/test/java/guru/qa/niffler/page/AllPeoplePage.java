package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class AllPeoplePage extends BasePage<AllPeoplePage> {
    public static final String ALL_PEOPLE_PAGE_URL = Config.getConfig().getFrontUrl() + "/people";
    private final Header header = new Header();
    private final SelenideElement allPeopleContentSection = $(".people-content .main-content__section");
    private final SelenideElement addFriendBtn = $(".abstract-table__buttons .button-icon_type_add");
    private final SelenideElement removeFriendBtn = $(".abstract-table__buttons .button-icon_type_close");
    private final SelenideElement peoplesUsernamesForSearch = $(".people-content tr");
    public Header getHeader() {
        return header;
    }


    @Override
    public AllPeoplePage checkThatPageLoaded() {
        allPeopleContentSection.shouldBe(visible);
        return this;
    }
}

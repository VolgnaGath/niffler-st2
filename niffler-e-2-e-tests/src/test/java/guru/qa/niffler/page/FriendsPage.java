package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class FriendsPage extends BasePage<FriendsPage> {
    public static final String FRIENDS_PAGE_URL = Config.getConfig().getFrontUrl() + "/friends";
    private final Header header = new Header();

    private final SelenideElement peopleContentSection = $(".people-content .main-content__section");
    private final SelenideElement removeFriendBtn = $(".abstract-table__buttons .button-icon_type_close");
    private final SelenideElement submitInvitationBtn = $(".abstract-table__buttons .button-icon_type_submit");
    private final SelenideElement declineInvitationBtn = $(".abstract-table__buttons .button-icon_type_close");
    public Header getHeader() {
        return header;
    }


    @Override
    public FriendsPage checkThatPageLoaded() {
        peopleContentSection.shouldBe(visible);
        return this;
    }
}

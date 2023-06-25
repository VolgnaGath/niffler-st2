package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }

    private final SelenideElement mainPageBtn = $("a[href*='main']");
    private final SelenideElement friendsPageBtn = $("a[href*='friends']");
    private final SelenideElement allPeoplePageBtn = $("a[href*='/people']");
    private final SelenideElement profilePageBtn = $("a[href*='/profile']");

    @Override
    public Header checkThatComponentDisplayed() {
        self.$(".header__title").shouldHave(text("Niffler. The coin keeper."));
        return this;
    }

    public FriendsPage goToFriendsPage() {
        friendsPageBtn.click();
        return new FriendsPage();
    }

    public MainPage goToMainPage() {
        mainPageBtn.click();
        return new MainPage();
    }

    public ProfilePage goToProfilePage() {
        profilePageBtn.click();
        return new ProfilePage();
    }
    public AllPeoplePage goToAllPeoplePage(){
        allPeoplePageBtn.click();
        return new AllPeoplePage();}
}

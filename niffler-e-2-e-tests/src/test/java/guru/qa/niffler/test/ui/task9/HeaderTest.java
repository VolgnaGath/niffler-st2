package guru.qa.niffler.test.ui.task9;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HeaderTest extends BaseWebTest {
    @BeforeEach
    void doLogin() {
        Selenide.open(WelcomePage.WELCOME_PAGE_URL, WelcomePage.class)
                .clickOnLoginBtn()
                .doLoginWithCorrectData("dima", "12345");
    }

    @AllureId("116")
    @Test
    void fromMainToFriends() {
        new MainPage()
                .getHeader()
                .goToFriendsPage()
                .checkThatPageLoaded();
    }

    @AllureId("117")
    @Test
    void fromMainToProfile() {
        new MainPage()
                .getHeader()
                .goToProfilePage()
                .checkThatPageLoaded();
    }

    @AllureId("118")
    @Test
    void fromMainToAllPeople() {
        new MainPage()
                .getHeader()
                .goToAllPeoplePage()
                .checkThatPageLoaded();
    }

    @AllureId("119")
    @Test
    void fromMainToAllPeopleToProfile() {
        new MainPage()
                .getHeader()
                .goToAllPeoplePage()
                .checkThatPageLoaded()
                .getHeader()
                .goToProfilePage()
                .checkThatPageLoaded();
    }
    @AllureId("119")
    @Test
    void checkLastWeekFilter() {
        new MainPage()
                .getFilterLastWeekBtn().scrollTo()
                .click();
    }
}
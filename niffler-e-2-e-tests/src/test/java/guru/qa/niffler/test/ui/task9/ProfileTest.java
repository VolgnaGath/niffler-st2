package guru.qa.niffler.test.ui.task9;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProfileTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        Selenide.open(WelcomePage.WELCOME_PAGE_URL, WelcomePage.class)
                .clickOnLoginBtn()
                .doLoginWithCorrectData("dima", "12345");
    }

    @AllureId("110")
    @Test
    void updateProfile() {
        new MainPage()
                .getHeader()
                .goToProfilePage()
                .checkThatPageLoaded()
                .updateProfile("DIMA", "DIMA");
    }

    @AllureId("111")
    @Test
    void addNewCategory() {
        new MainPage()
                .getHeader()
                .goToProfilePage()
                .checkThatPageLoaded()
                .createNewCategory("new");
    }
    @AllureId("112")
    @Test
    void addNewSpending() {
        new MainPage()
                .checkThatPageLoaded();
    }
    @AllureId("113")
    @Test
    void deleteSpending() {
        new MainPage()
                .addNewSpendingAndCheckThatSpendIsDisplayed("123", "New Spend1")
                .deleteSpending("New Spend1");
    }
    @AllureId("114")
    @Test
    void checkThatMainPageLoaded() {
        new MainPage()
                .checkThatPageLoaded();
    }

    @AllureId("115")
    @Test
    void checkThatMainPageStatsLoaded() {
        new MainPage()
                .checkThatPageLoaded();
    }
}

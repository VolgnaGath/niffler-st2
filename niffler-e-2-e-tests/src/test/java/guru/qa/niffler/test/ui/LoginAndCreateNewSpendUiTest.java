package guru.qa.niffler.test.ui;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.WelcomePage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class LoginAndCreateNewSpendUiTest {
    @AllureId("107")
    @Test
    void loginAndCreateNewSpendingPositiveTest() {
        Selenide.open(WelcomePage.WELCOME_PAGE_URL, WelcomePage.class)
                .clickOnLoginBtn()
                .checkThatPageLoaded()
                .doLoginWithCorrectData("dima", "12345")
                .getHeader()
                .goToProfilePage()
                .checkThatPageLoaded()
                .createNewCategory("test1")
                .getHeader()
                .goToMainPage()
                .checkThatPageLoaded()
                .addNewSpendingAndCheckThatSpendIsDisplayed("3333", "testDescrip")
                .deleteSpending("testDescrip");
    }
}

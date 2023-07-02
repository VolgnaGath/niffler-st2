package guru.qa.niffler.test.web;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;


import static guru.qa.niffler.condition.SpendCondition.spends;

public class SpendsWebTest extends BaseWebTest {

    @ApiLogin(user = @GenerateUser(
            categories = @Category("Обучение"),
            spends = @GenerateSpend(
                    description = "QA GURU ADVANCED VOL 2",
                    currency = CurrencyValues.RUB,
                    amount = 52000.00
            )
    ))
    @AllureId("107")
    @Test
    void spendShouldBeDeletedByActionInTable(UserJson user) {
        final SpendJson spend = user.getSpends().get(0);

        Selenide.open(MainPage.MAIN_PAGE_URL, MainPage.class)
                .deleteSpending(spend.getDescription())
                .checkThatSpendsListIsEmpty();
    }

    @ApiLogin(user = @GenerateUser(
            categories = @Category("Обучение"),
            spends = @GenerateSpend(
                    description = "QA GURU ADVANCED VOL 2",
                    currency = CurrencyValues.RUB,
                    amount = 52000.00
            )
    ))
    @AllureId("108")
    @Test
    void spendInTableShouldBeEqualToGiven(UserJson user) {
        final SpendJson spend = user.getSpends().get(0);
        Selenide.open(MainPage.MAIN_PAGE_URL, MainPage.class)
                .checkThatPageLoaded()
                .getSpendingTable().$$("tr").shouldHave(spends(spend));

    }
}

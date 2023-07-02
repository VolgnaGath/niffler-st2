package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import lombok.Getter;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Getter
public class MainPage extends BasePage<MainPage> {
    public static final String MAIN_PAGE_URL = Config.getConfig().getFrontUrl() + "/main";
    private final Header header = new Header();

    private final SelenideElement chooseSpendingCategorySelector = $(".main-content__section-add-spending [class$=Input]");
    private final SelenideElement spendCategoryMenu = $("[class$=MenuList]");
    private final SelenideElement setAmountInput = $(".form__input[type='number']");
    private final SelenideElement spendDateInput = $(".main-content__section.main-content__section-add-spending .react-datepicker__input-container");
    private final SelenideElement spendDateFormPicker = $(".main-content__section.main-content__section-add-spending .react-datepicker-popper");
    private final SelenideElement descriptionInput = $(".form__input[type='text']");
    private final SelenideElement addNewSpendingBtn = $(".button[type='submit']");

    private final SelenideElement deleteSelectedBtn = $$(".button_type_small").find(text("Delete selected"));
    private final SelenideElement filterTodayBtn = $$(".button_type_small").find(text("Today"));
    private final SelenideElement filterLastWeekBtn = $$(".spendings__buttons .button_type_small").find(text("Last week"));
    private final SelenideElement filterLastMonthBtn = $$(".button_type_small").find(text("Last month"));
    private final SelenideElement filterAllTimeBtn = $$(".button_type_small").find(text("All time"));
    private final SelenideElement filterCloseBtn = $(".spendings__table-controls .button-icon_type_close");
    private final SelenideElement editSpendingBtn = $(".spendings-table .button-icon_type_edit");
    private final SelenideElement editSpendingCloseBtn = $(".spendings__button-group .button-icon_type_close");
    private final SelenideElement editSpendingSubmitBtn = $(".spendings__button-group .button-icon_type_submit");
    private final SelenideElement spendingTable = $(".spendings-table tbody");
    private final SelenideElement mainContentSectionStats = $(".main-content .main-content__section-stats");
    public Header getHeader() {
        return header;
    }

    @Override
    public MainPage checkThatPageLoaded() {
        chooseSpendingCategorySelector.shouldBe(visible);
        setAmountInput.shouldBe(visible);
        spendDateInput.shouldBe(visible);
        descriptionInput.shouldBe(visible);
        addNewSpendingBtn.shouldBe(visible);
        return this;
    }

    public MainPage addNewSpendingAndCheckThatSpendIsDisplayed(String amount, String descrip) {
        chooseSpendingCategorySelector.click();
        spendCategoryMenu.$$("[class$=option]").first().click();
        setAmountInput.val(amount);
        descriptionInput.val(descrip);
        addNewSpendingBtn.click();
        spendingTable.$$("tr").find(text(descrip)).shouldHave(text(descrip));
        return this;
    }

    public MainPage deleteSpending(String descrip) {
        spendingTable.$$("tr").find(text(descrip)).$$("td").first()
                .scrollTo()
                .click();
        deleteSelectedBtn.click();
        return this;
    }
    public MainPage checkThatStatsLoaded() {
        mainContentSectionStats.shouldBe(visible);
        return this;
    }
    public MainPage checkThatSpendsListIsEmpty() {
        spendingTable
                .$$("tr")
                .shouldHave(CollectionCondition.size(0));
        return this;
    }
}

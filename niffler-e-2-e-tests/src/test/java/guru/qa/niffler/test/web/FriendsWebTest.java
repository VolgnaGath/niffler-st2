package guru.qa.niffler.test.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.FriendsCondition.friends;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

public class FriendsWebTest extends BaseWebTest {

    @GenerateUser(
            friends = @Friend
    )
    @AllureId("100")
    @Test
    void friendsShouldBeVisible0(UserJson user) {
        final UserJson friend = user.getFriends().get(0);

        Allure.step("open page", () -> Selenide.open(CFG.getFrontUrl() + "/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").should(friends(friend));
    }

    @GenerateUser(
            outcomeInvitations = @Friend
    )
    @AllureId("101")
    @Test
    void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user) {
        final UserJson outcomeInvitation = user.getOutcomeInvitations().get(0);

        Allure.step("open page", () -> Selenide.open(CFG.getFrontUrl() + "/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr")
                .find(Condition.text(outcomeInvitation.getUsername()))
                .should(Condition.text("Pending invitation"));
    }
}

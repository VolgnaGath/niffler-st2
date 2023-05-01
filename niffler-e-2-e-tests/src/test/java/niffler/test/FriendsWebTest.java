package niffler.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.User;
import niffler.jupiter.extension.UsersQueueExtension;
import niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

  @AllureId("102")
  @Test
  void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson userFirst,
                               @User(userType = WITH_FRIENDS) UserJson userSecond) {
    step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    step("Юзер " + userFirst.getUsername(), () -> {
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userFirst.getUsername());
    $("input[name='password']").setValue(userFirst.getPassword());
    $("button[type='submit']").click();
    $("a[href*='friends']").click();
    $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    $(".header__logout").click();});
    step("Юзер " + userSecond.getUsername(), () -> {
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userSecond.getUsername());
    $("input[name='password']").setValue(userSecond.getPassword());
    $("button[type='submit']").click();
    $("a[href*='friends']").click();
    $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    $(".header__logout").click();});


  }

  @AllureId("103")
  @Test
  void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user,
                               @User(userType = WITH_FRIENDS) UserJson user2) {
    step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();

    $("a[href*='people']").click();
    $$(".table tbody tr").find(Condition.text("Pending invitation"))
        .should(Condition.visible);
    System.out.println(user.getUsername());
    System.out.println(user2.getUsername());

  }

}

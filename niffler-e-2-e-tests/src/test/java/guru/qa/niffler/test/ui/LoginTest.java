package guru.qa.niffler.test.ui;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.NifflerDaoType;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.ClasspathUser;
import guru.qa.niffler.jupiter.annotation.GenerateUserEntity;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LoginTest extends BaseWebTest {

  @ValueSource(strings = {
          "testdata/dima.json",
          "testdata/emma.json"
  })
  @AllureId("104")
  @ParameterizedTest
  void loginTest(@ClasspathUser UserJson user) {
    Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();

    $("a[href*='friends']").click();
    $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
  }

  @AllureId("105")
  @Test
  void createUserJdbcTest(@GenerateUserEntity() UserEntity user,
                          @GenerateUserEntity(userDao = NifflerDaoType.SPRING, username = "allan", password = "12345") UserEntity user2) {
    System.out.println(user.getUsername());
    System.out.println(user2.getUsername());

  }

}

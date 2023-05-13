package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import java.io.IOException;

import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.jupiter.annotation.GenerateRandomUserEntity;
import niffler.model.UserJson;
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
  void loginTest(@ClasspathUser UserJson user) throws IOException {
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
  void createUserJdbcTest(@GenerateRandomUserEntity() UserEntity user,
                          @GenerateRandomUserEntity(username = "allah", password = "12345") UserEntity user2) throws IOException {
    System.out.println(user.getUsername());
    System.out.println(user2.getUsername());

  }

}

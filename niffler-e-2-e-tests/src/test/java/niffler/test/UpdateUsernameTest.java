package niffler.test;


import io.qameta.allure.AllureId;
import niffler.api.UpdateUserInfoService;
import niffler.jupiter.annotation.ClasspathUserUpdate;
import niffler.model.UserJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.io.IOException;

public class UpdateUsernameTest extends BaseWebTest {
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8089")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UpdateUserInfoService updateUserInfoService = retrofit.create(UpdateUserInfoService.class);



    @ValueSource(strings = {
            "testdata/dimaUpdate.json"
    })
    @AllureId("105")
    @ParameterizedTest
    void updateUserInfo(@ClasspathUserUpdate UserJson user) throws IOException {
        Assertions.assertTrue(updateUserInfoService.update(user).execute().isSuccessful());
    }
}

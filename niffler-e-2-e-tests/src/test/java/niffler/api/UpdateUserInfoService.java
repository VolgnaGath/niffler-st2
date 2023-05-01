package niffler.api;

import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpdateUserInfoService {
    @POST("/updateUserInfo")
    Call<UserJson> update(@Body UserJson user);

}

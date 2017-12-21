package bigfish.bigfishtest;

import bigfish.bigfishtest.model.OAuthToken;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public interface TokenService {
    @FormUrlEncoded
    @POST("/oauth/v2/token")
    Observable<OAuthToken> getToken(@Field("grant_type") String grantType,
                                    @Field("client_id") String clientId,
                                    @Field("client_secret") String clientSecret,
                                    @Field("username") String username,
                                    @Field("password") String password);
}

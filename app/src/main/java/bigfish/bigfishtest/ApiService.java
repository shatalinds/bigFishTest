package bigfish.bigfishtest;

import java.util.Map;

import bigfish.bigfishtest.model.RequestAnswer;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/mobile/registration")
    Observable<RequestAnswer> registration(@Body Map<String, String> body);

    @Headers("Content-Type: application/json")
    @POST("/api/mobile/registration_confirm")
    Observable<RequestAnswer> registration_confirm(@Body Map<String, String> body);
}
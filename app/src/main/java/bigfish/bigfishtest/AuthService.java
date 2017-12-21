package bigfish.bigfishtest;

import java.util.Map;

import bigfish.bigfishtest.model.ChargePriceAnticipateModel;
import bigfish.bigfishtest.model.ChargeStationsModel;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public interface AuthService {
    @GET("/api/mobile/chargerstations")
    Observable<ChargeStationsModel> chargerstations();

    @POST("/api/mobile/charge_price_anticipate")
    Observable<ChargePriceAnticipateModel> charge_price_anticipate(@Body Map<String, String> parameters);
}

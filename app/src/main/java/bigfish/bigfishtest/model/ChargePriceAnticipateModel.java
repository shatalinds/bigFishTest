package bigfish.bigfishtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class ChargePriceAnticipateModel implements Serializable {
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("data")
    @Expose
    private ChargePriceAnticipate data;

    @SerializedName("error")
    @Expose
    private Object error;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public ChargePriceAnticipate getData() {
        return data;
    }

    public void setData(ChargePriceAnticipate data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}

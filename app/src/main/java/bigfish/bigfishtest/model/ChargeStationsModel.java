package bigfish.bigfishtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class ChargeStationsModel {
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("data")
    @Expose
    private List<ChangeStations> data = null;

    @SerializedName("error")
    @Expose
    private Object error;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<ChangeStations> getData() {
        return data;
    }

    public void setData(List<ChangeStations> data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}

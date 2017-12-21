package bigfish.bigfishtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class ChangeStations implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("charge_box_identity")
    @Expose
    private String chargeBoxIdentity;

    @SerializedName("charge_point_serial_number")
    @Expose
    private Object chargePointSerialNumber;

    @SerializedName("coordinates")
    @Expose
    private Object coordinates;

    @SerializedName("max_power")
    @Expose
    private String maxPower;

    @SerializedName("name_en")
    @Expose
    private Object nameEn;

    @SerializedName("name_ru")
    @Expose
    private Object nameRu;

    @SerializedName("connectors")
    @Expose
    private List<Connector> connectors = null;

    @SerializedName("facilities")
    @Expose
    private List<Facility> facilities = null;

    @SerializedName("registration_status")
    @Expose
    private RegistrationStatus registrationStatus;

    @SerializedName("bill_rate")
    @Expose
    private Double billRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChargeBoxIdentity() {
        return chargeBoxIdentity;
    }

    public void setChargeBoxIdentity(String chargeBoxIdentity) {
        this.chargeBoxIdentity = chargeBoxIdentity;
    }

    public Object getChargePointSerialNumber() {
        return chargePointSerialNumber;
    }

    public void setChargePointSerialNumber(Object chargePointSerialNumber) {
        this.chargePointSerialNumber = chargePointSerialNumber;
    }

    public Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public String getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(String maxPower) {
        this.maxPower = maxPower;
    }

    public Object getNameEn() {
        return nameEn;
    }

    public void setNameEn(Object nameEn) {
        this.nameEn = nameEn;
    }

    public Object getNameRu() {
        return nameRu;
    }

    public void setNameRu(Object nameRu) {
        this.nameRu = nameRu;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Double getBillRate() {
        return billRate;
    }

    public void setBillRate(Double billRate) {
        this.billRate = billRate;
    }
}
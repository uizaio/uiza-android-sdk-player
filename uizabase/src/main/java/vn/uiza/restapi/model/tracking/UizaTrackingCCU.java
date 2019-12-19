package vn.uiza.restapi.model.tracking;

/**
 * Created by loitp on 18/1/2019.
 */

import com.google.gson.annotations.SerializedName;

public class UizaTrackingCCU {

    @SerializedName("dt")
    private String dt;
    @SerializedName("ho")
    private String ho;
    @SerializedName("ai")
    private String ai;
    @SerializedName("sn")
    private String sn;
    @SerializedName("di")
    private String di;
    @SerializedName("ua")
    private String ua;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

}


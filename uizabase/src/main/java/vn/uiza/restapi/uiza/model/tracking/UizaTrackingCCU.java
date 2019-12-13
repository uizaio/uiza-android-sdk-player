package vn.uiza.restapi.uiza.model.tracking;

/**
 * Created by loitp on 18/1/2019.
 */

import com.squareup.moshi.Json;

public class UizaTrackingCCU {

    @Json(name = "dt")
    private String dt;
    @Json(name = "ho")
    private String ho;
    @Json(name = "ai")
    private String ai;
    @Json(name = "sn")
    private String sn;
    @Json(name = "di")
    private String di;
    @Json(name = "ua")
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


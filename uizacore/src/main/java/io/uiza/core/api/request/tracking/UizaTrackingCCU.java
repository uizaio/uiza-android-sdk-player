package io.uiza.core.api.request.tracking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UizaTrackingCCU {

    @SerializedName("dt")
    @Expose
    private String dt;
    @SerializedName("ho")
    @Expose
    private String ho;
    @SerializedName("ai")
    @Expose
    private String ai;
    @SerializedName("sn")
    @Expose
    private String sn;
    @SerializedName("di")
    @Expose
    private String di;
    @SerializedName("ua")
    @Expose
    private String ua;

    private UizaTrackingCCU(Builder builder) {
        dt = builder.dt;
        ho = builder.ho;
        ai = builder.ai;
        sn = builder.sn;
        di = builder.di;
        ua = builder.ua;
    }

    public static final class Builder {

        private String dt;
        private String ho;
        private String ai;
        private String sn;
        private String di;
        private String ua;

        public Builder() {
        }

        public Builder dt(String val) {
            dt = val;
            return this;
        }

        public Builder ho(String val) {
            ho = val;
            return this;
        }

        public Builder ai(String val) {
            ai = val;
            return this;
        }

        public Builder sn(String val) {
            sn = val;
            return this;
        }

        public Builder di(String val) {
            di = val;
            return this;
        }

        public Builder ua(String val) {
            ua = val;
            return this;
        }

        public UizaTrackingCCU build() {
            return new UizaTrackingCCU(this);
        }
    }
}


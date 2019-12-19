package vn.uiza.restapi.model.v5.vod;

import com.google.gson.annotations.SerializedName;

public class PublishVODStatusResponse {
    @SerializedName("process")
    String process;
    @SerializedName("status")
    String status;


    public String getProcess() {
        return process;
    }

    public String getStatus() {
        return status;
    }
}

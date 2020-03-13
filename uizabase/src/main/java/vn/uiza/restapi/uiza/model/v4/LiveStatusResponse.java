package vn.uiza.restapi.uiza.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStatusResponse {
    @SerializedName("data")
    @Expose
    CheckLiveStatus checkLiveStatus;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("policy")
    @Expose
    private String policy;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("type")
    @Expose
    private String type;


    public CheckLiveStatus getCheckLiveStatus() {
        return checkLiveStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPolicy() {
        return policy;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}

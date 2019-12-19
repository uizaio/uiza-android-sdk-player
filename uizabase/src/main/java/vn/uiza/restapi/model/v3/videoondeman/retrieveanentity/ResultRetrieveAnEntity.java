package vn.uiza.restapi.model.v3.videoondeman.retrieveanentity;

/**
 * Created by loitp on 6/21/2018.
 */

import com.google.gson.annotations.SerializedName;

import vn.uiza.restapi.model.v3.metadata.getdetailofmetadata.Data;

public class ResultRetrieveAnEntity {

    @SerializedName("data")
    private Data data;
    @SerializedName("version")
    private Integer version;
    @SerializedName("datetime")
    private String datetime;
    @SerializedName("policy")
    private String policy;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("serviceName")
    private String serviceName;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private Integer code;
    @SerializedName("type")
    private String type;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
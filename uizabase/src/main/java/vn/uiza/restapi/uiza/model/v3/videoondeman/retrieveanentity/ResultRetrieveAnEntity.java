package vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity;

/**
 * Created by loitp on 6/21/2018.
 */

import com.squareup.moshi.Json;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class ResultRetrieveAnEntity {

    @Json(name = "data")
    private Data data;
    @Json(name = "version")
    private Integer version;
    @Json(name = "datetime")
    private String datetime;
    @Json(name = "policy")
    private String policy;
    @Json(name = "requestId")
    private String requestId;
    @Json(name = "serviceName")
    private String serviceName;
    @Json(name = "message")
    private String message;
    @Json(name = "code")
    private Integer code;
    @Json(name = "type")
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
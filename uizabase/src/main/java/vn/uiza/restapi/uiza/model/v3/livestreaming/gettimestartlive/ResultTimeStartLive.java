
package vn.uiza.restapi.uiza.model.v3.livestreaming.gettimestartlive;

import com.squareup.moshi.Json;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class ResultTimeStartLive {

    @Json(name = "data")
    private Data data;
    @Json(name = "version")
    private Long version;
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
    private Long code;
    @Json(name = "type")
    private String type;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

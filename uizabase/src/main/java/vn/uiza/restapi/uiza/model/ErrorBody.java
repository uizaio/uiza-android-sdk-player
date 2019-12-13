
package vn.uiza.restapi.uiza.model;

import com.squareup.moshi.Json;

public class ErrorBody {

    @Json(name = "code")
    private long code;
    @Json(name = "type")
    private String type;
    @Json(name = "data")
    private Object data;
    @Json(name = "retryable")
    private boolean retryable;
    @Json(name = "message")
    private String message;
    @Json(name = "version")
    private long version;
    @Json(name = "datetime")
    private String datetime;
    @Json(name = "policy")
    private String policy;
    @Json(name = "requestId")
    private String requestId;
    @Json(name = "serviceName")
    private String serviceName;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
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

}

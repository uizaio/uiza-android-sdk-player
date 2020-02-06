
package vn.uiza.models;


import com.google.gson.annotations.SerializedName;

public class ErrorBody {

    @SerializedName("code")
    private long code;
    @SerializedName("type")
    private String type;
    @SerializedName("data")
    private Object data;
    @SerializedName("retryable")
    private boolean retryable;
    @SerializedName("message")
    private String message;
    @SerializedName("version")
    private long version;
    @SerializedName("datetime")
    private String datetime;
    @SerializedName("policy")
    private String policy;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("serviceName")
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

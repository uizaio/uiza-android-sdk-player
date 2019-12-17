
package vn.uiza.restapi.uiza.model.v4.subtitle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.Metadata;

public class ResultGetSubtitles {
    @SerializedName("data")
    private List<Subtitle> data = null;
    @SerializedName("metadata")
    private Metadata metadata;
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

    public List<Subtitle> getData() {
        return data;
    }

    public void setData(List<Subtitle> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
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


package vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata;

import com.squareup.moshi.Json;

import java.util.List;

import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.Metadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class ResultGetListMetadata {

    @Json(name = "data")
    private List<Data> data = null;
    @Json(name = "metadata")
    private Metadata metadata;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
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

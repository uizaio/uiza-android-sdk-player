
package vn.uiza.restapi.model.v2.listallentity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllEntity {

    @SerializedName("data")
    private List<Item> data = null;
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("version")
    private double version;
    @SerializedName("datetime")
    private String datetime;
    @SerializedName("name")
    private String name;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private double code;
    @SerializedName("type")
    private String type;

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

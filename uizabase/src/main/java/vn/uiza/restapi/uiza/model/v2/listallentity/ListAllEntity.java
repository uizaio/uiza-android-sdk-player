
package vn.uiza.restapi.uiza.model.v2.listallentity;

import com.squareup.moshi.Json;

import java.util.List;

public class ListAllEntity {

    @Json(name = "data")
    private List<Item> data = null;
    @Json(name = "metadata")
    private Metadata metadata;
    @Json(name = "version")
    private double version;
    @Json(name = "datetime")
    private String datetime;
    @Json(name = "name")
    private String name;
    @Json(name = "message")
    private String message;
    @Json(name = "code")
    private double code;
    @Json(name = "type")
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

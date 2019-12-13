
package vn.uiza.restapi.uiza.model.v2.auth;

import com.squareup.moshi.Json;

public class Auth {

    @Json(name = "data")
    private Data data;
    @Json(name = "version")
    private int version;
    @Json(name = "datetime")
    private String datetime;
    @Json(name = "name")
    private String name;
    @Json(name = "message")
    private String message;
    @Json(name = "code")
    private int code;
    @Json(name = "type")
    private String type;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

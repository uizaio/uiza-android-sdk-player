
package vn.uiza.restapi.uiza.model.v2.getdetailentity;

import com.squareup.moshi.Json;

import java.util.List;

import vn.uiza.restapi.uiza.model.v2.listallentity.Item;

public class GetDetailEntity {

    @Json(name = "data")
    private List<Item> itemList = null;
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

    public List<Item> getData() {
        return itemList;
    }

    public void setData(List<Item> itemList) {
        this.itemList = itemList;
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
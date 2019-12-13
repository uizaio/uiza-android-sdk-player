package vn.uiza.restapi.uiza.model.v2.search;

/**
 * Created by LENOVO on 3/7/2018.
 */

import com.squareup.moshi.Json;

import java.util.List;

import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v2.listallentity.Metadata;

public class Search {

    @Json(name = "data")
    private List<Item> itemList = null;
    @Json(name = "metadata")
    private Metadata metadata;
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
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
package vn.uiza.restapi.uiza.model.v3.metadata.createmetadata;

/**
 * Created by LENOVO on 6/21/2018.
 */

import com.squareup.moshi.Json;

public class CreateMetadata {

    public final static String TYPE_FOLDER = "folder";
    public final static String TYPE_PLAYLIST = "playlist";
    public final static String TYPE_TAG = "tag";

    @Json(name = "id")
    private String id;
    @Json(name = "name")
    private String name;
    @Json(name = "type")
    private String type;
    @Json(name = "description")
    private String description;
    @Json(name = "orderNumber")
    private Integer orderNumber;
    @Json(name = "icon")
    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
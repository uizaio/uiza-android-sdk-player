package vn.uiza.restapi.uiza.model.v3.metadata.createmetadata;

/**
 * Created by LENOVO on 6/21/2018.
 */

import com.google.gson.annotations.SerializedName;

public class CreateMetadata {

    public final static String TYPE_FOLDER = "folder";
    public final static String TYPE_PLAYLIST = "playlist";
    public final static String TYPE_TAG = "tag";

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("description")
    private String description;
    @SerializedName("orderNumber")
    private Integer orderNumber;
    @SerializedName("icon")
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
package vn.uiza.restapi.model.v2.listallentity;

/**
 * Created by LENOVO on 3/21/2018.
 */

import androidx.annotation.IntDef;


import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Subtitle {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;
    @SerializedName("mine")
    private String mine;
    @SerializedName("language")
    private String language;
    @SerializedName("isDefault")
    private int isDefault;
    @SerializedName("status")
    private @Status
    int status;
    @SerializedName("createAt")
    private String createdAt;
    @SerializedName("updateAt")
    private String updatedAt;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMine() {
        return mine;
    }

    public void setMine(String mine) {
        this.mine = mine;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Status
    public int getStatus() {
        return status;
    }

    public void setStatus(@Status int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.DISABLE, Status.ENABLE})
    public @interface Status {
        int DISABLE = 0;
        int ENABLE = 1;
    }

}
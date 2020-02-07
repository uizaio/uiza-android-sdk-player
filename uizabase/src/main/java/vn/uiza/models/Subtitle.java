package vn.uiza.models;

/**
 * Created by NamNd on 1/21/2020.
 */

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Subtitle implements Parcelable {

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
    @SerializedName("is_default")
    private int isDefault;
    @SerializedName("status")
    @StatusValues
    private int status;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    protected Subtitle(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        url = in.readString();
        mine = in.readString();
        language = in.readString();
        isDefault = in.readInt();
        status = in.readInt();
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeString(mine);
        dest.writeString(language);
        dest.writeInt(isDefault);
        dest.writeInt(status);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subtitle> CREATOR = new Creator<Subtitle>() {
        @Override
        public Subtitle createFromParcel(Parcel in) {
            return new Subtitle(in);
        }

        @Override
        public Subtitle[] newArray(int size) {
            return new Subtitle[size];
        }
    };

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

    @StatusValues
    public int getStatus() {
        return status;
    }

    public void setStatus(@StatusValues int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public interface Status {
        int DISABLE = 0;
        int ENABLE = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.DISABLE, Status.ENABLE})
    public @interface StatusValues {
    }

}
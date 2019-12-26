package vn.uiza.restapi.model.v5.vod;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import vn.uiza.restapi.model.v5.PlaybackInfo;
import vn.uiza.restapi.model.v5.UizaPlayback;

public class VODEntity implements Parcelable {

    /**
     * Unique identifier for the object.
     */
    @SerializedName("id")
    String id;
    /**
     * The video's name, meant to be displayable to end users.
     */
    @SerializedName("name")
    String name;
    /**
     * The video's description, meant to be displayable to end users. Can contain up to 65,535 characters.
     */
    @SerializedName("description")
    String description;
    /**
     * The video's short description, meant to be displayable to end users. Can contain up to 250 characters.
     */
    @SerializedName("short_description")
    String shortDescription;
    /**
     * Total times this entity has been viewed.
     */
    @SerializedName("view")
    String view;

    /**
     * The video's poster URL, meant to be displayable to end users.
     */
    @SerializedName("poster")
    String poster;
    /**
     * The video's thumbnail URL, meant to be displayable to end users.
     */
    @SerializedName("thumbnail")
    String thumbnail;
    /**
     * Has 2 types: VOD and AOD
     */
    @SerializedName("type")
    VODType type;
    /**
     * Duration of entity in seconds. Measured up to oneth million of a second.
     */
    @SerializedName("duration")
    String duration;
    /**
     * Status of published task, possible values are [ queue, not-ready, success, failed ]
     */
    @SerializedName("publish_to_cdn")
    String publishToCdn;

    @SerializedName("playback")
    UizaPlayback playback;

    /**
     * A set of predefined key-value pairs that you can attach to a video object,
     * meant to be displayable to end users.
     * See {@link EmbeddedMetadata}.
     */
    @SerializedName("embedded_metadata")
    EmbeddedMetadata embeddedMetadata;

    /**
     * The time at which the entity was created. The format is ISO 8601.
     */
    @SerializedName("created_at")
    Date createdAt;
    /**
     * The time at which the entity was last edited. The format is ISO 8601.
     */
    @SerializedName("updated_at")
    Date updatedAt;

    // default constructor
    public VODEntity() {
    }


    protected VODEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        shortDescription = in.readString();
        view = in.readString();
        poster = in.readString();
        thumbnail = in.readString();
        int typeInt = in.readInt();
        type = VODType.values()[typeInt];
        duration = in.readString();
        publishToCdn = in.readString();
        embeddedMetadata = in.readParcelable(EmbeddedMetadata.class.getClassLoader());
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(shortDescription);
        dest.writeString(view);
        dest.writeString(poster);
        dest.writeString(thumbnail);
        int typeInt = type == null ? -1 : type.ordinal();
        dest.writeInt(typeInt);
        dest.writeString(duration);
        dest.writeString(publishToCdn);
        dest.writeParcelable(embeddedMetadata, flags);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());

    }

    public static final Creator<VODEntity> CREATOR = new Creator<VODEntity>() {
        @Override
        public VODEntity createFromParcel(Parcel in) {
            return new VODEntity(in);
        }

        @Override
        public VODEntity[] newArray(int size) {
            return new VODEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getView() {
        return view;
    }

    public String getPoster() {
        return poster;
    }

    public String getThumbnail() {
        return thumbnail == null ? poster : thumbnail;
    }

    public VODType getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public String getPublishToCdn() {
        return publishToCdn;
    }

    public UizaPlayback getPlayback() {
        return playback;
    }

    public EmbeddedMetadata getEmbeddedMetadata() {
        return embeddedMetadata;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }


    public PlaybackInfo getPlaybackInfo() {
        return new PlaybackInfo(this);
    }
}

package vn.uiza.models.live;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LiveSession implements Parcelable {

    @SerializedName("id")
    String id;
    @SerializedName("entity_id")
    String entityId;
    @SerializedName("stream_key")
    String streamKey;
    @SerializedName("server")
    String server;
    @SerializedName("duration")
    float duration;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updateAt;

    //default constructor
    public LiveSession() {
    }

    protected LiveSession(Parcel in) {
        id = in.readString();
        entityId = in.readString();
        streamKey = in.readString();
        server = in.readString();
        duration = in.readFloat();
        createdAt = new Date(in.readLong());
        updateAt = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(entityId);
        dest.writeString(streamKey);
        dest.writeString(server);
        dest.writeFloat(duration);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updateAt.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveSession> CREATOR = new Creator<LiveSession>() {
        @Override
        public LiveSession createFromParcel(Parcel in) {
            return new LiveSession(in);
        }

        @Override
        public LiveSession[] newArray(int size) {
            return new LiveSession[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public String getServer() {
        return server;
    }

    public float getDuration() {
        return duration;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}

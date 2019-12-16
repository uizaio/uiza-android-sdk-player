package vn.uiza.restapi.uiza.model.v5;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.Date;

public class LiveEntity implements Parcelable {
    @Json(name = "id")
    private String id;
    @Json(name = "name")
    String name;
    @Json(name = "description")
    String description;
    @Json(name = "ingest")
    LiveIngest ingest;
    @Json(name = "playback")
    LivePlayback playback;
    @Json(name = "region")
    String region;
    @Json(name = "status")
    String status;
    @Json(name = "broadcast")
    String broadcast;
    @Json(name = "created_at")
    Date createdAt;
    @Json(name = "updated_at")
    Date updatedAt;

    protected LiveEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        region = in.readString();
        status = in.readString();
        broadcast = in.readString();
        ingest = in.readParcelable(LiveIngest.class.getClassLoader());
        playback = in.readParcelable(LivePlayback.class.getClassLoader());
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(region);
        dest.writeString(status);
        dest.writeString(broadcast);
        dest.writeParcelable(ingest, flags);
        dest.writeParcelable(playback, flags);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveEntity> CREATOR = new Creator<LiveEntity>() {
        @Override
        public LiveEntity createFromParcel(Parcel in) {
            return new LiveEntity(in);
        }

        @Override
        public LiveEntity[] newArray(int size) {
            return new LiveEntity[size];
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

    public LiveIngest getIngest() {
        return ingest;
    }

    public LivePlayback getPlayback() {
        return playback;
    }

    public String getRegion() {
        return region;
    }

    public String getStatus() {
        return status;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean canLive() {
        return ingest != null && ingest.canLive();
    }
}

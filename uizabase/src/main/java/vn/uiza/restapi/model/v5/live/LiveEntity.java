package vn.uiza.restapi.model.v5.live;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import vn.uiza.restapi.model.v5.UizaPlayback;

public class LiveEntity implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("ingest")
    LiveIngest ingest;
    @SerializedName("playback")
    LivePlayback playback;
    @SerializedName("region")
    String region;
    @SerializedName("status")
    LiveStatus status;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("updated_at")
    Date updatedAt;

    public LiveEntity() {
    }

    protected LiveEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        region = in.readString();
        status = LiveStatus.valueOf(in.readString());
        ingest = in.readParcelable(LiveIngest.class.getClassLoader());
        playback = in.readParcelable(UizaPlayback.class.getClassLoader());
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(region);
        if (status != null)
            dest.writeString(status.name());
        else dest.writeString("");
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

    public LiveStatus getStatus() {
        return status;
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

    public boolean isOnline() {
        return status == LiveStatus.BROADCASTING;
    }

    public boolean isPlayback() {
        return playback != null && playback.getHls() != null;
    }

    public UizaPlayback getUizaPlayback() {
        if (playback != null)
            return new UizaPlayback(this);
        return null;
    }

}

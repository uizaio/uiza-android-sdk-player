package vn.uiza.restapi.model.v5;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.model.v5.vod.VODEntity;

public class UizaPlayback implements Parcelable {

    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("thumbnail")
    String thumbnail;
    @SerializedName("channel_name")
    String channelName;
    @SerializedName("last_feed_id")
    String lastFeedId;
    @SerializedName("created_at")
    Date createdAt;
    @SerializedName("hls")
    String hls;
    @SerializedName("is_live")
    boolean live;

    public UizaPlayback(){
        this.live = false;
    }

    public UizaPlayback(LiveEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdAt = entity.getCreatedAt();
        this.hls = entity.getPlayback() == null ? null : entity.getPlayback().getHls();
        this.live = true;
    }

    public UizaPlayback(VODEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdAt = entity.getCreatedAt();
        this.hls = entity.getPlayback() == null ? null : entity.getPlayback().getHls();
        this.live = false;
    }

    protected UizaPlayback(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        createdAt = new Date(in.readLong());
        hls = in.readString();
        live = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(createdAt.getTime());
        dest.writeString(hls);
        dest.writeInt(live ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UizaPlayback> CREATOR = new Creator<UizaPlayback>() {
        @Override
        public UizaPlayback createFromParcel(Parcel in) {
            return new UizaPlayback(in);
        }

        @Override
        public UizaPlayback[] newArray(int size) {
            return new UizaPlayback[size];
        }
    };

    public void setHls(String hls) {
        this.hls = hls;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getId() {
        return id;
    }

    public boolean isLive() {
        return live;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLastFeedId() {
        return lastFeedId;
    }

    public String getHls() {
        return hls;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean canPlay() {
        return !TextUtils.isEmpty(hls);
    }
}

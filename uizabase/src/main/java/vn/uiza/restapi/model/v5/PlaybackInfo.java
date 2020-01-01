package vn.uiza.restapi.model.v5;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.model.v5.vod.VODEntity;

public class PlaybackInfo implements Parcelable {

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
    @SerializedName("hls_ts")
    String hlsTs;
    @SerializedName("mpd")
    String mpd;
    @SerializedName("is_live")
    boolean live;

    public PlaybackInfo() {
        this.live = false;
    }

    public PlaybackInfo(LiveEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdAt = entity.getCreatedAt();
        UizaPlayback playback = entity.getPlayback();
        if (playback != null) {
            this.hls = playback.getHls();
            this.hlsTs = playback.getHlsTs();
            this.mpd = playback.getMpd();
        }
        this.live = true;
    }

    public PlaybackInfo(VODEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.createdAt = entity.getCreatedAt();
        UizaPlayback playback = entity.getPlayback();
        if (playback != null) {
            this.hls = playback.getHls();
            this.hlsTs = playback.getHlsTs();
            this.mpd = playback.getMpd();
        }
        this.live = false;
    }

    protected PlaybackInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        createdAt = new Date(in.readLong());
        hls = in.readString();
        hlsTs = in.readString();
        mpd = in.readString();
        live = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(createdAt.getTime());
        dest.writeString(hls);
        dest.writeString(hlsTs);
        dest.writeString(mpd);
        dest.writeInt(live ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaybackInfo> CREATOR = new Creator<PlaybackInfo>() {
        @Override
        public PlaybackInfo createFromParcel(Parcel in) {
            return new PlaybackInfo(in);
        }

        @Override
        public PlaybackInfo[] newArray(int size) {
            return new PlaybackInfo[size];
        }
    };

    public void setUizaPlayback(UizaPlayback playback) {
        this.hls = playback.getHls();
        this.hlsTs = playback.getHlsTs();
        this.mpd = playback.getMpd();
    }

    public List<String> getUrls() {
        List<String> urls = new ArrayList<>();
        if (!TextUtils.isEmpty(this.hls))
            urls.add(this.hls);
        if (!TextUtils.isEmpty(this.hlsTs))
            urls.add(this.hlsTs);
        if (!TextUtils.isEmpty(this.mpd))
            urls.add(this.mpd);
        return urls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public void setHlsTs(String hlsTs) {
        this.hlsTs = hlsTs;
    }

    public void setMpd(String mpd) {
        this.mpd = mpd;
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
        return !TextUtils.isEmpty(hls) || !TextUtils.isEmpty(hlsTs) || !TextUtils.isEmpty(mpd);
    }

    public String getLinkPlay() {
        if (!TextUtils.isEmpty(hls)) {
            return hls;
        } else if (!TextUtils.isEmpty(hlsTs)) {
            return hlsTs;
        } else {
            return mpd;
        }
    }
}

package vn.uiza.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * "playback": {
 * "hls": "https://c1851eb25f.streamwiz.dev/live/c1851e6e-7e4f-4f8e-b86d-8e277248b25f/playlist.m3u8",
 * "hls_ts": "https://c1851eb25f.streamwiz.dev/live/c1851e6e-7e4f-4f8e-b86d-8e277248b25f/playlist_ts.m3u8",
 * "mpd": "https://c1851eb25f.streamwiz.dev/live/c1851e6e-7e4f-4f8e-b86d-8e277248b25f/manifest.mpd",
 * },
 */
public class UizaPlayback implements Parcelable {

    @SerializedName("hls")
    String hls;
    @SerializedName("hls_ts")
    String hlsTs;
    @SerializedName("mpd")
    String mpd;

    public UizaPlayback() {
        //
    }

    public UizaPlayback(String hls, String hlsTs, String mpd){
        this.hls = hls;
        this.hlsTs = hlsTs;
        this.mpd = mpd;
    }

    protected UizaPlayback(Parcel in) {
        hls = in.readString();
        hlsTs = in.readString();
        mpd = in.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hls);
        parcel.writeString(hlsTs);
        parcel.writeString(mpd);
    }


    public String getHls() {
        return hls;
    }

    public String getHlsTs() {
        return hlsTs;
    }

    public String getMpd() {
        return mpd;
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

}

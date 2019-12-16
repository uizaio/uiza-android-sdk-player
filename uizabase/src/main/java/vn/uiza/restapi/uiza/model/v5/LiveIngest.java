package vn.uiza.restapi.uiza.model.v5;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.squareup.moshi.Json;

public class LiveIngest implements Parcelable {

    @Json(name = "stream_url")
    String streamUrl;
    @Json(name = "stream_key")
    String streamKey;

    // default constructor
    public LiveIngest() {
    }

    // constructor with optional
    public LiveIngest(String streamUrl, String streamKey) {
        this.streamUrl = streamUrl;
        this.streamKey = streamKey;
    }

    //constructor with Parcel
    protected LiveIngest(Parcel in) {
        streamUrl = in.readString();
        streamKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(streamUrl);
        dest.writeString(streamKey);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveIngest> CREATOR = new Creator<LiveIngest>() {
        @Override
        public LiveIngest createFromParcel(Parcel in) {
            return new LiveIngest(in);
        }

        @Override
        public LiveIngest[] newArray(int size) {
            return new LiveIngest[size];
        }
    };

    public String getStreamKey() {
        return streamKey;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public boolean canLive() {
        return !(TextUtils.isEmpty(streamUrl) || TextUtils.isEmpty(streamKey));
    }

    public String getStreamLink() {
        return String.format("%s/%s", streamUrl, streamKey);
    }
}

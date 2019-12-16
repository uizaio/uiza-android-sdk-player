package vn.uiza.restapi.uiza.model.v5;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class LivePlayback implements Parcelable {

    @Json(name = "hls")
    String hls;

    protected LivePlayback(Parcel in) {
        hls = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LivePlayback> CREATOR = new Creator<LivePlayback>() {
        @Override
        public LivePlayback createFromParcel(Parcel in) {
            return new LivePlayback(in);
        }

        @Override
        public LivePlayback[] newArray(int size) {
            return new LivePlayback[size];
        }
    };

    public String getHls() {
        return hls;
    }
}

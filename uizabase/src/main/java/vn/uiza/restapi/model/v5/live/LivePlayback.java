package vn.uiza.restapi.model.v5.live;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;


public class LivePlayback implements Parcelable {

    @SerializedName("hls")
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

    public boolean canPlay() {
        return !TextUtils.isEmpty(hls);
    }
}

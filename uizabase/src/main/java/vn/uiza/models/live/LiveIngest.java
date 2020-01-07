package vn.uiza.models.live;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class LiveIngest implements Parcelable {

    @SerializedName("key")
    String key;
    @SerializedName("url")
    String url;

    // default constructor
    public LiveIngest() {
    }

    // constructor with optional
    public LiveIngest(String key, String url) {
        this.key = key;
        this.url = url;
    }

    //constructor with Parcel
    protected LiveIngest(Parcel in) {
        key = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(url);
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

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public boolean canLive() {
        return !(TextUtils.isEmpty(key) || TextUtils.isEmpty(url));
    }

    public String getStreamLink() {
        if (canLive())
            return String.format("%s/%s", url, key);
        return null;
    }
}
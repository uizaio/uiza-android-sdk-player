package vn.uiza.restapi.uiza.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckLiveStatus {
    @SerializedName("signalStatus")
    @Expose
    LiveStatus status;

    public LiveStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return status == LiveStatus.AVAILABLE;
    }

    public boolean isOffline() {
        return status == LiveStatus.OFFLINE;
    }
}

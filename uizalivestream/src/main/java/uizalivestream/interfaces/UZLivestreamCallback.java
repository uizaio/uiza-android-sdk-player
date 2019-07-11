package uizalivestream.interfaces;

import uizalivestream.model.PresetLiveStreamingFeed;
import uizalivestream.view.UZLivestream;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface UZLivestreamCallback {
    void onUICreate();

    void onPermission(boolean areAllPermissionsGranted);

    void onError(String reason);

    void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode,
            PresetLiveStreamingFeed presetLiveStreamingFeed);

    void onConnectionSuccessRtmp();

    void onConnectionFailedRtmp(String reason);

    void onDisconnectRtmp();

    void onAuthErrorRtmp();

    void onAuthSuccessRtmp();

    void surfaceCreated();

    void surfaceChanged(UZLivestream.StartPreview startPreview);

    void onBackgroundTooLong();
}

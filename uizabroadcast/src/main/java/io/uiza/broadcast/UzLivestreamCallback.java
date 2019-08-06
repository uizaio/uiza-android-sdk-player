package io.uiza.broadcast;

import io.uiza.broadcast.config.PresetLiveFeed;
import io.uiza.broadcast.util.UzLivestreamError;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface UzLivestreamCallback {

    void onUiCreated();

    void onPermission(boolean areAllPermissionsGranted);

    void onError(UzLivestreamError error);

    void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode,
                          PresetLiveFeed presetLiveFeed);

    void onConnectionSuccessRtmp();

    void onConnectionFailedRtmp(String reason);

    void onDisconnectRtmp();

    void onAuthErrorRtmp();

    void onAuthSuccessRtmp();

    void surfaceCreated();

    void surfaceChanged(UzLivestream.StartPreview startPreview);

    void onBackgroundTooLong();
}

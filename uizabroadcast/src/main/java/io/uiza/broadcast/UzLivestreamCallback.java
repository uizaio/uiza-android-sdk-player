package io.uiza.broadcast;

import io.uiza.broadcast.config.PresetLiveFeed;
import io.uiza.broadcast.util.UzLivestreamError;
import io.uiza.core.api.response.video.VideoData;

public interface UzLivestreamCallback {

    void onUiCreated();

    void onPermission(boolean areAllPermissionsGranted);

    void onError(UzLivestreamError error);

    void onGetDataSuccess(VideoData d, String mainUrl, boolean isTranscode,
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

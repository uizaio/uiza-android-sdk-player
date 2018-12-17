package uizalivestream.uiza;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface UZLivestreamCallback {
    public void onPermission(boolean areAllPermissionsGranted);

    public void onError(String reason);

    public void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode, PresetLiveStreamingFeed presetLiveStreamingFeed);

    public void onConnectionSuccessRtmp();

    public void onConnectionFailedRtmp(String reason);

    public void onDisconnectRtmp();

    public void onAuthErrorRtmp();

    public void onAuthSuccessRtmp();

    public void surfaceCreated();

    public void surfaceChanged(UZLivestream.StartPreview startPreview);
}

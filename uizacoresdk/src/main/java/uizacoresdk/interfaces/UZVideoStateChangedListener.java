package uizacoresdk.interfaces;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface UZVideoStateChangedListener {
    void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay,
            Data data);

    void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    void onVideoProgress(long currentMls, int s, long duration, int percent);

    void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration);

    void onVideoEnded();

    void onError(UZException exception);
}

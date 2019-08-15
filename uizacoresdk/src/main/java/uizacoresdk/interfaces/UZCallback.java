package uizacoresdk.interfaces;

import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;

public interface UZCallback {
    //when video init done with result
    //isInitSuccess onStateReadyFirst
    //isGetDataSuccess da co data ResultGetLinkPlay va LinkPlay
    void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data);

    //when pip video is init success
    void onStateMiniPlayer(boolean isInitMiniPlayerSuccess);

    //when skin is changed
    void onSkinChange();

    //when screen rotate
    void onScreenRotate(boolean isLandscape);

    //when uiimavideo had an error
    void onError(UzException e);

}
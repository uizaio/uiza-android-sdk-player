package uizacoresdk.interfaces;

import vn.uiza.core.exception.UizaException;
import vn.uiza.restapi.model.v5.PlaybackInfo;

public interface UZCallback {
    //when video init done with result
    //isInitSuccess onStateReadyFirst
    //isGetDataSuccess da co data ResultGetLinkPlay va Data
    void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, PlaybackInfo playback);

    //when pip video is init success
    void onStateMiniPlayer(boolean isInitMiniPlayerSuccess);

    //when skin is changed
    void onSkinChange();

    //when screen rotate
    void onScreenRotate(boolean isLandscape);

    //when uiimavideo had an error
    void onError(UizaException e);

}
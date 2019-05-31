package uizacoresdk.interfaces;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * @deprecated use {@link UZPlayerStateChangedListener} or {@link UZVideoStateChangedListener} instead
 */
@Deprecated
public interface UZCallback {
    //when video init done with result
    //isInitSuccess onStateReadyFirst
    //isGetDataSuccess da co data ResultGetLinkPlay va Data
    @Deprecated
    void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay,
            Data data);

    //when pip video is init success
    @Deprecated
    void onStateMiniPlayer(boolean isInitMiniPlayerSuccess);

    //when skin is changed
    @Deprecated
    void onSkinChange();

    //when screen rotate
    @Deprecated
    void onScreenRotate(boolean isLandscape);

    //when uiimavideo had an error
    @Deprecated
    void onError(UZException e);
}

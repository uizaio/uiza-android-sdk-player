package uizacoresdk.interfaces;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface UZCallback {
    //when video init done with result
    //isInitSuccess onStateReadyFirst
    //isGetDataSuccess da co data ResultGetLinkPlay va Data
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data);

    //user click button pip in controller
    //public void onClickPip(Intent intent);

    //when pip video is inited success
    public void onClickPipVideoInitSuccess(boolean isInitSuccess);

    //when skin is changed
    public void onSkinChange();

    //when screen rotate
    public void onScreenRotate(boolean isLandscape);

    //when uiimavideo had an error
    public void onError(UZException e);

}
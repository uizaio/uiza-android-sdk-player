package vn.uiza.core.exception;

public class UizaExceptionUtils {

    public static UizaException getExceptionNoConnection() {
        return new UizaException(UizaException.ERR_CODE_0, UizaException.ERR_0);
    }

    public static UizaException getExceptionCannotGetDetailEntitity() {
        return new UizaException(UizaException.ERR_CODE_1, UizaException.ERR_1);
    }

    public static UizaException getExceptionNoTokenStreaming() {
        return new UizaException(UizaException.ERR_CODE_2, UizaException.ERR_2);
    }

    public static UizaException getExceptionCannotGetLinkPlayLive() {
        return new UizaException(UizaException.ERR_CODE_3, UizaException.ERR_3);
    }

    public static UizaException getExceptionCannotGetLinkPlayVOD() {
        return new UizaException(UizaException.ERR_CODE_4, UizaException.ERR_4);
    }

    public static UizaException getExceptionEntityId() {
        return new UizaException(UizaException.ERR_CODE_5, UizaException.ERR_5);
    }

    public static UizaException getExceptionTryAllLinkPlay() {
        return new UizaException(UizaException.ERR_CODE_6, UizaException.ERR_6);
    }

    public static UizaException getExceptionSetup() {
        return new UizaException(UizaException.ERR_CODE_7, UizaException.ERR_7);
    }

    public static UizaException getExceptionListAllEntity() {
        return new UizaException(UizaException.ERR_CODE_8, UizaException.ERR_8);
    }

    public static UizaException getExceptionChangeSkin() {
        return new UizaException(UizaException.ERR_CODE_9, UizaException.ERR_9);
    }

    public static UizaException getExceptionListHQ() {
        return new UizaException(UizaException.ERR_CODE_10, UizaException.ERR_10);
    }

    public static UizaException getExceptionListAudio() {
        return new UizaException(UizaException.ERR_CODE_11, UizaException.ERR_11);
    }

    public static UizaException getExceptionShowPip() {
        return new UizaException(UizaException.ERR_CODE_19, UizaException.ERR_19);
    }

    public static UizaException getExceptionRetrieveALiveEvent() {
        return new UizaException(UizaException.ERR_CODE_21, UizaException.ERR_21);
    }

    public static UizaException getExceptionPlayback() {
        return new UizaException(UizaException.ERR_CODE_24, UizaException.ERR_24);
    }

    public static UizaException getExceptionPlaylistFolderItemFirst() {
        return new UizaException(UizaException.ERR_CODE_25, UizaException.ERR_25);
    }

    public static UizaException getExceptionPlaylistFolderItemLast() {
        return new UizaException(UizaException.ERR_CODE_26, UizaException.ERR_26);
    }

    public static UizaException getExceptionPlayerInfor() {
        return new UizaException(UizaException.ERR_CODE_27, UizaException.ERR_27);
    }
}

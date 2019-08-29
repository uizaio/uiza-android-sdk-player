package io.uiza.core.exception;

public class UzExceptionUtil {

    public static UzException getExceptionNoConnection() {
        Exception exception = new Exception(UzException.ERR_0);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_0);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionCannotGetDetailEntity() {
        Exception exception = new Exception(UzException.ERR_1);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_1);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionNoTokenStreaming() {
        Exception exception = new Exception(UzException.ERR_2);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_2);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionCannotGetLinkPlayLive() {
        Exception exception = new Exception(UzException.ERR_3);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_3);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionCannotGetLinkPlayVOD() {
        Exception exception = new Exception(UzException.ERR_4);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_4);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionEntityId() {
        Exception exception = new Exception(UzException.ERR_5);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_5);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionTryAllLinkPlay() {
        Exception exception = new Exception(UzException.ERR_6);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_6);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionSetup() {
        Exception exception = new Exception(UzException.ERR_7);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_7);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionListAllEntity() {
        Exception exception = new Exception(UzException.ERR_8);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_8);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionChangeSkin() {
        Exception exception = new Exception(UzException.ERR_9);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_9);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionListHQ() {
        Exception exception = new Exception(UzException.ERR_10);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_10);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionListAudio() {
        Exception exception = new Exception(UzException.ERR_11);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_11);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionShowPip() {
        Exception exception = new Exception(UzException.ERR_19);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_19);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionRetrieveALiveEvent() {
        Exception exception = new Exception(UzException.ERR_21);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_21);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionPlayback() {
        Exception exception = new Exception(UzException.ERR_24);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_24);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionPlaylistFolderItemFirst() {
        Exception exception = new Exception(UzException.ERR_25);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_25);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionPlaylistFolderItemLast() {
        Exception exception = new Exception(UzException.ERR_26);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_26);
        uzException.setException(exception);
        return uzException;
    }

    public static UzException getExceptionPlayerInfo() {
        Exception exception = new Exception(UzException.ERR_27);

        UzException uzException = new UzException();
        uzException.setErrorCode(UzException.ERR_CODE_27);
        uzException.setException(exception);
        return uzException;
    }
}

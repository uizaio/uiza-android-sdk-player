package vn.uiza.core.exception;

public class UizaExceptionUtil {

    public static UizaException getExceptionNoConnection() {
        Exception exception = new Exception(UizaException.ERR_0);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_0);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionCannotGetDetailEntitity() {
        Exception exception = new Exception(UizaException.ERR_1);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_1);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionNoTokenStreaming() {
        Exception exception = new Exception(UizaException.ERR_2);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_2);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionCannotGetLinkPlayLive() {
        Exception exception = new Exception(UizaException.ERR_3);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_3);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionCannotGetLinkPlayVOD() {
        Exception exception = new Exception(UizaException.ERR_4);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_4);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionEntityId() {
        Exception exception = new Exception(UizaException.ERR_5);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_5);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionTryAllLinkPlay() {
        Exception exception = new Exception(UizaException.ERR_6);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_6);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionSetup() {
        Exception exception = new Exception(UizaException.ERR_7);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_7);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionListAllEntity() {
        Exception exception = new Exception(UizaException.ERR_8);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_8);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionChangeSkin() {
        Exception exception = new Exception(UizaException.ERR_9);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_9);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionListHQ() {
        Exception exception = new Exception(UizaException.ERR_10);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_10);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionListAudio() {
        Exception exception = new Exception(UizaException.ERR_11);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_11);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionShowPip() {
        Exception exception = new Exception(UizaException.ERR_19);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_19);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionRetrieveALiveEvent() {
        Exception exception = new Exception(UizaException.ERR_21);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_21);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionPlayback() {
        Exception exception = new Exception(UizaException.ERR_24);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_24);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionPlaylistFolderItemFirst() {
        Exception exception = new Exception(UizaException.ERR_25);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_25);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionPlaylistFolderItemLast() {
        Exception exception = new Exception(UizaException.ERR_26);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_26);
        uzException.setException(exception);
        return uzException;
    }

    public static UizaException getExceptionPlayerInfor() {
        Exception exception = new Exception(UizaException.ERR_27);

        UizaException uzException = new UizaException();
        uzException.setErrorCode(UizaException.ERR_CODE_27);
        uzException.setException(exception);
        return uzException;
    }
}

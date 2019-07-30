package vn.uiza.core.exception;

public class UZExceptionUtil {

    public static UZException getExceptionNoConnection() {
        Exception exception = new Exception(UZException.ERR_0);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_0);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionCannotGetDetailEntity() {
        Exception exception = new Exception(UZException.ERR_1);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_1);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionNoTokenStreaming() {
        Exception exception = new Exception(UZException.ERR_2);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_2);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionCannotGetLinkPlayLive() {
        Exception exception = new Exception(UZException.ERR_3);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_3);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionCannotGetLinkPlayVOD() {
        Exception exception = new Exception(UZException.ERR_4);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_4);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionEntityId() {
        Exception exception = new Exception(UZException.ERR_5);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_5);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionTryAllLinkPlay() {
        Exception exception = new Exception(UZException.ERR_6);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_6);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionSetup() {
        Exception exception = new Exception(UZException.ERR_7);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_7);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionListAllEntity() {
        Exception exception = new Exception(UZException.ERR_8);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_8);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionChangeSkin() {
        Exception exception = new Exception(UZException.ERR_9);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_9);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionListHQ() {
        Exception exception = new Exception(UZException.ERR_10);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_10);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionListAudio() {
        Exception exception = new Exception(UZException.ERR_11);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_11);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionShowPip() {
        Exception exception = new Exception(UZException.ERR_19);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_19);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionRetrieveALiveEvent() {
        Exception exception = new Exception(UZException.ERR_21);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_21);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionPlayback() {
        Exception exception = new Exception(UZException.ERR_24);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_24);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionPlaylistFolderItemFirst() {
        Exception exception = new Exception(UZException.ERR_25);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_25);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionPlaylistFolderItemLast() {
        Exception exception = new Exception(UZException.ERR_26);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_26);
        uzException.setException(exception);
        return uzException;
    }

    public static UZException getExceptionPlayerInfor() {
        Exception exception = new Exception(UZException.ERR_27);

        UZException uzException = new UZException();
        uzException.setErrorCode(UZException.ERR_CODE_27);
        uzException.setException(exception);
        return uzException;
    }
}

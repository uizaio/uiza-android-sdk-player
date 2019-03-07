package vn.uiza.core.exception;

public class UZException {
    public final static int ERR_CODE_0 = 0;
    public final static String ERR_0 = "No internet connection.";

    public final static int ERR_CODE_1 = 1;
    public final static String ERR_1 = "Cannot get detail of entity.";

    public final static int ERR_CODE_2 = 2;
    public final static String ERR_2 = "Token streaming cannot be null or empty.";

    public final static int ERR_CODE_3 = 3;
    public final static String ERR_3 = "Cannot get linkplay of LIVE entity.";

    public final static int ERR_CODE_4 = 4;
    public final static String ERR_4 = "Cannot get linkplay of VOD entity.";

    public final static int ERR_CODE_5 = 5;
    public final static String ERR_5 = "Entity ID cannot be null or empty.";

    public final static int ERR_CODE_6 = 6;
    public final static String ERR_6 = "Tried to play all linkplay of this entity, but failed.";

    public final static int ERR_CODE_7 = 7;
    public final static String ERR_7 = "Setup failed";

    public final static int ERR_CODE_8 = 8;
    public final static String ERR_8 = "Cannot get list all entity.";

    public final static int ERR_CODE_9 = 9;
    public final static String ERR_9 = "You cannot change skin if player is playing ad.";

    public final static int ERR_CODE_10 = 10;
    public final static String ERR_10 = "Error getHQList null";

    public final static int ERR_CODE_11 = 11;
    public final static String ERR_11 = "Error audio null";

    public final static int ERR_CODE_12 = 12;
    public final static String ERR_12 = "Activity cannot be null";

    public final static int ERR_CODE_13 = 13;
    public final static String ERR_13 = "UZVideo cannot be null";

    public final static int ERR_CODE_14 = 14;
    public final static String ERR_14 = "You must init custom linkPlay first.";

    public final static int ERR_CODE_15 = 15;
    public final static String ERR_15 = "Context cannot be null.";

    public final static int ERR_CODE_16 = 16;
    public final static String ERR_16 = "Domain api cannot be null or empty";

    public final static int ERR_CODE_17 = 17;
    public final static String ERR_17 = "Token cannot be null or empty";

    public final static int ERR_CODE_18 = 18;
    public final static String ERR_18 = "AppID cannot be null or empty";

    public final static int ERR_CODE_19 = 19;
    public final static String ERR_19 = "Cannot use this feature at this time";

    public final static int ERR_CODE_20 = 20;
    public final static String ERR_20 = "Cannot chat messenger now";

    public final static int ERR_CODE_21 = 21;
    public final static String ERR_21 = "Cannot get data from api retrieveALiveEvent()";

    public final static int ERR_CODE_22 = 22;
    public final static String ERR_22 = "Cannot find Messenger App";

    public final static int ERR_CODE_23 = 23;
    public final static String ERR_23 = "Data of this entity is invalid";

    public final static int ERR_CODE_24 = 24;
    public final static String ERR_24 = "Error: Playback exception";

    public final static int ERR_CODE_25 = 25;
    public final static String ERR_25 = "This is the first item of playlist/folder";

    public final static int ERR_CODE_26 = 26;
    public final static String ERR_26 = "This is the first item of playlist/folder";

    public final static int ERR_CODE_27 = 27;
    public final static String ERR_27 = "Cannot get player infor";

    public final static int ERR_CODE_400 = 400;
    public final static String ERR_400 = "Bad Request: The request was unacceptable, often due to missing a required parameter.";

    public final static int ERR_CODE_401 = 401;
    public final static String ERR_401 = "Unauthorized: No valid API key provided.";

    public final static int ERR_CODE_404 = 404;
    public final static String ERR_404 = "Not Found: The requested resource does not exist.";

    public final static int ERR_CODE_422 = 422;
    public final static String ERR_422 = "Unprocessable: The syntax of the request entity is incorrect (often is wrong parameter).";

    public final static int ERR_CODE_500 = 500;
    public final static String ERR_500 = "Internal Server Error: We had a problem with our server. Try again later.";

    public final static int ERR_CODE_503 = 503;
    public final static String ERR_503 = "Service Unavailable: The server is overloaded or down for maintenance.";

    private Exception exception;
    private int errorCode;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return "ErrorCode: " + errorCode + " - Message: " + (exception == null ? "null" : exception.getMessage());
    }

    public void printStackTrace() {
        if (exception != null) {
            exception.printStackTrace();
        }
    }
}

package io.uiza.core.exception;

public class UzException {

    public static final int ERR_CODE_0 = 0;
    public static final String ERR_0 = "No internet connection.";

    public static final int ERR_CODE_1 = 1;
    public static final String ERR_1 = "Cannot get detail of entity.";

    public static final int ERR_CODE_2 = 2;
    public static final String ERR_2 = "Token streaming cannot be null or empty.";

    public static final int ERR_CODE_3 = 3;
    public static final String ERR_3 = "Cannot get linkplay of LIVE entity.";

    public static final int ERR_CODE_4 = 4;
    public static final String ERR_4 = "Cannot get linkplay of VOD entity.";

    public static final int ERR_CODE_5 = 5;
    public static final String ERR_5 = "Entity ID cannot be null or empty.";

    public static final int ERR_CODE_6 = 6;
    public static final String ERR_6 = "Tried to play all linkplay of this entity, but failed.";

    public static final int ERR_CODE_7 = 7;
    public static final String ERR_7 = "Setup failed";

    public static final int ERR_CODE_8 = 8;
    public static final String ERR_8 = "Cannot get list all entity.";

    public static final int ERR_CODE_9 = 9;
    public static final String ERR_9 = "You cannot change skin if player is playing ad.";

    public static final int ERR_CODE_10 = 10;
    public static final String ERR_10 = "Error getHQList null";

    public static final int ERR_CODE_11 = 11;
    public static final String ERR_11 = "Error audio null";

    public static final int ERR_CODE_12 = 12;
    public static final String ERR_12 = "Activity cannot be null";

    public static final int ERR_CODE_13 = 13;
    public static final String ERR_13 = "UZVideo cannot be null";

    public static final int ERR_CODE_14 = 14;
    public static final String ERR_14 = "You must init custom linkPlay first.";

    public static final int ERR_CODE_15 = 15;
    public static final String ERR_15 = "Context cannot be null.";

    public static final int ERR_CODE_16 = 16;
    public static final String ERR_16 = "Domain api cannot be null or empty";

    public static final int ERR_CODE_17 = 17;
    public static final String ERR_17 = "Token cannot be null or empty";

    public static final int ERR_CODE_18 = 18;
    public static final String ERR_18 = "AppID cannot be null or empty";

    public static final int ERR_CODE_19 = 19;
    public static final String ERR_19 = "Cannot use this feature at this time";

    public static final int ERR_CODE_20 = 20;
    public static final String ERR_20 = "Cannot chat messenger now";

    public static final int ERR_CODE_21 = 21;
    public static final String ERR_21 = "Cannot get data from api retrieveALiveEvent()";

    public static final int ERR_CODE_22 = 22;
    public static final String ERR_22 = "Cannot find Messenger App";

    public static final int ERR_CODE_23 = 23;
    public static final String ERR_23 = "LinkPlay of this entity is invalid";

    public static final int ERR_CODE_24 = 24;
    public static final String ERR_24 = "Error: Playback exception";

    public static final int ERR_CODE_25 = 25;
    public static final String ERR_25 = "This is the first item of playlist/folder";

    public static final int ERR_CODE_26 = 26;
    public static final String ERR_26 = "This is the first item of playlist/folder";

    public static final int ERR_CODE_27 = 27;
    public static final String ERR_27 = "Cannot get player infor";

    public static final int ERR_CODE_400 = 400;
    public static final String ERR_400 = "Bad Request: The request was unacceptable, often due to missing a required parameter.";

    public static final int ERR_CODE_401 = 401;
    public static final String ERR_401 = "Unauthorized: No valid API key provided.";

    public static final int ERR_CODE_404 = 404;
    public static final String ERR_404 = "Not Found: The requested resource does not exist.";

    public static final int ERR_CODE_422 = 422;
    public static final String ERR_422 = "Unprocessable: The syntax of the request entity is incorrect (often is wrong parameter).";

    public static final int ERR_CODE_500 = 500;
    public static final String ERR_500 = "Internal Server Error: We had a problem with our server. Try again later.";

    public static final int ERR_CODE_503 = 503;
    public static final String ERR_503 = "Service Unavailable: The server is overloaded or down for maintenance.";

    public static final int ERR_CODE_504 = 504;
    public static final String ERR_504 = "Exo Player library is missing";

    public static final int ERR_CODE_505 = 505;
    public static final String ERR_505 = "Chromecast library is missing";

    public static final int ERR_CODE_506 = 506;
    public static final String ERR_506 = "IMA ads library is missing";

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
        return "ErrorCode: " + errorCode + " - Message: "
                + (exception == null ? "null" : exception.getMessage());
    }

    public void printStackTrace() {
        if (exception != null) {
            exception.printStackTrace();
        }
    }
}

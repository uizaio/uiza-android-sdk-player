package io.uiza.broadcast.util;

public enum UzLivestreamError {

    /**
     * It is illegal state when try to access a component while livestream is not initialized.
     */
    LIVESTREAM_NOT_INITIALIZED("Livestream is not initialized"),
    /**
     * This error occurs when the camera is not running or supported in this case.
     */
    CAMERA_NOT_RUNNING("Camera is not run properly"),
    /**
     * This error occurs when the livestream video can not be recorded.
     */
    VIDEO_CAN_NOT_RECORD("Video can not be recorded"),
    /**
     * This error occurs when livestream data is incorrect or has stopped.
     */
    LIVE_INFO_INCORRECT("The live info is incorrect"),
    /**
     * This error occurs when can not get live info.
     */
    CAN_NOT_GET_INFO("Can not get live info");

    private String reason;

    UzLivestreamError(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

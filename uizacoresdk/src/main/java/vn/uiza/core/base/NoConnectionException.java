package vn.uiza.core.base;

/**
 * Created by LENOVO on 5/21/2018.
 */

public class NoConnectionException extends Exception {

    public NoConnectionException(String message) {
        super(message);
    }

    public NoConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

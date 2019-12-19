package vn.uiza.restapi;

public interface CallbackResponse<T> {
    void onSuccess(T data);
    void onError(Throwable e);
}

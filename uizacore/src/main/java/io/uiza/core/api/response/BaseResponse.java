package io.uiza.core.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("policy")
    @Expose
    private String policy;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("env")
    @Expose
    private String env;

    public T getData() {
        return data;
    }
}

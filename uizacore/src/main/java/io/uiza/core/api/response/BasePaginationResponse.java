package io.uiza.core.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasePaginationResponse<T> extends BaseResponse<T> {

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public Metadata getMetadata() {
        return metadata;
    }
}

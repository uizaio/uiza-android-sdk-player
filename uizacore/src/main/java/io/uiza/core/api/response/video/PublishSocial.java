package io.uiza.core.api.response.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class PublishSocial {

    @SerializedName("dropdown")
    @Expose
    private String dropdown;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("streamKey")
    @Expose
    private String streamKey;
}

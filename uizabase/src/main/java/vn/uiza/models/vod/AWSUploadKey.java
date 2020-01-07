package vn.uiza.models.vod;

import com.google.gson.annotations.SerializedName;

public class AWSUploadKey {

    @SerializedName("temp_expire_at")
    String tempExpireAt;
    @SerializedName("temp_access_id")
    String tempAccessId;
    @SerializedName("bucket_name")
    String bucketName;
    @SerializedName("temp_session_token")
    String tempSessionToken;
    @SerializedName("region_name")
    String regionName;
    @SerializedName("temp_access_secret")
    String tempAccessSecret;


    public String getTempExpireAt() {
        return tempExpireAt;
    }

    public String getTempAccessId() {
        return tempAccessId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getTempSessionToken() {
        return tempSessionToken;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getTempAccessSecret() {
        return tempAccessSecret;
    }
}

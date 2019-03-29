package uizalivestream.data;

import vn.uiza.core.common.Constants;

public class UZLivestreamData {
    private static final UZLivestreamData ourInstance = new UZLivestreamData();

    public static UZLivestreamData getInstance() {
        return ourInstance;
    }

    private UZLivestreamData() {
    }

    private String mAppId;
    private int mAPIVersion = Constants.API_VERSION_3;

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getAPIVersion() {
        return "v" + mAPIVersion;
    }

    public void setAPIVersion(int APIVersion) {
        this.mAPIVersion = APIVersion;
    }
}

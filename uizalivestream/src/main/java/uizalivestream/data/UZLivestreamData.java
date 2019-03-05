package uizalivestream.data;

public class UZLivestreamData {
    private static final UZLivestreamData ourInstance = new UZLivestreamData();

    public static UZLivestreamData getInstance() {
        return ourInstance;
    }

    private UZLivestreamData() {
    }

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}

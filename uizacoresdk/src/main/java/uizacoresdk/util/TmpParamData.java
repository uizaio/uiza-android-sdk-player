package uizacoresdk.util;

public class TmpParamData {
    private static final TmpParamData ourInstance = new TmpParamData();

    public static TmpParamData getInstance() {
        return ourInstance;
    }

    private TmpParamData() {
    }

    public void clearAll() {
        entityCnd = "";
    }

    private String entityCnd = "";
    private String entityContentType = "";

    public String getEntityCnd() {
        return entityCnd;
    }

    public void setEntityCnd(String entityCnd) {
        this.entityCnd = entityCnd;
    }

    public String getEntityContentType() {
        return entityContentType;
    }

    public void setEntityContentType(String entityContentType) {
        this.entityContentType = entityContentType;
    }
}

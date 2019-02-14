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
    private String entityContentType = "video";//TODO correct
    private String entityDuration = "";//TODO correct
    private String entityEncodingVariant = "";//TODO correct
    private String entityLanguageCode = "";//TODO correct
    private String entityPosterUrl;
    private String entityProducer;//TODO correct
    private String entitySeries;//TODO correct
    private String entitySourceDomain;//TODO correct

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

    public String getEntityDuration() {
        return entityDuration;
    }

    public void setEntityDuration(String entityDuration) {
        this.entityDuration = entityDuration;
    }

    public String getEntityEncodingVariant() {
        return entityEncodingVariant;
    }

    public void setEntityEncodingVariant(String entityEncodingVariant) {
        this.entityEncodingVariant = entityEncodingVariant;
    }

    public String getEntityLanguageCode() {
        return entityLanguageCode;
    }

    public void setEntityLanguageCode(String entityLanguageCode) {
        this.entityLanguageCode = entityLanguageCode;
    }

    public String getEntityPosterUrl() {
        return entityPosterUrl;
    }

    public void setEntityPosterUrl(String entityPosterUrl) {
        this.entityPosterUrl = entityPosterUrl;
    }

    public String getEntityProducer() {
        return entityProducer;
    }

    public void setEntityProducer(String entityProducer) {
        this.entityProducer = entityProducer;
    }

    public String getEntitySeries() {
        return entitySeries;
    }

    public void setEntitySeries(String entitySeries) {
        this.entitySeries = entitySeries;
    }

    public String getEntitySourceDomain() {
        return entitySourceDomain;
    }

    public void setEntitySourceDomain(String entitySourceDomain) {
        this.entitySourceDomain = entitySourceDomain;
    }
}

package vn.uiza.models.auth;

/**
 * Created by LENOVO on 3/23/2018.
 */


public class JsonBodyAuth {
    private String accessKeyId;
    private String secretKeyId;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretKeyId() {
        return secretKeyId;
    }

    public void setSecretKeyId(String secretKeyId) {
        this.secretKeyId = secretKeyId;
    }
}

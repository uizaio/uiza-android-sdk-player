package vn.uiza.restapi.uiza.model.v3.drm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LicenseAcquisitionUrl {

    @SerializedName("licenseAcquisitionUrl")
    @Expose
    private String licenseAcquisitionUrl;

    public String getLicenseAcquisitionUrl() {
        return licenseAcquisitionUrl;
    }

    public void setLicenseAcquisitionUrl(String licenseAcquisitionUrl) {
        this.licenseAcquisitionUrl = licenseAcquisitionUrl;
    }
}
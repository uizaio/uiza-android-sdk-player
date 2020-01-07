package vn.uiza.models.drm;

import com.google.gson.annotations.SerializedName;

public class LicenseAcquisitionUrl {

    @SerializedName("licenseAcquisitionUrl")
    private String licenseAcquisitionUrl;

    public String getLicenseAcquisitionUrl() {
        return licenseAcquisitionUrl;
    }

    public void setLicenseAcquisitionUrl(String licenseAcquisitionUrl) {
        this.licenseAcquisitionUrl = licenseAcquisitionUrl;
    }
}
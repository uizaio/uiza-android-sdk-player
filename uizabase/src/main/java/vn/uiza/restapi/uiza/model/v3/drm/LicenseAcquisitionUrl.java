package vn.uiza.restapi.uiza.model.v3.drm;

import com.squareup.moshi.Json;

public class LicenseAcquisitionUrl {

    @Json(name = "licenseAcquisitionUrl")
    private String licenseAcquisitionUrl;

    public String getLicenseAcquisitionUrl() {
        return licenseAcquisitionUrl;
    }

    public void setLicenseAcquisitionUrl(String licenseAcquisitionUrl) {
        this.licenseAcquisitionUrl = licenseAcquisitionUrl;
    }
}

package vn.uiza.restapi.model.v2.getlinkdownload;

import com.google.gson.annotations.SerializedName;

public class Download {

    @SerializedName("linkDownload")
    private String linkDownload;
    @SerializedName("profileName")
    private String profileName;
    @SerializedName("videoDetail")
    private VideoDetail videoDetail;
    @SerializedName("resolution")
    private String resolution;
    @SerializedName("audioDetail")
    private AudioDetail audioDetail;

    public String getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public VideoDetail getVideoDetail() {
        return videoDetail;
    }

    public void setVideoDetail(VideoDetail videoDetail) {
        this.videoDetail = videoDetail;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public AudioDetail getAudioDetail() {
        return audioDetail;
    }

    public void setAudioDetail(AudioDetail audioDetail) {
        this.audioDetail = audioDetail;
    }

}

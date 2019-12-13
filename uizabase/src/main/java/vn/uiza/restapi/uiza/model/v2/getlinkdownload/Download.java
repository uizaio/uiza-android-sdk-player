
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

public class Download {

    @Json(name = "linkDownload")
    private String linkDownload;
    @Json(name = "profileName")
    private String profileName;
    @Json(name = "videoDetail")
    private VideoDetail videoDetail;
    @Json(name = "resolution")
    private String resolution;
    @Json(name = "audioDetail")
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

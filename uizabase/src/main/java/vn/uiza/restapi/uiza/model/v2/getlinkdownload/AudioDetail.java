
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

public class AudioDetail {

    @Json(name = "id")
    private String id;
    @Json(name = "audio_type")
    private String audioType;
    @Json(name = "bitrate")
    private String bitrate;
    @Json(name = "name")
    private String name;
    @Json(name = "volume")
    private String volume;
    @Json(name = "template")
    private String template;
    @Json(name = "enable_normalize_audio")
    private boolean enableNormalizeAudio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isEnableNormalizeAudio() {
        return enableNormalizeAudio;
    }

    public void setEnableNormalizeAudio(boolean enableNormalizeAudio) {
        this.enableNormalizeAudio = enableNormalizeAudio;
    }

}

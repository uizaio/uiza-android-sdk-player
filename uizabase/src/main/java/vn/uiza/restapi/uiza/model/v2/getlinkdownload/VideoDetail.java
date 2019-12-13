
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

public class VideoDetail {

    @Json(name = "id")
    private String id;
    @Json(name = "bitrate")
    private String bitrate;
    @Json(name = "bufsize")
    private int bufsize;
    @Json(name = "width")
    private String width;
    @Json(name = "height")
    private String height;
    @Json(name = "framerate")
    private String framerate;
    @Json(name = "keyint")
    private String keyint;
    @Json(name = "level")
    private String level;
    @Json(name = "maxrate")
    private int maxrate;
    @Json(name = "name")
    private String name;
    @Json(name = "preset")
    private String preset;
    @Json(name = "template")
    private String template;
    @Json(name = "vprofile")
    private String vprofile;
    @Json(name = "weight")
    private int weight;
    @Json(name = "enable_hevc")
    private boolean enableHevc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public int getBufsize() {
        return bufsize;
    }

    public void setBufsize(int bufsize) {
        this.bufsize = bufsize;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFramerate() {
        return framerate;
    }

    public void setFramerate(String framerate) {
        this.framerate = framerate;
    }

    public String getKeyint() {
        return keyint;
    }

    public void setKeyint(String keyint) {
        this.keyint = keyint;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getMaxrate() {
        return maxrate;
    }

    public void setMaxrate(int maxrate) {
        this.maxrate = maxrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getVprofile() {
        return vprofile;
    }

    public void setVprofile(String vprofile) {
        this.vprofile = vprofile;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isEnableHevc() {
        return enableHevc;
    }

    public void setEnableHevc(boolean enableHevc) {
        this.enableHevc = enableHevc;
    }

}

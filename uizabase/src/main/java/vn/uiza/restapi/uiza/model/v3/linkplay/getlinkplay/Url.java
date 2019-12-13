
package vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay;

import com.squareup.moshi.Json;

import java.util.List;

public class Url {

    @Json(name = "url")
    private String url;
    @Json(name = "support")
    private String support;
    @Json(name = "codec")
    private List<String> codec = null;
    @Json(name = "type")
    private String type;
    @Json(name = "region")
    private String region;
    @Json(name = "priority")
    private Integer priority;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public List<String> getCodec() {
        return codec;
    }

    public void setCodec(List<String> codec) {
        this.codec = codec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}

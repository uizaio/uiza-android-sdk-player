
package vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay;

import com.squareup.moshi.Json;

import java.util.List;

public class FileName {

    @Json(name = "type")
    private String type;
    @Json(name = "support")
    private String support;
    @Json(name = "codec")
    private List<String> codec = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

}

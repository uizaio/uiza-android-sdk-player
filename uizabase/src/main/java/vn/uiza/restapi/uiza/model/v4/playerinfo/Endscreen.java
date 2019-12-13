
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.squareup.moshi.Json;

public class Endscreen {

    @Json(name = "display")
    private String display;
    @Json(name = "content")
    private String content;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

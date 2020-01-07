
package vn.uiza.models.playerinfo;

import com.google.gson.annotations.SerializedName;

public class Endscreen {

    @SerializedName("display")
    private String display;
    @SerializedName("content")
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

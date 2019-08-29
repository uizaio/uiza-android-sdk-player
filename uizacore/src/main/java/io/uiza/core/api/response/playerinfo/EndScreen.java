package io.uiza.core.api.response.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndScreen {

    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("content")
    @Expose
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

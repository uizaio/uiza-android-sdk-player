package io.uiza.core.api.response.playerinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Styling {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icons")
    @Expose
    private String icons;
    @SerializedName("progress")
    @Expose
    private String progress;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("buffer")
    @Expose
    private String buffer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

}

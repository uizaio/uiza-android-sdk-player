
package vn.uiza.restapi.model.v2.getplayerinfo;

import com.google.gson.annotations.SerializedName;

public class Styling {

    @SerializedName("name")
    private String name;
    @SerializedName("title")
    private String title;
    @SerializedName("icons")
    private String icons;
    @SerializedName("progress")
    private String progress;
    @SerializedName("background")
    private String background;
    @SerializedName("buffer")
    private String buffer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

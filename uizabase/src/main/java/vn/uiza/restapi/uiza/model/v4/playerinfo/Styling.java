
package vn.uiza.restapi.uiza.model.v4.playerinfo;

import com.squareup.moshi.Json;

public class Styling {

    @Json(name = "name")
    private String name;
    @Json(name = "icons")
    private String icons;
    @Json(name = "progress")
    private String progress;
    @Json(name = "background")
    private String background;
    @Json(name = "buffer")
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

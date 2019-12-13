
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

public class Mpd {

    @Json(name = "name")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}


package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

public class Hevc {

    @Json(name = "name")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "url")
    private Object url;

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

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

}


package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.google.gson.annotations.SerializedName;

public class HlsT {

    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
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

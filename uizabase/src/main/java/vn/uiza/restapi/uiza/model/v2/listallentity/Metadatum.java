
package vn.uiza.restapi.uiza.model.v2.listallentity;

import com.squareup.moshi.Json;

public class Metadatum {

    @Json(name = "id")
    private String id;
    @Json(name = "name")
    private String name;
    @Json(name = "type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

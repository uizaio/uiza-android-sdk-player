package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

/**
 * Created by LENOVO on 2/23/2018.
 */

import com.squareup.moshi.Json;

import java.util.List;

public class JsonBodyGetLinkDownload {

    @Json(name = "listEntityIds")
    private List<String> listEntityIds = null;

    public List<String> getListEntityIds() {
        return listEntityIds;
    }

    public void setListEntityIds(List<String> listEntityIds) {
        this.listEntityIds = listEntityIds;
    }
}
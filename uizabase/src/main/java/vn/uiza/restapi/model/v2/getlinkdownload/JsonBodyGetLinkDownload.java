package vn.uiza.restapi.model.v2.getlinkdownload;

/**
 * Created by LENOVO on 2/23/2018.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonBodyGetLinkDownload {

    @SerializedName("listEntityIds")
    private List<String> listEntityIds = null;

    public List<String> getListEntityIds() {
        return listEntityIds;
    }

    public void setListEntityIds(List<String> listEntityIds) {
        this.listEntityIds = listEntityIds;
    }
}
package vn.uiza.restapi.model.v2.listallentity;

/**
 * Created by LENOVO on 2/23/2018.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonBodyListAllEntity {

    @SerializedName("limit")
    private int limit;
    @SerializedName("orderBy")
    private String orderBy;
    @SerializedName("orderType")
    private String orderType;
    @SerializedName("page")
    private int page;
    @SerializedName("metadataId")
    private List<String> metadataId = null;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(List<String> metadataId) {
        this.metadataId = metadataId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
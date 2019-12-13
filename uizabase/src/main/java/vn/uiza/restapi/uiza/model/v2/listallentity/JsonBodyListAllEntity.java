package vn.uiza.restapi.uiza.model.v2.listallentity;

/**
 * Created by LENOVO on 2/23/2018.
 */

import com.squareup.moshi.Json;

import java.util.List;

public class JsonBodyListAllEntity {

    @Json(name = "limit")
    private int limit;
    @Json(name = "orderBy")
    private String orderBy;
    @Json(name = "orderType")
    private String orderType;
    @Json(name = "page")
    private int page;
    @Json(name = "metadataId")
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
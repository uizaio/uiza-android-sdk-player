
package vn.uiza.restapi.model.v3.livestreaming.retrievealiveevent;

import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("total")
    private long total;
    @SerializedName("result")
    private long result;
    @SerializedName("page")
    private long page;
    @SerializedName("limit")
    private long limit;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

}

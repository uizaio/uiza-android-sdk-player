
package vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent;

import com.squareup.moshi.Json;

public class Metadata {

    @Json(name = "total")
    private long total;
    @Json(name = "result")
    private long result;
    @Json(name = "page")
    private long page;
    @Json(name = "limit")
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

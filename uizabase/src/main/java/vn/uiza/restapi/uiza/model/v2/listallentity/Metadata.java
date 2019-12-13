
package vn.uiza.restapi.uiza.model.v2.listallentity;

import com.squareup.moshi.Json;

public class Metadata {

    @Json(name = "total")
    private double total;
    @Json(name = "result")
    private double result;
    @Json(name = "page")
    private double page;
    @Json(name = "limit")
    private double limit;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
        this.page = page;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

}

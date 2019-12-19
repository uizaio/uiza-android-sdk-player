
package vn.uiza.restapi.model.v2.listallentity;

import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("total")
    private double total;
    @SerializedName("result")
    private double result;
    @SerializedName("page")
    private double page;
    @SerializedName("limit")
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

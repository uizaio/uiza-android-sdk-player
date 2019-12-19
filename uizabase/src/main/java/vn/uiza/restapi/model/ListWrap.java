package vn.uiza.restapi.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListWrap<T> {
    @SerializedName("data")
    List<T> data;
    @SerializedName("next_page_token")
    String nextPageToken;


    public List<T> getData() {
        return data;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}

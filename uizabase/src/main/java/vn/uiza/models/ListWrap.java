package vn.uiza.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListWrap<T> {
    @SerializedName("data")
    List<T> data;
    @SerializedName("next_page_token")
    String nextPageToken;
    @SerializedName("prev_page_token")
    String prevPageToken;


    public List<T> getData() {
        return data;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }
}

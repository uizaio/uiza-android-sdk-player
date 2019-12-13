package vn.uiza.restapi.uiza.model;


import com.squareup.moshi.Json;

import java.util.List;

public class ListWrap<T> {
    @Json(name = "data")
    List<T> data;
    @Json(name = "next_page_token")
    String nextPageToken;


    public List<T> getData() {
        return data;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}

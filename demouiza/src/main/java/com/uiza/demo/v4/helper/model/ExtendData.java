package com.uiza.demo.v4.helper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendData {

    @SerializedName("published-date")
    @Expose
    private String publishedDate;
    @SerializedName("duration-mins")
    @Expose
    private String durationMins;
    @SerializedName("views")
    @Expose
    private String views;

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(String durationMins) {
        this.durationMins = durationMins;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

}

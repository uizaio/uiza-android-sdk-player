package com.uiza.demo.v4.helper.model;

import java.util.List;

/**
 * Created by www.muathu@gmail.com on 11/15/2017.
 */

public class ChannelObject {
    private String channelName;
    private List<Item> itemList;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}

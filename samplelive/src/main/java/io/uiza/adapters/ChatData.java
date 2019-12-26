package io.uiza.adapters;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class responsible to hold the name and the message to the user
 * to send to firebase
 */
@IgnoreExtraProperties
public class ChatData implements Serializable {

    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;
    @SerializedName("message")
    public String message;

    public ChatData() {
        // empty constructor
    }

    public ChatData(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }


    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
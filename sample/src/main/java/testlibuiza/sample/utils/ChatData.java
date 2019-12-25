package testlibuiza.sample.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class responsible to hold the name and the message to the user
 * to send to firebase
 */
public class ChatData implements Serializable {

    @SerializedName("name")
    String name;
    @SerializedName("id")
    String id;
    @SerializedName("message")
    String message;

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

}
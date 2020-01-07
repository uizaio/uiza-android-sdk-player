package vn.uiza.models.event;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EventEntity {

    /**
     * Unique identifier for the object.
     * This string is in UUID form.
     */
    @SerializedName("id")
    String id;
    /**
     * String representing the object’s type.
     * Objects of the same type share the same value.
     */
    @SerializedName("object")
    String object;
    /**
     * String representing the current api version of the generated event.
     */
    @SerializedName("api_version")
    String apiVersion;
    /**
     * Timestamp in milliseconds
     */
    @SerializedName("created_at")
    Date createdAt;
    /**
     * Object representing the actual event's data.
     * The state of that resource at the time of the change is embedded in the event's data field.
     * For example, an `entity.created` event will contain an entity,
     * while `entity.publish` event will contain an entity and a session.
     */
    @SerializedName("data")
    EventData data;
    /**
     * String representing the type of the event.
     */
    @SerializedName("type")
    String type;
    /**
     * ID of the API request that caused the event.
     * If null, the event was automatic.
     */
    @SerializedName("request_id")
    String requestId;
}

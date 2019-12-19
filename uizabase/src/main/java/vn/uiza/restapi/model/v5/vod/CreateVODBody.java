package vn.uiza.restapi.model.v5.vod;

import com.google.gson.annotations.SerializedName;

public class CreateVODBody {
    /**
     * required
     * The video's name, meant to be displayable to end users.
     */
    @SerializedName("name")
    String name;
    /**
     * required
     * URL of media file (HTTP/HTTPS, FTP, S3).
     * Send empty string if you upload with SDK
     */
    @SerializedName("url")
    String url;
    /**
     * required
     * Type of URL (Acceptable values: [ http|s3|ftp|s3-uiza ] ).
     * If URL is empty, this attribute must be s3-uiza
     */
    @SerializedName("input_type")
    VODInputType inputType;
    /**
     * Optional
     * The video's description, meant to be displayable to end users.
     * Can contain up to 65,535 characters.
     */
    @SerializedName("description")
    String description;
    /**
     * Optional
     * The video's short description, meant to be displayable to end users.
     * Can contain up to 250 characters.
     */
    @SerializedName("short_description")
    String shortDescription;
    /**
     * Optional
     * The video's poster URL, meant to be displayable to end users.
     */
    @SerializedName("poster")
    String poster;
    /**
     * Optional
     * The video's thumbnail URL, meant to be displayable to end users.
     */
    @SerializedName("thumbnail")
    String thumbnail;

    /**
     * Optional
     * A set of predefined key-value pairs that you can attach to a video object,
     * meant to be displayable to end users.
     * See {@link EmbeddedMetadata}.
     */
    @SerializedName("embedded_metadata")
    EmbeddedMetadata embeddedMetadata;

    public CreateVODBody(String name, String url, VODInputType inputType) {
        this.name = name;
        this.url = url;
        this.inputType = inputType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setEmbeddedMetadata(EmbeddedMetadata embeddedMetadata) {
        this.embeddedMetadata = embeddedMetadata;
    }
}

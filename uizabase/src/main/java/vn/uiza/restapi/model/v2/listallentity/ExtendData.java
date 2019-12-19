
package vn.uiza.restapi.model.v2.listallentity;


import com.google.gson.annotations.SerializedName;

public class ExtendData {

    @SerializedName("price")
    private Object price;
    @SerializedName("genre")
    private Object genre;
    @SerializedName("country")
    private Object country;
    @SerializedName("published-date")
    private Object publishedDate;
    @SerializedName("upload")
    private Object upload;
    @SerializedName("artist")
    private String artist;
    @SerializedName("director")
    private String director;
    @SerializedName("text")
    private String text;

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getGenre() {
        return genre;
    }

    public void setGenre(Object genre) {
        this.genre = genre;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Object publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Object getUpload() {
        return upload;
    }

    public void setUpload(Object upload) {
        this.upload = upload;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

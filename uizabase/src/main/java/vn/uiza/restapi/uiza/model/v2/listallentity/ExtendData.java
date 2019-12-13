
package vn.uiza.restapi.uiza.model.v2.listallentity;

import com.squareup.moshi.Json;

public class ExtendData {

    @Json(name = "price")
    private Object price;
    @Json(name = "genre")
    private Object genre;
    @Json(name = "country")
    private Object country;
    @Json(name = "published-date")
    private Object publishedDate;
    @Json(name = "upload")
    private Object upload;
    @Json(name = "artist")
    private String artist;
    @Json(name = "director")
    private String director;
    @Json(name = "text")
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

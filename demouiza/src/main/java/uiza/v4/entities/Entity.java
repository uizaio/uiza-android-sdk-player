package uiza.v4.entities;

/**
 * Created by www.muathu@gmail.com on 12/8/2017.
 */

public class Entity {
    private String title, genre, year, cover;

    public Entity() {
    }

    public Entity(String title, String genre, String year, String cover) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
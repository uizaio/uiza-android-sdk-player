package vn.uiza.models.vod;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EmbeddedMetadata implements Parcelable {

    @SerializedName("title")
    String title;
    @SerializedName("artist")
    String artist;
    @SerializedName("album_artist")
    String albumArtist;
    @SerializedName("album")
    String album;
    @SerializedName("composer")
    String composer;
    @SerializedName("year")
    String year;
    @SerializedName("track")
    String track;
    @SerializedName("genre")
    String genre;
    @SerializedName("description")
    String description;
    @SerializedName("copyright")
    String copyright;
    @SerializedName("network")
    String network;
    @SerializedName("show")
    String show;
    @SerializedName("episode_id")
    String episodeId;

    // default constructor
    public EmbeddedMetadata() {
    }

    protected EmbeddedMetadata(Parcel in) {
        title = in.readString();
        artist = in.readString();
        albumArtist = in.readString();
        album = in.readString();
        composer = in.readString();
        year = in.readString();
        track = in.readString();
        genre = in.readString();
        description = in.readString();
        copyright = in.readString();
        network = in.readString();
        show = in.readString();
        episodeId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(albumArtist);
        dest.writeString(album);
        dest.writeString(composer);
        dest.writeString(year);
        dest.writeString(track);
        dest.writeString(genre);
        dest.writeString(description);
        dest.writeString(copyright);
        dest.writeString(network);
        dest.writeString(show);
        dest.writeString(episodeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmbeddedMetadata> CREATOR = new Creator<EmbeddedMetadata>() {
        @Override
        public EmbeddedMetadata createFromParcel(Parcel in) {
            return new EmbeddedMetadata(in);
        }

        @Override
        public EmbeddedMetadata[] newArray(int size) {
            return new EmbeddedMetadata[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public String getAlbum() {
        return album;
    }

    public String getComposer() {
        return composer;
    }

    public String getYear() {
        return year;
    }

    public String getTrack() {
        return track;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getNetwork() {
        return network;
    }

    public String getShow() {
        return show;
    }

    public String getEpisodeId() {
        return episodeId;
    }
}


package vn.uiza.restapi.uiza.model.v2.getplayerinfo;

import com.squareup.moshi.Json;

public class Controller {

    @Json(name = "facebook")
    private String facebook;
    @Json(name = "twitter")
    private String twitter;
    @Json(name = "pinterest")
    private String pinterest;
    @Json(name = "linkedin")
    private String linkedin;
    @Json(name = "tumblr")
    private String tumblr;

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTumblr() {
        return tumblr;
    }

    public void setTumblr(String tumblr) {
        this.tumblr = tumblr;
    }

}

package vn.loitp.uizavideov3.view.rl.livestream.uiza;

public class PresetLiveStreamingFeed {
    private boolean isTranscode;
    private int s1080p;
    private int s720p;
    private int s480p;

    public boolean isTranscode() {
        return isTranscode;
    }

    public void setTranscode(boolean transcode) {
        isTranscode = transcode;
    }

    public int getS1080p() {
        return s1080p;
    }

    public void setS1080p(int s1080p) {
        this.s1080p = s1080p;
    }

    public int getS720p() {
        return s720p;
    }

    public void setS720p(int s720p) {
        this.s720p = s720p;
    }

    public int getS480p() {
        return s480p;
    }

    public void setS480p(int s480p) {
        this.s480p = s480p;
    }
}

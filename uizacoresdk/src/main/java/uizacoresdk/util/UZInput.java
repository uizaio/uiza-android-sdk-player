package uizacoresdk.util;

import vn.uiza.models.PlaybackInfo;

/**
 * Created by loitp on 18/1/2019.
 */

public class UZInput {
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";
    private PlaybackInfo playbackInfo;

    public String getUrlIMAAd() {
        return urlIMAAd;
    }

    public void setUrlIMAAd(String urlIMAAd) {
        this.urlIMAAd = urlIMAAd;
    }

    public String getUrlThumnailsPreviewSeekbar() {
        return urlThumnailsPreviewSeekbar;
    }

    public void setUrlThumnailsPreviewSeekbar(String urlThumnailsPreviewSeekbar) {
        this.urlThumnailsPreviewSeekbar = urlThumnailsPreviewSeekbar;
    }

    public PlaybackInfo getPlaybackInfo() {
        return playbackInfo;
    }

    public void setPlaybackInfo(PlaybackInfo playback) {
        this.playbackInfo = playback;
    }

    public boolean isLivestream() {
        return playbackInfo != null && playbackInfo.isLive();
    }
}

package uizacoresdk.util;

import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;

/**
 * Created by loitp on 18/1/2019.
 */

public class UZInput {
    private String urlIMAAd = "";
    private String urlThumnailsPreviewSeekbar = "";
    private VideoData data;
    private StreamingToken streamingToken;
    private LinkPlay linkPlay;

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

    public VideoData getData() {
        return data;
    }

    public void setData(VideoData data) {
        this.data = data;
    }

    public boolean isLivestream() {
        return data != null && data.getLastFeedId() != null && !data.getLastFeedId().isEmpty();
    }

    public StreamingToken getStreamingToken() {
        return streamingToken;
    }

    public void setStreamingToken(StreamingToken streamingToken) {
        this.streamingToken = streamingToken;
    }

    public LinkPlay getLinkPlay() {
        return linkPlay;
    }

    public void setLinkPlay(LinkPlay linkPlay) {
        this.linkPlay = linkPlay;
    }
}

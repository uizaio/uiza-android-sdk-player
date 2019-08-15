package io.uiza.core.model;

import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;

public final class UzLinkPlayData {

    private String imaAdUrl;
    private String seekbarPreviewThumbUrl;
    private VideoData data;
    private StreamingToken streamingToken;
    private LinkPlay linkPlay;

    private UzLinkPlayData(Builder builder) {
        imaAdUrl = builder.imaAdUrl;
        seekbarPreviewThumbUrl = builder.seekbarPreviewThumbUrl;
        data = builder.data;
        streamingToken = builder.streamingToken;
        linkPlay = builder.linkPlay;
    }

    public String getImaAdUrl() {
        return imaAdUrl;
    }

    public String getSeekbarPreviewThumbUrl() {
        return seekbarPreviewThumbUrl;
    }

    public VideoData getVideoData() {
        return data;
    }

    public boolean isLivestream() {
        return data != null && data.getLastFeedId() != null && !data.getLastFeedId().isEmpty();
    }

    public StreamingToken getStreamingToken() {
        return streamingToken;
    }

    public LinkPlay getLinkPlay() {
        return linkPlay;
    }

    public void setLinkPlay(LinkPlay linkPlay) {
        this.linkPlay = linkPlay;
    }

    public static final class Builder {

        private String imaAdUrl;
        private String seekbarPreviewThumbUrl;
        private VideoData data;
        private StreamingToken streamingToken;
        private LinkPlay linkPlay;

        public Builder() {
        }

        public Builder imaAdUrl(String val) {
            imaAdUrl = val;
            return this;
        }

        public Builder seekbarPreviewThumbUrl(String val) {
            seekbarPreviewThumbUrl = val;
            return this;
        }

        public Builder data(VideoData val) {
            data = val;
            return this;
        }

        public Builder streamingToken(StreamingToken val) {
            streamingToken = val;
            return this;
        }

        public Builder linkPlay(LinkPlay val) {
            linkPlay = val;
            return this;
        }

        public UzLinkPlayData build() {
            return new UzLinkPlayData(this);
        }
    }
}

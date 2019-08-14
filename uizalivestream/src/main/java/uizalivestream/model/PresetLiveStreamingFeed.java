package uizalivestream.model;

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

    /**
     * initial values
     *
     * @param connectedFast: network quality
     */
    public void setVideoBitRates(boolean connectedFast) {
        if (isTranscode) {
            //Push with Transcode
            setS1080p(connectedFast ? 5500000 : 3500000); // 2.5M - 5.5M
            setS720p(connectedFast ? 3000000 : 1500000); // 1.5M - 3M
            setS480p(connectedFast ? 1500000 : 800000); // 0.8M - 1.5M
        } else {
            //Push-only, no transcode
            setS1080p(connectedFast ? 3500000 : 1500000); // 1.5M - 2.5M
            setS720p(connectedFast ? 1500000 : 800000); // 0.8M - 1.5M
            setS480p(connectedFast ? 800000 : 400000); // 0.4M - 0.8M
        }
    }

    /**
     * get Audio bitrate from video sampleRate
     * @param videoBitrate: video Bitrate see setEntity
     * @return audio Bitrate
     */
    public int getAudioBitRate(int videoBitrate) {
        if (isTranscode) {
            if (videoBitrate >= 2500000) return 192 * 1024; // 1080p => 192kps
            if (videoBitrate >= 1500000) return 128 * 1024; // 720p => 128kps
            else return 96 * 1024; // 480p => 96kps
        } else {
            if (videoBitrate >= 1500000) return 192 * 1024; // 1080p => 192kps
            if (videoBitrate >= 800000) return 128 * 1024; // 720p => 128kps
            else return 96 * 1024; // 480p => 96kps
        }

    }
}

package io.uiza.live;

import io.uiza.live.enums.FrameRate;
import io.uiza.live.enums.OrientationMode;


public class VideoEncoderConfig {
    private VideoDimension dimension;
    private FrameRate frameRate;
    private int bitrate;
    private OrientationMode orientationMode;

    public VideoEncoderConfig(VideoDimension dimension, FrameRate frameRate, int bitrate, OrientationMode orientationMode){
        this.dimension = dimension;
        this.frameRate = frameRate;
        this.bitrate = bitrate;
        this.orientationMode = orientationMode;
    }

    public VideoDimension getDimension() {
        return dimension;
    }

    public FrameRate getFrameRate() {
        return frameRate;
    }

    public int getBitrate() {
        return bitrate;
    }

    public OrientationMode getOrientationMode() {
        return orientationMode;
    }
}
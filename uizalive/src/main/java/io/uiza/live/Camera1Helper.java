package io.uiza.live;

import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.util.RecordController;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.uiza.live.enums.ProfileVideoEncoder;
import io.uiza.live.enums.RecordStatus;
import io.uiza.live.interfaces.CameraChangeListener;
import io.uiza.live.interfaces.ICameraHelper;
import io.uiza.live.interfaces.RecordListener;
import io.uiza.live.interfaces.UizaCameraOpenException;

public class Camera1Helper implements ICameraHelper {

    private RtmpCamera1 rtmpCamera1;

    private CameraChangeListener cameraChangeListener;

    private RecordListener recordListener;

    Camera1Helper(@NonNull RtmpCamera1 camera) {
        this.rtmpCamera1 = camera;
    }

    @Override
    public void setConnectReTries(int reTries) {
        rtmpCamera1.setReTries(reTries);
    }

    @Override
    public boolean reTry(long delay, @NonNull String reason) {
        return rtmpCamera1.reTry(delay, reason);
    }

//    @Override
//    public boolean shouldRetry(@NotNull String reason) {
//        return rtmpCamera1.shouldRetry(reason);
//    }

    @Override
    public void setCameraChangeListener(@NonNull CameraChangeListener cameraChangeListener) {
        this.cameraChangeListener = cameraChangeListener;
    }

    @Override
    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }


    @Override
    public void setFilter(@NotNull BaseFilterRender filterReader) {
        rtmpCamera1.getGlInterface().setFilter(filterReader);
    }

    @Override
    public void setFilter(int filterPosition, @NotNull BaseFilterRender filterReader) {
        rtmpCamera1.getGlInterface().setFilter(filterPosition, filterReader);
    }

    @Override
    public void enableAA(boolean aAEnabled) {
        rtmpCamera1.getGlInterface().enableAA(aAEnabled);
    }

    @Override
    public boolean isAAEnabled() {
        return rtmpCamera1.getGlInterface().isAAEnabled();
    }

    @Override
    public int getStreamWidth() {
        return rtmpCamera1.getStreamHeight();
    }

    @Override
    public int getStreamHeight() {
        return rtmpCamera1.getStreamWidth();
    }

    @Override
    public void enableAudio() {
        rtmpCamera1.enableAudio();
    }

    @Override
    public void disableAudio() {
        rtmpCamera1.disableAudio();
    }

    @Override
    public boolean isAudioMuted() {
        return rtmpCamera1.isAudioMuted();
    }

    @Override
    public boolean prepareAudio() {
        return rtmpCamera1.prepareAudio();
    }

    @Override
    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo) {
        return rtmpCamera1.prepareAudio(bitrate, sampleRate, isStereo, AcousticEchoCanceler.isAvailable(), NoiseSuppressor.isAvailable());
    }

    @Override
    public boolean isVideoEnabled() {
        return rtmpCamera1.isVideoEnabled();
    }

    @Override
    public boolean prepareVideo(@NotNull ProfileVideoEncoder profile) {
        return rtmpCamera1.prepareVideo(profile.getWidth(), profile.getHeight(), 24, profile.getBitrate(), false, 90);
    }

    @Override
    public boolean prepareVideo(@NotNull ProfileVideoEncoder profile, int fps, int iFrameInterval, int rotation) {
        return rtmpCamera1.prepareVideo(profile.getWidth(), profile.getHeight(), fps, profile.getBitrate(), false, iFrameInterval, rotation);
    }

    @Override
    public void startStream(@NotNull String liveEndpoint) {
        rtmpCamera1.startStream(liveEndpoint);
    }

    @Override
    public void stopStream() {
        rtmpCamera1.stopStream();
    }

    @Override
    public boolean isStreaming() {
        return rtmpCamera1.isStreaming();
    }

    @Override
    public void setVideoBitrateOnFly(int bitrate) {
        rtmpCamera1.setVideoBitrateOnFly(bitrate);
    }

    @Override
    public int getBitrate() {
        return rtmpCamera1.getBitrate();
    }

    @Override
    public boolean isFrontCamera() {
        return rtmpCamera1.isFrontCamera();
    }

    @Override
    public void switchCamera() throws UizaCameraOpenException {
        try {
            rtmpCamera1.switchCamera();
        } catch (CameraOpenException e) {
            throw new UizaCameraOpenException(e.getMessage());
        }
        if (cameraChangeListener != null)
            cameraChangeListener.onCameraChange(rtmpCamera1.isFrontCamera());
    }

    @Override
    public void startPreview(@NotNull CameraHelper.Facing cameraFacing) {
        rtmpCamera1.startPreview(cameraFacing, 480, 640);
    }

    @Override
    public void startPreview(@NotNull CameraHelper.Facing cameraFacing, int w, int h) {
        rtmpCamera1.startPreview(cameraFacing, h, w);
    }

    @Override
    public void stopPreview() {
        rtmpCamera1.stopPreview();
    }

    @Override
    public boolean isRecording() {
        return rtmpCamera1.isRecording();
    }

    @Override
    public void startRecord(@NotNull String savePath) throws IOException {
        if (recordListener != null) {
            rtmpCamera1.startRecord(savePath, new RecordController.Listener() {
                @Override
                public void onStatusChange(RecordController.Status status) {
                    recordListener.onStatusChange(RecordStatus.lookup(status));
                }
            });
        } else {
            rtmpCamera1.startRecord(savePath);
        }
    }

    @Override
    public void stopRecord() {
        rtmpCamera1.stopRecord();
        rtmpCamera1.startPreview();
    }

    @Override
    public boolean isLanternSupported() {
        return false;
    }

    @Override
    public void enableLantern() throws Exception {
        rtmpCamera1.enableLantern();
    }

    @Override
    public void disableLantern() {
        rtmpCamera1.disableLantern();
    }

    @Override
    public boolean isLanternEnabled() {
        return rtmpCamera1.isLanternEnabled();
    }

    @Override
    public float getMaxZoom() {
        return 1.0f;
    }

    @Override
    public float getZoom() {
        return 1.0f;
    }

    @Override
    public void setZoom(float level) {

    }

    @Override
    public void setZoom(@NotNull MotionEvent event) {
        rtmpCamera1.setZoom(event);
    }
}

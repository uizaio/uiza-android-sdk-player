package io.uiza.live;

import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.util.RecordController;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.uiza.live.enums.ProfileVideoEncoder;
import io.uiza.live.enums.RecordStatus;
import io.uiza.live.interfaces.CameraChangeListener;
import io.uiza.live.interfaces.ICameraHelper;
import io.uiza.live.interfaces.RecordListener;
import io.uiza.live.interfaces.UizaCameraOpenException;
import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Helper implements ICameraHelper {

    private RtmpCamera2 rtmpCamera2;

    private CameraChangeListener cameraChangeListener;

    private RecordListener recordListener;


    Camera2Helper(@NonNull RtmpCamera2 camera) {
        this.rtmpCamera2 = camera;
    }

    @Override
    public void setConnectReTries(int reTries) {
        rtmpCamera2.setReTries(reTries);
    }

    @Override
    public boolean reTry(long delay, @NonNull String reason) {
        return rtmpCamera2.reTry(delay, reason);
    }

    @Override
    public void setCameraChangeListener(@NonNull CameraChangeListener cameraChangeListener) {
        this.cameraChangeListener = cameraChangeListener;
    }

    @Override
    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    @Override
    public boolean supportGlInterface() {
        try {
            return rtmpCamera2.getGlInterface() != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public void setFilter(@NotNull BaseFilterRender filterReader) {
        if (supportGlInterface())
            rtmpCamera2.getGlInterface().setFilter(filterReader);
        else
            Timber.e("Filter is not support in this view");
    }

    @Override
    public void setFilter(int filterPosition, @NotNull BaseFilterRender filterReader) {
        if (supportGlInterface())
            rtmpCamera2.getGlInterface().setFilter(filterPosition, filterReader);
        else
            Timber.e("Filter is not support in this view");
    }

    @Override
    public void enableAA(boolean aAEnabled) {
        if (supportGlInterface()) {
            rtmpCamera2.getGlInterface().enableAA(aAEnabled);
        } else {
            Timber.e("AA is not support in this view");
        }
    }

    @Override
    public boolean isAAEnabled() {
        if (supportGlInterface()) {
            return rtmpCamera2.getGlInterface().isAAEnabled();
        }
        return false;
    }

    @Override
    public void setVideoBitrateOnFly(int bitrate) {
        rtmpCamera2.setVideoBitrateOnFly(bitrate);
    }

    @Override
    public int getBitrate() {
        return rtmpCamera2.getBitrate();
    }

    @Override
    public int getStreamWidth() {
        return rtmpCamera2.getStreamWidth();
    }

    @Override
    public int getStreamHeight() {
        return rtmpCamera2.getStreamHeight();
    }

    public boolean prepareAudio() {
        return rtmpCamera2.prepareAudio();
    }

    @Override
    public void enableAudio() {
        rtmpCamera2.enableAudio();
    }

    @Override
    public void disableAudio() {
        rtmpCamera2.disableAudio();
    }

    @Override
    public boolean isAudioMuted() {
        return rtmpCamera2.isAudioMuted();
    }

    @Override
    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo) {
        return rtmpCamera2.prepareAudio(bitrate, sampleRate, isStereo, AcousticEchoCanceler.isAvailable(), NoiseSuppressor.isAvailable());
    }

    @Override
    public boolean isVideoEnabled() {
        return rtmpCamera2.isVideoEnabled();
    }

    @Override
    public boolean prepareVideo(@NotNull ProfileVideoEncoder profile) {
        return rtmpCamera2.prepareVideo(profile.getWidth(), profile.getHeight(), 24, profile.getBitrate(), false, 90);
    }

    @Override
    public boolean prepareVideo(@NotNull ProfileVideoEncoder profile, int fps, int iFrameInterval, int rotation) {
        Timber.d("rotation = %d", rotation);
        if (supportGlInterface()) {
            return rtmpCamera2.prepareVideo(profile.getWidth(), profile.getHeight(), fps, profile.getBitrate(), false, iFrameInterval, rotation);
        } else {
            return rtmpCamera2.prepareVideo(profile.getHeight(), profile.getWidth(), fps, profile.getBitrate(), false, iFrameInterval, rotation);
        }

    }

    @Override
    public void startStream(@NotNull String liveEndpoint) {
        rtmpCamera2.startStream(liveEndpoint);
    }

    @Override
    public void stopStream() {
        rtmpCamera2.stopStream();
    }

    @Override
    public boolean isStreaming() {
        return rtmpCamera2.isStreaming();
    }

    @Override
    public boolean isFrontCamera() {
        return rtmpCamera2.isFrontCamera();
    }

    @Override
    public void switchCamera() throws UizaCameraOpenException {
        try {
            rtmpCamera2.switchCamera();
            if (cameraChangeListener != null)
                cameraChangeListener.onCameraChange(rtmpCamera2.isFrontCamera());
        } catch (CameraOpenException e) {
            throw new UizaCameraOpenException(e.getMessage());
        }
    }

    @Override
    public void startPreview(@NotNull CameraHelper.Facing cameraFacing) {
        rtmpCamera2.startPreview(cameraFacing);
    }

    @Override
    public void startPreview(@NotNull CameraHelper.Facing cameraFacing, int width, int height) {
        rtmpCamera2.startPreview(cameraFacing, height, width);
    }

    @Override
    public void stopPreview() {
        rtmpCamera2.stopPreview();
    }

    @Override
    public boolean isRecording() {
        return rtmpCamera2.isRecording();
    }


    @Override
    public void startRecord(@NotNull String savePath) throws IOException {
        if (recordListener != null) {
            rtmpCamera2.startRecord(savePath, new RecordController.Listener() {
                @Override
                public void onStatusChange(RecordController.Status status) {
                    recordListener.onStatusChange(RecordStatus.lookup(status));
                }
            });
        } else {
            rtmpCamera2.startRecord(savePath);
        }

    }

    @Override
    public void stopRecord() {
        rtmpCamera2.stopRecord();
    }


    @Override
    public boolean isLanternSupported() {
        return rtmpCamera2.isLanternSupported();
    }

    @Override
    public void enableLantern() throws Exception {
        rtmpCamera2.enableLantern();
    }

    @Override
    public void disableLantern() {
        rtmpCamera2.disableLantern();
    }

    @Override
    public boolean isLanternEnabled() {
        return rtmpCamera2.isLanternEnabled();
    }

    @Override
    public float getMaxZoom() {
        return rtmpCamera2.getMaxZoom();
    }

    @Override
    public float getZoom() {
        return rtmpCamera2.getZoom();
    }

    @Override
    public void setZoom(float level) {
        rtmpCamera2.setZoom(level);
    }

    @Override
    public void setZoom(@NotNull MotionEvent event) {
        rtmpCamera2.setZoom(event);
    }
}

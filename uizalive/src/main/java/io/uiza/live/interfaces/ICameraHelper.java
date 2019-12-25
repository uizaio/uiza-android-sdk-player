package io.uiza.live.interfaces;

import android.view.MotionEvent;

import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.base.Camera2Base;

import java.io.IOException;

import io.uiza.live.enums.ProfileVideoEncoder;

public interface ICameraHelper {
    /**
     * @param reTries retry connect reTries times
     */
    void setConnectReTries(int reTries);

    void setCameraChangeListener(CameraChangeListener cameraChangeListener);

    void setRecordListener(RecordListener recordListener);

    /**
     * Set filter in position 0.
     *
     * @param filterReader filter to set. You can modify parameters to filter after set it to stream.
     */
    void setFilter(BaseFilterRender filterReader);

    /**
     * Set filter in position 0.
     *
     * @param filterReader filter to set. You can modify parameters to filter after set it to stream.
     */
    void setFilter(int filterPosition, BaseFilterRender filterReader);

    /**
     * Get Anti alias is enabled.
     *
     * @return true is enabled, false is disabled.
     */
    boolean isAAEnabled();

    /**
     * Enable or disable Anti aliasing (This method use FXAA).
     *
     * @param aAEnabled true is AA enabled, false is AA disabled. False by default.
     */
    void enableAA(boolean aAEnabled);

    /**
     * get Stream Width
     */
    int getStreamWidth();

    /**
     * get Stream Height
     */
    int getStreamHeight();

    /**
     * Enable a muted microphone, can be called before, while and after stream.
     */
    void enableAudio();


    /**
     * Mute microphone, can be called before, while and after stream.
     */

    void disableAudio();

    /**
     * Get mute state of microphone.
     *
     * @return true if muted, false if enabled
     */
    boolean isAudioMuted();

    /**
     * Same to call: prepareAudio(64 * 1024, 32000, true, false, false);
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    boolean prepareAudio();

    /**
     * Same to call: prepareAudio(64 * 1024, 32000, true, false, false);
     *
     * @param bitrate    AAC in kb.
     * @param sampleRate of audio in hz. Can be 8000, 16000, 22500, 32000, 44100.
     * @param isStereo   true if you want Stereo audio (2 audio channels), false if you want Mono audio
     *                   (1 audio channel).
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo);

    /**
     * Get video camera state
     *
     * @return true if disabled, false if enabled
     */
    boolean isVideoEnabled();

    /**
     * Use profle
     * prepareVideo(profile, 24, 2, 90);
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a H264 encoder).
     */

    boolean prepareVideo(ProfileVideoEncoder profile);

    /**
     * @param fps      frames per second of the stream.
     * @param rotation could be 90, 180, 270 or 0 (Normally 0 if you are streaming in landscape or 90
     *                 if you are streaming in Portrait). This only affect to stream result. NOTE: Rotation with
     *                 encoder is silence ignored in some devices.
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a H264 encoder).
     */
    boolean prepareVideo(ProfileVideoEncoder profile,
                         int fps,
                         int iFrameInterval,
                         int rotation
    );

    /**
     * Need be called after @prepareVideo or/and @prepareAudio. This method override resolution of
     *
     * @param liveEndpoint of the stream like: rtmp://ip:port/application/stream_name
     *                     <p>
     *                     RTMP: rtmp://192.168.1.1:1935/fmp4/live_stream_name
     * @startPreview to resolution seated in @prepareVideo. If you never startPreview this method
     * startPreview for you to resolution seated in @prepareVideo.
     */
    void startStream(String liveEndpoint);

    /**
     * Stop stream started with @startStream.
     */
    void stopStream();

    /**
     * Get stream state.
     *
     * @return true if streaming, false if not streaming.
     */
    boolean isStreaming();

    /**
     * Switch camera used. Can be called on preview or while stream, ignored with preview off.
     *
     * @throws UizaCameraOpenException If the other camera doesn't support same resolution.
     */
    void switchCamera() throws UizaCameraOpenException;

    void startPreview(CameraHelper.Facing cameraFacing);

    /**
     * Start preview
     */
    void startPreview(CameraHelper.Facing cameraFacing, int width, int height);

    /**
     * is Front Camera
     */
    boolean isFrontCamera();

    /**
     * Stop camera preview. Ignored if streaming or already stopped. You need call it after
     *
     * @stopStream to release camera properly if you will close activity.
     */
    void stopPreview();

    /**
     * Get record state.
     *
     * @return true if recording, false if not recoding.
     */
    boolean isRecording();

    /**
     * Start record a MP4 video. Need be called while stream.
     *
     * @param savePath where file will be saved.
     * @throws IOException If you init it before start stream.
     */
    void startRecord(String savePath) throws IOException;

    /**
     * Stop record MP4 video started with @startRecord. If you don't call it file will be unreadable.
     */
    void stopRecord();

    /**
     * Set video bitrate of H264 in kb while stream.
     *
     * @param bitrate H264 in kb.
     */
    void setVideoBitrateOnFly(int bitrate);

    int getBitrate();


    boolean reTry(long delay, String reason);

    /**
     * Check support Flashlight
     * if use Camera1 always return false
     *
     * @return true if support, false if not support.
     */
    boolean isLanternSupported();

    /**
     * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
     */
    void enableLantern() throws Exception;

    /**
     * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
     */
    void disableLantern();

    boolean isLanternEnabled();

    /**
     * Return max zoom level
     *
     * @return max zoom level
     */
    float getMaxZoom();

    /**
     * Return current zoom level
     *
     * @return current zoom level
     */
    float getZoom();

    /**
     * Set zoomIn or zoomOut to camera.
     * Use this method if you use a zoom slider.
     *
     * @param level Expected to be >= 1 and <= max zoom level
     * @see Camera2Base#getMaxZoom()
     */
    void setZoom(float level);

    /**
     * Set zoomIn or zoomOut to camera.
     *
     * @param event motion event. Expected to get event.getPointerCount() > 1
     */
    void setZoom(MotionEvent event);
}

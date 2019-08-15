package io.uiza.broadcast;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import io.uiza.broadcast.config.UzPresetLiveFeed;
import io.uiza.broadcast.util.UzLiveVideoMode;
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.connection.UzConnectivityUtil;
import io.uiza.core.util.UzCoreUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class UzLiveCameraHelper {

    private static final String TAG = UzLiveCameraHelper.class.getSimpleName();
    private RtmpCamera1 rtmpCamera1;
    private UzLiveCameraCallback cameraCallback;
    private int audioBitRate = 128 * 1024; //kps

    UzLiveCameraHelper(RtmpCamera1 camera) {
        this.rtmpCamera1 = camera;
    }

    RtmpCamera1 getRtmpCamera() {
        return rtmpCamera1;
    }

    void setCameraCallback(UzLiveCameraCallback cameraCallback) {
        this.cameraCallback = cameraCallback;
    }

    boolean isAntiAliasingEnabled() {
        return isCameraValid() && rtmpCamera1.getGlInterface().isAAEnabled();
    }

    void enableAntiAliasing(boolean enable) {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.getGlInterface().enableAA(enable);
    }

    void setFilter(BaseFilterRender baseFilterRender) {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.getGlInterface().setFilter(baseFilterRender);
    }

    int getStreamWidth() {
        if (!isCameraValid()) {
            return 0;
        }
        return rtmpCamera1.getStreamWidth();
    }

    int getStreamHeight() {
        if (!isCameraValid()) {
            return 0;
        }
        return rtmpCamera1.getStreamHeight();
    }

    void enableLantern() {
        if (!isCameraValid()) {
            return;
        }
        try {
            rtmpCamera1.enableLantern();
        } catch (Exception e) {
            SentryUtil.captureException(e);
        }
    }

    void disableLantern() {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.disableLantern();
    }

    void toggleLantern() {
        if (!isCameraValid()) {
            return;
        }
        Boolean isLanternEnabled = isLanternEnabled();
        if (isLanternEnabled == null) {
            return;
        }
        if (isLanternEnabled) {
            disableLantern();
        } else {
            enableLantern();
        }
    }

    Boolean isLanternEnabled() {
        if (!isCameraValid()) {
            return null;
        }
        return rtmpCamera1.isLanternEnabled();
    }

    void stopPreview() {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.stopPreview();
    }

    int[] getBestSizePreview() {
        List<Camera.Size> sizeList = getBestResolutionList();
        int[] result = new int[2];
        if (sizeList == null || sizeList.isEmpty()) {
            result[0] = UzDisplayUtil.getScreenWidth();
            result[1] = UzDisplayUtil.getScreenHeight();
        } else {
            result[0] = sizeList.get(0).width;
            result[1] = sizeList.get(0).height;
        }
        return result;
    }

    boolean isStreaming() {
        return isCameraValid() && rtmpCamera1.isStreaming();
    }

    boolean isRecording() {
        return isCameraValid() && rtmpCamera1.isRecording();
    }

    boolean prepareAudio(int sampleRate, boolean isStereo, boolean echoCanceler,
            boolean noiseSuppressor) {
        return prepareAudio(audioBitRate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler,
            boolean noiseSuppressor) {
        if (!isCameraValid()) {
            return false;
        }
        LLog.d(TAG, "prepareAudio ===> bitrate " + bitrate + ", sampleRate: " + sampleRate
                + ", isStereo: " + isStereo + ", echoCanceler: " + echoCanceler
                + ", noiseSuppressor: " + noiseSuppressor);
        return rtmpCamera1
                .prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    boolean prepareVideo(Context context, UzPresetLiveFeed presetLiveFeed, UzLiveVideoMode mode,
            boolean isLandscape) {
        if (presetLiveFeed == null) {
            Log.e(TAG, "prepareVideo false with presetLiveFeed null");
            return false;
        }
        if (!isCameraValid()) {
            Log.e(TAG, "prepareVideo false -> rtmpCamera1 == null");
            return false;
        }
        Camera.Size bestSize = getBestResolution(mode);
        if (bestSize == null) {
            Log.e(TAG, "prepareVideo false -> bestSize == null");
            return false;
        }
        int bestBitrate = getBestBitrate(context, presetLiveFeed);
        audioBitRate = presetLiveFeed.getAudioBitRate(bestBitrate);
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1,
                isLandscape ? 0 : 90);
    }

    boolean prepareVideo(Context context, UzPresetLiveFeed presetLiveFeed, boolean isLandscape) {
        if (presetLiveFeed == null) {
            Log.e(TAG, "prepareVideo false with presetLiveFeed null");
            return false;
        }
        if (!isCameraValid()) {
            Log.e(TAG, "prepareVideo false -> rtmpCamera1 == null");
            return false;
        }
        Camera.Size bestSize = getBestResolutionByNetwork(context);
        if (bestSize == null) {
            Log.e(TAG, "prepareVideo false -> bestSize == null");
            return false;
        }
        int bestBitrate = getBestBitrate(context, presetLiveFeed);
        audioBitRate = presetLiveFeed.getAudioBitRate(bestBitrate);
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1,
                isLandscape ? 0 : 90);
    }

    boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation,
            int frameInterval, int rotation) {
        if (!isCameraValid()) {
            return false;
        }

        LLog.d(TAG, "prepareVideo ===> " + width + "x" + height + ", bitrate " + bitrate
                + ", fps: " + fps + ", frameInterval: " + frameInterval + ", rotation: " + rotation
                + ", hardwareRotation: " + hardwareRotation);
        rtmpCamera1.startPreview(width, height);
        return rtmpCamera1
                .prepareVideo(width, height, fps, bitrate, hardwareRotation, frameInterval,
                        rotation);
    }

    void switchCamera() {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.switchCamera();
        if (cameraCallback != null) {
            cameraCallback.onCameraChanged(rtmpCamera1.isFrontCamera());
        }
    }

    boolean startPreview(CameraHelper.Facing facing, int width, int height) {
        if (!isCameraValid()) {
            return false;
        }
        if (checkCanOpen(facing, width, height)) {
            rtmpCamera1.startPreview(facing, width, height);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void startRecord(String videoPath) throws IOException {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.startRecord(videoPath);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void stopRecord() {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.stopRecord();
    }

    void startStream(String streamUrl) {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.startStream(streamUrl);
    }

    void stopStream() {
        if (!isCameraValid()) {
            return;
        }
        rtmpCamera1.stopStream();
    }

    boolean isCameraValid() {
        return getRtmpCamera() != null;
    }

    /**
     * Gets the best bitrate based on preset live feed & current network quality.
     *
     * @param context the context
     * @param presetLiveFeed the preset live feed
     * @return the best bitrate
     */
    private int getBestBitrate(Context context, UzPresetLiveFeed presetLiveFeed) {
        int bestBitrate;
        if (UzConnectivityUtil.isConnectedFast(context)
                && UzConnectivityUtil.isConnectedWifi(context)) {

            bestBitrate = presetLiveFeed.getS1080p();
        } else if (UzConnectivityUtil.isConnectedFast(context)
                && UzConnectivityUtil.isConnectedMobile(context)) {

            bestBitrate = presetLiveFeed.getS720p();
        } else {

            bestBitrate = presetLiveFeed.getS480p();
        }
        return bestBitrate;
    }

    /**
     * Gets the best resolution of current camera based on network quality.
     *
     * @param context the context
     * @return the best resolution of current camera
     */
    private Camera.Size getBestResolutionByNetwork(@NonNull Context context) {
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            return null;
        }
        int sizeList = bestResolutionList.size();
        int index;
        if (UzConnectivityUtil.isConnectedFast(context)
                && UzConnectivityUtil.isConnectedWifi(context)) {
            index = 0;
        } else if (UzConnectivityUtil.isConnectedFast(context)
                && UzConnectivityUtil.isConnectedMobile(context)) {
            if (sizeList > 2) {
                index = sizeList / 2;
            } else if (sizeList == 2) {
                index = 1;
            } else {
                index = 0;
            }
        } else {
            index = sizeList - 1;
        }
        return bestResolutionList.get(index);
    }

    private Camera.Size getBestResolution(UzLiveVideoMode mode) {
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            return null;
        }
        int sizeList = bestResolutionList.size();
        int index;
        switch (mode) {
            case MODE_FULL_HD:
                index = 0;
                break;
            case MODE_HD:
                if (sizeList > 2) {
                    index = sizeList / 2;
                } else if (sizeList == 2) {
                    index = 1;
                } else {
                    index = 0;
                }
                break;
            case MODE_SD:
                index = sizeList - 1;
                break;
            case MODE_DEFAULT:
            default:
                return getBestResolutionByNetwork(UzCoreUtil.getContext());
        }
        return bestResolutionList.get(index);
    }

    /**
     * Gets the best resolutions of current camera.
     *
     * @return the list of resolutions
     */
    private List<Camera.Size> getBestResolutionList() {
        if (!isCameraValid()) {
            return null;
        }
        List<Camera.Size> sizeListFront = rtmpCamera1.getResolutionsFront();
        if (sizeListFront == null || sizeListFront.isEmpty()) {
            return null;
        }
        List<Camera.Size> bestResolutionFrontList = new ArrayList<>();
        for (int i = 0; i < sizeListFront.size(); i++) {
            Camera.Size size = sizeListFront.get(i);
            float w = size.width;
            float h = size.height;
            float ratioWH = w / h;
            if (ratioWH == 16f / 9f) {
                bestResolutionFrontList.add(size);
            }
        }
        return bestResolutionFrontList;
    }

    /**
     * Check that can open camera of current device or not.
     *
     * @param facing current camera facing
     * @param width the width
     * @param height the height
     * @return true if can open, false otherwise
     */
    private boolean checkCanOpen(CameraHelper.Facing facing, int width, int height) {
        List<Camera.Size> previews;
        if (facing == CameraHelper.Facing.BACK) {
            previews = rtmpCamera1.getResolutionsBack();
        } else {
            previews = rtmpCamera1.getResolutionsFront();
        }
        for (Camera.Size size : previews) {
            if (size.width == width && size.height == height) {
                return true;
            }
        }
        return false;
    }
}

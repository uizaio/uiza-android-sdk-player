package uizalivestream.view;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import uizalivestream.interfaces.CameraCallback;
import uizalivestream.model.PresetLiveStreamingFeed;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.utils.util.SentryUtils;

final class RtmpCameraHelper {

    private static final String TAG = RtmpCameraHelper.class.getSimpleName();
    private RtmpCamera1 rtmpCamera1;
    private CameraCallback cameraCallback;
    private int audioBitRate = 128 * 1024; //kps

    RtmpCameraHelper(RtmpCamera1 camera) {
        this.rtmpCamera1 = camera;
    }

    RtmpCamera1 getRtmpCamera() {
        return rtmpCamera1;
    }

    void setCameraCallback(CameraCallback cameraCallback) {
        this.cameraCallback = cameraCallback;
    }

    boolean isAAEnabled() {
        return isCameraValid() && rtmpCamera1.getGlInterface().isAAEnabled();
    }

    void enableAA(boolean isEnable) {
        if (!isCameraValid()) return;
        rtmpCamera1.getGlInterface().enableAA(isEnable);
        // filters. NOTE: You can change filter values on fly without reset the filter.
        // Example:
        // ColorFilterRender color = new ColorFilterRender()
        // rtmpCamera1.setFilter(color);
        // color.setRGBColor(255, 0, 0); //red tint
    }

    void setFilter(BaseFilterRender baseFilterRender) {
        if (!isCameraValid()) return;
        rtmpCamera1.getGlInterface().setFilter(baseFilterRender);
    }

    int getStreamWidth() {
        if (!isCameraValid()) return 0;
        return rtmpCamera1.getStreamWidth();
    }

    int getStreamHeight() {
        if (!isCameraValid()) return 0;
        return rtmpCamera1.getStreamHeight();
    }

    void enableLantern() {
        if (!isCameraValid()) return;
        try {
            rtmpCamera1.enableLantern();
        } catch (Exception e) {
            SentryUtils.captureException(e);
        }
    }

    void disableLantern() {
        if (!isCameraValid()) return;
        rtmpCamera1.disableLantern();
    }

    void toggleLantern() {
        if (!isCameraValid()) return;
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
        if (!isCameraValid()) return null;
        return rtmpCamera1.isLanternEnabled();
    }

    void stopPreview() {
        if (!isCameraValid()) return;
        rtmpCamera1.stopPreview();
    }

    int[] getBestSizePreview() {
        List<Camera.Size> sizeList = getBestResolutionList();
        int[] result = new int[2];
        if (sizeList == null || sizeList.isEmpty()) {
            result[0] = LScreenUtil.getScreenWidth();
            result[1] = LScreenUtil.getScreenHeight();
        } else {
            result[0] = sizeList.get(0).width;
            result[1] = sizeList.get(0).height;
        }
        return result;
    }

    boolean isStreaming() {
        if (!isCameraValid()) return false;
        return rtmpCamera1.isStreaming();
    }

    boolean isRecording() {
        if (!isCameraValid()) return false;
        return rtmpCamera1.isRecording();
    }

    boolean prepareVideo(int width, int height, int fps, int bitrate,
                         boolean hardwareRotation, int iFrameInterval, int rotation) {
        if (!isCameraValid()) return false;

        LLog.d(TAG, "prepareVideo ===> " + width + "x" + height + ", bitrate " + bitrate + ", fps: " + fps + ", rotation: " + rotation + ", hardwareRotation: " + hardwareRotation);
        rtmpCamera1.startPreview(width, height);
        return rtmpCamera1.prepareVideo(width, height, fps, bitrate, hardwareRotation, iFrameInterval, rotation);
    }

    /**
      * dynamic audio bitrate, detect via video
      *
      * @param sampleRate
      * @param isStereo
      * @param echoCanceler
      * @param noiseSuppressor
      * @return
      */
    boolean prepareAudio(int sampleRate, boolean isStereo, boolean echoCanceler,
                          boolean noiseSuppressor) {
        LLog.d(TAG, "prepareAudio ===> bitrate " + audioBitRate + ", sampleRate: " + sampleRate + ", isStereo: " + isStereo + ", echoCanceler: " + echoCanceler + ", noiseSuppressor: " + noiseSuppressor);
        return rtmpCamera1.prepareAudio(audioBitRate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler,
                         boolean noiseSuppressor) {
        if (!isCameraValid()) return false;
        LLog.d(TAG, "prepareAudio ===> bitrate " + bitrate + ", sampleRate: " + sampleRate + ", isStereo: " + isStereo + ", echoCanceler: " + echoCanceler + ", noiseSuppressor: " + noiseSuppressor);
        return rtmpCamera1.prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    boolean prepareVideoFullHD(Context context, PresetLiveStreamingFeed presetLiveStreamingFeed, boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoFullHD false with presetLiveStreamingFeed null");
            return false;
        }
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            Log.e(TAG, "prepareVideoFullHD false -> bestResolutionList null or empty");
            return false;
        }
        Camera.Size bestSize = bestResolutionList.get(0);
        int bestBitrate = getBestBitrate(context, presetLiveStreamingFeed);
        audioBitRate = presetLiveStreamingFeed.getAudioBitRate(bestBitrate);
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1, isLandscape ? 0 : 90);
    }

    boolean prepareVideoHD(Context context, PresetLiveStreamingFeed presetLiveStreamingFeed, boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoHD false with presetLiveStreamingFeed null");
            return false;
        }
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            Log.e(TAG, "prepareVideoHD false -> bestResolutionList null or empty");
            return false;
        }
        int sizeList = bestResolutionList.size();
        int index;
        if (sizeList > 2) {
            index = sizeList / 2;
        } else if (sizeList == 2) {
            index = 1;
        } else {
            index = 0;
        }
        Camera.Size bestSize = bestResolutionList.get(index);
        int bestBitrate = getBestBitrate(context, presetLiveStreamingFeed);
        audioBitRate = presetLiveStreamingFeed.getAudioBitRate(bestBitrate);
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1, isLandscape ? 0 : 90);
    }

    boolean prepareVideoSD(Context context, PresetLiveStreamingFeed presetLiveStreamingFeed, boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoSD false with presetLiveStreamingFeed null");
            return false;
        }
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            Log.e(TAG, "prepareVideoSD false -> bestResolutionList null or empty");
            return false;
        }
        Camera.Size bestSize = bestResolutionList.get(bestResolutionList.size() - 1);
        int bestBitrate = getBestBitrate(context, presetLiveStreamingFeed);
        audioBitRate = presetLiveStreamingFeed.getAudioBitRate(bestBitrate);
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1, isLandscape ? 0 : 90);
    }

    boolean prepareVideo(Context context, PresetLiveStreamingFeed presetLiveStreamingFeed, boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideo false with presetLiveStreamingFeed null");
            return false;
        }
        if (!isCameraValid()) {
            Log.e(TAG, "prepareVideo false -> rtmpCamera1 == null");
            return false;
        }
        Camera.Size bestSize = getBestResolution(context);
        int bestBitrate = getBestBitrate(context, presetLiveStreamingFeed);
        audioBitRate = presetLiveStreamingFeed.getAudioBitRate(bestBitrate);
        if (bestSize == null) {
            Log.e(TAG, "prepareVideo false -> bestSize == null");
            return false;
        }
        return prepareVideo(bestSize.width, bestSize.height, 30, bestBitrate, false, 1, isLandscape ? 0 : 90);
    }

    void switchCamera() {
        if (!isCameraValid()) return;
        rtmpCamera1.switchCamera();
        if (cameraCallback != null) {
            cameraCallback.onCameraChange(rtmpCamera1.isFrontCamera());
        }
    }

    boolean startPreview(CameraHelper.Facing facing, int width, int height) {
        if (!isCameraValid()) return false;
        if (checkCanOpen(facing, width, height)) {
            rtmpCamera1.startPreview(facing, width, height);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void startRecord(String videoPath) throws IOException {
        if (!isCameraValid()) return;
        rtmpCamera1.startRecord(videoPath);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    void stopRecord() {
        if (!isCameraValid()) return;
        rtmpCamera1.stopRecord();
    }

    void startStream(String streamURL) {
        if (!isCameraValid()) return;
        rtmpCamera1.startStream(streamURL);
    }

    void stopStream() {
        if (!isCameraValid()) return;
        rtmpCamera1.stopStream();
    }

    boolean isCameraValid() {
        return rtmpCamera1 != null;
    }

    private int getBestBitrate(Context context, PresetLiveStreamingFeed presetLiveStreamingFeed) {
        int bestBitrate;
        if (LConnectivityUtil.isConnectedFast(context) && LConnectivityUtil.isConnectedWifi(context)) {
            bestBitrate = presetLiveStreamingFeed.getS1080p();
        } else if (LConnectivityUtil.isConnectedFast(context) && LConnectivityUtil.isConnectedMobile(context)) {
            bestBitrate = presetLiveStreamingFeed.getS720p();
        } else {
            bestBitrate = presetLiveStreamingFeed.getS480p();
        }
        return bestBitrate;
    }

    private Camera.Size getBestResolution(@NonNull Context context) {
        List<Camera.Size> bestResolutionList = getBestResolutionList();
        if (bestResolutionList == null || bestResolutionList.isEmpty()) {
            return null;
        }
        int sizeList = bestResolutionList.size();
        int index;
        if (LConnectivityUtil.isConnectedFast(context)
                && LConnectivityUtil.isConnectedWifi(context)) {
            index = 0;
        } else if (LConnectivityUtil.isConnectedFast(context)
                && LConnectivityUtil.isConnectedMobile(context)) {
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

    private List<Camera.Size> getBestResolutionList() {
        //WORKS FINE
        /*List<Camera.Size> sizeListFront = rtmpCamera1.getResolutionsFront();
        List<Camera.Size> sizeListBack = rtmpCamera1.getResolutionsBack();
        if (sizeListFront == null || sizeListFront.isEmpty() || sizeListBack == null || sizeListBack.isEmpty()) {
            return null;
        }
        List<Camera.Size> bestList = new ArrayList<>();
        //scan sizeListFront
        List<Camera.Size> bestResolutionFrontList = new ArrayList<>();
        for (int i = 0; i < sizeListFront.size(); i++) {
            Camera.Size size = sizeListFront.get(i);
            float w = size.width;
            float h = size.height;
            float ratioWH = w / h;
            LLog.d(TAG, "front " + i + " -> " + w + "x" + h + " -> " + ratioWH);
            if (ratioWH == 16f / 9f) {
                bestResolutionFrontList.add(size);
            }
        }
        //scan sizeListBack
        List<Camera.Size> bestResolutionBackList = new ArrayList<>();
        for (int i = 0; i < sizeListFront.size(); i++) {
            Camera.Size size = sizeListFront.get(i);
            float w = size.width;
            float h = size.height;
            float ratioWH = w / h;
            LLog.d(TAG, "back " + i + " -> " + w + "x" + h + " -> " + ratioWH);
            if (ratioWH == 16f / 9f) {
                bestResolutionBackList.add(size);
            }
        }
        //get same size between front and back list
        for (int i = 0; i < bestResolutionFrontList.size(); i++) {
            Camera.Size sizeF = bestResolutionFrontList.get(i);
            for (int j = 0; j < bestResolutionBackList.size(); j++) {
                Camera.Size sizeB = bestResolutionBackList.get(j);
                if (sizeF.width == sizeB.width && sizeF.height == sizeB.height) {
                    bestList.add(sizeF);
                }
            }
        }
        for (int i = 0; i < bestList.size(); i++) {
            LLog.d(TAG, "final " + bestList.get(i).width + "x" + bestList.get(i).height);
        }
        return bestList;*/
        if (!isCameraValid()) return null;
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

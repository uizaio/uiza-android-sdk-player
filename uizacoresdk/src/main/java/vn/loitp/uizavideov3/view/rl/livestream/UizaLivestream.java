package vn.loitp.uizavideov3.view.rl.livestream;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import loitp.core.R;
import retrofit2.HttpException;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BaseFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.utils.gl.TranslateTo;
import vn.loitp.libstream.uiza.ossrs.rtmp.ConnectCheckerRtmp;
import vn.loitp.libstream.uiza.rtplibrary.rtmp.RtmpCamera1;
import vn.loitp.libstream.uiza.rtplibrary.view.OpenGlView;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.ErrorBody;
import vn.loitp.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.views.LToast;

/**
 * Created by loitp on 7/26/2017.
 */

public class UizaLivestream extends RelativeLayout implements ConnectCheckerRtmp, SurfaceHolder.Callback {
    private final String TAG = "TAG" + getClass().getSimpleName();
    //TODO remove gson later
    private Gson gson = new Gson();
    private RtmpCamera1 rtmpCamera1;
    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Uizalivestream");
    private OpenGlView openGlView;
    private PresetLiveStreamingFeed presetLiveStreamingFeed;
    private ProgressBar progressBar;
    private TextView tvLiveStatus;
    private Callback callback;
    private String mainStreamUrl;

    public UizaLivestream(Context context) {
        super(context);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public RtmpCamera1 getRtmpCamera() {
        return rtmpCamera1;
    }

    public OpenGlView getOpenGlView() {
        return openGlView;
    }

    public PresetLiveStreamingFeed getPresetLiveStreamingFeed() {
        return presetLiveStreamingFeed;
    }

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_livestream_filter, this);
        tvLiveStatus = (TextView) findViewById(R.id.tv_live_status);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, Color.WHITE);
        openGlView = findViewById(R.id.surfaceView);
        //Number of filters to use at same time.
        // You must modify it before create rtmp or rtsp object.
        //ManagerRender.numFilters = 2;
        rtmpCamera1 = new RtmpCamera1(openGlView, this);
        openGlView.getHolder().addCallback(this);
        //openGlView.getHolder().setFixedSize(LScreenUtil.getScreenWidth(), LScreenUtil.getScreenWidth() * 16 / 9);

        //openGlView.setKeepAspectRatio(true);
        //openGlView.setFrontPreviewFlip(true);
    }

    private void updateUISurfaceView(int width, int height) {
        int screenWidth = LScreenUtil.getScreenWidth();
        openGlView.getLayoutParams().width = screenWidth;
        openGlView.getLayoutParams().height = width * screenWidth / height;
        LLog.d(TAG, "updateUISurfaceView " + screenWidth + "x" + (width * screenWidth / height));
        openGlView.requestLayout();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onConnectionSuccessRtmp() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLiveStatus.setVisibility(VISIBLE);
            }
        });
        if (callback != null) {
            callback.onConnectionSuccessRtmp();
        }
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLiveStatus.setVisibility(GONE);
                rtmpCamera1.stopStream();
            }
        });
        if (callback != null) {
            callback.onConnectionFailedRtmp(reason);
        }
    }

    @Override
    public void onDisconnectRtmp() {
        if (callback != null) {
            callback.onDisconnectRtmp();
        }
    }

    @Override
    public void onAuthErrorRtmp() {
        if (callback != null) {
            callback.onAuthErrorRtmp();
        }
    }

    @Override
    public void onAuthSuccessRtmp() {
        if (callback != null) {
            callback.onAuthSuccessRtmp();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (callback != null) {
            callback.surfaceCreated();
        }
        LUIUtil.hideProgressBar(progressBar);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        LLog.d(TAG, "surfaceChanged " + i1 + "x" + i2);
        //rtmpCamera1.startPreview();
        //rtmpCamera1.startPreview(Camera.CameraInfo.CAMERA_FACING_FRONT);
        //rtmpCamera1.startPreview(1280, 720);
        //rtmpCamera1.startPreview(Camera.CameraInfo.CAMERA_FACING_BACK, 1280, 720);
        //rtmpCamera1.startPreview(Camera.CameraInfo.CAMERA_FACING_FRONT, 1280, 720);
        //updateUISurfaceView();
        if (callback != null) {
            callback.surfaceChanged(new StartPreview() {
                @Override
                public void onSizeStartPreview(int width, int height) {
                    rtmpCamera1.startPreview(Camera.CameraInfo.CAMERA_FACING_FRONT, width, height);
                    updateUISurfaceView(width, height);
                }
            });
        }
    }

    public interface StartPreview {
        public void onSizeStartPreview(int width, int height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
            LToast.show(getContext(), "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
            currentDateAndTime = "";
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
        }
        rtmpCamera1.stopPreview();
    }

    public boolean isStreaming() {
        return rtmpCamera1.isStreaming();
    }

    public void startStream(String streamUrl) {
        startStream(streamUrl, false);
    }

    public void startStream(String streamUrl, boolean isSavedToDevice) {
        rtmpCamera1.startStream(streamUrl);
        LLog.d(TAG, "startStream streamUrl " + streamUrl + ", isSavedToDevice: " + isSavedToDevice);
        if (isSavedToDevice) {
            startRecord();
        }
    }

    public void stopStream() {
        if (isRecording()) {
            stopRecord();
        }
        rtmpCamera1.stopStream();
    }

    public boolean prepareAudio() {
        return prepareAudio(128, 44100, true, false, false);
    }

    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        LLog.d(TAG, "prepareAudio ===> bitrate " + bitrate + ", sampleRate: " + sampleRate + ", isStereo: " + isStereo + ", echoCanceler: " + echoCanceler + ", noiseSuppressor: " + noiseSuppressor);
        return rtmpCamera1.prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    public boolean prepareVideoFullHD(boolean isLandscape) {
        Camera.Size size = getCorrectCameraSize(1920, 1080);
        if (size == null) {
            Log.e(TAG, getContext().getString(R.string.err_dont_support));
            return false;
        }
        return prepareVideo(size.width, size.height, 30, presetLiveStreamingFeed.getS1080p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideoHD(boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoFullHD false with presetLiveStreamingFeed null");
            return false;
        }
        Camera.Size size = getCorrectCameraSize(1280, 720);
        if (size == null) {
            Log.e(TAG, getContext().getString(R.string.err_dont_support));
            return false;
        }
        return prepareVideo(size.width, size.height, 30, presetLiveStreamingFeed.getS720p(), false, isLandscape ? 0 : 90);
        //return prepareVideo(1280, 720, 30, presetLiveStreamingFeed.getS720p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideoSD(boolean isLandscape) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoFullHD false with presetLiveStreamingFeed null");
            return false;
        }
        Camera.Size size = getCorrectCameraSize(640, 360);
        if (size == null) {
            Log.e(TAG, getContext().getString(R.string.err_dont_support));
            return false;
        }
        return prepareVideo(size.width, size.height, 30, presetLiveStreamingFeed.getS480p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int rotation) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoFullHD false with presetLiveStreamingFeed null");
            return false;
        }
        LLog.d(TAG, "prepareVideo ===> " + width + "x" + height + ", bitrate " + bitrate + ", fps: " + fps + ", rotation: " + rotation + ", hardwareRotation: " + hardwareRotation);
        rtmpCamera1.startPreview(width, height);
        boolean isPrepareVideo = rtmpCamera1.prepareVideo(width, height, fps, bitrate, hardwareRotation, rotation);
        //updateUISurfaceView();
        return isPrepareVideo;
    }

    public void switchCamera() {
        rtmpCamera1.switchCamera();
    }

    public boolean isRecording() {
        return rtmpCamera1.isRecording();
    }

    private void startRecord() {
        if (!isStreaming()) {
            LLog.e(TAG, "startRecord !isStreaming() -> return");
            return;
        }
        try {
            if (!folder.exists()) {
                folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            currentDateAndTime = sdf.format(new Date());
            rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
            LLog.d(TAG, "Recording...");
        } catch (IOException e) {
            rtmpCamera1.stopRecord();
            LLog.e(TAG, "Error startRecord " + e.toString());
        }
    }

    private void stopRecord() {
        rtmpCamera1.stopRecord();
        LToast.show(getContext(), "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
        currentDateAndTime = "";
    }

    public boolean isAAEnabled() {
        return rtmpCamera1.getGlInterface().isAAEnabled();
    }

    /*
     **AAEnabled true is AA enabled, false is AA disabled. False by default.
     */
    public void enableAA(boolean isEnable) {
        rtmpCamera1.getGlInterface().enableAA(isEnable);
        //filters. NOTE: You can change filter values on fly without reset the filter.
        // Example:
        // ColorFilterRender color = new ColorFilterRender()
        // rtmpCamera1.setFilter(color);
        // color.setRGBColor(255, 0, 0); //red tint
    }

    public void setFilter(BaseFilterRender baseFilterRender) {
        rtmpCamera1.getGlInterface().setFilter(baseFilterRender);
    }

    public int getStreamWidth() {
        return rtmpCamera1.getStreamWidth();
    }

    public int getStreamHeight() {
        return rtmpCamera1.getStreamHeight();
    }

    public void setTextToStream(String text, int textSize, int textCorlor, TranslateTo translateTo) {
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        setFilter(textObjectFilterRender);
        textObjectFilterRender.setText(text, textSize, textCorlor);
        textObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        textObjectFilterRender.setPosition(translateTo);
        textObjectFilterRender.setListeners(getOpenGlView());  //Optional
    }

    public void setImageToStream(int res, TranslateTo translateTo) {
        ImageObjectFilterRender imageObjectFilterRender = new ImageObjectFilterRender();
        setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(BitmapFactory.decodeResource(getResources(), res));
        imageObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        imageObjectFilterRender.setPosition(translateTo);
        imageObjectFilterRender.setListeners(getOpenGlView()); //Optional
    }

    public boolean setGifToStream(int res, TranslateTo translateTo) {
        try {
            GifObjectFilterRender gifObjectFilterRender = new GifObjectFilterRender();
            gifObjectFilterRender.setGif(getResources().openRawResource(res));
            setFilter(gifObjectFilterRender);
            gifObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
            gifObjectFilterRender.setPosition(translateTo);
            gifObjectFilterRender.setListeners(getOpenGlView()); //Optional
            return true;
        } catch (IOException e) {
            LLog.e(TAG, "Error setGifToStream " + e.toString());
            LToast.show(getContext(), "Error " + e.toString());
            return false;
        }
    }

    private Camera.Size getCorrectCameraSize(int width, int height) {
        return rtmpCamera1.getCorrectCameraSize(width, height);
    }

    public List<Camera.Size> getListCameraResolutionSupportBack() {
        return rtmpCamera1.getListCameraResolutionSupportBack();
    }

    public List<Camera.Size> getListCameraResolutionSupportFront() {
        return rtmpCamera1.getListCameraResolutionSupportFront();
    }

    public String getMainStreamUrl() {
        return mainStreamUrl;
    }

    public void setId(final String entityLiveId) {
        if (entityLiveId == null || entityLiveId.isEmpty()) {
            throw new NullPointerException(getContext().getString(R.string.entity_cannot_be_null_or_empty));
        }
        //Chỉ cần gọi start live thôi, ko cần quan tâm đến kết quả của api này start success hay ko
        //Vẫn tiếp tục gọi detail entity để lấy streamUrl
        startLivestream(entityLiveId);
    }

    private void startLivestream(final String entityLiveId) {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId(entityLiveId);
        ((BaseActivity) getContext()).subscribe(service.startALiveEvent(bodyStartALiveFeed), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "startLivestream onSuccess " + new Gson().toJson(result));
                getDetailEntity(entityLiveId, false, null);
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "startLivestream onFail " + e.toString() + ", " + e.getMessage());
                HttpException error = (HttpException) e;
                String responseBody = null;
                try {
                    responseBody = error.response().errorBody().string();
                    ErrorBody errorBody = gson.fromJson(responseBody, ErrorBody.class);
                    Log.e(TAG, "startLivestream onFail try " + errorBody);
                    /*if (callback != null) {
                        callback.onError(errorBody.getMessage());
                    }*/
                    getDetailEntity(entityLiveId, true, errorBody.getMessage());
                } catch (IOException e1) {
                    Log.e(TAG, "startLivestream IOException catch " + e1.toString());
                    /*if (callback != null) {
                        callback.onError(e1.getMessage());
                    }*/
                    getDetailEntity(entityLiveId, true, e1.getMessage());
                }
            }
        });
    }

    private void getDetailEntity(String entityLiveId, final boolean isErrorStartLive, final String errorMsg) {
        UizaUtil.getDataFromEntityIdLIVE((BaseActivity) getContext(), entityLiveId, new UizaUtil.Callback() {
            @Override
            public void onSuccess(Data d) {
                LLog.d(TAG, "init getDetailEntity onSuccess: " + gson.toJson(d));
                if (d == null || d.getLastPushInfo() == null || d.getLastPushInfo().isEmpty()) {
                    throw new NullPointerException("Data is null");
                }
                String streamKey = d.getLastPushInfo().get(0).getStreamKey();
                String streamUrl = d.getLastPushInfo().get(0).getStreamUrl();
                String mainUrl = streamUrl + "/" + streamKey;
                LLog.d(TAG, ">>>>mainUrl: " + mainUrl);

                mainStreamUrl = mainUrl;

                boolean isTranscode = d.getEncode() == 1;//1 is Push with Transcode, !1 Push-only, no transcode
                LLog.d(TAG, "isTranscode " + isTranscode);

                presetLiveStreamingFeed = new PresetLiveStreamingFeed();
                presetLiveStreamingFeed.setTranscode(isTranscode);

                boolean isConnectedFast = LConnectivityUtil.isConnectedFast(getContext());
                if (isTranscode) {
                    //Push with Transcode
                    presetLiveStreamingFeed.setS1080p(isConnectedFast ? 5000000 : 2500000);
                    presetLiveStreamingFeed.setS720p(isConnectedFast ? 3000000 : 1500000);
                    presetLiveStreamingFeed.setS480p(isConnectedFast ? 1500000 : 800000);
                } else {
                    //Push-only, no transcode
                    presetLiveStreamingFeed.setS1080p(isConnectedFast ? 2500000 : 1500000);
                    presetLiveStreamingFeed.setS720p(isConnectedFast ? 1500000 : 800000);
                    presetLiveStreamingFeed.setS480p(isConnectedFast ? 800000 : 400000);
                }

                if (isErrorStartLive) {
                    if (d.getLastProcess() == null) {
                        if (callback != null) {
                            LLog.d(TAG, "isErrorStartLive -> onError Last process null");
                            callback.onError("Error: Last process null");
                        }
                    } else {
                        if (d.getLastProcess().toLowerCase().equals(Constants.LAST_PROCESS_START)) {
                            LLog.d(TAG, "Start live 400 but last process START -> can livestream");
                            if (callback != null) {
                                callback.onGetDataSuccess(d, mainUrl, isTranscode, presetLiveStreamingFeed);
                            }
                        } else if ((d.getLastProcess().toLowerCase().equals(Constants.LAST_PROCESS_STOP))) {
                            LLog.d(TAG, "Start live 400 but last process STOP -> cannot livestream");
                            if (callback != null) {
                                callback.onError(errorMsg);
                            }
                        }
                    }
                } else {
                    if (callback != null) {
                        LLog.d(TAG, "onGetDataSuccess");
                        callback.onGetDataSuccess(d, mainUrl, isTranscode, presetLiveStreamingFeed);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LLog.e(TAG, "setId onError " + e.toString());
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    public interface Callback {
        public void onError(String reason);

        public void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode, PresetLiveStreamingFeed presetLiveStreamingFeed);

        public void onConnectionSuccessRtmp();

        public void onConnectionFailedRtmp(String reason);

        public void onDisconnectRtmp();

        public void onAuthErrorRtmp();

        public void onAuthSuccessRtmp();

        public void surfaceCreated();

        public void surfaceChanged(StartPreview startPreview);
    }
}
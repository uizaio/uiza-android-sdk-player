package vn.loitp.uizavideov3.view.rl.livestream.uiza;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BaseFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.utils.gl.TranslateTo;
import vn.loitp.libstream.uiza.ossrs.rtmp.ConnectCheckerRtmp;
import vn.loitp.libstream.uiza.rtplibrary.rtmp.RtmpCamera1;
import vn.loitp.libstream.uiza.rtplibrary.view.OpenGlView;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.views.LToast;

/**
 * Created by loitp on 7/26/2017.
 */

public class UizaLivestream extends RelativeLayout implements ConnectCheckerRtmp, SurfaceHolder.Callback {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private RtmpCamera1 rtmpCamera1;
    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Uizalivestream");
    private OpenGlView openGlView;
    private PresetLiveStreamingFeed presetLiveStreamingFeed;
    private ProgressBar progressBar;
    private TextView tvLiveStatus;

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

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_livestream, this);
        tvLiveStatus = (TextView) findViewById(R.id.tv_live_status);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, Color.WHITE);
        openGlView = findViewById(R.id.surfaceView);
        //Number of filters to use at same time.
        // You must modify it before create rtmp or rtsp object.
        //ManagerRender.numFilters = 2;
        rtmpCamera1 = new RtmpCamera1(openGlView, this);
        openGlView.getHolder().addCallback(this);

        //openGlView.setKeepAspectRatio(true);
        //openGlView.setFrontPreviewFlip(true);
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onConnectionSuccessRtmp() {
        if (callback != null) {
            callback.onConnectionSuccessRtmp();
        }
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        if (callback != null) {
            callback.onConnectionFailedRtmp(reason);
        }
        rtmpCamera1.stopStream();
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

    public void setId(String entityLiveId) {
        if (entityLiveId == null || entityLiveId.isEmpty()) {
            throw new NullPointerException(getContext().getString(R.string.entity_cannot_be_null_or_empty));
        }
        UizaUtil.getDetailEntity((BaseActivity) getContext(), entityLiveId, new UizaUtil.Callback() {
            @Override
            public void onSuccess(Data d) {
                //LLog.d(TAG, "init getDetailEntity onSuccess: " + new Gson().toJson(d));
                if (d == null || d.getLastPushInfo() == null || d.getLastPushInfo().isEmpty()) {
                    throw new NullPointerException("Data is null");
                }
                String streamKey = d.getLastPushInfo().get(0).getStreamKey();
                String streamUrl = d.getLastPushInfo().get(0).getStreamUrl();
                String mainUrl = streamUrl + "/" + streamKey;
                LLog.d(TAG, "mainUrl: " + mainUrl);

                boolean isTranscode = d.getEncode() == 1;//1 is Push with Transcode, !1 Push-only, no transcode
                LLog.d(TAG, "isTranscode " + isTranscode);

                presetLiveStreamingFeed = new PresetLiveStreamingFeed();
                presetLiveStreamingFeed.setTranscode(isTranscode);

                if (isTranscode) {
                    //Push with Transcode
                    presetLiveStreamingFeed.setS1080p(5000000);
                    presetLiveStreamingFeed.setS720p(3000000);
                    presetLiveStreamingFeed.setS480p(1500000);
                } else {
                    //Push-only, no transcode
                    presetLiveStreamingFeed.setS1080p(2500000);
                    presetLiveStreamingFeed.setS720p(1500000);
                    presetLiveStreamingFeed.setS480p(800000);
                }

                if (callback != null) {
                    callback.onGetDataSuccess(d, mainUrl, isTranscode, presetLiveStreamingFeed);
                }
            }

            @Override
            public void onError(Throwable e) {
                LLog.e(TAG, "setId onError " + e.toString());
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        LLog.d(TAG, "surfaceChanged");
        //rtmpCamera1.startPreview();
        //rtmpCamera1.startPreview(1280, 720);
        rtmpCamera1.startPreview(Camera.CameraInfo.CAMERA_FACING_FRONT, 1280, 720);
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
        tvLiveStatus.setVisibility(VISIBLE);

        if (isSavedToDevice) {
            startRecord();
        }
    }

    public void stopStream() {
        if (isRecording()) {
            stopRecord();
        }
        rtmpCamera1.stopStream();
        tvLiveStatus.setVisibility(GONE);
    }

    public boolean prepareAudio() {
        return prepareAudio(128, 44100, true, false, false);
    }

    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        return rtmpCamera1.prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    public boolean prepareVideo1080p(boolean isLandscape) {
        return prepareVideo(1920, 1080, 30, presetLiveStreamingFeed.getS1080p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideo720p(boolean isLandscape) {
        return prepareVideo(1280, 730, 30, presetLiveStreamingFeed.getS720p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideo480p(boolean isLandscape) {
        return prepareVideo(854, 480, 30, presetLiveStreamingFeed.getS480p(), false, isLandscape ? 0 : 90);
    }

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int rotation) {
        return rtmpCamera1.prepareVideo(width, height, fps, bitrate, hardwareRotation, rotation);
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
            if (!rtmpCamera1.isStreaming()) {
                if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                    rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                    LLog.d(TAG, "Recording...");
                } else {
                    LLog.e(TAG, "Error preparing stream, This device cant do it");
                }
            } else {
                rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                LLog.d(TAG, "Recording...");
            }
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

    public interface Callback {
        public void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode, PresetLiveStreamingFeed presetLiveStreamingFeed);

        public void onConnectionSuccessRtmp();

        public void onConnectionFailedRtmp(String reason);

        public void onDisconnectRtmp();

        public void onAuthErrorRtmp();

        public void onAuthSuccessRtmp();

        public void surfaceCreated();
    }
}
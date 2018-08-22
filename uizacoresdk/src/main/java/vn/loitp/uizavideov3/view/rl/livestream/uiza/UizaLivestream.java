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

    @Override
    public void onConnectionSuccessRtmp() {
        LToast.show(getContext(), "Connection success");
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        LToast.show(getContext(), "Connection failed");
        rtmpCamera1.stopStream();
    }

    @Override
    public void onDisconnectRtmp() {
        LToast.show(getContext(), "Disconnected");
    }

    @Override
    public void onAuthErrorRtmp() {
        LToast.show(getContext(), "Auth error");
    }

    @Override
    public void onAuthSuccessRtmp() {
        LToast.show(getContext(), "Auth success");
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        LLog.d(TAG, "surfaceCreated");
        LUIUtil.hideProgressBar(progressBar);
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

    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        //return rtmpCamera1.prepareAudio(128, 44100, true, false, false);
        return rtmpCamera1.prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int rotation) {
        //return rtmpCamera1.prepareVideo(1920, 1080, 30, 2500000, false, 0);
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
}
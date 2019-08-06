package io.uiza.broadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pedro.encoder.input.gl.SpriteGestureController;
import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.utils.gl.TranslateTo;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.view.OpenGlView;
import io.uiza.broadcast.config.LiveConfig;
import io.uiza.broadcast.config.PresetLiveFeed;
import io.uiza.broadcast.util.UzLiveVideoMode;
import io.uiza.broadcast.util.UzLivestreamError;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import net.ossrs.rtmp.ConnectCheckerRtmp;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.CallbackGetDetailEntity;
import vn.uiza.utils.UZUtilBase;
import vn.uiza.utils.util.AppUtils;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.views.LToast;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class UzLivestream extends RelativeLayout implements ConnectCheckerRtmp,
        SurfaceHolder.Callback, View.OnTouchListener {

    private static final String TAG = UzLivestream.class.getSimpleName();
    private static final String TIME_FORMAT = "yyyyMMdd_HHmmss";
    private static final int LIVESTREAM_PERMISSION_RC = 101;
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private final File folder = new File(
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils
                    .getAppName());
    private final SpriteGestureController spriteGestureController = new SpriteGestureController();
    private String currentDateAndTime = "";
    private OpenGlView openGlView;
    private PresetLiveFeed presetLiveFeed;
    private ProgressBar progressBar;
    private TextView tvLiveStatus;
    private UzLivestreamCallback uzLivestreamCallback;
    private String mainStreamUrl;
    private boolean isShowDialogCheck;
    private UzLiveCameraHelper cameraHelper;
    private boolean isSavedToDevice;
    private CameraHelper.Facing lastFacing = CameraHelper.Facing.FRONT;
    private long backgroundAllowedDuration = 2 * MINUTE; // default is 2 minutes
    private boolean isBroadcastingBeforeGoingBackground;
    private boolean isFromBackgroundTooLong;
    private boolean isLandscape;
    private boolean visibleLiveText;
    private CountDownTimer backgroundTimer;
    private UzLiveVideoMode liveVideoMode = UzLiveVideoMode.MODE_DEFAULT;

    public UzLivestream(Context context) {
        super(context);
    }

    public UzLivestream(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UzLivestream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UzLivestream(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Sets the camera listener for listen the camera changed event.
     *
     * @param cameraCallback the camera callback
     */
    public void setCameraCallback(UzLiveCameraCallback cameraCallback) {
        if (cameraHelper != null) {
            cameraHelper.setCameraCallback(cameraCallback);
        }
    }

    public SpriteGestureController getSpriteGestureController() {
        return spriteGestureController;
    }

    public void setVisibleLiveText(boolean visibleLiveText) {
        this.visibleLiveText = visibleLiveText;
    }

    /**
     * Gets the current camera.
     *
     * @return the current camera
     */
    public RtmpCamera1 getRtmpCamera() {
        if (cameraHelper == null) {
            notifyError(UzLivestreamError.LIVESTREAM_NOT_INITIALIZED);
            return null;
        }
        return cameraHelper.getRtmpCamera();
    }

    public PresetLiveFeed getPresetLiveFeed() {
        return presetLiveFeed;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onCreate() {
        inflate(getContext(), R.layout.layout_uz_livestream, this);
        tvLiveStatus = findViewById(R.id.tv_live_status);
        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, Color.WHITE);
        openGlView = findViewById(R.id.surfaceView);
        RtmpCamera1 rtmpCamera = new RtmpCamera1(openGlView, this);
        cameraHelper = new UzLiveCameraHelper(rtmpCamera);
        openGlView.getHolder().addCallback(this);
        openGlView.setOnTouchListener(this);
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onUiCreated();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (spriteGestureController.spriteTouched(view, motionEvent)) {
            spriteGestureController.moveSprite(view, motionEvent);
            spriteGestureController.scaleSprite(motionEvent);
            return true;
        }
        return false;
    }

    // Stop listener for image, text and gif stream objects.
    public void setBaseObjectFilterRender() {
        spriteGestureController.setBaseObjectFilterRender(null);
    }

    /**
     * Must be called when the app go to resume state.
     */
    public void onResume() {
        if (!isShowDialogCheck) {
            checkPermission();
            return;
        }

        checkAndResumeLivestreamIfNeeded();

        if (isFromBackgroundTooLong) {
            if (uzLivestreamCallback != null) {
                uzLivestreamCallback.onBackgroundTooLong();
            }
            isFromBackgroundTooLong = false;
        }
    }

    private void checkAndResumeLivestreamIfNeeded() {
        cancelBackgroundTimer();

        if (!isBroadcastingBeforeGoingBackground) {
            return;
        }

        isBroadcastingBeforeGoingBackground = false;
        // We delay a second because the surface need to be resumed before we can prepare something
        // Improve this method whenever you can
        LUIUtil.setDelay((int) SECOND, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                try {
                    stopStream(); // make sure stop stream and start it again
                    if (prepareAudio() && prepareVideo(liveVideoMode, isLandscape)) {
                        startStream(getMainStreamUrl(), isSavedToDevice());
                    }
                } catch (Exception ignored) {
                    LLog.e(TAG, "Can not resume livestream right now !");
                }
            }
        });
    }

    /**
     * Set duration which allows livestream to keep the info.
     *
     * @param duration the duration which allows livestream to keep the info
     */
    public void setBackgroundAllowedDuration(long duration) {
        this.backgroundAllowedDuration = duration;
    }

    private void checkPermission() {
        isShowDialogCheck = true;
        Dexter.withActivity((Activity) getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            onCreate();
                            if (uzLivestreamCallback != null) {
                                uzLivestreamCallback.onPermission(true);
                            }
                        } else {
                            showShouldAcceptPermission();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                        isShowDialogCheck = true;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    private void showShouldAcceptPermission() {
        AlertDialog alertDialog =
                LDialogUtil.showDialog2(getContext(), getString(R.string.need_permission),
                        getString(R.string.this_app_needs_permission), getString(R.string.okay),
                        getString(R.string.cancel), new LDialogUtil.Callback2() {
                            @Override
                            public void onClick1() {
                                checkPermission();
                            }

                            @Override
                            public void onClick2() {
                                if (uzLivestreamCallback != null) {
                                    uzLivestreamCallback.onPermission(false);
                                }
                            }
                        });
        alertDialog.setCancelable(false);
    }

    private void showSettingsDialog() {
        AlertDialog alertDialog =
                LDialogUtil.showDialog2(getContext(), getString(R.string.need_permission),
                        getString(R.string.this_app_needs_permission_grant_it),
                        getString(R.string.goto_settings), getString(R.string.cancel),
                        new LDialogUtil.Callback2() {
                            @Override
                            public void onClick1() {
                                isShowDialogCheck = false;
                                Intent intent =
                                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts(Constants.PACKAGE,
                                        AppUtils.getAppPackageName(), null);
                                intent.setData(uri);
                                ((Activity) getContext()).startActivityForResult(intent,
                                        LIVESTREAM_PERMISSION_RC);
                            }

                            @Override
                            public void onClick2() {
                                if (uzLivestreamCallback != null) {
                                    uzLivestreamCallback.onPermission(false);
                                }
                            }
                        });
        alertDialog.setCancelable(false);
    }

    private void updateSurfaceView(int width, int height) {
        if (openGlView == null) {
            return;
        }
        int screenWidth = LScreenUtil.getScreenWidth();
        openGlView.getLayoutParams().width = screenWidth;
        openGlView.getLayoutParams().height = width * screenWidth / height;
        openGlView.requestLayout();
    }

    public void setUzLivestreamCallback(UzLivestreamCallback callback) {
        this.uzLivestreamCallback = callback;
    }

    @Override
    public void onConnectionSuccessRtmp() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLiveText();
                LUIUtil.goneViews(progressBar);
            }
        });
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onConnectionSuccessRtmp();
        }

        isBroadcastingBeforeGoingBackground = true;
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLiveText();
                cameraHelper.stopStream();
            }
        });
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onConnectionFailedRtmp(reason);
        }
    }

    @Override
    public void onDisconnectRtmp() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLiveText();
                LUIUtil.goneViews(progressBar);
            }
        });
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onDisconnectRtmp();
        }
    }

    @Override
    public void onAuthErrorRtmp() {
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onAuthErrorRtmp();
        }
    }

    @Override
    public void onAuthSuccessRtmp() {
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onAuthSuccessRtmp();
            LUIUtil.goneViews(progressBar);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.surfaceCreated();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.surfaceChanged(new StartPreview() {
                @Override
                public void onSizeStartPreview(int width, int height) {
                    startPreview(lastFacing, width, height);
                }
            });
        }
    }

    private void startPreview(CameraHelper.Facing facing, int width, int height) {
        boolean canStart = cameraHelper.startPreview(facing, width, height);
        if (canStart) {
            updateSurfaceView(width, height);
        } else {
            notifyError(UzLivestreamError.CAMERA_NOT_RUNNING);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (!isCameraValid()) {
            return;
        }
        stopStream();
        stopPreview();
        startBackgroundTimer();
    }

    private void startBackgroundTimer() {
        if (backgroundTimer == null) {
            backgroundTimer = new CountDownTimer(backgroundAllowedDuration, SECOND) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    isBroadcastingBeforeGoingBackground = false;
                    isFromBackgroundTooLong = true;
                }
            };
        }
        backgroundTimer.start();
    }

    private void cancelBackgroundTimer() {
        if (backgroundTimer != null) {
            backgroundTimer.cancel();
        }
        backgroundTimer = null;
    }

    public void startStream(String streamUrl) {
        startStream(streamUrl, false);
    }

    /**
     * Start the livestream with given URL.
     *
     * @param streamUrl the stream URL
     * @param isSavedToDevice flag whether you want to save the recorded video
     */
    public void startStream(String streamUrl, boolean isSavedToDevice) {
        if (!isCameraValid()) {
            return;
        }
        LLog.d(TAG, "startStream streamUrl " + streamUrl + ", isSavedToDevice: " + isSavedToDevice);
        LUIUtil.visibleViews(progressBar);
        cameraHelper.startStream(streamUrl);
        this.isSavedToDevice = isSavedToDevice;
        if (isSavedToDevice) {
            startRecord();
        }
    }

    public boolean isSavedToDevice() {
        return isSavedToDevice;
    }

    /**
     * Stops the current livestream.
     */
    public void stopStream() {
        if (!isCameraValid()) {
            return;
        }
        if (isRecording()) {
            stopRecord();
        }
        if (isStreaming()) {
            cameraHelper.stopStream();
        }
    }

    /**
     * Call this method before use @startStream.
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder)
     */
    public boolean prepareStream(boolean isLandscape) {
        return prepareVideo(isLandscape) && prepareAudio();
    }

    /**
     * Call this method before use @startStream. If not you will do a stream without audio. audio
     * bitrate is dynamic sampleRate of audio in hz: 44100 isStereo true if you want Stereo audio (2
     * audio channels), false if you want Mono audio (1 audio channel). echoCanceler: check from
     * AcousticEchoCanceler.isAvailable() noiseSuppressor: check from NoiseSuppressor.isAvailable()
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */

    public boolean prepareAudio() {
        return prepareAudio(44100, true, AcousticEchoCanceler.isAvailable(),
                NoiseSuppressor.isAvailable());
    }

    /**
     * Call this method before use @startStream. If not you will do a stream without audio.
     *
     * @param sampleRate of audio in hz. Can be 8000, 16000, 22500, 32000, 44100.
     * @param isStereo true if you want Stereo audio (2 audio channels), false if you want Mono
     * audio (1 audio channel).
     * @param echoCanceler true enable echo canceler, false disable.
     * @param noiseSuppressor true enable noise suppressor, false  disable.
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    public boolean prepareAudio(int sampleRate, boolean isStereo, boolean echoCanceler,
            boolean noiseSuppressor) {
        return cameraHelper.prepareAudio(sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler,
            boolean noiseSuppressor) {
        return cameraHelper
                .prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    /**
     * Prepare the video encoder based on Video mode & device orientation.
     *
     * @param mode video mode (ex: FullHD, HD, SD)
     * @param isLandscape true if device orientation is landscape, otherwise false
     * @return true if success, otherwise false
     */
    public boolean prepareVideo(UzLiveVideoMode mode, boolean isLandscape) {
        this.isLandscape = isLandscape;
        this.liveVideoMode = mode;
        return cameraHelper.prepareVideo(getContext(), presetLiveFeed, mode, isLandscape);
    }

    /**
     * Prepare the video encoder based on device orientation.
     *
     * @param isLandscape true if device orientation is landscape, otherwise false
     * @return true if success, otherwise false
     */
    public boolean prepareVideo(boolean isLandscape) {
        this.isLandscape = isLandscape;
        this.liveVideoMode = UzLiveVideoMode.MODE_DEFAULT;
        return cameraHelper.prepareVideo(getContext(), presetLiveFeed, isLandscape);
    }

    /**
     * Call this method before use @startStream. If not you will do a stream without video. NOTE:
     * Rotation with encoder is silence ignored in some devices.
     *
     * @param width resolution in px.
     * @param height resolution in px.
     * @param fps frames per second of the stream.
     * @param bitrate H264 in kb.
     * @param hardwareRotation true if you want rotate using encoder, false if you want rotate with
     * software if you are using a SurfaceView or TextureView or with OpenGl if you are using
     * OpenGlView.
     * @param frameInterval sets 0 if you want send all frames
     * @param rotation could be 90, 180, 270 or 0. You should use CameraHelper.getCameraOrientation
     * with SurfaceView or TextureView and 0 with OpenGlView or LightOpenGlView. NOTE: Rotation with
     * encoder is silence ignored in some devices.
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a H264 encoder).
     */
    public boolean prepareVideo(int width, int height, int fps, int bitrate,
            boolean hardwareRotation, int frameInterval, int rotation) {
        if (presetLiveFeed == null) {
            LLog.e(TAG, "prepareVideo false because presetLiveFeed null");
            return false;
        }
        return cameraHelper
                .prepareVideo(width, height, fps, bitrate, hardwareRotation, frameInterval,
                        rotation);
    }

    private void startRecord() {
        if (!isCameraValid()) {
            return;
        }
        if (!isStreaming()) {
            LLog.e(TAG, "startRecord !isStreaming() -> return");
            return;
        }
        try {
            if (!folder.exists() && folder.mkdir()) {
                notifyError(UzLivestreamError.VIDEO_CAN_NOT_RECORD);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            currentDateAndTime = sdf.format(new Date());
            cameraHelper.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
            LLog.d(TAG, "Recording...");
        } catch (IOException e) {
            LLog.e(TAG, "Error startRecord " + e.toString());
            stopRecord();
            SentryUtils.captureException(e);
        }
    }

    private void stopRecord() {
        if (!isCameraValid()) {
            return;
        }
        LToast.show(getContext(),
                "File " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
        cameraHelper.stopRecord();
        currentDateAndTime = "";
    }

    /**
     * Adds text over camera preview.
     *
     * @param text the text content
     * @param textSize the text size
     * @param textColor the text color
     * @param translateTo the position of text
     */
    public void setTextToStream(String text, int textSize, int textColor, TranslateTo translateTo) {
        if (!isCameraValid()) {
            return;
        }
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        cameraHelper.setFilter(textObjectFilterRender);
        textObjectFilterRender.setText(text, textSize, textColor);
        textObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        textObjectFilterRender.setPosition(translateTo);
        spriteGestureController.setBaseObjectFilterRender(textObjectFilterRender); //Optional
    }

    /**
     * Adds image over camera preview.
     *
     * @param resId the image resource id
     * @param translateTo the position of image
     */
    public void setImageToStream(int resId, TranslateTo translateTo) {
        if (!isCameraValid()) {
            return;
        }
        ImageObjectFilterRender imageObjectFilterRender = new ImageObjectFilterRender();
        cameraHelper.setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(BitmapFactory.decodeResource(getResources(), resId));
        imageObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        imageObjectFilterRender.setPosition(translateTo);
        spriteGestureController.setBaseObjectFilterRender(imageObjectFilterRender); //Optional
    }

    /**
     * Adds gif image over camera preview.
     *
     * @param resId the image resource id
     * @param translateTo the position of image
     * @return true if successful added, otherwise false
     */
    public boolean setGifToStream(int resId, TranslateTo translateTo) {
        if (!isCameraValid()) {
            return false;
        }
        try {
            GifObjectFilterRender gifObjectFilterRender = new GifObjectFilterRender();
            gifObjectFilterRender.setGif(getResources().openRawResource(resId));
            cameraHelper.setFilter(gifObjectFilterRender);
            gifObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
            gifObjectFilterRender.setPosition(translateTo);
            spriteGestureController.setBaseObjectFilterRender(gifObjectFilterRender); //Optional
            return true;
        } catch (IOException e) {
            LLog.e(TAG, "Error setGifToStream " + e.toString());
            SentryUtils.captureException(e);
            return false;
        }
    }

    public String getMainStreamUrl() {
        return mainStreamUrl;
    }

    /**
     * Sets the live entity id to start livestream.
     *
     * @param entityLiveId the livestream entity id
     */
    public void setId(final String entityLiveId) {
        if (entityLiveId == null || entityLiveId.isEmpty()) {
            throw new NullPointerException(UZException.ERR_5);
        }
        startLivestream(entityLiveId);
    }

    // Just call, don't care about the result
    // Call the gets detail of entity to get stream Url
    private void startLivestream(final String entityLiveId) {
        LUIUtil.visibleViews(progressBar);
        UZService service = UZRestClient.createService(UZService.class);
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId(entityLiveId);

        String apiVersion = LiveConfig.getConfig().getApiVersion();
        UZAPIMaster.getInstance().subscribe(service.startALiveEvent(apiVersion, bodyStartALiveFeed),
                new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        getDetailEntity(entityLiveId, false);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, ">>>>>>startLivestream onFail " + e.getMessage());
                        getDetailEntity(entityLiveId, true);
                        SentryUtils.captureException(e);
                        if (!(e instanceof HttpException)) {
                            return;
                        }
                        ResponseBody responseBody = ((HttpException) e).response().errorBody();
                        try {
                            if (responseBody != null) {
                                LLog.e(TAG, "startLivestream error catch " + responseBody.string());
                            }
                        } catch (IOException e1) {
                            LLog.e(TAG, "startLivestream Exception catch " + e1.getMessage());
                        }
                    }
                });
    }

    private void getDetailEntity(String entityLiveId, final boolean isErrorStartLive) {
        String appId = LiveConfig.getConfig().getAppId();
        String apiVersion = LiveConfig.getConfig().getApiVersion();
        UZUtilBase.getDataFromEntityIdLive(getContext(), apiVersion, appId, entityLiveId,
                new CallbackGetDetailEntity() {
                    @Override
                    public void onSuccess(Data d) {
                        if (d == null || d.getLastPushInfo() == null || d.getLastPushInfo()
                                .isEmpty() || d.getLastPushInfo().get(0) == null) {
                            throw new NullPointerException("Data is null");
                        }
                        processLiveData(d, isErrorStartLive);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LUIUtil.goneViews(progressBar);
                        notifyError(UzLivestreamError.CAN_NOT_GET_INFO);
                    }
                });
    }

    private void processLiveData(Data data, boolean isErrorStartLive) {
        String streamKey = data.getLastPushInfo().get(0).getStreamKey();
        String streamUrl = data.getLastPushInfo().get(0).getStreamUrl();
        mainStreamUrl = streamUrl + "/" + streamKey;
        LLog.d(TAG, ">>>>mainStreamUrl: " + mainStreamUrl);

        //1 is Push with Transcode, !1 Push-only, no transcode
        boolean isTranscode = data.getEncode() == 1;
        LLog.d(TAG, "isTranscode " + isTranscode);

        boolean isConnectedFast = LConnectivityUtil.isConnectedFast(getContext());
        presetLiveFeed = new PresetLiveFeed();
        presetLiveFeed.setTranscode(isTranscode);
        presetLiveFeed.setVideoBitRates(isConnectedFast);

        LLog.d(TAG, "isErrorStartLive " + isErrorStartLive);
        LUIUtil.goneViews(progressBar);
        if (!isErrorStartLive && uzLivestreamCallback != null) {
            LLog.d(TAG, "onGetDataSuccess");
            uzLivestreamCallback.onGetDataSuccess(data, mainStreamUrl, isTranscode, presetLiveFeed);
            return;
        }
        String lastProcess = data.getLastProcess();
        if (lastProcess == null) {
            LLog.d(TAG, "Start live 400 but last process is null -> cannot livestream");
            notifyError(UzLivestreamError.LIVE_INFO_INCORRECT);
            return;
        }
        if (lastProcess.toLowerCase().equals(Constants.LAST_PROCESS_STOP)) {
            LLog.d(TAG, "Start live 400 but last process STOP -> cannot livestream");
            notifyError(UzLivestreamError.LIVE_INFO_INCORRECT);
            return;
        }

        LLog.d(TAG, "Start live 400 but last process START || INIT -> can livestream");
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onGetDataSuccess(data, mainStreamUrl, isTranscode, presetLiveFeed);
        }
    }

    public boolean isStreaming() {
        return cameraHelper.isStreaming();
    }

    /**
     * Switch the camera facing.
     */
    public void switchCamera() {
        cameraHelper.switchCamera();
        lastFacing = getRtmpCamera().isFrontCamera() ? CameraHelper.Facing.FRONT
                : CameraHelper.Facing.BACK;
    }

    public boolean isAntiAliasingEnabled() {
        return cameraHelper.isAntiAliasingEnabled();
    }

    public void enableAntiAliasing(boolean enable) {
        cameraHelper.enableAntiAliasing(enable);
    }

    public void setFilter(BaseFilterRender baseFilterRender) {
        cameraHelper.setFilter(baseFilterRender);
    }

    public int getStreamWidth() {
        return cameraHelper.getStreamWidth();
    }

    public int getStreamHeight() {
        return cameraHelper.getStreamHeight();
    }

    public boolean isRecording() {
        return cameraHelper.isRecording();
    }

    public int[] getBestSizePreview() {
        return cameraHelper.getBestSizePreview();
    }

    public void enableLantern() {
        cameraHelper.enableLantern();
    }

    public void disableLantern() {
        cameraHelper.disableLantern();
    }

    public void toggleLantern() {
        cameraHelper.toggleLantern();
    }

    public Boolean isLanternEnabled() {
        return cameraHelper.isLanternEnabled();
    }

    public void stopPreview() {
        cameraHelper.stopPreview();
    }

    private boolean isCameraValid() {
        return cameraHelper.isCameraValid();
    }

    private void hideLiveText() {
        if (!visibleLiveText || tvLiveStatus == null) {
            return;
        }
        LUIUtil.goneViews(tvLiveStatus);
        tvLiveStatus.clearAnimation();
    }

    private void showLiveText() {
        if (!visibleLiveText || tvLiveStatus == null) {
            return;
        }
        LUIUtil.visibleViews(tvLiveStatus);
        LAnimationUtil.blinking(tvLiveStatus);
    }

    private void notifyError(UzLivestreamError error) {
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onError(error);
        }
    }

    public interface StartPreview {

        void onSizeStartPreview(int width, int height);
    }
}

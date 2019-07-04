package uizalivestream.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import com.google.gson.Gson;
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

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.HttpException;
import uizalivestream.R;
import uizalivestream.data.UZLivestreamData;
import uizalivestream.interfaces.CameraCallback;
import uizalivestream.interfaces.UZLivestreamCallback;
import uizalivestream.model.PresetLiveStreamingFeed;
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
import vn.uiza.restapi.uiza.model.ErrorBody;
import vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.CallbackGetDetailEntity;
import vn.uiza.utils.UZUtilBase;
import vn.uiza.utils.util.AppUtils;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 9/1/2019.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class UZLivestream extends RelativeLayout
        implements ConnectCheckerRtmp, SurfaceHolder.Callback, View.OnTouchListener {

    private final String TAG = getClass().getSimpleName();
    private static final String TIME_FORMAT = "yyyyMMdd_HHmmss";

    private Gson gson = new Gson();
    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName());
    private OpenGlView openGlView;
    private SpriteGestureController spriteGestureController = new SpriteGestureController();
    private PresetLiveStreamingFeed presetLiveStreamingFeed;
    private ProgressBar progressBar;
    private TextView tvLiveStatus;
    private UZLivestreamCallback uzLivestreamCallback;
    private String mainStreamUrl;
    private boolean isShowDialogCheck;
    private RtmpCameraHelper cameraHelper;
    private boolean isSavedToDevice;

    public void setCameraCallback(CameraCallback cameraCallback) {
        if (cameraHelper != null) {
            cameraHelper.setCameraCallback(cameraCallback);
        }
    }

    public SpriteGestureController getSpriteGestureController() {
        return spriteGestureController;
    }

    public UZLivestream(Context context) {
        super(context);
    }

    public UZLivestream(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UZLivestream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UZLivestream(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public void hideTvLiveStatus() {
        if (tvLiveStatus != null) {
            tvLiveStatus.setVisibility(View.GONE);
            tvLiveStatus = null;
        }
    }

    public RtmpCamera1 getRtmpCamera() {
        return cameraHelper.getRtmpCamera();
    }

    public OpenGlView getOpenGlView() {
        return openGlView;
    }

    public PresetLiveStreamingFeed getPresetLiveStreamingFeed() {
        return presetLiveStreamingFeed;
    }

    private void onCreate() {
        inflate(getContext(), R.layout.layout_uz_livestream, this);
        tvLiveStatus = findViewById(R.id.tv_live_status);
        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, Color.WHITE);
        openGlView = findViewById(R.id.surfaceView);
        //Number of filters to use at same time.
        //You must modify it before create rtmp or rtsp object.
        //ManagerRender.numFilters = 2;
        RtmpCamera1 rtmpCamera1 = new RtmpCamera1(openGlView, this);
        cameraHelper = new RtmpCameraHelper(rtmpCamera1);
        openGlView.getHolder().addCallback(this);
        openGlView.setOnTouchListener(this);
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onUICreate();
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

    //Stop listener for image, text and gif stream objects.
    public void setBaseObjectFilterRender() {
        if (spriteGestureController != null) {
            spriteGestureController.setBaseObjectFilterRender(null);
        }
    }

    public void onResume() {
        if (!isShowDialogCheck) {
            checkPermission();
        }
    }

    public void destroyApiMaster() {
        UZAPIMaster.getInstance().destroy();
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
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
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
                                ((Activity) getContext()).startActivityForResult(intent, 101);
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

    private void updateUISurfaceView(int width, int height) {
        if (openGlView == null) {
            return;
        }
        int screenWidth = LScreenUtil.getScreenWidth();
        openGlView.getLayoutParams().width = screenWidth;
        openGlView.getLayoutParams().height = width * screenWidth / height;
        openGlView.requestLayout();
    }

    public void setUzLivestreamCallback(UZLivestreamCallback uzLivestreamCallback) {
        this.uzLivestreamCallback = uzLivestreamCallback;
    }

    @Override
    public void onConnectionSuccessRtmp() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvLiveStatus != null) {
                    tvLiveStatus.setVisibility(View.VISIBLE);
                    LAnimationUtil.blinking(tvLiveStatus);
                }
                LDialogUtil.hide(progressBar);
            }
        });
        if (uzLivestreamCallback != null) {
            uzLivestreamCallback.onConnectionSuccessRtmp();
        }
        switchCamera();
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvLiveStatus != null) {
                    tvLiveStatus.setVisibility(View.GONE);
                    tvLiveStatus.clearAnimation();
                }
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
                if (tvLiveStatus != null) {
                    tvLiveStatus.setVisibility(View.GONE);
                    tvLiveStatus.clearAnimation();
                }
                //rtmpCamera1.stopStream();
                LDialogUtil.hide(progressBar);
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
            LDialogUtil.hide(progressBar);
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
                    cameraHelper.startPreview(CameraHelper.Facing.FRONT, width, height);
                    updateUISurfaceView(width, height);
                }
            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (!isCameraValid()) return;
        stopStream();
        stopPreview();
    }

    public void startStream(String streamUrl) {
        startStream(streamUrl, false);
    }

    public void startStream(String streamUrl, boolean isSavedToDevice) {
        if (!isCameraValid()) return;
        LDialogUtil.show(progressBar);
        cameraHelper.startStream(streamUrl);
        LLog.d(TAG, "startStream streamUrl " + streamUrl + ", isSavedToDevice: " + isSavedToDevice);
        this.isSavedToDevice = isSavedToDevice;
        if (isSavedToDevice) {
            startRecord();
        }
    }

    public boolean isSavedToDevice() {
        return isSavedToDevice;
    }

    public void stopStream() {
        if (!isCameraValid()) return;
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
     * @param isLandscape:
     * @return true if success, false if you get a error (Normally because the encoder selected
     * * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    public boolean prepareStream(boolean isLandscape) {
        boolean prepareVideo = prepareVideo(isLandscape);
        return prepareVideo && prepareAudio();
    }

    /**
     * Call this method before use @startStream. If not you will do a stream without audio.
     * audio bitrate is dynamic
     * sampleRate of audio in hz: 44100
     * isStereo true if you want Stereo audio (2 audio channels), false if you want Mono audio
     * (1 audio channel).
     * echoCanceler: check from AcousticEchoCanceler.isAvailable()
     * noiseSuppressor: check from NoiseSuppressor.isAvailable()
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */

    public boolean prepareAudio() {
        return prepareAudio(44100, true, AcousticEchoCanceler.isAvailable(), NoiseSuppressor.isAvailable());
    }

    /**
     * Call this method after use @prepareVideo.
     * <p>
     * dynamic bitrate AAC in kb.
     *
     * @param sampleRate      of audio in hz. Can be 8000, 16000, 22500, 32000, 44100.
     * @param isStereo        true if you want Stereo audio (2 audio channels), false if you want Mono audio
     *                        (1 audio channel).
     * @param echoCanceler    true enable echo canceler, false disable.
     * @param noiseSuppressor true enable noise suppressor, false  disable.
     * @return true if success, false if you get a error (Normally because the encoder selected
     * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    private boolean prepareAudio(int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        return cameraHelper.prepareAudio(sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    /**
      * Call this method before use @startStream. If not you will do a stream without audio.
      *
      * @param bitrate AAC in kb.
      * @param sampleRate of audio in hz. Can be 8000, 16000, 22500, 32000, 44100.
      * @param isStereo true if you want Stereo audio (2 audio channels), false if you want Mono audio
      * (1 audio channel).
      * @param echoCanceler true enable echo canceler, false disable.
      * @param noiseSuppressor true enable noise suppressor, false  disable.
      * @return true if success, false if you get a error (Normally because the encoder selected
      * doesn't support any configuration seated or your device hasn't a AAC encoder).
      */
    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        return cameraHelper.prepareAudio(bitrate, sampleRate, isStereo, echoCanceler, noiseSuppressor);
    }

    public boolean prepareVideoFullHDPortrait() {
        return prepareVideoFullHD(false);
    }

    public boolean prepareVideoFullHDLandscape() {
        return prepareVideoFullHD(true);
    }

    private boolean prepareVideoFullHD(boolean isLandscape) {
        return cameraHelper.prepareVideoFullHD(getContext(), presetLiveStreamingFeed, isLandscape);
    }

    public boolean prepareVideoHDPortrait() {
        return prepareVideoHD(false);
    }

    public boolean prepareVideoHDLandscape() {
        return prepareVideoHD(true);
    }

    private boolean prepareVideoHD(boolean isLandscape) {
        return cameraHelper.prepareVideoHD(getContext(), presetLiveStreamingFeed, isLandscape);
    }

    public boolean prepareVideoSDPortrait() {
        return prepareVideoSD(false);
    }

    public boolean prepareVideoSDLandscape() {
        return prepareVideoSD(true);
    }

    private boolean prepareVideoSD(boolean isLandscape) {
        return cameraHelper.prepareVideoSD(getContext(), presetLiveStreamingFeed, isLandscape);
    }

    public boolean prepareVideoPortrait() {
        return prepareVideo(false);
    }

    public boolean prepareVideoLandscape() {
        return prepareVideo(true);
    }

    private boolean prepareVideo(boolean isLandscape) {
        return cameraHelper.prepareVideo(getContext(), presetLiveStreamingFeed, isLandscape);
    }

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int iFrameInterval, int rotation) {
        if (presetLiveStreamingFeed == null) {
            Log.e(TAG, "prepareVideoFullHD false with presetLiveStreamingFeed null");
            return false;
        }
        return cameraHelper.prepareVideo(width, height, fps, bitrate, hardwareRotation, iFrameInterval, rotation);
    }

    private void startRecord() {
        if (!isCameraValid()) return;
        if (!isStreaming()) {
            LLog.e(TAG, "startRecord !isStreaming() -> return");
            return;
        }
        try {
            if (!folder.exists()) {
                folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            currentDateAndTime = sdf.format(new Date());
            cameraHelper.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
            LLog.d(TAG, "Recording...");
        } catch (IOException e) {
            stopRecord();
            LLog.e(TAG, "Error startRecord " + e.toString());
            SentryUtils.captureException(e);
        }
    }

    private void stopRecord() {
        if (!isCameraValid()) return;
        cameraHelper.stopRecord();
        LToast.show(getContext(), "File " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
        currentDateAndTime = "";
    }

    public void setTextToStream(String text, int textSize, int textCorlor, TranslateTo translateTo) {
        if (!isCameraValid()) return;
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        cameraHelper.setFilter(textObjectFilterRender);
        textObjectFilterRender.setText(text, textSize, textCorlor);
        textObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        textObjectFilterRender.setPosition(translateTo);
        spriteGestureController.setBaseObjectFilterRender(textObjectFilterRender); //Optional
    }

    public void setImageToStream(int res, TranslateTo translateTo) {
        if (!isCameraValid()) return;
        ImageObjectFilterRender imageObjectFilterRender = new ImageObjectFilterRender();
        cameraHelper.setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(BitmapFactory.decodeResource(getResources(), res));
        imageObjectFilterRender.setDefaultScale(getStreamWidth(), getStreamHeight());
        imageObjectFilterRender.setPosition(translateTo);
        spriteGestureController.setBaseObjectFilterRender(imageObjectFilterRender); //Optional
    }

    public boolean setGifToStream(int res, TranslateTo translateTo) {
        if (!isCameraValid()) return false;
        try {
            GifObjectFilterRender gifObjectFilterRender = new GifObjectFilterRender();
            gifObjectFilterRender.setGif(getResources().openRawResource(res));
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

    public void setId(final String entityLiveId) {
        if (entityLiveId == null || entityLiveId.isEmpty()) {
            throw new NullPointerException(UZException.ERR_5);
        }
        startLivestream(entityLiveId);
    }

    // Chi can goi start live thoi, khong can quan tam den ket qua cua api nay start success hay ko
    // Van tiep tuc goi detail entity de lay streamUrl
    private void startLivestream(final String entityLiveId) {
        LDialogUtil.show(progressBar);
        UZService service = UZRestClient.createService(UZService.class);
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId(entityLiveId);
        UZAPIMaster.getInstance().subscribe(service.startALiveEvent(UZLivestreamData.getInstance().getAPIVersion(), bodyStartALiveFeed), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                getDetailEntity(entityLiveId, false, null);
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, ">>>>>>startLivestream onFail " + e.toString() + ", " + e.getMessage());
                try {
                    HttpException error = (HttpException) e;
                    String responseBody;
                    try {
                        responseBody = error.response().errorBody().string();
                        Log.e(TAG, "responseBody " + responseBody);
                        ErrorBody errorBody = gson.fromJson(responseBody, ErrorBody.class);
                        getDetailEntity(entityLiveId, true, errorBody.getMessage());
                    } catch (IOException e1) {
                        Log.e(TAG, "startLivestream IOException catch " + e1.toString());
                        getDetailEntity(entityLiveId, true, e1.getMessage());
                        SentryUtils.captureException(e1);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "startLivestream Exception catch " + ex.toString());
                    getDetailEntity(entityLiveId, true, ex.getMessage());
                    SentryUtils.captureException(ex);
                }
            }
        });
    }

    private void getDetailEntity(String entityLiveId, final boolean isErrorStartLive, final String errorMsg) {
        String appId = UZLivestreamData.getInstance().getAppId();
        UZUtilBase.getDataFromEntityIdLIVE(getContext(), UZLivestreamData.getInstance().getAPIVersion(), appId, entityLiveId, new CallbackGetDetailEntity() {
            @Override
            public void onSuccess(Data d) {
                if (d == null || d.getLastPushInfo() == null || d.getLastPushInfo().isEmpty() || d.getLastPushInfo().get(0) == null) {
                    throw new NullPointerException("Data is null");
                }
                String streamKey = d.getLastPushInfo().get(0).getStreamKey();
                String streamUrl = d.getLastPushInfo().get(0).getStreamUrl();
                mainStreamUrl = streamUrl + "/" + streamKey;
                LLog.d(TAG, ">>>>mainStreamUrl: " + mainStreamUrl);

                boolean isTranscode = d.getEncode() == 1;//1 is Push with Transcode, !1 Push-only, no transcode
                LLog.d(TAG, "isTranscode " + isTranscode);
                boolean isConnectedFast = LConnectivityUtil.isConnectedFast(getContext());
                presetLiveStreamingFeed = new PresetLiveStreamingFeed();
                presetLiveStreamingFeed.setTranscode(isTranscode);
                presetLiveStreamingFeed.setVideoBitRates(isConnectedFast);
                
                LLog.d(TAG, "isErrorStartLive " + isErrorStartLive);
                if (isErrorStartLive) {
                    if (d.getLastProcess() == null) {
                        if (uzLivestreamCallback != null) {
                            uzLivestreamCallback.onError("Error: Last process null");
                        }
                    } else {
                        if ((d.getLastProcess().toLowerCase().equals(Constants.LAST_PROCESS_STOP))) {
                            LLog.d(TAG, "Start live 400 but last process STOP -> cannot livestream");
                            if (uzLivestreamCallback != null) {
                                uzLivestreamCallback.onError(errorMsg);
                            }
                        } else {
                            LLog.d(TAG, "Start live 400 but last process START || INIT -> can livestream");
                            if (uzLivestreamCallback != null) {
                                uzLivestreamCallback.onGetDataSuccess(d, mainStreamUrl, isTranscode, presetLiveStreamingFeed);
                            }
                        }
                    }
                } else {
                    if (uzLivestreamCallback != null) {
                        LLog.d(TAG, "onGetDataSuccess");
                        uzLivestreamCallback.onGetDataSuccess(d, mainStreamUrl, isTranscode, presetLiveStreamingFeed);
                    }
                }
                LUIUtil.hideProgressBar(progressBar);
            }

            @Override
            public void onError(Throwable e) {
                LUIUtil.hideProgressBar(progressBar);
                if (uzLivestreamCallback != null) {
                    uzLivestreamCallback.onError(e.getMessage());
                }
            }
        });
    }

    public boolean isStreaming() {
        return cameraHelper.isStreaming();
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }

    public boolean isAAEnabled() {
        return cameraHelper.isAAEnabled();
    }

    public void enableAA(boolean isEnable) {
        cameraHelper.enableAA(isEnable);
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

    public interface StartPreview {
        void onSizeStartPreview(int width, int height);
    }
}

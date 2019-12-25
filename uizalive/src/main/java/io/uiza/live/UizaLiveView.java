package io.uiza.live;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pedro.encoder.input.gl.render.ManagerRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import io.uiza.live.enums.AspectRatio;
import io.uiza.live.enums.FilterRender;
import io.uiza.live.enums.ProfileVideoEncoder;
import io.uiza.live.interfaces.CCUListener;
import io.uiza.live.interfaces.CameraChangeListener;
import io.uiza.live.interfaces.ICameraHelper;
import io.uiza.live.interfaces.RecordListener;
import io.uiza.live.interfaces.UizaLiveListener;
import io.uiza.live.util.LiveUtil;
import timber.log.Timber;
import vn.uiza.core.utilities.LScreenUtil;

/**
 * @required: <uses-permission android:name="android.permission.CAMERA"/> and
 * <uses-permission android:name="android.permission.RECORD_AUDIO"/>
 */
public class UizaLiveView extends RelativeLayout {

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private String mainStreamUrl;
    private ICameraHelper cameraHelper;
    /**
     * ProfileEncoder default 360p
     */
    private ProfileVideoEncoder profile;
    /**
     * FPS default 24
     */
    private int fps;

    /**
     * Keyframe default 2
     */
    private int frameInterval;
    /**
     * Audio Stereo: default true
     */
    private boolean audioStereo;
    /**
     * Audio Bitrate: default 64 Kbps
     */
    private int audioBitrate;
    /**
     * Audio SampleRate: default 32 KHz
     */
    private int audioSampleRate;

    private ProgressBar progressBar;
    private TextView tvLiveStatus;

    OpenGlView openGlView;
    private boolean useCamera2;

    private UizaLiveListener liveListener;

    private CCUListener ccuListener;
    private long backgroundAllowedDuration = 2 * MINUTE; // default is 2 minutes
    private CountDownTimer backgroundTimer;
    private boolean isBroadcastingBeforeGoingBackground;
    private boolean isFromBackgroundTooLong;
    private boolean isLandscape = false;
    private boolean AAEnabled = false;
    private boolean keepAspectRatio = false;
    private boolean isFlipHorizontal = false, isFlipVertical = false;

    AspectRatio aspectRatio = AspectRatio.RATIO_16_9;

    public UizaLiveView(Context context) {
        this(context, null);
    }

    public UizaLiveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UizaLiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        initView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UizaLiveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleAttr);
    }

    /**
     * Call twice time
     * Node: Don't call inflate in this method
     */
    private void initView(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.UizaLiveView, defStyleAttr, 0);
            try {
                boolean hasLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
                useCamera2 = a.getBoolean(R.styleable.UizaLiveView_useCamera2, hasLollipop) && hasLollipop;
                int res = a.getInt(R.styleable.UizaLiveView_videoSize, 360);
                if (res == 1080) {
                    profile = ProfileVideoEncoder.P1080;
                } else if (res == 720) {
                    profile = ProfileVideoEncoder.P720;
                } else {
                    profile = ProfileVideoEncoder.P360;
                }
                fps = a.getInt(R.styleable.UizaLiveView_fps, 24);
                frameInterval = a.getInt(R.styleable.UizaLiveView_frame_interval, 2);
                audioStereo = a.getBoolean(R.styleable.UizaLiveView_audioStereo, true);
                audioBitrate = a.getInt(R.styleable.UizaLiveView_audioBitrate, 64) * 1024; //64 Kbps
                audioSampleRate = a.getInt(R.styleable.UizaLiveView_audioSampleRate, 32000); // 32 KHz
                // for openGL
                keepAspectRatio = a.getBoolean(R.styleable.UizaLiveView_keepAspectRatio, true);
                AAEnabled = a.getBoolean(R.styleable.UizaLiveView_AAEnabled, false);
                ManagerRender.numFilters = a.getInt(R.styleable.UizaLiveView_numFilters, 1);
                isFlipHorizontal = a.getBoolean(R.styleable.UizaLiveView_isFlipHorizontal, false);
                isFlipVertical = a.getBoolean(R.styleable.UizaLiveView_isFlipVertical, false);
            } finally {
                a.recycle();
            }
        } else {
            useCamera2 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
            profile = ProfileVideoEncoder.P360;
            fps = 24;
            frameInterval = 2;
            audioStereo = true;
            audioBitrate = 64 * 1024; //64 Kbps
            audioSampleRate = 32000; // 32 KHz
            // for OpenGL
            keepAspectRatio = true;
            AAEnabled = false;
            ManagerRender.numFilters = 1;
            isFlipHorizontal = false;
            isFlipVertical = false;
        }
    }


    /**
     * Call one time
     * Note: you must call inflate in this method
     */
    private void onCreateView() {
        inflate(getContext(), R.layout.layout_uiza_glview, this);
        openGlView = findViewById(R.id.camera_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2)
            cameraHelper = new Camera2Helper(new RtmpCamera2(openGlView, connectCheckerRtmp));
        else
            cameraHelper = new Camera1Helper(new RtmpCamera1(openGlView, connectCheckerRtmp));
        openGlView.setCameraFlip(isFlipHorizontal, isFlipVertical);
        openGlView.setKeepAspectRatio(keepAspectRatio);
        openGlView.enableAA(AAEnabled);
        openGlView.getHolder().addCallback(surfaceCallback);
        tvLiveStatus = findViewById(R.id.live_status);
        progressBar = findViewById(R.id.pb);
        progressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        cameraHelper.setConnectReTries(10);
    }

    /**
     * Set AspectRatio
     *
     * @param aspectRatio One of {@link AspectRatio#RATIO_19_9},
     *                    {@link AspectRatio#RATIO_18_9},
     *                    {@link AspectRatio#RATIO_16_9} or {@link AspectRatio#RATIO_4_3}
     */
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
        if (openGlView != null) {
            int screenWidth = LScreenUtil.getScreenWidth();
            openGlView.getLayoutParams().width = screenWidth;
            openGlView.getLayoutParams().height = (int) (screenWidth * aspectRatio.getAspectRatio());
            openGlView.requestLayout();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        checkLivePermission();
    }


    private void checkLivePermission() {
        Dexter.withActivity((Activity) getContext()).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    onCreateView();
                    if (liveListener != null) {
                        liveListener.onInit(true);
                    }
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    showSettingsDialog();
                } else {
                    showShouldAcceptPermission();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread()
                .check();
    }


    private void showShouldAcceptPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.need_permission);
        builder.setMessage(R.string.this_app_needs_permission);
        builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkLivePermission();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (liveListener != null) {
                    liveListener.onInit(false);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.need_permission);
        builder.setMessage(R.string.this_app_needs_permission_grant_it);
        builder.setPositiveButton(R.string.goto_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                ((Activity) getContext()).startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (liveListener != null) {
                    liveListener.onInit(false);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void setLiveListener(UizaLiveListener liveListener) {
        this.liveListener = liveListener;
    }

    int ccu;

    public void setCcuListener(CCUListener ccuListener) {
        this.ccuListener = ccuListener;
        handler.sendEmptyMessage(0);
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (ccuListener != null) {
                if (cameraHelper != null && cameraHelper.isStreaming()) {
                    ccu += (new Random()).nextInt(11);
                    ccuListener.onCcu(ccu);
                }
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };

    /**
     * Must be called when the app go to resume state
     */
    public void onResume() {
        checkAndResumeLiveStreamIfNeeded();
        if (isFromBackgroundTooLong) {
            if (liveListener != null) {
                liveListener.onBackgroundTooLong();
            }
            isFromBackgroundTooLong = false;
        }
    }

    /**
     * Set duration which allows livestream to keep the info
     *
     * @param duration the duration which allows livestream to keep the info
     */
    public void setBackgroundAllowedDuration(long duration) {
        this.backgroundAllowedDuration = duration;
    }

    private void checkAndResumeLiveStreamIfNeeded() {
        cancelBackgroundTimer();

        if (!isBroadcastingBeforeGoingBackground) return;

        isBroadcastingBeforeGoingBackground = false;
        // We delay a second because the surface need to be resumed before we can prepare something
        // Improve this method whenever you can
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    stopStream(); // make sure stop stream and start it again
                    if (prepareAudio() && prepareVideo(isLandscape)) {
                        startStream(mainStreamUrl);
                    }
                } catch (Exception ignored) {
                    Timber.e("Can not resume livestream right now !");
                }
            }
        }, SECOND);
    }

    private void startBackgroundTimer() {
        if (backgroundTimer == null) {
            backgroundTimer = new CountDownTimer(backgroundAllowedDuration, SECOND) {
                public void onTick(long millisUntilFinished) {
                    // Nothing
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

    /**
     * you must call in onInit()
     *
     * @param cameraChangeListener : camera witch listener
     */
    public void setCameraChangeListener(CameraChangeListener cameraChangeListener) {
        cameraHelper.setCameraChangeListener(cameraChangeListener);
    }

    /**
     * you must call in oInit()
     *
     * @param recordListener : record status listener
     */
    public void setRecordListener(RecordListener recordListener) {
        cameraHelper.setRecordListener(recordListener);
    }

    public void hideLiveStatus() {
        if (tvLiveStatus != null) {
            tvLiveStatus.setVisibility(View.GONE);
            tvLiveStatus.clearAnimation();
        }
    }

    /**
     * run on main Thread
     */
    public void showLiveStatus() {
        tvLiveStatus.setVisibility(View.VISIBLE);
        LiveUtil.blinking(tvLiveStatus);
    }

    public ProfileVideoEncoder getProfile() {
        return profile;
    }

    public void startPreview() {
        cameraHelper.startPreview(cameraHelper.isFrontCamera() ? CameraHelper.Facing.FRONT : CameraHelper.Facing.BACK);
    }


    /**
     * Please call {@link #prepareStream} before use
     *
     * @param liveEndpoint: Stream Url
     */
    public void startStream(String liveEndpoint) {
        mainStreamUrl = liveEndpoint;
        progressBar.setVisibility(View.VISIBLE);
        cameraHelper.startStream(liveEndpoint);
    }

    public boolean isStreaming() {
        return cameraHelper.isStreaming();
    }

    public void stopStream() {
        cameraHelper.stopStream();
    }

    public void switchCamera() {
        cameraHelper.switchCamera();
    }

    /**
     * @required: <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    public void startRecord(String savePath) throws IOException {
        cameraHelper.startRecord(savePath);
    }

    public boolean isRecording() {
        return cameraHelper.isRecording();
    }

    public void stopRecord() {
        cameraHelper.stopRecord();
    }

    /**
     * Call this method before use @startStream.
     *
     * @return true if success, false if you get a error (Normally because the encoder selected
     * * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    public boolean prepareStream() {
        return prepareAudio() && prepareVideo(isLandscape);
    }

    /**
     * Call this method before use {@link #startStream}.
     *
     * @param isLandscape:
     * @return true if success, false if you get a error (Normally because the encoder selected
     * * doesn't support any configuration seated or your device hasn't a AAC encoder).
     */
    public boolean prepareStream(boolean isLandscape) {
        return prepareAudio() && prepareVideo(isLandscape);
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
        return cameraHelper.prepareAudio(audioBitrate, audioSampleRate, audioStereo);
    }

    /**
     * default rotation
     *
     * @return true if success, false if otherwise
     */
    public boolean prepareVideo() {
        int rotation = CameraHelper.getCameraOrientation(getContext());
        isLandscape = rotation == 0 || rotation == 180;
        return cameraHelper.prepareVideo(profile, fps, frameInterval, rotation);
    }


    public boolean prepareVideo(boolean isLandscape) {
        this.isLandscape = isLandscape;
        return cameraHelper.prepareVideo(profile, fps, frameInterval, isLandscape ? 0 : 90);
    }

    public void enableAA(boolean enable) {
        cameraHelper.enableAA(enable);
    }

    public boolean isAAEnabled() {
        return cameraHelper.isAAEnabled();
    }

    public void setFilter(FilterRender filterRender) {
        cameraHelper.setFilter(filterRender.getFilterRender());
    }

    public void setFilter(int position, FilterRender filterRender) {
        cameraHelper.setFilter(position, filterRender.getFilterRender());
    }

    public int getStreamWidth() {
        return cameraHelper.getStreamWidth();
    }

    public int getStreamHeight() {
        return cameraHelper.getStreamHeight();
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (liveListener != null) {
                liveListener.surfaceCreated();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            int mHeight = Math.min((int) (width * aspectRatio.getAspectRatio()), height);
            cameraHelper.startPreview(CameraHelper.Facing.BACK, width, mHeight);
            if (liveListener != null) {
                liveListener.surfaceChanged(format, width, mHeight);
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (cameraHelper.isRecording()) {
                cameraHelper.stopRecord();
            }
            if (cameraHelper.isStreaming()) {
                cameraHelper.stopStream();
            }
            cameraHelper.stopPreview();
            startBackgroundTimer();
            if (liveListener != null) {
                liveListener.surfaceDestroyed();
            }
        }
    };

    private ConnectCheckerRtmp connectCheckerRtmp = new ConnectCheckerRtmp() {
        @Override
        public void onConnectionSuccessRtmp() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLiveStatus();
                    progressBar.setVisibility(View.GONE);
                    invalidate();
                    requestLayout();
                    if (liveListener != null) {
                        liveListener.onConnectionSuccess();
                    }
                }
            });
            isBroadcastingBeforeGoingBackground = true;
        }

        @Override
        public void onConnectionFailedRtmp(final String reason) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Wait 5s and retry connect stream
                    if (cameraHelper.reTry(5000, reason)) {
                        if (liveListener != null) {
                            liveListener.onRetryConnection(5000);
                        }
                    } else {
                        cameraHelper.stopStream();
                        progressBar.setVisibility(View.GONE);
                        hideLiveStatus();
                        invalidate();
                        requestLayout();
                        if (liveListener != null) {
                            liveListener.onConnectionFailed(reason);
                        }
                    }
                }
            });

        }

        @Override
        public void onNewBitrateRtmp(long bitrate) {
            if (liveListener != null) liveListener.onNewBitrate(bitrate);
        }

        @Override
        public void onDisconnectRtmp() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLiveStatus();
                    progressBar.setVisibility(View.GONE);
                    invalidate();
                    requestLayout();
                    if (liveListener != null) {
                        liveListener.onDisconnect();
                    }
                }
            });

        }

        @Override
        public void onAuthErrorRtmp() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    invalidate();
                    requestLayout();
                    if (liveListener != null)
                        liveListener.onAuthError();
                }
            });
        }

        @Override
        public void onAuthSuccessRtmp() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    invalidate();
                    requestLayout();
                    if (liveListener != null)
                        liveListener.onAuthSuccess();
                }
            });
        }
    };

    // SETTER

    /**
     * Each video encoder configuration corresponds to a set of video parameters, including the resolution, frame rate, bitrate, and video orientation.
     * The parameters specified in this method are the maximum values under ideal network conditions.
     * If the video engine cannot render the video using the specified parameters due to poor network conditions,
     * the parameters further down the list are considered until a successful configuration is found.
     * <p>
     * If you do not set the video encoder configuration after joining the channel,
     * you can call this method before calling the enableVideo method to reduce the render time of the first video frame.
     *
     * @param profile The local video encoder configuration
     */
    public void setProfile(ProfileVideoEncoder profile) {
        this.profile = profile;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
    }

    public void setAudioStereo(boolean audioStereo) {
        this.audioStereo = audioStereo;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate * 1024;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public void setVideoBitrateOnFly(int bitrate) {
        cameraHelper.setVideoBitrateOnFly(bitrate);
    }

    public void enableAudio() {
        cameraHelper.enableAudio();
    }

    public void disableAudio() {
        cameraHelper.disableAudio();
    }

    public boolean isAudioMuted() {
        return cameraHelper.isAudioMuted();
    }

    public boolean isVideoEnabled() {
        return cameraHelper.isVideoEnabled();
    }

    /**
     * Check support Flashlight
     * if use Camera1 always return false
     *
     * @return true if support, false if not support.
     */
    public boolean isLanternSupported() {
        return cameraHelper.isLanternSupported();
    }

    /**
     * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
     */
    public void enableLantern() throws Exception {
        cameraHelper.enableLantern();
    }

    /**
     * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
     */
    public void disableLantern() {
        cameraHelper.disableLantern();
    }

    public boolean isLanternEnabled() {
        return cameraHelper.isLanternEnabled();
    }
}

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
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.util.BitrateAdapter;
import com.pedro.rtplibrary.view.AutoFitTextureView;
import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.IOException;
import java.util.List;

import io.uiza.live.enums.FilterRender;
import io.uiza.live.enums.FrameRate;
import io.uiza.live.enums.OrientationMode;
import io.uiza.live.enums.ProfileVideoEncoder;
import io.uiza.live.interfaces.CameraChangeListener;
import io.uiza.live.interfaces.ICameraHelper;
import io.uiza.live.interfaces.RecordListener;
import io.uiza.live.interfaces.UizaLiveListener;
import io.uiza.live.util.LiveUtil;
import timber.log.Timber;

/**
 * @required: <uses-permission android:name="android.permission.CAMERA"/> and
 * <uses-permission android:name="android.permission.RECORD_AUDIO"/>
 */
public class UizaLiveView extends RelativeLayout {

    private static final String TAG = "UizaLiveView";

    private static final int SURFACE = 0;
    private static final int TEXTURE = 1;
    private static final int OPENGL = 2;
    private static final int LIGH_OPENGL = 3;

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
    private int keyframe;
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

    private int viewType = OPENGL;
    private OrientationMode orientationMode;
    private ProgressBar progressBar;
    private TextView tvLiveStatus;

    private boolean useCamera2;
    private boolean adaptiveBitrate;

    private UizaLiveListener liveListener;

    private BitrateAdapter bitrateAdapter;

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
            boolean hasLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
            viewType = a.getInt(R.styleable.UizaLiveView_viewType, OPENGL);
            useCamera2 = a.getBoolean(R.styleable.UizaLiveView_useCamera2, hasLollipop) && hasLollipop;
            int res = a.getInt(R.styleable.UizaLiveView_videoSize, 360);
            if (res == 1080) {
                profile = ProfileVideoEncoder.P1080;
            } else if (res == 720) {
                profile = ProfileVideoEncoder.P720;
            } else {
                profile = ProfileVideoEncoder.P360;
            }
            int orimode = a.getInt(R.styleable.UizaLiveView_orientationMode, 0);
            orientationMode = OrientationMode.fromValue(orimode);
            fps = a.getInt(R.styleable.UizaLiveView_fps, 24);
            keyframe = a.getInt(R.styleable.UizaLiveView_keyframe, 2);
            adaptiveBitrate = a.getBoolean(R.styleable.UizaLiveView_adaptiveBitrate, false);
            audioStereo = a.getBoolean(R.styleable.UizaLiveView_audioStereo, true);
            audioBitrate = a.getInt(R.styleable.UizaLiveView_audioBitrate, 64) * 1024; //64 Kbps
            audioSampleRate = a.getInt(R.styleable.UizaLiveView_audioSampleRate, 32000); // 32 KHz
        } else {
            boolean hasLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
            viewType = OPENGL;
            useCamera2 = hasLollipop;
            profile = ProfileVideoEncoder.P360;
            orientationMode = OrientationMode.ADAPTIVE;
            fps = 24;
            keyframe = 2;
            adaptiveBitrate = false;
            audioStereo = true;
            audioBitrate = 64 * 1024; //64 Kbps
            audioSampleRate = 32000; // 32 KHz
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

    /**
     * Call one time
     * Note: you must call inflate in this method
     */
    private void onCreateView() {
        Timber.d("viewType: %d", viewType);
        switch (viewType) {
            case SURFACE:
                inflate(getContext(), R.layout.layout_uiza_surfaceview, this);
                SurfaceView surfaceView = findViewById(R.id.camera_view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2)
                    cameraHelper = new Camera2Helper(new RtmpCamera2(surfaceView, connectCheckerRtmp));
                else
                    cameraHelper = new Camera1Helper(new RtmpCamera1(surfaceView, connectCheckerRtmp));
                surfaceView.getHolder().addCallback(surfaceCallback);
                break;
            case TEXTURE:
                inflate(getContext(), R.layout.layout_uiza_textureview, this);
                AutoFitTextureView textureView = findViewById(R.id.camera_view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2) {
                    cameraHelper = new Camera2Helper(new RtmpCamera2(textureView, connectCheckerRtmp));
                } else
                    cameraHelper = new Camera1Helper(new RtmpCamera1(textureView, connectCheckerRtmp));
                textureView.setSurfaceTextureListener(surfaceTextureListener);
                break;
            case LIGH_OPENGL:
                inflate(getContext(), R.layout.layout_uiza_light_glview, this);
                LightOpenGlView lightOpenGlView = findViewById(R.id.camera_view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2)
                    cameraHelper = new Camera2Helper(new RtmpCamera2(lightOpenGlView, connectCheckerRtmp));
                else
                    cameraHelper = new Camera1Helper(new RtmpCamera1(lightOpenGlView, connectCheckerRtmp));
                lightOpenGlView.getHolder().addCallback(surfaceCallback);
                break;
            default:
                inflate(getContext(), R.layout.layout_uiza_glview, this);
                OpenGlView openGlView = findViewById(R.id.camera_view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2)
                    cameraHelper = new Camera2Helper(new RtmpCamera2(openGlView, connectCheckerRtmp));
                else
                    cameraHelper = new Camera1Helper(new RtmpCamera1(openGlView, connectCheckerRtmp));
                openGlView.getHolder().addCallback(surfaceCallback);
                break;
        }
        tvLiveStatus = findViewById(R.id.live_status);
        progressBar = findViewById(R.id.pb);
        progressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        cameraHelper.setConnectReTries(10);
        VideoEncoderConfig config = new VideoEncoderConfig(
                new VideoDimension(profile.getWidth(), profile.getHeight()),
                FrameRate.FPS_24, profile.getBitrate(), orientationMode);
        cameraHelper.setVideoEncoderConfig(config);
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

    /**
     * you must call in onInit()
     *
     * @param cameraChangeListener
     */
    public void setCameraChangeListener(CameraChangeListener cameraChangeListener) {
        cameraHelper.setCameraChangeListener(cameraChangeListener);
    }

    /**
     * you must call in oInit()
     *
     * @param recordListener
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

    public void startStream(String liveEndpoint) {
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

    public boolean prepareStream() {
        return prepareAudio() && prepareVideo();
    }

    public boolean prepareAudio() {
        return cameraHelper.prepareAudio(audioBitrate, audioSampleRate, audioStereo);
    }

    public boolean prepareVideo() {
        return cameraHelper.prepareVideo(profile, fps, keyframe, CameraHelper.getCameraOrientation(getContext()));
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

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            ((AutoFitTextureView) findViewById(R.id.camera_view)).setAspectRatio(480, 640);
            if (liveListener != null) {
                liveListener.surfaceCreated();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            cameraHelper.startPreview(CameraHelper.Facing.FRONT);
            if (liveListener != null) {
                liveListener.surfaceChanged(0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if (cameraHelper.isRecording()) {
                cameraHelper.stopRecord();
            }
            if (cameraHelper.isStreaming()) {
                cameraHelper.stopStream();
            }
            cameraHelper.stopPreview();
            if (liveListener != null) {
                liveListener.surfaceDestroyed();
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (liveListener != null) {
                liveListener.surfaceCreated();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && useCamera2) {
                cameraHelper.startPreview(CameraHelper.Facing.FRONT);
//            } else {
//                cameraHelper.startPreview(CameraHelper.Facing.FRONT);
//            }
            if (liveListener != null) {
                liveListener.surfaceChanged(format, width, height);
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
            if (liveListener != null) {
                liveListener.surfaceDestroyed();
            }
        }
    };

    private ConnectCheckerRtmp connectCheckerRtmp = new ConnectCheckerRtmp() {
        @Override
        public void onConnectionSuccessRtmp() {
            if (adaptiveBitrate) {
                bitrateAdapter = new BitrateAdapter(new BitrateAdapter.Listener() {

                    @Override
                    public void onBitrateAdapted(int bitrate) {
                        cameraHelper.setVideoBitrateOnFly(bitrate);
                    }
                });
                bitrateAdapter.setMaxBitrate(cameraHelper.getBitrate());
            }
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
            if (bitrateAdapter != null) bitrateAdapter.adaptBitrate(bitrate);
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

    public void setKeyframe(int keyframe) {
        this.keyframe = keyframe;
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

    public boolean supportFilter() {
        if (cameraHelper != null)
            return cameraHelper.supportGlInterface();
        return (viewType == 2 || viewType == 3);
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

package vn.loitp.uizavideo.view.floatview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.uizavideo.listerner.ProgressCallback;

/**
 * Created by LENOVO on 3/27/2018.
 */

public class FloatingUizaVideoService extends Service implements FloatUizaIMAVideo.Callback {
    private final String TAG = getClass().getSimpleName();
    private final int CLICK_ACTION_THRESHHOLD = 200;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private RelativeLayout rlControl;
    private RelativeLayout moveView;
    private ImageButton btExit;
    private ImageButton btFullScreen;
    private FloatUizaIMAVideo floatUizaIMAVideo;
    private WindowManager.LayoutParams params;

    private String linkPlay;
    private long currentPositionPlayer;
    private String entityId;
    private String entityCover;
    private String entityTitle;

    public FloatingUizaVideoService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LLog.d(TAG, "onStartCommand");
        if (intent != null && intent.getExtras() != null) {
            linkPlay = intent.getStringExtra(Constants.FLOAT_LINK_PLAY);
            currentPositionPlayer = intent.getLongExtra(Constants.FLOAT_CURRENT_POSITION, 0);
            entityId = intent.getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
            entityCover = intent.getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
            entityTitle = intent.getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
            LLog.d(TAG, "linkPlay " + linkPlay);
            LLog.d(TAG, "currentPositionPlayer " + currentPositionPlayer);
            LLog.d(TAG, "entityId " + entityId);
            LLog.d(TAG, "entityCover " + entityCover);
            LLog.d(TAG, "entityTitle " + entityTitle);

            setupVideo();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void findViews() {
        rlControl = (RelativeLayout) mFloatingView.findViewById(R.id.rl_control);
        moveView = (RelativeLayout) mFloatingView.findViewById(R.id.move_view);
        btExit = (ImageButton) mFloatingView.findViewById(R.id.bt_exit);
        btFullScreen = (ImageButton) mFloatingView.findViewById(R.id.bt_full_screen);
    }

    @Override
    public void onCreate() {
        LLog.d(TAG, "onCreate");
        super.onCreate();

        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_uiza_video, null);
        findViews();

        //Add the view to the window.
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        setSizeMoveView();

        //Specify the view position
        //params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        //params.x = 0;
        //params.y = 0;

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = LScreenUtil.getScreenWidth() - getWidth();
        params.y = LScreenUtil.getScreenHeight() - getHeight();

        floatUizaIMAVideo = (FloatUizaIMAVideo) mFloatingView.findViewById(R.id.uiza_video);

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //Drag and move floating view using user's touch action.
        dragAndMove();

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                Intent intent = new Intent();
                LLog.d(TAG, "btFullScreen getPackageName: " + getPackageName());
                intent.putExtra(Constants.FLOAT_CURRENT_POSITION, floatUizaIMAVideo.getCurrentPosition());
                intent.putExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME, getPackageName());
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
                intent.setAction(Constants.FLOAT_CLICKED_FULLSCREEN);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }
        });
    }

    private void slideToPosition(int goToPosX, int goToPosY) {
        int currentPosX = params.x;
        int currentPosY = params.y;
        LLog.d(TAG, "slideToLeft current Point: " + currentPosX + " x " + currentPosY);

        final int a = (int) Math.abs(goToPosX - currentPosX);
        final int b = (int) Math.abs(goToPosY - currentPosY);
        LLog.d(TAG, "slideToLeft " + a + " : " + b);

        rlControl.setVisibility(View.GONE);
        setSizeMoveView();

        new CountDownTimer(500, 5) {
            public void onTick(long t) {
                float step = (500 - t) / 5;
                LLog.d(TAG, "slideToLeft onTick step: " + step);
                //LLog.d(TAG, "slideToLeft onTick: " + a * step / 100 + " - " + b * step / 100);
                updateUISlide((int) (a * step / 100), (int) (b * step / 100));
            }

            public void onFinish() {
                //LLog.d(TAG, "slideToLeft onFinish");
                updateUISlide(a, b);
            }
        }.start();
    }

    private void updateUISlide(int x, int y) {
        params.x = x;
        params.y = y;
        mWindowManager.updateViewLayout(mFloatingView, params);
    }

    private void dragAndMove() {
        moveView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long lastTouchDown;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastTouchDown = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //on Click event
                        clickRoot(lastTouchDown);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    private void clickRoot(long lastTouchDown) {
        if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
            rlControl.setVisibility(rlControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            setSizeMoveView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
        if (floatUizaIMAVideo != null) {
            floatUizaIMAVideo.onDestroy();
        }
    }

    private void setListener() {
        LLog.d(TAG, TAG + " addListener");
        if (floatUizaIMAVideo == null || floatUizaIMAVideo.getPlayer() == null) {
            return;
        }
        floatUizaIMAVideo.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                LLog.d(TAG, "onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                LLog.d(TAG, "onLoadingChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                LLog.d(TAG, "onPlayerStateChanged");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                LLog.d(TAG, "onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                LLog.d(TAG, "onShuffleModeEnabledChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LLog.d(TAG, "onPlayerError");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                LLog.d(TAG, "onPositionDiscontinuity");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                LLog.d(TAG, "onPlaybackParametersChanged");
            }

            @Override
            public void onSeekProcessed() {
                LLog.d(TAG, "onSeekProcessed");
            }
        });
        floatUizaIMAVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                LLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
                LLog.d(TAG, "onAudioInputFormatChanged");
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                LLog.d(TAG, "onAudioSinkUnderrun");
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioDisabled");
            }
        });
        floatUizaIMAVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        floatUizaIMAVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                LLog.d(TAG, "onVideoInputFormatChanged");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                LLog.d(TAG, "onDroppedFrames");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                LLog.d(TAG, "onAudioDisabled");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                LLog.d(TAG, "onRenderedFirstFrame");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoDisabled");
            }
        });
        floatUizaIMAVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                LLog.d(TAG, "onMetadata");
            }
        });
        floatUizaIMAVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                LLog.d(TAG, "onCues");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess) {
        LLog.d(TAG, "isPiPInitResult isInitSuccess: " + isInitSuccess);
        if (serviceCallbacks != null) {
            serviceCallbacks.isPiPInitResult(isInitSuccess);
        }
        setListener();
    }

    private void setupVideo() {
        floatUizaIMAVideo.init(linkPlay, currentPositionPlayer, this);
    }

    private void setSizeMoveView() {
        int widthScreen = LScreenUtil.getScreenWidth();
        if (rlControl.getVisibility() == View.VISIBLE) {
            //LLog.d(TAG, "setSizeMoveView if");
            //LLog.d(TAG, "setSizeMoveView: " + widthScreen + "x" + widthScreen);
            moveView.getLayoutParams().width = widthScreen * 70 / 100;
            moveView.getLayoutParams().height = widthScreen * 70 / 100 * 9 / 16;
        } else {
            //LLog.d(TAG, "setSizeMoveView else");
            //LLog.d(TAG, "setSizeMoveView: " + widthScreen + "x" + widthScreen);
            moveView.getLayoutParams().width = widthScreen * 50 / 100;
            moveView.getLayoutParams().height = widthScreen * 50 / 100 * 9 / 16;
        }
        moveView.requestLayout();
    }

    private int getWidth() {
        return moveView.getLayoutParams().width;
    }

    private int getHeight() {
        return moveView.getLayoutParams().height;
    }

    public interface ServiceCallbacks {
        public void isPiPInitResult(boolean isInitSuccess);
    }

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public FloatingUizaVideoService getService() {
            // Return this instance of MyService so clients can call public methods
            return FloatingUizaVideoService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }
}
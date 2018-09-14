package vn.uiza.uzv3.view.floatview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import loitp.core.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.view.ComunicateMng;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.uzv3.util.UZUtil;

/**
 * Created by loitp on 3/27/2018.
 */

public class FUZVideoService extends Service implements FUZVideo.Callback {
    private final String TAG = getClass().getSimpleName();
    private final int CLICK_ACTION_THRESHHOLD = 200;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private RelativeLayout rlControl;
    private RelativeLayout moveView;
    private ImageButton btExit;
    private ImageButton btFullScreen;
    private ImageButton btPlayPause;
    private TextView tvMsg;
    private FUZVideo FUZVideo;
    private WindowManager.LayoutParams params;

    //private Data data;
    private String linkPlay;
    private Gson gson = new Gson();

    private boolean isLivestream;
    private int widthScreen;

    public FUZVideoService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //LLog.d(TAG, "onStartCommand");
        //data = UZUtil.getData(this, gson);
        if (UZData.getInstance().getData() == null) {
            LLog.e(TAG, "onStartCommand data == null");
            return super.onStartCommand(intent, flags, startId);
        }
        if (intent != null && intent.getExtras() != null) {
            linkPlay = intent.getStringExtra(Constants.FLOAT_LINK_PLAY);
            isLivestream = intent.getBooleanExtra(Constants.FLOAT_IS_LIVESTREAM, false);
            setupVideo();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void findViews() {
        widthScreen = LScreenUtil.getScreenWidth();
        LLog.d(TAG, "widthScreen " + widthScreen);
        rlControl = (RelativeLayout) mFloatingView.findViewById(R.id.rl_control);
        moveView = (RelativeLayout) mFloatingView.findViewById(R.id.move_view);
        btExit = (ImageButton) mFloatingView.findViewById(R.id.bt_exit);
        btFullScreen = (ImageButton) mFloatingView.findViewById(R.id.bt_full_screen);
        btPlayPause = (ImageButton) mFloatingView.findViewById(R.id.bt_play_pause);
        tvMsg = (TextView) mFloatingView.findViewById(R.id.tv_msg);
        LUIUtil.setTextShadow(tvMsg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onEnd() {
                        setupVideo();
                    }

                    @Override
                    public void onRepeat() {
                    }

                    @Override
                    public void onStart() {
                    }
                });
            }
        });
    }

    @Override
    public void onCreate() {
        //LLog.d(TAG, "onCreate");
        super.onCreate();

        EventBus.getDefault().register(this);

        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.v3_layout_floating_uiza_video, null);
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

        FUZVideo = (FUZVideo) mFloatingView.findViewById(R.id.uiza_video);

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
                //set UI
                moveView.getLayoutParams().width = widthScreen;
                moveView.getLayoutParams().height = widthScreen * 9 / 16;
                moveView.requestLayout();
                updateUISlide(0, 0);
                rlControl.setVisibility(View.GONE);

                //stop video
                FUZVideo.getPlayer().setPlayWhenReady(false);
                LUIUtil.showProgressBar(FUZVideo.getProgressBar());
                moveView.setOnTouchListener(null);//disabled move view

                //bắn cho FloatClickFullScreenReceiverV3
                UZUtil.setClickedPip(getApplicationContext(), true);
                Intent intent = new Intent();
                intent.putExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME, getPackageName());
                intent.setAction(Constants.FLOAT_CLICKED_FULLSCREEN_V3);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }
        });
        btPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FUZVideo == null) {
                    return;
                }
                boolean isToggleResume = FUZVideo.togglePauseResume();
                if (isToggleResume) {
                    btPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
                } else {
                    btPlayPause.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
                }
            }
        });
    }

    /*private void slideToPosition(int goToPosX, int goToPosY) {
        int currentPosX = params.x;
        int currentPosY = params.y;
        LLog.d(TAG, "slideToPosition current Point: " + currentPosX + " x " + currentPosY);

//        final int a = (int) Math.abs(goToPosX - currentPosX);
//        final int b = (int) Math.abs(goToPosY - currentPosY);

//        final int a = goToPosX;
//        final int b = goToPosY;

        final int a = (int) Math.abs(goToPosX - currentPosX);
        final int b = (int) Math.abs(goToPosY - currentPosY);

        LLog.d(TAG, "slideToPosition " + a + " : " + b);

        //rlControl.setVisibility(View.GONE);
        //setSizeMoveView();

        new CountDownTimer(500, 5) {
            public void onTick(long t) {
                float step = (500 - t) / 5;
                LLog.d(TAG, "slideToLeft onTick step: " + step);
                //LLog.d(TAG, "slideToPosition onTick: " + a * step / 100 + " - " + b * step / 100);
                updateUISlide((int) (a * step / 100), (int) (b * step / 100));
            }

            public void onFinish() {
                LLog.d(TAG, "slideToLeft onFinish " + a + " x " + b);
                updateUISlide(a, b);
            }
        }.start();
    }*/

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
        EventBus.getDefault().unregister(this);
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
        if (FUZVideo != null) {
            FUZVideo.onDestroy();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long lastCurrentPosition;

    private void setListener() {
        //LLog.d(TAG, TAG + " addListener");
        if (FUZVideo == null || FUZVideo.getPlayer() == null) {
            return;
        }
        FUZVideo.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                //LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                //LLog.d(TAG, "onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                //LLog.d(TAG, "onLoadingChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                /*if (playbackState == Player.STATE_READY) {
                    LLog.d(TAG, "onPlayerStateChanged STATE_READY");
                }*/
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                //LLog.d(TAG, "onRepeatModeChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                //LLog.d(TAG, "onShuffleModeEnabledChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LLog.e(TAG, "onPlayerError " + error.getMessage());
                lastCurrentPosition = FUZVideo.getPlayer().getCurrentPosition();
                LLog.d(TAG, "onPlayerError lastCurrentPosition " + lastCurrentPosition);
                setupVideo();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                //LLog.d(TAG, "onPositionDiscontinuity");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                //LLog.d(TAG, "onPlaybackParametersChanged");
            }

            @Override
            public void onSeekProcessed() {
                //LLog.d(TAG, "onSeekProcessed");
            }
        });
        FUZVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
                //LLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                //LLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                //LLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
                //LLog.d(TAG, "onAudioInputFormatChanged");
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                //LLog.d(TAG, "onAudioSinkUnderrun");
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {
                //LLog.d(TAG, "onAudioDisabled");
            }
        });
        FUZVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        FUZVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                //LLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                //LLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                //LLog.d(TAG, "onVideoInputFormatChanged");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                //LLog.d(TAG, "onDroppedFrames");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                //LLog.d(TAG, "onVideoSizeChanged");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                //LLog.d(TAG, "onRenderedFirstFrame");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                //LLog.d(TAG, "onVideoDisabled");
            }
        });
        FUZVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                //LLog.d(TAG, "onMetadata");
            }
        });
        FUZVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                // LLog.d(TAG, "onCues");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess) {
        if (isInitSuccess && FUZVideo != null) {
            LLog.d(TAG, "isInitResult seekTo lastCurrentPosition: " + lastCurrentPosition);
            setListener();
            if (lastCurrentPosition > 0) {
                FUZVideo.getPlayer().seekTo(lastCurrentPosition);
            }
            if (!isSendMsgToActivity) {
                //LLog.d(TAG, "isPiPInitResult isSendMsgToActivity false");
                //bắn cho UizaIMAVideoV3 nhận
                ComunicateMng.MsgFromServiceIsInitSuccess msgFromServiceIsInitSuccess = new ComunicateMng.MsgFromServiceIsInitSuccess(null);
                msgFromServiceIsInitSuccess.setInitSuccess(isInitSuccess);
                ComunicateMng.postFromService(msgFromServiceIsInitSuccess);
                isSendMsgToActivity = true;
            }
        }
    }

    @Override
    public void onPlayerStateEnded() {
        //Cần check xem nếu play pip ở playlist folder thì auto next, còn nếu là entity thì thôi
        if (UZData.getInstance().isPlayWithPlaylistFolder()) {
            //TODO iplm this
            LLog.d(TAG, "Dang play o che do playlist folder -> auto switch next data");
        } else {
            LLog.d(TAG, "Dang play o che do entity -> do nothing");
        }
        stopSelf();
    }

    private boolean isSendMsgToActivity;

    private void setupVideo() {
        if (linkPlay == null || linkPlay.isEmpty()) {
            LLog.d(TAG, "setupVideo linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        LLog.d(TAG, "setupVideo linkPlay " + linkPlay + ", isLivestream: " + isLivestream);
        if (LConnectivityUtil.isConnected(this)) {
            FUZVideo.init(linkPlay, isLivestream, this);
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
        }
    }

    private void setSizeMoveView() {
        if (rlControl.getVisibility() == View.VISIBLE) {
            //LLog.d(TAG, "setSizeMoveView if");
            //LLog.d(TAG, "setSizeMoveView: " + widthScreen + "x" + widthScreen);

            /*moveView.getLayoutParams().width = widthScreen * 50 / 100;
            moveView.getLayoutParams().height = widthScreen * 50 / 100 * 9 / 16;*/

            moveView.getLayoutParams().width = widthScreen * 70 / 100;
            moveView.getLayoutParams().height = widthScreen * 70 / 100 * 9 / 16;

            /*moveView.getLayoutParams().width = widthScreen;
            moveView.getLayoutParams().height = widthScreen * 9 / 16;*/
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

    //listen msg from UizaIMAVideoV3
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromActivity msg) {
        if (msg == null) {
            return;
        }
        if (msg instanceof ComunicateMng.MsgFromActivityPosition) {
            //Nhận được vị trí từ UizaIMAVideoV3, tiến hành seek tới vị trí này
            //LLog.d(TAG, "MsgFromActivityPosition position " + ((ComunicateMng.MsgFromActivityPosition) msg).getPosition());
            if (FUZVideo != null) {
                FUZVideo.seekTo(((ComunicateMng.MsgFromActivityPosition) msg).getPosition());
            }
        } else if (msg instanceof ComunicateMng.MsgFromActivityIsInitSuccess) {
            //lắng nghe UizaIMAVideoV3 đã init success hay chưa
            //LLog.d(TAG, "MsgFromActivityIsInitSuccess isInitSuccess: " + ((ComunicateMng.MsgFromActivityIsInitSuccess) msg).isInitSuccess());
            if (FUZVideo != null) {
                //LLog.d(TAG, "getCurrentPosition: " + floatUizaIMAVideo.getCurrentPosition());
                //lấy vị trí của pip hiện tại để bắn cho UizaIMAVideoV3
                ComunicateMng.MsgFromServicePosition msgFromServicePosition = new ComunicateMng.MsgFromServicePosition(null);
                msgFromServicePosition.setPosition(FUZVideo.getCurrentPosition());
                ComunicateMng.postFromService(msgFromServicePosition);
                stopSelf();
            }
        }
    }
}
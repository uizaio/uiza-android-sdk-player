package vn.loitp.uizavideo.view.floatview;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.manager.FloatUizaPlayerManager;
import vn.loitp.uizavideo.view.util.UizaData;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class FloatUizaIMAVideo extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private Gson gson = new Gson();//TODO remove
    private PlayerView playerView;
    private FloatUizaPlayerManager floatUizaPlayerManager;
    private ProgressBar progressBar;

    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private String linkPlay;
    private long currentPosition;

    public long getCurrentPosition() {
        return getPlayer().getCurrentPosition();
    }

    public void init(String linkPlay, long currentPosition, Callback callback) {
        if (linkPlay == null || linkPlay.isEmpty()) {
            //LLog.e(TAG, "init failed: linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        LUIUtil.showProgressBar(progressBar);
        this.linkPlay = linkPlay;
        this.currentPosition = currentPosition;
        this.callback = callback;
        if (floatUizaPlayerManager != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            floatUizaPlayerManager.release();
        }
        checkToSetUp();

        //cannot delete delay below, only works after 500mls
        LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //track event eventype display
                trackUiza(UizaData.getInstance().createTrackingInput(getContext(), UizaData.EVENT_TYPE_DISPLAY));

                //track event plays_requested
                trackUiza(UizaData.getInstance().createTrackingInput(getContext(), UizaData.EVENT_TYPE_PLAYS_REQUESTED));
            }
        });
    }

    private void checkToSetUp() {
        //LLog.d(TAG, "checkToSetUp");
        initData(linkPlay, null, null, null);
        onResume();
    }

    public FloatUizaIMAVideo(Context context) {
        super(context);
        onCreate();
    }

    public FloatUizaIMAVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FloatUizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatUizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        //LLog.d(TAG, "onCreate");
        inflate(getContext(), R.layout.uiza_float_ima_video_core_rl, this);
        findViews();
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(getContext(), R.color.White));
        playerView = findViewById(R.id.player_view);
    }

    private ProgressCallback progressCallback;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void initData(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        //LLog.d(TAG, "initData");
        floatUizaPlayerManager = new FloatUizaPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        floatUizaPlayerManager.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                if (progressCallback != null) {
                    progressCallback.onAdProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                //LLog.d(TAG, TAG + " onVideoProgress video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                trackProgress(s, percent);
                if (progressCallback != null) {
                    progressCallback.onVideoProgress(currentMls, s, duration, percent);
                }
            }
        });
    }

    private int oldPercent = Constants.NOT_FOUND;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s)
        if (s == 5) {
            LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaData.getInstance().createTrackingInput(getContext(), UizaData.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaData.getInstance().createTrackingInput(getContext(), "25", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaData.getInstance().createTrackingInput(getContext(), "50", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaData.getInstance().createTrackingInput(getContext(), "75", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaData.getInstance().createTrackingInput(getContext(), "100", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true);
        }
        //track event video_starts
        trackUiza(UizaData.getInstance().createTrackingInput(getContext(), UizaData.EVENT_TYPE_VIDEO_STARTS));
    }

    public void onDestroy() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.release();
        }
    }

    public void onResume() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.init(currentPosition);
        }
    }

    public void onPause() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.reset();
        }
    }

    public SimpleExoPlayer getPlayer() {
        return floatUizaPlayerManager.getPlayer();
    }

    public interface Callback {
        public void isInitResult(boolean isInitSuccess);
    }

    private Callback callback;

    private void trackUiza(final UizaTracking uizaTracking) {
        //TODO
        LLog.d(TAG, "trackUiza getEntityName: " + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());

        /*UizaService service = RestClientTracking.createService(UizaService.class);
        ((BaseActivity) getContext()).subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                LLog.d(TAG, "trackUiza getEntityName: " + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
                ((BaseActivity) getContext()).showDialogError("Cannot track this entity");
            }
        });*/
    }
}
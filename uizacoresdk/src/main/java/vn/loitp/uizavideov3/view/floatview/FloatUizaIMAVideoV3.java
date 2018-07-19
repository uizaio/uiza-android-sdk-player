package vn.loitp.uizavideov3.view.floatview;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.ApiMaster;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideov3.view.manager.FloatUizaPlayerManagerV3;
import vn.loitp.uizavideov3.view.util.UizaDataV3;
import vn.loitp.views.LToast;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class FloatUizaIMAVideoV3 extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private PlayerView playerView;
    private FloatUizaPlayerManagerV3 floatUizaPlayerManager;
    private ProgressBar progressBar;
    private RelativeLayout rootView;
    private ImageView ivVideoCover;

    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private String linkPlay;
    private boolean isLivestream;

    public long getCurrentPosition() {
        return getPlayer().getCurrentPosition();
    }

    public void init(String linkPlay, boolean isLivestream, Callback callback) {
        if (linkPlay == null || linkPlay.isEmpty()) {
            //LLog.e(TAG, "init failed: linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        LUIUtil.showProgressBar(progressBar);
        this.linkPlay = linkPlay;
        this.isLivestream = isLivestream;

        LLog.d(TAG, "init linkPlay: " + linkPlay + ", isLivestream: " + isLivestream);

        this.callback = callback;
        if (floatUizaPlayerManager != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            floatUizaPlayerManager.release();
        }
        checkToSetUp();

        //duong nhu ko bao h track EVENT_TYPE_DISPLAY, EVENT_TYPE_PLAYS_REQUESTED, EVENT_TYPE_VIDEO_STARTS
        //cannot delete delay below, only works after 500mls
        /*LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //track event eventype display
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_DISPLAY));

                //track event plays_requested
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_PLAYS_REQUESTED));
            }
        });*/
    }

    private void checkToSetUp() {
        setVideoCover();
        initData(linkPlay, null, null, null);
        onResume();
    }

    public FloatUizaIMAVideoV3(Context context) {
        super(context);
        onCreate();
    }

    public FloatUizaIMAVideoV3(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FloatUizaIMAVideoV3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatUizaIMAVideoV3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.uiza_float_ima_video_core_rl, this);
        findViews();
    }

    private void findViews() {
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
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
        floatUizaPlayerManager = new FloatUizaPlayerManagerV3(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
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
    private boolean isTrackedEventView;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s), only track if isLivestream false
        if (s == 5 && !isTrackedEventView) {
            //LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_VIEW));
            isTrackedEventView = true;
        }

        //if current link play is livestream link play
        //we dont track progress play throught 25, 50, 75, 100
        if (isLivestream) {
            LLog.d(TAG, "isLivestream true -> we dont track progress play throught 25, 50, 75, 100");
            return;
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), "25", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), "50", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), "75", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(getContext(), "100", Constants.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true);
        }
        removeVideoCover();
    }

    public void onDestroy() {
        //LLog.d(TAG, "trackUiza onDestroy");
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.release();
        }
    }

    public void onResume() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.init();
        }
    }

    public void onPause() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.reset();
        }
    }

    public SimpleExoPlayer getPlayer() {
        if (floatUizaPlayerManager == null) {
            return null;
        }
        return floatUizaPlayerManager.getPlayer();
    }

    public interface Callback {
        public void isInitResult(boolean isInitSuccess);
    }

    private Callback callback;

    private void trackUiza(final UizaTracking uizaTracking) {
        //LLog.d(TAG, "------------->trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
        if (RestClientTracking.getRetrofit() == null) {
            String currentApiTrackingEndPoint = LPref.getApiTrackEndPoint(getContext());
            if (currentApiTrackingEndPoint == null || currentApiTrackingEndPoint.isEmpty()) {
                LLog.e(TAG, "trackUiza failed pip urrentApiTrackingEndPoint == null || currentApiTrackingEndPoint.isEmpty()");
                return;
            }
            RestClientTracking.init(currentApiTrackingEndPoint);
        }
        UizaServiceV2 service = RestClientTracking.createService(UizaServiceV2.class);
        ApiMaster.getInstance().subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                LLog.d(TAG, "<-------------trackUiza pip onSuccess getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Pip Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
                LLog.e(TAG, "Pip trackUiza onFail from service PiP:" + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
            }
        });
    }

    private void setVideoCover() {
        //LLog.d(TAG, "setVideoCover");
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
        }
        LImageUtil.load(getContext(), UizaDataV3.getInstance().getUizaInputV3().getData().getThumbnail() == null ? Constants.URL_IMG_THUMBNAIL :
                UizaDataV3.getInstance().getUizaInputV3().getData().getName(), ivVideoCover, R.drawable.uiza);
    }

    public void removeVideoCover() {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "removeVideoCover");
            ivVideoCover.setVisibility(GONE);
        }
    }

    public void seekTo(long position) {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.seekTo(position);
        }
    }

    //return true if toggleResume
    //return false if togglePause
    public boolean togglePauseResume() {
        if (floatUizaPlayerManager == null) {
            return false;
        }
        return floatUizaPlayerManager.togglePauseResume();
    }
}
package vn.uiza.uzv3.view.floatview;

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
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.ApiMaster;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.uiza.UizaServiceV2;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.uzv3.util.UZTrackingUtil;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

public class FUZVideo extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private PlayerView playerView;
    private FUZPlayerManager floatUizaPlayerManager;
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

    //Lấy vị trí của pip player
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

        //track event eventype display
        if (UZTrackingUtil.isTrackedEventTypeDisplay(getContext())) {
            //da track roi ko can track nua
        } else {
            trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_DISPLAY), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(getContext(), true);
                }
            });
        }

        //track event plays_requested
        if (UZTrackingUtil.isTrackedEventTypePlaysRequested(getContext())) {
            //da track roi ko can track nua
        } else {
            trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_PLAYS_REQUESTED), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(getContext(), true);
                }
            });
        }
    }

    private void checkToSetUp() {
        setVideoCover();
        initData(linkPlay, null, null, null);
        onResume();
    }

    public FUZVideo(Context context) {
        super(context);
        onCreate();
    }

    public FUZVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FUZVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FUZVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        //LLog.d(TAG, "initData linkPlay " + linkPlay);
        floatUizaPlayerManager = new FUZPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
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
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s), only track if isLivestream false
        if (s == (isLivestream ? 3 : 5)) {
            //LLog.d(TAG, "onVideoProgress -> track view");
            if (UZTrackingUtil.isTrackedEventTypeView(getContext())) {
                //da track roi ko can track nua
            } else {
                trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), Constants.EVENT_TYPE_VIEW), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypeView(getContext(), true);
                    }
                });
            }
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

        oldPercent = percent;

        //LLog.d(TAG, "trackProgress percent: " + percent);
        if (percent >= Constants.PLAYTHROUGH_100) {
            if (isTracked100) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again isTracked100 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought100(getContext())) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again");
                isTracked100 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), "100", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again isTracked75 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought75(getContext())) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again");
                isTracked75 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), "75", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again isTracked50 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought50(getContext())) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again");
                isTracked50 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), "50", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again isTracked25 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought25(getContext())) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again");
                isTracked25 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInputV3(getContext(), "25", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(getContext(), true);
                    }
                });
            }
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

        public void onPlayerStateEnded();
    }

    private Callback callback;

    private void trackUiza(final UizaTracking uizaTracking, final UZTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        //LLog.d(TAG, "------------->trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
        if (RestClientTracking.getRetrofit() == null) {
            String currentApiTrackingEndPoint = UZUtil.getApiTrackEndPoint(getContext());
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
                LLog.d(TAG, "<------------------------pip track success: " + uizaTracking.getEventType() + " : " + uizaTracking.getPlayThrough() + " : " + uizaTracking.getEntityName());
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Pip Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
                if (uizaTrackingCallback != null) {
                    uizaTrackingCallback.onTrackingSuccess();
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
        String urlImg = "";
        if (UZData.getInstance().getUZInput() == null || UZData.getInstance().getUZInput().getData() == null || UZData.getInstance().getUZInput().getData().getThumbnail() == null) {
            urlImg = Constants.URL_IMG_THUMBNAIL;
        } else {
            urlImg = UZData.getInstance().getUZInput().getData().getThumbnail();
        }
        LImageUtil.load(getContext(), urlImg, ivVideoCover, R.drawable.uiza);
    }

    protected void removeVideoCover() {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "removeVideoCover");
            ivVideoCover.setVisibility(GONE);
        }
    }

    protected void seekTo(long position) {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.seekTo(position);
        }
    }

    //return true if toggleResume
    //return false if togglePause
    protected boolean togglePauseResume() {
        if (floatUizaPlayerManager == null) {
            return false;
        }
        return floatUizaPlayerManager.togglePauseResume();
    }

    //Nếu pip đang play 1 playlist folder end thì mình phải switch sang position kế tiếp
    protected void onPlayerStateEnded() {
        LLog.d(TAG, "onPlayerStateEnded");
        if (callback != null) {
            callback.onPlayerStateEnded();
        }
    }
}
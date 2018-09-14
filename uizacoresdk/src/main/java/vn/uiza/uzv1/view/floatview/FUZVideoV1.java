package vn.uiza.uzv1.view.floatview;

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
import vn.uiza.uzv1.manager.FUZPlayerManagerV1;
import vn.uiza.uzv1.view.util.UizaDataV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class FUZVideoV1 extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    //private Gson gson = new Gson();
    private PlayerView playerView;
    private FUZPlayerManagerV1 FUZPlayerManagerV1;
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

    public long getCurrentPosition() {
        return getPlayer().getCurrentPosition();
    }

    public void init(String linkPlay, Callback callback) {
        if (linkPlay == null || linkPlay.isEmpty()) {
            //LLog.e(TAG, "init failed: linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        LUIUtil.showProgressBar(progressBar);
        this.linkPlay = linkPlay;
        this.callback = callback;
        if (FUZPlayerManagerV1 != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            FUZPlayerManagerV1.release();
        }
        checkToSetUp();

        //duong nhu ko bao h track EVENT_TYPE_DISPLAY, EVENT_TYPE_PLAYS_REQUESTED, EVENT_TYPE_VIDEO_STARTS
        //cannot delete delay below, only works after 500mls
        /*LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //track event eventype display
                trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), UizaDataV1.EVENT_TYPE_DISPLAY));

                //track event plays_requested
                trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), UizaDataV1.EVENT_TYPE_PLAYS_REQUESTED));
            }
        });*/
    }

    private void checkToSetUp() {
        setVideoCover();
        initData(linkPlay, null, null, null);
        onResume();
    }

    public FUZVideoV1(Context context) {
        super(context);
        onCreate();
    }

    public FUZVideoV1(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FUZVideoV1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FUZVideoV1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        //LLog.d(TAG, "onCreate");
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
        FUZPlayerManagerV1 = new FUZPlayerManagerV1(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        FUZPlayerManagerV1.setProgressCallback(new ProgressCallback() {
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
            //LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), "25", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), "50", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), "75", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(getContext(), "100", Constants.EVENT_TYPE_PLAY_THROUGHT));
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
        if (FUZPlayerManagerV1 != null) {
            FUZPlayerManagerV1.release();
        }
    }

    public void onResume() {
        if (FUZPlayerManagerV1 != null) {
            FUZPlayerManagerV1.init();
        }
    }

    public void onPause() {
        if (FUZPlayerManagerV1 != null) {
            FUZPlayerManagerV1.reset();
        }
    }

    public SimpleExoPlayer getPlayer() {
        if (FUZPlayerManagerV1 == null) {
            return null;
        }
        return FUZPlayerManagerV1.getPlayer();
    }

    public interface Callback {
        public void isInitResult(boolean isInitSuccess);
    }

    private Callback callback;

    private void trackUiza(final UizaTracking uizaTracking) {
        //LLog.d(TAG, "<<<trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
        if (RestClientTracking.getRetrofit() == null) {
            String currentApiTrackingEndPoint = UZUtil.getApiTrackEndPoint(getContext());
            //LLog.d(TAG, "trackUiza currentApiTrackingEndPoint: " + currentApiTrackingEndPoint);
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
                //LLog.d(TAG, ">>>trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
                LLog.e(TAG, "trackUiza onFail from service PiP:" + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
            }
        });
    }

    private void setVideoCover() {
        //LLog.d(TAG, "setVideoCover");
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
        }
        //LLog.d(TAG, "setVideoCover: " + UizaDataV1.getInstance().getEntityCover());
        //ivVideoCover.setBackgroundColor(LStoreUtil.getRandomColor());
        LImageUtil.load(getContext(), UizaDataV1.getInstance().getUizaInputV1().getEntityCover() == null ? Constants.URL_IMG_THUMBNAIL : Constants.PREFIXS + UizaDataV1.getInstance().getUizaInputV1().getEntityCover(), ivVideoCover, R.drawable.uiza);
    }

    public void removeVideoCover() {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "removeVideoCover");
            ivVideoCover.setVisibility(GONE);
        }
    }

    public void seekTo(long position) {
        if (FUZPlayerManagerV1 != null) {
            FUZPlayerManagerV1.seekTo(position);
        }
    }
}
package uizacoresdk.view.floatview;

/**
 * Created by www.muathu@gmail.com on 12/11/2018.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

import uizacoresdk.R;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZTrackingUtil;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.Url;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.rxandroid.ApiSubscriber;

public class FUZVideo extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private PlayerView playerView;
    private FUZPlayerManager fuzUizaPlayerManager;
    private ProgressBar progressBar;
    //private Gson gson = new Gson();

    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private String linkPlay;
    private boolean isLivestream;
    private long contentPosition;

    //Lấy vị trí của pip player
    public long getCurrentPosition() {
        if (getPlayer() == null) {
            return 0;
        }
        return getPlayer().getCurrentPosition();
    }

    public long getContentBufferedPosition() {
        if (getPlayer() == null) {
            return 0;
        }
        return getPlayer().getContentBufferedPosition();
    }

    protected void setDefautValueForFlagIsTracked() {
        UZTrackingUtil.clearAllValues(getContext());
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    public void init(String linkPlay, boolean isLivestream, long contentPosition, Callback callback) {
        if (linkPlay == null || linkPlay.isEmpty()) {
            LLog.e(TAG, "init failed: linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        showProgress();
        this.linkPlay = linkPlay;
        this.isLivestream = isLivestream;
        this.contentPosition = contentPosition;
        isFirstStateReady = false;
        LLog.d(TAG, "init linkPlay: " + linkPlay + ", isLivestream: " + isLivestream);
        this.callback = callback;
        if (fuzUizaPlayerManager != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            fuzUizaPlayerManager.release();
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
        //setVideoCover();
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
        inflate(getContext(), R.layout.float_uiza_video, this);
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
        //LLog.d(TAG, "initData linkPlay " + linkPlay);
        fuzUizaPlayerManager = new FUZPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        fuzUizaPlayerManager.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdEnded() {
            }

            @Override
            public void onAdProgress(int s, int duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                if (progressCallback != null) {
                    progressCallback.onAdProgress(s, duration, percent);
                }
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                //LLog.d(TAG, TAG + " onVideoProgress video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                trackProgress(s, percent);
                if (progressCallback != null) {
                    progressCallback.onVideoProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
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
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again");
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
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again");
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
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again");
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
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again");
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

    public void onDestroy() {
        //LLog.d(TAG, "trackUiza onDestroy");
        if (fuzUizaPlayerManager != null) {
            fuzUizaPlayerManager.release();
        }
    }

    public void onResume() {
        if (fuzUizaPlayerManager != null) {
            fuzUizaPlayerManager.init(isLivestream, contentPosition);
        }
    }

    public void onPause() {
        if (fuzUizaPlayerManager != null) {
            fuzUizaPlayerManager.reset();
        }
    }

    public SimpleExoPlayer getPlayer() {
        if (fuzUizaPlayerManager == null) {
            return null;
        }
        return fuzUizaPlayerManager.getPlayer();
    }

    public interface Callback {
        public void isInitResult(boolean isInitSuccess);

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        public void onVideoSizeChanged(int width, int height);

        public void onPlayerError(ExoPlaybackException error);
    }

    private boolean isFirstStateReady;

    private void onFirstStateReady() {
        //LLog.d(TAG, ">>>>>>>>>> onFirstStateReady");
        if (callback != null) {
            callback.isInitResult(true);
        }
    }

    protected void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                showProgress();
                break;
            case Player.STATE_ENDED:
                setDefautValueForFlagIsTracked();
                break;
            case Player.STATE_IDLE:
                showProgress();
                break;
            case Player.STATE_READY:
                if (!isFirstStateReady) {
                    onFirstStateReady();
                    isFirstStateReady = true;
                }
                hideProgress();
                break;
        }
        if (callback != null) {
            callback.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    protected void onVideoSizeChanged(int width, int height) {
        if (callback != null) {
            callback.onVideoSizeChanged(width, height);
        }
    }

    protected void onPlayerError(ExoPlaybackException error) {
        if (callback != null) {
            callback.onPlayerError(error);
        }
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
        UZService service = RestClientTracking.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                //LLog.d(TAG, "<------------------------pip track success: " + uizaTracking.getEventType() + " : " + uizaTracking.getPlayThrough() + " : " + uizaTracking.getEntityName());
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

    /*private void setVideoCover() {
        //LLog.d(TAG, "setVideoCover");
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
        }
        String urlImg = "";
        if (UZData.getInstance().getUzInput() == null || UZData.getInstance().getUzInput().getData() == null || UZData.getInstance().getUzInput().getData().getThumbnail() == null) {
            urlImg = Constants.URL_IMG_THUMBNAIL;
        } else {
            urlImg = UZData.getInstance().getUzInput().getData().getThumbnail();
        }
        LImageUtil.load(getContext(), urlImg, ivVideoCover, R.drawable.uiza);
    }*/

    /*protected void removeVideoCover() {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "removeVideoCover");
            ivVideoCover.setVisibility(GONE);
            onStateReadyFirst();
        }
    }*/

    protected void seekTo(long position) {
        if (fuzUizaPlayerManager != null) {
            fuzUizaPlayerManager.seekTo(position);
        }
    }

    //return true if toggleResume
    //return false if togglePause
    protected boolean togglePauseResume() {
        if (fuzUizaPlayerManager == null) {
            return false;
        }
        return fuzUizaPlayerManager.togglePauseResume();
    }

    protected void pauseVideo() {
        if (fuzUizaPlayerManager == null) {
            return;
        }
        fuzUizaPlayerManager.pauseVideo();
    }

    protected void resumeVideo() {
        if (fuzUizaPlayerManager == null) {
            return;
        }
        fuzUizaPlayerManager.resumeVideo();
    }

    protected int getVideoW() {
        if (fuzUizaPlayerManager == null) {
            return 0;
        }
        return fuzUizaPlayerManager.getVideoW();
    }

    protected int getVideoH() {
        if (fuzUizaPlayerManager == null) {
            return 0;
        }
        return fuzUizaPlayerManager.getVideoH();
    }

    protected void getLinkPlayOfNextItem(CallbackGetLinkPlay callbackGetLinkPlay) {
        if (UZData.getInstance().getDataList() == null) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        int currentPositionOfDataList = UZData.getInstance().getCurrentPositionOfDataList();
        int position = currentPositionOfDataList + 1;
        if (position < 0) {
            LLog.e(TAG, "This is the first item");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        if (position > UZData.getInstance().getDataList().size() - 1) {
            LLog.e(TAG, "This is the last item");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        UZData.getInstance().setCurrentPositionOfDataList(position);
        Data data = UZData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position + " -> " + data.getName());
        callAPIGetTokenStreaming(data.getId(), callbackGetLinkPlay);
    }

    private void callAPIGetTokenStreaming(final String entityId, final CallbackGetLinkPlay callbackGetLinkPlay) {
        //LLog.d(TAG, "callAPIGetTokenStreaming entityId " + entityId);
        UZService service = UZRestClient.createService(UZService.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityId);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        UZAPIMaster.getInstance().subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                //LLog.d(TAG, "callAPIGetTokenStreaming onSuccess: " + gson.toJson(result));
                if (result == null || result.getData() == null || result.getData().getToken() == null || result.getData().getToken().isEmpty()) {
                    callbackGetLinkPlay.onSuccess(null);
                    return;
                }
                String tokenStreaming = result.getData().getToken();
                //LLog.d(TAG, "tokenStreaming " + tokenStreaming);
                callAPIGetLinkPlay(entityId, tokenStreaming, callbackGetLinkPlay);
            }

            @Override
            public void onFail(Throwable e) {
                //LLog.e(TAG, "callAPIGetTokenStreaming onFail " + e.getMessage());
                callbackGetLinkPlay.onSuccess(null);
            }
        });
    }

    private void callAPIGetLinkPlay(String entityId, String tokenStreaming, final CallbackGetLinkPlay callbackGetLinkPlay) {
        //LLog.d(TAG, "callAPIGetLinkPlay isLivestream " + isLivestream);
        if (tokenStreaming == null || tokenStreaming.isEmpty()) {
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        UZRestClientGetLinkPlay.addAuthorization(tokenStreaming);
        UZService service = UZRestClientGetLinkPlay.createService(UZService.class);
        String appId = UZData.getInstance().getAppId();
        String typeContent = SendGetTokenStreaming.STREAM;
        //LLog.d(TAG, "===================================");
        //LLog.d(TAG, "========tokenStreaming: " + tokenStreaming);
        //LLog.d(TAG, "========appId: " + appId);
        //LLog.d(TAG, "========entityId: " + entityId);
        //LLog.d(TAG, "===================================");
        UZAPIMaster.getInstance().subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
            @Override
            public void onSuccess(ResultGetLinkPlay result) {
                //LLog.d(TAG, "getLinkPlayVOD onSuccess: " + gson.toJson(result));
                checkToSetUpResouce(result, callbackGetLinkPlay);
            }

            @Override
            public void onFail(Throwable e) {
                callbackGetLinkPlay.onSuccess(null);
            }
        });
    }

    public interface CallbackGetLinkPlay {
        public void onSuccess(String linkPlay);
    }

    private CallbackGetLinkPlay callbackGetLinkPlay;

    private void checkToSetUpResouce(ResultGetLinkPlay mResultGetLinkPlay, CallbackGetLinkPlay callbackGetLinkPlay) {
        //LLog.d(TAG, "checkToSetUpResouce isResultGetLinkPlayDone");
        if (mResultGetLinkPlay != null && UZData.getInstance().getData() != null) {
            //LLog.d(TAG, "checkToSetUpResouce if");
            List<String> listLinkPlay = new ArrayList<>();
            List<Url> urlList = mResultGetLinkPlay.getData().getUrls();
            if (isLivestream) {
                //LLog.d(TAG, "checkToSetUpResouce isLivestream true -> m3u8");
                //Bat buoc dung linkplay m3u8 cho nay, do bug cua system
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(".m3u8")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
                /*for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(".mpd")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }*/
            } else {
                //LLog.d(TAG, "checkToSetUpResouce isLivestream false -> mpd -> m3u8");
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(".mpd")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(".m3u8")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
            }

            //LLog.d(TAG, "listLinkPlay toJson: " + gson.toJson(listLinkPlay));
            if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                LLog.e(TAG, "checkToSetUpResouce listLinkPlay == null || listLinkPlay.isEmpty()");
                callbackGetLinkPlay.onSuccess(null);
                return;
            }
            String lp = listLinkPlay.get(0);
            callbackGetLinkPlay.onSuccess(lp);
        }
    }

    protected void hideProgress() {
        LUIUtil.hideProgressBar(progressBar);
    }

    protected void showProgress() {
        LUIUtil.showProgressBar(progressBar);
    }
}
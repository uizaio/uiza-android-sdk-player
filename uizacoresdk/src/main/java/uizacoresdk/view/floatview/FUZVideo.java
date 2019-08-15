package uizacoresdk.view.floatview;

/**
 * Created by www.muathu@gmail.com on 14/1/2019.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.api.request.streaming.StreamingTokenRequest;
import io.uiza.core.api.request.tracking.UizaTracking;
import io.uiza.core.api.request.tracking.UizaTrackingCCU;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.BaseResponse;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.linkplay.Url;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import java.util.ArrayList;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZOsUtil;
import uizacoresdk.util.UZTrackingUtil;
import uizacoresdk.util.UZUtil;

public class FUZVideo extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private static final String M3U8_EXTENSION = ".m3u8";
    private static final String MPD_EXTENSION = ".mpd";
    private static final String PLAY_THROUGH_100 = "100";
    private static final String PLAY_THROUGH_75 = "75";
    private static final String PLAY_THROUGH_50 = "50";
    private static final String PLAY_THROUGH_25 = "25";
    private static final int INTERVAL_TRACK_CCU = 3000;

    private PlayerView playerView;
    private FUZPlayerManagerAbs fuzUizaPlayerManager;
    private ProgressBar progressBar;
    private String cdnHost;
    private String uuid;
    private String linkPlay;
    private boolean isLivestream;
    private long contentPosition;
    private boolean isOnStateReadyFirst;
    private boolean isInitCustomLinkPlay;
    private int progressBarColor = Color.WHITE;
    private long timestampRebufferStart;
    private int oldPercent = Constants.NOT_FOUND;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;
    private ProgressCallback progressCallback;
    private boolean isTrackingMuiza;

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
        progressBar = findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(progressBar, progressBarColor);
        playerView = findViewById(R.id.player_view);
    }

    public void setProgressBarColor(int progressBarColor) {
        if (progressBar != null) {
            this.progressBarColor = progressBarColor;
            UzDisplayUtil.setColorProgressBar(progressBar, progressBarColor);
        }
    }

    //=============================================================================================================START CONFIG
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

    private void releasePlayerManager() {
        if (fuzUizaPlayerManager != null) {
            fuzUizaPlayerManager.release();
        }
    }

    //=============================================================================================================END CONFIG

    //=============================================================================================================START VIEW
    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public SimpleExoPlayer getPlayer() {
        if (fuzUizaPlayerManager == null) {
            return null;
        }
        return fuzUizaPlayerManager.getPlayer();
    }

    protected void onVideoSizeChanged(int width, int height) {
        if (callback != null) {
            callback.onVideoSizeChanged(width, height);
        }
    }

    protected void hideProgress() {
        UzDisplayUtil.hideProgressBar(progressBar);
    }

    protected void showProgress() {
        UzDisplayUtil.showProgressBar(progressBar);
    }
    //=============================================================================================================END VIEW

    public void init(String linkPlay, String cdnHost, String uuid, boolean isLivestream, long contentPosition, boolean isInitCustomLinkPlay, int progressBarColor, Callback callback) {
        if (linkPlay == null || linkPlay.isEmpty()) {
            LLog.e(TAG, "init failed: linkPlay == null || linkPlay.isEmpty()");
            return;
        }
        showProgress();
        this.linkPlay = linkPlay;
        this.cdnHost = cdnHost;
        this.uuid = uuid;
        this.isLivestream = isLivestream;
        this.contentPosition = contentPosition;
        this.isInitCustomLinkPlay = isInitCustomLinkPlay;
        this.progressBarColor = progressBarColor;
        UzDisplayUtil.setColorProgressBar(progressBar, this.progressBarColor);
        isOnStateReadyFirst = false;
        LLog.d(TAG, "init linkPlay: " + linkPlay + ", isLivestream: " + isLivestream + ", isInitCustomLinkPlay: " + isInitCustomLinkPlay);
        this.callback = callback;
        releasePlayerManager();
        checkToSetUp();
        //track event eventype display
        if (!UZTrackingUtil.isTrackedEventTypeDisplay(getContext())) {
            trackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_DISPLAY),
                    new UZTrackingUtil.UizaTrackingCallback() {
                        @Override
                        public void onTrackingSuccess() {
                            UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(getContext(), true);
                        }
                    });
        }

        //track event plays_requested
        if (!UZTrackingUtil.isTrackedEventTypePlaysRequested(getContext())) {
            trackUiza(UZData.getInstance()
                            .createTrackingInput(getContext(), Constants.EVENT_TYPE_PLAYS_REQUESTED),
                    new UZTrackingUtil.UizaTrackingCallback() {
                        @Override
                        public void onTrackingSuccess() {
                            UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(getContext(), true);
                        }
                    });
        }
    }

    private void checkToSetUp() {
        initData(linkPlay, null, null, null, isAdsDependencyAvailable());
        onResume();
    }

    public void initData(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList, boolean includeAd) {
        if (includeAd) {
            fuzUizaPlayerManager =
                    new FUZPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
            fuzUizaPlayerManager.setProgressCallback(new ProgressCallback() {
                @Override
                public void onAdEnded() {
                }

                @Override
                public void onAdProgress(int s, int duration, int percent) {
                    if (progressCallback != null) {
                        progressCallback.onAdProgress(s, duration, percent);
                    }
                }

                @Override
                public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                    TmpParamData.getInstance().setPlayerPlayheadTime(s);
                    trackProgress(s, percent);
                    callAPITrackMuiza(s);
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
        } else {
            fuzUizaPlayerManager =
                    new FUZNoAdsPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar,
                            subtitleList);
        }
    }

    //=============================================================================================================START LIFE CIRCLE
    public void onDestroy() {
        releasePlayerManager();
        cdnHost = null;
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

    //=============================================================================================================END LIFE CIRCLE
    //=============================================================================================================START API
    private void callAPIGetTokenStreaming(final String entityId,
            final CallbackGetLinkPlay callbackGetLinkPlay) {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        StreamingTokenRequest streamingTokenRequest = new StreamingTokenRequest();
        streamingTokenRequest.setAppId(UZData.getInstance().getAppId());
        streamingTokenRequest.setEntityId(entityId);
        streamingTokenRequest.setContentType(StreamingTokenRequest.STREAM);
        UzApiMaster.getInstance().subscribe(
                service.getTokenStreaming(UZData.getInstance().getAPIVersion(),
                        streamingTokenRequest), new ApiSubscriber<BaseResponse<StreamingToken>>() {
                    @Override
                    public void onSuccess(BaseResponse<StreamingToken> response) {
                        if (response == null || response.getData() == null
                                || response.getData().getToken() == null || response.getData()
                                .getToken().isEmpty()) {
                            callbackGetLinkPlay.onSuccess(null);
                            return;
                        }
                        String tokenStreaming = response.getData().getToken();
                        LLog.d(TAG, "tokenStreaming " + tokenStreaming);
                        callAPIGetLinkPlay(entityId, tokenStreaming, callbackGetLinkPlay);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        callbackGetLinkPlay.onSuccess(null);
                    }
                });
    }

    private void callAPIGetLinkPlay(String entityId, String tokenStreaming, final CallbackGetLinkPlay callbackGetLinkPlay) {
        if (tokenStreaming == null || tokenStreaming.isEmpty()) {
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        UzRestClientGetLinkPlay.addAuthorization(tokenStreaming);
        UzServiceApi service = UzRestClientGetLinkPlay.createService(UzServiceApi.class);
        String appId = UZData.getInstance().getAppId();
        String typeContent = StreamingTokenRequest.STREAM;
        UzApiMaster.getInstance().subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<BaseResponse<LinkPlay>>() {
            @Override
            public void onSuccess(BaseResponse<LinkPlay> response) {
                LLog.d(TAG, "getLinkPlayVOD onSuccess");
                checkToSetUpResource(response, callbackGetLinkPlay);
            }

            @Override
            public void onFail(Throwable e) {
                callbackGetLinkPlay.onSuccess(null);
            }
        });
    }

    private void checkToSetUpResource(BaseResponse<LinkPlay> response, CallbackGetLinkPlay callbackGetLinkPlay) {
        if (response != null && UZData.getInstance().getData() != null) {
            List<String> listLinkPlay = new ArrayList<>();
            List<Url> urlList = response.getData().getUrls();
            if (isLivestream) {
                // Bat buoc dung linkplay m3u8 cho nay, do bug cua system
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(M3U8_EXTENSION)) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
            } else {
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(MPD_EXTENSION)) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
                for (Url url : urlList) {
                    if (url.getUrl().toLowerCase().endsWith(M3U8_EXTENSION)) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
            }

            if (listLinkPlay.isEmpty()) {
                LLog.e(TAG, "checkToSetUpResource listLinkPlay == null || listLinkPlay.isEmpty()");
                callbackGetLinkPlay.onSuccess(null);
                return;
            }
            String lp = listLinkPlay.get(0);
            callbackGetLinkPlay.onSuccess(lp);
        }
    }

    //=============================================================================================================END API

    //===========================================================================================================START TRACKING


    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s), only track if isLivestream false
        if (s == (isLivestream ? 3 : 5)) {
            if (!UZTrackingUtil.isTrackedEventTypeView(getContext())) {
                trackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIEW), new UZTrackingUtil.UizaTrackingCallback() {
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
        if (percent >= Constants.PLAYTHROUGH_100) {
            if (isTracked100) {
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought100(getContext())) {
                isTracked100 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_100, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought75(getContext())) {
                isTracked75 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_75, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought50(getContext())) {
                isTracked50 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_50, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(getContext(), true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought25(getContext())) {
                isTracked25 = true;
            } else {
                trackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_25, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(getContext(), true);
                    }
                });
            }
        }
    }

    protected void setDefaultValueForFlagIsTracked() {
        UZTrackingUtil.clearAllValues(getContext());
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    private void trackUiza(final UizaTracking uizaTracking, final UZTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        if (UzRestClientTracking.getRetrofit() == null) {
            String currentApiTrackingEndPoint = UZUtil.getApiTrackEndPoint(getContext());
            if (currentApiTrackingEndPoint == null || currentApiTrackingEndPoint.isEmpty()) {
                LLog.e(TAG, "trackUiza failed pip urrentApiTrackingEndPoint == null || currentApiTrackingEndPoint.iuizacoresdk/view/floatview/FUZVideo.java:419sEmpty()");
                return;
            }
            UzRestClientTracking.init(currentApiTrackingEndPoint);
        }
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                if (uizaTrackingCallback != null) {
                    uizaTrackingCallback.onTrackingSuccess();
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
            }
        });
    }

    private void pingHeartBeat() {
        if (fuzUizaPlayerManager == null || cdnHost == null || cdnHost.isEmpty()) {
            LLog.e(TAG, "Error cannot call API pingHeartBeat() -> destroy");
            return;
        }
        UzServiceApi service = UzRestClientHeartBeat.createService(UzServiceApi.class);
        String cdnName = cdnHost;
        String session = uuid;
        UzApiMaster.getInstance().subscribe(service.pingHeartBeat(cdnName, session), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "pingHeartBeat onSuccess " + UZData.getInstance().getEntityName());
                UzDisplayUtil.setDelay(10000, new UzDisplayUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        pingHeartBeat();
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "pingHeartBeat onFail: " + e.toString());
                UzDisplayUtil.setDelay(10000, new UzDisplayUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        pingHeartBeat();
                    }
                });
            }
        });
    }

    private void trackUizaCCUForLivestream() {
        if (fuzUizaPlayerManager == null || !isLivestream || isInitCustomLinkPlay) {
            LLog.e(TAG, "Error cannot trackUizaCCUForLivestream() -> return " + UZData.getInstance()
                    .getEntityName());
            return;
        }
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.trackCCU(ccuTrackingRequest()), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        LLog.d(TAG, "trackCCU success: " + UZData.getInstance().getEntityName());
                        UzDisplayUtil
                                .setDelay(INTERVAL_TRACK_CCU, new UzDisplayUtil.DelayCallback() {
                                    @Override
                                    public void doAfter(int mls) {
                                        trackUizaCCUForLivestream();
                                    }
                                });
                    }

                    @Override
                    public void onFail(Throwable e) {
                        UzDisplayUtil
                                .setDelay(INTERVAL_TRACK_CCU, new UzDisplayUtil.DelayCallback() {
                                    @Override
                                    public void doAfter(int mls) {
                                        trackUizaCCUForLivestream();
                                    }
                                });
                    }
                });
    }


    private UizaTrackingCCU ccuTrackingRequest() {
        UizaTrackingCCU.Builder ccuBuilder = new UizaTrackingCCU.Builder();
        ccuBuilder.dt(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1)).ho(cdnHost)
                .ai(UZData.getInstance().getAppId()).sn(UZData.getInstance().getEntityName())
                .di(UZOsUtil.getDeviceId(getContext())).ua(Constants.USER_AGENT);
        return ccuBuilder.build();
    }

    protected void addTrackingMuizaError(String event, UzException e) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UZData.getInstance().addTrackingMuiza(getContext(), event, e);
    }

    protected void addTrackingMuiza(String event) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UZData.getInstance().addTrackingMuiza(getContext(), event);
    }

    private void callAPITrackMuiza(int s) {
        if (isInitCustomLinkPlay) {
            return;
        }
        if (s <= 0 || s % 10 != 0) {
            return;
        }
        if (isTrackingMuiza) {
            return;
        }
        if (UZData.getInstance().isMuizaListEmpty()) {
            return;
        }
        isTrackingMuiza = true;
        final List<Muiza> muizaListToTracking = new ArrayList<>(UZData.getInstance().getMuizaList());
        UZData.getInstance().clearMuizaList();
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(service.trackMuiza(muizaListToTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                isTrackingMuiza = false;
            }

            @Override
            public void onFail(Throwable e) {
                isTrackingMuiza = false;
                UZData.getInstance().addListTrackingMuiza(muizaListToTracking);
            }
        });
    }

    //=============================================================================================================END TRACKING
    //=============================================================================================================START CALLBACK

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public interface Callback {
        void isInitResult(boolean isInitSuccess);

        void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        void onVideoSizeChanged(int width, int height);

        void onPlayerError(UzException error);
    }


    protected void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                showProgress();
                timestampRebufferStart = System.currentTimeMillis();
                addTrackingMuiza(Constants.MUIZA_EVENT_REBUFFERSTART);
                TmpParamData.getInstance().addViewRebufferCount();
                if (!playWhenReady) {
                    addTrackingMuiza(Constants.MUIZA_EVENT_WAITING);
                }
                break;
            case Player.STATE_ENDED:
                timestampRebufferStart = 0;
                setDefaultValueForFlagIsTracked();
                addTrackingMuiza(Constants.MUIZA_EVENT_VIEWENDED);
                break;
            case Player.STATE_IDLE:
                timestampRebufferStart = 0;
                showProgress();
                break;
            case Player.STATE_READY:
                if (timestampRebufferStart != 0) {
                    TmpParamData.getInstance().setViewRebufferDuration(System.currentTimeMillis() - timestampRebufferStart);
                    addTrackingMuiza(Constants.MUIZA_EVENT_REBUFFEREND);
                }
                timestampRebufferStart = 0;

                if (!isOnStateReadyFirst) {
                    onStateReadyFirst();
                    isOnStateReadyFirst = true;
                }
                hideProgress();
                break;
        }
        if (callback != null) {
            callback.onPlayerStateChanged(playWhenReady, playbackState);
        }
    }

    protected void onPlayerError(UzException error) {
        addTrackingMuizaError(Constants.MUIZA_EVENT_ERROR, error);
        if (callback != null) {
            callback.onPlayerError(error);
        }
    }

    private Callback callback;

    private void onStateReadyFirst() {
        if (callback != null) {
            callback.isInitResult(true);
        }
        pingHeartBeat();
        trackUizaCCUForLivestream();
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
        VideoData data = UZData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position + " -> " + data.getName());
        callAPIGetTokenStreaming(data.getId(), callbackGetLinkPlay);
    }

    public interface CallbackGetLinkPlay {
        void onSuccess(String linkPlay);
    }

    protected boolean isAdsDependencyAvailable() {
        return UZUtil.isDependencyAvailable("com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer");
    }
    //=============================================================================================================END CALLBACK
}
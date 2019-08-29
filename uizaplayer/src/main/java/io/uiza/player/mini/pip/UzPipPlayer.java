package io.uiza.player.mini.pip;

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
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCommonUtil.DelayCallback;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.UzOsUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.R;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.analytic.event.EventTrackingManager;
import io.uiza.player.analytic.event.TrackingEvent;
import io.uiza.player.analytic.muiza.MuizaEvent;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.util.UzPlayerData;
import java.util.ArrayList;
import java.util.List;

public class UzPipPlayer extends RelativeLayout {

    private static final String TAG = UzPipPlayer.class.getSimpleName();
    private static final String M3U8_EXTENSION = ".m3u8";
    private static final String MPD_EXTENSION = ".mpd";
    private static final int INTERVAL_TRACK_CCU = 3000;
    private static final int INTERVAL_HEART_BEAT = 1000;

    private PlayerView playerView;
    private PipPlayerManagerAbs pipPlayerManager;
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
    private UzAdEventListener adEventListener;
    private UzPlayerEventListener playerEventListener;
    private boolean isTrackingMuiza;
    private int counterInterval;
    private EventTrackingManager eventTrackingManager;

    public UzPipPlayer(Context context) {
        super(context);
        onCreate();
    }

    public UzPipPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UzPipPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UzPipPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        eventTrackingManager = new EventTrackingManager(getContext());
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
        if (pipPlayerManager != null) {
            pipPlayerManager.seekTo(position);
        }
    }

    protected boolean togglePauseResume() {
        if (pipPlayerManager == null) {
            return false;
        }
        return pipPlayerManager.togglePauseResume();
    }

    protected void pauseVideo() {
        if (pipPlayerManager == null) {
            return;
        }
        pipPlayerManager.pauseVideo();
    }

    protected void resumeVideo() {
        if (pipPlayerManager == null) {
            return;
        }
        pipPlayerManager.resumeVideo();
    }

    protected int getVideoW() {
        if (pipPlayerManager == null) {
            return 0;
        }
        return pipPlayerManager.getVideoWidth();
    }

    protected int getVideoH() {
        if (pipPlayerManager == null) {
            return 0;
        }
        return pipPlayerManager.getVideoHeight();
    }

    private void releasePlayerManager() {
        if (pipPlayerManager != null) {
            pipPlayerManager.release();
        }
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public SimpleExoPlayer getPlayer() {
        if (pipPlayerManager == null) {
            return null;
        }
        return pipPlayerManager.getPlayer();
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

    public void init(String linkPlay, String cdnHost, String uuid, boolean isLivestream,
            long contentPosition, boolean isInitCustomLinkPlay, int progressBarColor,
            Callback callback) {
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
        LLog.d(TAG, "init linkPlay: " + linkPlay + ", isLivestream: " + isLivestream
                + ", isInitCustomLinkPlay: " + isInitCustomLinkPlay);
        this.callback = callback;
        releasePlayerManager();
        checkToSetUp();
        //track event eventype display
        if (!isInitCustomLinkPlay) {
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_DISPLAY);
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_PLAYS_REQUESTED);
        }
    }

    private void checkToSetUp() {
        initData(linkPlay, null, null, null, UzCommonUtil.isAdsDependencyAvailable());
        onResume();
    }

    public void initData(String linkPlay, String imaAdUrl, String urlThumnailsPreviewSeekbar,
            List<Subtitle> subtitleList, boolean includeAd) {
        if (includeAd) {
            pipPlayerManager = new PipPlayerManager(this, linkPlay, imaAdUrl,
                    urlThumnailsPreviewSeekbar, subtitleList);

            pipPlayerManager.setUzAdEventListener(new UzAdEventListener() {
                @Override
                public void onAdProgress(int s, int duration, int percent) {
                    if (adEventListener != null) {
                        adEventListener.onAdProgress(s, duration, percent);
                    }
                }

                @Override
                public void onAdEnded() {
                    if (adEventListener != null) {
                        adEventListener.onAdEnded();
                    }
                }
            });
        } else {
            pipPlayerManager = new PipNoAdsPlayerManager(this, linkPlay, urlThumnailsPreviewSeekbar,
                    subtitleList);
        }
        pipPlayerManager.setUzPlayerEventListener(new UzPlayerEventListener() {
            @Override
            public void onDataInitialized(boolean initSuccess, boolean getDataSuccess,
                    LinkPlay linkPlay, VideoData data) {
                if (playerEventListener != null) {
                    playerEventListener
                            .onDataInitialized(initSuccess, getDataSuccess, linkPlay, data);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playerEventListener != null) {
                    playerEventListener.onPlayerStateChanged(playWhenReady, playbackState);
                }
            }


            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                TmpParamData.getInstance().setPlayerPlayHeadTime(s);
                if (playerEventListener != null) {
                    playerEventListener.onVideoProgress(currentMls, s, duration, percent);
                }
                if (isInitCustomLinkPlay) {
                    return;
                }
                trackingData(s, percent);
            }


            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage,
                    long duration) {
                if (playerEventListener != null) {
                    playerEventListener
                            .onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }

            @Override
            public void onVideoEnded() {
                if (playerEventListener != null) {
                    playerEventListener.onVideoEnded();
                }
            }

            @Override
            public void onPlayerError(UzException exception) {
                if (playerEventListener != null) {
                    playerEventListener.onPlayerError(exception);
                }
            }
        });
    }

    public void onDestroy() {
        releasePlayerManager();
        cdnHost = null;
    }

    public void onResume() {
        if (pipPlayerManager != null) {
            pipPlayerManager.init(isLivestream, contentPosition);
        }
    }

    public void onPause() {
        if (pipPlayerManager != null) {
            pipPlayerManager.reset();
        }
    }

    private void callApiGetTokenStreaming(final String entityId,
            final CallbackGetLinkPlay callbackGetLinkPlay) {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        StreamingTokenRequest request = new StreamingTokenRequest();
        request.setAppId(UzPlayerData.getInstance().getAppId());
        request.setEntityId(entityId);
        request.setContentType(StreamingTokenRequest.STREAM);

        String apiVersion = UzPlayerData.getInstance().getApiVersion();
        UzApiMaster.getInstance().subscribe(
                service.getTokenStreaming(apiVersion, request),
                new ApiSubscriber<BaseResponse<StreamingToken>>() {
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
                        callApiGetLinkPlay(entityId, tokenStreaming, callbackGetLinkPlay);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        callbackGetLinkPlay.onSuccess(null);
                    }
                });
    }

    private void callApiGetLinkPlay(String entityId, String tokenStreaming,
            final CallbackGetLinkPlay callbackGetLinkPlay) {
        if (tokenStreaming == null || tokenStreaming.isEmpty()) {
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        UzRestClientGetLinkPlay.addAuthorization(tokenStreaming);
        UzServiceApi service = UzRestClientGetLinkPlay.createService(UzServiceApi.class);
        String appId = UzPlayerData.getInstance().getAppId();
        String typeContent = StreamingTokenRequest.STREAM;

        UzApiMaster.getInstance().subscribe(service.getLinkPlay(appId, entityId, typeContent),
                new ApiSubscriber<BaseResponse<LinkPlay>>() {
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

    private void checkToSetUpResource(BaseResponse<LinkPlay> response,
            CallbackGetLinkPlay callbackGetLinkPlay) {
        if (response != null && UzPlayerData.getInstance().getVideoData() != null) {
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

    private void trackingData(long s, int percent) {
        // track progress, VOD only
        if (!isLivestream && oldPercent != percent) {
            oldPercent = percent;
            eventTrackingManager.trackProgress(percent);
        }

        counterInterval++;
        // track event view
        if (counterInterval == (isLivestream ? 3 : 5)) {
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_VIEW);
        }

        // track analytic
        if ((s > 0 && counterInterval % 10 == 0) && !isTrackingMuiza
                && !UzPlayerData.getInstance().isMuizaListEmpty()) {
            trackMuizaData();
        }
    }

    private void pingHeartBeat() {
        if (pipPlayerManager == null || cdnHost == null || cdnHost.isEmpty()) {
            LLog.e(TAG, "Error cannot call API pingHeartBeat() -> destroy");
            return;
        }
        UzServiceApi service = UzRestClientHeartBeat.createService(UzServiceApi.class);
        String cdnName = cdnHost;
        String session = uuid;
        UzApiMaster.getInstance()
                .subscribe(service.pingHeartBeat(cdnName, session), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        LLog.d(TAG, "pingHeartBeat onSuccess " + UzPlayerData.getInstance()
                                .getEntityName());
                        rePingHeartBeat();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "pingHeartBeat onFail: " + e.toString());
                        rePingHeartBeat();
                    }
                });
    }

    private void rePingHeartBeat() {
        UzCommonUtil.actionWithDelayed(INTERVAL_HEART_BEAT, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                pingHeartBeat();
            }
        });
    }

    private void trackUizaCcuForLivestream() {
        if (pipPlayerManager == null || !isLivestream || isInitCustomLinkPlay) {
            LLog.e(TAG, "Error cannot trackUizaCcuForLivestream() -> return " + UzPlayerData
                    .getInstance().getEntityName());
            return;
        }
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.trackCCU(ccuTrackingRequest()), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        LLog.d(TAG,
                                "trackCCU success: " + UzPlayerData.getInstance().getEntityName());
                        reTrackUizaCcuForLivestream();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        reTrackUizaCcuForLivestream();
                    }
                });
    }

    private void reTrackUizaCcuForLivestream() {
        UzCommonUtil.actionWithDelayed(INTERVAL_TRACK_CCU, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                trackUizaCcuForLivestream();
            }
        });
    }

    private UizaTrackingCCU ccuTrackingRequest() {
        UizaTrackingCCU.Builder ccuBuilder = new UizaTrackingCCU.Builder();
        ccuBuilder.dt(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1)).ho(cdnHost)
                .ai(UzPlayerData.getInstance().getAppId())
                .sn(UzPlayerData.getInstance().getChannelName())
                .di(UzOsUtil.getDeviceId(getContext())).ua(Constants.USER_AGENT);
        return ccuBuilder.build();
    }

    protected void addTrackingMuizaError(@MuizaEvent String event, UzException e) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UzPlayerData.getInstance().addTrackingMuiza(getContext(), event, e);
    }

    protected void addTrackingMuiza(@MuizaEvent String event) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UzPlayerData.getInstance().addTrackingMuiza(getContext(), event);
    }

    private void trackMuizaData() {
        isTrackingMuiza = true;
        final List<Muiza> muizaListToTracking = new ArrayList<>(
                UzPlayerData.getInstance().getMuizaList());
        UzPlayerData.getInstance().clearMuizaList();
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);

        UzApiMaster.getInstance()
                .subscribe(service.trackMuiza(muizaListToTracking), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        isTrackingMuiza = false;
                        counterInterval = 0;
                    }

                    @Override
                    public void onFail(Throwable e) {
                        isTrackingMuiza = false;
                        counterInterval = 0;
                        UzPlayerData.getInstance().addListTrackingMuiza(muizaListToTracking);
                    }
                });
    }

    public void setUzAdEventListener(UzAdEventListener adEventListener) {
        this.adEventListener = adEventListener;
    }

    public void setUzPlayerEventListener(UzPlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public interface Callback {

        void onDataInitialized(boolean isInitSuccess);

        void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        void onVideoSizeChanged(int width, int height);

        void onPlayerError(UzException error);
    }


    protected void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                showProgress();
                timestampRebufferStart = System.currentTimeMillis();
                addTrackingMuiza(MuizaEvent.MUIZA_EVENT_REBUFFERSTART);
                TmpParamData.getInstance().addViewRebufferCount();
                if (!playWhenReady) {
                    addTrackingMuiza(MuizaEvent.MUIZA_EVENT_WAITING);
                }
                break;
            case Player.STATE_ENDED:
                timestampRebufferStart = 0;
                eventTrackingManager.resetTracking();
                addTrackingMuiza(MuizaEvent.MUIZA_EVENT_VIEWENDED);
                break;
            case Player.STATE_IDLE:
                timestampRebufferStart = 0;
                showProgress();
                break;
            case Player.STATE_READY:
                if (timestampRebufferStart != 0) {
                    TmpParamData.getInstance().setViewRebufferDuration(
                            System.currentTimeMillis() - timestampRebufferStart);
                    addTrackingMuiza(MuizaEvent.MUIZA_EVENT_REBUFFEREND);
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
        addTrackingMuizaError(MuizaEvent.MUIZA_EVENT_ERROR, error);
        if (callback != null) {
            callback.onPlayerError(error);
        }
    }

    private Callback callback;

    private void onStateReadyFirst() {
        if (callback != null) {
            callback.onDataInitialized(true);
        }
        pingHeartBeat();
        trackUizaCcuForLivestream();
    }

    protected void getLinkPlayOfNextItem(CallbackGetLinkPlay callbackGetLinkPlay) {
        if (UzPlayerData.getInstance().getPlaylistData() == null) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        int currentPositionOfDataList = UzPlayerData.getInstance().getPositionInPlaylist();
        int position = currentPositionOfDataList + 1;
        if (position < 0) {
            LLog.e(TAG, "This is the first item");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        if (position > UzPlayerData.getInstance().getPlaylistData().size() - 1) {
            LLog.e(TAG, "This is the last item");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        UzPlayerData.getInstance().setPositionInPlaylist(position);
        VideoData data = UzPlayerData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            callbackGetLinkPlay.onSuccess(null);
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position + " -> " + data
                .getName());
        callApiGetTokenStreaming(data.getId(), callbackGetLinkPlay);
    }

    public interface CallbackGetLinkPlay {

        void onSuccess(String linkPlay);
    }
}
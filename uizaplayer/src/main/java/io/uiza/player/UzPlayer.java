package io.uiza.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.github.rubensousa.previewseekbar.PreviewView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import io.uiza.core.api.CallbackGetDetailEntity;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.UzUtilBase;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.api.request.streaming.StreamingTokenRequest;
import io.uiza.core.api.request.tracking.UizaTrackingCCU;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.BaseResponse;
import io.uiza.core.api.response.ad.Ad;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.linkplay.Url;
import io.uiza.core.api.response.playerinfo.Logo;
import io.uiza.core.api.response.playerinfo.PlayerInfo;
import io.uiza.core.api.response.streaming.LiveFeedViews;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;
import io.uiza.core.exception.UzExceptionUtil;
import io.uiza.core.model.UzLinkPlayData;
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCommonUtil.DelayCallback;
import io.uiza.core.util.UzConvertUtils;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.UzOsUtil;
import io.uiza.core.util.connection.UzConectifyService;
import io.uiza.core.util.connection.UzConnectivityEvent;
import io.uiza.core.util.connection.UzConnectivityUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.autosize.UzImageButton;
import io.uiza.core.view.autosize.UzTextView;
import io.uiza.core.view.seekbar.UzVerticalSeekBar;
import io.uiza.player.ads.UzAdPlayerCallback;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.analytic.event.EventTrackingManager;
import io.uiza.player.analytic.event.TrackingEvent;
import io.uiza.player.analytic.muiza.MuizaEvent;
import io.uiza.player.cast.CastyPlayer;
import io.uiza.player.cast.UzChromeCast;
import io.uiza.player.cast.UzChromeCast.CastListener;
import io.uiza.player.interfaces.UzAdEventListener;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzLiveInfoListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerTvListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.interfaces.UzTimeBarPreviewListener;
import io.uiza.player.interfaces.UzPlayerBufferChangedListener;
import io.uiza.player.mini.pip.PipHelper;
import io.uiza.player.mini.pip.UzPipService;
import io.uiza.player.util.ComunicateMsg;
import io.uiza.player.util.SensorChangeNotifier;
import io.uiza.player.util.UzPlayerData;
import io.uiza.player.util.UzPlayerUtil;
import io.uiza.player.view.UzPlayerView;
import io.uiza.player.view.UzTimeBar;
import io.uiza.player.view.UzTrackSelectionView;
import io.uiza.player.view.dialog.SpeedDialog;
import io.uiza.player.view.dialog.UzDialogInfo;
import io.uiza.player.view.dialog.playlist.UzPlaylistCallback;
import io.uiza.player.view.dialog.playlist.UzPlaylistDialog;
import io.uiza.player.view.stats.StatsForNerdsHelper;
import io.uiza.player.view.stats.StatsForNerdsView;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UzPlayer extends RelativeLayout implements PreviewView.OnPreviewChangeListener,
        View.OnClickListener, View.OnFocusChangeListener, UzPlayerView.ControllerStateCallback,
        SensorChangeNotifier.Listener {

    private static final String TAG = UzPlayer.class.getSimpleName();
    private static final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private static final int DELAY_TO_GET_LIVE_INFORMATION = 15000;
    private static final int INTERVAL_HEART_BEAT = 10000;
    private static final int INTERVAL_TRACK_CCU = 3000;
    private static final String M3U8_EXTENSION = ".m3u8";
    private static final String MPD_EXTENSION = ".mpd";
    private static final String HYPHEN = "-";
    private int defaultValueBackwardForward = 10000;
    private int defaultValueControllerTimeout = 8000;
    private boolean isLivestream;
    private boolean isTablet;
    private String cdnHost;
    private boolean isTV;
    private View bkg;
    private RelativeLayout rootView, rlChromeCast;
    private UzPlayerManagerAbs uzPlayerManager;
    private ProgressBar progressBar;
    private LinearLayout llTop, debugRootView;
    private RelativeLayout rlMsg, rlLiveInfo, rlEndScreen;
    private FrameLayout previewFrameLayout;
    private UzTimeBar uzTimebar;
    private ImageView ivThumbnail, ivVideoCover, ivLogo;
    private UzTextView tvPosition, tvDuration;
    private TextView tvMsg, tvTitle, tvLiveStatus, tvLiveView, tvLiveTime;
    private UzImageButton ibFullscreenIcon, ibPauseIcon, ibPlayIcon, ibReplayIcon, ibRewIcon,
            ibFfwdIcon, ibBackScreenIcon, ibVolumeIcon, ibSettingIcon, ibCcIcon,
            ibPlaylistFolderIcon, ibHearingIcon, ibPictureInPictureIcon, ibSkipPreviousIcon,
            ibSkipNextIcon, ibSpeedIcon, ivLiveTime, ivLiveView, ibsCast;
    private TextView debugTextView, tvEndScreenMsg;
    private UzPlayerView uzPlayerView;
    private View firstViewHasFocus;
    private boolean isAutoShowController;
    private boolean isFreeSize;
    private boolean isPlayerControllerAlwaysVisible;
    private boolean isSetFirstRequestFocusDone;
    private boolean hasError;
    private boolean isInitCustomLinkPlay;//user pass any link (not use entityId or metadataId)
    private boolean activityIsPausing = false;
    private boolean isOnPreview;
    private boolean isLandscape;
    private boolean isInitMiniPlayerSuccess = true;
    private boolean isHideOnTouch = true;
    private boolean isDefaultUseController = true;
    private boolean isOnPlayerEnded;
    private boolean isClickedSkipNextOrSkipPrevious;
    private boolean isRefreshFromChangeSkin;
    private long currentPositionBeforeChangeSkin;
    private boolean isCalledFromChangeSkin;
    private boolean isCalledApiGetDetailEntity;
    private boolean isCalledApiGetImaAdUrlTag;
    private boolean isCalledApiGetTokenStreaming;
    private VideoData data;
    private boolean isGetClickedPip;
    private int oldPercent = Constants.NOT_FOUND;
    private boolean isTrackingMuiza;
    private boolean isCalledFromConnectionEventBus = false;
    private long positionMiniPlayer;
    private boolean isCastPlayerPlayingFirst;
    private UzAdPlayerCallback videoAdPlayerCallback;
    private String entityId;
    private UUID uuid;
    private long timestampBeforeInitNewSession;
    private long timestampOnStartPreview;
    private String urlImgThumbnail;
    private boolean isAutoStart = Constants.DF_PLAYER_IS_AUTO_START;
    private boolean isAutoSwitchItemPlaylistFolder = true;
    private LinkPlay linkPlay;
    private StreamingToken streamingToken;
    private String imaAdsUrl = null;
    private PlayerInfo playerInfo;
    private long startTime = Constants.UNKNOWN;
    private boolean isSetUzTimeBarBottom;
    private UzChromeCast uzChromeCast;
    private boolean isCasting = false;
    private List<Subtitle> subtitleList = new ArrayList<>();
    private boolean autoMoveToLiveEdge;
    @LayoutRes
    private int pipControlSkin;
    private StatsForNerdsView statsForNerdsView;
    private StatsForNerdsHelper statsForNerdsHelper;
    private int countTryLinkPlayError = 0;
    private final int pfLimit = 100;
    private int pfTotalPage = Integer.MAX_VALUE;
    private long maxSeekLastDuration;
    private int progressBarColor = Color.WHITE;
    private long timestampInitDataSource;
    //last current position lúc từ exoplayer switch sang cast player
    private long lastCurrentPosition;
    private int counterInterval;
    private EventTrackingManager eventTrackingManager;

    public UzPlayer(Context context) {
        super(context);
        onCreate();
    }

    public UzPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UzPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UzPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        EventBus.getDefault().register(this);
        startConectifyService();
        initTrackingHelper();
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);
        checkDevices();
        rootView = findViewById(R.id.root_view);
        addPlayerView();
        findViews();
        resizeContainerView();
        updateUiEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        if (UzCommonUtil.isCastDependencyAvailable() && !isTV) {
            setupChromeCast();
        }
        updateUiSizeThumbnail();
        scheduleJob();
    }

    private void initTrackingHelper() {
        eventTrackingManager = new EventTrackingManager(getContext());
    }

    private void checkDevices() {
        isTablet = UzCommonUtil.isTablet(getContext());
        isTV = UzCommonUtil.isTV(getContext());
    }

    private void resizeContainerView() {
        if (!isFreeSize) {
            setSize(getVideoWidth(), getVideoHeight());
        } else {
            setSize(this.getWidth(), this.getHeight());
        }
    }

    private void startConectifyService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(getContext(), UzConectifyService.class);
            getContext().startService(startServiceIntent);
        }
    }

    public void setAutoStart(boolean isAutoStart) {
        this.isAutoStart = isAutoStart;
        TmpParamData.getInstance().setPlayerAutoPlayOn(isAutoStart);
        updateUiDependOnAutoStart();
    }

    public boolean isAutoStart() {
        return isAutoStart;
    }

    public void setAutoSwitchItemPlaylistFolder(boolean isAutoSwitchItemPlaylistFolder) {
        this.isAutoSwitchItemPlaylistFolder = isAutoSwitchItemPlaylistFolder;
    }

    public boolean isAutoSwitchItemPlaylistFolder() {
        return isAutoSwitchItemPlaylistFolder;
    }

    public void setControllerAutoShow(boolean isAutoShowController) {
        this.isAutoShowController = isAutoShowController;
        if (uzPlayerView != null) {
            uzPlayerView.setControllerAutoShow(isAutoShowController);
        }
    }

    public boolean getControllerAutoShow() {
        if (uzPlayerView == null) {
            return false;
        }
        return uzPlayerView.getControllerAutoShow();
    }

    public long getDuration() {
        if (getExoPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getExoPlayer().getDuration();
    }

    /**
     * Returns an estimate of the position in the current content window or ad up to which data is
     * buffered in milliseconds.
     */
    public long getBufferedPosition() {
        if (getExoPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getExoPlayer().getBufferedPosition();
    }

    /**
     * Returns an estimate of the percentage in the current content window or ad up to which data is
     * buffered or 0 if no estimate is available.
     */
    public int getBufferedPercentage() {
        if (getExoPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getExoPlayer().getBufferedPercentage();
    }

    /**
     * Return the duration in milliseconds between current position & buffered position.
     */
    public long getBufferedDuration() {
        if (getExoPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        long bufferedDuration = getBufferedPosition() - getCurrentPosition();
        return bufferedDuration <= 0 ? 0 : bufferedDuration;
    }

    /**
     * Gets pixel for custom ui like youtube, uzTimeBar bottom of player controller.
     */
    public int getPixelAdded() {
        return isSetUzTimeBarBottom ? getUzTimeBarHeight() / 2 : 0;
    }

    /**
     * Gets the video width is rendering on player or 0 if it's not rendering.
     *
     * @return the width of video
     */
    public int getVideoWidth() {
        return uzPlayerManager == null ? 0 : uzPlayerManager.getVideoWidth();
    }

    /**
     * Gets the video height is rendering on player or 0 if it's not rendering.
     *
     * @return the height of video
     */
    public int getVideoHeight() {
        return uzPlayerManager == null ? 0 : uzPlayerManager.getVideoHeight();
    }

    /**
     * Gets the player height,this value is updated whenever view is laid out on your layout.
     *
     * @return the height of video
     */
    public int getPlayerHeight() {
        return uzPlayerView != null ? uzPlayerView.getHeight() : 0;
    }

    /**
     * Gets the player width, this value is updated whenever view is laid out on your layout.
     *
     * @return the width of player
     */
    public int getPlayerWidth() {
        return uzPlayerView != null ? uzPlayerView.getWidth() : 0;
    }

    //return pixel
    public int getUzTimeBarHeight() {
        return UzDisplayUtil.getHeightOfView(uzTimebar);
    }

    /**
     * The current position of playing. the window means playable region, which is all of the
     * content if vod, and some portion of the content if live.
     */
    public long getCurrentPosition() {
        return uzPlayerManager == null ? Constants.NOT_FOUND : uzPlayerManager.getCurrentPosition();
    }

    /**
     * Gets current video format.
     */
    public Format getVideoFormat() {
        return getExoPlayer() == null ? null : getExoPlayer().getVideoFormat();
    }

    /**
     * Gets current audio format.
     */
    public Format getAudioFormat() {
        return getExoPlayer() == null ? null : getExoPlayer().getAudioFormat();
    }

    public int getVideoProfileWidth() {
        return uzPlayerManager == null ? Constants.UNKNOWN : uzPlayerManager.getVideoProfileWidth();
    }

    public int getVideoProfileHeight() {
        return uzPlayerManager == null ? Constants.UNKNOWN
                : uzPlayerManager.getVideoProfileHeight();
    }

    public void setResizeMode(int resizeMode) {
        if (uzPlayerView != null) {
            uzPlayerView.setResizeMode(resizeMode);
        }
    }

    public void setSize(int width, int height) {
        UzPlayerUtil
                .resizeLayout(rootView, ivVideoCover, getPixelAdded(), width, height, isFreeSize);
    }

    public void setFreeSize(boolean isFreeSize) {
        this.isFreeSize = isFreeSize;
        resizeContainerView();
    }

    public void toggleStatsForNerds() {
        if (getExoPlayer() == null || statsForNerdsHelper == null) {
            return;
        }
        statsForNerdsHelper.toggleStatsForNerds();
    }

    public void setPlayerControllerAlwaysVisible() {
        setControllerAutoShow(true);
        setHideControllerOnTouch(false);
        setControllerShowTimeoutMs(0);
        isPlayerControllerAlwaysVisible = true;
    }

    protected void handleError(UzException uzException) {
        if (uzException == null) {
            return;
        }
        notifyError(uzException);
        // Capture by Sentry, in uzException already contains Message, Error Code
        SentryUtil.captureException(uzException.getException());
        addTrackingMuizaError(MuizaEvent.MUIZA_EVENT_ERROR, uzException);
        if (hasError) {
            return;
        }
        hasError = true;
        UzPlayerData.getInstance().setSettingPlayer(false);
    }

    private void notifyError(UzException exception) {
        if (playerEventListener != null) {
            playerEventListener.onPlayerError(exception);
        }
    }

    protected void init(String entityId, boolean isClearDataPlaylistFolder, boolean isLivestream) {
        LLog.d(TAG, "**********NEW SESSION********** entityId: " + entityId);
        this.isLivestream = isLivestream;
        this.uuid = UUID.randomUUID();
        if (isClearDataPlaylistFolder) {
            UzPlayerData.getInstance().clearDataForPlaylistFolder();
        }
        if (entityId == null) {
            LLog.e(TAG, "init error because entityId == null -> called from PIP");
            try {
                if (TextUtils.isEmpty(getVideoData().getId())) {
                    notifyError(UzExceptionUtil.getExceptionEntityId());
                    LLog.e(TAG, "init error: entityId null or empty");
                    return;
                } else {
                    entityId = UzPlayerData.getInstance().getEntityId();
                }
            } catch (NullPointerException e) {
                notifyError(UzExceptionUtil.getExceptionEntityId());
                SentryUtil.captureException(e);
                LLog.e(TAG, "init NullPointerException: " + e.toString());
                return;
            }
        } else {
            timestampBeforeInitNewSession = System.currentTimeMillis();
            timestampOnStartPreview = 0;
            maxSeekLastDuration = 0;
            UzPlayerData.getInstance().clearUzLinkPlayData();
            TmpParamData.getInstance().clear();
            TmpParamData.getInstance().addPlayerViewCount();
            TmpParamData.getInstance().setSessionId(uuid.toString());
        }
        LLog.d(TAG, "isPlayWithPlaylistFolder " + UzPlayerData.getInstance()
                .isPlayWithPlaylistFolder());
        handlePlayPlayListFolderUi();
        isCalledFromChangeSkin = false;
        isInitCustomLinkPlay = false;
        isCalledApiGetDetailEntity = false;
        isCalledApiGetImaAdUrlTag = false;
        isCalledApiGetTokenStreaming = false;
        imaAdsUrl = null;
        streamingToken = null;
        hasError = false;
        isOnPlayerEnded = false;
        this.entityId = entityId;
        LLog.d(TAG, "get entityId: " + entityId);
        UzPlayerData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(defaultValueControllerTimeout);
        updateUiEndScreen();
        if (!UzConnectivityUtil.isConnected(getContext())) {
            notifyError(UzExceptionUtil.getExceptionNoConnection());
            LLog.d(TAG, "!isConnected return");
            return;
        }
        callApiGetDetailEntity();
        callApiGetPlayerInfo();
        callApiGetUrlImaAdTag();
        callApiGetTokenStreaming();
    }

    private void handlePlayPlayListFolderUi() {
        if (isPlayPlaylistFolder()) {
            setVisibilityOfPlaylistFolderController(VISIBLE);
        } else {
            setVisibilityOfPlaylistFolderController(GONE);
        }
    }

    /**
     * init player with vod entity id.
     */
    public void initVodEntity(@NonNull String entityId) {
        init(entityId, true, false);
    }

    /**
     * init player with livestream entity id.
     */
    public void initLiveEntity(@NonNull String entityId) {
        init(entityId, true, true);
    }

    /**
     * init the input (custom) link play.
     *
     * @param videoUrl the video url
     * @param isLivestream true if is livestream video, otherwise false
     */
    public void initCustomLinkPlay(@NonNull String videoUrl, boolean isLivestream) {
        LLog.d(TAG, "*****NEW SESSION*************");
        LLog.d(TAG, "init linkPlay " + videoUrl);
        isInitCustomLinkPlay = true;
        isCalledFromChangeSkin = false;
        setVisibilityOfPlaylistFolderController(GONE);
        isCalledApiGetDetailEntity = false;
        isCalledApiGetImaAdUrlTag = false;
        isCalledApiGetTokenStreaming = false;
        imaAdsUrl = null;
        streamingToken = null;
        this.entityId = null;
        UzPlayerData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(defaultValueControllerTimeout);
        isOnPlayerEnded = false;
        updateUiEndScreen();
        hasError = false;
        this.isLivestream = isLivestream;
        if (isLivestream) {
            startTime = Constants.UNKNOWN;
        }
        eventTrackingManager.resetTracking();
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
            this.linkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        updateUiDependOnLivestream();
        // TODO: Check how to get subtitle of a custom link play,
        //  because we have no idea about entityId or appId
        List<Subtitle> subtitleList = null;

        if (!UzConnectivityUtil.isConnected(getContext())) {
            notifyError(UzExceptionUtil.getExceptionNoConnection());
            return;
        }
        initDataSource(videoUrl, UzPlayerData.getInstance().getImaAdUrl(),
                UzPlayerData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList,
                UzCommonUtil.isAdsDependencyAvailable());
        notifyDataInitialized(false, true, linkPlay, getVideoData());
        initUzPlayerManager();
    }

    public void initPlaylistFolder(String metadataId) {
        if (metadataId == null) {
            LLog.d(TAG,
                    "initPlaylistFolder metadataId null -> called from PIP: " + isGetClickedPip);
        } else {
            LLog.d(TAG, "initPlaylistFolder metadataId " + metadataId + ", -> called from PIP: "
                    + isGetClickedPip);
            UzPlayerData.getInstance().clearDataForPlaylistFolder();
        }
        hasError = false;
        isClickedSkipNextOrSkipPrevious = false;
        callApiGetListAllEntity(metadataId);
    }

    protected void tryNextLinkPlay() {
        if (isLivestream) {
            // try to play 5 times
            if (countTryLinkPlayError >= 5) {
                if (uzLiveInfoListener != null) {
                    uzLiveInfoListener.onLivestreamUnAvailable();
                }
                return;
            }
            LLog.e(TAG, "tryNextLinkPlay isLivestream true -> try to replay = count "
                    + countTryLinkPlayError);
            if (uzPlayerManager != null) {
                uzPlayerManager.initWithoutReset();
                uzPlayerManager.setRunnable();
            }
            countTryLinkPlayError++;
            return;
        }
        countTryLinkPlayError++;
        LLog.e(TAG, getContext().getString(R.string.cannot_play_will_try) + ": "
                + countTryLinkPlayError);
        releaseUzPlayerManager();
        checkToSetUpResource();
    }

    /**
     * if callApiGetLinkPlay return no data, try to play the latest video or notify error.
     */
    private void handleErrorNoData() {
        removeVideoCover(true);
        UzPlayerData.getInstance().setSettingPlayer(false);
        notifyDataInitialized(false, false, null, null);
    }

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    /**
     * Call when activity or fragment going to be destroyed (in onDestroy() callback).
     */
    public void onDestroy() {
        //cannot use isGetClickedPip (global variable), must use getClickedPip(context)
        if (PipHelper.getClickedPip(getContext())) {
            PipHelper.stopMiniPlayer(getContext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getContext().stopService(new Intent(getContext(), UzConectifyService.class));
        }
        releasePlayerAnalytic();
        releaseUzPlayerManager();
        UzPlayerData.getInstance().setSettingPlayer(false);
        UzDialogUtil.hideAllDialog();
        isCasting = false;
        isCastPlayerPlayingFirst = false;
        cdnHost = null;
        EventBus.getDefault().unregister(this);
    }

    private void releasePlayerAnalytic() {
        if (statsForNerdsHelper != null) {
            statsForNerdsHelper.releaseStatsForNerds();
        }
    }

    private void releaseUzPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.release();
        }
    }

    /**
     * Call when activity or fragment is resumed (in onResume() callback).
     */
    public void onResume() {
        SensorChangeNotifier.getInstance(getContext()).addListener(this);
        if (isCasting()) {
            return;
        }
        activityIsPausing = false;
        if (uzPlayerManager != null) {
            if (ibPlayIcon == null || ibPlayIcon.getVisibility() != VISIBLE) {
                uzPlayerManager.resumeVideo();
            }
        }
        // try to move to the edge of livestream video
        if (autoMoveToLiveEdge && isLivestream()) {
            seekToLiveEdge();
        }
    }

    public boolean isPlaying() {
        if (getExoPlayer() == null) {
            return false;
        }
        return getExoPlayer().getPlayWhenReady();
    }

    /**
     * Set auto move the the last window of livestream, default is false.
     *
     * @param autoMoveToLiveEdge true if always seek to last livestream video, otherwise false
     */
    public void setAutoMoveToLiveEdge(boolean autoMoveToLiveEdge) {
        this.autoMoveToLiveEdge = autoMoveToLiveEdge;
    }

    /**
     * Seek to live edge of a streaming video.
     */
    public void seekToLiveEdge() {
        if (isLivestream() && getExoPlayer() != null) {
            getExoPlayer().seekToDefaultPosition();
        }
    }

    /**
     * Call when activity or fragment is going to be paused (in onPause() callback).
     */
    public void onPause() {
        activityIsPausing = true;
        SensorChangeNotifier.getInstance(getContext()).remove(this);
        if (uzPlayerManager != null) {
            uzPlayerManager.pauseVideo();
        }
    }

    /**
     * Call in activity or fragment onActivityResult() callback).
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getContext() == null) {
            return;
        }
        if (isCasting()) {
            LLog.e(TAG, "Error: handleClickPictureInPicture isCasting -> return");
            return;
        }
        if (UzCommonUtil.isCanOverlay(getContext())) {
            initializePiP();
        }
    }

    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        timestampOnStartPreview = System.currentTimeMillis();
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_SEEKING);
        if (uzTimeBarPreviewListener != null) {
            uzTimeBarPreviewListener.onStartPreview(previewView, progress);
        }
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        isOnPreview = true;
        updateUiIbRewIconDependOnProgress(progress, true);
        if (uzTimeBarPreviewListener != null) {
            uzTimeBarPreviewListener.onPreview(previewView, progress, fromUser);
        }
    }

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        if (isCasting()) {
            getCastyPlayer().seek(progress);
        }
        TmpParamData.getInstance().addViewSeekCount();
        long seekLastDuration = System.currentTimeMillis() - timestampOnStartPreview;
        TmpParamData.getInstance().setViewSeekDuration(seekLastDuration);
        if (maxSeekLastDuration < seekLastDuration) {
            maxSeekLastDuration = seekLastDuration;
            TmpParamData.getInstance().setViewMaxSeekTime(maxSeekLastDuration);
        }
        isOnPreview = false;
        onStopPreview(progress);
        if (uzTimeBarPreviewListener != null) {
            uzTimeBarPreviewListener.onStopPreview(previewView, progress);
        }
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_SEEKED);
    }

    public void onStopPreview(int progress) {
        if (uzPlayerManager != null && !isCasting) {
            uzPlayerManager.seekTo(progress);
            uzPlayerManager.resumeVideo();
            isOnPlayerEnded = false;
            updateUiEndScreen();
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (uzPlayerTvListener != null) {
            uzPlayerTvListener.onFocusChange(view, isFocus);
        } else {
            if (firstViewHasFocus == null) {
                firstViewHasFocus = view;
            }
        }
    }

    public boolean isLandscape() {
        return isLandscape;
    }

    @Override
    public void onOrientationChange(int orientation) {
        boolean isDeviceAutoRotation = UzCommonUtil.isRotationPossible(getContext());
        if (orientation == 90 || orientation == 270) {
            if (isDeviceAutoRotation && !isLandscape) {
                UzDisplayUtil.changeScreenLandscape((Activity) getContext(), orientation);
            }
        } else {
            if (isDeviceAutoRotation && isLandscape) {
                UzDisplayUtil.changeScreenPortrait((Activity) getContext());
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getContext() == null) {
            return;
        }
        resizeContainerView();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            UzDisplayUtil.hideDefaultControls(getContext());
            isLandscape = true;
            UzPlayerUtil.setUiFullScreenIcon(ibFullscreenIcon, true);
            UzDisplayUtil.goneViews(ibPictureInPictureIcon);
        } else {
            UzDisplayUtil.showDefaultControls(getContext());
            isLandscape = false;
            UzPlayerUtil.setUiFullScreenIcon(ibFullscreenIcon, false);
            if (!isCasting()) {
                UzDisplayUtil.visibleViews(ibPictureInPictureIcon);
            }
        }
        TmpParamData.getInstance().setPlayerIsFullscreen(isLandscape);
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        updateUiSizeThumbnail();
        updatePositionOfProgressBar();
        if (isSetUzTimeBarBottom) {
            setMarginDependOnUzTimeBar(uzPlayerView.getVideoSurfaceView());
        }
        if (uiEventListener != null) {
            uiEventListener.onPlayerRotated(isLandscape);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            UzAnimationUtil.play(v, Techniques.Pulse);
        } else if (v == ibFullscreenIcon) {
            toggleFullscreen();
        } else if (v == ibBackScreenIcon) {
            handleClickBackScreen();
        } else if (v == ibVolumeIcon) {
            handleClickBtVolume();
        } else if (v == ibSettingIcon) {
            handleClickSetting();
        } else if (v == ibCcIcon) {
            handleClickCC();
        } else if (v == ibPlaylistFolderIcon) {
            handleClickPlaylistFolder();
        } else if (v == ibHearingIcon) {
            handleClickHearing();
        } else if (v == ibPictureInPictureIcon) {
            handleClickPictureInPicture();
        } else if (v.getParent() == debugRootView) {
            showUzTrackSelectionDialog(v, true);
        } else if (v == rlChromeCast) {
            //dangerous to remove
        } else if (v == ibFfwdIcon) {
            handleClickFastForward();
        } else if (v == ibRewIcon) {
            handleClickFastReward();
        } else if (v == ibPauseIcon) {
            pause();
        } else if (v == ibPlayIcon) {
            resume();
        } else if (v == ibReplayIcon) {
            replay();
        } else if (v == ibSkipNextIcon) {
            handleClickSkipNext();
        } else if (v == ibSkipPreviousIcon) {
            handleClickSkipPrevious();
        } else if (v == ibSpeedIcon) {
            showSpeed();
        } else if (v == tvEndScreenMsg) {
            UzAnimationUtil.play(v, Techniques.Pulse);
        } else if (v == ivLogo) {
            UzAnimationUtil.play(v, Techniques.Pulse);
            if (playerInfo.getLogo() == null || playerInfo.getLogo().getUrl() == null) {
                return;
            }
            UzCommonUtil.openUrlInBrowser(getContext(), playerInfo.getLogo().getUrl());
        }
        if (isDefaultUseController) {
            if (rlMsg == null || rlMsg.getVisibility() != VISIBLE) {
                if (isPlayerControllerShowing()) {
                    showController();
                }
            }
        }
        if (uzItemClickListener != null) {
            uzItemClickListener.onItemClick(v);
        }
    }

    private void handleClickFastForward() {
        if (isCasting()) {
            getCastyPlayer().seekToForward(defaultValueBackwardForward);
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.seekToForward(defaultValueBackwardForward);
            }
        }
    }

    private void handleClickFastReward() {
        if (isCasting()) {
            getCastyPlayer().seekToBackward(defaultValueBackwardForward);
        } else {
            if (uzPlayerManager == null) {
                return;
            }
            uzPlayerManager.seekToBackward(defaultValueBackwardForward);
            if (isPlaying()) {
                isOnPlayerEnded = false;
                updateUiEndScreen();
            }
        }
    }

    private void handleClickPictureInPicture() {
        if (getContext() == null) {
            notifyError(UzExceptionUtil.getExceptionShowPip());
            return;
        }
        if (!isInitMiniPlayerSuccess) {
            //dang init 1 instance mini player roi, khong cho init nua
            notifyError(UzExceptionUtil.getExceptionShowPip());
            return;
        }
        if (isCasting()) {
            notifyError(UzExceptionUtil.getExceptionShowPip());
            return;
        }
        if (UzCommonUtil.isCanOverlay(getContext())) {
            initializePiP();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            ((Activity) getContext())
                    .startActivityForResult(intent, Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    public void initializePiP() {
        if (uzPlayerManager == null || uzPlayerManager.getLinkPlay() == null) {
            notifyError(UzExceptionUtil.getExceptionShowPip());
            return;
        }
        UzDisplayUtil.goneViews(ibPictureInPictureIcon);
        if (uiEventListener != null) {
            isInitMiniPlayerSuccess = false;
            uiEventListener.onStateMiniPlayer(false);
        }
        PipHelper.setVideoWidth(getContext(), getVideoWidth());
        PipHelper.setVideoHeight(getContext(), getVideoHeight());
        Intent intent = new Intent(getContext(), UzPipService.class);
        intent.putExtra(Constants.FLOAT_CONTENT_POSITION, getCurrentPosition());
        intent.putExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, isInitCustomLinkPlay);
        intent.putExtra(Constants.FLOAT_UUID, uuid.toString());
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uzPlayerManager.getLinkPlay());
        intent.putExtra(Constants.FLOAT_IS_LIVESTREAM, isLivestream);
        intent.putExtra(Constants.FLOAT_PROGRESS_BAR_COLOR, progressBarColor);
        intent.putExtra(Constants.FLOAT_CONTROL_SKIN_ID, pipControlSkin);
        getContext().startService(intent);
    }

    public SimpleExoPlayer getExoPlayer() {
        if (uzPlayerManager == null) {
            return null;
        }
        return uzPlayerManager.getExoPlayer();
    }

    public void seekTo(long positionMs) {
        if (uzPlayerManager != null) {
            uzPlayerManager.seekTo(positionMs);
        }
    }

    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        defaultValueControllerTimeout = controllerShowTimeoutMs;
        uzPlayerView.setControllerShowTimeoutMs(defaultValueControllerTimeout);
    }

    public int getControllerShowTimeoutMs() {
        if (uzPlayerView != null) {
            return uzPlayerView.getControllerShowTimeoutMs();
        }
        return Constants.NOT_FOUND;
    }

    public boolean isPlayerControllerShowing() {
        if (uzPlayerView != null) {
            return uzPlayerView.isControllerVisible();
        }
        return false;
    }

    public void showController() {
        if (uzPlayerView != null) {
            uzPlayerView.showController();
        }
    }

    public void hideController() {
        if (isPlayerControllerAlwaysVisible || isCasting()) {
            return;
        }
        if (uzPlayerView != null) {
            uzPlayerView.hideController();
        }
    }

    public void setHideControllerOnTouch(boolean isHide) {
        if (uzPlayerView != null) {
            this.isHideOnTouch = isHide;
            uzPlayerView.setControllerHideOnTouch(isHide);
        }
    }

    public boolean getControllerHideOnTouch() {
        if (uzPlayerView != null) {
            return uzPlayerView.getControllerHideOnTouch();
        }
        return false;
    }

    public boolean isDefaultUseController() {
        return isDefaultUseController;
    }

    public void setDefaultUseController(boolean isDefaultUseController) {
        this.isDefaultUseController = isDefaultUseController;
        setUseController(this.isDefaultUseController);
    }

    protected void setUseController(final boolean isUseController) {
        if (uzPlayerView != null) {
            uzPlayerView.setUseController(isUseController);
        }
    }

    private void callApiGetListAllEntity(String metadataId) {
        showProgress();
        if (isPlayPlaylistFolder()) {
            playPlaylistPosition(getPositionInPlaylist());
            hideProgress();
        } else {
            UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
            int pfPage = 0;
            String pfOrderBy = "createdAt";
            String pfOrderType = "DESC";
            String publishToCdn = "success";
            UzApiMaster.getInstance().subscribe(
                    service.getListAllEntity(getApiVersion(), metadataId, pfLimit, pfPage,
                            pfOrderBy, pfOrderType, publishToCdn, getAppId()),
                    new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                        @Override
                        public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                            if (response == null || response.getMetadata() == null
                                    || response.getData().isEmpty()) {
                                handleError(UzExceptionUtil.getExceptionListAllEntity());
                                return;
                            }
                            if (pfTotalPage == Integer.MAX_VALUE) {
                                int totalItem = (int) response.getMetadata().getTotal();
                                float ratio = (float) (totalItem / pfLimit);
                                if (ratio == 0) {
                                    pfTotalPage = (int) ratio;
                                } else if (ratio > 0) {
                                    pfTotalPage = (int) ratio + 1;
                                } else {
                                    pfTotalPage = (int) ratio;
                                }
                            }
                            UzPlayerData.getInstance().setPlaylistData(response.getData());
                            if (!isPlayPlaylistFolder()) {
                                notifyError(UzExceptionUtil.getExceptionListAllEntity());
                                return;
                            }
                            playPlaylistPosition(getPositionInPlaylist());
                            hideProgress();
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "callApiGetListAllEntity onFail " + e.getMessage());
                            notifyError(UzExceptionUtil.getExceptionListAllEntity());
                            hideProgress();
                        }
                    });
        }
    }

    protected boolean isPlayPlaylistFolder() {
        return UzPlayerData.getInstance().getPlaylistData() != null
                && !UzPlayerData.getInstance().getPlaylistData().isEmpty();
    }

    private void playPlaylistPosition(int position) {
        if (!isPlayPlaylistFolder()) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            return;
        }
        LLog.d(TAG, "playPlaylistPosition position: " + position);
        if (position < 0) {
            LLog.e(TAG, "This is the first item");
            notifyError(UzExceptionUtil.getExceptionPlaylistFolderItemFirst());
            return;
        }
        if (position > UzPlayerData.getInstance().getPlaylistData().size() - 1) {
            LLog.e(TAG, "This is the last item");
            notifyError(UzExceptionUtil.getExceptionPlaylistFolderItemLast());
            return;
        }
        urlImgThumbnail = null;
        pause();
        hideController();
        //update UI for skip next and skip previous button
        setSrcDrawableEnabledForViews(ibSkipPreviousIcon, ibSkipNextIcon);
        //set disabled prevent double click, will enable onStateReadyFirst()
        setClickableForViews(false, ibSkipPreviousIcon, ibSkipNextIcon);
        //end update UI for skip next and skip previous button
        UzPlayerData.getInstance().setPositionInPlaylist(position);
        VideoData data = UzPlayerData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position);
        init(data.getId(), false, !TextUtils.isEmpty(data.getLastFeedId()));
    }

    private void setSrcDrawableEnabledForViews(UzImageButton... views) {
        for (UzImageButton v : views) {
            if (v != null && !v.isFocused()) {
                v.setSrcDrawableEnabled();
            }
        }
    }

    private void setClickableForViews(boolean able, View... views) {
        for (View v : views) {
            if (v != null) {
                v.setClickable(able);
                v.setFocusable(able);
            }
        }
    }

    protected void onPlayerEnded() {
        if (!isPlaying()) {
            return;
        }
        isOnPlayerEnded = true;
        if (isPlayPlaylistFolder() && isAutoSwitchItemPlaylistFolder) {
            hideController();
            switchNextVideo();
        } else {
            updateUiEndScreen();
        }
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_VIEWENDED);
    }

    private void switchNextVideo() {
        playPlaylistPosition(getPositionInPlaylist() + 1);
    }

    private void switchPreviousVideo() {
        playPlaylistPosition(getPositionInPlaylist() - 1);
    }

    private void handleClickPlaylistFolder() {
        UzPlaylistDialog uzPlaylistDialog = new UzPlaylistDialog(getContext(), isLandscape,
                UzPlayerData.getInstance().getPlaylistData(), getPositionInPlaylist(),
                new UzPlaylistCallback() {
                    @Override
                    public void onClickItem(VideoData data, int position) {
                        UzPlayerData.getInstance().setPositionInPlaylist(position);
                        playPlaylistPosition(position);
                    }

                    @Override
                    public void onFocusChange(VideoData data, int position) {
                    }

                    @Override
                    public void onDismiss() {
                    }
                });
        UzPlayerUtil.showUizaDialog(getContext(), uzPlaylistDialog);
    }

    private void handleClickSkipNext() {
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUiEndScreen();
        switchNextVideo();
    }

    private void handleClickSkipPrevious() {
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUiEndScreen();
        switchPreviousVideo();
    }

    public boolean replay() {
        if (uzPlayerManager == null) {
            return false;
        }
        TmpParamData.getInstance().addPlayerViewCount();
        //TODO Chỗ này đáng lẽ chỉ clear value của tracking khi đảm bảo rằng seekTo(0) true
        eventTrackingManager.resetTracking();
        boolean result = uzPlayerManager.seekTo(0);
        if (result) {
            isSetFirstRequestFocusDone = false;
            isOnPlayerEnded = false;
            updateUiEndScreen();
            handlePlayPlayListFolderUi();
            if (!isInitCustomLinkPlay) {
                eventTrackingManager.trackUizaEvent(TrackingEvent.E_VIDEO_STARTS);
                eventTrackingManager.trackUizaEvent(TrackingEvent.E_DISPLAY);
                eventTrackingManager.trackUizaEvent(TrackingEvent.E_PLAYS_REQUESTED);
            }
        }
        if (isCasting()) {
            replayChromeCast();
        }
        return result;
    }

    private void replayChromeCast() {
        lastCurrentPosition = 0;
        handleConnectedChromeCast();
        showController();
    }

    private void handleClickBtVolume() {
        if (isCasting()) {
            boolean isMute = UzPlayerData.getInstance().getCasty().toggleMuteVolume();
            if (ibVolumeIcon != null) {
                if (isMute) {
                    ibVolumeIcon.setImageResource(R.drawable.ic_volume_off_black_48dp);
                } else {
                    ibVolumeIcon.setImageResource(R.drawable.baseline_volume_up_white_48);
                }
            }
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.toggleVolumeMute(ibVolumeIcon);
            }
        }
    }

    private void handleClickBackScreen() {
        if (isLandscape()) {
            toggleFullscreen();
        }
    }

    private void handleClickSetting() {
        View view = UzPlayerUtil.getBtVideo(debugRootView);
        UzDisplayUtil.performClick(view);
    }

    private void handleClickCC() {
        if (uzPlayerManager.getSubtitleList() == null
                || uzPlayerManager.getSubtitleList().isEmpty()) {
            UzDialogInfo uzDialogInfo = new UzDialogInfo(getContext(),
                    getContext().getString(R.string.text),
                    getContext().getString(R.string.no_caption));
            UzPlayerUtil.showUizaDialog(getContext(), uzDialogInfo);
        } else {
            View view = UzPlayerUtil.getBtText(debugRootView);
            UzDisplayUtil.performClick(view);
            if (view == null) {
                LLog.e(TAG, "error handleClickCC null");
            }
        }
    }

    private void handleClickHearing() {
        View view = UzPlayerUtil.getBtAudio(debugRootView);
        UzDisplayUtil.performClick(view);
    }

    public void resume() {
        TmpParamData.getInstance().setPlayerIsPaused(false);
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_PLAY);
        if (isCasting()) {
            getCastyPlayer().play();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.resumeVideo();
            }
        }
        UzDisplayUtil.goneViews(ibPlayIcon);
        if (ibPauseIcon != null) {
            UzDisplayUtil.visibleViews(ibPauseIcon);
            ibPauseIcon.requestFocus();
        }
    }

    public void pause() {
        TmpParamData.getInstance().setPlayerIsPaused(true);
        if (isCasting()) {
            getCastyPlayer().pause();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.pauseVideo();
            }
        }
        UzDisplayUtil.goneViews(ibPauseIcon);
        if (ibPlayIcon != null) {
            UzDisplayUtil.visibleViews(ibPlayIcon);
            ibPlayIcon.requestFocus();
        }
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_PAUSE);
    }

    public void setDefaultValueBackwardForward(int mls) {
        defaultValueBackwardForward = mls;
    }

    public int getDefaultValueBackwardForward() {
        return defaultValueBackwardForward;
    }

    public void seekToForward(int mls) {
        setDefaultValueBackwardForward(mls);
        UzDisplayUtil.performClick(ibFfwdIcon);
    }

    public void seekToBackward(int mls) {
        setDefaultValueBackwardForward(mls);
        UzDisplayUtil.performClick(ibRewIcon);
    }

    //chi toggle show hide controller khi video da vao dc onStateReadyFirst();
    public void toggleShowHideController() {
        if (uzPlayerView != null) {
            uzPlayerView.toggleShowHideController();
        }
    }

    public void togglePlayPause() {
        if (uzPlayerManager == null || getExoPlayer() == null) {
            return;
        }
        if (getExoPlayer().getPlayWhenReady()) {
            pause();
        } else {
            resume();
        }
    }

    public void toggleVolume() {
        UzDisplayUtil.performClick(ibVolumeIcon);
    }

    public void toggleFullscreen() {
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_FULLSCREENCHANGE);
        UzDisplayUtil.toggleScreenOrientation((Activity) getContext());
    }

    public void showSubTitlePopup() {
        UzDisplayUtil.performClick(ibCcIcon);
    }

    public void showQualityPopup() {
        UzDisplayUtil.performClick(ibSettingIcon);
    }

    public void showPip() {
        if (isCasting()) {
            LLog.e(TAG, UzException.ERR_19);
            notifyError(UzExceptionUtil.getExceptionShowPip());
        } else {
            // [Re-check]: Why use performClick?
            // UzDisplayUtil.performClick(ibPictureInPictureIcon);
            handleClickPictureInPicture();
        }
    }

    public void setPipControlSkin(@LayoutRes int skinId) {
        this.pipControlSkin = skinId;
    }

    public void showSpeed() {
        if (getExoPlayer() == null) {
            return;
        }
        float currentSpeed = getExoPlayer().getPlaybackParameters().speed;
        final SpeedDialog speedDialog = new SpeedDialog(getContext(), currentSpeed,
                new SpeedDialog.Callback() {
                    @Override
                    public void onSelectItem(SpeedDialog.Speed speed) {
                        if (speed != null) {
                            setPlaybackSpeed(speed.getValue());
                        }
                    }
                });
        UzPlayerUtil.showUizaDialog(getContext(), speedDialog);
    }

    public void nextVideo() {
        handleClickSkipNext();
    }

    public void previousVideo() {
        handleClickSkipPrevious();
    }

    public PlayerView getUzPlayerView() {
        return uzPlayerView;
    }

    protected TextView getDebugTextView() {
        return debugTextView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    public boolean isLivestream() {
        return isLivestream;
    }

    public boolean isTablet() {
        return isTablet;
    }

    public UzPlayerManagerAbs getUzPlayerManager() {
        return uzPlayerManager;
    }

    public TextView getTvMsg() {
        return tvMsg;
    }

    public ImageView getIvVideoCover() {
        return ivVideoCover;
    }

    public UzImageButton getIbFullscreenIcon() {
        return ibFullscreenIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public UzImageButton getIbPauseIcon() {
        return ibPauseIcon;
    }

    public UzImageButton getIbPlayIcon() {
        return ibPlayIcon;
    }

    public UzImageButton getIbReplayIcon() {
        return ibReplayIcon;
    }

    public UzImageButton getIbRewIcon() {
        return ibRewIcon;
    }

    public UzImageButton getIbFfwdIcon() {
        return ibFfwdIcon;
    }

    public UzImageButton getIbBackScreenIcon() {
        return ibBackScreenIcon;
    }

    public UzImageButton getIbVolumeIcon() {
        return ibVolumeIcon;
    }

    public UzImageButton getIbSettingIcon() {
        return ibSettingIcon;
    }

    public UzImageButton getIbCcIcon() {
        return ibCcIcon;
    }

    public UzImageButton getIbPlaylistFolderIcon() {
        return ibPlaylistFolderIcon;
    }

    public UzImageButton getIbHearingIcon() {
        return ibHearingIcon;
    }

    public UzImageButton getIbPictureInPictureIcon() {
        return ibPictureInPictureIcon;
    }

    public UzImageButton getIbSkipPreviousIcon() {
        return ibSkipPreviousIcon;
    }

    public UzImageButton getIbSkipNextIcon() {
        return ibSkipNextIcon;
    }

    public UzImageButton getIbSpeedIcon() {
        return ibSpeedIcon;
    }

    public RelativeLayout getRlLiveInfo() {
        return rlLiveInfo;
    }

    public TextView getTvLiveView() {
        return tvLiveView;
    }

    public TextView getTvLiveTime() {
        return tvLiveTime;
    }

    public RelativeLayout getRlChromeCast() {
        return rlChromeCast;
    }

    public UzImageButton getIbsCast() {
        return ibsCast;
    }

    public String getEntityId() {
        return entityId;
    }

    public UzTextView getTvPosition() {
        return tvPosition;
    }

    public UzTextView getTvDuration() {
        return tvDuration;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public UzImageButton getIvLiveTime() {
        return ivLiveTime;
    }

    public UzImageButton getIvLiveView() {
        return ivLiveView;
    }

    public RelativeLayout getRlEndScreen() {
        return rlEndScreen;
    }

    public TextView getTvEndScreenMsg() {
        return tvEndScreenMsg;
    }

    public UzTimeBar getUzTimeBar() {
        return uzTimebar;
    }

    public LinearLayout getLlTop() {
        return llTop;
    }

    public View getBkg() {
        return bkg;
    }

    public List<UzTrackItem> getQualityList() {
        View view = UzPlayerUtil.getBtVideo(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error getQualityList null");
            notifyError(UzExceptionUtil.getExceptionListHQ());
            return null;
        }
        return showUzTrackSelectionDialog(view, false);
    }

    public List<UzTrackItem> getAudioList() {
        View view = UzPlayerUtil.getBtAudio(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error audio null");
            notifyError(UzExceptionUtil.getExceptionListAudio());
            return null;
        }
        return showUzTrackSelectionDialog(view, false);
    }

    public void setVolume(float volume) {
        if (uzPlayerManager == null) {
            return;
        }
        uzPlayerManager.setVolume(volume);
    }

    public float getVolume() {
        if (uzPlayerManager == null) {
            return Constants.NOT_FOUND;
        }
        return uzPlayerManager.getVolume();
    }

    @Override
    public void onVisibilityChanged(boolean isShow) {
        if (ivLogo != null) {
            ivLogo.setClickable(!isShow);
        }
        if (controllerStateCallback != null) {
            controllerStateCallback.onVisibilityChanged(isShow);
        }
    }

    public void setPlaybackSpeed(float speed) {
        if (getContext() == null) {
            return;
        }
        if (isLivestream) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.error_speed_live_content));
        }
        if (speed > 2 || speed < 0) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.error_speed_illegal));
        }
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        if (getExoPlayer() != null) {
            getExoPlayer().setPlaybackParameters(playbackParameters);
        }
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_RATECHANGE);
    }

    public float getPlaybackSpeed() {
        if (getExoPlayer() == null) {
            return 1.f;
        }
        return getExoPlayer().getPlaybackParameters().speed;
    }

    private void findViews() {
        bkg = findViewById(R.id.bkg);
        rlMsg = findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = findViewById(R.id.tv_msg);
        if (tvMsg != null) {
            UzDisplayUtil.setTextShadow(tvMsg);
        }
        ivVideoCover = findViewById(R.id.iv_cover);
        llTop = findViewById(R.id.ll_top);
        progressBar = findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(progressBar, progressBarColor);
        updatePositionOfProgressBar();
        uzPlayerView.setControllerStateCallback(this);
        uzTimebar = uzPlayerView.findViewById(R.id.exo_progress);
        previewFrameLayout = uzPlayerView.findViewById(R.id.preview_frame_layout);
        if (uzTimebar != null) {
            if (uzTimebar.getTag() == null) {
                isSetUzTimeBarBottom = false;
                UzDisplayUtil.visibleViews(uzPlayerView);
            } else {
                if (uzTimebar.getTag().toString()
                        .equals(getContext().getString(R.string.use_bottom_uz_timebar))) {
                    isSetUzTimeBarBottom = true;
                    setMarginDependOnUzTimeBar(uzPlayerView.getVideoSurfaceView());
                } else {
                    isSetUzTimeBarBottom = false;
                    UzDisplayUtil.visibleViews(uzPlayerView);
                }
            }
            uzTimebar.addOnPreviewChangeListener(this);
            uzTimebar.setOnFocusChangeListener(this);
        } else {
            UzDisplayUtil.visibleViews(uzPlayerView);
        }
        ivThumbnail = uzPlayerView.findViewById(R.id.image_view_thumnail);
        tvPosition = uzPlayerView.findViewById(R.id.uz_position);
        if (tvPosition != null) {
            tvPosition.setText(UzDateTimeUtil.convertMillisecondsToHMmSs(0));
        }
        tvDuration = uzPlayerView.findViewById(R.id.uz_duration);
        if (tvDuration != null) {
            tvDuration.setText("-:-");
        }
        ibFullscreenIcon = uzPlayerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = uzPlayerView.findViewById(R.id.tv_title);
        ibPauseIcon = uzPlayerView.findViewById(R.id.exo_pause_uiza);
        ibPlayIcon = uzPlayerView.findViewById(R.id.exo_play_uiza);
        //If auto start true, show button play and gone button pause
        UzDisplayUtil.goneViews(ibPlayIcon);
        ibReplayIcon = uzPlayerView.findViewById(R.id.exo_replay_uiza);
        ibRewIcon = uzPlayerView.findViewById(R.id.exo_rew);
        if (ibRewIcon != null) {
            ibRewIcon.setSrcDrawableDisabled();
        }
        ibFfwdIcon = uzPlayerView.findViewById(R.id.exo_ffwd);
        ibBackScreenIcon = uzPlayerView.findViewById(R.id.exo_back_screen);
        ibVolumeIcon = uzPlayerView.findViewById(R.id.exo_volume);
        ibSettingIcon = uzPlayerView.findViewById(R.id.exo_setting);
        ibCcIcon = uzPlayerView.findViewById(R.id.exo_cc);
        ibPlaylistFolderIcon = uzPlayerView.findViewById(R.id.exo_playlist_folder);
        ibHearingIcon = uzPlayerView.findViewById(R.id.exo_hearing);
        ibPictureInPictureIcon = uzPlayerView.findViewById(R.id.exo_picture_in_picture);
        ibSkipNextIcon = uzPlayerView.findViewById(R.id.exo_skip_next);
        ibSkipPreviousIcon = uzPlayerView.findViewById(R.id.exo_skip_previous);
        ibSpeedIcon = uzPlayerView.findViewById(R.id.exo_speed);
        LinearLayout debugLayout = findViewById(R.id.debug_layout);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);
        if (Constants.IS_DEBUG) {
            debugLayout.setVisibility(VISIBLE);
        } else {
            debugLayout.setVisibility(GONE);
            debugTextView = null;
        }
        rlLiveInfo = uzPlayerView.findViewById(R.id.rl_live_info);
        tvLiveStatus = uzPlayerView.findViewById(R.id.tv_live);
        tvLiveView = uzPlayerView.findViewById(R.id.tv_live_view);
        tvLiveTime = uzPlayerView.findViewById(R.id.tv_live_time);
        ivLiveView = uzPlayerView.findViewById(R.id.iv_live_view);
        ivLiveTime = uzPlayerView.findViewById(R.id.iv_live_time);
        UzDisplayUtil.setFocusableViews(false, ivLiveView, ivLiveTime);
        rlEndScreen = uzPlayerView.findViewById(R.id.rl_end_screen);
        UzDisplayUtil.goneViews(rlEndScreen);
        tvEndScreenMsg = uzPlayerView.findViewById(R.id.tv_end_screen_msg);
        if (tvEndScreenMsg != null) {
            UzDisplayUtil.setTextShadow(tvEndScreenMsg, Color.WHITE);
            tvEndScreenMsg.setOnClickListener(this);
        }
        setEventForViews();
        //set visibility first, so scared if removed
        setVisibilityOfPlaylistFolderController(GONE);

        statsForNerdsView = findViewById(R.id.stats_for_nerds);
    }

    private void setEventForViews() {
        setClickAndFocusEventForViews(ibFullscreenIcon, ibBackScreenIcon, ibVolumeIcon,
                ibSettingIcon, ibCcIcon, ibPlaylistFolderIcon, ibHearingIcon,
                ibPictureInPictureIcon, ibFfwdIcon, ibRewIcon, ibPlayIcon, ibPauseIcon,
                ibReplayIcon, ibSkipNextIcon, ibSkipPreviousIcon, ibSpeedIcon);
    }

    private void setClickAndFocusEventForViews(View... views) {
        for (View v : views) {
            if (v != null) {
                v.setOnClickListener(this);
                v.setOnFocusChangeListener(this);
            }
        }
    }

    //If auto start true, show button play and gone button pause
    //if not, gone button play and show button pause
    private void updateUiDependOnAutoStart() {
        if (isAutoStart()) {
            UzDisplayUtil.goneViews(ibPlayIcon);
            if (ibPauseIcon != null) {
                UzDisplayUtil.visibleViews(ibPauseIcon);
                if (!isSetFirstRequestFocusDone) {
                    ibPauseIcon.requestFocus();//set first request focus if using player for TV
                    isSetFirstRequestFocusDone = true;
                }
            }
        } else {
            if (isPlaying()) {
                UzDisplayUtil.goneViews(ibPlayIcon);
                if (ibPauseIcon != null) {
                    UzDisplayUtil.visibleViews(ibPauseIcon);
                    if (!isSetFirstRequestFocusDone) {
                        ibPauseIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
            } else {
                if (ibPlayIcon != null) {
                    UzDisplayUtil.visibleViews(ibPlayIcon);
                    if (!isSetFirstRequestFocusDone) {
                        ibPlayIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
                UzDisplayUtil.goneViews(ibPauseIcon);
            }
        }
    }

    public void setUrlImgThumbnail(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        this.urlImgThumbnail = url;
        if (ivVideoCover == null) {
            return;
        }
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
            UzImageUtil.load(getContext(), url, ivVideoCover, R.drawable.background_black);
        }
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            resetCountTryLinkPlayError();
            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            String urlCover;
            if (urlImgThumbnail == null || urlImgThumbnail.isEmpty()) {
                if (data == null) {
                    urlCover = Constants.URL_IMG_THUMBNAIL_BLACK;
                } else {
                    urlCover = data.getThumbnail();
                }
            } else {
                urlCover = urlImgThumbnail;
            }
            TmpParamData.getInstance().setEntityPosterUrl(urlCover);
            UzImageUtil.load(getContext(), urlCover, ivVideoCover, R.drawable.background_black);
        }
    }

    protected void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            ivVideoCover.setVisibility(GONE);
            ivVideoCover.invalidate();
            if (isLivestream) {
                if (tvLiveTime != null) {
                    tvLiveTime.setText(HYPHEN);
                }
                if (tvLiveView != null) {
                    tvLiveView.setText(HYPHEN);
                }
                callApiUpdateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
                callApiUpdateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
            }
            if (!isFromHandleError) {
                onStateReadyFirst();
            }
        } else {
            //goi change skin realtime thi no ko vao if nen ko update tvDuration dc
            updateTvDuration();
        }
    }

    private void updateUiEachSkin() {
        int currentPlayerId = UzPlayerData.getInstance().getCurrentSkinRes();
        if (currentPlayerId == R.layout.uz_player_skin_2
                || currentPlayerId == R.layout.uz_player_skin_3) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setRatioLand(7);
                ibPlayIcon.setRatioPort(5);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setRatioLand(7);
                ibPauseIcon.setRatioPort(5);
            }
            if (ibReplayIcon != null) {
                ibReplayIcon.setRatioLand(7);
                ibReplayIcon.setRatioPort(5);
            }
        }
    }

    private void updatePositionOfProgressBar() {
        if (uzPlayerView == null || progressBar == null) {
            return;
        }
        uzPlayerView.post(new Runnable() {
            @Override
            public void run() {
                int marginL =
                        uzPlayerView.getMeasuredWidth() / 2 - progressBar.getMeasuredWidth() / 2;
                int marginT =
                        uzPlayerView.getMeasuredHeight() / 2 - progressBar.getMeasuredHeight() / 2;
                UzDisplayUtil.setMarginPx(progressBar, marginL, marginT, 0, 0);
            }
        });
    }

    private void addPlayerView() {
        uzPlayerView = null;
        int skinId = UzPlayerData.getInstance().getCurrentSkinRes();
        uzPlayerView = (UzPlayerView) ((Activity) getContext()).getLayoutInflater()
                .inflate(skinId, null);
        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        uzPlayerView.setVisibility(GONE);
        rootView.addView(uzPlayerView);
        setControllerAutoShow(isAutoShowController);
    }

    /**
     * change skin of player (realtime) return true if success.
     *
     * @param skinId the layout id of skin
     */
    public boolean changeSkin(int skinId) {
        if (getContext() == null || uzPlayerManager == null) {
            return false;
        }
        if (UzPlayerData.getInstance().isUseDraggableLayout()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.error_change_skin_with_vdhview));
        }
        if (uzPlayerManager.isPlayingAd()) {
            notifyError(UzExceptionUtil.getExceptionChangeSkin());
            return false;
        }
        UzPlayerConfig.setCurrentSkinRes(skinId);
        isRefreshFromChangeSkin = true;
        isCalledFromChangeSkin = true;
        rootView.removeView(uzPlayerView);
        rootView.requestLayout();
        uzPlayerView = (UzPlayerView) ((Activity) getContext()).getLayoutInflater()
                .inflate(skinId, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        rootView.addView(uzPlayerView);
        rootView.requestLayout();
        findViews();
        resizeContainerView();
        updateUiEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        if (UzCommonUtil.isCastDependencyAvailable() && !isTV) {
            setupChromeCast();
        }
        currentPositionBeforeChangeSkin = getCurrentPosition();
        releaseUzPlayerManager();
        updateUiDependOnLivestream();
        setTitle();
        checkToSetUpResource();
        updateUiSizeThumbnail();
        if (uiEventListener != null) {
            uiEventListener.onSkinChanged();
        }
        return true;
    }

    private void setupChromeCast() {
        uzChromeCast = new UzChromeCast();
        uzChromeCast.setUzChromeCastListener(new CastListener() {
            @Override
            public void onConnected() {
                if (uzPlayerManager != null) {
                    lastCurrentPosition = getCurrentPosition();
                }
                handleConnectedChromeCast();
            }

            @Override
            public void onDisconnected() {
                handleDisconnectedChromeCast();
            }

            @Override
            public void addUiChromeCast() {
                if (llTop != null) {
                    llTop.addView(uzChromeCast.getUzMediaRouteButton());
                }
                addUiChromeCastLayer();
            }
        });
        uzChromeCast.setupChromeCast(getContext());
    }

    private void updateTvDuration() {
        if (tvDuration == null) {
            return;
        }
        if (isLivestream) {
            tvDuration.setText(UzDateTimeUtil.convertMillisecondsToHMmSs(0));
        } else {
            tvDuration.setText(UzDateTimeUtil.convertMillisecondsToHMmSs(getDuration()));
        }
    }

    public void setSeekBarProgress(final UzVerticalSeekBar seekBar, final int progress) {
        if (seekBar == null) {
            return;
        }
        seekBar.setProgress(progress);
    }

    private void setTextPosition(long currentMls) {
        if (tvPosition == null) {
            return;
        }
        if (isLivestream) {
            long duration = getDuration();
            long past = duration - currentMls;
            tvPosition.setText(HYPHEN + UzDateTimeUtil.convertMillisecondsToHMmSs(past));
        } else {
            tvPosition.setText(UzDateTimeUtil.convertMillisecondsToHMmSs(currentMls));
        }
    }

    private void updateUiIbRewIconDependOnProgress(long currentMls,
            boolean isCalledFromUzTimeBarEvent) {
        if (isCalledFromUzTimeBarEvent) {
            setTextPosition(currentMls);
        } else {
            if (isOnPreview) {
                return;
            } else {
                setTextPosition(currentMls);
            }
        }
        if (isLivestream) {
            return;
        }
        if (ibRewIcon == null || ibFfwdIcon == null) {
            return;
        }
        if (currentMls == 0) {
            if (ibRewIcon.isSetSrcDrawableEnabled()) {
                ibRewIcon.setSrcDrawableDisabled();
            }
            if (!ibFfwdIcon.isSetSrcDrawableEnabled()) {
                ibFfwdIcon.setSrcDrawableEnabled();
            }
        } else if (currentMls == getDuration()) {
            if (!ibRewIcon.isSetSrcDrawableEnabled()) {
                ibRewIcon.setSrcDrawableEnabled();
            }
            if (ibFfwdIcon.isSetSrcDrawableEnabled()) {
                ibFfwdIcon.setSrcDrawableDisabled();
            }
        } else {
            if (!ibRewIcon.isSetSrcDrawableEnabled()) {
                ibRewIcon.setSrcDrawableEnabled();
            }
            if (!ibFfwdIcon.isSetSrcDrawableEnabled()) {
                ibFfwdIcon.setSrcDrawableEnabled();
            }
        }
    }

    //FOR TV
    public void updateUiFocusChanged(View view, boolean isFocus) {
        if (view == null) {
            return;
        }
        if (isFocus) {
            if (view instanceof UzImageButton) {
                UzPlayerConfig.updateUiFocusChange(view, true, R.drawable.bkg_tv_has_focus,
                        R.drawable.bkg_tv_no_focus);
                ((UzImageButton) view).setColorFilter(Color.GRAY);
            } else if (view instanceof Button) {
                UzPlayerConfig.updateUiFocusChange(view, true, R.drawable.bkg_tv_has_focus,
                        R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UzTimeBar) {
                UzPlayerConfig
                        .updateUiFocusChange(view, true, R.drawable.bkg_tv_has_focus_uz_timebar,
                                R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        } else {
            if (view instanceof UzImageButton) {
                UzPlayerConfig.updateUiFocusChange(view, false, R.drawable.bkg_tv_has_focus,
                        R.drawable.bkg_tv_no_focus);
                ((UzImageButton) view).clearColorFilter();
            } else if (view instanceof Button) {
                UzPlayerConfig.updateUiFocusChange(view, false, R.drawable.bkg_tv_has_focus,
                        R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UzTimeBar) {
                UzPlayerConfig
                        .updateUiFocusChange(view, false, R.drawable.bkg_tv_has_focus_uz_timebar,
                                R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        }
    }

    private void handleFirstViewHasFocus() {
        if (firstViewHasFocus != null && uzPlayerTvListener != null) {
            uzPlayerTvListener.onFocusChange(firstViewHasFocus, true);
            firstViewHasFocus = null;
        }
    }

    private void updateUiSizeThumbnail() {
        int screenWidth = UzDisplayUtil.getScreenWidth();
        int widthIv = isLandscape ? screenWidth / 4 : screenWidth / 5;
        if (previewFrameLayout != null) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = widthIv;
            layoutParams.height = (int) (widthIv * Constants.RATIO_9_16);
            previewFrameLayout.setLayoutParams(layoutParams);
            previewFrameLayout.requestLayout();
        }
    }

    private void setMarginPreviewTimeBar() {
        if (isLandscape) {
            UzDisplayUtil.setMarginDimen(uzTimebar, 5, 0, 5, 0);
        } else {
            UzDisplayUtil.setMarginDimen(uzTimebar, 0, 0, 0, 0);
        }
    }

    private void setMarginRlLiveInfo() {
        if (isLandscape) {
            UzDisplayUtil.setMarginDimen(rlLiveInfo, 50, 0, 50, 0);
        } else {
            UzDisplayUtil.setMarginDimen(rlLiveInfo, 5, 0, 5, 0);
        }
    }

    private void setTitle() {
        if (tvTitle != null) {
            tvTitle.setText(UzPlayerData.getInstance().getEntityName());
        }
    }

    private void updateUiDependOnLivestream() {
        if (isCasting()) {
            UzDisplayUtil.goneViews(ibPictureInPictureIcon);
        } else {
            if (isTablet && isTV) {
                //only hide ibPictureInPictureIcon if device is TV
                UzDisplayUtil.goneViews(ibPictureInPictureIcon);
            }
        }
        if (isLivestream) {
            UzDisplayUtil.visibleViews(rlLiveInfo);
            setUiVisible(false, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        } else {
            UzDisplayUtil.goneViews(rlLiveInfo);
            setUiVisible(true, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        }
        if (isTV) {
            UzDisplayUtil.goneViews(ibFullscreenIcon);
        }
    }

    private void setUiVisible(boolean visible, UzImageButton... views) {
        for (UzImageButton v : views) {
            if (v != null) {
                v.setUiVisible(visible);
            }
        }
    }

    protected void updateUiButtonVisibilities() {
        if (debugRootView != null) {
            debugRootView.removeAllViews();
        }
        if (getExoPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector()
                .getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if (getContext() == null) {
                    return;
                }
                Button button = new Button(getContext());
                button.setSoundEffectsEnabled(false);
                int label;
                switch (uzPlayerManager.getExoPlayer().getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }
                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(this);
                if (debugRootView != null) {
                    debugRootView.addView(button);
                }
            }
        }
    }

    public void showLayoutMsg() {
        hideController();
        UzDisplayUtil.visibleViews(rlMsg);
    }

    public void hideLayoutMsg() {
        UzDisplayUtil.goneViews(rlMsg);
    }

    private void updateLiveInfoTimeStartLive() {
        if (!isLivestream() || getContext() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        long duration = now - startTime;
        String s = UzDateTimeUtil.convertMillisecondsToHMmSs(duration);
        if (tvLiveTime != null) {
            tvLiveTime.setText(s);
        }
        if (uzLiveInfoListener != null) {
            uzLiveInfoListener.onStartTimeUpdate(duration, s);
        }
        callApiUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
    }

    private void updateUiEndScreen() {
        if (getContext() == null) {
            return;
        }
        if (isOnPlayerEnded) {
            LLog.i(TAG, "Video or Stream is ended !");
            setVisibilityOfPlayPauseReplay(true);
            showController();
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
                uzPlayerView.setControllerHideOnTouch(false);
            }
        } else {
            setVisibilityOfPlayPauseReplay(false);
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(defaultValueControllerTimeout);
            }
            setHideControllerOnTouch(isHideOnTouch);
        }
    }

    private void setVisibilityOfPlayPauseReplay(boolean isShowReplay) {
        if (isShowReplay) {
            UzDisplayUtil.goneViews(ibPlayIcon, ibPauseIcon);
            if (ibReplayIcon != null) {
                UzDisplayUtil.visibleViews(ibReplayIcon);
                ibReplayIcon.requestFocus();
            }
        } else {
            updateUiDependOnAutoStart();
            UzDisplayUtil.goneViews(ibReplayIcon);
        }
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
        UzDisplayUtil.setVisibilityViews(visibilityOfPlaylistFolderController, ibPlaylistFolderIcon,
                ibSkipNextIcon, ibSkipPreviousIcon);
        setVisibilityOfPlayPauseReplay(false);
    }

    public void hideUzTimeBar() {
        UzDisplayUtil.goneViews(previewFrameLayout, ivThumbnail, uzTimebar);
    }

    private List<UzTrackItem> showUzTrackSelectionDialog(final View view, boolean showDialog) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector()
                .getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = ((Button) view).getText();
            int rendererIndex = (int) view.getTag();
            final Pair<AlertDialog, UzTrackSelectionView> dialogPair = UzTrackSelectionView
                    .getDialog(getContext(), title, uzPlayerManager.getTrackSelector(),
                            rendererIndex);
            dialogPair.second.setShowDisableOption(false);
            dialogPair.second.setAllowAdaptiveSelections(false);
            dialogPair.second.setCallback(new UzTrackSelectionView.Callback() {
                @Override
                public void onClick() {
                    UzCommonUtil.actionWithDelayed(300, new DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            if (dialogPair == null || dialogPair.first == null) {
                                return;
                            }
                            dialogPair.first.cancel();
                        }
                    });
                }
            });
            if (showDialog) {
                UzPlayerUtil.showUizaDialog(getContext(), dialogPair.first);
            }
            return dialogPair.second.getUzTracks();
        }
        return null;
    }

    public void setUzTimeBarBottom() {
        if (uzPlayerView == null) {
            throw new NullPointerException("uzPlayerView cannot be null");
        }
        if (uzTimebar == null) {
            throw new NullPointerException("uzTimeBar cannot be null");
        }
        if (uzPlayerView.getResizeMode() != AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT) {
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        }
    }

    public int getUzPlayerHeight() {
        if (rootView == null) {
            return 0;
        }
        if (isSetUzTimeBarBottom) {
            int heightRootView = UzDisplayUtil.getHeightOfView(rootView);
            int heightUzTimeBar = getUzTimeBarHeight();
            return heightRootView - heightUzTimeBar / 2;
        } else {
            return UzDisplayUtil.getHeightOfView(rootView);
        }
    }

    public void setBackgroundColorBkg(int color) {
        if (bkg != null) {
            bkg.setBackgroundColor(color);
        }
    }

    public void setBackgroundColorUzPlayerRootView(int color) {
        RelativeLayout rootView = findViewById(R.id.root_view_uz_video);
        if (rootView != null) {
            rootView.setBackgroundColor(color);
        }
    }

    public void setMarginDependOnUzTimeBar(View view) {
        if (view == null || uzTimebar == null) {
            return;
        }
        if (isLandscape) {
            UzDisplayUtil.setMarginPx(view, 0, 0, 0, 0);
        } else {
            UzDisplayUtil.setMarginPx(view, 0, 0, 0, getUzTimeBarHeight() / 2);
        }
    }

    public void setProgressBarColor(int progressBarColor) {
        if (progressBar != null) {
            this.progressBarColor = progressBarColor;
            UzDisplayUtil.setColorProgressBar(progressBar, progressBarColor);
        }
    }

    public void hideProgress() {
        if (uzPlayerManager != null) {
            uzPlayerManager.hideProgress();
        }
    }

    public void showProgress() {
        if (uzPlayerManager != null) {
            uzPlayerManager.showProgress();
        }
    }

    private void updateUiPlayerInfo() {
        if (playerInfo == null || uzPlayerView == null) {
            return;
        }
        Logo logo = playerInfo.getLogo();
        if (logo == null) {
            return;
        }
        boolean isDisplay = logo.isDisplay();
        if (isDisplay) {
            String position = logo.getPosition();
            ivLogo = new ImageView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    UzConvertUtils.dp2px(50f), ViewGroup.LayoutParams.WRAP_CONTENT);
            if (position.equalsIgnoreCase("top-left")) {
                layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            } else if (position.equalsIgnoreCase("top-right")) {
                layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
            } else if (position.equalsIgnoreCase("bottom-left")) {
                layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            } else if (position.equalsIgnoreCase("bottom-right")) {
                layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            } else {
                layoutParams = null;
                ivLogo = null;
                return;
            }
            uzPlayerView.getOverlayFrameLayout().addView(ivLogo, layoutParams);
            ivLogo.setOnClickListener(this);
            UzImageUtil.load(getContext(), logo.getLogo(), ivLogo);
        } else {
            ivLogo = null;
        }
    }

    private UzLiveInfoListener uzLiveInfoListener;
    private UzTimeBarPreviewListener uzTimeBarPreviewListener;
    private UzItemClickListener uzItemClickListener;
    private UzPlayerEventListener playerEventListener;
    private UzPlayerUiEventListener uiEventListener;
    private UzAdEventListener adEventListener;
    private UzPlayerTvListener uzPlayerTvListener;
    private UzPlayerView.ControllerStateCallback controllerStateCallback;
    protected AudioListener audioListener;
    protected MetadataOutput metadataOutput;
    protected Player.EventListener eventListener;
    protected VideoListener videoListener;
    protected TextOutput textOutput;

    public void setUzLiveInfoListener(UzLiveInfoListener uzLiveInfoListener) {
        this.uzLiveInfoListener = uzLiveInfoListener;
    }

    public void setUzPlayerTvListener(UzPlayerTvListener uzPlayerTvListener) {
        this.uzPlayerTvListener = uzPlayerTvListener;
        handleFirstViewHasFocus();
    }

    public void setUzPlayerEventListener(UzPlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public void setUzAdEventListener(UzAdEventListener adEventListener) {
        this.adEventListener = adEventListener;
    }

    public void setUzPlayerUiEventListener(UzPlayerUiEventListener uiEventListener) {
        this.uiEventListener = uiEventListener;
    }

    public void setUzTimeBarPreviewListener(UzTimeBarPreviewListener uzTimeBarPreviewListener) {
        this.uzTimeBarPreviewListener = uzTimeBarPreviewListener;
    }

    public void setUzItemClickListener(UzItemClickListener uzItemClickListener) {
        this.uzItemClickListener = uzItemClickListener;
    }

    public void setControllerStateCallback(
            final UzPlayerView.ControllerStateCallback controllerStateCallback) {
        this.controllerStateCallback = controllerStateCallback;
    }

    public void setOnTouchEvent(UzPlayerView.OnTouchEvent onTouchEvent) {
        if (uzPlayerView != null) {
            uzPlayerView.setOnTouchEvent(onTouchEvent);
        }
    }

    public void addAudioListener(AudioListener audioListener) {
        this.audioListener = audioListener;
    }

    public void addPlayerEventListener(Player.EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void addVideoListener(VideoListener videoListener) {
        this.videoListener = videoListener;
    }

    public void addMetadataOutput(MetadataOutput metadataOutput) {
        this.metadataOutput = metadataOutput;
    }

    public void addTextOutput(TextOutput textOutput) {
        this.textOutput = textOutput;
    }

    private void callApiGetDetailEntity() {
        boolean isDataExist = getVideoData() != null;
        if (isDataExist) {
            isCalledApiGetDetailEntity = true;
            data = getVideoData();
            handleDataCallApi();
        } else {
            if (isLivestream) {
                UzUtilBase.getDataFromEntityIdLive(getContext(), getApiVersion(), getAppId(),
                        entityId,
                        new CallbackGetDetailEntity() {
                            @Override
                            public void onSuccess(VideoData data) {
                                handleDetailEntityResponse(data);
                            }

                            @Override
                            public void onError(Throwable e) {
                                UzPlayerData.getInstance().setSettingPlayer(false);
                                handleError(UzExceptionUtil.getExceptionCannotGetDetailEntity());
                            }
                        });
            } else {
                UzUtilBase.getDetailEntity(getContext(), getApiVersion(), entityId, getAppId(),
                        new CallbackGetDetailEntity() {
                            @Override
                            public void onSuccess(VideoData data) {
                                handleDetailEntityResponse(data);
                            }

                            @Override
                            public void onError(Throwable e) {
                                UzPlayerData.getInstance().setSettingPlayer(false);
                                handleError(UzExceptionUtil.getExceptionCannotGetDetailEntity());
                            }
                        });
            }
        }
    }

    private void handleDetailEntityResponse(VideoData data) {
        isCalledApiGetDetailEntity = true;
        this.data = data;
        //set video cover o moi case, ngoai tru
        //click tu pip entity thi ko can show video cover
        //click tu pip playlist folder lan dau tien thi ko can show video cover,
        // neu nhan skip next hoac skip prev thi se show video cover
        if (isPlayPlaylistFolder()) {
            if (isGetClickedPip) {
                if (isClickedSkipNextOrSkipPrevious) {
                    setVideoCover();
                }
            } else {
                setVideoCover();
            }
        } else {
            setVideoCover();
        }
        handleDataCallApi();
    }

    private void callApiGetPlayerInfo() {
        int resLayout = UzPlayerData.getInstance().getCurrentSkinRes();
        if (resLayout != R.layout.uz_player_skin_0 && resLayout != R.layout.uz_player_skin_1
                && resLayout != R.layout.uz_player_skin_2
                && resLayout != R.layout.uz_player_skin_3) {
            return;
        }
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String playerInfoId = UzPlayerData.getInstance().getPlayerInfoId();
        if (TextUtils.isEmpty(playerInfoId)) {
            return;
        }
        UzApiMaster.getInstance().subscribe(
                service.getPlayerInfo(getApiVersion(), playerInfoId,
                        getAppId()),
                new ApiSubscriber<BaseResponse<PlayerInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<PlayerInfo> response) {
                        playerInfo = response.getData();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        handleError(UzExceptionUtil.getExceptionPlayerInfo());
                    }
                });
    }

    private void callApiGetUrlImaAdTag() {
        boolean hasImaAdUrl = UzPlayerData.getInstance().getImaAdUrl() != null;
        if (hasImaAdUrl) {
            isCalledApiGetImaAdUrlTag = true;
            imaAdsUrl = UzPlayerData.getInstance().getImaAdUrl();
            handleDataCallApi();
        } else {
            UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
            UzApiMaster.getInstance().subscribe(
                    service.getCuePoint(getApiVersion(), entityId,
                            getAppId()),
                    new ApiSubscriber<BasePaginationResponse<List<Ad>>>() {
                        @Override
                        public void onSuccess(BasePaginationResponse<List<Ad>> response) {
                            isCalledApiGetImaAdUrlTag = true;
                            List<Ad> result = response.getData();
                            if (result == null || result.isEmpty()) {
                                imaAdsUrl = "";
                            } else {
                                //Hien tai chi co the play ima ad o item thu 0
                                Ad ad = result.get(0);
                                if (ad != null) {
                                    imaAdsUrl = ad.getLink();
                                }
                            }
                            handleDataCallApi();
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "callApiGetUrlImaAdTag onFail but ignored (dont care): "
                                    + e.getMessage());
                        }
                    });
        }
    }

    private void callApiGetTokenStreaming() {
        boolean isGetTokenStreamingExist = UzPlayerData.getInstance().getStreamingToken() != null;
        if (isGetTokenStreamingExist) {
            streamingToken = UzPlayerData.getInstance().getStreamingToken();
            isCalledApiGetTokenStreaming = true;
            handleDataCallApi();
        } else {
            UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
            StreamingTokenRequest streamingTokenRequest = new StreamingTokenRequest();
            streamingTokenRequest.setAppId(getAppId());
            streamingTokenRequest.setEntityId(entityId);
            streamingTokenRequest.setContentType(StreamingTokenRequest.STREAM);
            UzApiMaster.getInstance()
                    .subscribe(service.getTokenStreaming(getApiVersion(), streamingTokenRequest),
                            new ApiSubscriber<BaseResponse<StreamingToken>>() {
                                @Override
                                public void onSuccess(BaseResponse<StreamingToken> response) {
                                    if (response == null || response.getData() == null
                                            || response.getData().getToken() == null
                                            || response.getData().getToken().isEmpty()) {
                                        handleError(UzExceptionUtil.getExceptionNoTokenStreaming());
                                        return;
                                    }
                                    streamingToken = response.getData();
                                    isCalledApiGetTokenStreaming = true;
                                    handleDataCallApi();
                                }

                                @Override
                                public void onFail(Throwable e) {
                                    LLog.e(TAG,
                                            "callApiGetTokenStreaming onFail " + e.getMessage());
                                    handleError(UzExceptionUtil.getExceptionNoTokenStreaming());
                                }
                            });
        }
    }

    private void callApiGetLinkPlay() {
        if (streamingToken == null || streamingToken.getToken() == null) {
            handleError(UzExceptionUtil.getExceptionNoTokenStreaming());
            return;
        }
        boolean isResultGetLinkPlayExist = UzPlayerData.getInstance().getLinkPlay() != null;
        if (isResultGetLinkPlayExist) {
            linkPlay = UzPlayerData.getInstance().getLinkPlay();
            try {
                cdnHost = linkPlay.getCdn().get(0).getHost();
            } catch (NullPointerException e) {
                LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
                SentryUtil.captureException(e);
            }
            checkToSetUpResource();
        } else {
            String tokenStreaming = streamingToken.getToken();
            UzRestClientGetLinkPlay.addAuthorization(tokenStreaming);
            UzServiceApi service = UzRestClientGetLinkPlay.createService(UzServiceApi.class);
            if (isLivestream()) {
                String channelName = UzPlayerData.getInstance().getChannelName();
                UzApiMaster.getInstance()
                        .subscribe(service.getLinkPlayLive(getAppId(), channelName),
                                new ApiSubscriber<BaseResponse<LinkPlay>>() {
                                    @Override
                                    public void onSuccess(BaseResponse<LinkPlay> response) {
                                        if (response != null) {
                                            handleLinkPlayResponse(response.getData());
                                        }
                                        checkToSetUpResource();
                                    }

                                    @Override
                                    public void onFail(Throwable e) {
                                        LLog.e(TAG,
                                                "getLinkPlayLive LIVE onFail " + e.getMessage());
                                        handleError(UzExceptionUtil
                                                .getExceptionCannotGetLinkPlayLive());
                                    }
                                });
            } else {
                String typeContent = StreamingTokenRequest.STREAM;
                UzApiMaster.getInstance()
                        .subscribe(service.getLinkPlay(getAppId(), entityId, typeContent),
                                new ApiSubscriber<BaseResponse<LinkPlay>>() {
                                    @Override
                                    public void onSuccess(BaseResponse<LinkPlay> response) {
                                        handleLinkPlayResponse(response.getData());
                                        callApiGetSubtitles();
                                    }

                                    @Override
                                    public void onFail(Throwable e) {
                                        LLog.e(TAG,
                                                "callApiGetLinkPlay VOD onFail " + e.getMessage());
                                        handleError(
                                                UzExceptionUtil.getExceptionCannotGetLinkPlayVOD());
                                    }
                                });
            }
        }
    }

    private void handleLinkPlayResponse(LinkPlay linkPlay) {
        this.linkPlay = linkPlay;
        UzPlayerData.getInstance().setLinkPlay(linkPlay);
        try {
            cdnHost = linkPlay.getCdn().get(0).getHost();
            TmpParamData.getInstance().setEntityCnd(cdnHost);
            TmpParamData.getInstance().setEntitySourceDomain(cdnHost);
            TmpParamData.getInstance().setEntitySourceHostname(cdnHost);
        } catch (NullPointerException e) {
            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
            SentryUtil.captureException(e);
        }
    }

    private void callApiGetSubtitles() {
        if (linkPlay == null) {
            return;
        }

        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.getSubtitles(getApiVersion(),
                        linkPlay.getEntityId(), linkPlay.getAppId()),
                        new ApiSubscriber<BaseResponse<List<Subtitle>>>() {
                            @Override
                            public void onSuccess(BaseResponse<List<Subtitle>> response) {
                                subtitleList.clear();
                                subtitleList.addAll(response.getData());
                                checkToSetUpResource();
                            }

                            @Override
                            public void onFail(Throwable e) {
                                checkToSetUpResource();
                            }
                        });
    }

    private void callApiUpdateLiveInfoCurrentView(final int durationDelay) {
        if (!isLivestream() || getContext() == null || activityIsPausing) {
            return;
        }
        UzCommonUtil.actionWithDelayed(durationDelay, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                if (!isLivestream()) {
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (
                        uzPlayerView.isControllerVisible()
                                || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
                    String id = UzPlayerData.getInstance().getEntityId();

                    UzApiMaster.getInstance()
                            .subscribe(service.getViewALiveFeed(getApiVersion(), id, getAppId()),
                                    new ApiSubscriber<BaseResponse<LiveFeedViews>>() {
                                        @Override
                                        public void onSuccess(
                                                BaseResponse<LiveFeedViews> response) {
                                            if (response != null && response.getData() != null) {
                                                if (tvLiveView != null) {
                                                    tvLiveView.setText(String.valueOf(
                                                            response.getData().getWatchnow()));
                                                }
                                                if (uzLiveInfoListener != null) {
                                                    uzLiveInfoListener
                                                            .onCurrentViewUpdate(
                                                                    response.getData()
                                                                            .getWatchnow());
                                                }
                                            }
                                            callApiUpdateLiveInfoCurrentView(
                                                    DELAY_TO_GET_LIVE_INFORMATION);
                                        }

                                        @Override
                                        public void onFail(Throwable e) {
                                            LLog.e(TAG,
                                                    "getViewALiveFeed onFail " + e.getMessage());
                                            callApiUpdateLiveInfoCurrentView(
                                                    DELAY_TO_GET_LIVE_INFORMATION);
                                        }
                                    });
                } else {
                    callApiUpdateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    private void callApiUpdateLiveInfoTimeStartLive(final int durationDelay) {
        if (!isLivestream() || getContext() == null || activityIsPausing) {
            return;
        }
        UzCommonUtil.actionWithDelayed(durationDelay, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                if (!isLivestream() || getContext() == null || activityIsPausing) {
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (
                        uzPlayerView.isControllerVisible()
                                || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    if (startTime == Constants.UNKNOWN) {
                        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
                        String entityId = UzPlayerData.getInstance().getEntityId();
                        String feedId = UzPlayerData.getInstance().getLastFeedId();
                        UzApiMaster.getInstance().subscribe(
                                service.getTimeStartLive(getApiVersion(), entityId, feedId,
                                        getAppId()),
                                new ApiSubscriber<BaseResponse<VideoData>>() {
                                    @Override
                                    public void onSuccess(BaseResponse<VideoData> response) {
                                        if (response != null && response.getData() != null
                                                && response.getData().getStartTime() != null) {
                                            startTime = UzDateTimeUtil.convertDateToTimeStamp(
                                                    response.getData().getStartTime(),
                                                    UzDateTimeUtil.FORMAT_1);
                                            updateLiveInfoTimeStartLive();
                                        }
                                    }

                                    @Override
                                    public void onFail(Throwable e) {
                                        LLog.e(TAG, "getTimeStartLive onFail " + e.getMessage());
                                        callApiUpdateLiveInfoTimeStartLive(
                                                DELAY_TO_GET_LIVE_INFORMATION);
                                    }
                                });
                    } else {
                        updateLiveInfoTimeStartLive();
                    }
                } else {
                    callApiUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    private void handleDataCallApi() {
        if (!isCalledApiGetDetailEntity || !isCalledApiGetImaAdUrlTag
                || !isCalledApiGetTokenStreaming) {
            return;
        }
        if (UzPlayerData.getInstance().getUzLinkPlayData() == null) {
            // Note: No linkplay in builder here
            UzLinkPlayData uzLinkPlayData = new UzLinkPlayData.Builder()
                    .data(data)
                    .imaAdUrl(imaAdsUrl)
                    .seekbarPreviewThumbUrl(null) //TODO url thumbnail seekbar
                    .streamingToken(streamingToken)
                    .build();
            UzPlayerData.getInstance().setUzLinkPlayData(uzLinkPlayData);
        }
        checkData();
    }

    private void checkData() {
        UzPlayerData.getInstance().setSettingPlayer(true);
        hasError = false;
        if (TextUtils.isEmpty(UzPlayerData.getInstance().getEntityId())) {
            LLog.e(TAG, "checkData getEntityId null or empty -> return");
            handleError(UzExceptionUtil.getExceptionEntityId());
            UzPlayerData.getInstance().setSettingPlayer(false);
            return;
        }
        isLivestream = UzPlayerData.getInstance().isLivestream();
        isGetClickedPip = PipHelper.getClickedPip(getContext());
        LLog.d(TAG,
                "checkData isLivestream " + isLivestream + ", isGetClickedPip: " + isGetClickedPip);
        if (!isGetClickedPip) {
            eventTrackingManager.resetTracking();
        }
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
            linkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        setTitle();
        callApiGetLinkPlay();
        if (!isInitCustomLinkPlay) {
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_DISPLAY);
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_PLAYS_REQUESTED);
        }
    }

    private void checkToSetUpResource() {
        if (UzPlayerData.getInstance().getLinkPlay() != null && getVideoData() != null) {
            List<String> listLinkPlay = new ArrayList<>();
            List<Url> urlList = linkPlay.getUrls();
            if (isLivestream()) {
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
                handleErrorNoData();
                return;
            }
            if (countTryLinkPlayError >= listLinkPlay.size()) {
                if (UzConnectivityUtil.isConnected(getContext())) {
                    handleError(UzExceptionUtil.getExceptionTryAllLinkPlay());
                } else {
                    notifyError(UzExceptionUtil.getExceptionNoConnection());
                }
                return;
            }
            String videoUrl = listLinkPlay.get(countTryLinkPlayError);

            addTrackingMuiza(MuizaEvent.MUIZA_EVENT_READY);
            if (isCalledFromChangeSkin) {
                //if called from func changeSkin(), dont initDataSource with uilIMA Ad.
                initDataSource(videoUrl, null,
                        UzPlayerData.getInstance().getUrlThumnailsPreviewSeekbar(),
                        subtitleList, UzCommonUtil.isAdsDependencyAvailable());
            } else {
                initDataSource(videoUrl, UzPlayerData.getInstance().getImaAdUrl(),
                        UzPlayerData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList,
                        UzCommonUtil.isAdsDependencyAvailable());
            }
            notifyDataInitialized(false, true, linkPlay, getVideoData());
            initUzPlayerManager();
        } else {
            handleError(UzExceptionUtil.getExceptionSetup());
        }
    }

    private void notifyDataInitialized(boolean initSuccess, boolean getDataSuccess,
            LinkPlay linkPlay, VideoData data) {
        if (playerEventListener != null) {
            playerEventListener.onDataInitialized(initSuccess, getDataSuccess, linkPlay, data);
        }
    }

    private void initDataSource(String linkPlay, String imaAdUrl,
            String urlThumbnailsPreviewSeekbar, List<Subtitle> subtitleList, boolean includeAds) {

        if (subtitleList == null || subtitleList.isEmpty()) {
            UzDisplayUtil.goneViews(ibCcIcon);
        }

        timestampInitDataSource = System.currentTimeMillis();
        LLog.d(TAG, "-------------------->initDataSource linkPlay " + linkPlay);
        TmpParamData.getInstance().setEntitySourceUrl(linkPlay);
        TmpParamData.getInstance().setTimeFromInitEntityIdToAllApiCalledSuccess(
                System.currentTimeMillis() - timestampBeforeInitNewSession);
        if (includeAds) {
            uzPlayerManager = new UzPlayerManager(this, linkPlay, imaAdUrl,
                    urlThumbnailsPreviewSeekbar, subtitleList);
            ((UzPlayerManager) uzPlayerManager).addAdPlayerCallback(new UzAdPlayerCallback() {
                @Override
                public void onPlay() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onPlay();
                    }
                }

                @Override
                public void onVolumeChanged(int level) {
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onVolumeChanged(level);
                    }
                }

                @Override
                public void onPause() {
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onPause();
                    }
                }

                @Override
                public void onLoaded() {
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onLoaded();
                    }
                }

                @Override
                public void onResume() {
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onResume();
                    }
                }

                @Override
                public void onEnded() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onEnded();
                    }
                }

                @Override
                public void onError() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onError();
                    }
                }

                @Override
                public void onBuffering() {
                    if (videoAdPlayerCallback != null) {
                        videoAdPlayerCallback.onBuffering();
                    }
                }
            });
        } else {
            uzPlayerManager = new UzNoAdsPlayerManager(this, linkPlay, urlThumbnailsPreviewSeekbar,
                    subtitleList);
        }
        if (uzTimebar != null) {
            if (urlThumbnailsPreviewSeekbar == null || urlThumbnailsPreviewSeekbar.isEmpty()) {
                uzTimebar.setPreviewEnabled(false);
            } else {
                uzTimebar.setPreviewEnabled(true);
            }
            uzTimebar.setPreviewLoader(uzPlayerManager);
        }
        uzPlayerManager.setPlayerEventListener(new UzPlayerEventListener() {
            @Override
            public void onDataInitialized(boolean initSuccess, boolean getDataSuccess,
                    LinkPlay linkPlay, VideoData data) {
                if (playerEventListener != null) {
                    playerEventListener
                            .onDataInitialized(initSuccess, getDataSuccess, linkPlay, data);
                }
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                TmpParamData.getInstance().setPlayerPlayHeadTime(s);
                updateUiIbRewIconDependOnProgress(currentMls, false);
                if (playerEventListener != null) {
                    playerEventListener.onVideoProgress(currentMls, s, duration, percent);
                }
                if (isInitCustomLinkPlay) {
                    return;
                }
                trackingData(s, percent);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playerEventListener != null) {
                    playerEventListener.onPlayerStateChanged(playWhenReady, playbackState);
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

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage,
                    long duration) {
                if (playerEventListener != null) {
                    playerEventListener
                            .onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }
        });

        uzPlayerManager.setAdEventListener(new UzAdEventListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                if (adEventListener != null) {
                    adEventListener.onAdProgress(s, duration, percent);
                }
            }

            @Override
            public void onAdEnded() {
                setDefaultUseController(isDefaultUseController());
                if (adEventListener != null) {
                    adEventListener.onAdEnded();
                }
            }
        });

        uzPlayerManager.setBufferChangedListener(new UzPlayerBufferChangedListener() {
            @Override
            public void onBufferChanged(long bufferedDurationUs, float playbackSpeed) {
                if (statsForNerdsHelper != null) {
                    statsForNerdsHelper.setBufferedDurationUs(bufferedDurationUs);
                }
            }
        });

        uzPlayerManager.setDebugCallback(new DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                updateUiButtonVisibilities();
            }
        });
    }

    protected void onStateReadyFirst() {
        long pageLoadTime = System.currentTimeMillis() - timestampBeforeInitNewSession;
        TmpParamData.getInstance().setPageLoadTime(pageLoadTime);
        TmpParamData.getInstance().setViewStart(System.currentTimeMillis());
        TmpParamData.getInstance().setViewTimeToFirstFrame(System.currentTimeMillis());
        addTrackingMuiza(MuizaEvent.MUIZA_EVENT_VIEWSTART);
        updateTvDuration();
        updateUiDependOnAutoStart();
        updateUiDependOnLivestream();
        if (isSetUzTimeBarBottom) {
            UzDisplayUtil.visibleViews(uzPlayerView);
        }
        resizeContainerView();
        //enable from playPlaylistPosition() prevent double click
        setClickableForViews(true, ibSkipPreviousIcon, ibSkipNextIcon);
        if (isGetClickedPip) {
            LLog.d(TAG, "getClickedPip true -> setPlayWhenReady true");
            uzPlayerManager.getExoPlayer().setPlayWhenReady(true);
        }
        notifyDataInitialized(true, true, linkPlay, getVideoData());
        if (isCasting) {
            replayChromeCast();
        }
        updateUiPlayerInfo();
        TmpParamData.getInstance().setSessionStart(System.currentTimeMillis());
        long playerStartUpTime = System.currentTimeMillis() - timestampInitDataSource;
        TmpParamData.getInstance().setPlayerStartupTime(playerStartUpTime);
        trackUizaCcuForLivestream();
        pingHeartBeat();
        UzPlayerData.getInstance().setSettingPlayer(false);
        if (!isInitCustomLinkPlay) {
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_VIDEO_STARTS);
        }
    }

    private void initUzPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.init();
            addTrackingMuiza(MuizaEvent.MUIZA_EVENT_LOADSTART);
            if (isGetClickedPip && !isPlayPlaylistFolder()) {
                uzPlayerManager.getExoPlayer().setPlayWhenReady(false);
            } else {
                if (isRefreshFromChangeSkin) {
                    uzPlayerManager.seekTo(currentPositionBeforeChangeSkin);
                    isRefreshFromChangeSkin = false;
                    currentPositionBeforeChangeSkin = 0;
                }
            }
            if (isCalledFromConnectionEventBus) {
                uzPlayerManager.setRunnable();
                isCalledFromConnectionEventBus = false;
            }
            // Always using this options
            initStatsForNerds();
        }
    }

    private void trackingData(long s, int percent) {
        // track progress, VOD only
        if (!isLivestream() && oldPercent != percent) {
            oldPercent = percent;
            eventTrackingManager.trackProgress(percent);
        }

        counterInterval++;
        // track event view
        if (counterInterval == (isLivestream() ? 3 : 5)) {
            eventTrackingManager.trackUizaEvent(TrackingEvent.E_VIEW);
        }

        // track analytic
        if ((s > 0 && counterInterval % 10 == 0) && !isTrackingMuiza
                && !UzPlayerData.getInstance().isMuizaListEmpty()) {
            trackMuizaData();
        }
    }

    private void pingHeartBeat() {
        if (getContext() == null || cdnHost == null || cdnHost.isEmpty()) {
            LLog.e(TAG, "Error cannot call API pingHeartBeat() -> cdnHost: " + cdnHost
                    + ", entityName: " + UzPlayerData.getInstance().getEntityName() + " -> return");
            return;
        }
        if (activityIsPausing) {
            rePingHeartBeat();
            LLog.e(TAG, "Error cannot call API pingHeartBeat() because activity is pausing "
                    + UzPlayerData.getInstance().getEntityName());
            return;
        }
        UzServiceApi service = UzRestClientHeartBeat.createService(UzServiceApi.class);
        String cdnName = cdnHost;
        String session = uuid.toString();
        UzApiMaster.getInstance()
                .subscribe(service.pingHeartBeat(cdnName, session), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
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
        if (getContext() == null || !isLivestream() || isInitCustomLinkPlay) {
            LLog.e(TAG, "Error cannot trackUizaCcuForLivestream() -> return "
                    + UzPlayerData.getInstance().getEntityName());
            return;
        }
        if (activityIsPausing) {
            reTrackUizaCcuForLivestream();
            LLog.e(TAG, "Error cannot trackUizaCcuForLivestream() because activity is pausing "
                    + UzPlayerData.getInstance().getEntityName());
            return;
        }
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.trackCCU(ccuTrackingRequest()), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        reTrackUizaCcuForLivestream();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        reTrackUizaCcuForLivestream();
                    }
                });
    }

    private UizaTrackingCCU ccuTrackingRequest() {
        UizaTrackingCCU.Builder ccuBuilder = new UizaTrackingCCU.Builder();
        ccuBuilder.dt(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1))
                .ho(cdnHost)
                .ai(getAppId())
                .sn(UzPlayerData.getInstance().getChannelName())
                .di(UzOsUtil.getDeviceId(getContext())).ua(Constants.USER_AGENT);
        return ccuBuilder.build();
    }

    private void reTrackUizaCcuForLivestream() {
        UzCommonUtil.actionWithDelayed(INTERVAL_TRACK_CCU, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                trackUizaCcuForLivestream();
            }
        });
    }

    protected void addTrackingMuizaError(String event, UzException e) {
        if (!isInitCustomLinkPlay) {
            UzPlayerData.getInstance().addTrackingMuiza(getContext(), event, e);
        }
    }

    protected void addTrackingMuiza(@MuizaEvent String event) {
        if (!isInitCustomLinkPlay) {
            UzPlayerData.getInstance().addTrackingMuiza(getContext(), event);
        }
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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UzConnectivityEvent event) {
        if (event == null) {
            return;
        }
        if (!event.isConnected()) {
            notifyError(UzExceptionUtil.getExceptionNoConnection());
        } else {
            if (uzPlayerManager == null) {
                return;
            }
            UzDialogUtil.hideAllDialog();
            if (uzPlayerManager.getExoPlaybackException() == null) {
                hideController();
                hideLayoutMsg();
            } else {
                isCalledFromConnectionEventBus = true;
                uzPlayerManager.setResumeIfConnectionError();
                if (!activityIsPausing) {
                    uzPlayerManager.init();
                    if (isCalledFromConnectionEventBus) {
                        uzPlayerManager.setRunnable();
                        isCalledFromConnectionEventBus = false;
                    }
                }
            }
            resume();
        }
    }

    public boolean isInitNewItem(String urlImgThumbnail) {
        if (positionMiniPlayer != 0) {
            seekTo(positionMiniPlayer);
            resume();
            sendEventInitSuccess();
            positionMiniPlayer = 0;
            return false;
        } else {
            setUrlImgThumbnail(urlImgThumbnail);
            pause();
            showProgress();
            positionMiniPlayer = 0;
            return true;
        }
    }

    //listen msg from service UzPipService
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMsg.MsgFromService msg) {
        if (msg == null || uzPlayerManager == null) {
            return;
        }
        //click open app of mini player
        if (msg instanceof ComunicateMsg.MsgFromServiceOpenApp) {
            if (getContext() == null) {
                return;
            }
            LLog.d(TAG, "miniplayer STEP 6");
            try {
                positionMiniPlayer = ((ComunicateMsg.MsgFromServiceOpenApp) msg)
                        .getPositionMiniPlayer();
                Class classNamePfPlayer = Class
                        .forName(((Activity) getContext()).getClass().getName());
                Intent intent = new Intent(getContext(), classNamePfPlayer);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID,
                        UzPlayerData.getInstance().getEntityId());
                getContext().startActivity(intent);
            } catch (ClassNotFoundException e) {
                LLog.e(TAG, "onMessageEvent open app ClassNotFoundException " + e.toString());
                SentryUtil.captureException(e);
            }
            return;
        }
        //when pip float view init success
        if (uiEventListener != null && msg instanceof ComunicateMsg.MsgFromServiceIsInitSuccess) {
            //Ham nay duoc goi khi player o UzPipService da init xong
            //Nhiem vu la minh se gui vi tri hien tai sang cho UzPipService no biet
            LLog.d(TAG, "miniplayer STEP 3 UzPlayer biet UzPipService da init xong"
                    + " -> gui lai content position cua UzPlayer cho UzPipService");
            ComunicateMsg.MsgFromActivityPosition msgFromActivityPosition =
                    new ComunicateMsg.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(getCurrentPosition());
            ComunicateMsg.postFromActivity(msgFromActivityPosition);
            isInitMiniPlayerSuccess = true;
            uiEventListener.onStateMiniPlayer(
                    ((ComunicateMsg.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        }
    }

    public void sendEventInitSuccess() {
        ComunicateMsg.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMsg.MsgFromActivityIsInitSuccess(
                null);
        msgFromActivityIsInitSuccess.setInitSuccess(true);
        ComunicateMsg.postFromActivity(msgFromActivityIsInitSuccess);
    }

    private void handleConnectedChromeCast() {
        isCasting = true;
        isCastPlayerPlayingFirst = false;
        playChromeCast();
        updateUiChromeCast();
    }

    private void handleDisconnectedChromeCast() {
        isCasting = false;
        isCastPlayerPlayingFirst = false;
        updateUiChromeCast();
    }

    public boolean isCasting() {
        return isCasting;
    }

    private void playChromeCast() {
        if (getVideoData() == null || uzPlayerManager == null
                || uzPlayerManager.getExoPlayer() == null) {
            return;
        }
        showProgress();
        VideoData data = getVideoData();
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, data.getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, data.getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(data.getThumbnail())));

        // NOTE: The receiver app (on TV) should Satisfy CORS requirements
        // https://developers.google.com/cast/docs/android_sender/media_tracks#satisfy_cors_requirements
        List<MediaTrack> mediaTrackList = buildMediaTracks();
        long duration = getDuration();
        if (duration < 0) {
            LLog.e(TAG, "invalid duration -> cannot play chromecast");
            return;
        }

        MediaInfo mediaInfo = new MediaInfo.Builder(uzPlayerManager.getLinkPlay())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .setMediaTracks(mediaTrackList)
                .setStreamDuration(duration)
                .build();

        getCastyPlayer()
                .loadMediaAndPlayInBackground(mediaInfo, true, lastCurrentPosition);

        getCastyPlayer().getRemoteMediaClient()
                .addProgressListener(new RemoteMediaClient.ProgressListener() {
                    @Override
                    public void onProgressUpdated(long currentPosition, long duration) {
                        if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                            hideProgress();
                            isCastPlayerPlayingFirst = true;
                        }

                        if (currentPosition > 0) {
                            uzPlayerManager.seekTo(currentPosition);
                        }
                    }
                }, 1000);

    }

    private List<MediaTrack> buildMediaTracks() {
        List<MediaTrack> result = new ArrayList<>();
        if (subtitleList == null || subtitleList.isEmpty()) {
            return result;
        }
        for (int i = 0; i < subtitleList.size(); i++) {
            Subtitle subtitle = subtitleList.get(i);
            if (subtitle.getStatus() == Subtitle.Status.DISABLE) {
                continue;
            }
            MediaTrack subtitleTrack = new MediaTrack.Builder(i + 1001/* ID is unique */,
                    MediaTrack.TYPE_TEXT)
                    .setName(subtitle.getName())
                    .setContentType("text/vtt")
                    .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                    .setContentId(subtitle.getUrl())
                    .setLanguage(subtitle.getLanguage())
                    .build();
            // Re-order default subtitle track
            if (subtitle.getIsDefault() == 1) {
                result.add(0, subtitleTrack);
            } else {
                result.add(subtitleTrack);
            }
        }
        return result;
    }

    /* khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnect thì local player sẽ resume*/
    private void updateUiChromeCast() {
        if (uzPlayerManager == null || rlChromeCast == null || isTV) {
            return;
        }
        if (isCasting) {
            uzPlayerManager.pauseVideo();
            uzPlayerManager.setVolume(0f);
            UzDisplayUtil.visibleViews(rlChromeCast, ibPlayIcon);
            UzDisplayUtil.goneViews(ibPauseIcon);
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
            }
        } else {
            uzPlayerManager.resumeVideo();
            uzPlayerManager.setVolume(0.99f);
            UzDisplayUtil.goneViews(rlChromeCast, ibPlayIcon);
            UzDisplayUtil.visibleViews(ibPauseIcon);
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(defaultValueControllerTimeout);
            }
        }
    }

    private void scheduleJob() {
        if (getContext() == null) {
            return;
        }
        JobInfo myJob;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myJob = new JobInfo.Builder(0,
                    new ComponentName(getContext(), UzConectifyService.class))
                    .setRequiresCharging(true)
                    .setMinimumLatency(1000)
                    .setOverrideDeadline(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) getContext()
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(myJob);
            }
        }
    }

    private void initStatsForNerds() {
        if (statsForNerdsHelper == null) {
            statsForNerdsHelper = new StatsForNerdsHelper(this, statsForNerdsView);
        }
        statsForNerdsHelper.initStatsForNerds();
    }

    public UzChromeCast getUzChromeCast() {
        return uzChromeCast;
    }

    private void addUiChromeCastLayer() {
        rlChromeCast = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlChromeCastParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        rlChromeCast.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Black));
        ibsCast = new UzImageButton(getContext());
        ibsCast.setBackgroundColor(Color.TRANSPARENT);
        ibsCast.setImageResource(R.drawable.cast);
        RelativeLayout.LayoutParams ibsCastParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ibsCastParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ibsCast.setLayoutParams(ibsCastParams);
        ibsCast.setRatioPort(5);
        ibsCast.setRatioLand(5);
        ibsCast.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ibsCast.setColorFilter(Color.WHITE);
        rlChromeCast.addView(ibsCast);
        rlChromeCast.setOnClickListener(this);
        if (llTop != null) {
            if (llTop.getParent() instanceof ViewGroup) {
                ((RelativeLayout) llTop.getParent()).addView(rlChromeCast, 0);
            }
        } else if (rlLiveInfo != null) {
            if (rlLiveInfo.getParent() instanceof ViewGroup) {
                ((RelativeLayout) rlLiveInfo.getParent()).addView(rlChromeCast, 0);
            }
        }
    }

    public void addVideoAdPlayerCallback(UzAdPlayerCallback uzAdPlayerCallback) {
        if (UzCommonUtil.isAdsDependencyAvailable()) {
            this.videoAdPlayerCallback = uzAdPlayerCallback;
        } else {
            throw new NoClassDefFoundError(UzException.ERR_506);
        }
    }

    protected void updateLiveStreamLatency(long latency) {
        if (statsForNerdsHelper != null) {
            statsForNerdsHelper.updateLiveStreamLatency(latency);
        }
    }

    protected void hideTextLiveStreamLatency() {
        if (statsForNerdsHelper != null) {
            statsForNerdsHelper.hideTextLiveStreamLatency();
        }
    }

    private CastyPlayer getCastyPlayer() {
        return UzPlayerData.getInstance().getCasty().getPlayer();
    }

    private String getApiVersion() {
        return UzPlayerData.getInstance().getApiVersion();
    }

    private String getAppId() {
        return UzPlayerData.getInstance().getAppId();
    }

    private VideoData getVideoData() {
        return UzPlayerData.getInstance().getVideoData();
    }

    private int getPositionInPlaylist() {
        return UzPlayerData.getInstance().getPositionInPlaylist();
    }
}

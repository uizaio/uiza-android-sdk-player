package uizacoresdk.view;

import android.annotation.TargetApi;
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
import android.os.Handler;
import android.provider.Settings;
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

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.daimajia.androidanimations.library.Techniques;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import timber.log.Timber;
import uizacoresdk.BuildConfig;
import uizacoresdk.R;
import uizacoresdk.animations.AnimationUtils;
import uizacoresdk.dialog.hq.UizaItem;
import uizacoresdk.dialog.hq.UizaTrackSelectionView;
import uizacoresdk.dialog.info.UZDlgInfoV1;
import uizacoresdk.dialog.playlistfolder.CallbackPlaylistFolder;
import uizacoresdk.dialog.playlistfolder.UizaPlaylistFolderDialog;
import uizacoresdk.dialog.speed.UizaSpeedDialog;
import uizacoresdk.floatview.FloatUizaVideoService;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZLiveContentCallback;
import uizacoresdk.interfaces.UizaAdPlayerCallback;
import uizacoresdk.interfaces.UizaVideoViewItemClick;
import uizacoresdk.listerner.UizaTVFocusChangeListener;
import uizacoresdk.util.SensorOrientationChangeNotifier;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UizaTrackingUtil;
import uizacoresdk.util.UizaUtil;
import uizacoresdk.util.UizaCLPData;
import uizacoresdk.util.UizaData;
import uizacoresdk.widget.UizaPreviewTimeBar;
import uizacoresdk.widget.previewseekbar.PreviewView;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UizaException;
import vn.uiza.core.exception.UizaExceptionUtils;
import vn.uiza.core.utilities.connection.LConnectifyService;
import vn.uiza.data.EventBusData;
import vn.uiza.models.ListWrap;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.models.Subtitle;
import vn.uiza.models.ads.Ad;
import vn.uiza.models.live.LiveEntity;
import vn.uiza.models.playerinfo.Logo;
import vn.uiza.models.playerinfo.PlayerInfor;
import vn.uiza.models.tracking.Muiza;
import vn.uiza.models.tracking.UizaTracking;
import vn.uiza.models.tracking.UizaTrackingCCU;
import vn.uiza.models.vod.VODEntity;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.restapi.UizaHeartBeatService;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.UizaTrackingService;
import vn.uiza.restapi.UizaVideoService;
import vn.uiza.utils.ActivityUtils;
import vn.uiza.utils.AppUtils;
import vn.uiza.utils.ConvertUtils;
import vn.uiza.utils.DateUtils;
import vn.uiza.utils.DeviceUtils;
import vn.uiza.utils.ImageUtils;
import vn.uiza.utils.ConnectivityUtils;
import vn.uiza.utils.LDialogUtil;
import vn.uiza.utils.LSocialUtil;
import vn.uiza.utils.UIUtils;
import vn.uiza.utils.ListUtils;
import vn.uiza.utils.ScreenUtils;
import uizacoresdk.widget.autosize.UizaImageButton;
import uizacoresdk.widget.autosize.UizaTextView;
import uizacoresdk.widget.seekbar.UZVerticalSeekBar;

/**
 * Created by loitp on 2/27/2019.
 */

public class UizaVideoView extends VideoViewBase
        implements PreviewView.OnPreviewChangeListener, View.OnClickListener, View.OnFocusChangeListener,
        UizaPlayerView.ControllerStateCallback, SensorOrientationChangeNotifier.Listener {
    private int DEFAULT_VALUE_BACKWARD_FORWARD = 10000;//10000 mls
    private int DEFAULT_VALUE_CONTROLLER_TIMEOUT = 8000;//8000 mls
    private final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private final int DELAY_TO_GET_LIVE_INFORMATION = 15000;
    private static final String M3U8_EXTENSION = ".m3u8";
    private static final String MPD_EXTENSION = ".mpd";
    private static final String PLAY_THROUGH_100 = "100";
    private static final String PLAY_THROUGH_75 = "75";
    private static final String PLAY_THROUGH_50 = "50";
    private static final String PLAY_THROUGH_25 = "25";
    private static final String HYPHEN = "-";
    private boolean isLivestream;
    private boolean isTablet;
    private String cdnHost;
    private boolean isTV;//current device is TV or not (smartphone, tablet)
    private View bkg;
    private RelativeLayout rootView, rlChromeCast;
    private IUizaPlayerManager uzPlayerManager;
    private ProgressBar progressBar;
    private LinearLayout llTop, debugRootView;
    private RelativeLayout rlMsg, rlLiveInfo, rlEndScreen;
    private FrameLayout previewFrameLayout;
    private UizaPreviewTimeBar uzTimebar;
    private ImageView ivThumbnail, ivVideoCover, ivLogo;
    private UizaTextView tvPosition, tvDuration;
    private TextView tvMsg, tvTitle, tvLiveStatus, tvLiveView, tvLiveTime;
    private UizaImageButton ibFullscreenIcon, ibPauseIcon, ibPlayIcon, ibReplayIcon, ibRewIcon, ibFfwdIcon, ibBackScreenIcon, ibVolumeIcon,
            ibSettingIcon, ibCcIcon, ibPlaylistFolderIcon//danh sach playlist folder
            , ibHearingIcon, ibPictureInPictureIcon, ibSkipPreviousIcon, ibSkipNextIcon, ibSpeedIcon, ivLiveTime, ivLiveView, ibsCast;
    private TextView tvEndScreenMsg;
    private UizaPlayerView uzPlayerView;

    //    private ResultGetLinkPlay mResultGetLinkPlay;
//    private ResultGetTokenStreaming mResultGetTokenStreaming;
    private String urlIMAAd = null;
    private PlayerInfor playerInfor;
    private long startTime = Constants.UNKNOW;
    private boolean isSetUZTimebarBottom;
    private UizaChromeCast uZChromeCast;
    private boolean isCastingChromecast = false;
    private List<Subtitle> subtitleList = new ArrayList<>();
    private boolean autoMoveToLiveEdge;
    private @LayoutRes
    int pipControlSkin;

    Handler handler = new Handler();

    public UizaVideoView(Context context) {
        super(context);
        initView(null, 0);
    }

    public UizaVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public UizaVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UizaVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleAttr);
    }

    /**
     * Call twice time
     * Node: Don't call inflate in this method
     */
    private void initView(AttributeSet attrs, int defStyleAttr) {
        // todo
    }

    /**
     * Call one time from {@link #onAttachedToWindow}
     * Note: you must call inflate in this method
     */
    @Override
    public void onCreateView() {
        EventBus.getDefault().register(this);
        startConectifyService();
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);
        checkDevices();
        rootView = findViewById(R.id.root_view);
        addPlayerView();
        findViews();
        resizeContainerView();
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        if (AppUtils.checkChromeCastAvailable()) {
            setupChromeCast();
        }
        updateUISizeThumbnail();
        scheduleJob();
    }

    private void checkDevices() {
        isTablet = DeviceUtils.isTablet(getContext());
        isTV = DeviceUtils.isTV(getContext());
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
            Intent startServiceIntent = new Intent(getContext(), LConnectifyService.class);
            getContext().startService(startServiceIntent);
        }
    }

    //========================================================================START CONFIG
    private boolean isAutoStart = Constants.DF_PLAYER_IS_AUTO_START;

    public void setAutoStart(boolean isAutoStart) {
        this.isAutoStart = isAutoStart;
        TmpParamData.getInstance().setPlayerAutoplayOn(isAutoStart);
        updateUIButtonPlayPauseDependOnIsAutoStart();
    }

    public boolean isAutoStart() {
        return isAutoStart;
    }

    private boolean isAutoSwitchItemPlaylistFolder = true;

    public void setAutoSwitchItemPlaylistFolder(boolean isAutoSwitchItemPlaylistFolder) {
        this.isAutoSwitchItemPlaylistFolder = isAutoSwitchItemPlaylistFolder;
    }

    public boolean isAutoSwitchItemPlaylistFolder() {
        return isAutoSwitchItemPlaylistFolder;
    }

    private boolean isAutoShowController;

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
        return (getPlayer() == null) ? -1 : getPlayer().getDuration();
    }

    // An estimate of the position in the current window up to which data is buffered.
    // If the length of the content is 100,00 ms, and played 50,000 ms already with extra 50,000 ms~ 60,000 ms buffered,
    // it returns 60,000 ms.
    public long getBufferedPosition() {
        return (getPlayer() == null) ? -1 : getPlayer().getBufferedPosition();
    }

    // An estimate of the percentage in the current window up to which data is buffered.
    // If the length of the content is 100,00 ms, and played 50,000 ms already with extra 50,000 ms~ 60,000 ms buffered,
    // it returns 60(%).
    public int getBufferedPercentage() {
        return (getPlayer() == null) ? -1 : getPlayer().getBufferedPercentage();
    }

    // Lay pixel dung cho custom UI like youtube, uzTimebar bottom of player controller
    public int getPixelAdded() {
        if (isSetUZTimebarBottom) {
            return getHeightUZTimeBar() / 2;
        }
        return 0;
    }


    //return pixel
    public int getHeightUZTimeBar() {
        return UIUtils.getHeightOfView(uzTimebar);
    }

    //The current position of playing. the window means playable region, which is all of the content if vod, and some portion of the content if live.
    @Override
    public long getCurrentPosition() {
        return (uzPlayerManager == null) ? -1 : uzPlayerManager.getCurrentPosition();
    }

    public Format getVideoFormat() {
        if (getPlayer() == null) {
            return null;
        }
        Format format = getPlayer().getVideoFormat();
        if (format == null) {
            return null;
        }
        return format;
    }

    public Format getAudioFormat() {
        if (getPlayer() == null) {
            return null;
        }
        Format format = getPlayer().getAudioFormat();
        if (format == null) {
            return null;
        }
        return format;
    }

    public int getVideoProfileW() {
        if (uzPlayerManager == null) {
            return Constants.UNKNOW;
        }
        return uzPlayerManager.getVideoProfileW();
    }

    public int getVideoProfileH() {
        if (uzPlayerManager == null) {
            return Constants.UNKNOW;
        }
        return uzPlayerManager.getVideoProfileH();
    }

    public void setResizeMode(int resizeMode) {
        if (uzPlayerView != null) {
            uzPlayerView.setResizeMode(resizeMode);
        }
    }

    public void setSize(int width, int height) {
        UizaUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), width, height, isFreeSize);
    }

    private boolean isFreeSize;

    public void setFreeSize(boolean isFreeSize) {
        this.isFreeSize = isFreeSize;
        resizeContainerView();
    }

    private boolean isPlayerControllerAlwayVisible;

    public void setPlayerControllerAlwayVisible() {
        setControllerAutoShow(true);
        setHideControllerOnTouch(false);
        setControllerShowTimeoutMs(0);
        isPlayerControllerAlwayVisible = true;
    }
    //========================================================================END CONFIG

    private boolean isSetFirstRequestFocusDone;
    private boolean isHasError;

    protected void handleError(UizaException uzException) {
        if (uzException == null) {
            return;
        }
        notifyError(uzException);
        // Capture by Sentry, in uzException already contains Message, Error Code
        Timber.e(uzException);
        addTrackingMuizaError(Constants.MUIZA_EVENT_ERROR, uzException);
        if (isHasError) {
            return;
        }
        isHasError = true;
        UizaData.getInstance().setSettingPlayer(false);
    }

    private void notifyError(UizaException exception) {
        if (uzCallback != null) {
            uzCallback.onError(exception);
        }
    }

    private String entityId;
    private UUID uuid;
    private long timestampBeforeInitNewSession;

    private void init(String entityId, boolean isClearDataPlaylistFolder, boolean isLive) {
        Timber.d("entityId %s", entityId);
        this.isLivestream = isLive;
        this.uuid = UUID.randomUUID();
        if (isClearDataPlaylistFolder) {
            UizaData.getInstance().clearDataForPlaylistFolder();
        }
        if (entityId == null) {
            Timber.e("init error because entityId == null -> called from PIP");
            try {
                if (TextUtils.isEmpty(UizaData.getInstance().getPlaybackInfo().getHls())) {
                    notifyError(UizaExceptionUtils.getExceptionEntityId());
                    Timber.e("init error: entityId null or empty");
                    return;
                } else {
                    entityId = UizaData.getInstance().getEntityId();
                }
            } catch (NullPointerException e) {
                notifyError(UizaExceptionUtils.getExceptionEntityId());
                Timber.e(e, "init NullPointerException");
                return;
            }
        } else {
            timestampBeforeInitNewSession = System.currentTimeMillis();
            timestampOnStartPreview = 0;
            maxSeekLastDuration = 0;
            UizaData.getInstance().clearUizaInput();
            TmpParamData.getInstance().clearAll();
            TmpParamData.getInstance().addPlayerViewCount();
            TmpParamData.getInstance().setSessionId(uuid.toString());
        }
        Timber.d("isPlayWithPlaylistFolder %s", UizaData.getInstance().isPlayWithPlaylistFolder());
        handlePlayPlayListFolderUI();
        isCalledFromChangeSkin = false;
        isInitCustomLinkPlay = false;
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        urlIMAAd = null;
        isHasError = false;
        isOnPlayerEnded = false;
        this.entityId = entityId;
        Timber.d("get entityId: %s", entityId);
        UizaData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
        updateUIEndScreen();
        if (!ConnectivityUtils.isConnected(getContext())) {
            notifyError(UizaExceptionUtils.getExceptionNoConnection());
            Timber.d("!isConnected return");
            return;
        }
        callAPIGetDetailEntity();
//        callAPIGetPlayerInfor();
//        callAPIGetUrlIMAAdTag();
    }

    public boolean initCustomLinkPlay() {
        Context context = getContext();
        if (UizaCLPData.getInstance().getPlaybackInfo() == null) {
            Timber.e(UizaException.ERR_14);
            return false;
        }
        if (!ConnectivityUtils.isConnected(context)) {
            Timber.e(UizaException.ERR_0);
            return false;
        }
        PlaybackInfo playbackInfo = UizaCLPData.getInstance().getPlaybackInfo();
        if (!UizaUtil.getClickedPip(context)) {
            UizaUtil.stopMiniPlayer(context);
        }
        initPlayback(playbackInfo.getLinkPlay(), playbackInfo.isLive());
        UizaUtil.setIsInitPlaylistFolder(context, false);
        return true;
    }

    private void handlePlayPlayListFolderUI() {
        if (isPlayPlaylistFolder()) {
            setVisibilityOfPlaylistFolderController(VISIBLE);
        } else {
            setVisibilityOfPlaylistFolderController(GONE);
        }
    }

    @Override
    public SimpleExoPlayer getPlayer() {
        return (uzPlayerManager == null) ? null : uzPlayerManager.getPlayer();
    }

    @Override
    public void seekTo(long positionMs) {
        if (uzPlayerManager != null) {
            uzPlayerManager.seekTo(positionMs);
        }
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId) {
        init(entityId, true, false);
    }

    /**
     * Play with {@link PlaybackInfo}
     *
     * @param playback PlaybackInfo nonnull
     * @return true if not error
     */
    @Override
    public boolean play(@NonNull PlaybackInfo playback) {
        Context context = getContext();
        UizaUtil.setClickedPip(context, false);
        if (!ConnectivityUtils.isConnected(context)) {
            Timber.e(UizaException.ERR_0);
            return false;
        }
        UizaUtil.stopMiniPlayer(context);
        UizaData.getInstance().setSettingPlayer(false);
        //
        UizaData.getInstance().setPlaybackInfo(playback);
        post(() -> initPlayback(playback.getLinkPlay(), playback.isLive()));
        UizaUtil.setIsInitPlaylistFolder(context, false);
        UizaCLPData.getInstance().clearData();
        return true;
    }

    /**
     * Play VOD with entityId
     *
     * @param entityId: entity Id
     * @return true if not error
     */
    public boolean play(@NonNull String entityId) {
        return play(entityId, true, false);
    }

    /**
     * Play with entityId
     *
     * @param entityId: entity Id
     * @param isLive:   true if live
     * @return true if not error
     */
    @Override
    public boolean play(@NonNull String entityId, boolean isLive) {
        return play(entityId, true, isLive);
    }

    /**
     * Play with entityId
     *
     * @param entityId:                  entity Id
     * @param isClearDataPlaylistFolder: boolean
     * @param isLive:                    true if live
     * @return true if not error
     */
    public boolean play(@NonNull String entityId, boolean isClearDataPlaylistFolder, boolean isLive) {
        Context context = getContext();
        UizaUtil.setClickedPip(context, false);
        if (!ConnectivityUtils.isConnected(context)) {
            Timber.e(UizaException.ERR_0);
            return false;
        }
        UizaUtil.stopMiniPlayer(context);
        post(() -> init(entityId, isClearDataPlaylistFolder, isLive));
        UizaUtil.setIsInitPlaylistFolder(context, false);
        UizaCLPData.getInstance().clearData();
        return true;
    }

    @Override
    public void resume() {
        TmpParamData.getInstance().setPlayerIsPaused(false);
        addTrackingMuiza(Constants.MUIZA_EVENT_PLAY);
        if (isCastingChromecast) {
            UizaData.getInstance().getCasty().getPlayer().play();
        } else if (uzPlayerManager != null) {
            uzPlayerManager.resume();
        }
        UIUtils.goneViews(ibPlayIcon);
        if (ibPauseIcon != null) {
            UIUtils.visibleViews(ibPauseIcon);
            ibPauseIcon.requestFocus();
        }
    }

    @Override
    public void pause() {
        TmpParamData.getInstance().setPlayerIsPaused(true);
        if (isCastingChromecast) {
            UizaData.getInstance().getCasty().getPlayer().pause();
        } else if (uzPlayerManager != null) {
            uzPlayerManager.pause();
        }
        UIUtils.goneViews(ibPauseIcon);
        if (ibPlayIcon != null) {
            UIUtils.visibleViews(ibPlayIcon);
            ibPlayIcon.requestFocus();
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_PAUSE);
    }

    @Override
    public int getVideoWidth() {
        return (uzPlayerManager == null) ? 0 : uzPlayerManager.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return (uzPlayerManager == null) ? 0 : uzPlayerManager.getVideoHeight();
    }

    private boolean isInitCustomLinkPlay;//user pass any link (not use entityId or metadataId)

    public void initPlayback(@NonNull String linkPlay, boolean isLiveStream) {
        Timber.d("init linkPlay %s", linkPlay);
        isInitCustomLinkPlay = true;
        isCalledFromChangeSkin = false;
        setVisibilityOfPlaylistFolderController(GONE);
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
//        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
//        mResultGetTokenStreaming = null;
        this.entityId = null;
        UizaData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
        isOnPlayerEnded = false;
        updateUIEndScreen();
        isHasError = false;
        this.isLivestream = isLiveStream;
        if (isLivestream) {
            startTime = Constants.UNKNOW;
        }
        setDefaultValueForFlagIsTracked();
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
            resetCountTryLinkPlayError();
            showProgress();
        }
        updateUIDependOnLivestream();
        // TODO: Check how to get subtitle of a custom link play, because we have no idea about entityId or appId
        List<Subtitle> subtitleList = null;

        if (!ConnectivityUtils.isConnected(getContext())) {
            notifyError(UizaExceptionUtils.getExceptionNoConnection());
            return;
        }
        initDataSource(linkPlay, UizaData.getInstance().getUrlIMAAd(), UizaData.getInstance().getUrlThumbnailsPreviewSeekBar(), subtitleList, AppUtils.isAdsDependencyAvailable());
        if (uzCallback != null) {
            uzCallback.isInitResult(false, true, UizaData.getInstance().getPlaybackInfo());
        }
        initUizaPlayerManager();
    }

    public void initPlaylistFolder(String metadataId) {
        if (metadataId == null) {
            Timber.d("initPlaylistFolder metadataId null -> called from PIP: %b", isGetClickedPip);
        } else {
            Timber.d("initPlaylistFolder metadataId %s, -> called from PIP: %b", metadataId, isGetClickedPip);
            UizaData.getInstance().clearDataForPlaylistFolder();
        }
        isHasError = false;
        isClickedSkipNextOrSkipPrevious = false;
        callAPIGetListAllEntity(metadataId);
    }

    public void toggleStatsForNerds() {
        if (getPlayer() == null) return;
        boolean isEnableStatsForNerds =
                statsForNerdsView == null || statsForNerdsView.getVisibility() != View.VISIBLE;
        if (isEnableStatsForNerds) {
            UIUtils.visibleViews(statsForNerdsView);
        } else {
            UIUtils.goneViews(statsForNerdsView);
        }
    }

    private int countTryLinkPlayError = 0;

    protected void tryNextLinkPlay() {
        if (isLivestream) {
            // try to play 5 times
            if (countTryLinkPlayError >= 5) {
                if (uzLiveContentCallback != null) {
                    uzLiveContentCallback.onLiveStreamUnAvailable();
                }
                return;
            }
            // if entity is livestreaming, dont try to next link play
            Timber.e("tryNextLinkPlay isLivestream true -> try to replay = count %d", countTryLinkPlayError);
            if (uzPlayerManager != null) {
                uzPlayerManager.initWithoutReset();
                uzPlayerManager.setRunnable();
            }
            countTryLinkPlayError++;
            return;
        }
        countTryLinkPlayError++;
        Timber.e("%s: %d", getContext().getString(R.string.cannot_play_will_try), countTryLinkPlayError);
        releaseUzPlayerManager();
        checkToSetUpResource();
    }

    //khi call api callAPIGetLinkPlay nhung json tra ve ko co data
    //se co gang choi video da play gan nhat
    //neu co thi se play
    //khong co thi bao loi
    private void handleErrorNoData() {
        removeVideoCover(true);
        if (uzCallback != null) {
            UizaData.getInstance().setSettingPlayer(false);
            uzCallback.isInitResult(false, false, null);
        }
    }

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    public void onDestroyView() {
        //cannot use isGetClickedPip (global variable), must use UizaUtil.getClickedPip(activity)
        if (UizaUtil.getClickedPip(getContext())) {
            UizaUtil.stopMiniPlayer(getContext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getContext().stopService(new Intent(getContext(), LConnectifyService.class));
        }
        releasePlayerAnalytic();
        releaseUzPlayerManager();
        UizaData.getInstance().setSettingPlayer(false);
        LDialogUtil.clearAll();
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        cdnHost = null;
        EventBus.getDefault().unregister(this);
    }

    private void releasePlayerAnalytic() {
        if (getPlayer() != null) {
            getPlayer().removeAnalyticsListener(statsForNerdsView);
        }
    }

    private void releaseUzPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.release();
        }
    }

    public void onResumeView() {
        SensorOrientationChangeNotifier.getInstance(getContext()).addListener(this);
        if (isCastingChromecast) {
            return;
        }
        activityIsPausing = false;
        if (uzPlayerManager != null) {
            if (ibPlayIcon == null || ibPlayIcon.getVisibility() != VISIBLE) {
                uzPlayerManager.resume();
            }
        }
        // try to move to the edge of livestream video
        if (autoMoveToLiveEdge && isLivestream()) {
            seekToLiveEdge();
        }
    }

    public boolean isPlaying() {
        return (getPlayer() != null) && getPlayer().getPlayWhenReady();
    }

    /**
     * Set auto move the the last window of livestream, default is false
     *
     * @param autoMoveToLiveEdge true if always seek to last livestream video, otherwise false
     */
    public void setAutoMoveToLiveEdge(boolean autoMoveToLiveEdge) {
        this.autoMoveToLiveEdge = autoMoveToLiveEdge;
    }

    /**
     * Seek to live edge of a streaming video
     */
    public void seekToLiveEdge() {
        if (isLivestream() && getPlayer() != null) {
            getPlayer().seekToDefaultPosition();
        }
    }

    private boolean activityIsPausing = false;

    public void onPauseView() {
        activityIsPausing = true;
        SensorOrientationChangeNotifier.getInstance(getContext()).remove(this);
        if (uzPlayerManager != null) {
            uzPlayerManager.pause();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isCastingChromecast()) {
            Timber.e("Error: handleClickPictureInPicture isCastingChromecast -> return");
            return;
        }
        if (DeviceUtils.isCanOverlay(getContext())) {
            initializePiP();
        }
    }

    private long timestampOnStartPreview;

    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        timestampOnStartPreview = System.currentTimeMillis();
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKING);
        if (onPreviewChangeListener != null) {
            onPreviewChangeListener.onStartPreview(previewView, progress);
        }
    }

    private boolean isOnPreview;

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        isOnPreview = true;
        updateUIIbRewIconDependOnProgress(progress, true);
        if (onPreviewChangeListener != null) {
            onPreviewChangeListener.onPreview(previewView, progress, fromUser);
        }
    }

    private long maxSeekLastDuration;

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        if (isCastingChromecast) {
            UizaData.getInstance().getCasty().getPlayer().seek(progress);
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
        if (onPreviewChangeListener != null) {
            onPreviewChangeListener.onStopPreview(previewView, progress);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKED);
    }

    public void onStopPreview(int progress) {
        if (uzPlayerManager != null && !isCastingChromecast) {
            uzPlayerManager.seekTo(progress);
            uzPlayerManager.resume();
            isOnPlayerEnded = false;
            updateUIEndScreen();
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (uizaTVFocusChangeListener != null) {
            uizaTVFocusChangeListener.onFocusChange(view, isFocus);
        } else {
            if (firstViewHasFocus == null) {
                firstViewHasFocus = view;
            }
        }
    }

    private boolean isLandscape;//current screen is landscape or portrait

    public boolean isLandscape() {
        return isLandscape;
    }

    @Override
    public void onOrientationChange(int orientation) {
        //270 land trai
        //0 portrait duoi
        //90 land phai
        //180 portrait tren
        boolean isDeviceAutoRotation = DeviceUtils.isRotationPossible(getContext());
        if (orientation == 90 || orientation == 270) {
            if (isDeviceAutoRotation && !isLandscape) {
                ActivityUtils.changeScreenLandscape((Activity) getContext(), orientation);
            }
        } else {
            if (isDeviceAutoRotation && isLandscape) {
                ActivityUtils.changeScreenPortrait((Activity) getContext());
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeContainerView();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScreenUtils.hideDefaultControls((Activity) getContext());
            isLandscape = true;
            UizaUtil.setUIFullScreenIcon(ibFullscreenIcon, true);
            UIUtils.goneViews(ibPictureInPictureIcon);
        } else {
            ScreenUtils.showDefaultControls((Activity) getContext());
            isLandscape = false;
            UizaUtil.setUIFullScreenIcon(ibFullscreenIcon, false);
            if (!isCastingChromecast()) {
                UIUtils.visibleViews(ibPictureInPictureIcon);
            }
        }
        TmpParamData.getInstance().setPlayerIsFullscreen(isLandscape);
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        updateUISizeThumbnail();
        updateUIPositionOfProgressBar();
        if (isSetUZTimebarBottom) {
            setMarginDependOnUZTimeBar(uzPlayerView.getVideoSurfaceView());
        }
        if (uzCallback != null) {
            uzCallback.onScreenRotate(isLandscape);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            AnimationUtils.play(v, Techniques.Pulse);
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
            showUZTrackSelectionDialog(v, true);
        } else if (v == rlChromeCast) {
            Timber.e("dangerous to remove");
        } else if (v == ibFfwdIcon) {
            if (isCastingChromecast) {
                UizaData.getInstance().getCasty().getPlayer().seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                if (uzPlayerManager != null) {
                    uzPlayerManager.seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
                }
            }
        } else if (v == ibRewIcon) {
            if (isCastingChromecast) {
                UizaData.getInstance().getCasty().getPlayer().seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else if (uzPlayerManager != null) {
                uzPlayerManager.seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
                if (isPlaying()) {
                    isOnPlayerEnded = false;
                    updateUIEndScreen();
                }
            }
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
            AnimationUtils.play(v, Techniques.Pulse);
        } else if (v == ivLogo) {
            AnimationUtils.play(v, Techniques.Pulse);
            if (playerInfor == null || playerInfor.getLogo() == null || playerInfor.getLogo().getUrl() == null) {
                return;
            }
            LSocialUtil.openUrlInBrowser(getContext(), playerInfor.getLogo().getUrl());
        }
        /*có trường hợp đang click vào các control thì bị ẩn control ngay lập tức, trường hợp này ta có thể xử lý khi click vào control thì reset count down để ẩn control ko
        default controller timeout là 8s, vd tới s thứ 7 bạn tương tác thì tới s thứ 8 controller sẽ bị ẩn*/
        if (isDefaultUseController) {
            if (rlMsg == null || rlMsg.getVisibility() != VISIBLE) {
                if (isPlayerControllerShowing()) {
                    showController();
                }
            }
        }
        if (uizaVideoViewItemClick != null) {
            uizaVideoViewItemClick.onItemClick(v);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handleClickPictureInPicture() {
        if (!isInitMiniPlayerSuccess) {
            //dang init 1 instance mini player roi, khong cho init nua
            notifyError(UizaExceptionUtils.getExceptionShowPip());
            return;
        }
        if (isCastingChromecast()) {
            notifyError(UizaExceptionUtils.getExceptionShowPip());
            return;
        }
        if (DeviceUtils.isCanOverlay(getContext())) {
            initializePiP();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
            ((Activity) getContext()).startActivityForResult(intent, Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    private boolean isInitMiniPlayerSuccess = true;

    public void initializePiP() {
        if (uzPlayerManager == null || TextUtils.isEmpty(uzPlayerManager.getLinkPlay())) {
            notifyError(UizaExceptionUtils.getExceptionShowPip());
            return;
        }
        UIUtils.goneViews(ibPictureInPictureIcon);
        if (uzCallback != null) {
            isInitMiniPlayerSuccess = false;
            uzCallback.onStateMiniPlayer(false);
        }
        UizaUtil.setVideoWidth(getContext(), getVideoWidth());
        UizaUtil.setVideoHeight(getContext(), getVideoHeight());
        Intent intent = new Intent(getContext(), FloatUizaVideoService.class);
        intent.putExtra(Constants.FLOAT_CONTENT_POSITION, getCurrentPosition());
        intent.putExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, isInitCustomLinkPlay);
        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        intent.putExtra(Constants.FLOAT_UUID, uuid.toString());
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uzPlayerManager.getLinkPlay());
        intent.putExtra(Constants.FLOAT_IS_LIVESTREAM, isLivestream);
        intent.putExtra(Constants.FLOAT_PROGRESS_BAR_COLOR, progressBarColor);
        intent.putExtra(Constants.FLOAT_CONTROL_SKIN_ID, pipControlSkin);
        getContext().startService(intent);
    }


    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        DEFAULT_VALUE_CONTROLLER_TIMEOUT = controllerShowTimeoutMs;
        post(() -> uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT));
    }

    public int getControllerShowTimeoutMs() {
        return (uzPlayerView == null) ? -1 : uzPlayerView.getControllerShowTimeoutMs();
    }

    public boolean isPlayerControllerShowing() {
        return (uzPlayerView != null) && uzPlayerView.isControllerVisible();

    }

    public void showController() {
        if (uzPlayerView != null) {
            uzPlayerView.showController();
        }
    }

    public void hideController() {
        if (isPlayerControllerAlwayVisible) {
            return;
        }
        if (!isCastingChromecast) {//dont hide if is casting chromecast
            if (uzPlayerView != null) {
                uzPlayerView.hideController();
            }
        }
    }

    private boolean isHideOnTouch = true;

    public void setHideControllerOnTouch(boolean isHide) {
        if (uzPlayerView != null) {
            this.isHideOnTouch = isHide;
            uzPlayerView.setControllerHideOnTouch(isHide);
        }
    }

    public boolean getControllerHideOnTouch() {
        return (uzPlayerView != null) && uzPlayerView.getControllerHideOnTouch();
    }

    private boolean isDefaultUseController = true;

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

    //===================================================================START FOR PLAYLIST/FOLDER
    private final int pfLimit = 100;
    private int pfPage = 0;
    private int pfTotalPage = Integer.MAX_VALUE;
    private final String pfOrderBy = "createdAt";
    private final String pfOrderType = "DESC";
    private final String publishToCdn = "success";

    @Deprecated
    private void callAPIGetListAllEntity(String metadataId) {
        showProgress();
        boolean isDataListExist = UizaData.getInstance().getPlayList() != null;
        if (isDataListExist) {
            playPlaylistPosition(UizaData.getInstance().getCurrentPositionOfPlayList());
            hideProgress();
        } else {
            UizaVideoService service = UizaClientFactory.getVideoService();
            //metadataId, pfLimit, pfPage, pfOrderBy, pfOrderType, publishToCdn, ""
            RxBinder.bind(service.getEntities().map(ListWrap::getData),
                    result -> {
                        if (ListUtils.isEmpty(result)) {
                            if (uzCallback != null) {
                                handleError(UizaExceptionUtils.getExceptionListAllEntity());
                            }
                            return;
                        }
                        if (pfTotalPage == Integer.MAX_VALUE) {
                            int totalItem = result.size();
                            float ratio = (float) (totalItem / pfLimit);
                            if (ratio == 0) {
                                pfTotalPage = (int) ratio;
                            } else if (ratio > 0) {
                                pfTotalPage = (int) ratio + 1;
                            } else {
                                pfTotalPage = (int) ratio;
                            }
                        }
                        List<PlaybackInfo> playlist = ListUtils.map(result, VODEntity::getPlaybackInfo);
                        UizaData.getInstance().setPlayList(playlist);
                        if (ListUtils.isEmpty(playlist)) {
                            notifyError(UizaExceptionUtils.getExceptionListAllEntity());
                            return;
                        }
                        playPlaylistPosition(UizaData.getInstance().getCurrentPositionOfPlayList());
                        hideProgress();
                    }, throwable -> {
                        Timber.e(throwable, "callAPIGetListAllEntity onFail");
                        notifyError(UizaExceptionUtils.getExceptionListAllEntity());
                        hideProgress();
                    });
        }
    }

    protected boolean isPlayPlaylistFolder() {
        return UizaData.getInstance().getPlayList() != null;
    }

    private void playPlaylistPosition(int position) {
        if (!isPlayPlaylistFolder()) {
            Timber.e("playPlaylistPosition error: incorrect position");
            return;
        }
        Timber.d("playPlaylistPosition position: %d", position);
        if (position < 0) {
            Timber.e("This is the first item");
            notifyError(UizaExceptionUtils.getExceptionPlaylistFolderItemFirst());
            return;
        }
        if (position > UizaData.getInstance().getPlayList().size() - 1) {
            Timber.e("This is the last item");
            notifyError(UizaExceptionUtils.getExceptionPlaylistFolderItemLast());
            return;
        }
        urlImgThumbnail = null;
        pause();
        hideController();
        //update UI for skip next and skip previous button
        if (position == 0) {
            setSrcDrawableEnabledForViews(ibSkipPreviousIcon, ibSkipNextIcon);
        } else if (position == UizaData.getInstance().getPlayList().size() - 1) {
            setSrcDrawableEnabledForViews(ibSkipPreviousIcon, ibSkipNextIcon);
        } else {
            setSrcDrawableEnabledForViews(ibSkipPreviousIcon, ibSkipNextIcon);
        }
        //set disabled prevent double click, will enable onStateReadyFirst()
        setClickableForViews(false, ibSkipPreviousIcon, ibSkipNextIcon);
        //end update UI for skip next and skip previous button
        UizaData.getInstance().setCurrentPositionOfPlayList(position);
        PlaybackInfo data = UizaData.getInstance().getDataWithPositionOfPlayList(position);
        if (data == null || TextUtils.isEmpty(data.getId())) {
            Timber.e("playPlaylistPosition error: data null or cannot get id");
            return;
        }
        Timber.d("-----------------------> playPlaylistPosition %d", position);
        init(UizaData.getInstance().getDataWithPositionOfPlayList(position).getId(), false,
                !TextUtils.isEmpty(data.getLastFeedId()));
    }

    private void setSrcDrawableEnabledForViews(UizaImageButton... views) {
        for (UizaImageButton v : views) {
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

    private boolean isOnPlayerEnded;

    protected void onPlayerEnded() {
        if (isPlaying()) {
            isOnPlayerEnded = true;
            if (isPlayPlaylistFolder() && isAutoSwitchItemPlaylistFolder) {
                hideController();
                autoSwitchNextVideo();
            } else {
                updateUIEndScreen();
            }
            addTrackingMuiza(Constants.MUIZA_EVENT_VIEWENDED);
        }
    }

    private void autoSwitchNextVideo() {
        playPlaylistPosition(UizaData.getInstance().getCurrentPositionOfPlayList() + 1);
    }

    private void autoSwitchPreviousLinkVideo() {
        playPlaylistPosition(UizaData.getInstance().getCurrentPositionOfPlayList() - 1);
    }

    private void handleClickPlaylistFolder() {
        UizaPlaylistFolderDialog UZDlgPlaylistFolder = new UizaPlaylistFolderDialog(getContext(), isLandscape, UizaData.getInstance().getPlayList(), UizaData.getInstance().getCurrentPositionOfPlayList(), new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(PlaybackInfo playback, int position) {
                UizaData.getInstance().setCurrentPositionOfPlayList(position);
                playPlaylistPosition(position);
            }

            @Override
            public void onFocusChange(PlaybackInfo playback, int position) {
            }

            @Override
            public void onDismiss() {
            }
        });
        UizaUtil.showUizaDialog(getContext(), UZDlgPlaylistFolder);
    }

    private boolean isClickedSkipNextOrSkipPrevious;

    private void handleClickSkipNext() {
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUIEndScreen();
        autoSwitchNextVideo();
    }

    private void handleClickSkipPrevious() {
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUIEndScreen();
        autoSwitchPreviousLinkVideo();
    }

    public boolean replay() {
        if (uzPlayerManager == null) {
            return false;
        }
        TmpParamData.getInstance().addPlayerViewCount();
        //TODO Chỗ này đáng lẽ chỉ clear value của tracking khi đảm bảo rằng seekTo(0) true
        setDefaultValueForFlagIsTracked();
        boolean result = uzPlayerManager.seekTo(0);
        if (result) {
            isSetFirstRequestFocusDone = false;
            isOnPlayerEnded = false;
            updateUIEndScreen();
            handlePlayPlayListFolderUI();
            trackUizaEventVideoStarts();
            trackUizaEventDisplay();
            trackUizaEventPlaysRequested();
        }
        if (isCastingChromecast) {
            replayChromeCast();
        }
        return result;
    }

    private void replayChromeCast() {
        lastCurrentPosition = 0;
        handleConnectedChromecast();
        showController();
    }

    //===================================================================END FOR PLAYLIST/FOLDER

    /*Nếu đang casting thì button này sẽ handle volume on/off ở cast player
     * Ngược lại, sẽ handle volume on/off ở exo player*/
    private void handleClickBtVolume() {
        if (isCastingChromecast) {
            boolean isMute = UizaData.getInstance().getCasty().toggleMuteVolume();
            if (ibVolumeIcon != null) {
                if (isMute) {
                    ibVolumeIcon.setImageResource(R.drawable.ic_volume_off_white_48);
                } else {
                    ibVolumeIcon.setImageResource(R.drawable.ic_volume_up_white_48);
                }
            }
        } else if (uzPlayerManager != null) {
            uzPlayerManager.toggleVolumeMute(ibVolumeIcon);

        }
    }

    private void handleClickBackScreen() {
        if (isLandscape) {
            toggleFullscreen();
        }
    }

    private void handleClickSetting() {
        View view = UizaUtil.getBtVideo(debugRootView);
        if (view != null)
            view.performClick();
    }

    private void handleClickCC() {
        if (ListUtils.isEmpty(uzPlayerManager.getSubtitleList())) {
            UZDlgInfoV1 uzDlgInfoV1 = new UZDlgInfoV1(getContext(), getContext().getString(R.string.text), getContext().getString(R.string.no_caption));
            UizaUtil.showUizaDialog(getContext(), uzDlgInfoV1);
        } else {
            View view = UizaUtil.getBtText(debugRootView);
            if (view != null) {
                view.performClick();
            }
        }
    }

    private void handleClickHearing() {
        View view = UizaUtil.getBtAudio(debugRootView);
        if (view != null)
        view.performClick();
    }


    public void setDefaultValueBackwardForward(int mls) {
        DEFAULT_VALUE_BACKWARD_FORWARD = mls;
    }

    public int getDefaultValueBackwardForward() {
        return DEFAULT_VALUE_BACKWARD_FORWARD;
    }

    /*
     ** Seek tu vi tri hien tai cong them bao nhieu mls
     */
    public void seekToForward(int mls) {
        setDefaultValueBackwardForward(mls);
        ibFfwdIcon.performClick();
    }

    /*
     ** Seek tu vi tri hien tai tru di bao nhieu mls
     */
    public void seekToBackward(int mls) {
        setDefaultValueBackwardForward(mls);
        ibRewIcon.performClick();
    }

    //chi toggle show hide controller khi video da vao dc onStateReadyFirst();
    public void toggleShowHideController() {
        if (uzPlayerView != null) {
            uzPlayerView.toggleShowHideController();
        }
    }

    public void togglePlayPause() {
        if (uzPlayerManager == null || getPlayer() == null) {
            return;
        }
        if (getPlayer().getPlayWhenReady()) {
            pause();
        } else {
            resume();
        }
    }

    public void toggleVolume() {
        ibVolumeIcon.performClick();
    }

    public void toggleFullscreen() {
        addTrackingMuiza(Constants.MUIZA_EVENT_FULLSCREENCHANGE);
        ActivityUtils.toggleScreenOritation((Activity) getContext());
    }

    public void showCCPopup() {
        ibCcIcon.performClick();
    }

    public void showHQPopup() {
        ibSettingIcon.performClick();
    }

    /*
     ** Hiển thị picture in picture và close video view hiện tại
     * Chỉ work nếu local player đang không casting
     * Device phải là tablet
     */
    public void showPip() {
        if (isCastingChromecast()) {
            Timber.e(UizaException.ERR_19);
            notifyError(UizaExceptionUtils.getExceptionShowPip());
        } else {
            // [Re-check]: Why use performClick?
            // UIUtils.performClick(ibPictureInPictureIcon);
            handleClickPictureInPicture();
        }
    }

    public void setPipControlSkin(@LayoutRes int skinId) {
        this.pipControlSkin = skinId;
    }

    public void showSpeed() {
        if (getPlayer() == null) {
            return;
        }
        final UizaSpeedDialog uzDlgSpeed = new UizaSpeedDialog(getContext(), getPlayer().getPlaybackParameters().speed,
                speed -> {
                    if (speed != null) {
                        setSpeed(speed.getValue());
                    }
                });
        UizaUtil.showUizaDialog(getContext(), uzDlgSpeed);
    }

    /*
     ** Bo video hien tai va choi tiep theo 1 video trong playlist/folder
     */
    public void skipNextVideo() {
        handleClickSkipNext();
    }

    /*
     * Bo video hien tai va choi lui lai 1 video trong playlist/folder
     */
    public void skipPreviousVideo() {
        handleClickSkipPrevious();
    }

    public PlayerView getUzPlayerView() {
        return uzPlayerView;
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

    public IUizaPlayerManager getUzPlayerManager() {
        return uzPlayerManager;
    }

    public TextView getTvMsg() {
        return tvMsg;
    }

    public ImageView getIvVideoCover() {
        return ivVideoCover;
    }

    public UizaImageButton getIbFullscreenIcon() {
        return ibFullscreenIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public UizaImageButton getIbPauseIcon() {
        return ibPauseIcon;
    }

    public UizaImageButton getIbPlayIcon() {
        return ibPlayIcon;
    }

    public UizaImageButton getIbReplayIcon() {
        return ibReplayIcon;
    }

    public UizaImageButton getIbRewIcon() {
        return ibRewIcon;
    }

    public UizaImageButton getIbFfwdIcon() {
        return ibFfwdIcon;
    }

    public UizaImageButton getIbBackScreenIcon() {
        return ibBackScreenIcon;
    }

    public UizaImageButton getIbVolumeIcon() {
        return ibVolumeIcon;
    }

    public UizaImageButton getIbSettingIcon() {
        return ibSettingIcon;
    }

    public UizaImageButton getIbCcIcon() {
        return ibCcIcon;
    }

    public UizaImageButton getIbPlaylistFolderIcon() {
        return ibPlaylistFolderIcon;
    }

    public UizaImageButton getIbHearingIcon() {
        return ibHearingIcon;
    }

    public UizaImageButton getIbPictureInPictureIcon() {
        return ibPictureInPictureIcon;
    }

    public UizaImageButton getIbSkipPreviousIcon() {
        return ibSkipPreviousIcon;
    }

    public UizaImageButton getIbSkipNextIcon() {
        return ibSkipNextIcon;
    }

    public UizaImageButton getIbSpeedIcon() {
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

    public UizaImageButton getIbsCast() {
        return ibsCast;
    }

    public String getEntityId() {
        return entityId;
    }

    public UizaTextView getTvPosition() {
        return tvPosition;
    }

    public UizaTextView getTvDuration() {
        return tvDuration;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public UizaImageButton getIvLiveTime() {
        return ivLiveTime;
    }

    public UizaImageButton getIvLiveView() {
        return ivLiveView;
    }

    public RelativeLayout getRlEndScreen() {
        return rlEndScreen;
    }

    public TextView getTvEndScreenMsg() {
        return tvEndScreenMsg;
    }

    public UizaPreviewTimeBar getUZTimeBar() {
        return uzTimebar;
    }

    public LinearLayout getLlTop() {
        return llTop;
    }

    public View getBkg() {
        return bkg;
    }

    public List<UizaItem> getHQList() {
        View view = UizaUtil.getBtVideo(debugRootView);
        if (view == null) {
            Timber.e("Error getHQList null");
            notifyError(UizaExceptionUtils.getExceptionListHQ());
            return null;
        }
        return showUZTrackSelectionDialog(view, false);
    }

    public List<UizaItem> getAudioList() {
        View view = UizaUtil.getBtAudio(debugRootView);
        if (view == null) {
            notifyError(UizaExceptionUtils.getExceptionListAudio());
            Timber.e("Error audio null");
            return null;
        }
        return showUZTrackSelectionDialog(view, false);
    }

    public void setVolume(float volume) {
        if (uzPlayerManager != null) {
            uzPlayerManager.setVolume(volume);
        }
    }

    public float getVolume() {
        return (uzPlayerManager == null) ? -1 : uzPlayerManager.getVolume();
    }

    @Override
    public void onVisibilityChange(boolean isShow) {
        if (ivLogo != null) {
            ivLogo.setClickable(!isShow);
        }
        if (controllerStateCallback != null) {
            controllerStateCallback.onVisibilityChange(isShow);
        }
    }

    public void setSpeed(float speed) {
        if (getContext() == null) {
            return;
        }
        if (isLivestream) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_speed_live_content));
        }
        if (speed > 3 || speed < -3) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_speed_illegal));
        }
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        if (getPlayer() != null) {
            getPlayer().setPlaybackParameters(playbackParameters);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_RATECHANGE);
    }

    //=============================================================================================START UI
    private void findViews() {
        bkg = findViewById(R.id.bkg);
        rlMsg = findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = findViewById(R.id.tv_msg);
        if (tvMsg != null) {
            UIUtils.setTextShadow(tvMsg);
        }
        ivVideoCover = findViewById(R.id.iv_cover);
        llTop = findViewById(R.id.ll_top);
        progressBar = findViewById(R.id.pb);
        UIUtils.setColorProgressBar(progressBar, progressBarColor);
        updateUIPositionOfProgressBar();
        uzPlayerView.setControllerStateCallback(this);
        uzTimebar = uzPlayerView.findViewById(R.id.exo_progress);
        previewFrameLayout = uzPlayerView.findViewById(R.id.preview_frame_layout);
        if (uzTimebar != null) {
            if (uzTimebar.getTag() == null) {
                isSetUZTimebarBottom = false;
                uzPlayerView.setVisibility(VISIBLE);
            } else {
                if (uzTimebar.getTag().toString().equals(getContext().getString(R.string.use_bottom_uz_timebar))) {
                    isSetUZTimebarBottom = true;
                    setMarginDependOnUZTimeBar(uzPlayerView.getVideoSurfaceView());
                } else {
                    isSetUZTimebarBottom = false;
                    uzPlayerView.setVisibility(VISIBLE);
                }
            }
            uzTimebar.addOnPreviewChangeListener(this);
            uzTimebar.setOnFocusChangeListener(this);
        } else {
            uzPlayerView.setVisibility(VISIBLE);
        }
        ivThumbnail = uzPlayerView.findViewById(R.id.image_view_thumnail);
        tvPosition = uzPlayerView.findViewById(R.id.uz_position);
        if (tvPosition != null) {
            tvPosition.setText(DateUtils.convertMlsecondsToHMmSs(0));
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
        UIUtils.goneViews(ibPlayIcon);
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
        if (BuildConfig.DEBUG) {
            debugLayout.setVisibility(View.VISIBLE);
        } else {
            debugLayout.setVisibility(View.GONE);
        }
        rlLiveInfo = uzPlayerView.findViewById(R.id.rl_live_info);
        tvLiveStatus = uzPlayerView.findViewById(R.id.tv_live);
        tvLiveView = uzPlayerView.findViewById(R.id.tv_live_view);
        tvLiveTime = uzPlayerView.findViewById(R.id.tv_live_time);
        ivLiveView = uzPlayerView.findViewById(R.id.iv_live_view);
        ivLiveTime = uzPlayerView.findViewById(R.id.iv_live_time);
        UIUtils.setFocusableViews(false, ivLiveView, ivLiveTime);
        rlEndScreen = uzPlayerView.findViewById(R.id.rl_end_screen);
        UIUtils.goneViews(rlEndScreen);
        tvEndScreenMsg = uzPlayerView.findViewById(R.id.tv_end_screen_msg);
        if (tvEndScreenMsg != null) {
            UIUtils.setTextShadow(tvEndScreenMsg, Color.WHITE);
            tvEndScreenMsg.setOnClickListener(this);
        }
        setEventForViews();
        //set visibility first, so scared if removed
        setVisibilityOfPlaylistFolderController(GONE);

        statsForNerdsView = findViewById(R.id.stats_for_nerds);
    }

    private void setEventForViews() {
        setClickAndFocusEventForViews(ibFullscreenIcon, ibBackScreenIcon, ibVolumeIcon, ibSettingIcon,
                ibCcIcon, ibPlaylistFolderIcon, ibHearingIcon, ibPictureInPictureIcon, ibFfwdIcon,
                ibRewIcon, ibPlayIcon, ibPauseIcon, ibReplayIcon, ibSkipNextIcon, ibSkipPreviousIcon, ibSpeedIcon);
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
    private void updateUIButtonPlayPauseDependOnIsAutoStart() {
        if (isAutoStart) {
            UIUtils.goneViews(ibPlayIcon);
            if (ibPauseIcon != null) {
                UIUtils.visibleViews(ibPauseIcon);
                if (!isSetFirstRequestFocusDone) {
                    ibPauseIcon.requestFocus();//set first request focus if using player for TV
                    isSetFirstRequestFocusDone = true;
                }
            }
        } else {
            if (isPlaying()) {
                UIUtils.goneViews(ibPlayIcon);
                if (ibPauseIcon != null) {
                    UIUtils.visibleViews(ibPauseIcon);
                    if (!isSetFirstRequestFocusDone) {
                        ibPauseIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
            } else {
                if (ibPlayIcon != null) {
                    UIUtils.visibleViews(ibPlayIcon);
                    if (!isSetFirstRequestFocusDone) {
                        ibPlayIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
                UIUtils.goneViews(ibPauseIcon);
            }
        }
    }

    private String urlImgThumbnail;

    public void setUrlImgThumbnail(String urlImgThumbnail) {
        if (TextUtils.isEmpty(urlImgThumbnail)) {
            return;
        }
        this.urlImgThumbnail = urlImgThumbnail;
        if (ivVideoCover == null) {
            return;
        }
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
            ImageUtils.load(ivVideoCover, urlImgThumbnail, R.drawable.background_black);
        }
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            resetCountTryLinkPlayError();
            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            String urlCover;
            if (TextUtils.isEmpty(urlImgThumbnail)) {
                if (mPlaybackInfo == null) {
                    urlCover = Constants.URL_IMG_THUMBNAIL_BLACK;
                } else {
                    urlCover = mPlaybackInfo.getThumbnail();
                }
            } else {
                urlCover = urlImgThumbnail;
            }
            TmpParamData.getInstance().setEntityPosterUrl(urlCover);
            ImageUtils.load(ivVideoCover, urlCover, R.drawable.background_black);
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
//                callAPIUpdateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
//                callAPIUpdateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
            }
            if (!isFromHandleError) {
                onStateReadyFirst();
            }
        } else {
            //goi changeskin realtime thi no ko vao if nen ko update tvDuration dc
            updateTvDuration();
        }
    }

    private void updateUIEachSkin() {
        int currentPlayerId = UizaData.getInstance().getCurrentPlayerId();
        if (currentPlayerId == R.layout.uz_player_skin_2 || currentPlayerId == R.layout.uz_player_skin_3) {
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

    private void updateUIPositionOfProgressBar() {
        if (uzPlayerView == null || progressBar == null) {
            return;
        }
        uzPlayerView.post(() -> {
            int marginL = uzPlayerView.getMeasuredWidth() / 2 - progressBar.getMeasuredWidth() / 2;
            int marginT = uzPlayerView.getMeasuredHeight() / 2 - progressBar.getMeasuredHeight() / 2;
            UIUtils.setMarginPx(progressBar, marginL, marginT, 0, 0);
        });
    }

    private void addPlayerView() {
        uzPlayerView = null;
        int skinId = UizaData.getInstance().getCurrentPlayerId();
        uzPlayerView = (UizaPlayerView) ((Activity) getContext()).getLayoutInflater().inflate(skinId, null);
        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        uzPlayerView.setVisibility(GONE);
        rootView.addView(uzPlayerView);
        setControllerAutoShow(isAutoShowController);
    }

    /*
     **Change skin via skin id resources
     * changeSkin(R.layout.uz_player_skin_1);
     */
    //TODO improve this func
    private boolean isRefreshFromChangeSkin;
    private long currentPositionBeforeChangeSkin;
    private boolean isCalledFromChangeSkin;

    /*
     ** change skin of player (realtime)
     * return true if success
     */
    public boolean changeSkin(@LayoutRes int skinId) {
        if (uzPlayerManager == null) {
            return false;
        }
        if (UizaData.getInstance().isUseWithVDHView()) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_change_skin_with_vdhview));
        }
        if (uzPlayerManager.isPlayingAd()) {
            notifyError(UizaExceptionUtils.getExceptionChangeSkin());
            return false;
        }
        UizaData.getInstance().setCurrentPlayerId(skinId);
        isRefreshFromChangeSkin = true;
        isCalledFromChangeSkin = true;
        rootView.removeView(uzPlayerView);
        rootView.requestLayout();
        uzPlayerView = (UizaPlayerView) ((Activity) getContext()).getLayoutInflater().inflate(skinId, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        rootView.addView(uzPlayerView);
        rootView.requestLayout();
        findViews();
        resizeContainerView();
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        //setup chromecast
        if (AppUtils.checkChromeCastAvailable()) {
            setupChromeCast();
        }
        currentPositionBeforeChangeSkin = getCurrentPosition();
        releaseUzPlayerManager();
        updateUIDependOnLivestream();
        setTitle();
        checkToSetUpResource();
        updateUISizeThumbnail();
        if (uzCallback != null) {
            uzCallback.onSkinChange();
        }
        return true;
    }

    private void setupChromeCast() {
        uZChromeCast = new UizaChromeCast();
        uZChromeCast.setUZChromeCastListener(new UizaChromeCast.UZChromeCastListener() {
            @Override
            public void onConnected() {
                if (uzPlayerManager != null) {
                    lastCurrentPosition = getCurrentPosition();
                }
                handleConnectedChromecast();
            }

            @Override
            public void onDisconnected() {
                handleDisconnectedChromecast();
            }

            @Override
            public void addUIChromecast() {
                if (llTop != null) {
                    llTop.addView(uZChromeCast.getUzMediaRouteButton());
                }
                addUIChromecastLayer();
            }
        });
        uZChromeCast.setupChromeCast(getContext(), isTV);
    }

    private void updateTvDuration() {
        if (tvDuration != null) {
            if (isLivestream) {
                tvDuration.setText(DateUtils.convertMlsecondsToHMmSs(0));
            } else {
                tvDuration.setText(DateUtils.convertMlsecondsToHMmSs(getDuration()));
            }
        }
    }

    public void setProgressSeekbar(final UZVerticalSeekBar uzVerticalSeekBar, final int progressSeekbar) {
        if (uzVerticalSeekBar == null) {
            return;
        }
        uzVerticalSeekBar.setProgress(progressSeekbar);
    }

    private void setTextPosition(long currentMls) {
        if (tvPosition == null) return;
        if (isLivestream) {
            long duration = getDuration();
            long past = duration - currentMls;
            tvPosition.setText(HYPHEN + DateUtils.convertMlsecondsToHMmSs(past));
        } else {
            tvPosition.setText(DateUtils.convertMlsecondsToHMmSs(currentMls));
        }
    }

    private void updateUIIbRewIconDependOnProgress(long currentMls, boolean isCalledFromUZTimebarEvent) {
        if (isCalledFromUZTimebarEvent) {
            setTextPosition(currentMls);
        } else {
            if (isOnPreview) {//uzTimebar is displaying
                return;
            } else {
                setTextPosition(currentMls);
            }
        }
        if (isLivestream) return;
        if (ibRewIcon != null && ibFfwdIcon != null) {
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
    }

    //FOR TV
    public void updateUIFocusChange(@NonNull View view, boolean isFocus) {
        if (isFocus) {
            if (view instanceof UizaImageButton) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
                ((UizaImageButton) view).setColorFilter(Color.GRAY);
            } else if (view instanceof Button) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UizaPreviewTimeBar) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus_uz_timebar, R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        } else {
            if (view instanceof UizaImageButton) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
                ((UizaImageButton) view).clearColorFilter();
            } else if (view instanceof Button) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UizaPreviewTimeBar) {
                UizaUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus_uz_timebar, R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        }
    }

    private View firstViewHasFocus;

    private void handleFirstViewHasFocus() {
        if (firstViewHasFocus != null && uizaTVFocusChangeListener != null) {
            uizaTVFocusChangeListener.onFocusChange(firstViewHasFocus, true);
            firstViewHasFocus = null;
        }
    }

    private void updateUISizeThumbnail() {
        int screenWidth = ScreenUtils.getScreenWidth();
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
            UIUtils.setMarginDimen(uzTimebar, 5, 0, 5, 0);
        } else {
            UIUtils.setMarginDimen(uzTimebar, 0, 0, 0, 0);
        }
    }

    private void setMarginRlLiveInfo() {
        if (isLandscape) {
            UIUtils.setMarginDimen(rlLiveInfo, 50, 0, 50, 0);
        } else {
            UIUtils.setMarginDimen(rlLiveInfo, 5, 0, 5, 0);
        }
    }

    private void setTitle() {
        if (tvTitle != null) {
            tvTitle.setText(UizaData.getInstance().getEntityName());
        }
    }

    private void updateUIDependOnLivestream() {
        if (isCastingChromecast) {
            UIUtils.goneViews(ibPictureInPictureIcon);
        } else {
            if (isTablet && isTV) {//only hide ibPictureInPictureIcon if device is TV
                UIUtils.goneViews(ibPictureInPictureIcon);
            }
        }
        if (isLivestream) {
            UIUtils.visibleViews(rlLiveInfo);
            //TODO why set gone not work?
            setUIVisible(false, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        } else {
            UIUtils.goneViews(rlLiveInfo);
            //TODO why set visible not work?
            setUIVisible(true, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        }
        if (isTV) {
            UIUtils.goneViews(ibFullscreenIcon);
        }
    }

    private void setUIVisible(boolean visible, UizaImageButton... views) {
        for (UizaImageButton v : views) {
            if (v != null) {
                v.setUIVisible(visible);
            }
        }
    }

    protected void updateUIButtonVisibilities() {
        if (debugRootView != null) {
            debugRootView.removeAllViews();
        }
        if (getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if (getContext() == null) {
                    return;
                }
                Button button = new Button(getContext());
                button.setSoundEffectsEnabled(false);
                int label;
                switch (uzPlayerManager.getPlayer().getRendererType(i)) {
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
        UIUtils.visibleViews(rlMsg);
    }

    public void hideLayoutMsg() {
        UIUtils.goneViews(rlMsg);
    }

    private void updateLiveInfoTimeStartLive() {
        if (!isLivestream || getContext() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        long duration = now - startTime;
        String s = DateUtils.convertMlsecondsToHMmSs(duration);
        if (tvLiveTime != null) {
            tvLiveTime.setText(s);
        }
        if (uzLiveContentCallback != null) {
            uzLiveContentCallback.onUpdateLiveInfoTimeStartLive(duration, s);
        }
//        callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
    }

    private void updateUIEndScreen() {
        if (getContext() == null) {
            return;
        }
        if (isOnPlayerEnded) {
            Timber.i("Video or Stream is ended !");
            setVisibilityOfPlayPauseReplay(true);
            showController();
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
                uzPlayerView.setControllerHideOnTouch(false);
            }
        } else {
            setVisibilityOfPlayPauseReplay(false);
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
            }
            setHideControllerOnTouch(isHideOnTouch);
        }
    }

    private void setVisibilityOfPlayPauseReplay(boolean isShowReplay) {
        if (isShowReplay) {
            UIUtils.goneViews(ibPlayIcon, ibPauseIcon);
            if (ibReplayIcon != null) {
                UIUtils.visibleViews(ibReplayIcon);
                ibReplayIcon.requestFocus();
            }
        } else {
            updateUIButtonPlayPauseDependOnIsAutoStart();
            UIUtils.goneViews(ibReplayIcon);
        }
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
        UIUtils.setVisibilityViews(visibilityOfPlaylistFolderController, ibPlaylistFolderIcon,
                ibSkipNextIcon, ibSkipPreviousIcon);
        //Có play kiểu gì đi nữa thì cũng phải ibPlayIcon GONE và ibPauseIcon VISIBLE và ibReplayIcon GONE
        setVisibilityOfPlayPauseReplay(false);
    }

    public void hideUzTimebar() {
        UIUtils.goneViews(previewFrameLayout, ivThumbnail, uzTimebar);
    }

    private List<UizaItem> showUZTrackSelectionDialog(final View view, boolean showDialog) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = ((Button) view).getText();
            int rendererIndex = (int) view.getTag();
            boolean allowAdaptiveSelections = false;
            final Pair<AlertDialog, UizaTrackSelectionView> dialogPair = UizaTrackSelectionView.getDialog(getContext(), title, uzPlayerManager.getTrackSelector(), rendererIndex);
            dialogPair.second.setShowDisableOption(false);
            dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
            dialogPair.second.setCallback(() -> handler.postDelayed(() -> {
                {
                    if (dialogPair.first == null) {
                        return;
                    }
                    dialogPair.first.cancel();
                }
            }, 300));
            if (showDialog) {
                UizaUtil.showUizaDialog(getContext(), dialogPair.first);
            }
            return dialogPair.second.getUZItemList();
        }
        return null;
    }

    public void setUzTimebarBottom() {
        if (uzPlayerView == null) {
            throw new NullPointerException("uzPlayerView cannot be null");
        }
        if (uzTimebar == null) {
            throw new NullPointerException("uzTimebar cannot be null");
        }
        if (uzPlayerView.getResizeMode() != AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT) {
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        }
    }

    public int getHeightUZVideo() {
        if (rootView == null) {
            return 0;
        }
        if (isSetUZTimebarBottom) {
            int hRootView = UIUtils.getHeightOfView(rootView);
            int hUZTimebar = getHeightUZTimeBar();
            return hRootView - hUZTimebar / 2;
        } else {
            return UIUtils.getHeightOfView(rootView);
        }
    }

    public void setBackgroundColorBkg(int color) {
        if (bkg != null) {
            bkg.setBackgroundColor(color);
        }
    }

    public void setBackgroundColorUZVideoRootView(int color) {
        RelativeLayout uzVideoRootView = findViewById(R.id.root_view_uz_video);
        if (uzVideoRootView != null) {
            uzVideoRootView.setBackgroundColor(color);
        }
    }

    public void setMarginDependOnUZTimeBar(View view) {
        if (view == null || uzTimebar == null) {
            return;
        }
        int heightUZTimebar;
        if (isLandscape) {
            UIUtils.setMarginPx(view, 0, 0, 0, 0);
        } else {
            heightUZTimebar = getHeightUZTimeBar();
            UIUtils.setMarginPx(view, 0, 0, 0, heightUZTimebar / 2);
        }
    }

    private int progressBarColor = Color.WHITE;

    public void setProgressBarColor(int progressBarColor) {
        if (progressBar != null) {
            this.progressBarColor = progressBarColor;
            UIUtils.setColorProgressBar(progressBar, progressBarColor);
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

    private void updateUIPlayerInfo() {
        if (playerInfor == null || uzPlayerView == null) {
            return;
        }
        Logo logo = playerInfor.getLogo();
        if (logo == null) {
            return;
        }
        boolean isDisplay = logo.isDisplay();
        if (isDisplay) {
            String position = logo.getPosition();
            ivLogo = new ImageView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ConvertUtils.dp2px(50f), ViewGroup.LayoutParams.WRAP_CONTENT);
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
            if (uzPlayerView.getOverlayFrameLayout() != null) {
                uzPlayerView.getOverlayFrameLayout().addView(ivLogo, layoutParams);
            }
            ivLogo.setOnClickListener(this);
            ImageUtils.load(ivLogo, logo.getLogo());
        } else {
            ivLogo = null;
        }
    }
    /** ======== END UI ========= */

    /**
     * ======== START EVENT =====
     */
    private UZLiveContentCallback uzLiveContentCallback;
    private ProgressListener progressListener;
    private PreviewView.OnPreviewChangeListener onPreviewChangeListener;
    private UizaVideoViewItemClick uizaVideoViewItemClick;
    private UZCallback uzCallback;
    private UizaTVFocusChangeListener uizaTVFocusChangeListener;
    private UizaPlayerView.ControllerStateCallback controllerStateCallback;
    protected AudioListener audioListener;
    protected MetadataOutput metadataOutput;
    protected Player.EventListener eventListener;
    protected VideoListener videoListener;
    protected TextOutput textOutput;

    public void addUZLiveContentCallback(UZLiveContentCallback uzLiveContentCallback) {
        this.uzLiveContentCallback = uzLiveContentCallback;
    }

    public void addUZCallback(UZCallback uzCallback) {
        this.uzCallback = uzCallback;
    }

    public void setUizaTVFocusChangeListener(UizaTVFocusChangeListener uizaTVFocusChangeListener) {
        this.uizaTVFocusChangeListener = uizaTVFocusChangeListener;
        handleFirstViewHasFocus();
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public void setOnPreviewChangeListener(PreviewView.OnPreviewChangeListener onPreviewChangeListener) {
        this.onPreviewChangeListener = onPreviewChangeListener;
    }

    public void setUizaVideoViewItemClick(UizaVideoViewItemClick uizaVideoViewItemClick) {
        this.uizaVideoViewItemClick = uizaVideoViewItemClick;
    }

    public void addControllerStateCallback(final UizaPlayerView.ControllerStateCallback controllerStateCallback) {
        this.controllerStateCallback = controllerStateCallback;
    }

    public void addOnTouchEvent(UizaPlayerView.OnTouchEvent onTouchEvent) {
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

    //=============================================================================================END EVENT
    //=============================================================================================START CALL API
    private boolean isCalledApiGetDetailEntity;
    private boolean isCalledAPIGetUrlIMAAdTag;
    private PlaybackInfo mPlaybackInfo;

    private void callAPIGetDetailEntity() {
        //Neu da ton tai Data roi thi no duoc goi tu pip, minh ko can phai call api lay detail entity lam gi nua
        boolean isDataExist = UizaData.getInstance().getPlaybackInfo() != null;
        if (isDataExist) {
            //init player khi user click vào fullscreen của floating view (pic)
            isCalledApiGetDetailEntity = true;
            mPlaybackInfo = UizaData.getInstance().getPlaybackInfo();
            handleDataCallAPI();
        } else {
            if (isLivestream) {
                UizaLiveService liveService = UizaClientFactory.getLiveService();
                RxBinder.bind(liveService.getEntity(entityId).map(LiveEntity::getPlaybackInfo), data -> {
                    if (data.canPlay())
                        handleDetailEntityResponse(data);
                    else {
                        UizaData.getInstance().setSettingPlayer(false);
                        handleError(UizaExceptionUtils.getExceptionCannotGetDetailEntitity());
                    }
                }, throwable -> {
                    UizaData.getInstance().setSettingPlayer(false);
                    handleError(UizaExceptionUtils.getExceptionCannotGetDetailEntitity());
                });
            } else {
                UizaVideoService vodService = UizaClientFactory.getVideoService();
                RxBinder.bind(vodService.getEntity(entityId).map(VODEntity::getPlaybackInfo), data -> {
                    if (data.canPlay())
                        handleDetailEntityResponse(data);
                    else {
                        UizaData.getInstance().setSettingPlayer(false);
                        handleError(UizaExceptionUtils.getExceptionCannotGetDetailEntitity());
                    }
                }, throwable -> {
                    UizaData.getInstance().setSettingPlayer(false);
                    handleError(UizaExceptionUtils.getExceptionCannotGetDetailEntitity());
                });
            }
        }
    }

    private void handleDetailEntityResponse(PlaybackInfo playback) {
        isCalledApiGetDetailEntity = true;
        this.mPlaybackInfo = playback;
        //set video cover o moi case, ngoai tru
        //click tu pip entity thi ko can show video cover
        //click tu pip playlist folder lan dau tien thi ko can show video cover, neu nhan skip next hoac skip prev thi se show video cover
        play(playback);
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
        handleDataCallAPI();
    }

    @Deprecated
    private void callAPIGetPlayerInfor() {
        int resLayout = UizaData.getInstance().getCurrentPlayerId();
        if (resLayout != R.layout.uz_player_skin_0 && resLayout != R.layout.uz_player_skin_1 && resLayout != R.layout.uz_player_skin_2 && resLayout != R.layout.uz_player_skin_3) {
            return;
        }
        UizaVideoService service = UizaClientFactory.getVideoService();
        String playerInforId = UizaData.getInstance().getPlayerInfoId();
        if (TextUtils.isEmpty(playerInforId)) {
            return;
        }
        RxBinder.bind(service.getPlayerInfo(playerInforId),
                pi -> playerInfor = pi,
                throwable -> handleError(UizaExceptionUtils.getExceptionPlayerInfor()));
    }

    @Deprecated
    private void callAPIGetUrlIMAAdTag() {
        boolean isUrlIMAAdExist = UizaData.getInstance().getUrlIMAAd() != null;
        if (isUrlIMAAdExist) {
            isCalledAPIGetUrlIMAAdTag = true;
            urlIMAAd = UizaData.getInstance().getUrlIMAAd();
            handleDataCallAPI();
        } else {
            UizaVideoService service = UizaClientFactory.getVideoService();
            RxBinder.bind(service.getCuePoint(entityId).map(ListWrap::getData), ads -> {
                isCalledAPIGetUrlIMAAdTag = true;
                if (ListUtils.isEmpty(ads)) {
                    urlIMAAd = "";
                } else {
                    //Hien tai chi co the play ima ad o item thu 0
                    Ad ad = ads.get(0);
                    if (ad != null) {
                        urlIMAAd = ad.getLink();
                    }
                }
                handleDataCallAPI();
            }, throwable -> Timber.e(throwable, "callAPIGetUrlIMAAdTag onFail but ignored (dont care):"));
        }
    }


    private void callAPIGetSubtitles() {
        if (mPlaybackInfo == null) return;

        UizaVideoService service = UizaClientFactory.getVideoService();
        RxBinder.bind(service.getSubtitles(mPlaybackInfo.getId()).map(ListWrap::getData),
                subtitles -> {
                    subtitleList.clear();
                    subtitleList.addAll(subtitles);
                    checkToSetUpResource();
                }, throwable -> checkToSetUpResource());
    }

    //=============================================================================================END CALL API

    private void handleDataCallAPI() {
//        if (isCalledApiGetDetailEntity && isCalledAPIGetUrlIMAAdTag) {
        if (UizaData.getInstance().getPlaybackInfo() == null) {
            UizaData.getInstance().setPlaybackInfo(mPlaybackInfo);
            UizaData.getInstance().setUrlIMAAd(urlIMAAd);
            //TODO iplm url thumbnail seekbar
            UizaData.getInstance().setUrlThumbnailsPreviewSeekBar(null);
//            }
            checkData();
        }
    }

    private boolean isGetClickedPip;

    private void checkData() {
        UizaData.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UizaData.getInstance().getEntityId() == null || UizaData.getInstance().getEntityId().isEmpty()) {
            Timber.e("checkData getEntityId null or empty -> return");
            handleError(UizaExceptionUtils.getExceptionEntityId());
            UizaData.getInstance().setSettingPlayer(false);
            return;
        }
        isLivestream = UizaData.getInstance().isLiveStream();
        isGetClickedPip = UizaUtil.getClickedPip(getContext());
        Timber.d("checkData isLivestream: %b, isGetClickedPip: %b", isLivestream, isGetClickedPip);
        if (!isGetClickedPip) {
            setDefaultValueForFlagIsTracked();
        }
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
//            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        setTitle();
//        callAPIGetLinkPlay();
        trackUizaEventDisplay();
        trackUizaEventPlaysRequested();
    }

    private void checkToSetUpResource() {
        if (mPlaybackInfo != null) {
            List<String> listLinkPlay = new ArrayList<>();
            List<String> urlList = mPlaybackInfo.getUrls();
            if (isLivestream) {
                //Bat buoc dung linkplay m3u8 cho nay, do bug cua system
                for (String url : urlList) {
                    if (url.toLowerCase().endsWith(M3U8_EXTENSION)) {
                        listLinkPlay.add(url);
                    }
                }
            } else {
                for (String url : urlList) {
                    if (url.toLowerCase().endsWith(MPD_EXTENSION)) {
                        listLinkPlay.add(url);
                    }
                }
                for (String url : urlList) {
                    if (url.toLowerCase().endsWith(M3U8_EXTENSION)) {
                        listLinkPlay.add(url);
                    }
                }
            }
            if (listLinkPlay.isEmpty()) {
                handleErrorNoData();
                return;
            }
            if (countTryLinkPlayError >= listLinkPlay.size()) {
                if (ConnectivityUtils.isConnected(getContext())) {
                    handleError(UizaExceptionUtils.getExceptionTryAllLinkPlay());
                } else {
                    notifyError(UizaExceptionUtils.getExceptionNoConnection());
                }
                return;
            }
            String linkPlay = listLinkPlay.get(countTryLinkPlayError);
            addTrackingMuiza(Constants.MUIZA_EVENT_READY);
            boolean isAdsDevendency = AppUtils.isAdsDependencyAvailable();
            if (isCalledFromChangeSkin) {
                //if called from func changeSkin(), dont initDataSource with uilIMA Ad.
                initDataSource(linkPlay, null, UizaData.getInstance().getUrlThumbnailsPreviewSeekBar(), subtitleList, isAdsDevendency);
            } else {
                initDataSource(linkPlay, UizaData.getInstance().getUrlIMAAd(), UizaData.getInstance().getUrlThumbnailsPreviewSeekBar(), subtitleList, isAdsDevendency);
            }
            if (uzCallback != null) {
                uzCallback.isInitResult(false, true, UizaData.getInstance().getPlaybackInfo());
            }
            initUizaPlayerManager();
        } else {
            handleError(UizaExceptionUtils.getExceptionSetup());
        }
    }

    private long timestampInitDataSource;

    private void initDataSource(String linkPlay, String urlIMAAd, String urlThumbnailsPreviewSeekbar, List<Subtitle> subtitleList, boolean includeAds) {

        if (ListUtils.isEmpty(subtitleList)) {
            // hide the cc (subtitle) button
            UIUtils.goneViews(ibCcIcon);
        }

        timestampInitDataSource = System.currentTimeMillis();
        Timber.d("-------------------->initDataSource linkPlay %s", linkPlay);
        TmpParamData.getInstance().setEntitySourceUrl(linkPlay);
        TmpParamData.getInstance().setTimeFromInitEntityIdToAllApiCalledSuccess(System.currentTimeMillis() - timestampBeforeInitNewSession);
        if (includeAds) {
            uzPlayerManager = new UizaPlayerManager(this, linkPlay, urlIMAAd, urlThumbnailsPreviewSeekbar, subtitleList);
            ((UizaPlayerManager) uzPlayerManager).addAdPlayerCallback(new UizaAdPlayerCallback() {
                @Override
                public void onPlay() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onPlay();
                }

                @Override
                public void onVolumeChanged(int i) {
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onVolumeChanged(i);
                }

                @Override
                public void onPause() {
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onPause();
                }

                @Override
                public void onLoaded() {
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onLoaded();
                }

                @Override
                public void onResume() {
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onResume();
                }

                @Override
                public void onEnded() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onEnded();
                }

                @Override
                public void onError() {
                    updateTvDuration();
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onError();
                }

                @Override
                public void onBuffering() {
                    if (videoAdPlayerCallback != null) videoAdPlayerCallback.onBuffering();
                }
            });
        } else {
            uzPlayerManager =
                    new UizaPlayerNoAdsManager(this, linkPlay, urlThumbnailsPreviewSeekbar, subtitleList);
        }
        if (uzTimebar != null) {
            boolean disable = urlThumbnailsPreviewSeekbar == null || urlThumbnailsPreviewSeekbar.isEmpty();
            uzTimebar.setEnabled(!disable);
            uzTimebar.setPreviewLoader(uzPlayerManager);
        }
        uzPlayerManager.setProgressListener(new VideoViewBase.ProgressListener() {
            @Override
            public void onAdEnded() {
                setDefaultUseController(isDefaultUseController());
                if (progressListener != null) {
                    progressListener.onAdEnded();
                }
            }

            @Override
            public void onAdProgress(int s, int duration, int percent) {
                if (progressListener != null) {
                    progressListener.onAdProgress(s, duration, percent);
                }
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                TmpParamData.getInstance().setPlayerPlayheadTime(s);
                updateUIIbRewIconDependOnProgress(currentMls, false);
                trackProgress(s, percent);
                callAPITrackMuiza(s);
                if (progressListener != null) {
                    progressListener.onVideoProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (progressListener != null) {
                    progressListener.onPlayerStateChanged(playWhenReady, playbackState);
                }
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                if (progressListener != null) {
                    progressListener.onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }
        });
        uzPlayerManager.setDebugCallback(this::updateUIButtonVisibilities);

        uzPlayerManager.setBufferCallback((bufferedDurationUs, playbackSpeed) -> statsForNerdsView.setBufferedDurationUs(bufferedDurationUs));
    }

    protected void onStateReadyFirst() {
        long pageLoadTime = System.currentTimeMillis() - timestampBeforeInitNewSession;
        TmpParamData.getInstance().setPageLoadTime(pageLoadTime);
        addTrackingMuiza(Constants.MUIZA_EVENT_VIEWSTART);
        TmpParamData.getInstance().setViewStart(System.currentTimeMillis());
        TmpParamData.getInstance().setViewTimeToFirstFrame(System.currentTimeMillis());
        updateTvDuration();
        updateUIButtonPlayPauseDependOnIsAutoStart();
        updateUIDependOnLivestream();
        if (isSetUZTimebarBottom) {
            UIUtils.visibleViews(uzPlayerView);
        }
        resizeContainerView();
        //enable from playPlaylistPosition() prevent double click
        setClickableForViews(true, ibSkipPreviousIcon, ibSkipNextIcon);
        if (isGetClickedPip) {
            Timber.d("getClickedPip true -> setPlayWhenReady true");
            uzPlayerManager.getPlayer().setPlayWhenReady(true);
        }
        if (uzCallback != null) {
            Timber.d("onStateReadyFirst ===> isInitResult");
            uzCallback.isInitResult(true, true, UizaData.getInstance().getPlaybackInfo());
        }
        if (isCastingChromecast) {
            replayChromeCast();
        }
        updateUIPlayerInfo();
        TmpParamData.getInstance().setSessionStart(System.currentTimeMillis());
        long playerStartUpTime = System.currentTimeMillis() - timestampInitDataSource;
        TmpParamData.getInstance().setPlayerStartupTime(playerStartUpTime);
        trackUizaEventVideoStarts();
        trackUizaCCUForLiveStream();
        pingHeartBeat();
        UizaData.getInstance().setSettingPlayer(false);
    }

    private void initUizaPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.init();
            addTrackingMuiza(Constants.MUIZA_EVENT_LOADSTART);
            if (isGetClickedPip && !isPlayPlaylistFolder()) {
                uzPlayerManager.getPlayer().setPlayWhenReady(false);
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

    //=============================================================================================START TRACKING
    private int oldPercent = -1;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    private void setDefaultValueForFlagIsTracked() {
        UizaTrackingUtil.clearAllValues(getContext());
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    protected void trackProgress(int s, int percent) {
        //dont track if user use custom linkplay
        if (isInitCustomLinkPlay) {
            return;
        }
        //track event view (after video is played 5s)
        if (s == (isLivestream ? 3 : 5)) {
            if (!UizaTrackingUtil.isTrackedEventTypeView(getContext())) {
                callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIEW),
                        () -> UizaTrackingUtil.setTrackingDoneWithEventTypeView(getContext(), true));
            }
        }
        //dont track play_throught if entity is live
        if (isLivestream) {
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
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought100(getContext())) {
                isTracked100 = true;
            } else {
                callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_100, Constants.EVENT_TYPE_PLAY_THROUGHT), () -> UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(getContext(), true));
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought75(getContext())) {
                isTracked75 = true;
            } else {
                callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_75, Constants.EVENT_TYPE_PLAY_THROUGHT), () -> UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(getContext(), true));
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought50(getContext())) {
                isTracked50 = true;
            } else {
                callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_50, Constants.EVENT_TYPE_PLAY_THROUGHT), () -> UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(getContext(), true));
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought25(getContext())) {
                isTracked25 = true;
            } else {
                callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_25, Constants.EVENT_TYPE_PLAY_THROUGHT), () -> UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(getContext(), true));
            }
        }
    }

    private void callAPITrackUiza(final UizaTracking uizaTracking, final UizaTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UizaTrackingService service = UizaClientFactory.getTrackingService();
        RxBinder.bind(service.track(uizaTracking), tracking -> {
            if (uizaTrackingCallback != null) {
                uizaTrackingCallback.onTrackingSuccess();
            }
        }, Timber::e);
    }

    private void trackUizaEventVideoStarts() {
        if (isInitCustomLinkPlay) {
            Timber.d("trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UizaTrackingUtil.isTrackedEventTypeVideoStarts(getContext())) {
            callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIDEO_STARTS),
                    () -> UizaTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(getContext(), true));
        }
    }

    private void trackUizaEventDisplay() {
        if (isInitCustomLinkPlay) {
            Timber.d("trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UizaTrackingUtil.isTrackedEventTypeDisplay(getContext())) {
            callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_DISPLAY),
                    () -> UizaTrackingUtil.setTrackingDoneWithEventTypeDisplay(getContext(), true));
        }
    }

    private void trackUizaEventPlaysRequested() {
        if (isInitCustomLinkPlay) {
            Timber.d("trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UizaTrackingUtil.isTrackedEventTypePlaysRequested(getContext())) {
            callAPITrackUiza(UizaData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_PLAYS_REQUESTED),
                    () -> UizaTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(getContext(), true));
        }
    }

    private final int INTERVAL_HEART_BEAT = 10000;

    private void pingHeartBeat() {
        if (getContext() == null || TextUtils.isEmpty(cdnHost)) {
            Timber.e("Error cannot call API pingHeartBeat() -> cdnHost: %s, entityName: %s -> return", cdnHost, UizaData.getInstance().getEntityName());
            return;
        }
        if (activityIsPausing) {
            rePingHeartBeat();
            Timber.e("Error cannot call API pingHeartBeat() because activity is pausing %s", UizaData.getInstance().getEntityName());
            return;
        }
        UizaHeartBeatService service = UizaClientFactory.getHeartBeatService();
        String cdnName = cdnHost;
        String session = uuid.toString();
        RxBinder.bind(service.pingHeartBeat(cdnName, session),
                result -> rePingHeartBeat(),
                throwable -> {
                    Timber.e(throwable, "pingHeartBeat onFail:");
                    rePingHeartBeat();
                });
    }

    private void rePingHeartBeat() {
        handler.postDelayed(() -> pingHeartBeat(), INTERVAL_HEART_BEAT);
    }

    private final int INTERVAL_TRACK_CCU = 3000;

    private void trackUizaCCUForLiveStream() {
        if (!isLivestream || isInitCustomLinkPlay) {
            Timber.e("Error cannot trackUizaCCUForLiveStream() -> return %s", UizaData.getInstance().getEntityName());
            return;
        }
        if (activityIsPausing) {
            reTrackUizaCCUForLiveStream();
            Timber.e("Error cannot trackUizaCCUForLiveStream() because activity is pausing %s", UizaData.getInstance().getEntityName());
            return;
        }
        UizaTrackingService service = UizaClientFactory.getTrackingService();
        UizaTrackingCCU uizaTrackingCCU = new UizaTrackingCCU();
        uizaTrackingCCU.setDt(DateUtils.getCurrent(DateUtils.FORMAT_1));
        uizaTrackingCCU.setHo(cdnHost);
        uizaTrackingCCU.setAi("");
        uizaTrackingCCU.setSn(UizaData.getInstance().getChannelName()); // stream name
        uizaTrackingCCU.setDi(DeviceUtils.getDeviceId(getContext()));
        uizaTrackingCCU.setUa(Constants.USER_AGENT);
        RxBinder.bind(service.trackCCU(uizaTrackingCCU),
                result -> reTrackUizaCCUForLiveStream(),
                throwable -> reTrackUizaCCUForLiveStream());
    }

    private void reTrackUizaCCUForLiveStream() {
        handler.postDelayed(() -> trackUizaCCUForLiveStream(), INTERVAL_TRACK_CCU);
    }

    protected void addTrackingMuizaError(String event, UizaException e) {
        if (!isInitCustomLinkPlay) {
            UizaData.getInstance().addTrackingMuiza(getContext(), event, e);
        }
    }

    protected void addTrackingMuiza(String event) {
        if (!isInitCustomLinkPlay) {
            UizaData.getInstance().addTrackingMuiza(getContext(), event);
        }
    }

    private boolean isTrackingMuiza;

    private void callAPITrackMuiza(int s) {
        if (isInitCustomLinkPlay || (s <= 0 || s % 10 != 0) || isTrackingMuiza || UizaData.getInstance().isMuizaListEmpty()) {
            return;
        }
        isTrackingMuiza = true;
        final List<Muiza> muizaListToTracking = new ArrayList<>(UizaData.getInstance().getMuizaList());
        UizaData.getInstance().clearMuizaList();
        UizaTrackingService service = UizaClientFactory.getTrackingService();
        RxBinder.bind(service.trackMuiza(muizaListToTracking),
                result -> isTrackingMuiza = false,
                throwable -> {
                    isTrackingMuiza = false;
                    UizaData.getInstance().addListTrackingMuiza(muizaListToTracking);
                });
    }
    //=============================================================================================END TRACKING

    //=============================================================================================START EVENTBUS
    private boolean isCalledFromConnectionEventBus = false;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        if (event == null || uzPlayerManager == null) {
            return;
        }
        if (!event.isConnected()) {
            notifyError(UizaExceptionUtils.getExceptionNoConnection());
        } else {
            LDialogUtil.clearAll();
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

    private long positionMiniPlayer;

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

    //listen msg from service FloatUizaVideoService
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromService msg) {
        if (msg == null || uzPlayerManager == null) {
            return;
        }
        //click open app of mini player
        if (msg instanceof ComunicateMng.MsgFromServiceOpenApp) {
            Timber.d("miniplayer STEP 6");
            try {
                positionMiniPlayer = ((ComunicateMng.MsgFromServiceOpenApp) msg).getPositionMiniPlayer();
                Class classNamePfPlayer = Class.forName(((Activity) getContext()).getClass().getName());
                Intent intent = new Intent(getContext(), classNamePfPlayer);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, UizaData.getInstance().getEntityId());
                getContext().startActivity(intent);
            } catch (ClassNotFoundException e) {
                Timber.e(e, "onMessageEvent open app ClassNotFoundException");
            }
            return;
        }
        //when pip float view init success
        if (uzCallback != null && msg instanceof ComunicateMng.MsgFromServiceIsInitSuccess) {
            //Ham nay duoc goi khi player o FloatUizaVideoService da init xong
            //Nhiem vu la minh se gui vi tri hien tai sang cho FloatUizaVideoService no biet
            Timber.d("miniplayer STEP 3 UZVideo biet FloatUizaVideoService da init xong -> gui lai content position cua UZVideo cho FloatUizaVideoService");
            ComunicateMng.MsgFromActivityPosition msgFromActivityPosition = new ComunicateMng.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);
            isInitMiniPlayerSuccess = true;
            uzCallback.onStateMiniPlayer(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        }
    }

    public void sendEventInitSuccess() {
        ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
        msgFromActivityIsInitSuccess.setInitSuccess(true);
        ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
    }
    //=============================================================================================END EVENTBUS

    //=============================================================================================START CHROMECAST


    private void handleConnectedChromecast() {
        isCastingChromecast = true;
        isCastPlayerPlayingFirst = false;
        playChromecast();
        updateUIChromecast();
    }

    private void handleDisconnectedChromecast() {
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        updateUIChromecast();
    }

    public boolean isCastingChromecast() {
        return isCastingChromecast;
    }

    //last current position lúc từ exoplayer switch sang cast player
    private long lastCurrentPosition;

    private void playChromecast() {
        if (UizaData.getInstance().getPlaybackInfo() == null || uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return;
        }
        showProgress();
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

//        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UizaData.getInstance().getPlayback().getDescription());
//        movieMetadata.putString(MediaMetadata.KEY_TITLE, UizaData.getInstance().getPlayback().getEntityName());
//        movieMetadata.addImage(new WebImage(Uri.parse(UizaData.getInstance().getPlayback().getThumbnail())));

        // NOTE: The receiver app (on TV) should Satisfy CORS requirements
        // https://developers.google.com/cast/docs/android_sender/media_tracks#satisfy_cors_requirements
        List<MediaTrack> mediaTrackList = buildMediaTracks();
        long duration = getDuration();
        if (duration < 0) {
            Timber.e("invalid duration -> cannot play chromecast");
            return;
        }

        MediaInfo mediaInfo = new MediaInfo.Builder(uzPlayerManager.getLinkPlay())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .setMediaTracks(mediaTrackList)
                .setStreamDuration(duration)
                .build();

        //play chromecast with full screen control
        //UizaData.getInstance().getCasty().getPlayer().loadMediaAndPlay(mediaInfo, true, lastCurrentPosition);

        //play chromecast without screen control
        UizaData.getInstance().getCasty().getPlayer().loadMediaAndPlayInBackground(mediaInfo, true, lastCurrentPosition);

        UizaData.getInstance().getCasty().getPlayer().getRemoteMediaClient().addProgressListener((currentPosition, duration1) -> {
            if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                hideProgress();
                isCastPlayerPlayingFirst = true;
            }
            if (currentPosition > 0) {
                uzPlayerManager.seekTo(currentPosition);
            }
        }, 1000);

    }

    private List<MediaTrack> buildMediaTracks() {
        List<MediaTrack> result = new ArrayList<>();
        if (ListUtils.isEmpty(subtitleList)) return result;
        for (int i = 0; i < subtitleList.size(); i++) {
            Subtitle subtitle = subtitleList.get(i);
            if (subtitle.getStatus() == Subtitle.Status.DISABLE) continue;
            MediaTrack subtitleTrack = new MediaTrack.Builder(i + 1001/* ID is unique */, MediaTrack.TYPE_TEXT)
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

    private boolean isCastPlayerPlayingFirst;

    /* khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnect thì local player sẽ resume*/
    private void updateUIChromecast() {
        if (uzPlayerManager == null || rlChromeCast == null || isTV) {
            return;
        }
        if (isCastingChromecast) {
            uzPlayerManager.pause();
            uzPlayerManager.setVolume(0f);
            UIUtils.visibleViews(rlChromeCast, ibPlayIcon);
            UIUtils.goneViews(ibPauseIcon);
//            UIUtils.goneViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon, ibPlayIcon, ibPauseIcon, ibVolumeIcon);
            //casting player luôn play first với volume not mute
            //UizaData.getInstance().getCasty().setVolume(0.99);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
            }
        } else {
            uzPlayerManager.resume();
            uzPlayerManager.setVolume(0.99f);
            UIUtils.goneViews(rlChromeCast, ibPlayIcon);
            UIUtils.visibleViews(ibPauseIcon);
//            UIUtils.visibleViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon, ibPauseIcon, ibVolumeIcon);
            //TODO iplm volume mute on/off o cast player
            //khi quay lại exoplayer từ cast player thì mặc định sẽ bật lại âm thanh (dù cast player đang mute hay !mute)
            //uzPlayerManager.setVolume(0.99f);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
            }
        }
    }

    //=============================================================================================END CHROMECAST
    private void scheduleJob() {
        if (getContext() == null) {
            return;
        }
        JobInfo myJob;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myJob = new JobInfo.Builder(0, new ComponentName(getContext(), LConnectifyService.class))
                    .setRequiresCharging(true)
                    .setMinimumLatency(1000)
                    .setOverrideDeadline(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(myJob);
            }
        }
    }

    private StatsForNerdsView statsForNerdsView;

    // ===== Stats For Nerds =====
    private void initStatsForNerds() {
        getPlayer().addAnalyticsListener(statsForNerdsView);
    }


    public UizaChromeCast getuZChromeCast() {
        return uZChromeCast;
    }

    private void addUIChromecastLayer() {
        rlChromeCast = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlChromeCastParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        rlChromeCast.setBackgroundColor(Color.BLACK);
        ibsCast = new UizaImageButton(getContext());
        ibsCast.setBackgroundColor(Color.TRANSPARENT);
        ibsCast.setImageResource(R.drawable.cast);
        RelativeLayout.LayoutParams ibsCastParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private UizaAdPlayerCallback videoAdPlayerCallback;

    public void addVideoAdPlayerCallback(UizaAdPlayerCallback uzAdPlayerCallback) {
        if (AppUtils.isAdsDependencyAvailable()) {
            this.videoAdPlayerCallback = uzAdPlayerCallback;
        } else {
            throw new NoClassDefFoundError(UizaException.ERR_506);
        }
    }

    protected void updateLiveStreamLatency(long latency) {
        statsForNerdsView.showTextLiveStreamLatency();
        statsForNerdsView.setTextLiveStreamLatency(ConvertUtils.groupingSeparatorLong(latency));
    }

    protected void hideTextLiveStreamLatency() {
        statsForNerdsView.hideTextLiveStreamLatency();
    }


}

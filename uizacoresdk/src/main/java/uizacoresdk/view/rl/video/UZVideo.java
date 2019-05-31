package uizacoresdk.view.rl.video;

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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
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
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
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
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import uizacoresdk.interfaces.CallbackUZTimebar;
import uizacoresdk.interfaces.StateEndCallback;
import uizacoresdk.interfaces.UZAdStateChangedListener;
import uizacoresdk.interfaces.UZBufferCallback;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZLiveContentCallback;
import uizacoresdk.interfaces.UZLiveInfoChangedListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZTVCallback;
import uizacoresdk.interfaces.UZTimeBarChangedListener;
import uizacoresdk.interfaces.UZVideoBufferChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.interfaces.UZVideoTVListener;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.SensorOrientationChangeNotifier;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZInput;
import uizacoresdk.util.UZOsUtil;
import uizacoresdk.util.UZTrackingUtil;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.ComunicateMng;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.dlg.hq.UZItem;
import uizacoresdk.view.dlg.hq.UZTrackSelectionView;
import uizacoresdk.view.dlg.info.UZDlgInfoV1;
import uizacoresdk.view.dlg.playlistfolder.CallbackPlaylistFolder;
import uizacoresdk.view.dlg.playlistfolder.UZDlgPlaylistFolder;
import uizacoresdk.view.dlg.speed.UZDlgSpeed;
import uizacoresdk.view.floatview.FUZVideoService;
import uizacoresdk.view.rl.timebar.UZTimebar;
import uizacoresdk.view.rl.videoinfo.StatsForNerdsView;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.exception.UZExceptionUtil;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LSocialUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.core.utilities.connection.LConectifyService;
import vn.uiza.data.EventBusData;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.restclient.UZRestClientHeartBeat;
import vn.uiza.restapi.restclient.UZRestClientTracking;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.tracking.UizaTrackingCCU;
import vn.uiza.restapi.uiza.model.tracking.muiza.Muiza;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.restapi.uiza.model.v3.ad.Ad;
import vn.uiza.restapi.uiza.model.v3.ad.AdWrapper;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.Url;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.livestreaming.gettimestartlive.ResultTimeStartLive;
import vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v4.playerinfo.Logo;
import vn.uiza.restapi.uiza.model.v4.playerinfo.PlayerInfor;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.CallbackGetDetailEntity;
import vn.uiza.utils.util.ConvertUtils;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.utils.util.ViewUtils;
import vn.uiza.views.autosize.UZImageButton;
import vn.uiza.views.autosize.UZTextView;
import vn.uiza.views.seekbar.UZVerticalSeekBar;

/**
 * Created by loitp on 2/27/2019.
 */

public class UZVideo extends RelativeLayout
        implements PreviewView.OnPreviewChangeListener, View.OnClickListener, View.OnFocusChangeListener,
        UZPlayerView.ControllerStateCallback, SensorOrientationChangeNotifier.Listener {
    private static final String TAG = "TAG" + UZVideo.class.getSimpleName();
    private static final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private static final int DELAY_TO_GET_LIVE_INFORMATION = 15000;
    private static final String M3U8_EXTENSION = ".m3u8";
    private static final String MPD_EXTENSION = ".mpd";
    private static final String PLAY_THROUGH_100 = "100";
    private static final String PLAY_THROUGH_75 = "75";
    private static final String PLAY_THROUGH_50 = "50";
    private static final String PLAY_THROUGH_25 = "25";
    private static final String HYPHEN = "-";

    private int defaultValueBackwardForward = 10000;//10000 mls
    private int defaultValueControllerTimeout = 8000;//8000 mls
    private boolean isLivestream;
    private boolean isTablet;
    private String cdnHost;
    private boolean isTV;//current device is TV or not (smartphone, tablet)
    private View bkg;
    private RelativeLayout rootView, rlChromeCast;
    private UZPlayerManager uzPlayerManager;
    private ProgressBar progressBar;
    private LinearLayout llTop, debugLayout, debugRootView;
    private RelativeLayout rlLiveInfo, rlEndScreen;
    private FrameLayout previewFrameLayout;
    private UZTimebar uzTimebar;
    private ImageView ivThumbnail, ivVideoCover, ivLogo;
    private UZTextView tvPosition, tvDuration;
    private TextView tvTitle, tvLiveStatus, tvLiveView, tvLiveTime;
    private UZImageButton ibFullscreenIcon, ibPlayIcon, ibReplayIcon, ibRewIcon, ibFfwdIcon, ibBackScreenIcon,
            ibVolumeIcon, ibSettingIcon, ibCcIcon, ibPlaylistFolderIcon, ibHearingIcon, ibPictureInPictureIcon,
            ibSkipPreviousIcon, ibSkipNextIcon, ibSpeedIcon, ivLiveTime, ivLiveView, ibsCast;
    private TextView debugTextView, tvEndScreenMsg;
    //chromecast https://github.com/DroidsOnRoids/Casty
    private UZMediaRouteButton uzMediaRouteButton;
    private UZPlayerView uzPlayerView;

    private ResultGetLinkPlay mResultGetLinkPlay;
    private ResultGetTokenStreaming mResultGetTokenStreaming;
    private String urlIMAAd = null;
    private PlayerInfor playerInfor;
    private long startTime = Constants.UNKNOW;
    private boolean isSetUZTimebarBottom;
    private boolean isEnableMux;

    private StatsForNerdsView statsForNerdsView;
    private UZVideoVisualizeInfoHelper visualizeInfoHelper;

    public UZVideo(Context context) {
        super(context);
        onCreate();
    }

    public UZVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UZVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UZVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
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
        setupChromeCast();
        updateUISizeThumbnail();
        scheduleJob();
    }

    private void checkDevices() {
        isTablet = LDeviceUtil.isTablet(getContext());
        isTV = LDeviceUtil.isTV(getContext());
    }

    private void resizeContainerView() {
        setSize(getVideoWidth(), getVideoHeight());
    }

    private void startConectifyService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(getContext(), LConectifyService.class);
            getContext().startService(startServiceIntent);
        }
    }

    private void setupChromeCast() {
        if (isTV) return;
        uzMediaRouteButton = new UZMediaRouteButton(getContext());
        if (llTop != null) {
            llTop.addView(uzMediaRouteButton);
        }
        setUpMediaRouteButton();
        addUIChromecastLayer();
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
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getPlayer().getDuration();
    }

    /**
     * Returns an estimate of the position in the current content window or ad up to which data is buffered,
     * in milliseconds
     */
    public long getBufferedPosition() {
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getPlayer().getBufferedPosition();
    }

    /**
     * Returns an estimate of the percentage in the current content window or ad up to which data is buffered,
     * or 0 if no estimate is available.
     */
    public int getBufferedPercentage() {
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return getPlayer().getBufferedPercentage();
    }

    /**
     * Return the duration in milliseconds between current position & buffered position
     */
    public long getBufferedDuration() {
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        long bufferedDuration = getBufferedPosition() - getCurrentPosition();
        return bufferedDuration <= 0 ? 0 : bufferedDuration;
    }

    // Lay pixel dung cho custom UI like youtube, uzTimebar bottom of player controller
    public int getPixelAdded() {
        if (isSetUZTimebarBottom) {
            return getHeightUZTimeBar() / 2;
        }
        return 0;
    }

    /**
     * @deprecated use {@link UZVideo#getVideoWidth()} instead
     */
    @Deprecated
    public int getVideoW() {
        return getVideoWidth();
    }

    /**
     * @deprecated use {@link UZVideo#getVideoHeight()} instead
     */
    @Deprecated
    public int getVideoH() {
        return getVideoHeight();
    }

    /**
     * Gets the video width is rendering on player or 0 if it's not rendering
     * @return the width of video
     */
    public int getVideoWidth() {
        return uzPlayerManager == null ? 0 : uzPlayerManager.getVideoW();
    }

    /**
     * Gets the video height is rendering on player or 0 if it's not rendering
     * @return the height of video
     */
    public int getVideoHeight() {
        return uzPlayerManager == null ? 0 : uzPlayerManager.getVideoH();
    }

    /**
     * Gets the player width, this value is updated whenever view is laid out on your layout
     * @return the width of player
     */
    public int getPlayerWidth() {
        return uzPlayerView != null ? uzPlayerView.getWidth() : 0;
    }

    /**
     * Gets the player height, this value is updated whenever view is laid out on your layout
     * @return the height of player
     */
    public int getPlayerHeight() {
        return uzPlayerView != null ? uzPlayerView.getHeight() : 0;
    }

    //return pixel
    public int getHeightUZTimeBar() {
        return LUIUtil.getHeightOfView(uzTimebar);
    }

    // The current position of playing. the window means playable region,
    // which is all of the content if vod, and some portion of the content if live.
    public long getCurrentPosition() {
        if (uzPlayerManager == null) {
            return Constants.NOT_FOUND;
        }
        return uzPlayerManager.getCurrentPosition();
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

    /**
     * @deprecated use {@link UZVideo#getVideoWidth()} instead
     */
    public int getVideoProfileW() {
        if (uzPlayerManager == null) {
            return Constants.UNKNOW;
        }
        return uzPlayerManager.getVideoProfileW();
    }

    /**
     * @deprecated use {@link UZVideo#getVideoHeight()} instead
     */
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
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), width, height, isFreeSize);
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

    protected void handleError(UZException uzException) {
        if (uzException == null) {
            return;
        }
        notifyError(uzException);
        // Capture by Sentry, in uzException already contains Message, Error Code
        SentryUtils.captureException(uzException.getException());
        addTrackingMuizaError(Constants.MUIZA_EVENT_ERROR, uzException);
        if (isHasError) {
            return;
        }
        isHasError = true;
        UZData.getInstance().setSettingPlayer(false);
    }

    private void notifyError(UZException exception) {
        if (uzCallback != null) {
            uzCallback.onError(exception);
        }
        if (uzVideoStateChangedListener != null) {
            uzVideoStateChangedListener.onError(exception);
        }
    }

    private String entityId;
    private UUID uuid;
    private long timestampBeforeInitNewSession;

    protected void init(String entityId, boolean isClearDataPlaylistFolder) {
        LLog.d(TAG, "*****NEW SESSION**********************************************************************************************************************************");
        LLog.d(TAG, "entityId " + entityId);
        releaseUzPlayerManager();
        uuid = UUID.randomUUID();
        if (isClearDataPlaylistFolder) {
            UZData.getInstance().clearDataForPlaylistFolder();
        }
        if (entityId == null) {
            LLog.e(TAG, "init error because entityId == null -> called from PIP");
            try {
                if (TextUtils.isEmpty(UZData.getInstance().getData().getId())) {
                    notifyError(UZExceptionUtil.getExceptionEntityId());
                    LLog.e(TAG, "init error: entityId null or empty");
                    return;
                } else {
                    entityId = UZData.getInstance().getEntityId();
                }
            } catch (NullPointerException e) {
                notifyError(UZExceptionUtil.getExceptionEntityId());
                SentryUtils.captureException(e);
                LLog.e(TAG, "init NullPointerException: " + e.toString());
                return;
            }
        } else {
            timestampBeforeInitNewSession = System.currentTimeMillis();
            timestampOnStartPreview = 0;
            maxSeekLastDuration = 0;
            UZData.getInstance().clearUizaInput();
            TmpParamData.getInstance().clearAll();
            TmpParamData.getInstance().addPlayerViewCount();
            TmpParamData.getInstance().setSessionId(uuid.toString());
        }
        LLog.d(TAG, "isPlayWithPlaylistFolder " + UZData.getInstance().isPlayWithPlaylistFolder());
        handlePlayPlayListFolderUI();
        isCalledFromChangeSkin = false;
        isInitCustomLinkPlay = false;
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
        mResultGetTokenStreaming = null;
        isHasError = false;
        isOnPlayerEnded = false;
        isHandleErrorContentNotAvailable = false;
        this.entityId = entityId;
        LLog.d(TAG, "get entityId: " + entityId);
        UZData.getInstance().setSettingPlayer(true);
        setControllerShowTimeoutMs(defaultValueControllerTimeout);
        updateUIEndScreen();
        if (!LConnectivityUtil.isConnected(getContext())) {
            notifyError(UZExceptionUtil.getExceptionNoConnection());
            LLog.d(TAG, "!isConnected return");
            return;
        }
        callAPIGetDetailEntity();
        callAPIGetPlayerInfor();
        callAPIGetUrlIMAAdTag();
        callAPIGetTokenStreaming();
    }

    private void handlePlayPlayListFolderUI() {
        if (isPlayPlaylistFolder()) {
            setVisibilityOfPlaylistFolderController(VISIBLE);
        } else {
            setVisibilityOfPlaylistFolderController(GONE);
        }
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId) {
        init(entityId, true);
    }

    private boolean isInitCustomLinkPlay;//user pass any link (not use entityId or metadataId)

    public void initCustomLinkPlay(@NonNull String linkPlay, boolean isLivestream) {
        LLog.d(TAG, "*****NEW SESSION**********************************************************************************************************************************");
        LLog.d(TAG, "init linkPlay " + linkPlay);
        isInitCustomLinkPlay = true;
        isCalledFromChangeSkin = false;
        setVisibilityOfPlaylistFolderController(GONE);
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
        mResultGetTokenStreaming = null;
        this.entityId = null;
        UZData.getInstance().setSettingPlayer(true);
        setControllerShowTimeoutMs(defaultValueControllerTimeout);
        isOnPlayerEnded = false;
        isHandleErrorContentNotAvailable = false;
        updateUIEndScreen();
        isHasError = false;
        this.isLivestream = isLivestream;
        if (isLivestream) {
            startTime = Constants.UNKNOW;
        }
        setDefaultValueForFlagIsTracked();
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        updateUIDependOnLivestream();
        List<Subtitle> subtitleList = null;

        if (!LConnectivityUtil.isConnected(getContext())) {
            notifyError(UZExceptionUtil.getExceptionNoConnection());
            return;
        }
        initDataSource(linkPlay, UZData.getInstance().getUrlIMAAd(), UZData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
        if (uzCallback != null) {
            uzCallback.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        if (uzVideoStateChangedListener != null) {
            uzVideoStateChangedListener.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        initUizaPlayerManager();
    }

    public void initPlaylistFolder(String metadataId) {
        if (metadataId == null) {
            LLog.d(TAG, "initPlaylistFolder metadataId null -> called from PIP: " + isGetClickedPip);
        } else {
            LLog.d(TAG, "initPlaylistFolder metadataId " + metadataId + ", -> called from PIP: " + isGetClickedPip);
            UZData.getInstance().clearDataForPlaylistFolder();
        }
        isHasError = false;
        isClickedSkipNextOrSkipPrevious = false;
        callAPIGetListAllEntity(metadataId);
    }

    public void toggleStatsForNerds() {
        if (getPlayer() == null || visualizeInfoHelper == null) return;
        visualizeInfoHelper.toggleStatsForNerds();
    }

    private int countTryLinkPlayError = 0;

    private boolean isHandleErrorContentNotAvailable;

    protected void tryNextLinkPlay() {
        if (isLivestream) {
            //try to play 5 times
            if (countTryLinkPlayError >= 5) {
                if (!isHandleErrorContentNotAvailable) {
                    removeVideoCover(true);
                    handleError(UZExceptionUtil.getExceptionContentNotAvailable());
                    isHandleErrorContentNotAvailable = true;
                }
            }
            if (uzPlayerManager != null) {
                uzPlayerManager.initWithoutReset();
                uzPlayerManager.setRunnable();
            }
            countTryLinkPlayError++;
            return;
        }
        countTryLinkPlayError++;
        releaseUzPlayerManager();
        checkToSetUpResource();
    }

    /**
     * if callAPIGetLinkPlay return no data, try to play the latest video or notify error
     */
    private void handleErrorNoData() {
        removeVideoCover(true);
        LDialogUtil.showDialog1Immersive(getContext(), UZException.ERR_23, new LDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                abortResult();
            }

            @Override
            public void onCancel() {
                abortResult();
            }
        });
    }

    private void abortResult() {
        if (uzCallback != null) {
            UZData.getInstance().setSettingPlayer(false);
            uzCallback.isInitResult(false, false, null, null);
        }
        if (uzVideoStateChangedListener != null) {
            UZData.getInstance().setSettingPlayer(false);
            uzVideoStateChangedListener.isInitResult(false, false, null, null);
        }
    }

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    public void onDestroy() {
        // cannot use isGetClickedPip (global variable), must use UZUtil.getClickedPip(activity)
        if (UZUtil.getClickedPip(getContext())) {
            UZUtil.stopMiniPlayer(getContext());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getContext().stopService(new Intent(getContext(), LConectifyService.class));
        }
        releasePlayerAnalytic();
        releaseUzPlayerManager();
        UZData.getInstance().setSettingPlayer(false);
        LDialogUtil.clearAll();
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        cdnHost = null;
        EventBus.getDefault().unregister(this);
    }

    private void releasePlayerAnalytic() {
        if (visualizeInfoHelper != null) {
            visualizeInfoHelper.releaseStatsForNerds();
        }
    }

    private void releaseUzPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.release();
        }
    }

    public void onResume() {
        if (isCasting()) {
            return;
        }
        activityIsPausing = false;
        SensorOrientationChangeNotifier.getInstance(getContext()).addListener(this);
        if (uzPlayerManager != null) {
            if (ibPlayIcon == null || ibPlayIcon.getVisibility() != VISIBLE) {
                uzPlayerManager.resumeVideo();
            }
        }
    }

    public boolean isPlaying() {
        if (getPlayer() == null) {
            return false;
        }
        return getPlayer().getPlayWhenReady();
    }

    private boolean activityIsPausing = false;

    public void onPause() {
        activityIsPausing = true;
        SensorOrientationChangeNotifier.getInstance(getContext()).remove(this);
        if (uzPlayerManager != null) {
            uzPlayerManager.pauseVideo();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getContext() == null) {
            return;
        }
        if (isCasting()) {
            LLog.e(TAG, "Error: handleClickPictureInPicture isCasting -> return");
            return;
        }
        if (LDeviceUtil.isCanOverlay(getContext())) {
            initializePiP();
        }
    }

    private long timestampOnStartPreview;

    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        timestampOnStartPreview = System.currentTimeMillis();
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKING);
        // TODO: remove deprecated callback
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onStartPreview(previewView, progress);
        }
        if (uzTimeBarChangedListener != null) {
            uzTimeBarChangedListener.onStartPreview(previewView, progress);
        }
    }

    private boolean isOnPreview;

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        isOnPreview = true;
        if (isCasting()) {
            UZData.getInstance().getCasty().getPlayer().seek(progress);
        }
        updateUIIbRewIconDependOnProgress(progress, true);
        // TODO: remove deprecated callback
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onPreview(previewView, progress, fromUser);
        }
        if (uzTimeBarChangedListener != null) {
            uzTimeBarChangedListener.onPreview(previewView, progress, fromUser);
        }
    }

    private long maxSeekLastDuration;

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        TmpParamData.getInstance().addViewSeekCount();
        long seekLastDuration = System.currentTimeMillis() - timestampOnStartPreview;
        TmpParamData.getInstance().setViewSeekDuration(seekLastDuration);
        if (maxSeekLastDuration < seekLastDuration) {
            maxSeekLastDuration = seekLastDuration;
            TmpParamData.getInstance().setViewMaxSeekTime(maxSeekLastDuration);
        }
        isOnPreview = false;
        onStopPreview(progress);
        // TODO: remove deprecated callback
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onStopPreview(previewView, progress);
        }
        if (uzTimeBarChangedListener != null) {
            uzTimeBarChangedListener.onStopPreview(previewView, progress);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKED);
    }

    public void onStopPreview(int progress) {
        if (uzPlayerManager != null) {
            seek(progress);
            uzPlayerManager.resumeVideo();
            isOnPlayerEnded = false;
            updateUIEndScreen();
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (uztvCallback != null || uzVideoTVListener != null) {
            if (uztvCallback != null) {
                uztvCallback.onFocusChange(view, isFocus);
            }
            if (uzVideoTVListener != null) {
                uzVideoTVListener.onFocusChanged(view, isFocus);
            }
        } else {
            if (firstViewHasFocus == null) {
                firstViewHasFocus = view;
            }
        }
    }

    private boolean isLandscape;

    public boolean isLandscape() {
        return isLandscape;
    }

    @Override
    public void onOrientationChange(int orientation) {
        boolean isDeviceAutoRotation = LDeviceUtil.isRotationPossible(getContext());
        if (orientation == 90 || orientation == 270) {
            if (isDeviceAutoRotation && !isLandscape) {
                LActivityUtil.changeScreenLandscape((Activity) getContext(), orientation);
            }
        } else {
            if (isDeviceAutoRotation && isLandscape) {
                LActivityUtil.changeScreenPortrait((Activity) getContext());
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
            LScreenUtil.hideDefaultControls(getContext());
            isLandscape = true;
            UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, true);
            ViewUtils.goneViews(ibPictureInPictureIcon);
        } else {
            LScreenUtil.showDefaultControls(getContext());
            isLandscape = false;
            UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, false);
            if (!isCasting()) {
                ViewUtils.visibleViews(ibPictureInPictureIcon);
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
        if (uzPlayerStateChangedListener != null) {
            uzPlayerStateChangedListener.onScreenRotated(isLandscape);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ibFullscreenIcon) {
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
            //dangerous to remove
        } else if (v == ibFfwdIcon) {
            handleClickFastForward();
        } else if (v == ibRewIcon) {
            handleClickFastReward();
        } else if (v == ibPlayIcon) {
            handleClickPlayPause();
        } else if (v == ibReplayIcon) {
            replay();
        } else if (v == ibSkipNextIcon) {
            handleClickSkipNext();
        } else if (v == ibSkipPreviousIcon) {
            handleClickSkipPrevious();
        } else if (v == ibSpeedIcon) {
            showSpeed();
        } else if (v == tvEndScreenMsg) {
            LAnimationUtil.play(v, Techniques.Pulse);
        } else if (v == ivLogo) {
            LAnimationUtil.play(v, Techniques.Pulse);
            if (playerInfor == null || playerInfor.getData() == null || playerInfor.getData().getLogo() == null || playerInfor.getData().getLogo().getUrl() == null) {
                return;
            }
            LSocialUtil.openUrlInBrowser(getContext(), playerInfor.getData().getLogo().getUrl());
        }
        /*có trường hợp đang click vào các control thì bị ẩn control ngay lập tức, trường hợp này ta có thể xử lý khi click vào control thì reset count down để ẩn control ko
        default controller timeout là 8s, vd tới s thứ 7 bạn tương tác thì tới s thứ 8 controller sẽ bị ẩn*/
        if (isDefaultUseController && isPlayerControllerShowing()) {
            showController();
        }
        if (uzItemClick != null) {
            uzItemClick.onItemClick(v);
        }
        if (uzItemClickListener != null) {
            uzItemClickListener.onItemClick(v);
        }
    }

    private void handleClickPlayPause() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void handleClickFastReward() {
        if (isCasting()) {
            UZData.getInstance().getCasty().getPlayer().seekToBackward(defaultValueBackwardForward);
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.seekToBackward(defaultValueBackwardForward);
            }
            if (isPlaying()) {
                isOnPlayerEnded = false;
                updateUIEndScreen();
            }
        }
    }

    private void handleClickFastForward() {
        if (isCasting()) {
            UZData.getInstance().getCasty().getPlayer().seekToForward(defaultValueBackwardForward);
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.seekToForward(defaultValueBackwardForward);
            }
        }
    }

    private void handleClickPictureInPicture() {
        if (getContext() == null) {
            notifyError(UZExceptionUtil.getExceptionShowPip());
            return;
        }
        if (!isInitMiniPlayerSuccess) {
            //dang init 1 instance mini player roi, khong cho init nua
            notifyError(UZExceptionUtil.getExceptionShowPip());
            return;
        }
        if (isCasting()) {
            notifyError(UZExceptionUtil.getExceptionShowPip());
            return;
        }
        if (LDeviceUtil.isCanOverlay(getContext())) {
            initializePiP();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
            ((Activity) getContext()).startActivityForResult(intent, Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    private boolean isInitMiniPlayerSuccess = true;

    public void initializePiP() {
        if (getContext() == null || uzPlayerManager == null || uzPlayerManager.getLinkPlay() == null) {
            notifyError(UZExceptionUtil.getExceptionShowPip());
            return;
        }
        ViewUtils.goneViews(ibPictureInPictureIcon);
        if (uzCallback != null) {
            isInitMiniPlayerSuccess = false;
            uzCallback.onStateMiniPlayer(false);
        }
        if (uzPlayerStateChangedListener != null) {
            isInitMiniPlayerSuccess = false;
            uzPlayerStateChangedListener.onStateMiniPlayer(false);
        }
        UZUtil.setVideoWidth(getContext(), getVideoWidth());
        UZUtil.setVideoHeight(getContext(), getVideoHeight());
        Intent intent = new Intent(getContext(), FUZVideoService.class);
        intent.putExtra(Constants.FLOAT_CONTENT_POSITION, getCurrentPosition());
        intent.putExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, isInitCustomLinkPlay);
        intent.putExtra(Constants.FLOAT_UUID, uuid.toString());
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uzPlayerManager.getLinkPlay());
        intent.putExtra(Constants.FLOAT_IS_LIVESTREAM, isLivestream);
        intent.putExtra(Constants.FLOAT_PROGRESS_BAR_COLOR, progressBarColor);
        getContext().startService(intent);
    }

    public SimpleExoPlayer getPlayer() {
        if (uzPlayerManager == null) {
            return null;
        }
        return uzPlayerManager.getPlayer();
    }

    /**
     * @deprecated use {@link UZVideo#seek(long)} instead
     */
    @Deprecated
    public void seekTo(long positionMs) {
        seek(positionMs);
    }

    /**
     * Seek to specific position
     * @param positionMs: video position in milliseconds
     */
    public boolean seek(long positionMs) {
        if (positionMs < 0) positionMs = 0;
        if (positionMs > getDuration()) positionMs = getDuration();
        return uzPlayerManager != null && uzPlayerManager.seekTo(positionMs);
    }

    /**
     * @deprecated use {@link UZVideo#seek(long)} instead <br>
     *
     * For live streams it will typically be the live edge of the window.
     * For other streams it will typically be the start of the window.
     */
    @Deprecated
    public void seekToDefaultPosition(long positionMs) {
        if (isLivestream) seek(getDuration());
        else seek(0);
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
        if (isPlayerControllerAlwayVisible || isCasting()) {
            return;
        }
        if (uzPlayerView != null) {
            uzPlayerView.hideController();
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
        if (uzPlayerView != null) {
            return uzPlayerView.getControllerHideOnTouch();
        }
        return false;
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

    private void callAPIGetListAllEntity(String metadataId) {
        showProgress();
        boolean isDataListExist = UZData.getInstance().getDataList() != null;
        if (isDataListExist) {
            playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
            hideProgress();
        } else {
            UZService service = UZRestClient.createService(UZService.class);
            UZAPIMaster.getInstance().subscribe(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, pfLimit, pfPage, pfOrderBy, pfOrderType, publishToCdn, UZData.getInstance().getAppId()), new ApiSubscriber<ResultListEntity>() {
                @Override
                public void onSuccess(ResultListEntity result) {
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
                        if (uzCallback != null) {
                            handleError(UZExceptionUtil.getExceptionListAllEntity());
                        }
                        if (uzVideoStateChangedListener != null) {
                            handleError(UZExceptionUtil.getExceptionListAllEntity());
                        }
                        return;
                    }
                    if (pfTotalPage == Integer.MAX_VALUE) {
                        int totalItem = (int) result.getMetadata().getTotal();
                        float ratio = (float) (totalItem / pfLimit);
                        if (ratio == 0) {
                            pfTotalPage = (int) ratio;
                        } else if (ratio > 0) {
                            pfTotalPage = (int) ratio + 1;
                        } else {
                            pfTotalPage = (int) ratio;
                        }
                    }
                    UZData.getInstance().setDataList(result.getData());
                    if (UZData.getInstance().getDataList() == null || UZData.getInstance().getDataList().isEmpty()) {
                        notifyError(UZExceptionUtil.getExceptionListAllEntity());
                        return;
                    }
                    playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
                    hideProgress();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "callAPIGetListAllEntity onFail " + e.getMessage());
                    notifyError(UZExceptionUtil.getExceptionListAllEntity());
                    hideProgress();
                }
            });
        }
    }

    protected boolean isPlayPlaylistFolder() {
        return UZData.getInstance().getDataList() != null;
    }

    private void playPlaylistPosition(int position) {
        if (!isPlayPlaylistFolder()) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            return;
        }
        LLog.d(TAG, "playPlaylistPosition position: " + position);
        if (position < 0) {
            LLog.e(TAG, "This is the first item");
            notifyError(UZExceptionUtil.getExceptionPlaylistFolderItemFirst());
            return;
        }
        if (position > UZData.getInstance().getDataList().size() - 1) {
            LLog.e(TAG, "This is the last item");
            notifyError(UZExceptionUtil.getExceptionPlaylistFolderItemLast());
            return;
        }
        urlImgThumbnail = null;
        pause();
        hideController();
        //update UI for skip next and skip previous button
        setSrcDrawableEnabledForViews(ibSkipPreviousIcon, ibSkipNextIcon);
        //set disabled prevent double click, will enable onStateReadyFirst()
        setClickableForViews(false, ibSkipPreviousIcon, ibSkipNextIcon);
        Data data = UZData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            return;
        }
        UZData.getInstance().setCurrentPositionOfDataList(position);
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position);
        init(data.getId(), false);
    }

    private void setSrcDrawableEnabledForViews(UZImageButton... views) {
        for (UZImageButton v : views) {
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
            if (stateEndCallback != null) {
                stateEndCallback.onPlayerEnded();
            }
            if (uzVideoStateChangedListener != null) {
                uzVideoStateChangedListener.onVideoEnded();
            }
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
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() + 1);
    }

    private void autoSwitchPreviousLinkVideo() {
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() - 1);
    }

    private void handleClickPlaylistFolder() {
        UZDlgPlaylistFolder UZDlgPlaylistFolder = new UZDlgPlaylistFolder(getContext(), isLandscape, UZData.getInstance().getDataList(), UZData.getInstance().getCurrentPositionOfDataList(), new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(Data data, int position) {
                UZData.getInstance().setCurrentPositionOfDataList(position);
                playPlaylistPosition(position);
            }

            @Override
            public void onFocusChange(Data data, int position) {
            }

            @Override
            public void onDismiss() {
            }
        });
        UZUtil.showUizaDialog(getContext(), UZDlgPlaylistFolder);
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
        boolean result = seek(0);
        if (result) {
            isSetFirstRequestFocusDone = false;
            isOnPlayerEnded = false;
            updateUIEndScreen();
            handlePlayPlayListFolderUI();
            trackUizaEventVideoStarts();
            trackUizaEventDisplay();
            trackUizaEventPlaysRequested();
        }
        return result;
    }

    //===================================================================END FOR PLAYLIST/FOLDER

    /* Nếu đang casting thì button này sẽ handle volume on/off ở cast player
     * Ngược lại, sẽ handle volume on/off ở exo player*/
    private void handleClickBtVolume() {
        if (isCasting()) {
            boolean isMute = UZData.getInstance().getCasty().toggleMuteVolume();
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
        if (isLandscape) {
            toggleFullscreen();
        }
    }

    private void handleClickSetting() {
        View view = UZUtil.getBtVideo(debugRootView);
        ViewUtils.performClick(view);
    }

    private void handleClickCC() {
        if (uzPlayerManager != null && (uzPlayerManager.getSubtitleList() == null
                || uzPlayerManager.getSubtitleList().isEmpty())) {
            UZDlgInfoV1 uzDlgInfoV1 = new UZDlgInfoV1(getContext(), getContext().getString(R.string.text), getContext().getString(R.string.no_caption));
            UZUtil.showUizaDialog(getContext(), uzDlgInfoV1);
        } else {
            View view = UZUtil.getBtText(debugRootView);
            ViewUtils.performClick(view);
            if (view == null) {
                LLog.e(TAG, "error handleClickCC null");
            }
        }
    }

    private void handleClickHearing() {
        View view = UZUtil.getBtAudio(debugRootView);
        ViewUtils.performClick(view);
    }

    /**
     * @deprecated use {@link UZVideo#play()} instead
     */
    @Deprecated
    public void resumeVideo() {
        play();
    }

    /**
     * Play or resume the video
     */
    public void play() {
        TmpParamData.getInstance().setPlayerIsPaused(false);
        addTrackingMuiza(Constants.MUIZA_EVENT_PLAY);

        if (isCasting()) {
            UZData.getInstance().getCasty().getPlayer().play();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.resumeVideo();
            }
        }

        updatePlayPauseIcon();
    }

    /**
     * @deprecated use {@link UZVideo#pause()} instead
     */
    @Deprecated
    public void pauseVideo() {
        pause();
    }

    /**
     * Pause the video
     */
    public void pause() {
        TmpParamData.getInstance().setPlayerIsPaused(true);
        if (isCasting()) {
            UZData.getInstance().getCasty().getPlayer().pause();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.pauseVideo();
            }
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_PAUSE);
        updatePlayPauseIcon();
    }

    public void setDefaultValueBackwardForward(int mls) {
        defaultValueBackwardForward = mls;
    }

    public int getDefaultValueBackwardForward() {
        return defaultValueBackwardForward;
    }

    /**
     * @deprecated use {@link UZVideo#seek(long)} instead
     */
    @Deprecated
    public void seekToForward(int mls) {
        seek(getCurrentPosition() + mls);
    }

    /**
     * @deprecated use {@link UZVideo#seek(long)} instead
     */
    @Deprecated
    public void seekToBackward(int mls) {
        seek(getCurrentPosition() - mls);
    }

    //chi toggle show hide controller khi video da vao dc onStateReadyFirst();
    public void toggleShowHideController() {
        if (uzPlayerView != null && !isOnPlayerEnded) {
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
            play();
        }
    }

    public void toggleFullscreen() {
        if (!getFullScreen() && !enableFullScreenMode) {
            return;
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_FULLSCREENCHANGE);
        LActivityUtil.toggleScreenOritation((Activity) getContext());
    }

    private boolean enableFullScreenMode = true;

    /**
     * Enables or disables fullscreen mode.
     * @param enable true if enable, otherwise false
     */
    public void setFullScreen(boolean enable) {
        enableFullScreenMode = enable;
        if (getFullScreen() && !enable) {
            // disable when player is fullscreen -> reset it to portrait
            toggleFullscreen();
        }
    }

    /**
     * Returns whether the player is currently in fullscreen.
     */
    public boolean getFullScreen() {
        return LScreenUtil.isFullScreen(getContext());
    }

    public void showCCPopup() {
        ViewUtils.performClick(ibCcIcon);
    }

    public void showHQPopup() {
        ViewUtils.performClick(ibSettingIcon);
    }

    /*
     ** Hiển thị picture in picture và close video view hiện tại
     * Chỉ work nếu local player đang không casting
     * Device phải là tablet
     */
    public void showPip() {
        if (isCasting()) {
            LLog.e(TAG, UZException.ERR_19);
            notifyError(UZExceptionUtil.getExceptionShowPip());
        } else {
            ViewUtils.performClick(ibPictureInPictureIcon);
        }
    }

    public void showSpeed() {
        if (getPlayer() == null) {
            return;
        }
        final UZDlgSpeed uzDlgSpeed = new UZDlgSpeed(getContext(), getPlayer().getPlaybackParameters().speed, new UZDlgSpeed.Callback() {
            @Override
            public void onSelectItem(UZDlgSpeed.Speed speed) {
                if (speed != null) {
                    setPlaybackSpeed(speed.getValue());
                }
            }
        });
        UZUtil.showUizaDialog(getContext(), uzDlgSpeed);
    }

    /**
     * @deprecated use {@link UZVideo#nextVideo()} instead
     */
    @Deprecated
    public void skipNextVideo() {
        nextVideo();
    }

    /**
     * Play next video in playlist if has
     */
    public void nextVideo() {
        handleClickSkipNext();
    }

    /**
     * @deprecated use {@link UZVideo#previousVideo()} instead
     */
    @Deprecated
    public void skipPreviousVideo() {
        previousVideo();
    }

    /**
     * Play previous video in playlist if has
     */
    public void previousVideo() {
        handleClickSkipPrevious();
    }

    protected PlayerView getUzPlayerView() {
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

    public UZPlayerManager getUzPlayerManager() {
        return uzPlayerManager;
    }

    public UZImageButton getIbFullscreenIcon() {
        return ibFullscreenIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public UZImageButton getIbPlayIcon() {
        return ibPlayIcon;
    }

    public UZImageButton getIbReplayIcon() {
        return ibReplayIcon;
    }

    public UZImageButton getIbRewIcon() {
        return ibRewIcon;
    }

    public UZImageButton getIbFfwdIcon() {
        return ibFfwdIcon;
    }

    public UZImageButton getIbBackScreenIcon() {
        return ibBackScreenIcon;
    }

    public UZImageButton getIbVolumeIcon() {
        return ibVolumeIcon;
    }

    public UZImageButton getIbSettingIcon() {
        return ibSettingIcon;
    }

    public UZImageButton getIbCcIcon() {
        return ibCcIcon;
    }

    public UZImageButton getIbPlaylistFolderIcon() {
        return ibPlaylistFolderIcon;
    }

    public UZImageButton getIbHearingIcon() {
        return ibHearingIcon;
    }

    public UZImageButton getIbPictureInPictureIcon() {
        return ibPictureInPictureIcon;
    }

    public UZImageButton getIbSkipPreviousIcon() {
        return ibSkipPreviousIcon;
    }

    public UZImageButton getIbSkipNextIcon() {
        return ibSkipNextIcon;
    }

    public UZImageButton getIbSpeedIcon() {
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

    public UZMediaRouteButton getUzMediaRouteButton() {
        return uzMediaRouteButton;
    }

    public RelativeLayout getRlChromeCast() {
        return rlChromeCast;
    }

    public UZImageButton getIbsCast() {
        return ibsCast;
    }

    public String getEntityId() {
        return entityId;
    }

    public UZTextView getTvPosition() {
        return tvPosition;
    }

    public UZTextView getTvDuration() {
        return tvDuration;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public UZImageButton getIvLiveTime() {
        return ivLiveTime;
    }

    public UZImageButton getIvLiveView() {
        return ivLiveView;
    }

    public RelativeLayout getRlEndScreen() {
        return rlEndScreen;
    }

    public TextView getTvEndScreenMsg() {
        return tvEndScreenMsg;
    }

    public UZTimebar getUZTimeBar() {
        return uzTimebar;
    }

    public LinearLayout getLlTop() {
        return llTop;
    }

    public View getBkg() {
        return bkg;
    }

    public FrameLayout getOverlayFrameLayout() {
        if (uzPlayerView == null) {
            return null;
        }
        return uzPlayerView.getOverlayFrameLayout();
    }

    public List<UZItem> getHQList() {
        View view = UZUtil.getBtVideo(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error getHQList null");
            notifyError(UZExceptionUtil.getExceptionListHQ());
            return null;
        }
        return showUZTrackSelectionDialog(view, false);
    }

    public List<UZItem> getAudioList() {
        View view = UZUtil.getBtAudio(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error audio null");
            notifyError(UZExceptionUtil.getExceptionListAudio());
            return null;
        }
        return showUZTrackSelectionDialog(view, false);
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

    /**
     * Sets the mute state of the player.
     * @param mute If true, mute the player. If false, un-mute the player
     */
    public void setMuted(boolean mute) {
        if (uzPlayerManager != null) uzPlayerManager.setMuted(mute);
    }

    /**
     * Returns whether the player is currently muted or not
     */
    public boolean getMuted() {
        return uzPlayerManager != null && getVolume() == 0;
    }

    /**
     * @deprecated use {@link UZVideo#toggleMuted()} instead
     */
    @Deprecated
    public void toggleVolume() {
        toggleMuted();
    }

    /**
     * Toggles the mute state of the player.
     */
    public void toggleMuted() {
        ViewUtils.performClick(ibVolumeIcon);
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

    /**
     * @deprecated use {@link UZVideo#setPlaybackSpeed(float)} instead
     */
    @Deprecated
    public void setSpeed(float speed) {
        setPlaybackSpeed(speed);
    }

    /**
     * Set playback speed <br>
     * See {@link UZDlgSpeed} for supported speed
     * @param speed new speed of playback
     */
    public void setPlaybackSpeed(float speed) {
        if (getContext() == null) {
            return;
        }
        if (isLivestream) {
            handleError(UZExceptionUtil.getExceptionSpeed());
            return;
        }
        if (speed > 2 || speed < 0) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_speed_illegal));
        }
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        if (getPlayer() != null) {
            getPlayer().setPlaybackParameters(playbackParameters);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_RATECHANGE);
    }

    public float getPlaybackSpeed() {
        if (getPlayer() == null) return 1.f;
        return getPlayer().getPlaybackParameters().speed;
    }

    //=============================================================================================START UI
    private void findViews() {
        bkg = findViewById(R.id.bkg);
        ivVideoCover = findViewById(R.id.iv_cover);
        llTop = findViewById(R.id.ll_top);
        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, progressBarColor);
        updateUIPositionOfProgressBar();
        uzPlayerView.setControllerStateCallback(this);
        uzTimebar = uzPlayerView.findViewById(R.id.exo_progress);
        previewFrameLayout = uzPlayerView.findViewById(R.id.preview_frame_layout);
        if (uzTimebar != null) {
            if (uzTimebar.getTag() == null) {
                isSetUZTimebarBottom = false;
                ViewUtils.visibleViews(uzPlayerView);
            } else {
                if (uzTimebar.getTag().toString().equals(getContext().getString(R.string.use_bottom_uz_timebar))) {
                    isSetUZTimebarBottom = true;
                    setMarginDependOnUZTimeBar(uzPlayerView.getVideoSurfaceView());
                } else {
                    isSetUZTimebarBottom = false;
                    ViewUtils.visibleViews(uzPlayerView);
                }
            }
            uzTimebar.addOnPreviewChangeListener(this);
            uzTimebar.setOnFocusChangeListener(this);
        } else {
            ViewUtils.visibleViews(uzPlayerView);
        }
        ivThumbnail = uzPlayerView.findViewById(R.id.image_view_thumnail);
        tvPosition = uzPlayerView.findViewById(R.id.uz_position);
        if (tvPosition != null) {
            tvPosition.setText(LDateUtils.convertMlsecondsToHMmSs(0));
        }
        tvDuration = uzPlayerView.findViewById(R.id.uz_duration);
        if (tvDuration != null) {
            tvDuration.setText("-:-");
        }
        ibFullscreenIcon = uzPlayerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = uzPlayerView.findViewById(R.id.tv_title);
        ibPlayIcon = uzPlayerView.findViewById(vn.uiza.R.id.exo_play_uiza);
        //If auto start true, show button play and gone button pause
        if (ibPlayIcon != null) {
            updatePlayPauseIcon(true, false);
        }
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
        debugLayout = findViewById(R.id.debug_layout);
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
        ViewUtils.setFocusableViews(false, ivLiveTime, ivLiveView);
        rlEndScreen = uzPlayerView.findViewById(R.id.rl_end_screen);
        ViewUtils.goneViews(rlEndScreen);
        tvEndScreenMsg = uzPlayerView.findViewById(R.id.tv_end_screen_msg);
        if (tvEndScreenMsg != null) {
            LUIUtil.setTextShadow(tvEndScreenMsg, Color.WHITE);
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
                ibRewIcon, ibPlayIcon, ibReplayIcon, ibSkipNextIcon, ibSkipPreviousIcon, ibSpeedIcon);
    }

    private void setClickAndFocusEventForViews(View... views) {
        for (View v : views) {
            if (v != null) {
                v.setOnClickListener(this);
                v.setOnFocusChangeListener(this);
            }
        }
    }

    public void setTintMediaRouteButton(final int color) {
        if (uzMediaRouteButton != null) {
            uzMediaRouteButton.post(new Runnable() {
                @Override
                public void run() {
                    uzMediaRouteButton.applyTint(color);
                }
            });
        }
    }

    private void updatePlayPauseIcon(boolean isPlay, boolean isFocus) {
        if (ibPlayIcon == null) return;
        if (isPlay) {
            ibPlayIcon.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
        } else {
            ibPlayIcon.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
        }
        if (isFocus) {
            ibPlayIcon.requestFocus();
        }
    }

    //If auto start true, show button play and gone button pause
    //if not, gone button play and show button pause
    private void updateUIButtonPlayPauseDependOnIsAutoStart() {
        if (isAutoStart) {
            updatePlayPauseIcon(true, !isSetFirstRequestFocusDone);
            if (!isSetFirstRequestFocusDone) {
                isSetFirstRequestFocusDone = true;
            }
        } else {
            updatePlayPauseIcon();
        }
    }

    private void updatePlayPauseIcon() {
        LLog.e(TAG, "updatePlayPauseIcon");
        updatePlayPauseIcon(isPlaying(), !isSetFirstRequestFocusDone);
        if (!isSetFirstRequestFocusDone) {
            isSetFirstRequestFocusDone = true;
        }
    }

    private String urlImgThumbnail;

    public void setUrlImgThumbnail(String urlImgThumbnail) {
        if (urlImgThumbnail == null || urlImgThumbnail.isEmpty()) {
            return;
        }
        this.urlImgThumbnail = urlImgThumbnail;
        if (ivVideoCover == null) {
            return;
        }
        if (ivVideoCover.getVisibility() != VISIBLE) {
            ivVideoCover.setVisibility(VISIBLE);
            LImageUtil.load(getContext(), urlImgThumbnail, ivVideoCover, R.drawable.background_black);
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
            LImageUtil.load(getContext(), urlCover, ivVideoCover, R.drawable.background_black);
        }
    }

    protected void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            ivVideoCover.setVisibility(GONE);
            ivVideoCover.invalidate();
            if (isFromHandleError) {
                updateUIDependOnLivestream();
            } else {
                onStateReadyFirst();
                if (isLivestream) {
                    if (tvLiveTime != null) {
                        tvLiveTime.setText(HYPHEN);
                    }
                    if (tvLiveView != null) {
                        tvLiveView.setText(HYPHEN);
                    }
                    callAPIUpdateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
                    callAPIUpdateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
                }
            }
        } else {
            //goi changeskin realtime thi no ko vao if nen ko update tvDuration dc
            updateTvDuration();
        }
    }

    private void updateUIEachSkin() {
        int currentPlayerId = UZData.getInstance().getCurrentPlayerId();
        if (currentPlayerId == R.layout.uz_player_skin_2 || currentPlayerId == R.layout.uz_player_skin_3) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setRatioLand(7);
                ibPlayIcon.setRatioPort(5);
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
        uzPlayerView.post(new Runnable() {
            @Override
            public void run() {
                int marginL = uzPlayerView.getMeasuredWidth() / 2 - progressBar.getMeasuredWidth() / 2;
                int marginT = uzPlayerView.getMeasuredHeight() / 2 - progressBar.getMeasuredHeight() / 2;
                LUIUtil.setMarginPx(progressBar, marginL, marginT, 0, 0);
            }
        });
    }

    //tự tạo layout chromecast và background đen
    //Gen layout chromecast with black background programmatically
    private void addUIChromecastLayer() {
        //listener check state of chromecast
        CastContext castContext = null;
        try {
            castContext = CastContext.getSharedInstance(getContext());
        } catch (Exception e) {
            LLog.e(TAG, "Error addUIChromecastLayer: " + e.toString());
            SentryUtils.captureException(e);
        }
        if (castContext == null) {
            ViewUtils.goneViews(uzMediaRouteButton);
            return;
        }
        updateMediaRouteButtonVisibility(castContext.getCastState());
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                updateMediaRouteButtonVisibility(state);
            }
        });
        rlChromeCast = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlChromeCastParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        rlChromeCast.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Black));
        ibsCast = new UZImageButton(getContext());
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

    private void updateMediaRouteButtonVisibility(int state) {
        if (state == CastState.NO_DEVICES_AVAILABLE) {
            ViewUtils.goneViews(uzMediaRouteButton);
        } else {
            ViewUtils.visibleViews(uzMediaRouteButton);
        }
    }

    private void addPlayerView() {
        uzPlayerView = null;
        int skinId = UZData.getInstance().getCurrentPlayerId();
        uzPlayerView = (UZPlayerView) ((Activity) getContext()).getLayoutInflater().inflate(skinId, null);
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
    public boolean changeSkin(int skinId) {
        if (getContext() == null || uzPlayerManager == null) {
            return false;
        }
        if (UZData.getInstance().isUseWithVDHView()) {
            throw new IllegalArgumentException(getContext().getString(R.string.error_change_skin_with_vdhview));
        }
        if (uzPlayerManager.isPlayingAd()) {
            notifyError(UZExceptionUtil.getExceptionChangeSkin());
            return false;
        }
        UZUtil.setCurrentPlayerId(skinId);
        isRefreshFromChangeSkin = true;
        isCalledFromChangeSkin = true;
        rootView.removeView(uzPlayerView);
        rootView.requestLayout();
        uzPlayerView = (UZPlayerView) ((Activity) getContext()).getLayoutInflater().inflate(skinId, null);
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
        if (!isTV) {
            uzMediaRouteButton = new UZMediaRouteButton(getContext());
            if (llTop != null) {
                llTop.addView(uzMediaRouteButton);
            }
            setUpMediaRouteButton();
            addUIChromecastLayer();
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
        if (uzPlayerStateChangedListener != null) {
            uzPlayerStateChangedListener.onSkinChanged();
        }
        return true;
    }

    private void updateTvDuration() {
        if (tvDuration != null) {
            if (isLivestream) {
                tvDuration.setText(LDateUtils.convertMlsecondsToHMmSs(0));
            } else {
                tvDuration.setText(LDateUtils.convertMlsecondsToHMmSs(getDuration()));
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
            tvPosition.setText(HYPHEN + LDateUtils.convertMlsecondsToHMmSs(past));
        } else {
            tvPosition.setText(LDateUtils.convertMlsecondsToHMmSs(currentMls));
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
    public void updateUIFocusChange(View view, boolean isFocus) {
        if (view == null) {
            return;
        }
        if (isFocus) {
            if (view instanceof UZImageButton) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
                ((UZImageButton) view).setColorFilter(Color.GRAY);
            } else if (view instanceof Button) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UZTimebar) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus_uz_timebar, R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        } else {
            if (view instanceof UZImageButton) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
                ((UZImageButton) view).clearColorFilter();
            } else if (view instanceof Button) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus, R.drawable.bkg_tv_no_focus);
            } else if (view instanceof UZTimebar) {
                UZUtil.updateUIFocusChange(view, isFocus, R.drawable.bkg_tv_has_focus_uz_timebar, R.drawable.bkg_tv_no_focus_uz_timebar);
            }
        }
    }

    private View firstViewHasFocus;

    private void handleFirstViewHasFocus() {
        if (firstViewHasFocus != null && (uztvCallback != null || uzVideoTVListener != null)) {
            if (uztvCallback != null) {
                uztvCallback.onFocusChange(firstViewHasFocus, true);
            }
            if (uzVideoTVListener != null) {
                uzVideoTVListener.onFocusChanged(firstViewHasFocus, true);
            }
            firstViewHasFocus = null;
        }
    }

    private void updateUISizeThumbnail() {
        int screenWidth = LScreenUtil.getScreenWidth();
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
            LUIUtil.setMarginDimen(uzTimebar, 5, 0, 5, 0);
        } else {
            LUIUtil.setMarginDimen(uzTimebar, 0, 0, 0, 0);
        }
    }

    private void setMarginRlLiveInfo() {
        if (isLandscape) {
            LUIUtil.setMarginDimen(rlLiveInfo, 50, 0, 50, 0);
        } else {
            LUIUtil.setMarginDimen(rlLiveInfo, 5, 0, 5, 0);
        }
    }

    private void setTitle() {
        if (tvTitle != null) {
            tvTitle.setText(UZData.getInstance().getEntityName());
        }
    }

    private void updateUIDependOnLivestream() {
        if (isCasting()) {
            ViewUtils.goneViews(ibPictureInPictureIcon);
        } else {
            if (isTablet && isTV) {//only hide ibPictureInPictureIcon if device is TV
                ViewUtils.goneViews(ibPictureInPictureIcon);
            }
        }
        if (isLivestream) {
            ViewUtils.visibleViews(rlLiveInfo);
            //TODO why set gone not work?
            setUIVisible(false, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        } else {
            ViewUtils.goneViews(rlLiveInfo);
            //TODO why set visible not work?
            setUIVisible(true, ibSpeedIcon, ibRewIcon, ibFfwdIcon);
        }
        if (isTV) {
            ViewUtils.goneViews(ibFullscreenIcon);
        }
    }

    private void setUIVisible(boolean visible, UZImageButton... views) {
        for (UZImageButton v : views) {
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

    private void updateLiveInfoTimeStartLive() {
        if (!isLivestream || getContext() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        long duration = now - startTime;
        String s = LDateUtils.convertMlsecondsToHMmSs(duration);
        if (tvLiveTime != null) {
            tvLiveTime.setText(s);
        }
        if (uzLiveContentCallback != null) {
            uzLiveContentCallback.onUpdateLiveInfoTimeStartLive(duration, s);
        }
        if (uzLiveInfoChangedListener != null) {
            uzLiveInfoChangedListener.onLiveTimeChanged(duration, s);
        }
        callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
    }

    private void updateUIEndScreen() {
        if (getContext() == null) {
            return;
        }
        if (isOnPlayerEnded) {
            if (rlEndScreen != null && tvEndScreenMsg != null) {
                rlEndScreen.setVisibility(VISIBLE);
                //TODO call api skin config to correct this text
                setTextEndscreen(getContext().getString(R.string.tks_4_watching));
            }
            setVisibilityOfPlayPauseReplay(true);
            showController();
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
                uzPlayerView.setControllerHideOnTouch(false);
            }
        } else {
            if (rlEndScreen != null && tvEndScreenMsg != null) {
                rlEndScreen.setVisibility(GONE);
                setTextEndscreen("");
            }
            setVisibilityOfPlayPauseReplay(false);
            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(defaultValueControllerTimeout);
            }
            setHideControllerOnTouch(isHideOnTouch);
        }
    }

    public void setTextEndscreen(String msg) {
        if (tvEndScreenMsg != null) {
            tvEndScreenMsg.setText(msg);
        }
    }

    private void setVisibilityOfPlayPauseReplay(boolean isShowReplay) {
        if (isShowReplay) {
            ViewUtils.goneViews(ibPlayIcon);
            if (ibReplayIcon != null) {
                ViewUtils.visibleViews(ibReplayIcon);
                ibReplayIcon.requestFocus();
            }
        } else {
            updateUIButtonPlayPauseDependOnIsAutoStart();
            ViewUtils.goneViews(ibReplayIcon);
            if (ibPlayIcon != null) {
                ViewUtils.visibleViews(ibPlayIcon);
                ibPlayIcon.requestFocus();
            }
        }
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
        ViewUtils.setVisibilityViews(visibilityOfPlaylistFolderController, ibPlaylistFolderIcon,
                ibSkipNextIcon, ibSkipPreviousIcon);
        //Có play kiểu gì đi nữa thì cũng phải ibPlayIcon GONE và ibPauseIcon VISIBLE và ibReplayIcon GONE
        setVisibilityOfPlayPauseReplay(false);
    }

    public void hideUzTimebar() {
        ViewUtils.goneViews(previewFrameLayout, ivThumbnail, uzTimebar);
    }

    private List<UZItem> showUZTrackSelectionDialog(final View view, boolean showDialog) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = ((Button) view).getText();
            int rendererIndex = (int) view.getTag();
            boolean allowAdaptiveSelections = false;
            final Pair<AlertDialog, UZTrackSelectionView> dialogPair = UZTrackSelectionView.getDialog(getContext(), title, uzPlayerManager.getTrackSelector(), rendererIndex);
            dialogPair.second.setShowDisableOption(false);
            dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
            dialogPair.second.setCallback(new UZTrackSelectionView.Callback() {
                @Override
                public void onClick() {
                    LUIUtil.setDelay(300, new LUIUtil.DelayCallback() {
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
                UZUtil.showUizaDialog(getContext(), dialogPair.first);
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
            int hRootView = LUIUtil.getHeightOfView(rootView);
            int hUZTimebar = getHeightUZTimeBar();
            return hRootView - hUZTimebar / 2;
        } else {
            return LUIUtil.getHeightOfView(rootView);
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
            LUIUtil.setMarginPx(view, 0, 0, 0, 0);
        } else {
            heightUZTimebar = getHeightUZTimeBar();
            LUIUtil.setMarginPx(view, 0, 0, 0, heightUZTimebar / 2);
        }
    }

    private int progressBarColor = Color.WHITE;

    public void setProgressBarColor(int progressBarColor) {
        if (progressBar != null) {
            this.progressBarColor = progressBarColor;
            LUIUtil.setColorProgressBar(progressBar, progressBarColor);
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
        vn.uiza.restapi.uiza.model.v4.playerinfo.Data data = playerInfor.getData();
        if (data == null) {
            return;
        }
        Logo logo = data.getLogo();
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
            uzPlayerView.getOverlayFrameLayout().addView(ivLogo, layoutParams);
            ivLogo.setOnClickListener(this);
            LImageUtil.load(getContext(), logo.getLogo(), ivLogo);
        } else {
            ivLogo = null;
        }
    }

    //=============================================================================================END UI
    //=============================================================================================START EVENT
    private UZLiveContentCallback uzLiveContentCallback;
    private ProgressCallback progressCallback;
    private CallbackUZTimebar callbackUZTimebar;
    private UZItemClick uzItemClick;
    private UZCallback uzCallback;
    private UZTVCallback uztvCallback;
    private StateEndCallback stateEndCallback;

    private UZLiveInfoChangedListener uzLiveInfoChangedListener;
    private UZVideoStateChangedListener uzVideoStateChangedListener;
    private UZPlayerStateChangedListener uzPlayerStateChangedListener;
    private UZAdStateChangedListener uzAdStateChangedListener;
    private UZVideoTVListener uzVideoTVListener;
    private UZTimeBarChangedListener uzTimeBarChangedListener;
    private UZItemClickListener uzItemClickListener;

    private UZPlayerView.ControllerStateCallback controllerStateCallback;
    protected AudioListener audioListener;
    protected MetadataOutput metadataOutput;
    protected Player.EventListener eventListener;
    protected VideoListener videoListener;
    protected TextOutput textOutput;

    /**
     * @deprecated use {@link UZVideo#setUzLiveInfoChangedListener(UZLiveInfoChangedListener)} instead
     */
    @Deprecated
    public void addUZLiveContentCallback(UZLiveContentCallback uzLiveContentCallback) {
        this.uzLiveContentCallback = uzLiveContentCallback;
    }

    /**
     * @deprecated use {@link UZVideo#setUzVideoStateChangedListener(UZVideoStateChangedListener)} instead
     */
    @Deprecated
    public void addUZCallback(UZCallback uzCallback) {
        this.uzCallback = uzCallback;
    }

    /**
     * @deprecated use {@link UZVideo#setUzVideoTVListener(UZVideoTVListener)} instead
     */
    @Deprecated
    public void addUZTVCallback(UZTVCallback uztvCallback) {
        this.uztvCallback = uztvCallback;
        handleFirstViewHasFocus();
    }

    /**
     * @deprecated use {@link UZVideo#setUzVideoStateChangedListener(UZVideoStateChangedListener)} instead
     */
    @Deprecated
    public void addProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    /**
     * @deprecated use {@link UZVideo#setUzTimeBarChangedListener(UZTimeBarChangedListener)} instead
     */
    @Deprecated
    public void addCallbackUZTimebar(CallbackUZTimebar callbackUZTimebar) {
        this.callbackUZTimebar = callbackUZTimebar;
    }

    /**
     * @deprecated use {@link UZVideo#setUzItemClickListener(UZItemClickListener)} instead
     */
    @Deprecated
    public void addItemClick(UZItemClick uzItemClick) {
        this.uzItemClick = uzItemClick;
    }

    /**
     * @deprecated use {@link UZVideo#setUzVideoStateChangedListener(UZVideoStateChangedListener)} instead
     */
    @Deprecated
    public void addStateEndCallback(StateEndCallback stateEndCallback) {
        this.stateEndCallback = stateEndCallback;
    }

    public void setUzLiveInfoChangedListener(UZLiveInfoChangedListener listener) {
        this.uzLiveInfoChangedListener = listener;
    }

    public void setUzVideoStateChangedListener(UZVideoStateChangedListener listener) {
        this.uzVideoStateChangedListener = listener;
    }

    public void setUzPlayerStateChangedListener(UZPlayerStateChangedListener listener) {
        this.uzPlayerStateChangedListener = listener;
    }

    public void setUzAdStateChangedListener(UZAdStateChangedListener listener) {
        this.uzAdStateChangedListener = listener;
    }

    public void setUzVideoTVListener(UZVideoTVListener listener) {
        this.uzVideoTVListener = listener;
        handleFirstViewHasFocus();
    }

    public void setUzTimeBarChangedListener(UZTimeBarChangedListener listener) {
        this.uzTimeBarChangedListener = listener;
    }

    public void setUzItemClickListener(UZItemClickListener listener) {
        this.uzItemClickListener = listener;
    }

    public void addControllerStateCallback(final UZPlayerView.ControllerStateCallback controllerStateCallback) {
        this.controllerStateCallback = controllerStateCallback;
    }

    public void addOnTouchEvent(UZPlayerView.OnTouchEvent onTouchEvent) {
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

    protected VideoAdPlayer.VideoAdPlayerCallback videoAdPlayerCallback;

    public void addVideoAdPlayerCallback(VideoAdPlayer.VideoAdPlayerCallback videoAdPlayerCallback) {
        this.videoAdPlayerCallback = videoAdPlayerCallback;
    }

    //=============================================================================================END EVENT
    //=============================================================================================START CALL API
    private boolean isCalledApiGetDetailEntity;
    private boolean isCalledAPIGetUrlIMAAdTag;
    private boolean isCalledAPIGetTokenStreaming;
    private Data data;

    private void callAPIGetDetailEntity() {
        //Neu da ton tai Data roi thi no duoc goi tu pip, minh ko can phai call api lay detail entity lam gi nua
        boolean isDataExist = UZData.getInstance().getData() != null;
        if (isDataExist) {
            //init player khi user click vào fullscreen của floating view (pic)
            isCalledApiGetDetailEntity = true;
            data = UZData.getInstance().getData();
            handleDataCallAPI();
        } else {
            UZUtil.getDetailEntity(getContext(), entityId, new CallbackGetDetailEntity() {
                @Override
                public void onSuccess(Data d) {
                    isCalledApiGetDetailEntity = true;
                    data = d;
                    //set video cover o moi case, ngoai tru
                    //click tu pip entity thi ko can show video cover
                    //click tu pip playlist folder lan dau tien thi ko can show video cover, neu nhan skip next hoac skip prev thi se show video cover
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

                @Override
                public void onError(Throwable e) {
                    UZData.getInstance().setSettingPlayer(false);
                    handleError(UZExceptionUtil.getExceptionCannotGetDetailEntitity());
                }
            });
        }
    }

    private void callAPIGetPlayerInfor() {
        int resLayout = UZData.getInstance().getCurrentPlayerId();
        if (resLayout != R.layout.uz_player_skin_0 && resLayout != R.layout.uz_player_skin_1 && resLayout != R.layout.uz_player_skin_2 && resLayout != R.layout.uz_player_skin_3) {
            return;
        }
        UZService service = UZRestClient.createService(UZService.class);
        String playerInforId = UZData.getInstance().getPlayerInforId();
        //LLog.d(TAG, "callAPIGetConfigSkin -> call api -> playerInforId: " + playerInforId);
        if (playerInforId == null || playerInforId.isEmpty()) {
            return;
        }
        UZAPIMaster.getInstance().subscribe(service.getPlayerInfo(UZData.getInstance().getAPIVersion(), playerInforId, UZData.getInstance().getAppId()), new ApiSubscriber<PlayerInfor>() {
            @Override
            public void onSuccess(PlayerInfor pi) {
                playerInfor = pi;
            }

            @Override
            public void onFail(Throwable e) {
                handleError(UZExceptionUtil.getExceptionPlayerInfor());
            }
        });
    }

    private void callAPIGetUrlIMAAdTag() {
        boolean isUrlIMAAdExist = UZData.getInstance().getUrlIMAAd() != null;
        if (isUrlIMAAdExist) {
            isCalledAPIGetUrlIMAAdTag = true;
            urlIMAAd = UZData.getInstance().getUrlIMAAd();
            handleDataCallAPI();
        } else {
            UZService service = UZRestClient.createService(UZService.class);
            UZAPIMaster.getInstance().subscribe(service.getCuePoint(UZData.getInstance().getAPIVersion(), entityId, UZData.getInstance().getAppId()), new ApiSubscriber<AdWrapper>() {
                @Override
                public void onSuccess(AdWrapper result) {
                    isCalledAPIGetUrlIMAAdTag = true;
                    if (result == null || result.getData() == null || result.getData().isEmpty()) {
                        urlIMAAd = "";
                    } else {
                        //Hien tai chi co the play ima ad o item thu 0
                        Ad ad = result.getData().get(0);
                        if (ad != null) {
                            urlIMAAd = ad.getLink();
                        }
                    }
                    handleDataCallAPI();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "callAPIGetUrlIMAAdTag onFail but ignored (dont care): " + e.getMessage());
                }
            });
        }
    }

    private void callAPIGetTokenStreaming() {
        boolean isGetTokenStreamingExist = UZData.getInstance().getResultGetTokenStreaming() != null;
        if (isGetTokenStreamingExist) {
            mResultGetTokenStreaming = UZData.getInstance().getResultGetTokenStreaming();
            isCalledAPIGetTokenStreaming = true;
            handleDataCallAPI();
        } else {
            UZService service = UZRestClient.createService(UZService.class);
            SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
            sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
            sendGetTokenStreaming.setEntityId(entityId);
            sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
            UZAPIMaster.getInstance().subscribe(service.getTokenStreaming(UZData.getInstance().getAPIVersion(), sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
                @Override
                public void onSuccess(ResultGetTokenStreaming resultGetTokenStreaming) {
                    if (resultGetTokenStreaming == null || resultGetTokenStreaming.getData() == null || resultGetTokenStreaming.getData().getToken() == null || resultGetTokenStreaming.getData().getToken().isEmpty()) {
                        handleError(UZExceptionUtil.getExceptionNoTokenStreaming());
                        return;
                    }
                    mResultGetTokenStreaming = resultGetTokenStreaming;
                    isCalledAPIGetTokenStreaming = true;
                    handleDataCallAPI();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "callAPIGetTokenStreaming onFail " + e.getMessage());
                    handleError(UZExceptionUtil.getExceptionNoTokenStreaming());
                }
            });
        }
    }

    private void callAPIGetLinkPlay() {
        if (mResultGetTokenStreaming == null || mResultGetTokenStreaming.getData() == null || mResultGetTokenStreaming.getData().getToken() == null) {
            handleError(UZExceptionUtil.getExceptionNoTokenStreaming());
            return;
        }
        boolean isResultGetLinkPlayExist = UZData.getInstance().getResultGetLinkPlay() != null;
        String appId = UZData.getInstance().getAppId();
        String[] appIdsWithMux = getResources().getStringArray(R.array.app_ids_with_mux);
        boolean contained = false;
        for (String appIdsWithMux1 : appIdsWithMux) {
            if (appIdsWithMux1.equals(appId)) {
                contained = true;
                break;
            }
        }
        isEnableMux = contained;
        if (isResultGetLinkPlayExist) {
            mResultGetLinkPlay = UZData.getInstance().getResultGetLinkPlay();
            try {
                cdnHost = mResultGetLinkPlay.getData().getCdn().get(0).getHost();
            } catch (NullPointerException e) {
                LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
                SentryUtils.captureException(e);
            }
            checkToSetUpResource();
        } else {
            String tokenStreaming = mResultGetTokenStreaming.getData().getToken();
            UZRestClientGetLinkPlay.addAuthorization(tokenStreaming);
            UZService service = UZRestClientGetLinkPlay.createService(UZService.class);
            if (isLivestream) {
                String channelName = UZData.getInstance().getChannelName();
                UZAPIMaster.getInstance().subscribe(service.getLinkPlayLive(appId, channelName), new ApiSubscriber<ResultGetLinkPlay>() {
                    @Override
                    public void onSuccess(ResultGetLinkPlay resultGetLinkPlay) {
                        handleLinkPlayResponse(resultGetLinkPlay);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getLinkPlayLive LIVE onFail " + e.getMessage());
                        handleError(UZExceptionUtil.getExceptionCannotGetLinkPlayLive());
                    }
                });
            } else {
                String typeContent = SendGetTokenStreaming.STREAM;
                UZAPIMaster.getInstance().subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
                    @Override
                    public void onSuccess(ResultGetLinkPlay resultGetLinkPlay) {
                        handleLinkPlayResponse(resultGetLinkPlay);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "callAPIGetLinkPlay VOD onFail " + e.getMessage());
                        handleError(UZExceptionUtil.getExceptionCannotGetLinkPlayVOD());
                    }
                });
            }
        }
    }

    private void handleLinkPlayResponse(ResultGetLinkPlay resultGetLinkPlay) {
        mResultGetLinkPlay = resultGetLinkPlay;
        UZData.getInstance().setResultGetLinkPlay(resultGetLinkPlay);
        try {
            cdnHost = mResultGetLinkPlay.getData().getCdn().get(0).getHost();
            TmpParamData.getInstance().setEntityCnd(cdnHost);
            TmpParamData.getInstance().setEntitySourceDomain(cdnHost);
            TmpParamData.getInstance().setEntitySourceHostname(cdnHost);
        } catch (NullPointerException e) {
            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
            SentryUtils.captureException(e);
        }
        checkToSetUpResource();
    }

    private void callAPIUpdateLiveInfoCurrentView(final int durationDelay) {
        if (!isLivestream || getContext() == null || activityIsPausing) {
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                if (!isLivestream) {
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (uzPlayerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    UZService service = UZRestClient.createService(UZService.class);
                    String id = UZData.getInstance().getEntityId();
                    UZAPIMaster.getInstance().subscribe(service.getViewALiveFeed(UZData.getInstance().getAPIVersion(), id, UZData.getInstance().getAppId()), new ApiSubscriber<ResultGetViewALiveFeed>() {
                        @Override
                        public void onSuccess(ResultGetViewALiveFeed result) {
                            if (result != null && result.getData() != null) {
                                if (tvLiveView != null) {
                                    tvLiveView.setText(String.valueOf(result.getData().getWatchnow()));
                                }
                                if (uzLiveContentCallback != null) {
                                    uzLiveContentCallback.onUpdateLiveInfoCurrentView(result.getData().getWatchnow());
                                }
                                if (uzLiveInfoChangedListener != null) {
                                    uzLiveInfoChangedListener.onCurrentViewChanged(result.getData().getWatchnow());
                                }
                            }
                            callAPIUpdateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "getViewALiveFeed onFail " + e.getMessage());
                            callAPIUpdateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                        }
                    });
                } else {
                    callAPIUpdateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    private void callAPIUpdateLiveInfoTimeStartLive(final int durationDelay) {
        if (!isLivestream || getContext() == null || activityIsPausing) {
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                if (!isLivestream || getContext() == null || activityIsPausing) {
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (uzPlayerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    if (startTime == Constants.UNKNOW) {
                        UZService service = UZRestClient.createService(UZService.class);
                        String entityId = UZData.getInstance().getEntityId();
                        String feedId = UZData.getInstance().getLastFeedId();
                        UZAPIMaster.getInstance().subscribe(service.getTimeStartLive(UZData.getInstance().getAPIVersion(), entityId, feedId, UZData.getInstance().getAppId()), new ApiSubscriber<ResultTimeStartLive>() {
                            @Override
                            public void onSuccess(ResultTimeStartLive result) {
                                if (result != null && result.getData() != null && result.getData().getStartTime() != null) {
                                    startTime = LDateUtils.convertDateToTimeStamp(result.getData().getStartTime(), LDateUtils.FORMAT_1);
                                    updateLiveInfoTimeStartLive();
                                }
                            }

                            @Override
                            public void onFail(Throwable e) {
                                LLog.e(TAG, "getTimeStartLive onFail " + e.getMessage());
                                callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                            }
                        });
                    } else {
                        updateLiveInfoTimeStartLive();
                    }
                } else {
                    callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }
    //=============================================================================================END CALL API

    private void handleDataCallAPI() {
        if (isCalledApiGetDetailEntity && isCalledAPIGetUrlIMAAdTag && isCalledAPIGetTokenStreaming) {
            if (UZData.getInstance().getUzInput() == null) {
                UZInput uzInput = new UZInput();
                uzInput.setData(data);
                uzInput.setUrlIMAAd(urlIMAAd);
                uzInput.setResultGetTokenStreaming(mResultGetTokenStreaming);
                //TODO iplm url thumbnail seekbar
                uzInput.setUrlThumnailsPreviewSeekbar(null);
                UZData.getInstance().setUizaInput(uzInput);
            }
            checkData();
        }
    }

    private boolean isGetClickedPip;

    private void checkData() {
        UZData.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UZData.getInstance().getEntityId() == null || UZData.getInstance().getEntityId().isEmpty()) {
            LLog.e(TAG, "checkData getEntityId null or empty -> return");
            handleError(UZExceptionUtil.getExceptionEntityId());
            UZData.getInstance().setSettingPlayer(false);
            return;
        }
        isLivestream = UZData.getInstance().isLivestream();
        isGetClickedPip = UZUtil.getClickedPip(getContext());
        LLog.d(TAG, "checkData isLivestream " + isLivestream + ", isGetClickedPip: " + isGetClickedPip);
        if (!isGetClickedPip) {
            setDefaultValueForFlagIsTracked();
        }
        if (uzPlayerManager != null) {
            releaseUzPlayerManager();
            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        setTitle();
        callAPIGetLinkPlay();
        trackUizaEventDisplay();
        trackUizaEventPlaysRequested();
    }

    private void checkToSetUpResource() {
        if (UZData.getInstance().getResultGetLinkPlay() != null && UZData.getInstance().getData() != null) {
            List<String> listLinkPlay = new ArrayList<>();
            List<Url> urlList = mResultGetLinkPlay.getData().getUrls();
            if (isLivestream) {
                //Bat buoc dung linkplay m3u8 cho nay, do bug cua system
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
            if (countTryLinkPlayError > (listLinkPlay.size() - 1)) {
                if (LConnectivityUtil.isConnected(getContext())) {
                    removeVideoCover(true);
                    handleError(UZExceptionUtil.getExceptionTryAllLinkPlay());
                } else {
                    handleError(UZExceptionUtil.getExceptionNoConnection());
                }
                return;
            }
            String linkPlay = listLinkPlay.get(countTryLinkPlayError);
            List<Subtitle> subtitleList = null;
            //TODO iplm v3 chua co subtitle
            addTrackingMuiza(Constants.MUIZA_EVENT_READY);
            if (isCalledFromChangeSkin) {
                //if called from func changeSkin(), dont initDataSource with uilIMA Ad.
                initDataSource(linkPlay, null, UZData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
            } else {
                initDataSource(linkPlay, UZData.getInstance().getUrlIMAAd(), UZData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
            }
            if (uzCallback != null) {
                uzCallback.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
            }
            if (uzVideoStateChangedListener != null) {
                uzVideoStateChangedListener.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
            }
            initUizaPlayerManager();
        } else {
            handleError(UZExceptionUtil.getExceptionSetup());
        }
    }

//    protected MuxStatsExoPlayer muxStatsExoPlayer;

    private long timestampInitDataSource;

    private void initDataSource(String linkPlay, String urlIMAAd, String urlThumbnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        timestampInitDataSource = System.currentTimeMillis();
        LLog.d(TAG, "-------------------->initDataSource linkPlay " + linkPlay);
        TmpParamData.getInstance().setEntitySourceUrl(linkPlay);
        TmpParamData.getInstance().setTimeFromInitEntityIdToAllApiCalledSuccess(System.currentTimeMillis() - timestampBeforeInitNewSession);
        if (uzPlayerManager == null) {
            uzPlayerManager = new UZPlayerManager();
        }
        uzPlayerManager.initUZPlayerManager(this, linkPlay, urlIMAAd, urlThumbnailsPreviewSeekbar, subtitleList);
        if (uzTimebar != null) {
            if (urlThumbnailsPreviewSeekbar == null || urlThumbnailsPreviewSeekbar.isEmpty()) {
                uzTimebar.setPreviewEnabled(false);
            } else {
                uzTimebar.setPreviewEnabled(true);
            }
            uzTimebar.setPreviewLoader(uzPlayerManager);
        }
        uzPlayerManager.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdEnded() {
                setDefaultUseController(isDefaultUseController());
                if (progressCallback != null) {
                    progressCallback.onAdEnded();
                }
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
                updateUIIbRewIconDependOnProgress(currentMls, false);
                trackProgress(s, percent);
                callAPITrackMuiza(s);
                if (progressCallback != null) {
                    progressCallback.onVideoProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (progressCallback != null) {
                    progressCallback.onPlayerStateChanged(playWhenReady, playbackState);
                }
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                if (progressCallback != null) {
                    progressCallback.onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }
        });

        uzPlayerManager.setUzAdStateChangedListener(new UZAdStateChangedListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                if (uzAdStateChangedListener != null) {
                    uzAdStateChangedListener.onAdProgress(s, duration, percent);
                }
            }

            @Override
            public void onAdEnded() {
                setDefaultUseController(isDefaultUseController());
                if (uzAdStateChangedListener != null) {
                    uzAdStateChangedListener.onAdEnded();
                }
            }
        });

        uzPlayerManager.setUzVideoStateChangedListener(new UZVideoStateChangedListener() {
            @Override
            public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
                    ResultGetLinkPlay resultGetLinkPlay, Data data) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (uzVideoStateChangedListener != null) {
                    uzVideoStateChangedListener.onPlayerStateChanged(playWhenReady, playbackState);
                }
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                TmpParamData.getInstance().setPlayerPlayheadTime(s);
                updateUIIbRewIconDependOnProgress(currentMls, false);
                trackProgress(s, percent);
                callAPITrackMuiza(s);
                if (uzVideoStateChangedListener != null) {
                    uzVideoStateChangedListener.onVideoProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                if (uzVideoStateChangedListener != null) {
                    uzVideoStateChangedListener.onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onError(UZException exception) {

            }
        });

        uzPlayerManager.setDebugCallback(new UZPlayerManager.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                updateUIButtonVisibilities();
            }
        });

        uzPlayerManager.setBufferCallback(new UZBufferCallback() {
            @Override
            public void onBufferChanged(long bufferedDurationUs, float playbackSpeed) {
                if (visualizeInfoHelper != null) {
                    visualizeInfoHelper.setBufferedDurationUs(bufferedDurationUs);
                }
            }
        });

        uzPlayerManager.setUzVideoBufferChangedListener(new UZVideoBufferChangedListener() {
            @Override
            public void onBufferChanged(long bufferedDurationUs, float playbackSpeed) {
                if (visualizeInfoHelper != null) {
                    visualizeInfoHelper.setBufferedDurationUs(bufferedDurationUs);
                }
            }
        });
    }

    protected void onStateReadyFirst() {
        UZData.getInstance().setSettingPlayer(false);
        long pageLoadTime = System.currentTimeMillis() - timestampBeforeInitNewSession;
        TmpParamData.getInstance().setPageLoadTime(pageLoadTime);
        addTrackingMuiza(Constants.MUIZA_EVENT_VIEWSTART);
        TmpParamData.getInstance().setViewStart(System.currentTimeMillis());
        TmpParamData.getInstance().setViewTimeToFirstFrame(System.currentTimeMillis());
        updateTvDuration();
        updateUIButtonPlayPauseDependOnIsAutoStart();
        updateUIDependOnLivestream();
        if (isSetUZTimebarBottom) {
            ViewUtils.visibleViews(uzPlayerView);
        }
        resizeContainerView();
        //enable from playPlaylistPosition() prevent double click
        setClickableForViews(true, ibSkipPreviousIcon, ibSkipNextIcon);
        if (isGetClickedPip) {
            LLog.d(TAG, "getClickedPip true -> setPlayWhenReady true");
            uzPlayerManager.getPlayer().setPlayWhenReady(true);
        }
        if (UZData.getInstance().isUseWithVDHView()) {
            showController();
        }
        if (uzCallback != null) {
            uzCallback.isInitResult(true, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        if (uzVideoStateChangedListener != null) {
            uzVideoStateChangedListener.isInitResult(true, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        if (isCasting()) {
            lastCurrentPosition = 0;
            handleConnectedChromecast();
            showController();
        }
        updateUIPlayerInfo();
        TmpParamData.getInstance().setSessionStart(System.currentTimeMillis());
        long playerStartUpTime = System.currentTimeMillis() - timestampInitDataSource;
        TmpParamData.getInstance().setPlayerStartupTime(playerStartUpTime);
        trackUizaEventVideoStarts();
        trackUizaCCUForLivestream();
        pingHeartBeat();
    }

    private void initUizaPlayerManager() {
        if (uzPlayerManager != null) {
            uzPlayerManager.init();
            addTrackingMuiza(Constants.MUIZA_EVENT_LOADSTART);
            if (isGetClickedPip && !isPlayPlaylistFolder()) {
                uzPlayerManager.getPlayer().setPlayWhenReady(false);
            } else {
                if (isRefreshFromChangeSkin) {
                    seek(currentPositionBeforeChangeSkin);
                    isRefreshFromChangeSkin = false;
                    currentPositionBeforeChangeSkin = 0;
                }
            }
            if (isCalledFromConnectionEventBus) {
                uzPlayerManager.setRunnable();
                isCalledFromConnectionEventBus = false;
            }
            if (isEnableMux) {
//                CustomerPlayerData customerPlayerData = new CustomerPlayerData();
//                customerPlayerData.setEnvironmentKey(getContext().getString(R.string.mux_environment_key));
//                CustomerVideoData customerVideoData = new CustomerVideoData();
//                customerVideoData.setVideoTitle(UZData.getInstance().getEntityName());
//                muxStatsExoPlayer = new MuxStatsExoPlayer(getContext(), getPlayer(), Constants.PLAYER_NAME, customerPlayerData, customerVideoData);
//                Point size = new Point();
//                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(size);
//                muxStatsExoPlayer.setScreenSize(size.x, size.y);
//                muxStatsExoPlayer.setPlayerView(uzPlayerView.getVideoSurfaceView());
            }
            // Always using this options
            if (visualizeInfoHelper == null) {
                visualizeInfoHelper = new UZVideoVisualizeInfoHelper(this, statsForNerdsView);
            }
            visualizeInfoHelper.initStatsForNerds();
        }
    }

    //=============================================================================================START TRACKING
    private int oldPercent = Constants.NOT_FOUND;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    private void setDefaultValueForFlagIsTracked() {
        UZTrackingUtil.clearAllValues(getContext());
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
            if (!UZTrackingUtil.isTrackedEventTypeView(getContext())) {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIEW), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypeView(getContext(), true);
                    }
                });
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
            if (UZTrackingUtil.isTrackedEventTypePlayThrought100(getContext())) {
                isTracked100 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_100, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_75, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_50, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), PLAY_THROUGH_25, Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(getContext(), true);
                    }
                });
            }
        }
    }

    private void callAPITrackUiza(final UizaTracking uizaTracking, final UZTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        if (getContext() == null || isInitCustomLinkPlay) {
            return;
        }
        UZService service = UZRestClientTracking.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
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

    private void trackUizaEventVideoStarts() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UZTrackingUtil.isTrackedEventTypeVideoStarts(getContext())) {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_VIDEO_STARTS), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(getContext(), true);
                }
            });
        }
    }

    private void trackUizaEventDisplay() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UZTrackingUtil.isTrackedEventTypeDisplay(getContext())) {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_DISPLAY), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(getContext(), true);
                }
            });
        }
    }

    private void trackUizaEventPlaysRequested() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (!UZTrackingUtil.isTrackedEventTypePlaysRequested(getContext())) {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(getContext(), Constants.EVENT_TYPE_PLAYS_REQUESTED), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(getContext(), true);
                }
            });
        }
    }

    private final int INTERVAL_HEART_BEAT = 10000;

    private void pingHeartBeat() {
        if (getContext() == null || cdnHost == null || cdnHost.isEmpty()) {
            LLog.e(TAG, "Error cannot call API pingHeartBeat() -> cdnHost: " + cdnHost + ", entityName: " + UZData.getInstance().getEntityName() + " -> return");
            return;
        }
        if (activityIsPausing) {
            rePingHeartBeat();
            LLog.e(TAG, "Error cannot call API pingHeartBeat() because activity is pausing " + UZData.getInstance().getEntityName());
            return;
        }
        UZService service = UZRestClientHeartBeat.createService(UZService.class);
        String cdnName = cdnHost;
        String session = uuid.toString();
        UZAPIMaster.getInstance().subscribe(service.pingHeartBeat(cdnName, session), new ApiSubscriber<Object>() {
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
        LUIUtil.setDelay(INTERVAL_HEART_BEAT, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                pingHeartBeat();
            }
        });
    }

    private final int INTERVAL_TRACK_CCU = 3000;

    private void trackUizaCCUForLivestream() {
        if (getContext() == null || !isLivestream || isInitCustomLinkPlay) {
            LLog.e(TAG, "Error cannot trackUizaCCUForLivestream() -> return " + UZData.getInstance().getEntityName());
            return;
        }
        if (activityIsPausing) {
            reTrackUizaCCUForLivestream();
            LLog.e(TAG, "Error cannot trackUizaCCUForLivestream() because activity is pausing " + UZData.getInstance().getEntityName());
            return;
        }
        UZService service = UZRestClientTracking.createService(UZService.class);
        UizaTrackingCCU uizaTrackingCCU = new UizaTrackingCCU();
        uizaTrackingCCU.setDt(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        uizaTrackingCCU.setHo(cdnHost);
        uizaTrackingCCU.setAi(UZData.getInstance().getAppId());
        uizaTrackingCCU.setSn(UZData.getInstance().getEntityName());
        uizaTrackingCCU.setDi(UZOsUtil.getDeviceId(getContext()));
        uizaTrackingCCU.setUa(Constants.USER_AGENT);
        UZAPIMaster.getInstance().subscribe(service.trackCCU(uizaTrackingCCU), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                reTrackUizaCCUForLivestream();
            }

            @Override
            public void onFail(Throwable e) {
                reTrackUizaCCUForLivestream();
            }
        });
    }

    private void reTrackUizaCCUForLivestream() {
        LUIUtil.setDelay(INTERVAL_TRACK_CCU, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                trackUizaCCUForLivestream();
            }
        });
    }

    protected void addTrackingMuizaError(String event, UZException e) {
        if (!isInitCustomLinkPlay) {
            UZData.getInstance().addTrackingMuiza(getContext(), event, e);
        }
    }

    protected void addTrackingMuiza(String event) {
        if (!isInitCustomLinkPlay) {
            UZData.getInstance().addTrackingMuiza(getContext(), event);
        }
    }

    private boolean isTrackingMuiza;

    private void callAPITrackMuiza(int s) {
        if (isInitCustomLinkPlay || (s <= 0 || s % 10 != 0) || isTrackingMuiza || UZData.getInstance().isMuizaListEmpty()) {
            return;
        }
        isTrackingMuiza = true;
        final List<Muiza> muizaListToTracking = new ArrayList<>(UZData.getInstance().getMuizaList());
        UZData.getInstance().clearMuizaList();
        UZService service = UZRestClientTracking.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.trackMuiza(muizaListToTracking), new ApiSubscriber<Object>() {
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
    //=============================================================================================END TRACKING

    //=============================================================================================START EVENTBUS
    private boolean isCalledFromConnectionEventBus = false;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        if (event == null) return;
        if (event.isConnected()) {
            if (uzPlayerManager == null) return;
            LDialogUtil.clearAll();
            if (uzPlayerManager.getExoPlaybackException() == null) {
                hideController();
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
        } else {
            handleError(UZExceptionUtil.getExceptionNoConnection());
        }
    }

    private long positionMiniPlayer;

    public boolean isInitNewItem(String urlImgThumbnail) {
        if (positionMiniPlayer != 0) {
            seek(positionMiniPlayer);
            play();
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

    //listen msg from service FUZVideoService
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromService msg) {
        if (msg == null || uzPlayerManager == null) {
            return;
        }
        //click open app of mini player
        if (msg instanceof ComunicateMng.MsgFromServiceOpenApp) {
            if (getContext() == null) {
                return;
            }
            LLog.d(TAG, "miniplayer STEP 6");
            try {
                positionMiniPlayer = ((ComunicateMng.MsgFromServiceOpenApp) msg).getPositionMiniPlayer();
                Class classNamePfPlayer = Class.forName(((Activity) getContext()).getLocalClassName());
                Intent intent = new Intent(getContext(), classNamePfPlayer);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, UZData.getInstance().getEntityId());
                getContext().startActivity(intent);
            } catch (ClassNotFoundException e) {
                LLog.e(TAG, "onMessageEvent open app ClassNotFoundException " + e.toString());
                SentryUtils.captureException(e);
            }
            return;
        }
        //when pip float view init success
        if ((uzCallback != null || uzPlayerStateChangedListener != null)
                && msg instanceof ComunicateMng.MsgFromServiceIsInitSuccess) {
            //Ham nay duoc goi khi player o FUZVideoService da init xong
            //Nhiem vu la minh se gui vi tri hien tai sang cho FUZVideoService no biet
            LLog.d(TAG, "miniplayer STEP 3 UZVideo biet FUZVideoService da init xong -> gui lai content position cua UZVideo cho FUZVideoService");
            ComunicateMng.MsgFromActivityPosition msgFromActivityPosition = new ComunicateMng.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);
            isInitMiniPlayerSuccess = true;
            if (uzCallback != null) {
                uzCallback.onStateMiniPlayer(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
            }
            if (uzPlayerStateChangedListener != null) {
                uzPlayerStateChangedListener.onStateMiniPlayer(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
            }
        }
    }

    public void sendEventInitSuccess() {
        ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
        msgFromActivityIsInitSuccess.setInitSuccess(true);
        ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
    }
    //=============================================================================================END EVENTBUS

    //=============================================================================================START CHROMECAST
    @UiThread
    private void setUpMediaRouteButton() {
        if (isTV) {
            return;
        }
        UZData.getInstance().getCasty().setUpMediaRouteButton(uzMediaRouteButton);
        UZData.getInstance().getCasty().setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
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
        });
    }

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

    private boolean isCastingChromecast;

    /**
     * @deprecated use {@link UZVideo#isCasting()} instead
     */
    @Deprecated
    public boolean isCastingChromecast() {
        return isCasting();
    }

    /**
     * @return true if video is casting via chrome cast
     */
    public boolean isCasting() {
        return isCastingChromecast;
    }

    //last current position lúc từ exoplayer switch sang cast player
    private long lastCurrentPosition;

    private void playChromecast() {
        if (UZData.getInstance().getData() == null || uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return;
        }
        showProgress();
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UZData.getInstance().getData().getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, UZData.getInstance().getData().getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(UZData.getInstance().getData().getThumbnail())));

        //TODO add subtitle vtt to chromecast
        List<MediaTrack> mediaTrackList = new ArrayList<>();
        long duration = getDuration();
        if (duration < 0) {
            LLog.e(TAG, "invalid duration -> cannot play chromecast");
            return;
        }

        MediaInfo mediaInfo = new MediaInfo.Builder(
                uzPlayerManager.getLinkPlay())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .setMediaTracks(mediaTrackList)
                .setStreamDuration(duration)
                .build();

        //play chromecast with full screen control
        //UZData.getInstance().getCasty().getPlayer().loadMediaAndPlay(mediaInfo, true, lastCurrentPosition);

        //play chromecast without screen control
        UZData.getInstance().getCasty().getPlayer().loadMediaAndPlayInBackground(mediaInfo, true, lastCurrentPosition);

        UZData.getInstance().getCasty().getPlayer().getRemoteMediaClient().addProgressListener(new RemoteMediaClient.ProgressListener() {
            @Override
            public void onProgressUpdated(long currentPosition, long duration) {
                if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                    hideProgress();
                    isCastPlayerPlayingFirst = true;
                }

                if (currentPosition > 0) {
                    seek(currentPosition);
                }
            }
        }, 1000);

    }

    private boolean isCastPlayerPlayingFirst;

    /*khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnect thì local player sẽ resume*/
    private void updateUIChromecast() {
        if (uzPlayerManager == null || rlChromeCast == null || isTV) {
            return;
        }
        if (isCasting()) {
            uzPlayerManager.pauseVideo();
            setVolume(0f);
            ViewUtils.visibleViews(rlChromeCast);
            ViewUtils.goneViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon, ibVolumeIcon);
            updatePlayPauseIcon(true, false);
            //casting player luôn play first với volume not mute
            //UZData.getInstance().getCasty().setVolume(0.99);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
            }
        } else {
            uzPlayerManager.resumeVideo();
            setVolume(0.99f);
            rlChromeCast.setVisibility(GONE);
            ViewUtils.goneViews(rlChromeCast);
            ViewUtils.visibleViews(ibSettingIcon, ibCcIcon, ibBackScreenIcon);
            updatePlayPauseIcon(true, false);
            //TODO iplm volume mute on/off o cast player
            //khi quay lại exoplayer từ cast player thì mặc định sẽ bật lại âm thanh (dù cast player đang mute hay !mute)
            //setVolume(0.99f);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(defaultValueControllerTimeout);
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
            myJob = new JobInfo.Builder(0, new ComponentName(getContext(), LConectifyService.class))
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
}

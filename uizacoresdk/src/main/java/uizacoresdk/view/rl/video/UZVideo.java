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
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
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
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uizacoresdk.R;
import uizacoresdk.chromecast.Casty;
import uizacoresdk.interfaces.CallbackUZTimebar;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.interfaces.UZLiveContentCallback;
import uizacoresdk.interfaces.UZTVCallback;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.Loitp;
import uizacoresdk.util.SensorOrientationChangeNotifier;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZInput;
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
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.CallbackGetDetailEntity;
import vn.uiza.views.autosize.UZImageButton;
import vn.uiza.views.autosize.UZTextView;
import vn.uiza.views.seekbar.UZVerticalSeekBar;

/**
 * Created by loitp on 2/27/2019.
 */

public class UZVideo extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, View.OnFocusChangeListener, UZPlayerView.ControllerStateCallback, SensorOrientationChangeNotifier.Listener {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private int DEFAULT_VALUE_BACKWARD_FORWARD = 10000;//10000 mls
    private int DEFAULT_VALUE_CONTROLLER_TIMEOUT = 8000;//8000 mls
    private final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private final int DELAY_TO_GET_LIVE_INFORMATION = 15000;
    private Activity activity;
    private boolean isLivestream;
    private boolean isTablet;
    private String cdnHost;
    private boolean isTV;//current device is TV or not (smartphone, tablet)
    //private Gson gson = new Gson();
    private View bkg;
    private RelativeLayout rootView;
    private UZPlayerManager uzPlayerManager;
    private ProgressBar progressBar;
    private LinearLayout llTop;
    private FrameLayout previewFrameLayout;
    private UZTimebar uzTimebar;
    private ImageView ivThumbnail;
    private UZTextView tvPosition;
    private UZTextView tvDuration;
    private RelativeLayout rlMsg;
    private TextView tvMsg;
    private ImageView ivVideoCover;
    private UZImageButton ibFullscreenIcon;
    private TextView tvTitle;
    private UZImageButton ibPauseIcon;
    private UZImageButton ibPlayIcon;
    private UZImageButton ibReplayIcon;
    private UZImageButton ibRewIcon;
    private UZImageButton ibFfwdIcon;
    private UZImageButton ibBackScreenIcon;
    private UZImageButton ibVolumeIcon;
    private UZImageButton ibSettingIcon;
    private UZImageButton ibCcIcon;
    private UZImageButton ibPlaylistFolderIcon;//danh sach playlist folder
    private UZImageButton ibHearingIcon;
    private UZImageButton ibPictureInPictureIcon;
    private UZImageButton ibShareIcon;
    private UZImageButton ibSkipPreviousIcon;
    private UZImageButton ibSkipNextIcon;
    private UZImageButton ibSpeedIcon;
    private RelativeLayout rlLiveInfo;
    private TextView tvLiveStatus;
    private TextView tvLiveView;
    private TextView tvLiveTime;
    private UZImageButton ivLiveTime;
    private UZImageButton ivLiveView;
    private LinearLayout debugLayout;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private RelativeLayout rlEndScreen;
    private TextView tvEndScreenMsg;
    //chromecast https://github.com/DroidsOnRoids/Casty
    private UZMediaRouteButton uzMediaRouteButton;
    private RelativeLayout rlChromeCast;
    private UZImageButton ibsCast;
    private UZPlayerView uzPlayerView;

    private ResultGetLinkPlay mResultGetLinkPlay;
    private ResultGetTokenStreaming mResultGetTokenStreaming;
    private String urlIMAAd = null;

    private long startTime = Constants.UNKNOW;
    private boolean isSetUZTimebarBottom;

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
        activity = (Activity) getContext();
        EventBus.getDefault().register(this);
        //register LConectifyService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(activity, LConectifyService.class);
            activity.startService(startServiceIntent);
        }
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        isTablet = LDeviceUtil.isTablet(activity);
        isTV = LDeviceUtil.isTV(activity);
        addPlayerView();
        findViews();
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), getVideoW(), getVideoH(), isFreeSize);
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        //setup chromecast
        if (!isTV) {
            uzMediaRouteButton = new UZMediaRouteButton(activity);
            if (llTop != null) {
                llTop.addView(uzMediaRouteButton);
            }
            setUpMediaRouteButton();
            addUIChromecastLayer();
        }
        updateUISizeThumnail();
        scheduleJob();//for LConnectifyService
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

    private boolean isAutoShow;

    public void setControllerAutoShow(boolean isAutoShow) {
        this.isAutoShow = isAutoShow;
        uzPlayerView.setControllerAutoShow(isAutoShow);
    }

    public boolean getControllerAutoShow() {
        if (uzPlayerView == null) {
            return false;
        }
        return uzPlayerView.getControllerAutoShow();
    }

    /*
     **The duration of the playing content
     */
    public long getDuration() {
        if (uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        return uzPlayerManager.getPlayer().getDuration();
    }

    //An estimate of the position in the current window up to which data is buffered. If the length of the content is 100,00 ms, and played 50,000 ms already with extra 50,000 ms~ 60,000 ms buffered, it returns 60,000 ms.
    public long getBufferedPosition() {
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        //LLog.d(TAG, "getBufferedPosition " + getPlayer().getBufferedPosition());
        return getPlayer().getBufferedPosition();
    }

    //An estimate of the percentage in the current window up to which data is buffered. If the length of the content is 100,00 ms, and played 50,000 ms already with extra 50,000 ms~ 60,000 ms buffered, it returns 60(%).
    public int getBufferedPercentage() {
        if (getPlayer() == null) {
            return Constants.NOT_FOUND;
        }
        //LLog.d(TAG, "getBufferedPosition " + getPlayer().getBufferedPosition());
        return getPlayer().getBufferedPercentage();
    }

    //Lay pixel dung cho custom UI like youtube, uzTimebar bottom of player controller
    private int getPixelAdded() {
        if (isSetUZTimebarBottom) {
            return getHeightUZTimeBar() / 2;
        }
        return 0;
    }

    public int getVideoW() {
        if (uzPlayerManager == null) {
            return 0;
        }
        return uzPlayerManager.getVideoW();
    }

    public int getVideoH() {
        if (uzPlayerManager == null) {
            return 0;
        }
        return uzPlayerManager.getVideoH();
    }

    //return pixel
    public int getHeightUZTimeBar() {
        return LUIUtil.getHeightOfView(uzTimebar);
    }//The current position of playing. the window means playable region, which is all of the content if vod, and some portion of the content if live.

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

    //the current track selections for each renderer
    public TrackSelectionArray getCurrentTrackSelections() {
        if (getPlayer() == null) {
            return null;
        }
        return getPlayer().getCurrentTrackSelections();
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
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), getVideoW(), getVideoH(), isFreeSize);
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

    protected void handleError(UZException e) {
        if (e == null) {
            return;
        }
        if (uzCallback != null) {
            uzCallback.onError(e);
        }
        addTrackingMuizaError(Constants.MUIZA_EVENT_ERROR, e);
        if (isHasError) {
            //LLog.e(TAG, "handleError isHasError=true -> return -> isLivestream: " + isLivestream);
            return;
        }
        //LLog.e(TAG, "handleError " + e.toString());
        isHasError = true;
        UZData.getInstance().setSettingPlayer(false);
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    private String entityId;
    private UUID uuid;
    private long timestampBeforeInitNewSession;

    protected void init(@NonNull String entityId, boolean isClearDataPlaylistFolder) {
        LLog.d(TAG, "*****NEW SESSION**********************************************************************************************************************************");
        LLog.d(TAG, "entityId " + entityId);
        uuid = UUID.randomUUID();
        if (isClearDataPlaylistFolder) {
            UZData.getInstance().clearDataForPlaylistFolder();
        }
        if (entityId == null) {
            LLog.e(TAG, "init error because entityId == null -> called from PIP");
            try {
                if (UZData.getInstance().getData().getId() == null || UZData.getInstance().getData().getId().isEmpty()) {
                    if (uzCallback != null) {
                        uzCallback.onError(UZExceptionUtil.getExceptionEntityId());
                    }
                    LLog.e(TAG, "init error: entityId null or empty");
                    return;
                } else {
                    entityId = UZData.getInstance().getEntityId();
                }
                LLog.d(TAG, "get entityId: " + entityId);
            } catch (NullPointerException e) {
                if (uzCallback != null) {
                    uzCallback.onError(UZExceptionUtil.getExceptionEntityId());
                }
                LLog.e(TAG, "init NullPointerException: " + e.toString());
                return;
            }
        } else {
            //UZData.getInstance().setFirstResizeLayout(true);
            timestampBeforeInitNewSession = System.currentTimeMillis();
            timestampOnStartPreview = 0;
            maxSeekLastDuration = 0;
            UZData.getInstance().clearUizaInput();
            TmpParamData.getInstance().clearAll();
            TmpParamData.getInstance().addPlayerViewCount();
            TmpParamData.getInstance().setSessionId(uuid.toString());
        }
        LLog.d(TAG, "isPlayWithPlaylistFolder " + UZData.getInstance().isPlayWithPlaylistFolder());
        if (UZData.getInstance().isPlayWithPlaylistFolder()) {
            setVisibilityOfPlaylistFolderController(View.VISIBLE);
        } else {
            setVisibilityOfPlaylistFolderController(View.GONE);
        }
        isCalledFromChangeSkin = false;
        isInitCustomLinkPlay = false;
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
        mResultGetTokenStreaming = null;
        isHasError = false;
        isOnPlayerEnded = false;
        this.entityId = entityId;
        UZData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
        updateUIEndScreen();
        if (!LConnectivityUtil.isConnected(activity)) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionNoConnection());
            }
            return;
        }
        callAPIGetDetailEntity();
        callAPIGetUrlIMAAdTag();
        callAPIGetTokenStreaming();
        //TODO api setting config here
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId) {
        init(entityId, true);
    }

    //user pass any link (not use entityId or metadataId)
    private boolean isInitCustomLinkPlay;

    public boolean isInitCustomLinkPlay() {
        return isInitCustomLinkPlay;
    }

    public void initCustomLinkPlay(@NonNull String linkPlay, boolean isLivestream) {
        LLog.d(TAG, "*****NEW SESSION**********************************************************************************************************************************");
        LLog.d(TAG, "init linkPlay " + linkPlay);
        //UZData.getInstance().setFirstResizeLayout(true);
        isInitCustomLinkPlay = true;
        isCalledFromChangeSkin = false;
        setVisibilityOfPlaylistFolderController(View.GONE);
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
        mResultGetTokenStreaming = null;
        this.entityId = null;
        UZData.getInstance().setSettingPlayer(true);
        hideLayoutMsg();
        setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
        isOnPlayerEnded = false;
        updateUIEndScreen();
        isHasError = false;
        this.isLivestream = isLivestream;
        if (isLivestream) {
            startTime = Constants.UNKNOW;
        }
        setDefautValueForFlagIsTracked();
        if (uzPlayerManager != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            uzPlayerManager.release();
            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        updateUIDependOnLivetream();
        List<Subtitle> subtitleList = null;
        //TODO iplm init linkplay chua co subtitle
        /*subtitleList = new ArrayList<>();
        Subtitle subtitle = new Subtitle();
        subtitle.setLanguage("en");
        subtitle.setUrl("https://www.iandevlin.com/html5test/webvtt/upc-video-subtitles-en.vtt");
        subtitleList.add(subtitle);*/

        //List<Subtitle> subtitleList = mResultRetrieveAnEntity.getData().get(0).getSubtitle();
        //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

        if (!LConnectivityUtil.isConnected(activity)) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionNoConnection());
            }
            return;
        }
        initDataSource(linkPlay, UZData.getInstance().getUrlIMAAd(), UZData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
        if (uzCallback != null) {
            uzCallback.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
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

    private int countTryLinkPlayError = 0;

    protected void tryNextLinkPlay() {
        if (isLivestream) {
            //try to play 5 times
            if (countTryLinkPlayError >= 5) {
                showTvMsg(activity.getString(R.string.err_live_is_stopped));
            }
            //if entity is livestreaming, dont try to next link play
            LLog.e(TAG, "tryNextLinkPlay isLivestream true -> try to replay = count " + countTryLinkPlayError);
            if (uzPlayerManager != null) {
                uzPlayerManager.initWithoutReset();
                uzPlayerManager.setRunnable();
            }
            countTryLinkPlayError++;
            return;
        }
        countTryLinkPlayError++;
        LLog.e(TAG, activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        uzPlayerManager.release();
        checkToSetUpResouce();
    }

    //khi call api callAPIGetLinkPlay nhung json tra ve ko co data
    //se co gang choi video da play gan nhat
    //neu co thi se play
    //khong co thi bao loi
    private void handleErrorNoData() {
        LLog.e(TAG, "handleErrorNoData");
        removeVideoCover(true);
        LDialogUtil.showDialog1Immersive(activity, UZException.ERR_23, new LDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                if (uzCallback != null) {
                    UZData.getInstance().setSettingPlayer(false);
                    uzCallback.isInitResult(false, false, null, null);
                }
            }

            @Override
            public void onCancel() {
                if (uzCallback != null) {
                    UZData.getInstance().setSettingPlayer(false);
                    uzCallback.isInitResult(false, false, null, null);
                }
            }
        });
    }

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    public void onDestroy() {
        //cannot use isGetClikedPip (global variable), must use UZUtil.getClickedPip(activity)
        if (UZUtil.getClickedPip(activity)) {
            UZUtil.stopMiniPlayer(activity);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.stopService(new Intent(activity, LConectifyService.class));
        }
        activity = null;
        if (uzPlayerManager != null) {
            uzPlayerManager.release();
        }
        UZData.getInstance().setSettingPlayer(false);
        LDialogUtil.clearAll();
        activityIsPausing = true;
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        cdnHost = null;
        //LLog.d(TAG, "onDestroy -> set activityIsPausing = true");
        EventBus.getDefault().unregister(this);
    }

    public void onResume() {
        if (isCastingChromecast) {
            //LLog.d(TAG, "onResume isCastingChromecast true => return");
            return;
        }
        activityIsPausing = false;
        SensorOrientationChangeNotifier.getInstance(activity).addListener(this);
        if (uzPlayerManager != null) {
            if (ibPlayIcon != null && ibPlayIcon.getVisibility() == VISIBLE) {
                //LLog.d(TAG, "onResume uzPlayerManager != null - if -> do nothing");
            } else {
                //LLog.d(TAG, "onResume uzPlayerManager != null - else -> resumeVideo");
                uzPlayerManager.resumeVideo();
            }
        } else {
            //LLog.d(TAG, "onResume uzPlayerManager == null -> do nothing");
        }
    }

    /*public void onStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(activity, LConectifyService.class);
            activity.startService(startServiceIntent);
        }
    }

    public void onStop() {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.stopService(new Intent(activity, LConectifyService.class));
        }
    }*/

    public boolean isPlaying() {
        if (uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return false;
        }
        return uzPlayerManager.getPlayer().getPlayWhenReady();
    }

    private boolean activityIsPausing = false;

    private boolean isActivityIsPausing() {
        return activityIsPausing;
    }

    public void onPause() {
        activityIsPausing = true;
        SensorOrientationChangeNotifier.getInstance(activity).remove(this);
        if (uzPlayerManager != null) {
            uzPlayerManager.pauseVideo();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //LLog.d(TAG, "onActivityResult " + requestCode + " - " + resultCode);
        if (activity == null) {
            return;
        }
        if (isCastingChromecast()) {
            LLog.e(TAG, "Error: handleClickPictureInPicture isCastingChromecast -> return");
            return;
        }
        if (LDeviceUtil.isCanOverlay(activity)) {
            initializePiP();
        }
    }

    private long timestampOnStartPreview;

    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        //LLog.d(TAG, "PreviewView onStartPreview");
        timestampOnStartPreview = System.currentTimeMillis();
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKING);
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onStartPreview(previewView, progress);
        }
    }

    private boolean isOnPreview;

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        //LLog.d(TAG, "PreviewView onPreview progress " + progress + " - " + uzTimebar.getMax());
        isOnPreview = true;
        if (isCastingChromecast) {
            UZData.getInstance().getCasty().getPlayer().seek(progress);
        }
        updateUIIbRewIconDependOnProgress(progress, true);
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onPreview(previewView, progress, fromUser);
        }
    }

    private long maxSeekLastDuration;

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        //LLog.d(TAG, "PreviewView onStopPreview");
        TmpParamData.getInstance().addViewSeekCount();
        long seekLastDuration = System.currentTimeMillis() - timestampOnStartPreview;
        TmpParamData.getInstance().setViewSeekDuration(seekLastDuration);
        if (maxSeekLastDuration < seekLastDuration) {
            maxSeekLastDuration = seekLastDuration;
            TmpParamData.getInstance().setViewMaxSeekTime(maxSeekLastDuration);
        }
        isOnPreview = false;
        onStopPreview(progress);
        if (callbackUZTimebar != null) {
            callbackUZTimebar.onStopPreview(previewView, progress);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_SEEKED);
    }

    public void onStopPreview(int progress) {
        if (uzPlayerManager != null) {
            uzPlayerManager.seekTo(progress);
            uzPlayerManager.resumeVideo();
            isOnPlayerEnded = false;
            updateUIEndScreen();
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        //LLog.d(TAG, "onFocusChange isFocus " + isFocus);
        /*if (isFocus) {
            if (view == ibBackScreenIcon) {
                LLog.d(TAG, "onFocusChange ibSettingIcon");
            } else if (view == ibSettingIcon) {
                LLog.d(TAG, "onFocusChange ibSettingIcon");
            } else if (view == ibCcIcon) {
                LLog.d(TAG, "onFocusChange ibCcIcon");
            } else if (view == ibPlaylistRelationIcon) {
                LLog.d(TAG, "onFocusChange ibPlaylistRelationIcon");
            } else if (view == ibRewIcon) {
                LLog.d(TAG, "onFocusChange ibRewIcon");
            } else if (view == ibPlayIcon) {
                LLog.d(TAG, "onFocusChange ibPlayIcon");
            } else if (view == ibPauseIcon) {
                LLog.d(TAG, "onFocusChange ibPauseIcon");
            } else if (view == ibReplayIcon) {
                LLog.d(TAG, "onFocusChange ibReplayIcon");
            } else if (view == ibSkipNextIcon) {
                LLog.d(TAG, "onFocusChange ibSkipNextIcon");
            } else if (view == ibSkipPreviousIcon) {
                LLog.d(TAG, "onFocusChange ibSkipPreviousIcon");
            } else if (view == uzTimebar) {
                LLog.d(TAG, "onFocusChange uzTimebar");
            }
        }*/
        if (uztvCallback != null) {
            uztvCallback.onFocusChange(view, isFocus);
        } else {
            if (firstViewHasFocus == null) {
                firstViewHasFocus = view;
            }
        }
    }

    //current screen is landscape or portrait
    private boolean isLandscape;

    public boolean isLandscape() {
        return isLandscape;
    }

    @Override
    public void onOrientationChange(int orientation) {
        //LLog.d(TAG, "=====================================" + orientation);
        //270 land trai
        //0 portrait duoi
        //90 land phai
        //180 portrait tren
        boolean isDeviceAutoRotation = LDeviceUtil.isRotationPossible(activity);
        if (orientation == 90 || orientation == 270) {
            //LLog.d(TAG, "orientation is landscape, isDeviceAutoRotation is On: " + isDeviceAutoRotation + ", isLandscape: " + isLandscape);
            if (isDeviceAutoRotation) {
                if (!isLandscape) {
                    LActivityUtil.changeScreenLandscape(activity, orientation);
                }
            }
        } else {
            //LLog.d(TAG, "orientation is portrait, isDeviceAutoRotation is On: " + isDeviceAutoRotation + ", isLandscape: " + isLandscape);
            if (isDeviceAutoRotation) {
                if (isLandscape) {
                    LActivityUtil.changeScreenPortrait(activity);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LScreenUtil.hideDefaultControls(activity);
                isLandscape = true;
                UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, true);
                if (ibPictureInPictureIcon != null) {
                    ibPictureInPictureIcon.setVisibility(GONE);
                }
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, false);
                if (!isCastingChromecast()) {
                    if (ibPictureInPictureIcon != null) {
                        ibPictureInPictureIcon.setVisibility(VISIBLE);
                    }
                }
            }
        }
        TmpParamData.getInstance().setPlayerIsFullscreen(isLandscape);
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        updateUISizeThumnail();
        updateUIPositionOfProgressBar();
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), getVideoW(), getVideoH(), isFreeSize);
        if (isSetUZTimebarBottom) {
            View videoSurfaceView = uzPlayerView.getVideoSurfaceView();
            setMarginDependOnUZTimeBar(videoSurfaceView);
        }
        if (uzCallback != null) {
            uzCallback.onScreenRotate(isLandscape);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            LAnimationUtil.play(v, Techniques.Pulse);
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
        } else if (v == ibShareIcon) {
            handleClickShare();
        } else if (v.getParent() == debugRootView) {
            showUZTrackSelectionDialog(((Button) v), true);
        } else if (v == rlChromeCast) {
            //dangerous to remove
            //LLog.d(TAG, "do nothing click rl_chrome_cast");
        } else if (v == ibFfwdIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                if (uzPlayerManager != null) {
                    uzPlayerManager.seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
                }
            }
        } else if (v == ibRewIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                if (uzPlayerManager != null) {
                    uzPlayerManager.seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
                    if (isPlaying()) {
                        isOnPlayerEnded = false;
                        updateUIEndScreen();
                    }
                }
            }
        } else if (v == ibPauseIcon) {
            pauseVideo();
        } else if (v == ibPlayIcon) {
            resumeVideo();
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
        }

        /*có trường hợp đang click vào các control thì bị ẩn control ngay lập tức, trường hợp này ta có thể xử lý khi click vào control thì reset count down để ẩn control ko
        default controller timeout là 8s, vd tới s thứ 7 bạn tương tác thì tới s thứ 8 controller sẽ bị ẩn, cái này mình sẽ reset cout và update bản mới.*/
        if (isDefaultUseController) {
            if (rlMsg != null && rlMsg.getVisibility() == View.VISIBLE) {
            } else {
                if (isPlayerControllerShowing()) {
                    showController();
                }
            }
        }
        if (uzItemClick != null) {
            uzItemClick.onItemClick(v);
        }
    }

    private void handleClickPictureInPicture() {
        if (activity == null) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionShowPip());
            }
            return;
        }
        if (!isInitMiniPlayerSuccess) {
            //dang init 1 instance mini player roi, khong cho init nua
            //LLog.d(TAG, "!isInitMiniPlayerSuccess -> return");
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionShowPip());
            }
            return;
        }
        if (isCastingChromecast()) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionShowPip());
            }
            return;
        }
        if (LDeviceUtil.isCanOverlay(activity)) {
            initializePiP();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }

    private boolean isInitMiniPlayerSuccess = true;

    public void initializePiP() {
        if (activity == null || uzPlayerManager == null || uzPlayerManager.getLinkPlay() == null) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionShowPip());
            }
            return;
        }
        if (ibPictureInPictureIcon != null) {
            ibPictureInPictureIcon.setVisibility(View.GONE);
        }
        if (uzCallback != null) {
            isInitMiniPlayerSuccess = false;
            uzCallback.onStateMiniPlayer(isInitMiniPlayerSuccess);
        }
        UZUtil.setVideoWidth(activity, getVideoW());
        UZUtil.setVideoHeight(activity, getVideoH());
        Intent intent = new Intent(activity, FUZVideoService.class);
        intent.putExtra(Constants.FLOAT_CONTENT_POSITION, getCurrentPosition());
        intent.putExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, isInitCustomLinkPlay);
        intent.putExtra(Constants.FLOAT_UUID, uuid.toString());
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uzPlayerManager.getLinkPlay());
        intent.putExtra(Constants.FLOAT_IS_LIVESTREAM, isLivestream);
        intent.putExtra(Constants.FLOAT_PROGRESS_BAR_COLOR, progressBarColor);
        activity.startService(intent);
    }

    public SimpleExoPlayer getPlayer() {
        if (uzPlayerManager == null) {
            return null;
        }
        return uzPlayerManager.getPlayer();
    }

    public void seekTo(long positionMs) {
        if (uzPlayerManager != null) {
            //LLog.d(TAG, "seekTo positionMs: " + positionMs);
            uzPlayerManager.seekTo(positionMs);
        }
    }

    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        DEFAULT_VALUE_CONTROLLER_TIMEOUT = controllerShowTimeoutMs;
        uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
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
        if (isPlayerControllerAlwayVisible) {
            return;
        }
        if (isCastingChromecast) {
            //dont hide if is casting chromecast
        } else {
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
        if (uzPlayerView != null) {
            return uzPlayerView.getControllerHideOnTouch();
        }
        return false;
    }

    /*
     **Cho phep su dung controller hay khong
     * Default: true
     * Neu truyen false se an tat ca cac component
     */
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
            LLog.d(TAG, "isDataListExist -> dont call getListAllEntity()");
            playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
            hideProgress();
        } else {
            LLog.d(TAG, "!isDataListExist -> call getListAllEntity()");
            UZService service = UZRestClient.createService(UZService.class);
            UZAPIMaster.getInstance().subscribe(service.getListAllEntity(metadataId, pfLimit, pfPage, pfOrderBy, pfOrderType, publishToCdn), new ApiSubscriber<ResultListEntity>() {
                @Override
                public void onSuccess(ResultListEntity result) {
                    //LLog.d(TAG, "callAPIGetListAllEntity onSuccess");
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
                        if (uzCallback != null) {
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
                    LLog.d(TAG, "callAPIGetListAllEntity pfPage/pfTotalPage: " + pfPage + "/" + pfTotalPage + " ->>>>>>> setDataList");
                    UZData.getInstance().setDataList(result.getData());
                    if (UZData.getInstance().getDataList() == null || UZData.getInstance().getDataList().isEmpty()) {
                        LLog.e(TAG, "callAPIGetListAllEntity success but no data");
                        if (uzCallback != null) {
                            uzCallback.onError(UZExceptionUtil.getExceptionListAllEntity());
                        }
                        return;
                    }
                    //LLog.d(TAG, "list size: " + UZData.getInstance().getDataList().size());
                    playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
                    hideProgress();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "callAPIGetListAllEntity onFail " + e.getMessage());
                    if (uzCallback != null) {
                        uzCallback.onError(UZExceptionUtil.getExceptionListAllEntity());
                    }
                    hideProgress();
                }
            });
        }
    }

    protected boolean isPlayPlaylistFolder() {
        if (UZData.getInstance().getDataList() == null) {
            return false;
        }
        return true;
    }

    private void playPlaylistPosition(int position) {
        if (!isPlayPlaylistFolder()) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            return;
        }
        LLog.d(TAG, "playPlaylistPosition position: " + position);
        if (position < 0) {
            LLog.e(TAG, "This is the first item");
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionPlaylistFolderItemFirst());
            }
            return;
        }
        if (position > UZData.getInstance().getDataList().size() - 1) {
            LLog.e(TAG, "This is the last item");
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionPlaylistFolderItemLast());
            }
            return;
        }
        urlImgThumbnail = null;
        pauseVideo();
        hideController();
        //update UI for skip next and skip previous button
        if (position == 0) {
            if (ibSkipPreviousIcon != null) {
                if (!ibSkipPreviousIcon.isFocused()) {
                    ibSkipPreviousIcon.setSrcDrawableDisabled();
                }
            }
            if (ibSkipNextIcon != null) {
                if (!ibSkipNextIcon.isFocused()) {
                    ibSkipNextIcon.setSrcDrawableEnabled();
                }
            }
        } else if (position == UZData.getInstance().getDataList().size() - 1) {
            if (ibSkipPreviousIcon != null) {
                if (!ibSkipPreviousIcon.isFocused()) {
                    ibSkipPreviousIcon.setSrcDrawableEnabled();
                }
            }
            if (ibSkipNextIcon != null) {
                if (!ibSkipNextIcon.isFocused()) {
                    ibSkipNextIcon.setSrcDrawableDisabled();
                }
            }
        } else {
            if (ibSkipPreviousIcon != null) {
                if (!ibSkipPreviousIcon.isFocused()) {
                    ibSkipPreviousIcon.setSrcDrawableEnabled();
                }
            }
            if (ibSkipNextIcon != null) {
                if (!ibSkipNextIcon.isFocused()) {
                    ibSkipNextIcon.setSrcDrawableEnabled();
                }
            }
        }
        //set disabled prevent double click, will enable onStateReadyFirst()
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setClickable(false);
            ibSkipPreviousIcon.setFocusable(false);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setClickable(false);
            ibSkipNextIcon.setFocusable(false);
        }
        //end update UI for skip next and skip previous button
        UZData.getInstance().setCurrentPositionOfDataList(position);
        Data data = UZData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position);
        init(UZData.getInstance().getDataWithPositionOfDataList(position).getId(), false);
    }

    private boolean isOnPlayerEnded;

    protected void onPlayerEnded() {
        //LLog.d(TAG, ">>>onPlayerEnded isPlaying " + isPlaying());
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
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() + 1);
    }

    private void autoSwitchPreviousLinkVideo() {
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() - 1);
    }

    private void handleClickPlaylistFolder() {
        UZDlgPlaylistFolder UZDlgPlaylistFolder = new UZDlgPlaylistFolder(activity, isLandscape, UZData.getInstance().getDataList(), UZData.getInstance().getCurrentPositionOfDataList(), new CallbackPlaylistFolder() {
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
        UZUtil.showUizaDialog(activity, UZDlgPlaylistFolder);
    }

    private boolean isClickedSkipNextOrSkipPrevious;

    private void handleClickSkipNext() {
        //LLog.d(TAG, "handleClickSkipNext");
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUIEndScreen();
        autoSwitchNextVideo();
    }

    private void handleClickSkipPrevious() {
        //LLog.d(TAG, "handleClickSkipPrevious");
        isClickedSkipNextOrSkipPrevious = true;
        isOnPlayerEnded = false;
        updateUIEndScreen();
        autoSwitchPreviousLinkVideo();
    }

    /*
     **replay this current content
     * return true if replay successed
     * return fail if replay failed
     */
    public boolean replay() {
        if (uzPlayerManager == null) {
            return false;
        }
        TmpParamData.getInstance().addPlayerViewCount();
        //TODO Chỗ này đáng lẽ chỉ clear value của tracking khi đảm bảo rằng seekTo(0) true
        setDefautValueForFlagIsTracked();
        boolean result = uzPlayerManager.seekTo(0);
        if (result) {
            isSetFirstRequestFocusDone = false;
            isOnPlayerEnded = false;
            updateUIEndScreen();
            if (isPlayPlaylistFolder()) {
                setVisibilityOfPlaylistFolderController(View.VISIBLE);
            } else {
                setVisibilityOfPlaylistFolderController(View.GONE);
            }
            trackUizaEventVideoStarts();
            trackUizaEventDisplay();
            trackUizaEventPlaysRequested();
        }
        return result;
    }

    //===================================================================END FOR PLAYLIST/FOLDER

    /*Nếu đang casting thì button này sẽ handle volume on/off ở cast player
     * Ngược lại, sẽ handle volume on/off ở exo player*/
    private void handleClickBtVolume() {
        if (isCastingChromecast) {
            LLog.d(TAG, "handleClickBtVolume isCastingChromecast");
            boolean isMute = UZData.getInstance().getCasty().toggleMuteVolume();
            if (ibVolumeIcon != null) {
                if (isMute) {
                    ibVolumeIcon.setImageResource(R.drawable.ic_volume_off_black_48dp);
                } else {
                    ibVolumeIcon.setImageResource(R.drawable.baseline_volume_up_white_48);
                }
            }
        } else {
            LLog.d(TAG, "handleClickBtVolume !isCastingChromecast");
            if (uzPlayerManager != null) {
                uzPlayerManager.toggleVolumeMute(ibVolumeIcon);
            }
        }
    }

    private void handleClickBackScreen() {
        if (!isOnPlayerEnded) {
            //LLog.d(TAG, "handleClickBackScreen");
            hideController();
        }
        if (isLandscape) {
            toggleFullscreen();
        }
    }

    private void handleClickSetting() {
        View view = UZUtil.getBtVideo(debugRootView);
        if (view != null) {
            UZUtil.getBtVideo(debugRootView).performClick();
        }
    }

    private void handleClickCC() {
        if (uzPlayerManager.getSubtitleList() == null || uzPlayerManager.getSubtitleList().isEmpty()) {
            UZDlgInfoV1 uzDlgInfoV1 = new UZDlgInfoV1(activity, activity.getString(R.string.text), activity.getString(R.string.no_caption));
            UZUtil.showUizaDialog(activity, uzDlgInfoV1);
        } else {
            View view = UZUtil.getBtText(debugRootView);
            if (view != null) {
                UZUtil.getBtText(debugRootView).performClick();
            } else {
                LLog.e(TAG, "error handleClickCC null");
            }
        }
    }

    private void handleClickHearing() {
        View view = UZUtil.getBtAudio(debugRootView);
        if (view != null) {
            UZUtil.getBtAudio(debugRootView).performClick();
        }
    }

    private void handleClickShare() {
        LSocialUtil.share(activity, isLandscape, activity.getString(R.string.uiza_introduce));
    }

    public void resumeVideo() {
        TmpParamData.getInstance().setPlayerIsPaused(false);
        addTrackingMuiza(Constants.MUIZA_EVENT_PLAY);
        if (isCastingChromecast) {
            UZData.getInstance().getCasty().getPlayer().play();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.resumeVideo();
            }
        }
        if (ibPlayIcon != null) {
            ibPlayIcon.setVisibility(GONE);
        }
        if (ibPauseIcon != null) {
            ibPauseIcon.setVisibility(VISIBLE);
            ibPauseIcon.requestFocus();
        }
    }

    public void pauseVideo() {
        TmpParamData.getInstance().setPlayerIsPaused(true);
        if (isCastingChromecast) {
            UZData.getInstance().getCasty().getPlayer().pause();
        } else {
            if (uzPlayerManager != null) {
                uzPlayerManager.pauseVideo();
            }
        }
        if (ibPauseIcon != null) {
            ibPauseIcon.setVisibility(GONE);
        }
        if (ibPlayIcon != null) {
            ibPlayIcon.setVisibility(VISIBLE);
            ibPlayIcon.requestFocus();
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_PAUSE);
    }

    /*
     ** Doi thoi gian seek mac dinh
     */
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
        if (ibFfwdIcon != null) {
            ibFfwdIcon.performClick();
        }
    }

    /*
     ** Seek tu vi tri hien tai tru di bao nhieu mls
     */
    public void seekToBackward(int mls) {
        setDefaultValueBackwardForward(mls);
        if (ibRewIcon != null) {
            ibRewIcon.performClick();
        }
    }

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
            pauseVideo();
        } else {
            resumeVideo();
        }
    }

    public void toggleVolume() {
        if (ibVolumeIcon != null) {
            ibVolumeIcon.performClick();
        }
    }

    public void toggleFullscreen() {
        addTrackingMuiza(Constants.MUIZA_EVENT_FULLSCREENCHANGE);
        LActivityUtil.toggleScreenOritation(activity);
    }

    public void showCCPopup() {
        if (ibCcIcon != null) {
            ibCcIcon.performClick();
        }
    }

    public void showHQPopup() {
        if (ibSettingIcon != null) {
            ibSettingIcon.performClick();
        }
    }

    public void showSharePopup() {
        if (ibShareIcon != null) {
            ibShareIcon.performClick();
        }
    }

    /*
     ** Hiển thị picture in picture và close video view hiện tại
     * Chỉ work nếu local player đang không casting
     * Device phải là tablet
     */
    public void showPip() {
        if (isCastingChromecast()) {
            LLog.e(TAG, UZException.ERR_19);
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionShowPip());
            }
        } else {
            if (ibPictureInPictureIcon != null) {
                ibPictureInPictureIcon.performClick();
            }
        }
    }

    public void showSpeed() {
        if (getPlayer() == null) {
            return;
        }
        final UZDlgSpeed uzDlgSpeed = new UZDlgSpeed(activity, getPlayer().getPlaybackParameters().speed, new UZDlgSpeed.Callback() {
            @Override
            public void onSelectItem(UZDlgSpeed.Speed speed) {
                //LLog.d(TAG, "select " + speed.getName() + " - " + speed.getValue());
                if (speed != null) {
                    setSpeed(speed.getValue());
                }
            }
        });
        UZUtil.showUizaDialog(activity, uzDlgSpeed);
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

    public TextView getTvMsg() {
        return tvMsg;
    }

    public ImageView getIvVideoCover() {
        return ivVideoCover;
    }

    public UZImageButton getIbFullscreenIcon() {
        return ibFullscreenIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public UZImageButton getIbPauseIcon() {
        return ibPauseIcon;
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

    public UZImageButton getIbShareIcon() {
        return ibShareIcon;
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

    public List<UZItem> getHQList() {
        View view = UZUtil.getBtVideo(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error getHQList null");
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionListHQ());
            }
            return null;
        }
        return showUZTrackSelectionDialog(view, false);
    }

    public List<UZItem> getAudioList() {
        View view = UZUtil.getBtAudio(debugRootView);
        if (view == null) {
            LLog.e(TAG, "Error audio null");
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionListAudio());
            }
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

    @Override
    public void onVisibilityChange(boolean isShow) {
        //LLog.d(TAG, "onVisibilityChange " + isShow);
        if (controllerStateCallback != null) {
            controllerStateCallback.onVisibilityChange(isShow);
        }
    }

    public void setSpeed(float speed) {
        if (isLivestream) {
            throw new IllegalArgumentException("Error UizaSpeed: You cannot set speed with live content.");
        }
        if (speed > 3 || speed < -3) {
            throw new IllegalArgumentException("Error UizaSpeed: Please set speed with the value between -3 and 3.");
        }
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        if (getPlayer() != null) {
            getPlayer().setPlaybackParameters(playbackParameters);
        }
        addTrackingMuiza(Constants.MUIZA_EVENT_RATECHANGE);
    }

    //=============================================================================================START UI
    private void findViews() {
        //LLog.d(TAG, "findViews");
        bkg = (View) findViewById(R.id.bkg);
        rlMsg = (RelativeLayout) findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        if (tvMsg != null) {
            LUIUtil.setTextShadow(tvMsg);
        }
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, progressBarColor);
        updateUIPositionOfProgressBar();
        uzPlayerView.setControllerStateCallback(this);
        uzTimebar = uzPlayerView.findViewById(R.id.exo_progress);
        previewFrameLayout = uzPlayerView.findViewById(R.id.preview_frame_layout);
        if (uzTimebar != null) {
            if (uzTimebar.getTag() == null) {
                isSetUZTimebarBottom = false;
            } else {
                if (uzTimebar.getTag().toString().equals(activity.getString(R.string.use_bottom_uz_timebar))) {
                    isSetUZTimebarBottom = true;
                } else {
                    isSetUZTimebarBottom = false;
                }
            }
            uzTimebar.addOnPreviewChangeListener(this);
            uzTimebar.setOnFocusChangeListener(this);
        }
        ivThumbnail = (ImageView) uzPlayerView.findViewById(R.id.image_view_thumnail);
        tvPosition = (UZTextView) uzPlayerView.findViewById(R.id.uz_position);
        if (tvPosition != null) {
            tvPosition.setText(LDateUtils.convertMlsecondsToHMmSs(0));
        }
        tvDuration = (UZTextView) uzPlayerView.findViewById(R.id.uz_duration);
        if (tvDuration != null) {
            tvDuration.setText("-:-");
        }
        ibFullscreenIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) uzPlayerView.findViewById(R.id.tv_title);
        ibPauseIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_pause_uiza);
        ibPlayIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_play_uiza);
        //If auto start true, show button play and gone button pause
        if (ibPlayIcon != null) {
            ibPlayIcon.setVisibility(GONE);
        }
        ibReplayIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_replay_uiza);
        ibRewIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_rew);
        if (ibRewIcon != null) {
            ibRewIcon.setSrcDrawableDisabled();
        }
        ibFfwdIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_ffwd);
        ibBackScreenIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_back_screen);
        ibVolumeIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_volume);
        ibSettingIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_setting);
        ibCcIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_cc);
        ibPlaylistFolderIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_playlist_folder);
        ibHearingIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_hearing);
        ibPictureInPictureIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_picture_in_picture);
        ibShareIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_share);
        ibSkipNextIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_skip_next);
        ibSkipPreviousIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_skip_previous);
        ibSpeedIcon = (UZImageButton) uzPlayerView.findViewById(R.id.exo_speed);
        debugLayout = findViewById(R.id.debug_layout);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);
        if (Constants.IS_DEBUG) {
            debugLayout.setVisibility(View.VISIBLE);
        } else {
            debugLayout.setVisibility(View.GONE);
            debugTextView = null;
        }
        rlLiveInfo = (RelativeLayout) uzPlayerView.findViewById(R.id.rl_live_info);
        tvLiveStatus = (TextView) uzPlayerView.findViewById(R.id.tv_live);
        tvLiveView = (TextView) uzPlayerView.findViewById(R.id.tv_live_view);
        tvLiveTime = (TextView) uzPlayerView.findViewById(R.id.tv_live_time);
        ivLiveView = (UZImageButton) uzPlayerView.findViewById(R.id.iv_live_view);
        ivLiveTime = (UZImageButton) uzPlayerView.findViewById(R.id.iv_live_time);
        if (ivLiveView != null) {
            ivLiveView.setFocusable(false);
        }
        if (ivLiveTime != null) {
            ivLiveTime.setFocusable(false);
        }
        rlEndScreen = (RelativeLayout) uzPlayerView.findViewById(R.id.rl_end_screen);
        if (rlEndScreen != null) {
            rlEndScreen.setVisibility(GONE);
        }
        tvEndScreenMsg = (TextView) uzPlayerView.findViewById(R.id.tv_end_screen_msg);
        if (tvEndScreenMsg != null) {
            LUIUtil.setTextShadow(tvEndScreenMsg, Color.WHITE);
            tvEndScreenMsg.setOnClickListener(this);
        }
        setEventForView();
        //set visibility first, so scared if removed
        setVisibilityOfPlaylistFolderController(GONE);
    }

    private void setEventForView() {
        if (ibFullscreenIcon != null) {
            ibFullscreenIcon.setOnClickListener(this);
            ibFullscreenIcon.setOnFocusChangeListener(this);
        }
        if (ibBackScreenIcon != null) {
            ibBackScreenIcon.setOnClickListener(this);
            ibBackScreenIcon.setOnFocusChangeListener(this);
        }
        if (ibVolumeIcon != null) {
            ibVolumeIcon.setOnClickListener(this);
            ibVolumeIcon.setOnFocusChangeListener(this);
        }
        if (ibSettingIcon != null) {
            ibSettingIcon.setOnClickListener(this);
            ibSettingIcon.setOnFocusChangeListener(this);
        }
        if (ibCcIcon != null) {
            ibCcIcon.setOnClickListener(this);
            ibCcIcon.setOnFocusChangeListener(this);
        }
        if (ibPlaylistFolderIcon != null) {
            ibPlaylistFolderIcon.setOnClickListener(this);
            ibPlaylistFolderIcon.setOnFocusChangeListener(this);
        }
        if (ibHearingIcon != null) {
            ibHearingIcon.setOnClickListener(this);
            ibHearingIcon.setOnFocusChangeListener(this);
        }
        if (ibPictureInPictureIcon != null) {
            ibPictureInPictureIcon.setOnClickListener(this);
            ibPictureInPictureIcon.setOnFocusChangeListener(this);
        }
        if (ibShareIcon != null) {
            ibShareIcon.setOnClickListener(this);
            ibShareIcon.setOnFocusChangeListener(this);
        }
        if (ibFfwdIcon != null) {
            ibFfwdIcon.setOnClickListener(this);
            ibFfwdIcon.setOnFocusChangeListener(this);
        }
        if (ibRewIcon != null) {
            ibRewIcon.setOnClickListener(this);
            ibRewIcon.setOnFocusChangeListener(this);
        }
        if (ibPlayIcon != null) {
            ibPlayIcon.setOnClickListener(this);
            ibPlayIcon.setOnFocusChangeListener(this);
        }
        if (ibPauseIcon != null) {
            ibPauseIcon.setOnClickListener(this);
            ibPauseIcon.setOnFocusChangeListener(this);
        }
        if (ibReplayIcon != null) {
            ibReplayIcon.setOnClickListener(this);
            ibReplayIcon.setOnFocusChangeListener(this);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setOnClickListener(this);
            ibSkipNextIcon.setOnFocusChangeListener(this);
        }
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setOnClickListener(this);
            ibSkipPreviousIcon.setOnFocusChangeListener(this);
        }
        if (ibSpeedIcon != null) {
            ibSpeedIcon.setOnClickListener(this);
            ibSpeedIcon.setOnFocusChangeListener(this);
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

    //If auto start true, show button play and gone button pause
    //if not, gone button play and show button pause
    private void updateUIButtonPlayPauseDependOnIsAutoStart() {
        if (isAutoStart) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(VISIBLE);
                if (!isSetFirstRequestFocusDone) {
                    ibPauseIcon.requestFocus();//set first request focus if using player for TV
                    isSetFirstRequestFocusDone = true;
                }
            }
        } else {
            if (isPlaying()) {
                if (ibPlayIcon != null) {
                    ibPlayIcon.setVisibility(GONE);
                }
                if (ibPauseIcon != null) {
                    ibPauseIcon.setVisibility(VISIBLE);
                    if (!isSetFirstRequestFocusDone) {
                        ibPauseIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
            } else {
                if (ibPlayIcon != null) {
                    ibPlayIcon.setVisibility(VISIBLE);
                    if (!isSetFirstRequestFocusDone) {
                        ibPlayIcon.requestFocus();//set first request focus if using player for TV
                        isSetFirstRequestFocusDone = true;
                    }
                }
                if (ibPauseIcon != null) {
                    ibPauseIcon.setVisibility(GONE);
                }
            }
        }
    }

    private String urlImgThumbnail;

    public void setUrlImgThumbnail(String urlImgThumbnail) {
        this.urlImgThumbnail = urlImgThumbnail;
        if (ivVideoCover == null) {
            return;
        }
        if (ivVideoCover.getVisibility() != View.VISIBLE) {
            ivVideoCover.setVisibility(View.VISIBLE);
            LImageUtil.load(activity, urlImgThumbnail, ivVideoCover, R.drawable.background_black);
        }
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != View.VISIBLE) {
            resetCountTryLinkPlayError();
            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            //LLog.d(TAG, "setVideoCover invalidate");
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
            LImageUtil.load(activity, urlCover, ivVideoCover, R.drawable.background_black);
        }
    }

    protected void removeVideoCover(boolean isFromHandleError) {
        //LLog.d(TAG, "removeVideoCover isFromHandleError " + isFromHandleError + ", isInitCustomLinkPlay: " + isInitCustomLinkPlay);
        if (ivVideoCover.getVisibility() != View.GONE) {
            ivVideoCover.setVisibility(GONE);
            ivVideoCover.invalidate();
            //LLog.d(TAG, "removeVideoCover invalidate");
            if (isLivestream) {
                if (tvLiveTime != null) {
                    tvLiveTime.setText("-");
                }
                if (tvLiveView != null) {
                    tvLiveView.setText("-");
                }
                callAPIUpdateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
                callAPIUpdateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
            }
            if (!isFromHandleError) {
                onStateReadyFirst();
            }
        } else {
            //goi changskin realtime thi no ko vao if nen ko update tvDuration dc
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
        //LLog.d(TAG, "updateUIPositionOfProgressBar set progressBar center in parent");
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
    //Gen layout chromecast with black backgroudn programmatically
    private void addUIChromecastLayer() {
        //listener check state of chromecast
        CastContext castContext = null;
        try {
            castContext = CastContext.getSharedInstance(activity);
        } catch (Exception e) {
            Log.e(TAG, "Error addUIChromecastLayer: " + e.toString());
        }
        if (castContext == null) {
            uzMediaRouteButton.setVisibility(View.GONE);
            return;
        }
        if (castContext.getCastState() == CastState.NO_DEVICES_AVAILABLE) {
            uzMediaRouteButton.setVisibility(View.GONE);
        } else {
            uzMediaRouteButton.setVisibility(View.VISIBLE);
        }
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                if (state == CastState.NO_DEVICES_AVAILABLE) {
                    uzMediaRouteButton.setVisibility(View.GONE);
                } else {
                    if (uzMediaRouteButton.getVisibility() != View.VISIBLE) {
                        uzMediaRouteButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        rlChromeCast = new RelativeLayout(activity);
        RelativeLayout.LayoutParams rlChromeCastParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        rlChromeCast.setBackgroundColor(ContextCompat.getColor(activity, R.color.Black));
        ibsCast = new UZImageButton(activity);
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

    private void addPlayerView() {
        uzPlayerView = null;
        int resLayout = UZData.getInstance().getCurrentPlayerId();
        uzPlayerView = (UZPlayerView) activity.getLayoutInflater().inflate(resLayout, null);
        setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        //lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        rootView.addView(uzPlayerView);
        setControllerAutoShow(isAutoShow);
    }

    /*
     **Change skin via skin id resouces
     * changeSkin(R.layout.uz_player_skin_1);
     */
    //TODO improve this func
    private boolean isRefreshFromChangeSkin;
    private long currentPositionBeforeChangeSkin;
    private boolean isCalledFromChangeSkin;

    /*
     **change skin of player (realtime)
     * return true if success
     */
    public boolean changeSkin(int skinId) {
        if (activity == null || uzPlayerManager == null) {
            return false;
        }
        if (UZData.getInstance().isUseWithVDHView()) {
            throw new IllegalArgumentException(activity.getString(R.string.error_change_skin_with_vdhview));
        }
        if (uzPlayerManager.isPlayingAd()) {
            if (uzCallback != null) {
                uzCallback.onError(UZExceptionUtil.getExceptionChangeSkin());
            }
            return false;
        }
        UZData.getInstance().setCurrentPlayerId(skinId);
        isRefreshFromChangeSkin = true;
        isCalledFromChangeSkin = true;
        rootView.removeView(uzPlayerView);
        rootView.requestLayout();
        uzPlayerView = (UZPlayerView) activity.getLayoutInflater().inflate(skinId, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        uzPlayerView.setLayoutParams(lp);
        rootView.addView(uzPlayerView);
        rootView.requestLayout();
        findViews();
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), getVideoW(), getVideoH(), isFreeSize);
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        //setup chromecast
        if (!isTV) {
            uzMediaRouteButton = new UZMediaRouteButton(activity);
            if (llTop != null) {
                llTop.addView(uzMediaRouteButton);
            }
            setUpMediaRouteButton();
            addUIChromecastLayer();
        }
        LLog.d(TAG, "changeSkin getCurrentPosition " + uzPlayerManager.getCurrentPosition());
        currentPositionBeforeChangeSkin = uzPlayerManager.getCurrentPosition();

        uzPlayerManager.release();
        updateUIDependOnLivetream();
        setTitle();
        checkToSetUpResouce();
        updateUISizeThumnail();
        if (uzCallback != null) {
            uzCallback.onSkinChange();
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
            LLog.e(TAG, "Error setProgressSeekbar null");
            return;
        }
        uzVerticalSeekBar.setProgress(progressSeekbar);
    }

    private void setTextPosition(long currentMls) {
        if (tvPosition != null) {
            if (isLivestream) {
                long duration = getDuration();
                //LLog.d(TAG, "current " + currentMls + "/" + duration);
                long past = duration - currentMls;
                //LLog.d(TAG, "setTextPosition -" + LDateUtils.convertMlsecondsToHMmSs(past));
                tvPosition.setText("-" + LDateUtils.convertMlsecondsToHMmSs(past));
            } else {
                tvPosition.setText(LDateUtils.convertMlsecondsToHMmSs(currentMls));
            }
        }
    }

    private void updateUIIbRewIconDependOnProgress(long currentMls, boolean isCalledFromUZTimebarEvent) {
        //LLog.d(TAG, "updateUIIbRewIconDependOnProgress currentMls " + currentMls + ", isCalledFromUZTimebarEvent: " + isCalledFromUZTimebarEvent);
        if (isCalledFromUZTimebarEvent) {
            setTextPosition(currentMls);
        } else {
            if (isOnPreview) {
                //uzTimebar is displaying
                return;
            } else {
                setTextPosition(currentMls);
            }
        }
        if (isLivestream) {
            //do nothing
        } else {
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

    private void hanldeFirstViewHasFocus() {
        if (firstViewHasFocus != null && uztvCallback != null) {
            uztvCallback.onFocusChange(firstViewHasFocus, true);
            firstViewHasFocus = null;
        }
    }

    private void updateUISizeThumnail() {
        int screenWidth = LScreenUtil.getScreenWidth();
        int widthIv = isLandscape ? screenWidth / 4 : screenWidth / 5;
        if (previewFrameLayout != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = widthIv;
            layoutParams.height = (int) (widthIv * Constants.RATIO_9_16);
            previewFrameLayout.setLayoutParams(layoutParams);
            previewFrameLayout.requestLayout();//quyen
        }
    }

    //quyen
    private void setMarginPreviewTimeBar() {
        if (isLandscape) {
            LUIUtil.setMarginDimen(uzTimebar, 5, 0, 5, 0);
        } else {
            LUIUtil.setMarginDimen(uzTimebar, 0, 0, 0, 0);
        }
    }

    //quyen
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

    private void updateUIDependOnLivetream() {
        if (isCastingChromecast) {
            if (ibPictureInPictureIcon != null) {
                ibPictureInPictureIcon.setVisibility(GONE);
            }
        } else {
            if (isTablet && isTV) {
                //only hide ibPictureInPictureIcon if device is TV
                if (ibPictureInPictureIcon != null) {
                    ibPictureInPictureIcon.setVisibility(GONE);
                }
            }
        }
        //LLog.d(TAG, "updateUIDependOnLivetream isLivestream " + isLivestream);
        if (isLivestream) {
            if (rlLiveInfo != null) {
                rlLiveInfo.setVisibility(VISIBLE);
            }
            /*if (rlTimeBar != null) {
                rlTimeBar.setVisibility(INVISIBLE);//set GONE ok, but then VISIBLE not work huhu :(
            }*/
            //TODO why set gone not work?
            if (ibSpeedIcon != null) {
                //ibSpeedIcon.setVisibility(GONE);
                ibSpeedIcon.setUIVisible(false);
            }
            if (ibRewIcon != null) {
                //ibRewIcon.setVisibility(GONE);
                ibRewIcon.setUIVisible(false);
            }
            if (ibFfwdIcon != null) {
                //ibFfwdIcon.setVisibility(GONE);
                ibFfwdIcon.setUIVisible(false);
            }
        } else {
            if (rlLiveInfo != null) {
                rlLiveInfo.setVisibility(GONE);
            }
            /*if (rlTimeBar != null) {
                rlTimeBar.setVisibility(VISIBLE);
            }*/
            //TODO why set visible not work?
            if (ibSpeedIcon != null) {
                //ibSpeedIcon.setVisibility(VISIBLE);
                ibSpeedIcon.setUIVisible(true);
            }
            if (ibRewIcon != null) {
                //ibRewIcon.setVisibility(VISIBLE);
                ibRewIcon.setUIVisible(true);
            }
            if (ibFfwdIcon != null) {
                //ibFfwdIcon.setVisibility(VISIBLE);
                ibFfwdIcon.setUIVisible(true);
            }
        }
        if (isTV) {
            if (ibShareIcon != null) {
                ibShareIcon.setVisibility(GONE);
            }
            if (ibFullscreenIcon != null) {
                ibFullscreenIcon.setVisibility(GONE);
            }
        }
    }

    protected void updateUIButtonVisibilities() {
        if (debugRootView != null) {
            debugRootView.removeAllViews();
        }
        if (uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if (activity == null) {
                    return;
                }
                Button button = new Button(activity);
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

    private void showTvMsg(String msg) {
        if (tvMsg != null) {
            tvMsg.setText(msg);
            showLayoutMsg();
        }
    }

    protected void showLayoutMsg() {
        //LLog.d(TAG, "showLayoutMsg");
        hideController();
        if (rlMsg.getVisibility() != VISIBLE) {
            rlMsg.setVisibility(VISIBLE);
        }
    }

    protected void hideLayoutMsg() {
        if (rlMsg.getVisibility() != GONE) {
            rlMsg.setVisibility(GONE);
        }
    }

    private void updateLiveInfoTimeStartLive() {
        if (!isLivestream || activity == null) {
            //LLog.d(TAG, "updateLiveInfoTimeStartLive return");
            return;
        }
        //LLog.d(TAG, "updateLiveInfoTimeStartLive -> startTime: " + startTime);
        long now = System.currentTimeMillis();
        long duration = now - startTime;
        String s = LDateUtils.convertMlsecondsToHMmSs(duration);
        if (tvLiveTime != null) {
            tvLiveTime.setText(s);
        }
        if (uzLiveContentCallback != null) {
            uzLiveContentCallback.onUpdateLiveInfoTimeStartLive(duration, s);
        }
        callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
    }

    private void updateUIEndScreen() {
        //LLog.d(TAG, "updateUIEndScreen isOnPlayerEnded " + isOnPlayerEnded);
        if (isOnPlayerEnded) {
            if (rlEndScreen != null && tvEndScreenMsg != null) {
                rlEndScreen.setVisibility(VISIBLE);
                //TODO call api skin config to correct this text
                setTextEndscreen("Thanks for your watching");
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
                uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
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
        //LLog.d(TAG, "setVisibilityOfPlayPauseReplay isShowReplay " + isShowReplay);
        if (isShowReplay) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(GONE);
            }
            if (ibReplayIcon != null) {
                ibReplayIcon.setVisibility(VISIBLE);
                ibReplayIcon.requestFocus();
            }
            if (ibFfwdIcon != null) {
                //ibFfwdIcon.setSrcDrawableDisabled();
            }
        } else {
            updateUIButtonPlayPauseDependOnIsAutoStart();
            if (ibReplayIcon != null) {
                ibReplayIcon.setVisibility(GONE);
            }
        }
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
        //LLog.d(TAG, "setVisibilityOfPlaylistFolderController " + visibilityOfPlaylistFolderController);
        if (ibPlaylistFolderIcon != null) {
            ibPlaylistFolderIcon.setVisibility(visibilityOfPlaylistFolderController);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setVisibility(visibilityOfPlaylistFolderController);
        }
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setVisibility(visibilityOfPlaylistFolderController);
        }
        //Có play kiểu gì đi nữa thì cũng phải ibPlayIcon GONE và ibPauseIcon VISIBLE và ibReplayIcon GONE
        setVisibilityOfPlayPauseReplay(false);
    }

    public void hideUzTimebar() {
        if (previewFrameLayout != null) {
            previewFrameLayout.setVisibility(GONE);
        }
        if (ivThumbnail != null) {
            ivThumbnail.setVisibility(GONE);
        }
        if (uzTimebar != null) {
            uzTimebar.setVisibility(GONE);
        }
    }

    private List<UZItem> showUZTrackSelectionDialog(final View view, boolean showDialog) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uzPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = ((Button) view).getText();
            int rendererIndex = (int) view.getTag();
            int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
            /*boolean allowAdaptiveSelections =
                    rendererType == C.TRACK_TYPE_VIDEO
                            || (rendererType == C.TRACK_TYPE_AUDIO
                            && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);*/
            boolean allowAdaptiveSelections = false;
            //Pair<AlertDialog, TrackSelectionView> dialogPair = TrackSelectionView.getDialog(activity, title, uzPlayerManager.getTrackSelector(), rendererIndex);
            final Pair<AlertDialog, UZTrackSelectionView> dialogPair = UZTrackSelectionView.getDialog(activity, title, uzPlayerManager.getTrackSelector(), rendererIndex);
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
                //dialogPair.first.show();
                UZUtil.showUizaDialog(activity, dialogPair.first);
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
        /*if (uzPlayerView.getResizeMode() != AspectRatioFrameLayout.RESIZE_MODE_FIT) {
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }*/
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
        RelativeLayout uzVideoRootView = (RelativeLayout) findViewById(R.id.root_view_uz_video);
        if (uzVideoRootView != null) {
            uzVideoRootView.setBackgroundColor(color);
        }
    }

    public void setMarginDependOnUZTimeBar(View view) {
        if (view == null || uzTimebar == null) {
            return;
        }
        int heightUZTimebar = 0;
        if (isLandscape) {
            //LLog.d(TAG, "setMarginBottom !isLandscape heightUZTimebar " + heightUZTimebar + "->" + heightUZTimebar / 2);
            LUIUtil.setMarginPx(view, 0, 0, 0, 0);
        } else {
            heightUZTimebar = getHeightUZTimeBar();
            //LLog.d(TAG, "setMarginBottom isLandscape heightUZTimebar " + heightUZTimebar + "->" + heightUZTimebar / 2);
            LUIUtil.setMarginPx(view, 0, 0, 0, heightUZTimebar == 0 ? 0 : heightUZTimebar / 2);
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

    //=============================================================================================END UI
    //=============================================================================================START EVENT
    private UZLiveContentCallback uzLiveContentCallback;
    private ProgressCallback progressCallback;
    private CallbackUZTimebar callbackUZTimebar;
    private UZItemClick uzItemClick;
    private UZCallback uzCallback;
    private UZTVCallback uztvCallback;
    private UZPlayerView.ControllerStateCallback controllerStateCallback;
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

    public void addUZTVCallback(UZTVCallback uztvCallback) {
        this.uztvCallback = uztvCallback;
        hanldeFirstViewHasFocus();
    }

    public void addProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void addCallbackUZTimebar(CallbackUZTimebar callbackUZTimebar) {
        this.callbackUZTimebar = callbackUZTimebar;
    }

    public void addItemClick(UZItemClick uzItemClick) {
        this.uzItemClick = uzItemClick;
    }

    public void addControllerStateCallback(final UZPlayerView.ControllerStateCallback controllerStateCallback) {
        this.controllerStateCallback = controllerStateCallback;
    }

    /*
     ** Bat cac event cua player nhu click, long click
     */
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
            LLog.d(TAG, "isDataExist -> dont callAPIGetDetailEntity");
            isCalledApiGetDetailEntity = true;
            data = UZData.getInstance().getData();
            handleDataCallAPI();
        } else {
            UZUtil.getDetailEntity(activity, entityId, new CallbackGetDetailEntity() {
                @Override
                public void onSuccess(Data d) {
                    //LLog.d(TAG, "!isDataExist -> callAPIGetDetailEntity onSuccess");
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
                    LLog.e(TAG, "init onError " + e.toString());
                    UZData.getInstance().setSettingPlayer(false);
                    handleError(UZExceptionUtil.getExceptionCannotGetDetailEntitity());
                }
            });
        }
    }

    private void callAPIGetUrlIMAAdTag() {
        boolean isUrlIMAAdExist = UZData.getInstance().getUrlIMAAd() != null;
        if (isUrlIMAAdExist) {
            //LLog.d(TAG, "isUrlIMAAdExist -> dont callAPIGetUrlIMAAdTag()");
            isCalledAPIGetUrlIMAAdTag = true;
            urlIMAAd = UZData.getInstance().getUrlIMAAd();
            handleDataCallAPI();
        } else {
            //LLog.d(TAG, "!isUrlIMAAdExist -> callAPIGetUrlIMAAdTag()");
            UZService service = UZRestClient.createService(UZService.class);
            UZAPIMaster.getInstance().subscribe(service.getCuePoint(entityId), new ApiSubscriber<AdWrapper>() {
                @Override
                public void onSuccess(AdWrapper result) {
                    isCalledAPIGetUrlIMAAdTag = true;
                    if (result == null || result.getData() == null || result.getData().isEmpty()) {
                        //LLog.d(TAG, "callAPIGetUrlIMAAdTag onSuccess -> this content has no ad");
                        urlIMAAd = "";
                    } else {
                        //Hien tai chi co the play ima ad o item thu 0
                        Ad ad = result.getData().get(0);
                        if (ad != null) {
                            urlIMAAd = ad.getLink();
                        }
                        //LLog.d(TAG, "callAPIGetUrlIMAAdTag onSuccess -> this content has ad -> play ad urlIMAAd " + urlIMAAd);
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
            //LLog.d(TAG, "isGetTokenStreamingExist -> dont call getTokenStreaming");
            mResultGetTokenStreaming = UZData.getInstance().getResultGetTokenStreaming();
            isCalledAPIGetTokenStreaming = true;
            handleDataCallAPI();
        } else {
            //LLog.d(TAG, "!isGetTokenStreamingExist -> call getTokenStreaming");
            UZService service = UZRestClient.createService(UZService.class);
            SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
            sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
            sendGetTokenStreaming.setEntityId(entityId);
            sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
            UZAPIMaster.getInstance().subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
                @Override
                public void onSuccess(ResultGetTokenStreaming resultGetTokenStreaming) {
                    if (resultGetTokenStreaming == null || resultGetTokenStreaming.getData() == null || resultGetTokenStreaming.getData().getToken() == null || resultGetTokenStreaming.getData().getToken().isEmpty()) {
                        handleError(UZExceptionUtil.getExceptionNoTokenStreaming());
                        return;
                    }
                    mResultGetTokenStreaming = resultGetTokenStreaming;
                    //LLog.d(TAG, "callAPIGetTokenStreaming onSuccess");
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
        //LLog.d(TAG, "callAPIGetLinkPlay " + gson.toJson(UZData.getInstance().getUzInput()));
        boolean isResultGetLinkPlayExist = UZData.getInstance().getResultGetLinkPlay() != null;
        if (isResultGetLinkPlayExist) {
            //LLog.d(TAG, "isResultGetLinkPlayExist -> dont call API GetLinkPlay");
            mResultGetLinkPlay = UZData.getInstance().getResultGetLinkPlay();
            try {
                cdnHost = mResultGetLinkPlay.getData().getCdn().get(0).getHost();
            } catch (NullPointerException e) {
                LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
            }
            checkToSetUpResouce();
        } else {
            //LLog.d(TAG, "!isResultGetLinkPlayExist -> call API getLinkPlay VOD or LIVE -> isLivestream: " + isLivestream);
            String tokenStreaming = mResultGetTokenStreaming.getData().getToken();
            UZRestClientGetLinkPlay.addAuthorization(tokenStreaming);
            UZService service = UZRestClientGetLinkPlay.createService(UZService.class);
            if (isLivestream) {
                String appId = UZData.getInstance().getAppId();
                String channelName = UZData.getInstance().getChannelName();
                UZAPIMaster.getInstance().subscribe(service.getLinkPlayLive(appId, channelName), new ApiSubscriber<ResultGetLinkPlay>() {
                    @Override
                    public void onSuccess(ResultGetLinkPlay resultGetLinkPlay) {
                        mResultGetLinkPlay = resultGetLinkPlay;
                        UZData.getInstance().setResultGetLinkPlay(resultGetLinkPlay);
                        //LLog.d(TAG, "getLinkPlayLive onSuccess -> save mResultGetLinkPlay to UZData");
                        //LLog.d(TAG, "-> " + gson.toJson(UZData.getInstance().getUzInput()));
                        try {
                            cdnHost = mResultGetLinkPlay.getData().getCdn().get(0).getHost();
                            //LLog.d(TAG, "getLinkPlayLive cdnHost " + cdnHost);

                            //setEntityCnd, setEntitySourceDomain, setEntitySourceHostname has the same value
                            TmpParamData.getInstance().setEntityCnd(cdnHost);
                            TmpParamData.getInstance().setEntitySourceDomain(cdnHost);
                            TmpParamData.getInstance().setEntitySourceHostname(cdnHost);
                        } catch (NullPointerException e) {
                            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
                        }
                        checkToSetUpResouce();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getLinkPlayLive LIVE onFail " + e.getMessage());
                        handleError(UZExceptionUtil.getExceptionCannotGetLinkPlayLive());
                    }
                });
            } else {
                String appId = UZData.getInstance().getAppId();
                String typeContent = SendGetTokenStreaming.STREAM;
                UZAPIMaster.getInstance().subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
                    @Override
                    public void onSuccess(ResultGetLinkPlay resultGetLinkPlay) {
                        mResultGetLinkPlay = resultGetLinkPlay;
                        UZData.getInstance().setResultGetLinkPlay(resultGetLinkPlay);
                        //LLog.d(TAG, "getLinkPlayVOD onSuccess -> save mResultGetLinkPlay to UZData");
                        //LLog.d(TAG, "-> " + gson.toJson(UZData.getInstance().getUzInput()));
                        try {
                            cdnHost = mResultGetLinkPlay.getData().getCdn().get(0).getHost();
                            //LLog.d(TAG, "getLinkPlay cdnHost " + cdnHost);

                            //setEntityCnd, setEntitySourceDomain, setEntitySourceHostname has the same value
                            TmpParamData.getInstance().setEntityCnd(cdnHost);
                            TmpParamData.getInstance().setEntitySourceDomain(cdnHost);
                            TmpParamData.getInstance().setEntitySourceHostname(cdnHost);
                        } catch (NullPointerException e) {
                            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
                        }
                        checkToSetUpResouce();
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

    private void callAPIUpdateLiveInfoCurrentView(final int durationDelay) {
        if (!isLivestream || activity == null || activityIsPausing) {
            //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView return");
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView " + System.currentTimeMillis());
                if (!isLivestream) {
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (uzPlayerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView isShowing -> call API to get View count");
                    UZService service = UZRestClient.createService(UZService.class);
                    String id = UZData.getInstance().getEntityId();
                    UZAPIMaster.getInstance().subscribe(service.getViewALiveFeed(id), new ApiSubscriber<ResultGetViewALiveFeed>() {
                        @Override
                        public void onSuccess(ResultGetViewALiveFeed result) {
                            //LLog.d(TAG, "getViewALiveFeed onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null) {
                                if (tvLiveView != null) {
                                    tvLiveView.setText(result.getData().getWatchnow() + "");
                                }
                                if (uzLiveContentCallback != null) {
                                    uzLiveContentCallback.onUpdateLiveInfoCurrentView(result.getData().getWatchnow());
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
                    //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView !isShowing");
                    callAPIUpdateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    private void callAPIUpdateLiveInfoTimeStartLive(final int durationDelay) {
        if (!isLivestream || activity == null || activityIsPausing) {
            //LLog.d(TAG, "callAPIUpdateLiveInfoTimeStartLive return");
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                if (!isLivestream || activity == null || activityIsPausing) {
                    //LLog.d(TAG, "callAPIUpdateLiveInfoTimeStartLive return");
                    return;
                }
                if (uzPlayerManager != null && uzPlayerView != null && (uzPlayerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    if (startTime == Constants.UNKNOW) {
                        UZService service = UZRestClient.createService(UZService.class);
                        String entityId = UZData.getInstance().getEntityId();
                        String feedId = UZData.getInstance().getLastFeedId();
                        UZAPIMaster.getInstance().subscribe(service.getTimeStartLive(entityId, feedId), new ApiSubscriber<ResultTimeStartLive>() {
                            @Override
                            public void onSuccess(ResultTimeStartLive result) {
                                //LLog.d(TAG, "getTimeStartLive onSuccess: " + gson.toJson(result));
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
        //LLog.d(TAG, "______________________________handleDataCallAPI isCalledApiGetDetailEntity: " + isCalledApiGetDetailEntity + ", isCalledAPIGetUrlIMAAdTag: " + isCalledAPIGetUrlIMAAdTag + ", isCalledAPIGetTokenStreaming: " + isCalledAPIGetTokenStreaming);
        if (isCalledApiGetDetailEntity && isCalledAPIGetUrlIMAAdTag && isCalledAPIGetTokenStreaming) {
            if (UZData.getInstance().getUzInput() == null) {
                UZInput uzInput = new UZInput();
                uzInput.setData(data);
                uzInput.setUrlIMAAd(urlIMAAd);
                uzInput.setResultGetTokenStreaming(mResultGetTokenStreaming);
                //TODO iplm url thumnail seekbar
                uzInput.setUrlThumnailsPreviewSeekbar(null);
                UZData.getInstance().setUizaInput(uzInput);
            } else {
                //do nothing
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
        isGetClickedPip = UZUtil.getClickedPip(activity);
        LLog.d(TAG, "checkData isLivestream " + isLivestream + ", isGetClickedPip: " + isGetClickedPip);
        if (isGetClickedPip) {
            //LLog.d(TAG, "__________trackUiza getClickedPip true -> dont setDefautValueForFlagIsTracked");
        } else {
            setDefautValueForFlagIsTracked();
            //LLog.d(TAG, "__________trackUiza getClickedPip false -> setDefautValueForFlagIsTracked");
        }
        if (uzPlayerManager != null) {
            uzPlayerManager.release();
            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
            showProgress();
        }
        setTitle();
        callAPIGetLinkPlay();
        trackUizaEventDisplay();
        trackUizaEventPlaysRequested();
    }

    private void checkToSetUpResouce() {
        if (UZData.getInstance().getResultGetLinkPlay() != null && UZData.getInstance().getData() != null) {
            //LLog.d(TAG, ">>>>>>>>>>>>>>>>>>>>>checkToSetUpResouce onSuccess");
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
            //listLinkPlay.clear();
            //listLinkPlay.add("https://toanvk-live.uizacdn.net/893db5e8bb3943bfb12894fec56c8875-live/hi-uaqsv9as/manifest.mpd");
            //listLinkPlay.add("http://112.78.4.162/drm/test/hevc/playlist.mpd");
            //listLinkPlay.add("http://112.78.4.162/6yEB8Lgd/package/playlist.mpd");
            //listLinkPlay.add("https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");
            //LLog.d(TAG, "listLinkPlay toJson: " + gson.toJson(listLinkPlay));
            if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                LLog.e(TAG, "checkToSetUpResouce listLinkPlay == null || listLinkPlay.isEmpty() -> return");
                handleErrorNoData();
                return;
            }
            if (countTryLinkPlayError >= listLinkPlay.size()) {
                if (LConnectivityUtil.isConnected(activity)) {
                    handleError(UZExceptionUtil.getExceptionTryAllLinkPlay());
                } else {
                    showTvMsg(UZException.ERR_0);
                }
                return;
            }
            String linkPlay = listLinkPlay.get(countTryLinkPlayError);
            List<Subtitle> subtitleList = null;
            //TODO iplm v3 chua co subtitle
            /*subtitleList = new ArrayList<>();
            Subtitle subtitle = new Subtitle();
            subtitle.setLanguage("en");
            subtitle.setUrl("https://www.iandevlin.com/html5test/webvtt/upc-video-subtitles-en.vtt");
            subtitleList.add(subtitle);*/

            //List<Subtitle> subtitleList = mResultRetrieveAnEntity.getData().get(0).getSubtitle();
            //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));
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
            initUizaPlayerManager();
        } else {
            LLog.d(TAG, "checkToSetUpResouce onFailed");
            handleError(UZExceptionUtil.getExceptionSetup());
        }
    }

    private long timestampInitDataSource;

    private void initDataSource(String linkPlay, String urlIMAAd, String urlThumbnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        timestampInitDataSource = System.currentTimeMillis();
        //hardcode to test

        //subtitleList
        /*subtitleList = new ArrayList<>();
        Subtitle subtitle = new Subtitle();
        subtitle.setLanguage("en");
        subtitle.setUrl("https://www.iandevlin.com/html5test/webvtt/upc-video-subtitles-en.vtt");
        subtitleList.add(subtitle);*/

        //ima ad
        //urlIMAAd = activity.getString(R.string.ad_tag_url);
        //urlIMAAd = activity.getString(R.string.ad_tag_url_uiza);

        //thumbnail seekbar
        //urlThumbnailsPreviewSeekbar = activity.getString(R.string.url_thumbnails);

        LLog.d(TAG, "-------------------->initDataSource linkPlay " + linkPlay);
        TmpParamData.getInstance().setEntitySourceUrl(linkPlay);
        TmpParamData.getInstance().setTimeFromInitEntityIdToAllApiCalledSuccess(System.currentTimeMillis() - timestampBeforeInitNewSession);
        uzPlayerManager = new UZPlayerManager(this, linkPlay, urlIMAAd, urlThumbnailsPreviewSeekbar, subtitleList);
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
                //LLog.d(TAG, "progressCallback onAdEnded");
                setDefaultUseController(isDefaultUseController());
                if (progressCallback != null) {
                    progressCallback.onAdEnded();
                }
            }

            @Override
            public void onAdProgress(int s, int duration, int percent) {
                //LLog.d(TAG, "progressCallback ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ", percent: " + percent + "%");
                if (progressCallback != null) {
                    progressCallback.onAdProgress(s, duration, percent);
                }
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                //LLog.d(TAG, "progressCallback onVideoProgress video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ", percent: " + percent + "%");
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
                //LLog.d(TAG, "progressCallback onPlayerStateChanged playWhenReady " + playWhenReady + ", playbackState: " + playbackState);
                if (progressCallback != null) {
                    progressCallback.onPlayerStateChanged(playWhenReady, playbackState);
                }
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                //LLog.d(TAG, "progressCallback onBufferProgress bufferedPosition: " + bufferedPosition + ", bufferedPercentage: " + bufferedPercentage);
                if (progressCallback != null) {
                    progressCallback.onBufferProgress(bufferedPosition, bufferedPercentage, duration);
                }
            }
        });
        uzPlayerManager.setDebugCallback(new UZPlayerManager.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                //LLog.d(TAG, "onUpdateButtonVisibilities");
                updateUIButtonVisibilities();
            }
        });
    }

    protected void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst " + uzPlayerManager.isLIVE() + ", videoW x H: " + uzPlayerManager.getVideoW() + "x" + uzPlayerManager.getVideoH());
        long pageLoadTime = System.currentTimeMillis() - timestampBeforeInitNewSession;
        TmpParamData.getInstance().setPageLoadTime(pageLoadTime);
        addTrackingMuiza(Constants.MUIZA_EVENT_VIEWSTART);
        TmpParamData.getInstance().setViewStart(System.currentTimeMillis());
        TmpParamData.getInstance().setViewTimeToFirstFrame(System.currentTimeMillis());
        updateTvDuration();
        updateUIButtonPlayPauseDependOnIsAutoStart();
        updateUIDependOnLivetream();
        //LLog.d(TAG, "onStateReadyFirst isSetUZTimebarBottom: " + isSetUZTimebarBottom);
        UZUtil.resizeLayout(rootView, ivVideoCover, getPixelAdded(), getVideoW(), getVideoH(), isFreeSize);
        if (isSetUZTimebarBottom && uzPlayerView != null) {
            View videoSurfaceView = uzPlayerView.getVideoSurfaceView();
            setMarginDependOnUZTimeBar(videoSurfaceView);
        }
        //enable from playPlaylistPosition() prvent double click
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setClickable(true);
            ibSkipPreviousIcon.setFocusable(true);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setClickable(true);
            ibSkipNextIcon.setFocusable(true);
        }
        if (isGetClickedPip) {
            LLog.d(TAG, "getClickedPip true -> setPlayWhenReady true");
            uzPlayerManager.getPlayer().setPlayWhenReady(true);
        }
        if (uzCallback != null) {
            LLog.d(TAG, "onStateReadyFirst ===> isInitResult");
            uzCallback.isInitResult(true, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        if (isCastingChromecast) {
            //LLog.d(TAG, "onStateReadyFirst init new play check isCastingChromecast: " + isCastingChromecast);
            lastCurrentPosition = 0;
            handleConnectedChromecast();
            showController();
        }
        TmpParamData.getInstance().setSessionStart(System.currentTimeMillis());
        long playerStartUpTime = System.currentTimeMillis() - timestampInitDataSource;
        TmpParamData.getInstance().setPlayerStartupTime(playerStartUpTime);
        trackUizaEventVideoStarts();
        trackUizaCCUForLivestream();
        pingHeartBeat();
        UZData.getInstance().setSettingPlayer(false);
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
        }
    }

    //=============================================================================================START TRACKING
    private int oldPercent = Constants.NOT_FOUND;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    private void setDefautValueForFlagIsTracked() {
        UZTrackingUtil.clearAllValues(activity);
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    protected void trackProgress(int s, int percent) {
        //dont track if user use custom linkplay
        if (isInitCustomLinkPlay) {
            //LLog.d(TAG, "trackProgress return isInitCustomLinkPlay");
            return;
        }
        //track event view (after video is played 5s)
        //LLog.d(TAG, "onVideoProgress -> track view s: " + s + ", percent " + percent);
        if (s == (isLivestream ? 3 : 5)) {
            //LLog.d(TAG, "onVideoProgress -> track view s: " + s + ", percent " + percent);
            if (UZTrackingUtil.isTrackedEventTypeView(activity)) {
                //da track roi ko can track nua
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_VIEW), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypeView(activity, true);
                    }
                });
            }
        }
        //dont track play_throught if entity is live
        if (isLivestream) {
            //LLog.d(TAG, "trackProgress percent: " + percent + "% -> dont track play_throught if entity is live");
            return;
        }
        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent >= Constants.PLAYTHROUGH_100) {
            if (isTracked100) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again isTracked100 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought100(activity)) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again");
                isTracked100 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, "100", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again isTracked75 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought75(activity)) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again");
                isTracked75 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, "75", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again isTracked50 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought50(activity)) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again");
                isTracked50 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, "50", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again isTracked25 true");
                return;
            }
            if (UZTrackingUtil.isTrackedEventTypePlayThrought25(activity)) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again");
                isTracked25 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, "25", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(activity, true);
                    }
                });
            }
        }
    }

    private void callAPITrackUiza(final UizaTracking uizaTracking, final UZTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        //LLog.d(TAG, "------------------------>callAPITrackUiza noPiP getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(uizaTracking));
        if (activity == null || isInitCustomLinkPlay) {
            //LLog.e(TAG, "Error callAPITrackUiza return because activity == null || isInitCustomLinkPlay");
            return;
        }
        UZService service = UZRestClientTracking.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                //LLog.d(TAG, "<------------------------track success: " + uizaTracking.getEventType() + " : " + uizaTracking.getPlayThrough() + " : " + uizaTracking.getEntityName());
                if (uizaTrackingCallback != null) {
                    uizaTrackingCallback.onTrackingSuccess();
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
                //LLog.e(TAG, "callAPITrackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
            }
        });
    }

    private void trackUizaEventVideoStarts() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (UZTrackingUtil.isTrackedEventTypeVideoStarts(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_VIDEO_STARTS), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(activity, true);
                }
            });
        }
    }

    private void trackUizaEventDisplay() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (UZTrackingUtil.isTrackedEventTypeDisplay(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_DISPLAY), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(activity, true);
                }
            });
        }
    }

    private void trackUizaEventPlaysRequested() {
        if (isInitCustomLinkPlay) {
            LLog.d(TAG, "trackUizaEventVideoStarts return isInitCustomLinkPlay");
            return;
        }
        if (UZTrackingUtil.isTrackedEventTypePlaysRequested(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_PLAYS_REQUESTED), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(activity, true);
                }
            });
        }
    }

    private final int INTERVAL_HEART_BEAT = 10000;

    private void pingHeartBeat() {
        if (activity == null || cdnHost == null || cdnHost.isEmpty()) {
            LLog.e(TAG, "Error cannot call API pingHeartBeat() -> cdnHost: " + cdnHost + ", entityName: " + UZData.getInstance().getEntityName() + " -> return");
            return;
        }
        if (activityIsPausing) {
            LUIUtil.setDelay(INTERVAL_HEART_BEAT, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    pingHeartBeat();
                }
            });
            LLog.e(TAG, "Error cannot call API pingHeartBeat() because activity is pausing " + UZData.getInstance().getEntityName());
            return;
        }
        UZService service = UZRestClientHeartBeat.createService(UZService.class);
        String cdnName = cdnHost;
        String session = uuid.toString();
        //LLog.d(TAG, "pingHeartBeat cdnName " + cdnName);
        //LLog.d(TAG, "pingHeartBeat session " + session);
        UZAPIMaster.getInstance().subscribe(service.pingHeartBeat(cdnName, session), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                //LLog.d(TAG, "pingHeartBeat onSuccess " + UZData.getInstance().getEntityName());
                LUIUtil.setDelay(INTERVAL_HEART_BEAT, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        pingHeartBeat();
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "pingHeartBeat onFail: " + e.toString());
                LUIUtil.setDelay(INTERVAL_HEART_BEAT, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        pingHeartBeat();
                    }
                });
            }
        });
    }

    private final int INTERVAL_TRACK_CCU = 3000;

    private void trackUizaCCUForLivestream() {
        if (activity == null || !isLivestream || isInitCustomLinkPlay) {
            LLog.e(TAG, "Error cannot trackUizaCCUForLivestream() -> return " + UZData.getInstance().getEntityName());
            return;
        }
        if (activityIsPausing) {
            LUIUtil.setDelay(INTERVAL_TRACK_CCU, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    trackUizaCCUForLivestream();
                }
            });
            LLog.e(TAG, "Error cannot trackUizaCCUForLivestream() because activity is pausing " + UZData.getInstance().getEntityName());
            return;
        }
        UZService service = UZRestClientTracking.createService(UZService.class);
        UizaTrackingCCU uizaTrackingCCU = new UizaTrackingCCU();
        uizaTrackingCCU.setDt(LDateUtils.getCurrent(LDateUtils.FORMAT_1));
        uizaTrackingCCU.setHo(cdnHost);
        uizaTrackingCCU.setAi(UZData.getInstance().getAppId());
        uizaTrackingCCU.setSn(UZData.getInstance().getEntityName());
        uizaTrackingCCU.setDi(Loitp.getDeviceId(activity));
        uizaTrackingCCU.setUa(Constants.USER_AGENT);
        //LLog.d(TAG, "trackUizaCCUForLivestream " + gson.toJson(uizaTrackingCCU));
        UZAPIMaster.getInstance().subscribe(service.trackCCU(uizaTrackingCCU), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                //LLog.d(TAG, "trackCCU success: " + UZData.getInstance().getEntityName());
                LUIUtil.setDelay(INTERVAL_TRACK_CCU, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        trackUizaCCUForLivestream();
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LUIUtil.setDelay(INTERVAL_TRACK_CCU, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        trackUizaCCUForLivestream();
                    }
                });
            }
        });
    }

    protected void addTrackingMuizaError(String event, UZException e) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UZData.getInstance().addTrackingMuiza(activity, event, e);
    }

    protected void addTrackingMuiza(String event) {
        if (isInitCustomLinkPlay) {
            return;
        }
        UZData.getInstance().addTrackingMuiza(activity, event);
    }

    private boolean isTrackingMuiza;

    private void callAPITrackMuiza(int s) {
        if (isInitCustomLinkPlay) {
            return;
        }
        if (s <= 0 || s % 10 != 0) {
            return;
        }
        //LLog.d(TAG, "trackMuiza s: " + s + "s");
        if (isTrackingMuiza) {
            //LLog.d(TAG, "isTrackingMuiza -> return");
            return;
        }
        if (UZData.getInstance().isMuizaListEmpty()) {
            //LLog.d(TAG, "no tracking muiza -> return");
            return;
        }
        isTrackingMuiza = true;
        final List<Muiza> muizaListToTracking = new ArrayList<>();
        muizaListToTracking.addAll(UZData.getInstance().getMuizaList());
        //LLog.d(TAG, "call api trackMuiza -> muizaListToTracking.size(): " + muizaListToTracking.size());
        UZData.getInstance().clearMuizaList();
        UZService service = UZRestClientTracking.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.trackMuiza(muizaListToTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                //LLog.d(TAG, "trackMuiza onSuccess " + gson.toJson(result));
                isTrackingMuiza = false;
            }

            @Override
            public void onFail(Throwable e) {
                //LLog.e(TAG, "trackMuiza failed: " + e.toString());
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
        if (event != null) {
            //LLog.d(TAG, "onMessageEventConnectEvent isConnected: " + event.isConnected());
            if (event.isConnected()) {
                if (uzPlayerManager != null) {
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
                    resumeVideo();
                }
            } else {
                showTvMsg(UZException.ERR_0);
                //if current screen is portrait -> do nothing
                //else current screen is landscape -> change screen to portrait
                //LActivityUtil.changeScreenPortrait(activity);
            }
        }
    }

    private long positionMiniPlayer;

    public boolean isInitNewItem(String urlImgThumbnail) {
        //LLog.d(TAG, "onNewIntent positionMiniPlayer: " + positionMiniPlayer);
        if (positionMiniPlayer != 0) {
            seekTo(positionMiniPlayer);
            resumeVideo();
            sendEventInitSuccess();
            positionMiniPlayer = 0;
            return false;
        } else {
            setUrlImgThumbnail(urlImgThumbnail);
            pauseVideo();
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
            if (activity == null) {
                return;
            }
            LLog.d(TAG, "miniplayer STEP 6");
            //LLog.d(TAG, "onMessageEvent getPositionMiniPlayer: " + ((ComunicateMng.MsgFromServiceOpenApp) msg).getPositionMiniPlayer());
            try {
                positionMiniPlayer = ((ComunicateMng.MsgFromServiceOpenApp) msg).getPositionMiniPlayer();
                Class classNamePfPlayer = Class.forName(activity.getLocalClassName());
                Intent intent = new Intent(activity, classNamePfPlayer);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, UZData.getInstance().getEntityId());
                //intent.putExtra(Constants.FLOAT_CONTENT_POSITION, ((ComunicateMng.MsgFromServiceOpenApp) msg).getPositionMiniPlayer());
                activity.startActivity(intent);
            } catch (ClassNotFoundException e) {
                LLog.e(TAG, "onMessageEvent open app ClassNotFoundException " + e.toString());
            }
            return;
        }
        //when pip float view init success
        if (uzCallback != null && msg instanceof ComunicateMng.MsgFromServiceIsInitSuccess) {
            //Ham nay duoc goi khi player o FUZVideoService da init xong
            //Nhiem vu la minh se gui vi tri hien tai sang cho FUZVideoService no biet
            LLog.d(TAG, "miniplayer STEP 3 UZVideo biet FUZVideoService da init xong -> gui lai content position cua UZVideo cho FUZVideoService");
            ComunicateMng.MsgFromActivityPosition msgFromActivityPosition = new ComunicateMng.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(uzPlayerManager.getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);
            isInitMiniPlayerSuccess = true;
            uzCallback.onStateMiniPlayer(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        } else if (msg instanceof ComunicateMng.MsgFromServicePosition) {
            //FUZVideo truoc khi huy da gui position cua pip toi day
            //Nhan duoc vi tru tu FUZVideoService roi seek toi vi tri nay
            //LLog.d(TAG, "seek to: " + ((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            /*if (uzPlayerManager != null) {
                uzPlayerManager.seekTo(((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            }*/
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
                    lastCurrentPosition = uzPlayerManager.getCurrentPosition();
                }
                handleConnectedChromecast();
            }

            @Override
            public void onDisconnected() {
                handleDisconnectedChromecast();
            }
        });
        /*casty.setOnCastSessionUpdatedListener(new Casty.OnCastSessionUpdatedListener() {
            @Override
            public void onCastSessionUpdated(CastSession castSession) {
                if (castSession != null) {
                    LLog.d(TAG, "onCastSessionUpdated " + castSession.getSessionRemainingTimeMs());
                    RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
                }
            }
        });*/
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

    public boolean isCastingChromecast() {
        return isCastingChromecast;
    }

    //last current position lúc từ exoplayer switch sang cast player
    private long lastCurrentPosition;

    private void playChromecast() {
        if (UZData.getInstance().getData() == null || uzPlayerManager == null || uzPlayerManager.getPlayer() == null) {
            return;
        }
        showProgress();
        //LLog.d(TAG, "playChromecast exo stop lastCurrentPosition: " + lastCurrentPosition);

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UZData.getInstance().getData().getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, UZData.getInstance().getData().getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(UZData.getInstance().getData().getThumbnail())));

        //TODO add subtitle vtt to chromecast
        List<MediaTrack> mediaTrackList = new ArrayList<>();
        /*MediaTrack mediaTrack = UZData.getInstance().buildTrack(
                1,
                "text",
                "captions",
                "http://112.78.4.162/sub.vtt",
                "English Subtitle",
                "en-US");
        mediaTrackList.add(mediaTrack);
        mediaTrack = UZData.getInstance().buildTrack(
                2,
                "text",
                "captions",
                "http://112.78.4.162/sub.vtt",
                "XYZ Subtitle",
                "en-US");
        mediaTrackList.add(mediaTrack);*/

        long duration = getDuration();
        //LLog.d(TAG, "duration " + duration);
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
                //LLog.d(TAG, "onProgressUpdated " + currentPosition + " - " + duration + " >>> max " + previewTimeBar.getMax());
                if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                    //LLog.d(TAG, "onProgressUpdated PLAYING FIRST");
                    hideProgress();
                    isCastPlayerPlayingFirst = true;
                }

                if (currentPosition > 0) {
                    uzPlayerManager.seekTo(currentPosition);
                }
            }
        }, 1000);

    }

    private boolean isCastPlayerPlayingFirst;

    /*khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnet thì local player sẽ resume*/
    private void updateUIChromecast() {
        if (uzPlayerManager == null || rlChromeCast == null || isTV) {
            return;
        }
        //LLog.d(TAG, "updateUIChromecast " + isCastingChromecast);
        if (isCastingChromecast) {
            uzPlayerManager.pauseVideo();
            uzPlayerManager.setVolume(0f);
            rlChromeCast.setVisibility(VISIBLE);
            if (ibSettingIcon != null) {
                ibSettingIcon.setVisibility(GONE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(GONE);
            }
            if (ibBackScreenIcon != null) {
                ibBackScreenIcon.setVisibility(GONE);
            }
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(VISIBLE);
            }
            if (ibVolumeIcon != null) {
                ibVolumeIcon.setVisibility(GONE);
            }

            //casting player luôn play first với volume not mute
            //ibVolumeIcon.setImageResource(R.drawable.ic_volume_up_black_48dp);
            //UZData.getInstance().getCasty().setVolume(0.99);

            //double volumeOfExoPlayer = uzPlayerManager.getVolume();
            //LLog.d(TAG, "volumeOfExoPlayer " + volumeOfExoPlayer);
            //UZData.getInstance().getCasty().setVolume(volumeOfExoPlayer);

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(0);
            }
        } else {
            uzPlayerManager.resumeVideo();
            uzPlayerManager.setVolume(0.99f);
            rlChromeCast.setVisibility(GONE);
            if (ibSettingIcon != null) {
                ibSettingIcon.setVisibility(VISIBLE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(VISIBLE);
            }
            if (ibBackScreenIcon != null) {
                ibBackScreenIcon.setVisibility(VISIBLE);
            }
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(VISIBLE);
            }
            //TODO iplm volume mute on/off o cast player
            if (ibVolumeIcon != null) {
                ibVolumeIcon.setVisibility(VISIBLE);
            }
            //khi quay lại exoplayer từ cast player thì mặc định sẽ bật lại âm thanh (dù cast player đang mute hay !mute)
            //ibVolumeIcon.setImageResource(R.drawable.ic_volume_up_black_48dp);
            //uzPlayerManager.setVolume(0.99f);

            /*double volumeOfCastPlayer = UZData.getInstance().getCasty().getVolume();
            LLog.d(TAG, "volumeOfCastPlayer " + volumeOfCastPlayer);
            uzPlayerManager.setVolume((float) volumeOfCastPlayer);*/

            if (uzPlayerView != null) {
                uzPlayerView.setControllerShowTimeoutMs(DEFAULT_VALUE_CONTROLLER_TIMEOUT);
            }
        }
    }

    //=============================================================================================END CHROMECAST
    private void scheduleJob() {
        if (activity == null) {
            return;
        }
        JobInfo myJob = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myJob = new JobInfo.Builder(0, new ComponentName(activity, LConectifyService.class))
                    .setRequiresCharging(true)
                    .setMinimumLatency(1000)
                    .setOverrideDeadline(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(myJob);
            }
        }
    }
}
package vn.loitp.uizavideov3.view.rl.video;

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
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.rubensousa.previewseekbar.base.PreviewView;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBarLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.chromecast.Casty;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LSocialUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.data.EventBusData;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.restclient.RestClientV3GetLinkPlay;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.Url;
import vn.loitp.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.loitp.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.loitp.restapi.uiza.model.v3.livestreaming.gettimestartlive.ResultTimeStartLive;
import vn.loitp.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.dlg.info.UizaDialogInfo;
import vn.loitp.uizavideo.view.rl.video.UizaPlayerView;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.uizavideov3.util.UizaInputV3;
import vn.loitp.uizavideov3.util.UizaTrackingUtil;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.uizavideov3.view.dlg.listentityrelation.PlayListCallbackV3;
import vn.loitp.uizavideov3.view.dlg.listentityrelation.UizaDialogListEntityRelationV3;
import vn.loitp.uizavideov3.view.dlg.playlistfolder.CallbackPlaylistFolder;
import vn.loitp.uizavideov3.view.dlg.playlistfolder.UizaDialogPlaylistFolder;
import vn.loitp.uizavideov3.view.floatview.FloatingUizaVideoServiceV3;
import vn.loitp.views.LToast;
import vn.loitp.views.autosize.imagebuttonwithsize.ImageButtonWithSize;
import vn.loitp.views.seekbar.verticalseekbar.VerticalSeekBar;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideoV3 extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private int DEFAULT_VALUE_BACKWARD_FORWARD = 10000;//10000 mls
    private BaseActivity activity;
    private boolean isLivestream;
    private boolean isTablet;
    private Gson gson = new Gson();
    private RelativeLayout rootView;
    private UizaPlayerManagerV3 uizaPlayerManagerV3;
    private ProgressBar progressBar;

    private LinearLayout llTop;
    //play controller
    private RelativeLayout llMid;
    private View llMidSub;

    private PreviewTimeBarLayout previewTimeBarLayout;
    private PreviewTimeBar previewTimeBar;
    private ImageView ivThumbnail;

    private RelativeLayout rlTimeBar;
    private RelativeLayout rlMsg;
    private TextView tvMsg;

    private ImageView ivVideoCover;
    private ImageButtonWithSize exoFullscreenIcon;
    private TextView tvTitle;
    private ImageButtonWithSize exoPause;
    private ImageButtonWithSize exoPlay;
    private ImageButtonWithSize exoRew;
    private ImageButtonWithSize exoFfwd;
    private ImageButtonWithSize exoBackScreen;
    private ImageButtonWithSize exoVolume;
    private ImageButtonWithSize exoSetting;
    private ImageButtonWithSize exoCc;
    private ImageButtonWithSize exoPlaylistRelation;//danh sach video co lien quan
    private ImageButtonWithSize exoPlaylistFolder;//danh sach playlist folder
    private ImageButtonWithSize exoHearing;
    private ImageButtonWithSize exoPictureInPicture;
    private ImageButtonWithSize exoShare;
    private ImageButtonWithSize exoSkipPrevious;
    private ImageButtonWithSize exoSkipNext;
    private VerticalSeekBar seekbarVolume;
    private VerticalSeekBar seekbarBirghtness;
    private ImageView exoIvPreview;

    private RelativeLayout rlLiveInfo;
    private TextView tvLiveView;
    private TextView tvLiveTime;

    private LinearLayout debugLayout;
    private LinearLayout debugRootView;
    private TextView debugTextView;

    private int firstBrightness = Constants.NOT_FOUND;

    private ResultGetLinkPlay mResultGetLinkPlay;
    //private Data mData;//information of video (VOD or LIVE)
    private boolean isResultGetLinkPlayDone;

    private final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private final int DELAY_TO_GET_LIVE_INFORMATION = 15000;

    //chromecast https://github.com/DroidsOnRoids/Casty
    private MediaRouteButton mediaRouteButton;
    private RelativeLayout rlChromeCast;
    private ImageButtonWithSize ibsCast;

    private boolean isDisplayPortrait;

    public boolean isDisplayPortrait() {
        return isDisplayPortrait;
    }

    public void setDisplayPortrait(boolean isDisplayPortrait) {
        this.isDisplayPortrait = isDisplayPortrait;
        UizaUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
    }

    /**
     * return player view
     */
    public PlayerView getPlayerView() {
        return playerView;
    }

    public TextView getDebugTextView() {
        return debugTextView;
    }

    /**
     * return progress bar view
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public PreviewTimeBarLayout getPreviewTimeBarLayout() {
        return previewTimeBarLayout;
    }

    /**
     * return thumnail imageview
     */
    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    private boolean isHasError;

    private void handleError(Exception e) {
        if (isHasError) {
            LLog.e(TAG, "handleError isHasError=true -> return");
            return;
        }
        LLog.e(TAG, "handleError " + e.toString());
        isHasError = true;
        if (uizaCallback != null) {
            uizaCallback.onError(e);
        }
        UizaDataV3.getInstance().setSettingPlayer(false);
    }

    /**
     * set uizaCallback for uiza video
     */
    public void setUizaCallback(UizaCallback uizaCallback) {
        this.uizaCallback = uizaCallback;
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    protected void init(@NonNull String entityId, final String urlIMAAd, final String urlThumnailsPreviewSeekbar, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed, boolean isClearDataPlaylistFolder) {
        LLog.d(TAG, "=======================================================");
        LLog.d(TAG, "=======================================================");
        LLog.d(TAG, "=======================================================");
        LLog.d(TAG, "======================NEW SESSION======================");
        LLog.d(TAG, "entityId " + entityId);
        if (isClearDataPlaylistFolder) {
            //LLog.d(TAG, "xxxxxxxxxxxxxx clearDataForPlaylistFolder");
            UizaDataV3.getInstance().clearDataForPlaylistFolder();
        } else {
            //LLog.d(TAG, "xxxxxxxxxxxxxx !clearDataForPlaylistFolder");
        }
        if (entityId == null) {
            //do nothing
            //Case này được gọi từ pip
        } else {
            UizaDataV3.getInstance().clearDataForEntity();
        }
        UizaDataV3.getInstance().setSettingPlayer(true);
        isHasError = false;
        hideLLMsg();
        //Nếu đã tồn tại Data rồi thì nó được gọi từ pip, mình ko cần phải call api lấy detail entity làm gì nữa
        if (UizaDataV3.getInstance().getData() == null) {
            LLog.d(TAG, "init UizaDataV3.getInstance().getData() == null -> call api lấy detail entity");
            UizaUtil.getDetailEntity((BaseActivity) activity, entityId, new UizaUtil.Callback() {
                @Override
                public void onSuccess(Data d) {
                    //LLog.d(TAG, "init getDetailEntity onSuccess: " + gson.toJson(mData));
                    LLog.d(TAG, "init getDetailEntity onSuccess");
                    //save current data
                    UizaDataV3.getInstance().setData(d);
                    handleGetDetailEntityDone(urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
                }

                @Override
                public void onError(Throwable e) {
                    LLog.e(TAG, "init onError " + e.toString());
                    LToast.show(activity, "init onError: " + e.getMessage());
                    UizaDataV3.getInstance().setSettingPlayer(false);
                }
            });
        } else {
            //init player khi user click vào fullscreen của floating view (pic)
            LLog.d(TAG, "init UizaDataV3.getInstance().getData() != null");
            handleGetDetailEntityDone(urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
        }
    }

    private void handleGetDetailEntityDone(String urlIMAAd, String urlThumnailsPreviewSeekbar, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        UizaInputV3 uizaInputV3 = new UizaInputV3();
        uizaInputV3.setData(UizaDataV3.getInstance().getData());
        //uizaInputV3.setUrlIMAAd(activity.getString(loitp.core.R.string.ad_tag_url));
        uizaInputV3.setUrlIMAAd(urlIMAAd);
        //uizaInputV3.setUrlThumnailsPreviewSeekbar(activity.getString(loitp.core.R.string.url_thumbnails));
        uizaInputV3.setUrlThumnailsPreviewSeekbar(urlThumnailsPreviewSeekbar);
        UizaDataV3.getInstance().setUizaInput(uizaInputV3, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
        checkData();
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId, final String urlIMAAd, final String urlThumnailsPreviewSeekbar, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        init(entityId, urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed, true);
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId, final String urlIMAAd, final String urlThumnailsPreviewSeekbar) {
        init(entityId, urlIMAAd, urlThumnailsPreviewSeekbar, false, true);
    }

    /**
     * init player with entity id
     */
    public void init(@NonNull String entityId) {
        init(entityId, null, null, false, true);
    }

    /**
     * TODO init player with any link play
     */
    /*public void initLinkPlay(String anyLinkPlay){

    }*/

    /**
     * init player with metadatId (playlist/folder)
     */

    public void initPlaylistFolder(String metadataId) {
        LLog.d(TAG, "initPlaylistFolder metadataId " + metadataId);
        if (metadataId == null) {
            //Được gọi initPlaylistFolder nếu click fullscreen từ pip
            //do pass metadataId null
        } else {
            UizaDataV3.getInstance().clearDataForPlaylistFolder();
        }
        getListAllEntity(metadataId);
    }

    private void checkData() {
        //LLog.d(TAG, "checkData");
        UizaDataV3.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UizaDataV3.getInstance().getEntityId() == null || UizaDataV3.getInstance().getEntityId().isEmpty()) {
            LLog.e(TAG, "checkData getEntityId null or empty -> return");
            LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.entity_cannot_be_null_or_empty), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    handleError(new Exception(activity.getString(R.string.entity_cannot_be_null_or_empty)));
                }

                @Override
                public void onCancel() {
                    handleError(new Exception(activity.getString(R.string.entity_cannot_be_null_or_empty)));
                }
            });
            UizaDataV3.getInstance().setSettingPlayer(false);
            return;
        }

        isLivestream = UizaDataV3.getInstance().isLivestream();
        //LLog.d(TAG, "isLivestream " + isLivestream);

        if (UizaUtil.getClickedPip(activity)) {
            //LLog.d(TAG, "__________trackUiza getClickedPip true -> dont setDefautValueForFlagIsTracked");
        } else {
            setDefautValueForFlagIsTracked();
            //LLog.d(TAG, "__________trackUiza getClickedPip false -> setDefautValueForFlagIsTracked");
        }

        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            uizaPlayerManagerV3.release();
            mResultGetLinkPlay = null;
            isResultGetLinkPlayDone = false;
            resetCountTryLinkPlayError();
        }
        updateUI();
        setTitle();
        if (!UizaUtil.getClickedPip(activity)) {
            setVideoCover();
        }
        getTokenStreaming();
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }

        //track event eventype display
        if (UizaTrackingUtil.isTrackedEventTypeDisplay(activity)) {
            //da track roi ko can track nua
        } else {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_DISPLAY), new UizaTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UizaTrackingUtil.setTrackingDoneWithEventTypeDisplay(activity, true);
                }
            });
        }

        //track event plays_requested
        if (UizaTrackingUtil.isTrackedEventTypePlaysRequested(activity)) {
            //da track roi ko can track nua
        } else {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_PLAYS_REQUESTED), new UizaTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UizaTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(activity, true);
                }
            });
        }
    }

    private int countTryLinkPlayError = 0;

    protected void tryNextLinkPlay() {
        if (isLivestream) {
            //try to play 3 times
            if (countTryLinkPlayError >= 3) {
                showTvMsg(activity.getString(R.string.err_live_is_stopped));
            }

            //if entity is livestreaming, dont try to next link play
            //LLog.d(TAG, "tryNextLinkPlay isLivestream true -> try to replay = count " + countTryLinkPlayError);
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.initWithoutReset();
                uizaPlayerManagerV3.setRunnable();
            }
            countTryLinkPlayError++;
            return;
        }
        countTryLinkPlayError++;
        if (Constants.IS_DEBUG) {
            LToast.show(activity, activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        }
        //LLog.d(TAG, "tryNextLinkPlay countTryLinkPlayError " + countTryLinkPlayError);
        uizaPlayerManagerV3.release();
        checkToSetUp();
    }

    //khi call api getLinkPlay nhung json tra ve ko co data
    //se co gang choi video da play gan nhat
    //neu co thi se play
    //khong co thi bao loi
    private void handleErrorNoData() {
        //LLog.e(TAG, "handleErrorNoData");
        removeVideoCover(true);
        LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.has_no_linkplay), new LDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                LLog.e(TAG, "handleErrorNoData onClick1");
                if (uizaCallback != null) {
                    UizaDataV3.getInstance().setSettingPlayer(false);
                    uizaCallback.isInitResult(false, null, null);

                    //uizaCallback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }

            @Override
            public void onCancel() {
                LLog.e(TAG, "handleErrorNoData onCancel");
                if (uizaCallback != null) {
                    UizaDataV3.getInstance().setSettingPlayer(false);
                    uizaCallback.isInitResult(false, null, null);
                    //uizaCallback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }
        });
    }

    private void checkToSetUp() {
        //LLog.d(TAG, "checkToSetUp isResultGetLinkPlayDone: " + isResultGetLinkPlayDone);
        if (isResultGetLinkPlayDone) {
            if (mResultGetLinkPlay != null && UizaDataV3.getInstance().getData() != null) {
                //LLog.d(TAG, "checkToSetUp if");
                List<String> listLinkPlay = new ArrayList<>();
                List<Url> urlList = mResultGetLinkPlay.getData().getUrls();

                for (Url url : urlList) {
                    if (url.getSupport().toLowerCase().equals("mpd")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }
                for (Url url : urlList) {
                    if (url.getSupport().toLowerCase().equals("m3u8")) {
                        listLinkPlay.add(url.getUrl());
                    }
                }

                //listLinkPlay.add("https://toanvk-live.uizacdn.net/893db5e8bb3943bfb12894fec56c8875-live/hi-uaqsv9as/manifest.mpd");
                //listLinkPlay.add("http://112.78.4.162/drm/test/hevc/playlist.mpd");
                //listLinkPlay.add("http://112.78.4.162/6yEB8Lgd/package/playlist.mpd");
                //listLinkPlay.add("https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");

                //LLog.d(TAG, "listLinkPlay toJson: " + gson.toJson(listLinkPlay));
                if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                    LLog.e(TAG, "checkToSetUp listLinkPlay == null || listLinkPlay.isEmpty()");
                    handleErrorNoData();
                    return;
                }
                if (countTryLinkPlayError >= listLinkPlay.size()) {
                    if (LConnectivityUtil.isConnected(activity)) {
                        LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.try_all_link_play_but_no_luck), new LDialogUtil.Callback1() {
                            @Override
                            public void onClick1() {
                                handleError(new Exception(activity.getString(R.string.try_all_link_play_but_no_luck)));
                            }

                            @Override
                            public void onCancel() {
                                handleError(new Exception(activity.getString(R.string.try_all_link_play_but_no_luck)));
                            }
                        });
                    } else {
                        //LLog.d(TAG, "checkToSetUp else err_no_internet");
                        showTvMsg(activity.getString(R.string.err_no_internet));
                    }
                    return;
                }
                String linkPlay = listLinkPlay.get(countTryLinkPlayError);

                List<Subtitle> subtitleList = null;
                //TODO iplm v3 chua co subtitle
                //List<Subtitle> subtitleList = mResultRetrieveAnEntity.getData().get(0).getSubtitle();
                //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

                initDataSource(linkPlay, UizaDataV3.getInstance().getUrlIMAAd(), UizaDataV3.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
                initUizaPlayerManagerV3();
            } else {
                //LLog.d(TAG, "checkToSetUp else");
                LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.err_setup), new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        handleError(new Exception(activity.getString(R.string.err_setup)));
                    }

                    @Override
                    public void onCancel() {
                        handleError(new Exception(activity.getString(R.string.err_setup)));
                    }
                });
            }
        }
    }

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            LLog.d(TAG, "setVideoCover");
            resetCountTryLinkPlayError();

            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            LImageUtil.load(activity, UizaDataV3.getInstance().getThumbnail() == null ? Constants.URL_IMG_THUMBNAIL : UizaDataV3.getInstance().getThumbnail(), ivVideoCover, R.drawable.uiza);
        }
    }

    protected void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            LLog.d(TAG, "--------removeVideoCover isFromHandleError: " + isFromHandleError);
            ivVideoCover.setVisibility(GONE);
            if (isLivestream) {
                tvLiveTime.setText("-");
                tvLiveView.setText("-");
                updateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
                updateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
            }
            if (!isFromHandleError) {
                onStateReadyFirst();
            }
        }
    }

    public UizaIMAVideoV3(Context context) {
        super(context);
        onCreate();
    }

    public UizaIMAVideoV3(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaIMAVideoV3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UizaIMAVideoV3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        activity = ((BaseActivity) getContext());
        UizaUtil.setClassNameOfPlayer(activity, activity.getLocalClassName());
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);

        rootView = (RelativeLayout) findViewById(R.id.root_view);
        isTablet = LDeviceUtil.isTablet(activity);
        LLog.d(TAG, "onCreate isTablet " + isTablet);
        addPlayerView();
        findViews();
        UizaUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
        updateUIEachSkin();
        setMarginPreviewTimeBarLayout();
        setMarginRlLiveInfo();

        //setup chromecast
        mediaRouteButton = new MediaRouteButton(activity);
        llTop.addView(mediaRouteButton);
        setUpMediaRouteButton();
        addChromecastLayer();
    }

    private void updateSizeOfMediaRouteButton() {
        if (exoPlay != null) {
            LLog.d(TAG, "updateSizeOfMediaRouteButton: " + exoPlay.getSize());
        }
    }

    private UizaPlayerView playerView;

    private void addPlayerView() {
        playerView = null;
        switch (UizaDataV3.getInstance().getCurrentPlayerId()) {
            case Constants.PLAYER_ID_SKIN_1:
                playerView = (UizaPlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_1, null);
                break;
            case Constants.PLAYER_ID_SKIN_2:
                playerView = (UizaPlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_2, null);
                break;
            case Constants.PLAYER_ID_SKIN_3:
                playerView = (UizaPlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_3, null);
                break;
            case Constants.PLAYER_ID_SKIN_0:
                playerView = (UizaPlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_default, null);
                break;
            default:
                playerView = (UizaPlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_default, null);
                break;
        }
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        playerView.setLayoutParams(lp);
        rootView.addView(playerView);
    }

    private void updateUIEachSkin() {
        //LLog.d(TAG, "updateUIEachSkin");
        switch (UizaDataV3.getInstance().getCurrentPlayerId()) {
            case Constants.PLAYER_ID_SKIN_2:
            case Constants.PLAYER_ID_SKIN_3:
                exoPlay.setRatioLand(7);
                exoPlay.setRatioPort(5);
                exoPause.setRatioLand(7);
                exoPause.setRatioPort(5);
                break;
        }
    }

    private void updatePositionOfProgressBar() {
        //LLog.d(TAG, "updatePositionOfProgressBar set progressBar center in parent");
        playerView.post(new Runnable() {
            @Override
            public void run() {
                int marginL = playerView.getMeasuredWidth() / 2 - progressBar.getMeasuredWidth() / 2;
                int marginT = playerView.getMeasuredHeight() / 2 - progressBar.getMeasuredHeight() / 2;
                //LLog.d(TAG, "updatePositionOfProgressBar " + marginL + "x" + marginT);
                LUIUtil.setMarginPx(progressBar, marginL, marginT, 0, 0);
            }
        });
    }

    private void findViews() {
        //LLog.d(TAG, "findViews");
        rlMsg = (RelativeLayout) findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        LUIUtil.setTextShadow(tvMsg);
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        llMid = (RelativeLayout) findViewById(R.id.ll_mid);
        llMidSub = (View) findViewById(R.id.ll_mid_sub);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        playerView = findViewById(R.id.player_view);

        updatePositionOfProgressBar();

        rlTimeBar = playerView.findViewById(R.id.rl_time_bar);
        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        previewTimeBarLayout = playerView.findViewById(R.id.previewSeekBarLayout);
        previewTimeBarLayout.setTintColorResource(R.color.colorPrimary);
        previewTimeBar.addOnPreviewChangeListener(this);
        ivThumbnail = (ImageView) playerView.findViewById(R.id.image_view_thumnail);

        exoFullscreenIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) playerView.findViewById(R.id.tv_title);
        exoPause = (ImageButtonWithSize) playerView.findViewById(R.id.exo_pause_uiza);
        exoPlay = (ImageButtonWithSize) playerView.findViewById(R.id.exo_play_uiza);
        exoPlay.setVisibility(GONE);
        exoRew = (ImageButtonWithSize) playerView.findViewById(R.id.exo_rew);
        exoFfwd = (ImageButtonWithSize) playerView.findViewById(R.id.exo_ffwd);
        exoBackScreen = (ImageButtonWithSize) playerView.findViewById(R.id.exo_back_screen);
        exoVolume = (ImageButtonWithSize) playerView.findViewById(R.id.exo_volume);
        exoSetting = (ImageButtonWithSize) playerView.findViewById(R.id.exo_setting);
        exoCc = (ImageButtonWithSize) playerView.findViewById(R.id.exo_cc);
        exoPlaylistRelation = (ImageButtonWithSize) playerView.findViewById(R.id.exo_playlist_relation);
        exoPlaylistFolder = (ImageButtonWithSize) playerView.findViewById(R.id.exo_playlist_folder);
        exoHearing = (ImageButtonWithSize) playerView.findViewById(R.id.exo_hearing);

        //TODO exoHearing works fine, but QC dont want to show it, fuck QC team
        exoHearing.setVisibility(GONE);

        exoPictureInPicture = (ImageButtonWithSize) playerView.findViewById(R.id.exo_picture_in_picture);
        exoShare = (ImageButtonWithSize) playerView.findViewById(R.id.exo_share);
        exoSkipNext = (ImageButtonWithSize) playerView.findViewById(R.id.exo_skip_next);
        exoSkipPrevious = (ImageButtonWithSize) playerView.findViewById(R.id.exo_skip_previous);

        exoIvPreview = (ImageView) playerView.findViewById(R.id.exo_iv_preview);
        seekbarVolume = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_volume);
        seekbarBirghtness = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_birghtness);
        LUIUtil.setColorSeekBar(seekbarVolume, Color.TRANSPARENT);
        LUIUtil.setColorSeekBar(seekbarBirghtness, Color.TRANSPARENT);

        debugLayout = findViewById(R.id.debug_layout);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);

        if (Constants.IS_DEBUG) {
            //TODO revert to VISIBILE
            debugLayout.setVisibility(View.GONE);
        } else {
            debugLayout.setVisibility(View.GONE);
        }

        rlLiveInfo = (RelativeLayout) playerView.findViewById(R.id.rl_live_info);
        tvLiveView = (TextView) playerView.findViewById(R.id.tv_live_view);
        tvLiveTime = (TextView) playerView.findViewById(R.id.tv_live_time);

        //onclick
        exoFullscreenIcon.setOnClickListener(this);
        exoBackScreen.setOnClickListener(this);
        exoVolume.setOnClickListener(this);
        exoSetting.setOnClickListener(this);
        exoCc.setOnClickListener(this);
        exoPlaylistRelation.setOnClickListener(this);
        exoPlaylistFolder.setOnClickListener(this);
        exoHearing.setOnClickListener(this);
        exoPictureInPicture.setOnClickListener(this);
        exoShare.setOnClickListener(this);
        exoFfwd.setOnClickListener(this);
        exoRew.setOnClickListener(this);
        exoPlay.setOnClickListener(this);
        exoPause.setOnClickListener(this);
        exoSkipNext.setOnClickListener(this);
        exoSkipPrevious.setOnClickListener(this);

        //seekbar change
        seekbarVolume.setOnSeekBarChangeListener(this);
        seekbarBirghtness.setOnSeekBarChangeListener(this);

        //set visinility first
        setVisibilityOfPlaylistFolderController(GONE);
    }

    //tự tạo layout chromecast và background đen
    private void addChromecastLayer() {
        //listener check state of chromecast
        CastContext castContext = CastContext.getSharedInstance(activity);
        if (castContext.getCastState() == CastState.NO_DEVICES_AVAILABLE) {
            //LLog.d(TAG, "addChromecastLayer setVisibility GONE");
            mediaRouteButton.setVisibility(View.GONE);
        } else {
            //LLog.d(TAG, "addChromecastLayer setVisibility VISIBLE");
            mediaRouteButton.setVisibility(View.VISIBLE);
        }
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                if (state == CastState.NO_DEVICES_AVAILABLE) {
                    //LLog.d(TAG, "addChromecastLayer setVisibility GONE");
                    mediaRouteButton.setVisibility(View.GONE);
                } else {
                    if (mediaRouteButton.getVisibility() != View.VISIBLE) {
                        //LLog.d(TAG, "addChromecastLayer setVisibility VISIBLE");
                        mediaRouteButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        rlChromeCast = new RelativeLayout(activity);
        RelativeLayout.LayoutParams rlChromeCastParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlChromeCast.setLayoutParams(rlChromeCastParams);
        rlChromeCast.setVisibility(GONE);
        //rlChromeCast.setBackgroundColor(ContextCompat.getColor(activity, R.color.black_65));
        rlChromeCast.setBackgroundColor(ContextCompat.getColor(activity, R.color.Black));

        ibsCast = new ImageButtonWithSize(activity);
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
        } else if (llMid != null) {
            if (llMid.getParent() instanceof ViewGroup) {
                ((RelativeLayout) llMid.getParent()).addView(rlChromeCast, 0);
            }
        } else if (rlLiveInfo != null) {
            if (rlLiveInfo.getParent() instanceof ViewGroup) {
                ((RelativeLayout) rlLiveInfo.getParent()).addView(rlChromeCast, 0);
            }
        }
    }

    private ProgressCallback progressCallback;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    private void initDataSource(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        LLog.d(TAG, "-------------------->initDataSource linkPlay " + linkPlay);
        uizaPlayerManagerV3 = new UizaPlayerManagerV3(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        if (urlThumnailsPreviewSeekbar == null || urlThumnailsPreviewSeekbar.isEmpty()) {
            previewTimeBarLayout.setEnabled(false);
        } else {
            previewTimeBarLayout.setEnabled(true);
        }
        if (previewTimeBarLayout != null) {
            previewTimeBarLayout.setPreviewLoader(uizaPlayerManagerV3);
        }
        uizaPlayerManagerV3.setProgressCallback(new ProgressCallback() {
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
        uizaPlayerManagerV3.setDebugCallback(new UizaPlayerManagerV3.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                //LLog.d(TAG, "onUpdateButtonVisibilities");
                updateButtonVisibilities();
            }
        });

        //========================>>>>>start init seekbar
        isSetProgressSeekbarFirst = true;
        //set volume max in first play
        seekbarVolume.setMax(100);
        setProgressSeekbar(seekbarVolume, 99);
        exoVolume.setImageResource(R.drawable.baseline_volume_up_white_48);

        //set bightness max in first play
        firstBrightness = LScreenUtil.getCurrentBrightness(getContext());
        //LLog.d(TAG, "firstBrightness " + firstBrightness);
        seekbarBirghtness.setMax(255);
        setProgressSeekbar(seekbarBirghtness, firstBrightness);
        isSetProgressSeekbarFirst = false;
        //========================<<<<<end init seekbar
    }

    private boolean isSetProgressSeekbarFirst;
    private int oldPercent = Constants.NOT_FOUND;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    private void setDefautValueForFlagIsTracked() {
        UizaTrackingUtil.clearAllValues(activity);
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s)
        //LLog.d(TAG, "onVideoProgress -> track view s: " + s + ", percent " + percent);
        if (s == (isLivestream ? 3 : 5)) {
            //LLog.d(TAG, "onVideoProgress -> track view s: " + s + ", percent " + percent);
            if (UizaTrackingUtil.isTrackedEventTypeView(activity)) {
                //da track roi ko can track nua
            } else {
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIEW), new UizaTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UizaTrackingUtil.setTrackingDoneWithEventTypeView(activity, true);
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
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought100(activity)) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again");
                isTracked100 = true;
            } else {
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "100", Constants.EVENT_TYPE_PLAY_THROUGHT), new UizaTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again isTracked75 true");
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought75(activity)) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again");
                isTracked75 = true;
            } else {
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "75", Constants.EVENT_TYPE_PLAY_THROUGHT), new UizaTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again isTracked50 true");
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought50(activity)) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again");
                isTracked50 = true;
            } else {
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "50", Constants.EVENT_TYPE_PLAY_THROUGHT), new UizaTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(activity, true);
                    }
                });
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                //LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again isTracked25 true");
                return;
            }
            if (UizaTrackingUtil.isTrackedEventTypePlayThrought25(activity)) {
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again");
                isTracked25 = true;
            } else {
                trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "25", Constants.EVENT_TYPE_PLAY_THROUGHT), new UizaTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(activity, true);
                    }
                });
            }
        }
    }

    protected void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst");
        if (UizaUtil.getClickedPip(activity)) {
            LLog.d(TAG, "getClickedPip true -> setPlayWhenReady true");
            uizaPlayerManagerV3.getPlayer().setPlayWhenReady(true);
        }
        if (uizaCallback != null) {
            //LLog.d(TAG, "onStateReadyFirst ===> isInitResult");
            uizaCallback.isInitResult(true, mResultGetLinkPlay, UizaDataV3.getInstance().getData());
        }
        if (isCastingChromecast) {
            //LLog.d(TAG, "onStateReadyFirst init new play check isCastingChromecast: " + isCastingChromecast);
            lastCurrentPosition = 0;
            handleConnectedChromecast();
            showController();
        }
        if (UizaTrackingUtil.isTrackedEventTypeVideoStarts(activity)) {
            //da track roi ko can track nua
        } else {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIDEO_STARTS), new UizaTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UizaTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(activity, true);
                }
            });
        }
        UizaDataV3.getInstance().setSettingPlayer(false);
    }

    public void setProgressSeekbar(final VerticalSeekBar verticalSeekBar,
                                   final int progressSeekbar) {
        verticalSeekBar.setProgress(progressSeekbar);
        //LLog.d(TAG, "setProgressSeekbar " + progressSeekbar);
    }

    public void setProgressVolumeSeekbar(int progress) {
        setProgressSeekbar(seekbarVolume, progress);
    }

    public int getCurrentProgressSeekbarVolume() {
        if (seekbarVolume == null) {
            return 0;
        }
        return seekbarVolume.getProgress();
    }

    public void onDestroy() {
        if (firstBrightness != Constants.NOT_FOUND) {
            //LLog.d(TAG, "onDestroy setBrightness " + firstBrightness);
            LScreenUtil.setBrightness(getContext(), firstBrightness);
        }
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.release();
        }
        UizaDataV3.getInstance().setSettingPlayer(false);
        //UizaDataV3.getInstance().clearDataForPlaylistFolder();
        //UizaDataV3.getInstance().clearDataForEntity();
        LDialogUtil.clearAll();
        activityIsPausing = true;
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        //LLog.d(TAG, "onDestroy -> set activityIsPausing = true");
    }

    public void onResume() {
        if (isCastingChromecast) {
            //LLog.d(TAG, "onResume isCastingChromecast true => return");
            return;
        }

        //LLog.d(TAG, "onResume");
        activityIsPausing = false;
        if (isExoShareClicked) {
            isExoShareClicked = false;

            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.resumeVideo();
            }
        } else {
            initUizaPlayerManagerV3();
        }
    }

    private void initUizaPlayerManagerV3() {
        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "onResume uizaPlayerManagerV3 init");
            uizaPlayerManagerV3.init();
            if (UizaUtil.getClickedPip(activity) && !UizaDataV3.getInstance().isPlayWithPlaylistFolder()) {
                LLog.d(TAG, "initUizaPlayerManagerV3 setPlayWhenReady false ");
                uizaPlayerManagerV3.getPlayer().setPlayWhenReady(false);
            } else {
                //LLog.d(TAG, "initUizaPlayerManagerV3 do nothing");
            }
            if (isCalledFromConnectionEventBus) {
                uizaPlayerManagerV3.setRunnable();
                isCalledFromConnectionEventBus = false;
            }
        }
    }

    private boolean activityIsPausing = false;

    private boolean isActivityIsPausing() {
        return activityIsPausing;
    }

    public void onPause() {
        //LLog.d(TAG, "onPause " + isExoShareClicked);
        activityIsPausing = true;
        if (isExoShareClicked) {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.pauseVideo();
            }
        } else {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.reset();
            }
        }
    }

    public void onStart() {
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStartPreview(PreviewView previewView) {
        //LLog.d(TAG, "onStartPreview");
    }

    @Override
    public void onStopPreview(PreviewView previewView) {
        //LLog.d(TAG, "onStopPreview");
        uizaPlayerManagerV3.resumeVideo();
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        //LLog.d(TAG, "onPreview progress " + progress);
        if (isCastingChromecast) {
            UizaDataV3.getInstance().getCasty().getPlayer().seek(progress);
        }
    }

    private boolean isExoShareClicked;
    private boolean isExoVolumeClicked;

    protected void toggleScreenOritation() {
        boolean isLandToPort = LActivityUtil.toggleScreenOritation(activity);
        if (isLandToPort) {
            //do nothing
        } else {
            UizaUtil.resizeLayout(rootView, llMid, ivVideoCover, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            //do nothing
            //LLog.d(TAG, "onClick llMsg");
        } else if (v == exoFullscreenIcon) {
            toggleScreenOritation();
        } else if (v == exoBackScreen) {
            handleClickBackScreen();
        } else if (v == exoVolume) {
            handleClickBtVolume();
        } else if (v == exoSetting) {
            handleClickSetting();
        } else if (v == exoCc) {
            handleClickCC();
        } else if (v == exoPlaylistRelation) {
            handleClickPlaylistRelation();
        } else if (v == exoPlaylistFolder) {
            handleClickPlaylistFolder();
        } else if (v == exoHearing) {
            handleClickHearing();
        } else if (v == exoPictureInPicture) {
            handleClickPictureInPicture();
        } else if (v == exoShare) {
            handleClickShare();
        } else if (v.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManagerV3.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null && uizaPlayerManagerV3.getTrackSelectionHelper() != null) {
                uizaPlayerManagerV3.getTrackSelectionHelper().showSelectionDialog(activity, ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
            }
        } else if (v == rlChromeCast) {
            //dangerous to remove
            //LLog.d(TAG, "do nothing click rl_chrome_cast");
        } else if (v == exoFfwd) {
            if (isCastingChromecast) {
                UizaDataV3.getInstance().getCasty().getPlayer().seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                uizaPlayerManagerV3.seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
            }
        } else if (v == exoRew) {
            if (isCastingChromecast) {
                UizaDataV3.getInstance().getCasty().getPlayer().seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                uizaPlayerManagerV3.seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
            }
        } else if (v == exoPause) {
            if (isCastingChromecast) {
                UizaDataV3.getInstance().getCasty().getPlayer().pause();
            } else {
                uizaPlayerManagerV3.pauseVideo();
            }
            exoPause.setVisibility(GONE);
            exoPlay.setVisibility(VISIBLE);
        } else if (v == exoPlay) {
            if (isCastingChromecast) {
                UizaDataV3.getInstance().getCasty().getPlayer().play();
            } else {
                uizaPlayerManagerV3.resumeVideo();
            }
            exoPlay.setVisibility(GONE);
            exoPause.setVisibility(VISIBLE);
        } else if (v == exoSkipNext) {
            handleClickSkipNext();
        } else if (v == exoSkipPrevious) {
            handleClickSkipPrevious();
        }
    }

    //current screen is landscape or portrait
    private boolean isLandscape;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LScreenUtil.hideDefaultControls(activity);
                isLandscape = true;
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, true);
                if (isTablet) {
                    exoPictureInPicture.setVisibility(GONE);
                }
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, false);
                if (isTablet && !isCastingChromecast()) {
                    exoPictureInPicture.setVisibility(VISIBLE);
                }
            }
        }
        setMarginPreviewTimeBarLayout();
        setMarginRlLiveInfo();
        UizaUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
        updatePositionOfProgressBar();
    }

    private void setMarginPreviewTimeBarLayout() {
        if (isLandscape) {
            LUIUtil.setMarginDimen(previewTimeBarLayout, 24, 0, 24, 0);
        } else {
            LUIUtil.setMarginDimen(previewTimeBarLayout, 15, 0, 15, 0);
        }
    }

    private void setMarginRlLiveInfo() {
        if (isLandscape) {
            LUIUtil.setMarginDimen(rlLiveInfo, 50, 0, 50, 0);
        } else {
            LUIUtil.setMarginDimen(rlLiveInfo, 5, 0, 5, 0);
        }
    }

    public void setTitle() {
        tvTitle.setText(UizaDataV3.getInstance().getEntityName());
    }

    private void updateUI() {
        //LLog.d(TAG, "updateUI isTablet " + isTablet);
        if (isTablet && !isCastingChromecast()) {
            exoPictureInPicture.setVisibility(VISIBLE);
        } else {
            exoPictureInPicture.setVisibility(GONE);
        }
        if (isLivestream) {
            exoPlaylistRelation.setVisibility(GONE);
            exoCc.setVisibility(GONE);
            rlTimeBar.setVisibility(GONE);

            //TODO why set gone not work?
            //exoRew.setVisibility(GONE);
            //exoFfwd.setVisibility(GONE);

            changeVisibilitiesOfButton(exoRew, false, 0);
            changeVisibilitiesOfButton(exoFfwd, false, 0);

            rlLiveInfo.setVisibility(VISIBLE);
        } else {
            //TODO exoPlaylistRelation works fine, but QC wanne hide it
            exoPlaylistRelation.setVisibility(GONE);
            exoCc.setVisibility(VISIBLE);
            rlTimeBar.setVisibility(VISIBLE);

            //TODO why set visible not work?
            //exoRew.setVisibility(VISIBLE);
            //exoFfwd.setVisibility(VISIBLE);

            changeVisibilitiesOfButton(exoRew, true, R.drawable.baseline_replay_10_white_48);
            changeVisibilitiesOfButton(exoFfwd, true, R.drawable.baseline_forward_10_white_48);

            rlLiveInfo.setVisibility(GONE);
        }
    }

    //trick to gone view
    private void changeVisibilitiesOfButton(ImageButtonWithSize imageButtonWithSize,
                                            boolean isVisible, int res) {
        if (imageButtonWithSize == null) {
            return;
        }
        imageButtonWithSize.setClickable(isVisible);
        imageButtonWithSize.setImageResource(res);
    }

    protected void updateButtonVisibilities() {
        debugRootView.removeAllViews();
        if (uizaPlayerManagerV3.getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManagerV3.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(activity);
                int label;
                switch (uizaPlayerManagerV3.getPlayer().getRendererType(i)) {
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
                debugRootView.addView(button);
            }
        }
    }

    //on seekbar change
    @Override
    public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
        //LLog.d(TAG, "onProgressChanged progress: " + progress);
        if (seekBar == null || !isLandscape) {
            if (isExoVolumeClicked) {
                //LLog.d(TAG, "onProgressChanged !isExoVolumeClicked ctn");
            } else {
                //LLog.d(TAG, "onProgressChanged !isExoVolumeClicked return");
                return;
            }
        }
        if (seekBar == seekbarVolume) {
            if (isSetProgressSeekbarFirst || isExoVolumeClicked) {
                exoIvPreview.setVisibility(INVISIBLE);
            } else {
                if (progress >= 66) {
                    exoIvPreview.setImageResource(R.drawable.baseline_volume_up_white_48);
                } else if (progress >= 33) {
                    exoIvPreview.setImageResource(R.drawable.baseline_volume_down_white_48);
                } else {
                    exoIvPreview.setImageResource(R.drawable.baseline_volume_mute_white_48);
                }
            }
            //LLog.d(TAG, "seekbarVolume onProgressChanged " + progress + " -> " + ((float) progress / 100));
            if (progress == 0) {
                exoVolume.setImageResource(R.drawable.baseline_volume_off_white_48);
            } else {
                exoVolume.setImageResource(R.drawable.baseline_volume_up_white_48);
            }
            uizaPlayerManagerV3.setVolume(((float) progress / 100));
            if (isExoVolumeClicked) {
                isExoVolumeClicked = false;
            }
        } else if (seekBar == seekbarBirghtness) {
            //LLog.d(TAG, "seekbarBirghtness onProgressChanged " + progress);
            if (isSetProgressSeekbarFirst) {
                exoIvPreview.setVisibility(INVISIBLE);
            } else {
                if (progress >= 210) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_7_black_48dp);
                } else if (progress >= 175) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_6_black_48dp);
                } else if (progress >= 140) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_5_black_48dp);
                } else if (progress >= 105) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_4_black_48dp);
                } else if (progress >= 70) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_3_black_48dp);
                } else if (progress >= 35) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_2_black_48dp);
                } else {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_1_black_48dp);
                }
            }
            LLog.d(TAG, "onProgressChanged setBrightness " + progress);
            LScreenUtil.setBrightness(activity, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //LLog.d(TAG, "onStartTrackingTouch");
        if (!isLandscape) {
            return;
        }
        LUIUtil.setTintSeekbar(seekBar, Color.WHITE);
        exoIvPreview.setVisibility(VISIBLE);
        if (llMidSub != null) {
            llMidSub.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //LLog.d(TAG, "onStopTrackingTouch");
        if (!isLandscape) {
            return;
        }
        LUIUtil.setTintSeekbar(seekBar, Color.TRANSPARENT);
        exoIvPreview.setVisibility(INVISIBLE);
        if (llMidSub != null) {
            llMidSub.setVisibility(VISIBLE);
        }
    }
    //end on seekbar change

    private void handleClickPictureInPicture() {
        //LLog.d(TAG, "handleClickPictureInPicture");
        if (activity == null) {
            return;
        }
        if (isTablet) {
            LLog.d(TAG, "handleClickPictureInPicture only available for tablet -> return");
            return;
        }
        if (isCastingChromecast()) {
            LLog.d(TAG, "handleClickPictureInPicture isCastingChromecast -> return");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            //LLog.d(TAG, "handleClickPictureInPicture if");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            //LLog.d(TAG, "handleClickPictureInPicture else");
            initializePiP();
        }
    }

    public void initializePiP() {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, FloatingUizaVideoServiceV3.class);
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uizaPlayerManagerV3.getLinkPlay());
        intent.putExtra(Constants.FLOAT_IS_LIVESTREAM, isLivestream);
        activity.startService(intent);
        if (uizaCallback != null) {
            uizaCallback.onClickPip(intent);
        }
    }

    public SimpleExoPlayer getPlayer() {
        if (uizaPlayerManagerV3 == null) {
            return null;
        }
        return uizaPlayerManagerV3.getPlayer();
    }

    private void getTokenStreaming() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UizaDataV3.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(UizaDataV3.getInstance().getEntityId());
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        activity.subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                //LLog.d(TAG, "getTokenStreaming onSuccess: " + gson.toJson(result));
                if (Constants.IS_DEBUG) {
                    LToast.show(activity, "getTokenStreaming onSuccess");
                }
                if (result == null || result.getData() == null || result.getData().getToken() == null || result.getData().getToken().isEmpty()) {
                    LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.no_token_streaming), new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                            handleError(new Exception(activity.getString(R.string.no_token_streaming)));
                        }

                        @Override
                        public void onCancel() {
                            handleError(new Exception(activity.getString(R.string.no_token_streaming)));
                        }
                    });
                    return;
                }
                String tokenStreaming = result.getData().getToken();
                LLog.d(TAG, "getTokenStreaming onSuccess: " + tokenStreaming);
                getLinkPlay(tokenStreaming);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getTokenStreaming onFail " + e.getMessage());
                final String msg = Constants.IS_DEBUG ? activity.getString(R.string.no_token_streaming) + "\n" + e.getMessage() : activity.getString(R.string.no_token_streaming);
                LDialogUtil.showDialog1Immersive(activity, msg, new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        handleError(new Exception(msg));
                    }

                    @Override
                    public void onCancel() {
                        handleError(new Exception(msg));
                    }
                });
            }
        });
    }

    private void getLinkPlay(String tokenStreaming) {
        //LLog.d(TAG, "getLinkPlay isLivestream " + isLivestream);
        RestClientV3GetLinkPlay.addAuthorization(tokenStreaming);
        UizaServiceV3 service = RestClientV3GetLinkPlay.createService(UizaServiceV3.class);
        if (isLivestream) {
            String appId = UizaDataV3.getInstance().getAppId();
            String channelName = UizaDataV3.getInstance().getChannelName();
            //LLog.d(TAG, "===================================");
            //LLog.d(TAG, "========name: " + channelName);
            //LLog.d(TAG, "========appId: " + appId);
            //LLog.d(TAG, "===================================");
            activity.subscribe(service.getLinkPlayLive(appId, channelName), new ApiSubscriber<ResultGetLinkPlay>() {
                @Override
                public void onSuccess(ResultGetLinkPlay result) {
                    if (Constants.IS_DEBUG) {
                        LToast.show(activity, "getLinkPlay isLivestream onSuccess");
                    }
                    LLog.d(TAG, "getLinkPlayLive onSuccess: " + gson.toJson(result));
                    mResultGetLinkPlay = result;
                    isResultGetLinkPlayDone = true;
                    checkToSetUp();
                }

                @Override
                public void onFail(Throwable e) {
                    if (e == null) {
                        LLog.e(TAG, "getLinkPlay LIVE onFail");
                        return;
                    }
                    LLog.e(TAG, "getLinkPlayLive LIVE onFail " + e.getMessage());
                    final String msg = Constants.IS_DEBUG ? activity.getString(R.string.no_link_play) + "\n" + e.getMessage() : activity.getString(R.string.no_link_play);
                    LDialogUtil.showDialog1Immersive(activity, msg, new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                            handleError(new Exception(msg));
                        }

                        @Override
                        public void onCancel() {
                            handleError(new Exception(msg));
                        }
                    });
                }
            });
        } else {
            String appId = UizaDataV3.getInstance().getAppId();
            String entityId = UizaDataV3.getInstance().getEntityId();
            String typeContent = SendGetTokenStreaming.STREAM;
            //LLog.d(TAG, "===================================");
            //LLog.d(TAG, "========tokenStreaming: " + tokenStreaming);
            //LLog.d(TAG, "========appId: " + appId);
            //LLog.d(TAG, "========entityId: " + entityId);
            //LLog.d(TAG, "===================================");
            activity.subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
                @Override
                public void onSuccess(ResultGetLinkPlay result) {
                    if (Constants.IS_DEBUG) {
                        LToast.show(activity, "getLinkPlay !isLivestream onSuccess");
                    }
                    //LLog.d(TAG, "getLinkPlayVOD onSuccess: " + gson.toJson(result));
                    //LLog.d(TAG, "getLinkPlayVOD onSuccess");
                    mResultGetLinkPlay = result;
                    isResultGetLinkPlayDone = true;
                    checkToSetUp();
                }

                @Override
                public void onFail(Throwable e) {
                    if (e == null) {
                        LLog.e(TAG, "getLinkPlay VOD onFail");
                        return;
                    }
                    LLog.e(TAG, "getLinkPlay VOD onFail " + e.getMessage());
                    final String msg = Constants.IS_DEBUG ? activity.getString(R.string.no_link_play) + "\n" + e.getMessage() : activity.getString(R.string.no_link_play);
                    LDialogUtil.showDialog1Immersive(activity, msg, new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                            handleError(new Exception(msg));
                        }

                        @Override
                        public void onCancel() {
                            handleError(new Exception(msg));
                        }
                    });
                }
            });
        }
    }

    private UizaCallback uizaCallback;

    private void trackUiza(final UizaTracking uizaTracking, final UizaTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        //LLog.d(TAG, "------------------------>trackUiza noPiP getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
        UizaServiceV2 service = RestClientTracking.createService(UizaServiceV2.class);
        activity.subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                //LLog.d(TAG, "<------------------------track success: " + uizaTracking.getEventType() + " : " + uizaTracking.getPlayThrough() + " : " + uizaTracking.getEntityName());
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
                if (uizaTrackingCallback != null) {
                    uizaTrackingCallback.onTrackingSuccess();
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
            }
        });
    }

    /**
     * seekTo position
     */
    public void seekTo(long positionMs) {
        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "seekTo positionMs: " + positionMs);
            uizaPlayerManagerV3.seekTo(positionMs);
        }
    }

    private boolean isCalledFromConnectionEventBus = false;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        if (event != null) {
            //LLog.d(TAG, "onMessageEventConnectEvent isConnected: " + event.isConnected());
            if (event.isConnected()) {
                if (uizaPlayerManagerV3 != null) {
                    LDialogUtil.clearAll();
                    if (uizaPlayerManagerV3.getExoPlaybackException() == null) {
                        //LLog.d(TAG, "onMessageEventConnectEvent do nothing");
                        hideLLMsg();
                    } else {
                        isCalledFromConnectionEventBus = true;
                        uizaPlayerManagerV3.setResumeIfConnectionError();
                        //LLog.d(TAG, "onMessageEventConnectEvent activityIsPausing " + activityIsPausing);
                        if (!activityIsPausing) {
                            uizaPlayerManagerV3.init();
                            if (isCalledFromConnectionEventBus) {
                                uizaPlayerManagerV3.setRunnable();
                                isCalledFromConnectionEventBus = false;
                            }
                        } else {
                            //LLog.d(TAG, "onMessageEventConnectEvent auto call onResume() again");
                        }
                    }
                } else {
                    //LLog.d(TAG, "onMessageEventConnectEvent uizaPlayerManagerV3 == null");
                }
            } else {
                showTvMsg(activity.getString(R.string.err_no_internet));
                //if current screen is portrait -> do nothing
                //else current screen is landscape -> change screen to portrait
                LActivityUtil.changeScreenPortrait(activity);
            }
        }
    }

    //listen msg from service FloatingUizaVideoServiceV3
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromService msg) {
        //LLog.d(TAG, "get event from service");
        if (msg == null) {
            return;
        }
        //when pip float view init success
        if (uizaCallback != null && msg instanceof ComunicateMng.MsgFromServiceIsInitSuccess) {
            //Hàm này được gọi khi player ở FloatingUizaVideoServiceV3 đã init xong (nó đang play ở vị trí 0)
            //Nhiệm vụ là mình sẽ gửi vị trí hiện tại sang cho FloatingUizaVideoServiceV3 nó biết
            //LLog.d(TAG, "get event from service isInitSuccess: " + ((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
            ComunicateMng.MsgFromActivityPosition msgFromActivityPosition = new ComunicateMng.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(uizaPlayerManagerV3.getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);
            uizaCallback.onClickPipVideoInitSuccess(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        } else if (msg instanceof ComunicateMng.MsgFromServicePosition) {
            //FloatingUizaVideoService trước khi hủy đã gửi position của pip tới đây
            //Nhận được vị trí từ FloatingUizaVideoServiceV3 rồi seek tới vị trí này
            //LLog.d(TAG, "seek to: " + ((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.seekTo(((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            }
        }
    }

    public void showController() {
        playerView.showController();
    }

    public void hideController() {
        playerView.hideController();
    }

    protected void hideControllerOnTouch(boolean isHide) {
        playerView.setControllerHideOnTouch(isHide);
    }

    private void showTvMsg(String msg) {
        //LLog.d(TAG, "showTvMsg " + msg);
        tvMsg.setText(msg);
        showLLMsg();
    }

    protected void showLLMsg() {
        if (rlMsg.getVisibility() != VISIBLE) {
            rlMsg.setVisibility(VISIBLE);
        }
        hideController();
    }

    protected void hideLLMsg() {
        if (rlMsg.getVisibility() != GONE) {
            rlMsg.setVisibility(GONE);
        }
    }

    private void updateLiveInfoCurrentView(final int durationDelay) {
        if (!isLivestream) {
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //LLog.d(TAG, "updateLiveInfoCurrentView " + System.currentTimeMillis());
                if (!isLivestream) {
                    return;
                }
                if (uizaPlayerManagerV3 != null && playerView != null && (playerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    //LLog.d(TAG, "updateLiveInfoCurrentView isShowing");
                    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
                    String id = UizaDataV3.getInstance().getEntityId();
                    activity.subscribe(service.getViewALiveFeed(id), new ApiSubscriber<ResultGetViewALiveFeed>() {
                        @Override
                        public void onSuccess(ResultGetViewALiveFeed result) {
                            if (Constants.IS_DEBUG) {
                                LToast.show(activity, "updateLiveInfoCurrentView onSuccess");
                            }
                            //LLog.d(TAG, "getViewALiveFeed onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null) {
                                tvLiveView.setText(result.getData().getWatchnow() + "");
                            }
                            updateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "getViewALiveFeed onFail " + e.getMessage());
                            updateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                        }
                    });
                } else {
                    //LLog.d(TAG, "updateLiveInfoCurrentView !isShowing");
                    updateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    private void updateLiveInfoTimeStartLive(final int durationDelay) {
        if (!isLivestream) {
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {

            @Override
            public void doAfter(int mls) {
                if (!isLivestream) {
                    return;
                }
                if (uizaPlayerManagerV3 != null && playerView != null && (playerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
                    String entityId = UizaDataV3.getInstance().getEntityId();
                    String feedId = UizaDataV3.getInstance().getLastFeedId();
                    activity.subscribe(service.getTimeStartLive(entityId, feedId), new ApiSubscriber<ResultTimeStartLive>() {
                        @Override
                        public void onSuccess(ResultTimeStartLive result) {
                            if (Constants.IS_DEBUG) {
                                LToast.show(activity, "updateLiveInfoTimeStartLive onSuccess");
                            }
                            //LLog.d(TAG, "getTimeStartLive onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null && result.getData().getStartTime() != null) {
                                //LLog.d(TAG, "startTime " + result.getData().getStartTime());
                                long startTime = LDateUtils.convertDateToTimeStamp(result.getData().getStartTime(), LDateUtils.FORMAT_1);
                                //LLog.d(TAG, "startTime " + startTime);
                                long now = System.currentTimeMillis();
                                //LLog.d(TAG, "now: " + now);
                                long duration = now - startTime;
                                //LLog.d(TAG, "duration " + duration);
                                String s = LDateUtils.convertMlscondsToHMmSs(duration);
                                //LLog.d(TAG, "s " + s);
                                tvLiveTime.setText(s);
                            }
                            updateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "getTimeStartLive onFail " + e.getMessage());
                            updateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                        }
                    });
                } else {
                    updateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    /*Kiểm tra xem nếu activity được tạo thành công nếu user click vào pip thì sẽ bắn 1 eventbus báo rằng đã init success
     * receiver FloatingUizaVideoServiceV3 để truyền current position */
    public void setEventBusMsgFromActivityIsInitSuccess() {
        if (UizaUtil.getClickedPip(activity)) {
            ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
            msgFromActivityIsInitSuccess.setInitSuccess(true);
            ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
        }
    }

    /*START CHROMECAST*/
    @UiThread
    private void setUpMediaRouteButton() {
        UizaDataV3.getInstance().getCasty().setUpMediaRouteButton(mediaRouteButton);
        UizaDataV3.getInstance().getCasty().setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                //LLog.d(TAG, "setUpMediaRouteButton setOnConnectChangeListener onConnected");
                if (uizaPlayerManagerV3 != null) {
                    lastCurrentPosition = uizaPlayerManagerV3.getCurrentPosition();
                }
                handleConnectedChromecast();
            }

            @Override
            public void onDisconnected() {
                //LLog.d(TAG, "setUpMediaRouteButton setOnConnectChangeListener onDisconnected");
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
        if (UizaDataV3.getInstance().getData() == null || uizaPlayerManagerV3 == null || uizaPlayerManagerV3.getPlayer() == null) {
            return;
        }
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }
        //LLog.d(TAG, "playChromecast exo stop lastCurrentPosition: " + lastCurrentPosition);

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UizaDataV3.getInstance().getData().getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, UizaDataV3.getInstance().getData().getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(UizaDataV3.getInstance().getData().getThumbnail())));

        //TODO add subtile vtt to chromecast
        List<MediaTrack> mediaTrackList = new ArrayList<>();
        /*MediaTrack mediaTrack = UizaDataV3.getInstance().buildTrack(
                1,
                "text",
                "captions",
                "http://112.78.4.162/sub.vtt",
                "English Subtitle",
                "en-US");
        mediaTrackList.add(mediaTrack);
        mediaTrack = UizaDataV3.getInstance().buildTrack(
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
                uizaPlayerManagerV3.getLinkPlay())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .setMediaTracks(mediaTrackList)
                .setStreamDuration(duration)
                .build();

        //play chromecast with full screen control
        //UizaDataV3.getInstance().getCasty().getPlayer().loadMediaAndPlay(mediaInfo, true, lastCurrentPosition);

        //play chromecast without screen control
        UizaDataV3.getInstance().getCasty().getPlayer().loadMediaAndPlayInBackground(mediaInfo, true, lastCurrentPosition);

        UizaDataV3.getInstance().getCasty().getPlayer().getRemoteMediaClient().addProgressListener(new RemoteMediaClient.ProgressListener() {
            @Override
            public void onProgressUpdated(long currentPosition, long duration) {
                //LLog.d(TAG, "onProgressUpdated " + currentPosition + " - " + duration + " >>> max " + previewTimeBar.getMax());
                if (currentPosition >= lastCurrentPosition && !isCastPlayerPlayingFirst) {
                    //LLog.d(TAG, "onProgressUpdated PLAYING FIRST");
                    uizaPlayerManagerV3.hideProgress();
                    //UizaDataV3.getInstance().getCasty().setVolume(0.99f);
                    isCastPlayerPlayingFirst = true;
                }

                if (currentPosition > 0) {
                    uizaPlayerManagerV3.seekTo(currentPosition);
                    //previewTimeBar.setPosition(currentPosition);
                }
            }
        }, 1000);

    }

    private boolean isCastPlayerPlayingFirst;

    /*STOP CHROMECAST*/

    /*khi click vào biểu tượng casting
     * thì sẽ pause local player và bắt đầu loading lên cast player
     * khi disconnet thì local player sẽ resume*/
    private void updateUIChromecast() {
        if (uizaPlayerManagerV3 == null || rlChromeCast == null) {
            return;
        }
        if (isCastingChromecast) {
            uizaPlayerManagerV3.pauseVideo();
            uizaPlayerManagerV3.setVolume(0f);
            rlChromeCast.setVisibility(VISIBLE);
            exoSetting.setVisibility(GONE);
            exoCc.setVisibility(GONE);
            llMid.setVisibility(GONE);

            exoPlay.setVisibility(GONE);
            exoPause.setVisibility(VISIBLE);

            exoVolume.setVisibility(GONE);

            //casting player luôn play first với volume not mute
            //exoVolume.setImageResource(R.drawable.ic_volume_up_black_48dp);
            //UizaDataV3.getInstance().getCasty().setVolume(0.99);

            //double volumeOfExoPlayer = uizaPlayerManagerV3.getVolume();
            //LLog.d(TAG, "volumeOfExoPlayer " + volumeOfExoPlayer);
            //UizaDataV3.getInstance().getCasty().setVolume(volumeOfExoPlayer);
        } else {
            uizaPlayerManagerV3.resumeVideo();
            uizaPlayerManagerV3.setVolume(0.99f);
            rlChromeCast.setVisibility(GONE);
            exoSetting.setVisibility(VISIBLE);
            exoCc.setVisibility(VISIBLE);
            llMid.setVisibility(VISIBLE);

            exoPlay.setVisibility(GONE);
            exoPause.setVisibility(VISIBLE);

            //TODO iplm volume mute on/off o cast player
            exoVolume.setVisibility(VISIBLE);
            //khi quay lại exoplayer từ cast player thì mặc định sẽ bật lại âm thanh (dù cast player đang mute hay !mute)
            //exoVolume.setImageResource(R.drawable.ic_volume_up_black_48dp);
            //uizaPlayerManagerV3.setVolume(0.99f);

            /*double volumeOfCastPlayer = UizaDataV3.getInstance().getCasty().getVolume();
            LLog.d(TAG, "volumeOfCastPlayer " + volumeOfCastPlayer);
            uizaPlayerManagerV3.setVolume((float) volumeOfCastPlayer);*/
        }
    }

    /**
     * Hide the button back screen
     */
    public void hideBackScreen() {
        if (exoBackScreen != null) {
            exoBackScreen.setVisibility(GONE);
        }
    }

    //===================================================================START FOR PLAYLIST/FOLDER
    private final int pfLimit = 50;
    private int pfPage = 0;
    private int pfTotalPage = Integer.MAX_VALUE;
    private final String pfOrderBy = "createdAt";
    private final String pfOrderType = "DESC";
    private final String publishToCdn = "success";

    private void getListAllEntity(String metadataId) {
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }
        if (UizaDataV3.getInstance().getDataList() == null) {
            LLog.d(TAG, "getListAllEntity UizaDataV3.getInstance().getDataList() == null -> call api lấy data list");
            UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
            activity.subscribe(service.getListAllEntity(metadataId, pfLimit, pfPage, pfOrderBy, pfOrderType, publishToCdn), new ApiSubscriber<ResultListEntity>() {
                @Override
                public void onSuccess(ResultListEntity result) {
                    if (Constants.IS_DEBUG) {
                        LToast.show(activity, "getListAllEntity onSuccess");
                    }
                    LLog.d(TAG, "getListAllEntity onSuccess: " + gson.toJson(result));
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
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
                    //TODO wait api from duyqt
                    LLog.d(TAG, "<<<getListAllEntity: " + pfPage + "/" + pfTotalPage);
                    UizaDataV3.getInstance().setDataList(result.getData());
                    if (UizaDataV3.getInstance().getDataList() == null || UizaDataV3.getInstance().getDataList().isEmpty()) {
                        LLog.e(TAG, "getListAllEntity success but noda");
                        if (uizaCallback != null) {
                            uizaCallback.onError(new Exception("getListAllEntity success but no data"));
                        }
                        return;
                    }
                    //LLog.d(TAG, "list size: " + UizaDataV3.getInstance().getDataList().size());
                    playPlaylistPosition(UizaDataV3.getInstance().getCurrentPositionOfDataList());
                    if (uizaPlayerManagerV3 != null) {
                        uizaPlayerManagerV3.hideProgress();
                    }

                    //show controller for playlist folder
                    setVisibilityOfPlaylistFolderController(VISIBLE);
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                    if (uizaCallback != null) {
                        uizaCallback.onError(new Exception("getListAllEntity failed: " + e.toString()));
                    }
                    if (uizaPlayerManagerV3 != null) {
                        uizaPlayerManagerV3.hideProgress();
                    }
                }
            });
        } else {
            LLog.d(TAG, "getListAllEntity UizaDataV3.getInstance().getDataList() != null -> không cần call api lấy data list nữa mà lấy dữ liệu có sẵn play luôn");
            //LLog.d(TAG, "list size: " + UizaDataV3.getInstance().getDataList().size());
            playPlaylistPosition(UizaDataV3.getInstance().getCurrentPositionOfDataList());
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.hideProgress();
            }
            //show controller for playlist folder
            setVisibilityOfPlaylistFolderController(VISIBLE);
        }
    }

    protected boolean isPlayPlaylistFolder() {
        if (UizaDataV3.getInstance().getDataList() == null) {
            return false;
        }
        return true;
    }

    private void playPlaylistPosition(int position) {
        if (UizaDataV3.getInstance().getDataList() == null || position < 0 || position > UizaDataV3.getInstance().getDataList().size() - 1) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            return;
        }

        //update UI for skip next and skip previous button
        if (position == 0) {
            exoSkipPrevious.setEnabled(false);
            exoSkipNext.setEnabled(true);
            exoSkipPrevious.setColorFilter(Color.GRAY);
            exoSkipNext.setColorFilter(Color.WHITE);
        } else if (position == UizaDataV3.getInstance().getDataList().size() - 1) {
            exoSkipPrevious.setEnabled(true);
            exoSkipNext.setEnabled(false);
            exoSkipPrevious.setColorFilter(Color.WHITE);
            exoSkipNext.setColorFilter(Color.GRAY);
        } else {
            exoSkipPrevious.setEnabled(true);
            exoSkipNext.setEnabled(true);
            exoSkipPrevious.setColorFilter(Color.WHITE);
            exoSkipNext.setColorFilter(Color.WHITE);
        }
        //end update UI for skip next and skip previous button

        //currentPositionOfDataList = position;
        UizaDataV3.getInstance().setCurrentPositionOfDataList(position);
        //Data data = dataList.get(currentPositionOfDataList);
        Data data = UizaDataV3.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position);
        init(UizaDataV3.getInstance().getDataWithPositionOfDataList(position).getId(), null, null, false, false);
    }

    protected void onPlayerEnded() {
        if (isPlayPlaylistFolder()) {
            autoSwitchNextVideo();
        }
    }

    private void autoSwitchNextVideo() {
        playPlaylistPosition(UizaDataV3.getInstance().getCurrentPositionOfDataList() + 1);
    }

    private void autoSwitchPreviousLinkVideo() {
        playPlaylistPosition(UizaDataV3.getInstance().getCurrentPositionOfDataList() - 1);
    }

    private void handleClickPlaylistFolder() {
        UizaDialogPlaylistFolder uizaDialogPlaylistFolder = new UizaDialogPlaylistFolder(activity, isLandscape, UizaDataV3.getInstance().getDataList(), UizaDataV3.getInstance().getCurrentPositionOfDataList(), new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(Data data, int position) {
                //LLog.d(TAG, "UizaDialogPlaylistFolder onClickItem " + gson.toJson(data));
                //currentPositionOfDataList = position;
                UizaDataV3.getInstance().setCurrentPositionOfDataList(position);
                //playPlaylistPosition(currentPositionOfDataList);
                playPlaylistPosition(position);
            }

            @Override
            public void onDismiss() {
            }
        });
        UizaUtil.showUizaDialog(activity, uizaDialogPlaylistFolder);
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
        exoPlaylistFolder.setVisibility(visibilityOfPlaylistFolderController);
        exoSkipNext.setVisibility(visibilityOfPlaylistFolderController);
        exoSkipPrevious.setVisibility(visibilityOfPlaylistFolderController);
    }

    private void handleClickSkipNext() {
        //LLog.d(TAG, "handleClickSkipNext");
        autoSwitchNextVideo();
    }

    private void handleClickSkipPrevious() {
        //LLog.d(TAG, "handleClickSkipPrevious");
        autoSwitchPreviousLinkVideo();
    }

    //===================================================================END FOR PLAYLIST/FOLDER

    /*Nếu đang casting thì button này sẽ handle volume on/off ở cast player
     * Ngược lại, sẽ handle volume on/off ở exo player*/
    private void handleClickBtVolume() {
        if (isCastingChromecast) {
            //LLog.d(TAG, "handleClickBtVolume isCastingChromecast");
            boolean isMute = UizaDataV3.getInstance().getCasty().toggleMuteVolume();
            if (isMute) {
                exoVolume.setImageResource(R.drawable.ic_volume_off_black_48dp);
            } else {
                exoVolume.setImageResource(R.drawable.baseline_volume_up_white_48);
            }
        } else {
            //LLog.d(TAG, "handleClickBtVolume !isCastingChromecast");
            if (uizaPlayerManagerV3 != null) {
                isExoVolumeClicked = true;
                uizaPlayerManagerV3.toggleVolumeMute(exoVolume);
            }
        }
    }

    private void handleClickBackScreen() {
        if (isLandscape) {
            exoFullscreenIcon.performClick();
        } else {
            if (uizaCallback != null) {
                uizaCallback.onClickBack();
            }
        }
    }

    private void handleClickPlaylistRelation() {
        UizaDialogListEntityRelationV3 uizaDialogListEntityRelation = new UizaDialogListEntityRelationV3(activity, isLandscape, new PlayListCallbackV3() {
            @Override
            public void onClickItem(Item item, int position) {
                //LLog.d(TAG, "onClickItem " + gson.toJson(item));
                if (uizaCallback != null) {
                    uizaCallback.onClickListEntityRelation(item, position);
                }
            }

            @Override
            public void onDismiss() {
                //do nothing
            }
        });
        UizaUtil.showUizaDialog(activity, uizaDialogListEntityRelation);
    }

    private void handleClickSetting() {
        View view = UizaUtil.getBtVideo(debugRootView);
        if (view != null) {
            UizaUtil.getBtVideo(debugRootView).performClick();
        }
    }

    private void handleClickCC() {
        if (uizaPlayerManagerV3 == null) {
            LLog.e(TAG, "Error handleClickCC uizaPlayerManagerV3 == null");
            return;
        }
        if (uizaPlayerManagerV3.getSubtitleList() == null || uizaPlayerManagerV3.getSubtitleList().isEmpty()) {
            UizaDialogInfo uizaDialogInfo = new UizaDialogInfo(activity, activity.getString(R.string.text), activity.getString(R.string.no_caption));
            UizaUtil.showUizaDialog(activity, uizaDialogInfo);
        } else {
            View view = UizaUtil.getBtText(debugRootView);
            if (view != null) {
                UizaUtil.getBtText(debugRootView).performClick();
            }
        }
    }

    private void handleClickHearing() {
        View view = UizaUtil.getBtAudio(debugRootView);
        if (view != null) {
            UizaUtil.getBtAudio(debugRootView).performClick();
        }
    }

    private void handleClickShare() {
        LSocialUtil.share(activity, isLandscape);
        isExoShareClicked = true;
    }

    /*
     ** Phát tiếp video
     */
    public void resumeVideo() {
        if (exoPlay != null) {
            exoPlay.performClick();
        }
    }

    /*
     ** Tạm dừng video
     */
    public void pauseVideo() {
        if (exoPause != null) {
            exoPause.performClick();
        }
    }

    public void setControllerStateCallback(UizaPlayerView.ControllerStateCallback controllerStateCallback) {
        if (playerView != null) {
            playerView.setControllerStateCallback(controllerStateCallback);
        }
    }

    /*
     **Cho phép sử dụng controller hay không
     * Mặc định: true
     * Nếu truyền false sẽ ẩn tất cả các component
     */
    public void setUseController(boolean isUseController) {
        if (playerView != null) {
            playerView.setUseController(isUseController);
        }
    }

    /*
     ** Bắt các event của player như click, long click...
     */
    public void setOnTouchEvent(UizaPlayerView.OnTouchEvent onTouchEvent) {
        if (playerView != null) {
            playerView.setOnTouchEvent(onTouchEvent);
        }
    }

    /*
     ** Đổi thời gian seek mặc định
     */
    public void setDefaultValueBackwardForward(int mls) {
        DEFAULT_VALUE_BACKWARD_FORWARD = mls;
    }

    /*
     ** Seek từ vị trí hiện tại cộng thêm bao nhiêu mls
     */
    public void seekToForward(int mls) {
        setDefaultValueBackwardForward(mls);
        if (exoFfwd != null) {
            exoFfwd.performClick();
        }
    }

    /*
     ** Seek từ vị trí hiện tại trừ đi bao nhiêu mls
     */
    public void seekToBackward(int mls) {
        setDefaultValueBackwardForward(mls);
        if (exoRew != null) {
            exoRew.performClick();
        }
    }

    /*
     **toggle volume on/off
     */
    public void toggleVolume() {
        if (exoVolume != null) {
            exoVolume.performClick();
        }
    }

    /*
     **toggle fullscreen
     */
    public void toggleFullscreen() {
        if (exoFullscreenIcon != null) {
            exoFullscreenIcon.performClick();
        }
    }

    /*
     **Hiển thị subtitle
     */
    public void showCCPopup() {
        if (exoCc != null) {
            exoCc.performClick();
        }
    }

    /*
     **Hiển thị chất lượng video
     */
    public void showHQPopup() {
        if (exoSetting != null) {
            exoSetting.performClick();
        }
    }

    /*
     **Hiển thị share lên mạng xã hội
     */
    public void showSharePopup() {
        if (exoShare != null) {
            exoShare.performClick();
        }
    }

    /*
     ** Hiển thị picture in picture và close video view hiện tại
     * Chỉ work nếu local player đang không casting
     * Device phải là tablet
     */
    public void showPip() {
        if (isCastingChromecast() || !isTablet) {
            LLog.d(TAG, "showPip isCastingChromecast || !isTablet -> return");
        } else {
            if (exoPictureInPicture != null) {
                exoPictureInPicture.performClick();
            }
        }
    }

    /*
     **Lấy độ dài video
     */
    public long getDuration() {
        if (uizaPlayerManagerV3 == null) {
            return Constants.NOT_FOUND;
        }
        return uizaPlayerManagerV3.getPlayer().getDuration();
    }

    /*
     ** Bỏ video hiện tại và chơi video tiếp theo trong playlist/folder
     */
    public void skipNextVideo() {
        if (exoSkipNext != null) {
            exoSkipNext.performClick();
        }
    }

    /*
     ** Bỏ video hiện tại và chơi lùi lại 1 video trong playlist/folder
     */
    public void skipPreviousVideo() {
        if (exoSkipPrevious != null) {
            exoSkipPrevious.performClick();
        }
    }
}
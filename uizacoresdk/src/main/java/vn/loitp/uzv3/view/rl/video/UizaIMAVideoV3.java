package vn.loitp.uzv3.view.rl.video;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.github.rubensousa.previewseekbar.PreviewView;
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
import vn.loitp.core.utilities.LAnimationUtil;
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
import vn.loitp.uzv1.listerner.ProgressCallback;
import vn.loitp.uzv1.view.ComunicateMng;
import vn.loitp.uzv1.view.dlg.info.UZDlgInfoV1;
import vn.loitp.uzv1.view.rl.video.UZPlayerViewV1;
import vn.loitp.uzv3.util.UZData;
import vn.loitp.uzv3.util.UZInput;
import vn.loitp.uzv3.util.UZTrackingUtil;
import vn.loitp.uzv3.util.UZUtil;
import vn.loitp.uzv3.view.dlg.listentityrelation.PlayListCallbackV3;
import vn.loitp.uzv3.view.dlg.listentityrelation.UizaDialogListEntityRelationV3;
import vn.loitp.uzv3.view.dlg.playlistfolder.CallbackPlaylistFolder;
import vn.loitp.uzv3.view.dlg.playlistfolder.UizaDialogPlaylistFolder;
import vn.loitp.uzv3.view.floatview.FloatingUizaVideoServiceV3;
import vn.loitp.uzv3.view.rl.timebar.UZTimebar;
import vn.loitp.views.LToast;
import vn.loitp.views.autosize.imagebuttonwithsize.ImageButtonWithSize;
import vn.loitp.views.autosize.textviewwithsize.TextViewWithSize;
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

    private FrameLayout previewFrameLayout;
    private UZTimebar uzTimebar;
    private ImageView ivThumbnail;
    private TextViewWithSize tvPosition;
    private TextViewWithSize tvDuration;

    private ViewGroup rlTimeBar;
    private RelativeLayout rlMsg;
    private TextView tvMsg;

    private ImageView ivVideoCover;
    private ImageButtonWithSize ibFullscreenIcon;
    private TextView tvTitle;
    private ImageButtonWithSize ibPauseIcon;
    private ImageButtonWithSize ibPlayIcon;
    private ImageButtonWithSize ibReplayIcon;
    private ImageButtonWithSize ibRewIcon;
    private ImageButtonWithSize ibFfwdIcon;
    private ImageButtonWithSize ibBackScreenIcon;
    private ImageButtonWithSize ibVolumeIcon;
    private ImageButtonWithSize ibSettingIcon;
    private ImageButtonWithSize ibCcIcon;
    private ImageButtonWithSize ibPlaylistRelationIcon;//danh sach video co lien quan
    private ImageButtonWithSize ibPlaylistFolderIcon;//danh sach playlist folder
    private ImageButtonWithSize ibHearingIcon;
    private ImageButtonWithSize ibPictureInPictureIcon;
    private ImageButtonWithSize ibShareIcon;
    private ImageButtonWithSize ibSkipPreviousIcon;
    private ImageButtonWithSize ibSkipNextIcon;
    private VerticalSeekBar seekbarVolume;
    private VerticalSeekBar seekbarBirghtness;
    private ImageView ivPreview;

    private RelativeLayout rlLiveInfo;
    private TextView tvLiveStatus;
    private TextView tvLiveView;
    private TextView tvLiveTime;
    private ImageButtonWithSize ivLiveTime;
    private ImageButtonWithSize ivLiveView;

    private LinearLayout debugLayout;
    private LinearLayout debugRootView;
    private TextView debugTextView;

    private RelativeLayout rlEndScreen;
    private TextView tvEndScreenMsg;

    private int firstBrightness = Constants.NOT_FOUND;

    private ResultGetLinkPlay mResultGetLinkPlay;

    private final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private final int DELAY_TO_GET_LIVE_INFORMATION = 15000;

    //chromecast https://github.com/DroidsOnRoids/Casty
    private UizaMediaRouteButton uizaMediaRouteButton;
    private RelativeLayout rlChromeCast;
    private ImageButtonWithSize ibsCast;

    private boolean isDisplayPortrait;//display with 9:16 ratio(portrait screen like YUP)

    public boolean isDisplayPortrait() {
        return isDisplayPortrait;
    }

    public void setDisplayPortrait(boolean isDisplayPortrait) {
        this.isDisplayPortrait = isDisplayPortrait;
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
    }

    private boolean isAutoStart = true;

    /*
     **set video auto start
     * true auto start
     * false stop before
     */
    public void setAutoStart(boolean isAutoStart) {
        this.isAutoStart = isAutoStart;
        updateUIButtonPlayPauseDependOnIsAutoStart();
    }

    public boolean isAutoStart() {
        return isAutoStart;
    }

    private void updateUIButtonPlayPauseDependOnIsAutoStart() {
        //If auto start true, show button play and gone button pause
        //if not, gone button play and show button pause
        if (isAutoStart) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(VISIBLE);
            }
        } else {
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(VISIBLE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(GONE);
            }
        }
    }

    private boolean isHasError;

    private void handleError(Exception e) {
        //TODO if has error, should clear all variable, flag to default?
        if (e == null) {
            return;
        }
        if (isHasError) {
            LLog.e(TAG, "handleError isHasError=true -> return");
            return;
        }
        LLog.e(TAG, "handleError " + e.toString());
        isHasError = true;
        if (uizaCallback != null) {
            uizaCallback.onError(e);
        }
        UZData.getInstance().setSettingPlayer(false);
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
    private String entityId;
    private boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
    private int valuePlayerControllerTimeout = 8000;

    /*
     **set time out player controller
     */
    public void setValuePlayerControllerTimeout(int valuePlayerControllerTimeout) {
        this.valuePlayerControllerTimeout = valuePlayerControllerTimeout;
    }

    protected void init(@NonNull String entityId, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed, boolean isClearDataPlaylistFolder) {
        LLog.d(TAG, "*****NEW SESSION**********************************************************************************************************************************");
        LLog.d(TAG, "entityId " + entityId);
        if (isClearDataPlaylistFolder) {
            //LLog.d(TAG, "xxxxxxxxxxxxxx clearDataForPlaylistFolder");
            UZData.getInstance().clearDataForPlaylistFolder();
        } else {
            //LLog.d(TAG, "xxxxxxxxxxxxxx !clearDataForPlaylistFolder");
        }
        if (entityId == null) {
            //do nothing
            //Case này được gọi từ pip
        } else {
            UZData.getInstance().clearDataForEntity();
        }
        if (UZData.getInstance().isPlayWithPlaylistFolder()) {
            setVisibilityOfPlaylistFolderController(View.VISIBLE);
        } else {
            setVisibilityOfPlaylistFolderController(View.GONE);
        }
        isCalledApiGetDetailEntity = false;
        isCalledAPIGetUrlIMAAdTag = false;
        isCalledAPIGetTokenStreaming = false;
        urlIMAAd = null;
        tokenStreaming = null;
        this.entityId = entityId;
        this.isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed = isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed;
        UZData.getInstance().setSettingPlayer(true);
        isHasError = false;
        hideLayoutMsg();
        setControllerShowTimeoutMs(valuePlayerControllerTimeout);
        //called api paralle here
        callAPIGetDetailEntity();
        callAPIGetUrlIMAAdTag();
        callAPIGetTokenStreaming();
    }

    //TODO reset these flags
    private boolean isCalledApiGetDetailEntity;
    private boolean isCalledAPIGetUrlIMAAdTag;
    private boolean isCalledAPIGetTokenStreaming;

    private void callAPIGetDetailEntity() {
        //Nếu đã tồn tại Data rồi thì nó được gọi từ pip, mình ko cần phải call api lấy detail entity làm gì nữa
        if (UZData.getInstance().getData() == null) {
            //LLog.d(TAG, "init UZData.getInstance().getData() == null -> call api lấy detail entity if");
            UZUtil.getDetailEntity((BaseActivity) activity, entityId, new UZUtil.Callback() {
                @Override
                public void onSuccess(Data d) {
                    //LLog.d(TAG, "init getDetailEntity onSuccess: " + gson.toJson(d));
                    LLog.d(TAG, "callAPIGetDetailEntity onSuccess -> handleGetDetailEntityDone");
                    isCalledApiGetDetailEntity = true;
                    //save current data
                    UZData.getInstance().setData(d);
                    if (!UZUtil.getClickedPip(activity)) {
                        setVideoCover();
                    }
                    handleDataCallAPI();
                }

                @Override
                public void onError(Throwable e) {
                    LLog.e(TAG, "init onError " + e.toString());
                    LToast.show(activity, "init onError: " + e.getMessage());
                    UZData.getInstance().setSettingPlayer(false);
                }
            });
        } else {
            //init player khi user click vào fullscreen của floating view (pic)
            LLog.d(TAG, "init UZData.getInstance().getData() != null else");
            handleDataCallAPI();
        }
    }

    private String urlIMAAd = null;

    private void callAPIGetUrlIMAAdTag() {
        LUIUtil.setDelay(100, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                LLog.d(TAG, "callAPIGetUrlIMAAdTag success");
                isCalledAPIGetUrlIMAAdTag = true;
                //TODO remove hard code, call api
                urlIMAAd = null;
                //urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
                handleDataCallAPI();
            }
        });
    }

    private String tokenStreaming;

    private void callAPIGetTokenStreaming() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityId);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        activity.subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                //LLog.d(TAG, "callAPIGetTokenStreaming onSuccess: " + gson.toJson(result));
                if (Constants.IS_DEBUG) {
                    LToast.show(activity, "callAPIGetTokenStreaming onSuccess");
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
                tokenStreaming = result.getData().getToken();
                LLog.d(TAG, "callAPIGetTokenStreaming onSuccess: " + tokenStreaming);
                isCalledAPIGetTokenStreaming = true;
                handleDataCallAPI();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "callAPIGetTokenStreaming onFail " + e.getMessage());
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

    private void callAPIGetLinkPlay() {
        //LLog.d(TAG, "callAPIGetLinkPlay isLivestream " + isLivestream);
        if (tokenStreaming == null || tokenStreaming.isEmpty()) {
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
        RestClientV3GetLinkPlay.addAuthorization(tokenStreaming);
        UizaServiceV3 service = RestClientV3GetLinkPlay.createService(UizaServiceV3.class);
        if (isLivestream) {
            String appId = UZData.getInstance().getAppId();
            String channelName = UZData.getInstance().getChannelName();
            //LLog.d(TAG, "===================================");
            //LLog.d(TAG, "========name: " + channelName);
            //LLog.d(TAG, "========appId: " + appId);
            //LLog.d(TAG, "===================================");
            activity.subscribe(service.getLinkPlayLive(appId, channelName), new ApiSubscriber<ResultGetLinkPlay>() {
                @Override
                public void onSuccess(ResultGetLinkPlay result) {
                    if (Constants.IS_DEBUG) {
                        LToast.show(activity, "callAPIGetLinkPlay isLivestream onSuccess");
                    }
                    LLog.d(TAG, "getLinkPlayLive onSuccess: " + gson.toJson(result));
                    mResultGetLinkPlay = result;
                    checkToSetUpResouce();
                }

                @Override
                public void onFail(Throwable e) {
                    if (e == null) {
                        LLog.e(TAG, "callAPIGetLinkPlay LIVE onFail");
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
            String appId = UZData.getInstance().getAppId();
            String entityId = UZData.getInstance().getEntityId();
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
                        LToast.show(activity, "callAPIGetLinkPlay !isLivestream onSuccess");
                    }
                    //LLog.d(TAG, "getLinkPlayVOD onSuccess: " + gson.toJson(result));
                    //LLog.d(TAG, "getLinkPlayVOD onSuccess");
                    mResultGetLinkPlay = result;
                    checkToSetUpResouce();
                }

                @Override
                public void onFail(Throwable e) {
                    if (e == null) {
                        LLog.e(TAG, "callAPIGetLinkPlay VOD onFail");
                        return;
                    }
                    LLog.e(TAG, "callAPIGetLinkPlay VOD onFail " + e.getMessage());
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

    private void handleDataCallAPI() {
        //LLog.d(TAG, "______________________________handleDataCallAPI isCalledApiGetDetailEntity: " + isCalledApiGetDetailEntity + ", isCalledAPIGetUrlIMAAdTag: " + isCalledAPIGetUrlIMAAdTag + ", isCalledAPIGetTokenStreaming: " + isCalledAPIGetTokenStreaming);
        if (isCalledApiGetDetailEntity && isCalledAPIGetUrlIMAAdTag && isCalledAPIGetTokenStreaming) {
            LLog.d(TAG, "______________________________handleDataCallAPI ->>>>>>>>>>>>>>>>> READY");
            UZInput UZInput = new UZInput();
            UZInput.setData(UZData.getInstance().getData());
            UZInput.setUrlIMAAd(urlIMAAd);

            //TODO correct url thumnail, null till now
            UZInput.setUrlThumnailsPreviewSeekbar(activity.getString(loitp.core.R.string.url_thumbnails));
            //UZInput.setUrlThumnailsPreviewSeekbar(urlThumnailsPreviewSeekbar);
            //UZInput.setUrlThumnailsPreviewSeekbar(null);
            UZData.getInstance().setUizaInput(UZInput, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
            checkData();
        }
    }

    private void checkData() {
        UZData.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UZData.getInstance().getEntityId() == null || UZData.getInstance().getEntityId().isEmpty()) {
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
            UZData.getInstance().setSettingPlayer(false);
            return;
        }

        isLivestream = UZData.getInstance().isLivestream();
        //LLog.d(TAG, "isLivestream " + isLivestream);

        if (UZUtil.getClickedPip(activity)) {
            //LLog.d(TAG, "__________trackUiza getClickedPip true -> dont setDefautValueForFlagIsTracked");
        } else {
            setDefautValueForFlagIsTracked();
            //LLog.d(TAG, "__________trackUiza getClickedPip false -> setDefautValueForFlagIsTracked");
        }

        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            uizaPlayerManagerV3.release();
            mResultGetLinkPlay = null;
            resetCountTryLinkPlayError();
        }
        updateUIDependOnLivstream();
        setTitle();
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }
        callAPIGetLinkPlay();
        trackUizaEventDisplay();
        trackUizaEventPlaysRequested();
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId, final boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        init(entityId, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed, true);
    }

    /**
     * init player with entity id, ad, seekbar thumnail
     */
    public void init(@NonNull String entityId) {
        init(entityId, false, true);
    }

    /**
     * init player with metadatId (playlist/folder)
     */
    public void initPlaylistFolder(String metadataId) {
        LLog.d(TAG, "initPlaylistFolder metadataId " + metadataId);
        if (metadataId == null) {
            //Được gọi initPlaylistFolder nếu click fullscreen từ pip
            //do pass metadataId null
        } else {
            UZData.getInstance().clearDataForPlaylistFolder();
        }
        isHasError = false;
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
            LLog.d(TAG, "tryNextLinkPlay isLivestream true -> try to replay = count " + countTryLinkPlayError);
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
        checkToSetUpResouce();
    }

    //khi call api callAPIGetLinkPlay nhung json tra ve ko co data
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
                    UZData.getInstance().setSettingPlayer(false);
                    uizaCallback.isInitResult(false, false, null, null);

                    //uizaCallback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }

            @Override
            public void onCancel() {
                LLog.e(TAG, "handleErrorNoData onCancel");
                if (uizaCallback != null) {
                    UZData.getInstance().setSettingPlayer(false);
                    uizaCallback.isInitResult(false, false, null, null);
                    //uizaCallback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }
        });
    }

    private void checkToSetUpResouce() {
        LLog.d(TAG, "checkToSetUpResouce isResultGetLinkPlayDone");
        if (mResultGetLinkPlay != null && UZData.getInstance().getData() != null) {
            //LLog.d(TAG, "checkToSetUpResouce if");
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
                LLog.e(TAG, "checkToSetUpResouce listLinkPlay == null || listLinkPlay.isEmpty()");
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
                    //LLog.d(TAG, "checkToSetUpResouce else err_no_internet");
                    showTvMsg(activity.getString(R.string.err_no_internet));
                }
                return;
            }
            String linkPlay = listLinkPlay.get(countTryLinkPlayError);

            List<Subtitle> subtitleList = null;
            //TODO iplm v3 chua co subtitle
            //List<Subtitle> subtitleList = mResultRetrieveAnEntity.getData().get(0).getSubtitle();
            //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

            initDataSource(linkPlay, UZData.getInstance().getUrlIMAAd(), UZData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
            if (uizaCallback != null) {
                uizaCallback.isInitResult(false, true, mResultGetLinkPlay, UZData.getInstance().getData());
            }
            initUizaPlayerManagerV3();
        } else {
            //LLog.d(TAG, "checkToSetUpResouce else");
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

    protected void resetCountTryLinkPlayError() {
        countTryLinkPlayError = 0;
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            resetCountTryLinkPlayError();
            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            String urlCover = UZData.getInstance().getThumbnail() == null ? Constants.URL_IMG_THUMBNAIL : UZData.getInstance().getThumbnail();
            LLog.d(TAG, "setVideoCover urlCover " + urlCover);
            LImageUtil.load(activity, urlCover, ivVideoCover, R.drawable.background_black);
        }
    }

    protected void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            LLog.d(TAG, "removeVideoCover isFromHandleError: " + isFromHandleError);
            ivVideoCover.setVisibility(GONE);
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
        UZUtil.setClassNameOfPlayer(activity, activity.getLocalClassName());
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);

        rootView = (RelativeLayout) findViewById(R.id.root_view);
        isTablet = LDeviceUtil.isTablet(activity);
        //LLog.d(TAG, "onCreate isTablet " + isTablet);
        addPlayerView();
        findViews();
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();

        //setup chromecast
        uizaMediaRouteButton = new UizaMediaRouteButton(activity);
        if (llTop != null) {
            llTop.addView(uizaMediaRouteButton);
        }
        setUpMediaRouteButton();
        addUIChromecastLayer();
        updateUISizeThumnail();
    }

    private UZPlayerViewV1 playerView;

    private void addPlayerView() {
        playerView = null;
        //LLog.d(TAG, "addPlayerView getCurrentPlayerId: " + UZData.getInstance().getCurrentPlayerId());
        int resLayout = UZData.getInstance().getCurrentPlayerId();
        playerView = (UZPlayerViewV1) activity.getLayoutInflater().inflate(resLayout, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        playerView.setLayoutParams(lp);
        rootView.addView(playerView);
        setControllerAutoShow(false);
    }

    /*
     **Change skin via skin id resouces
     * changeSkin(R.layout.player_skin_1);
     */
    //TODO improve this func
    private boolean isRefreshFromChangeSkin;
    private long currentPositionBeforeChangeSkin;

    /*
     **change skin of player
     * return true if success
     */
    public boolean changeSkin(int skinId) {
        LLog.d(TAG, "changeSkin skinId " + skinId);
        if (activity == null || uizaPlayerManagerV3 == null) {
            return false;
        }
        UZData.getInstance().setCurrentPlayerId(skinId);
        isRefreshFromChangeSkin = true;
        rootView.removeView(playerView);
        rootView.requestLayout();
        playerView = (UZPlayerViewV1) activity.getLayoutInflater().inflate(skinId, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        playerView.setLayoutParams(lp);
        rootView.addView(playerView);
        rootView.requestLayout();

        findViews();
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
        updateUIEachSkin();
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        //setup chromecast
        uizaMediaRouteButton = new UizaMediaRouteButton(activity);
        if (llTop != null) {
            llTop.addView(uizaMediaRouteButton);
        }
        setUpMediaRouteButton();
        addUIChromecastLayer();
        //uizaPlayerManagerV3.pauseVideo();
        LLog.d(TAG, "changeSkin getCurrentPosition " + uizaPlayerManagerV3.getCurrentPosition());
        currentPositionBeforeChangeSkin = uizaPlayerManagerV3.getCurrentPosition();

        uizaPlayerManagerV3.release();
        updateUIDependOnLivstream();
        setTitle();
        checkToSetUpResouce();
        setColorAllViewsEnable(colorAllViewsEnable);
        updateUISizeThumnail();
        if (uizaCallback != null) {
            uizaCallback.onSkinChange();
        }
        return true;
    }

    private void updateUIEachSkin() {
        //LLog.d(TAG, "updateUIEachSkin " + UZData.getInstance().getCurrentPlayerId());
        int currentPlayerId = UZData.getInstance().getCurrentPlayerId();
        if (currentPlayerId == R.layout.player_skin_2 || currentPlayerId == R.layout.player_skin_3) {
            //LLog.d(TAG, "updateUIEachSkin player_skin_2 || player_skin_3 -> edit size of ibPlayIcon ibPauseIcon");
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
        } else {
            //LLog.d(TAG, "updateUIEachSkin !player_skin_2 || !player_skin_3");
        }
    }

    private void updateUIPositionOfProgressBar() {
        //LLog.d(TAG, "updateUIPositionOfProgressBar set progressBar center in parent");
        if (playerView == null || progressBar == null) {
            return;
        }
        playerView.post(new Runnable() {
            @Override
            public void run() {
                int marginL = playerView.getMeasuredWidth() / 2 - progressBar.getMeasuredWidth() / 2;
                int marginT = playerView.getMeasuredHeight() / 2 - progressBar.getMeasuredHeight() / 2;
                //LLog.d(TAG, "updateUIPositionOfProgressBar " + marginL + "x" + marginT);
                LUIUtil.setMarginPx(progressBar, marginL, marginT, 0, 0);
            }
        });
    }

    private void findViews() {
        //LLog.d(TAG, "findViews");
        rlMsg = (RelativeLayout) findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        if (tvMsg != null) {
            LUIUtil.setTextShadow(tvMsg, Color.BLACK);
        }
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        llMid = (RelativeLayout) findViewById(R.id.ll_mid);
        llMidSub = (View) findViewById(R.id.ll_mid_sub);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        playerView = findViewById(R.id.player_view);

        updateUIPositionOfProgressBar();

        rlTimeBar = playerView.findViewById(R.id.rl_time_bar);
        uzTimebar = playerView.findViewById(R.id.exo_progress);
        previewFrameLayout = playerView.findViewById(R.id.previewFrameLayout);
        if (uzTimebar != null) {
            uzTimebar.addOnPreviewChangeListener(this);
        }
        ivThumbnail = (ImageView) playerView.findViewById(R.id.image_view_thumnail);

        tvPosition = (TextViewWithSize) playerView.findViewById(R.id.exo_position);
        tvDuration = (TextViewWithSize) playerView.findViewById(R.id.exo_duration);

        ibFullscreenIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) playerView.findViewById(R.id.tv_title);
        ibPauseIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_pause_uiza);
        ibPlayIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_play_uiza);//If auto start true, show button play and gone button pause
        if (ibPlayIcon != null) {
            ibPlayIcon.setVisibility(GONE);
        }
        ibReplayIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_replay_uiza);
        ibRewIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_rew);
        ibFfwdIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_ffwd);
        ibBackScreenIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_back_screen);
        ibVolumeIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_volume);
        ibSettingIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_setting);
        ibCcIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_cc);
        ibPlaylistRelationIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_playlist_relation);
        ibPlaylistFolderIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_playlist_folder);
        ibHearingIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_hearing);

        //TODO ibHearingIcon works fine, but QC dont want to show it, fuck QC team
        if (ibHearingIcon != null) {
            ibHearingIcon.setVisibility(GONE);
        }

        ibPictureInPictureIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_picture_in_picture);
        ibShareIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_share);
        ibSkipNextIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_skip_next);
        ibSkipPreviousIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_skip_previous);

        ivPreview = (ImageView) playerView.findViewById(R.id.exo_iv_preview);
        seekbarVolume = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_volume);
        seekbarBirghtness = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_birghtness);
        LUIUtil.setColorSeekBar(seekbarVolume, Color.TRANSPARENT);
        LUIUtil.setColorSeekBar(seekbarBirghtness, Color.TRANSPARENT);

        debugLayout = findViewById(R.id.debug_layout);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);

        if (Constants.IS_DEBUG) {
            //TODO revert to VISIBLE
            debugLayout.setVisibility(View.GONE);
        } else {
            debugLayout.setVisibility(View.GONE);
        }

        rlLiveInfo = (RelativeLayout) playerView.findViewById(R.id.rl_live_info);
        tvLiveStatus = (TextView) playerView.findViewById(R.id.tv_live);
        tvLiveView = (TextView) playerView.findViewById(R.id.tv_live_view);
        tvLiveTime = (TextView) playerView.findViewById(R.id.tv_live_time);
        ivLiveView = (ImageButtonWithSize) playerView.findViewById(R.id.iv_live_view);
        ivLiveTime = (ImageButtonWithSize) playerView.findViewById(R.id.iv_live_time);

        rlEndScreen = (RelativeLayout) playerView.findViewById(R.id.rl_end_screen);
        if (rlEndScreen != null) {
            rlEndScreen.setVisibility(GONE);
        }
        tvEndScreenMsg = (TextView) playerView.findViewById(R.id.tv_end_screen_msg);
        if (tvEndScreenMsg != null) {
            LUIUtil.setTextShadow(tvEndScreenMsg, Color.BLACK);
            tvEndScreenMsg.setOnClickListener(this);
        }

        setEventForView();

        //set visinility first
        setVisibilityOfPlaylistFolderController(GONE);
    }

    private void setEventForView() {
        //onclick
        if (ibFullscreenIcon != null) {
            ibFullscreenIcon.setOnClickListener(this);
        }
        if (ibBackScreenIcon != null) {
            ibBackScreenIcon.setOnClickListener(this);
        }
        if (ibVolumeIcon != null) {
            ibVolumeIcon.setOnClickListener(this);
        }
        if (ibSettingIcon != null) {
            ibSettingIcon.setOnClickListener(this);
        }
        if (ibCcIcon != null) {
            ibCcIcon.setOnClickListener(this);
        }
        if (ibPlaylistRelationIcon != null) {
            ibPlaylistRelationIcon.setOnClickListener(this);
        }
        if (ibPlaylistFolderIcon != null) {
            ibPlaylistFolderIcon.setOnClickListener(this);
        }
        if (ibHearingIcon != null) {
            ibHearingIcon.setOnClickListener(this);
        }
        if (ibPictureInPictureIcon != null) {
            ibPictureInPictureIcon.setOnClickListener(this);
        }
        if (ibShareIcon != null) {
            ibShareIcon.setOnClickListener(this);
        }
        if (ibFfwdIcon != null) {
            ibFfwdIcon.setOnClickListener(this);
        }
        if (ibRewIcon != null) {
            ibRewIcon.setOnClickListener(this);
        }
        if (ibPlayIcon != null) {
            ibPlayIcon.setOnClickListener(this);
        }
        if (ibPauseIcon != null) {
            ibPauseIcon.setOnClickListener(this);
        }
        if (ibReplayIcon != null) {
            ibReplayIcon.setOnClickListener(this);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setOnClickListener(this);
        }
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setOnClickListener(this);
        }

        //seekbar change
        if (seekbarVolume != null) {
            seekbarVolume.setOnSeekBarChangeListener(this);
        }
        if (seekbarVolume != null) {
            seekbarBirghtness.setOnSeekBarChangeListener(this);
        }
    }

    //tự tạo layout chromecast và background đen
    private void addUIChromecastLayer() {
        //listener check state of chromecast
        CastContext castContext = CastContext.getSharedInstance(activity);
        if (castContext.getCastState() == CastState.NO_DEVICES_AVAILABLE) {
            //LLog.d(TAG, "addUIChromecastLayer setVisibility GONE");
            uizaMediaRouteButton.setVisibility(View.GONE);
        } else {
            //LLog.d(TAG, "addUIChromecastLayer setVisibility VISIBLE");
            uizaMediaRouteButton.setVisibility(View.VISIBLE);
        }
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                if (state == CastState.NO_DEVICES_AVAILABLE) {
                    //LLog.d(TAG, "addUIChromecastLayer setVisibility GONE");
                    uizaMediaRouteButton.setVisibility(View.GONE);
                } else {
                    if (uizaMediaRouteButton.getVisibility() != View.VISIBLE) {
                        //LLog.d(TAG, "addUIChromecastLayer setVisibility VISIBLE");
                        uizaMediaRouteButton.setVisibility(View.VISIBLE);
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
        ibsCast.setColorFilter(colorAllViewsEnable);
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
            if (uzTimebar != null) {
                uzTimebar.setPreviewEnabled(false);
            }
        } else {
            if (uzTimebar != null) {
                uzTimebar.setPreviewEnabled(true);
            }
        }
        if (uzTimebar != null) {
            uzTimebar.setPreviewLoader(uizaPlayerManagerV3);
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
                updateUIButtonVisibilities();
            }
        });

        //========================>>>>>start init seekbar
        isSetProgressSeekbarFirst = true;
        //set volume max in first play
        if (seekbarVolume != null) {
            seekbarVolume.setMax(100);
            setProgressSeekbar(seekbarVolume, 99);
        }
        if (ibVolumeIcon != null) {
            ibVolumeIcon.setImageResource(R.drawable.baseline_volume_up_white_48);
        }

        //set bightness max in first play
        firstBrightness = LScreenUtil.getCurrentBrightness(getContext());
        //LLog.d(TAG, "firstBrightness " + firstBrightness);
        if (seekbarBirghtness != null) {
            seekbarBirghtness.setMax(255);
            setProgressSeekbar(seekbarBirghtness, firstBrightness);
        }
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
        UZTrackingUtil.clearAllValues(activity);
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
            if (UZTrackingUtil.isTrackedEventTypeView(activity)) {
                //da track roi ko can track nua
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIEW), new UZTrackingUtil.UizaTrackingCallback() {
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
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought100 again");
                isTracked100 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, "100", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought75 again");
                isTracked75 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, "75", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought50 again");
                isTracked50 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, "50", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
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
                LLog.d(TAG, "No need to isTrackedEventTypePlayThrought25 again");
                isTracked25 = true;
            } else {
                callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, "25", Constants.EVENT_TYPE_PLAY_THROUGHT), new UZTrackingUtil.UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(activity, true);
                    }
                });
            }
        }
    }

    protected void onStateReadyFirst() {
        LLog.d(TAG, "onStateReadyFirst");
        if (UZUtil.getClickedPip(activity)) {
            LLog.d(TAG, "getClickedPip true -> setPlayWhenReady true");
            uizaPlayerManagerV3.getPlayer().setPlayWhenReady(true);
        }
        if (uizaCallback != null) {
            //LLog.d(TAG, "onStateReadyFirst ===> isInitResult");
            uizaCallback.isInitResult(true, true, mResultGetLinkPlay, UZData.getInstance().getData());
        }
        if (isCastingChromecast) {
            //LLog.d(TAG, "onStateReadyFirst init new play check isCastingChromecast: " + isCastingChromecast);
            lastCurrentPosition = 0;
            handleConnectedChromecast();
            showController();
        }
        trackUizaEventVideoStarts();
        UZData.getInstance().setSettingPlayer(false);
    }

    public void setProgressSeekbar(final VerticalSeekBar verticalSeekBar, final int progressSeekbar) {
        if (verticalSeekBar == null) {
            return;
        }
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
        LLog.d(TAG, "onDestroy");
        if (firstBrightness != Constants.NOT_FOUND) {
            //LLog.d(TAG, "onDestroy setBrightness " + firstBrightness);
            LScreenUtil.setBrightness(getContext(), firstBrightness);
        }
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.release();
        }
        UZData.getInstance().setSettingPlayer(false);
        UZData.getInstance().clearUizaInputList();
        //UZData.getInstance().clearDataForPlaylistFolder();
        //UZData.getInstance().clearDataForEntity();
        LDialogUtil.clearAll();
        activityIsPausing = true;
        isCastingChromecast = false;
        isCastPlayerPlayingFirst = false;
        //LLog.d(TAG, "onDestroy -> set activityIsPausing = true");
    }

    public void onResume() {
        if (isCastingChromecast) {
            LLog.d(TAG, "onResume isCastingChromecast true => return");
            return;
        }
        //LLog.d(TAG, "onResume getUZInputList().size " + UZData.getInstance().getUZInputList().size());
        activityIsPausing = false;
        /*if (isExoShareClicked) {
            LLog.d(TAG, "onResume if");
            isExoShareClicked = false;
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.resumeVideo();
            }
        } else {
            LLog.d(TAG, "onResume else initUizaPlayerManagerV3");
            //initUizaPlayerManagerV3();
        }*/
        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "onResume uizaPlayerManagerV3 != null -> resumeVideo");
            uizaPlayerManagerV3.resumeVideo();
        } else {
            //LLog.d(TAG, "onResume uizaPlayerManagerV3 == null -> do nothing");
        }
    }

    private void initUizaPlayerManagerV3() {
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.init();
            if (UZUtil.getClickedPip(activity) && !UZData.getInstance().isPlayWithPlaylistFolder()) {
                //LLog.d(TAG, "initUizaPlayerManagerV3 setPlayWhenReady false ");
                uizaPlayerManagerV3.getPlayer().setPlayWhenReady(false);
            } else {
                //LLog.d(TAG, "initUizaPlayerManagerV3 do nothing");
                if (isRefreshFromChangeSkin) {
                    uizaPlayerManagerV3.seekTo(currentPositionBeforeChangeSkin);
                    isRefreshFromChangeSkin = false;
                    currentPositionBeforeChangeSkin = 0;
                }
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
        /*if (isExoShareClicked) {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.pauseVideo();
            }
        } else {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.reset();
            }
        }*/
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.pauseVideo();
        }
    }

    public void onStart() {
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStartPreview(PreviewView previewView, int progress) {
        //LLog.d(TAG, "PreviewView onStartPreview");
        /*if (uizaPlayerManagerV3 != null && uizaPlayerManagerV3.getPlayer() != null) {
            LLog.d(TAG, "PreviewView onStartPreview getPlaybackState() " + uizaPlayerManagerV3.getPlayer().getPlaybackState());
        }*/
    }

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        LLog.d(TAG, "PreviewView onStopPreview");
        /*if (uizaPlayerManagerV3 != null && uizaPlayerManagerV3.getPlayer() != null) {
            LLog.d(TAG, "PreviewView onStopPreview getPlaybackState() " + uizaPlayerManagerV3.getPlayer().getPlaybackState());
        }*/
        uizaPlayerManagerV3.seekTo(progress);
        uizaPlayerManagerV3.resumeVideo();
        isOnPlayerEnded = false;
        updateUIEndScreen();
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        //LLog.d(TAG, "PreviewView onPreview progress " + progress);
        /*if (uizaPlayerManagerV3 != null && uizaPlayerManagerV3.getPlayer() != null) {
            LLog.d(TAG, "PreviewView onPreview getPlaybackState() " + uizaPlayerManagerV3.getPlayer().getPlaybackState());
        }*/
        if (isCastingChromecast) {
            UZData.getInstance().getCasty().getPlayer().seek(progress);
        }
    }

    private boolean isExoVolumeClicked;

    protected void toggleScreenOritation() {
        boolean isLandToPort = LActivityUtil.toggleScreenOritation(activity);
        if (isLandToPort) {
            //do nothing
        } else {
            UZUtil.resizeLayout(rootView, llMid, ivVideoCover, false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            LAnimationUtil.play(v, Techniques.Pulse);
        } else if (v == ibFullscreenIcon) {
            toggleScreenOritation();
        } else if (v == ibBackScreenIcon) {
            handleClickBackScreen();
        } else if (v == ibVolumeIcon) {
            handleClickBtVolume();
        } else if (v == ibSettingIcon) {
            handleClickSetting();
        } else if (v == ibCcIcon) {
            handleClickCC();
        } else if (v == ibPlaylistRelationIcon) {
            handleClickPlaylistRelation();
        } else if (v == ibPlaylistFolderIcon) {
            handleClickPlaylistFolder();
        } else if (v == ibHearingIcon) {
            handleClickHearing();
        } else if (v == ibPictureInPictureIcon) {
            handleClickPictureInPicture();
        } else if (v == ibShareIcon) {
            handleClickShare();
        } else if (v.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManagerV3.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null && uizaPlayerManagerV3.getTrackSelectionHelper() != null) {
                uizaPlayerManagerV3.getTrackSelectionHelper().showSelectionDialog(activity, ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
            }
        } else if (v == rlChromeCast) {
            //dangerous to remove
            //LLog.d(TAG, "do nothing click rl_chrome_cast");
        } else if (v == ibFfwdIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                if (uizaPlayerManagerV3 != null) {
                    uizaPlayerManagerV3.seekToForward(DEFAULT_VALUE_BACKWARD_FORWARD);

                }
            }
        } else if (v == ibRewIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
            } else {
                if (uizaPlayerManagerV3 != null) {
                    uizaPlayerManagerV3.seekToBackward(DEFAULT_VALUE_BACKWARD_FORWARD);
                    isOnPlayerEnded = false;
                    updateUIEndScreen();
                }
            }
        } else if (v == ibPauseIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().pause();
            } else {
                if (uizaPlayerManagerV3 != null) {
                    uizaPlayerManagerV3.pauseVideo();
                }
            }
            ibPauseIcon.setVisibility(GONE);
            ibPlayIcon.setVisibility(VISIBLE);
        } else if (v == ibPlayIcon) {
            if (isCastingChromecast) {
                UZData.getInstance().getCasty().getPlayer().play();
            } else {
                if (uizaPlayerManagerV3 != null) {
                    uizaPlayerManagerV3.resumeVideo();
                }
            }
            ibPlayIcon.setVisibility(GONE);
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(VISIBLE);
            }
        } else if (v == ibReplayIcon) {
            replay();
        } else if (v == ibSkipNextIcon) {
            handleClickSkipNext();
        } else if (v == ibSkipPreviousIcon) {
            handleClickSkipPrevious();
        } else if (v == tvEndScreenMsg) {
            LAnimationUtil.play(v, Techniques.Pulse);
        }
    }

    //current screen is landscape or portrait
    private boolean isLandscape;

    public boolean isLandscape() {
        return isLandscape;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LScreenUtil.hideDefaultControls(activity);
                isLandscape = true;
                UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, true);
                if (isTablet) {
                    if (ibPictureInPictureIcon != null) {
                        ibPictureInPictureIcon.setVisibility(GONE);
                    }
                }
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                UZUtil.setUIFullScreenIcon(getContext(), ibFullscreenIcon, false);
                if (isTablet && !isCastingChromecast()) {
                    if (ibPictureInPictureIcon != null) {
                        ibPictureInPictureIcon.setVisibility(VISIBLE);
                    }
                }
            }
        }
        setMarginPreviewTimeBar();
        setMarginRlLiveInfo();
        updateUISizeThumnail();
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, isDisplayPortrait);
        updateUIPositionOfProgressBar();
    }

    private void updateUISizeThumnail() {
        int screenWidth = LScreenUtil.getScreenWidth();
        int widthIv = isLandscape ? screenWidth / 4 : screenWidth / 5;
        if (previewFrameLayout != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = widthIv;
            layoutParams.height = widthIv * 9 / 16;
            previewFrameLayout.setLayoutParams(layoutParams);
            previewFrameLayout.requestLayout();
        }
    }

    private void setMarginPreviewTimeBar() {
        if (uzTimebar == null) {
            return;
        }
        if (isLandscape) {
            LUIUtil.setMarginDimen(uzTimebar, 24, 0, 24, 0);
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

    private void updateUIDependOnLivstream() {
        //LLog.d(TAG, "updateUIDependOnLivstream isTablet " + isTablet);
        if (isTablet && !isCastingChromecast()) {
            if (ibPictureInPictureIcon != null) {
                ibPictureInPictureIcon.setVisibility(VISIBLE);
            }
        } else {
            if (ibPictureInPictureIcon != null) {
                ibPictureInPictureIcon.setVisibility(GONE);
            }
        }
        if (isLivestream) {
            if (ibPlaylistRelationIcon != null) {
                ibPlaylistRelationIcon.setVisibility(GONE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(GONE);
            }
            if (rlTimeBar != null) {
                rlTimeBar.setVisibility(GONE);
            }

            //TODO why set gone not work?
            //ibRewIcon.setVisibility(GONE);
            //ibFfwdIcon.setVisibility(GONE);
            changeUIVisibilitiesOfButton(ibRewIcon, false, 0);
            changeUIVisibilitiesOfButton(ibFfwdIcon, false, 0);

            if (rlLiveInfo != null) {
                rlLiveInfo.setVisibility(VISIBLE);
            }
        } else {
            //TODO ibPlaylistRelationIcon works fine, but QC wanne hide it
            if (ibPlaylistRelationIcon != null) {
                ibPlaylistRelationIcon.setVisibility(GONE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(VISIBLE);
            }
            if (rlTimeBar != null) {
                rlTimeBar.setVisibility(VISIBLE);
            }

            //TODO why set visible not work?
            //ibRewIcon.setVisibility(VISIBLE);
            //ibFfwdIcon.setVisibility(VISIBLE);
            changeUIVisibilitiesOfButton(ibRewIcon, true, R.drawable.baseline_replay_10_white_48);
            changeUIVisibilitiesOfButton(ibFfwdIcon, true, R.drawable.baseline_forward_10_white_48);

            if (rlLiveInfo != null) {
                rlLiveInfo.setVisibility(GONE);
            }
        }
    }

    //trick to gone view
    private void changeUIVisibilitiesOfButton(ImageButtonWithSize imageButtonWithSize, boolean isVisible, int res) {
        if (imageButtonWithSize == null) {
            return;
        }
        imageButtonWithSize.setClickable(isVisible);
        imageButtonWithSize.setImageResource(res);
    }

    protected void updateUIButtonVisibilities() {
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
                if (ivPreview != null) {
                    ivPreview.setVisibility(INVISIBLE);
                }
            } else {
                if (ivPreview != null) {
                    if (progress >= 66) {
                        ivPreview.setImageResource(R.drawable.baseline_volume_up_white_48);
                    } else if (progress >= 33) {
                        ivPreview.setImageResource(R.drawable.baseline_volume_down_white_48);
                    } else {
                        ivPreview.setImageResource(R.drawable.baseline_volume_mute_white_48);
                    }
                }
            }
            //LLog.d(TAG, "seekbarVolume onProgressChanged " + progress + " -> " + ((float) progress / 100));
            if (progress == 0) {
                if (ibVolumeIcon != null) {
                    ibVolumeIcon.setImageResource(R.drawable.baseline_volume_off_white_48);
                }
            } else {
                if (ibVolumeIcon != null) {
                    ibVolumeIcon.setImageResource(R.drawable.baseline_volume_up_white_48);
                }
            }
            uizaPlayerManagerV3.setVolume(((float) progress / 100));
            if (isExoVolumeClicked) {
                isExoVolumeClicked = false;
            }
        } else if (seekBar == seekbarBirghtness) {
            //LLog.d(TAG, "seekbarBirghtness onProgressChanged " + progress);
            if (isSetProgressSeekbarFirst) {
                if (ivPreview != null) {
                    ivPreview.setVisibility(INVISIBLE);
                }
            } else {
                if (ivPreview != null) {
                    if (progress >= 210) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_7_black_48dp);
                    } else if (progress >= 175) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_6_black_48dp);
                    } else if (progress >= 140) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_5_black_48dp);
                    } else if (progress >= 105) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_4_black_48dp);
                    } else if (progress >= 70) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_3_black_48dp);
                    } else if (progress >= 35) {
                        ivPreview.setImageResource(R.drawable.ic_brightness_2_black_48dp);
                    } else {
                        ivPreview.setImageResource(R.drawable.ic_brightness_1_black_48dp);
                    }
                }
            }
            //LLog.d(TAG, "onProgressChanged setBrightness " + progress);
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
        if (ivPreview != null) {
            ivPreview.setVisibility(VISIBLE);
        }
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
        if (ivPreview != null) {
            ivPreview.setVisibility(INVISIBLE);
        }
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

    private UizaCallback uizaCallback;

    private void callAPITrackUiza(final UizaTracking uizaTracking, final UZTrackingUtil.UizaTrackingCallback uizaTrackingCallback) {
        //LLog.d(TAG, "------------------------>callAPITrackUiza noPiP getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
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
                LLog.e(TAG, "callAPITrackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
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
                        hideLayoutMsg();
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
            //FUZVideoServiceV1 trước khi hủy đã gửi position của pip tới đây
            //Nhận được vị trí từ FloatingUizaVideoServiceV3 rồi seek tới vị trí này
            //LLog.d(TAG, "seek to: " + ((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.seekTo(((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            }
        }
    }

    //set timeout controller
    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        playerView.setControllerShowTimeoutMs(controllerShowTimeoutMs);
    }

    public void setControllerAutoShow(boolean isAutoShow) {
        playerView.setControllerAutoShow(isAutoShow);
    }

    public void showController() {
        if (playerView != null) {
            playerView.showController();
        }
    }

    public boolean isPlayerControllerShowing() {
        if (playerView != null) {
            return playerView.isControllerVisible();
        }
        return false;
    }

    public void hideController() {
        if (isCastingChromecast) {
            //dont hide if is casting chromecast
        } else {
            if (playerView != null) {
                playerView.hideController();
            }
        }
    }

    public void hideControllerOnTouch(boolean isHide) {
        playerView.setControllerHideOnTouch(isHide);
    }

    private void showTvMsg(String msg) {
        tvMsg.setText(msg);
        showLayoutMsg();
    }

    protected void showLayoutMsg() {
        if (rlMsg.getVisibility() != VISIBLE) {
            rlMsg.setVisibility(VISIBLE);
        }
        hideController();
    }

    protected void hideLayoutMsg() {
        if (rlMsg.getVisibility() != GONE) {
            rlMsg.setVisibility(GONE);
        }
    }

    private void callAPIUpdateLiveInfoCurrentView(final int durationDelay) {
        if (!isLivestream) {
            return;
        }
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView " + System.currentTimeMillis());
                if (!isLivestream) {
                    return;
                }
                if (uizaPlayerManagerV3 != null && playerView != null && (playerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    //LLog.d(TAG, "callAPIUpdateLiveInfoCurrentView isShowing");
                    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
                    String id = UZData.getInstance().getEntityId();
                    activity.subscribe(service.getViewALiveFeed(id), new ApiSubscriber<ResultGetViewALiveFeed>() {
                        @Override
                        public void onSuccess(ResultGetViewALiveFeed result) {
                            if (Constants.IS_DEBUG) {
                                LToast.show(activity, "callAPIUpdateLiveInfoCurrentView onSuccess");
                            }
                            //LLog.d(TAG, "getViewALiveFeed onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null) {
                                if (tvLiveView != null) {
                                    tvLiveView.setText(result.getData().getWatchnow() + "");
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
                    String entityId = UZData.getInstance().getEntityId();
                    String feedId = UZData.getInstance().getLastFeedId();
                    activity.subscribe(service.getTimeStartLive(entityId, feedId), new ApiSubscriber<ResultTimeStartLive>() {
                        @Override
                        public void onSuccess(ResultTimeStartLive result) {
                            if (Constants.IS_DEBUG) {
                                LToast.show(activity, "callAPIUpdateLiveInfoTimeStartLive onSuccess");
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
                                if (tvLiveTime != null) {
                                    tvLiveTime.setText(s);
                                }
                            }
                            callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            LLog.e(TAG, "getTimeStartLive onFail " + e.getMessage());
                            callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                        }
                    });
                } else {
                    callAPIUpdateLiveInfoTimeStartLive(DELAY_TO_GET_LIVE_INFORMATION);
                }
            }
        });
    }

    /*Kiểm tra xem nếu activity được tạo thành công nếu user click vào pip thì sẽ bắn 1 eventbus báo rằng đã init success
     * receiver FloatingUizaVideoServiceV3 để truyền current position */
    public void setEventBusMsgFromActivityIsInitSuccess() {
        if (UZUtil.getClickedPip(activity)) {
            ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
            msgFromActivityIsInitSuccess.setInitSuccess(true);
            ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
        }
    }

    /*START CHROMECAST*/
    @UiThread
    private void setUpMediaRouteButton() {
        UZData.getInstance().getCasty().setUpMediaRouteButton(uizaMediaRouteButton);
        UZData.getInstance().getCasty().setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
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
        if (UZData.getInstance().getData() == null || uizaPlayerManagerV3 == null || uizaPlayerManagerV3.getPlayer() == null) {
            return;
        }
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }
        //LLog.d(TAG, "playChromecast exo stop lastCurrentPosition: " + lastCurrentPosition);

        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, UZData.getInstance().getData().getDescription());
        movieMetadata.putString(MediaMetadata.KEY_TITLE, UZData.getInstance().getData().getEntityName());
        movieMetadata.addImage(new WebImage(Uri.parse(UZData.getInstance().getData().getThumbnail())));

        //TODO add subtile vtt to chromecast
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
                uizaPlayerManagerV3.getLinkPlay())
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
                    uizaPlayerManagerV3.hideProgress();
                    isCastPlayerPlayingFirst = true;
                }

                if (currentPosition > 0) {
                    uizaPlayerManagerV3.seekTo(currentPosition);
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
        LLog.d(TAG, "updateUIChromecast " + isCastingChromecast);
        if (isCastingChromecast) {
            uizaPlayerManagerV3.pauseVideo();
            uizaPlayerManagerV3.setVolume(0f);
            rlChromeCast.setVisibility(VISIBLE);
            if (ibSettingIcon != null) {
                ibSettingIcon.setVisibility(GONE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(GONE);
            }
            if (llMid != null) {
                llMid.setVisibility(GONE);
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

            //double volumeOfExoPlayer = uizaPlayerManagerV3.getVolume();
            //LLog.d(TAG, "volumeOfExoPlayer " + volumeOfExoPlayer);
            //UZData.getInstance().getCasty().setVolume(volumeOfExoPlayer);
        } else {
            uizaPlayerManagerV3.resumeVideo();
            uizaPlayerManagerV3.setVolume(0.99f);
            rlChromeCast.setVisibility(GONE);
            if (ibSettingIcon != null) {
                ibSettingIcon.setVisibility(VISIBLE);
            }
            if (ibCcIcon != null) {
                ibCcIcon.setVisibility(VISIBLE);
            }
            if (llMid != null) {
                llMid.setVisibility(VISIBLE);
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
            //uizaPlayerManagerV3.setVolume(0.99f);

            /*double volumeOfCastPlayer = UZData.getInstance().getCasty().getVolume();
            LLog.d(TAG, "volumeOfCastPlayer " + volumeOfCastPlayer);
            uizaPlayerManagerV3.setVolume((float) volumeOfCastPlayer);*/
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
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.showProgress();
        }
        if (UZData.getInstance().getDataList() == null) {
            LLog.d(TAG, "callAPIGetListAllEntity UZData.getInstance().getDataList() == null -> call api lấy data list");
            UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
            activity.subscribe(service.getListAllEntity(metadataId, pfLimit, pfPage, pfOrderBy, pfOrderType, publishToCdn), new ApiSubscriber<ResultListEntity>() {
                @Override
                public void onSuccess(ResultListEntity result) {
                    if (Constants.IS_DEBUG) {
                        LToast.show(activity, "callAPIGetListAllEntity onSuccess");
                    }
                    LLog.d(TAG, "callAPIGetListAllEntity onSuccess: " + gson.toJson(result));
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
                        if (uizaCallback != null) {
                            LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.no_data), new LDialogUtil.Callback1() {
                                @Override
                                public void onClick1() {
                                    handleError(new Exception(activity.getString(R.string.no_data)));
                                }

                                @Override
                                public void onCancel() {
                                    handleError(new Exception(activity.getString(R.string.no_data)));
                                }
                            });
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
                    LLog.d(TAG, "<<<callAPIGetListAllEntity pfPage/pfTotalPage: " + pfPage + "/" + pfTotalPage);
                    UZData.getInstance().setDataList(result.getData());
                    if (UZData.getInstance().getDataList() == null || UZData.getInstance().getDataList().isEmpty()) {
                        LLog.e(TAG, "callAPIGetListAllEntity success but no data");
                        if (uizaCallback != null) {
                            uizaCallback.onError(new Exception("callAPIGetListAllEntity success but no data"));
                        }
                        return;
                    }
                    //LLog.d(TAG, "list size: " + UZData.getInstance().getDataList().size());
                    playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
                    if (uizaPlayerManagerV3 != null) {
                        uizaPlayerManagerV3.hideProgress();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "callAPIGetListAllEntity onFail " + e.getMessage());
                    if (uizaCallback != null) {
                        uizaCallback.onError(new Exception("callAPIGetListAllEntity failed: " + e.toString()));
                    }
                    if (uizaPlayerManagerV3 != null) {
                        uizaPlayerManagerV3.hideProgress();
                    }
                }
            });
        } else {
            LLog.d(TAG, "callAPIGetListAllEntity UZData.getInstance().getDataList() != null -> không cần call api lấy data list nữa mà lấy dữ liệu có sẵn play luôn");
            //LLog.d(TAG, "list size: " + UZData.getInstance().getDataList().size());
            playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList());
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.hideProgress();
            }
        }
    }

    protected boolean isPlayPlaylistFolder() {
        if (UZData.getInstance().getDataList() == null) {
            return false;
        }
        return true;
    }

    private void playPlaylistPosition(int position) {
        if (UZData.getInstance().getDataList() == null || position < 0 || position > UZData.getInstance().getDataList().size() - 1) {
            LLog.e(TAG, "playPlaylistPosition error: incorrect position");
            return;
        }

        //update UI for skip next and skip previous button
        if (position == 0) {
            if (ibSkipPreviousIcon != null) {
                ibSkipPreviousIcon.setEnabled(false);
                ibSkipPreviousIcon.setColorFilter(colorAllViewsDisable);
            }
            if (ibSkipNextIcon != null) {
                ibSkipNextIcon.setEnabled(true);
                ibSkipNextIcon.setColorFilter(colorAllViewsEnable);
            }
        } else if (position == UZData.getInstance().getDataList().size() - 1) {
            if (ibSkipPreviousIcon != null) {
                ibSkipPreviousIcon.setEnabled(true);
                ibSkipPreviousIcon.setColorFilter(colorAllViewsEnable);
            }
            if (ibSkipNextIcon != null) {
                ibSkipNextIcon.setEnabled(false);
                ibSkipNextIcon.setColorFilter(colorAllViewsDisable);
            }
        } else {
            if (ibSkipPreviousIcon != null) {
                ibSkipPreviousIcon.setEnabled(true);
                ibSkipPreviousIcon.setColorFilter(colorAllViewsEnable);
            }
            if (ibSkipNextIcon != null) {
                ibSkipNextIcon.setEnabled(true);
                ibSkipNextIcon.setColorFilter(colorAllViewsEnable);
            }
        }
        //end update UI for skip next and skip previous button

        UZData.getInstance().setCurrentPositionOfDataList(position);
        Data data = UZData.getInstance().getDataWithPositionOfDataList(position);
        if (data == null || data.getId() == null || data.getId().isEmpty()) {
            LLog.e(TAG, "playPlaylistPosition error: data null or cannot get id");
            return;
        }
        LLog.d(TAG, "-----------------------> playPlaylistPosition " + position);
        init(UZData.getInstance().getDataWithPositionOfDataList(position).getId(), false, false);
    }

    private boolean isOnPlayerEnded;
    //private TextView tvWaitingNextEntity;

    protected void onPlayerEnded() {
        LLog.d(TAG, "onPlayerEnded");
        isOnPlayerEnded = true;
        if (isPlayPlaylistFolder()) {
            hideController();
            autoSwitchNextVideo();
        } else {
            updateUIEndScreen();
        }
    }

    private void updateUIEndScreen() {
        //LLog.d(TAG, "updateUIEndScreen isOnPlayerEnded " + isOnPlayerEnded);
        if (isOnPlayerEnded) {
            if (rlEndScreen != null && tvEndScreenMsg != null) {
                rlEndScreen.setVisibility(VISIBLE);
                //TODO call api skin config to correct this text
                setTextEndscreen("This is end screen");
            }
            setVisibilityOfPlayPauseReplay(true);
            showController();
            setControllerShowTimeoutMs(0);
            hideControllerOnTouch(false);
        } else {
            if (rlEndScreen != null && tvEndScreenMsg != null) {
                rlEndScreen.setVisibility(GONE);
                setTextEndscreen("");
            }
            setVisibilityOfPlayPauseReplay(false);
            //hideController();
            setControllerShowTimeoutMs(valuePlayerControllerTimeout);
            hideControllerOnTouch(true);
        }
    }

    public void setTextEndscreen(String msg) {
        if (tvEndScreenMsg != null) {
            tvEndScreenMsg.setText(msg);
        }
    }

    private void autoSwitchNextVideo() {
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() + 1);
    }

    private void autoSwitchPreviousLinkVideo() {
        playPlaylistPosition(UZData.getInstance().getCurrentPositionOfDataList() - 1);
    }

    private void handleClickPlaylistFolder() {
        UizaDialogPlaylistFolder uizaDialogPlaylistFolder = new UizaDialogPlaylistFolder(activity, isLandscape, UZData.getInstance().getDataList(), UZData.getInstance().getCurrentPositionOfDataList(), new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(Data data, int position) {
                //LLog.d(TAG, "UizaDialogPlaylistFolder onClickItem " + gson.toJson(data));
                //currentPositionOfDataList = position;
                UZData.getInstance().setCurrentPositionOfDataList(position);
                //playPlaylistPosition(currentPositionOfDataList);
                playPlaylistPosition(position);
            }

            @Override
            public void onDismiss() {
            }
        });
        UZUtil.showUizaDialog(activity, uizaDialogPlaylistFolder);
    }

    private void setVisibilityOfPlayPauseReplay(boolean isShowReplay) {
        if (isShowReplay) {
            if (ibPlayIcon != null) {
                ibPlayIcon.setVisibility(GONE);
            }
            if (ibPauseIcon != null) {
                ibPauseIcon.setVisibility(GONE);
            }
            if (ibReplayIcon != null) {
                ibReplayIcon.setVisibility(VISIBLE);
            }
            if (ibFfwdIcon != null) {
                ibFfwdIcon.setEnabled(false);
                ibFfwdIcon.setColorFilter(colorAllViewsDisable);
            }
        } else {
            updateUIButtonPlayPauseDependOnIsAutoStart();
            if (ibReplayIcon != null) {
                ibReplayIcon.setVisibility(GONE);
            }
            if (ibFfwdIcon != null) {
                ibFfwdIcon.setEnabled(true);
                ibFfwdIcon.setColorFilter(colorAllViewsEnable);
            }
        }
    }

    private void setVisibilityOfPlaylistFolderController(int visibilityOfPlaylistFolderController) {
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

    private void handleClickSkipNext() {
        //LLog.d(TAG, "handleClickSkipNext");
        autoSwitchNextVideo();
    }

    private void handleClickSkipPrevious() {
        //LLog.d(TAG, "handleClickSkipPrevious");
        autoSwitchPreviousLinkVideo();
    }

    /*
     **replay this current content
     * return true if replay successed
     * return fail if replay failed
     */
    public boolean replay() {
        if (uizaPlayerManagerV3 == null) {
            return false;
        }
        //TODO Chỗ này đáng lẽ chỉ clear value của tracking khi đảm bảo rằng seekTo(0) true
        setDefautValueForFlagIsTracked();
        boolean result = uizaPlayerManagerV3.seekTo(0);
        if (result) {
            isOnPlayerEnded = false;
            updateUIEndScreen();
            setVisibilityOfPlaylistFolderController(View.GONE);
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
            //LLog.d(TAG, "handleClickBtVolume isCastingChromecast");
            boolean isMute = UZData.getInstance().getCasty().toggleMuteVolume();
            if (ibVolumeIcon != null) {
                if (isMute) {
                    ibVolumeIcon.setImageResource(R.drawable.ic_volume_off_black_48dp);
                } else {
                    ibVolumeIcon.setImageResource(R.drawable.baseline_volume_up_white_48);
                }
            }
        } else {
            //LLog.d(TAG, "handleClickBtVolume !isCastingChromecast");
            if (uizaPlayerManagerV3 != null) {
                isExoVolumeClicked = true;
                uizaPlayerManagerV3.toggleVolumeMute(ibVolumeIcon);
            }
        }
    }

    private void handleClickBackScreen() {
        if (!isOnPlayerEnded) {
            hideController();
        }
        if (isLandscape) {
            if (ibFullscreenIcon != null) {
                ibFullscreenIcon.performClick();
            }
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
        UZUtil.showUizaDialog(activity, uizaDialogListEntityRelation);
    }

    private void handleClickSetting() {
        View view = UZUtil.getBtVideo(debugRootView);
        if (view != null) {
            UZUtil.getBtVideo(debugRootView).performClick();
        }
    }

    private void handleClickCC() {
        if (uizaPlayerManagerV3 == null) {
            LLog.e(TAG, "Error handleClickCC uizaPlayerManagerV3 == null");
            return;
        }
        if (uizaPlayerManagerV3.getSubtitleList() == null || uizaPlayerManagerV3.getSubtitleList().isEmpty()) {
            UZDlgInfoV1 UZDlgInfoV1 = new UZDlgInfoV1(activity, activity.getString(R.string.text), activity.getString(R.string.no_caption));
            UZUtil.showUizaDialog(activity, UZDlgInfoV1);
        } else {
            View view = UZUtil.getBtText(debugRootView);
            if (view != null) {
                UZUtil.getBtText(debugRootView).performClick();
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
        LSocialUtil.share(activity, isLandscape);
    }

    /*
     ** Phát tiếp video
     */
    public void resumeVideo() {
        if (ibPlayIcon != null) {
            ibPlayIcon.performClick();
        }
    }

    /*
     ** Tạm dừng video
     */
    public void pauseVideo() {
        if (ibPauseIcon != null) {
            ibPauseIcon.performClick();
        }
    }

    public void setControllerStateCallback(UZPlayerViewV1.ControllerStateCallback controllerStateCallback) {
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
    public void setOnTouchEvent(UZPlayerViewV1.OnTouchEvent onTouchEvent) {
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
        if (ibFfwdIcon != null) {
            ibFfwdIcon.performClick();
        }
    }

    /*
     ** Seek từ vị trí hiện tại trừ đi bao nhiêu mls
     */
    public void seekToBackward(int mls) {
        setDefaultValueBackwardForward(mls);
        if (ibRewIcon != null) {
            ibRewIcon.performClick();
        }
    }

    /*
     **toggle volume on/off
     */
    public void toggleVolume() {
        if (ibVolumeIcon != null) {
            ibVolumeIcon.performClick();
        }
    }

    /*
     **toggle fullscreen
     */
    public void toggleFullscreen() {
        if (ibFullscreenIcon != null) {
            ibFullscreenIcon.performClick();
        }
    }

    /*
     **Hiển thị subtitle
     */
    public void showCCPopup() {
        if (ibCcIcon != null) {
            ibCcIcon.performClick();
        }
    }

    /*
     **Hiển thị chất lượng video
     */
    public void showHQPopup() {
        if (ibSettingIcon != null) {
            ibSettingIcon.performClick();
        }
    }

    /*
     **Hiển thị share lên mạng xã hội
     */
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
        if (isCastingChromecast() || !isTablet) {
            LLog.d(TAG, "showPip isCastingChromecast || !isTablet -> return");
        } else {
            if (ibPictureInPictureIcon != null) {
                ibPictureInPictureIcon.performClick();
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
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.performClick();
        }
    }

    /*
     ** Bỏ video hiện tại và chơi lùi lại 1 video trong playlist/folder
     */
    public void skipPreviousVideo() {
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.performClick();
        }
    }

    private void trackUizaEventVideoStarts() {
        if (UZTrackingUtil.isTrackedEventTypeVideoStarts(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIDEO_STARTS), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(activity, true);
                }
            });
        }
    }

    private void trackUizaEventDisplay() {
        if (UZTrackingUtil.isTrackedEventTypeDisplay(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_DISPLAY), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(activity, true);
                }
            });
        }
    }

    private void trackUizaEventPlaysRequested() {
        if (UZTrackingUtil.isTrackedEventTypePlaysRequested(activity)) {
            //da track roi ko can track nua
        } else {
            callAPITrackUiza(UZData.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_PLAYS_REQUESTED), new UZTrackingUtil.UizaTrackingCallback() {
                @Override
                public void onTrackingSuccess() {
                    UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(activity, true);
                }
            });
        }
    }

    private int colorAllViewsEnable = Color.WHITE;
    private int colorAllViewsDisable = Color.GRAY;

    public void setColorAllViewsEnable(int colorAllViewsEnable) {
        this.colorAllViewsEnable = colorAllViewsEnable;
        updateUIColorAllView();
    }

    public void updateUIColorAllView() {
        if (progressBar != null) {
            LUIUtil.setColorProgressBar(progressBar, colorAllViewsEnable);
        }
        if (ibBackScreenIcon != null) {
            ibBackScreenIcon.setColorFilter(colorAllViewsEnable);
        }
        if (tvTitle != null) {
            tvTitle.setTextColor(colorAllViewsEnable);
        }
        if (ibsCast != null) {
            ibsCast.setColorFilter(colorAllViewsEnable);
        }
        if (uizaMediaRouteButton != null) {
            uizaMediaRouteButton.post(new Runnable() {
                @Override
                public void run() {
                    uizaMediaRouteButton.applyTint(colorAllViewsEnable);
                }
            });
        }
        if (tvDuration != null) {
            tvDuration.setTextColor(colorAllViewsEnable);
        }
        if (tvPosition != null) {
            tvPosition.setTextColor(colorAllViewsEnable);
        }
        if (ibSkipPreviousIcon != null) {
            ibSkipPreviousIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibRewIcon != null) {
            ibRewIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibPlayIcon != null) {
            ibPlayIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibPauseIcon != null) {
            ibPauseIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibReplayIcon != null) {
            ibReplayIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibFfwdIcon != null) {
            ibFfwdIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibSkipNextIcon != null) {
            ibSkipNextIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibVolumeIcon != null) {
            ibVolumeIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibCcIcon != null) {
            ibCcIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibPlaylistRelationIcon != null) {
            ibPlaylistRelationIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibPlaylistFolderIcon != null) {
            ibPlaylistFolderIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibHearingIcon != null) {
            ibHearingIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibSettingIcon != null) {
            ibSettingIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibShareIcon != null) {
            ibShareIcon.setColorFilter(colorAllViewsEnable);
        }
        if (ibFullscreenIcon != null) {
            ibFullscreenIcon.setColorFilter(colorAllViewsEnable);
        }
        if (tvLiveStatus != null) {
            tvLiveStatus.setBackgroundResource(R.drawable.background_live);
            tvLiveStatus.setTextColor(Color.WHITE);
            LUIUtil.setTextShadow(tvLiveStatus, Color.BLACK);
        }
        if (tvLiveTime != null) {
            tvLiveTime.setTextColor(colorAllViewsEnable);
        }
        if (tvLiveView != null) {
            tvLiveView.setTextColor(colorAllViewsEnable);
        }
        if (ivLiveTime != null) {
            ivLiveTime.setColorFilter(colorAllViewsEnable);
        }
        if (ivLiveView != null) {
            ivLiveView.setColorFilter(colorAllViewsEnable);
        }
        if (tvEndScreenMsg != null) {
            tvEndScreenMsg.setTextColor(colorAllViewsEnable);
        }
        if (uzTimebar != null) {
            //uzTimebar.setPreviewColorTint(R.color.Red);
            //uzTimebar.setPreviewColorResourceTint(R.color.Red);
        }
    }

    /**
     * return player view
     */
    public PlayerView getPlayerView() {
        return playerView;
    }

    protected TextView getDebugTextView() {
        return debugTextView;
    }

    /**
     * return progress bar view
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * return thumnail imageview
     */
    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    public boolean isLivestream() {
        return isLivestream;
    }

    public boolean isTablet() {
        return isTablet;
    }

    public UizaPlayerManagerV3 getUizaPlayerManagerV3() {
        return uizaPlayerManagerV3;
    }

    public TextView getTvMsg() {
        return tvMsg;
    }

    public ImageView getIvVideoCover() {
        return ivVideoCover;
    }

    public ImageButtonWithSize getIbFullscreenIcon() {
        return ibFullscreenIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageButtonWithSize getIbPauseIcon() {
        return ibPauseIcon;
    }

    public ImageButtonWithSize getIbPlayIcon() {
        return ibPlayIcon;
    }

    public ImageButtonWithSize getIbReplayIcon() {
        return ibReplayIcon;
    }

    public ImageButtonWithSize getIbRewIcon() {
        return ibRewIcon;
    }

    public ImageButtonWithSize getIbFfwdIcon() {
        return ibFfwdIcon;
    }

    public ImageButtonWithSize getIbBackScreenIcon() {
        return ibBackScreenIcon;
    }

    public ImageButtonWithSize getIbVolumeIcon() {
        return ibVolumeIcon;
    }

    public ImageButtonWithSize getIbSettingIcon() {
        return ibSettingIcon;
    }

    public ImageButtonWithSize getIbCcIcon() {
        return ibCcIcon;
    }

    public ImageButtonWithSize getIbPlaylistRelationIcon() {
        return ibPlaylistRelationIcon;
    }

    public ImageButtonWithSize getIbPlaylistFolderIcon() {
        return ibPlaylistFolderIcon;
    }

    public ImageButtonWithSize getIbHearingIcon() {
        return ibHearingIcon;
    }

    public ImageButtonWithSize getIbPictureInPictureIcon() {
        return ibPictureInPictureIcon;
    }

    public ImageButtonWithSize getIbShareIcon() {
        return ibShareIcon;
    }

    public ImageButtonWithSize getIbSkipPreviousIcon() {
        return ibSkipPreviousIcon;
    }

    public ImageButtonWithSize getIbSkipNextIcon() {
        return ibSkipNextIcon;
    }

    public VerticalSeekBar getSeekbarVolume() {
        return seekbarVolume;
    }

    public VerticalSeekBar getSeekbarBirghtness() {
        return seekbarBirghtness;
    }

    public ImageView getIvPreview() {
        return ivPreview;
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

    public UizaMediaRouteButton getUizaMediaRouteButton() {
        return uizaMediaRouteButton;
    }

    public RelativeLayout getRlChromeCast() {
        return rlChromeCast;
    }

    public ImageButtonWithSize getIbsCast() {
        return ibsCast;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getUrlIMAAd() {
        return urlIMAAd;
    }

    public String getTokenStreaming() {
        return tokenStreaming;
    }

    public TextViewWithSize getTvPosition() {
        return tvPosition;
    }

    public TextViewWithSize getTvDuration() {
        return tvDuration;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    public ImageButtonWithSize getIvLiveTime() {
        return ivLiveTime;
    }

    public ImageButtonWithSize getIvLiveView() {
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
}
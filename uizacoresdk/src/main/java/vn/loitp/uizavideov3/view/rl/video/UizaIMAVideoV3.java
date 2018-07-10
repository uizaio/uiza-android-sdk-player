package vn.loitp.uizavideov3.view.rl.video;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
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
import vn.loitp.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.loitp.restapi.uiza.util.UizaV3Util;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.dlg.info.UizaDialogInfo;
import vn.loitp.uizavideo.view.rl.video.UizaPlayerView;
import vn.loitp.uizavideo.view.util.UizaUtil;
import vn.loitp.uizavideov3.view.dlg.listentityrelation.PlayListCallbackV3;
import vn.loitp.uizavideov3.view.dlg.listentityrelation.UizaDialogListEntityRelationV3;
import vn.loitp.uizavideov3.view.floatview.FloatingUizaVideoServiceV3;
import vn.loitp.uizavideov3.view.manager.UizaPlayerManagerV3;
import vn.loitp.uizavideov3.view.util.UizaDataV3;
import vn.loitp.views.LToast;
import vn.loitp.views.autosize.imagebuttonwithsize.ImageButtonWithSize;
import vn.loitp.views.seekbar.verticalseekbar.VerticalSeekBar;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideoV3 extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = getClass().getSimpleName();
    private BaseActivity activity;
    private boolean isLivestream;
    //TODO remove
    private Gson gson = new Gson();
    private RelativeLayout rootView;
    private UizaPlayerManagerV3 uizaPlayerManagerV3;
    private ProgressBar progressBar;
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
    private ImageButtonWithSize exoPlaylist;
    private ImageButtonWithSize exoHearing;
    private ImageButtonWithSize exoPictureInPicture;
    private ImageButtonWithSize exoShare;
    private ImageButtonWithSize exoCast;
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
    private ResultRetrieveAnEntity mResultRetrieveAnEntity;
    private boolean isResultGetLinkPlayDone;
    private boolean isResultRetrieveAnEntityDone;

    private final int DELAY_FIRST_TO_GET_LIVE_INFORMATION = 100;
    private final int DELAY_TO_GET_LIVE_INFORMATION = 15000;

    public PlayerView getPlayerView() {
        return playerView;
    }

    public TextView getDebugTextView() {
        return debugTextView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public PreviewTimeBarLayout getPreviewTimeBarLayout() {
        return previewTimeBarLayout;
    }

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
        if (callback != null) {
            callback.onError(e);
        }
    }

    public void init(Callback callback) {
        UizaDataV3.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UizaDataV3.getInstance().getUizaInputV3().getData().getId() == null || UizaDataV3.getInstance().getUizaInputV3().getData().getId().isEmpty()) {
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
            return;
        }
        isLivestream = UizaDataV3.getInstance().getUizaInputV3().isLivestream();
        LLog.d(TAG, "isLivestream " + isLivestream);
        this.callback = callback;
        if (uizaPlayerManagerV3 != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            uizaPlayerManagerV3.release();
            mResultGetLinkPlay = null;
            mResultRetrieveAnEntity = null;
            isResultGetLinkPlayDone = false;
            isResultRetrieveAnEntityDone = false;
            countTryLinkPlayError = 0;
        }
        updateUI();
        setTitle();
        setVideoCover();
        getTokenStreaming();
        getDetailEntity();

        LUIUtil.showProgressBar(progressBar);

        //ko track neu play tu clicked pip
        if (!LPref.getClickedPip(activity)) {
            //cannot delete delay below, only works after 500mls
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    //track event eventype display
                    trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_DISPLAY));

                    //track event plays_requested
                    trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_PLAYS_REQUESTED));
                }
            });
        }
    }

    private int countTryLinkPlayError = 0;

    public void tryNextLinkPlay() {
        if (isLivestream) {
            //if entity is livestreaming, dont try to next link play
            LLog.d(TAG, "tryNextLinkPlay isLivestream true -> return");
            //TODO
            /*LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.try_all_link_play_but_no_luck), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    handleError(new Exception(activity.getString(R.string.try_all_link_play_but_no_luck)));
                }

                @Override
                public void onCancel() {
                    handleError(new Exception(activity.getString(R.string.try_all_link_play_but_no_luck)));
                }
            });*/
            return;
        }
        countTryLinkPlayError++;
        if (Constants.IS_DEBUG) {
            LToast.show(activity, activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        }
        LLog.d(TAG, "tryNextLinkPlay countTryLinkPlayError " + countTryLinkPlayError);
        uizaPlayerManagerV3.release();
        checkToSetUp();
    }

    //khi call api getLinkPlay nhung json tra ve ko co data
    //se co gang choi video da play gan nhat
    //neu co thi se play
    //khong co thi bao loi
    private void handleErrorNoData() {
        LLog.e(TAG, "handleErrorNoData");
        removeVideoCover(true);
        LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.has_no_linkplay), new LDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                LLog.e(TAG, "handleErrorNoData onClick1");
                if (callback != null) {
                    UizaDataV3.getInstance().setSettingPlayer(false);
                    callback.isInitResult(false, null, null);

                    //callback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }

            @Override
            public void onCancel() {
                LLog.e(TAG, "handleErrorNoData onCancel");
                if (callback != null) {
                    UizaDataV3.getInstance().setSettingPlayer(false);
                    callback.isInitResult(false, null, null);

                    //callback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }
        });
    }

    private void checkToSetUp() {
        if (isResultGetLinkPlayDone && isResultRetrieveAnEntityDone) {
            if (mResultGetLinkPlay != null && mResultRetrieveAnEntity != null) {
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
                        LLog.d(TAG, "checkToSetUp else err_no_internet");
                        showTvMsg(activity.getString(R.string.err_no_internet));
                    }
                    return;
                }
                String linkPlay = listLinkPlay.get(countTryLinkPlayError);

                List<Subtitle> subtitleList = null;
                //TODO iplm v3 chua co subtitle
                //List<Subtitle> subtitleList = mResultRetrieveAnEntity.getData().get(0).getSubtitle();
                //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

                initData(linkPlay, UizaDataV3.getInstance().getUizaInputV3().getUrlIMAAd(), UizaDataV3.getInstance().getUizaInputV3().getUrlThumnailsPreviewSeekbar(), subtitleList);
                onResume();
            } else {
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

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            countTryLinkPlayError = 0;

            ivVideoCover.setVisibility(VISIBLE);
            ivVideoCover.invalidate();
            LImageUtil.load(activity, UizaDataV3.getInstance().getUizaInputV3().getData().getThumbnail() == null ? Constants.URL_IMG_THUMBNAIL : UizaDataV3.getInstance().getUizaInputV3().getData().getThumbnail(), ivVideoCover, R.drawable.uiza);
        }
    }

    public void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "--------removeVideoCover isFromHandleError: " + isFromHandleError);
            ivVideoCover.setVisibility(GONE);
            tvLiveTime.setText("-");
            tvLiveView.setText("-");
            updateLiveInfoCurrentView(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
            updateLiveInfoTimeStartLive(DELAY_FIRST_TO_GET_LIVE_INFORMATION);
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
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_rl, this);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        addPlayerView();
        findViews();
        UizaUtil.resizeLayout(rootView, llMid, ivVideoCover);
        updateUIEachSkin();
        setMarginPreviewTimeBarLayout();
        setMarginRlLiveInfo();
    }

    private UizaPlayerView playerView;

    private void addPlayerView() {
        //PlayerView playerView = null;
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

    private void findViews() {
        rlMsg = (RelativeLayout) findViewById(R.id.rl_msg);
        rlMsg.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        LUIUtil.setTextShadow(tvMsg);
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llMid = (RelativeLayout) findViewById(R.id.ll_mid);
        llMidSub = (View) findViewById(R.id.ll_mid_sub);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        playerView = findViewById(R.id.player_view);

        rlTimeBar = playerView.findViewById(R.id.rl_time_bar);
        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        previewTimeBarLayout = playerView.findViewById(R.id.previewSeekBarLayout);
        previewTimeBarLayout.setTintColorResource(R.color.colorPrimary);
        previewTimeBar.addOnPreviewChangeListener(this);
        ivThumbnail = (ImageView) playerView.findViewById(R.id.image_view_thumnail);

        exoFullscreenIcon = (ImageButtonWithSize) playerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) playerView.findViewById(R.id.tv_title);
        exoPause = (ImageButtonWithSize) playerView.findViewById(R.id.exo_pause);
        exoPlay = (ImageButtonWithSize) playerView.findViewById(R.id.exo_play);
        exoRew = (ImageButtonWithSize) playerView.findViewById(R.id.exo_rew);
        exoFfwd = (ImageButtonWithSize) playerView.findViewById(R.id.exo_ffwd);
        exoBackScreen = (ImageButtonWithSize) playerView.findViewById(R.id.exo_back_screen);
        exoVolume = (ImageButtonWithSize) playerView.findViewById(R.id.exo_volume);
        exoSetting = (ImageButtonWithSize) playerView.findViewById(R.id.exo_setting);
        exoCc = (ImageButtonWithSize) playerView.findViewById(R.id.exo_cc);
        exoPlaylist = (ImageButtonWithSize) playerView.findViewById(R.id.exo_playlist);
        exoHearing = (ImageButtonWithSize) playerView.findViewById(R.id.exo_hearing);

        //TODO exoHearing works fine, but QC dont want to show it, fuck QC team
        exoHearing.setVisibility(GONE);

        exoPictureInPicture = (ImageButtonWithSize) playerView.findViewById(R.id.exo_picture_in_picture);
        exoShare = (ImageButtonWithSize) playerView.findViewById(R.id.exo_share);
        exoCast = (ImageButtonWithSize) playerView.findViewById(R.id.exo_cast);

        exoIvPreview = (ImageView) playerView.findViewById(R.id.exo_iv_preview);
        seekbarVolume = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_volume);
        seekbarBirghtness = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_birghtness);
        LUIUtil.setColorSeekBar(seekbarVolume, Color.TRANSPARENT);
        LUIUtil.setColorSeekBar(seekbarBirghtness, Color.TRANSPARENT);
        //ivVolumeSeekbar = (ImageView) playerView.findViewById(R.id.exo_volume_seekbar);
        //ivBirghtnessSeekbar = (ImageView) playerView.findViewById(R.id.exo_birghtness_seekbar);

        debugLayout = findViewById(R.id.debug_layout);
        debugRootView = findViewById(R.id.controls_root);
        debugTextView = findViewById(R.id.debug_text_view);

        if (Constants.IS_DEBUG) {
            debugLayout.setVisibility(View.VISIBLE);
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
        exoPlaylist.setOnClickListener(this);
        exoHearing.setOnClickListener(this);
        exoPictureInPicture.setOnClickListener(this);
        exoShare.setOnClickListener(this);

        //seekbar change
        seekbarVolume.setOnSeekBarChangeListener(this);
        seekbarBirghtness.setOnSeekBarChangeListener(this);
    }

    private ProgressCallback progressCallback;

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    private void initData(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        LLog.d(TAG, "initData linkPlay " + linkPlay);
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
        exoVolume.setImageResource(R.drawable.ic_volume_up_black_48dp);

        //set bightness max in first play
        firstBrightness = LScreenUtil.getCurrentBrightness(getContext()) * 100 / 255 + 1;
        //LLog.d(TAG, "firstBrightness " + firstBrightness);
        seekbarBirghtness.setMax(100);
        setProgressSeekbar(seekbarBirghtness, firstBrightness);
        isSetProgressSeekbarFirst = false;
        //========================<<<<<end init seekbar
    }

    private boolean isSetProgressSeekbarFirst;
    private int oldPercent = Constants.NOT_FOUND;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s)
        if (s == 5) {
            //LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "25", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "50", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "75", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, "100", Constants.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true, mResultGetLinkPlay, mResultRetrieveAnEntity);
        }
        //ko track neu play tu clicked pip
        if (!LPref.getClickedPip(activity)) {
            //track event video_starts
            trackUiza(UizaDataV3.getInstance().createTrackingInputV3(activity, Constants.EVENT_TYPE_VIDEO_STARTS));
        }
        UizaDataV3.getInstance().setSettingPlayer(false);
    }

    public void setProgressSeekbar(final VerticalSeekBar verticalSeekBar, final int progressSeekbar) {
        verticalSeekBar.setProgress(progressSeekbar);
        //LLog.d(TAG, "setProgressSeekbar " + progressSeekbar);
    }

    public void setProgressVolumeSeekbar(int progress) {
        setProgressSeekbar(seekbarVolume, progress);
    }

    public void onDestroy() {
        if (firstBrightness != Constants.NOT_FOUND) {
            LScreenUtil.setBrightness(getContext(), firstBrightness);
        }
        if (uizaPlayerManagerV3 != null) {
            uizaPlayerManagerV3.release();
        }
        UizaDataV3.getInstance().setSettingPlayer(false);
        LDialogUtil.clearAll();
        activityIsPausing = true;
        //LLog.d(TAG, "onDestroy -> set activityIsPausing = true");
    }

    public void onResume() {
        activityIsPausing = false;
        if (isExoShareClicked) {
            isExoShareClicked = false;

            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.resumeVideo();
            }
        } else {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.init();
                if (isCalledFromConnectionEventBus) {
                    uizaPlayerManagerV3.setRunnable();
                    isCalledFromConnectionEventBus = false;
                }
            }
        }
    }

    private boolean activityIsPausing = false;

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
    }

    private boolean isExoShareClicked;

    @Override
    public void onClick(View v) {
        if (v == rlMsg) {
            //do nothing
            //LLog.d(TAG, "onClick llMsg");
        } else if (v == exoFullscreenIcon) {
            LActivityUtil.toggleScreenOritation(activity);
        } else if (v == exoBackScreen) {
            if (isLandscape) {
                exoFullscreenIcon.performClick();
            } else {
                if (callback != null) {
                    callback.onClickBack();
                }
            }
        } else if (v == exoVolume) {
            if (uizaPlayerManagerV3 != null) {
                uizaPlayerManagerV3.toggleVolumeMute(exoVolume);
            }
        } else if (v == exoSetting) {
            View view = UizaUtil.getBtVideo(debugRootView);
            if (view != null) {
                UizaUtil.getBtVideo(debugRootView).performClick();
            }
        } else if (v == exoCc) {
            if (uizaPlayerManagerV3.getSubtitleList() == null || uizaPlayerManagerV3.getSubtitleList().isEmpty()) {
                UizaDialogInfo uizaDialogInfo = new UizaDialogInfo(activity, activity.getString(R.string.text), activity.getString(R.string.no_caption));
                UizaUtil.showUizaDialog(activity, uizaDialogInfo);
            } else {
                View view = UizaUtil.getBtText(debugRootView);
                if (view != null) {
                    UizaUtil.getBtText(debugRootView).performClick();
                }
            }
        } else if (v == exoPlaylist) {
            UizaDialogListEntityRelationV3 uizaDialogListEntityRelation = new UizaDialogListEntityRelationV3(activity, isLandscape, new PlayListCallbackV3() {
                @Override
                public void onClickItem(Item item, int position) {
                    //LLog.d(TAG, "onClickItem " + gson.toJson(item));
                    if (callback != null) {
                        callback.onClickListEntityRelation(item, position);
                    }
                }

                @Override
                public void onDismiss() {
                    //do nothing
                }
            });
            UizaUtil.showUizaDialog(activity, uizaDialogListEntityRelation);
        } else if (v == exoHearing) {
            View view = UizaUtil.getBtAudio(debugRootView);
            if (view != null) {
                UizaUtil.getBtAudio(debugRootView).performClick();
            }
        } else if (v == exoPictureInPicture) {
            clickPiP();
        } else if (v == exoShare) {
            LSocialUtil.share(activity, isLandscape);
            isExoShareClicked = true;
        } else if (v.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManagerV3.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null && uizaPlayerManagerV3.getTrackSelectionHelper() != null) {
                uizaPlayerManagerV3.getTrackSelectionHelper().showSelectionDialog(activity, ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
            }
        }
    }

    private boolean isLandscape;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LScreenUtil.hideDefaultControls(activity);
                isLandscape = true;
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, true);
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, false);
            }
        }
        setMarginPreviewTimeBarLayout();
        setMarginRlLiveInfo();
        UizaUtil.resizeLayout(rootView, llMid, ivVideoCover);
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
        tvTitle.setText(UizaDataV3.getInstance().getUizaInputV3().getData().getName());
        LUIUtil.setTextShadow(tvTitle);
    }

    private void updateUI() {
        boolean isTablet = LDeviceUtil.isTablet(activity);
        LLog.d(TAG, "updateUI isTablet " + isTablet);
        if (isTablet) {
            exoPictureInPicture.setVisibility(VISIBLE);
        } else {
            exoPictureInPicture.setVisibility(GONE);
        }
        if (isLivestream) {
            exoCast.setVisibility(GONE);
            rlTimeBar.setVisibility(GONE);
            exoPlay.setVisibility(GONE);
            exoPause.setVisibility(GONE);

            //TODO why set gone not work?
            //exoRew.setVisibility(GONE);
            //exoFfwd.setVisibility(GONE);
            //exoPlay.setVisibility(GONE);
            //exoPause.setVisibility(GONE);

            changeVisibilitiesOfButton(exoRew, false, 0);
            changeVisibilitiesOfButton(exoFfwd, false, 0);
            changeVisibilitiesOfButton(exoPlay, false, 0);
            changeVisibilitiesOfButton(exoPause, false, 0);

            rlLiveInfo.setVisibility(VISIBLE);
        } else {
            exoCast.setVisibility(GONE);
            rlTimeBar.setVisibility(VISIBLE);
            exoPlay.setVisibility(VISIBLE);
            exoPause.setVisibility(VISIBLE);

            //TODO why set visible not work?
            //exoRew.setVisibility(VISIBLE);
            //exoFfwd.setVisibility(VISIBLE);
            //exoPlay.setVisibility(VISIBLE);
            //exoPause.setVisibility(VISIBLE);

            changeVisibilitiesOfButton(exoRew, true, R.drawable.ic_fast_rewind_black_48dp);
            changeVisibilitiesOfButton(exoFfwd, true, R.drawable.ic_fast_forward_black_48dp);
            changeVisibilitiesOfButton(exoPlay, true, R.drawable.ic_play_arrow_black_48dp);
            changeVisibilitiesOfButton(exoPause, true, R.drawable.ic_pause_black_48dp);

            rlLiveInfo.setVisibility(GONE);
        }
    }

    //trick to gone view
    private void changeVisibilitiesOfButton(ImageButtonWithSize imageButtonWithSize, boolean isVisible, int res) {
        if (imageButtonWithSize == null) {
            return;
        }
        imageButtonWithSize.setClickable(isVisible);
        imageButtonWithSize.setImageResource(res);
    }

    public void updateButtonVisibilities() {
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
        LLog.d(TAG, "onProgressChanged progress: " + progress);
        if (seekBar == null || !isLandscape) {
            return;
        }
        if (seekBar == seekbarVolume) {
            if (isSetProgressSeekbarFirst) {
                exoIvPreview.setVisibility(INVISIBLE);
            } else {
                if (progress >= 66) {
                    exoIvPreview.setImageResource(R.drawable.ic_volume_up_black_48dp);
                } else if (progress >= 33) {
                    exoIvPreview.setImageResource(R.drawable.ic_volume_down_black_48dp);
                } else {
                    exoIvPreview.setImageResource(R.drawable.ic_volume_mute_black_48dp);
                }
            }
            //LLog.d(TAG, "seekbarVolume onProgressChanged " + progress + " -> " + ((float) progress / 100));
            uizaPlayerManagerV3.setVolume(((float) progress / 100));
        } else if (seekBar == seekbarBirghtness) {
            //LLog.d(TAG, "seekbarBirghtness onProgressChanged " + progress);
            if (isSetProgressSeekbarFirst) {
                exoIvPreview.setVisibility(INVISIBLE);
            } else {
                if (progress >= 85) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_7_black_48dp);
                } else if (progress >= 71) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_6_black_48dp);
                } else if (progress >= 57) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_5_black_48dp);
                } else if (progress >= 42) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_4_black_48dp);
                } else if (progress >= 28) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_3_black_48dp);
                } else if (progress >= 14) {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_2_black_48dp);
                } else {
                    exoIvPreview.setImageResource(R.drawable.ic_brightness_1_black_48dp);
                }
            }
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

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 6969;

    private void clickPiP() {
        //LLog.d(TAG, "clickPiP");
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            //LLog.d(TAG, "clickPiP if");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            //LLog.d(TAG, "clickPiP else");
            initializePiP();
        }
    }

    public void initializePiP() {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, FloatingUizaVideoServiceV3.class);
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uizaPlayerManagerV3.getLinkPlay());
        activity.startService(intent);
        if (callback != null) {
            callback.onClickPip(intent);
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
        sendGetTokenStreaming.setAppId(UizaV3Util.getAppId(activity));
        sendGetTokenStreaming.setEntityId(UizaDataV3.getInstance().getUizaInputV3().getData().getId());
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        activity.subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                //LLog.d(TAG, "getTokenStreaming onSuccess: " + gson.toJson(result));
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
                LLog.d(TAG, "tokenStreaming: " + tokenStreaming);
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
        LLog.d(TAG, "getLinkPlay isLivestream " + isLivestream);
        RestClientV3GetLinkPlay.addAuthorization(tokenStreaming);
        UizaServiceV3 service = RestClientV3GetLinkPlay.createService(UizaServiceV3.class);

        if (isLivestream) {
            String appId = UizaV3Util.getAppId(activity);
            String channelName = UizaDataV3.getInstance().getUizaInputV3().getData().getChannelName();
            LLog.d(TAG, "===================================");
            LLog.d(TAG, "========name: " + channelName);
            LLog.d(TAG, "========appId: " + appId);
            //LLog.d(TAG, "========entityId: " + entityId);
            LLog.d(TAG, "===================================");
            activity.subscribe(service.getLinkPlayLive(appId, channelName), new ApiSubscriber<ResultGetLinkPlay>() {
                @Override
                public void onSuccess(ResultGetLinkPlay result) {
                    LLog.d(TAG, "getLinkPlayLive onSuccess: " + gson.toJson(result));
                    mResultGetLinkPlay = result;
                    isResultGetLinkPlayDone = true;
                    checkToSetUp();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "getLinkPlayLive onFail " + e.getMessage());
                    if (e == null) {
                        return;
                    }
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
            String appId = UizaV3Util.getAppId(activity);
            String entityId = UizaDataV3.getInstance().getUizaInputV3().getData().getId();
            String typeContent = SendGetTokenStreaming.STREAM;
            //LLog.d(TAG, "===================================");
            //LLog.d(TAG, "========tokenStreaming: " + tokenStreaming);
            //LLog.d(TAG, "========appId: " + appId);
            //LLog.d(TAG, "========entityId: " + entityId);
            //LLog.d(TAG, "===================================");
            activity.subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
                @Override
                public void onSuccess(ResultGetLinkPlay result) {
                    //LLog.d(TAG, "getLinkPlay onSuccess: " + gson.toJson(result));
                    mResultGetLinkPlay = result;
                    isResultGetLinkPlayDone = true;
                    checkToSetUp();
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "getLinkPlay onFail " + e.getMessage());
                    if (e == null) {
                        return;
                    }
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

    private void getDetailEntity() {
        //LLog.d(TAG, "getDetailEntity");

        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String id = UizaDataV3.getInstance().getUizaInputV3().getData().getId();
        activity.subscribe(service.retrieveAnEntity(id), new ApiSubscriber<ResultRetrieveAnEntity>() {
            @Override
            public void onSuccess(ResultRetrieveAnEntity result) {
                //LLog.d(TAG, "retrieveAnEntity onSuccess: " + gson.toJson(result));
                mResultRetrieveAnEntity = result;
                isResultRetrieveAnEntityDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "retrieveAnEntity onFail " + e.getMessage());
                LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.cannot_get_detail_entity), new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        handleError(new Exception(activity.getString(R.string.cannot_get_detail_entity)));
                    }

                    @Override
                    public void onCancel() {
                        handleError(new Exception(activity.getString(R.string.cannot_get_detail_entity)));
                    }
                });
            }
        });
    }

    public interface Callback {
        //when video init done with result
        public void isInitResult(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, ResultRetrieveAnEntity resultRetrieveAnEntity);

        //user click an item in entity relation
        public void onClickListEntityRelation(Item item, int position);

        //user click button back in controller
        public void onClickBack();

        //user click button pip in controller
        public void onClickPip(Intent intent);

        //when pip video is inited success
        public void onClickPipVideoInitSuccess(boolean isInitSuccess);

        //when uiimavideo had an error
        public void onError(Exception e);
    }

    private Callback callback;

    private void trackUiza(final UizaTracking uizaTracking) {
        UizaServiceV2 service = RestClientTracking.createService(UizaServiceV2.class);
        activity.subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
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
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
            }
        });
    }

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
                    hideLLMsg();
                    if (uizaPlayerManagerV3.getExoPlaybackException() == null) {
                        //LLog.d(TAG, "onMessageEventConnectEvent do nothing");
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
                }
            } else {
                showTvMsg(activity.getString(R.string.err_no_internet));
                //if current screen is portrait -> do nothing
                //else current screen is landscape -> change screen to portrait
                LActivityUtil.changeScreenPortrait(activity);
            }
        }
    }

    //listen msg from service
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromService msg) {
        //LLog.d(TAG, "get event from service");
        if (msg == null) {
            return;
        }
        //when pip float view init success
        if (callback != null && msg instanceof ComunicateMng.MsgFromServiceIsInitSuccess) {
            //LLog.d(TAG, "get event from service isInitSuccess: " + ((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());

            ComunicateMng.MsgFromActivityPosition msgFromActivityPosition = new ComunicateMng.MsgFromActivityPosition(null);
            msgFromActivityPosition.setPosition(uizaPlayerManagerV3.getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);

            callback.onClickPipVideoInitSuccess(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        } else if (msg instanceof ComunicateMng.MsgFromServicePosition) {
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

    public void hideControllerOnTouch(boolean isHide) {
        playerView.setControllerHideOnTouch(isHide);
    }

    private void showTvMsg(String msg) {
        //LLog.d(TAG, "showTvMsg " + msg);
        tvMsg.setText(msg);
        showLLMsg();
    }

    private void showLLMsg() {
        if (rlMsg.getVisibility() != VISIBLE) {
            rlMsg.setVisibility(VISIBLE);
        }
        hideController();
    }

    private void hideLLMsg() {
        if (rlMsg.getVisibility() != GONE) {
            rlMsg.setVisibility(GONE);
        }
    }

    private void updateLiveInfoCurrentView(final int durationDelay) {
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //LLog.d(TAG, "updateLiveInfoCurrentView " + System.currentTimeMillis());
                if (uizaPlayerManagerV3 != null && playerView != null && (playerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    //LLog.d(TAG, "updateLiveInfoCurrentView isShowing");
                    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
                    String id = UizaDataV3.getInstance().getUizaInputV3().getData().getId();
                    activity.subscribe(service.getViewALiveFeed(id), new ApiSubscriber<ResultGetViewALiveFeed>() {
                        @Override
                        public void onSuccess(ResultGetViewALiveFeed result) {
                            //LLog.d(TAG, "getViewALiveFeed onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null) {
                                tvLiveView.setText(result.getData().getWatchnow() + "");
                            }
                            updateLiveInfoCurrentView(DELAY_TO_GET_LIVE_INFORMATION);
                        }

                        @Override
                        public void onFail(Throwable e) {
                            //LLog.e(TAG, "getViewALiveFeed onFail " + e.getMessage());
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
        LUIUtil.setDelay(durationDelay, new LUIUtil.DelayCallback() {

            @Override
            public void doAfter(int mls) {
                if (uizaPlayerManagerV3 != null && playerView != null && (playerView.isControllerVisible() || durationDelay == DELAY_FIRST_TO_GET_LIVE_INFORMATION)) {
                    UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
                    String entityId = UizaDataV3.getInstance().getUizaInputV3().getData().getId();
                    String feedId = UizaDataV3.getInstance().getUizaInputV3().getData().getLastFeedId();
                    activity.subscribe(service.getTimeStartLive(entityId, feedId), new ApiSubscriber<ResultTimeStartLive>() {
                        @Override
                        public void onSuccess(ResultTimeStartLive result) {
                            LLog.d(TAG, "getTimeStartLive onSuccess: " + gson.toJson(result));
                            if (result != null && result.getData() != null && result.getData().getStartTime() != null) {
                                tvLiveTime.setText(LDateUtils.getDateWithoutTime(result.getData().getStartTime()));
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
}
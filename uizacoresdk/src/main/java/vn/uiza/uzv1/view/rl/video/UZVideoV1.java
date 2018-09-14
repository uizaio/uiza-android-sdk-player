package vn.uiza.uzv1.view.rl.video;

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

import com.github.rubensousa.previewseekbar.PreviewView;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar;
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
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LSocialUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.data.EventBusData;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;
import vn.uiza.restapi.uiza.UizaServiceV2;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.uiza.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.uzv1.listerner.ProgressCallback;
import vn.uiza.uzv1.manager.UZPlayerManagerV1;
import vn.uiza.uzv1.view.ComunicateMng;
import vn.uiza.uzv1.view.dlg.info.UZDlgInfoV1;
import vn.uiza.uzv1.view.dlg.listentityrelation.PlayListCallback;
import vn.uiza.uzv1.view.dlg.listentityrelation.UZDlgListEntityRelation;
import vn.uiza.uzv1.view.floatview.FUZVideoServiceV1;
import vn.uiza.uzv1.view.util.UizaDataV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;
import vn.uiza.views.autosize.UZImageButton;
import vn.uiza.views.seekbar.UZVerticalSeekBar;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UZVideoV1 extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = getClass().getSimpleName();
    private BaseActivity activity;
    //TODO remove
    private Gson gson = new Gson();
    private RelativeLayout rootView;
    private PlayerView playerView;
    private UZPlayerManagerV1 UZPlayerManagerV1;
    private ProgressBar progressBar;
    //play controller
    private RelativeLayout llMid;
    private View llMidSub;

    private PreviewTimeBar previewTimeBar;
    private ImageView ivThumbnail;

    private ImageView ivVideoCover;
    private UZImageButton exoFullscreenIcon;
    private TextView tvTitle;
    private UZImageButton exoBackScreen;
    private UZImageButton exoVolume;
    private UZImageButton exoSetting;
    private UZImageButton exoCc;
    private UZImageButton exoPlaylist;
    private UZImageButton exoHearing;
    private UZImageButton exoPictureInPicture;
    private UZImageButton exoShare;
    private UZVerticalSeekBar seekbarVolume;
    private UZVerticalSeekBar seekbarBirghtness;
    private ImageView exoIvPreview;

    private LinearLayout debugLayout;
    private LinearLayout debugRootView;
    private TextView debugTextView;

    private int firstBrightness = Constants.NOT_FOUND;

    private GetLinkPlay mGetLinkPlay;
    private GetDetailEntity mGetDetailEntity;
    private boolean isGetLinkPlayDone;
    private boolean isGetDetailEntityDone;

    public PlayerView getPlayerView() {
        return playerView;
    }

    public TextView getDebugTextView() {
        return debugTextView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
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
        UizaDataV1.getInstance().setSettingPlayer(true);
        isHasError = false;
        if (UizaDataV1.getInstance().getUizaInputV1().getEntityId() == null || UizaDataV1.getInstance().getUizaInputV1().getEntityId().isEmpty()) {
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

        //UZUtil.stopServicePiPIfRunning(activity);

        this.callback = callback;
        if (UZPlayerManagerV1 != null) {
            //LLog.d(TAG, "init UZPlayerManagerV1 != null");
            UZPlayerManagerV1.release();
            mGetLinkPlay = null;
            mGetDetailEntity = null;
            isGetLinkPlayDone = false;
            isGetDetailEntityDone = false;
            countTryLinkPlayError = 0;
        }
        setTitle();
        setVideoCover();
        getLinkPlay();
        getDetailEntity();

        LUIUtil.showProgressBar(progressBar);

        //ko track neu play tu clicked pip
        if (!UZUtil.getClickedPip(activity)) {
            //cannot delete delay below, only works after 500mls
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    //track event eventype display
                    trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_DISPLAY));

                    //track event plays_requested
                    trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_PLAYS_REQUESTED));
                }
            });
        }
    }

    private int countTryLinkPlayError = 0;

    public void tryNextLinkPlay() {
        countTryLinkPlayError++;
        //LToast.show(activity, activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        //LLog.d(TAG, "tryNextLinkPlay countTryLinkPlayError " + countTryLinkPlayError);
        UZPlayerManagerV1.release();
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
                    UizaDataV1.getInstance().setSettingPlayer(false);
                    callback.isInitResult(false, null, null);

                    //callback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }

            @Override
            public void onCancel() {
                LLog.e(TAG, "handleErrorNoData onCancel");
                if (callback != null) {
                    UizaDataV1.getInstance().setSettingPlayer(false);
                    callback.isInitResult(false, null, null);

                    //callback.onError(new Exception(activity.getString(R.string.has_no_linkplay)));
                }
            }
        });
    }

    private void checkToSetUp() {
        if (isGetLinkPlayDone && isGetDetailEntityDone) {
            if (mGetLinkPlay != null && mGetDetailEntity != null) {
                //LLog.d(TAG, "checkToSetUp if");
                List<String> listLinkPlay = new ArrayList<>();
                List<Mpd> mpdList = mGetLinkPlay.getMpd();

                for (Mpd mpd : mpdList) {
                    if (mpd.getUrl() != null) {
                        listLinkPlay.add(mpd.getUrl());
                    }
                }

                //listLinkPlay.add("https://toanvk-live.uizacdn.net/893db5e8bb3943bfb12894fec56c8875-live/hi-uaqsv9as/manifest.mpd");
                //listLinkPlay.add("http://112.78.4.162/drm/test/hevc/playlist.mpd");
                //listLinkPlay.add("http://112.78.4.162/6yEB8Lgd/package/playlist.mpd");
                //listLinkPlay.add("http://112.78.4.162/a204e9cdeca44948a33e0d012ef74e90/DjcqBOOI/package/playlist.mpd");

                //listLinkPlay.add("https://cdn-vn-cache-3.uiza.io:443/a204e9cdeca44948a33e0d012ef74e90/DjcqBOOI/package/video/avc1/854x480/playlist.m3u8");

                LLog.d(TAG, "listLinkPlay toJson: " + gson.toJson(listLinkPlay));
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
                        LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.err_no_internet), new LDialogUtil.Callback1() {
                            @Override
                            public void onClick1() {
                                handleError(new Exception(activity.getString(R.string.err_no_internet)));
                            }

                            @Override
                            public void onCancel() {
                                handleError(new Exception(activity.getString(R.string.err_no_internet)));
                            }
                        });
                    }
                    return;
                }
                String linkPlay = listLinkPlay.get(countTryLinkPlayError);
                //LLog.d(TAG, "mGetDetailEntity toJson: " + gson.toJson(mGetDetailEntity));
                List<Subtitle> subtitleList = mGetDetailEntity.getData().get(0).getSubtitle();
                //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

                initData(linkPlay, UizaDataV1.getInstance().getUizaInputV1().getUrlIMAAd(), UizaDataV1.getInstance().getUizaInputV1().getUrlThumnailsPreviewSeekbar(), subtitleList);
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
            LImageUtil.load(activity, UizaDataV1.getInstance().getUizaInputV1().getEntityCover() == null ? Constants.URL_IMG_THUMBNAIL : Constants.PREFIXS + UizaDataV1.getInstance().getUizaInputV1().getEntityCover(), ivVideoCover, R.drawable.uiza);
        }
    }

    public void removeVideoCover(boolean isFromHandleError) {
        if (ivVideoCover.getVisibility() != GONE) {
            //LLog.d(TAG, "removeVideoCover isFromHandleError: " + isFromHandleError);
            ivVideoCover.setVisibility(GONE);
            if (!isFromHandleError) {
                onStateReadyFirst();
            }
        }
    }

    public UZVideoV1(Context context) {
        super(context);
        onCreate();
    }

    public UZVideoV1(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UZVideoV1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UZVideoV1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        activity = ((BaseActivity) getContext());
        inflate(getContext(), R.layout.uiza_ima_video_core_rl, this);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        addPlayerView();
        findViews();
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, false);
        updateUIEachSkin();
        setMarginPreviewTimeBarLayout(false);
    }

    private void addPlayerView() {
        //PlayerView playerView = null;
        UZPlayerViewV1 playerView = null;
        int resLayout = UizaDataV1.getInstance().getCurrentPlayerId();
        //LLog.d(TAG, "addPlayerView resLayout " + resLayout);
        /*switch (resLayout) {
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
        }*/
        playerView = (UZPlayerViewV1) activity.getLayoutInflater().inflate(resLayout, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        playerView.setLayoutParams(lp);
        rootView.addView(playerView);
    }

    private void updateUIEachSkin() {
        int resLayout = UizaDataV1.getInstance().getCurrentPlayerId();
        if (resLayout == R.layout.player_skin_2 || resLayout == R.layout.player_skin_3) {
            UZImageButton exoPlay = (UZImageButton) playerView.findViewById(R.id.exo_play);
            exoPlay.setRatioLand(7);
            exoPlay.setRatioPort(5);
            UZImageButton exoPause = (UZImageButton) playerView.findViewById(R.id.exo_pause);
            exoPause.setRatioLand(7);
            exoPause.setRatioPort(5);
        }
    }

    private void findViews() {
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llMid = (RelativeLayout) findViewById(R.id.ll_mid);
        llMidSub = (View) findViewById(R.id.ll_mid_sub);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        playerView = findViewById(R.id.player_view);

        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        previewTimeBar.addOnPreviewChangeListener(this);
        ivThumbnail = (ImageView) playerView.findViewById(R.id.image_view_thumnail);

        exoFullscreenIcon = (UZImageButton) playerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) playerView.findViewById(R.id.tv_title);
        exoBackScreen = (UZImageButton) playerView.findViewById(R.id.exo_back_screen);
        exoVolume = (UZImageButton) playerView.findViewById(R.id.exo_volume);
        exoSetting = (UZImageButton) playerView.findViewById(R.id.exo_setting);
        exoCc = (UZImageButton) playerView.findViewById(R.id.exo_cc);
        exoPlaylist = (UZImageButton) playerView.findViewById(R.id.exo_playlist_relation);
        exoHearing = (UZImageButton) playerView.findViewById(R.id.exo_hearing);

        exoPictureInPicture = (UZImageButton) playerView.findViewById(R.id.exo_picture_in_picture);
        exoShare = (UZImageButton) playerView.findViewById(R.id.exo_share);

        exoIvPreview = (ImageView) playerView.findViewById(R.id.exo_iv_preview);
        seekbarVolume = (UZVerticalSeekBar) playerView.findViewById(R.id.seekbar_volume);
        seekbarBirghtness = (UZVerticalSeekBar) playerView.findViewById(R.id.seekbar_birghtness);
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
        UZPlayerManagerV1 = new UZPlayerManagerV1(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        if (urlThumnailsPreviewSeekbar == null || urlThumnailsPreviewSeekbar.isEmpty()) {
            previewTimeBar.setEnabled(false);
        } else {
            previewTimeBar.setEnabled(true);
        }
        previewTimeBar.setPreviewLoader(UZPlayerManagerV1);
        UZPlayerManagerV1.setProgressCallback(new ProgressCallback() {
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
        UZPlayerManagerV1.setDebugCallback(new UZPlayerManagerV1.DebugCallback() {
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
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, "25", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, "50", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, "75", Constants.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, "100", Constants.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        //LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true, mGetLinkPlay, mGetDetailEntity);
        }
        //ko track neu play tu clicked pip
        if (!UZUtil.getClickedPip(activity)) {
            //track event video_starts
            trackUiza(UizaDataV1.getInstance().createTrackingInput(activity, Constants.EVENT_TYPE_VIDEO_STARTS));
        }
        UizaDataV1.getInstance().setSettingPlayer(false);
    }

    public void setProgressSeekbar(final UZVerticalSeekBar UZVerticalSeekBar, final int progressSeekbar) {
        UZVerticalSeekBar.setProgress(progressSeekbar);
        //LLog.d(TAG, "setProgressSeekbar " + progressSeekbar);
    }

    public void setProgressVolumeSeekbar(int progress) {
        setProgressSeekbar(seekbarVolume, progress);
    }

    public void onDestroy() {
        if (firstBrightness != Constants.NOT_FOUND) {
            LScreenUtil.setBrightness(getContext(), firstBrightness);
        }
        if (UZPlayerManagerV1 != null) {
            UZPlayerManagerV1.release();
        }
        UizaDataV1.getInstance().setSettingPlayer(false);
        LDialogUtil.clearAll();
    }

    public void onResume() {
        activityIsPausing = false;
        if (isExoShareClicked) {
            isExoShareClicked = false;

            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.resumeVideo();
            }
        } else {
            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.init();
                if (isCalledFromConnectionEventBus) {
                    UZPlayerManagerV1.setRunnable();
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
            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.pauseVideo();
            }
        } else {
            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.reset();
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
    public void onStartPreview(PreviewView previewView, int progress) {
        //LLog.d(TAG, "onStartPreview");
    }

    @Override
    public void onStopPreview(PreviewView previewView, int progress) {
        //LLog.d(TAG, "onStopPreview");
        UZPlayerManagerV1.resumeVideo();
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        //LLog.d(TAG, "onPreview progress " + progress);
    }

    private boolean isExoShareClicked;

    @Override
    public void onClick(View v) {
        if (v == exoFullscreenIcon) {
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
            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.toggleVolumeMute(exoVolume);
            }
        } else if (v == exoSetting) {
            View view = UZUtil.getBtVideo(debugRootView);
            if (view != null) {
                UZUtil.getBtVideo(debugRootView).performClick();
            }
        } else if (v == exoCc) {
            if (UZPlayerManagerV1.getSubtitleList() == null || UZPlayerManagerV1.getSubtitleList().isEmpty()) {
                UZDlgInfoV1 UZDlgInfoV1 = new UZDlgInfoV1(activity, activity.getString(R.string.text), activity.getString(R.string.no_caption));
                UZUtil.showUizaDialog(activity, UZDlgInfoV1);
            } else {
                View view = UZUtil.getBtText(debugRootView);
                if (view != null) {
                    UZUtil.getBtText(debugRootView).performClick();
                }
            }
        } else if (v == exoPlaylist) {
            UZDlgListEntityRelation UZDlgListEntityRelation = new UZDlgListEntityRelation(activity, isLandscape, new PlayListCallback() {
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
            UZUtil.showUizaDialog(activity, UZDlgListEntityRelation);
        } else if (v == exoHearing) {
            View view = UZUtil.getBtAudio(debugRootView);
            if (view != null) {
                UZUtil.getBtAudio(debugRootView).performClick();
            }
        } else if (v == exoPictureInPicture) {
            clickPiP();
        } else if (v == exoShare) {
            LSocialUtil.share(activity, isLandscape);
            isExoShareClicked = true;
        } else if (v.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = UZPlayerManagerV1.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null && UZPlayerManagerV1.getTrackSelectionHelper() != null) {
                UZPlayerManagerV1.getTrackSelectionHelper().showSelectionDialog(activity, ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
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
                setExoPictureInPictureVisibility(GONE);
                UZUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, true);

                setMarginPreviewTimeBarLayout(true);
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                setExoPictureInPictureVisibility(VISIBLE);
                UZUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, false);

                setMarginPreviewTimeBarLayout(false);
            }
        }
        UZUtil.resizeLayout(rootView, llMid, ivVideoCover, false);
    }

    private void setMarginPreviewTimeBarLayout(boolean isLandscape) {
        if (isLandscape) {
            LUIUtil.setMarginDimen(previewTimeBar, 24, 0, 24, 0);
        } else {
            LUIUtil.setMarginDimen(previewTimeBar, 15, 0, 15, 0);
        }
    }

    public void setTitle() {
        tvTitle.setText(UizaDataV1.getInstance().getUizaInputV1().getEntityName());
        LUIUtil.setTextShadow(tvTitle);
    }

    public void updateButtonVisibilities() {
        debugRootView.removeAllViews();
        if (UZPlayerManagerV1.getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = UZPlayerManagerV1.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(activity);
                int label;
                switch (UZPlayerManagerV1.getPlayer().getRendererType(i)) {
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
        if (seekBar == null) {
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
            UZPlayerManagerV1.setVolume(((float) progress / 100));
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
        LUIUtil.setTintSeekbar(seekBar, Color.WHITE);
        exoIvPreview.setVisibility(VISIBLE);
        if (llMidSub != null) {
            llMidSub.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //LLog.d(TAG, "onStopTrackingTouch");
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

    public void setExoPictureInPictureVisibility(int visibility) {
        exoPictureInPicture.setVisibility(visibility);
    }

    public void initializePiP() {
        if (activity == null) {
            return;
        }
        setExoPictureInPictureVisibility(GONE);
        Intent intent = new Intent(activity, FUZVideoServiceV1.class);
        intent.putExtra(Constants.FLOAT_LINK_PLAY, UZPlayerManagerV1.getLinkPlay());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, UizaDataV1.getInstance().getUizaInputV1().getEntityId());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, UizaDataV1.getInstance().getUizaInputV1().getEntityCover());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, UizaDataV1.getInstance().getUizaInputV1().getEntityName());
        activity.startService(intent);
        if (callback != null) {
            callback.onClickPip(intent);
        }
    }

    public SimpleExoPlayer getPlayer() {
        if (UZPlayerManagerV1 == null) {
            return null;
        }
        return UZPlayerManagerV1.getPlayer();
    }

    private void getLinkPlay() {
        //LLog.d(TAG, "getLinkPlay");
        UZUtil.setupRestClientV2(activity);
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        Auth auth = UZUtil.getAuth(activity, gson);
        if (auth == null || auth.getData().getAppId() == null) {
            LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.auth_or_app_id_is_null_or_empty), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    handleError(new Exception(activity.getString(R.string.auth_or_app_id_is_null_or_empty)));
                }

                @Override
                public void onCancel() {
                    handleError(new Exception(activity.getString(R.string.auth_or_app_id_is_null_or_empty)));
                }
            });
            return;
        }
        String appId = auth.getData().getAppId();
        //LLog.d(TAG, "getLinkPlay entityId: " + UizaDataV1.getInstance().getEntityId() + ", appId: " + appId);
        activity.subscribe(service.getLinkPlayV2(UizaDataV1.getInstance().getUizaInputV1().getEntityId(), appId), new ApiSubscriber<GetLinkPlay>() {
            @Override
            public void onSuccess(GetLinkPlay getLinkPlay) {
                LLog.d(TAG, "getLinkPlay onSuccess " + gson.toJson(getLinkPlay));
                mGetLinkPlay = getLinkPlay;
                isGetLinkPlayDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "onFail getLinkPlay: " + e.toString());
                LDialogUtil.showDialog1Immersive(activity, activity.getString(R.string.no_link_play), new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        handleError(new Exception(activity.getString(R.string.no_link_play)));
                    }

                    @Override
                    public void onCancel() {
                        handleError(new Exception(activity.getString(R.string.no_link_play)));
                    }
                });
            }
        });
    }

    private void getDetailEntity() {
        //LLog.d(TAG, "getDetailEntity");
        UZUtil.setupRestClientV2(activity);
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        final JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(UizaDataV1.getInstance().getUizaInputV1().getEntityId());

        ((BaseActivity) activity).subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntity) {
                LLog.d(TAG, "getDetailEntity entityId " + jsonBodyGetDetailEntity.getId() + " -> " + gson.toJson(getDetailEntity));
                mGetDetailEntity = getDetailEntity;
                isGetDetailEntityDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntity onFail " + e.toString());
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
        public void isInitResult(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity);

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

    /*private void getLinkDownload() {
        LLog.d(TAG, ">>>getLinkDownload entityId: " + inputModel.getEntityID());
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        Auth auth = UizaPref.getAuth(activity, gson);
        if (auth == null || auth.getData().getAppId() == null) {
            showDialogError("Error auth == null || auth.getAppId() == null");
            return;
        }
        LLog.d(TAG, ">>>getLinkDownload appId: " + auth.getData().getAppId());

        JsonBodyGetLinkDownload jsonBodyGetLinkDownload = new JsonBodyGetLinkDownload();
        List<String> listEntityIds = new ArrayList<>();
        listEntityIds.add(inputModel.getEntityID());
        jsonBodyGetLinkDownload.setListEntityIds(listEntityIds);

        //API v2
        subscribe(service.getLinkDownloadV2(jsonBodyGetLinkDownload), new ApiSubscriber<GetLinkDownload>() {
            @Override
            public void onSuccess(GetLinkDownload getLinkDownload) {
                LLog.d(TAG, "getLinkDownloadV2 onSuccess " + gson.toJson(getLinkDownload));
                //UizaDataV1.getInstance().setLinkPlay("http://demos.webmproject.org/dash/201410/vp9_glass/manifest_vp9_opus.mpd");
                //UizaDataV1.getInstance().setLinkPlay("http://dev-preview.uiza.io/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJVSVpBIiwiYXVkIjoidWl6YS5pbyIsImlhdCI6MTUxNjMzMjU0NSwiZXhwIjoxNTE2NDE4OTQ1LCJlbnRpdHlfaWQiOiIzYWUwOWJhNC1jMmJmLTQ3MjQtYWRmNC03OThmMGFkZDY1MjAiLCJlbnRpdHlfbmFtZSI6InRydW5nbnQwMV8xMiIsImVudGl0eV9zdHJlYW1fdHlwZSI6InZvZCIsImFwcF9pZCI6ImEyMDRlOWNkZWNhNDQ5NDhhMzNlMGQwMTJlZjc0ZTkwIiwic3ViIjoiYTIwNGU5Y2RlY2E0NDk0OGEzM2UwZDAxMmVmNzRlOTAifQ.ktZsaoGA3Dp4J1cGR00bt4UIiMtcsjxgzJWSTnxnxKk/a204e9cdeca44948a33e0d012ef74e90-data/transcode-output/unzKBIUm/package/playlist.mpd");

                List<String> listLinkPlay = new ArrayList<>();
                List<Mpd> mpdList = getLinkDownload.getData().get(0).getMpd();
                for (Mpd mpd : mpdList) {
                    if (mpd.getUrl() != null) {
                        listLinkPlay.add(mpd.getUrl());
                    }
                }
                LLog.d(TAG, "getLinkDownloadV2 toJson: " + gson.toJson(listLinkPlay));

                if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                    LLog.d(TAG, "listLinkPlay == null || listLinkPlay.isEmpty()");
                    showDialogOne(getString(R.string.has_no_linkplay), true);
                    return;
                }

                UizaDataV1.getInstance().setLinkPlay(listLinkPlay);
                isGetLinkPlayDone = true;
                init();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "onFail getLinkDownloadV2: " + e.toString());
                handleException(e);
            }
        });
        //End API v2
    }*/

    private void trackUiza(final UizaTracking uizaTracking) {
        UizaServiceV2 service = RestClientTracking.createService(UizaServiceV2.class);
        activity.subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                LLog.d(TAG, ">>>trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
            }

            @Override
            public void onFail(Throwable e) {
                //TODO iplm if track fail
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
                //((BaseActivity) getContext()).showDialogError("Cannot track this entity");
            }
        });
    }

    public void seekTo(long positionMs) {
        if (UZPlayerManagerV1 != null) {
            //LLog.d(TAG, "seekTo positionMs: " + positionMs);
            UZPlayerManagerV1.seekTo(positionMs);
        }
    }

    private boolean isCalledFromConnectionEventBus = false;

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        if (event != null) {
            //LLog.d(TAG, "onMessageEvent isConnected: " + event.isConnected());
            if (event.isConnected()) {
                if (UZPlayerManagerV1 != null) {
                    LDialogUtil.clearAll();
                    if (UZPlayerManagerV1.getExoPlaybackException() == null) {
                        //LLog.d(TAG, "onMessageEvent do nothing");
                    } else {
                        /*UZPlayerManagerV1.setResumeIfConnectionError();
                        UZPlayerManagerV1.setRunnable();
                        UZPlayerManagerV1.init();*/
                        isCalledFromConnectionEventBus = true;
                        UZPlayerManagerV1.setResumeIfConnectionError();
                        //LLog.d(TAG, "onMessageEvent activityIsPausing " + activityIsPausing);
                        if (!activityIsPausing) {
                            if (UZPlayerManagerV1 != null) {
                                UZPlayerManagerV1.init();
                                if (isCalledFromConnectionEventBus) {
                                    UZPlayerManagerV1.setRunnable();
                                    isCalledFromConnectionEventBus = false;
                                }
                            }
                        } else {
                            //auto call onResume() again
                            //LLog.d(TAG, "onMessageEvent auto call onResume() again");
                        }
                    }
                }
            } else {
                /*if (UZPlayerManagerV1 != null) {
                    UZPlayerManagerV1.pauseVideo();
                }*/
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
            msgFromActivityPosition.setPosition(UZPlayerManagerV1.getCurrentPosition());
            ComunicateMng.postFromActivity(msgFromActivityPosition);

            callback.onClickPipVideoInitSuccess(((ComunicateMng.MsgFromServiceIsInitSuccess) msg).isInitSuccess());
        } else if (msg instanceof ComunicateMng.MsgFromServicePosition) {
            //LLog.d(TAG, "seek to: " + ((ComunicateMng.MsgFromServicePosition) msg).getPosition());
            if (UZPlayerManagerV1 != null) {
                UZPlayerManagerV1.seekTo(((ComunicateMng.MsgFromServicePosition) msg).getPosition());
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

    public PreviewTimeBar getPreviewTimeBar() {
        return previewTimeBar;
    }
}
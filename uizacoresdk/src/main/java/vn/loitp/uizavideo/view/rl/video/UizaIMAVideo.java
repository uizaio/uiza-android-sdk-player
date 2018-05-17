package vn.loitp.uizavideo.view.rl.video;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LSocialUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.manager.UizaPlayerManager;
import vn.loitp.uizavideo.view.dlg.listentityrelation.PlayListCallback;
import vn.loitp.uizavideo.view.dlg.listentityrelation.UizaDialogListEntityRelation;
import vn.loitp.uizavideo.view.floatview.FloatingUizaVideoService;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.uizavideo.view.util.UizaUtil;
import vn.loitp.views.LToast;
import vn.loitp.views.autosize.imagebuttonwithsize.ImageButtonWithSize;
import vn.loitp.views.seekbar.verticalseekbar.VerticalSeekBar;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideo extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = getClass().getSimpleName();
    private BaseActivity activity;
    private Gson gson = new Gson();//TODO remove
    private RelativeLayout rootView;
    private PlayerView playerView;
    private UizaPlayerManager uizaPlayerManager;
    private ProgressBar progressBar;
    //play controller
    private RelativeLayout llMid;

    private PreviewTimeBarLayout previewTimeBarLayout;
    private PreviewTimeBar previewTimeBar;
    private ImageView ivThumbnail;

    private ImageView ivVideoCover;
    private ImageButton exoFullscreenIcon;
    private TextView tvTitle;
    private ImageButton exoBackScreen;
    private ImageButton exoVolume;
    private ImageButton exoSetting;
    private ImageButton exoCc;
    private ImageButton exoPlaylist;
    private ImageButton exoHearing;
    private ImageButton exoPictureInPicture;
    private ImageButton exoShare;
    private VerticalSeekBar seekbarVolume;
    private ImageView ivVolumeSeekbar;
    private VerticalSeekBar seekbarBirghtness;
    private ImageView ivBirghtnessSeekbar;

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

    public PreviewTimeBarLayout getPreviewTimeBarLayout() {
        return previewTimeBarLayout;
    }

    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    private void stopServicePiPIfRunning() {
        //LLog.d(TAG, "stopServicePiPIfRunning");
        boolean isSvPipRunning = UizaUtil.checkServiceRunning(activity, FloatingUizaVideoService.class.getName());
        //LLog.d(TAG, "isSvPipRunning " + isSvPipRunning);
        if (isSvPipRunning) {
            //stop service if running
            Intent intent = new Intent(activity, FloatingUizaVideoService.class);
            activity.stopService(intent);
        }
    }

    public void init(Callback callback) {
        if (UizaData.getInstance().getEntityId() == null || UizaData.getInstance().getEntityId().isEmpty()) {
            ((BaseActivity) activity).showDialogOne("entityId cannot be null or empty", true);
            return;
        }

        stopServicePiPIfRunning();

        this.callback = callback;
        if (uizaPlayerManager != null) {
            //LLog.d(TAG, "init uizaPlayerManager != null");
            uizaPlayerManager.release();
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
        if (!LPref.getClickedPip(activity)) {
            //cannot delete delay below, only works after 500mls
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    //track event eventype display
                    trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_DISPLAY));

                    //track event plays_requested
                    trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_PLAYS_REQUESTED));
                }
            });
        }
    }

    private int countTryLinkPlayError = 0;

    public void tryNextLinkPlay() {
        countTryLinkPlayError++;
        LToast.show(activity, activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        //LLog.d(TAG, "tryNextLinkPlay countTryLinkPlayError " + countTryLinkPlayError);
        uizaPlayerManager.release();
        checkToSetUp();
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
                //LLog.d(TAG, "listLinkPlay toJson: " + gson.toJson(listLinkPlay));
                if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                    //LLog.d(TAG, "listLinkPlay == null || listLinkPlay.isEmpty()");
                    ((BaseActivity) activity).showDialogOne(activity.getString(R.string.has_no_linkplay), true);
                    return;
                }
                if (countTryLinkPlayError >= listLinkPlay.size()) {
                    activity.showDialogOne("Đã thử play tất cả các link nhưng đều không thành công", true);
                    return;
                }
                String linkPlay = listLinkPlay.get(countTryLinkPlayError);
                //LLog.d(TAG, "mGetDetailEntity toJson: " + gson.toJson(mGetDetailEntity));
                List<Subtitle> subtitleList = mGetDetailEntity.getData().get(0).getSubtitle();
                //LLog.d(TAG, "subtitleList toJson: " + gson.toJson(subtitleList));

                if (subtitleList == null || subtitleList.isEmpty()) {
                    exoCc.setVisibility(GONE);
                } else {
                    exoCc.setVisibility(VISIBLE);
                }

                initData(linkPlay, UizaData.getInstance().getUrlIMAAd(), UizaData.getInstance().getUrlThumnailsPreviewSeekbar(), subtitleList);
                onResume();
            } else {
                ((BaseActivity) activity).showDialogOne(activity.getString(R.string.err_setup), true);
                //LLog.d(TAG, "checkToSetUp else");
            }
        }
    }

    private void setVideoCover() {
        if (ivVideoCover.getVisibility() != VISIBLE) {
            countTryLinkPlayError = 0;

            ivVideoCover.setVisibility(VISIBLE);
            //LLog.d(TAG, "setVideoCover: " + UizaData.getInstance().getEntityCover());
            //ivVideoCover.setBackgroundColor(LStoreUtil.getRandomColor());
            LImageUtil.load(activity, UizaData.getInstance().getEntityCover() == null ? Constants.URL_IMG_THUMBNAIL : Constants.PREFIXS + UizaData.getInstance().getEntityCover(), ivVideoCover, R.drawable.uiza);
        }
    }

    public void removeVideoCover() {
        if (ivVideoCover.getVisibility() != GONE) {
            //rootView.removeView(ivVideoCover);
            ivVideoCover.setVisibility(GONE);
            //ivVideoCover = null;
            onStateReadyFirst();
        }
    }

    public UizaIMAVideo(Context context) {
        super(context);
        onCreate();
    }

    public UizaIMAVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        activity = ((BaseActivity) getContext());
        inflate(getContext(), R.layout.uiza_ima_video_core_rl, this);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        addPlayerView();
        findViews();
        UizaUtil.resizeLayout(rootView, llMid);
        updateUIEachSkin();
    }

    private void addPlayerView() {
        PlayerView playerView = null;
        //LLog.d(TAG, "addPlayerView getPlayerId " + UizaData.getInstance().getPlayerId());
        switch (UizaData.getInstance().getPlayerId()) {
            case Constants.PLAYER_ID_SKIN_1:
                playerView = (PlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_1, null);
                break;
            case Constants.PLAYER_ID_SKIN_2:
                playerView = (PlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_2, null);
                break;
            case Constants.PLAYER_ID_SKIN_3:
                playerView = (PlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_3, null);
                break;
            case Constants.PLAYER_ID_SKIN_0:
                playerView = (PlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_default, null);
                break;
            default:
                playerView = (PlayerView) activity.getLayoutInflater().inflate(R.layout.player_skin_default, null);
                break;
        }
        rootView.addView(playerView);
    }

    private void updateUIEachSkin() {
        switch (UizaData.getInstance().getPlayerId()) {
            case Constants.PLAYER_ID_SKIN_2:
            case Constants.PLAYER_ID_SKIN_3:
                ImageButtonWithSize exoPlay = (ImageButtonWithSize) playerView.findViewById(R.id.exo_play);
                exoPlay.setRatioLand(7);
                exoPlay.setRatioPort(5);
                ImageButtonWithSize exoPause = (ImageButtonWithSize) playerView.findViewById(R.id.exo_pause);
                exoPause.setRatioLand(7);
                exoPause.setRatioPort(5);
                break;
        }
    }

    private void findViews() {
        ivVideoCover = (ImageView) findViewById(R.id.iv_cover);
        llMid = (RelativeLayout) findViewById(R.id.ll_mid);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        playerView = findViewById(R.id.player_view);

        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        previewTimeBarLayout = playerView.findViewById(R.id.previewSeekBarLayout);
        previewTimeBarLayout.setTintColorResource(R.color.colorPrimary);
        previewTimeBar.addOnPreviewChangeListener(this);
        ivThumbnail = (ImageView) playerView.findViewById(R.id.image_view_thumnail);

        exoFullscreenIcon = (ImageButton) playerView.findViewById(R.id.exo_fullscreen_toggle_icon);
        tvTitle = (TextView) playerView.findViewById(R.id.tv_title);
        exoBackScreen = (ImageButton) playerView.findViewById(R.id.exo_back_screen);
        exoVolume = (ImageButton) playerView.findViewById(R.id.exo_volume);
        exoSetting = (ImageButton) playerView.findViewById(R.id.exo_setting);
        exoCc = (ImageButton) playerView.findViewById(R.id.exo_cc);
        exoPlaylist = (ImageButton) playerView.findViewById(R.id.exo_playlist);
        exoHearing = (ImageButton) playerView.findViewById(R.id.exo_hearing);

        //TODO set gone to visible
        exoHearing.setVisibility(VISIBLE);

        exoPictureInPicture = (ImageButton) playerView.findViewById(R.id.exo_picture_in_picture);
        exoShare = (ImageButton) playerView.findViewById(R.id.exo_share);

        seekbarVolume = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_volume);
        seekbarBirghtness = (VerticalSeekBar) playerView.findViewById(R.id.seekbar_birghtness);
        LUIUtil.setColorSeekBar(seekbarVolume, ContextCompat.getColor(getContext(), R.color.White));
        LUIUtil.setColorSeekBar(seekbarBirghtness, ContextCompat.getColor(getContext(), R.color.White));
        ivVolumeSeekbar = (ImageView) playerView.findViewById(R.id.exo_volume_seekbar);
        ivBirghtnessSeekbar = (ImageView) playerView.findViewById(R.id.exo_birghtness_seekbar);

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

    public void initData(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        uizaPlayerManager = new UizaPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        if (urlThumnailsPreviewSeekbar == null || urlThumnailsPreviewSeekbar.isEmpty()) {
            previewTimeBarLayout.setEnabled(false);
        } else {
            previewTimeBarLayout.setEnabled(true);
        }
        if (previewTimeBarLayout != null) {
            previewTimeBarLayout.setPreviewLoader(uizaPlayerManager);
        }
        uizaPlayerManager.setProgressCallback(new ProgressCallback() {
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
        uizaPlayerManager.setDebugCallback(new UizaPlayerManager.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                //LLog.d(TAG, "onUpdateButtonVisibilities");
                updateButtonVisibilities();
            }
        });

        //set volume max in first play
        seekbarVolume.setMax(100);
        setProgressSeekbar(seekbarVolume, 99);

        //set bightness max in first play
        firstBrightness = LScreenUtil.getCurrentBrightness(getContext()) * 100 / 255 + 1;
        //LLog.d(TAG, "firstBrightness " + firstBrightness);
        seekbarBirghtness.setMax(100);
        setProgressSeekbar(seekbarBirghtness, firstBrightness);
    }

    private int oldPercent = Constants.NOT_FOUND;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s)
        if (s == 5) {
            //LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        //LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "25", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "50", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "75", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "100", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true, mGetLinkPlay, mGetDetailEntity);
        }
        //ko track neu play tu clicked pip
        if (!LPref.getClickedPip(activity)) {
            //track event video_starts
            trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_VIDEO_STARTS));
        }
    }

    public void setProgressSeekbar(SeekBar seekbar, int progressSeekbar) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekbar.setProgress(progressSeekbar, true);
        } else {
            seekbar.setProgress(progressSeekbar);
        }*/
        seekbar.setProgress(progressSeekbar);
    }

    public void setProgressVolumeSeekbar(int progress) {
        setProgressSeekbar(seekbarVolume, progress);
    }

    public void onDestroy() {
        if (firstBrightness != Constants.NOT_FOUND) {
            LScreenUtil.setBrightness(getContext(), firstBrightness);
        }
        if (uizaPlayerManager != null) {
            uizaPlayerManager.release();
        }
    }

    public void onResume() {
        if (isExoShareClicked) {
            isExoShareClicked = false;
        } else {
            if (uizaPlayerManager != null) {
                uizaPlayerManager.init();
            }
        }
    }

    public void onPause() {
        if (isExoShareClicked) {
            //do nothing
        } else {
            if (uizaPlayerManager != null) {
                uizaPlayerManager.reset();
            }
        }
    }

    @Override
    public void onStartPreview(PreviewView previewView) {
        //LLog.d(TAG, "onStartPreview");
    }

    @Override
    public void onStopPreview(PreviewView previewView) {
        //LLog.d(TAG, "onStopPreview");
        uizaPlayerManager.resumeVideo();
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
            uizaPlayerManager.toggleVolumeMute(exoVolume);
        } else if (v == exoSetting) {
            View view = UizaUtil.getBtVideo(debugRootView);
            if (view != null) {
                UizaUtil.getBtVideo(debugRootView).performClick();
            }
        } else if (v == exoCc) {
            View view = UizaUtil.getBtText(debugRootView);
            if (view != null) {
                UizaUtil.getBtText(debugRootView).performClick();
            }
        } else if (v == exoPlaylist) {
            UizaDialogListEntityRelation uizaDialogListEntityRelation = new UizaDialogListEntityRelation(activity, isLandscape, new PlayListCallback() {
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
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                uizaPlayerManager.getTrackSelectionHelper().showSelectionDialog(activity, ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
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
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, true);
            } else {
                LScreenUtil.showDefaultControls(activity);
                isLandscape = false;
                setExoPictureInPictureVisibility(VISIBLE);
                UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon, false);
            }
        }
        UizaUtil.resizeLayout(rootView, llMid);
    }

    public void setTitle() {
        tvTitle.setText(UizaData.getInstance().getEntityName());
        LUIUtil.setTextShadow(tvTitle);
    }

    public void updateButtonVisibilities() {
        debugRootView.removeAllViews();
        if (uizaPlayerManager.getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(activity);
                int label;
                switch (uizaPlayerManager.getPlayer().getRendererType(i)) {
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seekbarVolume) {
            //LLog.d(TAG, "seekbarVolume onProgressChanged " + progress);
            if (progress >= 66) {
                ivVolumeSeekbar.setImageResource(R.drawable.ic_volume_up_black_48dp);
            } else if (progress >= 33) {
                ivVolumeSeekbar.setImageResource(R.drawable.ic_volume_down_black_48dp);
            } else {
                ivVolumeSeekbar.setImageResource(R.drawable.ic_volume_mute_black_48dp);
            }
            //LLog.d(TAG, "seekbarVolume onProgressChanged " + progress + " -> " + ((float) progress / 100));
            uizaPlayerManager.setVolume(((float) progress / 100));
        } else if (seekBar == seekbarBirghtness) {
            //LLog.d(TAG, "seekbarBirghtness onProgressChanged " + progress);
            if (progress >= 85) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_7_black_48dp);
            } else if (progress >= 71) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_6_black_48dp);
            } else if (progress >= 57) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_5_black_48dp);
            } else if (progress >= 42) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_4_black_48dp);
            } else if (progress >= 28) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_3_black_48dp);
            } else if (progress >= 14) {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_2_black_48dp);
            } else {
                ivBirghtnessSeekbar.setImageResource(R.drawable.ic_brightness_1_black_48dp);
            }
            LScreenUtil.setBrightness(activity, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LLog.d(TAG, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LLog.d(TAG, "onStopTrackingTouch");
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
        //TODO go code
        //exoPictureInPicture.setVisibility(visibility);
    }

    public void initializePiP() {
        if (activity == null) {
            return;
        }
        setExoPictureInPictureVisibility(GONE);
        Intent intent = new Intent(activity, FloatingUizaVideoService.class);
        intent.putExtra(Constants.FLOAT_LINK_PLAY, uizaPlayerManager.getLinkPlay());
        intent.putExtra(Constants.FLOAT_CURRENT_POSITION, getPlayer().getCurrentPosition());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, UizaData.getInstance().getEntityId());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, UizaData.getInstance().getEntityCover());
        intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, UizaData.getInstance().getEntityName());
        activity.startService(intent);
        if (callback != null) {
            callback.onClickPip(intent);
        }
    }

    public SimpleExoPlayer getPlayer() {
        return uizaPlayerManager.getPlayer();
    }

    private void getLinkPlay() {
        //LLog.d(TAG, "getLinkPlay");
        UizaUtil.setupRestClientV2(activity);
        UizaService service = RestClientV2.createService(UizaService.class);
        Auth auth = LPref.getAuth(activity, gson);
        if (auth == null || auth.getData().getAppId() == null) {
            ((BaseActivity) activity).showDialogError("Error auth == null || auth.getAppId() == null");
            return;
        }
        String appId = auth.getData().getAppId();
        //LLog.d(TAG, "getLinkPlay entityId: " + UizaData.getInstance().getEntityId() + ", appId: " + appId);
        activity.subscribe(service.getLinkPlayV2(UizaData.getInstance().getEntityId(), appId), new ApiSubscriber<GetLinkPlay>() {
            @Override
            public void onSuccess(GetLinkPlay getLinkPlay) {
                mGetLinkPlay = getLinkPlay;
                isGetLinkPlayDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "onFail getLinkPlay: " + e.toString());
                activity.showDialogError("Không có linkplay");
            }
        });
    }

    private void getDetailEntity() {
        //LLog.d(TAG, "getDetailEntity");
        UizaUtil.setupRestClientV2(activity);
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(UizaData.getInstance().getEntityId());
        //jsonBodyGetDetailEntity.setId(Constants.ENTITYID_WITH_SUBTITLE);

        ((BaseActivity) activity).subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntity) {
                //LLog.d(TAG, "getDetailEntity entityId " + UizaData.getInstance().getEntityId() + " -> " + gson.toJson(getDetailEntity));
                mGetDetailEntity = getDetailEntity;
                isGetDetailEntityDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntity onFail " + e.toString());
                ((BaseActivity) activity).showDialogError("Cannot get detail of this entity.");
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
    }

    private Callback callback;

    /*private void getLinkDownload() {
        LLog.d(TAG, ">>>getLinkDownload entityId: " + inputModel.getEntityID());
        UizaService service = RestClientV2.createService(UizaService.class);
        Auth auth = LPref.getAuth(activity, gson);
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
                //UizaData.getInstance().setLinkPlay("http://demos.webmproject.org/dash/201410/vp9_glass/manifest_vp9_opus.mpd");
                //UizaData.getInstance().setLinkPlay("http://dev-preview.uiza.io/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJVSVpBIiwiYXVkIjoidWl6YS5pbyIsImlhdCI6MTUxNjMzMjU0NSwiZXhwIjoxNTE2NDE4OTQ1LCJlbnRpdHlfaWQiOiIzYWUwOWJhNC1jMmJmLTQ3MjQtYWRmNC03OThmMGFkZDY1MjAiLCJlbnRpdHlfbmFtZSI6InRydW5nbnQwMV8xMiIsImVudGl0eV9zdHJlYW1fdHlwZSI6InZvZCIsImFwcF9pZCI6ImEyMDRlOWNkZWNhNDQ5NDhhMzNlMGQwMTJlZjc0ZTkwIiwic3ViIjoiYTIwNGU5Y2RlY2E0NDk0OGEzM2UwZDAxMmVmNzRlOTAifQ.ktZsaoGA3Dp4J1cGR00bt4UIiMtcsjxgzJWSTnxnxKk/a204e9cdeca44948a33e0d012ef74e90-data/transcode-output/unzKBIUm/package/playlist.mpd");

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

                UizaData.getInstance().setLinkPlay(listLinkPlay);
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
        UizaService service = RestClientTracking.createService(UizaService.class);
        ((BaseActivity) getContext()).subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                //LLog.d(TAG, ">>>trackUiza  getEventType: " + uizaTracking.getEventType() + ", getEntityName:" + uizaTracking.getEntityName() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
                if (Constants.IS_DEBUG) {
                    LToast.show(getContext(), "Track success!\n" + uizaTracking.getEntityName() + "\n" + uizaTracking.getEventType() + "\n" + uizaTracking.getPlayThrough());
                }
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
                //((BaseActivity) getContext()).showDialogError("Cannot track this entity");
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                boolean isInitSuccess = intent.getBooleanExtra(Constants.FLOAT_VIDEO_INIT_RESULT, false);
                //LLog.d(TAG, "broadcastReceiver onReceive isInitSuccess: " + isInitSuccess);
                if (callback != null) {
                    callback.onClickPipVideoInitSuccess(isInitSuccess);
                }
            }
        }
    };

    public void registerReceiverPiPInitSuccess() {
        activity.registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    public void unregisterReceiverPiPInitSuccess() {
        if (broadcastReceiver != null) {
            activity.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    public void seekTo(long positionMs) {
        if (uizaPlayerManager != null) {
            //LLog.d(TAG, "seekTo positionMs: " + positionMs);
            uizaPlayerManager.seekTo(positionMs);
        }
    }
}
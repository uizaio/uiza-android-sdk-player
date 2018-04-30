package vn.loitp.uizavideo.view.rl.video;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
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
import vn.loitp.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.UizaPlayerManager;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.floatview.FloatingUizaVideoService;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.uizavideo.view.util.UizaUtil;
import vn.loitp.views.LToast;
import vn.loitp.views.realtimeblurview.RealtimeBlurView;
import vn.loitp.views.seekbar.verticalseekbar.VerticalSeekBar;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideo extends RelativeLayout implements PreviewView.OnPreviewChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private Gson gson = new Gson();//TODO remove
    private RelativeLayout rootView;
    private PlayerView playerView;
    private UizaPlayerManager uizaPlayerManager;
    private ProgressBar progressBar;
    //play controller
    private RelativeLayout llMid;
    private PreviewTimeBarLayout previewTimeBarLayout;
    private PreviewTimeBar previewTimeBar;
    private ImageButton exoFullscreenIcon;
    private ImageView ivThumbnail;
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

    public void init(Callback callback) {
        if (UizaData.getInstance().getEntityId() == null || UizaData.getInstance().getEntityId().isEmpty()) {
            ((BaseActivity) activity).showDialogOne("entityId cannot be null or empty", true);
            return;
        }
        this.callback = callback;
        if (uizaPlayerManager != null) {
            uizaPlayerManager.release();
        }
        setTitle();
        setVideoCover();
        getLinkPlay();

        LUIUtil.showProgressBar(progressBar);

        //track event eventype display
        trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_DISPLAY));

        //track event plays_requested
        trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_PLAYS_REQUESTED));
    }

    private ImageView ivVideoCover;
    private RealtimeBlurView realtimeBlurView;

    private void setVideoCover() {
        if (ivVideoCover == null && realtimeBlurView == null) {
            realtimeBlurView = new RealtimeBlurView(activity, 15, ContextCompat.getColor(activity, R.color.black_35));
            ViewGroup.LayoutParams layoutParamsBlur = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            realtimeBlurView.setLayoutParams(layoutParamsBlur);
            realtimeBlurView.setVisibility(GONE);

            ivVideoCover = new ImageView(activity);
            ivVideoCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams layoutParamsIv = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ivVideoCover.setLayoutParams(layoutParamsIv);
            LImageUtil.load(activity, UizaData.getInstance().getEntityCover() == null ? Constants.URL_IMG_THUMBNAIL : Constants.PREFIXS + UizaData.getInstance().getEntityCover(), ivVideoCover);

            rootView.addView(ivVideoCover);
            rootView.addView(realtimeBlurView);

            progressBar.bringToFront();
        }
    }

    public void removeVideoCover() {
        if (ivVideoCover != null && realtimeBlurView != null) {
            rootView.removeView(ivVideoCover);
            rootView.removeView(realtimeBlurView);
            ivVideoCover = null;
            realtimeBlurView = null;
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

    public UizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.uiza_ima_video_core_rl, this);
        activity = ((BaseActivity) getContext());
        findViews();
        UizaUtil.resizeLayout(rootView, llMid);
    }

    private void findViews() {
        rootView = (RelativeLayout) findViewById(R.id.root_view);
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
        previewTimeBarLayout.setPreviewLoader(uizaPlayerManager);
        uizaPlayerManager.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                if (progressCallback != null) {
                    progressCallback.onAdProgress(currentMls, s, duration, percent);
                }
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " onVideoProgress video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
                trackProgress(s, percent);
                if (progressCallback != null) {
                    progressCallback.onVideoProgress(currentMls, s, duration, percent);
                }
            }
        });
        uizaPlayerManager.setDebugCallback(new UizaPlayerManager.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                LLog.d(TAG, "onUpdateButtonVisibilities");
                updateButtonVisibilities();
            }
        });

        //set volume max in first play
        seekbarVolume.setMax(100);
        setProgressSeekbar(seekbarVolume, 99);

        //set bightness max in first play
        firstBrightness = LScreenUtil.getCurrentBrightness(getContext()) * 100 / 255 + 1;
        LLog.d(TAG, "firstBrightness " + firstBrightness);
        seekbarBirghtness.setMax(100);
        setProgressSeekbar(seekbarBirghtness, firstBrightness);
    }

    private int oldPercent = Constants.NOT_FOUND;

    private void trackProgress(int s, int percent) {
        //track event view (after video is played 5s)
        if (s == 5) {
            LLog.d(TAG, "onVideoProgress -> track view");
            trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_VIEW));
        }

        if (oldPercent == percent) {
            return;
        }
        LLog.d(TAG, "trackProgress percent: " + percent);
        oldPercent = percent;
        if (percent == 25) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "25", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == 50) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "50", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == 75) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "75", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == 99) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "100", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        }
    }

    public void onStateReadyFirst() {
        LLog.d(TAG, "onStateReadyFirst");
        if (callback != null) {
            callback.isInitResult(true);
        }
        //track event video_starts
        trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_VIDEO_STARTS));
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
        LLog.d(TAG, "onStartPreview");
    }

    @Override
    public void onStopPreview(PreviewView previewView) {
        LLog.d(TAG, "onStopPreview");
        uizaPlayerManager.resumeVideo();
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {
        LLog.d(TAG, "onPreview progress " + progress);
    }

    private boolean isExoShareClicked;

    @Override
    public void onClick(View v) {
        if (v == exoFullscreenIcon) {
            UizaUtil.setUIFullScreenIcon(getContext(), exoFullscreenIcon);
            LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
        } else if (v == exoBackScreen) {
            if (isLandscape) {
                exoFullscreenIcon.performClick();
            } else {
                if ((BaseActivity) getContext() != null) {
                    ((BaseActivity) getContext()).onBackPressed();
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
            //TODO
            LToast.show(getContext(), "Click exoPlaylist");
        } else if (v == exoHearing) {
            View view = UizaUtil.getBtAudio(debugRootView);
            if (view != null) {
                UizaUtil.getBtAudio(debugRootView).performClick();
            }
        } else if (v == exoPictureInPicture) {
            clickPiP();
        } else if (v == exoShare) {
            //TODO
            LSocialUtil.share((BaseActivity) getContext(), isLandscape, "Share");
            if(isLandscape){
                LScreenUtil.hideNavigationBar(activity);
            }
            isExoShareClicked = true;
        } else if (v.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = uizaPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                uizaPlayerManager.getTrackSelectionHelper().showSelectionDialog((BaseActivity) getContext(), ((Button) v).getText(), mappedTrackInfo, (int) v.getTag());
            }
        }
    }

    private boolean isLandscape;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((BaseActivity) getContext() != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LScreenUtil.hideDefaultControls((BaseActivity) getContext());
                isLandscape = true;
            } else {
                LScreenUtil.showDefaultControls((BaseActivity) getContext());
                isLandscape = false;
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
                Button button = new Button(getContext());
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
            LLog.d(TAG, "seekbarVolume onProgressChanged " + progress + " -> " + ((float) progress / 100));
            uizaPlayerManager.setVolume(((float) progress / 100));
        } else if (seekBar == seekbarBirghtness) {
            LLog.d(TAG, "seekbarBirghtness onProgressChanged " + progress);
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
            LScreenUtil.setBrightness(getContext(), progress);
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
        LLog.d(TAG, "clickPiP");
        if (getContext() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            LLog.d(TAG, "clickPiP if");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
            ((BaseActivity) getContext()).startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            LLog.d(TAG, "clickPiP else");
            initializePiP();
        }
    }

    public void initializePiP() {
        if (getContext() == null) {
            return;
        }
        LToast.show(getContext(), "initializePiP");
        getContext().startService(new Intent(getContext(), FloatingUizaVideoService.class));
        ((BaseActivity) getContext()).onBackPressed();
    }

    public SimpleExoPlayer getPlayer() {
        return uizaPlayerManager.getPlayer();
    }

    private void getLinkPlay() {
        UizaService service = RestClientV2.createService(UizaService.class);
        Auth auth = LPref.getAuth(activity, gson);
        if (auth == null || auth.getData().getAppId() == null) {
            ((BaseActivity) activity).showDialogError("Error auth == null || auth.getAppId() == null");
            return;
        }
        String appId = auth.getData().getAppId();
        LLog.d(TAG, "getLinkPlay entityId: " + UizaData.getInstance().getEntityId() + ", appId: " + appId);
        //API v2
        ((BaseActivity) getContext()).subscribe(service.getLinkPlayV2(UizaData.getInstance().getEntityId(), appId), new ApiSubscriber<GetLinkPlay>() {
            @Override
            public void onSuccess(GetLinkPlay getLinkPlay) {
                List<String> listLinkPlay = new ArrayList<>();
                List<Mpd> mpdList = getLinkPlay.getMpd();
                for (Mpd mpd : mpdList) {
                    if (mpd.getUrl() != null) {
                        listLinkPlay.add(mpd.getUrl());
                    }
                }
                LLog.d(TAG, "getLinkPlayV2 toJson: " + gson.toJson(listLinkPlay));
                if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                    LLog.d(TAG, "listLinkPlay == null || listLinkPlay.isEmpty()");
                    ((BaseActivity) activity).showDialogOne(activity.getString(R.string.has_no_linkplay), true);
                    return;
                }

                String linkPlay = listLinkPlay.get(0);
                initData(linkPlay, UizaData.getInstance().getUrlIMAAd(), UizaData.getInstance().getUrlThumnailsPreviewSeekbar(), createDummySubtitle());
                onResume();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.d(TAG, "onFail getLinkDownloadV2: " + e.toString());
                ((BaseActivity) getContext()).handleException(e);
                if (callback != null) {
                    callback.isInitResult(false);
                }
            }
        });
        //End API v2
    }

    private List<Subtitle> createDummySubtitle() {
        String json = "[\n" +
                "                {\n" +
                "                    \"id\": \"18414566-c0c8-4a51-9d60-03f825bb64a9\",\n" +
                "                    \"name\": \"\",\n" +
                "                    \"type\": \"subtitle\",\n" +
                "                    \"url\": \"//dev-static.uiza.io/subtitle_56a4f990-17e6-473c-8434-ef6c7e40bba1_en_1522812430080.vtt\",\n" +
                "                    \"mine\": \"vtt\",\n" +
                "                    \"language\": \"en\",\n" +
                "                    \"isDefault\": \"0\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"271787a0-5d23-4a35-a10a-5c43fdcb71a8\",\n" +
                "                    \"name\": \"\",\n" +
                "                    \"type\": \"subtitle\",\n" +
                "                    \"url\": \"//dev-static.uiza.io/subtitle_56a4f990-17e6-473c-8434-ef6c7e40bba1_vi_1522812445904.vtt\",\n" +
                "                    \"mine\": \"vtt\",\n" +
                "                    \"language\": \"vi\",\n" +
                "                    \"isDefault\": \"0\"\n" +
                "                }\n" +
                "            ]";
        Subtitle[] subtitles = gson.fromJson(json, new TypeToken<Subtitle[]>() {
        }.getType());
        LLog.d(TAG, "createDummySubtitle subtitles " + gson.toJson(subtitles));
        List subtitleList = Arrays.asList(subtitles);
        LLog.d(TAG, "createDummySubtitle subtitleList " + gson.toJson(subtitleList));
        return subtitleList;
    }

    public interface Callback {
        public void isInitResult(boolean isInitSuccess);
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
                LLog.d(TAG, "onFail getLinkDownloadV2: " + e.toString());
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
                LLog.d(TAG, "trackUiza getEntityName: " + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
            }

            @Override
            public void onFail(Throwable e) {
                ((BaseActivity) getContext()).handleException(e);
            }
        });
    }
}
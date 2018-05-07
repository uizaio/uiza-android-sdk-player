package vn.loitp.uizavideo.view.floatview;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.manager.FloatUizaPlayerManager;
import vn.loitp.uizavideo.listerner.ProgressCallback;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.views.LToast;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class FloatUizaIMAVideo extends RelativeLayout implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Gson gson = new Gson();//TODO remove
    private PlayerView playerView;
    private FloatUizaPlayerManager floatUizaPlayerManager;
    private ProgressBar progressBar;

    public PlayerView getPlayerView() {
        return playerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void init(Callback callback) {
        if (UizaData.getInstance().getEntityId() == null || UizaData.getInstance().getEntityId().isEmpty()) {
            LToast.show(getContext(), "entityId cannot be null or empty");
            return;
        }
        this.callback = callback;
        if (floatUizaPlayerManager != null) {
            LLog.d(TAG, "init uizaPlayerManager != null");
            floatUizaPlayerManager.release();
            countTryLinkPlayError = 0;
        }

        checkToSetUp();
        LUIUtil.showProgressBar(progressBar);

        //cannot delete delay below, only works after 500mls
        //TODO
        /*LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                //track event eventype display
                trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_DISPLAY));

                //track event plays_requested
                trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_PLAYS_REQUESTED));
            }
        });*/
    }

    private int countTryLinkPlayError = 0;

    /*public void tryNextLinkPlay() {
        countTryLinkPlayError++;
        LToast.show(getContext(), activity.getString(R.string.cannot_play_will_try) + "\n" + countTryLinkPlayError);
        LLog.d(TAG, "tryNextLinkPlay countTryLinkPlayError " + countTryLinkPlayError);
        uizaPlayerManager.release();
        checkToSetUp();
    }*/

    private void checkToSetUp() {
        LLog.d(TAG, "checkToSetUp");
        initData("https://cdn-vn-cache-4.uiza.io:443/0fa01cc4bc264023850069c3e07a0a38/wNAVjUC3/package/playlist.mpd", UizaData.getInstance().getUrlIMAAd(), UizaData.getInstance().getUrlThumnailsPreviewSeekbar(), null);
        onResume();
    }

    public FloatUizaIMAVideo(Context context) {
        super(context);
        onCreate();
    }

    public FloatUizaIMAVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public FloatUizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatUizaIMAVideo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        LLog.d(TAG, "onCreate");
        inflate(getContext(), R.layout.uiza_float_ima_video_core_rl, this);
        findViews();
        //UizaUtil.resizeLayout(rootView, llMid);
    }

    private void findViews() {
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(getContext(), R.color.White));
        playerView = findViewById(R.id.player_view);
    }

    public void initData(String linkPlay, String urlIMAAd, String urlThumnailsPreviewSeekbar, List<Subtitle> subtitleList) {
        LLog.d(TAG, "initData");
        floatUizaPlayerManager = new FloatUizaPlayerManager(this, linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, subtitleList);
        floatUizaPlayerManager.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, int s, float duration, int percent) {
                LLog.d(TAG, TAG + " onVideoProgress video progress currentMls: " + currentMls + ", s:" + s + ", duration: " + duration + ",percent: " + percent + "%");
            }
        });
        floatUizaPlayerManager.setDebugCallback(new FloatUizaPlayerManager.DebugCallback() {
            @Override
            public void onUpdateButtonVisibilities() {
                LLog.d(TAG, "onUpdateButtonVisibilities");
                updateButtonVisibilities();
            }
        });
    }

    /*private void trackProgress(int s, int percent) {
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
        if (percent == Constants.PLAYTHROUGH_25) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "25", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_50) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "50", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_75) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "75", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        } else if (percent == Constants.PLAYTHROUGH_100) {
            trackUiza(UizaData.getInstance().createTrackingInput(activity, "100", UizaData.EVENT_TYPE_PLAY_THROUGHT));
        }
    }*/

    public void onStateReadyFirst() {
        LLog.d(TAG, "onStateReadyFirst");
        //track event video_starts
        //trackUiza(UizaData.getInstance().createTrackingInput(activity, UizaData.EVENT_TYPE_VIDEO_STARTS));
    }

    public void setProgressSeekbar(SeekBar seekbar, int progressSeekbar) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekbar.setProgress(progressSeekbar, true);
        } else {
            seekbar.setProgress(progressSeekbar);
        }*/
        seekbar.setProgress(progressSeekbar);
    }

    public void onDestroy() {
        if (floatUizaPlayerManager != null) {
            floatUizaPlayerManager.release();
        }
    }

    public void onResume() {
        if (isExoShareClicked) {
            isExoShareClicked = false;
        } else {
            if (floatUizaPlayerManager != null) {
                floatUizaPlayerManager.init();
            }
        }
    }

    public void onPause() {
        if (isExoShareClicked) {
            //do nothing
        } else {
            if (floatUizaPlayerManager != null) {
                floatUizaPlayerManager.reset();
            }
        }
    }

    private boolean isExoShareClicked;

    @Override
    public void onClick(View v) {
    }

    private boolean isLandscape;

    public void updateButtonVisibilities() {
        if (floatUizaPlayerManager.getPlayer() == null) {
            return;
        }
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = floatUizaPlayerManager.getTrackSelector().getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }
        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(getContext());
                int label;
                switch (floatUizaPlayerManager.getPlayer().getRendererType(i)) {
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
            }
        }
    }

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 6969;

    /*private void clickPiP() {
        LLog.d(TAG, "clickPiP");
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            LLog.d(TAG, "clickPiP if");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            LLog.d(TAG, "clickPiP else");
            initializePiP();
        }
    }*/

    /*public void initializePiP() {
        if (activity == null) {
            return;
        }
        LToast.show(activity, "initializePiP");
        activity.startService(new Intent(activity, FloatingUizaVideoService.class));
        (activity).onBackPressed();
    }*/

    public SimpleExoPlayer getPlayer() {
        return floatUizaPlayerManager.getPlayer();
    }

    /*private void getLinkPlay() {
        UizaService service = RestClientV2.createService(UizaService.class);
        Auth auth = LPref.getAuth(getContext(), gson);
        //TODO
        *//*if (auth == null || auth.getData().getAppId() == null) {
            ((BaseActivity) activity).showDialogError("Error auth == null || auth.getAppId() == null");
            return;
        }*//*
        String appId = auth.getData().getAppId();
        LLog.d(TAG, "getLinkPlay entityId: " + UizaData.getInstance().getEntityId() + ", appId: " + appId);
        //TODO
        *//*activity.subscribe(service.getLinkPlayV2(UizaData.getInstance().getEntityId(), appId), new ApiSubscriber<GetLinkPlay>() {
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
                if (callback != null) {
                    callback.isInitResult(false, null, null);
                }
            }
        });*//*
    }*/

    /*private void getDetailEntity() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(UizaData.getInstance().getEntityId());
        //jsonBodyGetDetailEntity.setId(Constants.ENTITYID_WITH_SUBTITLE);

        //TODO
        *//*((BaseActivity) activity).subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntity) {
                LLog.d(TAG, "getDetailEntity entityId " + UizaData.getInstance().getEntityId() + " -> " + gson.toJson(getDetailEntity));
                mGetDetailEntity = getDetailEntity;
                isGetDetailEntityDone = true;
                checkToSetUp();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntity onFail " + e.toString());
                //((BaseActivity) activity).showDialogError("Cannot get detail of this entity.");
            }
        });*//*
    }*/

    public interface Callback {
        public void isInitResult(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity);

        public void onClickListEntityRelation(Item item, int position);

        public void onClickBack();
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
                LLog.d(TAG, "trackUiza getEntityName: " + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough() + " ==> " + gson.toJson(tracking));
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "trackUiza onFail " + e.toString() + "\n->>>" + uizaTracking.getEntityName() + ", getEventType: " + uizaTracking.getEventType() + ", getPlayThrough: " + uizaTracking.getPlayThrough());
                ((BaseActivity) getContext()).showDialogError("Cannot track this entity");
            }
        });
    }
}
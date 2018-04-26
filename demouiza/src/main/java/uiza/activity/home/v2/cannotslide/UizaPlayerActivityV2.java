package uiza.activity.home.v2.cannotslide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uiza.app.LSApplication;
import uiza.uiza.com.demo.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.Subtitle;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.uizavideo.listerner.ProgressCallback;
import vn.loitp.views.uizavideo.view.rl.UizaIMAVideo;

import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_COVER;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_ID;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_TITLE;

public class UizaPlayerActivityV2 extends BaseActivity {
    private String entityId;
    private String entityCover;
    private String entityTitle;
    private UizaIMAVideo uizaIMAVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uizaIMAVideo = (UizaIMAVideo) findViewById(R.id.uiza_video);
        entityId = getIntent().getStringExtra(KEY_UIZA_ENTITY_ID);
        entityCover = getIntent().getStringExtra(KEY_UIZA_ENTITY_COVER);
        entityTitle = getIntent().getStringExtra(KEY_UIZA_ENTITY_TITLE);
        LLog.d(TAG, "entityId " + entityId);
        LLog.d(TAG, "entityCover " + entityCover);
        LLog.d(TAG, "entityTitle " + entityTitle);
        if (entityId == null || entityId.isEmpty()) {
            showDialogError("entityId == null || entityId.isEmpty()");
            return;
        }
        getLinkPlay();
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
        Subtitle[] subtitles = LSApplication.getInstance().getGson().fromJson(json, new TypeToken<Subtitle[]>() {
        }.getType());
        LLog.d(TAG, "createDummySubtitle subtitles " + LSApplication.getInstance().getGson().toJson(subtitles));
        List subtitleList = Arrays.asList(subtitles);
        LLog.d(TAG, "createDummySubtitle subtitleList " + LSApplication.getInstance().getGson().toJson(subtitleList));
        return subtitleList;
    }

    private void init() {
        LLog.d(TAG, "init");
        String linkPlay = getString(loitp.core.R.string.url_dash);
        String urlIMAAd = getString(loitp.core.R.string.ad_tag_url);
        //String urlIMAAd = null;
        String urlThumnailsPreviewSeekbar = getString(loitp.core.R.string.url_thumbnails);
        //String urlThumnailsPreviewSeekbar = null;
        uizaIMAVideo.initData(linkPlay, urlIMAAd, urlThumnailsPreviewSeekbar, createDummySubtitle());
        uizaIMAVideo.onResume();
        setListener();
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.uiza_player_activity;
    }

    private void getLinkPlay() {
        UizaService service = RestClientV2.createService(UizaService.class);
        Auth auth = LPref.getAuth(activity, LSApplication.getInstance().getGson());
        if (auth == null || auth.getData().getAppId() == null) {
            showDialogError("Error auth == null || auth.getAppId() == null");
            return;
        }
        String appId = auth.getData().getAppId();
        //API v2
        subscribe(service.getLinkPlayV2(entityId, appId), new ApiSubscriber<GetLinkPlay>() {
            @Override
            public void onSuccess(GetLinkPlay getLinkPlay) {
                List<String> listLinkPlay = new ArrayList<>();
                List<Mpd> mpdList = getLinkPlay.getMpd();
                for (Mpd mpd : mpdList) {
                    if (mpd.getUrl() != null) {
                        listLinkPlay.add(mpd.getUrl());
                    }
                }
                LLog.d(TAG, "getLinkPlayV2 toJson: " + LSApplication.getInstance().getGson().toJson(listLinkPlay));
                if (listLinkPlay == null || listLinkPlay.isEmpty()) {
                    LLog.d(TAG, "listLinkPlay == null || listLinkPlay.isEmpty()");
                    showDialogOne(getString(R.string.has_no_linkplay), true);
                    return;
                }
                getDetailEntity();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.d(TAG, "onFail getLinkDownloadV2: " + e.toString());
                handleException(e);
            }
        });
        //End API v2
    }

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

    private void getDetailEntity() {
        //API v2
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(entityId);

        subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntityV2) {
                LLog.d(TAG, "getDetailEntityV2 onSuccess " + LSApplication.getInstance().getGson().toJson(getDetailEntityV2));
                init();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntityV2 onFail " + e.toString());
                handleException(e);
            }
        });
        //EndAPI v2
    }

    ;

    public void playOnClickItem(Item item, int position) {
        LLog.d(TAG, "onClick " + position);
        Intent intent = new Intent(activity, UizaPlayerActivityV2.class);
        intent.putExtra(KEY_UIZA_ENTITY_ID, item.getId());
        intent.putExtra(KEY_UIZA_ENTITY_COVER, item.getThumbnail());
        intent.putExtra(KEY_UIZA_ENTITY_TITLE, item.getName());
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uizaIMAVideo.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uizaIMAVideo.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uizaIMAVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UizaIMAVideo.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                LLog.d(TAG, "onActivityResult RESULT_OK");
                uizaIMAVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        LLog.d(TAG, TAG + " addListener");
        uizaIMAVideo.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                LLog.d(TAG, "onTimelineChanged");
            }

            @Override
            public void onSeekProcessed() {
                LLog.d(TAG, "onTimelineChanged");
            }
        });
        uizaIMAVideo.getPlayer().addAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioEnabled");
            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                LLog.d(TAG, "onAudioSessionId");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onAudioDecoderInitialized");
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {
                LLog.d(TAG, "onAudioInputFormatChanged");
            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
                LLog.d(TAG, "onAudioSinkUnderrun");
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onAudioDisabled");
            }
        });
        uizaIMAVideo.setProgressCallback(new ProgressCallback() {
            @Override
            public void onAdProgress(float currentMls, float duration, int percent) {
                LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }

            @Override
            public void onVideoProgress(float currentMls, float duration, int percent) {
                LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
            }
        });
        uizaIMAVideo.getPlayer().addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoEnabled");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                LLog.d(TAG, "onVideoDecoderInitialized");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                LLog.d(TAG, "onVideoInputFormatChanged");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                LLog.d(TAG, "onDroppedFrames");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                LLog.d(TAG, "onAudioDisabled");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                LLog.d(TAG, "onRenderedFirstFrame");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                LLog.d(TAG, "onVideoDisabled");
            }
        });
        uizaIMAVideo.getPlayer().addMetadataOutput(new MetadataOutput() {
            @Override
            public void onMetadata(Metadata metadata) {
                LLog.d(TAG, "onMetadata");
            }
        });
        uizaIMAVideo.getPlayer().addTextOutput(new TextOutput() {
            @Override
            public void onCues(List<Cue> cues) {
                LLog.d(TAG, "onCues");
            }
        });
    }
}

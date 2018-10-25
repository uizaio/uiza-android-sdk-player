package testlibuiza.sample.v3.linkplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZCallback;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class PlayerActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;
    private EditText etLinkPlay;
    private Button btPlay;

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
        return R.layout.player_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        etLinkPlay = (EditText) findViewById(R.id.et_link_play);
        btPlay = (Button) findViewById(R.id.bt_play);
        uzVideo.setUZCallback(this);

        etLinkPlay.setText("https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");

        //listLinkPlay.add("https://toanvk-live.uizacdn.net/893db5e8bb3943bfb12894fec56c8875-live/hi-uaqsv9as/manifest.mpd");
        //listLinkPlay.add("http://112.78.4.162/drm/test/hevc/playlist.mpd");
        //listLinkPlay.add("http://112.78.4.162/6yEB8Lgd/package/playlist.mpd");
        //listLinkPlay.add("https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd");

        LUIUtil.setLastCursorEditText(etLinkPlay);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkPlay = etLinkPlay.getText().toString();
                if (linkPlay == null || linkPlay.isEmpty()) {
                    LToast.show(activity, "Please input correct linkplay");
                } else {
                    UZUtil.initLinkPlay(activity, uzVideo, linkPlay);
                }
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {

    }

    @Override
    public void onClickBack() {

    }

    @Override
    public void onClickPip(Intent intent) {

    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {

    }

    @Override
    public void onSkinChange() {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uzVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}

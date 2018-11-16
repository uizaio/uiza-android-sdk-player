package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZCallback;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinCodeUZTimebarUTubeActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;
    private View shadow;

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
        return R.layout.activity_uiza_custom_skin_u_tube;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.u_tube_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);

        //config uztimebar bottom
        uzVideo.setBackgroundColorUZVideoRootView(Color.TRANSPARENT);
        uzVideo.setUzTimebarBottom();
        //uzVideo.setViewsAlwaysVisible(uzVideo.getUZTimeBar(), uzVideo.getIbBackScreenIcon());

        //shadow background
        shadow = (View) uzVideo.findViewById(R.id.bkg_shadow);
        uzVideo.setMarginDependOnUZTimeBar(shadow);

        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
        onBackPressed();
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
    public void onScreenRotate(boolean isLandscape) {
        uzVideo.setMarginDependOnUZTimeBar(shadow);
    }

    @Override
    public void onError(UZException e) {
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

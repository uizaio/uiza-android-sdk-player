package testlibuiza.activity;

import android.content.Intent;
import android.os.Bundle;

import testlibuiza.app.R;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.rl.video.UZCallback;
import vn.uiza.uzv3.view.rl.video.UZVideo;

public class PlayerActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzVideo.setUZCallback(this);
        UZUtil.initEntity(activity, uzVideo, "put the entity id here");
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_player;
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
}

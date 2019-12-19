package testlibuiza.sample.v3.fullscreen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.restapi.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.model.v5.UizaPlayback;

public class PortraitFullScreenActivity extends AppCompatActivity implements UZCallback {

    private UZVideo uzVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.fullscreen_skin);
        super.onCreate(savedInstanceState);
        Constants.setDebugMode(false);
        setContentView(R.layout.activity_fullscreen_portrait);
        uzVideo = findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);
        LActivityUtil.toggleFullScreen(this);
        uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT); // optional
        uzVideo.init("78ded059-a268-44e8-8b39-115612c69187");
        uzVideo.setFreeSize(true); // must be set this line

    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
                             ResultGetLinkPlay resultGetLinkPlay, UizaPlayback data) {
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {

    }

    @Override
    public void onSkinChange() {

    }

    @Override
    public void onScreenRotate(boolean isLandscape) {

    }

    @Override
    public void onError(UZException e) {

    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPause();
        super.onPause();
    }

}

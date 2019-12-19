package testlibuiza.sample.v5;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.restapi.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.model.v5.UizaPlayback;
import vn.uiza.restapi.model.v5.live.LiveEntity;

public class LivePlaybackActivity extends AppCompatActivity implements UZCallback {

    private UZVideo uzVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.fullscreen_skin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_portrait);
        uzVideo = findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);
        LActivityUtil.toggleFullScreen(this);
        uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT); // optional
        setupContent();
    }

    private void setupContent() {
        LiveEntity entity = getIntent().getParcelableExtra("extra_live_entity");
        if (entity == null) {
            Toast.makeText(this, "Live entity = null", Toast.LENGTH_LONG).show();
            (new Handler()).postDelayed(() -> finish(), 1000);
            return;
        }
        UizaPlayback playback = entity.getUizaPlayback();
        if (playback != null && entity.isOnline()) {
            UZUtil.initLiveEntity(this, uzVideo, playback);
            uzVideo.setFreeSize(true); // must be set this line
        } else {
            Toast.makeText(this, "No playback or Offline", Toast.LENGTH_LONG).show();
            (new Handler()).postDelayed(() -> finish(), 2000);
        }

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

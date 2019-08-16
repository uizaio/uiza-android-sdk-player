package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import testlibuiza.R;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 27/2/2019.
 */

public class CustomSkinCodeUZTimebarActivity extends AppCompatActivity
        implements UZItemClickListener, UZVideoStateChangedListener, UZPlayerStateChangedListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UZVideo uzVideo;
    private View shadow;
    private LinearLayout ll;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_uz_timebar);
        uzVideo = findViewById(R.id.uiza_video);
        ll = findViewById(R.id.ll);
        LUIUtil.setMarginDimen(ll, 0, -uzVideo.getPixelAdded() / 2, 0, 0);
        pb = findViewById(R.id.p);
        pb.setVisibility(View.VISIBLE);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
        shadow = uzVideo.findViewById(R.id.bkg_shadow);
        uzVideo.setMarginDependOnUZTimeBar(shadow);
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());
        checkId(getIntent());
    }

    private void checkId(Intent intent) {
        if (intent == null) {
            return;
        }
        String thumb = intent.getStringExtra(Constants.KEY_UIZA_THUMBNAIL);
        uzVideo.setUrlImgThumbnail(thumb);
        String metadataId = intent.getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = intent.getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(activity, uzVideo, null);
                } else {
                    UZUtil.initEntity(activity, uzVideo, null);
                }
            } else {
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
            ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {

    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onItemClick(View view) {
        if (view.getId() == R.id.exo_back_screen) {
            if (!uzVideo.isLandscape()) {
                onBackPressed();
            }
        }
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }

    @Override
    public void onScreenRotated(boolean isLandscape) {
        uzVideo.setMarginDependOnUZTimeBar(uzVideo.getBkg());
        uzVideo.setMarginDependOnUZTimeBar(shadow);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}

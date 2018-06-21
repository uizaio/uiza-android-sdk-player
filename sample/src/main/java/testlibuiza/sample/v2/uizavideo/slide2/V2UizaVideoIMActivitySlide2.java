package testlibuiza.sample.v2.uizavideo.slide2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import testlibuiza.R;
import testlibuiza.sample.v2.uizavideo.slide2.detail.WWLVideoMetaInfoFragment;
import testlibuiza.sample.v2.uizavideo.slide2.detail.WWLVideoPlayerFragment;
import testlibuiza.sample.v2.uizavideo.slide2.detail.WWLVideoUpNextFragment;
import testlibuiza.sample.v2.uizavideo.slide2.interfaces.FragmentHost;
import testlibuiza.sample.v2.uizavideo.slide2.utils.WWLVideoDataset;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.views.wwlvideo.layout.WWLVideo;
import vn.loitp.views.wwlvideo.utils.WWLViewHelper;

public class V2UizaVideoIMActivitySlide2 extends BaseActivity implements WWLVideo.Listener, FragmentHost {
    private WWLVideo wwlVideo;
    private float mLastAlpha;
    private FrameLayout mPlayerFragmentContainer;
    private WWLVideoPlayerFragment wwlVideoPlayerFragment;
    private WWLVideoUpNextFragment wwlVideoUpNextFragment;
    private WWLVideoMetaInfoFragment wwlVideoMetaInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.wwlVideo = (WWLVideo) findViewById(R.id.watch_while_layout);
        this.wwlVideo.setListener(this);

        this.mPlayerFragmentContainer = (FrameLayout) findViewById(R.id.player_fragment_container);
        this.wwlVideoPlayerFragment = (WWLVideoPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);
        this.wwlVideoUpNextFragment = (WWLVideoUpNextFragment) getSupportFragmentManager().findFragmentById(R.id.up_next_fragment);
        this.wwlVideoMetaInfoFragment = (WWLVideoMetaInfoFragment) getSupportFragmentManager().findFragmentById(R.id.meta_info_fragment);
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
        return R.layout.test_uiza_ima_video_activity_rl_slide_2;
    }

    @Override
    public void onWWLSliding(float offset) {
        //LLog.d(TAG, "onWWLSliding " + offset);
        wwlVideoPlayerFragment.getUizaIMAVideo().hideController();
        float alpha;
        if (offset > 2.0f) {
            alpha = this.mLastAlpha * (3.0f - offset);
        } else {
            if (offset > 1.0f) {
                alpha = 0.25f + (0.75f * (2.0f - offset));
            } else {
                alpha = 1.0f;
            }
            if (offset >= 0.0f && offset <= 1.0f) {
                updateStatusBarAlpha(1.0f - offset);
            }
        }
        updatePlayerAlpha(alpha);
        this.mLastAlpha = alpha;
    }

    @Override
    public void onWWLClicked() {
        LLog.d(TAG, "onWWLClicked");
        if (this.wwlVideo.mState == WWLVideo.STATE_MINIMIZED) {
            this.wwlVideo.maximize(true);
        }
        /*if (this.wwlVideo.mState == WWLVideo.STATE_MAXIMIZED) {
            this.wwlVideoPlayerFragment.toggleControls();
        }*/
    }

    @Override
    public void onWWLHided() {
        LLog.d(TAG, "onWWLHided");
        this.wwlVideoPlayerFragment.stopPlay();
    }

    @Override
    public void onWWLminimized() {
        LLog.d(TAG, "onWWLminimized");
        wwlVideoPlayerFragment.getUizaIMAVideo().hideController();
        wwlVideoPlayerFragment.getUizaIMAVideo().hideControllerOnTouch(false);
        this.mLastAlpha = 0.0f;
        //this.wwlVideoPlayerFragment.hideControls();
    }

    @Override
    public void onWWLmaximized() {
        LLog.d(TAG, "onWWLmaximized");
        wwlVideoPlayerFragment.getUizaIMAVideo().hideControllerOnTouch(true);
        this.mLastAlpha = 1.0f;
    }

    @Override
    public void goToDetail(WWLVideoDataset.DatasetItem item) {
        LLog.d(TAG, "goToDetail");
        if (this.wwlVideo.mState == WWLVideo.STATE_HIDED) {
            this.wwlVideo.mState = WWLVideo.STATE_MAXIMIZED;
            this.wwlVideo.mIsFullscreen = false;
            if (this.wwlVideo.canAnimate()) {
                this.wwlVideo.setAnimatePos(this.wwlVideo.mMaxY);
            }
            this.wwlVideo.reset();
        }
        this.wwlVideo.maximize(true);

        this.wwlVideoPlayerFragment.startPlay(item);
        if (this.wwlVideoUpNextFragment != null) {
            this.wwlVideoUpNextFragment.updateItem(item);
        }
        if (this.wwlVideoMetaInfoFragment != null) {
            this.wwlVideoMetaInfoFragment.updateItem(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (wwlVideo.mState == WWLVideo.STATE_MAXIMIZED) {
            wwlVideo.minimize(true);
        } else {
            super.onBackPressed();
        }
    }

    private void updateStatusBarAlpha(float alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            int color = getResources().getColor(R.color.colorPrimaryDark);
            int color2 = Color.BLACK;
            int color3 = WWLViewHelper.evaluateColorAlpha(Math.max(0.0f, Math.min(1.0f, alpha)), color, color2);
            getWindow().setStatusBarColor(color3);
        }
    }

    private void updatePlayerAlpha(float alpha) {
        this.mPlayerFragmentContainer.setAlpha(alpha);
    }
}

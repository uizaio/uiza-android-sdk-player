package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;

/**
 * Created by loitp on 27/2/2019.
 */

public class CustomSkinCodeSeekbarActivity extends AppCompatActivity implements UZCallback,
        UZItemClick {

    private UZVideo uzVideo;
    private SeekBar seekBar;
    private final String TAG = getClass().getSimpleName();
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_seekbar);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        seekBar = (SeekBar) findViewById(R.id.sb);
        uzVideo.setAutoStart(true);
        uzVideo.hideUzTimebar();
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzVideo.onStopPreview(seekBar.getProgress());
            }
        });
        uzVideo.addProgressCallback(new ProgressCallback() {
            @Override
            public void onAdEnded() {
                seekBar.setMax((int) uzVideo.getDuration());
            }

            @Override
            public void onAdProgress(int s, int duration, int percent) {
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                seekBar.setProgress((int) currentMls);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage,
                    long duration) {
            }
        });
        uzVideo.addControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                seekBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }
        });
        uzVideo.setControllerShowTimeoutMs(15000);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (isInitSuccess) {
            seekBar.setMax((int) uzVideo.getDuration());
            updateUISeekbarPosition(false);
        }
    }

    private void updateUISeekbarPosition(final boolean isLandscape) {
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                int huzVideo = UzDisplayUtil.getHeightOfView(uzVideo);
                int hSeekbar = UzDisplayUtil.getHeightOfView(seekBar);
                if (isLandscape) {
                    UzDisplayUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar, 0, 0);
                } else {
                    UzDisplayUtil.setMarginPx(seekBar, 0, huzVideo - hSeekbar / 2, 0, 0);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
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
    public void onError(UzException e) {
        if (e == null) {
            return;
        }
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateUISeekbarPosition(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateUISeekbarPosition(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }
}

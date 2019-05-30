package testlibuiza.sample.v3.uzv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.exoplayer2.Player;
import testlibuiza.R;
import uizacoresdk.interfaces.UZAdStateChangedListener;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 27/2/2019.
 */

public class UZPlayerActivity extends AppCompatActivity
        implements UZVideoStateChangedListener, UZPlayerStateChangedListener, UZItemClickListener {
    private Activity activity;
    private UZVideo uzVideo;
    private TextView tvProgressAd;
    private TextView tvProgressVideo;
    private TextView tvStateVideo;
    private TextView tvBuffer;
    private TextView tvClickEvent;
    private TextView tvScreenRotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_cannot_slide_player);
        uzVideo = findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(true);
        uzVideo.setAutoStart(true);
        tvProgressAd = findViewById(R.id.tv_progress_ad);
        tvProgressVideo = findViewById(R.id.tv_progress_video);
        tvStateVideo = findViewById(R.id.tv_state_video);
        tvBuffer = findViewById(R.id.tv_buffer);
        tvClickEvent = findViewById(R.id.tv_click_event);
        tvScreenRotate = findViewById(R.id.tv_screen_rotate);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
                } else {
                    UZUtil.initEntity(activity, uzVideo, entityId);
                }
            } else {
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        }
        uzVideo.addOnTouchEvent(new UZPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed(float x, float y) {
                tvClickEvent.setText("onSingleTapConfirmed");
            }

            @Override
            public void onLongPress(float x, float y) {
                tvClickEvent.setText("onLongPress");
            }

            @Override
            public void onDoubleTap(float x, float y) {
                tvClickEvent.setText("onDoubleTap");
            }

            @Override
            public void onSwipeRight() {
                tvClickEvent.setText("onSwipeRight");
            }

            @Override
            public void onSwipeLeft() {
                tvClickEvent.setText("onSwipeLeft");
            }

            @Override
            public void onSwipeBottom() {
                tvClickEvent.setText("onSwipeBottom");
            }

            @Override
            public void onSwipeTop() {
                tvClickEvent.setText("onSwipeTop");
            }
        });
        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.play();
            }
        });
        findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.pause();
            }
        });
        findViewById(R.id.bt_next_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.seek(uzVideo.getCurrentPosition() + 10000);
            }
        });
        findViewById(R.id.bt_prev_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.seek(uzVideo.getCurrentPosition() - 10000);
            }
        });
        findViewById(R.id.bt_volume_on_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.toggleMuted();
            }
        });
        findViewById(R.id.bt_toggle_fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.toggleFullscreen();
            }
        });
        findViewById(R.id.bt_cc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.showCCPopup();
            }
        });
        findViewById(R.id.bt_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.showHQPopup();
            }
        });
        findViewById(R.id.bt_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.showSpeed();
            }
        });
        findViewById(R.id.bt_next_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.nextVideo();
            }
        });
        findViewById(R.id.bt_prev_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.previousVideo();
            }
        });

        findViewById(R.id.bt_stats_for_nerds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uzVideo.toggleStatsForNerds();
            }
        });
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

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }

        uzVideo.setUzAdStateChangedListener(new UZAdStateChangedListener() {
            @Override
            public void onAdProgress(int s, int duration, int percent) {
                tvProgressAd.setText("Ad: " + s + "/" + duration + " (s) => " + percent + "%");
            }


            @Override
            public void onAdEnded() {
                tvProgressAd.setText("onAdEnded");
            }

        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
            ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setListener();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            tvStateVideo.setText(
                    "onPlayerStateChanged STATE_BUFFERING, playWhenReady: " + playWhenReady);
        } else if (playbackState == Player.STATE_IDLE) {
            tvStateVideo.setText("onPlayerStateChanged STATE_IDLE, playWhenReady: " + playWhenReady);
        } else if (playbackState == Player.STATE_READY) {
            tvStateVideo.setText("onPlayerStateChanged STATE_READY, playWhenReady: " + playWhenReady);
        } else if (playbackState == Player.STATE_ENDED) {
            tvStateVideo.setText("onPlayerStateChanged STATE_ENDED, playWhenReady: " + playWhenReady);
        }
    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {
        tvProgressVideo.setText(
                "Video: " + currentMls + "/" + duration + " (mls) => " + percent + "%");
    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
        tvBuffer.setText("onBufferProgress bufferedPosition: " + bufferedPosition + "/" + duration + "(mls), bufferedPercentage: " + bufferedPercentage + "%");
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
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onScreenRotated(boolean isLandscape) {
        tvScreenRotate.setText("isLandscape " + isLandscape);
    }

    @Override
    public void onError(UZException e) {
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

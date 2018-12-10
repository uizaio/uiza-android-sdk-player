package testlibuiza.sample.v3.uzv3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.Player;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.listerner.ProgressCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

/**
 * Created by loitp on 7/16/2018.
 */

public class UZPlayerActivity extends BaseActivity implements UZCallback, UZItemClick {
    private UZVideo uzVideo;
    private TextView tvProgressAd;
    private TextView tvProgressVideo;
    private TextView tvStateVideo;
    private TextView tvBuffer;
    private TextView tvClickEvent;
    private TextView tvScreenRotate;
    private SeekBar sb;

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
        return R.layout.v3_cannot_slide_player;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_0);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_2);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(true);
        uzVideo.setAutoStart(true);

        sb = (SeekBar) findViewById(R.id.sb);
        sb.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        sb.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        tvProgressAd = (TextView) findViewById(R.id.tv_progress_ad);
        tvProgressVideo = (TextView) findViewById(R.id.tv_progress_video);
        tvStateVideo = (TextView) findViewById(R.id.tv_state_video);
        tvBuffer = (TextView) findViewById(R.id.tv_buffer);
        tvClickEvent = (TextView) findViewById(R.id.tv_click_event);
        tvScreenRotate = (TextView) findViewById(R.id.tv_screen_rotate);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.setControllerShowTimeoutMs(8000);

        String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
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
                uzVideo.resumeVideo();
            }
        });
        findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.pauseVideo();
            }
        });
        findViewById(R.id.bt_next_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.seekToForward(10000);
            }
        });
        findViewById(R.id.bt_prev_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.seekToBackward(10000);
            }
        });
        findViewById(R.id.bt_volume_on_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.toggleVolume();
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
        findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.showSharePopup();
            }
        });
        findViewById(R.id.bt_next_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.skipNextVideo();
            }
        });
        findViewById(R.id.bt_prev_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uzVideo.skipPreviousVideo();
            }
        });
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
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
        uzVideo.addProgressCallback(new ProgressCallback() {
            @Override
            public void onAdEnded() {
                sb.setMax((int) uzVideo.getDuration());
                tvProgressAd.setText("onAdEnded");
            }

            @Override
            public void onAdProgress(int s, int duration, int percent) {
                //LLog.d(TAG, TAG + " ad progress: " + currentMls + "/" + duration + " -> " + percent + "%");
                tvProgressAd.setText("Ad: " + s + "/" + duration + " (s) => " + percent + "%");
            }

            @Override
            public void onVideoProgress(long currentMls, int s, long duration, int percent) {
                //LLog.d(TAG, TAG + " video progress: " + currentMls + "/" + duration + " -> " + percent + "%");
                tvProgressVideo.setText("Video: " + currentMls + "/" + duration + " (mls) => " + percent + "%");
                sb.setProgress((int) currentMls);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    tvStateVideo.setText("onPlayerStateChanged STATE_BUFFERING, playWhenReady: " + playWhenReady);
                } else if (playbackState == Player.STATE_IDLE) {
                    tvStateVideo.setText("onPlayerStateChanged STATE_IDLE, playWhenReady: " + playWhenReady);
                } else if (playbackState == Player.STATE_READY) {
                    tvStateVideo.setText("onPlayerStateChanged STATE_READY, playWhenReady: " + playWhenReady);
                } else if (playbackState == Player.STATE_ENDED) {
                    tvStateVideo.setText("onPlayerStateChanged STATE_ENDED, playWhenReady: " + playWhenReady);
                }
            }

            @Override
            public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {
                tvBuffer.setText("onBufferProgress bufferedPosition: " + bufferedPosition + "/" + duration + "(mls), bufferedPercentage: " + bufferedPercentage + "%");
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            setListener();
            sb.setMax((int) uzVideo.getDuration());
        }
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
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
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

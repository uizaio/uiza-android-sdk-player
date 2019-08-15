package testlibuiza.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.autosize.UzImageButton;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.UzTrackItem;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerTvListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.view.UzPlayerView;
import java.util.List;
import testlibuiza.app.R;

public class PlayerActivity extends AppCompatActivity implements UzPlayerUiEventListener,
        UzPlayerEventListener, UzPlayerTvListener, UzPlayerView.ControllerStateCallback,
        UzItemClickListener {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UzPlayer uzPlayer;
    private UzImageButton uzibCustomHq;
    private UzImageButton uzibCustomAudio;
    private ScrollView sv;
    private LinearLayout llListHq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);

        //init skin
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_tv_custom);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        sv = findViewById(R.id.sv);
        llListHq = findViewById(R.id.ll_list_hq);
        uzPlayer = findViewById(R.id.uiza_video);
        uzibCustomHq = uzPlayer.findViewById(R.id.uzib_custom_hq);
        uzibCustomAudio = uzPlayer.findViewById(R.id.uzib_custom_audio);

        uzibCustomHq.setOnFocusChangeListener((view, b) -> uzPlayer.updateUiFocusChanged(view, b));
        uzibCustomAudio.setOnFocusChangeListener((view, b) -> uzPlayer.updateUiFocusChanged(view, b));

        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerTvListener(this);
        uzPlayer.setControllerStateCallback(this);
        uzPlayer.setUzItemClickListener(this);

        String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
        if (entityId == null) {
            String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
            UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
        } else {
            UzPlayerConfig.initVodEntity(uzPlayer, entityId);
        }

        uzibCustomHq.setOnClickListener(view -> displayUI(uzPlayer.getQualityList()));
        uzibCustomAudio.setOnClickListener(view -> displayUI(uzPlayer.getAudioList()));
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {

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
    public void onPlayerError(UzException exception) {

    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onPlayerRotated(boolean isLandscape) {

    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzPlayer.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzPlayer.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_REWIND");
                uzPlayer.seekToBackward(uzPlayer.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_FAST_FORWARD");
                uzPlayer.seekToForward(uzPlayer.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PLAY_PAUSE");
                uzPlayer.togglePlayPause();
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_STOP");
                uzPlayer.pause();
                return true;
            case KeyEvent.KEYCODE_BACK:
                LLog.d(TAG, "onKeyUp KEYCODE_BACK");
                onBackPressed();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                LLog.d(TAG, "onKeyUp KEYCODE_VOLUME_UP");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_VOLUME_DOWN");
                return true;
            case KeyEvent.KEYCODE_MENU:
                LLog.d(TAG, "onKeyUp KEYCODE_MENU");
                uzPlayer.toggleShowHideController();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_UP");
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_LEFT");
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_RIGHT");
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_DOWN");
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_CENTER");
                uzPlayer.showController();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        uzPlayer.updateUiFocusChanged(view, isFocus);
    }

    private void displayUI(List<UzTrackItem> uzTrackItemList) {
        sv.setVisibility(View.VISIBLE);
        llListHq.removeAllViews();
        for (int i = 0; i < uzTrackItemList.size(); i++) {
            UzTrackItem uzTrackItem = uzTrackItemList.get(i);
            final CheckedTextView c = uzTrackItem.getCheckedTextView();

            //customize here
            final Button bt = new Button(activity);
            bt.setAllCaps(false);
            bt.setFocusable(true);
            bt.setSoundEffectsEnabled(true);
            if (c.isChecked()) {
                bt.setText(c.getText().toString() + " -> format: " + uzTrackItem.getFormat()
                        .getFormat() + ", getProfile: " + uzTrackItem
                        .getFormat().getProfile() + " (✔)");
                bt.setBackgroundColor(Color.GREEN);
                bt.requestFocus();
            } else {
                bt.setText(c.getText().toString() + " -> format: " + uzTrackItem.getFormat()
                        .getFormat() + ", getProfile: " + uzTrackItem
                        .getFormat().getProfile());
                bt.setBackgroundColor(Color.WHITE);
            }
            bt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        bt.setBackgroundColor(Color.GREEN);
                        uzPlayer.showController();
                    } else {
                        bt.setBackgroundColor(Color.WHITE);
                    }
                }
            });
            bt.setOnClickListener(view -> {
                UzAnimationUtil.play(view, Techniques.Pulse);
                c.performClick();
                UzCommonUtil.actionWithDelayed(300, mls -> {
                    llListHq.removeAllViews();
                    llListHq.invalidate();
                    sv.setVisibility(View.INVISIBLE);
                });
            });
            llListHq.addView(bt);
        }
        llListHq.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (sv.getVisibility() == View.VISIBLE) {
            sv.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVisibilityChanged(boolean isShow) {
        if (!isShow) {
            if (sv.getVisibility() == View.VISIBLE) {
                sv.setVisibility(View.INVISIBLE);
            }
        }
    }
}

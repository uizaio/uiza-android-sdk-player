package testlibuiza.sample.v3.customhq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.google.android.exoplayer2.video.VideoListener;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.autosize.UzImageButton;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.UzTrackItem;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.util.UzPlayerUtil;
import java.util.List;
import testlibuiza.R;
import testlibuiza.app.LSApplication;

/**
 * Created by loitp on 12/10/2018.
 */

public class CustomHQActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerUiEventListener, UzItemClickListener {

    private Activity activity;
    private UzPlayer uzPlayer;
    private Button btCustomHq;
    private UzImageButton uzibCustomAudio;
    private LinearLayout llListHq;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.uiza_controller_hq_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_hq);
        uzPlayer = findViewById(R.id.uiza_video);
        btCustomHq = uzPlayer.findViewById(R.id.uzib_custom_hq);
        uzibCustomAudio = uzPlayer.findViewById(R.id.uzib_custom_audio);
        llListHq = findViewById(R.id.ll_list_hq);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                    float pixelWidthHeightRatio) {
                btCustomHq.setText("Auto " + width + "x" + height + " - " + UzPlayerUtil
                        .getFormatVideo(width, height).getProfile());
            }
        });

        final String entityId = LSApplication.entityIdDefaultVOD;
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);
        btCustomHq.setOnClickListener(view -> displayUI(uzPlayer.getQualityList()));
        uzibCustomAudio.setOnClickListener(view -> displayUI(uzPlayer.getAudioList()));
    }

    private void displayUI(List<UzTrackItem> uzTrackItemList) {
        llListHq.removeAllViews();
        for (int i = 0; i < uzTrackItemList.size(); i++) {
            UzTrackItem uzTrackItem = uzTrackItemList.get(i);
            final CheckedTextView c = uzTrackItem.getCheckedTextView();
            LLog.d(TAG,
                    i + ", getDescription: " + uzTrackItem.getDescription() + ", isChecked: " + c
                            .isChecked() + ", getFormat: " + uzTrackItem
                            .getFormat());

            //add space
            View view = new View(activity);
            view.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 5);
            view.setLayoutParams(layoutParams);
            llListHq.addView(view);

            //customize here
            final Button bt = new Button(activity);
            bt.setAllCaps(false);
            if (c.isChecked()) {
                bt.setText(c.getText().toString() + " -> format: " + uzTrackItem.getFormat()
                        .getFormat() + ", getProfile: " + uzTrackItem
                        .getFormat().getProfile() + " (✔)");
                bt.setBackgroundColor(Color.GREEN);
            } else {
                bt.setText(c.getText().toString() + " -> format: " + uzTrackItem.getFormat()
                        .getFormat() + ", getProfile: " + uzTrackItem
                        .getFormat().getProfile());
                bt.setBackgroundColor(Color.WHITE);
            }
            bt.setSoundEffectsEnabled(true);
            bt.setOnClickListener(view1 -> {
                UzAnimationUtil.play(view1, Techniques.Pulse);
                c.performClick();
                UzCommonUtil.actionWithDelayed(300, mls -> {
                    llListHq.removeAllViews();
                    llListHq.invalidate();
                });
            });
            llListHq.addView(bt);
        }
        llListHq.invalidate();
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
        if (exception == null) {
            return;
        }
        UzDialogUtil.showDialog1(activity, exception.getMessage(), new UzDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                onBackPressed();
            }

            @Override
            public void onCancel() {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onPlayerRotated(boolean isLandscape) {

    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (UzDisplayUtil.isFullScreen(activity)) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
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
}

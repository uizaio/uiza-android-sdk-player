package testlibuiza.sample.v3.customskin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import testlibuiza.R;

public class CustomSkinXMLActivity extends AppCompatActivity implements UzPlayerUiEventListener,
        UzPlayerEventListener, UzItemClickListener {

    private UzPlayer uzPlayer;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.uiza_controller_skin_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_xml);
        uzPlayer = findViewById(R.id.uiza_video);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);

        final String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);

        findViewById(R.id.bt_change_skin_custom).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.changeSkin(R.layout.uiza_controller_skin_custom_main);
            }
        });
        findViewById(R.id.bt_change_skin_0).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.changeSkin(R.layout.uz_player_skin_0);
            }
        });
        findViewById(R.id.bt_change_skin_1).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.changeSkin(R.layout.uz_player_skin_1);
            }
        });
        findViewById(R.id.bt_change_skin_2).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.changeSkin(R.layout.uz_player_skin_2);
            }
        });
        findViewById(R.id.bt_change_skin_3).setOnClickListener(v -> {
            if (uzPlayer != null) {
                uzPlayer.changeSkin(R.layout.uz_player_skin_3);
            }
        });
        handleClickSampleText();
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

    private void handleClickSampleText() {
        TextView tvSample = uzPlayer.findViewById(R.id.tv_sample);
        if (tvSample != null) {
            tvSample.setText("This is a view from custom skin.\nTry to tap me.");
            UzDisplayUtil.setTextShadow(tvSample);
            tvSample.setOnClickListener(v -> {
                UzAnimationUtil.play(v, Techniques.Pulse);
                LToast.show(activity, "Click!");
            });
        }
    }
}

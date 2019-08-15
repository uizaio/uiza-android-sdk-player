package testlibuiza.sample.v3.volume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.exoplayer2.audio.AudioListener;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.seekbar.UzVerticalSeekBar;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import testlibuiza.R;
import testlibuiza.app.LSApplication;

public class VolumeActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerUiEventListener, UzItemClickListener {

    private Activity activity;
    private UzPlayer uzPlayer;
    private SeekBar sb;
    private UzVerticalSeekBar sb1;
    private UzVerticalSeekBar sb2;
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCasty(this);
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        uzPlayer = findViewById(R.id.uiza_video);
        sb = findViewById(R.id.sb);
        sb1 = findViewById(R.id.sb_1);
        sb2 = findViewById(R.id.sb_2);
        tv = findViewById(R.id.tv);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.addAudioListener(new AudioListener() {
            @Override
            public void onVolumeChanged(float volume) {
                sb.setProgress((int) (volume * 100));
                sb1.setProgress((int) (volume * 100));
                sb2.setProgress((int) (volume * 100));
            }
        });

        final String entityId = LSApplication.entityIdDefaultVOD;
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzPlayer.setVolume(seekBar.getProgress() / 100f);
                print();
            }
        });
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzPlayer.setVolume(seekBar.getProgress() / 100f);
                print();
            }
        });
        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzPlayer.setVolume(seekBar.getProgress() / 100f);
                print();
            }
        });
    }

    private void print() {
        tv.setText(uzPlayer.getVolume() * 100 + "%");
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
        if (initSuccess) {
            sb.setProgress((int) (uzPlayer.getVolume() * 100));
            sb1.setProgress((int) (uzPlayer.getVolume() * 100));
            sb2.setProgress((int) (uzPlayer.getVolume() * 100));
            print();
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
}

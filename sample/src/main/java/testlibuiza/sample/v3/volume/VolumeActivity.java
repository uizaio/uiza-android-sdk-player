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
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;

/**
 * Created by loitp on 9/1/2019.
 */

public class VolumeActivity extends AppCompatActivity implements UZCallback, UZItemClick {
    private Activity activity;
    private UZVideo uzVideo;
    private SeekBar sb;
    private UzVerticalSeekBar sb1;
    private UzVerticalSeekBar sb2;
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        sb = (SeekBar) findViewById(R.id.sb);
        sb1 = (UzVerticalSeekBar) findViewById(R.id.sb_1);
        sb2 = (UzVerticalSeekBar) findViewById(R.id.sb_2);
        tv = (TextView) findViewById(R.id.tv);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.addAudioListener(new AudioListener() {
            @Override
            public void onVolumeChanged(float volume) {
                sb.setProgress((int) (volume * 100));
                sb1.setProgress((int) (volume * 100));
                sb2.setProgress((int) (volume * 100));
            }
        });

        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                uzVideo.setVolume(seekBar.getProgress() / 100f);
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
                uzVideo.setVolume(seekBar.getProgress() / 100f);
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
                uzVideo.setVolume(seekBar.getProgress() / 100f);
                print();
            }
        });
    }

    private void print() {
        tv.setText(uzVideo.getVolume() * 100 + "%");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        if (isInitSuccess) {
            sb.setProgress((int) (uzVideo.getVolume() * 100));
            sb1.setProgress((int) (uzVideo.getVolume() * 100));
            sb2.setProgress((int) (uzVideo.getVolume() * 100));
            print();
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UzException e) {
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

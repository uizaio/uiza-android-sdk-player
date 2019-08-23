package com.uiza.demo.v4.live.broadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.uiza.demo.LSApplication;
import com.uiza.demo.R;

public class MenuActivity extends AppCompatActivity {

    public static final String LIVE_ENTITY_ID = "LIVE_ENTITY_ID";
    public static final String VIDEO_BITRATE = "VIDEO_BITRATE";
    public static final String VIDEO_FPS = "VIDEO_FPS";
    public static final String VIDEO_FI = "VIDEO_FI";
    public static final String VIDEO_WIDTH = "VIDEO_WIDTH";
    public static final String VIDEO_HEIGHT = "VIDEO_HEIGHT";
    public static final String AUDIO_BITRATE = "AUDIO_BITRATE";
    public static final String AUDIO_SAMPLERATE = "AUDIO_SAMPLERATE";
    private Activity activity;
    private EditText edtEntityId;
    private EditText edtVideoBitrate;
    private EditText edtVideoFps;
    private EditText edtVideoFrameInterval;
    private EditText edtVideoWidth;
    private EditText edtVideoHeight;
    private EditText edtAudioBitrate;
    private EditText edtAudioSampleRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        edtEntityId = findViewById(R.id.edt_live_entity);

        edtVideoBitrate = findViewById(R.id.edt_video_bitrate);
        edtVideoFps = findViewById(R.id.edt_video_fps);
        edtVideoFrameInterval = findViewById(R.id.edt_video_frame_interval);
        edtVideoWidth = findViewById(R.id.edt_video_width);
        edtVideoHeight = findViewById(R.id.edt_video_height);

        edtAudioBitrate = findViewById(R.id.edt_audio_bitrate);
        edtAudioSampleRate = findViewById(R.id.edt_audio_samplerate);

        init();

        findViewById(R.id.bt_portrait).setOnClickListener(view -> {
            if (dataInValid()) {
                Toast.makeText(this, "Data invalid!", Toast.LENGTH_SHORT).show();
                return;
            }
            startLiveActivity(false);
        });
        findViewById(R.id.bt_landscape).setOnClickListener(view -> {
            if (dataInValid()) {
                Toast.makeText(this, "Data invalid!", Toast.LENGTH_SHORT).show();
                return;
            }
            startLiveActivity(true);
        });
    }

    private void init() {
        edtEntityId.setText(LSApplication.entityIdDefaultLIVE_TRANSCODE);

        edtVideoBitrate.setText("5500000");
        edtVideoFps.setText("30");
        edtVideoFrameInterval.setText("2");
        edtVideoWidth.setText("1920");
        edtVideoHeight.setText("1080");

        edtAudioBitrate.setText("131072");
        edtAudioSampleRate.setText("48000");
    }

    private boolean dataInValid() {
        return TextUtils.isEmpty(edtEntityId.getText());
    }

    private void startLiveActivity(boolean isLandscape) {
        if (TextUtils.isEmpty(edtEntityId.getText())) {
            Toast.makeText(getApplicationContext(), "Entity Id is empty", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent;
        if (isLandscape) {
            intent = new Intent(activity, LiveLandscapeActivity.class);
        } else {
            intent = new Intent(activity, LivePortraitActivity.class);
        }
        intent.putExtra(LIVE_ENTITY_ID, edtEntityId.getText().toString());

        intent.putExtra(VIDEO_BITRATE, Integer.parseInt(edtVideoBitrate.getText().toString()));
        intent.putExtra(VIDEO_FPS, Integer.parseInt(edtVideoFps.getText().toString()));
        intent.putExtra(VIDEO_FI, Integer.parseInt(edtVideoFrameInterval.getText().toString()));
        intent.putExtra(VIDEO_WIDTH, Integer.parseInt(edtVideoWidth.getText().toString()));
        intent.putExtra(VIDEO_HEIGHT, Integer.parseInt(edtVideoHeight.getText().toString()));

        intent.putExtra(AUDIO_BITRATE, Integer.parseInt(edtAudioBitrate.getText().toString()));
        intent.putExtra(AUDIO_SAMPLERATE, Integer.parseInt(edtAudioSampleRate.getText().toString()));

        startActivity(intent);
    }
}

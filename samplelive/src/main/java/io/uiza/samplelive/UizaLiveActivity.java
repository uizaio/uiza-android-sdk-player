package io.uiza.samplelive;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.uiza.adapters.ChatAdapter;
import io.uiza.adapters.ChatData;
import io.uiza.extensions.SampleUtils;
import io.uiza.live.UizaLiveView;
import io.uiza.live.enums.AspectRatio;
import io.uiza.live.enums.FilterRender;
import io.uiza.live.enums.ProfileVideoEncoder;
import io.uiza.live.enums.RecordStatus;
import io.uiza.live.enums.Translate;
import io.uiza.live.interfaces.CCUListener;
import io.uiza.live.interfaces.CameraChangeListener;
import io.uiza.live.interfaces.RecordListener;
import io.uiza.live.interfaces.UizaCameraOpenException;
import io.uiza.live.interfaces.UizaLiveListener;
import timber.log.Timber;

public class UizaLiveActivity extends AppCompatActivity implements UizaLiveListener,
        View.OnClickListener, RecordListener, CameraChangeListener, CCUListener, OrientationManager.OrientationListener {

    private static final String TAG = "UizaLiveActivity";
    private static final String RECORD_FOLDER = "uiza-live";
    private UizaMediaButton startButton;
    private UizaMediaButton bRecord;
    private UizaMediaButton btAudio;
    private String liveStreamUrl;
    private String currentDateAndTime = "";
    private File folder;
    private UizaLiveView liveView;
    private TextView tvCCU;
    SharedPreferences preferences;
    private String liveStreamId; // entityId
    private ChatAdapter mAdapter;
    private Handler handler = new Handler();
    RecyclerView chatRCV;
    OrientationManager orientationManager;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_live_stream);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        findViewById(R.id.btn_back).setOnClickListener(this);
        liveView = findViewById(R.id.uiza_live_view);
        liveView.setLiveListener(this);
        liveView.setProfile(ProfileVideoEncoder.P720);
        startButton = findViewById(R.id.b_start_stop);
        startButton.setOnClickListener(this);
        startButton.setEnabled(false);
        bRecord = findViewById(R.id.b_record);
        btAudio = findViewById(R.id.btn_audio);
        chatRCV = findViewById(R.id.rcv_chat);
        tvCCU = findViewById(R.id.tv_ccu);
        bRecord.setOnClickListener(this);
        btAudio.setOnClickListener(this);
        AppCompatImageButton switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this);
        File movieFolder = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (movieFolder != null)
            folder = new File(movieFolder.getAbsolutePath()
                    + RECORD_FOLDER);
        liveStreamId = getIntent().getStringExtra(SampleLiveApplication.EXTRA_STREAM_ID);
        liveStreamUrl = getIntent().getStringExtra(SampleLiveApplication.EXTRA_STREAM_ENDPOINT);
        if (TextUtils.isEmpty(liveStreamUrl)) {
            liveStreamUrl = SampleLiveApplication.getLiveEndpoint();
        }
        Timber.e("liveStreamUrl = %s", liveStreamUrl);
        liveView.setCcuListener(this);
        liveView.setBackgroundAllowedDuration(10000);
        int profile = Integer.valueOf(preferences.getString("camera_profile_key", "360"));
        int fps = Integer.valueOf(preferences.getString("fps_key", "24"));
        int frameInterval = Integer.valueOf(preferences.getString("frame_interval_key", "2"));
        int audioBitrate = Integer.valueOf(preferences.getString("audio_bitrate_key", "64"));
        int audioSampleRate = Integer.valueOf(preferences.getString("sample_rate_key", "32000"));
        boolean stereo = preferences.getBoolean("audio_stereo_key", true);
        liveView.setProfile(ProfileVideoEncoder.find(profile));
        liveView.setFps(fps);
        liveView.setFrameInterval(frameInterval);
        liveView.setAudioBitrate(audioBitrate);
        liveView.setAudioSampleRate(audioSampleRate);
        liveView.setAudioStereo(stereo);
//        liveView.setAspectRatio(AspectRatio.RATIO_18_9);
        String mUserId = SampleUtils.getLocalUserId(this);
        if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(liveStreamId)) {
            chatRCV.setVisibility(View.VISIBLE);
            SampleUtils.setVertical(chatRCV);
            mAdapter = new ChatAdapter();
            chatRCV.setAdapter(mAdapter);
            setupConnection();
        } else {
            chatRCV.setVisibility(View.GONE);
        }

        orientationManager = new OrientationManager(this, SensorManager.SENSOR_DELAY_NORMAL, this);
        orientationManager.enable();
    }

    @Override
    public void onOrientationChange(OrientationManager.ScreenOrientation screenOrientation) {
        switch (screenOrientation) {
            case PORTRAIT:
                rotateView(0);
                break;
            case LANDSCAPE:
                rotateView(1);
                break;
            case REVERSED_PORTRAIT:
                rotateView(2);
                break;
            case REVERSED_LANDSCAPE:
                rotateView(3);
                break;
        }
    }

    int beforeRotation;

    public void rotateView(int rotation) {
        AppCompatImageButton switchCamera = findViewById(R.id.switch_camera);
        float dk = rotation - beforeRotation;
        if (dk >= 3) dk = -1f;
        if (dk <= -3) dk = 1f;
        float deg = switchCamera.getRotation() + dk * 90F;
        switchCamera.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        beforeRotation = rotation;
    }

    @Override
    protected void onResume() {
        if (liveView != null) {
            liveView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want exit?");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            super.onBackPressed();
            dialog.dismiss();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gl_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Stop listener for image, text and gif stream objects.
//        openGlView.setFilter(null);
        int itemId = item.getItemId();
        if (itemId == R.id.e_d_fxaa) {
            liveView.enableAA(!liveView.isAAEnabled());
            Toast.makeText(this,
                    "FXAA " + (liveView.isAAEnabled() ? "enabled" : "disabled"),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.no_filter) {
            liveView.setFilter(FilterRender.None);
            return true;
        } else if (itemId == R.id.analog_tv) {
            liveView.setFilter(FilterRender.AnalogTV);
            return true;
        } else if (itemId == R.id.android_view) {
//            AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
//            androidViewFilterRender.setView(findViewById(R.id.switch_camera));
            liveView.setFilter(FilterRender.AndroidView);
            return true;
        } else if (itemId == R.id.basic_deformation) {
            liveView.setFilter(FilterRender.BasicDeformation);
            return true;
        } else if (itemId == R.id.beauty) {
            liveView.setFilter(FilterRender.Beauty);
            return true;
        } else if (itemId == R.id.black) {
            liveView.setFilter(FilterRender.Black);
            return true;
        } else if (itemId == R.id.blur) {
            liveView.setFilter(FilterRender.Blur);
            return true;
        } else if (itemId == R.id.brightness) {
            liveView.setFilter(FilterRender.Brightness);
            return true;
        } else if (itemId == R.id.cartoon) {
            liveView.setFilter(FilterRender.Cartoon);
            return true;
        } else if (itemId == R.id.circle) {
            liveView.setFilter(FilterRender.Circle);
            return true;
        } else if (itemId == R.id.color) {
            liveView.setFilter(FilterRender.Color);
            return true;
        } else if (itemId == R.id.contrast) {
            liveView.setFilter(FilterRender.Contrast);
            return true;
        } else if (itemId == R.id.duotone) {
            liveView.setFilter(FilterRender.Duotone);
            return true;
        } else if (itemId == R.id.early_bird) {
            liveView.setFilter(FilterRender.EarlyBird);
            return true;
        } else if (itemId == R.id.edge_detection) {
            liveView.setFilter(FilterRender.EdgeDetection);
            return true;
        } else if (itemId == R.id.exposure) {
            liveView.setFilter(FilterRender.Exposure);
            return true;
        } else if (itemId == R.id.fire) {
            liveView.setFilter(FilterRender.Fire);
            return true;
        } else if (itemId == R.id.gamma) {
            liveView.setFilter(FilterRender.Gamma);
            return true;
        } else if (itemId == R.id.glitch) {
            liveView.setFilter(FilterRender.Glitch);
            return true;
        } else if (itemId == R.id.gif) {
            setGifToStream();
            return true;
        } else if (itemId == R.id.grey_scale) {
            liveView.setFilter(FilterRender.GreyScale);
            return true;
        } else if (itemId == R.id.halftone_lines) {
            liveView.setFilter(FilterRender.HalftoneLines);
            return true;
        } else if (itemId == R.id.image) {
            setImageToStream();
            return true;
        } else if (itemId == R.id.image_70s) {
            liveView.setFilter(FilterRender.Image70s);
            return true;
        } else if (itemId == R.id.lamoish) {
            liveView.setFilter(FilterRender.Lamoish);
            return true;
        } else if (itemId == R.id.money) {
            liveView.setFilter(FilterRender.Money);
            return true;
        } else if (itemId == R.id.negative) {
            liveView.setFilter(FilterRender.Negative);
            return true;
        } else if (itemId == R.id.pixelated) {
            liveView.setFilter(FilterRender.Pixelated);
            return true;
        } else if (itemId == R.id.polygonization) {
            liveView.setFilter(FilterRender.Polygonization);
            return true;
        } else if (itemId == R.id.rainbow) {
            liveView.setFilter(FilterRender.Rainbow);
            return true;
        } else if (itemId == R.id.rgb_saturate) {
            FilterRender rgbSaturation = FilterRender.RGBSaturation;
            liveView.setFilter(rgbSaturation);
            //Reduce green and blue colors 20%. Red will predominate.
            rgbSaturation.setRGBSaturation(1f, 0.8f, 0.8f);
            return true;
        } else if (itemId == R.id.ripple) {
            liveView.setFilter(FilterRender.Ripple);
            return true;
        } else if (itemId == R.id.rotation) {
            FilterRender filterRender = FilterRender.Rotation;
            liveView.setFilter(filterRender);
            filterRender.setRotation(90);
            return true;
        } else if (itemId == R.id.saturation) {
            liveView.setFilter(FilterRender.Saturation);
            return true;
        } else if (itemId == R.id.sepia) {
            liveView.setFilter(FilterRender.Sepia);
            return true;
        } else if (itemId == R.id.sharpness) {
            liveView.setFilter(FilterRender.Sharpness);
            return true;
        } else if (itemId == R.id.snow) {
            liveView.setFilter(FilterRender.Snow);
            return true;
        } else if (itemId == R.id.swirl) {
            liveView.setFilter(FilterRender.Swirl);
            return true;
        } else if (itemId == R.id.surface_filter) {//You can render this filter with other api that draw in a surface. for example you can use VLC
            FilterRender surfaceFilterRender = FilterRender.Surface;
            liveView.setFilter(surfaceFilterRender);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.big_bunny_240p);
            mediaPlayer.setSurface(surfaceFilterRender.getSurface());
            mediaPlayer.start();
            //Video is 360x240 so select a percent to keep aspect ratio (50% x 33.3% screen)
            surfaceFilterRender.setScale(50f, 33.3f);
            liveView.setFilter(surfaceFilterRender); //Optional
            return true;
        } else if (itemId == R.id.temperature) {
            liveView.setFilter(FilterRender.Temperature);
            return true;
        } else if (itemId == R.id.text) {
            setTextToStream();
            return true;
        } else if (itemId == R.id.zebra) {
            liveView.setFilter(FilterRender.Zebra);
            return true;
        }
        return false;
    }

    private void setTextToStream() {
        FilterRender textObject = FilterRender.TextObject;
        liveView.setFilter(textObject);
        textObject.setText("Hello world", 22, Color.RED);
        textObject.setDefaultScale(liveView.getStreamWidth(),
                liveView.getStreamHeight());
        textObject.setPosition(Translate.CENTER);
        liveView.setFilter(textObject); //Optional
    }

    private void setImageToStream() {
        FilterRender imageObjectFilterRender = FilterRender.ImageObject;
        liveView.setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imageObjectFilterRender.setDefaultScale(liveView.getStreamWidth(),
                liveView.getStreamHeight());
        imageObjectFilterRender.setPosition(Translate.RIGHT);
        liveView.setFilter(imageObjectFilterRender); //Optional
//        liveView.setPreventMoveOutside(false); //Optional
    }

    private void setGifToStream() {
        try {
            FilterRender gifObjectFilterRender = FilterRender.GifObject;
            gifObjectFilterRender.setGif(getResources().openRawResource(R.raw.banana));
            liveView.setFilter(gifObjectFilterRender);
            gifObjectFilterRender.setDefaultScale(liveView.getStreamWidth(),
                    liveView.getStreamHeight());
            gifObjectFilterRender.setPosition(Translate.BOTTOM);
            liveView.setFilter(gifObjectFilterRender); //Optional
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recordAction();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void recordAction() {
        try {
            if (!folder.exists()) {
                try {
                    folder.mkdir();
                } catch (SecurityException ex) {
                    Toast.makeText(this, ex.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            currentDateAndTime = sdf.format(new Date());
            if (!liveView.isStreaming()) {
                if (liveView.prepareStream()) {
                    liveView.startRecord(
                            folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                } else {
                    Toast.makeText(this, "Error preparing stream, This device cant do it",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                liveView.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
            }
        } catch (IOException e) {
            liveView.stopRecord();
            bRecord.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_record_white_24, null));
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.b_start_stop) {
            if (!liveView.isStreaming()) {
                if (liveView.isRecording()
                        || liveView.prepareStream()) {
                    liveView.startStream(liveStreamUrl);
                } else {
                    Toast.makeText(this, "Error preparing stream, This device cant do it",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                liveView.stopStream();
            }
        } else if (id == R.id.switch_camera) {
            try {
                liveView.switchCamera();
            } catch (UizaCameraOpenException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.b_record) {
            if (!liveView.isRecording()) {
                ActivityCompat.requestPermissions(UizaLiveActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            } else {
                liveView.stopRecord();
            }
        } else if (id == R.id.btn_audio) {
            Timber.e("audioMuted: %b", liveView.isAudioMuted());
            if (liveView.isAudioMuted()) {
                liveView.enableAudio();
            } else {
                liveView.disableAudio();
            }
            btAudio.setChecked(liveView.isAudioMuted());
        } else if (id == R.id.btn_back) {
            onBackPressed();
        }
    }

    @Override
    public void onInit(boolean success) {
        startButton.setEnabled(success);
        btAudio.setVisibility(View.GONE);
        liveView.setCameraChangeListener(this);
        liveView.setRecordListener(this);
    }

    @Override
    public void onConnectionSuccess() {
        startButton.setChecked(true);
        btAudio.setVisibility(View.VISIBLE);
        tvCCU.setVisibility(View.VISIBLE);
        btAudio.setChecked(false);
        Toast.makeText(UizaLiveActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRetryConnection(long delay) {
        Toast.makeText(UizaLiveActivity.this, "Retry " + delay / 1000 + " s", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onConnectionFailed(@Nullable final String reason) {
        Toast.makeText(UizaLiveActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onNewBitrate(long bitrate) {
        Timber.e("newBitrate: %d", bitrate);
    }

    @Override
    public void onDisconnect() {
        startButton.setChecked(false);
        btAudio.setVisibility(View.GONE);
        tvCCU.setVisibility(View.GONE);
        btAudio.setChecked(false);
        Toast.makeText(UizaLiveActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthError() {
        Toast.makeText(UizaLiveActivity.this, "Auth error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAuthSuccess() {
        Toast.makeText(UizaLiveActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceCreated() {
        Timber.e("surfaceCreated");
    }

    @Override
    public void surfaceChanged(int format, int width, int height) {
        Timber.e("surfaceChanged: {" + format + ", " + width + ", " + height + "}");
    }

    @Override
    public void surfaceDestroyed() {
        Timber.e("surfaceDestroyed");
    }

    @Override
    public void onBackgroundTooLong() {
        Toast.makeText(this, "You go to background for a long time !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCcu(int viewers) {
        runOnUiThread(() -> tvCCU.setText(String.valueOf(viewers)));
    }

    @Override
    public void onCameraChange(boolean isFrontCamera) {
        Timber.e("onCameraChange: %b", isFrontCamera);
    }

    @Override
    public void onStatusChange(RecordStatus status) {
        bRecord.setChecked(status == RecordStatus.RECORDING);

        if (status == RecordStatus.RECORDING) {
            Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
        } else if (status == RecordStatus.STOPPED) {
            currentDateAndTime = "";
            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UizaLiveActivity.this, "Record " + status.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // CHAT
    private void setupConnection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference(liveStreamId);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Timber.d("SUCCESS!");
                handleReturn(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.e("ERROR: %s", databaseError.getDetails());
                handler.post(() -> Toast.makeText(UizaLiveActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleReturn(DataSnapshot dataSnapshot) {
        mAdapter.clearData();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            ChatData data = item.getValue(ChatData.class);
            mAdapter.addData(data);
        }
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() > 1)
            chatRCV.post(() -> chatRCV.smoothScrollToPosition(mAdapter.getItemCount() - 1));
    }
}

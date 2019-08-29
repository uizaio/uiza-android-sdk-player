package com.uiza.demo.v4.live.broadcast;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.pedro.encoder.input.gl.render.filters.AndroidViewFilterRender;
import com.pedro.encoder.input.gl.render.filters.BasicDeformationFilterRender;
import com.pedro.encoder.input.gl.render.filters.BeautyFilterRender;
import com.pedro.encoder.input.gl.render.filters.BlurFilterRender;
import com.pedro.encoder.input.gl.render.filters.BrightnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.CartoonFilterRender;
import com.pedro.encoder.input.gl.render.filters.ColorFilterRender;
import com.pedro.encoder.input.gl.render.filters.ContrastFilterRender;
import com.pedro.encoder.input.gl.render.filters.DuotoneFilterRender;
import com.pedro.encoder.input.gl.render.filters.EarlyBirdFilterRender;
import com.pedro.encoder.input.gl.render.filters.EdgeDetectionFilterRender;
import com.pedro.encoder.input.gl.render.filters.ExposureFilterRender;
import com.pedro.encoder.input.gl.render.filters.FireFilterRender;
import com.pedro.encoder.input.gl.render.filters.GammaFilterRender;
import com.pedro.encoder.input.gl.render.filters.GreyScaleFilterRender;
import com.pedro.encoder.input.gl.render.filters.HalftoneLinesFilterRender;
import com.pedro.encoder.input.gl.render.filters.Image70sFilterRender;
import com.pedro.encoder.input.gl.render.filters.LamoishFilterRender;
import com.pedro.encoder.input.gl.render.filters.MoneyFilterRender;
import com.pedro.encoder.input.gl.render.filters.NegativeFilterRender;
import com.pedro.encoder.input.gl.render.filters.NoFilterRender;
import com.pedro.encoder.input.gl.render.filters.PixelatedFilterRender;
import com.pedro.encoder.input.gl.render.filters.PolygonizationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RGBSaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RainbowFilterRender;
import com.pedro.encoder.input.gl.render.filters.RippleFilterRender;
import com.pedro.encoder.input.gl.render.filters.RotationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SepiaFilterRender;
import com.pedro.encoder.input.gl.render.filters.SharpnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.TemperatureFilterRender;
import com.pedro.encoder.input.gl.render.filters.ZebraFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.SurfaceFilterRender;
import com.pedro.encoder.utils.gl.TranslateTo;
import com.uiza.demo.R;
import io.uiza.broadcast.UzLiveCameraCallback;
import io.uiza.broadcast.UzLivestream;
import io.uiza.broadcast.UzLivestream.StartPreview;
import io.uiza.broadcast.UzLivestreamCallback;
import io.uiza.broadcast.config.UzPresetLiveFeed;
import io.uiza.broadcast.util.UzLivestreamError;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.view.LToast;

public class LiveLandscapeActivity extends AppCompatActivity implements View.OnClickListener,
        UzLivestreamCallback, UzLiveCameraCallback {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UzLivestream uzLivestream;
    private Button bStartStop;
    private Button bStartStopStore;
    private Button btSwitchCamera;
    private Button btFilter;
    private Button btFlash;
    private TextView tvMainUrl;
    private TextView tvInfo;
    private String entityId;
    private int videoBitrate;
    private int videoFps;
    private int videoFrameInterval;
    private int videoWidth;
    private int videoHeight;
    private int audioBitrate;
    private int audioSampleRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_landscape);

        entityId = getIntent().getStringExtra(MenuActivity.LIVE_ENTITY_ID);

        videoBitrate = getIntent().getIntExtra(MenuActivity.VIDEO_BITRATE, 5500000);
        videoFps = getIntent().getIntExtra(MenuActivity.VIDEO_FPS, 30);
        videoFrameInterval = getIntent().getIntExtra(MenuActivity.VIDEO_FI, 2);
        videoWidth = getIntent().getIntExtra(MenuActivity.VIDEO_WIDTH, -1);
        videoHeight = getIntent().getIntExtra(MenuActivity.VIDEO_HEIGHT, -1);

        audioBitrate = getIntent().getIntExtra(MenuActivity.AUDIO_BITRATE, 128 * 1024);
        audioSampleRate = getIntent().getIntExtra(MenuActivity.AUDIO_SAMPLERATE, 48000);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        findViews();
    }

    private void findViews() {
        uzLivestream = findViewById(R.id.uiza_livestream);
        uzLivestream.setUzLivestreamCallback(this);
        uzLivestream.setCameraCallback(this);
        bStartStop = findViewById(R.id.b_start_stop);
        bStartStopStore = findViewById(R.id.b_start_stop_store);
        btSwitchCamera = findViewById(R.id.b_switch_camera);
        btFilter = findViewById(R.id.b_filter);
        btFlash = findViewById(R.id.b_flash);
        tvMainUrl = findViewById(R.id.tv_main_url);
        tvInfo = findViewById(R.id.tv_info);
        bStartStop.setEnabled(false);
        bStartStopStore.setEnabled(false);
        btSwitchCamera.setEnabled(false);
        btFilter.setEnabled(false);
        bStartStop.setOnClickListener(this);
        bStartStopStore.setOnClickListener(this);
        btSwitchCamera.setOnClickListener(this);
        btFilter.setOnClickListener(this);
        btFlash.setOnClickListener(this);
        uzLivestream.setBackgroundAllowedDuration(10000);
    }

    @Override
    protected void onResume() {
        if (uzLivestream != null) {
            uzLivestream.onResume();
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                startStop();
                break;
            case R.id.b_start_stop_store:
                startStopStore();
                break;
            case R.id.b_switch_camera:
                uzLivestream.switchCamera();
                break;
            case R.id.b_filter:
                LPopupMenu.show(activity, btFilter, R.menu.gl_menu, this::handleFilterClick);
                break;
            case R.id.b_flash:
                toggleFlash();
                break;
        }
    }

    private void startStop() {
        if (!uzLivestream.isStreaming()) {
            //AUTO
            boolean prepareAudio = uzLivestream.prepareAudio(audioBitrate, audioSampleRate, true,
                    AcousticEchoCanceler.isAvailable(), NoiseSuppressor.isAvailable());
            boolean prepareVideo = uzLivestream
                    .prepareVideo(videoWidth, videoHeight, videoFps, videoBitrate, false,
                            videoFrameInterval, 0);

            if (prepareAudio && prepareVideo) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl());
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }

            //SD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoSDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl());
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/

            //HD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoHDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl());
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/

            //FULL HD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoFullHDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl());
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/
        } else {
            bStartStop.setText(R.string.start_button);
            uzLivestream.stopStream();
        }
        if (uzLivestream.isStreaming()) {
            bStartStop.setText("Stop streaming");
            bStartStopStore.setEnabled(false);
        } else {
            bStartStop.setText("Start streaming");
            bStartStopStore.setEnabled(true);
        }
    }

    private void startStopStore() {
        if (!uzLivestream.isStreaming()) {
            //AUTO
            boolean prepareAudio = uzLivestream.prepareAudio(audioBitrate, audioSampleRate, true,
                    AcousticEchoCanceler.isAvailable(), NoiseSuppressor.isAvailable());
            boolean prepareVideo = uzLivestream
                    .prepareVideo(videoWidth, videoHeight, videoFps, videoBitrate, false,
                            videoFrameInterval, 0);

            if (prepareAudio && prepareVideo) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl());
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }

            //SD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoSDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/

            //HD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoHDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/

            //FULL HD
            /*if (uzLivestream.prepareAudio() && uzLivestream.prepareVideoFullHDLandscape()) {
                uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);
            } else {
                LToast.show(activity, "Error preparing stream, This device cant do it");
            }*/
        } else {
            bStartStopStore.setText(R.string.start_button);
            uzLivestream.stopStream();
        }
        if (uzLivestream.isStreaming()) {
            bStartStopStore.setText("Stop streaming");
            bStartStop.setEnabled(false);
        } else {
            bStartStopStore.setText("Start stream and Store");
            bStartStop.setEnabled(true);
        }
    }

    private void handleFilterClick(MenuItem item) {
        uzLivestream.setBaseObjectFilterRender();
        switch (item.getItemId()) {
            case R.id.e_d_fxaa:
                uzLivestream.enableAntiAliasing(!uzLivestream.isAntiAliasingEnabled());
                break;
            case R.id.no_filter:
                uzLivestream.setFilter(new NoFilterRender());
                break;
            case R.id.android_view:
                AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
                androidViewFilterRender.setView(findViewById(R.id.activity_example_rtmp));
                uzLivestream.setFilter(androidViewFilterRender);
                break;
            case R.id.basic_deformation:
                uzLivestream.setFilter(new BasicDeformationFilterRender());
                break;
            case R.id.beauty:
                uzLivestream.setFilter(new BeautyFilterRender());
                break;
            case R.id.blur:
                uzLivestream.setFilter(new BlurFilterRender());
                break;
            case R.id.brightness:
                uzLivestream.setFilter(new BrightnessFilterRender());
                break;
            case R.id.cartoon:
                uzLivestream.setFilter(new CartoonFilterRender());
                break;
            case R.id.color:
                uzLivestream.setFilter(new ColorFilterRender());
                break;
            case R.id.contrast:
                uzLivestream.setFilter(new ContrastFilterRender());
                break;
            case R.id.duotone:
                uzLivestream.setFilter(new DuotoneFilterRender());
                break;
            case R.id.early_bird:
                uzLivestream.setFilter(new EarlyBirdFilterRender());
                break;
            case R.id.edge_detection:
                uzLivestream.setFilter(new EdgeDetectionFilterRender());
                break;
            case R.id.exposure:
                uzLivestream.setFilter(new ExposureFilterRender());
                break;
            case R.id.fire:
                uzLivestream.setFilter(new FireFilterRender());
                break;
            case R.id.gamma:
                uzLivestream.setFilter(new GammaFilterRender());
                break;
            case R.id.gif:
                uzLivestream.setGifToStream(R.raw.banana, TranslateTo.BOTTOM);
                break;
            case R.id.grey_scale:
                uzLivestream.setFilter(new GreyScaleFilterRender());
                break;
            case R.id.halftone_lines:
                uzLivestream.setFilter(new HalftoneLinesFilterRender());
                break;
            case R.id.image:
                uzLivestream.setImageToStream(R.mipmap.ic_launcher, TranslateTo.RIGHT);
                break;
            case R.id.image_70s:
                uzLivestream.setFilter(new Image70sFilterRender());
                break;
            case R.id.lamoish:
                uzLivestream.setFilter(new LamoishFilterRender());
                break;
            case R.id.money:
                uzLivestream.setFilter(new MoneyFilterRender());
                break;
            case R.id.negative:
                uzLivestream.setFilter(new NegativeFilterRender());
                break;
            case R.id.pixelated:
                uzLivestream.setFilter(new PixelatedFilterRender());
                break;
            case R.id.polygonization:
                uzLivestream.setFilter(new PolygonizationFilterRender());
                break;
            case R.id.rainbow:
                uzLivestream.setFilter(new RainbowFilterRender());
                break;
            case R.id.rgb_saturate:
                RGBSaturationFilterRender rgbSaturationFilterRender = new RGBSaturationFilterRender();
                uzLivestream.setFilter(rgbSaturationFilterRender);
                //Reduce green and blue colors 20%. Red will predominate.
                rgbSaturationFilterRender.setRGBSaturation(1f, 0.8f, 0.8f);
                break;
            case R.id.ripple:
                uzLivestream.setFilter(new RippleFilterRender());
                break;
            case R.id.rotation:
                RotationFilterRender rotationFilterRender = new RotationFilterRender();
                uzLivestream.setFilter(rotationFilterRender);
                rotationFilterRender.setRotation(90);
                break;
            case R.id.saturation:
                uzLivestream.setFilter(new SaturationFilterRender());
                break;
            case R.id.sepia:
                uzLivestream.setFilter(new SepiaFilterRender());
                break;
            case R.id.sharpness:
                uzLivestream.setFilter(new SharpnessFilterRender());
                break;
            case R.id.surface_filter:
                //You can render this btFilter with other api that draw in a surface. for example you can use VLC
                SurfaceFilterRender surfaceFilterRender = new SurfaceFilterRender();
                uzLivestream.setFilter(surfaceFilterRender);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.big_bunny_240p);
                mediaPlayer.setSurface(surfaceFilterRender.getSurface());
                mediaPlayer.start();
                //Video is 360x240 so select a percent to keep aspect ratio (50% x 33.3% screen)
                surfaceFilterRender.setScale(50f, 33.3f);
                uzLivestream.getSpriteGestureController()
                        .setBaseObjectFilterRender(surfaceFilterRender); //Optional
                break;
            case R.id.temperature:
                uzLivestream.setFilter(new TemperatureFilterRender());
                break;
            case R.id.text:
                uzLivestream.setTextToStream("Hello Uiza", 40, Color.RED, TranslateTo.CENTER);
                break;
            case R.id.zebra:
                uzLivestream.setFilter(new ZebraFilterRender());
                break;
        }
    }

    private void toggleFlash() {
        uzLivestream.toggleLantern();
    }

    @Override
    public void onCameraChanged(boolean isFrontCamera) {
        runOnUiThread(() -> {
            tvInfo.setText("isFrontCamera " + isFrontCamera);
            btFlash.setVisibility(isFrontCamera ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onUiCreated() {

    }

    @Override
    public void onError(UzLivestreamError error) {
        runOnUiThread(() -> {
            LToast.show(activity, error.getReason());
            bStartStop.setEnabled(true);
            bStartStopStore.setEnabled(true);
            bStartStop.setText("Start streaming");
            bStartStopStore.setText("Start stream and Store");
        });
    }

    @Override
    public void onGetDataSuccess(VideoData d, String mainUrl, boolean isTranscode,
            UzPresetLiveFeed presetLiveFeed) {
        bStartStop.setEnabled(true);
        bStartStopStore.setEnabled(true);
        btSwitchCamera.setEnabled(true);
        btFilter.setEnabled(true);
        tvMainUrl.setText(mainUrl);
    }

    @Override
    public void surfaceChanged(StartPreview startPreview) {
        int[] result = uzLivestream.getBestSizePreview();
        int width = result[0];
        int height = result[1];
        startPreview.onSizeStartPreview(width, height);
    }

    @Override
    public void onPermission(boolean areAllPermissionsGranted) {
        if (areAllPermissionsGranted) {
            bStartStop.setEnabled(true);
            bStartStopStore.setEnabled(true);
            btSwitchCamera.setEnabled(true);
            btFilter.setEnabled(true);
            uzLivestream.setId(entityId);
        } else {
            LToast.show(activity,
                    "Cannot use this feature because user does not allow our permissions");
            onBackPressed();
        }
    }

    @Override
    public void onBackgroundTooLong() {
        LToast.show(getApplicationContext(), "You go to background for a long time !",
                Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionSuccessRtmp() {
    }

    @Override
    public void onConnectionFailedRtmp(String reason) {
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(() -> {
            bStartStop.setEnabled(true);
            bStartStopStore.setEnabled(true);
            btSwitchCamera.setEnabled(true);
            btFilter.setEnabled(true);
            bStartStop.setText("Start streaming");
            bStartStopStore.setText("Start stream and Store");
        });
    }

    @Override
    public void onAuthErrorRtmp() {
    }

    @Override
    public void onAuthSuccessRtmp() {
    }

    @Override
    public void surfaceCreated() {
    }
}

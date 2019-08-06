package testlibuiza.sample.livestream;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import io.uiza.broadcast.UzLivestream;
import io.uiza.broadcast.UzLivestreamCallback;
import io.uiza.broadcast.util.UzLivestreamError;
import io.uiza.broadcast.config.PresetLiveFeed;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

public class LivestreamBroadcasterActivity extends AppCompatActivity implements View.OnClickListener,
        UzLivestreamCallback {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UzLivestream uzLivestream;
    private Button bStartStop;
    private Button bStartStopStore;
    private Button btSwitchCamera;
    private Button btFilter;
    private TextView tvMainUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_livestream_video_broadcaster);
        uzLivestream = findViewById(R.id.uiza_livestream);
        uzLivestream.setUzLivestreamCallback(this);
        bStartStop = findViewById(R.id.b_start_stop);
        bStartStopStore = findViewById(R.id.b_start_stop_store);
        btSwitchCamera = findViewById(R.id.b_switch_camera);
        btFilter = findViewById(R.id.b_filter);
        tvMainUrl = findViewById(R.id.tv_main_url);
        bStartStop.setEnabled(false);
        bStartStopStore.setEnabled(false);
        btSwitchCamera.setEnabled(false);
        btFilter.setEnabled(false);
        bStartStop.setOnClickListener(this);
        bStartStopStore.setOnClickListener(this);
        btSwitchCamera.setOnClickListener(this);
        btFilter.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        uzLivestream.onResume();
        super.onResume();
    }

    private void handleFilterClick(MenuItem item) {
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
                uzLivestream.getSpriteGestureController().setBaseObjectFilterRender(surfaceFilterRender); //Optional
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                if (!uzLivestream.isStreaming()) {
                    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideo(false)) {
                        uzLivestream.startStream(uzLivestream.getMainStreamUrl());
                    } else {
                        LToast.show(activity, "Error preparing stream, This device cant do it");
                    }
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
                break;
            case R.id.b_start_stop_store:
                if (!uzLivestream.isStreaming()) {
                    if (uzLivestream.prepareAudio() && uzLivestream.prepareVideo(false)) {
                        uzLivestream.startStream(uzLivestream.getMainStreamUrl(), true);
                    } else {
                        LToast.show(activity, "Cannot start");
                    }
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
                break;
            case R.id.b_switch_camera:
                uzLivestream.switchCamera();
                break;
            case R.id.b_filter:
                PopupMenu popup = new PopupMenu(activity, btFilter);
                popup.getMenuInflater().inflate(R.menu.gl_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    handleFilterClick(menuItem);
                    return true;
                });
                popup.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUiCreated() {
    }

    @Override
    public void onPermission(boolean b) {
        if (b) {
            bStartStop.setEnabled(true);
            bStartStopStore.setEnabled(true);
            btSwitchCamera.setEnabled(true);
            btFilter.setEnabled(true);
            //uzLivestream.setId(LSApplication.entityIdDefaultLIVE_TRANSCODE);
            uzLivestream.setId(LSApplication.entityIdDefaultLIVE_NO_TRANSCODE);
        } else {
            LToast.show(activity, "Cannot use this feature because user does not allow our permissions");
        }
    }

    @Override
    public void onError(UzLivestreamError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LToast.show(activity, error.getReason());
            }
        });
    }

    @Override
    public void onBackgroundTooLong() {
        LToast.show(getApplicationContext(), "You go to background for a long time !", Toast.LENGTH_LONG);
    }

    @Override
    public void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode, PresetLiveFeed presetLiveFeed) {
        LLog.d(TAG, "onGetDataSuccess " + LSApplication.getInstance().getGson().toJson(presetLiveFeed));
        bStartStop.setEnabled(true);
        bStartStopStore.setEnabled(true);
        btSwitchCamera.setEnabled(true);
        btFilter.setEnabled(true);
        tvMainUrl.setText(mainUrl);
    }

    @Override
    public void onConnectionSuccessRtmp() {
    }

    @Override
    public void onConnectionFailedRtmp(String reason) {
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bStartStop.setEnabled(true);
                bStartStopStore.setEnabled(true);
                btSwitchCamera.setEnabled(true);
                btFilter.setEnabled(true);
            }
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

    @Override
    public void surfaceChanged(UzLivestream.StartPreview startPreview) {
        int[] result = uzLivestream.getBestSizePreview();
        int width = result[0];
        int height = result[1];
        startPreview.onSizeStartPreview(width, height);
    }
}

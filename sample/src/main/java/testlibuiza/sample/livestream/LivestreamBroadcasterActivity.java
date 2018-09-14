package testlibuiza.sample.livestream;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LPopupMenu;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.AndroidViewFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.BasicDeformationFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.BeautyFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.BlurFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.BrightnessFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.CartoonFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.ColorFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.ContrastFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.DuotoneFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.EarlyBirdFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.EdgeDetectionFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.ExposureFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.FireFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.GammaFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.GreyScaleFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.HalftoneLinesFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.Image70sFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.LamoishFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.MoneyFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.NegativeFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.NoFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.PixelatedFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.PolygonizationFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.RGBSaturationFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.RainbowFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.RippleFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.RotationFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.SaturationFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.SepiaFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.SharpnessFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.SurfaceFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.TemperatureFilterRender;
import vn.uiza.libstream.uiza.encoder.input.gl.render.filters.ZebraFilterRender;
import vn.uiza.libstream.uiza.encoder.utils.gl.TranslateTo;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv3.view.rl.livestream.PresetLiveStreamingFeed;
import vn.uiza.uzv3.view.rl.livestream.UZLivestream;
import vn.uiza.views.LToast;

public class LivestreamBroadcasterActivity extends BaseActivity implements View.OnClickListener, UZLivestream.Callback {
    private UZLivestream UZLivestream;
    private Button bStartStop;
    private Button bStartStopStore;
    private Button btSwitchCamera;
    private Button btFilter;
    private TextView tvMainUrl;

    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_livestream_video_broadcaster;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //LActivityUtil.changeScreenLandscape(activity);

        UZLivestream = (UZLivestream) findViewById(R.id.uiza_livestream);
        UZLivestream.setCallback(this);
        bStartStop = findViewById(R.id.b_start_stop);
        bStartStopStore = findViewById(R.id.b_start_stop_store);
        btSwitchCamera = findViewById(R.id.b_switch_camera);
        btFilter = (Button) findViewById(R.id.b_filter);
        tvMainUrl = (TextView) findViewById(R.id.tv_main_url);

        bStartStop.setEnabled(false);
        bStartStopStore.setEnabled(false);
        btSwitchCamera.setEnabled(false);
        btFilter.setEnabled(false);

        bStartStop.setOnClickListener(this);
        bStartStopStore.setOnClickListener(this);
        btSwitchCamera.setOnClickListener(this);
        btFilter.setOnClickListener(this);

        UZLivestream.setId(LSApplication.entityIdDefaultLIVE_TRANSCODE);
        //uizaLivestream.setId(LSApplication.entityIdDefaultLIVE_NO_TRANSCODE);
    }

    private void handleFilterClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.e_d_fxaa:
                UZLivestream.enableAA(!UZLivestream.isAAEnabled());
                break;
            case R.id.no_filter:
                UZLivestream.setFilter(new NoFilterRender());
                break;
            case R.id.android_view:
                AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
                androidViewFilterRender.setView(findViewById(R.id.activity_example_rtmp));
                UZLivestream.setFilter(androidViewFilterRender);
                break;
            case R.id.basic_deformation:
                UZLivestream.setFilter(new BasicDeformationFilterRender());
                break;
            case R.id.beauty:
                UZLivestream.setFilter(new BeautyFilterRender());
                break;
            case R.id.blur:
                UZLivestream.setFilter(new BlurFilterRender());
                break;
            case R.id.brightness:
                UZLivestream.setFilter(new BrightnessFilterRender());
                break;
            case R.id.cartoon:
                UZLivestream.setFilter(new CartoonFilterRender());
                break;
            case R.id.color:
                UZLivestream.setFilter(new ColorFilterRender());
                break;
            case R.id.contrast:
                UZLivestream.setFilter(new ContrastFilterRender());
                break;
            case R.id.duotone:
                UZLivestream.setFilter(new DuotoneFilterRender());
                break;
            case R.id.early_bird:
                UZLivestream.setFilter(new EarlyBirdFilterRender());
                break;
            case R.id.edge_detection:
                UZLivestream.setFilter(new EdgeDetectionFilterRender());
                break;
            case R.id.exposure:
                UZLivestream.setFilter(new ExposureFilterRender());
                break;
            case R.id.fire:
                UZLivestream.setFilter(new FireFilterRender());
                break;
            case R.id.gamma:
                UZLivestream.setFilter(new GammaFilterRender());
                break;
            case R.id.gif:
                UZLivestream.setGifToStream(R.raw.banana, TranslateTo.BOTTOM);
                break;
            case R.id.grey_scale:
                UZLivestream.setFilter(new GreyScaleFilterRender());
                break;
            case R.id.halftone_lines:
                UZLivestream.setFilter(new HalftoneLinesFilterRender());
                break;
            case R.id.image:
                UZLivestream.setImageToStream(R.mipmap.ic_launcher, TranslateTo.RIGHT);
                break;
            case R.id.image_70s:
                UZLivestream.setFilter(new Image70sFilterRender());
                break;
            case R.id.lamoish:
                UZLivestream.setFilter(new LamoishFilterRender());
                break;
            case R.id.money:
                UZLivestream.setFilter(new MoneyFilterRender());
                break;
            case R.id.negative:
                UZLivestream.setFilter(new NegativeFilterRender());
                break;
            case R.id.pixelated:
                UZLivestream.setFilter(new PixelatedFilterRender());
                break;
            case R.id.polygonization:
                UZLivestream.setFilter(new PolygonizationFilterRender());
                break;
            case R.id.rainbow:
                UZLivestream.setFilter(new RainbowFilterRender());
                break;
            case R.id.rgb_saturate:
                RGBSaturationFilterRender rgbSaturationFilterRender = new RGBSaturationFilterRender();
                UZLivestream.setFilter(rgbSaturationFilterRender);
                //Reduce green and blue colors 20%. Red will predominate.
                rgbSaturationFilterRender.setRGBSaturation(1f, 0.8f, 0.8f);
                break;
            case R.id.ripple:
                UZLivestream.setFilter(new RippleFilterRender());
                break;
            case R.id.rotation:
                RotationFilterRender rotationFilterRender = new RotationFilterRender();
                UZLivestream.setFilter(rotationFilterRender);
                rotationFilterRender.setRotation(90);
                break;
            case R.id.saturation:
                UZLivestream.setFilter(new SaturationFilterRender());
                break;
            case R.id.sepia:
                UZLivestream.setFilter(new SepiaFilterRender());
                break;
            case R.id.sharpness:
                UZLivestream.setFilter(new SharpnessFilterRender());
                break;
            case R.id.surface_filter:
                //You can render this btFilter with other api that draw in a surface. for example you can use VLC
                SurfaceFilterRender surfaceFilterRender = new SurfaceFilterRender();
                UZLivestream.setFilter(surfaceFilterRender);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.big_bunny_240p);
                mediaPlayer.setSurface(surfaceFilterRender.getSurface());
                mediaPlayer.start();
                //Video is 360x240 so select a percent to keep aspect ratio (50% x 33.3% screen)
                surfaceFilterRender.setScale(50f, 33.3f);
                surfaceFilterRender.setListeners(UZLivestream.getOpenGlView()); //Optional
                break;
            case R.id.temperature:
                UZLivestream.setFilter(new TemperatureFilterRender());
                break;
            case R.id.text:
                UZLivestream.setTextToStream("Hello Uiza", 40, Color.RED, TranslateTo.CENTER);
                break;
            case R.id.zebra:
                UZLivestream.setFilter(new ZebraFilterRender());
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                if (!UZLivestream.isStreaming()) {
                    if (UZLivestream.prepareAudio() && UZLivestream.prepareVideoHD(false)) {
                        UZLivestream.startStream(UZLivestream.getMainStreamUrl());
                    } else {
                        LToast.show(activity, getString(R.string.err_dont_support));
                    }
                } else {
                    bStartStop.setText(R.string.start_button);
                    UZLivestream.stopStream();
                }
                if (UZLivestream.isStreaming()) {
                    bStartStop.setText("Stop streaming");
                    bStartStopStore.setEnabled(false);
                } else {
                    bStartStop.setText("Start streaming");
                    bStartStopStore.setEnabled(true);
                }
                break;
            case R.id.b_start_stop_store:
                if (!UZLivestream.isStreaming()) {
                    if (UZLivestream.prepareAudio() && UZLivestream.prepareVideoHD(false)) {
                        UZLivestream.startStream(UZLivestream.getMainStreamUrl(), true);
                    } else {
                        LToast.show(activity, "Cannot start");
                    }
                } else {
                    bStartStopStore.setText(R.string.start_button);
                    UZLivestream.stopStream();
                }
                if (UZLivestream.isStreaming()) {
                    bStartStopStore.setText("Stop streaming");
                    bStartStop.setEnabled(false);
                } else {
                    bStartStopStore.setText("Start stream and Store");
                    bStartStop.setEnabled(true);
                }
                break;
            case R.id.b_switch_camera:
                UZLivestream.switchCamera();
                break;
            case R.id.b_filter:
                LPopupMenu.show(activity, btFilter, R.menu.gl_menu, new LPopupMenu.CallBack() {
                    @Override
                    public void clickOnItem(MenuItem menuItem) {
                        handleFilterClick(menuItem);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LToast.show(activity, reason);
            }
        });
    }

    @Override
    public void onGetDataSuccess(Data d, String mainUrl, boolean isTranscode, PresetLiveStreamingFeed presetLiveStreamingFeed) {
        LLog.d(TAG, "onGetDataSuccess " + LSApplication.getInstance().getGson().toJson(presetLiveStreamingFeed));
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
    public void surfaceChanged(UZLivestream.StartPreview startPreview) {
        startPreview.onSizeStartPreview(1280, 720);
    }
}

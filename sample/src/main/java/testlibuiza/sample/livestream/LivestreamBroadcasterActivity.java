package testlibuiza.sample.livestream;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LPopupMenu;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.AndroidViewFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BasicDeformationFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BeautyFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BlurFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.BrightnessFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.CartoonFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.ColorFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.ContrastFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.DuotoneFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.EarlyBirdFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.EdgeDetectionFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.ExposureFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.FireFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.GammaFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.GreyScaleFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.HalftoneLinesFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.Image70sFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.LamoishFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.MoneyFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.NegativeFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.NoFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.PixelatedFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.PolygonizationFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.RGBSaturationFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.RainbowFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.RippleFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.RotationFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.SaturationFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.SepiaFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.SharpnessFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.SurfaceFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.TemperatureFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.ZebraFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import vn.loitp.libstream.uiza.encoder.input.video.CameraOpenException;
import vn.loitp.libstream.uiza.encoder.utils.gl.TranslateTo;
import vn.loitp.libstream.uiza.ossrs.rtmp.ConnectCheckerRtmp;
import vn.loitp.libstream.uiza.rtplibrary.rtmp.RtmpCamera1;
import vn.loitp.libstream.uiza.rtplibrary.view.OpenGlView;

public class LivestreamBroadcasterActivity extends BaseActivity implements ConnectCheckerRtmp, View.OnClickListener, SurfaceHolder.Callback {
    private RtmpCamera1 rtmpCamera1;
    private Button button;
    private Button bRecord;
    private EditText etUrl;

    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Uizalivestream");
    private OpenGlView openGlView;

    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
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

        openGlView = findViewById(R.id.surfaceView);
        button = findViewById(R.id.b_start_stop);
        button.setOnClickListener(this);
        bRecord = findViewById(R.id.b_record);
        bRecord.setOnClickListener(this);
        Button switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this);
        etUrl = findViewById(R.id.et_rtp_url);
        etUrl.setHint(R.string.hint_rtmp);
        etUrl.setText("rtmp://ap-southeast-1-u-01.uiza.io:80/push2transcode/test-loitp?token=ee86cb59c26bf734898ac4635aff1e82");
        //Number of filters to use at same time.
        // You must modify it before create rtmp or rtsp object.
        //ManagerRender.numFilters = 2;
        rtmpCamera1 = new RtmpCamera1(openGlView, this);
        openGlView.getHolder().addCallback(this);
        //openGlView.setKeepAspectRatio(true);
        //openGlView.setFrontPreviewFlip(true);

        Button filter = (Button) findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPopupMenu.show(activity, v, R.menu.gl_menu, new LPopupMenu.CallBack() {
                    @Override
                    public void clickOnItem(MenuItem menuItem) {
                        handleFilterClick(menuItem);
                    }
                });
            }
        });
    }

    private void handleFilterClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.e_d_fxaa:
                Toast.makeText(this,
                        "FXAA " + (rtmpCamera1.getGlInterface().isAAEnabled() ? " enabled" : "disabled"),
                        Toast.LENGTH_SHORT).show();
                rtmpCamera1.getGlInterface().enableAA(!rtmpCamera1.getGlInterface().isAAEnabled());
                //filters. NOTE: You can change filter values on fly without reset the filter.
                // Example:
                // ColorFilterRender color = new ColorFilterRender()
                // rtmpCamera1.setFilter(color);
                // color.setRGBColor(255, 0, 0); //red tint
                break;
            case R.id.no_filter:
                rtmpCamera1.getGlInterface().setFilter(new NoFilterRender());
                break;
            case R.id.android_view:
                AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
                androidViewFilterRender.setView(findViewById(R.id.activity_example_rtmp));
                rtmpCamera1.getGlInterface().setFilter(androidViewFilterRender);
                break;
            case R.id.basic_deformation:
                rtmpCamera1.getGlInterface().setFilter(new BasicDeformationFilterRender());
                break;
            case R.id.beauty:
                rtmpCamera1.getGlInterface().setFilter(new BeautyFilterRender());
                break;
            case R.id.blur:
                rtmpCamera1.getGlInterface().setFilter(new BlurFilterRender());
                break;
            case R.id.brightness:
                rtmpCamera1.getGlInterface().setFilter(new BrightnessFilterRender());
                break;
            case R.id.cartoon:
                rtmpCamera1.getGlInterface().setFilter(new CartoonFilterRender());
                break;
            case R.id.color:
                rtmpCamera1.getGlInterface().setFilter(new ColorFilterRender());
                break;
            case R.id.contrast:
                rtmpCamera1.getGlInterface().setFilter(new ContrastFilterRender());
                break;
            case R.id.duotone:
                rtmpCamera1.getGlInterface().setFilter(new DuotoneFilterRender());
                break;
            case R.id.early_bird:
                rtmpCamera1.getGlInterface().setFilter(new EarlyBirdFilterRender());
                break;
            case R.id.edge_detection:
                rtmpCamera1.getGlInterface().setFilter(new EdgeDetectionFilterRender());
                break;
            case R.id.exposure:
                rtmpCamera1.getGlInterface().setFilter(new ExposureFilterRender());
                break;
            case R.id.fire:
                rtmpCamera1.getGlInterface().setFilter(new FireFilterRender());
                break;
            case R.id.gamma:
                rtmpCamera1.getGlInterface().setFilter(new GammaFilterRender());
                break;
            case R.id.gif:
                setGifToStream();
                break;
            case R.id.grey_scale:
                rtmpCamera1.getGlInterface().setFilter(new GreyScaleFilterRender());
                break;
            case R.id.halftone_lines:
                rtmpCamera1.getGlInterface().setFilter(new HalftoneLinesFilterRender());
                break;
            case R.id.image:
                setImageToStream();
                break;
            case R.id.image_70s:
                rtmpCamera1.getGlInterface().setFilter(new Image70sFilterRender());
                break;
            case R.id.lamoish:
                rtmpCamera1.getGlInterface().setFilter(new LamoishFilterRender());
                break;
            case R.id.money:
                rtmpCamera1.getGlInterface().setFilter(new MoneyFilterRender());
                break;
            case R.id.negative:
                rtmpCamera1.getGlInterface().setFilter(new NegativeFilterRender());
                break;
            case R.id.pixelated:
                rtmpCamera1.getGlInterface().setFilter(new PixelatedFilterRender());
                break;
            case R.id.polygonization:
                rtmpCamera1.getGlInterface().setFilter(new PolygonizationFilterRender());
                break;
            case R.id.rainbow:
                rtmpCamera1.getGlInterface().setFilter(new RainbowFilterRender());
                break;
            case R.id.rgb_saturate:
                RGBSaturationFilterRender rgbSaturationFilterRender = new RGBSaturationFilterRender();
                rtmpCamera1.getGlInterface().setFilter(rgbSaturationFilterRender);
                //Reduce green and blue colors 20%. Red will predominate.
                rgbSaturationFilterRender.setRGBSaturation(1f, 0.8f, 0.8f);
                break;
            case R.id.ripple:
                rtmpCamera1.getGlInterface().setFilter(new RippleFilterRender());
                break;
            case R.id.rotation:
                RotationFilterRender rotationFilterRender = new RotationFilterRender();
                rtmpCamera1.getGlInterface().setFilter(rotationFilterRender);
                rotationFilterRender.setRotation(90);
                break;
            case R.id.saturation:
                rtmpCamera1.getGlInterface().setFilter(new SaturationFilterRender());
                break;
            case R.id.sepia:
                rtmpCamera1.getGlInterface().setFilter(new SepiaFilterRender());
                break;
            case R.id.sharpness:
                rtmpCamera1.getGlInterface().setFilter(new SharpnessFilterRender());
                break;
            case R.id.surface_filter:
                //You can render this filter with other api that draw in a surface. for example you can use VLC
                SurfaceFilterRender surfaceFilterRender = new SurfaceFilterRender();
                rtmpCamera1.getGlInterface().setFilter(surfaceFilterRender);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.big_bunny_240p);
                mediaPlayer.setSurface(surfaceFilterRender.getSurface());
                mediaPlayer.start();
                //Video is 360x240 so select a percent to keep aspect ratio (50% x 33.3% screen)
                surfaceFilterRender.setScale(50f, 33.3f);
                surfaceFilterRender.setListeners(openGlView); //Optional
                break;
            case R.id.temperature:
                rtmpCamera1.getGlInterface().setFilter(new TemperatureFilterRender());
                break;
            case R.id.text:
                setTextToStream();
                break;
            case R.id.zebra:
                rtmpCamera1.getGlInterface().setFilter(new ZebraFilterRender());
                break;
        }
    }

    private void setTextToStream() {
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        rtmpCamera1.getGlInterface().setFilter(textObjectFilterRender);
        textObjectFilterRender.setText("Hello world", 22, Color.RED);
        textObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(),
                rtmpCamera1.getStreamHeight());
        textObjectFilterRender.setPosition(TranslateTo.CENTER);
        textObjectFilterRender.setListeners(openGlView);  //Optional
    }

    private void setImageToStream() {
        ImageObjectFilterRender imageObjectFilterRender = new ImageObjectFilterRender();
        rtmpCamera1.getGlInterface().setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imageObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(),
                rtmpCamera1.getStreamHeight());
        imageObjectFilterRender.setPosition(TranslateTo.RIGHT);
        imageObjectFilterRender.setListeners(openGlView); //Optional
    }

    private void setGifToStream() {
        try {
            GifObjectFilterRender gifObjectFilterRender = new GifObjectFilterRender();
            gifObjectFilterRender.setGif(getResources().openRawResource(R.raw.banana));
            rtmpCamera1.getGlInterface().setFilter(gifObjectFilterRender);
            gifObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(), rtmpCamera1.getStreamHeight());
            gifObjectFilterRender.setPosition(TranslateTo.BOTTOM);
            gifObjectFilterRender.setListeners(openGlView); //Optional
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Connection success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                        .show();
                rtmpCamera1.stopStream();
                button.setText(R.string.start_button);
            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                if (!rtmpCamera1.isStreaming()) {
                    /*if (rtmpCamera1.isRecording()
                            || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                        button.setText(R.string.stop_button);
                        rtmpCamera1.startStream(etUrl.getText().toString());
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }*/
                    if (rtmpCamera1.prepareAudio(128, 44100, true, false,
                            false) && rtmpCamera1.prepareVideo(1920, 1080, 30, 2500000, false, 0)) {
                        rtmpCamera1.startStream(etUrl.getText().toString());
                    } else {
                    }
                } else {
                    button.setText(R.string.start_button);
                    rtmpCamera1.stopStream();
                }
                break;
            case R.id.switch_camera:
                try {
                    rtmpCamera1.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.b_record:
                if (!rtmpCamera1.isRecording()) {
                    try {
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        currentDateAndTime = sdf.format(new Date());
                        if (!rtmpCamera1.isStreaming()) {
                            if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                                rtmpCamera1.startRecord(
                                        folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                bRecord.setText(R.string.stop_record);
                                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error preparing stream, This device cant do it",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                            bRecord.setText(R.string.stop_record);
                            Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        rtmpCamera1.stopRecord();
                        bRecord.setText(R.string.start_record);
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    rtmpCamera1.stopRecord();
                    bRecord.setText(R.string.start_record);
                    Toast.makeText(this,
                            "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    currentDateAndTime = "";
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        rtmpCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
            bRecord.setText(R.string.start_record);
            Toast.makeText(this,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            currentDateAndTime = "";
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            button.setText(getResources().getString(R.string.start_button));
        }
        rtmpCamera1.stopPreview();
    }
}

package testlibuiza.sample.livestream;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.livestream.yasea.com.github.faucamp.simplertmp.RtmpHandler;
import vn.loitp.livestream.yasea.com.seu.magicfilter.utils.MagicFilterType;
import vn.loitp.livestream.yasea.net.ossrs.yasea.SrsCameraView;
import vn.loitp.livestream.yasea.net.ossrs.yasea.SrsEncodeHandler;
import vn.loitp.livestream.yasea.net.ossrs.yasea.SrsPublisher;
import vn.loitp.livestream.yasea.net.ossrs.yasea.SrsRecordHandler;
import vn.loitp.views.LToast;

public class LivestreamBroadcasterActivity extends BaseActivity implements RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {
    private Button btnPublish;
    private Button btnSwitchCamera;
    private Button btnRecord;
    private Button btnSwitchEncoder;
    private SharedPreferences sp;
    //private String rtmpUrl = "rtmp://ossrs.net/" + getRandomAlphaString(3) + '/' + getRandomAlphaDigitString(5);
    //private String rtmpUrl = "rtmp://14.161.0.68/live-origin/testapp";
    private String rtmpUrl = "rtmp://stag-ap-southeast-1-u-01.uiza.io:1935/push2transcode/test-live-loitp-transcode?token=3968e51d19bc7eaaff759b6792fe9630";
    //private String rtmpUrl = "rtmp://stag-ap-southeast-1-u-01.uiza.io:1935/push-only/suzuki-no-transcode?token=b9b9684f2be521fde1263ab8a62b8894";
    private String recPath = Environment.getExternalStorageDirectory().getPath() + "/testloitp.mp4";
    private SrsPublisher mPublisher;

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

    private static String getRandomAlphaString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private static String getRandomAlphaDigitString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        // response screen rotation event
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        //LActivityUtil.changeScreenLandscape(activity);
        // restore data.
        sp = getSharedPreferences("Yasea", MODE_PRIVATE);
        rtmpUrl = sp.getString("rtmpUrl", rtmpUrl);

        // initialize url.
        final EditText efu = (EditText) findViewById(R.id.url);
        efu.setText(rtmpUrl);

        btnPublish = (Button) findViewById(R.id.publish);
        btnSwitchCamera = (Button) findViewById(R.id.swCam);
        btnRecord = (Button) findViewById(R.id.record);
        btnSwitchEncoder = (Button) findViewById(R.id.swEnc);

        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.glsurfaceview_camera));
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        mPublisher.setPreviewResolution(640, 360);
        mPublisher.setOutputResolution(360, 640);
        /*mPublisher.setPreviewResolution(1280, 720);
        mPublisher.setOutputResolution(720, 1280);*/
        mPublisher.setVideoHDMode();
        mPublisher.startCamera();

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPublish.getText().toString().contentEquals("publish")) {
                    rtmpUrl = efu.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("rtmpUrl", rtmpUrl);
                    editor.apply();

                    mPublisher.startPublish(rtmpUrl);
                    mPublisher.startCamera();

                    if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
                        LToast.show(activity, "Use hard encoder");
                    } else {
                        LToast.show(activity, "Use soft encoder");
                    }
                    btnPublish.setText("stop");
                    btnSwitchEncoder.setEnabled(false);
                } else if (btnPublish.getText().toString().contentEquals("stop")) {
                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    btnPublish.setText("publish");
                    btnRecord.setText("record");
                    btnSwitchEncoder.setEnabled(true);
                }
            }
        });

        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRecord.getText().toString().contentEquals("record")) {
                    if (mPublisher.startRecord(recPath)) {
                        btnRecord.setText("pause");
                    }
                } else if (btnRecord.getText().toString().contentEquals("pause")) {
                    mPublisher.pauseRecord();
                    btnRecord.setText("resume");
                } else if (btnRecord.getText().toString().contentEquals("resume")) {
                    mPublisher.resumeRecord();
                    btnRecord.setText("pause");
                }
            }
        });

        btnSwitchEncoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
                    mPublisher.switchToSoftEncoder();
                    btnSwitchEncoder.setText("hard encoder");
                } else if (btnSwitchEncoder.getText().toString().contentEquals("hard encoder")) {
                    mPublisher.switchToHardEncoder();
                    btnSwitchEncoder.setText("soft encoder");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else {
            switch (id) {
                case R.id.cool_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.COOL);
                    break;
                case R.id.beauty_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
                    break;
                case R.id.early_bird_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.EARLYBIRD);
                    break;
                case R.id.evergreen_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.EVERGREEN);
                    break;
                case R.id.n1977_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.N1977);
                    break;
                case R.id.nostalgia_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.NOSTALGIA);
                    break;
                case R.id.romance_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.ROMANCE);
                    break;
                case R.id.sunrise_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.SUNRISE);
                    break;
                case R.id.sunset_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.SUNSET);
                    break;
                case R.id.tender_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.TENDER);
                    break;
                case R.id.toast_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.TOASTER2);
                    break;
                case R.id.valencia_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.VALENCIA);
                    break;
                case R.id.walden_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.WALDEN);
                    break;
                case R.id.warm_filter:
                    mPublisher.switchCameraFilter(MagicFilterType.WARM);
                    break;
                case R.id.original_filter:
                default:
                    mPublisher.switchCameraFilter(MagicFilterType.NONE);
                    break;
            }
        }
        setTitle(item.getTitle());

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button btn = (Button) findViewById(R.id.publish);
        btn.setEnabled(true);
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
        btnRecord.setText("record");
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (btnPublish.getText().toString().contentEquals("stop")) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }

    private void handleException(Exception e) {
        try {
            LToast.show(activity, e.getMessage());
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            btnPublish.setText("publish");
            btnRecord.setText("record");
            btnSwitchEncoder.setEnabled(true);
        } catch (Exception e1) {
            //
        }
    }

    // Implementation of SrsRtmpListener.

    @Override
    public void onRtmpConnecting(String msg) {
        LLog.d(TAG, "onRtmpConnecting " + msg);
    }

    @Override
    public void onRtmpConnected(String msg) {
        LLog.d(TAG, "onRtmpConnected " + msg);
    }

    @Override
    public void onRtmpVideoStreaming() {
        //LLog.d(TAG, "onRtmpVideoStreaming");
    }

    @Override
    public void onRtmpAudioStreaming() {
        //LLog.d(TAG, "onRtmpAudioStreaming");
    }

    @Override
    public void onRtmpStopped() {
        LLog.d(TAG, "onRtmpStopped");
    }

    @Override
    public void onRtmpDisconnected() {
        LLog.d(TAG, "onRtmpDisconnected");
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        LLog.d(TAG, "onRtmpVideoFpsChanged fps " + fps);
    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            LLog.d(TAG, String.format("onRtmpVideoBitrateChanged Video bitrate: %f kbps", bitrate / 1000));
        } else {
            LLog.d(TAG, String.format("onRtmpVideoBitrateChanged Video bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            LLog.d(TAG, String.format("onRtmpAudioBitrateChanged Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            LLog.d(TAG, String.format("onRtmpAudioBitrateChanged Audio bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        LLog.e(TAG, "onRtmpSocketException " + e.toString());
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        LLog.e(TAG, "onRtmpIOException " + e.toString());
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        LLog.e(TAG, "onRtmpIllegalArgumentException " + e.toString());
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        LLog.e(TAG, "onRtmpIllegalStateException " + e.toString());
        handleException(e);
    }

    // Implementation of SrsRecordHandler.

    @Override
    public void onRecordPause() {
        LLog.d(TAG, "onRecordPause");
        LToast.show(activity, "Record paused");
    }

    @Override
    public void onRecordResume() {
        LLog.d(TAG, "onRecordResume");
        LToast.show(activity, "Record resumed");
    }

    @Override
    public void onRecordStarted(String msg) {
        LLog.d(TAG, "onRecordStarted");
        LToast.show(activity, "Recording file: " + msg);
    }

    @Override
    public void onRecordFinished(String msg) {
        LLog.d(TAG, "onRecordFinished");
        LToast.show(activity, "MP4 file saved: " + msg);
    }

    @Override
    public void onRecordIOException(IOException e) {
        LLog.e(TAG, "onRecordIOException " + e.toString());
        handleException(e);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        LLog.e(TAG, "onRecordIllegalArgumentException " + e.toString());
        handleException(e);
    }

    // Implementation of SrsEncodeHandler.
    @Override
    public void onNetworkWeak() {
        LToast.show(activity, "Network weak");
    }

    @Override
    public void onNetworkResume() {
        LToast.show(activity, "Network resume");
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        LLog.e(TAG, "onEncodeIllegalArgumentException " + e.toString());
        handleException(e);
    }

}

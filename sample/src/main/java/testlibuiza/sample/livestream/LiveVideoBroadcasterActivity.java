package testlibuiza.sample.livestream;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.uizavideov3.view.rl.livestream.UizaLivestream;

public class LiveVideoBroadcasterActivity extends BaseActivity {
    private UizaLivestream uizaLivestream;
    private Button btStartLivestream;
    private Button btStopLivestream;
    private Button btSwitchCamera;

    @Override
    protected boolean setFullScreen() {
        LScreenUtil.hideDefaultControls(activity);
        return true;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_live_video_broadcaster;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        uizaLivestream = (UizaLivestream) findViewById(R.id.uiza_livestream);
        btStartLivestream = (Button) findViewById(R.id.bt_start_livestream);
        btStopLivestream = (Button) findViewById(R.id.bt_stop_livestream);
        btSwitchCamera = (Button) findViewById(R.id.bt_switch_camera);
        btStartLivestream.setEnabled(false);
        btStopLivestream.setEnabled(false);
        btSwitchCamera.setEnabled(false);
        uizaLivestream.setCallback(new UizaLivestream.Callback() {
            @Override
            public void onReadyToLivestream() {
                btStartLivestream.setEnabled(true);
                btSwitchCamera.setEnabled(true);
            }
        });

        btStartLivestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String streamUrl = "rtmp://stag-ap-southeast-1-u-01.uiza.io:1935/push-only";
                String streamKey = "4e9bea68-b59e-4932-b5c7-9724c62fe8f2?token=6d8317424131fc7da87c7168d8189b01";
                uizaLivestream.setStreamUrl(streamUrl + "/" + streamKey);
                uizaLivestream.startLivestream();

                btStartLivestream.setEnabled(false);
                btStopLivestream.setEnabled(true);
            }
        });

        btStopLivestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaLivestream.stopLivestream();

                btStartLivestream.setEnabled(true);
                btStopLivestream.setEnabled(false);
            }
        });

        btSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uizaLivestream.flipCamera();
            }
        });
    }

    @Override
    protected void onDestroy() {
        uizaLivestream.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        uizaLivestream.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        uizaLivestream.onResume();
        super.onResume();
    }
}

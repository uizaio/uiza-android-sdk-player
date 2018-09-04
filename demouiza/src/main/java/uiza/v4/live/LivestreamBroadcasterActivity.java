package uiza.v4.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import uiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;

public class LivestreamBroadcasterActivity extends BaseActivity implements View.OnClickListener {
    private TextView bStartStop;
    private TextView bStartStopStore;
    private FloatingActionButton btSwitchCamera;
    private FloatingActionButton btFilter;

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
        return R.layout.v4_activity_livestream_video_broadcaster;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        //LActivityUtil.changeScreenLandscape(activity);

        bStartStop = findViewById(R.id.b_start_stop);
        bStartStopStore = findViewById(R.id.b_start_stop_store);
        btSwitchCamera = findViewById(R.id.b_switch_camera);
        btFilter = findViewById(R.id.b_filter);

        bStartStop.setVisibility(View.INVISIBLE);
        bStartStopStore.setVisibility(View.INVISIBLE);
        btSwitchCamera.setVisibility(View.INVISIBLE);
        btFilter.setVisibility(View.INVISIBLE);

        bStartStop.setOnClickListener(this);
        bStartStopStore.setOnClickListener(this);
        btSwitchCamera.setOnClickListener(this);
        btFilter.setOnClickListener(this);

        String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);

        /*String x = "";
        List<MediaCodecInfo> mediaCodecInfos = CodecUtil.getAllCodecs();
        for (MediaCodecInfo mediaCodecInfo : mediaCodecInfos) {
            x += mediaCodecInfo.getName() + "\n";
        }
        LLog.d(TAG, "loitp " + x);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                break;
            case R.id.b_start_stop_store:
                break;
            case R.id.b_switch_camera:
                break;
            case R.id.b_filter:
                break;
            default:
                break;
        }
    }
}

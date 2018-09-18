package testlibuiza.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.app.R;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.utilities.LLog;

public class MainActivity extends BaseActivity implements View.OnFocusChangeListener {
    private EditText etInput;
    private Button btVod;
    private Button btLive;
    private Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etInput = (EditText) findViewById(R.id.et_input);
        btVod = (Button) findViewById(R.id.bt_vod);
        btLive = (Button) findViewById(R.id.bt_live);
        btStart = (Button) findViewById(R.id.bt_start);

        etInput.setOnFocusChangeListener(this);
        btVod.setOnFocusChangeListener(this);
        btLive.setOnFocusChangeListener(this);
        btStart.setOnFocusChangeListener(this);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == etInput) {
            LLog.d(TAG, "onFocusChange etInput");
        } else if (view == btVod) {
            LLog.d(TAG, "onFocusChange btVod");
        } else if (view == btLive) {
            LLog.d(TAG, "onFocusChange btLive");
        } else if (view == btStart) {
            LLog.d(TAG, "onFocusChange btStart");
        }
    }
}

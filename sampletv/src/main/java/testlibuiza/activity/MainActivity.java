package testlibuiza.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.app.LSApplication;
import testlibuiza.app.R;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.utils.util.KeyboardUtils;
import vn.uiza.views.LToast;

public class MainActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private EditText etInput;
    private Button btVod;
    private Button btLive;
    private Button btPlaylistFolder;
    private Button btClear;
    private Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etInput = (EditText) findViewById(R.id.et_input);
        btVod = (Button) findViewById(R.id.bt_vod);
        btLive = (Button) findViewById(R.id.bt_live);
        btPlaylistFolder = (Button) findViewById(R.id.bt_playlist_folder);
        btClear = (Button) findViewById(R.id.bt_clear);
        btStart = (Button) findViewById(R.id.bt_start);

        etInput.setText(LSApplication.entityIdDefaultVOD);

        etInput.setOnFocusChangeListener(this);
        btVod.setOnFocusChangeListener(this);
        btLive.setOnFocusChangeListener(this);
        btPlaylistFolder.setOnFocusChangeListener(this);
        btClear.setOnFocusChangeListener(this);
        btStart.setOnFocusChangeListener(this);

        updateUI(etInput, true);
        updateUI(btVod, false);
        updateUI(btLive, false);
        updateUI(btPlaylistFolder, false);
        updateUI(btClear, false);
        updateUI(btStart, false);

        btVod.setOnClickListener(this);
        btLive.setOnClickListener(this);
        btPlaylistFolder.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStart.setOnClickListener(this);
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
    public void onFocusChange(View view, boolean isFocus) {
        updateUI(view, isFocus);
        if (view == etInput) {
            LLog.d(TAG, "onFocusChange etInput " + isFocus);
            if (isFocus) {
                etInput.setTextColor(Color.BLACK);
                etInput.setHintTextColor(Color.BLACK);
            } else {
                etInput.setTextColor(Color.WHITE);
                etInput.setHintTextColor(Color.WHITE);
            }
            KeyboardUtils.toggleSoftInput();
        }
    }

    private void updateUI(View view, boolean isFocus) {
        if (view == null) {
            return;
        }
        if (isFocus) {
            view.setBackgroundResource(R.drawable.bkg_has_focus);
        } else {
            view.setBackgroundResource(R.drawable.bkg_no_focus);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btVod) {
            etInput.setText(LSApplication.entityIdDefaultVOD);
        } else if (view == btLive) {
            etInput.setText(LSApplication.entityIdDefaultLIVE);
        } else if (view == btPlaylistFolder) {
            etInput.setText(LSApplication.metadataDefault0);
        } else if (view == btClear) {
            etInput.setText("");
        } else if (view == btStart) {
            if (etInput.getText().toString().isEmpty()) {
                LToast.show(activity, "Please input the entityId");
            } else {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, etInput.getText().toString());
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        }
    }
}

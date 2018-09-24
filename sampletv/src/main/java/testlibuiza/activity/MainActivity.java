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
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

public class MainActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private EditText etInput;
    private Button btVod;
    private Button btLive;
    private Button btPlaylistFolder;
    private Button btClear;
    private Button btStartEntity;
    private Button btStartPlaylistFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etInput = (EditText) findViewById(R.id.et_input);
        btVod = (Button) findViewById(R.id.bt_vod);
        btLive = (Button) findViewById(R.id.bt_live);
        btPlaylistFolder = (Button) findViewById(R.id.bt_playlist_folder);
        btClear = (Button) findViewById(R.id.bt_clear);
        btStartEntity = (Button) findViewById(R.id.bt_start_entity);
        btStartPlaylistFolder = (Button) findViewById(R.id.bt_start_playlist_folder);

        etInput.setText(LSApplication.entityIdDefaultVOD);

        etInput.setOnFocusChangeListener(this);
        btVod.setOnFocusChangeListener(this);
        btLive.setOnFocusChangeListener(this);
        btPlaylistFolder.setOnFocusChangeListener(this);
        btClear.setOnFocusChangeListener(this);
        btStartEntity.setOnFocusChangeListener(this);
        btStartPlaylistFolder.setOnFocusChangeListener(this);

        UZUtil.updateUIFocusChange(etInput, true);
        UZUtil.updateUIFocusChange(btVod, false);
        UZUtil.updateUIFocusChange(btLive, false);
        UZUtil.updateUIFocusChange(btPlaylistFolder, false);
        UZUtil.updateUIFocusChange(btClear, false);
        UZUtil.updateUIFocusChange(btStartEntity, false);
        UZUtil.updateUIFocusChange(btStartPlaylistFolder, false);

        btVod.setOnClickListener(this);
        btLive.setOnClickListener(this);
        btPlaylistFolder.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStartEntity.setOnClickListener(this);
        btStartPlaylistFolder.setOnClickListener(this);
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
        UZUtil.updateUIFocusChange(view, isFocus);
        if (view == etInput) {
            //LLog.d(TAG, "onFocusChange etInput " + isFocus);
            if (isFocus) {
                etInput.setTextColor(Color.BLACK);
                etInput.setHintTextColor(Color.BLACK);
            } else {
                etInput.setTextColor(Color.WHITE);
                etInput.setHintTextColor(Color.WHITE);
            }
            //KeyboardUtils.toggleSoftInput();
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
        } else if (view == btStartEntity) {
            if (etInput.getText().toString().isEmpty()) {
                LToast.show(activity, "Please input the Id");
            } else {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, etInput.getText().toString());
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        } else if (view == btStartPlaylistFolder) {
            if (etInput.getText().toString().isEmpty()) {
                LToast.show(activity, "Please input the Id");
            } else {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, etInput.getText().toString());
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        }
    }

    /*@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LLog.d(TAG, "onKeyUp " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_REWIND");
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PLAY_PAUSE");
                return true;
            case KeyEvent.KEYCODE_BACK:
                LLog.d(TAG, "onKeyUp KEYCODE_BACK");
                return true;
            case KeyEvent.KEYCODE_UNKNOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_UNKNOWN");
                return true;
            case KeyEvent.KEYCODE_BUTTON_1:
                LLog.d(TAG, "onKeyUp KEYCODE_BUTTON_1");
                return true;
            case KeyEvent.KEYCODE_BUTTON_A:
                LLog.d(TAG, "onKeyUp KEYCODE_BUTTON_A");
                return true;
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                LLog.d(TAG, "onKeyUp KEYCODE_BUTTON_SELECT");
                return true;
            case KeyEvent.KEYCODE_BUTTON_START:
                LLog.d(TAG, "onKeyUp KEYCODE_BUTTON_START");
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_CHANNEL_DOWN");
                return true;
            case KeyEvent.KEYCODE_CLEAR:
                LLog.d(TAG, "onKeyUp KEYCODE_CLEAR");
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_CENTER");
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_DOWN");
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_LEFT");
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_RIGHT");
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_UP");
                return true;
            case KeyEvent.KEYCODE_ENTER:
                LLog.d(TAG, "onKeyUp KEYCODE_ENTER");
                return true;
            case KeyEvent.KEYCODE_HOME:
                LLog.d(TAG, "onKeyUp KEYCODE_HOME");
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PLAY");
                return true;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PREVIOUS");
                return true;
            case KeyEvent.KEYCODE_MENU:
                LLog.d(TAG, "onKeyUp KEYCODE_MENU");
                return true;
            case KeyEvent.KEYCODE_MUTE:
                LLog.d(TAG, "onKeyUp KEYCODE_MUTE");
                return true;
            case KeyEvent.KEYCODE_NUMPAD_0:
                LLog.d(TAG, "onKeyUp KEYCODE_NUMPAD_0");
                return true;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_PAGE_DOWN");
                return true;
            case KeyEvent.KEYCODE_TV_POWER:
                LLog.d(TAG, "onKeyUp KEYCODE_TV_POWER");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_VOLUME_DOWN");
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }*/
}

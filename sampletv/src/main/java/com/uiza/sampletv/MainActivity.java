package com.uiza.sampletv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import io.uiza.player.UzPlayerConfig;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private EditText etInput;
    private Button btVod;
    private Button btLive;
    private Button btPlaylistFolder;
    private Button btClear;
    private Button btStartEntity;
    private Button btStartPlaylistFolder;
    private Button btCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etInput = findViewById(R.id.et_input);
        btVod = findViewById(R.id.bt_vod);
        btLive = findViewById(R.id.bt_live);
        btPlaylistFolder = findViewById(R.id.bt_playlist_folder);
        btClear = findViewById(R.id.bt_clear);
        btStartEntity = findViewById(R.id.bt_start_entity);
        btStartPlaylistFolder = findViewById(R.id.bt_start_playlist_folder);
        btCustom = findViewById(R.id.bt_custom);
        if (Constants.IS_DEBUG) {
            btCustom.setVisibility(View.VISIBLE);
        } else {
            btCustom.setVisibility(View.GONE);
        }

        etInput.setText(LSApplication.entityIdDefaultVOD);
        UzDisplayUtil.setLastCursorEditText(etInput);

        etInput.setOnFocusChangeListener(this);
        btVod.setOnFocusChangeListener(this);
        btLive.setOnFocusChangeListener(this);
        btPlaylistFolder.setOnFocusChangeListener(this);
        btClear.setOnFocusChangeListener(this);
        btStartEntity.setOnFocusChangeListener(this);
        btStartPlaylistFolder.setOnFocusChangeListener(this);
        btCustom.setOnFocusChangeListener(this);

        UzPlayerConfig.updateUiFocusChange(etInput, true);
        UzPlayerConfig.updateUiFocusChange(btVod, false);
        UzPlayerConfig.updateUiFocusChange(btLive, false);
        UzPlayerConfig.updateUiFocusChange(btPlaylistFolder, false);
        UzPlayerConfig.updateUiFocusChange(btClear, false);
        UzPlayerConfig.updateUiFocusChange(btStartEntity, false);
        UzPlayerConfig.updateUiFocusChange(btStartPlaylistFolder, false);
        UzPlayerConfig.updateUiFocusChange(btCustom, false);

        btVod.setOnClickListener(this);
        btLive.setOnClickListener(this);
        btPlaylistFolder.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStartEntity.setOnClickListener(this);
        btStartPlaylistFolder.setOnClickListener(this);
        btCustom.setOnClickListener(this);

        TextView tvVs = findViewById(R.id.tv_vs);
        UzDisplayUtil.setTextShadow(tvVs);
        tvVs.setText("© 2018 Uiza. All rights reserved.\nVersion " + BuildConfig.VERSION_NAME);

        if (LSApplication.DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();
            onBackPressed();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        UzPlayerConfig.updateUiFocusChange(view, isFocus);
        if (view == etInput) {
            //LLog.d(TAG, "onFocusChange etInput " + isFocus);
            if (isFocus) {
                etInput.setTextColor(Color.BLACK);
                etInput.setHintTextColor(Color.BLACK);
            } else {
                etInput.setTextColor(Color.WHITE);
                etInput.setHintTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btVod) {
            etInput.setText(LSApplication.entityIdDefaultVOD);
            UzDisplayUtil.setLastCursorEditText(etInput);
        } else if (view == btLive) {
            etInput.setText(LSApplication.entityIdDefaultLIVE);
            UzDisplayUtil.setLastCursorEditText(etInput);
        } else if (view == btPlaylistFolder) {
            etInput.setText(LSApplication.metadataDefault0);
            UzDisplayUtil.setLastCursorEditText(etInput);
        } else if (view == btClear) {
            etInput.setText("");
        } else if (view == btStartEntity) {
            if (etInput.getText().toString().isEmpty()) {
                LToast.show(activity, "Please input the Id");
            } else {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, etInput.getText().toString());
                startActivity(intent);
            }
        } else if (view == btStartPlaylistFolder) {
            if (etInput.getText().toString().isEmpty()) {
                LToast.show(activity, "Please input the Id");
            } else {
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID,
                        etInput.getText().toString());
                startActivity(intent);
            }
        } else if (view == btCustom) {
            Intent intent = new Intent(activity, PlayerCustomActivity.class);
            startActivity(intent);
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

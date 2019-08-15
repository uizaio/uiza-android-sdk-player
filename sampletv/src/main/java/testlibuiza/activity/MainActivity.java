package testlibuiza.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import testlibuiza.app.BuildConfig;
import testlibuiza.app.LSApplication;
import testlibuiza.app.R;
import uizacoresdk.util.UZUtil;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
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
        etInput = (EditText) findViewById(R.id.et_input);
        btVod = (Button) findViewById(R.id.bt_vod);
        btLive = (Button) findViewById(R.id.bt_live);
        btPlaylistFolder = (Button) findViewById(R.id.bt_playlist_folder);
        btClear = (Button) findViewById(R.id.bt_clear);
        btStartEntity = (Button) findViewById(R.id.bt_start_entity);
        btStartPlaylistFolder = (Button) findViewById(R.id.bt_start_playlist_folder);
        btCustom = (Button) findViewById(R.id.bt_custom);
        if (Constants.IS_DEBUG) {
            btCustom.setVisibility(View.VISIBLE);
        } else {
            btCustom.setVisibility(View.GONE);
        }

        etInput.setText(LSApplication.getInstance().entityIdDefaultVOD);
        UzDisplayUtil.setLastCursorEditText(etInput);

        etInput.setOnFocusChangeListener(this);
        btVod.setOnFocusChangeListener(this);
        btLive.setOnFocusChangeListener(this);
        btPlaylistFolder.setOnFocusChangeListener(this);
        btClear.setOnFocusChangeListener(this);
        btStartEntity.setOnFocusChangeListener(this);
        btStartPlaylistFolder.setOnFocusChangeListener(this);
        btCustom.setOnFocusChangeListener(this);

        UZUtil.updateUIFocusChange(etInput, true);
        UZUtil.updateUIFocusChange(btVod, false);
        UZUtil.updateUIFocusChange(btLive, false);
        UZUtil.updateUIFocusChange(btPlaylistFolder, false);
        UZUtil.updateUIFocusChange(btClear, false);
        UZUtil.updateUIFocusChange(btStartEntity, false);
        UZUtil.updateUIFocusChange(btStartPlaylistFolder, false);
        UZUtil.updateUIFocusChange(btCustom, false);

        btVod.setOnClickListener(this);
        btLive.setOnClickListener(this);
        btPlaylistFolder.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btStartEntity.setOnClickListener(this);
        btStartPlaylistFolder.setOnClickListener(this);
        btCustom.setOnClickListener(this);

        TextView tvVs = (TextView) findViewById(R.id.tv_vs);
        UzDisplayUtil.setTextShadow(tvVs);
        tvVs.setText("© 2018 Uiza. All rights reserved.\nVersion " + BuildConfig.VERSION_NAME);

        if (LSApplication.getInstance().DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btVod) {
            etInput.setText(LSApplication.getInstance().entityIdDefaultVOD);
            UzDisplayUtil.setLastCursorEditText(etInput);
        } else if (view == btLive) {
            etInput.setText(LSApplication.getInstance().entityIdDefaultLIVE);
            UzDisplayUtil.setLastCursorEditText(etInput);
        } else if (view == btPlaylistFolder) {
            etInput.setText(LSApplication.getInstance().metadataDefault0);
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
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, etInput.getText().toString());
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

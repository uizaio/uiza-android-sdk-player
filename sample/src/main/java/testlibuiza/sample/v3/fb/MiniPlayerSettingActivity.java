package testlibuiza.sample.v3.fb;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import testlibuiza.R;
import uizacoresdk.util.UZUtil;

public class MiniPlayerSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private Button btColor0;
    private Button btColor1;
    private Button btColor2;
    private Button btSaveFirstPosition;
    private Switch swTapToFullPlayer;
    private Switch swEzDestroy;
    private Switch swVibrateDestroy;
    private Switch swSmoothSwitch;
    private Switch swAutoSize;
    private EditText etPositionX;
    private EditText etPositionY;
    private EditText etMarginLeft;
    private EditText etMarginTop;
    private EditText etMarginRight;
    private EditText etMarginBottom;
    private EditText etSizeW;
    private EditText etSizeH;
    private Button btSaveMargin;
    private Button btShowController;
    private Button btHideController;
    private Button btToggleController;
    private Button btPlay;
    private Button btPause;
    private Button btPlayPause;
    private Button btFullScreen;
    private Button btStopMiniPlayer;
    private Button btSaveSizeConfig;
    private Button btAppear;
    private Button btDisapper;

    private void findViews() {
        btColor0 = (Button) findViewById(R.id.bt_color_0);
        btColor1 = (Button) findViewById(R.id.bt_color_1);
        btColor2 = (Button) findViewById(R.id.bt_color_2);
        swTapToFullPlayer = (Switch) findViewById(R.id.sw_tap_to_full_player);
        swEzDestroy = (Switch) findViewById(R.id.sw_ez_destroy);
        swVibrateDestroy = (Switch) findViewById(R.id.sw_vibrate_destroy);
        swSmoothSwitch = (Switch) findViewById(R.id.sw_smooth_switch);
        swAutoSize = (Switch) findViewById(R.id.sw_auto_size);
        etPositionX = (EditText) findViewById(R.id.et_position_x);
        etPositionY = (EditText) findViewById(R.id.et_position_y);
        etMarginLeft = (EditText) findViewById(R.id.et_margin_left);
        etMarginTop = (EditText) findViewById(R.id.et_margin_top);
        etMarginRight = (EditText) findViewById(R.id.et_margin_right);
        etMarginBottom = (EditText) findViewById(R.id.et_margin_bottom);
        etSizeW = (EditText) findViewById(R.id.et_size_w);
        etSizeH = (EditText) findViewById(R.id.et_size_h);
        btSaveFirstPosition = (Button) findViewById(R.id.bt_save_first_position);
        btSaveMargin = (Button) findViewById(R.id.bt_save_margin);
        btShowController = (Button) findViewById(R.id.bt_show_controller);
        btHideController = (Button) findViewById(R.id.bt_hide_controller);
        btToggleController = (Button) findViewById(R.id.bt_toggle_controller);
        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_pause);
        btPlayPause = (Button) findViewById(R.id.bt_play_pause);
        btFullScreen = (Button) findViewById(R.id.bt_full_screen);
        btStopMiniPlayer = (Button) findViewById(R.id.bt_stop_mini_player);
        btSaveSizeConfig = (Button) findViewById(R.id.bt_save_size_config);
        btAppear = (Button) findViewById(R.id.bt_appear);
        btDisapper = (Button) findViewById(R.id.bt_disapper);
        btColor0.setOnClickListener(this);
        btColor1.setOnClickListener(this);
        btColor2.setOnClickListener(this);
        btSaveFirstPosition.setOnClickListener(this);
        btSaveMargin.setOnClickListener(this);
        btShowController.setOnClickListener(this);
        btHideController.setOnClickListener(this);
        btToggleController.setOnClickListener(this);
        btPlay.setOnClickListener(this);
        btPause.setOnClickListener(this);
        btPlayPause.setOnClickListener(this);
        btFullScreen.setOnClickListener(this);
        btStopMiniPlayer.setOnClickListener(this);
        btSaveSizeConfig.setOnClickListener(this);
        btAppear.setOnClickListener(this);
        btDisapper.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mini_player);
        findViews();
        int currentViewDestroyColor = UZUtil.getMiniPlayerColorViewDestroy(activity);
        if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.black_65)) {
            btColor0.setText("Default");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.RedTrans)) {
            btColor1.setText("RedTrans");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.GreenTran)) {
            btColor2.setText("GreenTran");
        }

        boolean isTapToFullPlayer = UZUtil.getMiniPlayerTapToFullPlayer(activity);
        swTapToFullPlayer.setChecked(isTapToFullPlayer);
        setSwTapToFullPlayer(isTapToFullPlayer);
        swTapToFullPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSwTapToFullPlayer(isChecked);
            }
        });

        boolean isEZDestroy = UZUtil.getMiniPlayerEzDestroy(activity);
        swEzDestroy.setChecked(isEZDestroy);
        setSwEzDestroy(isEZDestroy);
        swEzDestroy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSwEzDestroy(isChecked);
            }
        });

        boolean isEnableVibration = UZUtil.getMiniPlayerEnableVibration(activity);
        swVibrateDestroy.setChecked(isEnableVibration);
        setSwVibrateDestroy(isEnableVibration);
        swVibrateDestroy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSwVibrateDestroy(isChecked);
            }
        });

        boolean isEnableSmoothSwitch = UZUtil.getMiniPlayerEnableSmoothSwitch(activity);
        swSmoothSwitch.setChecked(isEnableSmoothSwitch);
        setSwSmoothSwitch(isEnableVibration);
        swSmoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSwSmoothSwitch(isChecked);
            }
        });

        boolean isAutoSize = UZUtil.getMiniPlayerAutoSize(activity);
        swAutoSize.setChecked(isAutoSize);
        etSizeW.setText("" + UZUtil.getMiniPlayerSizeWidth(activity));
        etSizeH.setText("" + UZUtil.getMiniPlayerSizeHeight(activity));
        if (isAutoSize) {
            swAutoSize.setText("Auto: Value width and height will be ignored");
        } else {
            swAutoSize.setText("Manual: Type your custom size (Dp)");
        }
        swAutoSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swAutoSize.setText("Auto: Value width and height will be ignored");
                } else {
                    swAutoSize.setText("Manual: Type your custom size (Dp)");
                }
            }
        });

        int firstPositionX = UZUtil.getMiniPlayerFirstPositionX(activity);
        int firstPositionY = UZUtil.getMiniPlayerFirstPositionY(activity);
        if (firstPositionX == Constants.NOT_FOUND || firstPositionY == Constants.NOT_FOUND) {
            //default: BOTTOM_RIGHT of device screen
        } else {
            etPositionX.setText(firstPositionX + "");
            etPositionY.setText(firstPositionY + "");
        }
        int marginL = UZUtil.getMiniPlayerMarginL(activity);
        int marginT = UZUtil.getMiniPlayerMarginT(activity);
        int marginR = UZUtil.getMiniPlayerMarginR(activity);
        int marginB = UZUtil.getMiniPlayerMarginB(activity);
        etMarginLeft.setText(marginL + "");
        etMarginTop.setText(marginT + "");
        etMarginRight.setText(marginR + "");
        etMarginBottom.setText(marginB + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_color_0:
                UZUtil.setMiniPlayerColorViewDestroy(activity, ContextCompat.getColor(activity, R.color.black_65));
                btColor0.setText("Default");
                btColor1.setText("");
                btColor2.setText("");
                break;
            case R.id.bt_color_1:
                UZUtil.setMiniPlayerColorViewDestroy(activity, ContextCompat.getColor(activity, R.color.RedTrans));
                btColor0.setText("");
                btColor1.setText("RedTrans");
                btColor2.setText("");
                break;
            case R.id.bt_color_2:
                UZUtil.setMiniPlayerColorViewDestroy(activity, ContextCompat.getColor(activity, R.color.GreenTran));
                btColor0.setText("");
                btColor1.setText("");
                btColor2.setText("GreenTran");
                break;
            case R.id.bt_save_first_position:
                saveConfigFirstPosition();
                break;
            case R.id.bt_save_margin:
                saveConfigMarginDp();
                break;
            case R.id.bt_show_controller:
                UZUtil.showMiniPlayerController(activity);
                break;
            case R.id.bt_hide_controller:
                UZUtil.hideMiniPlayerController(activity);
                break;
            case R.id.bt_toggle_controller:
                UZUtil.toggleMiniPlayerController(activity);
                break;
            case R.id.bt_play:
                UZUtil.resumeVideo(activity);
                break;
            case R.id.bt_pause:
                UZUtil.pauseVideo(activity);
                break;
            case R.id.bt_play_pause:
                UZUtil.toggleResumePauseVideo(activity);
                break;
            case R.id.bt_full_screen:
                UZUtil.openAppFromMiniPlayer(activity);
                break;
            case R.id.bt_stop_mini_player:
                UZUtil.stopMiniPlayer(activity);
                break;
            case R.id.bt_save_size_config:
                saveConfigSize();
                break;
            case R.id.bt_appear:
                UZUtil.appearMiniplayer(activity);
                break;
            case R.id.bt_disapper:
                UZUtil.disappearMiniplayer(activity);
                break;
        }
    }

    private void setSwTapToFullPlayer(boolean isChecked) {
        if (isChecked) {
            swTapToFullPlayer.setText("On");
        } else {
            swTapToFullPlayer.setText("Off");
        }
        UZUtil.setMiniPlayerTapToFullPlayer(activity, isChecked);
    }

    private void setSwEzDestroy(boolean isChecked) {
        if (isChecked) {
            swEzDestroy.setText("On");
        } else {
            swEzDestroy.setText("Off");
        }
        UZUtil.setMiniPlayerEzDestroy(activity, isChecked);
    }

    private void setSwVibrateDestroy(boolean isChecked) {
        if (isChecked) {
            swVibrateDestroy.setText("On");
        } else {
            swVibrateDestroy.setText("Off");
        }
        UZUtil.setMiniPlayerEnableVibration(activity, isChecked);
    }

    private void setSwSmoothSwitch(boolean isChecked) {
        if (isChecked) {
            swSmoothSwitch.setText("On");
        } else {
            swSmoothSwitch.setText("Off");
        }
        UZUtil.setMiniPlayerEnableSmoothSwitch(activity, isChecked);
    }

    private void saveConfigFirstPosition() {
        String x = etPositionX.getText().toString();
        String y = etPositionY.getText().toString();
        if (x.isEmpty() || y.isEmpty()) {
            LToast.show(activity, "Cannot set first position of mini player");
            return;
        }
        int firstPositionX = Integer.parseInt(x);
        int firstPositionY = Integer.parseInt(y);
        UZUtil.setMiniPlayerFirstPosition(activity, firstPositionX, firstPositionY);
        LToast.show(activity, "First position of mini player: " + firstPositionX + "x" + firstPositionY);
    }

    private void saveConfigMarginDp() {
        try {
            String l = etMarginLeft.getText().toString();
            String t = etMarginTop.getText().toString();
            String r = etMarginRight.getText().toString();
            String b = etMarginBottom.getText().toString();
            if (l.isEmpty() || t.isEmpty() || r.isEmpty() || b.isEmpty()) {
                LToast.show(activity, "Cannot set property margin.");
                return;
            }
            int marginL = Integer.parseInt(l);
            int marginT = Integer.parseInt(t);
            int marginR = Integer.parseInt(r);
            int marginB = Integer.parseInt(b);
            boolean isSuccess = UZUtil.setMiniPlayerMarginDp(activity, marginL, marginT, marginR, marginB);
            LToast.show(activity, "Set margin: " + isSuccess);
        } catch (Exception e) {
            LToast.show(activity, "Invalid value: " + e.toString());
        }
    }

    private void saveConfigSize() {
        try {
            boolean isAutoSize = swAutoSize.isChecked();
            String width = etSizeW.getText().toString();
            String height = etSizeH.getText().toString();
            if (!isAutoSize) {
                if (width.isEmpty() || height.isEmpty()) {
                    LToast.show(activity, "Cannot set property size: width or height is empty!");
                    return;
                }
            }
            int widthVideo = Integer.parseInt(width);
            int heightVideo = Integer.parseInt(height);
            boolean isSuccess = UZUtil.setMiniPlayerSizePixel(activity, isAutoSize, widthVideo, heightVideo);
            LToast.show(activity, "saveConfigSize isSuccess: " + isSuccess);
        } catch (Exception e) {
            LToast.show(activity, "Invalid value: " + e.toString());
        }
    }
}

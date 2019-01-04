package testlibuiza.sample.v3.fb;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import testlibuiza.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.views.LToast;

public class MiniPlayerSettingActivity extends BaseActivity implements View.OnClickListener {
    private Button btColor0;
    private Button btColor1;
    private Button btColor2;
    private Button btSaveFirstPosition;
    private Switch swEzDestroy;
    private Switch swVibrateDestroy;
    private Switch swSmoothSwitch;
    private EditText etPositionX;
    private EditText etPositionY;
    private EditText etMarginLeft;
    private EditText etMarginTop;
    private EditText etMarginRight;
    private EditText etMarginBottom;
    private Button btSaveMargin;
    private Button btShowController;
    private Button btHideController;
    private Button btToggleController;
    private Button btPlay;
    private Button btPause;
    private Button btPlayPause;
    private Button btFullScreen;

    private void findViews() {
        btColor0 = (Button) findViewById(R.id.bt_color_0);
        btColor1 = (Button) findViewById(R.id.bt_color_1);
        btColor2 = (Button) findViewById(R.id.bt_color_2);
        swEzDestroy = (Switch) findViewById(R.id.sw_ez_destroy);
        swVibrateDestroy = (Switch) findViewById(R.id.sw_vibrate_destroy);
        swSmoothSwitch = (Switch) findViewById(R.id.sw_smooth_switch);
        etPositionX = (EditText) findViewById(R.id.et_position_x);
        etPositionY = (EditText) findViewById(R.id.et_position_y);
        etMarginLeft = (EditText) findViewById(R.id.et_margin_left);
        etMarginTop = (EditText) findViewById(R.id.et_margin_top);
        etMarginRight = (EditText) findViewById(R.id.et_margin_right);
        etMarginBottom = (EditText) findViewById(R.id.et_margin_bottom);
        btSaveFirstPosition = (Button) findViewById(R.id.bt_save_first_position);
        btSaveMargin = (Button) findViewById(R.id.bt_save_margin);
        btShowController = (Button) findViewById(R.id.bt_show_controller);
        btHideController = (Button) findViewById(R.id.bt_hide_controller);
        btToggleController = (Button) findViewById(R.id.bt_toggle_controller);
        btPlay = (Button) findViewById(R.id.bt_play);
        btPause = (Button) findViewById(R.id.bt_pause);
        btPlayPause = (Button) findViewById(R.id.bt_play_pause);
        btFullScreen = (Button) findViewById(R.id.bt_full_screen);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        int currentViewDestroyColor = UZUtil.getMiniPlayerColorViewDestroy(activity);
        if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.black_65)) {
            btColor0.setText("Default");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.RedTrans)) {
            btColor1.setText("RedTrans");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.GreenTran)) {
            btColor2.setText("GreenTran");
        }
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
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_setting_mini_player;
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
                saveConfigMargin();
                break;
            case R.id.bt_show_controller:
                setBtShowController();
                break;
            case R.id.bt_hide_controller:
                setBtHideController();
                break;
            case R.id.bt_toggle_controller:
                setBtToggleController();
                break;
            case R.id.bt_play:
                setBtPlay();
                break;
            case R.id.bt_pause:
                setBtPause();
                break;
            case R.id.bt_play_pause:
                setBtPlayPause();
                break;
            case R.id.bt_full_screen:
                break;
        }
    }

    private void setBtShowController() {
        UZUtil.showMiniPlayerController(activity);
    }

    private void setBtHideController() {
        UZUtil.hideMiniPlayerController(activity);
    }

    private void setBtToggleController() {
        UZUtil.toggleMiniPlayerController(activity);
    }

    private void setBtPlay() {

    }

    private void setBtPause() {
        UZUtil.pauseVideo(activity);
    }

    private void setBtPlayPause() {

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

    private void saveConfigMargin() {
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
        UZUtil.setMiniPlayerMargin(activity, marginL, marginT, marginR, marginB);
        LToast.show(activity, "Set margin " + marginL + " - " + marginT + " - " + marginR + " - " + marginB);
    }
}

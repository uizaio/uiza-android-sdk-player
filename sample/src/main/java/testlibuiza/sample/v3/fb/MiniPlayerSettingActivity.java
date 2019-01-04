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

    private void findViews() {
        btColor0 = (Button) findViewById(R.id.bt_color_0);
        btColor1 = (Button) findViewById(R.id.bt_color_1);
        btColor2 = (Button) findViewById(R.id.bt_color_2);
        swEzDestroy = (Switch) findViewById(R.id.sw_ez_destroy);
        swVibrateDestroy = (Switch) findViewById(R.id.sw_vibrate_destroy);
        swSmoothSwitch = (Switch) findViewById(R.id.sw_smooth_switch);
        etPositionX = (EditText) findViewById(R.id.et_position_x);
        etPositionY = (EditText) findViewById(R.id.et_position_y);
        btSaveFirstPosition = (Button) findViewById(R.id.bt_save_first_position);
        btColor0.setOnClickListener(this);
        btColor1.setOnClickListener(this);
        btColor2.setOnClickListener(this);
        btSaveFirstPosition.setOnClickListener(this);
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
        }
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
}

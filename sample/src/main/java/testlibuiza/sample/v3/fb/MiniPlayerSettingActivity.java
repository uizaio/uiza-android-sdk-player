package testlibuiza.sample.v3.fb;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import testlibuiza.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.base.BaseActivity;

public class MiniPlayerSettingActivity extends BaseActivity implements View.OnClickListener {
    private Button btColor0;
    private Button btColor1;
    private Button btColor2;
    private Switch swEzDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btColor0 = (Button) findViewById(R.id.bt_color_0);
        btColor1 = (Button) findViewById(R.id.bt_color_1);
        btColor2 = (Button) findViewById(R.id.bt_color_2);
        swEzDestroy = (Switch) findViewById(R.id.sw_ez_destroy);
        int currentViewDestroyColor = UZUtil.getMiniPlayerColorViewDestroy(activity);
        if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.black_65)) {
            btColor0.setText("Default");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.RedTrans)) {
            btColor1.setText("RedTrans");
        } else if (currentViewDestroyColor == ContextCompat.getColor(activity, R.color.GreenTran)) {
            btColor2.setText("GreenTran");
        }
        btColor0.setOnClickListener(this);
        btColor1.setOnClickListener(this);
        btColor2.setOnClickListener(this);
        boolean isEZDestroy = UZUtil.getMiniPlayerEzDestroy(activity);
        swEzDestroy.setChecked(isEZDestroy);
        setSwEzDestroy(isEZDestroy);
        swEzDestroy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSwEzDestroy(isChecked);
            }
        });
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
}

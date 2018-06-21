package testlibuiza.sample.v3.api;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LUIUtil;

public class V3TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);

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
        return R.layout.v3_activity_test_api;
    }

    @Override
    public void onClick(View v) {
        tv.setText("Loading...");
        switch (v.getId()) {
            case R.id.bt_get_token:
                break;

        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }
}

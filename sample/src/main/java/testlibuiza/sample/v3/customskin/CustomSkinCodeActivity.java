package testlibuiza.sample.v3.customskin;

import android.os.Bundle;
import android.support.annotation.Nullable;

import testlibuiza.R;
import vn.uiza.core.base.BaseActivity;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomSkinCodeActivity extends BaseActivity {

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
        return R.layout.activity_uiza_custom_skin_code;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

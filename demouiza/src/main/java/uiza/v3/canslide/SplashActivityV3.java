package uiza.v3.canslide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import uiza.R;
import uiza.option.OptionActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class SplashActivityV3 extends BaseActivity {
    private String currentPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.HORIZION_RIGHT);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        //init skin
        currentPlayerId = getIntent().getStringExtra(OptionActivity.KEY_SKIN);
        UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);

        //TODO init tracking domain (with correct domain)
        UizaDataV3.getInstance().initTracking(Constants.URL_TRACKING_STAG);

        //TODO init domain call api
        final String domainApi = "android-api.uiza.co";
        final String token = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
        final String appId = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
        UizaDataV3.getInstance().initSDK(domainApi, token, appId);

        LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                goToHome();
            }
        });
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
        return R.layout.v3_uiza_splash_activity;
    }

    private Intent intent = null;

    private void goToHome() {
        intent = new Intent(activity, HomeV3CanSlideActivity.class);
        if (intent != null) {
            LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    LPref.setClickedPip(activity, false);
                    startActivity(intent);
                    LActivityUtil.tranIn(activity);
                    finish();
                }
            });
        }
    }
}

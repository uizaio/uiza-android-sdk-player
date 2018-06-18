package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.api.TestAPIActivity;
import testlibuiza.sample.uizavideo.rl.TestUizaVideoIMActivityRl;
import testlibuiza.sample.uizavideo.slide.TestUizaVideoIMActivityRlSlide;
import testlibuiza.sample.uizavideo.slide2.TestUizaVideoIMActivityRlSlide2;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TestAPIActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_uiza_video_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPref.setAcitivityCanSlideIsRunning(activity, false);
                uizaVideoRl();
            }
        });
        findViewById(R.id.bt_uiza_video_rl_slide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPref.setAcitivityCanSlideIsRunning(activity, true);
                uizaVideoRlSlide();
            }
        });
        findViewById(R.id.bt_uiza_video_rl_slide_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPref.setAcitivityCanSlideIsRunning(activity, true);
                uizaVideoRlSlide2();
            }
        });

        auth();
    }

    private void auth() {
        UizaService service = RestClientV2.createService(UizaService.class);
        String accessKeyId = Constants.A_K_DEV;
        String secretKeyId = Constants.S_K_DEV;

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                LPref.setAuth(activity, auth, LSApplication.getInstance().getGson());
                RestClientV2.addAuthorization(auth.getData().getToken());
                LToast.show(activity, "auth() onSuccess");
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
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
        return R.layout.activity_main;
    }

    private void uizaVideoRl() {
        LPref.setSlideUizaVideoEnabled(activity, false);
        Intent intent = new Intent(activity, TestUizaVideoIMActivityRl.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void uizaVideoRlSlide() {
        LPref.setSlideUizaVideoEnabled(activity, true);
        Intent intent = new Intent(activity, TestUizaVideoIMActivityRlSlide.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void uizaVideoRlSlide2() {
        LPref.setSlideUizaVideoEnabled(activity, true);
        Intent intent = new Intent(activity, TestUizaVideoIMActivityRlSlide2.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }
}

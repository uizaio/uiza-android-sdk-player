package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.guidecallapi.TestAPI;
import testlibuiza.sample.livestream.LivestreamBroadcasterActivity;
import testlibuiza.sample.v2.api.V2TestAPIActivity;
import testlibuiza.sample.v2.uizavideo.rl.V2UizaVideoIMActivity;
import testlibuiza.sample.v2.uizavideo.slide.V2UizaVideoIMActivitySlide;
import testlibuiza.sample.v3.api.V3TestAPIActivity;
import testlibuiza.sample.v3.customskin.CustomSkinActivity;
import testlibuiza.sample.v3.uizavideov3.V3SetEntityIdActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uzv3.util.UZUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth v2
        authV2();

        findViewById(R.id.bt_test_api_v2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, V2TestAPIActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_uiza_video_cannot_slide_v2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UZUtil.setAcitivityCanSlideIsRunning(activity, false);
                callUizaVideoCannotSlideV2();
            }
        });
        findViewById(R.id.bt_uiza_video_slide_v2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UZUtil.setAcitivityCanSlideIsRunning(activity, true);
                callUizaVideoSlideV2();
            }
        });
        findViewById(R.id.bt_test_api_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, V3TestAPIActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_sdk_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, V3SetEntityIdActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TestAPI.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_livestream_broadcaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LivestreamBroadcasterActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });

    }

    private void authV2() {
        LLog.d(TAG, "authV2");
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        /*String accessKeyId = Constants.A_K_DEV;
        String secretKeyId = Constants.S_K_DEV;*/
        String accessKeyId = Constants.A_K_UQC;
        String secretKeyId = Constants.S_K_UQC;

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                LLog.d(TAG, "authV2 onSuccess");
                UZUtil.setAuth(activity, auth, LSApplication.getInstance().getGson());
                RestClientV2.addAuthorization(auth.getData().getToken());
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

    private void callUizaVideoCannotSlideV2() {
        UZUtil.setSlideUizaVideoEnabled(activity, false);
        Intent intent = new Intent(activity, V2UizaVideoIMActivity.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void callUizaVideoSlideV2() {
        UZUtil.setSlideUizaVideoEnabled(activity, true);
        Intent intent = new Intent(activity, V2UizaVideoIMActivitySlide.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }
}

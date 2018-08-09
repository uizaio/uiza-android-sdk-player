package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.v2.api.V2TestAPIActivity;
import testlibuiza.sample.v2.uizavideo.rl.V2UizaVideoIMActivity;
import testlibuiza.sample.v2.uizavideo.slide.V2UizaVideoIMActivitySlide;
import testlibuiza.sample.v2.uizavideo.slide2.V2UizaVideoIMActivitySlide2;
import testlibuiza.sample.v3.api.V3TestAPIActivity;
import testlibuiza.sample.v3.uizavideov3.V3SetEntityIdActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.UizaPref;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.util.UizaV3Util;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth v2
        authV2();

        //auth v3
        String domainApi = "android-api.uiza.co";
        String token = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
        String appId = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
        //UizaDataV3.getInstance().setCurrentPlayerId(Constants.PLAYER_ID_SKIN_1);
        UizaDataV3.getInstance().initSDK(domainApi, token, appId, Constants.ENVIRONMENT_STAG);

        authV3();

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
                UizaPref.setAcitivityCanSlideIsRunning(activity, false);
                callUizaVideoCannotSlideV2();
            }
        });
        findViewById(R.id.bt_uiza_video_slide_v2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UizaPref.setAcitivityCanSlideIsRunning(activity, true);
                callUizaVideoSlideV2();
            }
        });
        findViewById(R.id.bt_uiza_video_slide_v2_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UizaPref.setAcitivityCanSlideIsRunning(activity, true);
                callUizaVideoSlideV2_2();
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
    }

    private void authV2() {
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
                UizaPref.setAuth(activity, auth, LSApplication.getInstance().getGson());
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
        UizaPref.setSlideUizaVideoEnabled(activity, false);
        Intent intent = new Intent(activity, V2UizaVideoIMActivity.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void callUizaVideoSlideV2() {
        UizaPref.setSlideUizaVideoEnabled(activity, true);
        Intent intent = new Intent(activity, V2UizaVideoIMActivitySlide.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void callUizaVideoSlideV2_2() {
        UizaPref.setSlideUizaVideoEnabled(activity, true);
        Intent intent = new Intent(activity, V2UizaVideoIMActivitySlide2.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    //for uiza api v3
    private void authV3() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        UizaWorkspaceInfo uizaWorkspaceInfo = UizaV3Util.getUizaWorkspace(activity);
        if (uizaWorkspaceInfo == null) {
            return;
        }
        subscribe(service.getToken(uizaWorkspaceInfo), new ApiSubscriber<ResultGetToken>() {
            @Override
            public void onSuccess(ResultGetToken resultGetToken) {
                String token = resultGetToken.getData().getToken();
                RestClientV3.addAuthorization(token);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
            }
        });
    }
    //end for uiza api v3
}

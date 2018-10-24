package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.guidecallapi.TestAPI;
import testlibuiza.sample.livestream.LivestreamBroadcasterActivity;
import testlibuiza.sample.v3.api.UZTestAPIActivity;
import testlibuiza.sample.v3.customhq.CustomHQActivity;
import testlibuiza.sample.v3.customskin.CustomSkinCodeActivity;
import testlibuiza.sample.v3.customskin.CustomSkinXMLActivity;
import testlibuiza.sample.v3.linkplay.PlayerActivity;
import testlibuiza.sample.v3.uzv3.SetEntityIdActivity;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.restclient.RestClientV2;
import vn.uiza.restapi.uiza.UZServiceV1;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.uiza.rxandroid.ApiSubscriber;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //auth v2
        authV2();

        findViewById(R.id.bt_test_api_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UZTestAPIActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_sdk_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SetEntityIdActivity.class);
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
        findViewById(R.id.bt_uiza_custom_skin_xml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinXMLActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinCodeActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_custom_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomHQActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_play_any_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlayerActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
    }

    private void authV2() {
        LLog.d(TAG, "authV2");
        UZServiceV1 service = RestClientV2.createService(UZServiceV1.class);
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
}

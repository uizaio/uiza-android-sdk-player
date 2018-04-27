package testlibuiza.uiza.com.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import testlibuiza.uiza.com.dummy.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.dummy.APIServices;
import vn.loitp.restapi.restclient.RestClient;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.uizavideo.view.floatview.FloatingUizaVideoService;

public class TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
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
        return R.layout.activity_test_api;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_token:
                auth();
                break;
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void auth() {
        LLog.d(TAG, ">>>>>>>>>auth");
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        UizaService service = RestClientV2.createService(UizaService.class);
        String accessKeyId = "Y0ZW0XM7HZL2CB8ODNDV";
        String secretKeyId = "qtQWc9Ut1SAfWK2viFJHBgViYCZYthSTjEJMlR9S";

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                showTv(auth);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

}

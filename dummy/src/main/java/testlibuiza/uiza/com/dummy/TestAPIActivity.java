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
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.uizavideo.view.floatview.FloatingUizaVideoService;

public class TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
        findViewById(R.id.bt_check_token).setOnClickListener(this);
        findViewById(R.id.bt_list_metadata).setOnClickListener(this);
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
            case R.id.bt_check_token:
                checkToken();
                break;
            case R.id.bt_list_metadata:
                listMetadata();
                break;
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void auth() {
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
                RestClientV2.addAuthorization(auth.getData().getToken());
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void checkToken() {
        UizaService service = RestClientV2.createService(UizaService.class);
        subscribe(service.checkToken(), new ApiSubscriber<Auth>() {
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

    private void listMetadata() {
        UizaService service = RestClientV2.createService(UizaService.class);
        int limit = 999;
        String orderBy = "name";
        String orderType = "ASC";
        JsonBodyMetadataList jsonBodyMetadataList = new JsonBodyMetadataList();
        jsonBodyMetadataList.setLimit(limit);
        jsonBodyMetadataList.setOrderBy(orderBy);
        jsonBodyMetadataList.setOrderType(orderType);
        subscribe(service.listAllMetadataV2(jsonBodyMetadataList), new ApiSubscriber<ListAllMetadata>() {
            @Override
            public void onSuccess(ListAllMetadata listAllMetadata) {
                showTv(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }
}

package testlibuiza.sample.v3.api;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.getlistmetadata.ResultGetListMetadata;
import vn.loitp.restapi.uiza.model.v3.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.util.UizaV3Util;
import vn.loitp.rxandroid.ApiSubscriber;

public class V3TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
        findViewById(R.id.bt_check_token).setOnClickListener(this);
        findViewById(R.id.bt_get_list_metadata).setOnClickListener(this);
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
                getToken();
                break;
            case R.id.bt_check_token:
                checkToken();
                break;
            case R.id.bt_get_list_metadata:
                getListMetadata();
                break;
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void getToken() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        UizaWorkspaceInfo uizaWorkspaceInfo = UizaV3Util.getUizaWorkspace(activity);
        if (uizaWorkspaceInfo == null) {
            return;
        }
        subscribe(service.getToken(uizaWorkspaceInfo), new ApiSubscriber<ResultGetToken>() {
            @Override
            public void onSuccess(ResultGetToken resultGetToken) {
                LLog.d(TAG, "getToken " + LSApplication.getInstance().getGson().toJson(resultGetToken));
                String token = resultGetToken.getData().getToken();
                LLog.d(TAG, "token: " + token);
                RestClientV3.addAuthorization(token);
                showTv(resultGetToken);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void checkToken() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.checkToken(), new ApiSubscriber<ResultGetToken>() {
            @Override
            public void onSuccess(ResultGetToken resultGetToken) {
                LLog.d(TAG, "checkToken onSuccess: " + LSApplication.getInstance().getGson().toJson(resultGetToken));
                showTv(resultGetToken);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getListMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
            @Override
            public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
                LLog.d(TAG, "getListMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(resultGetListMetadata));
                showTv(resultGetListMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }
}

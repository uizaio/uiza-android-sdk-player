package testlibuiza.sample.v3.uizavideov3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.view.util.UizaDataV3;
import vn.loitp.views.LToast;

public class V3SetEntityIdActivity extends BaseActivity {
    private ProgressDialog progressDialog;
    private EditText etInputEntityId;
    private Button btStart;

    //change your infor
    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    private final String domainTracking = Constants.URL_TRACKING_STAG;
    private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final String entityIdDefaultVOD = "61d4b1e2-95d9-418d-9e52-e1d798e869b4";
    private final String entityIdDefaultLIVE = "9f206879-f594-4775-aded-a173c1bca163";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new LDialogUtil().getSpinnerProgressDialog(activity, "Please wait", "Loading...");
        progressDialog.setCancelable(false);
        //progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        //find views
        etInputEntityId = (EditText) findViewById(R.id.et_input_entity_id);
        btStart = (Button) findViewById(R.id.bt_start);

        //set default value entity id
        etInputEntityId.setText(entityIdDefaultVOD);
        LUIUtil.setLastCursorEditText(etInputEntityId);

        etInputEntityId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    btStart.setEnabled(false);
                } else {
                    btStart.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEntityIdVOD(etInputEntityId.getText().toString());
            }
        });

        findViewById(R.id.bt_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(entityIdDefaultVOD);
                LUIUtil.setLastCursorEditText(etInputEntityId);
            }
        });
        findViewById(R.id.bt_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(entityIdDefaultLIVE);
                LUIUtil.setLastCursorEditText(etInputEntityId);
            }
        });
        findViewById(R.id.bt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText("");
            }
        });
    }

    private void goToPlay(Data data) {
        LDialogUtil.hide(progressDialog);
        LPref.setData(activity, data, LSApplication.getInstance().getGson());

        UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
        UizaDataV3.getInstance().initTracking(domainTracking);
        UizaDataV3.getInstance().initSDK(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);

        final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
    }

    private void getDataFromEntityIdVOD(final String entityId) {
        LDialogUtil.show(progressDialog);
        //TRY TO GET DATA VOD
        //IF NOT SUCCESS, TRY TO GET DATA LIVE
        LLog.d(TAG, "getDataFromEntityIdVOD entityId " + entityId);
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.retrieveAnEntity(entityId), new ApiSubscriber<ResultRetrieveAnEntity>() {
            @Override
            public void onSuccess(ResultRetrieveAnEntity result) {
                LLog.d(TAG, "getDataFromEntityIdVOD onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    getDataFromEntityIdLIVE(entityId);
                } else {
                    Data d = result.getData();
                    goToPlay(d);
                }
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDataFromEntityIdVOD onFail " + e.getMessage());
                LDialogUtil.hide(progressDialog);
                LToast.show(activity, "getDataFromEntityIdVOD onFail " + e.getMessage());
            }
        });
    }

    private void getDataFromEntityIdLIVE(String entityId) {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.retrieveALiveEvent(entityId), new ApiSubscriber<ResultRetrieveALive>() {
            @Override
            public void onSuccess(ResultRetrieveALive result) {
                LLog.d(TAG, "getDataFromEntityIdLIVE onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    LDialogUtil.hide(progressDialog);
                    LToast.show(activity, "getDataFromEntityIdLIVE result null");
                } else {
                    Data d = result.getData();
                    goToPlay(d);
                }
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDataFromEntityIdLIVE onFail " + e.getMessage());
                LDialogUtil.hide(progressDialog);
                LToast.show(activity, "getDataFromEntityIdLIVE onFail " + e.getMessage());
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
        return R.layout.v3_player_input_entity_id_activity;
    }

    @Override
    protected void onDestroy() {
        LDialogUtil.clearAll();
        super.onDestroy();
    }
}

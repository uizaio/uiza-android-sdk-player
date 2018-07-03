package uiza.v3.canslide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import uiza.R;
import uiza.app.LSApplication;
import uiza.option.OptionActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.util.UizaV3Util;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.util.UizaData;

public class SplashActivityV3 extends BaseActivity {
    private String currentPlayerId;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.HORIZION_RIGHT);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        currentPlayerId = getIntent().getStringExtra(OptionActivity.KEY_SKIN);
        LPref.setSlideUizaVideoEnabled(activity, true);

        //TODO init tracking dev (with correct domain)
        RestClientTracking.init(Constants.URL_TRACKING_DEV);

        UizaWorkspaceInfo uizaWorkspaceInfo = new UizaWorkspaceInfo("loitp@uiza.io", "04021993", "android-api.uiza.co", "android.uiza.co");
        UizaV3Util.initUizaWorkspace(activity, uizaWorkspaceInfo);
        authV3();

        LPref.setApiTrackEndPoint(activity, Constants.URL_TRACKING_STAG);
        //TODO iplm api check token
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
        LLog.d(TAG, "goToHome token: " + token);
        if (token == null) {
            LDialogUtil.showDialog1(activity, "Token cannot be null or empty", new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }

                @Override
                public void onCancel() {
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }
            });
            return;
        }
        UizaData.getInstance().setCurrentPlayerId(currentPlayerId);
        LPref.setToken(activity, token);
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

    private void authV3() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        UizaWorkspaceInfo uizaWorkspaceInfo = UizaV3Util.getUizaWorkspace(activity);
        if (uizaWorkspaceInfo == null) {
            return;
        }
        subscribe(service.getToken(uizaWorkspaceInfo), new ApiSubscriber<ResultGetToken>() {
            @Override
            public void onSuccess(ResultGetToken resultGetToken) {
                LLog.d(TAG, "authV3 " + LSApplication.getInstance().getGson().toJson(resultGetToken));
                UizaV3Util.setResultGetToken(activity, resultGetToken);
                token = resultGetToken.getData().getToken();
                LLog.d(TAG, "token: " + token);
                RestClientV3.addAuthorization(token);
                goToHome();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
            }
        });
    }
}

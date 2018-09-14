package uiza.v2.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import android.widget.Toast;

import uiza.R;
import uiza.app.LSApplication;
import uiza.option.OptionActivity;
import uiza.v2.home.cannotslide.HomeV2CannotSlideActivity;
import uiza.v2.home.canslide.HomeV2CanSlideActivity;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LDateUtils;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.restclient.RestClientTracking;
import vn.uiza.restapi.restclient.RestClientV2;
import vn.uiza.restapi.uiza.UizaServiceV2;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.uzv1.view.util.UizaDataV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

public class SplashActivity extends BaseActivity {
    private int currentPlayerId;
    private boolean canSlide;
    private String currentApiEndPoint;
    private String currentApiTrackingEndPoint;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        currentPlayerId = getIntent().getIntExtra(OptionActivity.KEY_SKIN, R.layout.player_skin_1);
        canSlide = getIntent().getBooleanExtra(OptionActivity.KEY_CAN_SLIDE, false);
        UZUtil.setSlideUizaVideoEnabled(activity, canSlide);
        currentApiEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_END_POINT);
        currentApiTrackingEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_TRACKING_END_POINT);

        LLog.d(TAG, "getIntent currentPlayerId " + currentPlayerId);
        LLog.d(TAG, "getIntent canSlide " + canSlide);
        LLog.d(TAG, "getIntent currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "getIntent currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        RestClientV2.init(currentApiEndPoint);
        RestClientTracking.init(currentApiTrackingEndPoint);

        UZUtil.setApiEndPoint(activity, currentApiEndPoint);
        UZUtil.setApiTrackEndPoint(activity, currentApiTrackingEndPoint);

        Auth auth = UZUtil.getAuth(activity, LSApplication.getInstance().getGson());
        LLog.d(TAG, "auth: " + LSApplication.getInstance().getGson().toJson(auth));
        if (auth == null) {
            auth();
        } else {
            token = auth.getData().getToken();
            RestClientV2.addAuthorization(token);
            checkToken(token);
        }
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
        return R.layout.uiza_splash_activity;
    }

    private void auth() {
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        String accessKeyId = null;
        String secretKeyId = null;
        switch (currentApiEndPoint) {
            case Constants.URL_DEV_UIZA_VERSION_2:
                accessKeyId = Constants.A_K_DEV;
                secretKeyId = Constants.S_K_DEV;
                break;
            case Constants.URL_DEV_UIZA_VERSION_2_STAG:
                accessKeyId = Constants.A_K_UQC;
                secretKeyId = Constants.S_K_UQC;
                break;
            case Constants.URL_DEV_UIZA_VERSION_2_DEMO:
                accessKeyId = Constants.A_K_DEMO;
                secretKeyId = Constants.S_K_DEMO;
                break;
        }

        if (accessKeyId == null || secretKeyId == null || accessKeyId.isEmpty() || secretKeyId.isEmpty()) {
            LDialogUtil.showDialog1(activity, getString(R.string.key_not_found), new LDialogUtil.Callback1() {
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

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                UZUtil.setAuth(activity, auth, LSApplication.getInstance().getGson());

                token = auth.getData().getToken();
                goToHome();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                LDialogUtil.showDialog1(activity, "Error: Auth failed", new LDialogUtil.Callback1() {
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
            }
        });
    }

    private Intent intent = null;

    private void goToHome() {
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
        UizaDataV1.getInstance().setCurrentPlayerId(currentPlayerId);
        RestClientV2.addAuthorization(token);
        UZUtil.setToken(activity, token);
        if (canSlide) {
            intent = new Intent(activity, HomeV2CanSlideActivity.class);
        } else {
            intent = new Intent(activity, HomeV2CannotSlideActivity.class);
        }
        if (intent != null) {
            LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    UZUtil.setClickedPip(activity, false);
                    startActivity(intent);
                    LActivityUtil.tranIn(activity);
                    finish();
                }
            });
        }
    }

    private void checkToken(String token) {
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        subscribe(service.checkToken(), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth a) {
                LLog.d(TAG, "checkToken: " + LSApplication.getInstance().getGson().toJson(a));
                LLog.d(TAG, "getExpired " + a.getData().getExpired());
                LLog.d(TAG, "try with FORMAT_3");
                long expiredTime = LDateUtils.convertDateToTimeStamp(a.getData().getExpired(), LDateUtils.FORMAT_3);
                if (expiredTime == Constants.NOT_FOUND) {
                    LLog.d(TAG, "try with FORMAT_1");
                    expiredTime = LDateUtils.convertDateToTimeStamp(a.getData().getExpired(), LDateUtils.FORMAT_1);
                }
                long currentTime = System.currentTimeMillis();
                LLog.d(TAG, "expiredTime " + expiredTime);
                LLog.d(TAG, "currentTime " + currentTime);
                if (currentTime > expiredTime) {
                    LDialogUtil.showDialog1(activity, "Token is expried", new LDialogUtil.Callback1() {
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
                } else {
                    LToast.show(activity, "Token sẽ hết hạn vào " + LDateUtils.convertTimestampToDate(expiredTime), Toast.LENGTH_LONG);
                    goToHome();
                }
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                LDialogUtil.showDialog1(activity, "Error: Cannot check token", new LDialogUtil.Callback1() {
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
            }
        });
    }
}

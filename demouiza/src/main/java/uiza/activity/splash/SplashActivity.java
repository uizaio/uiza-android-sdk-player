package uiza.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import android.widget.Toast;

import uiza.R;
import uiza.activity.home.v2.cannotslide.HomeV2CannotSlideActivity;
import uiza.activity.home.v2.canslide.HomeV2CanSlideActivity;
import uiza.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.views.LToast;
import vn.loitp.views.smoothtransition.SwitchAnimationUtil;

public class SplashActivity extends BaseActivity {
    private String currentPlayerId;
    private boolean canSlide;
    private String currentApiEndPoint;
    private String currentApiTrackingEndPoint;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.HORIZION_RIGHT);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        currentPlayerId = getIntent().getStringExtra(OptionActivity.KEY_SKIN);
        canSlide = getIntent().getBooleanExtra(OptionActivity.KEY_CAN_SLIDE, false);
        LPref.setSlideUizaVideoEnabled(activity, canSlide);
        currentApiEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_END_POINT);
        currentApiTrackingEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_TRACKING_END_POINT);

        LLog.d(TAG, "getIntent currentPlayerId " + currentPlayerId);
        LLog.d(TAG, "getIntent canSlide " + canSlide);
        LLog.d(TAG, "getIntent currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "getIntent currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        RestClientV2.init(currentApiEndPoint);
        RestClientTracking.init(currentApiTrackingEndPoint);

        LPref.setApiEndPoint(activity, currentApiEndPoint);
        LPref.setApiTrackEndPoint(activity, currentApiTrackingEndPoint);

        Auth auth = LPref.getAuth(activity, LSApplication.getInstance().getGson());
        LLog.d(TAG, "auth: " + LSApplication.getInstance().getGson().toJson(auth));
        if (auth == null) {
            LLog.d(TAG, "auth == null -> get token");
            auth();
        } else {
            LLog.d(TAG, "auth != null -> check token");
            token = auth.getData().getToken();
            RestClientV2.addAuthorization(token);
            checkToken(token);
        }
    }

    private Auth getDummyAuth() {
        LLog.d(TAG, "getDummyAuth");
        String json = "{\n" +
                "    \"data\": {\n" +
                "        \"token\": \"30e23580-f326-4db4-9f3e-a01d609b32b3\",\n" +
                "        \"expired\": \"22/04/2018 03:32:46\",\n" +
                "        \"appId\": \"a204e9cdeca44948a33e0d012ef74e90\"\n" +
                "    },\n" +
                "    \"version\": 2,\n" +
                "    \"datetime\": \"2018-03-23T03:32:46.242Z\",\n" +
                "    \"name\": \"Resource\",\n" +
                "    \"message\": \"ok\",\n" +
                "    \"code\": 200,\n" +
                "    \"type\": \"SUCCESS\"\n" +
                "}";
        Auth auth = LSApplication.getInstance().getGson().fromJson(json, Auth.class);
        return auth;
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
        UizaService service = RestClientV2.createService(UizaService.class);
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
                LLog.d(TAG, "getData auth onSuccess " + LSApplication.getInstance().getGson().toJson(auth));
                LPref.setAuth(activity, auth, LSApplication.getInstance().getGson());

                token = auth.getData().getToken();
                LLog.d(TAG, ">>>>token " + token);
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
        RestClientV2.addAuthorization(token);
        LPref.setToken(activity, token);
        if (canSlide) {
            LLog.d(TAG, "goToHome HomeV2CanSlideActivity");
            intent = new Intent(activity, HomeV2CanSlideActivity.class);
        } else {
            LLog.d(TAG, "goToHome HomeV2CannotSlideActivity");
            intent = new Intent(activity, HomeV2CannotSlideActivity.class);
        }
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

    private void checkToken(String token) {
        LLog.d(TAG, "checkToken: " + token);
        UizaService service = RestClientV2.createService(UizaService.class);
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

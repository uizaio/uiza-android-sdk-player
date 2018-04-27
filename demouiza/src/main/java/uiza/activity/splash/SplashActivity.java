package uiza.activity.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import uiza.activity.home.v2.cannotslide.HomeV2CannotSlideActivity;
import uiza.app.LSApplication;
import uiza.uiza.com.demo.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;

public class SplashActivity extends BaseActivity {
    private String currentPlayerId;
    private boolean canSlide;
    private String currentApiEndPoint;
    private String currentApiTrackingEndPoint;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPlayerId = getIntent().getStringExtra(OptionActivity.KEY_SKIN);
        canSlide = getIntent().getBooleanExtra(OptionActivity.KEY_CAN_SLIDE, false);
        currentApiEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_END_POINT);
        currentApiTrackingEndPoint = getIntent().getStringExtra(OptionActivity.KEY_API_TRACKING_END_POINT);

        LLog.d(TAG, "currentPlayerId " + currentPlayerId);
        LLog.d(TAG, "canSlide " + canSlide);
        LLog.d(TAG, "currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        switch (currentApiEndPoint) {
            case Constants.URL_DEV_UIZA_VERSION_2:
                LLog.d(TAG, "Constants.TOKEN_DEV_V1; -> gettoken");
                Auth auth = LPref.getAuth(activity, LSApplication.getInstance().getGson());
                LLog.d(TAG, "auth: " + LSApplication.getInstance().getGson().toJson(auth));
                if (auth == null) {
                    LLog.d(TAG, "auth == null -> get token");
                    auth();
                } else {
                    LLog.d(TAG, "auth != null -> check token");
                    token = auth.getData().getToken();
                    checkToken(token);
                }
                break;
            case Constants.URL_DEV_UIZA_VERSION_2_STAG:
                LLog.d(TAG, "Constants.TOKEN_STAG; -> token hardcode");
                token = Constants.TOKEN_STAG;

                //TODO dummy auth for api vs1
                LPref.setAuth(activity, getDummyAuth(), LSApplication.getInstance().getGson());

                goToHome();
                break;
            case Constants.URL_WTT:
                LLog.d(TAG, "Constants.TOKEN_WTT; -> token hardcode");
                token = Constants.TOKEN_WTT;

                //TODO dummy auth for api vs1
                LPref.setAuth(activity, getDummyAuth(), LSApplication.getInstance().getGson());

                goToHome();
                break;
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
        LLog.d(TAG, ">>>>>>>>>auth");
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        UizaService service = RestClientV2.createService(UizaService.class);
        String accessKeyId = "xxx";
        String secretKeyId = "xxx";

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                if (auth == null) {
                    showDialogError("onSuccess auth == null");
                    return;
                }
                LLog.d(TAG, "getData onSuccess " + LSApplication.getInstance().getGson().toJson(auth));
                LPref.setAuth(activity, auth, LSApplication.getInstance().getGson());

                token = auth.getData().getToken();
                LLog.d(TAG, ">>>>token " + token);
                goToHome();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private Intent intent = null;

    private void goToHome() {
        LLog.d(TAG, "goToHome token: " + token);

        if (token == null) {
            showDialogOne("token==null", true);
            return;
        }

        RestClientV2.init(currentApiEndPoint, token);
        //UizaData.getInstance().init(currentApiEndPoint, currentApiTrackingEndPoint, token, currentPlayerId);
        //UizaData.getInstance().setVideoCanSlide(canSlide);

        switch (currentApiEndPoint) {
            case Constants.URL_DEV_UIZA_VERSION_2:
                if (canSlide) {
                    LLog.d(TAG, "goToHome HomeV2CanSlideActivity");
                    //intent = new Intent(activity, HomeV2CanSlideActivity.class);
                } else {
                    LLog.d(TAG, "goToHome HomeV2CannotSlideActivity");
                    intent = new Intent(activity, HomeV2CannotSlideActivity.class);
                }
                break;
            case Constants.URL_DEV_UIZA_VERSION_2_STAG:
            case Constants.URL_WTT:
                if (canSlide) {
                    LLog.d(TAG, "goToHome HomeV1CanSlideActivity");
                    //intent = new Intent(activity, HomeV1CanSlideActivity.class);
                } else {
                    LLog.d(TAG, "goToHome HomeV1CannotSlideActivity");
                    //intent = new Intent(activity, HomeV1CannotSlideActivity.class);
                }
                break;
        }
        if (intent != null) {
            LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    startActivity(intent);
                    LActivityUtil.tranIn(activity);
                    finish();
                }
            });
        }
    }

    private void checkToken(String token) {
        LLog.d(TAG, "checkToken: " + token);
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2, token);
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
                    showDialogOne("Token đã hết hạn.", true);
                } else {
                    LToast.show(activity, "Token sẽ hết hạn vào " + LDateUtils.convertTimestampToDate(expiredTime), Toast.LENGTH_LONG);
                    goToHome();
                }
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                handleException(e);
            }
        });
    }
}

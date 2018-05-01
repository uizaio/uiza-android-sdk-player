package uiza.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;

public class OptionActivity extends BaseActivity {
    public static final String KEY_SKIN = "KEY_SKIN";
    public static final String KEY_CAN_SLIDE = "KEY_CAN_SLIDE";
    public static final String KEY_API_END_POINT = "KEY_API_END_POINT";
    public static final String KEY_API_TRACKING_END_POINT = "KEY_API_TRACKING_END_POINT";

    private RadioGroup radioGroupSkin;
    private RadioButton radioSkin1;
    private RadioButton radioSkin2;
    private RadioButton radioSkin3;
    private RadioButton radioSkin0;
    private String currentPlayerId;

    private RadioGroup radioGroupSlide;
    private RadioButton radioCanSlide;
    private RadioButton radioCannotSlide;
    private boolean canSlide;

    private RadioGroup radioEnvironment;
    private RadioButton radioEnvironmentDev;
    private RadioButton radioEnvironmentStag;
    private RadioButton radioEnvironmentProd;
    private int currentEnvironment = Constants.NOT_FOUND;
    String currentApiTrackingEndPoint = null;
    String currentApiEndPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSplashScreen();
            }
        });
        findViews();
        setupSkin();
        setupSlide();
        setupEnvironment();
    }

    private void findViews() {
        //setting theme
        radioGroupSkin = (RadioGroup) findViewById(R.id.radio_group_skin);
        radioSkin1 = (RadioButton) findViewById(R.id.radio_skin_1);
        radioSkin2 = (RadioButton) findViewById(R.id.radio_skin_2);
        radioSkin3 = (RadioButton) findViewById(R.id.radio_skin_3);
        radioSkin0 = (RadioButton) findViewById(R.id.radio_skin_0);
        //setting slide
        radioGroupSlide = (RadioGroup) findViewById(R.id.radio_group_slide);
        radioCanSlide = (RadioButton) findViewById(R.id.radio_can_slide);
        radioCannotSlide = (RadioButton) findViewById(R.id.radio_cannot_slide);
        //setting environment
        radioEnvironment = (RadioGroup) findViewById(R.id.radio_environment);
        radioEnvironmentDev = (RadioButton) findViewById(R.id.radio_environment_dev);
        radioEnvironmentStag = (RadioButton) findViewById(R.id.radio_environment_stag);
        radioEnvironmentProd = (RadioButton) findViewById(R.id.radio_environment_prod);
    }

    private void goToSplashScreen() {
        switch (currentEnvironment) {
            case Constants.ENVIRONMENT_DEV:
                currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_DEV;
                break;
            case Constants.ENVIRONMENT_STAG:
                currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2_STAG;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_STAG;
                break;
            case Constants.ENVIRONMENT_PROD:
                currentApiEndPoint = Constants.URL_WTT;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_PROD;
                break;
        }

        LLog.d(TAG, "currentPlayerId " + currentPlayerId);
        LLog.d(TAG, "canSlide " + canSlide);
        LLog.d(TAG, "currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra(KEY_SKIN, currentPlayerId);
        intent.putExtra(KEY_CAN_SLIDE, canSlide);
        intent.putExtra(KEY_API_END_POINT, currentApiEndPoint);
        intent.putExtra(KEY_API_TRACKING_END_POINT, currentApiTrackingEndPoint);
        startActivity(intent);
        LActivityUtil.tranIn(activity);
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
        return R.layout.uiza_option_activity;
    }

    private void setupSkin() {
        //default skin1
        radioSkin1.setChecked(true);
        currentPlayerId = Constants.PLAYER_ID_SKIN_1;

        radioGroupSkin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioGroupSkin.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_skin_1:
                        currentPlayerId = Constants.PLAYER_ID_SKIN_1;
                        break;
                    case R.id.radio_skin_2:
                        currentPlayerId = Constants.PLAYER_ID_SKIN_2;
                        break;
                    case R.id.radio_skin_3:
                        currentPlayerId = Constants.PLAYER_ID_SKIN_3;
                    case R.id.radio_skin_0:
                        currentPlayerId = Constants.PLAYER_ID_SKIN_0;
                        break;
                }
            }
        });
    }

    private void setupSlide() {
        //default cannot slide
        radioCannotSlide.setChecked(true);
        canSlide = false;

        radioGroupSlide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioGroupSlide.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_can_slide:
                        canSlide = true;
                        break;
                    case R.id.radio_cannot_slide:
                        canSlide = false;
                        break;
                }
            }
        });
    }

    private void setupEnvironment() {
        //default
        radioEnvironmentDev.setChecked(true);
        currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2;
        currentApiTrackingEndPoint = Constants.URL_TRACKING_DEV;

        radioEnvironment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioEnvironment.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_environment_dev:
                        currentEnvironment = Constants.ENVIRONMENT_DEV;
                        break;
                    case R.id.radio_environment_stag:
                        currentEnvironment = Constants.ENVIRONMENT_STAG;
                        break;
                    case R.id.radio_environment_prod:
                        currentEnvironment = Constants.ENVIRONMENT_PROD;
                        break;
                }
            }
        });
    }
}

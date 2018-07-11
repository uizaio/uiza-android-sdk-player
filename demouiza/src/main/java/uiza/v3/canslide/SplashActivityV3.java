package uiza.v3.canslide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import uiza.R;
import uiza.option.OptionActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class SplashActivityV3 extends BaseActivity {
    private String currentPlayerId;
    private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private EditText etApiDomain;
    private EditText etKey;
    private EditText etAppId;
    private Button btStart;
    private ProgressBar progressBar;
    private LinearLayout llInputInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.HORIZION_RIGHT);
        llInputInfo = (LinearLayout) findViewById(R.id.ll_input_info);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.GONE);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        //init skin
        currentPlayerId = getIntent().getStringExtra(OptionActivity.KEY_SKIN);

        etApiDomain = (EditText) findViewById(R.id.et_api_domain);
        etKey = (EditText) findViewById(R.id.et_key);
        etAppId = (EditText) findViewById(R.id.et_app_id);
        etApiDomain.setText(DF_DOMAIN_API);
        etKey.setText(DF_TOKEN);
        etAppId.setText(DF_APP_ID);
        LUIUtil.setLastCursorEditText(etApiDomain);

        etApiDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        etAppId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });

        btStart = (Button) findViewById(R.id.bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btStart.setVisibility(View.GONE);
                llInputInfo.setVisibility(View.GONE);

                String domainApi = etApiDomain.getText().toString().trim();
                String token = etKey.getText().toString().trim();
                String appId = etAppId.getText().toString().trim();

                LLog.d(TAG, "onClick domainApi " + domainApi);
                LLog.d(TAG, "onClick token " + token);
                LLog.d(TAG, "onClick appId " + appId);

                UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
                //TODO init tracking domain (with correct domain)
                UizaDataV3.getInstance().initTracking(Constants.URL_TRACKING_STAG);
                UizaDataV3.getInstance().initSDK(domainApi, token, appId);

                final Intent intent = new Intent(activity, HomeV3CanSlideActivity.class);
                if (intent != null) {
                    LUIUtil.setDelay(3000, new LUIUtil.DelayCallback() {
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
        });
    }

    private void checkToSetVisibilityBtStart() {
        String domainApi = etApiDomain.getText().toString();
        String token = etKey.getText().toString();
        String appId = etAppId.getText().toString();
        if (domainApi == null || domainApi.isEmpty() || token == null || token.isEmpty() || appId == null || appId.isEmpty()) {
            btStart.setClickable(false);
            btStart.setBackgroundResource(R.drawable.bt_login_disabled);
        } else {
            btStart.setClickable(true);
            btStart.setBackgroundResource(R.drawable.bt_login_enable);
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
        return R.layout.v3_uiza_splash_activity;
    }
}

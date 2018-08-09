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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uiza.R;
import uiza.option.OptionActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.uizavideov3.util.UizaDataV3;

public class SplashActivityV3 extends BaseActivity {
    private String currentPlayerId;

    //workspace loitp
    /*private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private int environment = Constants.ENVIRONMENT_STAG;*/

    //workspace Yan
    /*private final String DF_DOMAIN_API = "wworkspace-api.uiza.co";
    private final String DF_TOKEN = "uap-30062fa1ee7f4ce4822c55f00c6dc130-fa14be2b";
    private final String DF_APP_ID = "30062fa1ee7f4ce4822c55f00c6dc130";
    private int environment = Constants.ENVIRONMENT_PROD;*/

    //workspace Loc
    private final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    private final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    private int environment = Constants.ENVIRONMENT_PROD;

    private EditText etApiDomain;
    private EditText etKey;
    private EditText etAppId;
    private Button btStart;
    private ProgressBar progressBar;
    private LinearLayout llInputInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (environment == Constants.ENVIRONMENT_DEV) {
            ((RadioButton) findViewById(R.id.rd_env_dev)).setChecked(true);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            ((RadioButton) findViewById(R.id.rd_env_stag)).setChecked(true);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            ((RadioButton) findViewById(R.id.rd_env_prod)).setChecked(true);
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rd_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_env_dev) {
                    environment = Constants.ENVIRONMENT_DEV;
                } else if (checkedId == R.id.rd_env_stag) {
                    environment = Constants.ENVIRONMENT_STAG;
                } else if (checkedId == R.id.rd_env_prod) {
                    environment = Constants.ENVIRONMENT_PROD;
                }
                LLog.d(TAG, "onCheckedChanged " + environment);
            }
        });

        etApiDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etAppId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToSetVisibilityBtStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
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

                UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
                UizaDataV3.getInstance().initSDK(domainApi, token, appId, environment);

                final Intent intent = new Intent(activity, HomeV3CanSlideActivity.class);
                if (intent != null) {
                    LUIUtil.setDelay(3000, new LUIUtil.DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            UizaUtil.setClickedPip(activity, false);
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

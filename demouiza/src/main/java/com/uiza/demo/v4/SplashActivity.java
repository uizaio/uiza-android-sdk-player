package com.uiza.demo.v4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.uiza.demo.LSApplication;
import com.uiza.demo.R;
import com.uiza.demo.option.OptionActivity;
import io.uiza.broadcast.config.UzLiveConfig;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import io.uiza.player.UzPlayerConfig;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private int currentPlayerId = R.layout.uz_player_skin_1;
    private EditText etApiDomain;
    private EditText etKey;
    private EditText etAppId;
    private Button btStart;
    private ProgressBar progressBar;
    private LinearLayout llInputInfo;
    private int environment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_uiza_splash_activity);
        llInputInfo = findViewById(R.id.ll_input_info);
        progressBar = findViewById(R.id.pb);
        progressBar.setVisibility(View.GONE);
        UzDisplayUtil
                .setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        currentPlayerId = getIntent()
                .getIntExtra(OptionActivity.KEY_SKIN, R.layout.uz_player_skin_1);
        etApiDomain = findViewById(R.id.et_api_domain);
        etKey = findViewById(R.id.et_key);
        etAppId = findViewById(R.id.et_app_id);

        etApiDomain.setText(LSApplication.DF_DOMAIN_API_PROD);
        etKey.setText(LSApplication.DF_TOKEN);
        etAppId.setText(LSApplication.DF_APP_ID);
        environment = LSApplication.ENVIRONMENT;
        UzDisplayUtil.setLastCursorEditText(etApiDomain);

        if (environment == Constants.ENVIRONMENT_DEV) {
            ((RadioButton) findViewById(R.id.rd_env_dev)).setChecked(true);
        } else if (environment == Constants.ENVIRONMENT_STAG) {
            ((RadioButton) findViewById(R.id.rd_env_stag)).setChecked(true);
        } else if (environment == Constants.ENVIRONMENT_PROD) {
            ((RadioButton) findViewById(R.id.rd_env_prod)).setChecked(true);
        }

        RadioGroup radioGroup = findViewById(R.id.rd_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rd_env_dev) {
                environment = Constants.ENVIRONMENT_DEV;
            } else if (checkedId == R.id.rd_env_stag) {
                environment = Constants.ENVIRONMENT_STAG;
                etApiDomain.setText(LSApplication.DF_DOMAIN_API_STAG);
            } else if (checkedId == R.id.rd_env_prod) {
                environment = Constants.ENVIRONMENT_PROD;
                etApiDomain.setText(LSApplication.DF_DOMAIN_API_PROD);
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

        btStart = findViewById(R.id.bt_start);
        btStart.setOnClickListener(v -> {
            String domainApi = etApiDomain.getText().toString().trim();
            if (domainApi.equals("input")) {
                LToast.show(activity, "You must correct your workspace's information first.");
                return;
            }
            if (domainApi.contains("http://")) {
                domainApi = domainApi.replace("http://", "");
            }
            String token = etKey.getText().toString().trim();
            String appId = etAppId.getText().toString().trim();
            boolean isSuccess = UzPlayerConfig
                    .initWorkspace(activity, LSApplication.API_VERSION, domainApi, token, appId,
                            environment, currentPlayerId);
            UzLiveConfig
                    .initWorkspace(activity, LSApplication.API_VERSION, domainApi, token, appId);
            if (!isSuccess) {
                LToast.show(activity, "Your workspace is incorrect.");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            btStart.setVisibility(View.GONE);
            llInputInfo.setVisibility(View.GONE);
            final Intent intent = new Intent(activity, HomeV4CanSlideActivity.class);
            UzCommonUtil.actionWithDelayed(3000, mls -> {
                startActivity(intent);
                finish();
            });
        });
    }

    private void checkToSetVisibilityBtStart() {
        String domainApi = etApiDomain.getText().toString();
        String token = etKey.getText().toString();
        String appId = etAppId.getText().toString();
        if (domainApi.isEmpty() || token.isEmpty() || appId.isEmpty()) {
            btStart.setClickable(false);
            btStart.setBackgroundResource(R.drawable.bt_login_disabled);
        } else {
            btStart.setClickable(true);
            btStart.setBackgroundResource(R.drawable.bt_login_enable);
        }
    }
}

package testlibuiza.sample.v3.uizavideov3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class V3SetEntityIdActivity extends BaseActivity {
    private EditText etInputEntityId;
    private Button btStart;

    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final String entityIdDefaultVOD = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    private final String entityIdDefaultLIVE = "da3e6528-6a4a-4103-a24d-7518fb81e975";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find views
        etInputEntityId = (EditText) findViewById(R.id.et_input_entity_id);
        btStart = (Button) findViewById(R.id.bt_start);

        //set default value entity id
        etInputEntityId.setText(entityIdDefaultVOD);
        LUIUtil.setLastCursorEditText(etInputEntityId);

        initWorkspace();

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
                String entityId = etInputEntityId.getText().toString();

                final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, entityId);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
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

    private void initWorkspace() {
        UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
        //TODO hard code environment STAG
        UizaDataV3.getInstance().initSDK(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, Constants.ENVIRONMENT_STAG);
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

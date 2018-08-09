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
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class V3SetEntityIdActivity extends BaseActivity {
    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;

    //workspace stag
    /*private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final int env = Constants.ENVIRONMENT_STAG;
    private final String entityIdDefaultVOD = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    private final String entityIdDefaultLIVE = "45a908f7-a62e-4eaf-8ce2-dc5699f33406";
    private final String metadataDefault0 = "00932b61-1d39-45d2-8c7d-3d99ad9ea95a";*/

    //workspace prod
    private final String DF_DOMAIN_API = "loctbprod01.uiza.co";
    private final String DF_TOKEN = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private final String DF_APP_ID = "9816792bb84642f09d843af4f93fb748";
    private final int env = Constants.ENVIRONMENT_PROD;
    private final String entityIdDefaultVOD = "71472a9b-662d-4eee-837e-3ad98b99140a";
    private final String entityIdDefaultLIVE = "6356e2c3-00af-495e-b60c-361f976b4084";
    private final String metadataDefault0 = "0e87adaa-49ef-4b6e-a827-6c68a63796b4";

    //for entity id
    private EditText etInputEntityId;
    private Button btStart;

    //for metadata id
    private EditText etInputMetadataId;
    private Button btStartPf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWorkspace();
        findViews();
        initUIEntity();
        initUIPlaylistFolder();
    }

    private void findViews() {
        etInputEntityId = (EditText) findViewById(R.id.et_input_entity_id);
        btStart = (Button) findViewById(R.id.bt_start);
        etInputMetadataId = (EditText) findViewById(R.id.et_input_metadata_id);
        btStartPf = (Button) findViewById(R.id.bt_start_pf);
    }

    private void initWorkspace() {
        UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
        UizaDataV3.getInstance().initSDK(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID, env);
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

    private void initUIEntity() {
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
                LPref.setClickedPip(activity, false);
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

    private void initUIPlaylistFolder() {
        //set default value entity id
        etInputMetadataId.setText(metadataDefault0);
        LUIUtil.setLastCursorEditText(etInputEntityId);
        etInputMetadataId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    btStartPf.setEnabled(false);
                } else {
                    btStartPf.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btStartPf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LPref.setClickedPip(activity, false);
                String metadataId = etInputMetadataId.getText().toString();
                final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
                intent.putExtra(Constants.KEY_UIZA_METADAT_ENTITY_ID, metadataId);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_play_list_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputMetadataId.setText(metadataDefault0);
                LUIUtil.setLastCursorEditText(etInputMetadataId);
            }
        });
        findViewById(R.id.bt_clear_pf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputMetadataId.setText("");
            }
        });
    }
}

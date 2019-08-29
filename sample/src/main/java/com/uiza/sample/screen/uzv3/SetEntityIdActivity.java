package com.uiza.sample.screen.uzv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.uiza.sample.LSApplication;
import com.uiza.sample.R;
import com.uiza.sample.screen.demoui.HomeCanSlideActivity;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;

public class SetEntityIdActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    //for entity id
    private EditText etInputEntityId;
    private Button btStart;

    //for metadata id
    private EditText etInputMetadataId;
    private Button btStartPf;

    private boolean isLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_player_input_entity_id_activity);
        findViews();
        initUIEntity();
        initUIPlaylistFolder();
    }

    private void findViews() {
        etInputEntityId = findViewById(R.id.et_input_entity_id);
        btStart = findViewById(R.id.bt_start);
        etInputMetadataId = findViewById(R.id.et_input_metadata_id);
        btStartPf = findViewById(R.id.bt_start_pf);
        findViewById(R.id.bt_demo_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, HomeCanSlideActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        UzDialogUtil.hideAllDialog();
        super.onDestroy();
    }

    private void initUIEntity() {
        //set default value entity id
        etInputEntityId.setText(LSApplication.entityIdDefaultVOD);
        UzDisplayUtil.setLastCursorEditText(etInputEntityId);
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
                final Intent intent = new Intent(activity, UzPlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, entityId);
                intent.putExtra(Constants.KEY_UIZA_IS_LIVE, isLive);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(LSApplication.entityIdDefaultVOD);
                UzDisplayUtil.setLastCursorEditText(etInputEntityId);
                isLive = false;
            }
        });
        findViewById(R.id.bt_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(LSApplication.entityIdDefaultLIVE);
                UzDisplayUtil.setLastCursorEditText(etInputEntityId);
                isLive = true;
            }
        });
        findViewById(R.id.bt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText("");
                isLive = false;
            }
        });
    }

    private void initUIPlaylistFolder() {
        etInputMetadataId.setText(LSApplication.metadataDefault0);
        UzDisplayUtil.setLastCursorEditText(etInputEntityId);
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
                String metadataId = etInputMetadataId.getText().toString();
                final Intent intent = new Intent(activity, UzPlayerActivity.class);
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, metadataId);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_play_list_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputMetadataId.setText(LSApplication.metadataDefault0);
                UzDisplayUtil.setLastCursorEditText(etInputMetadataId);
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

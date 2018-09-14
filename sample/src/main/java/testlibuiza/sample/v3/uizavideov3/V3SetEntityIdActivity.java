package testlibuiza.sample.v3.uizavideov3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.v3.demoui.HomeV4CanSlideActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uzv3.util.UZUtil;

public class V3SetEntityIdActivity extends BaseActivity {
    //for entity id
    private EditText etInputEntityId;
    private Button btStart;

    //for metadata id
    private EditText etInputMetadataId;
    private Button btStartPf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initUIEntity();
        initUIPlaylistFolder();
    }

    private void findViews() {
        etInputEntityId = (EditText) findViewById(R.id.et_input_entity_id);
        btStart = (Button) findViewById(R.id.bt_start);
        etInputMetadataId = (EditText) findViewById(R.id.et_input_metadata_id);
        btStartPf = (Button) findViewById(R.id.bt_start_pf);

        findViewById(R.id.bt_demo_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, HomeV4CanSlideActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
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
        etInputEntityId.setText(LSApplication.entityIdDefaultVOD);
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
                UZUtil.setClickedPip(activity, false);
                String entityId = etInputEntityId.getText().toString();
                final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
                intent.putExtra(Constants.KEY_UIZA_IS_PLAYLIST_FOLDER, false);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, entityId);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(LSApplication.entityIdDefaultVOD);
                LUIUtil.setLastCursorEditText(etInputEntityId);
            }
        });
        findViewById(R.id.bt_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputEntityId.setText(LSApplication.entityIdDefaultLIVE);
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
        etInputMetadataId.setText(LSApplication.metadataDefault0);
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
                UZUtil.setClickedPip(activity, false);
                String metadataId = etInputMetadataId.getText().toString();
                final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
                intent.putExtra(Constants.KEY_UIZA_IS_PLAYLIST_FOLDER, true);
                intent.putExtra(Constants.KEY_UIZA_METADAT_ENTITY_ID, metadataId);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        findViewById(R.id.bt_play_list_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputMetadataId.setText(LSApplication.metadataDefault0);
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

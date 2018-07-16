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
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

public class UizaSDKPlayerV3Activity extends BaseActivity {

    private EditText etInputEntityId;
    private Button btStart;

    //change your infor
    private final String currentPlayerId = Constants.PLAYER_ID_SKIN_1;
    private final String domainTracking = Constants.URL_TRACKING_STAG;
    private final String DF_DOMAIN_API = "android-api.uiza.co";
    private final String DF_TOKEN = "uap-16f8e65d8e2643ffa3ff5ee9f4f9ba03-a07716a6";
    private final String DF_APP_ID = "16f8e65d8e2643ffa3ff5ee9f4f9ba03";
    private final String entityIdDefault = "61d4b1e2-95d9-418d-9e52-e1d798e869b4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //find views
        etInputEntityId = (EditText) findViewById(R.id.et_input_entity_id);
        btStart = (Button) findViewById(R.id.bt_start);

        //set default value entity id
        etInputEntityId.setText(entityIdDefault);
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
                UizaDataV3.getInstance().setCurrentPlayerId(currentPlayerId);
                UizaDataV3.getInstance().initTracking(domainTracking);
                UizaDataV3.getInstance().initSDK(DF_DOMAIN_API, DF_TOKEN, DF_APP_ID);

                final Intent intent = new Intent(activity, V3CannotSlidePlayer.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
                finish();
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
}

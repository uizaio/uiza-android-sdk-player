package testlibuiza.sample.v3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.v3.api.UZTestAPIActivity;
import testlibuiza.sample.v3.customhq.CustomHQActivity;
import testlibuiza.sample.v3.customskin.CustomSkinCodeSeekbarActivity;
import testlibuiza.sample.v3.customskin.CustomSkinCodeUZTimebarActivity;
import testlibuiza.sample.v3.customskin.CustomSkinXMLActivity;
import testlibuiza.sample.v3.customskin.ResizeActivity;
import testlibuiza.sample.v3.dummy.DummyActivity;
import testlibuiza.sample.v3.error.ErrorActivity;
import testlibuiza.sample.v3.event.EventActivity;
import testlibuiza.sample.v3.fb.FBListVideoActivity;
import testlibuiza.sample.v3.fullscreen.PortraitFullScreenActivity;
import testlibuiza.sample.v3.linkplay.PlayerActivity;
import testlibuiza.sample.v3.slide.Slide0Activity;
import testlibuiza.sample.v3.utube.CustomSkinCodeUZTimebarUTubeActivity;
import testlibuiza.sample.v3.utube.CustomSkinCodeUZTimebarUTubeWithSlideActivity;
import testlibuiza.sample.v3.uzv3.SetEntityIdActivity;
import testlibuiza.sample.v3.volume.VolumeActivity;
import vn.uiza.core.common.Constants;

public class MainV3Activity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v3_main);
        findViewById(R.id.bt_test_full_screen).setOnClickListener((v) -> {
            Intent intent = new Intent(activity, PortraitFullScreenActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.bt_sdk_v3).setOnClickListener(v -> {
            Intent intent = new Intent(activity, SetEntityIdActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_guide).setOnClickListener(view -> {
            Intent intent = new Intent(activity, UZTestAPIActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.bt_uiza_custom_skin_xml).setOnClickListener(v -> {
            Intent intent = new Intent(activity, CustomSkinXMLActivity.class);
            intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_code_seekbar)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(activity, CustomSkinCodeSeekbarActivity.class);
                    startActivity(intent);
                });
        findViewById(R.id.bt_uiza_custom_skin_code_uz_timebar)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(activity, CustomSkinCodeUZTimebarActivity.class);
                    intent.putExtra(Constants.KEY_UIZA_ENTITY_ID,
                            LSApplication.entityIdDefaultVOD);
                    startActivity(intent);
                });
        findViewById(R.id.bt_uiza_custom_skin_u_tube)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(activity,
                            CustomSkinCodeUZTimebarUTubeActivity.class);
                    startActivity(intent);
                });
        findViewById(R.id.bt_uiza_custom_skin_u_tube_with_slide)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(activity,
                            CustomSkinCodeUZTimebarUTubeWithSlideActivity.class);
                    startActivity(intent);
                });
        findViewById(R.id.bt_slide_0).setOnClickListener(v -> {
            final Intent intent = new Intent(activity, Slide0Activity.class);
            intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
            //intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, LSApplication.metadataDefault0);
            startActivity(intent);
        });
        findViewById(R.id.bt_custom_hq).setOnClickListener(view -> {
            Intent intent = new Intent(activity, CustomHQActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_play_any_link).setOnClickListener(view -> {
            Intent intent = new Intent(activity, PlayerActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_error).setOnClickListener(view -> {
            Intent intent = new Intent(activity, ErrorActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_volume).setOnClickListener(view -> {
            Intent intent = new Intent(activity, VolumeActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_event).setOnClickListener(view -> {
            Intent intent = new Intent(activity, EventActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_resize).setOnClickListener(view -> {
            Intent intent = new Intent(activity, ResizeActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_mini_fb).setOnClickListener(view -> {
            Intent intent = new Intent(activity, FBListVideoActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_dummy).setVisibility(View.GONE);
        findViewById(R.id.bt_dummy).setOnClickListener(view -> {
            Intent intent = new Intent(activity, DummyActivity.class);
            startActivity(intent);
        });

        if (LSApplication.DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.cancel();
                    onBackPressed();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

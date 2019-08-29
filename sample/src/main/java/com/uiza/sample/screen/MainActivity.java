package com.uiza.sample.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uiza.sample.LSApplication;
import com.uiza.sample.R;
import com.uiza.sample.screen.api.UZTestAPIActivity;
import com.uiza.sample.screen.customhq.CustomHQActivity;
import com.uiza.sample.screen.customskin.CustomSkinCodeSeekbarActivity;
import com.uiza.sample.screen.customskin.CustomSkinCodeUzTimebarActivityListener;
import com.uiza.sample.screen.customskin.CustomSkinXMLActivity;
import com.uiza.sample.screen.customskin.ResizeActivity;
import com.uiza.sample.screen.error.ErrorActivity;
import com.uiza.sample.screen.event.EventActivity;
import com.uiza.sample.screen.fb.FBListVideoActivity;
import com.uiza.sample.screen.fullscreen.PortraitFullScreenActivity;
import com.uiza.sample.screen.linkplay.PlayerActivity;
import com.uiza.sample.screen.slide.Slide0Activity;
import com.uiza.sample.screen.utube.CustomSkinCodeUZTimebarUTubeWithSlideActivity;
import com.uiza.sample.screen.utube.CustomSkinCodeUzTimebarUTubeActivityListener;
import com.uiza.sample.screen.uzv3.SetEntityIdActivity;
import com.uiza.sample.screen.volume.VolumeActivity;

import io.uiza.core.util.constant.Constants;

public class MainActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_test_full_screen).setOnClickListener((v) -> {
            Intent intent = new Intent(activity, PortraitFullScreenActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_test_api_v3).setOnClickListener(v -> {
            Intent intent = new Intent(activity, UZTestAPIActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_sdk_v3).setOnClickListener(v -> {
            Intent intent = new Intent(activity, SetEntityIdActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_xml).setOnClickListener(view -> {
            Intent intent = new Intent(activity, CustomSkinXMLActivity.class);
            intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_code_seekbar).setOnClickListener(view -> {
            Intent intent = new Intent(activity, CustomSkinCodeSeekbarActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_code_uz_timebar).setOnClickListener(view -> {
            Intent intent = new Intent(activity, CustomSkinCodeUzTimebarActivityListener.class);
            intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
            //intent.putExtra(Constants.KEY_UIZA_THUMBNAIL, LSApplication.thumbEntityIdDefaultVOD);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_u_tube).setOnClickListener(view -> {
            Intent intent = new Intent(activity,
                    CustomSkinCodeUzTimebarUTubeActivityListener.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_uiza_custom_skin_u_tube_with_slide).setOnClickListener(view -> {
            Intent intent = new Intent(activity,
                    CustomSkinCodeUZTimebarUTubeWithSlideActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.bt_slide_0).setOnClickListener(view -> {
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

        if (LSApplication.DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();
            onBackPressed();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

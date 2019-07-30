package testlibuiza.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import testlibuiza.sample.guidecallapi.TestAPI;
import testlibuiza.sample.livestream.LivestreamBroadcasterActivity;
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
import testlibuiza.sample.v3.linkplay.PlayerActivity;
import testlibuiza.sample.v3.slide.Slide0Activity;
import testlibuiza.sample.v3.utube.CustomSkinCodeUZTimebarUTubeActivity;
import testlibuiza.sample.v3.utube.CustomSkinCodeUZTimebarUTubeWithSlideActivity;
import testlibuiza.sample.v3.uzv3.SetEntityIdActivity;
import testlibuiza.sample.v3.volume.VolumeActivity;
import vn.uiza.core.common.Constants;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_test_api_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UZTestAPIActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_sdk_v3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SetEntityIdActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TestAPI.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_livestream_broadcaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LivestreamBroadcasterActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_xml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinXMLActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_code_seekbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinCodeSeekbarActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_code_uz_timebar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinCodeUZTimebarActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
                //intent.putExtra(Constants.KEY_UIZA_THUMBNAIL, LSApplication.thumbEntityIdDefaultVOD);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_u_tube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinCodeUZTimebarUTubeActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_uiza_custom_skin_u_tube_with_slide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomSkinCodeUZTimebarUTubeWithSlideActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_slide_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(activity, Slide0Activity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, LSApplication.entityIdDefaultVOD);
                //intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, LSApplication.metadataDefault0);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_custom_hq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CustomHQActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_play_any_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PlayerActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ErrorActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_volume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, VolumeActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EventActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_resize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ResizeActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_mini_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FBListVideoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_dummy).setVisibility(View.GONE);
        findViewById(R.id.bt_dummy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DummyActivity.class);
                startActivity(intent);
            }
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
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

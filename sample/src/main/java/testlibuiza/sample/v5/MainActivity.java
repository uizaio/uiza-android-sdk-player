package testlibuiza.sample.v5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;
import testlibuiza.sample.v3.dummy.DummyActivity;
import testlibuiza.sample.v3.error.ErrorActivity;
import testlibuiza.sample.v3.linkplay.PlayerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_vod_list).setOnClickListener(this);
        findViewById(R.id.bt_guide).setOnClickListener(this);
        findViewById(R.id.bt_fixtest).setOnClickListener(this);
        findViewById(R.id.bt_live_playback).setOnClickListener(this);
        findViewById(R.id.bt_settings).setOnClickListener(this);
        findViewById(R.id.bt_dummy).setOnClickListener(this);
        findViewById(R.id.bt_error).setOnClickListener(this);
    }

    private <T extends Activity> void launchActivity(Class<T> tClass) {
        startActivity(new Intent(MainActivity.this, tClass));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_vod_list:
                launchActivity(VODListActivity.class);
                break;
            case R.id.bt_guide:
                launchActivity(UizaTestAPIActivity.class);
                break;
            case R.id.bt_live_playback:
                launchActivity(LiveListActivity.class);
                break;
            case R.id.bt_fixtest:
                launchActivity(PlayerActivity.class);
                break;
            case R.id.bt_settings:
                launchActivity(SettingsActivity.class);
                break;
            case R.id.bt_dummy:
                launchActivity(DummyActivity.class);
                break;
            case R.id.bt_error:
                launchActivity(ErrorActivity.class);
                break;
            default:
                break;
        }
    }
}

package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.preference.PreferenceManager;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.live_btn).setOnClickListener(this);
        findViewById(R.id.force_live_btn).setOnClickListener(this);
        findViewById(R.id.setting_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_btn:
                startActivity(new Intent(MainActivity.this, LiveListActivity.class));
                break;
            case R.id.force_live_btn:
                showCreateLiveDialog();
                break;
            case R.id.setting_btn:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
    }

    private void showCreateLiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New LiveStream");
        View root = getLayoutInflater().inflate(R.layout.dlg_create_live, null);
        builder.setView(root);
        final AppCompatEditText streamIp = root.findViewById(R.id.stream_name);
        streamIp.setHint("rtmp://your_endpoint");
        streamIp.setText("rtmp://679b139b89-in.streamwiz.dev/transcode/live_ljNx4GLp3F");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            String endpoint = streamIp.getText().toString();
            boolean isValid = true;
            if (TextUtils.isEmpty(endpoint)) {
                streamIp.setError("Error");
                isValid = false;
            }
            if (isValid) {
                Timber.d("Your Endpoint = %s", endpoint);
                Intent intent = new Intent(MainActivity.this, UizaLiveActivity.class);
                intent.putExtra(SampleLiveApplication.EXTRA_STREAM_ENDPOINT, endpoint);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }
}

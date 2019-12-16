package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.preference.PreferenceManager;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String CURRENT_REGION_KEY = "extra_region_key";
    AppCompatSpinner spinner;
    String currentRegion = "asia-south-1";
    String[] regions;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        regions = getResources().getStringArray(R.array.region_arrays);
        spinner = findViewById(R.id.region_spinner);
        findViewById(R.id.live_btn).setOnClickListener(this);
        findViewById(R.id.force_live_btn).setOnClickListener(this);
        findViewById(R.id.setting_btn).setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        currentRegion = preferences.getString(CURRENT_REGION_KEY, regions[0]);
        for (int i = 0; i < regions.length; i++) {
            if (currentRegion.equalsIgnoreCase(regions[i])) {
                spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentRegion = regions[position];
        preferences.edit().putString(CURRENT_REGION_KEY, currentRegion).apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        currentRegion = regions[0];
        preferences.edit().putString(CURRENT_REGION_KEY, currentRegion).apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_btn:
                Intent intent = new Intent(MainActivity.this, LiveListActivity.class);
                intent.putExtra(CURRENT_REGION_KEY, currentRegion);
                startActivity(intent);
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

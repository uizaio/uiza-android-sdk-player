package testlibuiza.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import testlibuiza.R;
import testlibuiza.sample.utils.TabsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        String apiToken = preferences.getString("api_token_key", "");
        if (TextUtils.isEmpty(apiToken)) {
            showSourceSettingDialog();
        }
    }

    private void showSourceSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("API Config");
        builder.setMessage("Firstly, you need to set your API Key.");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> launchActivity(SettingsActivity.class));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            MainActivity.this.finish();
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login_firebase:
                launchActivity(FirebaseAuthActivity.class);
                break;
            case R.id.action_fixtest:
                launchActivity(PlayerActivity.class);
                break;
            case R.id.action_settings:
                launchActivity(SettingsActivity.class);
                break;
            case R.id.action_test_api:
                launchActivity(UizaTestAPIActivity.class);
                break;
            case R.id.action_dummy:
                launchActivity(DummyActivity.class);
                break;
            case R.id.action_error:
                launchActivity(ErrorActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private <T extends Activity> void launchActivity(Class<T> tClass) {
        startActivity(new Intent(MainActivity.this, tClass));
    }
}

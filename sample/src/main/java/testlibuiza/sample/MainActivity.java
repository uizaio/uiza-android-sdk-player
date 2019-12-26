package testlibuiza.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import testlibuiza.R;
import testlibuiza.sample.utils.TabsPagerAdapter;

public class MainActivity extends AppCompatActivity {

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

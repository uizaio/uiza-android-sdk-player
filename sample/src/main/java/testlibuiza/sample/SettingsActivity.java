package testlibuiza.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import testlibuiza.BuildConfig;
import testlibuiza.R;
import timber.log.Timber;
import uizacoresdk.UizaCoreSDK;
import vn.uiza.restapi.restclient.UizaRestClient;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preference, rootKey);
            EditTextPreference apiTokenPref = findPreference("app_id_key");
            if (apiTokenPref != null) {
                apiTokenPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue instanceof String) {
                        String value = (String) newValue;
                        UizaCoreSDK.changeAppId(value);
                        Timber.e(value);
                    }
                    return true;
                });
            }
            ListPreference lstPreference = findPreference("api_base_url_key");
            if (lstPreference != null) {
                lstPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
                    if (newValue instanceof String) {
                        String value = (String) newValue;
                        UizaRestClient.getInstance().changeApiBaseUrl(value);
                    }
                    return true;
                }));
            }

            Preference verPref = findPreference("version_key");
            if (verPref != null) {
                verPref.setDefaultValue(String.valueOf(BuildConfig.VERSION_CODE));
                verPref.setSummary(String.format(Locale.getDefault(), "%s - %d", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
            }
        }
    }
}

package io.uiza.samplelive;

import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import timber.log.Timber;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.restapi.restclient.UizaRestClient;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference apiTokenPref = findPreference("api_token_key");
            if (apiTokenPref != null) {
                apiTokenPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue instanceof String) {
                        String value = (String) newValue;
                        UizaClientFactory.changeAPIToken(value);
                        Timber.e(value);
                    }
                    return true;
                });
            }

            EditTextPreference frameInterval = findPreference("frame_interval_key");
            if (frameInterval != null)
                frameInterval.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

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

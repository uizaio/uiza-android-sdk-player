package io.uiza.samplelive;

import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import timber.log.Timber;
import vn.uiza.restapi.restclient.UizaClientFactory;

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

        }
    }
}

package testlibuiza.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import testlibuiza.R;
import timber.log.Timber;
import uizacoresdk.util.UZUtil;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.restapi.restclient.UizaRestClient;
import vn.uiza.utils.Utils;

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
            EditTextPreference apiTokenPref = findPreference("api_token_key");
            if (apiTokenPref != null) {
                apiTokenPref.setOnPreferenceChangeListener((preference, newValue) -> {
                    if (newValue instanceof String) {
                        String value = (String) newValue;
                        UZUtil.changeAPIToken(value);
                        Timber.e(value);
                    }
                    return true;
                });
            }
            ListPreference lstPreference = findPreference("api_base_url_key");
            if(lstPreference != null){
                lstPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
                    if(newValue instanceof String){
                        String value = (String)newValue;
                        UizaRestClient.getInstance().changeApiBaseUrl(value);
                    }
                    return true;
                }));
            }

        }
    }
}

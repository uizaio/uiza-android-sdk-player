package uiza.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.gson.Gson;

import uiza.uiza.com.demo.BuildConfig;
import vn.loitp.core.common.Constants;
import vn.loitp.data.ActivityData;

public class LSApplication extends MultiDexApplication {
    private final String TAG = LSApplication.class.getSimpleName();
    private static LSApplication instance;
    private Gson gson;
    protected String userAgent = Constants.USER_AGENT;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Constants.setIsDebug(true);
        ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_FADE);
        //userAgent = Util.getUserAgent(this, Constants.USER_AGENT);

        //RestClientV2.init(getString(R.string.dev_uiza_URL), Constants.TOKEN);
        //UizaData.getInstance().init(getString(R.string.dev_uiza_URL), Constants.TOKEN, UizaData.PLAYER_ID_SKIN_1);

        //RestClientV2.init("http://wtt-api.uiza.io/", "BIH80NYmucZwCoqPvrdI3ZU9ATB909Gi-1512972145301");
        //UizaData.getInstance().init(getString(R.string.dev_uiza_wtt_URL), Constants.TOKEN_WTT, UizaData.PLAYER_ID_SKIN_1);

        //RestClientV2.init(getString(R.string.dev_uiza_v2_URL));
        //RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
    }

    public Gson getGson() {
        return gson;
    }

    public static LSApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }
}

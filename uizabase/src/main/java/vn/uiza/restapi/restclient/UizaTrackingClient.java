package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import java.security.InvalidParameterException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class UizaTrackingClient extends RestClient {

    private Retrofit retrofit;

    private static class UZRestClientTrackingHelper {
        private static final UizaTrackingClient INSTANCE = new UizaTrackingClient();
    }

    public static UizaTrackingClient getInstance() {
        return UZRestClientTrackingHelper.INSTANCE;
    }

    private UizaTrackingClient() {
    }

    public void init(String baseApiUrl) {
        this.init(baseApiUrl, "", "");
    }

    @Override
    public void init(String baseApiUrl, String appId, String sigedKey) {
        Timber.d("init %s - %s", baseApiUrl, appId);
        if (TextUtils.isEmpty(baseApiUrl)) {
            throw new InvalidParameterException("baseApiUrl cannot null or empty");
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(provideHttpClient(false))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            throw new IllegalStateException("Must call init() before use");
        }
        return retrofit.create(serviceClass);
    }

}
package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import java.security.InvalidParameterException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

public class UZRestClientTracking extends RestClient {

    private Retrofit retrofit;

    private static class UZRestClientTrackingHelper {
        private static final UZRestClientTracking INSTANCE = new UZRestClientTracking();
    }

    public static UZRestClientTracking getInstance() {
        return UZRestClientTrackingHelper.INSTANCE;
    }

    private UZRestClientTracking() {
    }

    @Override
    public void init(String baseApiUrl, String token) {
        Timber.d("init %s - %s", baseApiUrl, token);
        if (TextUtils.isEmpty(baseApiUrl)) {
            throw new InvalidParameterException("baseApiUrl cannot null or empty");
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(provideHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
                .build();

        if (!TextUtils.isEmpty(token)) {
            addAccessToken(token);
        }
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
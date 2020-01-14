package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import java.security.InvalidParameterException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class UizaHeartBeatClient extends RestClient {
    private Retrofit retrofit;

    private static class UZRestClientHeartBeatHelper {
        private static final UizaHeartBeatClient INSTANCE = new UizaHeartBeatClient();
    }

    public static UizaHeartBeatClient getInstance() {
        return UZRestClientHeartBeatHelper.INSTANCE;
    }

    private UizaHeartBeatClient() {

    }

    public void init(String baseApiUrl){
        this.init(baseApiUrl, "", "");
    }

    @Override
    public void init(String baseApiUrl, String appId, String signedKey) {
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

    @Override
    public <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            throw new IllegalStateException("Must call init() before use");
        }
        return retrofit.create(serviceClass);
    }

}
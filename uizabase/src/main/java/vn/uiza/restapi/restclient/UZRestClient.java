package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import java.security.InvalidParameterException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class UZRestClient extends RestClient {


    private Retrofit retrofit;

    private static class UizaRestClientHelper {
        private static final UZRestClient INSTANCE = new UZRestClient();
    }

    public static UZRestClient getInstance() {
        return UizaRestClientHelper.INSTANCE;
    }

    private UZRestClient() {

    }

    @Override
    public void init(String baseApiUrl, String token) {
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
            addAuthorization(token);
        }
    }

    @Override
    public <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            throw new IllegalStateException("Must call init() before using");
        }
        return retrofit.create(serviceClass);
    }
}
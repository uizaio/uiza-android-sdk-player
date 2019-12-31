package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import java.security.InvalidParameterException;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UizaRestClient extends RestClient {


    private Retrofit retrofit;
    private String apiBaseUrl;
    private String token;

    private static class UizaRestClientHelper {
        private static final UizaRestClient INSTANCE = new UizaRestClient();
    }

    public static UizaRestClient getInstance() {
        return UizaRestClientHelper.INSTANCE;
    }

    private UizaRestClient() {

    }

    @Override
    public void init(String apiBaseUrl, String token) {
        if (TextUtils.isEmpty(apiBaseUrl)) {
            throw new InvalidParameterException("apiBaseUrl cannot null or empty");
        }
        this.apiBaseUrl = getApiBaseUrl(apiBaseUrl);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.apiBaseUrl)
                .client(provideHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();

        if (!TextUtils.isEmpty(token)) {
            this.token = token;
            addAuthorization(this.token);
        }
    }

    private String getApiBaseUrl(String apiBaseUrl) {
        if (apiBaseUrl.startsWith("http:") || apiBaseUrl.startsWith("https:")) {
            return apiBaseUrl;
        } else {
            return String.format(Locale.getDefault(), "https://%s", apiBaseUrl);
        }
    }

    private boolean isChangeUrl(String url) {
        return !this.apiBaseUrl.equalsIgnoreCase(url);

    }

    public void changeApiBaseUrl(String newApiBaseUrl) {
        if (TextUtils.isEmpty(newApiBaseUrl)) {
            throw new InvalidParameterException("apiBaseUrl cannot null or empty");
        }
        newApiBaseUrl = getApiBaseUrl(newApiBaseUrl);
        if (isChangeUrl(newApiBaseUrl)) {
            this.apiBaseUrl = newApiBaseUrl;
            retrofit = new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .client(provideHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .build();
            if (!TextUtils.isEmpty(this.token)) {
                addAuthorization(this.token);
            }
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
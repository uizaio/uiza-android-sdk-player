package vn.uiza.restapi.restclient;

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.security.InvalidParameterException;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UizaRestClient extends RestClient {


    private Retrofit retrofit;
    private String apiBaseUrl;

    private static class UizaRestClientHelper {
        private static final UizaRestClient INSTANCE = new UizaRestClient();
    }

    public static UizaRestClient getInstance() {
        return UizaRestClientHelper.INSTANCE;
    }

    private UizaRestClient() {

    }

    @Override
    public void init(String apiBaseUrl, String appId, String signedKey) {
        if (TextUtils.isEmpty(apiBaseUrl)) {
            throw new InvalidParameterException("apiBaseUrl cannot null or empty");
        }
        this.apiBaseUrl = getApiBaseUrl(apiBaseUrl);
        setAppId(appId);
        setSignedKey(signedKey);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.apiBaseUrl)
                .client(provideHttpClient(false))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    public void changeAppId(String appId) {
        if (!this.getAppId().equals(appId)) {
            setAppId(appId);
            retrofit = new Retrofit.Builder()
                    .baseUrl(this.apiBaseUrl)
                    .client(provideHttpClient(true))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .build();
        }
    }

    private String getApiBaseUrl(String apiBaseUrl) {
        if (URLUtil.isNetworkUrl(apiBaseUrl)) {
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
                    .client(provideHttpClient(true))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .build();
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
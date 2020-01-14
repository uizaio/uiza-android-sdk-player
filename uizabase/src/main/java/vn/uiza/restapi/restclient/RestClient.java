package vn.uiza.restapi.restclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import vn.uiza.helpers.DateTypeDeserializer;
import vn.uiza.restapi.interceptors.RestRequestInterceptor;

public abstract class RestClient {


    private static final String ACCESSTOKEN = "AccessToken";
    private static final int CONNECT_TIMEOUT_TIME = 20;//20s
    private RestRequestInterceptor restRequestInterceptor;
    private String appId;
    private String signedKey;


    OkHttpClient provideHttpClient(boolean forceRecreate) {
        return new OkHttpClient.Builder()
                .readTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .addInterceptor(provideInterceptor(forceRecreate))
                .retryOnConnectionFailure(true)
                .addInterceptor(provideLogging())  // <-- this is the important line!
                .build();
    }

    HttpLoggingInterceptor provideLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.d(message));
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    RestRequestInterceptor provideInterceptor(boolean forceRecreate) {
        if (restRequestInterceptor == null || forceRecreate)
            restRequestInterceptor = new RestRequestInterceptor(this.appId, this.signedKey);
        return restRequestInterceptor;
    }

    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeDeserializer())
                .create();
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setSignedKey(String signedKey) {
        this.signedKey = signedKey;
    }

    public abstract void init(String baseApiUrl, String appId, String signedKey);

    public abstract <T> T createService(Class<T> serviceClass);

    public void addHeader(String name, String value) {
        if (restRequestInterceptor != null) {
            restRequestInterceptor.addHeader(name, value);
        }
    }

    public void removeHeader(String name) {
        if (restRequestInterceptor != null) {
            restRequestInterceptor.removeHeader(name);
        }
    }

    public boolean hasHeader(String name) {
        if (restRequestInterceptor != null) {
            return restRequestInterceptor.hasHeader(name);
        }
        return false;
    }
}

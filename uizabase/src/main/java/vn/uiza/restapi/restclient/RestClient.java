package vn.uiza.restapi.restclient;

import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import vn.uiza.helpers.DateTimeAdapter;
import vn.uiza.restapi.interceptors.RestRequestInterceptor;

public abstract class RestClient {

    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESSTOKEN = "AccessToken";
    private static final int CONNECT_TIMEOUT_TIME = 20;//20s
    private RestRequestInterceptor restRequestInterceptor;

    OkHttpClient provideHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .addInterceptor(provideInterceptor())
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

    RestRequestInterceptor provideInterceptor() {
        if (restRequestInterceptor == null)
            restRequestInterceptor = new RestRequestInterceptor();
        return restRequestInterceptor;
    }

    Moshi provideMoshi() {
        return new Moshi.Builder().add(new DateTimeAdapter()).build();
    }

    public abstract void init(String baseApiUrl, String token);

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

    public void addAuthorization(String token) {
        addHeader(AUTHORIZATION, token);
    }

    public void removeAuthorization() {
        removeHeader(AUTHORIZATION);
    }

    public void addAccessToken(String accessToken) {
        addHeader(ACCESSTOKEN, accessToken);
    }

    public void removeAccessToken() {
        removeHeader(ACCESSTOKEN);
    }

    public boolean hasHeader(String name) {
        if (restRequestInterceptor != null) {
            return restRequestInterceptor.hasHeader(name);
        }
        return false;
    }
}

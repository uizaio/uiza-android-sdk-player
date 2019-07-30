package vn.uiza.restapi.restclient;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.DateTypeDeserializer;

public class UZRestClientTracking {
    private static final String TAG = UZRestClientTracking.class.getSimpleName();
    private static final int CONNECT_TIMEOUT_TIME = 20;//20s
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESSTOKEN = "AccessToken";
    private static Retrofit retrofit;
    private static RestRequestInterceptor restRequestInterceptor;

    public static void init(String baseApiUrl) {
        init(baseApiUrl, "");
    }

    public static void init(String baseApiUrl, String token) {
        LLog.d(TAG, "init " + baseApiUrl + " - " + token);
        if (TextUtils.isEmpty(baseApiUrl)) {
            throw new InvalidParameterException("baseApiUrl cannot null or empty");
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        restRequestInterceptor = new RestRequestInterceptor();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS)
                .addInterceptor(restRequestInterceptor)
                .retryOnConnectionFailure(true)
                .addInterceptor(logging)  // <-- this is the important line!
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        if (!TextUtils.isEmpty(token)) {
            addAuthorization(token);
        }
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            throw new IllegalStateException("Must call init() before use");
        }
        return retrofit.create(serviceClass);
    }

    public static void addHeader(String name, String value) {
        if (restRequestInterceptor != null) {
            restRequestInterceptor.addHeader(name, value);
        }
    }

    public static void addAuthorization(String token) {
        addHeader(AUTHORIZATION, token);
    }

    public static void removeAuthorization() {
        removeHeader(AUTHORIZATION);
    }

    public static void addAccessToken(String accessToken) {
        addHeader(ACCESSTOKEN, accessToken);
    }

    public static void removeAccessToken() {
        removeHeader(ACCESSTOKEN);
    }

    public static void removeHeader(String name) {
        if (restRequestInterceptor != null) {
            restRequestInterceptor.removeHeader(name);
        }
    }

    public static boolean hasHeader(String name) {
        if (restRequestInterceptor != null) {
            return restRequestInterceptor.hasHeader(name);
        }
        return false;
    }
}

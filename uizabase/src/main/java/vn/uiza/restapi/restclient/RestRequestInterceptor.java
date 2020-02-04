package vn.uiza.restapi.restclient;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import vn.uiza.utils.util.EncryptUtils;

public class RestRequestInterceptor implements Interceptor {
    private static final String TAG = "RestRequestInterceptor";
    public static final String AUTHORIZATION = "Authorization";
    private Hashtable<String, String> headers;
    private String hmacKey;
    private String appId;

    public RestRequestInterceptor(String appId, String signedKey) {
        this.appId = appId;
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(signedKey))
            this.hmacKey = String.format(Locale.getDefault(), "%s%s", signedKey, appId);
        headers = new Hashtable<>();
    }

    public RestRequestInterceptor() {
        headers = new Hashtable<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void removeHeader(String key) {
        headers.remove(key);
    }

    public boolean hasHeader(String key) {
        return headers.containsKey(key);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!TextUtils.isEmpty(this.appId) && !TextUtils.isEmpty(this.hmacKey)) {
            Request request = chain.request();
            String token;
            if (request.body() != null) {
                final Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                token = EncryptUtils.hmacSHA256(hmacKey, buffer.readByteArray());
            } else {
                token = EncryptUtils.hmacSHA256(hmacKey, "");
            }
            String authorization = String.format(Locale.getDefault(), "hmac %s:%s", this.appId, token);
            headers.remove(AUTHORIZATION);
            headers.put(AUTHORIZATION, authorization);
        }
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Enumeration<String> keys = headers.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = headers.get(key);
                builder.header(key, value);
            }
        }
        return chain.proceed(builder.build());
    }
}

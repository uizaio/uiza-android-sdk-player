package vn.uiza.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import timber.log.Timber;
import vn.uiza.helpers.DateTypeDeserializer;
import vn.uiza.helpers.DateTypeSerializer;

public final class StringUtils {

    /**
     * Email validation pattern.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");

    // default constructor
    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Convert object to string json
     *
     * @param value object instance T
     * @return String
     */

    public static <T> String toJson(T value) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .create();
        return gson.toJson(value);
    }

    /**
     * Convert object to string beauty json
     *
     * @param value object instance T
     * @return String
     */
    public static <T> String toBeautyJson(T value) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(value);
    }

    /**
     * Convert jsonString to List of object
     *
     * @param json The jsonString to convert
     * @return List object instance T if success. null otherwise
     */
    @Nullable
    public static <T> List<T> toList(@NonNull String json) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeDeserializer())
                .create();
        try {
            return gson.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        }
        return null;
    }

    /**
     * Convert jsonString to object
     *
     * @param json   The jsonString to convert
     * @param clazz: target Class
     * @return object instance T if success. null otherwise
     */
    @Nullable
    public static <T> T toObject(@NonNull String json, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeDeserializer())
                .create();
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        }
        return null;
    }

    /**
     * Validates if the given input is a valid email address.
     *
     * @param email The email to validate.
     * @return {@code true} if the input is a valid email. {@code false} otherwise.
     */
    public static boolean isEmailValid(@Nullable CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * convert html to plain text
     *
     * @param htmlText : html String
     * @return plain text
     */
    public static String htmlToPlainText(String htmlText) {
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(htmlText);
        }
        char[] chars = new char[spanned.length()];
        TextUtils.getChars(spanned, 0, spanned.length(), chars, 0);
        return new String(chars);
    }

}

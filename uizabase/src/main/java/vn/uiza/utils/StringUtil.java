package vn.uiza.utils;

import androidx.annotation.Nullable;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import timber.log.Timber;
import vn.uiza.core.common.Constants;
import vn.uiza.helpers.DateTimeAdapter;

public class StringUtil {
    private StringUtil() {
    }

    public static <T> String toJson(T value, Class<T> clazz) {
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        return builder.build()
                .adapter(clazz)
                .toJson(value);
    }

    public static <T> String toJson(List<T> values, Class<T> clazz) {
        ParameterizedType type = Types.newParameterizedType(List.class, clazz);
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        return builder.build()
                .adapter(type)
                .toJson(values);
    }

    public static <T> String toBeautyJson(T value, Class<T> clazz) {
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        return builder.build().adapter(clazz)
                .indent("    ")
                .toJson(value);
    }

    public static <T> String toBeautyJson(List<T> values, Class<T> clazz) {
        ParameterizedType type = Types.newParameterizedType(List.class, clazz);
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        return builder.build()
                .adapter(type)
                .indent("    ")
                .toJson(values);
    }

    @Nullable
    public static <T> List<T> toList(String json, Class<T> clazz) {
        ParameterizedType type = Types.newParameterizedType(List.class, clazz);
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        JsonAdapter<List<T>> adapter = builder.build().adapter(type);
        try {
            return adapter.fromJson(json);
        } catch (IOException e) {
            Timber.e(e);
        }
        return null;
    }

    @Nullable
    public static <T> T toObject(String json, Class<T> clazz) {
        Moshi.Builder builder = new Moshi.Builder();
        if (Constants.isV5AndAbove()) {
            builder.add(new DateTimeAdapter());
        }
        try {
            return builder.build()
                    .adapter(clazz)
                    .fromJson(json);
        } catch (IOException e) {
            Timber.e(e);
        }
        return null;
    }

}

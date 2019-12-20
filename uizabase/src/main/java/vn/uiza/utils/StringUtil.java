package vn.uiza.utils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import timber.log.Timber;
import vn.uiza.helpers.DateTypeDeserializer;
import vn.uiza.helpers.DateTypeSerializer;

public class StringUtil {
    private StringUtil() {
    }

    public static <T> String toJson(T value) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .create();
        return gson.toJson(value);
    }


    public static <T> String toBeautyJson(T value) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(value);
    }

    @Nullable
    public static <T> List<T> toList(String json) {
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

    @Nullable
    public static <T> T toObject(String json, Class<T> clazz) {
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

}

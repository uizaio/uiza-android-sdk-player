package vn.uiza.utils;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import vn.uiza.helpers.DateTypeSerializer;


public class ListUtil {
    // default constructor is private
    private ListUtil() {
    }

    public static <T> String toJson(List<T> values) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(values, listType);
    }


    public static <T> String toBeautyJson(List<T> values) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeSerializer())
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(values, listType);
    }


    public static <T> boolean isEmpty(@Nullable List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> filter(@NonNull List<T> list, Pre<T, Boolean> pre) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter(pre::get).collect(Collectors.toList());
        } else {
            List<T> col = new ArrayList<>();
            for (int i = 0; i < list.size(); i++)
                if (pre.get(list.get(i)))
                    col.add(list.get(i));
            return col;
        }
    }

    public interface Pre<T, R> {
        R get(T item);
    }
}

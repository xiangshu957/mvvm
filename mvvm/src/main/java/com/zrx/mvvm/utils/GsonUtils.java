package com.zrx.mvvm.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 */
public class GsonUtils {

    private GsonUtils() {

    }

    private static Gson getGsonObject() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson;
    }

    /**
     * 对象转字符串
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T extends Object> String ser(T object) {
        Gson gson = getGsonObject();
        return gson.toJson(object);
    }

    /**
     * 字符串转对象
     *
     * @param object
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends Object> T deser(String object, Class<T> tClass) {
        Gson gsonObject = getGsonObject();
        T result = null;
        try {
            result = gsonObject.fromJson(object, tClass);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    public static <T extends Object> T deserBeQuiet(String object, Class<T> tClass) {
        Gson gson = getGsonObject();
        T result;
        try {
            result = gson.fromJson(object, tClass);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static <T> T Json2Result(Class<T> tClass, String s) {
        T result;
        try {
            result = new Gson().fromJson(s, tClass);
            Log.d(tClass.toString() + "-----Json Mes", s);
        } catch (Exception e) {
            result = null;
            Log.e(tClass.toString() + "-----Json Error", s);
        }
        return result;
    }

}

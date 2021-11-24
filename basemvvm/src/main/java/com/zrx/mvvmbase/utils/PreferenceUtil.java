package com.zrx.mvvmbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/13
 * DES:
 */
public class PreferenceUtil {

    private static PreferenceUtil preferenceUtil;
    private Context context;
    private String FILE_NAME = "zrx_pro";

    private PreferenceUtil() {

    }

    public void init(Context context, String FILE_NAME) {
        this.context = context;
        this.FILE_NAME = FILE_NAME;
    }

    public static PreferenceUtil getInstance() {
        if (preferenceUtil == null) {
            synchronized (PreferenceUtil.class) {
                if (preferenceUtil == null) {
                    preferenceUtil = new PreferenceUtil();
                }
            }
        }
        return preferenceUtil;
    }

    public <T extends Serializable> boolean save(T entity, String key) {
        if (entity == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "T can't be null");
            return false;
        }
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        String preFileName = entity.getClass().getName();
        SharedPreferences sp = context.getSharedPreferences(preFileName, 0);
        SharedPreferences.Editor edit = sp.edit();
        String json = GsonUtils.ser(entity);
        edit.putString(key, json);
        return edit.commit();
    }

    public <T extends Serializable> List<T> findAll(String key, Class<T> tClass) {
        String preFileName = tClass.getName();
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(preFileName, 0);
        Map<String, String> values = (Map<String, String>) sp.getAll();
        List<T> results = new ArrayList<>();
        if (values == null || values.isEmpty()) {
            Log.e(PreferenceUtil.class.getSimpleName(), "not find " + key);
            return results;
        }
        Collection<String> collection = values.values();
        for (String json : collection) {
            results.add(GsonUtils.deser(json, tClass));
        }
        return results;
    }

    public <T extends Serializable> T find(String key, Class<T> tClass) {
        String preFileName = tClass.getName();
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(preFileName, 0);
        String json = sp.getString(key, null);
        if (json == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "not find " + key);
            return null;
        }
        return GsonUtils.deser(json, tClass);
    }

    public <T extends Serializable> boolean delete(String key, Class<T> tClass) {
        String preFileName = tClass.getName();
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(preFileName, 0);
        if (sp.contains(key)) {
            sp.edit().remove(key).apply();
        }
        return true;
    }

    public <T extends Serializable> boolean deleteAll(Class<T> tClass) {
        String preFileName = tClass.getName();
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(preFileName, 0);
        sp.edit().clear().apply();
        return true;
    }

    /**
     * 保存数据的方法，根据拿到数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     * @return
     */
    public boolean put(String key, Object object) {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
        return true;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对应的方法取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Object get(String key, Object defaultValue) {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return null;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        }
        Log.e(PreferenceUtil.class.getSimpleName(), "Nothing");
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
        return true;
    }

    /**
     * 清除所有数据
     *
     * @return
     */
    public boolean clear() {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
        return true;
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return false;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        if (context == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "context can't be null,please var");
            return null;
        }
        if (FILE_NAME == null) {
            Log.e(PreferenceUtil.class.getSimpleName(), "FILE_NAME can't be null,please var");
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        private static Method findApplyMethod() {
            Class clazz = SharedPreferences.Editor.class;
            try {
                return clazz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        private static void apply(SharedPreferences.Editor editor) {
            if (sApplyMethod != null) {
                try {
                    sApplyMethod.invoke(editor);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                editor.commit();
            }
        }
    }

}

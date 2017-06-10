
package com.aoliao.notebook.utils.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aoliao.notebook.AppController;
import com.aoliao.notebook.config.Config;


public class PreferenceData {


    /**
     * 保存文章和标题
     */
    public static void saveArticle(String title, String article) {
        setString(Config.preference.ARTICLE_TITLE, title);
        setString(Config.preference.ARTICLE, article);
    }

    /**
     * 获取标题和文章
     * @return
     */
    public static String[] getArticle() {
        return new String[]{getString(Config.preference.ARTICLE_TITLE, ""), getString(Config.preference.ARTICLE, "")};
    }

    private static SharedPreferences get() {
        return PreferenceManager.getDefaultSharedPreferences(AppController.getAppContext());
    }

    private static SharedPreferences.Editor getEditor() {
        return get().edit();
    }

    public static String getString(String key, final String defaultValue) {
        return get().getString(key, defaultValue);
    }

    public static void setString(final String key, final String value) {
        getEditor().putString(key, value).apply();
    }

    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return get().getBoolean(key, defaultValue);
    }

    public static boolean hasKey(final String key) {
        return get().contains(key);
    }

    public static void setBoolean(final String key, final boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void setInt(final String key, final int value) {
        getEditor().putInt(key, value).commit();
    }

    public static int getInt(final String key, final int defaultValue) {
        return get().getInt(key, defaultValue);
    }

    public static void setFloat(final String key, final float value) {
        getEditor().putFloat(key, value).apply();
    }

    public static float getFloat(final String key, final float defaultValue) {
        return get().getFloat(key, defaultValue);
    }

    public static void setLong(final String key, final long value) {
        getEditor().putLong(key, value).apply();
    }

    public static long getLong(final String key, final long defaultValue) {
        return get().getLong(key, defaultValue);
    }

    public static void clearPreference() {
        getEditor().clear().apply();
    }
}  
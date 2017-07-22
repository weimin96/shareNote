package com.aoliao.notebook;

import android.app.Application;
import android.content.Context;

/**
 * 用于提供全局变量
 */
public class AppController extends Application {
    private static AppController mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this.getApplicationContext();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mContext;
    }

}
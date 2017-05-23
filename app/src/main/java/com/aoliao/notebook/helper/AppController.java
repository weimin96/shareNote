package com.aoliao.notebook.helper;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.bmob.v3.Bmob;

/**
 * 用于提供全局变量,包括request对列
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue myRequestQueue;
    private static AppController mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this.getApplicationContext();
        Bmob.initialize(mContext,"3d9d9f910c51b02eea3d605178911aa5");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    //获取requestqueue
    public RequestQueue getMyRequestQueue() {
        if (myRequestQueue == null) {
            myRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return myRequestQueue;
    }

    //向requestqueue中添加自定义tag的request
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getMyRequestQueue().add(request);
    }

    //向requestqueue中添加带默认tag的request
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getMyRequestQueue().add(request);
    }

    //删除所有tag的request
    public void cancelPendingRequests(Object tag) {
        if (myRequestQueue != null) {
            myRequestQueue.cancelAll(tag);
        }
    }
    public static Context getAppContext() {
        return mContext;
    }

}
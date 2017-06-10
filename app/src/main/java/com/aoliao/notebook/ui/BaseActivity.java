package com.aoliao.notebook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aoliao.notebook.R;
import com.aoliao.notebook.broadcastreceiver.NetWorkBroadcastReceiver;
import com.aoliao.notebook.utils.LeakUtil;
import com.aoliao.notebook.utils.NetworkUtils;
import com.aoliao.notebook.xmvp.XBaseActivity;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 你的奥利奥 on 2017/1/25.
 */

public abstract class BaseActivity<T extends XBasePresenter> extends XBaseActivity<T> implements NetWorkBroadcastReceiver.NetEvevt{
    public static NetWorkBroadcastReceiver.NetEvevt evevt;

    //网络类型
    private int mNetType;

    //是否更新过用户信息
    private boolean updatedUser = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        evevt = this;
        isNetConnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeakUtil.fixInputMethodManagerLeak(this);
    }

    protected void initToolbar(Toolbar toolbar) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(R.string.app_name);//必要
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_action_mode_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化时判断网络是否可用
     */
    public boolean checkNetWork() {

        this.mNetType = NetworkUtils.checkNetWorkState(this);
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetWorkChange(int netMobile) {

        this.mNetType = netMobile;
        isNetConnect();
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (mNetType == NetworkUtils.NETWORK_WIFI) {
            return true;
        } else if (mNetType == NetworkUtils.NETWORK_MOBILE) {
            return true;
        } else if (mNetType == NetworkUtils.NETWORK_NONE) {
            return false;
        }
        return false;
    }
    public void setUpdatedUser(boolean updatedUser) {
        this.updatedUser = updatedUser;
    }

    //保存一些数据
    private static Map<String, Object> dataSave = new HashMap<>(2);

    /**
     * 获取数据
     * @param key
     * @param <D>
     * @return
     */
    public static <D> D getData(String key) {
        Object obj = dataSave.get(key);
        if (obj == null) {
            return null;
        } else {
            return (D) obj;
        }
    }
    /**
     * 保存数据
     * @param key
     * @param obj
     */
    public static void saveData(String key, Object obj) {
        dataSave.put(key, obj);
    }

    public static void clearData() {
        dataSave.clear();
    }


}

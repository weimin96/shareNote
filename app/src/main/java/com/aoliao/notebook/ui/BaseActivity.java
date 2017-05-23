package com.aoliao.notebook.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aoliao.notebook.R;
import com.aoliao.notebook.broadcastreceiver.NetWorkBroadcastReceiver;
import com.aoliao.notebook.utils.LeakUtil;
import com.aoliao.notebook.utils.NetworkUtils;
import com.aoliao.notebook.xmvp.XBaseActivity;
import com.aoliao.notebook.xmvp.XBasePresenter;


/**
 * Created by 你的奥利奥 on 2017/1/25.
 */

public abstract class BaseActivity<T extends XBasePresenter> extends XBaseActivity<T> implements NetWorkBroadcastReceiver.NetEvevt{
    public static NetWorkBroadcastReceiver.NetEvevt evevt;

    //网络类型
    private int mNetType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        evevt = this;
        isNetConnect();
    }

    private void initStatus() {
//        View status = ButterKnife.findById(this, R.id.status);
//        if (status != null) {
//            ActivityUtils.initSatus(status);
//        }
    }

    /**
     * 添加Fragment
     */
    protected void addFragment(android.support.v4.app.Fragment fragment, @IdRes int idRes) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        String tag = fragment.getClass().getSimpleName();
        android.support.v4.app.Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            manager.beginTransaction().add(idRes, fragment, tag).commit();
        }
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
        toolbar.setNavigationIcon(R.mipmap.ic_action_back);
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
}

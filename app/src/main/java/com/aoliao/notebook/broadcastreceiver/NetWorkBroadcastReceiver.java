package com.aoliao.notebook.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.aoliao.notebook.utils.NetworkUtils;

import static com.aoliao.notebook.ui.BaseActivity.evevt;

public class NetWorkBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt mEvevt = evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int mNetType = NetworkUtils.checkNetWorkState(context);
            // 接口回调，传递当前网络状态的类型
            mEvevt.onNetWorkChange(mNetType);
        }
    }


    // 自定义网络事件接口
    public interface NetEvevt {
        public void onNetWorkChange(int mNetType);
    }


}

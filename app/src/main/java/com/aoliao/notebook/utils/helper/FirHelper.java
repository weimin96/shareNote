package com.aoliao.notebook.utils.helper;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;




public class FirHelper {
    private BroadcastReceiver receiver;
    private Context context;
    public static FirHelper helper;

    private FirHelper() {
    }

    public static FirHelper getInstance() {
        if (helper == null) {
            helper = new FirHelper();
        }
        return helper;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public void destroy() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
        context = null;
        ProgressDialogUtil.destroy();
        helper = null;
    }

    public static class ProgressDialogUtil {
        private static ProgressDialog pd;

        public static void show(Context context) {
            // 创建进度对话框
            pd = new ProgressDialog(context);
            pd.setMax(100);
            // 设置对话框标题
            pd.setTitle("下载更新");
            // 设置对话框显示的内容
            pd.setMessage("进度...");
            // 设置对话框不能用取"消按"钮关闭
            pd.setCancelable(false);
            // 设置对话框的进度条风格
            // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // 设置对话框的进度条是否显示进度
            pd.setIndeterminate(false);
            pd.show();
        }

        public static void setProgress(int progress) {
            if (pd == null) {
                return;
            }
            pd.setProgress(progress);
        }

        public static void dismiss() {
            pd.dismiss();
            pd = null;
        }

        public static void destroy() {
            pd = null;
        }
    }

}

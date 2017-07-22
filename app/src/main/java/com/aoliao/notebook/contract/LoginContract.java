package com.aoliao.notebook.contract;


import com.aoliao.notebook.xmvp.XContract;

public interface LoginContract {
    //presenter要实现的业务逻辑方法
    interface Presenter extends XContract.Presenter {
        void requestLogin(String name, String password);
    }

    //view接口
    interface View extends XContract.View {
        void showDialog();

        void nameFormatError(String err);

        void passwordFormatError(String err);

        void callLoginSuccess();

        void callLoginFail(String failMassage);
    }
}

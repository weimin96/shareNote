package com.aoliao.notebook.contract;


import com.aoliao.notebook.xmvp.XContract;

public interface RegisterContract {
    interface Presenter extends XContract.Presenter{
        void requestRegister();
        void checkUsername(String username);
        void requestPhone(String phoneNumber);
        void checkPassword(String password);
        void checkNickname(String nickname);
        void checkPasswordConfirm(String passwordConfirm);
    }

    interface View extends XContract.View {
        void callRegisterSuccess(String username);
        void callRegisterFail(String err);
        void errUsername(String err);
        void errPassword(String err);
        void errPasswordConfirm(String err);
        void callPhoneSuccess();
        void callPhoneFail(String err);
        void errNickName(String err);
    }
}

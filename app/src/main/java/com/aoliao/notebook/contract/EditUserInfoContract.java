package com.aoliao.notebook.contract;

import com.aoliao.notebook.xmvp.XContract;

import java.util.Date;

public interface EditUserInfoContract {
    interface Presenter extends XContract.Presenter {
        void requestChangeHeadPic(String picPath);
        void requestChangeNickname(String nickname);
        void requestChangePhone(String phoneNumber);
        void requestChangeSign(String sign);
        void requestChangeCity(String city);
        void requestChangeSex(int sex);
        void requestBirthday(Date birthday);
        void requestChangeEmail(String email);
        void requestChangePassword(String oldPwd, String newPwd);
    }

    interface View extends XContract.View {
        void changeHeadPicSuccess();
        void changeHeadPicFail(String err);
        void changeNicknameSuccess();
        void changeNicknameFail(String err);
        void changePhoneSuccess();
        void changePhoneFail(String err);
        void changeSignSuccess();
        void changeSignFail(String err);
        void changeCitySuccess();
        void changeCityFail(String err);
        void changeSexSuccess();
        void changeSexFail(String err);
        void changeBirthdaySuccess();
        void changeBirthdayFail(String err);
        void changeEmailSuccess();
        void changeEmailFail(String err);
        void changePasswordSuccess();
        void changePasswordFail(String err);
    }
}

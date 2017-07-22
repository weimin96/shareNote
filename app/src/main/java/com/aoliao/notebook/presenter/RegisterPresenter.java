

package com.aoliao.notebook.presenter;


import com.aoliao.notebook.contract.RegisterContract;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.utils.LoginCheck;
import com.aoliao.notebook.utils.MD5Util;
import com.aoliao.notebook.xmvp.XBasePresenter;



public class RegisterPresenter extends XBasePresenter<RegisterContract.View> implements RegisterContract.Presenter{
    private String passwordSave;
    private User user = null;
    //密码确认
    private boolean pwdConfirm = false;
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();
        user = new User();
        user.setSex(Config.SEX_SECRET);
    }

    @Override
    public void end() {
        super.end();
        passwordSave = null;
        user = null;
    }

    @Override
    public void requestRegister() {
        if (
//                user.getEmail() != null &&
                user.getNickname() != null &&
                user.getUsername() != null &&
//                user.getMobilePhoneNumber() != null &&
//                user.getEmail() != null &&
                passwordSave != null &&
                pwdConfirm) {
//            user.setNickname(AppController.getAppContext().getString(R.string.secret_man));
            NetRequest.Instance().userRegister(user, new NetRequest.RequestListener<User>() {
                @Override
                public void success(User userGet) {
                    view.callRegisterSuccess(user.getUsername());
                }

                @Override
                public void error(String err) {
                    view.callRegisterFail(err);
                }
            });
        } else {
            view.callRegisterFail("请检查您的输入信息！");
        }
    }

    @Override
    public void requestPhone(String phoneNumber) {
        NetRequest.Instance().updateInfo(NetRequest.UpdateType.PHONE,
                phoneNumber,
                new NetRequest.RequestListener<User>() {
                    @Override
                    public void success(User user){
                        view.callPhoneSuccess();
                    }

                    @Override
                    public void error(String err) {
                        view.callPhoneFail(err);
                    }
                });
    }

    @Override
    public void checkUsername(String username) {
        String value = LoginCheck.checkAccount(username);
        if (value == null) {
            user.setUsername(username);
            return;
        }
        view.errUsername(value);
    }

    @Override
    public void checkPassword(String password) {
        String value = LoginCheck.checkPassword(password);
        if (value == null) {
            passwordSave = password;
            user.setPassword(MD5Util.getMD5(password));
            return;
        }
        view.errPassword(value);
    }

    @Override
    public void checkPasswordConfirm(String passwordConfirm) {
        String value = LoginCheck.checkConfirmPassword(passwordSave, passwordConfirm);
        if (value == null) {
            pwdConfirm = true;
            return;
        }
        pwdConfirm = false;
        view.errPasswordConfirm(value);
    }



    @Override
    public void checkNickname(String nickname) {
        String value = LoginCheck.checkNickname(nickname);
        if (value == null) {
            user.setNickname(nickname);
            return;
        }
        view.errNickName(value);
    }

}


package com.aoliao.notebook.presenter;


import com.aoliao.notebook.contract.LoginContract;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.utils.LoginCheck;
import com.aoliao.notebook.xmvp.XBasePresenter;





public class LoginPresenter extends XBasePresenter<LoginContract.View> implements LoginContract.Presenter {

    //获取view接口角色
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    //实现业务逻辑
    @Override
    public void requestLogin(final String name, final String password) {
        String nameErr = LoginCheck.checkAccount(name);
        String passwordErr = LoginCheck.checkPassword(password);
        if (nameErr != null) {
            view.nameFormatError(nameErr);
            return;
        }

        if (passwordErr != null) {
            view.passwordFormatError(passwordErr);
            return;
        }

        NetRequest.Instance().login(name, password, new NetRequest.RequestListener<User>() {
            @Override
            public void success(User user) {
                view.callLoginSuccess();
            }

            @Override
            public void error(String err) {
                view.callLoginFail(err);
            }
        });
    }

}

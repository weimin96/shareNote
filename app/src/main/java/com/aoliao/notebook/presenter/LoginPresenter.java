/*
 * Copyright 2016 XuJiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aoliao.notebook.presenter;

import android.app.Fragment;
import android.widget.Toast;

import com.aoliao.notebook.contract.LoginContract;
import com.aoliao.notebook.helper.SQLiteManager;
import com.aoliao.notebook.helper.SessionManager;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.model.entity.User;
import com.aoliao.notebook.utils.LoginCheck;
import com.aoliao.notebook.utils.ToastUtil;
import com.aoliao.notebook.xmvp.XBasePresenter;


import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * Created by jiana on 16-11-4.
 */

public class LoginPresenter extends XBasePresenter<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

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

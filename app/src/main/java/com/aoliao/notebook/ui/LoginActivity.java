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

package com.aoliao.notebook.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.LoginContract;
import com.aoliao.notebook.factory.FragmentFactory;
import com.aoliao.notebook.helper.SQLiteManager;
import com.aoliao.notebook.presenter.LoginPresenter;
import com.aoliao.notebook.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.status)
    RelativeLayout status;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.toolBar)
    Toolbar toolbar;

    private SweetAlertDialog dialog;
    private SQLiteManager sqLiteManager;

    /**
     * 当注册后来后设置这个值
     */
//    private String registerUsername = null;

//    public void setRegisterUsername(String name) {
//        registerUsername = name;
//    }

//    @Override
//    public void introAnimate() {
//        super.introAnimate();
//        if (registerUsername == null) {
//            return;
//        }
//        username.setText(registerUsername);
//    }

//    public static LoginActivity newInstance() {
//        return new LoginActivity();
//    }

//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_login;
//    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();

    }

    private void initView(){
//        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sqLiteManager = new SQLiteManager(getApplicationContext());//数据库
        //如果用户已经登陆,那么跳转到用户页面
//        if (sessionManager.isLoggedIn()) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
        initToolbar(toolbar);
//        if (registerUsername == null) {
//            return;
//        }
//        username.setText(registerUsername);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("登录");
    }

    @Override
    public void showDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("正在登录");
        dialog.showCancelButton(false);
//        dialog.showConfirmButton(false);
        dialog.show();
    }

    @OnClick({R.id.btnLogin, R.id.btnRegistered})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnLogin:
                    onLogging(true);
                    usernameWrapper.setErrorEnabled(false);
                    usernameWrapper.setErrorEnabled(false);
                    presenter.requestLogin(username.getText().toString().trim(), password.getText().toString().trim());
                    break;
                case R.id.btnRegistered:
                    Intent intent=new Intent(this,MobSMSCodeActivity.class);
                    startActivity(intent);
//                MainActivity.startFragment(Config.fragment.REGISTER);
//                getFragmentManager().beginTransaction().add(R.id.container, new RegisterActivity()).commit();

                    break;
            }
    }

    @Override
    public void nameFormatError(String err) {
        onLogging(false);
        usernameWrapper.setError(err);
    }

    @Override
    public void passwordFormatError(String err) {
        onLogging(false);
        passwordWrapper.setError(err);
    }

    @Override
    public void callLoginSuccess() {
        if (dialog != null) {
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText(getString(R.string.login_success));
            onLogging(false);
        } else {
            ToastUtil.getInstance().showLongT(getString(R.string.login_success));
        }
        FragmentFactory.updatedUser();

        startActivity(new Intent(this,MainActivity.class));
        finish();
//        setDeleted(true);
//        MainActivity.startFragment(Config.fragment.USER_INFO);
    }

    @Override
    public void callLoginFail(final String failMassage) {
        if (dialog != null) {
            onLogging(false);
        }
        ToastUtil.getInstance().showLongT(failMassage);
    }


    /**
     * 是否正在登录中
     *
     * @param isLogging
     */
    private void onLogging(boolean isLogging) {
        if (isLogging) {
            btnLogin.setClickable(false);
            showDialog();
        } else {
            btnLogin.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnLogin.setClickable(true);
                    dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    dialog.dismissWithAnimation();
                }
            }, 1000);
        }
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        LoginSDKHelper.getIntance().onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (dialog != null) {
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//            dialog = null;
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
    }
}

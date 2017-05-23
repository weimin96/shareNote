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

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.RegisterContract;
import com.aoliao.notebook.presenter.RegisterPresenter;
import com.aoliao.notebook.utils.LogUtil;
import com.aoliao.notebook.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by jiana on 16-11-5.
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.passwordConfirmWrapper)
    TextInputLayout passwordConfirmWrapper;
    @BindView(R.id.btnRegistered)
    Button btnRegistered;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    private SweetAlertDialog dialog;

    @OnClick(R.id.btnRegistered)
    void onClick(View view) {
        onRegister(true);
        presenter.requestRegister();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnClose)
    void onClickClose(View view) {
        startActivity(new Intent(this,LoginActivity.class));

//        clickBack();
    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();
//        radioGroup.check(R.id.rbSexSecret);
    }

    private void initView(){
        initToolbar(toolbar);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("注册");
    }

    private void showDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("正在注册");
//        dialog.showConfirmButton(false);
        dialog.show();
    }

    @Override
    protected void onListener() {
        super.onListener();

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rbMan:
//                        presenter.sex(C.SEX_MAN);
//                        break;
//                    case R.id.rbWoman:
//                        presenter.sex(C.SEX_WOMAN);
//                        break;
//                    case R.id.rbSexSecret:
//                        presenter.sex(C.SEX_SECRET);
//                        break;
//                }
//            }
//        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_register;
    }

    @Override
    public void callRegisterSuccess(String username) {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        onRegister(false);
        ToastUtil.getInstance().showLongT("注册成功！");
//        Intent intent=new Intent(this,LoginActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void callRegisterFail(String err) {
        onRegister(false);
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void errUsername(String err) {
        usernameWrapper.setError(err);
    }

    @Override
    public void errNickName(String err) {

    }

    @Override
    public void errPhone(String err) {
//        phoneWrapper.setError(err);
    }

    @Override
    public void errEmail(String err) {
//        emailWrapper.setError(err);
    }

    @Override
    public void errPassword(String err) {
        passwordWrapper.setError(err);
    }

    @Override
    public void errPasswordConfirm(String err) {
        passwordConfirmWrapper.setError(err);
    }


    @OnTextChanged(R.id.username)
    public void usernameChanged(CharSequence text) {
        usernameWrapper.setErrorEnabled(false);
        presenter.checkUsername(text.toString());
    }

//    @OnTextChanged(R.id.nickname)
//    public void nicknameChanged(CharSequence text) {
////        nicknameWrapper.setErrorEnabled(false);
//        presenter.checkNickname(text.toString());
//    }

//    @OnTextChanged(R.id.phone)
//    public void phoneChanged(CharSequence text) {
////        phoneWrapper.setErrorEnabled(false);
//        presenter.checkPhone(text.toString());
//    }

//    @OnTextChanged(R.id.email)
//    public void emailChanged(CharSequence text) {
////        emailWrapper.setErrorEnabled(false);
//        presenter.checkEmail(text.toString());
//    }

    @OnTextChanged(R.id.password)
    public void passwordChanged(CharSequence text) {
        passwordWrapper.setErrorEnabled(false);
        presenter.checkPassword(text.toString());
    }

    @OnTextChanged(R.id.passwordConfirm)
    public void passwordConfirmChanged(CharSequence text) {
        passwordConfirmWrapper.setErrorEnabled(false);
        presenter.checkPasswordConfirm(text.toString());
    }

//    @Override
//    public boolean clickBack() {
//        if (super.clickBack()) {
//            return true;
//        }
//        setDeleted(true);
//        MainActivity.startFragment(C.fragment.LOGIN);
//        return true;
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



    /**
     * 是否正在注册中
     */
    private void onRegister(boolean isRegister) {
        LogUtil.e3("isLogging = " + isRegister);
        if (isRegister) {
            btnRegistered.setClickable(false);
            showDialog();
//            btnRegistered.setVisibility(View.GONE);
//            googleProgressBar.setVisibility(View.VISIBLE);
        } else {
//            googleProgressBar.setVisibility(View.GONE);
//            btnRegistered.setVisibility(View.VISIBLE);
            btnRegistered.setClickable(true);
            dialog.dismissWithAnimation();
        }
    }
}

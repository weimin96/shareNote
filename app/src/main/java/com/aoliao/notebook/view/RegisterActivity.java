
package com.aoliao.notebook.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.RegisterContract;
import com.aoliao.notebook.presenter.RegisterPresenter;
import com.aoliao.notebook.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;
    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;
    @BindView(R.id.nicknameWrapper)
    TextInputLayout nicknameWrapper;
    @BindView(R.id.passwordConfirmWrapper)
    TextInputLayout passwordConfirmWrapper;
    @BindView(R.id.btnRegistered)
    Button btnRegistered;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    private SweetAlertDialog dialog;
    private String phoneNum;
    private Bundle bd;

    @OnClick(R.id.btnRegistered)
    void onClick(View view) {
        onRegister(true);
        presenter.requestRegister();
    }

    @OnClick(R.id.btnClose)
    void onClickClose(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();
        Intent intent = getIntent();
        bd = intent.getExtras();
        phoneNum = bd.getString("phone");
    }

    private void initView() {
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
        dialog.show();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_register;
    }

    @Override
    public void callRegisterSuccess(String username) {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        onRegister(false);
        presenter.requestPhone(phoneNum);
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
    public void errPassword(String err) {
        passwordWrapper.setError(err);
    }

    @Override
    public void errPasswordConfirm(String err) {
        passwordConfirmWrapper.setError(err);
    }

    @Override
    public void callPhoneSuccess() {

    }

    @Override
    public void callPhoneFail(String err) {

    }

    @Override
    public void errNickName(String err) {
        nicknameWrapper.setError(err);
    }

    @OnTextChanged(R.id.username)
    public void usernameChanged(CharSequence text) {
        usernameWrapper.setErrorEnabled(false);
        presenter.checkUsername(text.toString());
    }

    @OnTextChanged(R.id.nickname)
    public void nicknameChanged(CharSequence text) {
        nicknameWrapper.setErrorEnabled(false);
        presenter.checkNickname(text.toString());
    }


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


    /**
     * 是否正在注册中
     */
    private void onRegister(boolean isRegister) {
        if (isRegister) {
            btnRegistered.setClickable(false);
            showDialog();
        } else {
            btnRegistered.setClickable(true);
            dialog.dismissWithAnimation();
        }
    }
}

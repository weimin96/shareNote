
package com.aoliao.notebook.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.LoginContract;
import com.aoliao.notebook.factory.FragmentFactory;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.LoginPresenter;
import com.aoliao.notebook.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
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

    @Override
    protected void onInit() {
        super.onInit();
        initView();
    }


    private void initView() {
        //如果用户已经登陆,那么跳转到用户页面
        if (BmobUser.getCurrentUser(User.class) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        initToolbar(toolbar);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("登录");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.TAG_EXIT, true);
        startActivity(intent);
    }

    @Override
    public void showDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("正在登录");
        dialog.showCancelButton(false);
        dialog.show();
    }

    @OnClick({R.id.btnLogin, R.id.btnRegistered})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                usernameWrapper.setErrorEnabled(false);
                usernameWrapper.setErrorEnabled(false);
                presenter.requestLogin(username.getText().toString().trim(), password.getText().toString().trim());
                break;
            case R.id.btnRegistered:
                Intent intent = new Intent(this, MobSMSCodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void nameFormatError(String err) {
        usernameWrapper.setError(err);
    }

    @Override
    public void passwordFormatError(String err) {
        passwordWrapper.setError(err);
    }

    @Override
    public void callLoginSuccess() {
        if (dialog != null) {
            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText(getString(R.string.login_success));
        } else {
            ToastUtil.getInstance().showLongT(getString(R.string.login_success));
        }
        FragmentFactory.updatedUser();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void callLoginFail(final String failMassage) {
        if (dialog != null) {
        }
        ToastUtil.getInstance().showLongT(failMassage);
    }

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

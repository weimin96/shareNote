package com.aoliao.notebook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.UserInfoContract;
import com.aoliao.notebook.contract.UserListContract;
import com.aoliao.notebook.factory.FragmentFactory;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.UserInfoPresenter;
import com.aoliao.notebook.ui.CollectActivity;
import com.aoliao.notebook.ui.EditUserInfoActivity;
import com.aoliao.notebook.ui.LoginActivity;
import com.aoliao.notebook.ui.ReleaseActivity;
import com.aoliao.notebook.ui.UserListActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoFragment extends BaseMainFragment<UserInfoPresenter> implements UserInfoContract.View {
    public static boolean SelfSwitch=true;
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.imgUserInfoBg)
    ImageView imgUserInfoBg;
    @BindView(R.id.tvNickname)
    TextView tvNickname;
    @BindView(R.id.tvSign)
    TextView tvSign;
    @BindView(R.id.tvFans)
    TextView tvFans;
    @BindView(R.id.tvFocus)
    TextView tvFocus;
    @BindView(R.id.tvCollect)
    TextView tvCollect;
    @BindView(R.id.myDynamic)
    TextView myDynamic;
    @BindView(R.id.exit_login)
    TextView exit_login;
    @BindView(R.id.headLayout)
    RelativeLayout headLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void onInit() {
        super.onInit();
        boldText();
        presenter.requestUserInfo();
    }

    /**
     * 加粗字体
     */
    private void boldText() {
        tvNickname.getPaint().setFakeBoldText(true);
    }

    @OnClick({R.id.headLayout, R.id.btnFans, R.id.btnFocus, R.id.btnCollect,R.id.exit_login,R.id.publicDynamic,R.id.userMessage,R.id.myDynamic})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.headLayout:
                startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
                break;
            case R.id.btnFans:
                goUserList(UserListContract.FANS);
                break;
            case R.id.btnFocus:
                goUserList(UserListContract.FOCUS);
                break;
            case R.id.btnCollect:
                BaseMainFragment.saveData(User.class.getSimpleName(), presenter.getUser());
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.publicDynamic:
                BaseMainFragment.saveData(User.class.getSimpleName(), presenter.getUser());
                startActivity(new Intent(getActivity(), ReleaseActivity.class));
                break;
            case R.id.myDynamic:
//                startActivity(new Intent(getActivity(), NoteActivity.class));
                break;
            case R.id.userMessage:
                startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
                break;
            case R.id.exit_login:
                presenter.requestExitLogin();
                break;
        }
    }

    /**
     * 跳转到用户列表
     */
    private void goUserList(int pageType) {
        saveData(UserListContract.class.getSimpleName(), pageType);
        saveData(User.class.getSimpleName(), presenter.getUser());
        startActivity(new Intent(getActivity(),UserListActivity.class));
    }


    @Override
    protected void updateUserInfo() {
        super.updateUserInfo();
        presenter.requestUserInfo();
    }

    @Override
    public void displayUser(User user) {
        tvNickname.setText("@" +user.getNickname());
        tvSign.setText(user.getSign());
        presenter.requestDisplayHeadPic(imgHead, user.getHeadPic());
        presenter.requestDisplayUserInfoBg(imgUserInfoBg, user.getHeadPic());
    }

    @Override
    public void exitLoginSuccess() {
        FragmentFactory.updatedUser();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void showFansNum(String num) {
        tvFans.setText(num);
    }

    @Override
    public void showFocusNum(String num) {
        tvFocus.setText(num);
    }

    @Override
    public void showCollectNum(String num) {
        tvCollect.setText(num);
    }

}

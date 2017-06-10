package com.aoliao.notebook.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.UserInfoContract;
import com.aoliao.notebook.contract.UserListContract;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.UserInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 你的奥利奥 on 2017/6/6.
 */

//临时添加
public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View {
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
    @BindView(R.id.toolBar)
    Toolbar toobar;

    @Override
    protected void onInit() {
        super.onInit();
        boldText();
        initToolbar(toobar);
        presenter.requestUserInfo();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("用户查看");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        SelfSwitch=true;
    }


    /**
     * 加粗字体
     */
    private void boldText() {
        tvNickname.getPaint().setFakeBoldText(true);
    }

    @OnClick({R.id.btnFans, R.id.btnFocus, R.id.btnCollect, R.id.publicDynamic,R.id.userMessage})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFans:
                goUserList(UserListContract.FANS);
                break;
            case R.id.btnFocus:
                goUserList(UserListContract.FOCUS);
                break;
            case R.id.btnCollect:
                BaseActivity.saveData(User.class.getSimpleName(), presenter.getUser());
                startActivity(new Intent(getApplicationContext(), CollectActivity.class));
                break;
            case R.id.publicDynamic:
                BaseActivity.saveData(User.class.getSimpleName(), presenter.getUser());
                startActivity(new Intent(getApplicationContext(), ReleaseActivity.class));
                break;
            case R.id.userMessage:
                BaseActivity.saveData(User.class.getSimpleName(), presenter.getUser());
                startActivity(new Intent(UserInfoActivity.this, ViewUserInfoActivity.class));
                break;
        }
    }

    /**
     * 跳转到用户列表
     */
    private void goUserList(int pageType) {
        saveData(UserListContract.class.getSimpleName(), pageType);
        saveData(User.class.getSimpleName(), presenter.getUser());
        startActivity(new Intent(getApplicationContext(), UserListActivity.class));
    }

    @Override
    public void displayUser(User user) {
        tvNickname.setText("@" + user.getNickname());
        tvSign.setText(user.getSign());
        presenter.requestDisplayHeadPic(imgHead, user.getHeadPic());
        presenter.requestDisplayUserInfoBg(imgUserInfoBg, user.getHeadPic());
    }

    @Override
    public void exitLoginSuccess() {
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


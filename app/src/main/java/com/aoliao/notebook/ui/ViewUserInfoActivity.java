package com.aoliao.notebook.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.UserInfoAdapter;
import com.aoliao.notebook.presenter.EditUserInfoPresenter;
import com.aoliao.notebook.utils.entity.User;
import com.flipboard.bottomsheet.BottomSheetLayout;

import butterknife.BindView;

/**
 * Created by 你的奥利奥 on 2017/6/10.
 */

public class ViewUserInfoActivity extends BaseActivity<EditUserInfoPresenter>{
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.rvUserInfo)
    RecyclerView rvUserInfo;
    @BindView(R.id.bottomSheet)
    BottomSheetLayout bottomSheet;
    private static final int REQUEST_CODE_PICK_CITY = 0;
    @Override
    protected int getContentId() {
        return  R.layout.activity_edit_user_info;
    }

    @Override
    protected void onInit() {
        super.onInit();
        initToolbar(toolbar);
        initList();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("查看用户信息");
    }

    /**
     * 初始化用户信息列表
     */
    private void initList() {
        rvUserInfo.setLayoutManager(new LinearLayoutManager(this));
        User user=BaseActivity.getData(User.class.getSimpleName());
        UserInfoAdapter adapter = new UserInfoAdapter(user);
        rvUserInfo.setAdapter(adapter);
    }
}

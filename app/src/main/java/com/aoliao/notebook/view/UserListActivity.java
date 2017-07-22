package com.aoliao.notebook.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.UserListAdapter;
import com.aoliao.notebook.contract.UserListContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.UserListPresenter;
import com.aoliao.notebook.utils.ToastUtil;


import java.util.List;

import butterknife.BindView;

/**
 * Created by 你的奥利奥 on 2017/6/4.
 */

public class UserListActivity  extends BaseActivity<UserListPresenter> implements UserListContract.View {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.rvUserList)
    RecyclerView rvUserList;


    @Override
    protected void onInit() {
        super.onInit();
        initToolbar(toolbar);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        Integer value = BaseMainFragment.getData(UserListContract.class.getSimpleName());
        if (value == null) return;
        if (value == UserListContract.FANS) {
            toolbar.setTitle(R.string.fans);
        } else if (value == UserListContract.FOCUS) {
            toolbar.setTitle(R.string.follow);
        }

    }

    @Override
    protected int getContentId() {
        return R.layout.activity_user_list;
    }

    @Override
    public void requestUserListFail(String err) {
        ToastUtil.getInstance().showShortT(err);
    }

    @Override
    public void showList(List<User> users) {
        rvUserList.setAdapter(new UserListAdapter(users));
    }
}

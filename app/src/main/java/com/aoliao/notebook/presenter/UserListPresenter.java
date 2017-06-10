
package com.aoliao.notebook.presenter;

import com.aoliao.notebook.contract.UserListContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.utils.NetRequest;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.List;


/**
 * 用户列表
 */

public class UserListPresenter extends XBasePresenter<UserListContract.View> implements UserListContract.Presenter {

    private User nowUser;
    /**
     * 该页面是什么
     */
    private int pageType = 0;

    public UserListPresenter(UserListContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        judgmentPage();
        requestUserList();
    }

    @Override
    public void requestUserList() {
        if (pageType == UserListContract.FANS)
            NetRequest.Instance().requestFans(nowUser, new NetRequest.RequestListener<List<User>>() {
                @Override
                public void success(List<User> users) {
                    view.showList(users);
                }

                @Override
                public void error(String err) {
                    view.requestUserListFail(err);
                }
            });

        if (pageType == UserListContract.FOCUS)
            NetRequest.Instance().requestFocus(nowUser, new NetRequest.RequestListener<List<User>>() {
                @Override
                public void success(List<User> users) {
                    view.showList(users);
                }

                @Override
                public void error(String err) {
                    view.requestUserListFail(err);
                }
            });
    }

    @Override
    public void judgmentPage() {
        Integer value = BaseMainFragment.getData(UserListContract.class.getSimpleName());
        if (value == null) return;
        pageType = value;
        nowUser = BaseMainFragment.getData(User.class.getSimpleName());
        BaseMainFragment.clearData();
    }

    @Override
    public void end() {
        super.end();
        nowUser = null;
    }
}

package com.aoliao.notebook.presenter;


import com.aoliao.notebook.contract.ReleaseContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.utils.NetRequest;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.ui.BaseActivity;
import com.aoliao.notebook.ui.BaseRefreshActivity;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.List;

/**
 * 收藏
 */

public class ReleasePresenter extends XBasePresenter<ReleaseContract.View> implements ReleaseContract.Presenter{
    private User user;

    public ReleasePresenter(ReleaseContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        if (BaseMainFragment.getData(User.class.getSimpleName())!=null){
            user = BaseMainFragment.getData(User.class.getSimpleName());
        }else if (BaseActivity.getData(User.class.getSimpleName())!=null){
            user = BaseActivity.getData(User.class.getSimpleName());
        }
        requestUpdateListData();
    }

    @Override
    public void requestLoadListData(int nowSize) {
        NetRequest.Instance().pullReleasePost(user.getObjectId(),nowSize, BaseRefreshActivity.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                if (posts == null || posts.size() == 0) {
                    view.loadListDateOver();
                } else {
                    view.loadListDataSuccess(posts);
                }
            }

            @Override
            public void error(String err) {
                view.loadListDataFail(err);
            }
        });
    }

    @Override
    public void requestUpdateListData() {
        NetRequest.Instance().pullReleasePost(user.getObjectId(),0, BaseRefreshActivity.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                view.updateListSuccess(posts);
            }

            @Override
            public void error(String err) {
                view.loadListDataFail(err);
            }
        });

    }

    @Override
    public void requestDeletePost(Post post) {
        NetRequest.Instance().deleteArticle(post, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.deletePostSuccess();
            }

            @Override
            public void error(String err) {
                view.deletePostFail(err);
            }
        });
    }
}

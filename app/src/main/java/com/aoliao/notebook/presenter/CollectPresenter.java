package com.aoliao.notebook.presenter;


import com.aoliao.notebook.contract.CollectContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.data.DataFiller;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.view.BaseActivity;
import com.aoliao.notebook.view.BaseRefreshActivity;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.List;

/**
 * 收藏
 */

public class CollectPresenter extends XBasePresenter<CollectContract.View> implements CollectContract.Presenter{

    private User user;

    public CollectPresenter(CollectContract.View view) {
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
//        if (isMe()) {
//            view.showTitle(R.string.my_collect);
//        } else {
//            view.showTitle(R.string.collect);
//        }
        requestUpdateListData();
    }

    @Override
    public void requestLoadListData(int nowSize) {
        NetRequest.Instance().pullCollectPost(user.getObjectId(), nowSize, BaseRefreshActivity.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
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
        NetRequest.Instance().pullCollectPost(user.getObjectId(), 0, BaseRefreshActivity.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
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
    public void end() {
        super.end();
        user = null;
    }

    @Override
    public boolean isMe() {
        return DataFiller.getLocalUser().getObjectId().equals(user.getObjectId());
    }

    @Override
    public void requestLike(Post post) {
        NetRequest.Instance().likeComment(post, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.likePostSuccess(s);
            }

            @Override
            public void error(String err) {
                view.likePostFail(err);
            }
        });
    }
}

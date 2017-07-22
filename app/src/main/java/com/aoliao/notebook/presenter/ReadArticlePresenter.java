
package com.aoliao.notebook.presenter;


import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.ReadActicleContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.AppController;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.view.BaseActivity;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.List;



public class ReadArticlePresenter extends XBasePresenter<ReadActicleContract.View> implements ReadActicleContract.Presenter {
    private Post post;
    private Post post_RC;


    public ReadArticlePresenter(ReadActicleContract.View view) {
        super(view);
    }

    @Override
    public void end() {
        super.end();
        post = null;
        post_RC=null;
    }

    @Override
    public void requestCommentsData(String postId) {
        NetRequest.Instance().getPostComment(postId, new NetRequest.RequestListener<List<Comment>>() {

            @Override
            public void success(List<Comment> comments) {
                requestCommentsReply(comments);
            }

            @Override
            public void error(String err) {
            }

        });
    }

    @Override
    public void requestCommentsReply(final List<Comment> comments) {
        NetRequest.Instance().getCommentReply(comments, new NetRequest.RequestListener<List<Reply>>() {

            @Override
            public void success(List<Reply> lists) {
                view.showComment(comments, lists);
            }

            @Override
            public void error(String err) {
            }
        });
    }

    @Override
    public void requestPostData() {
        if (BaseMainFragment.getData(Config.data.KEY_POST)!=null){
            post=BaseMainFragment.getData(Config.data.KEY_POST);
        }else if (BaseActivity.getData(Config.data.KEY_POST)!=null){
            post = BaseActivity.getData(Config.data.KEY_POST);
        }
        if (post != null) {
            view.showArticle(post);
            requestCommentsData(post.getObjectId());
            BaseMainFragment.clearData();
            BaseActivity.clearData();
        }
    }

    @Override
    public void requestLike(Post post) {
        NetRequest.Instance().likeComment(post, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.likePostSuccess();
            }

            @Override
            public void error(String err) {
                view.likePostFail(err);
            }
        });
    }

    @Override
    public void requestFollow(User user) {
        NetRequest.Instance().followUser(user, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.followUserSuccess();
            }

            @Override
            public void error(String err) {
                view.followUserFail(err);
            }
        });
    }

    @Override
    public void addComment(String comment) {
        if (post == null) {
            view.addCommentFail(AppController.getAppContext().getString(R.string.current_not_article));
        }
        NetRequest.Instance().addComment(post, comment, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.addCommentSuccess();
                requestCommentsData(post.getObjectId());
            }

            @Override
            public void error(String err) {
                view.addCommentFail(err);
            }
        });
    }

    @Override
    public void addReply(User replyUser, Comment comment, String content) {
        NetRequest.Instance().replyComment(replyUser, comment, content, new NetRequest.RequestListener() {
            @Override
            public void success(Object o) {
                requestCommentsData(post.getObjectId());
                view.replyCommentSuccess();
            }

            @Override
            public void error(String err) {
                view.replyCommentFail(err);
            }
        });
    }
}

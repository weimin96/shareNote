package com.aoliao.notebook.contract;

import com.aoliao.notebook.utils.entity.Post;

public interface CollectContract {
    interface Presenter extends RefreshContract.Presenter {
        boolean isMe();
        /**
         * 请求喜欢文章
         */
        void requestLike(Post post);



    }

    interface View extends RefreshContract.View<Post> {
        /**
         * 喜欢文章成功
         */
        void likePostSuccess(String s);
        void likePostFail(String err);
    }
}

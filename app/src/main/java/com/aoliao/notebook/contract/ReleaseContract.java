package com.aoliao.notebook.contract;


import com.aoliao.notebook.utils.entity.Post;

/**
 * 发布
 */

public interface ReleaseContract {
    interface Presenter extends RefreshContract.Presenter {
        void requestDeletePost(Post post);
    }

    interface View extends RefreshContract.View<Post> {
        void deletePostSuccess();
        void deletePostFail(String err);
    }
}

package com.aoliao.notebook.contract;

import android.content.Context;
import android.widget.ImageView;

import com.aoliao.notebook.utils.entity.MainTag;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XContract;

import java.util.List;

public interface MainContract {
    interface Presenter extends XContract.Presenter {
        boolean checkLocalUser();
        void requestUserInfo();
        void requestDisplayHeadPic(ImageView imgHead, String url);
        void requestDisplayUserInfoBg(ImageView imgUserInfoBg, String url);
        User getUser();
    }

    interface View extends XContract.View {
        void displayUser(User user);
    }

    interface MainFragPersenter extends RefreshContract.Presenter {
        List<MainTag> getTags();

        /**
         * 请求加载广告数据
         */
        void requestBannerData();

        /**
         * 请求打开链接
         */
        void requestOpenBannerLink(Context context, int position);

        void requestLoadHead(ImageView head);

        /**
         * 请求喜欢文章
         */
        void requestLike(Post post);
        /**
         * 请求跟随
         */
        void requestFollow(User user);
    }

    interface MainFragView extends RefreshContract.View<Post>,XContract.View {
        void contentLayoutToTop();
        void contentLayoutToTopListener(boolean start);
        void contentLayoutToDown();
        void contentLayoutToDownListener(boolean start);

        void pullBannerDataSuccess(List<String> titles, List<String> images);

        void pullBannerDataFail(String err);

        /**
         * 喜欢文章成功
         */
        void likePostSuccess(String s);
        void likePostFail(String err);

        /**
         * 跟随某人成功
         */
        void followUserSuccess(String s);

        /**
         * 跟随某人失败
         * @param err
         */
        void followUserFail(String err);

    }
}

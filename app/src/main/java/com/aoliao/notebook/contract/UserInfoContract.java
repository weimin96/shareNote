package com.aoliao.notebook.contract;

import android.content.Context;
import android.widget.ImageView;

import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XContract;

public interface UserInfoContract {

    interface Presenter extends XContract.Presenter {
        void requestExitLogin();
        void requestUserInfo();
        void requestEdit(Context context);
        void requestDisplayHeadPic(ImageView imgHead, String url);
        void requestDisplayUserInfoBg(ImageView imgUserInfoBg, String url);
        void requestBack();

        void requestFansNum(User user);
        void requestFocusNum(User user);
        void requestCollectNum(User user);
    }

    interface View extends XContract.View {
        void displayUser(User user);
        void exitLoginSuccess();

        /**
         * 显示粉丝数量
         * @param num
         */
        void showFansNum(String num);

        /**
         * 显示关注当前用户的人
         * @param num
         */
        void showFocusNum(String num);

        /**
         * 显示喜欢的文章数
         * @param num
         */
        void showCollectNum(String num);
    }

}

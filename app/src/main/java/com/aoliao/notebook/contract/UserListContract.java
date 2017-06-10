
package com.aoliao.notebook.contract;


import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XContract;

import java.util.List;

/**
 *
 * 用户列表
 */

public interface UserListContract {

    /**
     * 粉丝列表
     */
    int FANS = 124;

    /**
     * 关注列表
     */
    int FOCUS = 125;


    interface Presenter extends XContract.Presenter {
        /**
         * 请求用户列表
         */
        void requestUserList();

        void judgmentPage();
    }

    interface View extends XContract.View {

        /**
         * 显示错误信息
         * @param err
         */
        void requestUserListFail(String err);

        /**
         * 显示用户列表
         * @param users
         */
        void showList(List<User> users);
    }
}

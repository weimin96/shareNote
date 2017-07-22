

package com.aoliao.notebook.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.aoliao.notebook.contract.UserInfoContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.fragment.UserInfoFragment;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.data.DataFiller;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.view.BaseActivity;
import com.aoliao.notebook.view.UserInfoActivity;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.aoliao.notebook.xmvp.XBasePresenter;


public class UserInfoPresenter extends XBasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {

    public UserInfoPresenter(UserInfoContract.View view) {
        super(view);
    }
    private User user;

    @Override
    public void requestExitLogin() {
        NetRequest.Instance().exitLogin();
        view.exitLoginSuccess();
    }


    @Override
    public void requestUserInfo() {
        if (UserInfoActivity.SelfSwitch) {
            user = DataFiller.getLocalUser();
            view.displayUser(user);
        } else {
            if (BaseMainFragment.getData(Config.data.KEY_USER)!=null) {
                user = BaseMainFragment.getData(Config.data.KEY_USER);
            }else if (BaseActivity.getData(Config.data.KEY_USER)!=null){
                user = BaseActivity.getData(Config.data.KEY_USER);
            }
            if (user != null) {

                if (DataFiller.getLocalUser() != null && user.getObjectId().equals(DataFiller.getLocalUser().getObjectId())) {
                    UserInfoFragment.SelfSwitch = true;
                    UserInfoActivity.SelfSwitch=true;
                }
                view.displayUser(user);
                BaseMainFragment.clearData();
                BaseActivity.clearData();
            }
        }

        requestFansNum(user);
        requestFocusNum(user);
        requestCollectNum(user);
    }

    @Override
    public void requestEdit(Context context) {

    }

    @Override
    public void requestDisplayHeadPic(ImageView imgHead, String url) {
        ImgLoadUtil.loadHead(imgHead.getContext(), imgHead, url);
    }

    @Override
    public void requestDisplayUserInfoBg(ImageView imgUserInfoBg, String url) {
        ImgLoadUtil.loadBitmap(imgUserInfoBg.getContext(), imgUserInfoBg, url);
    }

    @Override
    public void requestBack() {

    }

    @Override
    public void requestFansNum(User user) {
        NetRequest.Instance().requestFansNum(user, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                if (view==null) {
                return;
                }
                view.showFansNum(s);
            }

            @Override
            public void error(String err) {

            }
        });
    }

    @Override
    public void requestFocusNum(User user) {
        NetRequest.Instance().requestFocusNum(user, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                if (view==null){
                    return;
                }
                view.showFocusNum(s);
            }

            @Override
            public void error(String err) {

            }
        });
    }

    @Override
    public void requestCollectNum(User user) {
        NetRequest.Instance().requestCollectNum(user, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                if (view==null){
                    return;
                }
                view.showCollectNum(s);
            }

            @Override
            public void error(String err) {

            }
        });
    }

    public User getUser() {
        return user;
    }

    @Override
    public void end() {
        super.end();
        user = null;
    }
}

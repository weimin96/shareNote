
package com.aoliao.notebook.presenter;

import android.widget.ImageView;

import com.aoliao.notebook.contract.MainContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.fragment.UserInfoFragment;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.utils.data.DataFiller;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.ui.UserInfoActivity;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.aoliao.notebook.xmvp.XBasePresenter;

/**
 * Created by jiana on 16-7-22.
 */
public class MainPresenter extends XBasePresenter<MainContract.View> implements MainContract.Presenter {
    private User user;

    public MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    public boolean checkLocalUser() {
        return DataFiller.getLocalUser() != null;
    }

    @Override
    public User getUser() {
        return DataFiller.getLocalUser();
    }

    @Override
    public void requestUserInfo() {
        if (UserInfoFragment.SelfSwitch&& UserInfoActivity.SelfSwitch) {
            user = DataFiller.getLocalUser();
            view.displayUser(user);
        } else {
            user = BaseMainFragment.getData(Config.data.KEY_USER);
            if (user != null) {
                if (DataFiller.getLocalUser() != null && user.getObjectId().equals(DataFiller.getLocalUser().getObjectId())) {
                    UserInfoFragment.SelfSwitch = true;
                    UserInfoActivity.SelfSwitch=true;
                }
                view.displayUser(user);
                BaseMainFragment.clearData();
            }
        }
    }

    @Override
    public void requestDisplayHeadPic(ImageView imgHead, String url) {
        ImgLoadUtil.loadHead(imgHead.getContext(), imgHead, url);
    }


    @Override
    public void requestDisplayUserInfoBg(ImageView imgUserInfoBg, String url) {
        ImgLoadUtil.loadBitmap(imgUserInfoBg.getContext(), imgUserInfoBg, url);
    }


}

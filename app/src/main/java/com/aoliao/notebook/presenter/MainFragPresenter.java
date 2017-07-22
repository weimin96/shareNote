
package com.aoliao.notebook.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.contract.MainContract;
import com.aoliao.notebook.fragment.MainFragment;
import com.aoliao.notebook.view.BaseActivity;
import com.aoliao.notebook.view.ReadArticleActivity;
import com.aoliao.notebook.model.NetRequest;
import com.aoliao.notebook.utils.entity.BannerData;
import com.aoliao.notebook.utils.entity.MainTag;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.ArrayList;
import java.util.List;


public class MainFragPresenter extends XBasePresenter<MainContract.MainFragView> implements MainContract.MainFragPersenter {
    private List<BannerData> bannerDataList = null;

    public MainFragPresenter(MainContract.MainFragView view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        requestUpdateListData();
        requestBannerData();
    }

    @Override
    public void end() {
        super.end();
        bannerDataList = null;
    }


    @Override
    public List<MainTag> getTags() {
        return null;
    }

    @Override
    public void requestBannerData() {
        NetRequest.Instance().pullBannerData(new NetRequest.RequestListener<List<BannerData>>() {
            @Override
            public void success(List<BannerData> bannerDatas) {
                bannerDataList = bannerDatas;
                List<String> titles = new ArrayList<String>();
                List<String> images = new ArrayList<String>();
                for (BannerData bd : bannerDatas) {
                    titles.add(bd.getTitle());
                    images.add(bd.getPicUrl());
                }
                if (view==null){
                    return;
                }
                view.pullBannerDataSuccess(titles, images);
            }

            @Override
            public void error(String err) {
                view.pullBannerDataFail(err);
            }
        });
    }

    @Override
    public void requestOpenBannerLink(final Context context, int position) {

        NetRequest.Instance().queryPost(bannerDataList.get(position).getLinkTo(),
                new NetRequest.RequestListener<Post>() {
            @Override
            public void success(Post post) {
                BaseActivity.saveData(Config.data.KEY_POST, post);
                context.startActivity(new Intent(context, ReadArticleActivity.class));
            }

            @Override
            public void error(String err) {

            }
        });
    }

    @Override
    public void requestLoadHead(ImageView head) {

    }


    @Override
    public void requestUpdateListData() {
        NetRequest.Instance().pullPostList(0, MainFragment.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                if (view==null){
                    return;
                }
                view.updateListSuccess(posts);
            }

            @Override
            public void error(String err) {
                view.updateListFail(err);
            }
        });
    }

    @Override
    public void requestLoadListData(int currentNum) {
        NetRequest.Instance().pullPostList(currentNum, MainFragment.PAGE_SIZE, new NetRequest.RequestListener<List<Post>>() {
            @Override
            public void success(List<Post> posts) {
                if (posts == null || posts.size() == 0) {
                   // view.loadListDateOver();
                } else {
                    if (view==null){
                        return;
                    }
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

    @Override
    public void requestFollow(User user) {
        NetRequest.Instance().followUser(user, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.followUserSuccess(s);
            }

            @Override
            public void error(String err) {
                view.followUserFail(err);
            }
        });
    }
}

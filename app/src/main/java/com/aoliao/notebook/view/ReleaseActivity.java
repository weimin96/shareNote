package com.aoliao.notebook.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.MainBottomRecyclerAdapterTwo;
import com.aoliao.notebook.contract.ReleaseContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.utils.data.DataFiller;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.ReleasePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import butterknife.BindView;

/**
 * Created by 你的奥利奥 on 2017/6/4.
 */

public class ReleaseActivity extends BaseRefreshActivity<Post, ReleasePresenter> implements ReleaseContract.View {
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.mainBottomRecycler)
    RecyclerView mainBottomRecycler;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private User user;


    @Override
    protected void onInit() {
        super.onInit();
        initToolbar(toolbar);
        swipeLayout.setRefreshing(true);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (BaseMainFragment.getData(User.class.getSimpleName())!=null){
            user = BaseMainFragment.getData(User.class.getSimpleName());
        }else if (BaseActivity.getData(User.class.getSimpleName())!=null){
            user = BaseActivity.getData(User.class.getSimpleName());
        }
        if (DataFiller.getLocalUser().getObjectId().equals(user.getObjectId())) {
            toolbar.setTitle(R.string.my_public_dynamic);
        } else {
            toolbar.setTitle(R.string.public_dynamic);
        }

    }

    @Override
    protected BaseQuickAdapter<Post, BaseViewHolder> getAdapter() {
        return new MainBottomRecyclerAdapterTwo(R.layout.item_release_card);
    }

    @Override
    protected SwipeRefreshLayout getSwipeLayout() {
        return swipeLayout;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return mainBottomRecycler;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_release;
    }

    @Override
    protected void onListener() {
        super.onListener();
        mainBottomRecycler.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                switch (view.getId()){
                    case R.id.btnLike:
                        if (DataFiller.getLocalUser().getObjectId().equals(user.getObjectId())) {
                            Post post = (Post) adapter.getData().get(i);
                            presenter.requestDeletePost(post);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onRefresh();
                                }
                            },20);

                        }
                        break;
                    case R.id.layoutBaseArticle:
                        BaseMainFragment.saveData(Config.data.KEY_POST, adapter.getData().get(i));
                        startActivity(new Intent(ReleaseActivity.this, ReadArticleActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void deletePostSuccess() {
        Toast.makeText(getApplicationContext(),"删除笔记成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void deletePostFail(String err) {
        Toast.makeText(getApplicationContext(),err,Toast.LENGTH_LONG).show();
    }
}

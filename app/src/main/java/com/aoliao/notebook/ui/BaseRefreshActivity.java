package com.aoliao.notebook.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.RefreshContract;

import com.aoliao.notebook.utils.ToastUtil;
import com.aoliao.notebook.xmvp.XBasePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;


/**
 * Created by 你的奥利奥 on 2017/6/4.
 */

public abstract class BaseRefreshActivity <X, T extends XBasePresenter> extends BaseActivity<T> implements
        RefreshContract.View<X>,
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final int PAGE_SIZE = 6;
    private int mCurrentCounter = 0;
    private BaseQuickAdapter<X, BaseViewHolder> mAdapter;
    SwipeRefreshLayout swipeLayout;
    private View notLoadingView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onInit() {
        super.onInit();
        Log.i("aaa","onInit");

        mAdapter = getAdapter();
        mRecyclerView = getRecyclerView();
        swipeLayout = getSwipeLayout();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        swipeLayout.setOnRefreshListener(this);
        mAdapter.openLoadAnimation();
        mAdapter.openLoadMore(PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected abstract BaseQuickAdapter<X, BaseViewHolder> getAdapter();
    //
    protected abstract SwipeRefreshLayout getSwipeLayout();

    protected abstract RecyclerView getRecyclerView();

    /**
     * 更新列表成功
     */
    @Override
    public void updateListSuccess(List<X> datas) {
        mAdapter.setNewData(datas);
        mAdapter.openLoadMore(PAGE_SIZE);
        mAdapter.removeAllFooterView();
        mCurrentCounter = PAGE_SIZE;
        swipeLayout.setRefreshing(false);
    }



    /**
     * 更新失败
     */
    @Override
    public void updateListFail(String err) {
        swipeLayout.setRefreshing(false);
        ToastUtil.getInstance().showLongT(err);
    }

    /**
     * 加载数据成功
     */
    @Override
    public void loadListDataSuccess(List<X> datas) {
        mAdapter.addData(datas);
        mCurrentCounter = mAdapter.getData().size();
    }

    /**
     * 加载数据失败
     *
     * @param err
     */
    @Override
    public void loadListDataFail(String err) {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.showLoadMoreFailedView();
            }
        });
        ToastUtil.getInstance().showLongT(err);

    }

    /**
     * 数据已经被加载完
     */
    @Override
    public void loadListDateOver() {
        mAdapter.loadComplete();
        if (notLoadingView == null) {
            notLoadingView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        }
        mAdapter.addFooterView(notLoadingView);
    }


    @Override
    public void onRefresh() {
        getPresenter().requestUpdateListData();
    }

    @Override
    public void onLoadMoreRequested() {
        getPresenter().requestLoadListData(mCurrentCounter);
    }

    private RefreshContract.Presenter getPresenter() {
        return  (RefreshContract.Presenter) presenter;
    }


}

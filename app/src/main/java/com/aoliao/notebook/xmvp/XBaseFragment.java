package com.aoliao.notebook.xmvp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aoliao.notebook.utils.GenericHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 你的奥利奥 on 2017/5/14.
 */

public abstract class XBaseFragment<T extends XBasePresenter> extends android.support.v4.app.Fragment {

    protected T presenter;

    private View rootView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            presenter = GenericHelper.initPresenter(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
        rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        onInit();
        onListener();
        if (presenter != null) {
            presenter.start();
        }
        return rootView;
    }



    /**
     * 添加监听
     */
    protected void onListener(){

    }

    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected void onInit(){}

    public View getRootView() {
        return this.rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.end();
        unbinder.unbind();
    }
}

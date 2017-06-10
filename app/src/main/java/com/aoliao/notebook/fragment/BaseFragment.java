package com.aoliao.notebook.fragment;

import android.os.Bundle;
import android.view.View;

import com.aoliao.notebook.utils.ActivityUtils;
import com.aoliao.notebook.xmvp.XBaseFragment;
import com.aoliao.notebook.xmvp.XBasePresenter;

import butterknife.ButterKnife;

/**
 * 项目中Fragment的基类
 */
public abstract class BaseFragment<T extends XBasePresenter> extends XBaseFragment<T> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}

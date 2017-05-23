/*
 * Copyright 2016 XuJiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        initStatus();
    }

    private void initStatus() {
//        View status = ButterKnife.findById(getRootView(), R.id.status);
//        if (status != null) {
//            ActivityUtils.initSatus(status);
//        }
    }
}

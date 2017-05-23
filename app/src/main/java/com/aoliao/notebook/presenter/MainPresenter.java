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

package com.aoliao.notebook.presenter;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.MainContract;
import com.aoliao.notebook.helper.AppController;
import com.aoliao.notebook.model.data.DataFiller;
import com.aoliao.notebook.model.entity.User;
import com.aoliao.notebook.xmvp.XBasePresenter;
import com.aoliao.notebook.xmvp.XContract;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by jiana on 16-7-22.
 */
public class MainPresenter extends XBasePresenter implements MainContract.Presenter {


    public MainPresenter(XContract.View view) {
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


}

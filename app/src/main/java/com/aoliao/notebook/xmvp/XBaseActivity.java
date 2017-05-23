package com.aoliao.notebook.xmvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aoliao.notebook.utils.GenericHelper;

import butterknife.ButterKnife;


/**
 * Created by 你的奥利奥 on 2017/5/14.
 */

public abstract class XBaseActivity<T extends XBasePresenter> extends AppCompatActivity {
    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getContentId());
        ButterKnife.bind(this);
        try{
            presenter = GenericHelper.initPresenter(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
        onInit();
        onListener();
        if (presenter != null) {
            presenter.start();
        }
    }

    /**
     * 需要在SetContentView之前做的操作
     */
    protected void beforeSetContentView() {
    }

    /**
     * 在这里面进行初始化
     */
    protected void onInit() {}

    /**
     * 这里面写监听事件
     */
    protected void onListener() {}

    /**
     * 获取布局的id
     * @return
     */
    protected abstract int getContentId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.end();
    }
}

package com.aoliao.notebook.xmvp;


/**
 * Created by 你的奥利奥 on 2017/5/14.
 */

public class XBasePresenter<T extends XContract.View> {
    protected T view;

    public XBasePresenter(T view) {
        this.view = view;
    }

    public void start() {
    }

    public void end() {
        view = null;
    }
}

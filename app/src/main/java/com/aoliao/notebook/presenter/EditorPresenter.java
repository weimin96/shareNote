
package com.aoliao.notebook.presenter;

import com.aoliao.notebook.R;
import com.aoliao.notebook.contract.EditorContract;
import com.aoliao.notebook.AppController;
import com.aoliao.notebook.utils.NetRequest;
import com.aoliao.notebook.xmvp.XBasePresenter;

import java.io.File;

public class EditorPresenter extends XBasePresenter<EditorContract.View> implements EditorContract.Presenter{

    public EditorPresenter(EditorContract.View view) {
        super(view);
    }

    @Override
    public void uploadPic(String path) {
        view.showUploadPicProgress();
        NetRequest.Instance().uploadPic(new File(path), new NetRequest.UploadPicListener<String>() {
            @Override
            public void compressingPic() {
                view.showCompressingPicDialog();
            }

            @Override
            public void compressedPicSuccess() {
                view.compressedPicSuccess();
            }

            @Override
            public void progress(int i) {
                view.uploadPicProgress(i);
            }

            @Override
            public void success(String s) {
                view.uploadedPicLink(s);
            }

            @Override
            public void error(String err) {
                view.uploadPicFail(err);
            }
        });
    }

    @Override
    public void uploadArticle(String coverPicture, String title, String article) {
        view.showUploadArticleProgress();
        if ("".equals(title)) {
            view.uploadArticleFail(AppController.getAppContext().getString(R.string.please_input_title));
            return;
        }
        if ("".equals(article)) {
            view.uploadArticleFail(AppController.getAppContext().getString(R.string.please_input_content));
        }

        NetRequest.Instance().uploadArticle(coverPicture, title, article, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.uploadArticleSuccess();
            }

            @Override
            public void error(String err) {
                view.uploadArticleFail(err);
            }
        });



    }

    @Override
    public void saveArticle(String coverPicture, String title, String article) {
        view.showUploadArticleProgress();

        NetRequest.Instance().saveArticle(coverPicture, title, article, new NetRequest.RequestListener<String>() {
            @Override
            public void success(String s) {
                view.saveArticleSuccess();
            }

            @Override
            public void error(String err) {
                view.saveArticleFail(err);
            }
        });
    }
}

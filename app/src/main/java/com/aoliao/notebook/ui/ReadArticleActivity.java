package com.aoliao.notebook.ui;

import android.app.ProgressDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.ExpandableCommentItemAdapter;
import com.aoliao.notebook.contract.ReadActicleContract;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.config.Config;
import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.CommentFill;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.utils.entity.ReplyFill;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.ReadArticlePresenter;
import com.aoliao.notebook.utils.ToastUtil;
import com.aoliao.notebook.widget.MarkdownPreviewView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 你的奥利奥 on 2017/6/1.
 */

public class ReadArticleActivity extends BaseActivity<ReadArticlePresenter> implements ReadActicleContract.View {

    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.markdownView)
    MarkdownPreviewView markdownView;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.btnAddComment)
    LinearLayout btnAddComment;
    private boolean isFirstLoad = true;
    private ProgressDialog mProgressDialog=null;
    @Override
    protected int getContentId() {
        return R.layout.activity_read_acticle;
    }

    @OnClick({R.id.btnAddComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddComment:
                clickAddComment();
                break;
        }
    }

    private void clickAddComment() {
        new SweetAlertDialog(this, SweetAlertDialog.EDIT_TYPE)
                .setTitleText("评论")
                .showConfirmButton(true)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        presenter.addComment(sweetAlertDialog.getEditText());
                    }
                })
                .show();
    }

    @Override
    protected void onInit() {
        super.onInit();
        mProgressDialog=ProgressDialog.show(this,"请稍等","获取数据中",true);
        initToolbar(toolbar);
        presenter.requestPostData();
        scrollView.scrollTo(0, 0);
        initCommentLayout();

    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        Post post = BaseMainFragment.getData(Config.data.KEY_POST);
        Post post_RC = BaseActivity.getData(Config.data.KEY_POST);
        if (post != null) {
            toolbar.setTitle(post.getTitle());
        }else if(post_RC!=null){
            toolbar.setTitle(post_RC.getTitle());
        }
    }

    @Override
    public void showArticle(final Post post) {
        markdownView.parseMarkdown(post.getContent(), true);
        if (isFirstLoad) {
            isFirstLoad = false;
            markdownView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    markdownView.parseMarkdown(post.getContent(), true);
                }
            }, 500);
        }
        mProgressDialog.dismiss();
    }

    private void initCommentLayout() {
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    @Override
    protected void onListener() {
        super.onListener();
        rvComments.addOnItemTouchListener(new OnItemChildClickListener() {

            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.btnReply:
                        List<MultiItemEntity> res = baseQuickAdapter.getData();
                        CommentFill commentFill = (CommentFill) res.get(i);
                        Comment comment = commentFill.getComment();
                        clickReply(comment.getUser(), comment);
                        break;
                    case R.id.imgHead:
//                        ToastUtil.getInstance().showLongT("点击头像，跳转用户信息界面");
                        break;
                    case R.id.tvReplyUser:
                        if (!(baseQuickAdapter.getData().get(i) instanceof ReplyFill)) {
                            break;
                        }
                        ReplyFill replyFill = (ReplyFill) baseQuickAdapter.getData().get(i);
                        Reply reply = replyFill.getReply();
                        clickReply(reply.getSpeakUser(), reply.getComment());
                        break;
                    case R.id.tvReplyWho:
                        if (!(baseQuickAdapter.getData().get(i) instanceof ReplyFill)) {
                            break;
                        }
                        ReplyFill replyFillWho = (ReplyFill) baseQuickAdapter.getData().get(i);
                        Reply replyWho = replyFillWho.getReply();
                        clickReply(replyWho.getReplyUser(), replyWho.getComment());
                        break;
                }
            }
        });
    }


    /**
     * 点击了回复
     */
    private void clickReply(final User replyUser, final Comment comment) {
        new SweetAlertDialog(this, SweetAlertDialog.EDIT_TYPE)
                .setTitleText("回复：" + replyUser.getNickname())
                .showConfirmButton(true)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (!"".equals(sweetAlertDialog.getEditText())) {
                            presenter.addReply(replyUser, comment, sweetAlertDialog.getEditText());
                        }
                    }
                })
                .show();
    }


    @Override
    public void showComment(List<Comment> comments, List<Reply> replyList) {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (Comment comment : comments) {
            final CommentFill commentFill = new CommentFill(comment);
            for (Reply reply : replyList) {
                if (!reply.getComment().getObjectId().equals(comment.getObjectId())) {
                    continue;
                }
                final ReplyFill rf = new ReplyFill(reply);
                commentFill.addSubItem(rf);
            }

            res.add(commentFill);
        }
        ExpandableCommentItemAdapter expandableCommentItemAdapter = new ExpandableCommentItemAdapter(res);
        rvComments.setAdapter(expandableCommentItemAdapter);
    }

    @Override
    public void addCommentSuccess() {
        ToastUtil.getInstance().showLongT(R.string.add_comment_success);
    }

    @Override
    public void addCommentFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void replyCommentSuccess() {
        ToastUtil.getInstance().showLongT(getString(R.string.reply_comment_success));
    }

    @Override
    public void replyCommentFail(String err) {
        ToastUtil.getInstance().showLongT(err);
    }

    @Override
    public void likePostSuccess() {

    }

    @Override
    public void likePostFail(String err) {

    }

    @Override
    public void followUserSuccess() {

    }

    @Override
    public void followUserFail(String err) {

    }

}


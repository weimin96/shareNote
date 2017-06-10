package com.aoliao.notebook.adapter;

import android.widget.ImageView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.CommentFill;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.utils.entity.ReplyFill;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;


public class ExpandableCommentItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableCommentItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_comment);
        addItemType(TYPE_LEVEL_1, R.layout.item_comment_reply);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final CommentFill commentFill = (CommentFill) item;
                final Comment comment = commentFill.getComment();
                holder.setText(R.id.tvNickname, comment.getUser().getNickname())
                        .setText(R.id.tvDate, comment.getCreatedAt())
                        .setText(R.id.tvComment, comment.getContent());
                holder.addOnClickListener(R.id.btnReply)
                        .addOnClickListener(R.id.imgHead);
                ImageView head = holder.getView(R.id.imgHead);
                ImgLoadUtil.loadHead(mContext, head, comment.getUser().getHeadPic());
                expandAll(holder.getAdapterPosition(), true);
                break;
            case TYPE_LEVEL_1:
                final Reply reply = ((ReplyFill) item).getReply();
                holder.setText(R.id.tvReplyUser, reply.getSpeakUser().getNickname() + " ")
                        .setText(R.id.tvReplyWho, reply.getReplyUser().getNickname())
                        .setText(R.id.tvReply, reply.getContent());
                holder.addOnClickListener(R.id.tvReplyUser)
                        .addOnClickListener(R.id.tvReplyWho);
                break;
        }
    }
}


package com.aoliao.notebook.utils.entity;

import com.aoliao.notebook.adapter.ExpandableCommentItemAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;


public class ReplyFill implements MultiItemEntity {
    private Reply reply;
    public ReplyFill(Reply reply) {
        this.reply = reply;
    }


    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    @Override
    public int getItemType() {
        return ExpandableCommentItemAdapter.TYPE_LEVEL_1;
    }
}

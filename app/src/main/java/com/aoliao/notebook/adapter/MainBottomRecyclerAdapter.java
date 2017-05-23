
package com.aoliao.notebook.adapter;

import android.view.View;
import android.widget.ImageView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.model.entity.Post;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;



public class MainBottomRecyclerAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {
    public MainBottomRecyclerAdapter() {
        super(R.layout.item_main_card, new ArrayList<Post>());
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Post post) {
        baseViewHolder.setText(R.id.tvUserName, "@" + post.getAuthor().getNickname())
                .addOnClickListener(R.id.layoutBaseArticle)
                .addOnClickListener(R.id.btnLike)
                .addOnClickListener(R.id.btnFollow)
                .addOnClickListener(R.id.imgHead)
                .addOnClickListener(R.id.tvUserName)
                .setText(R.id.tvTopContent, post.getCreatedAt())
                .setText(R.id.tvBottomContent, post.getTitle());

        ImgLoadUtil.loadHead(mContext, (ImageView) baseViewHolder.getView(R.id.imgHead), post.getAuthor().getHeadPic());
        if (post.getCoverPicture() == null) {
            baseViewHolder.getView(R.id.imageThumb).setVisibility(View.GONE);
        } else {
            baseViewHolder.getView(R.id.imageThumb).setVisibility(View.VISIBLE);
            ImgLoadUtil.load(mContext, (ImageView) baseViewHolder.getView(R.id.imageThumb), post.getCoverPicture());
        }
    }
}

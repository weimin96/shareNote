package com.aoliao.notebook.adapter;

import android.view.View;
import android.widget.ImageView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by 你的奥利奥 on 2017/6/6.
 */

public class MainBottomRecyclerAdapterTwo extends BaseQuickAdapter<Post, BaseViewHolder> {

    public MainBottomRecyclerAdapterTwo(int layoutId) {
        super(layoutId, new ArrayList<Post>());
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Post post) {
        baseViewHolder.setText(R.id.tvUserName, "@" + post.getAuthor().getNickname())
                .addOnClickListener(R.id.layoutBaseArticle)
                .addOnClickListener(R.id.btnLike)
//                .addOnClickListener(R.id.btnFollow)
//                .addOnClickListener(R.id.imgHead)
//                .addOnClickListener(R.id.tvUserName)
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

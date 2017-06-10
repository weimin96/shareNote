package com.aoliao.notebook.adapter;

import android.widget.ImageView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class UserListAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserListAdapter(List<User> data) {
        super(R.layout.item_user, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        baseViewHolder.setText(R.id.tvUserName, "@" + user.getNickname())
                .setText(R.id.tvTopContent, user.getSign());
        ImageView headPic = baseViewHolder.getView(R.id.imgHead);
        ImgLoadUtil.loadHead(mContext, headPic, user.getHeadPic());
    }
}

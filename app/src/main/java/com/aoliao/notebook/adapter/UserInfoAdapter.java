package com.aoliao.notebook.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.utils.data.DataFiller;
import com.aoliao.notebook.utils.entity.EditItem;
import com.aoliao.notebook.utils.ImgLoadUtil;
import com.aoliao.notebook.utils.entity.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.logging.Handler;

public class UserInfoAdapter extends BaseQuickAdapter<EditItem, BaseViewHolder> {
    public UserInfoAdapter(User user) {
        super(R.layout.item_edit_user_info, DataFiller.getEditList(user));
    }

    public void update() {
        List<EditItem> itemList = getData();
        itemList.clear();
        List<EditItem> newList = DataFiller.getEditList(DataFiller.getLocalUser());
        if (newList == null) {
            return;
        }
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EditItem editItem) {
        ImageView imgHead = baseViewHolder.getView(R.id.imgHead);
        TextView tvTypeName = baseViewHolder.getView(R.id.tvTypeName);
        TextView tvvalue = baseViewHolder.getView(R.id.tvValue);
        tvTypeName.getPaint().setFakeBoldText(true);
        baseViewHolder.setText(R.id.tvTypeName, editItem.getTypeName())
                .setText(R.id.tvValue, editItem.getValue());
        if (baseViewHolder.getAdapterPosition() == 0) {
            imgHead.setVisibility(View.VISIBLE);
            tvvalue.setVisibility(View.GONE);
            ImgLoadUtil.loadHead(mContext, imgHead, editItem.getHeadPic());
        } else {
            imgHead.setVisibility(View.GONE);
        }
    }
}

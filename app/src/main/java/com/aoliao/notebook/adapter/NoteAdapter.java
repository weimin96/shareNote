package com.aoliao.notebook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.utils.listener.OnItemClickListener;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.TimeUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by 你的奥利奥 on 2017/2/14.
 */

public class NoteAdapter extends SwipeMenuAdapter<NoteAdapter.DefaultViewHolder> {
    private List<Post> list;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public NoteAdapter(Context context, List<Post> list) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,parent,false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        DefaultViewHolder viewHolder=new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener=mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {
        Post post=list.get(position);
        if (!"".equals(post.getTitle().trim())) {
            holder.title.setText(post.getTitle());
        } else {
            holder.title.setText(" 未标题 ");
        }
        holder.content.setText(post.getContent());
        holder.time.setText(TimeUtils.getConciseTime(post.getTime(), context));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView content;
        TextView time;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.note_title);
            content = (TextView) itemView.findViewById(R.id.note_content);
            time = (TextView) itemView.findViewById(R.id.note_time);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition(),v);
            }
        }
    }
}
package com.aoliao.notebook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoliao.notebook.R;
import com.aoliao.notebook.model.Choose;

import java.util.List;




/**
 * Created by Administrator on 2017/4/23 0023.
 */

public class ChooseAdapter extends ArrayAdapter<Choose> {
    private int resourceId;
    public ChooseAdapter(Context context, int textViewResourceId, List<Choose> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Choose choose=getItem(position);//获取当前项的Choose实例
        View view;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else {
            view=convertView;
        }
       // View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView chooseImage=(ImageView)view.findViewById(R.id.choose_image);
        TextView chooseName=(TextView)view.findViewById(R.id.choose_name);
        chooseImage.setImageResource(choose.getImageId());
        chooseName.setText(choose.getChoose());
        return view;
    }
}

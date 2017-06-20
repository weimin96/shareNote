package com.aoliao.notebook.utils;

import android.content.Context;
import android.widget.ImageView;

import com.aoliao.notebook.R;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .crossFade()
                .into(imageView);
    }
}
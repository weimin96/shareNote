package com.aoliao.notebook.fragment;


import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoliao.notebook.R;

import java.util.ArrayList;



/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class NoteChatFragment extends Fragment implements View.OnClickListener{
    private ViewPager noteViewPager;
    private ArrayList<View> pageView;
    private TextView hotLayout;
    private TextView careLayout;
    private ImageView scrollBar;//滚动条图片
    private int offset=0;//滚动条初始偏移量
    private int currIndex=0;//当前页编号
    private int bmpW;//滚动条宽度
    private int one;//一倍滚动量
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.notechat_fragment,container,false);
        noteViewPager=(ViewPager)view.findViewById(R.id.notechat_viewpager);
        View view1=inflater.inflate(R.layout.hot,null);
        View view2=inflater.inflate(R.layout.care,null);
        hotLayout=(TextView)view.findViewById(R.id.hot_layout);
        careLayout=(TextView)view.findViewById(R.id.care_layout);
        scrollBar=(ImageView)view.findViewById(R.id.scrollBar);
        hotLayout.setOnClickListener(this);
        careLayout.setOnClickListener(this);
        pageView=new ArrayList<View>();
        pageView.add(view1);//添加想要切换的页面
        pageView.add(view2);
        //数据适配器
        PagerAdapter mPagerAdapter=new PagerAdapter() {
            @Override
            //获取当前窗体页面数
            public int getCount() {
                return pageView.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            //使从ViewGroup中移出当前View
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager)container).removeView(pageView.get(position));
            }

            @Override
            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager)container).addView(pageView.get(position));
                return pageView.get(position);
            }
        };
        //绑定适配器
        noteViewPager.setAdapter(mPagerAdapter);
        //设置noteViewPager的初始界面为第一个界面
        noteViewPager.setCurrentItem(0);
        //添加切换界面的监听器
        noteViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //获取滚动条的宽度
        bmpW= BitmapFactory.decodeResource(getResources(),R.drawable.point_focused).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics=new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW=displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset=(screenW/2-bmpW)/2;
        //计算出切换一个页面时滚动条的位移量
        one=offset*2+bmpW;
        Matrix matrix=new Matrix();
        matrix.postTranslate(offset,0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollBar.setImageMatrix(matrix);
        return view;
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollBar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.hot_layout:
                //点击"热门“时切换到第一页
                noteViewPager.setCurrentItem(0);
                break;
            case R.id.care_layout:
                //点击"关注"时切换的第二页
                noteViewPager.setCurrentItem(1);
                break;
        }
    }
}

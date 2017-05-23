package com.aoliao.notebook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.FragmentAdapter;
import com.aoliao.notebook.factory.FragmentFactory;
import com.aoliao.notebook.fragment.BaseMainFragment;
import com.aoliao.notebook.fragment.MainFragment;
import com.aoliao.notebook.fragment.NoteChatFragment;
import com.aoliao.notebook.fragment.PersonalFragment;
import com.aoliao.notebook.fragment.ShoppingFragment;
import com.aoliao.notebook.helper.Config;
import com.aoliao.notebook.helper.SQLiteManager;
import com.aoliao.notebook.helper.SessionManager;
import com.aoliao.notebook.model.entity.Msg;
import com.aoliao.notebook.presenter.MainPresenter;
import com.aoliao.notebook.utils.FirHelper;
import com.aoliao.notebook.utils.ToastUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.Bmob;


public class MainActivity extends BaseActivity<MainPresenter>  {

    private Handler handler;

    private FragmentTabHost tabHost;
    private ViewPager pager;
    private List<Fragment> fragments;
    private static final String TAG ="MainActivity";

    float editTime = 0;
    private SessionManager sessionManager;
    private SQLiteManager sqLiteManager;
    @BindView(R.id.navigationView)
    NavigationView navigationView;

    //当前Fragment
    private BaseMainFragment currentFragment;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sessionManager = new SessionManager(getApplicationContext());
//        sqLiteManager = new SQLiteManager(getApplicationContext());
//        if (!sessionManager.isLoggedIn()) {
//            logoutUser();
//        }

        //获取headerLayout中的view


        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        pager = (ViewPager) findViewById(R.id.main_viewpager);


        Bmob.initialize(this, "3d9d9f910c51b02eea3d605178911aa5");

        View view = navigationView.inflateHeaderView(R.layout.header_layout);

//        TextView username = (TextView) view.findViewById(R.id.usernameId);
//        username.setText(sqLiteManager.getUserDetails().get(Config.USERS_USERNAME));
        RoundedImageView headImage = (RoundedImageView) view.findViewById(R.id.headId);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                getFragmentManager().beginTransaction().add(R.id.container, new LoginActivity()).commit();

            }
        });





        initView();
        //初始化TabHost
        initTabHost();
        //初始化pager
        initPager();

        //添加监听关联TabHost和viewPager
        bindTabAndPager();

    }


//    private void logoutUser() {
//        //设置登陆状态为false
//        sessionManager.setLogin(false);
//        //数据库中删除用户信息
//        sqLiteManager.deleteUsers();
//        //跳转到登陆页面
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        initToolbar(toolbar);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        /**
         * 菜单控制开关
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    /**
     * 为TabHost和viewPager 添加监听 让其联动
     */
    private void bindTabAndPager() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            /**
             *  tab改变后
             * @param tabId 当前tab的tag
             */
            @Override
            public void onTabChanged(String tabId) {
                log("vonTabChanged:"+tabId);
                int position = tabHost.getCurrentTab();
                pager.setCurrentItem(position,true);
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面滑动 触发
             * @param position 当前显得第一个页面的索引，当滑动出现时屏幕就会显示两个pager， 向右滑 position为当前-1（左边的pager就显示出来了），向左滑position为当前（右面就显出来了），
             * @param positionOffset 0-1之间 position的偏移量 从原始位置的偏移量
             * @param positionOffsetPixels 从position偏移的像素值 从原始位置的便宜像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                log("onPageScrolled=============position:"+position+"====positionOffset:"+positionOffset+"====positionOffsetPixels:"+positionOffsetPixels);
            }

            /**
             * 页面选中后
             * @param position 当前页面的index
             */
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
                log("onPageSelected==========:position:"+position);
            }

            /**
             * 页面滑动状态改变时触发
             * @param state 当前滑动状态 共三个状态值
             */
            @Override
            public void onPageScrollStateChanged(int state) {

                String stateStr="";
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stateStr="正在拖动";
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        stateStr="正在去往最终位置 即将到达最终位置";
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        stateStr="滑动停止，当前页面充满屏幕";
                        break;
                }
                log("onPageScrollStateChanged========stateCode:"+state+"====state:"+stateStr);
            }

        });
    }


    /**
     * 初始化 pager 绑定适配器
     */
    private void initPager() {
        fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new NoteChatFragment());
        fragments.add(new PersonalFragment());
        fragments.add(new ShoppingFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        pager.setAdapter(adapter);
    }

    /**
     * 初始化 TabHost
     */
    private void initTabHost() {

        tabHost.setup(MainActivity.this,getSupportFragmentManager(), R.id.tabContent);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec("homepage").setIndicator(createView(R.drawable.homepage,"首页")), MainFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("notechat").setIndicator(createView(R.drawable.notechat,"笔记圈")), NoteChatFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("personal").setIndicator(createView(R.drawable.personal,"个人中心")), PersonalFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("shopping").setIndicator(createView(R.drawable.shopping,"购书")), ShoppingFragment.class,null);

    }


    /**
     * 返回view
     * @param icon
     * @param tab
     * @return
     */
    private View createView(int icon,String tab){
        View view = getLayoutInflater().inflate(R.layout.homepage,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView  title = (TextView) view.findViewById(R.id.title);
        imageView.setImageResource(icon);
        title.setText(tab);
        return  view;
    }

    private void log(String log){
        Log.e(TAG,"="+log+"=");
    }

/**
    @Override
    protected void onInit() {
        super.onInit();
        initView();
        currentFragment = FragmentFactory.newFragment(Config.fragment.HOME);
        addFragment(currentFragment, R.id.fragment_container);
        handler = new Handler(Looper.getMainLooper());
//        FirHelper.getInstance().checkUpdate(this);

    }
 */



    @Override
    protected void onListener() {
        super.onListener();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.notebookMenuId) {
                    startActivity(new Intent(getApplication(), NoteActivity.class));
                }
                if (id == R.id.tarMenuId) {
                }
                if (id == R.id.recycleMenuId) {
                    startActivity(new Intent(getApplication(), RecycleActivity.class));
                }
                if (id == R.id.settingMenuId){
                    startActivity(new Intent(getApplication(), SettingsActivity.class));
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - editTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            editTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public static void startFragment(String fragmentKey) {
        EventBus.getDefault().post(new Msg(Msg.GOTO_FRAGMENT, fragmentKey));
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirHelper.getInstance().destroy();
    }

}

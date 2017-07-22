package com.aoliao.notebook.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aoliao.notebook.R;
import com.aoliao.notebook.adapter.FragmentAdapter;
import com.aoliao.notebook.contract.MainContract;
import com.aoliao.notebook.fragment.MainFragment;
import com.aoliao.notebook.fragment.NoteChatFragment;
import com.aoliao.notebook.fragment.ShoppingFragment;
import com.aoliao.notebook.fragment.UserInfoFragment;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.presenter.MainPresenter;
import com.aoliao.notebook.utils.helper.FirHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private FragmentTabHost tabHost;
    private ViewPager pager;
    protected static final String TAG_EXIT = "exit";
    private ProgressDialog mProgressDialog = null;


    float editTime = 0;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    TextView username;
    ImageView headImage;
    ImageView imgUserInfoBg;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInit() {
        super.onInit();
        mProgressDialog = ProgressDialog.show(this, "请稍等", "获取数据中", true);
        Bmob.initialize(this, "3d9d9f910c51b02eea3d605178911aa5");
        if (BmobUser.getCurrentUser(User.class) == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);

        View view = navigationView.inflateHeaderView(R.layout.header_layout);
        username = (TextView) view.findViewById(R.id.usernameId);
        headImage = (ImageView) view.findViewById(R.id.headId);
        imgUserInfoBg = (ImageView) view.findViewById(R.id.imgBackground);
        presenter.requestUserInfo();

        initView();
        //初始化TabHost
        initTabHost();
        //初始化pager
        initPager();
        //添加监听关联TabHost和viewPager
        bindTabAndPager();
    }

    @Override
    public void displayUser(User user) {

        if (BmobUser.getCurrentUser(User.class) != null) {
            username.setText(user.getNickname());
            presenter.requestDisplayHeadPic(headImage, user.getHeadPic());
            presenter.requestDisplayUserInfoBg(imgUserInfoBg, user.getHeadPic());
        }
        mProgressDialog.dismiss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

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
                int position = tabHost.getCurrentTab();
                pager.setCurrentItem(position, true);
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
            }

            /**
             * 页面选中后
             * @param position 当前页面的index
             */
            @Override
            public void onPageSelected(final int position) {
                tabHost.setCurrentTab(position);
            }

            /**
             * 页面滑动状态改变时触发
             * @param state 当前滑动状态 共三个状态值
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }


    /**
     * 初始化 pager 绑定适配器
     */
    private void initPager() {
        pager = (ViewPager) findViewById(R.id.main_viewpager);
        pager.setOffscreenPageLimit(1);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new NoteChatFragment());
        fragments.add(new UserInfoFragment());
        fragments.add(new ShoppingFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
    }

    /**
     * 初始化 TabHost
     */
    private void initTabHost() {
        tabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.tabContent);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec("homepage").setIndicator(createView(R.drawable.homepage, "首页")), MainFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("notechat").setIndicator(createView(R.drawable.notechat, "笔记圈")), NoteChatFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("personal").setIndicator(createView(R.drawable.personal, "个人中心")), UserInfoFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("shopping").setIndicator(createView(R.drawable.shopping, "消息")), ShoppingFragment.class, null);
    }


    /**
     * 返回view
     *
     * @param icon
     * @param tab
     * @return
     */
    private View createView(int icon, String tab) {
        View view = getLayoutInflater().inflate(R.layout.homepage, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        imageView.setImageResource(icon);
        title.setText(tab);
        return view;
    }


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
                if (id == R.id.recycleMenuId) {
                    startActivity(new Intent(getApplication(), RecycleActivity.class));
                }
                if (id == R.id.settingMenuId) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirHelper.getInstance().destroy();
    }

}

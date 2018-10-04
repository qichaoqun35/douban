package com.example.qichaoqun.douban.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.adapter.FragmentAdapter;
import com.example.qichaoqun.douban.assist_view.NoScrollViewPager;
import com.example.qichaoqun.douban.view.fragment.AnimationFragment;
import com.example.qichaoqun.douban.view.fragment.ClassifyFragment;
import com.example.qichaoqun.douban.view.fragment.MovieFragment;
import com.example.qichaoqun.douban.view.fragment.Top250Fragment;
import com.example.qichaoqun.douban.view.fragment.TvFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author qichaoqun
 * @date 2018/9/27
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener, OnTabReselectListener {

    private static final int TIME = 2000;

    @BindView(R.id.tool_bar_main)
    Toolbar toolBarMain;

    @BindView(R.id.scroll_view_pager)
    NoScrollViewPager scrollViewPager;

    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private Intent mIntent;

    List<Fragment> mFragments = new ArrayList<>();
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
        //初始化控件
        initView();
    }

    /**
     * 用于初始化一些布局和控件，进行基本的设置
     */
    private void initView() {
        //设置toolbar的标题
        toolBarMain.setTitle(getResources().getString(R.string.movie));
        setSupportActionBar(toolBarMain);
        //为toolbar上设置导航的icon
        toolBarMain.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        //设置中间的viewpager的fragment
        mFragments.add(new MovieFragment());
        mFragments.add(new AnimationFragment());
        mFragments.add(new TvFragment());
        mFragments.add(new Top250Fragment());
        mFragments.add(new ClassifyFragment());
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),mFragments);
        //配置viewpager
        scrollViewPager.setNoScroll(true);
        scrollViewPager.setOffscreenPageLimit(5);
        scrollViewPager.setAdapter(mFragmentAdapter);

        //监听drawerLayout改变导航栏的图标
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBarMain, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //添加toolbar中的导航栏的点击事件
        toolBarMain.setNavigationOnClickListener(this);
        //添加toolbar中的搜索的菜单栏的点击事件
        toolBarMain.setOnMenuItemClickListener(this);
        //添加navigationView的导航栏选择事件
        navigationView.setNavigationItemSelectedListener(this);
        //底部导航栏选择事件
        bottomBar.setOnTabSelectListener(this);
        //设置底部导航栏不被选择时的状态
        bottomBar.setOnTabReselectListener(this);
    }

    /**
     * 重写返回键功能，当用户两次点击后才可以进行退出
     */
    long startTime = 0;

    @Override
    public void onBackPressed() {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > TIME) {
            Toast.makeText(MainActivity.this, "请再按一次推出。", Toast.LENGTH_SHORT).show();
            startTime = endTime;
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(false);
        switch (menuItem.getItemId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.favorite:
                mIntent.setClass(MainActivity.this, FavoriteActivity.class);
                startActivity(mIntent);
                break;
            case R.id.gift:
                mIntent.setClass(MainActivity.this, GiftActivity.class);
                startActivity(mIntent);
                break;
            case R.id.sun_mode:
                break;
            case R.id.night_mode:
                break;
            default:
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onTabSelected(int tabId) {
        setBottom(tabId);
    }

    @Override
    public void onTabReSelected(int tabId) {
        setBottom(tabId);
    }

    /**
     * 设置底部导航栏
     */
    private void setBottom(int id) {
        switch (id) {
            case R.id.tab_movie:
                setTitleAndColor(0,getResources().getString(R.string.movie),getResources().getColor(R.color.movie_color),R.style.movie_theme);
                break;
            case R.id.tab_anime:
                setTitleAndColor(1,getResources().getString(R.string.animation),getResources().getColor(R.color.animation_color),R.style.animation_theme);
                break;
            case R.id.tab_tv:
                setTitleAndColor(2,getResources().getString(R.string.tv),getResources().getColor(R.color.tv_color),R.style.tv_theme);
                break;
            case R.id.tab_top250:
                setTitleAndColor(3,getResources().getString(R.string.top250),getResources().getColor(R.color.top250_color),R.style.top250_theme);
                break;
            case R.id.tab_tag:
                setTitleAndColor(4,getResources().getString(R.string.classify),getResources().getColor(R.color.classify_color),R.style.classify_theme);
                break;
            default:
        }
    }

    /**
     * 设置顶部toolbar的颜色和文字
     */
    private void setTitleAndColor(int item,String title,int color,int styleId) {
        //设置viewpager中要显示的fragment
        scrollViewPager.setCurrentItem(item,false);

        toolBarMain.setTitle(title);
        toolBarMain.setBackgroundColor(color);
        navigationView.setBackgroundColor(color);
        mIntent = new Intent();
        mIntent.putExtra("theme",styleId);
        mIntent.putExtra("color",color);
    }
}

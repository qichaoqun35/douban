package com.example.qichaoqun.douban.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.assist_view.NoScrollViewPager;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author qichaoqun
 * @date 2018/9/27
 */
public class MainActivity extends Activity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
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
}

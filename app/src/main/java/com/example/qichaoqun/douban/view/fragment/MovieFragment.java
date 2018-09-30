package com.example.qichaoqun.douban.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.base.BaseFragment;
import com.example.qichaoqun.douban.base.BasePresenter;
import com.example.qichaoqun.douban.bean.MessageEvent;
import com.example.qichaoqun.douban.presenter.MoviePresenter;
import com.example.qichaoqun.douban.view.iView.IMovieView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class MovieFragment extends BaseFragment<IMovieView,MoviePresenter> implements IMovieView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MovieFragment";
    //判断是否第一次显示
    private boolean isFirst = true;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.tip_text)
    TextView tipText;

    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void initView() {
        //对下拉刷新的控件进行配置
        refreshLayout.setColorSchemeResources(R.color.movie_color);
        refreshLayout.setOnRefreshListener(this);


        //配置recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    protected MoviePresenter getPresenter() {
        return new MoviePresenter();
    }

    @Override
    protected void loadData() {
        if(isFirst & isPrepared && isVisible){
            isFirst = false;
            presenter.loadingData(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.movie_fragment_layout;
    }


    @Override
    public void showLoading() {
        Log.i(TAG, "showLoading: "+"程序正在加载。。。");
    }

    @Override
    public void showEmpty() {
        tipText.setText(getResources().getString(R.string.no_content));
    }

    @Override
    public void showError() {
        Log.i(TAG, "showError: "+"程序加载出错了。。。");
        tipText.setText(getResources().getString(R.string.error));
    }

    /**
     * 程序数据加载完成，进行数据的适配
     * @param models 携带有数据的bean
     */
    @Override
    public void showComplete(ArrayList<?> models) {
        Log.i(TAG, "showComplete: 成功返回的数据为：："+models);

    }

    /**
     * 用户进行下拉刷新所进行的操作
     */
    @Override
    public void onRefresh() {

    }
}

package com.example.qichaoqun.douban.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.ButterKnife;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class MovieFragment extends BaseFragment<IMovieView,MoviePresenter> implements IMovieView {

    private static final String TAG = "MovieFragment";
    //判断是否第一次显示
    private boolean isFirst = true;

    @Override
    protected void initView() {

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

    }

    @Override
    public void showError() {
        Log.i(TAG, "showError: "+"程序加载出错了。。。");
    }

    @Override
    public void showComplete(ArrayList<?> models) {
        Log.i(TAG, "showComplete: 成功返回的数据为：："+models);
    }
}

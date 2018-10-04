package com.example.qichaoqun.douban.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.bean.MessageEvent;
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
public abstract class BaseFragment<V,T extends BasePresenter<V>> extends Fragment{

    protected boolean isPrepared = false;
    protected T presenter = null;
    private View mView;
    protected boolean isVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(),container,false);
        //绑定视图与黄油刀
        ButterKnife.bind(this, mView);
        //注册广播事件
        EventBus.getDefault().register(this);
        //初始化控件
        initView();
        //得到p层引用
        presenter = getPresenter();
        //绑定视图与 p 层，该this是子类的this，系统调用子类的OnCreateView,但是子类中没有，所有去父类中调用
        presenter.attachView(getContext(), (V) this);
        //标志初始化已经完成
        isPrepared = true;
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //加载数据
        loadData();
    }

    /**
     * 初始化控件
     */
    abstract protected void initView();
    /**
     * 得到p层的引用
     */
    abstract protected T getPresenter();

    /**
     * 实现数据的加载
     */
    abstract protected void loadData();

    /**
     * 得到视图布局的id
     * @return 视图的id
     */
    abstract protected int getLayoutId();

    /**
     * 自动设置当fragment多次出现的时候是否重新加载数据
     * @param isVisibleToUser 视图是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            loadData();
        }else{
            isVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.detachView();
        presenter.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){

    }
}

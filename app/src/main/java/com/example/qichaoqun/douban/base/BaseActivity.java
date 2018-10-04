package com.example.qichaoqun.douban.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aitangba.swipeback.SwipeBackActivity;

import butterknife.ButterKnife;

/**
 * @author qichaoqun
 * @date 2018/10/3
 */
public abstract class BaseActivity<V,T extends BasePresenter<V>> extends SwipeBackActivity{

    public T presenter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //获取p层的引用
        presenter = initPresenter();
        //将视图的回调的接口与p层相绑定
        presenter.attachView((V) this);
        //初始化控件
        initView();
    }

    /**
     * 获取布局的id
     * @return id
     */
    abstract protected int getLayoutId();

    /**
     * 获取p层的引用
     * @return p层的对象
     */
    abstract protected T initPresenter();



    /**
     * 初始化视图
     */
    abstract protected void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.onDestroy();
    }
}

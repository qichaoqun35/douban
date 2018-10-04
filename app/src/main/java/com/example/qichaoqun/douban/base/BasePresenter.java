package com.example.qichaoqun.douban.base;

import android.content.Context;

import io.reactivex.disposables.Disposable;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public abstract class BasePresenter<V> {

    protected V mView = null;
    protected Context mContext = null;
    protected Disposable mDisposable = null;

    public void attachView(V view){
        mView = view;
    }

    public void attachView(Context context,V view){
        mView =  view;
        mContext = context;
    }

    public void detachView(){
        mView = null;
    }

    /**
     * Presenter销毁时调用
     */
    abstract protected void onDestroy();

    /**
     * 取消订阅
     */
    protected void unSubscribe(){
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

}

package com.example.qichaoqun.douban.presenter.ipresenter;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public interface IMoviePresenter {
    /**
     * 是否进行下拉刷新的操作
     * @param isRefresh 下拉刷新
     */
    void loadingData(boolean isRefresh);
}

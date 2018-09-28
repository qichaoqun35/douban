package com.example.qichaoqun.douban.base;

import java.util.ArrayList;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public interface BaseView {
    /**
     * 显示正在加载
     */
    void showLoading();

    /**
     * 加载的数据为空
     */
    void showEmpty();

    /**
     * 加载数据出错
     */
    void showError();

    /**
     * 加载数据正常完成
     */
    void showComplete(ArrayList<?> models);
}

package com.example.qichaoqun.douban.presenter.ipresenter;

import com.example.qichaoqun.douban.bean.MovieSubjectsModel;

/**
 * @author qichaoqun
 * @date 2018/10/3
 */
public interface IMovieDetialPresenter {
    /**
     * 根据id加载电影的详细的信息
     * @param id 电影的id
     */
    void loadingData(String id);

    /**
     * 是否已经收藏
     * @param id 被收藏的电影的id
     * @return 是否收藏成功
     */
    boolean isFavorite(String id);

    /**
     * 插入数据
     * @param movieSubjectsModel 带有数据的bean
     * @return 是否插入成功
     */
    boolean saveFavorite(MovieSubjectsModel movieSubjectsModel);

    /**
     * 删除一条数据
     * @param id 数据的id
     * @return 删除是否成功
     */
    boolean deleteFavorite(String id);
}

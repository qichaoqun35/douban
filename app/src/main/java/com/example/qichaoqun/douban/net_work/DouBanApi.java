package com.example.qichaoqun.douban.net_work;

import com.example.qichaoqun.douban.bean.CelebrityDetailModel;
import com.example.qichaoqun.douban.bean.MovieDetailModel;
import com.example.qichaoqun.douban.bean.MovieModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public interface DouBanApi {
    @GET("top250")
    Observable<MovieModel> getTop250(@Query("start") int start, @Query("count") int count);

    @GET("search")
    Observable<MovieModel> searchKeyword(@Query("q") String q, @Query("start") int start, @Query("count") int count);

    @GET("search")
    Observable<MovieModel> searchTag(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);

    @GET("subject/{id}")
    Observable<MovieDetailModel> getMovieDetail(@Path("id") String id);

    @GET("celebrity/{id}")
    Observable<CelebrityDetailModel> getCelebrityDetail(@Path("id") String id);
}

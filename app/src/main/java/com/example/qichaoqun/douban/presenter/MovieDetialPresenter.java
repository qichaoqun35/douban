package com.example.qichaoqun.douban.presenter;

import android.util.Log;

import com.example.qichaoqun.douban.base.BasePresenter;
import com.example.qichaoqun.douban.bean.MovieDetailModel;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;
import com.example.qichaoqun.douban.net_work.NetWork;
import com.example.qichaoqun.douban.presenter.ipresenter.IMovieDetialPresenter;
import com.example.qichaoqun.douban.view.iView.IMovieDetialView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author qichaoqun
 * @date 2018/10/3
 */
public class MovieDetialPresenter extends BasePresenter<IMovieDetialView> implements IMovieDetialPresenter {


    private Observer<MovieDetailModel> mObserver = new Observer<MovieDetailModel>() {
        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(MovieDetailModel value) {
            //成功获取数据则调用接口显示数据
            mView.showComplete(value);
            //数据库不存在此数据就插入
            if (DataSupport.where("movie_id = ?", value.getMovie_id()).find(MovieDetailModel.class).size() < 1) {
                for (MovieDetailModel.CastsBean castsBean : value.getCasts()) {
                    if (castsBean.getAvatars() != null) {
                        castsBean.getAvatars().save();
                        castsBean.save();
                    }
                }
                for (MovieDetailModel.DirectorsBean directorsBean : value.getDirectors()) {
                    if (directorsBean.getAvatars() != null) {
                        directorsBean.getAvatars().save();
                        directorsBean.save();
                    }
                }
                value.getImages().save();
                value.getRating().save();
                value.save();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.i("电影详情信息页面加载数出现的错误是：：", "onError: "+e.getMessage());
            if (e.getMessage().contains("HTTP 404 Not Found")) {
                mView.showError(true);
            } else {
                mView.showError(false);
            }
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onDestroy() {
        unSubscribe();
    }


    @Override
    public void loadingData(String id) {
        mView.showLoading();
        //检查数据库是否存在数据
        final MovieDetailModel detailModel = DataSupport.where("movie_id = ?",id).findFirst(MovieDetailModel.class,true);
        if(detailModel == null){
            //如果没有则会进行网络的请求
            NetWork.getDoubanApi().getMovieDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }else{
            //有的话则直接取出数据并显示
            Observable.create(new ObservableOnSubscribe<MovieDetailModel>() {
                @Override
                public void subscribe(ObservableEmitter<MovieDetailModel> e) throws Exception {
                    //取出演员信息
                    List<MovieDetailModel.CastsBean> castsBeanList = new ArrayList<>();
                    for(MovieDetailModel.CastsBean castsBean : detailModel.getCasts()){
                        castsBeanList.add(DataSupport.where("cast_id = ?",castsBean.getId()).findFirst(MovieDetailModel.CastsBean.class,true));
                    }
                    detailModel.setCasts(castsBeanList);
                    //取出导演信息
                    List<MovieDetailModel.DirectorsBean> directorsBeans = new ArrayList<>();
                    for (MovieDetailModel.DirectorsBean directorsBean : detailModel.getDirectors()) {
                        directorsBeans.add(DataSupport.where("director_id = ?", directorsBean.getId()).findFirst(MovieDetailModel.DirectorsBean.class, true));
                    }
                    detailModel.setDirectors(directorsBeans);
                    //发射数据
                    e.onNext(detailModel);
                }
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieDetailModel>() {
                    @Override
                    public void accept(MovieDetailModel movieDetailModel) throws Exception {
                        //显示数据
                        mView.showComplete(movieDetailModel);
                    }
                });
        }
    }

    @Override
    public boolean isFavorite(String id) {
        return DataSupport.where("movie_id = ?", id).find(MovieSubjectsModel.class).size() > 0;
    }

    @Override
    public boolean saveFavorite(MovieSubjectsModel movieSubjectsModel) {
        try {
            for (MovieSubjectsModel.CastsBean castsBean : movieSubjectsModel.getCasts()) {
                if (castsBean.getAvatars() != null) {
                    castsBean.getAvatars().saveThrows();
                    castsBean.saveThrows();
                }
            }
            for (MovieSubjectsModel.DirectorsBean directorsBean : movieSubjectsModel.getDirectors()) {
                if (directorsBean.getAvatars() != null) {
                    directorsBean.getAvatars().saveThrows();
                    directorsBean.saveThrows();
                }
            }
            movieSubjectsModel.getImages().saveThrows();
            movieSubjectsModel.getRating().saveThrows();
            movieSubjectsModel.saveThrows();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteFavorite(String id) {
        return DataSupport.deleteAll(MovieSubjectsModel.class, "movie_id = ?", id) > 0;
    }
}

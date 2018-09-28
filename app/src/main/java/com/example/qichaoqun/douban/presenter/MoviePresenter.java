package com.example.qichaoqun.douban.presenter;


import com.example.qichaoqun.douban.base.BasePresenter;
import com.example.qichaoqun.douban.bean.MovieModel;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;
import com.example.qichaoqun.douban.net_work.NetWork;
import com.example.qichaoqun.douban.utils.TagData;
import com.example.qichaoqun.douban.view.iView.IMovieView;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class MoviePresenter extends BasePresenter<IMovieView> implements IMoviePresenter {

    private String artist = null;
    private int start = 0;
    private Observer<MovieModel> mObserver = new Observer<MovieModel>() {
        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(MovieModel movieModel) {
            if (movieModel.getSubjects().size() > 0) {
                mView.showComplete((ArrayList<?>) movieModel.getSubjects());
                start = start + movieModel.getSubjects().size();
            } else {
                mView.showEmpty();
            }
        }

        @Override
        public void onError(Throwable e) {
            mView.showError();
        }

        @Override
        public void onComplete() {

        }
    };


    @Override
    public void loadingData(boolean isRefresh) {
//        mView.showLoading();
        if(isRefresh){
            artist = TagData.artists[new Random().nextInt(36)];
            start = 0;
        }
        NetWork.getDoubanApi().searchTag(artist,start,20)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(mObserver);
    }
}

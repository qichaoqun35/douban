package com.example.qichaoqun.douban.presenter;

import com.example.qichaoqun.douban.base.BasePresenter;
import com.example.qichaoqun.douban.bean.MovieModel;
import com.example.qichaoqun.douban.net_work.NetWork;
import com.example.qichaoqun.douban.presenter.ipresenter.IAnimationPresenter;
import com.example.qichaoqun.douban.view.iView.IAnimationView;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author qichaoqun
 * @date 2018/10/4
 */
public class AnimationPresenter extends BasePresenter<IAnimationView> implements IAnimationPresenter {

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
    protected void onDestroy() {
        unSubscribe();
    }

    @Override
    public void loadingData() {
        mView.showLoading();
       NetWork.getDoubanApi().searchTag("动漫", start, 20)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(mObserver);
    }
}

package com.example.qichaoqun.douban.view.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.adapter.MovieDetailAdapter;
import com.example.qichaoqun.douban.base.BaseActivity;
import com.example.qichaoqun.douban.bean.MovieDetailModel;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;
import com.example.qichaoqun.douban.presenter.MovieDetialPresenter;
import com.example.qichaoqun.douban.view.iView.IMovieDetialView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author qichaoqun
 * @date 2018/10/3
 */
public class MovieDetailActivity extends BaseActivity<IMovieDetialView, MovieDetialPresenter> implements IMovieDetialView, Toolbar.OnMenuItemClickListener, View.OnClickListener, MovieDetailAdapter.IOnItemClickListener {

    @BindView(R.id.iv_movie_detail)
    ImageView ivMovieDetail;
    @BindView(R.id.toolbar_movie_detail)
    Toolbar toolbarMovieDetail;
    @BindView(R.id.collapsing_movie_detail)
    CollapsingToolbarLayout collapsingMovieDetail;
    @BindView(R.id.tv_movie_detail_year)
    TextView tvMovieDetailYear;
    @BindView(R.id.tv_movie_detail_country)
    TextView tvMovieDetailCountry;
    @BindView(R.id.tv_movie_detail_type)
    TextView tvMovieDetailType;
    @BindView(R.id.tv_movie_detail_average)
    TextView tvMovieDetailAverage;
    @BindView(R.id.rv_movie_detail_director)
    RecyclerView rvMovieDetailDirector;
    @BindView(R.id.rv_movie_detail_cast)
    RecyclerView rvMovieDetailCast;
    @BindView(R.id.rv_movie_detail_summary)
    TextView rvMovieDetailSummary;
    @BindView(R.id.ll_movie_detail)
    LinearLayout llMovieDetail;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.srl_movie_detail)
    SwipeRefreshLayout srlMovieDetail;
    private MovieSubjectsModel mMovieSubjectsModel;

    @Override
    protected int getLayoutId() {
        return R.layout.movie_content_layout;
    }

    @Override
    protected MovieDetialPresenter initPresenter() {
        return new MovieDetialPresenter();
    }

    @Override
    protected void initView() {
        //显示图片
        Glide.with(this).load(getIntent().getStringExtra("img_url")).into(ivMovieDetail);
        //设置标题
        collapsingMovieDetail.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbarMovieDetail);
        toolbarMovieDetail.setNavigationIcon(R.drawable.ic_back_white_24dp);
        //配置swipeFreshLayout
        srlMovieDetail.setEnabled(false);
        srlMovieDetail.setColorSchemeColors(getIntent().getIntExtra("color", getResources().getColor(R.color.movie_color)));
        //设置点赞事件
        toolbarMovieDetail.setOnMenuItemClickListener(this);
        toolbarMovieDetail.setNavigationOnClickListener(this);
        //配置RecyclerView
        initRecyclerView(rvMovieDetailDirector);
        initRecyclerView(rvMovieDetailCast);
        //获取传递过来的电影的信息
        mMovieSubjectsModel = (MovieSubjectsModel) getIntent().getSerializableExtra("movieSubject");
        //加载数据
        presenter.loadingData(getIntent().getStringExtra("id"));
    }

    private void initRecyclerView(RecyclerView rvMovieDetailDirector) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        rvMovieDetailDirector.setLayoutManager(linearLayoutManager);
        rvMovieDetailDirector.setNestedScrollingEnabled(false);
    }


    @Override
    public void showLoading() {
        srlMovieDetail.setRefreshing(true);
    }

    @Override
    public void showEmpty() {
        srlMovieDetail.setRefreshing(false);
    }

    @Override
    public void showError(boolean is404) {
        srlMovieDetail.setRefreshing(false);
        if (is404) {
            Toast.makeText(this, getText(R.string.error_tips3), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getText(R.string.error_tips), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showComplete(MovieDetailModel movieDetailModel) {
        llMovieDetail.setVisibility(View.VISIBLE);
        srlMovieDetail.setRefreshing(false);
        //年份
        tvMovieDetailYear.setText(String.format(getResources().getString(R.string.detail_year), movieDetailModel.getYear()));
        //国家地区
        String country = "";
        for (int i = 0; i < movieDetailModel.getCountries().size(); i++) {
            if (i == movieDetailModel.getCountries().size() - 1) {
                country = country + movieDetailModel.getCountries().get(i);
            } else {
                country = country + movieDetailModel.getCountries().get(i) + "、";
            }
        }
        tvMovieDetailCountry.setText(String.format(getResources().getString(R.string.detail_country), country));
        //类型
        String type = "";
        for (int i = 0; i < movieDetailModel.getGenres().size(); i++) {
            if (i == movieDetailModel.getGenres().size() - 1) {
                type = type + movieDetailModel.getGenres().get(i);
            } else {
                type = type + movieDetailModel.getGenres().get(i) + "、";
            }
        }
        tvMovieDetailType.setText(String.format(getResources().getString(R.string.detail_type), type));
        //分数
        tvMovieDetailAverage.setText(String.format(getResources().getString(R.string.average), "" + movieDetailModel.getRating().getAverage()));
        //导演
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        for (MovieDetailModel.DirectorsBean directorsBean : movieDetailModel.getDirectors()) {
            if (directorsBean.getAvatars() != null && directorsBean.getId() != null) {
                imgs.add(directorsBean.getAvatars().getMedium());
                names.add(directorsBean.getName());
                ids.add(directorsBean.getId());
            }
        }
        MovieDetailAdapter directorsAdapter = new MovieDetailAdapter(imgs, names, ids);
        rvMovieDetailDirector.setAdapter(directorsAdapter);
        directorsAdapter.setOnItemClickListener(this);
        //演员
        imgs = new ArrayList<>();
        names = new ArrayList<>();
        ids = new ArrayList<>();
        for (MovieDetailModel.CastsBean castsBean : movieDetailModel.getCasts()) {
            if (castsBean.getAvatars() != null && castsBean.getId() != null) {
                imgs.add(castsBean.getAvatars().getMedium());
                names.add(castsBean.getName());
                ids.add(castsBean.getId());
            }
        }
        MovieDetailAdapter castsAdapter = new MovieDetailAdapter(imgs, names, ids);
        rvMovieDetailCast.setAdapter(castsAdapter);
        castsAdapter.setOnItemClickListener(this);
        //简介
        rvMovieDetailSummary.setText(String.format(getResources().getString(R.string.detail_summary), movieDetailModel.getSummary()));
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public void onClick(View v) {
        super.finish();
    }

    @Override
    public void onItemClick(String id, String name) {

    }
}

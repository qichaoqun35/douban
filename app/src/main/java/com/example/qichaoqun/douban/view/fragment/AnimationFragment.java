package com.example.qichaoqun.douban.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.adapter.MovieAdapter;
import com.example.qichaoqun.douban.base.BaseFragment;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;
import com.example.qichaoqun.douban.presenter.AnimationPresenter;
import com.example.qichaoqun.douban.view.activity.MovieDetailActivity;
import com.example.qichaoqun.douban.view.iView.IAnimationView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class AnimationFragment extends BaseFragment<IAnimationView, AnimationPresenter> implements IAnimationView, View.OnClickListener, MovieAdapter.IOnItemClickListener {

    @BindView(R.id.tv_tip_anime)
    TextView tvTipAnime;
    @BindView(R.id.rv_anime)
    RecyclerView rvAnime;
    @BindView(R.id.srl_anime)
    SwipeRefreshLayout srlAnime;
    Unbinder unbinder;

    private boolean isFirst = true;

    private ArrayList<MovieSubjectsModel> movieModelBeans = new ArrayList<>();

    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1) && dy != 0 && !srlAnime.isRefreshing()) {
                mAnimeFooterViewInfo.setText(getText(R.string.loading_tips));
                srlAnime.setRefreshing(true);
                presenter.loadingData();//刷新
            }
        }
    };
    private MovieAdapter mAnimeAdapter;
    private HeaderAndFooterWrapper mAnimeWrapperAdapter;
    private TextView mAnimeFooterViewInfo;

    @Override
    protected void initView() {
        //配置SwipeRefreshLayout
        srlAnime.setEnabled(false);
        srlAnime.setColorSchemeResources(R.color.colorAnime);
        //适配器
        mAnimeAdapter = new MovieAdapter(getContext(),movieModelBeans);
        mAnimeWrapperAdapter = new HeaderAndFooterWrapper(mAnimeAdapter);
        mAnimeAdapter.setOnItemClickListener(this);
        //添加footerView
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        mAnimeFooterViewInfo = (TextView) footerView.findViewById(R.id.footerview_info);
        mAnimeWrapperAdapter.addFootView(footerView);
        //配置RecyclerView
        rvAnime.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAnime.setAdapter(mAnimeWrapperAdapter);
        //监听列表上拉
        rvAnime.addOnScrollListener(mOnScrollListener);
        //监听点击提示文本
        tvTipAnime.setOnClickListener(this);
    }

    @Override
    protected AnimationPresenter getPresenter() {
        return new AnimationPresenter();
    }

    @Override
    protected void loadData() {
        if(isFirst & isPrepared && isVisible){
            isFirst = false;
            presenter.loadingData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.animation_fragment_layout;
    }

    @Override
    public void showLoading() {
        srlAnime.setRefreshing(true);
    }

    @Override
    public void showEmpty() {
        srlAnime.setRefreshing(false);
        if (movieModelBeans.size() > 0) {
            mAnimeFooterViewInfo.setText(getText(R.string.no_data_tips));
        } else {
            tvTipAnime.setText(getText(R.string.empty_tips));
        }
    }

    @Override
    public void showError() {
        srlAnime.setRefreshing(false);
        Toast.makeText(getContext(),getString(R.string.error_tips),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showComplete(ArrayList<?> models) {
        tvTipAnime.setVisibility(View.GONE);
        srlAnime.setRefreshing(false);
        movieModelBeans.addAll((Collection<? extends MovieSubjectsModel>) models);
        mAnimeWrapperAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        presenter.loadingData();
    }

    @Override
    public void onItemClick(int position, String id, String imageUrl, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.animation_theme);
        intent.putExtra("img_url", imageUrl);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", movieModelBeans.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorAnime));
        startActivity(intent);
    }
}

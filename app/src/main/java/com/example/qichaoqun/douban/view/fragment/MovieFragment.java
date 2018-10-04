package com.example.qichaoqun.douban.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.adapter.MovieAdapter;
import com.example.qichaoqun.douban.base.BaseFragment;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;
import com.example.qichaoqun.douban.presenter.MoviePresenter;
import com.example.qichaoqun.douban.view.activity.MovieDetailActivity;
import com.example.qichaoqun.douban.view.iView.IMovieView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class MovieFragment extends BaseFragment<IMovieView,MoviePresenter> implements IMovieView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, MovieAdapter.IOnItemClickListener {

    private static final String TAG = "MovieFragment";
    private boolean isFirst = true;
    private ArrayList<MovieSubjectsModel> mList = new ArrayList<>();
    private boolean isFresh = true;


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.tip_text)
    TextView tipText;

    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    private MovieAdapter mMovieAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private TextView mFootViewInfor;

    /**
     * 上拉加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if(!recyclerView.canScrollVertically(1) && dy != 0 && !refreshLayout.isRefreshing()){
                mFootViewInfor.setText(R.string.loading_with_life);
                refreshLayout.setRefreshing(true);
                presenter.loadingData(false);
            }
        }
    };

    @Override
    protected void initView() {
        //对下拉刷新的控件进行配置
        refreshLayout.setColorSchemeResources(R.color.movie_color);
        refreshLayout.setOnRefreshListener(this);

        //设置适配器
        mMovieAdapter = new MovieAdapter(getActivity(),mList);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMovieAdapter);

        //添加footview
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.footerview, null);
        mFootViewInfor = footView.findViewById(R.id.footerview_info);
        mHeaderAndFooterWrapper.addFootView(footView);

        //配置recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mHeaderAndFooterWrapper);

        //监听上拉刷 新
        recyclerView.addOnScrollListener(mOnScrollListener);
        tipText.setOnClickListener(this);

        //设置条目的监听事件
        mMovieAdapter.setOnItemClickListener(this);
    }

    @Override
    protected MoviePresenter getPresenter() {
        return new MoviePresenter();
    }

    @Override
    protected void loadData() {
        if(isFirst & isPrepared && isVisible){
            isFirst = false;
            presenter.loadingData(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.movie_fragment_layout;
    }


    @Override
    public void showLoading() {
        Log.i(TAG, "showLoading: "+"程序正在加载。。。");
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void showEmpty() {
        tipText.setText(getResources().getString(R.string.no_content));
    }

    @Override
    public void showError() {
        Log.i(TAG, "showError: "+"程序加载出错了。。。");
        refreshLayout.setRefreshing(false);
        tipText.setText(getResources().getString(R.string.error));
    }

    /**
     * 程序数据加载完成，进行数据的适配
     * @param models 携带有数据的bean
     */
    @Override
    public void showComplete(ArrayList<?> models) {
        Log.i(TAG, "showComplete: 成功返回的数据为：："+models);
        //如果时下拉刷新的话，清楚集合中所有的数据，重新加入数据
        if(isFresh){
            mList.clear();
        }
        isFresh = false;
        tipText.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
        mList.addAll((Collection<? extends MovieSubjectsModel>) models);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 用户进行下拉刷新所进行的操作
     */
    @Override
    public void onRefresh() {
        isFresh = true;
        presenter.loadingData(true);
    }

    /**
     * 点击文本提示框，没有加载到内容，重新开始加载
     * @param v
     */
    @Override
    public void onClick(View v) {
        presenter.loadingData(true);
    }

    @Override
    public void onItemClick(int position, String id, String imageUrl, String title) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("theme", R.style.movie_theme);
        intent.putExtra("img_url", imageUrl);
        intent.putExtra("title", title);
        intent.putExtra("movieSubject", mList.get(position));
        intent.putExtra("color", getResources().getColor(R.color.colorMovie));
        startActivity(intent);
    }
}

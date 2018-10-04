package com.example.qichaoqun.douban.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qichaoqun.douban.R;
import com.example.qichaoqun.douban.bean.MovieSubjectsModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String TAG = "MovieAdapter";
    private ArrayList<MovieSubjectsModel> mList = null;
    private Context mContext = null;

    public MovieAdapter(Context context,ArrayList<MovieSubjectsModel> list){
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: "+"程序执行到了这里");
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item,viewGroup,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        Log.i(TAG, "onBindViewHolder: "+"程序执行到了这里了...");
        final MovieSubjectsModel subjectsModel = mList.get(position);
        //加载图片
        Glide.with(mContext).load(subjectsModel.getImages().getMedium()).into(myViewHolder.imgMovie);
        //设置title
        myViewHolder.tvMovieTitle.setText(subjectsModel.getTitle());
        //设置导演
        String directors = "";
        if (subjectsModel.getDirectors() != null) {
            for (int i = 0; i < subjectsModel.getDirectors().size(); i++) {
                MovieSubjectsModel.DirectorsBean director = subjectsModel.getDirectors().get(i);
                if (i == subjectsModel.getDirectors().size() - 1) {
                    directors = directors + director.getName();
                } else {
                    directors = directors + director.getName() + "、";
                }
            }
        } else {
            directors = mContext.getResources().getString(R.string.unknown);
        }
        myViewHolder.tvMovieDirector.setText(String.format(mContext.getResources().getString(R.string.director), directors));
        //设置主演
        String casts = "";
        if (subjectsModel.getCasts() != null) {
            for (int i = 0; i < subjectsModel.getCasts().size(); i++) {
                MovieSubjectsModel.CastsBean cats = subjectsModel.getCasts().get(i);
                if (i == subjectsModel.getCasts().size() - 1) {
                    casts = casts + cats.getName();
                } else {
                    casts = casts + cats.getName() + "、";
                }
            }
        } else {
            casts = mContext.getResources().getString(R.string.unknown);
        }
        myViewHolder.tvMovieCast.setText(String.format(mContext.getResources().getString(R.string.cast), casts));
        //设置分数
        myViewHolder.tvMovieAverage.setText(String.format(mContext.getResources().getString(R.string.average), "" + subjectsModel.getRating().getAverage()));
        //为每一项设置监听事件
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIOnItemClickListener.onItemClick(myViewHolder.getAdapterPosition(),subjectsModel.getMovie_id(),subjectsModel.getImages().getLarge(),subjectsModel.getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: 数组的大小为：：："+mList.size());
        return mList.size();
    }

    /**
     * 设置viewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.checkbox)
        AppCompatCheckBox checkbox;
        @BindView(R.id.img_movie)
        ImageView imgMovie;
        @BindView(R.id.tv_movie_title)
        TextView tvMovieTitle;
        @BindView(R.id.tv_movie_director)
        TextView tvMovieDirector;
        @BindView(R.id.tv_movie_cast)
        TextView tvMovieCast;
        @BindView(R.id.tv_movie_average)
        TextView tvMovieAverage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private IOnItemClickListener mIOnItemClickListener = null;

    public void setOnItemClickListener(IOnItemClickListener onItemClickListener){
        mIOnItemClickListener = onItemClickListener;
    }

    /**
     * 定义用于监听回调的接口
     */
    public interface IOnItemClickListener{
        /**
         * 用于接口回调时的方法
         * @param position 条目的位置
         * @param id 当前电影的id
         * @param imageUrl 图片的路径
         * @param title 电影的标题
         */
        void onItemClick(int position,String id,String imageUrl,String title);
    }
}

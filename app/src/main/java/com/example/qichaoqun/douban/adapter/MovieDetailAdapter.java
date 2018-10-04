package com.example.qichaoqun.douban.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qichaoqun.douban.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Won on 2017/3/11.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MyViewHolder> {

    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();

    public MovieDetailAdapter(ArrayList<String> imgs, ArrayList<String> names, ArrayList<String> ids) {
        this.imgs = imgs;
        this.names = names;
        this.ids = ids;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_detail_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        //设置头像
        Glide.with(context).load(imgs.get(position)).into(holder.ivMovieDetailItem);
        //设置名字
        holder.tvMovieDetailItem.setText(names.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(ids.get(holder.getAdapterPosition()), names.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_movie_detail_item)
        ImageView ivMovieDetailItem;
        @BindView(R.id.tv_movie_detail_item)
        TextView tvMovieDetailItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private IOnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(IOnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface IOnItemClickListener {
        void onItemClick(String id, String name);
    }

}

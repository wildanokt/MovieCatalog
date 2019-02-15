package com.wildanokt.searchmoviemade.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wildanokt.searchmoviemade.BuildConfig;
import com.wildanokt.searchmoviemade.model.MovieItems;
import com.wildanokt.searchmoviemade.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieNowPlayingAdapter extends RecyclerView.Adapter<MovieNowPlayingAdapter.NowPlayingViewHolder> {

    private ArrayList<MovieItems> mData;
    private Context context;

    public MovieNowPlayingAdapter(Context context) {
        this.context = context;
    }

    //----------------------------------------------------------

    public ArrayList<MovieItems> getMovieData() {
        return mData;
    }
    public void setMovieData(ArrayList<MovieItems> mData) {
        this.mData = new ArrayList<>();
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    //----------------------------------------------------------

    @NonNull
    @Override
    public NowPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View MovieItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new MovieNowPlayingAdapter.NowPlayingViewHolder(MovieItem);
    }
    @Override
    public void onBindViewHolder(@NonNull NowPlayingViewHolder nowPlayingViewHolder, int i) {
        nowPlayingViewHolder.tvTitle.setText(mData.get(i).getTitle());
        nowPlayingViewHolder.tvOverview.setText(mData.get(i).getOverview());

        Glide.with(context)
                .load(BuildConfig.IMAGE_URL +mData.get(i).getPosterPath())
                .into(nowPlayingViewHolder.imgPoster);
    }
    @Override
    public int getItemCount() {
        if (mData==null)return 0;
        return mData.size();
    }

    public class NowPlayingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        CircleImageView imgPoster;

        public NowPlayingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvOverview = itemView.findViewById(R.id.tv_movie_overview);
            imgPoster = itemView.findViewById(R.id.img_movie);
        }
    }
}

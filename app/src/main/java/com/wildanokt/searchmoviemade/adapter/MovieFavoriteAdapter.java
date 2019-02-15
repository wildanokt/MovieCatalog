package com.wildanokt.searchmoviemade.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wildanokt.searchmoviemade.BuildConfig;
import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.entity.Favorite;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.FavoriteViewHolder> {

//    private ArrayList<Favorite> mData;
    Activity activity;
    public MovieFavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    private Cursor mData;
    public Cursor getFavoriteData() {
        return mData;
    }
    public void setFavoriteData(Cursor mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int i) {
        final Favorite favorite = getItem(i);
        holder.tvTitle.setText(favorite.getTitle());
        holder.tvOverview.setText(favorite.getOverview());
        Glide.with(activity)
                .load(BuildConfig.IMAGE_URL +favorite.getImgPath())
                .into(holder.imgPoster);
    }

    public Favorite getItem(int position){
        if (!mData.moveToPosition(position)){
            throw new IllegalStateException("Position Invalid");
        }
        return new Favorite(mData);
    }

    @Override
    public int getItemCount() {
        if (getFavoriteData() == null)return 0;
        return getFavoriteData().getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        CircleImageView imgPoster;
        RecyclerView rvFavoriteList;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvOverview = itemView.findViewById(R.id.tv_movie_overview);
            imgPoster = itemView.findViewById(R.id.img_movie);
            rvFavoriteList = itemView.findViewById(R.id.rv_favorite);
        }
    }
}

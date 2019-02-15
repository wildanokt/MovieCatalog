package com.wildanokt.movieapps.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wildanokt.movieapps.BuildConfig;
import com.wildanokt.movieapps.R;
import com.wildanokt.movieapps.db.DatabaseContract;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.wildanokt.movieapps.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.movieapps.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.movieapps.db.DatabaseContract.FavoriteCollums.TITLE;
import static com.wildanokt.movieapps.db.DatabaseContract.getCollumnString;

public class MovieAppsAdapter extends CursorAdapter {

    public MovieAppsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent,false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor!=null){
            TextView tvTitle = view.findViewById(R.id.tv_favorite_title);
            TextView tvOverview = view.findViewById(R.id.tv_favorite_overview);
            CircleImageView imgPoster = view.findViewById(R.id.img_favorite_movie);

            tvTitle.setText(getCollumnString(cursor, TITLE));
            tvOverview.setText(getCollumnString(cursor, OVERVIEW));
            Glide.with(view)
                    .load(BuildConfig.IMAGE_URL+getCollumnString(cursor, IMAGE_PATH))
                    .into(imgPoster);
        }
    }
}

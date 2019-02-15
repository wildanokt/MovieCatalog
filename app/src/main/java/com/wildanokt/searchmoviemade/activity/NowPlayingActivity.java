package com.wildanokt.searchmoviemade.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.wildanokt.searchmoviemade.itemclicksupport.MovieItemClickSupport;
import com.wildanokt.searchmoviemade.adapter.MovieNowPlayingAdapter;
import com.wildanokt.searchmoviemade.loader.NowPlayingAsyncTaskLoader;
import com.wildanokt.searchmoviemade.model.MovieItems;
import com.wildanokt.searchmoviemade.R;

import java.util.ArrayList;

public class NowPlayingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    ProgressBar pgNow;

    //recycler widget
    private RecyclerView rvMovie;
    private MovieNowPlayingAdapter rvAdapter;

    //key
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_IMAGE = "EXTRA_IMAGE";
    public static final String EXTRA_OVERVIEW = "EXTRA_OVERVIEW";

    //----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        pgNow = findViewById(R.id.pgNow);

        //init load
        getSupportLoaderManager().initLoader(0, null, this);

        //recycler
        rvAdapter = new MovieNowPlayingAdapter(this);
        rvAdapter.notifyDataSetChanged();

        rvMovie = findViewById(R.id.rv_movie_now_playing);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setAdapter(rvAdapter);

        MovieItemClickSupport.addTo(rvMovie).setOnItemClickListener(new MovieItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(rvAdapter.getMovieData().get(position));
            }
        });

        //bot navbar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_now);


        //action bar
        String actionBar = String.format(getResources().getString(R.string.action_bar_now));
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(actionBar);
        }
    }

    //----------------------------------------------------------
    //loader

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int i, @Nullable Bundle bundle) {
        pgNow.setVisibility(View.VISIBLE);
        return new NowPlayingAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> movieItems) {
        rvAdapter.setMovieData(movieItems);
        pgNow.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {
        rvAdapter.setMovieData(null);
    }


    //----------------------------------------------------------
    //open intent from item click

    private void showSelectedMovie(MovieItems items){
        Intent intent = new Intent(NowPlayingActivity.this, DetailMovieItem.class);
        intent.putExtra(EXTRA_TITLE, items.getTitle());
        intent.putExtra(EXTRA_OVERVIEW, items.getOverview());
        intent.putExtra(EXTRA_IMAGE, items.getPosterPath());
        startActivity(intent);
    }

    //----------------------------------------------------------
    //bottom navbar menu

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    Intent intent = new Intent(NowPlayingActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_now:
                    return true;
                case R.id.navigation_upcoming:
                    Intent intent2 = new Intent(NowPlayingActivity.this, UpcomingActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_favorite:
                    Intent intent3 = new Intent(NowPlayingActivity.this, FavoriteActivity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    //----------------------------------------------------------
}

package com.wildanokt.searchmoviemade.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.adapter.MovieFavoriteAdapter;
import com.wildanokt.searchmoviemade.db.MovieHelper;
import com.wildanokt.searchmoviemade.entity.Favorite;
import com.wildanokt.searchmoviemade.itemclicksupport.MovieItemClickSupport;
import com.wildanokt.searchmoviemade.model.MovieItems;

import java.util.ArrayList;

import static com.wildanokt.searchmoviemade.activity.DetailMovieFavoriteItem.REQUEST_ADD;
import static com.wildanokt.searchmoviemade.activity.DetailMovieFavoriteItem.REQUEST_UPDATE;
import static com.wildanokt.searchmoviemade.activity.DetailMovieFavoriteItem.RESULT_ADD;
import static com.wildanokt.searchmoviemade.activity.DetailMovieFavoriteItem.RESULT_DELETE;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.CONTENT_URI;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.TITLE;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView rvFavorite;

    private MovieFavoriteAdapter adapter;

    //provider
    private Cursor list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //action bar
        getSupportActionBar().setTitle(R.string.fav_title);
        //bot navbar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_favorite);
        //recycler
        rvFavorite = findViewById(R.id.rv_favorite);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.setHasFixedSize(true);

        adapter = new MovieFavoriteAdapter(this);
        adapter.setFavoriteData(list);
        rvFavorite.setAdapter(adapter);
        new LoadFavoriteDataAsync().execute();

        MovieItemClickSupport.addTo(rvFavorite).setOnItemClickListener(new MovieItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(FavoriteActivity.this, DetailMovieFavoriteItem.class);
                Uri uri = Uri.parse(CONTENT_URI+"/"+adapter.getItem(position).getId());
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_UPDATE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class LoadFavoriteDataAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //new
        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor favorites) {
            super.onPostExecute(favorites);
            list = favorites;
            adapter.setFavoriteData(list);
            adapter.notifyDataSetChanged();
            if (list.getCount() == 0) {
                Toast.makeText(FavoriteActivity.this, getString(R.string.empty_notif), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD){
            if (resultCode == RESULT_ADD){
                new LoadFavoriteDataAsync().execute();
            }
        }else if (requestCode == REQUEST_UPDATE){
            if (resultCode == RESULT_DELETE){
                new LoadFavoriteDataAsync().execute();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_now:
                    Intent intent2 = new Intent(FavoriteActivity.this, NowPlayingActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_upcoming:
                    Intent intent3 = new Intent(FavoriteActivity.this, UpcomingActivity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_favorite:
                    return true;
            }
            return false;
        }
    };
}

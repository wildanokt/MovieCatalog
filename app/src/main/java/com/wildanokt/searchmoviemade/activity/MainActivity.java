package com.wildanokt.searchmoviemade.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wildanokt.searchmoviemade.itemclicksupport.MovieItemClickSupport;
import com.wildanokt.searchmoviemade.adapter.MovieRecyclerAdapter;
import com.wildanokt.searchmoviemade.loader.MovieAsyncTaskLoader;
import com.wildanokt.searchmoviemade.model.MovieItems;
import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.notification.NotificationItem;
import com.wildanokt.searchmoviemade.notification.Receiver;
import com.wildanokt.searchmoviemade.notification.scheduler.SchedulerTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    //widget
    EditText etSearch;
    Button btnSearch;
    ProgressBar pgMain;

    //recycler widget
    private RecyclerView rvMovie;
    private MovieRecyclerAdapter rvAdapter;

    //key
    public static final String EXTRAS_MOVIE = "EXTRAS_MOVIE";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_IMAGE = "EXTRA_IMAGE";
    public static final String EXTRA_OVERVIEW = "EXTRA_OVERVIEW";

    //----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(onClickListener);
        pgMain = findViewById(R.id.pgMain);
        pgMain.setVisibility(View.GONE);

        //notif first time
        Receiver receiver = new Receiver();
        receiver.setContext(this);
        receiver.setRepeatingNotif(this, Receiver.TYPE_REPEATING, getResources().getString(R.string.Repeat_message));
        SchedulerTask task = new SchedulerTask(this);
        task.createPeriodicTask();

        //strings
        String searchForm = String.format(getResources().getString(R.string.search_hint));
        etSearch.setHint(searchForm);
        String searchButton = String.format(getResources().getString(R.string.search_button));
        btnSearch.setText(searchButton);

        //search
        String movieTitle = etSearch.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, movieTitle);
        getSupportLoaderManager().initLoader(0, bundle, this);

        //recycler
        rvAdapter = new MovieRecyclerAdapter(this);
        rvAdapter.notifyDataSetChanged();

        rvMovie = findViewById(R.id.rv_movie_list);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setAdapter(rvAdapter);

        //item click
        MovieItemClickSupport.addTo(rvMovie).setOnItemClickListener(new MovieItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(rvAdapter.getMovieData().get(position));
            }
        });

        //bot navbar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_search);
    }

    //----------------------------------------------------------
    //loader

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int i, @Nullable Bundle bundle) {
        String movie = "";
        if (bundle!=null){
            movie = bundle.getString(EXTRAS_MOVIE);
        }
        return new MovieAsyncTaskLoader(this, movie);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> movieItems) {
        rvAdapter.setMovieData(movieItems);
        pgMain.setVisibility(View.GONE);
    }
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {
        rvAdapter.setMovieData(null);
    }

    //----------------------------------------------------------
    //click search button

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String movieTitle = etSearch.getText().toString();
            if (TextUtils.isEmpty(movieTitle))return;

            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_MOVIE, movieTitle);
            pgMain.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(0,bundle,MainActivity.this);
        }
    };

    //----------------------------------------------------------
    //open intent from item click

    private void showSelectedMovie(MovieItems items){
        Intent intent = new Intent(MainActivity.this, DetailMovieItem.class);
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
                    return true;
                case R.id.navigation_now:
                    Intent intent = new Intent(MainActivity.this, NowPlayingActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_upcoming:
                    Intent intent2 = new Intent(MainActivity.this, UpcomingActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_favorite:
                    Intent intent3 = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    //----------------------------------------------------------
    //menu change language

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_change_language:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;
            case R.id.action_change_notif:
                Intent intent1 = new Intent(this, SettingActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

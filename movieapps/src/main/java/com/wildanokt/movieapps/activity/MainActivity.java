package com.wildanokt.movieapps.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.wildanokt.movieapps.R;
import com.wildanokt.movieapps.adapter.MovieAppsAdapter;

import static com.wildanokt.movieapps.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private MovieAppsAdapter adapter;
    ListView lvFavorite;

    private final int LOAD_FAVORITE_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("My Favorite Movie");

        lvFavorite = findViewById(R.id.lv_favorite);
        adapter = new MovieAppsAdapter(this, null, true);
        lvFavorite.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOAD_FAVORITE_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_FAVORITE_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAVORITE_ID);
    }
}

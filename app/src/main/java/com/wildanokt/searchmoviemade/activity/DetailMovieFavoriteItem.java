package com.wildanokt.searchmoviemade.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wildanokt.searchmoviemade.BuildConfig;
import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.db.MovieHelper;
import com.wildanokt.searchmoviemade.entity.Favorite;

import java.util.ArrayList;

import static com.wildanokt.searchmoviemade.db.DatabaseContract.CONTENT_URI;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.TITLE;

public class DetailMovieFavoriteItem extends AppCompatActivity implements View.OnClickListener {

    //widget
    TextView tvItemTitle;
    TextView tvItemOverview;
    ImageView ivItemPoster;
    Button btnFavorite;

    //db
    private Favorite favoriteItem;
    private MovieHelper helper;
    boolean isFavorite = false;

    private String title;
    private String overview;
    private String posterPath;

    //temp list from database
    ArrayList<Favorite> list = new ArrayList<>();

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;

    //----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie_favorite_item);

        //inisialisasi widget
        tvItemTitle = findViewById(R.id.item_fav_title);
        tvItemOverview = findViewById(R.id.item_fav_overview);
        ivItemPoster = findViewById(R.id.item_fav_image);

        //received data | provider
        Uri uri = getIntent().getData();
        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null,null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    favoriteItem = new Favorite(cursor);
                    title = favoriteItem.getTitle();
                    overview = favoriteItem.getOverview();
                    posterPath = favoriteItem.getImgPath();
                }
                cursor.close();
            }
        }

        //apply to widget
        tvItemTitle.setText(title);
        tvItemOverview.setText(overview);
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL +posterPath)
                .into(ivItemPoster);

        //action bar
        String actionBar = title;
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(actionBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //list check
        helper = new MovieHelper(this);
        helper.open();

        //favorite
        btnFavorite = findViewById(R.id.btn_fav_favorite);
        btnFavorite.setOnClickListener(this);
        new LoadFavoriteAsync().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper!=null){
            helper.close();
        }
    }

    @Override
    public void onClick(View v) {

        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(OVERVIEW, overview);
        values.put(IMAGE_PATH, posterPath);

        if (isFavorite == true) {
            //menghapus film dan menghilangkan status favorite
            for (Favorite item:list) {
                //cek dari database apakah ada data yang sama
                if (item.getTitle().equalsIgnoreCase(title)){
                    getContentResolver().delete(getIntent().getData(), null, null);
                    setResult(RESULT_DELETE);
                }
            }
            btnFavorite.setText(getString(R.string.favorite));
            isFavorite = false;
            Toast.makeText(this, title+" "+getString(R.string.remove), Toast.LENGTH_SHORT).show();
        } else {
            //memasukkan film baru dan menambah status favorite
            getContentResolver().insert(CONTENT_URI, values);
            setResult(RESULT_ADD);
            btnFavorite.setText(getString(R.string.unfavorite));
            isFavorite = true;
            Toast.makeText(this, title+" "+getString(R.string.add), Toast.LENGTH_SHORT).show();
        }
    }

    //get favorite data from database
    private class LoadFavoriteAsync extends AsyncTask<Void, Void, ArrayList<Favorite>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (list.size()>0){
                list.clear();
            }
        }
        @Override
        protected ArrayList<Favorite> doInBackground(Void... voids) {
            return helper.query();
        }

        @Override
        protected void onPostExecute(ArrayList<Favorite> favorites) {
            super.onPostExecute(favorites);
            list.addAll(favorites);
            for (Favorite item:list) {
                if (item.getTitle().equalsIgnoreCase(tvItemTitle.getText().toString())){
                    isFavorite = true;
                    btnFavorite.setText(getString(R.string.unfavorite));
                }
            }
        }
    }
}

package com.wildanokt.searchmoviemade.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.wildanokt.searchmoviemade.BuildConfig;
import com.wildanokt.searchmoviemade.model.MovieItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> mData;
    private Boolean mHasResult = false;

    private String movieTitle;

    public MovieAsyncTaskLoader(final Context context, String movieTitle) {
        super(context);
        onContentChanged();
        this.movieTitle = movieTitle;
    }

    //----------------------------------------------------------

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult){
            onReleaseResource(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Nullable
    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItems = new ArrayList<>();
        String url = BuildConfig.MOVIE_URL+ movieTitle;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i=0; i<list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems items = new MovieItems(movie);
                        movieItems.add(items);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieItems;
    }

    //----------------------------------------------------------

    protected void onReleaseResource(ArrayList<MovieItems> data){}
}

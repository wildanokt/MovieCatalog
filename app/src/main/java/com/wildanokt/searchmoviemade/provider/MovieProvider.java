package com.wildanokt.searchmoviemade.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildanokt.searchmoviemade.db.DatabaseContract;
import com.wildanokt.searchmoviemade.db.MovieHelper;

import static com.wildanokt.searchmoviemade.db.DatabaseContract.AUTHORITY;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.CONTENT_URI;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;;

    private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURI_MATCHER.addURI(AUTHORITY, DatabaseContract.TABLE_NAME, MOVIE);
        sURI_MATCHER.addURI(AUTHORITY, DatabaseContract.TABLE_NAME+"/#", MOVIE_ID);
    }

    private MovieHelper helper;

    @Override
    public boolean onCreate() {
        helper = new MovieHelper(getContext());
        helper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (sURI_MATCHER.match(uri)){
            case MOVIE:
                cursor = helper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = helper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor!= null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;

        switch (sURI_MATCHER.match(uri)){
            case MOVIE:
                added = helper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        if (added>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI+"/"+added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated ;
        switch (sURI_MATCHER.match(uri)) {
            case MOVIE_ID:
                updated =  helper.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sURI_MATCHER.match(uri)){
            case MOVIE_ID:
                deleted = helper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted= 0;
                break;
        }
        if (deleted>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }
}

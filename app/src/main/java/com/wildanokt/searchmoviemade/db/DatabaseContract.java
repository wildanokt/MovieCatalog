package com.wildanokt.searchmoviemade.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "table_favorite_movie";

    public static final class FavoriteCollums implements BaseColumns{
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String IMAGE_PATH = "image_path";
    }

    public static final String AUTHORITY = "com.wildanokt.searchmoviemade";
    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    public static String getCollumnString(Cursor cursor, String colName){
        return cursor.getString(cursor.getColumnIndex(colName));
    }

    public static int getCollumnInt(Cursor cursor, String colName){
        return cursor.getInt(cursor.getColumnIndex(colName));
    }

    public static long getCollumnLong(Cursor cursor, String colName){
        return cursor.getLong(cursor.getColumnIndex(colName));
    }
}

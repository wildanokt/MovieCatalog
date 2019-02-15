package com.wildanokt.searchmoviemade.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.IMAGE_PATH;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.OVERVIEW;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.FavoriteCollums.TITLE;
import static com.wildanokt.searchmoviemade.db.DatabaseContract.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbmovie";
    private static int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_FAVORITE =
            "CREATE TABLE "+TABLE_NAME+" ("
            + _ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TITLE + " TEXT NOT NULL,"
            + OVERVIEW + " TEXT NOT NULL,"
            + IMAGE_PATH + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
